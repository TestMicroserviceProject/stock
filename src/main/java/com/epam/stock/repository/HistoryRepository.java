package com.epam.stock.repository;

import com.epam.stock.entity.HistoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {

  List<HistoryEntity> findAllByCustomerIdAndOrderId(long clientId, long orderId);


}
