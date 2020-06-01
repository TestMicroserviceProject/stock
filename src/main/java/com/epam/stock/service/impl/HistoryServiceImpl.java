package com.epam.stock.service.impl;

import com.epam.stock.dto.OrderDto;
import com.epam.stock.dto.RollbackDto;
import com.epam.stock.entity.HistoryEntity;
import com.epam.stock.entity.ItemEntity;
import com.epam.stock.entity.RollbackHistoryEntity;
import com.epam.stock.repository.HistoryRepository;
import com.epam.stock.repository.ItemRepository;
import com.epam.stock.repository.RollbackHistoryRepository;
import com.epam.stock.service.HistoryServcie;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryServcie {

  private final HistoryRepository historyRepository;
  private final ItemRepository itemRepository;
  private final RollbackHistoryRepository rollbackHistoryRepository;

  @Override
  public void add(OrderDto orderDto) {
    final List<HistoryEntity> entities = HistoryEntity.createList(orderDto);
    historyRepository.saveAll(entities);
  }

  @Override
  public void rollback(RollbackDto rollbackDto) {
    var clientId = rollbackDto.getClientId();
    var orderId = rollbackDto.getOrderId();
    final RollbackHistoryEntity rollbackHistoryEntity = new RollbackHistoryEntity();
    rollbackHistoryEntity.setCustomerId(clientId);
    rollbackHistoryEntity.setOrderId(orderId);
    rollbackHistoryRepository.save(rollbackHistoryEntity);
    var historyEntities = historyRepository.findAllByCustomerIdAndOrderId(clientId, orderId);
    if (historyEntities != null) {
      historyRepository.deleteAll(historyEntities);
      historyEntities.forEach(this::rollback);
    }
  }

  @Override
  public List<HistoryEntity> findByCustomerIdAndOrderId(Long customerId, Long orderId) {
    return historyRepository.findAllByCustomerIdAndOrderId(customerId, orderId);
  }

  private void rollback(HistoryEntity entity) {
    final long itemId = entity.getItemId();
    final Optional<ItemEntity> item = itemRepository.findById(itemId);
    item.ifPresent(itemEntity -> itemEntity.setAmount(itemEntity.getAmount() + entity.getAmount()));
  }
}
