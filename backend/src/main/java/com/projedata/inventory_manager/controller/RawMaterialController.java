package com.projedata.inventory_manager.controller;

import com.projedata.inventory_manager.dto.rawMaterial.RawMaterialCreatedDTO;
import com.projedata.inventory_manager.dto.rawMaterial.RawMaterialUpdateDTO;
import com.projedata.inventory_manager.dto.rawMaterial.RawMaterialViewDTO;
import com.projedata.inventory_manager.service.RawMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rawMaterials")
@RequiredArgsConstructor
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @PostMapping
    public ResponseEntity<RawMaterialCreatedDTO> createRawMaterial(@RequestBody @Valid RawMaterialCreatedDTO request) {
        RawMaterialCreatedDTO response = rawMaterialService.createRawMaterial(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")

    public ResponseEntity<RawMaterialViewDTO> getRawMaterialById(@PathVariable Long id) {
        RawMaterialViewDTO response = rawMaterialService.getRawMaterialById(id);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RawMaterialViewDTO>> getAllRawMaterials() {
        List<RawMaterialViewDTO> responses = rawMaterialService.getAllRawMaterials();
        return ResponseEntity.status(200).body(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RawMaterialUpdateDTO> updateRawMaterial(@PathVariable Long id, @RequestBody @Valid RawMaterialUpdateDTO request) {
        RawMaterialUpdateDTO response = rawMaterialService.updateRawMaterial(id, request);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRawMaterial(@PathVariable Long id) {
        rawMaterialService.deleteRawMaterial(id);
        return ResponseEntity.status(204).build();
    }

}
