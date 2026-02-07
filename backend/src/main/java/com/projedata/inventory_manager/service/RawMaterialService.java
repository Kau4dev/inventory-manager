package com.projedata.inventory_manager.service;

import com.projedata.inventory_manager.mapper.RawMaterialMapper;
import com.projedata.inventory_manager.repository.RawMaterialRepository;
import com.projedata.inventory_manager.model.RawMaterial;
import com.projedata.inventory_manager.dto.rawMaterial.RawMaterialCreatedDTO;
import com.projedata.inventory_manager.dto.rawMaterial.RawMaterialViewDTO;
import com.projedata.inventory_manager.dto.rawMaterial.RawMaterialUpdateDTO;
import com.projedata.inventory_manager.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;
    private final RawMaterialMapper rawMaterialMapper;

    @Transactional
    public RawMaterialCreatedDTO createRawMaterial(RawMaterialCreatedDTO rawMaterialCreatedDTO) {
        RawMaterial rawMaterial = rawMaterialMapper.toEntity(rawMaterialCreatedDTO);
        RawMaterial saved = rawMaterialRepository.save(rawMaterial);
        return rawMaterialMapper.toCreatedDTO(saved);
    }

    public RawMaterialViewDTO getRawMaterialById(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", id));
        return rawMaterialMapper.toViewDTO(rawMaterial);
    }

    public List<RawMaterialViewDTO> getAllRawMaterials() {
        List<RawMaterial> rawMaterials = rawMaterialRepository.findAll();
        return rawMaterials.stream()
                .map(rawMaterialMapper::toViewDTO)
                .toList();

    }

    @Transactional
    public RawMaterialUpdateDTO updateRawMaterial(Long id, RawMaterialUpdateDTO rawMaterialUpdateDTO) {
        RawMaterial existingRawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", id));

        rawMaterialMapper.updateFromDto(rawMaterialUpdateDTO, existingRawMaterial);

        RawMaterial updatedRawMaterial = rawMaterialRepository.save(existingRawMaterial);
        return rawMaterialMapper.toUpdateDTO(updatedRawMaterial);
    }

    @Transactional
    public void deleteRawMaterial(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", id));
        rawMaterialRepository.delete(rawMaterial);
    }

}
