package cmu.ds.mr.mapred;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.conf.JobConf;
import cmu.ds.mr.mapred.TaskStatus.TaskType;

class TaskScheduler {
  public static final Log LOG = LogFactory.getLog(TaskScheduler.class);
  
  private Queue<JobInProgress> jobQueue;
  private Map<JobID, JobInProgress> jobTable;
  private Queue<MapTask> maptaskQueue;
  private Queue<ReduceTask> reducetaskQueue;  
  
  public TaskScheduler(Queue<JobInProgress> jobQueue, Map<JobID, JobInProgress> jobTable){
    this.jobQueue = jobQueue;
    this.jobTable = jobTable;
  }
  /**
   * Lifecycle method to allow the scheduler to start any work in separate
   * threads.
   * @throws IOException
   */
  public void start() throws IOException {
    // do nothing
  }
  
  /**
   * Lifecycle method to allow the scheduler to stop any work it is doing.
   * @throws IOException
   */
  public void terminate() throws IOException {
    // do nothing
  }

  
  private synchronized boolean addTasks(){
    
    if(jobQueue.isEmpty()){
      LOG.info("addTasks(): no more jobs in job queue");
      return false;
    }
    JobInProgress jip = jobQueue.poll();
    for(int i = 1; i <= jip.getJobconf().getNumMapTasks(); ++i){
      maptaskQueue.offer(new MapTask(jip.getJobid(), jip.getJobconf(), new TaskStatus(i, TaskType.MAP)));
    }
    
    for(int i = 1; i <= jip.getJobconf().getNumReduceTasks(); ++i){
      reducetaskQueue.offer(new ReduceTask(jip.getJobid(), jip.getJobconf(), new TaskStatus(i, TaskType.REDUCE)));
    }
    return true;
  }
  
  
  /**
   * Returns the tasks we'd like the TaskTracker to execute right now.
   * 
   * @param taskTracker The TaskTracker for which we're looking for tasks.
   * @return A list of tasks to run on that TaskTracker, possibly empty.
   */
  public synchronized Task assignTask(TaskStatus.TaskType type){

        if(type == TaskStatus.TaskType.MAP){
          if(reducetaskQueue.isEmpty()){
            if(!addTasks()){
              return null;
            }
          }
          
          JobID toreducejob = reducetaskQueue.peek().getConf();
          if(jobTable.get(toreducejob).getStatus().getMapProgress() == 1){
            return reducetaskQueue.poll();
          }
        }
        else{
          if(maptaskQueue.isEmpty()){
            if(!addTasks()){
              return null;
            }
          }
          
          return maptaskQueue.poll();
        }
        return null;
  }
  
  
  
  public synchronized List<Task> assignTasks(TaskTrackerStatus taskTracker){
    //TODO: give tasks to tasktrackers based on their status 
    
    return null;
  }
  

  /**
   * Returns a collection of jobs in an order which is specific to 
   * the particular scheduler.
   * @param queueName
   * @return
   */
  public Collection<JobInProgress> getJobs(String queueName){
    return null;
  }
   
}
