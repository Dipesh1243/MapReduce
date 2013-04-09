package cmu.ds.mr.mapred;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.conf.JobConf;

public abstract class Task {
  private static final Log LOG = LogFactory.getLog(Task.class);

  // JobID is in taskId, use task.getJobid() to get JobId
  // JobID can only be set by new TaskID
  protected TaskID taskId;

  protected JobConf taskConf; // task input and output path is defined in the taskConf

  // taskStatus include state, type and TaskId (a copy of Task.taskId, since we do communicate using
  // taskStatus)
  protected TaskStatus taskStatus;

  // protected int taskID; //id within a job, also means the index of the splited file

  public Task(TaskID taskId, JobConf taskConf, TaskStatus taskStatus) {
    super();
    this.taskId = taskId;
    this.taskConf = taskConf;
    this.taskStatus = taskStatus;
  }

  /**
   * Run this task as a part of the named job. This method is executed in the child process and is
   * what invokes user-supplied map, reduce, etc. methods.
   * 
   * @param taskTrackerProxy
   *          for progress reports
   */
  public abstract void startTask(JobConf taskConf, TaskUmbilicalProtocol taskTrackerProxy)
          throws IOException, ClassNotFoundException, InterruptedException, RuntimeException,
          InstantiationException, IllegalAccessException, InvocationTargetException,
          NoSuchMethodException;

  /**
   * Get a runner (in a different thread) to run the task
   **/
  public TaskRunner createRunner(TaskTracker tracker, Task task) throws IOException {
    return new TaskRunner(task, this.taskConf, tracker);
  }

  public JobConf getConf() {
    return taskConf;
  }

  public void setConf(JobConf conf) {
    this.taskConf = conf;
  }

  public TaskStatus getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(TaskStatus taskStatus) {
    this.taskStatus = taskStatus;
  }

  public JobID getJobid() {
    return taskId.getJobId();
  }

}
