#!/bin/ksh
# Author: Surya Kiran

if [ -z $JAVA_HOME ]; then
  echo "JAVA_HOME environment variable is not already set."
  export JAVA_HOME="/usr/java/default"
  export PATH="$JAVA_HOME/bin:$PATH"
  echo "Initializing JAVA_HOME with default value: $JAVA_HOME, and including it in PATH."
fi

if [ -z $CDH_HOME ]; then
  echo "CDH_HOME environment variable is not already set."
  export CDH_HOME="/opt/cloudera/parcels/CDH"
  echo "Initializing CDH_HOME with default value: $CDH_HOME"
fi

if [ -z $HADOOP_CONF ]; then
  echo "HADOOP_CONF environment variable is not already set."
  export HADOOP_CONF="/etc/hadoop/conf"
  echo "Initializing HADOOP_CONF with default value: $HADOOP_CONF"
fi

echo "Default CLASSPATH to start with: $CLASSPATH"
for i in $CDH_HOME/jars/log4j-*.jar \
         $CDH_HOME/jars/hive-jdbc-*.jar \
         $CDH_HOME/jars/hive-exec-*.jar \
         $CDH_HOME/jars/hive-service-*.jar \
         $CDH_HOME/jars/libthrift-*.jar \
         $CDH_HOME/jars/httpclient-*.jar \
         $CDH_HOME/jars/httpcore-*.jar \
         $CDH_HOME/jars/hadoop-hdfs-*.jar \
         $CDH_HOME/jars/hadoop-common-*.jar \
         $CDH_HOME/jars/hadoop-core-*.jar \
         $CDH_HOME/jars/hadoop-auth-*.jar \
         $CDH_HOME/jars/commons-logging-*.jar \
         $CDH_HOME/jars/commons-configuration-*.jar \
         $CDH_HOME/jars/commons-collections-*.jar \
         $CDH_HOME/jars/commons-cli-*.jar \
         $CDH_HOME/jars/javax.servlet-*.jar \
         $HADOOP_CONF \
         ./*.jar
do
  echo "Adding $i to CLASSPATH"
  export CLASSPATH=$i:$CLASSPATH
done

print() {
  echo $1
}

print "==========================================================="
print "Environment variables used when running this program: "
print "-----------------------------------------------------------"
print "JAVA_HOME = $JAVA_HOME"
print "CDH_HOME = $CDH_HOME"
print "HADOOP_CONF = $HADOOP_CONF"
print "CLASSPATH = $CLASSPATH"
print "==========================================================="

print "Invoking the Java program ..."
java -cp $CLASSPATH -Xms2048m -Xmx3096m com.surya.launcher.MainLauncher "$@"

JAVA_PROG_EXIT_STATUS=$?
print "Java program exited with status: $JAVA_PROG_EXIT_STATUS"

# Exit program with same status as that of the Java program
exit $JAVA_PROG_EXIT_STATUS
