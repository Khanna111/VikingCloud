package dev.khanna111.horsetrack.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryModel {
    @NotNull
    private Integer denomination;
    @NotNull
    private Integer quantity;
}
