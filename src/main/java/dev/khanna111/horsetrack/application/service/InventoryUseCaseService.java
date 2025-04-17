package dev.khanna111.horsetrack.application.service;

import dev.khanna111.horsetrack.application.dto.InventoryDTO;
import dev.khanna111.horsetrack.application.port.in.InventoryUseCasePort;
import dev.khanna111.horsetrack.application.port.out.InventoryMemoryMapPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryUseCaseService implements InventoryUseCasePort {

    private final InventoryMemoryMapPort inventoryMemoryMapPort;

    @Override
    public void restockInventory() {
        inventoryMemoryMapPort.restockInventory();
    }

    @Override
    public List<InventoryDTO> getInventoryItems() {
        return inventoryMemoryMapPort.getInventoryItems();
    }


}
