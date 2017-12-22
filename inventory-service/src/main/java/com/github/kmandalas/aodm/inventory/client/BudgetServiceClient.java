package com.github.kmandalas.aodm.inventory.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.kmandalas.aodm.inventory.transport.AccountDTO;

@FeignClient(name = "budget-service")
public interface BudgetServiceClient {

	@RequestMapping(method = RequestMethod.GET, value = "/budget/{adGroupId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	AccountDTO getAccount(@PathVariable(name = "adGroupId") int adGroupId);
}
