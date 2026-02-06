package com.projedata.inventory_manager.mapper;

import com.projedata.inventory_manager.dto.productMaterial.ProductMaterialDTO;
import com.projedata.inventory_manager.model.ProductMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMaterialMapper {

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "material", ignore = true)
    ProductMaterial toEntity(ProductMaterialDTO dto);
}
