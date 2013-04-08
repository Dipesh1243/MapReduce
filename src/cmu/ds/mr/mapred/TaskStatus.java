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
  
  private TaskID taskId;
  private TaskState state = TaskState.DEFINE;
  private TaskType type;
  
  
  //private float progress;
  
  public TaskID getTaskId() {
    return taskId;
  }
  public void setTaskId(TaskID taskId) {
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
  
}
