package cmu.ds.mr.mapred;

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


import java.io.Serializable;
import java.text.NumberFormat;

/**
 * JobID: the ID class for Job
 * An example JobID is : 
 * <code>job_200707121733_0003</code> , which represents the third job 
 * running at the jobtracker started at <code>200707121733</code>. 
 * <p>
 */
public class JobID implements Comparable<JobID>, Serializable {
  private String jobStartDate;
  private int id;
  
  public JobID(String jobStartDate, int id) {
    this.id = id;
    this.jobStartDate = jobStartDate;
  }
  
  public JobID() { 
    jobStartDate = "";
  }
  
  public String getJobStartDate() {
    return jobStartDate;
  }
  
  @Override
  public int compareTo(JobID o) {
    JobID that = (JobID)o;
    int jtComp = this.jobStartDate.compareTo(that.jobStartDate);
    if(jtComp == 0) {
      return this.id - that.id;
    }
    else return jtComp;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + ((jobStartDate == null) ? 0 : jobStartDate.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    JobID other = (JobID) obj;
    if (id != other.id)
      return false;
    if (jobStartDate == null) {
      if (other.jobStartDate != null)
        return false;
    } else if (!jobStartDate.equals(other.jobStartDate))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return String.format("job_%s_%04d", jobStartDate, id);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
