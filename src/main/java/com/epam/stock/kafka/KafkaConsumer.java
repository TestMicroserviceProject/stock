package com.epam.stock.kafka;

import com.epam.stock.dto.OrderDto;
import com.epam.stock.dto.ResultDto;
import com.epam.stock.dto.ResultDto.Check;
import com.epam.stock.dto.ResultDto.Service;
import com.epam.stock.dto.RollbackDto;
import com.epam.stock.entity.RollbackHistoryEntity;
import com.epam.stock.repository.RollbackHistoryRepository;
import com.epam.stock.service.HistoryServcie;
import com.epam.stock.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

  private final ObjectMapper objectMapper;
  private final ItemService itemService;
  private final HistoryServcie historyServcie;
  private final ResultKafkaProducer kafkaProducer;
  private final RollbackHistoryRepository rollbackHistoryRepository;

  @SneakyThrows
  @KafkaListener(
      topics = "${spring.kafka.consumer.topic}",
      clientIdPrefix = "stock-request-client",
      groupId = "stock-request-group",
      containerFactory = "consumerFactory"
  )
  @Transactional
  public ResultDto consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
    final OrderDto orderDto = objectMapper.readValue(record.value(), OrderDto.class);
    final Long customerId = orderDto.getCustomerId();
    final Long orderId = orderDto.getOrderId();
    final RollbackHistoryEntity rollback = rollbackHistoryRepository
        .findByCustomerIdAndOrderId(customerId, orderId);
    ResultDto resultDto;
    if (rollback == null) {
      final boolean enough = itemService.enoughItems(orderDto);
      if (enough) {
        resultDto = new ResultDto(orderDto, Check.SUCCESS, Service.STOCK);
        itemService.updateStock(orderDto);
        historyServcie.add(orderDto);
      } else {
        resultDto = new ResultDto(orderDto, Check.FAIL, Service.STOCK);
      }
      kafkaProducer.send(resultDto);
    } else {
      resultDto = new ResultDto(orderDto, Check.CANCELLED, Service.STOCK);
    }
    acknowledgment.acknowledge();
    return resultDto;
  }

  @SneakyThrows
  @KafkaListener(
      topics = "${spring.kafka.consumer.rollback.topic}",
      clientIdPrefix = "stock-rollback-client",
      groupId = "stock-rollback-group",
      containerFactory = "consumerFactory"
  )
  @Transactional
  public void rollback(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
    final RollbackDto rollbackDto = objectMapper.readValue(record.value(), RollbackDto.class);
    historyServcie.rollback(rollbackDto);
    acknowledgment.acknowledge();
  }
}
