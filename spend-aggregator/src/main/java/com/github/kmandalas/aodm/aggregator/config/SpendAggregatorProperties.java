package com.github.kmandalas.aodm.aggregator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "kstream.spend.aggregator")
@Getter
@Setter
public class SpendAggregatorProperties {

	private String applicationId, kafkaBootstrapServers, sourceTopic, outputTopic, stateDirConfig;

	private long windowSize;
}
