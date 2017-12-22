package com.github.kmandalas.aodm.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.github.kmandalas.aodm.inventory.domain.Insertion;
import com.github.kmandalas.aodm.inventory.kafka.InsertionsProducer;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

	@Autowired
	InsertionsProducer producer;

	@Async
	public void doSend(Insertion message) {
		ListenableFuture<SendResult<String, Insertion>> future = producer.send(message);

		future.addCallback(new ListenableFutureCallback<SendResult<String, Insertion>>() {
			@Override
			public void onFailure(Throwable ex) {
				log.error("Sending to kafka failed for message: [{}]", message);
			}

			@Override
			public void onSuccess(SendResult<String, Insertion> result) {
				log.trace("Message sent successfully to kafka: [{}]", message);
			}
		});
	}
}
