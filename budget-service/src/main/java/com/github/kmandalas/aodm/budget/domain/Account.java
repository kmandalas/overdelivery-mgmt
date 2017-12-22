package com.github.kmandalas.aodm.budget.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import lombok.Data;

@Entity
@Table(name = "accounts")
@Data
@Audited
public class Account implements Serializable {

	private static final long serialVersionUID = 3726476090044799835L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(nullable = false, unique = true)
	private Integer adGroupId;

	@Column(nullable = false, unique = true)
	private String adGroupName;

	@Column
	private Double actualSpend;

	@Column
	private Double inFlightSpend;

	@Column(nullable = false)
	private Double dailyBudget;

	@Column(nullable = false)
	private Double itemPrice;

	public void increaseActual(double amt) {
		actualSpend += amt;
	}

	public void increaseInflight(double amt) {
		inFlightSpend += amt;
	}
}
