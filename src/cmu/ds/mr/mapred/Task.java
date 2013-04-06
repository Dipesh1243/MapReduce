package cmu.ds.mr.mapred;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.conf.JobConf;

public class Task {
  private static final Log LOG = LogFactory.getLog(Task.class);
  
  public static enum TaskState {
    SUCCEEDED, WAITING, DEFINE, RUNNING, READY, FAILED, KILLED
  };
  
  private String inputpath;
  private long rangeStart;
  private long length;
  
  private String outputpath;
  
  protected JobConf conf;
  protected JobID jobid;
  protected TaskState state;
  
}
