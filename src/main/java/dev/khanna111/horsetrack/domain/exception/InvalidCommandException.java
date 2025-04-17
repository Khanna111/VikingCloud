package dev.khanna111.horsetrack.domain.exception;

import lombok.Getter;

@Getter
public class InvalidCommandException extends RuntimeException {
    private String unknownCommand;
    public InvalidCommandException(String unknownCommand, String message) {
        super(message);
        this.unknownCommand = unknownCommand;
    }
}
