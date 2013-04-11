package cmu.ds.mr.mapred;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import cmu.ds.mr.conf.JobConf;
import cmu.ds.mr.mapred.TaskStatus.TaskState;
import cmu.ds.mr.mapred.TaskStatus.TaskType;
import cmu.ds.mr.util.Log;
import cmu.ds.mr.util.Util;


public class TaskTracker implements TaskUmbilicalProtocol {
  
  public static final Log LOG =
          new Log("TaskTracker.class");
  
//  private class TaskLauncher extends Thread {
//    private List<Task> tasksQueue;
//
//    public TaskLauncher() {
//      tasksQueue = new LinkedList<Task>();
//      
//      setDaemon(true);
//      setName("TaskLauncher for task");
//    }
//    
//    public int getNumFreeSlots() {
//      return numFreeSlots.get();
//    }
//
//    public void addToTaskQueue(Task task) {
//      synchronized (tasksQueue) {
//        tasksQueue.add(task);
//        tasksQueue.notifyAll();
//      }
//    }
//    
//    public void cleanTaskQueue() {
//      tasksQueue.clear();
//    }
//    
//    public void addFreeSlot() {
//      numFreeSlots.incrementAndGet();
//      synchronized (numFreeSlots) {
//        numSlots++;
//        
//        LOG.info("addFreeSlot : current free slots : " + numSlots);
//        numSlots.notifyAll();
//      }
//    }
//    
//    public void run() {
//      //while (!Thread.interrupted()) {
//      while (true) {
//        try {
//          Task task;
//          synchronized (tasksQueue) {
//            while (tasksQueue.isEmpty()) {
//              tasksQueue.wait();
//            }
//            // removeFirst
//            task = tasksQueue.remove(0);
//            LOG.info("Launching : " + task.taskStatus.getTaskId());
//          }
//          //wait for a slot to run
//          synchronized (numSlots) {
//            while (numSlots == 0) {
//              numSlots.wait();
//            }
//            LOG.info("In TaskLauncher, current free slots : " + numSlots +
//                " and trying to launch "+ task.taskStatus.getTaskId());
//            numSlots--;
//            assert numSlots >= 0;
//          }
//          // check for valid tasks
//          synchronized (task) {
//            if (task.taskStatus.getState() == TaskStatus.TaskState.FAILED &&
//                    task.taskStatus.getState() == TaskStatus.TaskState.KILLED) {
//              addFreeSlot();
//              continue;
//            }
//          }
//          
//          // launch the task when we have free slot
//          TaskRunner runner = task.createRunner(TaskTracker.this, task);
//          runner.start();
//        } 
//        // task tracker finished
//        catch (InterruptedException e) { 
//          return; 
//        } 
//        catch (Throwable th) {
//          LOG.error("TaskLauncher error " + Util.stringifyException(th));
//        }
//      }
//    }
//  }
  
  // running task table
  private String taskTrackerName; // taskTrackerName assigned by jobtracker to uniquely identify a taskTracker
  private Map<TaskID, Task> taskMap;  // running tasks in taskTracker
  private Map<TaskID, Task> taskDoneMap;  // finisehed task map 
  private String localRootDir;  // local map output root dir
  private String jobTrackerAddrStr; // job tracker address
  
  private AtomicInteger numFreeSlots;
  private AtomicInteger numMaxSlots;
  
  // JobTracker stub (using RMI)
  private InterTrackerProtocol jobTrackerProxy; 
  // Map and reduce launcher (separate daemon process)
//  private TaskLauncher mapLauncher;
//  private TaskLauncher redLauncher;
  
  
  public TaskTracker(JobConf conf, String jobTrackerAddrStr) throws NotBoundException, UnknownHostException {
    try {
      taskTrackerName = InetAddress.getLocalHost().getCanonicalHostName();
      taskMap = new HashMap<TaskID, Task>();
      taskDoneMap = new HashMap<TaskID, Task>();
      
      LOG.info("create TaskTracker");
      this.jobTrackerAddrStr = jobTrackerAddrStr;
      Registry registry = LocateRegistry.getRegistry(jobTrackerAddrStr);
      jobTrackerProxy = (InterTrackerProtocol) registry.lookup(Util.SERVICE_NAME_INTERTRACKER);
      // TODO get a taskTracker name from jobTracker
      
      localRootDir = (String) conf.getProperties().get(Util.LOCAL_ROOT_DIR);
      
      numFreeSlots = new AtomicInteger();
      numMaxSlots = new AtomicInteger();
      numFreeSlots.set(Integer.parseInt((String)conf.getProperties().get(Util.NUM_TASK_MAX)));
      numMaxSlots.set(numFreeSlots.get());
    }
    catch (RemoteException re) {
      LOG.error("JobTracker hasn't been established!");
      System.exit(Util.EXIT_JT_NOTSTART);
    }
  }
 
  @Override
  public boolean fail(TaskID taskId) throws IOException,
          InterruptedException {
    Task ts = taskMap.get(taskId);
    ts.taskStatus.setState(TaskState.FAILED);
    // put into finished task map
    taskDoneMap.put(taskId, ts);
    
    numFreeSlots.incrementAndGet();
    return false;
  }

  @Override
  public void done(TaskID taskid) throws IOException {
    //taskMap.remove(taskid);
    // notify JobTracker
    Task ts = taskMap.get(taskid);
    ts.taskStatus.setState(TaskState.SUCCEEDED);
    // put into finished task map
    taskDoneMap.put(taskid, ts);
    
    numFreeSlots.incrementAndGet();
  }
  
  private void startTaskTracker() throws IOException {
    // TODO get run or stop instruction from JobTracker
	  
	taskTrackerName = InetAddress.getLocalHost().getCanonicalHostName() + "-" + Integer.toString(jobTrackerProxy.getNewTaskTrackerId());
	LOG.info("Tasktracker name: " + taskTrackerName);
    LOG.info("Starting taskTracker...");
    
    try{
      while(true) {
        Thread.sleep(Util.TIME_INTERVAL_HEARTBEAT);
        
        // build current task tracker status
        List<TaskStatus> taskStatusList = getAllTaskStatus();
        TaskTrackerStatus tts = new TaskTrackerStatus(taskTrackerName, taskStatusList, numFreeSlots.get());
        
        //LOG.debug(String.format("#freeSlot:%d", numFreeSlots.get()));
        
        // transmit heartbeat
        LOG.debug("TaskTracker: start heartbeat");
        Task retTask = jobTrackerProxy.heartbeat(tts);
        LOG.debug("TaskTracker: recv heartbeat");
        
        // retTask == null means JobTracker has no available task to assign
        if(retTask != null) {
        	
        	
          //TODO: need to check this work or not!!!	
          //if trynum = -1, then this is a intialize response, clean up the tasklist;
          if(retTask.getTaskStatus().getTryNum() == -1){
        	  taskMap = new HashMap<TaskID, Task>();
              taskDoneMap = new HashMap<TaskID, Task>();
        	  continue;
          }
          
          LOG.info("get new task id: " + retTask.taskId.toString());
          // put it in the taskTracker's table
          taskMap.put(retTask.taskId, retTask);
          
          // launch the task when we have free slot
          if(numFreeSlots.get() > 0) {
            numFreeSlots.decrementAndGet();
            
            TaskRunner runner = retTask.createRunner(TaskTracker.this, retTask);
            runner.start();
          }
          else
            assert numFreeSlots.get() > 0 : String.format("numFreeSlots:%d", numFreeSlots.get());
  //        if(retTask.taskStatus.getType() == TaskType.MAP)
  //          mapLauncher.addToTaskQueue(retTask);
  //        else
  //          redLauncher.addToTaskQueue(retTask);
        }
      } 
    }
    catch (RemoteException re) {
      LOG.error("Remote exception! JobTracker not started or down!");
      System.exit(Util.EXIT_JT_DOWN);
    }
    catch (InterruptedException ie){
      LOG.error("TaskTracker down!");
      System.exit(Util.EXIT_JT_DOWN);
    }
  }
  
  public List<TaskStatus> getAllTaskStatus() {
    List<TaskStatus> res = new ArrayList<TaskStatus>();
    for(Entry<TaskID, Task> en : taskMap.entrySet()) {
      res.add(en.getValue().getTaskStatus());
    }
    
    // delete finished task once it has used after heartbeat
    for(Entry<TaskID, Task> en : taskDoneMap.entrySet()) {
      if(taskMap.containsKey(en.getKey())) {
        taskMap.remove(en.getKey());
        taskDoneMap.remove(en.getKey());
      }
    }
    return res;
  }
  
  public static void main(String[] args) throws FileNotFoundException, IOException, NotBoundException, InterruptedException {
    if(args.length != 1) {
      LOG.error("Usage: TaskTracker <JobTrackerAddress>");
      return;
    }
    // read configure file
    LOG.setInfo(true);
    LOG.setDebug(false);
    JobConf conf = new JobConf();
    LOG.info("prepare to create TaskTracker");
    TaskTracker tt = new TaskTracker(conf, args[0]);
    tt.startTaskTracker();
  }

}
