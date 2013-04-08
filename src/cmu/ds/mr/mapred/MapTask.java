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
import cmu.ds.mr.mapred.TaskStatus.TaskType;
import cmu.ds.mr.util.Util;

public class MapTask extends Task {

  //private static final Log LOG = LogFactory.getLog(MapTask.class.getName());
  
  public MapTask(TaskID taskid, JobConf taskconf, TaskStatus taskStatus){
    super(taskid, taskconf, taskStatus);
  }

  @Override
  public void startTask(JobConf jobConf, TaskUmbilicalProtocol taskTrackerProxy) throws IOException,
          ClassNotFoundException, InterruptedException, RuntimeException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    // get split files
    List<FileSplit> files = jobConf.getSplitFiles(); 
    
    // read map input (sorted by key)
    LineRecordReader reader = new LineRecordReader();
    Map<Long, String> mapInput = reader.readAllRecordInFile(files.get(taskStatus.getTaskNum()));
    
    // get user defined mapper
    Mapper mapper = (Mapper) Util.newInstance(jobConf.getMapperclass());
    
    // get output collector
    String basePath = jobConf.getMapOutPath();
    int nred = jobConf.getNumReduceTasks();
    MapOutputCollector output = new MapOutputCollector(basePath, nred);
    
    for(Entry<Long, String> en : mapInput.entrySet()) {
      mapper.map(en.getKey(), en.getValue(), output);
    } 
    
    output.writeToDisk();
    // notify taskTracker
    taskTrackerProxy.done(taskStatus.getTaskId());
  }

  @Override
  public TaskRunner createRunner(TaskTracker tracker, Task task) throws IOException {
    return new TaskRunner(task, this.taskConf, tracker);
  }
}
