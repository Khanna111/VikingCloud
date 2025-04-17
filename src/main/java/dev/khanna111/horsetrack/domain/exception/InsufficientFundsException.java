package dev.khanna111.horsetrack.domain.exception;

import lombok.Getter;

@Getter
public class InsufficientFundsException extends RuntimeException {
    int payoutAmount;
    public InsufficientFundsException(int payoutAmount, String message) {
        super(message);
        this.payoutAmount = payoutAmount;
    }
}
