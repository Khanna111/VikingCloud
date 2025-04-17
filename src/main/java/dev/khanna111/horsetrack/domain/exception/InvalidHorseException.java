package dev.khanna111.horsetrack.domain.exception;

import lombok.Getter;

@Getter
public class InvalidHorseException extends RuntimeException {
    String invalidHorseNumber;
    public InvalidHorseException(String unknownHorseNumber, String message) {
        super(message);
        this.invalidHorseNumber = unknownHorseNumber;
    }
}
