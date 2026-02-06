package com.projedata.inventory_manager.mapper;

import com.projedata.inventory_manager.dto.rawMaterial.RawMaterialCreatedDTO;
import com.projedata.inventory_manager.dto.rawMaterial.RawMaterialUpdateDTO;
import com.projedata.inventory_manager.dto.rawMaterial.RawMaterialViewDTO;
import com.projedata.inventory_manager.model.RawMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RawMaterialMapper {

    RawMaterial toEntity(RawMaterialCreatedDTO rawMaterialCreatedDTO);

    RawMaterialCreatedDTO toCreatedDTO(RawMaterial rawMaterial);

    RawMaterialViewDTO toViewDTO(RawMaterial rawMaterial);

    RawMaterialUpdateDTO toUpdateDTO(RawMaterial rawMaterial);

    void updateFromDto(RawMaterialUpdateDTO dto, @MappingTarget RawMaterial entity);
}
