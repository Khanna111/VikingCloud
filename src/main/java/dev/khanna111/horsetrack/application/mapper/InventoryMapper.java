package dev.khanna111.horsetrack.application.mapper;


import dev.khanna111.horsetrack.application.dto.InventoryDTO;
import dev.khanna111.horsetrack.domain.model.InventoryModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    InventoryModel toModel(InventoryDTO dto);

    InventoryDTO toDTO(InventoryModel model);

    List<InventoryModel> toModelList(List<InventoryDTO> dtoList);

    List<InventoryDTO> toDTOList(List<InventoryModel> modelList);
}