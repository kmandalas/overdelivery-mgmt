package com.github.kmandalas.aodm.inventory;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class InventoryApplicationTests {

	private static final String DEFAULT_TOPIC = "ad-insertion-input-test";
	private static final String SPRING_CLOUD_BUS_TOPIC = "springCloudBus-0";

	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, DEFAULT_TOPIC, SPRING_CLOUD_BUS_TOPIC);

	@Test
	public void contextLoads() {
	}
}
