package cmu.ds.mr.mapred;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.conf.JobConf;
import cmu.ds.mr.util.Util;


public class TaskTracker implements TaskUmbilicalProtocol {
  
  public static final Log LOG =
          LogFactory.getLog(TaskTracker.class);
  
  private class TaskLauncher extends Thread {
    private Integer numSlots; // num of free slots
    private final int maxNumSlots;
    private List<Task> tasksQueue;

    public TaskLauncher(int numSlots) {
      this.numSlots = numSlots;
      maxNumSlots = numSlots;
      tasksQueue = new LinkedList<Task>();
      
      setDaemon(true);
      setName("TaskLauncher for task");
    }

    public void addToTaskQueue(Task task) {
      synchronized (tasksQueue) {
        //TaskInProgress tip = registerTask(action, this);
        tasksQueue.add(task);
        tasksQueue.notifyAll();
      }
    }
    
    public void cleanTaskQueue() {
      tasksQueue.clear();
    }
    
    public void addFreeSlot() {
      synchronized (numSlots) {
        numSlots++;
        
        LOG.info("addFreeSlot : current free slots : " + numSlots);
        numSlots.notifyAll();
      }
    }
    
    public void run() {
      //while (!Thread.interrupted()) {
      while (true) {
        try {
          Task task;
          synchronized (tasksQueue) {
            while (tasksQueue.isEmpty()) {
              tasksQueue.wait();
            }
            // removeFirst
            task = tasksQueue.remove(0);
            LOG.info("Launching : " + task.taskStatus.getTaskId());
          }
          //wait for a slot to run
          synchronized (numSlots) {
            while (numSlots == 0) {
              numSlots.wait();
            }
            LOG.info("In TaskLauncher, current free slots : " + numSlots +
                " and trying to launch "+ task.taskStatus.getTaskId());
            numSlots--;
            assert numSlots >= 0;
          }
          // check for valid tasks
          synchronized (task) {
            if (task.taskStatus.getState() == TaskStatus.TaskState.FAILED &&
                    task.taskStatus.getState() == TaskStatus.TaskState.KILLED) {
              addFreeSlot();
              continue;
            }
          }
          //got a free slot. launch the task
          startNewTask(task);
        } 
        // task tracker finished
        catch (InterruptedException e) { 
          return; 
        } 
        catch (Throwable th) {
          LOG.error("TaskLauncher error " + Util.stringifyException(th));
        }
      }
    }
  }
  
  // running task table
  private Map<TaskID, Task> tasksMap;
  private int maxTaskMax;
  private int redTaskMax;
  private int slotTotal;
  private int numSlots;
  private String localRootDir;  // local map output root dir
  private String jobTrackerAddrStr; // job tracker address
  
  // JobTracker stub (using RMI)
  private InterTrackerProtocol jobTrackerProxy; 
  // Map and reduce launcher (separate daemon process)
  private TaskLauncher mapLauncher;
  private TaskLauncher redLauncher;
  
  
  public TaskTracker(JobConf conf, String jobTrackerAddrStr) throws RemoteException, NotBoundException {
    this.jobTrackerAddrStr = jobTrackerAddrStr;
    Registry registry = LocateRegistry.getRegistry(jobTrackerAddrStr);
    jobTrackerProxy = (InterTrackerProtocol) registry.lookup(Util.SERVICE_NAME_INTERTRACKER);
    
    localRootDir = (String) conf.getProperties().get(Util.LOCAL_ROOT_DIR);
    
    maxTaskMax = (Integer) conf.getProperties().get(Util.MAP_TASK_MAX);
    redTaskMax = (Integer) conf.getProperties().get(Util.RED_TASK_MAX);
    
    mapLauncher = new TaskLauncher(maxTaskMax);
    redLauncher = new TaskLauncher(redTaskMax);
    mapLauncher.start();
    redLauncher.start();
  }
 
  @Override
  public boolean statusUpdate(TaskID taskId, TaskStatus taskStatus) throws IOException,
          InterruptedException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean ping(TaskID taskid) throws IOException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void done(TaskID taskid) throws IOException {
    // TODO Auto-generated method stub
    
  }
  
  private void startTaskTracker() throws InterruptedException {
    // TODO get run or stop instruction from JobTracker
    while(true) {
      Thread.sleep(Util.TIME_INTERVAL_HEARTBEAT);
      
      
    }
    
  }
  
  public static void main(String[] args) throws FileNotFoundException, IOException, NotBoundException {
    if(args.length != 1) {
      LOG.error("Usage: TaskTracker <JobTrackerAddress>");
      return;
    }
    // read configure file
    JobConf conf = new JobConf();
    TaskTracker tt = new TaskTracker(conf, args[1]);
    tt.startTaskTracker();
  }

}
