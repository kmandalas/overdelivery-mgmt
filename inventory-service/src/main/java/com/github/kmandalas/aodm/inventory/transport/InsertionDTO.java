package com.github.kmandalas.aodm.inventory.transport;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class InsertionDTO {

  int adGroupId;
  String domain;
}
