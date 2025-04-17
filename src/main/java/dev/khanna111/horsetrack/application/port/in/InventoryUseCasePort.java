package dev.khanna111.horsetrack.application.port.in;

import dev.khanna111.horsetrack.application.dto.InventoryDTO;

import java.util.List;

public interface InventoryUseCasePort {

    void restockInventory();
    List<InventoryDTO> getInventoryItems();
}
