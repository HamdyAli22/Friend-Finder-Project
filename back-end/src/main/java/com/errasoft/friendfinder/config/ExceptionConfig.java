package com.errasoft.friendfinder.config;

import com.errasoft.friendfinder.helper.BundleMessage;
import com.errasoft.friendfinder.service.bundlemessage.BundleMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@ControllerAdvice
public class ExceptionConfig {

    @Autowired
    private BundleMessageService bundleMessageService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BundleMessage> handleGenericException(Exception exception){
        String key = exception.getMessage();
        BundleMessage bundleMessage = bundleMessageService.getMessageArEn(key);
        return ResponseEntity.badRequest().body(bundleMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<BundleMessage>> handleValidationException(MethodArgumentNotValidException exception) {

        List<BundleMessage> bundleMessages = new ArrayList<>();

        Class<?> targetClass = exception.getBindingResult().getTarget().getClass();

        List<String> fieldOrder = Arrays.stream(targetClass.getDeclaredFields())
                .map(Field::getName)
                .toList();

        List<FieldError> sortedErrors = exception.getBindingResult().getFieldErrors().stream()
                .sorted(Comparator.comparingInt(f -> {
                    int idx = fieldOrder.indexOf(f.getField());
                    return idx == -1 ? Integer.MAX_VALUE : idx; // لو الحقل مش موجود نحطه في الآخر
                }))
                .toList();

        sortedErrors.forEach(fieldError -> {
            //exceptionResponses.add(new ExceptionResponse(fieldError.getDefaultMessage ()));
            String key = fieldError.getDefaultMessage();
            BundleMessage bundleMessage = bundleMessageService.getMessageArEn(key);
            bundleMessages.add(bundleMessage);
        });

        return ResponseEntity.badRequest().body(bundleMessages);
    }
}
