package com.github.kmandalas.aodm.impressions.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.github.kmandalas.aodm.impressions.consumer.domain.Impression;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableKafka
@Configuration
public class ImpressionsConsumerApplication implements CommandLineRunner {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

//	@Value("${consumer.group.id}")
//	private String consumerGroupId;

	public static void main(String[] args) {
		SpringApplication.run(ImpressionsConsumerApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {

	}

	@Bean
	public ConsumerFactory<String, Impression> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		// props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		DefaultKafkaConsumerFactory<String, Impression> consumerFactory = new DefaultKafkaConsumerFactory<>(props);
		consumerFactory.setValueDeserializer(new JsonDeserializer<>(Impression.class));

		return consumerFactory;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Impression> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Impression> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		containerFactory.setConsumerFactory(consumerFactory());

		return containerFactory;
	}
}