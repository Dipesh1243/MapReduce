package cmu.ds.mr.mapred;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import cmu.ds.mr.conf.JobConf;
import cmu.ds.mr.io.FileSplit;
import cmu.ds.mr.io.LineRecordReader;
import cmu.ds.mr.io.MapOutputCollector;
import cmu.ds.mr.mapred.TaskStatus.TaskType;
import cmu.ds.mr.util.Util;

public class MapTask extends Task {

  //private static final Log LOG = LogFactory.getLog(MapTask.class.getName());
  
  public MapTask(TaskID taskid, JobConf taskconf, TaskStatus taskStatus){
    super(taskid, taskconf, taskStatus);
  }

  @Override
  public void startTask(JobConf taskConf, TaskUmbilicalProtocol taskTrackerProxy) throws IOException,
          ClassNotFoundException, InterruptedException, RuntimeException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    // get split files
    List<FileSplit> files = taskConf.getSplitFiles(); 
    
    // read map input (sorted by key)
    LineRecordReader reader = new LineRecordReader();
    Map<Long, String> mapInput = reader.readAllRecordInFile(files.get(taskStatus.getTaskNum()));
    
    // get user defined mapper
    Mapper mapper = (Mapper) Util.newInstance(taskConf.getMapperclass());
    
    // get output collector
    taskConf.setMapOutPath(taskConf.get(Util.LOCAL_ROOT_DIR) + File.separator + "mapout"+ File.separator);
    String basePath = taskConf.getMapOutPath() + taskId.toString() + File.separator;
    int nred = taskConf.getNumReduceTasks();
    MapOutputCollector output = new MapOutputCollector(basePath, nred);
    
    for(Entry<Long, String> en : mapInput.entrySet()) {
      mapper.map(en.getKey(), en.getValue(), output);
    } 
    
    output.writeToDisk();
    // notify taskTracker
    taskTrackerProxy.done(taskStatus.getTaskId());
  }

}
