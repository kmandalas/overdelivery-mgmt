package com.github.kmandalas.aodm.budget.transport;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountDTO {

	private Integer id;

	private Integer adGroupId;

	private String adGroupName;

	private Double actualSpend;

	private Double inFlightSpend;

	private Double dailyBudget;

	private Double itemPrice;
}
