1.Go to Kafka folder:
cd /Users/user/Documents/software/kafka_2.13-3.0.0/bin

2.Start Zookeeper
./zookeeper-server-start.sh ../config/zookeeper.properties

3.Start Kafka Server
./kafka-server-start.sh ../config/server.properties

4.Create Topics
./kafka-topics.sh --create --topic input-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
./kafka-topics.sh --create --topic output-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1

5.Listing all the topics
./kafka-topics.sh --list --bootstrap-server localhost:9092

6.Create Kafka Console Producer:
./kafka-console-producer.sh --topic input-topic --bootstrap-server localhost:9092

7.Create Kafka Console Consumers:
./kafka-console-consumer.sh --topic input-topic --from-beginning --bootstrap-server localhost:9092
./kafka-console-consumer.sh --topic output-topic --from-beginning --bootstrap-server localhost:9092

8.Go to Apache Link folder
cd /Users/user/Documents/software/flink-1.14.0/bin

9.Start Apache Flink
./start-cluster.sh

10.Run the Java application main file

11.Write any lowercase string on the producer terminal

12.Check consumer terminal with output-topic. String should be converted to uppercase

