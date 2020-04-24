package com.epam.stock.service;


import com.epam.stock.dto.OrderDto;

public interface ItemService {

  boolean enoughItems(OrderDto order);

}
