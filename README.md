jps -v | grep “sbt”

jstack nnnn

top -o th

java.lang.Thread.State: WAITING (parking)
java.lang.Thread.State: TIMED_WAITING (sleeping)


To create and run a standalone jar file:
sbt assembly

java -jar target/scala-2.13/demo-assembly-0.1.jar


