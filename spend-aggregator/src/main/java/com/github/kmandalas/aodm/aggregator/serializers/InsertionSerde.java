package com.github.kmandalas.aodm.aggregator.serializers;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.github.kmandalas.aodm.aggregator.domain.Insertion;

public class InsertionSerde implements Serde {

	private final Serde<Insertion> inner;

	public InsertionSerde() {
		inner = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(Insertion.class));
	}

	@Override
	public void configure(final Map configs, final boolean isKey) {
		inner.serializer().configure(configs, isKey);
		inner.deserializer().configure(configs, isKey);
	}

	@Override
	public void close() {
		inner.serializer().close();
		inner.deserializer().close();
	}

	@Override
	public Serializer serializer() {
		return inner.serializer();
	}

	@Override
	public Deserializer deserializer() {
		return inner.deserializer();
	}
}
