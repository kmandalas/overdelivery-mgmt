package com.github.kmandalas.aodm.predictions.consumer.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Insertion {

	Integer adGroupId;
	Double inFlightSpend;
}
