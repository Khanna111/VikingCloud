package dev.khanna111.horsetrack.application.port.in;

import dev.khanna111.horsetrack.application.dto.HorseDTO;

import java.util.List;

public interface HorseUseCasePort {
    void intializeHorses();
    List<HorseDTO> getHorses();
    void setWinningHorse(int horseId);
}
