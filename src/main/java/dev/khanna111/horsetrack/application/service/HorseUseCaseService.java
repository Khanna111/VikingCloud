package dev.khanna111.horsetrack.application.service;

import dev.khanna111.horsetrack.application.dto.HorseDTO;
import dev.khanna111.horsetrack.application.port.in.HorseUseCasePort;
import dev.khanna111.horsetrack.application.port.out.HorseMemoryMapPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HorseUseCaseService implements HorseUseCasePort {

    private final HorseMemoryMapPort horseMemoryMapPort;

    @Override
    public void intializeHorses() {
        horseMemoryMapPort.intializeHorses();
    }

    @Override
    public List<HorseDTO> getHorses() {
        return horseMemoryMapPort.getHorses();
    }

    @Override
    public void setWinningHorse(int horseId) {
        horseMemoryMapPort.getHorses().forEach(
                horse -> {
                    if (horse.getId() == horseId) {
                        horse.setIfWonLastTime(true);
                    } else {
                        horse.setIfWonLastTime(false);
                    }
                }
        );
    }
}
