package com.epam.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

  private Long itemId;

  private String itemName;

  private Integer amount;

  private Double price;
}
