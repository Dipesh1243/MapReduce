package cmu.ds.mr.mapred;

import cmu.ds.mr.mapred.TaskStatus.TaskType;


/**
 * Task ID class:
 * An example TaskID is : 
 * <code>task_200707121733_0003_m_000005_01</code> , which represents the
 * first try of fifth map task in the third job running at the jobtracker 
 * started at <code>200707121733</code>.
 * (Adapted from javadoc)
 * 
 * */
public class TaskID {
  
  private static final String taskStr = "task";
  
  private JobID jobId;
  private TaskType type;
  private int taskNum;
  private int tryNum;
  
  
  public TaskID(JobID jobId, TaskType type, int taskNum, int tryNum) {
    super();
    this.jobId = jobId;
    this.type = type;
    this.taskNum = taskNum;
    this.tryNum = tryNum;
  }

  public String toString() {
    return String.format("%s_%s_%s_%06d_%02d", taskStr, jobId.toString(), type, taskNum, tryNum);
  }

  public JobID getJobId() {
    return jobId;
  }

  public void setJobId(JobID jobId) {
    this.jobId = jobId;
  }

  public TaskType getType() {
    return type;
  }

  public void setType(TaskType type) {
    this.type = type;
  }

  public int getTaskNum() {
    return taskNum;
  }

  public void setTaskNum(int taskNum) {
    this.taskNum = taskNum;
  }

  public int getTryNum() {
    return tryNum;
  }

  public void setTryNum(int tryNum) {
    this.tryNum = tryNum;
  }
  
  
}
