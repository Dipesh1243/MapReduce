package cmu.ds.mr.mapred;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.conf.JobConf;

public class ReduceTask extends Task {
  private static final Log LOG = LogFactory.getLog(ReduceTask.class.getName());

  public ReduceTask(JobID jobid, JobConf conf, int taskID) {
    super(jobid, conf, taskID);
  }
}
