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


import java.io.IOException;
import java.rmi.Remote;

/** 
 * Adapted and changed from Hadoop:
 * 
 * Protocol that task child process uses to contact its parent process.  The
 * parent is a daemon which which polls the central master for a new map or
 * reduce task and runs it as a child process.  All communication between child
 * and parent is via this protocol. */ 
interface TaskUmbilicalProtocol{

  /** 
   * Changed the version to 2, since we have a new method getMapOutputs 
   * Changed version to 3 to have progress() return a boolean
   * Changed the version to 4, since we have replaced 
   *         TaskUmbilicalProtocol.progress(String, float, String, 
   *         org.apache.hadoop.mapred.TaskStatus.Phase, Counters) 
   *         with statusUpdate(String, TaskStatus)
   * 
   * Version 5 changed counters representation for HADOOP-2248
   * Version 6 changes the TaskStatus representation for HADOOP-2208
   * Version 7 changes the done api (via HADOOP-3140). It now expects whether
   *           or not the task's output needs to be promoted.
   * Version 8 changes {job|tip|task}id's to use their corresponding 
   * objects rather than strings.
   * Version 9 changes the counter representation for HADOOP-1915
   * Version 10 changed the TaskStatus format and added reportNextRecordRange
   *            for HADOOP-153
   * Version 11 Adds RPCs for task commit as part of HADOOP-3150
   * Version 12 getMapCompletionEvents() now also indicates if the events are 
   *            stale or not. Hence the return type is a class that 
   *            encapsulates the events and whether to reset events index.
   * Version 13 changed the getTask method signature for HADOOP-249
   * Version 14 changed the getTask method signature for HADOOP-4232
   * Version 15 Adds FAILED_UNCLEAN and KILLED_UNCLEAN states for HADOOP-4759
   * Version 16 Change in signature of getTask() for HADOOP-5488
   * Version 17 Modified TaskID to be aware of the new TaskTypes
   * */

  /**
   * Report child's failure to parent.
   * 
   * @param taskId task-id of the child
   * @throws IOException
   * @throws InterruptedException
   * @return True if the task is known
   */
  boolean fail(TaskID taskId) 
  throws IOException, InterruptedException;

  /** Report that the task is successfully completed.  Failure is assumed if
   * the task process exits without calling this.
   * @param taskid task's id
   */
  void done(TaskID taskid) throws IOException;
}
