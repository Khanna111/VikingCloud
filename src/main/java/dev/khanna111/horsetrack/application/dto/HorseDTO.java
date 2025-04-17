package dev.khanna111.horsetrack.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HorseDTO {
    @NotNull
    private Integer id;
    @NotBlank
    private String name;
    @NotNull
    private Integer odds;
    private boolean ifWonLastTime;
}
