package com.github.kmandalas.aodm.inventory.controller;

import com.github.kmandalas.aodm.inventory.service.InventoryService;
import com.github.kmandalas.aodm.inventory.transport.InsertionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class InventoryController {

  public static final String CONTROLLER_PATH = "/inventory";

  private final InventoryService inventoryService;

  @Autowired
  public InventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @PostMapping(path = CONTROLLER_PATH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
          produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public boolean insertAd(@RequestBody InsertionDTO dto) {
    log.info("Requesting ad insertion for: " + dto);

    return inventoryService.insertAd(dto.getAdGroupId(), dto.getDomain());
  }

}
