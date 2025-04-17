package dev.khanna111.horsetrack.infrastructure.persistence;

import dev.khanna111.horsetrack.application.dto.HorseDTO;
import dev.khanna111.horsetrack.application.port.out.TestableHorseMemoryMapPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Primary
public class TestableHorseMemoryMapPortAdapter implements TestableHorseMemoryMapPort {

    private final ConcurrentHashMap<Integer, HorseDTO> horseMap = new ConcurrentHashMap<>();

    @Override
    public void intializeHorses() {
        // No file-based initialization for tests.
        horseMap.clear();
    }

    @Override
    public List<HorseDTO> getHorses() {
        return horseMap.values().stream().toList();
    }

    @Override
    public void setHorses(List<HorseDTO> horses) {
        horseMap.clear();
        for (HorseDTO horse : horses) {
            horseMap.put(horse.getId(), horse);
        }
    }
}