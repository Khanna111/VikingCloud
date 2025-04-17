package dev.khanna111.horsetrack.infrastructure.persistence;

import dev.khanna111.horsetrack.application.dto.InventoryDTO;
import dev.khanna111.horsetrack.application.port.out.InventoryMemoryMapPort;
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
public class InventoryMemoryMapAdapter implements InventoryMemoryMapPort {

    private final AppProperties appProperties;
    private ConcurrentHashMap<Integer, Integer> inventoryMap = new ConcurrentHashMap<>();


    @Override
    public void restockInventory() {
        inventoryMap = restockInventoryAsMap();
    }

    @Override
    public void updateInventoryItem(InventoryDTO inventoryDTO) {
        inventoryMap.put(inventoryDTO.getDenomination(), inventoryDTO.getQuantity());
    }

    @Override
    public List<InventoryDTO> getInventoryItems() {
        return inventoryMap.entrySet()
                .stream()
                .map(entry -> new InventoryDTO(entry.getKey(), entry.getValue()))
                .toList();
    }

    private ConcurrentHashMap<Integer, Integer> restockInventoryAsMap() {
        String fileName = appProperties.getInventory().getFile();

        Resource resource = null;
        BufferedReader bR = null;
        try {
            resource = new ClassPathResource(fileName);
            bR = new BufferedReader(new java.io.InputStreamReader(resource.getInputStream()));


            inventoryMap = bR.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .filter(line -> !line.startsWith("#"))
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length == 2)
                    .collect(toConcurrentMap(
                            parts -> Integer.parseInt(parts[0].trim()), // denomination
                            parts -> Integer.parseInt(parts[1].trim()), // quantity
                            (currentValue, newValue) -> currentValue, // never going to happen
                            ConcurrentHashMap::new
                    ));
            sanityCheck(inventoryMap);
            return inventoryMap;

        } catch (Exception e) {
            log.warn("Error reading inventory file: {} ", fileName);
            log.warn("Exception: {}", e.getMessage());
            throw new QuitException("Error intializing inventory");

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

    private void sanityCheck(ConcurrentHashMap<Integer, Integer> concurrentHashMap) {
        log.info("Sanity check of inventory");
        StringBuilder stringBuilder = new StringBuilder();
        concurrentHashMap.forEach((key, value) -> {
            stringBuilder.append("Denomination: ")
                    .append(key)
                    .append(", Quantity: ")
                    .append(value)
                    .append("\n");
        });
        log.info("Inventory: {}", stringBuilder.toString());
    }
}
