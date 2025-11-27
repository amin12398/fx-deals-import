package com.example.fxdeal.exception;

public class DuplicateDealException extends RuntimeException {

    public DuplicateDealException(String dealId) {
        super("Duplicate deal: " + dealId);
    }


    public DuplicateDealException(String dealId, Throwable cause) {
        super("Duplicate deal: " + dealId, cause);
    }
}
