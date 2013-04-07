package cmu.ds.mr.mapred;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.conf.JobConf;

public class MapTask extends Task {

  private static final Log LOG = LogFactory.getLog(MapTask.class.getName());
  
  public MapTask(JobID jobid, JobConf conf, int taskID, Task.TaskType type){
    super(jobid, conf, taskID, type);
  }
}
