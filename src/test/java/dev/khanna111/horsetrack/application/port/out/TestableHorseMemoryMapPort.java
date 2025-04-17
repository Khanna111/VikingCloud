package dev.khanna111.horsetrack.application.port.out;

import dev.khanna111.horsetrack.application.dto.HorseDTO;
import java.util.List;

public interface TestableHorseMemoryMapPort extends HorseMemoryMapPort {
    void setHorses(List<HorseDTO> horses);
}