package cmu.ds.mr.mapred;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cmu.ds.mr.conf.JobConf;
import cmu.ds.mr.util.Util;

/**
 * JobClient can 1. Configure job 2. Kill job 3. Query progress or status
 * */
public class JobClient {

  private static final Log LOG = LogFactory.getLog(JobClient.class);

  private JobConf jobConf;

  private JobSubmissionProtocol jobTrackerProxy;

  private Job job; // Implementation of RunningJob

  private String sysDir; // root directory of all job related files

  private Properties prop = new Properties();

  public JobClient(JobConf jobConf) throws FileNotFoundException, IOException {
    super();
    this.jobConf = jobConf;
    readConfig();
  }

  public void readConfig() throws FileNotFoundException, IOException {
    prop.load(new FileInputStream("./conf/jobClient.conf"));
  }

  public static RunningJob runJob(JobConf jobConf) throws IOException, InterruptedException {
    JobClient jc = new JobClient(jobConf);
    RunningJob job = jc.submitJob(jobConf);

    // query status and state every second
    if (!jc.monitorAndPrintJob(jobConf, job)) {
      throw new IOException("Job failed!");
    }
    // job done
    return job;
  }

  /**
   * Monitor and print job status
   * */
  private boolean monitorAndPrintJob(JobConf jobConf, RunningJob job) throws IOException,
          InterruptedException {
    JobID jid = job.getID();

    while (!job.isComplete()) {
      Thread.sleep(Util.TIME_INTERVAL_MONITOR);

      // ask job tracker for new job
      JobStatus jobStatusNew = jobTrackerProxy.getJobStatus(jid);
      job.setJobStatus(jobStatusNew);

      String logstr = String.format("%s: map %.1f\\%\treduce %.1f\\%", jid.toString(),
              job.mapProgress(), job.reduceProgress());
      LOG.info(logstr);
      System.out.println(logstr);
    }
    return true;
  }

  public RunningJob submitJob(JobConf jobConf) throws IOException {
    // step 2: get new job ID
    JobID jid = jobTrackerProxy.getNewJobId();

    // TODO: split input files
    String jobRootDir = getSystemDir().toString() + File.separatorChar + jid.toString();
    splitInputFiles(jobConf, jobRootDir);

    // step 3: submit job
    JobStatus status = jobTrackerProxy.submitJob(jid);
    if (status != null) {
      // return a RunningJob (Job class)
      return new Job(jid, jobConf, status);
    } else {
      LOG.error("Could not launch job");
      throw new IOException("Could not launch job");
    }
  }

  /**
   * Get the root directory of
   * */
  public String getSystemDir() {
    return System.getProperty("user.dir");
  }

  /**
   * Split input files
   * 
   * @throws IOException
   * */
  public void splitInputFiles(JobConf jobConf, String jobRootDir) throws IOException {
    File dirFile = new File(jobRootDir);
    if (!dirFile.exists())
      dirFile.mkdirs();
    
    long fileLen = 0l;
    long blksize = Long.parseLong((String) prop.get(Util.BLOCK_SIZE));
    int cnt = 0;
    String part = "part-", line;
    String outpath = String.format("%s%c%s%05d", jobConf.getOutpath(), File.separatorChar,
            "part-", cnt);
    BufferedReader br = null;
    BufferedWriter bw = new BufferedWriter(new FileWriter(outpath));

    try {
      File inFile = new File(jobConf.getInpath());
      if (inFile.isDirectory()) {
        // TODO: multiple files
        File[] files = inFile.listFiles();

        for (int i = 0; i < files.length; i++) {
          br = new BufferedReader(new FileReader(files[i]));
          while((line = br.readLine()) != null) {
            bw.write(line);
            fileLen += line.getBytes().length;

            if (fileLen >= blksize) {
              fileLen = 0;
              bw.close();
              outpath = String.format("%s%c%s%05d", jobConf.getOutpath(), File.separatorChar,
                      "part-", ++cnt);
              bw = new BufferedWriter(new FileWriter(outpath));
            }
          }
        }
      } 
      else {
        br = new BufferedReader(new FileReader(inFile));
        while ((line = br.readLine()) != null) {
          bw.write(line);
          fileLen += line.getBytes().length;

          if (fileLen >= blksize) {
            fileLen = 0;
            bw.close();
            outpath = String.format("%s%c%s%05d", jobConf.getOutpath(), File.separatorChar,
                    "part-", ++cnt);
            bw = new BufferedWriter(new FileWriter(outpath));
          }
        }
      }
    } finally {
      br.close();
      bw.close();
    }
  }
}
