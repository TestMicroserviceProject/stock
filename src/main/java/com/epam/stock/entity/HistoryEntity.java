package com.epam.stock.entity;

import com.epam.stock.dto.OrderDto;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_history_seq")
  @SequenceGenerator(name = "stock_history_seq", sequenceName = "stock_history_id_seq", allocationSize = 1)
  private Long id;
  private Long customerId;
  private Long orderId;
  private Long itemId;
  private Integer amount;

  public static List<HistoryEntity> createList(OrderDto orderDto) {
    final Long customerId = orderDto.getCustomerId();
    final Long orderId = orderDto.getOrderId();
    return orderDto.getItemDtos()
        .stream()
        .map(item -> new HistoryEntity().builder()
            .customerId(customerId)
            .orderId(orderId)
            .itemId(item.getItemId())
            .amount(item.getAmount())
            .build())
        .collect(Collectors.toList());
  }
}
