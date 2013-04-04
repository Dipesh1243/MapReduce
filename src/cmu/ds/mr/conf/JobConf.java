package cmu.ds.mr.conf;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.io.Path;

public class JobConf {
  private static final Log LOG =
          LogFactory.getLog(JobConf.class); 
  
  private ArrayList<Object> resources = new ArrayList();
  
  private ArrayList<Path> inputpath = new ArrayList();
  private Path outputpath = new Path();
  private int num_reducer;
  
  private Class<?> mapperclass;
  private Class<?> reducerclass;
  
  
  private Properties properties = new Properties();
  
  
  public JobConf(){
    
  }

}