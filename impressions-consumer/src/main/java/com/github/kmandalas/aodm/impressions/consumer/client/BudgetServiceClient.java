package com.github.kmandalas.aodm.impressions.consumer.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "budget-service")
public interface BudgetServiceClient {

	// @PostMapping(value = "/budget/actual/{adGroupId}")
	@RequestMapping(method = RequestMethod.PUT, value = "/budget/actual/{adGroupId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	void updateActualSpend(@PathVariable(name = "adGroupId") int adGroupId);
}
