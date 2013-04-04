package cmu.ds.mr.mapred;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.conf.JobConf;

public class Job {

  private static final Log LOG = LogFactory.getLog(Job.class);

  private int JobID;

  private String message;

  private JobConf jobconf;

  private JobState state;
  
  private float status;
  
  public JobConf getJobconf() {
    return jobconf;
  }

  public void setJobconf(JobConf jobconf) {
    this.jobconf = jobconf;
  }

  public int getJobID() {
    return JobID;
  }

  public void setJobID(int jobID) {
    JobID = jobID;
  }

  public synchronized void setState() {
    
  }
  
  /**
  * @return the state of this job
  */
  public synchronized JobState getState() {
    return state;
  }
  


}
