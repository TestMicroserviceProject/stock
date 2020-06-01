package com.epam.stock.service;

import com.epam.stock.dto.OrderDto;
import com.epam.stock.dto.RollbackDto;
import com.epam.stock.entity.HistoryEntity;
import java.util.List;

public interface HistoryServcie {

  void add(OrderDto orderDto);

  void rollback(RollbackDto rollbackDto);

  List<HistoryEntity> findByCustomerIdAndOrderId(Long customerId, Long orderId);

}
