package com.github.kmandalas.aodm.inventory.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.github.kmandalas.aodm.inventory.domain.Insertion;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InsertionsProducer {

	@Autowired
	private KafkaTemplate<String, Insertion> kafkaTemplate;

	public ListenableFuture<SendResult<String, Insertion>> send(Insertion insertion) {
		log.trace("sending payload: [{}]", insertion);

		return kafkaTemplate.sendDefault(insertion.getAdGroupId().toString(), insertion);
	}
}
