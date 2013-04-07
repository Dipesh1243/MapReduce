package cmu.ds.mr.mapred;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.conf.JobConf;
import cmu.ds.mr.io.FileSplit;

public class Task {
  private static final Log LOG = LogFactory.getLog(Task.class);
  
  public static enum TaskState {
    SUCCEEDED, WAITING, DEFINE, RUNNING, READY, FAILED, KILLED
  };

  public static enum TaskType {
    MAP, REDUCE
  };
  
  private String outputpath;
  
  protected JobConf conf;
  protected JobID jobid;
  protected TaskState state;
  protected int taskID; //id within a job, also means the index of the splited file
  protected TaskType type;
  
  public Task(JobID jobid, JobConf jobconf, int taskid, TaskType type){
    this.conf = jobconf;
    this.jobid = jobid;
    this.state = TaskState.DEFINE;
    this.taskID = taskid;
    this.type = type;
  }

  public String getOutputpath() {
    return outputpath;
  }

  public void setOutputpath(String outputpath) {
    this.outputpath = outputpath;
  }

  public JobConf getConf() {
    return conf;
  }

  public void setConf(JobConf conf) {
    this.conf = conf;
  }

  public JobID getJobid() {
    return jobid;
  }

  public void setJobid(JobID jobid) {
    this.jobid = jobid;
  }

  public TaskState getState() {
    return state;
  }

  public void setState(TaskState state) {
    this.state = state;
  }

  public int getTaskID() {
    return taskID;
  }

  public void setTaskID(int taskID) {
    this.taskID = taskID;
  }
  
  
}
