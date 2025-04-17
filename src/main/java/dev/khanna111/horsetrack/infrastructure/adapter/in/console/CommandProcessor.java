package dev.khanna111.horsetrack.infrastructure.adapter.in.console;

import dev.khanna111.horsetrack.application.dto.HorseDTO;
import dev.khanna111.horsetrack.application.dto.InventoryDTO;
import dev.khanna111.horsetrack.application.port.in.HorseUseCasePort;
import dev.khanna111.horsetrack.application.port.in.InventoryUseCasePort;
import dev.khanna111.horsetrack.application.port.in.RaceUseCasePort;
import dev.khanna111.horsetrack.domain.exception.InvalidBetException;
import dev.khanna111.horsetrack.domain.exception.InvalidCommandException;
import dev.khanna111.horsetrack.domain.exception.InvalidHorseException;
import dev.khanna111.horsetrack.infrastructure.adapter.in.error.QuitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommandProcessor {

    private final InventoryUseCasePort inventoryUseCasePort;
    private final HorseUseCasePort horseUseCasePort;
    private final RaceUseCasePort raceUseCasePort;

    public void restockInventory() {
        log.info("Restocking cash inventory");
        inventoryUseCasePort.restockInventory();
    }

    public List<InventoryDTO> getInventoryItems() {
        return inventoryUseCasePort.getInventoryItems();
    }

    public void intializeHorses() {
        horseUseCasePort.intializeHorses();
    }

    public List<HorseDTO> getHorses() {
        return horseUseCasePort.getHorses();
    }

    public String getHorseNameById(int horseId) {
        Optional<HorseDTO> horseDTOOptional= horseUseCasePort.getHorses()
                .stream()
                .filter(horse -> horse.getId() == horseId)
                .findAny();
        if (horseDTOOptional.isPresent())    {
            return horseDTOOptional.get().getName();
        }
        else {
            return "Unknown Horse";
        }
    }

    public List<InventoryDTO> wager(int horseId, int amount) {
        log.info("Wager placed on horse {} for amount {}", horseId, amount);
        List<InventoryDTO> winningToDispenseList = raceUseCasePort.placeWager(horseId, amount);
        return winningToDispenseList;
    }

    public void setWinningHorse(int horseId) {
        log.info("Setting winning horse to {}", horseId);
        horseUseCasePort.setWinningHorse(horseId);

    }

    public void quit() throws QuitException {
        log.info("Quitting application");
        throw new QuitException("Quitting application");
    }

    public void invalidCommand(String command) throws InvalidCommandException{
        log.info("Invalid command: {}", command);
        throw new InvalidCommandException(command, "Invalid command: " + command);
    }

    public void invalidHorseId(String horseId) throws InvalidHorseException{
        log.info("Invalid horseId: {}", horseId);
        throw new InvalidHorseException(horseId, "Invalid horseId: " + horseId);
    }

    public void invalidBet(String bet) throws InvalidHorseException{
        log.info("Invalid bet: {}", bet);
        throw new InvalidBetException(bet, "Invalid horseId: " + bet);
    }

}
