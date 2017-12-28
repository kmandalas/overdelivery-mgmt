package com.github.kmandalas.aodm.budget.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.kmandalas.aodm.budget.domain.Account;
import com.github.kmandalas.aodm.budget.service.AccountService;
import com.github.kmandalas.aodm.budget.transport.AccountDTO;
import com.github.kmandalas.aodm.budget.transport.ChartDTO;

import lombok.extern.slf4j.Slf4j;

@RestController("/budget")
@Slf4j
public class BudgetController {

	@Autowired
	AccountService accountService;

	@GetMapping("/eligible")
	public List<Account> getEligible() {
		log.info("Getting the eligible as groups...");

		return accountService.findEligible();
	}

	@GetMapping("/{adGroupId}")
	public AccountDTO getAccount(@PathVariable int adGroupId) {
		log.info("Getting account info fot ad-group: " + adGroupId);

		final Account account = accountService.findByAdGroupId(adGroupId);
		final AccountDTO dto = new AccountDTO();
		BeanUtils.copyProperties(account, dto);

		return dto;
	}

	@GetMapping("/poll/{adGroupId}")
	public ChartDTO poll(@PathVariable int adGroupId) {
		// todo...
		return null;
	}

	// @PutMapping("/actual/{adGroupId}/") | TODO: check if this works and if yes apply it everywhere
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/actual/{adGroupId}", method = RequestMethod.PUT)
	public void updateActualSpend(@PathVariable int adGroupId) {
		log.info("updating actual spend for ad-group-id: " + adGroupId);

		accountService.updateActualSpend(adGroupId);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/inFlight/{adGroupId}", method = RequestMethod.PUT)
	public void updateInflightSpend(@PathVariable int adGroupId, @RequestBody double amount) {
		log.info("updating in-flight spend for ad-group-id:: " + adGroupId);

		accountService.updateInFlightSpend(adGroupId, amount);
	}
}
