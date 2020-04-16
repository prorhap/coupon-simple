package com.github.prorhap.coupon.simple.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    private ObjectMapper objectMapper;

    @Inject
    public GlobalControllerExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler({ServletRequestBindingException.class, HttpMessageNotReadableException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage handleBadRequestException(Exception e) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.name(), e.getMessage(), e);
        logger.error("Bad Request Error", e);
        return errorMessage;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage handleRestClientException(HttpClientErrorException e) {
        Map<String, String> errorMsgMap = null;
        try {
            errorMsgMap = objectMapper.readValue(e.getResponseBodyAsString(), new TypeReference<HashMap<String, String>>() {
            });
        } catch (IOException ioe) {
            logger.error("error occurs during parse error message!", ioe);
            return new ErrorMessage("999", e.getResponseBodyAsString(), e);
        }
        return new ErrorMessage("999", errorMsgMap.get("message"), e);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage handleRuntimeException(RuntimeException e) {
        logger.error("Internal Server Error", e);
        return new ErrorMessage("999", e.getMessage(), e);
    }

    public static class ErrorMessage {

        private static final String IS_DEBUG_PARAMETER_NAME = "debug";
        private static final String EMPTY_STRING = "";
        private String code;
        private String error;
        private String message;

        public ErrorMessage(String code, String error, Throwable t) {
            this.code = code;
            this.error = error;
            this.message = Throwables.getStackTraceAsString(t);

        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }
    }
}