package com.projedata.inventory_manager.mapper;

import com.projedata.inventory_manager.dto.product.ProductCreatedDTO;
import com.projedata.inventory_manager.dto.product.ProductUpdateDTO;
import com.projedata.inventory_manager.dto.product.ProductViewDTO;
import com.projedata.inventory_manager.dto.productMaterial.ProductMaterialDTO;
import com.projedata.inventory_manager.model.Product;
import com.projedata.inventory_manager.model.ProductMaterial;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductCreatedDTO productCreatedDTO);

    ProductCreatedDTO toCreatedDTO(Product product);

    @Mapping(target = "materials", source = "materials")
    ProductViewDTO toViewDTO(Product product);

    ProductUpdateDTO toUpdateDTO(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(ProductUpdateDTO dto, @MappingTarget Product entity);

    @Mapping(target = "materialId", source = "material.id")
    @Mapping(target = "requiredQuantity", source = "requiredQuantity")
    ProductMaterialDTO toProductMaterialDTO(ProductMaterial productMaterial);

}
