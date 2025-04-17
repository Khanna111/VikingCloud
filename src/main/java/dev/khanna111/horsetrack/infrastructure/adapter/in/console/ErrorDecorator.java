package dev.khanna111.horsetrack.infrastructure.adapter.in.console;

import dev.khanna111.horsetrack.domain.exception.InsufficientFundsException;
import dev.khanna111.horsetrack.domain.exception.InvalidBetException;
import dev.khanna111.horsetrack.domain.exception.InvalidCommandException;
import dev.khanna111.horsetrack.domain.exception.InvalidHorseException;
import org.springframework.stereotype.Component;

@Component
public class ErrorDecorator {

    public void displayError(InvalidCommandException e) {
        StringBuilder display = new StringBuilder();
        display.append("Invalid command: ")
                .append(e.getUnknownCommand());
        System.out.println(display.toString());
    }

    public void displayError(InvalidHorseException e) {
        StringBuilder display = new StringBuilder();
        display.append("Invalid Horse Number: ")
                .append(e.getInvalidHorseNumber());
        System.out.println(display.toString());
    }

    public void displayError(InvalidBetException e) {
        StringBuilder display = new StringBuilder();
        display.append("Invalid Bet: ")
                .append(e.getInvalidBet());
        System.out.println(display.toString());
    }

    public void displayError(InsufficientFundsException e) {
        StringBuilder display = new StringBuilder();
        display.append("Insufficient Funds: ")
                .append(e.getPayoutAmount());
        System.out.println(display.toString());
    }


}
