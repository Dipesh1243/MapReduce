package cmu.ds.mr.mapred;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.conf.JobConf;

public class Task {
  private static final Log LOG = LogFactory.getLog(Task.class);
  
  private long rangeStart;
  private long rangeEnd;
  
  protected JobConf conf;
  
  
}
