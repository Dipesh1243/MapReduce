package cmu.ds.mr.mapred;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.conf.JobConf;
import cmu.ds.mr.io.FileSplit;
import cmu.ds.mr.io.LineRecordReader;
import cmu.ds.mr.io.MapOutputCollector;
import cmu.ds.mr.util.Util;

public class ReduceTask extends Task {
  public ReduceTask(JobID jobid, JobConf conf, TaskStatus taskStatus){
    super(jobid, conf, taskStatus);
  }

  @Override
  public void startTask(JobConf jobConf, TaskUmbilicalProtocol taskTrackerProxy) throws IOException,
          ClassNotFoundException, InterruptedException, RuntimeException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    

  }
}
