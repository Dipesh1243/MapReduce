package cmu.ds.mr.mapred;


import JobClient;
import RunningJob;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.conf.JobConf;
import cmu.ds.mr.io.Path;


/**
 * JobClient can
 * 1. Configure job
 * 2. Kill job 
 * 3. Query progress or status
 * */
public class JobClient {
   
  private static final Log LOG = LogFactory.getLog(JobClient.class);
  
  private JobConf jobConf;
  private JobSubmissionProtocol jobTrackerProxy;
  
  private Path sysDir;  // root directory of all job related files

  public JobClient(JobConf jobConf) {
    super();
    this.jobConf = jobConf;
  }
  
  public static RunningJob runJob(JobConf jobConf) {
    JobClient jc = new JobClient(jobConf);
    RunningJob job = jc.submitJob(jobConf);
    // query status and state every second
    if (!jc.monitorAndPrintJob(jobConf, job)) {
      throw new IOException("Job failed!");
    }
    // job done
    return job;
  }

  private boolean monitorAndPrintJob(JobConf jobConf, RunningJob job) {
    // TODO Auto-generated method stub
    return false;
  }

  public RunningJob submitJob(JobConf jobConf) throws IOException {
    // step 2: get new job ID
    JobID jobId = jobTrackerProxy.getNewJobId();
    
    // TODO: split input job and input files
    //String jobRootDir = getSystemDir().toString() + File.separatorChar + jobId.toString();
    
    
    
    // step 3: submit job
    JobStatus status = jobTrackerProxy.submitJob(jobId);
    if (status != null) {
      job.setJobStatus(status);
      return job;
    } else {
      LOG.error("Could not launch job");
      throw new IOException("Could not launch job");
    }
  }
  

}
