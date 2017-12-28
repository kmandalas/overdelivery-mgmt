package com.github.kmandalas.aodm.budget.service;

import java.util.List;

import org.springframework.data.util.Pair;

import com.github.kmandalas.aodm.budget.domain.Account;

public interface AccountService {

	List<Account> findEligible();

	Account findByAdGroupId(int adGroupId);

	void updateActualSpend(int adGroupId);

	void updateInFlightSpend(int adGroupId, double amount);

	Pair<Double, Double> poll(int adGroupId);
}
