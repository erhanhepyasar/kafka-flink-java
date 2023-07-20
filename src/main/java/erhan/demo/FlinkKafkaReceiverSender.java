package erhan.demo;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


public class FlinkKafkaReceiverSender {

	public static final String SERVER = "localhost:9092";
	public static final String INPUT_TOPIC = "input-topic";
	public static final String OUTPUT_TOPIC = "output-topic";
	public static final String FLINK_CONSUMER_GROUP = "flink-consumer-group";

	public static void main(String[] args) throws Exception {
		 StreamStringOperation();
	}
	
	public static class StringCapitalize implements MapFunction<String, String> {
	    @Override
	    public String map(String data) {
	        return data.toUpperCase();
	    }
	}

	public static void StreamStringOperation() throws Exception {
		final StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
		final KafkaSource<String> kafkaSource = createKafkaSource();
		final KafkaSink<String> kafkaSink = createKafkaSink();

		// Consuming from Kafka
		DataStreamSource<String> stringInputStream = environment.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source");
		stringInputStream.map(new StringCapitalize()).sinkTo(kafkaSink);
		environment.execute("Read from Kafka");

	}

	public static KafkaSource<String> createKafkaSource() {
		return KafkaSource.<String>builder()
				.setBootstrapServers(SERVER)
				.setTopics(INPUT_TOPIC)
				.setGroupId(FLINK_CONSUMER_GROUP)
				.setStartingOffsets(OffsetsInitializer.latest())
				.setValueOnlyDeserializer(new SimpleStringSchema())
				.build();
	}

	public static KafkaSink<String> createKafkaSink(){
		KafkaRecordSerializationSchema<String> serializer = KafkaRecordSerializationSchema.builder()
				.setValueSerializationSchema(new SimpleStringSchema())
				.setTopic(OUTPUT_TOPIC)
				.build();

		return KafkaSink.<String>builder()
				.setBootstrapServers(SERVER)
 				.setRecordSerializer(serializer)
				.build();
	}

}
