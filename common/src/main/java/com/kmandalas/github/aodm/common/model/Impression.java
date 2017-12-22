package com.kmandalas.github.aodm.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Impression {

	Integer adGroupId;
	Integer adId;
	String domain;
	boolean clicked;
}
