package com.projedata.inventory_manager.service;

import com.projedata.inventory_manager.dto.rawMaterial.RawMaterialCreatedDTO;
import com.projedata.inventory_manager.dto.rawMaterial.RawMaterialUpdateDTO;
import com.projedata.inventory_manager.dto.rawMaterial.RawMaterialViewDTO;
import com.projedata.inventory_manager.exception.ResourceNotFoundException;
import com.projedata.inventory_manager.mapper.RawMaterialMapper;
import com.projedata.inventory_manager.model.RawMaterial;
import com.projedata.inventory_manager.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialServiceTest {

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @Mock
    private RawMaterialMapper rawMaterialMapper;

    @InjectMocks
    private RawMaterialService rawMaterialService;

    private RawMaterial rawMaterial;
    private RawMaterialCreatedDTO rawMaterialCreatedDTO;
    private RawMaterialViewDTO rawMaterialViewDTO;
    private RawMaterialUpdateDTO rawMaterialUpdateDTO;

    @BeforeEach
    void setUp() {
        rawMaterial = RawMaterial.builder()
                .id(1L)
                .name("Steel")
                .stockQuantity(100)
                .build();

        rawMaterialCreatedDTO = new RawMaterialCreatedDTO("Steel", 100);
        rawMaterialViewDTO = new RawMaterialViewDTO(1L, "Steel", 100);
        rawMaterialUpdateDTO = new RawMaterialUpdateDTO("Updated Steel", 150);
    }

    @Test
    void createProduct_Success() {
        when(rawMaterialMapper.toEntity(rawMaterialCreatedDTO)).thenReturn(rawMaterial);
        when(rawMaterialRepository.save(rawMaterial)).thenReturn(rawMaterial);
        when(rawMaterialMapper.toCreatedDTO(rawMaterial)).thenReturn(rawMaterialCreatedDTO);

        RawMaterialCreatedDTO result = rawMaterialService.createRawMaterial(rawMaterialCreatedDTO);

        assertNotNull(result);
        assertEquals("Steel", result.name());
        assertEquals(100, result.stockQuantity());
        verify(rawMaterialRepository, times(1)).save(rawMaterial);
    }

    @Test
    void getProductById_Success() {
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterialMapper.toViewDTO(rawMaterial)).thenReturn(rawMaterialViewDTO);

        RawMaterialViewDTO result = rawMaterialService.getRawMaterialById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Steel", result.name());
        verify(rawMaterialRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_NotFound() {
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rawMaterialService.getRawMaterialById(1L));
        verify(rawMaterialRepository, times(1)).findById(1L);
    }

    @Test
    void getAllProducts_Success() {
        List<RawMaterial> materials = List.of(rawMaterial);
        when(rawMaterialRepository.findAll()).thenReturn(materials);
        when(rawMaterialMapper.toViewDTO(any(RawMaterial.class))).thenReturn(rawMaterialViewDTO);

        List<RawMaterialViewDTO> result = rawMaterialService.getAllRawMaterials();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(rawMaterialRepository, times(1)).findAll();
    }

    @Test
    void updateProduct_Success() {
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterialRepository.save(rawMaterial)).thenReturn(rawMaterial);
        when(rawMaterialMapper.toUpdateDTO(rawMaterial)).thenReturn(rawMaterialUpdateDTO);

        RawMaterialUpdateDTO result = rawMaterialService.updateRawMaterial(1L, rawMaterialUpdateDTO);

        assertNotNull(result);
        verify(rawMaterialMapper, times(1)).updateFromDto(rawMaterialUpdateDTO, rawMaterial);
        verify(rawMaterialRepository, times(1)).save(rawMaterial);
    }

    @Test
    void updateProduct_NotFound() {
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rawMaterialService.updateRawMaterial(1L, rawMaterialUpdateDTO));
        verify(rawMaterialRepository, times(1)).findById(1L);
    }

    @Test
    void deleteProduct_Success() {
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        doNothing().when(rawMaterialRepository).delete(rawMaterial);

        rawMaterialService.deleteRawMaterial(1L);

        verify(rawMaterialRepository, times(1)).findById(1L);
        verify(rawMaterialRepository, times(1)).delete(rawMaterial);
    }

    @Test
    void deleteProduct_NotFound() {
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rawMaterialService.deleteRawMaterial(1L));
        verify(rawMaterialRepository, times(1)).findById(1L);
        verify(rawMaterialRepository, never()).delete(any());
    }

    @Test
    void updateRawMaterial_PartialUpdate_OnlyName() {
        RawMaterialUpdateDTO partialUpdate = new RawMaterialUpdateDTO("New Name", null);

        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterialRepository.save(rawMaterial)).thenReturn(rawMaterial);
        when(rawMaterialMapper.toUpdateDTO(rawMaterial)).thenReturn(partialUpdate);

        RawMaterialUpdateDTO result = rawMaterialService.updateRawMaterial(1L, partialUpdate);

        assertNotNull(result);
        verify(rawMaterialMapper, times(1)).updateFromDto(partialUpdate, rawMaterial);
        verify(rawMaterialRepository, times(1)).save(rawMaterial);
    }

    @Test
    void updateRawMaterial_PartialUpdate_OnlyStockQuantity() {
        RawMaterialUpdateDTO partialUpdate = new RawMaterialUpdateDTO(null, 200);

        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterialRepository.save(rawMaterial)).thenReturn(rawMaterial);
        when(rawMaterialMapper.toUpdateDTO(rawMaterial)).thenReturn(partialUpdate);

        RawMaterialUpdateDTO result = rawMaterialService.updateRawMaterial(1L, partialUpdate);

        assertNotNull(result);
        verify(rawMaterialMapper, times(1)).updateFromDto(partialUpdate, rawMaterial);
        verify(rawMaterialRepository, times(1)).save(rawMaterial);
    }
}
