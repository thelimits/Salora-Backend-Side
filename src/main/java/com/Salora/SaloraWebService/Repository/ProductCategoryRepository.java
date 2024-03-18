package com.Salora.SaloraWebService.Repository;

import com.Salora.SaloraWebService.Model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String>  {
}
