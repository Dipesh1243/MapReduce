package cmu.ds.mr.conf;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.util.Util;

public class JobConf {
  private static final Log LOG = LogFactory.getLog(JobConf.class);

  private ArrayList<Object> resources = new ArrayList<Object>();

  private String inpath;
  private String outpath;
  private int numReduceTasks;

  private Class<?> mapclass;
  private Class<?> redclass;
  private Class mainclass;
  private Class outputKeyClass;
  private Class outputValueClass;

  private Properties properties = new Properties();

  public Class getOutputKeyClass() {
    return outputKeyClass;
  }

  public void setOutputKeyClass(Class outputKeyClass) {
    this.outputKeyClass = outputKeyClass;
  }

  public Class getOutputValueClass() {
    return outputValueClass;
  }

  public void setOutputValueClass(Class outputValueClass) {
    this.outputValueClass = outputValueClass;
  }

  public int getNumReduceTasks() {
    return numReduceTasks;
  }

  public void setNumReduceTasks(int numReduceTasks) {
    this.numReduceTasks = numReduceTasks;
  }

  public void setMapperClass(Class mapclass) {
    this.mapclass = mapclass;
  }

  public void setReducerClass(Class redclass) {
    this.redclass = redclass;
  }

  public String getJobName() {
    return get(Util.JOB_NAME);
  }

  public void setJobName(String name) {
    set(Util.JOB_NAME, name);
  }

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

  public String getInpath() {
    return inpath;
  }

  public void setInpath(String inpath) {
    this.inpath = inpath;
  }

  public String getOutpath() {
    return outpath;
  }

  public void setOutpath(String outpath) {
    this.outpath = outpath;
  }

  public Class<?> getMapperclass() {
    return mapclass;
  }

  public void setMapperclass(Class<?> mapperclass) {
    this.mapclass = mapperclass;
  }

  public Class<?> getReducerclass() {
    return redclass;
  }

  public void setReducerclass(Class<?> reducerclass) {
    this.redclass = reducerclass;
  }

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }
}