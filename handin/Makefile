
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

run-wc:
	java -cp . -Djava.security.policy=security.policy cmu.ds.mr.test.WordCount $(IN) $(OUT) $(NRED)

run-an:
	java -cp . -Djava.security.policy=security.policy cmu.ds.mr.test.Anagram $(IN) $(OUT) $(NRED)

run-jt:
	java -cp . -Djava.security.policy=security.policy cmu.ds.mr.mapred.JobTracker

run-tt:
	java -cp . -Djava.security.policy=security.policy cmu.ds.mr.mapred.TaskTracker $(ADDR)

run-registry:
	rmiregistry

clean:
	rm $(CLASSES)

doc: $(SOURCES)
	javadoc -author $(SOURCES)


