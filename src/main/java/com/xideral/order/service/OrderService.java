package com.xideral.order.service;

import com.xideral.order.dto.GenericResponse;
import com.xideral.order.dto.OrderDto;
import com.xideral.order.dto.OrderRequestDto;
import com.xideral.order.dto.UpdateOrderRequestDto;

import java.util.List;

public interface OrderService {
    GenericResponse<OrderDto> createOrder(OrderRequestDto user);

    GenericResponse<OrderDto> getOrderById(Long id);

    GenericResponse<List<OrderDto>> getOrderByUserId(Long id);

    GenericResponse<OrderDto> updateOrder(Long id, UpdateOrderRequestDto user);

    void deleteOrder(Long id);
}
