package com.github.kmandalas.aodm.aggregator.kstream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kstream.annotations.KStreamProcessor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.github.kmandalas.aodm.aggregator.domain.Insertion;

@EnableBinding(KStreamProcessor.class)
@EnableScheduling
public class InFlightSpendSum {

	@Value("${store-name}")
	private String storeName;

	@Value("${window-size}")
	private long windowSize;

//	@Autowired
//	private KStreamBuilderFactoryBean kStreamBuilderFactoryBean;
//
//	private ReadOnlyKeyValueStore<Object, Object> keyValueStore;

	@StreamListener("input")
	@SendTo("output")
	public KStream<String, Double> process(KStream<Object, Insertion> input) {

		final KTable<Windowed<String>, Double> windowedSums = input
				.map((key, value) -> new KeyValue<>(value.getAdGroupId().toString(), value.getInFlightSpend()))
				.groupByKey(Serdes.String(), Serdes.Double())
				.reduce((v1, v2) -> v1 + v2, TimeWindows.of(windowSize).until(windowSize), storeName);

		return windowedSums.toStream().map((key, value) -> new KeyValue<>(key.key(), value));
	}

//	@Scheduled(fixedRate = 30000, initialDelay = 5000)
//	public void printProductCounts() {
//		if (keyValueStore == null) {
//			KafkaStreams streams = kStreamBuilderFactoryBean.getKafkaStreams();
//			keyValueStore = streams.store(storeName, QueryableStoreTypes.keyValueStore());
//
//			keyValueStore.all().forEachRemaining(kv -> System.out.println("AD GROUP ID: " + kv.key + " in-flight spend TOTAL: " + kv.value));
//		}
//	}
}
