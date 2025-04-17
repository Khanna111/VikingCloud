package dev.khanna111.horsetrack.domain.service;

import dev.khanna111.horsetrack.application.dto.InventoryDTO;
import dev.khanna111.horsetrack.domain.exception.InsufficientFundsException;
import dev.khanna111.horsetrack.domain.model.InventoryModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RaceDomainService {

    /**
     * Calculate winnings based on the winning horse, odds and wager
     *
     * @param winningHorseId
     * @param odds
     * @param wager
     * @return
     */
    public int race(int winningHorseId, int odds, int wager) {
        int winnings = 0;
        if (winningHorseId > 0 && odds > 0 && wager > 0) {
            winnings = wager * odds;
        }
        return winnings;
    }

    /**
     * Dispense winnings based on the current inventory
     *
     * @param winnings
     * @param currentInventoryModelList
     * @return
     * @throws InsufficientFundsException
     */
    public List<InventoryModel> dispenseWinnings(int winnings, List<InventoryModel> currentInventoryModelList) {
        List<InventoryModel> inventoryToDispenseList = new ArrayList<>();

        var sortedInventoryModelList = currentInventoryModelList.stream()
                .sorted((o1, o2) -> o2.getDenomination() - o1.getDenomination())
                .toList();

        int remainingWinnings = winnings;

        for (InventoryModel inventoryModel : sortedInventoryModelList) {
            int availableQuantity = inventoryModel.getQuantity();
            int countNeeded = remainingWinnings / inventoryModel.getDenomination();
            if (availableQuantity >= 0) {
                var tempItem = InventoryModel.builder()
                        .quantity(Math.min(countNeeded, availableQuantity))
                        .denomination(inventoryModel.getDenomination())
                        .build();
                remainingWinnings -= tempItem.getQuantity() * tempItem.getDenomination();
                inventoryToDispenseList.add(tempItem);
            }
        }
        if (remainingWinnings > 0) {
            throw new InsufficientFundsException(winnings, "Insufficient inventory to dispense winnings");
        }

        return inventoryToDispenseList;
    }
}
