package com.projedata.inventory_manager.repository;

import com.projedata.inventory_manager.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {


}
