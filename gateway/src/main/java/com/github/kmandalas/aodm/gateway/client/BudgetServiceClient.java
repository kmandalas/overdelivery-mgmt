package com.github.kmandalas.aodm.gateway.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.kmandalas.aodm.gateway.transport.ChartDTO;

@FeignClient(name = "budget-service")
public interface BudgetServiceClient {

	@RequestMapping(method = RequestMethod.GET, value = "/budget/poll/{adGroupId}")
	ChartDTO poll(@PathVariable(name = "adGroupId") int adGroupId);
}
