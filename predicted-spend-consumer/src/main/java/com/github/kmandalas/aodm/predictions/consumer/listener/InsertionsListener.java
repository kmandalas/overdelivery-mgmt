package com.github.kmandalas.aodm.predictions.consumer.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import com.github.kmandalas.aodm.predictions.consumer.client.BudgetServiceClient;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InsertionsListener {

	@Value("${source-topic}")
	private String sourceTopic;

	@Value("${partition-no}")
	private String topicPartition;

	@Autowired
	BudgetServiceClient budgetServiceClient;

	@KafkaListener(topicPartitions = @TopicPartition(topic = "${source-topic}", partitions = {"${partition-no}"}))
	public void listen(ConsumerRecord<String, String> record) {
		String adGroupId = record.key();
		String amount = record.value();

		log.info(String.format("Consuming record with key: [%s] and value: [%s] from topic [%s] and partition [%s]",
				adGroupId, amount, sourceTopic, topicPartition));

		budgetServiceClient.updateInflightSpend(Integer.valueOf(adGroupId), Double.valueOf(amount));
	}
}
