package com.github.kmandalas.aodm.budget.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.kmandalas.aodm.budget.domain.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

	Account findByAdGroupId(int adGroupId);

	@Query("select a from Account a where a.dailyBudget >= a.actualSpend + a.inFlightSpend")
	List<Account> findEligible();

	@Modifying
	@Query("update Account a set a.inFlightSpend =  a.inFlightSpend + :amount where a.adGroupId = :adGroupId")
	int updateInFlightSpend(@Param("adGroupId") int adGroupId, @Param("amount") double amount);
}
