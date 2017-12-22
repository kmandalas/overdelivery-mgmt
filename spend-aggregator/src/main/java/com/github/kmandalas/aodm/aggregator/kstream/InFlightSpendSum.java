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

	@Autowired
	SpendAggregatorProperties spendAggregatorProperties;

	private KafkaStreams streams;

	@PostConstruct
	public void runStream() {

		Properties config = new Properties();
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, spendAggregatorProperties.getKafkaBootstrapServers());
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, spendAggregatorProperties.getApplicationId());
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, InsertionSerde.class);

		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		KStreamBuilder kStreamBuilder = new KStreamBuilder();
		KStream<String, Insertion> insertions = kStreamBuilder.stream(spendAggregatorProperties.getSourceTopic());

		final KTable<Windowed<String>, Double> windowedSums = insertions
				.map((key, value) -> new KeyValue<>(value.getAdGroupId().toString(), value.getInFlightSpend()))
				.groupByKey(Serdes.String(), Serdes.Double())
				.reduce((v1, v2) -> v1 + v2, TimeWindows.of(spendAggregatorProperties.getWindowSize()), spendAggregatorProperties.getStoreName());

		windowedSums
				.toStream()
				.map((key, value) -> new KeyValue<>(key.key(), value))
				.to(spendAggregatorProperties.getOutputTopic());

		streams = new KafkaStreams(kStreamBuilder, config);
		streams.start();
	}

	@PreDestroy
	public void closeStream() {
		streams.close();
	}
}
