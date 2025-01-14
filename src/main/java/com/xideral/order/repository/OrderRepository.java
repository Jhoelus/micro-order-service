package com.xideral.order.repository;

import com.xideral.order.model.OrderEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

    List<OrderEntity> findByUserId(Long userID);
}
