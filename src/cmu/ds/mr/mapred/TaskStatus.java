package cmu.ds.mr.mapred;

import java.io.Serializable;

/**
 * Task status to keep track of task progress and state
 * 
 * */
public class TaskStatus implements Serializable {
  
  public static enum TaskState {
    SUCCEEDED, WAITING, DEFINE, RUNNING, READY, FAILED, KILLED
  };
  
  public static enum TaskType {
    MAP("m"), REDUCE("r");
    private TaskType(final String text) {
      this.text = text;
    }

    private final String text;

    @Override
    public String toString() {
        return text;
    }
  };
  
  private int taskId;
  private TaskState state = TaskState.DEFINE;
  private TaskType type;
  private int taskNum;
  private int tryNum;
  


  public TaskStatus(int taskid, TaskType type){
    this.taskId = taskid;
    this.type = type;
    this.state = TaskState.READY;
  }
  
  
  //private float progress;
  
  public int getTaskId() {
    return taskId;
  }
  public void setTaskId(int taskId) {
    this.taskId = taskId;
  }
  public TaskState getState() {
    return state;
  }
  public void setState(TaskState state) {
    this.state = state;
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
