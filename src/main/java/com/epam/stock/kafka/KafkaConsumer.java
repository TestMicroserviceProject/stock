package com.epam.stock.kafka;

import com.epam.stock.dto.Check;
import com.epam.stock.dto.OrderDto;
import com.epam.stock.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

  private final ObjectMapper objectMapper;
  private final ItemService itemService;
  private final ResultKafkaProducer kafkaProducer;

  @SneakyThrows
  @KafkaListener(
      topics = "${spring.kafka.consumer.topic}",
      groupId = "${spring.kafka.consumer.group-id}",
      containerFactory = "consumerFactory"
  )
  public Check consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
    final OrderDto orderDto = objectMapper.readValue(record.value(), OrderDto.class);
    final boolean enough = itemService.enoughItems(orderDto);
    Check check = enough ? Check.SUCCESS : Check.FAILED;
    kafkaProducer.send(check);
    acknowledgment.acknowledge();
    return check;
  }
}
