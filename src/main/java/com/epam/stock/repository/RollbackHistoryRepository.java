package com.epam.stock.repository;

import com.epam.stock.entity.RollbackHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RollbackHistoryRepository extends JpaRepository<RollbackHistoryEntity, Long> {

  RollbackHistoryEntity findByCustomerIdAndOrderId(long customerId, long orderId);
}
