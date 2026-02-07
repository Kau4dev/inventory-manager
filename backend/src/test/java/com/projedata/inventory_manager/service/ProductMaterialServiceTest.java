package com.projedata.inventory_manager.service;

import com.projedata.inventory_manager.dto.productMaterial.ProductMaterialDTO;
import com.projedata.inventory_manager.exception.DuplicateResourceException;
import com.projedata.inventory_manager.exception.ResourceNotFoundException;
import com.projedata.inventory_manager.mapper.ProductMaterialMapper;
import com.projedata.inventory_manager.model.Product;
import com.projedata.inventory_manager.model.ProductMaterial;
import com.projedata.inventory_manager.model.RawMaterial;
import com.projedata.inventory_manager.repository.ProductMaterialRepository;
import com.projedata.inventory_manager.repository.ProductRepository;
import com.projedata.inventory_manager.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductMaterialServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @Mock
    private ProductMaterialRepository productMaterialRepository;

    @Mock
    private ProductMaterialMapper productMaterialMapper;

    @InjectMocks
    private ProductMaterialService productMaterialService;

    private Product product;
    private RawMaterial rawMaterial;
    private ProductMaterial productMaterial;
    private ProductMaterialDTO productMaterialDTO;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(BigDecimal.valueOf(100.00))
                .materials(new ArrayList<>())
                .build();

        rawMaterial = RawMaterial.builder()
                .id(1L)
                .name("Steel")
                .stockQuantity(100)
                .build();

        productMaterial = ProductMaterial.builder()
                .id(1L)
                .product(product)
                .material(rawMaterial)
                .requiredQuantity(10)
                .build();

        productMaterialDTO = new ProductMaterialDTO(1L, 10);
    }

    @Test
    void addMaterialToProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(productMaterialMapper.toEntity(productMaterialDTO)).thenReturn(productMaterial);
        when(productMaterialRepository.save(any(ProductMaterial.class))).thenReturn(productMaterial);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productMaterialService.addMaterialToProduct(1L, productMaterialDTO);

        verify(productRepository, times(1)).findById(1L);
        verify(rawMaterialRepository, times(1)).findById(1L);
        verify(productMaterialRepository, times(1)).save(any(ProductMaterial.class));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addMaterialToProduct_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productMaterialService.addMaterialToProduct(1L, productMaterialDTO));

        verify(productRepository, times(1)).findById(1L);
        verify(rawMaterialRepository, never()).findById(any());
        verify(productMaterialRepository, never()).save(any());
    }

    @Test
    void addMaterialToProduct_MaterialNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productMaterialService.addMaterialToProduct(1L, productMaterialDTO));

        verify(productRepository, times(1)).findById(1L);
        verify(rawMaterialRepository, times(1)).findById(1L);
        verify(productMaterialRepository, never()).save(any());
    }

    @Test
    void addMaterialToProduct_DuplicateMaterial() {
        // Adiciona um material existente ao produto
        ProductMaterial existingMaterial = ProductMaterial.builder()
                .id(2L)
                .product(product)
                .material(rawMaterial)
                .requiredQuantity(5)
                .build();
        product.getMaterials().add(existingMaterial);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));

        assertThrows(DuplicateResourceException.class,
                () -> productMaterialService.addMaterialToProduct(1L, productMaterialDTO));

        verify(productRepository, times(1)).findById(1L);
        verify(rawMaterialRepository, times(1)).findById(1L);
        verify(productMaterialRepository, never()).save(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void removeMaterialFromProduct_Success() {
        productMaterial.setProduct(product);
        product.getMaterials().add(productMaterial);

        when(productMaterialRepository.findByProduct_IdAndMaterial_Id(1L, 1L))
                .thenReturn(Optional.of(productMaterial));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        doNothing().when(productMaterialRepository).delete(productMaterial);

        productMaterialService.removeMaterialFromProduct(1L, 1L);

        verify(productMaterialRepository, times(1)).findByProduct_IdAndMaterial_Id(1L, 1L);
        verify(productMaterialRepository, times(1)).delete(productMaterial);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void removeMaterialFromProduct_AssociationNotFound() {
        when(productMaterialRepository.findByProduct_IdAndMaterial_Id(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productMaterialService.removeMaterialFromProduct(1L, 1L));

        verify(productMaterialRepository, times(1)).findByProduct_IdAndMaterial_Id(1L, 1L);
        verify(productMaterialRepository, never()).delete(any());
    }
}
