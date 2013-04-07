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
  
  private float progress;
  
}
