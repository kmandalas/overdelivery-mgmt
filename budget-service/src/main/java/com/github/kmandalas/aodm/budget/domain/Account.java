package com.github.kmandalas.aodm.budget.domain;

import lombok.Builder;
import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "accounts")
@Data
@Builder
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
