package com.github.kmandalas.aodm.impressions.producer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.github.kmandalas.aodm.impressions.producer.domain.Impression;
import com.github.kmandalas.aodm.impressions.producer.kafka.ImpressionsProducer;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

	@Autowired
	ImpressionsProducer producer;

	@Override
	public void save(Impression message) throws Exception {
		ListenableFuture<SendResult<String, Impression>> future = producer.send(message);

		future.addCallback(new ListenableFutureCallback<SendResult<String, Impression>>() {
			@Override
			public void onFailure(Throwable ex) {
				log.error("Sending to kafka failed for message: [{}]", message);
			}

			@Override
			public void onSuccess(SendResult<String, Impression> result) {
				log.trace("Message sent successfully to kafka: [{}]", message);
			}
		});
	}

}
