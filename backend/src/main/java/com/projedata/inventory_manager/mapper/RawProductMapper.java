package com.projedata.inventory_manager.mapper;

import com.projedata.inventory_manager.dto.product.ProductCreatedDTO;
import com.projedata.inventory_manager.dto.product.ProductUpdateDTO;
import com.projedata.inventory_manager.dto.product.ProductViewDTO;
import com.projedata.inventory_manager.model.RawMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RawProductMapper {

    RawMaterial toEntity(ProductCreatedDTO productCreatedDTO);

    ProductCreatedDTO toCreatedDTO(RawMaterial rawMaterial);

    ProductViewDTO toViewDTO(RawMaterial rawMaterial);

    ProductUpdateDTO toUpdateDTO(RawMaterial rawMaterial);

    void updateFromDto(ProductUpdateDTO dto, @MappingTarget RawMaterial entity);
}
