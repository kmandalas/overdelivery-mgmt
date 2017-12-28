package com.github.kmandalas.aodm.budget.service;

import java.util.List;

import com.github.kmandalas.aodm.budget.domain.Account;
import com.github.kmandalas.aodm.budget.domain.AmountsOnly;

public interface AccountService {

	List<Account> findEligible();

	Account findByAdGroupId(int adGroupId);

	void updateActualSpend(int adGroupId);

	void updateInFlightSpend(int adGroupId, double amount);

	AmountsOnly poll(int adGroupId);
}
