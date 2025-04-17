package dev.khanna111.horsetrack.application.mapper;

import dev.khanna111.horsetrack.application.dto.WagerDTO;
import dev.khanna111.horsetrack.domain.model.WagerModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WagerMapper {
    WagerModel toModel(WagerDTO dto);
    WagerDTO toDTO(WagerModel model);
}
