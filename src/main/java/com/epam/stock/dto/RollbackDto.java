package com.epam.stock.dto;

import lombok.Data;

@Data
public class RollbackDto {

  private final Long clientId;
  private final Long orderId;
}
