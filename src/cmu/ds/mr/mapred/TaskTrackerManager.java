package cmu.ds.mr.mapred;

interface TaskTrackerManager {
  /**
   * @return A collection of the {@link TaskTrackerStatus} for the tasktrackers
   * being managed.
   */
  public Collection<TaskTrackerStatus> taskTrackers();
  
  /**
   * @return The number of unique hosts running tasktrackers.
   */
  public int getNumberOfUniqueHosts();
  
  /**
   * @return a summary of the cluster's status.
   */
  public ClusterStatus getClusterStatus();

  /**
   * Registers a {@link JobInProgressListener} for updates from this
   * {@link TaskTrackerManager}.
   * @param jobInProgressListener the {@link JobInProgressListener} to add
   */
  public void addJobInProgressListener(JobInProgressListener listener);

  /**
   * Unregisters a {@link JobInProgressListener} from this
   * {@link TaskTrackerManager}.
   * @param jobInProgressListener the {@link JobInProgressListener} to remove
   */
  public void removeJobInProgressListener(JobInProgressListener listener);

  /**
   * Return the {@link QueueManager} which manages the queues in this
   * {@link TaskTrackerManager}.
   *
   * @return the {@link QueueManager}
   */
  public QueueManager getQueueManager();
  
  /**
   * Return the current heartbeat interval that's used by {@link TaskTracker}s.
   *
   * @return the heartbeat interval used by {@link TaskTracker}s
   */
  public int getNextHeartbeatInterval();

  /**
   * Kill the job identified by jobid
   * 
   * @param jobid
   * @throws IOException
   */
  public void killJob(JobID jobid)
      throws IOException;

  /**
   * Obtain the job object identified by jobid
   * 
   * @param jobid
   * @return jobInProgress object
   */
  public JobInProgress getJob(JobID jobid);
}
