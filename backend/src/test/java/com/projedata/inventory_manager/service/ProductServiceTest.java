package com.projedata.inventory_manager.service;

import com.projedata.inventory_manager.dto.product.ProductCreatedDTO;
import com.projedata.inventory_manager.dto.product.ProductProductionSuggestionDTO;
import com.projedata.inventory_manager.dto.product.ProductUpdateDTO;
import com.projedata.inventory_manager.dto.product.ProductViewDTO;
import com.projedata.inventory_manager.exception.ResourceNotFoundException;
import com.projedata.inventory_manager.mapper.ProductMapper;
import com.projedata.inventory_manager.model.Product;
import com.projedata.inventory_manager.model.ProductMaterial;
import com.projedata.inventory_manager.model.RawMaterial;
import com.projedata.inventory_manager.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductCreatedDTO productCreatedDTO;
    private ProductViewDTO productViewDTO;
    private ProductUpdateDTO productUpdateDTO;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(BigDecimal.valueOf(100.00))
                .materials(new ArrayList<>())
                .build();

        productCreatedDTO = new ProductCreatedDTO("Test Product", BigDecimal.valueOf(100.00));
        productViewDTO = new ProductViewDTO(1L, "Test Product", BigDecimal.valueOf(100.00), new ArrayList<>());
        productUpdateDTO = new ProductUpdateDTO("Updated Product", BigDecimal.valueOf(150.00));
    }

    @Test
    void createProduct_Success() {
        when(productMapper.toEntity(productCreatedDTO)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toCreatedDTO(product)).thenReturn(productCreatedDTO);

        ProductCreatedDTO result = productService.createProduct(productCreatedDTO);

        assertNotNull(result);
        assertEquals("Test Product", result.name());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toViewDTO(product)).thenReturn(productViewDTO);

        ProductViewDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Test Product", result.name());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getAllProducts_Success() {
        List<Product> products = List.of(product);
        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toViewDTO(any(Product.class))).thenReturn(productViewDTO);

        List<ProductViewDTO> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void updateProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toUpdateDTO(product)).thenReturn(productUpdateDTO);

        ProductUpdateDTO result = productService.updateProduct(1L, productUpdateDTO);

        assertNotNull(result);
        verify(productMapper, times(1)).updateFromDto(productUpdateDTO, product);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(1L, productUpdateDTO));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void deleteProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).delete(any());
    }

    @Test
    void getProducibleProducts_Success() {
        List<Product> products = List.of(product);
        when(productRepository.findProducibleProducts()).thenReturn(products);
        when(productMapper.toViewDTO(any(Product.class))).thenReturn(productViewDTO);

        List<ProductViewDTO> result = productService.getProducibleProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findProducibleProducts();
        verify(productMapper, times(1)).toViewDTO(product);
    }

    @Test
    void getProducibleProducts_EmptyList() {
        when(productRepository.findProducibleProducts()).thenReturn(new ArrayList<>());

        List<ProductViewDTO> result = productService.getProducibleProducts();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(productRepository, times(1)).findProducibleProducts();
    }

    @Test
    void getProductionSuggestions_Success() {
        // Setup raw material
        RawMaterial material = RawMaterial.builder()
                .id(1L)
                .name("Material A")
                .stockQuantity(100)
                .build();

        // Setup product material
        ProductMaterial productMaterial = ProductMaterial.builder()
                .id(1L)
                .material(material)
                .requiredQuantity(10)
                .product(product)
                .build();

        product.setMaterials(List.of(productMaterial));

        List<Product> products = List.of(product);
        when(productRepository.findAllWithMaterials()).thenReturn(products);

        List<ProductProductionSuggestionDTO> result = productService.getProductionSuggestions();

        assertNotNull(result);
        assertEquals(1, result.size());
        ProductProductionSuggestionDTO suggestion = result.getFirst();
        assertEquals(1L, suggestion.id());
        assertEquals("Test Product", suggestion.name());
        assertEquals(BigDecimal.valueOf(100.00), suggestion.price());
        assertEquals(10, suggestion.maxQuantity());
        assertEquals(BigDecimal.valueOf(1000.00), suggestion.totalValue());
        verify(productRepository, times(1)).findAllWithMaterials();
    }

    @Test
    void getProductionSuggestions_MultipleProducts_SortedByPrice() {
        // Setup first product (cheaper)
        RawMaterial material1 = RawMaterial.builder()
                .id(1L)
                .name("Material A")
                .stockQuantity(100)
                .build();

        ProductMaterial productMaterial1 = ProductMaterial.builder()
                .id(1L)
                .material(material1)
                .requiredQuantity(10)
                .product(product)
                .build();

        product.setMaterials(List.of(productMaterial1));

        // Setup second product (more expensive)
        Product product2 = Product.builder()
                .id(2L)
                .name("Expensive Product")
                .price(BigDecimal.valueOf(500.00))
                .materials(new ArrayList<>())
                .build();

        RawMaterial material2 = RawMaterial.builder()
                .id(2L)
                .name("Material B")
                .stockQuantity(50)
                .build();

        ProductMaterial productMaterial2 = ProductMaterial.builder()
                .id(2L)
                .material(material2)
                .requiredQuantity(5)
                .product(product2)
                .build();

        product2.setMaterials(List.of(productMaterial2));

        List<Product> products = List.of(product, product2);
        when(productRepository.findAllWithMaterials()).thenReturn(products);

        List<ProductProductionSuggestionDTO> result = productService.getProductionSuggestions();

        assertNotNull(result);
        assertEquals(2, result.size());
        // Should be sorted by price descending
        assertEquals("Expensive Product", result.get(0).name());
        assertEquals(BigDecimal.valueOf(500.00), result.get(0).price());
        assertEquals("Test Product", result.get(1).name());
        assertEquals(BigDecimal.valueOf(100.00), result.get(1).price());
        verify(productRepository, times(1)).findAllWithMaterials();
    }

    @Test
    void getProductionSuggestions_InsufficientMaterials() {
        // Setup raw material with insufficient stock
        RawMaterial material = RawMaterial.builder()
                .id(1L)
                .name("Material A")
                .stockQuantity(5)
                .build();

        ProductMaterial productMaterial = ProductMaterial.builder()
                .id(1L)
                .material(material)
                .requiredQuantity(10)
                .product(product)
                .build();

        product.setMaterials(List.of(productMaterial));

        List<Product> products = List.of(product);
        when(productRepository.findAllWithMaterials()).thenReturn(products);

        List<ProductProductionSuggestionDTO> result = productService.getProductionSuggestions();

        assertNotNull(result);
        assertEquals(0, result.size()); // No products can be produced
        verify(productRepository, times(1)).findAllWithMaterials();
    }

    @Test
    void getProductionSuggestions_MultipleMaterials_LimitedBySmallestStock() {
        // Setup multiple materials with different stock levels
        RawMaterial material1 = RawMaterial.builder()
                .id(1L)
                .name("Material A")
                .stockQuantity(100)
                .build();

        RawMaterial material2 = RawMaterial.builder()
                .id(2L)
                .name("Material B")
                .stockQuantity(25)
                .build();

        ProductMaterial productMaterial1 = ProductMaterial.builder()
                .id(1L)
                .material(material1)
                .requiredQuantity(10)
                .product(product)
                .build();

        ProductMaterial productMaterial2 = ProductMaterial.builder()
                .id(2L)
                .material(material2)
                .requiredQuantity(5)
                .product(product)
                .build();

        product.setMaterials(List.of(productMaterial1, productMaterial2));

        List<Product> products = List.of(product);
        when(productRepository.findAllWithMaterials()).thenReturn(products);

        List<ProductProductionSuggestionDTO> result = productService.getProductionSuggestions();

        assertNotNull(result);
        assertEquals(1, result.size());
        ProductProductionSuggestionDTO suggestion = result.getFirst();
        // Limited by material2: 25/5 = 5 (not 100/10 = 10)
        assertEquals(5, suggestion.maxQuantity());
        assertEquals(BigDecimal.valueOf(500.00), suggestion.totalValue());
        verify(productRepository, times(1)).findAllWithMaterials();
    }

    @Test
    void getProductionSuggestions_EmptyList() {
        when(productRepository.findAllWithMaterials()).thenReturn(new ArrayList<>());

        List<ProductProductionSuggestionDTO> result = productService.getProductionSuggestions();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(productRepository, times(1)).findAllWithMaterials();
    }

    @Test
    void updateProduct_PartialUpdate_OnlyName() {
        ProductUpdateDTO partialUpdate = new ProductUpdateDTO("New Name", null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toUpdateDTO(product)).thenReturn(partialUpdate);

        ProductUpdateDTO result = productService.updateProduct(1L, partialUpdate);

        assertNotNull(result);
        verify(productMapper, times(1)).updateFromDto(partialUpdate, product);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct_PartialUpdate_OnlyPrice() {
        ProductUpdateDTO partialUpdate = new ProductUpdateDTO(null, BigDecimal.valueOf(200.00));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toUpdateDTO(product)).thenReturn(partialUpdate);

        ProductUpdateDTO result = productService.updateProduct(1L, partialUpdate);

        assertNotNull(result);
        verify(productMapper, times(1)).updateFromDto(partialUpdate, product);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void getProductionSuggestions_ProductWithoutMaterials() {
        Product productWithoutMaterials = Product.builder()
                .id(2L)
                .name("Empty Product")
                .price(BigDecimal.valueOf(50.00))
                .materials(new ArrayList<>())
                .build();

        when(productRepository.findAllWithMaterials()).thenReturn(List.of(productWithoutMaterials));

        List<ProductProductionSuggestionDTO> result = productService.getProductionSuggestions();

        assertNotNull(result);
        assertEquals(0, result.size()); // Não deve incluir produtos sem materiais
        verify(productRepository, times(1)).findAllWithMaterials();
    }

    @Test
    void getProductionSuggestions_RequiredQuantityZero() {
        RawMaterial material = RawMaterial.builder()
                .id(1L)
                .name("Material A")
                .stockQuantity(100)
                .build();

        ProductMaterial productMaterial = ProductMaterial.builder()
                .id(1L)
                .material(material)
                .requiredQuantity(0) // Quantidade inválida
                .product(product)
                .build();

        product.setMaterials(List.of(productMaterial));

        when(productRepository.findAllWithMaterials()).thenReturn(List.of(product));

        List<ProductProductionSuggestionDTO> result = productService.getProductionSuggestions();

        assertNotNull(result);
        assertEquals(0, result.size()); // Não deve incluir se quantidade requerida for 0
        verify(productRepository, times(1)).findAllWithMaterials();
    }
}


