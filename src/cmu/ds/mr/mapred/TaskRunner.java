package cmu.ds.mr.mapred;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import cmu.ds.mr.conf.JobConf;
import cmu.ds.mr.util.Log;
import cmu.ds.mr.util.Util;

/**
 * Class for running mappaer and reducer in a separate thread
 * 
 * */
public class TaskRunner extends Thread {
  
  public static final Log LOG = new Log("TaskRunner.class");
  
  private Task task;
  private TaskTracker taskTrackerProxy;
  
  
  public TaskRunner(Task task, TaskTracker taskTrackerProxy) {
    super();
    this.task = task;
    this.taskTrackerProxy = taskTrackerProxy;
  }

  @Override
  public void run() {
    try {
      task.startTask(task, taskTrackerProxy);
    } catch (Exception e) {
      try {
        taskTrackerProxy.fail(task.getTaskStatus().getTaskId());
      } catch (IOException e1) {
        LOG.error("Task fails. IOException: " + e);
        System.exit(Util.EXIT_TASK_FAIL);
      } catch (InterruptedException e1) {
        LOG.error("Task fails. InterruptedException: " + e);
        System.exit(Util.EXIT_TASK_FAIL);
      }
      LOG.error("Task fails. Exception: " + e);
      System.exit(Util.EXIT_TASK_FAIL);
    } 
  }

}
