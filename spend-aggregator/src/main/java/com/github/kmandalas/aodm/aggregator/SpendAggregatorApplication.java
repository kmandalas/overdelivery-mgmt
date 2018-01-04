package com.github.kmandalas.aodm.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpendAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpendAggregatorApplication.class, args);
	}
}
