package cmu.ds.mr.mapred;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class JobScheduler implements Runnable{
  
  private Queue<JobInProgress> jobQueue;
  private Queue<Task> taskQueue;
  private Map<JobID, JobInProgress> jobTable;
  
  public JobScheduler(Queue<JobInProgress> jobQueue, Queue<Task> taskQueue, Map<JobID, JobInProgress> jobTable){
    this.jobQueue = jobQueue;
    this.taskQueue = taskQueue;
    this.jobTable = jobTable;
  }

  @Override
  public void run() {
    if(taskQueue.size() < 1){
      JobInProgress jip = Job
    }
    
  }
  
  
  
}
