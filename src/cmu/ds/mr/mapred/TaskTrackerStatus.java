package cmu.ds.mr.mapred;

import java.io.Serializable;
import java.util.List;

/**
 * TaskTrackerStatus class used to communication between TaskTracker and JobTracker for all task
 * status in current taskTracker
 * 
 * */
public class TaskTrackerStatus implements Serializable {

  private List<TaskStatus> taskStatusList;

  private int numFreeMapSlots;

  private int numFreeRedSlots;

  // set only through constructor
  public TaskTrackerStatus(List<TaskStatus> taskStatusList, int numFreeMapSlots, int numFreeRedSlots) {
    super();
    this.taskStatusList = taskStatusList;
    this.numFreeMapSlots = numFreeMapSlots;
    this.numFreeRedSlots = numFreeRedSlots;
  }

  public List<TaskStatus> getTaskStatusList() {
    return taskStatusList;
  }

  public int getNumFreeMapSlots() {
    return numFreeMapSlots;
  }

  public int getNumFreeRedSlots() {
    return numFreeRedSlots;
  }

}
