package cmu.ds.mr.conf;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.io.Path;

public class JobConf {
  private static final Log LOG =
          LogFactory.getLog(JobConf.class); 
  private static final String NUM_MAP_TASK = "mapred.map.tasks";
  private static final String NUM_RED_TASK = "mapred.red.tasks";
  
  
  private ArrayList<Object> resources = new ArrayList();
  
  private ArrayList<Path> inputpath = new ArrayList();
  private Path outputpath = new Path();
  private int num_reducer;
  
  private Class<?> mapperclass;
  private Class<?> reducerclass;
  
  private Properties properties = new Properties();
  private String jobName;
  
  
  
  
  public void set(String key, String val) {
    properties.setProperty(key, val);
  }
  
  public String get(String key) {
    return properties.getProperty(key);
  }
  
  public ArrayList<Object> getResources() {
    return resources;
  }
  public void setResources(ArrayList<Object> resources) {
    this.resources = resources;
  }
  public ArrayList<Path> getInputpath() {
    return inputpath;
  }
  public void setInputpath(ArrayList<Path> inputpath) {
    this.inputpath = inputpath;
  }
  public Path getOutputpath() {
    return outputpath;
  }
  public void setOutputpath(Path outputpath) {
    this.outputpath = outputpath;
  }
  public int getNum_reducer() {
    return num_reducer;
  }
  public void setNum_reducer(int num_reducer) {
    this.num_reducer = num_reducer;
  }
  public Class<?> getMapperclass() {
    return mapperclass;
  }
  public void setMapperclass(Class<?> mapperclass) {
    this.mapperclass = mapperclass;
  }
  public Class<?> getReducerclass() {
    return reducerclass;
  }
  public void setReducerclass(Class<?> reducerclass) {
    this.reducerclass = reducerclass;
  }
  public Properties getProperties() {
    return properties;
  }
  public void setProperties(Properties properties) {
    this.properties = properties;
  }
  public String getJobName() {
    return jobName;
  }
  public void setJobName(String jobName) {
    this.jobName = jobName;
  }
  
}