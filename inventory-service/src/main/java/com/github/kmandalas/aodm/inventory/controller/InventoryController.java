package com.github.kmandalas.aodm.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.kmandalas.aodm.inventory.service.InventoryService;
import com.github.kmandalas.aodm.inventory.transport.InsertionDTO;

import lombok.extern.slf4j.Slf4j;

@RestController("/inventory")
@Slf4j
public class InventoryController {

	@Autowired
	InventoryService inventoryService;

	@PostMapping
	public boolean insertAd(@RequestBody InsertionDTO dto) {
		log.info("Requesting ad insertion for: " + dto);

		return inventoryService.insertAd(dto.getAdGroupId(), dto.getDomain());
	}
}
