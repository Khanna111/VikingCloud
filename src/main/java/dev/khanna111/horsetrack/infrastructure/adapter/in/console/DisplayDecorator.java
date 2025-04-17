package dev.khanna111.horsetrack.infrastructure.adapter.in.console;

import dev.khanna111.horsetrack.application.dto.HorseDTO;
import dev.khanna111.horsetrack.application.dto.InventoryDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DisplayDecorator {

    public void displayInventoryAndHorses(List<InventoryDTO> inventoryItems, List<HorseDTO> horseItems) {
        displayInventory(inventoryItems);
        displayHorses(horseItems);
    }

    public void displayInventory(List<InventoryDTO> inventoryItems) {
        StringBuilder display = new StringBuilder();
        display.append("Inventory:\n");
        inventoryItems.stream()
                .sorted((item1, item2) -> Integer.compare(item1.getDenomination(), item2.getDenomination()))
                .forEach(item ->
                        display.append("$")
                                .append(item.getDenomination())
                                .append(",")
                                .append(item.getQuantity())
                                .append("\n")
                );
        display.deleteCharAt(display.length() - 1);
        System.out.println(display.toString());
    }

    public void displayHorses(List<HorseDTO> horseItems) {
        StringBuilder display = new StringBuilder();
        display.append("Horses:\n");
        horseItems.stream()
                .sorted((item1, item2) -> Integer.compare(item1.getId(), item2.getId()))
                .forEach(item ->
                        display.append("$")
                                .append(item.getId())
                                .append(",")
                                .append(item.getName())
                                .append(",")
                                .append(item.getOdds())
                                .append(",")
                                .append(item.isIfWonLastTime() ? "Won" : "Lost")
                                .append("\n")
                );
        display.deleteCharAt(display.length() - 1);
        System.out.println(display.toString());
    }

    public void displayWinningInventory(List<InventoryDTO> dispensedInventoryItems, String horseName, int totalWinning) {
        StringBuilder display = new StringBuilder();
        display.append("Payout: ")
                .append(horseName)
                .append(", ").
                append(totalWinning)
                .append("\n")
                .append("Dispensing:")
                .append("\n");

        dispensedInventoryItems.stream()
                .sorted((item1, item2) -> Integer.compare(item1.getDenomination(), item2.getDenomination()))
                .forEach(item ->
                        display.append("$")
                                .append(item.getDenomination())
                                .append(",")
                                .append(item.getQuantity())
                                .append("\n")
                );
        display.deleteCharAt(display.length() - 1);
        System.out.println(display.toString());
    }

    public void displayLosingHorse(String horseName) {
        StringBuilder display = new StringBuilder();
        display.append("No Payout: ")
                .append(horseName)
                .append("\n");

        display.deleteCharAt(display.length() - 1);
        System.out.println(display.toString());
    }

}
