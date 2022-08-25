package com.meli.shows.exception;

import lombok.experimental.StandardException;

@StandardException
public class NotFoundException extends RuntimeException {

    public NotFoundException(String msg) {
        super(msg);
    }
}
