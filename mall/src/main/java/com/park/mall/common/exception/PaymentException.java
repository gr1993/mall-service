package com.park.mall.common.exception;

public class PaymentException extends RuntimeException {
    public PaymentException() {
        super("Payment processing error occurred.");
    }

    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentException(Throwable cause) {
        super(cause);
    }
}
