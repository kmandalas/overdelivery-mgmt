package com.github.kmandalas.aodm.impressions.producer;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.kmandalas.aodm.impressions.producer.domain.Impression;
import com.github.kmandalas.aodm.impressions.producer.service.MessageService;

@SpringBootApplication
public class ImpressionsProducerApp implements CommandLineRunner {

	@Autowired
	MessageService messageService;

	public static void main(String[] args) {
		SpringApplication.run(ImpressionsProducerApp.class, args);
	}

	@Override
	public void run(final String... strings) throws Exception {

		final int[] adGroupIds = {100, 101, 102, 103, 104};

		ExecutorService executorService = Executors.newFixedThreadPool(2);

		Runnable task1 = () -> sendImpressionsEventToTopic(adGroupIds, 1, 50);
		Runnable task2 = () -> sendImpressionsEventToTopic(adGroupIds, 1, 50);

		executorService.submit(task1);
		executorService.submit(task2);

		executorService.shutdown();
	}

	private void sendImpressionsEventToTopic(int[] adGroupIds, int idStart, int idEnd) {
		for (int i = idStart; i <= idEnd; i++) {
			final Impression impression = Impression.builder()
					.adGroupId(adGroupIds[i % adGroupIds.length])
					.adId(new Random().nextInt())
					.domain("pinterest.gr")
					.clicked(true)
					.build();

			try {
				TimeUnit.SECONDS.sleep(1);
				messageService.save(impression);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}

