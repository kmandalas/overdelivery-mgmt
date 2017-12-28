package com.github.kmandalas.aodm.budget.transport;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ChartDTO {

	private int adGroupId;
	private double actual;
	private double predicted;
}
