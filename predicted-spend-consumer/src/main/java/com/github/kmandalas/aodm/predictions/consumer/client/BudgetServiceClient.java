package com.github.kmandalas.aodm.predictions.consumer.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "budget-service")
public interface BudgetServiceClient {
	
	@RequestMapping(method = RequestMethod.PUT, value = "/budget/inFlight/{adGroupId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	void updateInflightSpend(@PathVariable(name = "adGroupId") int adGroupId, @RequestBody double amount);
}
