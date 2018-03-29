package com.github.kmandalas.aodm.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.kmandalas.aodm.inventory.service.InventoryService;
import com.github.kmandalas.aodm.inventory.transport.InsertionDTO;

import lombok.extern.slf4j.Slf4j;

@RestController("/inventory")
@Slf4j
public class InventoryController {

	private final InventoryService inventoryService;

	@Autowired
	public InventoryController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public boolean insertAd(@RequestBody InsertionDTO dto) {
		log.info("Requesting ad insertion for: " + dto);

		return inventoryService.insertAd(dto.getAdGroupId(), dto.getDomain());
	}
}
