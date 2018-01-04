package com.github.kmandalas.aodm.aggregator.kstream;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.github.kmandalas.aodm.aggregator.config.SpendAggregatorProperties;
import com.github.kmandalas.aodm.aggregator.domain.Insertion;
import com.github.kmandalas.aodm.aggregator.serializers.InsertionSerde;

@Component
@EnableConfigurationProperties(SpendAggregatorProperties.class)
public class InFlightSpendSum {

	private final SpendAggregatorProperties appConfig;

	private KafkaStreams streams;

	@Autowired
	public InFlightSpendSum(final SpendAggregatorProperties appConfig) {
		this.appConfig = appConfig;
	}

	@PostConstruct
	public void runStream() {

		final Properties streamsConfig = new Properties();
		streamsConfig.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getKafkaBootstrapServers());
		streamsConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, appConfig.getApplicationId());
		streamsConfig.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		streamsConfig.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, InsertionSerde.class);

		streamsConfig.put(StreamsConfig.STATE_DIR_CONFIG, appConfig.getStateDirConfig());

		streamsConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		KStreamBuilder kStreamBuilder = new KStreamBuilder();
		KStream<String, Insertion> insertions = kStreamBuilder.stream(appConfig.getSourceTopic());

		final KTable<Windowed<String>, Double> windowedSums = insertions
				.map((key, value) -> new KeyValue<>(value.getAdGroupId().toString(), value.getInFlightSpend()))
				.groupByKey(Serdes.String(), Serdes.Double())
				.reduce((v1, v2) -> v1 + v2, TimeWindows.of(appConfig.getWindowSize()));

		windowedSums
				.toStream()
				.map((key, value) -> new KeyValue<>(key.key(), value))
				.to(appConfig.getOutputTopic());

		streams = new KafkaStreams(kStreamBuilder, streamsConfig);
		streams.start();
	}

	@PreDestroy
	public void closeStream() {
		streams.close();
	}
}
