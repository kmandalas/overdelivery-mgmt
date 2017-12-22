package com.github.kmandalas.aodm.impressions.producer.service;

import com.github.kmandalas.aodm.impressions.producer.domain.Impression;

public interface MessageService {

	void save(Impression message) throws Exception;

}
