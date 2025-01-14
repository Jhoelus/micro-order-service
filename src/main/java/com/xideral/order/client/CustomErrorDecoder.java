package com.xideral.order.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xideral.order.dto.GenericResponse;
import com.xideral.order.exception.BadRequestException;
import com.xideral.order.exception.NoFoundException;
import com.xideral.order.exception.UserNoFoundException;
import com.xideral.order.exception.UserServiceUnavailable;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    private ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Feign client error http {} reason {}, body {}", response.status(), response.reason(), response.body());
        GenericResponse<String> message = null;

        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, GenericResponse.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }

        if(response.status() > 500) {
            throw new UserServiceUnavailable(response.reason());
        }

        switch (response.status()) {
            case 400:
                return new BadRequestException(message.getError() != null && !message.getError().isEmpty()? String.join("\n", message.getError()) : "Bad Request");
            case 404:
                return new NoFoundException(message.getError() != null && !message.getError().isEmpty()? String.join("\n", message.getError())  : "Not found");
            default:
                return errorDecoder.decode(methodKey, response);
        }
    }
}

