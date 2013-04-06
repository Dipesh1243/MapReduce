package cmu.ds.mr.mapred;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.mapred.JobStatus.JobState;

public class JobTracker implements JobSubmissionProtocol{
  private static final Log LOG = LogFactory.getLog(JobTracker.class);
  
//  public static enum State { INITIALIZING, RUNNING }
//  State state = State.INITIALIZING;
  private Queue<Job> jobQueue;
  private Set<Integer> jobs;
  private Queue<Task> taskQueue;
  private Map<Integer, Job> JobTable;
  private int nextID;
  
  public JobState submitJob(int JobID){
    if(jobQueue.contains(JobID)){
      return null;
    }
    return null;
  }
  
  
  public int getNewJobID(){
    return nextID++;
  }


  @Override
  public JobID getNewJobId() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public JobStatus submitJob(JobID jobName) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public void killJob(JobID jobid) throws IOException {
    // TODO Auto-generated method stub
    
  }


  @Override
  public JobStatus getJobStatus(JobID jobid) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public JobStatus[] jobsToComplete() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public JobStatus[] getAllJobs() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public String getSystemDir() {
    // TODO Auto-generated method stub
    return null;
  }
  
  
  
  
}
