package cmu.ds.mr.mapred;


import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import cmu.ds.mr.conf.JobConf;
import cmu.ds.mr.mapred.JobStatus.JobState;

/**
 * Job class in the submitter's view. Can
 * 1. Configure job
 * 2. Kill job 
 * 3. Query progress or status
 * 
 * */
public class Job {

  private static final Log LOG = LogFactory.getLog(Job.class);

  private JobID jid;
  private String message;
  private String jobName;
  private JobConf jobConf;
  private JobStatus jobStatus;
  
  private JobClient jobClient;
  private RunningJob infoJob; // job handle in job tracker
  
  /**
   * Submit the job to the cluster and return immediately.
   * @throws IOException
   */
  public void submit() throws IOException, InterruptedException, 
                              ClassNotFoundException {
    infoJob = jobClient.submitJob(jobConf);
    jobStatus.setState(JobStatus.JobState.RUNNING);
   }
  


  public synchronized void setState() {
    
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public JobConf getJobConf() {
    return jobConf;
  }

  public void setJobConf(JobConf jobConf) {
    this.jobConf = jobConf;
  }

  public void setJobState(JobStatus jobState) {
    this.jobStatus = jobState;
  }

  @Override
  public JobID getID() {
    ensureState(JobState.RUNNING);
    return info.getID();
  }

  @Override
  public String getJobName() {
    return jobname;
  }

  @Override
  public float mapProgress() throws IOException {
    ensureState(JobState.RUNNING);
    return info.mapProgress();
  }

  @Override
  public float reduceProgress() throws IOException {
    ensureState(JobState.RUNNING);
    return infoJob.reduceProgress();
  }

  @Override
  public boolean isComplete() throws IOException {
    ensureState(JobState.RUNNING);
    return info.isComplete();
  }

  @Override
  public boolean isSuccessful() throws IOException {
    ensureState(JobStatus.JobState.RUNNING);
    return infoJob.isSuccessful();
  }
  
  @Override
  public void killJob() throws IOException {
    ensureState(JobStatus.JobState.RUNNING);
    info.killJob();
  }

  @Override
  public void waitForCompletion() throws IOException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public int getJobState() throws IOException {
    // TODO Auto-generated method stub
    return 0;
  }
  
  private void ensureState(JobState state) throws IllegalStateException {
    if (state != jobStatus.getState()) {
      throw new IllegalStateException("Job in state "+ jobStatus.getState() + 
                                      " instead of " + state);
    }
  }
  
  


}
