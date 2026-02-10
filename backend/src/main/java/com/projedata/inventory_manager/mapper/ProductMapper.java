package com.projedata.inventory_manager.mapper;

import com.projedata.inventory_manager.dto.product.ProductCreatedDTO;
import com.projedata.inventory_manager.dto.product.ProductUpdateDTO;
import com.projedata.inventory_manager.dto.product.ProductViewDTO;
import com.projedata.inventory_manager.dto.productMaterial.ProductMaterialViewDTO;
import com.projedata.inventory_manager.model.Product;
import com.projedata.inventory_manager.model.ProductMaterial;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductCreatedDTO productCreatedDTO);

    ProductCreatedDTO toCreatedDTO(Product product);

    default ProductViewDTO toViewDTO(Product product) {
        if (product == null) {
            return null;
        }

        List<ProductMaterialViewDTO> materialDTOs = product.getMaterials() == null ? null :
                product.getMaterials().stream()
                        .map(this::toProductMaterialViewDTO)
                        .collect(Collectors.toList());

        boolean producible = isProductProducible(product);

        return new ProductViewDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                materialDTOs,
                producible
        );
    }

    default boolean isProductProducible(Product product) {
        if (product.getMaterials() == null || product.getMaterials().isEmpty()) {
            return false;
        }

        return product.getMaterials().stream()
                .allMatch(pm -> pm.getMaterial() != null &&
                        pm.getMaterial().getStockQuantity() >= pm.getRequiredQuantity());
    }

    ProductUpdateDTO toUpdateDTO(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(ProductUpdateDTO dto, @MappingTarget Product entity);

    @Mapping(target = "materialId", source = "material.id")
    @Mapping(target = "materialName", source = "material.name")
    @Mapping(target = "requiredQuantity", source = "requiredQuantity")
    @Mapping(target = "stockQuantity", source = "material.stockQuantity")
    ProductMaterialViewDTO toProductMaterialViewDTO(ProductMaterial productMaterial);

}
