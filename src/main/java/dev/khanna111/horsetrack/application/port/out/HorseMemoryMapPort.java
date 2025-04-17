package dev.khanna111.horsetrack.application.port.out;

import dev.khanna111.horsetrack.application.dto.HorseDTO;
import dev.khanna111.horsetrack.application.dto.InventoryDTO;

import java.util.List;

public interface HorseMemoryMapPort {

    void intializeHorses();
    List<HorseDTO> getHorses();
}
