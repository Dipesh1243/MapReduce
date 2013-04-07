package cmu.ds.mr.mapred;

import TaskRunner;
import TaskTracker;
import TaskUmbilicalProtocol;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.conf.JobConf;
import cmu.ds.mr.io.FileSplit;

public abstract class Task {
  private static final Log LOG = LogFactory.getLog(Task.class);
  
  private String outputpath;
  
  protected JobConf taskConf;
  //protected JobID jobid;
  // task status include JobId, TaskId (communicate using taskStatus)
  protected TaskStatus taskStatus;  
  //protected int taskID; //id within a job, also means the index of the splited file
  
  public Task(String outputpath, JobConf conf, JobID jobid, TaskStatus taskStatus, int taskID) {
    super();
    this.outputpath = outputpath;
    this.taskConf = conf;
    this.taskStatus = taskStatus;
  }
  
  /** Run this task as a part of the named job.  This method is executed in the
   * child process and is what invokes user-supplied map, reduce, etc. methods.
   * @param umbilical for progress reports
   */
  public abstract void run(JobConf job, TaskUmbilicalProtocol umbilical)
    throws IOException, ClassNotFoundException, InterruptedException;


  /** Return an approprate thread runner for this task. 
   * @param tip TODO*/
  public abstract TaskRunner createRunner(TaskTracker tracker, 
      TaskTracker.TaskInProgress tip) throws IOException;

  public String getOutputpath() {
    return outputpath;
  }

  public void setOutputpath(String outputpath) {
    this.outputpath = outputpath;
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

  
}
