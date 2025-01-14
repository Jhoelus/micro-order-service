package com.xideral.order.client;

import com.xideral.order.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE", configuration = CustomErrorDecoder.class)
public interface UserClient {

    @GetMapping("/users/{userId}")
    UserDto getUserById(@PathVariable("userId") Long userId);

}
