package com.projedata.inventory_manager.mapper;

import com.projedata.inventory_manager.dto.product.ProductCreatedDTO;
import com.projedata.inventory_manager.dto.product.ProductUpdateDTO;
import com.projedata.inventory_manager.dto.product.ProductViewDTO;
import com.projedata.inventory_manager.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductCreatedDTO productCreatedDTO);

    ProductCreatedDTO toCreatedDTO(Product product);

    ProductViewDTO toViewDTO(Product product);

    ProductUpdateDTO toUpdateDTO(Product product);

    void updateFromDto(ProductUpdateDTO dto, @MappingTarget Product entity);

}
