package com.xideral.order.controller;

import com.xideral.order.dto.GenericResponse;
import com.xideral.order.dto.OrderRequestDto;
import com.xideral.order.dto.UpdateOrderRequestDto;
import com.xideral.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create order", description = "Create order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Incorrect order data")
    })
    @PostMapping
    public ResponseEntity<GenericResponse> createOrder(@Valid @RequestBody OrderRequestDto order) {
        return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<GenericResponse> getOrdersByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderByUserId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> updateOrder(@PathVariable Long id, @RequestBody UpdateOrderRequestDto order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
