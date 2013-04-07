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

  
  private String outputpath;
  
  protected JobConf conf;
  protected JobID jobid;
  protected TaskState state;
  protected int taskID; //id within a job, also means the index of the splited file
  
  public Task(JobID jobid, JobConf jobconf, int taskid){
    this.conf = jobconf;
    this.jobid = jobid;
    this.state = TaskState.DEFINE;
    this.taskID = taskid;
  }
  
  
}
