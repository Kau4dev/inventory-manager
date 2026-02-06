package com.projedata.inventory_manager.repository;

import com.projedata.inventory_manager.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM product p WHERE NOT EXISTS (" +
            "SELECT pm FROM product_material pm WHERE pm.product = p AND pm.requiredQuantity > pm.material.stockQuantity)")
    List<Product> findProducibleProducts();

    @Query("SELECT DISTINCT p FROM product p LEFT JOIN FETCH p.materials pm LEFT JOIN FETCH pm.material")
    List<Product> findAllWithMaterials();
}
