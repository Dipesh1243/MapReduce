package cmu.ds.mr.mapred;

import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JobScheduler implements Runnable{
  
  private static final Log LOG = LogFactory.getLog(JobScheduler.class);
  
  
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
      JobInProgress jip = jobQueue.poll();

      if(jip == null){
        LOG.info("run(): no more job in jobQueue");
        return;
      }
      
      for(int i = 1; i <= jip.getJobconf().getNumMapTasks(); ++i){
        
      }

    }    
    
    
    
  }
  
  
  
}
