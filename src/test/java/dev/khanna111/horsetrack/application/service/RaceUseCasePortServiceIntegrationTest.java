package dev.khanna111.horsetrack.application.service;

import dev.khanna111.horsetrack.application.dto.HorseDTO;
import dev.khanna111.horsetrack.application.dto.InventoryDTO;
import dev.khanna111.horsetrack.application.port.out.HorseMemoryMapPort;
import dev.khanna111.horsetrack.application.port.out.InventoryMemoryMapPort;
import dev.khanna111.horsetrack.application.port.out.TestableHorseMemoryMapPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RaceUseCasePortServiceIntegrationTest {

    @Autowired
    private RaceUseCasePortService raceUseCasePortService;

    @Autowired
    private TestableHorseMemoryMapPort horseMemoryMapPort;

    @Autowired
    private InventoryMemoryMapPort inventoryMemoryMapPort;

    @BeforeEach
    void setUp() {
        List<HorseDTO> horses = new ArrayList<>();
        HorseDTO horse = HorseDTO.builder()
                .id(1)
                .odds(10)
                .ifWonLastTime(true)
                .build();
        horses.add(horse);
        if (horseMemoryMapPort instanceof TestableHorseMemoryMapPort) {
            ((TestableHorseMemoryMapPort) horseMemoryMapPort).setHorses(horses);
        }

        // For inventory, manually initialize test data using the update method.
        List<InventoryDTO> testInventory = new ArrayList<>();
        testInventory.add(InventoryDTO.builder().denomination(2).quantity(50).build());
        testInventory.add(InventoryDTO.builder().denomination(5).quantity(20).build());
        testInventory.add(InventoryDTO.builder().denomination(10).quantity(10).build());
        testInventory.forEach(inventoryMemoryMapPort::updateInventoryItem);
    }

    @Test
    void testPlaceWagerWinningHorse() {
        List<InventoryDTO> initialInventory = inventoryMemoryMapPort.getInventoryItems();
        assertFalse(initialInventory.isEmpty(), "Inventory should be initialized.");

        // Placing a wager on a horse that has won
        List<InventoryDTO> result = raceUseCasePortService.placeWager(1, 10);
        assertNotNull(result, "Result list should not be null.");

        // Verify inventory has changed
        List<InventoryDTO> updatedInventory = inventoryMemoryMapPort.getInventoryItems();
        assertNotEquals(initialInventory, updatedInventory, "Inventory not updated after placing a winning wager. Why");
    }
}