package dev.khanna111.horsetrack.infrastructure.adapter.in.console;

import dev.khanna111.horsetrack.application.dto.InventoryDTO;
import dev.khanna111.horsetrack.domain.exception.InsufficientFundsException;
import dev.khanna111.horsetrack.domain.exception.InvalidBetException;
import dev.khanna111.horsetrack.domain.exception.InvalidCommandException;
import dev.khanna111.horsetrack.domain.exception.InvalidHorseException;
import dev.khanna111.horsetrack.infrastructure.adapter.in.error.QuitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class CommandLineConsole implements CommandLineRunner {

    private final CommandProcessor commandProcessor;
    private final DisplayDecorator displayDecorator;
    private final ErrorDecorator errorDecorator;

    @Override
    public void run(String... args) throws Exception {
        for (String arg : args) {
            log.info("Argument: {}", arg);
        }

        displayDecorator.displayInventory(commandProcessor.getInventoryItems());
        displayDecorator.displayHorses(commandProcessor.getHorses());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // System.out.print("Enter command: ");
            String input = scanner.nextLine();

            try {
                processCommand(input);
            } catch (InvalidCommandException e) {
                log.error("Invalid command: {}", e);
                errorDecorator.displayError(e);
            } catch (InvalidHorseException e) {
                log.error("Invalid Horse: {}", e);
                errorDecorator.displayError(e);
            } catch (InvalidBetException e) {
                log.error("Invalid Bet: {}", e);
                errorDecorator.displayError(e);
            } catch (QuitException e) {
                log.info("Quiting application");
                break;
            } catch (InsufficientFundsException e) {
                log.error("Insufficient Funds: {}", e);
                errorDecorator.displayError(e);
            } catch (Exception e) {
                log.error("Error processing command: {}", e.getMessage());
            }
            log.info("Received command: {}", input);
        }
    }

    /**
     * 'R' or 'r' - restocks the cash inventory
     * 'Q' or 'q' - quits the application
     * 'W' or 'w' [1-7] - sets the winning horse number
     * [1-7] <amount> - specifies the horse wagered on and the amount of the bet
     *
     * @param originalInput
     */
    private void processCommand(String originalInput) {
        log.info("Processing input: {}", originalInput);
        String input = originalInput.trim().toUpperCase();
        if (input.length() == 0) {
            return; // ignore blank lines
        }
        String[] parts = input.split(" ");
        String command = parts[0];
        switch (command) {
            case "R":
                if (parts.length == 1) {
                    commandProcessor.restockInventory();
                    displayDecorator.displayInventoryAndHorses(commandProcessor.getInventoryItems(), commandProcessor.getHorses());
                } else {
                    commandProcessor.invalidCommand(originalInput);
                }
                break;
            case "Q":
                commandProcessor.quit();
                break;
            case "W":
                // if (parts.length == 2 && Pattern.matches("[-+]?\\d+", parts[1])) {
                if (parts.length == 2) {
                    int horseId = getHorseId(parts[1]);
                    commandProcessor.setWinningHorse(horseId);
                    displayDecorator.displayInventoryAndHorses(commandProcessor.getInventoryItems(), commandProcessor.getHorses());
                } else {
                    commandProcessor.invalidCommand(originalInput);
                }
                break;
            default:
                // Assuming ids are >= 0
                // if (Pattern.matches("[-+]?\\d+ [-+]?\\d+", input)) {
                if (Pattern.matches("[-+]?\\d+", command)) {
                    int horseIdArg = getHorseId(command);
                    String horseName = commandProcessor.getHorseNameById(horseIdArg);
                    int amount = getWagerAmount(parts[1]);
                    List<InventoryDTO> inventoryToDispenseList = commandProcessor.wager(horseIdArg, amount);
                    if (inventoryToDispenseList.size() > 0) {
                        int winning = inventoryToDispenseList.stream()
                                .map(inventoryDTO -> inventoryDTO.getDenomination() * inventoryDTO.getQuantity())
                                .reduce(0, Integer::sum);
                        displayDecorator.displayWinningInventory(inventoryToDispenseList, horseName, winning);
                    } else {
                        displayDecorator.displayLosingHorse(horseName);
                    }
                    displayDecorator.displayInventoryAndHorses(commandProcessor.getInventoryItems(), commandProcessor.getHorses());
                    break;
                }
                commandProcessor.invalidCommand(originalInput);
        }
    }

    private int getHorseId(String idString) throws InvalidHorseException {

        try {
            int horseIdArg = Integer.valueOf(idString);
            boolean isValidHorseId = commandProcessor.getHorses().stream()
                    .anyMatch(horseDTO -> horseDTO.getId() == horseIdArg);
            if (!isValidHorseId) commandProcessor.invalidHorseId(idString);
            return horseIdArg;
        } catch (NumberFormatException e) {
            commandProcessor.invalidHorseId(idString);
        }
        return -1; // never executed
    }


    private int getWagerAmount(String amountString) throws InvalidBetException {
        try {
            int amount = Integer.valueOf(amountString);
            if (amount <= 0) {
                commandProcessor.invalidBet(amountString);
            }
            return amount;
        } catch (NumberFormatException e) {
            commandProcessor.invalidBet(amountString);
        }
        return -1; // never executed
    }
}