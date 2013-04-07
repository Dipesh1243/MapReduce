package cmu.ds.mr.mapred;

import Log;
import TaskTracker;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class TaskTracker implements TaskUmbilicalProtocol {
  
  public static final Log LOG =
          LogFactory.getLog(TaskTracker.class);
  
  private class TaskLauncher extends Thread {
    private int numSlots;
    private final int maxNumSlots;
    private List<TaskInProgress> tasksQueue;

    public TaskLauncher(int numSlots) {
      this.numSlots = numSlots;
      maxNumSlots = numSlots;
      tasksQueue = new LinkedList<TaskInProgress>();
      //setDaemon(true);
      setName("TaskLauncher for task");
    }

    public void addToTaskQueue(LaunchTaskAction action) {
      synchronized (tasksQueue) {
        TaskInProgress tip = registerTask(action, this);
        tasksQueue.add(tip);
        tasksQueue.notifyAll();
      }
    }
    
    public void cleanTaskQueue() {
      tasksQueue.clear();
    }
    
    public void addFreeSlot() {
      synchronized (numSlots) {
        numSlots.set(numSlots.get() + 1);
        assert (numSlots.get() <= maxNumSlots);
        LOG.info("addFreeSlot : current free slots : " + numSlots.get());
        numSlots.notifyAll();
      }
    }
    
    public void run() {
      //while (!Thread.interrupted()) {
      while (true) {
        try {
          TaskInProgress tip;
          synchronized (tasksQueue) {
            while (tasksQueue.isEmpty()) {
              tasksQueue.wait();
            }
            //get the TIP
            tip = tasksQueue.remove(0);
            LOG.info("Trying to launch : " + tip.getTask().getTaskID());
          }
          //wait for a slot to run
          synchronized (numSlots) {
            while (numSlots.get() == 0) {
              numSlots.wait();
            }
            LOG.info("In TaskLauncher, current free slots : " + numSlots.get()+
                " and trying to launch "+tip.getTask().getTaskID());
            numSlots.set(numSlots.get() - 1);
            assert (numSlots.get() >= 0);
          }
          synchronized (tip) {
            //to make sure that there is no kill task action for this
            if (tip.getRunState() != TaskStatus.State.UNASSIGNED &&
                tip.getRunState() != TaskStatus.State.FAILED_UNCLEAN &&
                tip.getRunState() != TaskStatus.State.KILLED_UNCLEAN) {
              //got killed externally while still in the launcher queue
              addFreeSlot();
              continue;
            }
            tip.slotTaken = true;
          }
          //got a free slot. launch the task
          startNewTask(tip);
        } 
        // task tracker finished
        catch (InterruptedException e) { 
          return; 
        } 
        catch (Throwable th) {
          LOG.error("TaskLauncher error " + StringUtils.stringifyException(th));
        }
      }
    }
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

}
