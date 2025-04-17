package dev.khanna111.horsetrack.infrastructure.persistence;

import dev.khanna111.horsetrack.application.dto.HorseDTO;
import dev.khanna111.horsetrack.application.port.out.HorseMemoryMapPort;
import dev.khanna111.horsetrack.infrastructure.adapter.in.error.QuitException;
import dev.khanna111.horsetrack.infrastructure.configuration.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toConcurrentMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class HorseMemoryMapPortAdapter implements HorseMemoryMapPort {
    private final AppProperties appProperties;
    private ConcurrentHashMap<Integer, HorseDTO> horseMap = new ConcurrentHashMap<>();

    @Override
    public void intializeHorses() {
        horseMap = restockHorsesAsMap();
    }

    @Override
    public List<HorseDTO> getHorses() {
        return horseMap.values()
                .stream()
                .toList();
    }


    private ConcurrentHashMap<Integer, HorseDTO> restockHorsesAsMap() {
        String fileName = appProperties.getHorses().getFile();

        Resource resource = null;
        BufferedReader bR = null;
        try {
            resource = new ClassPathResource(fileName);
            bR = new BufferedReader(new java.io.InputStreamReader(resource.getInputStream()));


            horseMap = bR.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .filter(line -> !line.startsWith("#"))
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length == 4)
                    .collect(toConcurrentMap(
                            parts -> Integer.parseInt(parts[0].trim()), // denomination
                            parts ->
                                    HorseDTO.builder()
                                            .id(Integer.parseInt(parts[0].trim()))
                                            .name(parts[1].trim())
                                            .odds(Integer.parseInt(parts[2].trim()))
                                            .ifWonLastTime(parts[3].trim().equalsIgnoreCase("won") ? true : false)
                                            .build(), // HorseDTO
                            (currentValue, newValue) -> currentValue, // never going to happen
                            ConcurrentHashMap::new
                    ));
            sanityCheck(horseMap);
            return horseMap;

        } catch (Exception e) {
            log.warn("Error reading horse file: {} ", fileName);
            log.warn("Exception: {}", e.getMessage());
            throw new QuitException("Error initializing horses");

        } finally {
            if (Optional.ofNullable(bR).isPresent()) {
                try {
                    bR.close();
                } catch (Exception e) {
                    log.warn("Error closing BufferedReader: {}", e.getMessage());
                }
            }
        }
    }

    private void sanityCheck(ConcurrentHashMap<Integer, HorseDTO> concurrentHashMap) {
        log.info("Sanity check of Horses");
        StringBuilder stringBuilder = new StringBuilder();
        concurrentHashMap.forEach((key, value) -> {
            stringBuilder.append("Id: ")
                    .append(key)
                    .append(", Name: ")
                    .append(value.getName())
                    .append(", Odds: ")
                    .append(value.getOdds())
                    .append(", Won Last Time: ")
                    .append(value.isIfWonLastTime())
                    .append("\n");
        });
        log.info("Horses: {}", stringBuilder.toString());
    }
}
