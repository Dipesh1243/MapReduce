
.SUFFIXES: .java .class 
SOURCES = ../src/*.java 
CLASSES = -r cmu
JFLAGS = -g -classpath . -d .


.java.class:
	javac $(JFLAGS) $*.java

default:
	ant
	#javac $(JFLAGS) $(SOURCES)

all:
	ant
	#javac $(JFLAGS) $(SOURCES)

demo:
	java -cp . -Djava.rmi.server.codebase=file:/Users/lzy/Code/MapReduce/bin/ -Djava.security.policy=/Users/lzy/Code/MapReduce/server.policy cmu.ds.mr.test.WordCount

run-jt:
	java -cp . -Djava.rmi.server.codebase=file:/Users/lzy/Code/MapReduce/bin/ -Djava.security.policy=/Users/lzy/Code/MapReduce/server.policy cmu.ds.mr.mapred.JobTracker

run-tt:
	java -cp . -Djava.rmi.server.codebase=file:/Users/lzy/Code/MapReduce/bin/ -Djava.security.policy=/Users/lzy/Code/MapReduce/server.policy cmu.ds.mr.mapred.TaskTracker localhost

run-registry:
	rmiregistry

clean:
	rm $(CLASSES)

doc: $(SOURCES)
	javadoc -author $(SOURCES)


