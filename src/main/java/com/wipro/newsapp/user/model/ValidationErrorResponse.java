package com.wipro.newsapp.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ValidationErrorResponse {
    private final Map<String, String> message;

    private final HttpStatus status;

}
