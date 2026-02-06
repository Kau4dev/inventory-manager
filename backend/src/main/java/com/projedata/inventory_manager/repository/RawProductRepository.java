package com.projedata.inventory_manager.repository;

import com.projedata.inventory_manager.model.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawProductRepository extends JpaRepository<RawMaterial, Long> {
}
