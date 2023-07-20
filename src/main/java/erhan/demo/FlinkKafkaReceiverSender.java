package erhan.demo;

import java.util.Properties;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;

public class FlinkKafkaReceiverSender {
	
	final static String SERVER = "localhost:9092";
	final static String INPUT_TOPIC = "input-topic";
	final static String OUTPUT_TOPIC = "output-topic";

	public static void main(String[] args) throws Exception {
		 StreamStringOperation(SERVER, INPUT_TOPIC, OUTPUT_TOPIC);
	}
	
	public static class StringCapitalize implements MapFunction<String, String> {
	    @Override
	    public String map(String data) {
	        return data.toUpperCase();
	    }
	}
	
	public static void StreamStringOperation(String server, String inputTopic, String outputTopic ) throws Exception {
	    final StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
	    final FlinkKafkaConsumer011<String> flinkKafkaConsumer = createStringConsumerForTopic(inputTopic, server);
	    final FlinkKafkaProducer011<String> flinkKafkaProducer = createStringProducer(outputTopic, server);
	    final DataStream<String> stringInputStream = environment.addSource(flinkKafkaConsumer);
	  
	    stringInputStream.map(new StringCapitalize()).addSink(flinkKafkaProducer);
	    environment.execute();
	}
	
	public static FlinkKafkaConsumer011<String> createStringConsumerForTopic(String topic, String kafkaAddress) {
		final Properties props = new Properties();
		props.setProperty("bootstrap.servers", kafkaAddress);

		return new FlinkKafkaConsumer011<>(topic, new SimpleStringSchema(), props);
	}
	
	public static FlinkKafkaProducer011<String> createStringProducer(String topic, String kafkaAddress){

		return new FlinkKafkaProducer011<>(kafkaAddress, topic, new SimpleStringSchema());
	}


}
