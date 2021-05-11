java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:G1.log GCLogAnalysis.java

java -Xms512m -Xmx512m -Xlog:gc*=info:file=G1.log:time GCLogAnalysis.java

java -XX:+UseSerialGC -Xms512m -Xmx512m -Xlog:gc*=info:file=Serial.log:time GCLogAnalysis.java

java -XX:+UseParallelGC -Xms512m -Xmx512m -Xlog:gc*=info:file=Parallel.log:time GCLogAnalysis.java

java -XX:+UseConcMarkSweepGC -Xms512m -Xmx512m -Xlog:gc*=info:file=ConcMarkSweep.log:time GCLogAnalysis.java


java -jar -Xms512m -Xmx512m -XX:+UseG1GC -Xlog:gc*=info:file=gateway.log:time gateway-server-0.0.1-SNAPSHOT.jar

sb -u "http://localhost:8088/api/hello" -c 4 -n 1000
















