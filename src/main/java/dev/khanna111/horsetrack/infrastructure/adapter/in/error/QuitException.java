package dev.khanna111.horsetrack.infrastructure.adapter.in.error;

public class QuitException extends RuntimeException {
    public QuitException(String message) {
        super(message);
    }
}
