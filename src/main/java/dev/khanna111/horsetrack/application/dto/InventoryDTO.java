package dev.khanna111.horsetrack.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class InventoryDTO {

    @NotNull
    private Integer denomination;
    @NotNull
    private Integer quantity;

}
