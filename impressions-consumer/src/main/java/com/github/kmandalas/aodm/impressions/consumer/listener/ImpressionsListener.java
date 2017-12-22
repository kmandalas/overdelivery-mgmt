package com.github.kmandalas.aodm.impressions.consumer.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import com.github.kmandalas.aodm.impressions.consumer.client.BudgetServiceClient;
import com.github.kmandalas.aodm.impressions.consumer.domain.Impression;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ImpressionsListener {

	@Value("${source-topic}")
	private String sourceTopic;

	@Value("${partition-no}")
	private String topicPartition;

	@Autowired
	BudgetServiceClient budgetServiceClient;

	@KafkaListener(topicPartitions = @TopicPartition(topic = "${source-topic}", partitions = {"${partition-no}"}))
	public void consume(Impression message) {
		log.info(String.format("Consuming message [%s] from topic [%s] and partition [%s]", message, sourceTopic, topicPartition));

		budgetServiceClient.updateActualSpend(message.getAdGroupId());
	}
}
