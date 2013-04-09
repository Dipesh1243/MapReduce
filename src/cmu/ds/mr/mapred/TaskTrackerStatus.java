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

  private int numFreeSlots;

  // set only through constructor
  public TaskTrackerStatus(List<TaskStatus> taskStatusList, int numFreeSlots) {
    super();
    this.taskStatusList = taskStatusList;
    this.numFreeSlots = numFreeSlots;
  }

  public List<TaskStatus> getTaskStatusList() {
    return taskStatusList;
  }

  public int getNumFreeSlots() {
    return numFreeSlots;
  }

}
