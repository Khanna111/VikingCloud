package dev.khanna111.horsetrack.application.port.out;

import dev.khanna111.horsetrack.application.dto.InventoryDTO;

import java.util.List;
import java.util.Map;

public interface InventoryMemoryMapPort {

    void restockInventory();
    void updateInventoryItem(InventoryDTO inventoryDTO);
    List<InventoryDTO> getInventoryItems();

}
