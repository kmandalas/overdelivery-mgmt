package com.github.kmandalas.aodm.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.github.kmandalas.aodm.inventory.client.BudgetServiceClient;
import com.github.kmandalas.aodm.inventory.domain.Insertion;
import com.github.kmandalas.aodm.inventory.transport.AccountDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RefreshScope
public class InventoryServiceImpl implements InventoryService {

	private final BudgetServiceClient budgetServiceClient;

	private final MessageService messageService;

	@Value("${impression-rate}")
	private Double impressionRate;

	@Value("${action-rate}")
	private Double actionRate;

	@Autowired
	public InventoryServiceImpl(BudgetServiceClient budgetServiceClient, MessageService messageService) {
		this.budgetServiceClient = budgetServiceClient;
		this.messageService = messageService;
	}

	@Override
	public boolean insertAd(final int adGroupId, final String domain) {
		final AccountDTO dto = budgetServiceClient.getAccount(adGroupId);

		if (dto.getActualSpend() + dto.getInFlightSpend() > dto.getDailyBudget()) {
			return false;
		} else {
			double inFlightSpend = dto.getItemPrice() * impressionRate * actionRate;
			Insertion message = Insertion.builder().adGroupId(adGroupId).inFlightSpend(inFlightSpend).build();

			messageService.doSend(message);
		}

		return true;
	}
}
