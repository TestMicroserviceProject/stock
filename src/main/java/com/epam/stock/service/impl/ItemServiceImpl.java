package com.epam.stock.service.impl;

import com.epam.stock.dto.ItemDto;
import com.epam.stock.dto.OrderDto;
import com.epam.stock.entity.ItemEntity;
import com.epam.stock.repository.ItemRepository;
import com.epam.stock.service.ItemService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

  private final ItemRepository repository;

  @Override
  public boolean enoughItems(OrderDto order) {
    boolean result = true;
    for (ItemDto itemDto : order.getItemDtos()) {
      final Long itemId = itemDto.getItemId();
      final Optional<ItemEntity> item = repository.findById(itemId);
      final Integer stockAmount = item.get().getAmount();
      final int orderAmount = itemDto.getAmount();
      final int compare = Integer.compare(stockAmount, orderAmount);
      if (compare == -1) {
        result = false;
      }
    }
    return result;
  }

  @Override
  public void updateStock(OrderDto orderDto) {
    orderDto.getItemDtos().forEach(this::updateItem);
  }

  private void updateItem(ItemDto itemDto) {
    final Optional<ItemEntity> item = repository.findById(itemDto.getItemId());
    final int amount = itemDto.getAmount();
    final ItemEntity itemEntity = item.get();
    itemEntity.setAmount(itemEntity.getAmount() - amount);
    repository.save(itemEntity);
  }
}
