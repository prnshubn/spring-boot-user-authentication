package com.wipro.newsapp.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class InvalidEmailOrPasswordException extends RuntimeException {
    private final String message;

}
