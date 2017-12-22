package com.github.kmandalas.aodm.inventory.service;

import com.github.kmandalas.aodm.inventory.domain.Insertion;

public interface MessageService {

	void doSend(Insertion message);
}
