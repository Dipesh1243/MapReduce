package cmu.ds.mr.mapred;

public class JobState {
  public static enum State {
    SUCCESS, WAITING, RUNNING, READY, FAILED
  };

  private int JobID;

  private float mapProgress;

  private float reduceProgress;

  private float cleanupProgress;

  public int getJobID() {
    return JobID;
  }

  public void setJobID(int jobID) {
    JobID = jobID;
  }

  public float getMapProgress() {
    return mapProgress;
  }

  public void setMapProgress(float mapProgress) {
    this.mapProgress = mapProgress;
  }

  public float getReduceProgress() {
    return reduceProgress;
  }

  public void setReduceProgress(float reduceProgress) {
    this.reduceProgress = reduceProgress;
  }

  public float getCleanupProgress() {
    return cleanupProgress;
  }

  public void setCleanupProgress(float cleanupProgress) {
    this.cleanupProgress = cleanupProgress;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  private long startTime;

}
