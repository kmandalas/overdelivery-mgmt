package com.github.kmandalas.aodm.aggregator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.kmandalas.aodm.aggregator.config.SpendAggregatorProperties;
import com.github.kmandalas.aodm.aggregator.domain.Insertion;
import com.github.kmandalas.aodm.aggregator.utils.IntegrationTestUtils;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@Slf4j
public class TumblingWindowSumIntegrationTest {

	private static final String SOURCE_TOPIC = "ad-insertion-input-test";
	private static final String OUTPUT_TOPIC = "predicted-spend-output-test";

	@Autowired
	private SpendAggregatorProperties testConfig;

	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, SOURCE_TOPIC, OUTPUT_TOPIC);

	@Before
	public void setUp() throws Exception {

		// Wait until the partitions are assigned
		for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry.getListenerContainers()) {
			ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafka.getPartitionsPerTopic());
		}

		//
		// Produce some input data to the input topic.
		//
		final List<Insertion> inputValues = new ArrayList<>();
		inputValues.add(Insertion.builder().adGroupId(101).inFlightSpend(5.00).build());
		inputValues.add(Insertion.builder().adGroupId(101).inFlightSpend(5.00).build());

		inputValues.add(Insertion.builder().adGroupId(102).inFlightSpend(3.00).build());
		inputValues.add(Insertion.builder().adGroupId(102).inFlightSpend(3.00).build());

		Properties producerConfig = new Properties();
		producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, testConfig.getKafkaBootstrapServers());
		producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
		producerConfig.put(ProducerConfig.RETRIES_CONFIG, 0);
		producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
		IntegrationTestUtils.produceValuesSynchronously(testConfig.getSourceTopic(), inputValues, producerConfig);
	}

	@Test
	public void testTumblingWindowsSum() throws Exception {
		final List<String> expectedValues = Arrays.asList("10.0", "6.0");

		//
		// Verify the application's output data.
		//
		Properties consumerConfig = new Properties();
		consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, testConfig.getKafkaBootstrapServers());
		consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "aodm-integration-test-dummy-consumer");
		consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		List<String> actualValues = IntegrationTestUtils.waitUntilMinValuesRecordsReceived(consumerConfig,
				testConfig.getOutputTopic(), expectedValues.size());
		assertThat(actualValues).isEqualTo(expectedValues);
	}

	@After
	public void tearDown() {
		// Remove any state for next test runs
		try {
			IntegrationTestUtils.purgeLocalStreamsState(testConfig.getStateDirConfig());
		} catch (IOException e) {
			log.warn("Error while purging local stream state: ", e.getMessage());
		}
	}
}
