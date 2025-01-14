package com.xideral.order.service.impl;

import com.xideral.order.client.UserClient;
import com.xideral.order.dto.*;
import com.xideral.order.exception.OrderNoFoundException;
import com.xideral.order.exception.UserNoFoundException;
import com.xideral.order.model.OrderEntity;
import com.xideral.order.repository.OrderRepository;
import com.xideral.order.service.OrderService;
import com.xideral.order.utils.OrderStatusEnum;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl  implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper mapper;
    private final UserClient userClient;

    @Override
    @Transactional
    public GenericResponse<OrderDto> createOrder(OrderRequestDto order) {
        UserDto responseClient = null;

        try {
            responseClient = userClient.getUserById(order.getUserId());
        }   catch (FeignException ex) {
            log.error(ex.getMessage());
        }

        if(Objects.isNull(responseClient)) {
            throw new UserNoFoundException("User not found");
        }

        var orderEntity = OrderEntity.builder().userId(order.getUserId()).total(order.getTotal()).build();
        orderEntity.setStatus(OrderStatusEnum.PENDING.name());

        var orderBDD = orderRepository.save(orderEntity);
        var orderResult = mapper.map(orderBDD, OrderDto.class);
        orderResult.setUser(responseClient);

        return GenericResponse.<OrderDto>builder().data(orderResult).message("Success").code("200").build();
    }


    @Override
    @Transactional(readOnly = true)
    public GenericResponse<OrderDto> getOrderById(Long id) {
        log.info("Get order by id:: {} ", id);
        return orderRepository.findById(id)
                .map(orderBDD -> {
                    var orderResult = mapper.map(orderBDD, OrderDto.class);
                    return GenericResponse.<OrderDto>builder().data(orderResult).message("Success").code("200").build();
                })
                .orElseThrow(() -> new OrderNoFoundException("Order not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<List<OrderDto>> getOrderByUserId(Long userId) {
        log.info("Get order to user by id:: {} ", userId);
        var ordersBDD = orderRepository.findByUserId(userId);

        if(ordersBDD.isEmpty()) {
            throw new UserNoFoundException("User not found");
        }
        List<OrderDto> orderResult = mapper.map(ordersBDD,  new TypeToken<List<OrderDto>>(){}.getType());
        return GenericResponse.<List<OrderDto>>builder().data(orderResult).message("Success").code("200").build();
    }

    @Override
    @Transactional
    public GenericResponse<OrderDto> updateOrder(Long id, UpdateOrderRequestDto orderRequest) {
        return orderRepository.findById(id)
                .map(orderBDD -> {
                    orderBDD.setTotal(Objects.nonNull(orderRequest.getTotal()) ? orderRequest.getTotal() : orderBDD.getTotal());
                    orderBDD.setStatus(Objects.nonNull(orderRequest.getStatus()) ? orderRequest.getStatus().name() : orderBDD.getStatus());
                    var newResult = orderRepository.save(orderBDD);
                    var responseClient =  userClient.getUserById(newResult.getUserId());
                    var orderResult = mapper.map(newResult, OrderDto.class);
                    orderResult.setUser(responseClient);
                    return GenericResponse.<OrderDto>builder().data(orderResult).message("Success").code("200").build();
                })
                .orElseThrow(() -> new OrderNoFoundException("Order not found"));
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        log.info("Deleting order by id:: {} ", id);
        orderRepository.findById(id).ifPresentOrElse(orderBDD -> {
            orderRepository.delete(orderBDD);
        }, () -> {
            throw new OrderNoFoundException("Order not found");
        });
    }
}
