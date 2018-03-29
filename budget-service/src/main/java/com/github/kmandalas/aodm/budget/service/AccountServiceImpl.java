package com.github.kmandalas.aodm.budget.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.kmandalas.aodm.budget.domain.Account;
import com.github.kmandalas.aodm.budget.domain.AmountsOnly;
import com.github.kmandalas.aodm.budget.repository.AccountRepository;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private final AccountRepository repository;

	@Autowired
	public AccountServiceImpl(AccountRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Account> findEligible() {
		return repository.findEligible();
	}

	@Override
	public Account findByAdGroupId(final int adGroupId) {
		return repository.findOneByAdGroupId(adGroupId);
	}

	@Override
	public void updateActualSpend(final int adGroupId) {
		Account account = repository.findOneByAdGroupId(adGroupId);
		account.increaseActual(account.getItemPrice());

		repository.save(account);
	}

	@Override
	public void updateInFlightSpend(final int adGroupId, final double amount) {
		Account account = repository.findOneByAdGroupId(adGroupId);
		account.increaseInflight(amount);

		repository.save(account);
	}

	@Override
	public AmountsOnly poll(final int adGroupId) {
		return repository.findByAdGroupId(adGroupId);
	}
}
