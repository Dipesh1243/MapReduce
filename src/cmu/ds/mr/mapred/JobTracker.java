package cmu.ds.mr.mapred;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.conf.JobConf;
import cmu.ds.mr.mapred.JobStatus.JobState;

public class JobTracker implements JobSubmissionProtocol{
  private static final Log LOG = LogFactory.getLog(JobTracker.class);
  
//  public static enum State { INITIALIZING, RUNNING }
//  State state = State.INITIALIZING;

  private Queue<JobInProgress> jobQueue = new LinkedList<JobInProgress>();
  private Queue<Task> taskQueue;
  private Map<JobID, JobInProgress> jobTable = new TreeMap<JobID, JobInProgress>();
  private Map<TaskTracker, Boolean> tasktrackers;
  
  private String jobIdentifier;  
  private JobScheduler jobscheduler;
//  private final TaskScheduler taskScheduler = new TaskScheduler();
  
  private int nextID = 1;
  int totalSubmissions = 0;
  
  public JobState submitJob(int JobID){
    if(jobQueue.contains(JobID)){
      return null;
    }
    return null;
  }


  @Override
  public JobID getNewJobId() throws IOException {
    return new JobID(jobIdentifier, nextID++);
  }


  @Override
  public synchronized JobStatus submitJob(JobID jobid, JobConf jobConf) throws IOException {
    
    //check if job already running, don't start twice
    if(jobTable.containsKey(jobid)){
      return jobTable.get(jobid).getStatus();
    }
    JobInProgress job = new JobInProgress(jobid, this, jobConf);
    
    //TODO: need to check Queue later
    if(!jobQueue.offer(job)){
      LOG.info("submitJob: Cannot enqueue the job");
      return null;
    }
   
    return addJob(jobid, job);
    
  }
  
  
  private synchronized JobStatus addJob(JobID jobId, JobInProgress job) {
    totalSubmissions++;

    synchronized (jobTable) {
        jobTable.put(jobId, job);
    }
    return job.getStatus();
  }


  @Override
  public void killJob(JobID jobid) throws IOException {
    // TODO Auto-generated method stub
    if (null == jobid) {
      LOG.info("Null jobid object sent to JobTracker.killJob()");
      return;
    }
    
    JobInProgress job = jobTable.get(jobid);
    
    if (null == job) {
      LOG.info("killJob(): JobId " + jobid.toString() + " is not a valid job");
      return;
    }
    
    job.kill();
  }


  @Override
  public JobStatus getJobStatus(JobID jobid) throws IOException {
    if (null == jobid) {
      LOG.warn("JobTracker.getJobStatus() cannot get status for null jobid");
      return null;
    }
    synchronized (this) {
      JobInProgress job = jobTable.get(jobid);
      if (job == null) {
        LOG.warn("JobTracker.getJobStatus() cannot get job from the given jobid");
      } 
      return job.getStatus();
    }
  }

  private synchronized JobStatus[] getJobStatus(Collection<JobInProgress> jips,
          boolean toComplete) {
        if(jips == null || jips.isEmpty()) {
          return new JobStatus[]{};
        }
        ArrayList<JobStatus> jobStatusList = new ArrayList<JobStatus>();
        for(JobInProgress jip : jips) {
          JobStatus status = jip.getStatus();
          status.setStartTime(jip.getStartTime());
          if(toComplete) {
            if(status.getState() == JobState.RUNNING)
              jobStatusList.add(status);
          }
          else{
            jobStatusList.add(status);
          }
        }
        return (JobStatus[]) jobStatusList.toArray(
            new JobStatus[jobStatusList.size()]);
      }

  @Override
  public JobStatus[] jobsToComplete() throws IOException {
    return getJobStatus(jobTable.values(), true);
  }


  @Override
  public JobStatus[] getAllJobs() throws IOException {
    return getJobStatus(jobTable.values(),false);
  }


  @Override
  public String getSystemDir() {
    // TODO Auto-generated method stub
    return null;
  }
  
  
  
  
}
