package com.Salora.SaloraWebService.Repository;

import com.Salora.SaloraWebService.Model.Product;
import com.Salora.SaloraWebService.Model.ProductCategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryTypeRepository extends JpaRepository<ProductCategoryType, String> {
}
