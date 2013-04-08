package cmu.ds.mr.mapred;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import cmu.ds.mr.conf.JobConf;

/**
 * Class for running mappaer and reducer in a separate thread
 * 
 * */
public class TaskRunner extends Thread {
  
  public static final Log LOG = LogFactory.getLog(TaskRunner.class);
  
  private Task task;
  private JobConf taskConf;
  private TaskTracker taskTrackerProxy;
  
  
  public TaskRunner(Task task, JobConf taskConf, TaskTracker taskTrackerProxy) {
    super();
    this.task = task;
    this.taskConf = taskConf;
    this.taskTrackerProxy = taskTrackerProxy;
  }

  @Override
  public void run() {
    try {
      task.startTask(taskConf, taskTrackerProxy);
    } catch (IOException e) {
      LOG.error("IOException: " + e);
    } catch (ClassNotFoundException e) {
      LOG.error("ClassNotFoundException: " + e);
    } catch (InterruptedException e) {
      LOG.info("Class being killed: " + e);
    } catch (RuntimeException e) {
      LOG.error("RuntimeException: " + e);
    } catch (InstantiationException e) {
      LOG.error("ClassNotFoundException: " + e);
    } catch (IllegalAccessException e) {
      LOG.error("IllegalAccessException: " + e);
    } catch (InvocationTargetException e) {
      LOG.error("InvocationTargetException: " + e);
    } catch (NoSuchMethodException e) {
      LOG.error("NoSuchMethodException: " + e);
    }
  }

}
