package dev.khanna111.horsetrack.application.service;

import dev.khanna111.horsetrack.application.dto.HorseDTO;
import dev.khanna111.horsetrack.application.dto.InventoryDTO;
import dev.khanna111.horsetrack.application.mapper.InventoryMapper;
import dev.khanna111.horsetrack.application.port.in.RaceUseCasePort;
import dev.khanna111.horsetrack.application.port.out.HorseMemoryMapPort;
import dev.khanna111.horsetrack.application.port.out.InventoryMemoryMapPort;
import dev.khanna111.horsetrack.domain.exception.InsufficientFundsException;
import dev.khanna111.horsetrack.domain.exception.InvalidHorseException;
import dev.khanna111.horsetrack.domain.model.InventoryModel;
import dev.khanna111.horsetrack.domain.service.RaceDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class RaceUseCasePortService implements RaceUseCasePort {
    private final RaceDomainService raceDomainService;
    private final HorseMemoryMapPort horseMemoryMapPort;
    private final InventoryMemoryMapPort inventoryMemoryMapPort;
    private final InventoryMapper inventoryMapper;

    @Override
    public List<InventoryDTO> placeWager(int horseId, int amount) {
        boolean isWagerWon = false;
        checkHorseExists(horseId);
        HorseDTO horseDTO = horseMemoryMapPort.getHorses().stream()
                .filter(horse -> horse.getId().equals(horseId))
                .findFirst().get();

        List<InventoryDTO> inventoryDTOToDispenseList = new ArrayList<>();
        if (horseDTO.isIfWonLastTime()) {
            isWagerWon = true;
            // This is a winning horse
            int winnings = raceDomainService.race(horseId, horseDTO.getOdds(), amount);

            List<InventoryDTO> currentInventoryDTOList = inventoryMemoryMapPort.getInventoryItems();

            List<InventoryModel> currentInventoryModelList = inventoryMapper.toModelList(currentInventoryDTOList);

            List<InventoryModel> inventoryModelToDispenseList = raceDomainService.dispenseWinnings(winnings, currentInventoryModelList);

            Map<Integer, InventoryDTO> currentInventoryMap = new HashMap<>();

            for (InventoryDTO inventoryDTO : currentInventoryDTOList) {
                currentInventoryMap.put(inventoryDTO.getDenomination(), inventoryDTO);
            }

            inventoryDTOToDispenseList = inventoryMapper.toDTOList(inventoryModelToDispenseList);

            // All is well. Update existing inventory
            inventoryDTOToDispenseList.forEach(inventoryDTO -> {
                InventoryDTO updatedToExistingItem =
                        InventoryDTO.builder()
                                .denomination(inventoryDTO.getDenomination())
                                .quantity(currentInventoryMap.get(inventoryDTO.getDenomination()).getQuantity() - inventoryDTO.getQuantity())
                                .build();

                inventoryMemoryMapPort.updateInventoryItem(updatedToExistingItem);
            });

        }
        return inventoryDTOToDispenseList;
    }

    private void checkHorseExists(int horseId) throws InvalidHorseException {
        if (horseMemoryMapPort.getHorses().stream()
                .noneMatch(horse -> horse.getId().equals(horseId))) {
            throw new InvalidHorseException("" + horseId, "Invalid horseId: " + horseId);
        }
    }
}
