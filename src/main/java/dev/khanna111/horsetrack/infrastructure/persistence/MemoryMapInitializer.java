package dev.khanna111.horsetrack.infrastructure.persistence;

import dev.khanna111.horsetrack.infrastructure.adapter.in.console.CommandProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MemoryMapInitializer implements ApplicationListener<ApplicationStartedEvent> {

    private final CommandProcessor commandProcessor;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("Initializing memory map for application");
        commandProcessor.restockInventory();
        commandProcessor.intializeHorses();
    }
}