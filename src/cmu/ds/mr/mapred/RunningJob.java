/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cmu.ds.mr.mapred;

import java.io.IOException;


/** 
 * Adapted and changed from Hadoop.
 * 
 * <code>RunningJob</code> is the user-interface to query for details on a 
 * running Map-Reduce job.
 * 
 * <p>Clients can get hold of <code>RunningJob</code> via the {@link JobClient}
 * and then query the running-job for details such as name, configuration, 
 * progress etc.</p> 
 * 
 * @see JobClient
 */
public interface RunningJob {
  /**
   * Get the job identifier.
   * 
   * @return the job identifier.
   */
  public JobID getID();
  
  /**
   * Get the name of the job.
   * 
   * @return the name of the job.
   */
  public String getJobName();

  /**
   * Get the <i>progress</i> of the job's map-tasks, as a float between 0.0 
   * and 1.0.  When all map tasks have completed, the function returns 1.0.
   * 
   * @return the progress of the job's map-tasks.
   * @throws IOException
   */
  public float mapProgress() throws IOException;

  /**
   * Get the <i>progress</i> of the job's reduce-tasks, as a float between 0.0 
   * and 1.0.  When all reduce tasks have completed, the function returns 1.0.
   * 
   * @return the progress of the job's reduce-tasks.
   * @throws IOException
   */
  public float reduceProgress() throws IOException;

  /**
   * Check if the job is finished or not. 
   * This is a non-blocking call.
   * 
   * @return <code>true</code> if the job is complete, else <code>false</code>.
   * @throws IOException
   */
  public boolean isComplete() throws IOException;

  /**
   * Check if the job completed successfully. 
   * 
   * @return <code>true</code> if the job succeeded, else <code>false</code>.
   * @throws IOException
   */
  public boolean isSuccessful() throws IOException;
  
  /**
   * Blocks until the job is complete.
   * 
   * @throws IOException
   */
  public void waitForCompletion() throws IOException;

  /**
   * Returns the current state of the Job.
   * {@link JobStatus}
   * 
   * @throws IOException
   */
  public JobStatus.JobState getJobState() throws IOException;
  
  /**
   * Kill the running job.  Blocks until all job tasks have been
   * killed as well.  If the job is no longer running, it simply returns.
   * 
   * @throws IOException
   */
  public void killJob() throws IOException;
  
  /**
   * Set job status (since we have update it)
   * 
   * @param jobStatusNew the updated job status
   * */
  public void setJobStatus(JobStatus jobStatusNew) throws IOException;
}
