package com.github.kmandalas.aodm.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.kmandalas.aodm.gateway.client.BudgetServiceClient;
import com.github.kmandalas.aodm.gateway.transport.ChartDTO;

@Component
public class RandomDataGenerator implements ApplicationListener<BrokerAvailabilityEvent> {

	private static final int DEFAULT_AD_GROUP_ID = 101;

	private final MessageSendingOperations<String> messagingTemplate;

	@Autowired
	public RandomDataGenerator(final MessageSendingOperations<String> messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@Override
	public void onApplicationEvent(final BrokerAvailabilityEvent event) {
	}

	@Autowired
	BudgetServiceClient budgetServiceClient;

	@Scheduled(fixedDelay = 1000)
	public void sendDataUpdates() {
		ChartDTO dto = budgetServiceClient.poll(DEFAULT_AD_GROUP_ID);

		this.messagingTemplate.convertAndSend("/data", dto);
	}
}