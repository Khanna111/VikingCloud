package dev.khanna111.horsetrack.domain.exception;

import lombok.Getter;

@Getter
public class InvalidBetException extends RuntimeException {
    private String invalidBet;
    public InvalidBetException(String invalidBet, String message) {
        super(message);
        this.invalidBet = invalidBet;
    }
}
