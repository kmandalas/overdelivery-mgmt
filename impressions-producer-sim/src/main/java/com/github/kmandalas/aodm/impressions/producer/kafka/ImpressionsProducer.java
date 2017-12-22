package com.github.kmandalas.aodm.impressions.producer.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.github.kmandalas.aodm.impressions.producer.domain.Impression;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ImpressionsProducer {

	@Autowired
	private KafkaTemplate<String, Impression> kafkaTemplate;

	public ListenableFuture<SendResult<String, Impression>> send(Impression impression) {
		log.trace("sending payload: [{}]", impression);

		return kafkaTemplate.sendDefault(impression.getAdGroupId().toString(), impression);
	}
}
