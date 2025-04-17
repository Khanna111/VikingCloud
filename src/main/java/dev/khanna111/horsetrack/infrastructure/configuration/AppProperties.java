package dev.khanna111.horsetrack.infrastructure.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {

    private Inventory inventory = new Inventory();
    private Horses horses = new Horses();

    @Data
    public static class Inventory {
        private String file;
    }

    @Data
    public static class Horses {
        private String file;
    }
}