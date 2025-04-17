package dev.khanna111.horsetrack.domain.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WagerModel {

    @NotNull
    @Min(1)
    private Integer horseId;

    @NotNull
    @Min(1)
    private Integer amount;
}
