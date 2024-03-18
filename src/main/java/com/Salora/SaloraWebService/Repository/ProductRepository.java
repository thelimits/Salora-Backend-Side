package com.Salora.SaloraWebService.Repository;

import com.Salora.SaloraWebService.Model.Enums.CategoryType;
import com.Salora.SaloraWebService.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT p FROM Product p JOIN p.productCategory pc JOIN pc.categoryTypes pct WHERE pct.type IN (:types)")
    Page<Product> findByType(@Param("types") List<CategoryType> types, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findProductById(@Param("id") String id);

}
