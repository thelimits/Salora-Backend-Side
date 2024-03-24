package com.Salora.SaloraWebService.Repository;

import com.Salora.SaloraWebService.DTO.ResponseDTO.GetCartResponseDTO;
import com.Salora.SaloraWebService.Model.Enums.CategoryType;
import com.Salora.SaloraWebService.Model.Enums.SizeProduct.Size;
import com.Salora.SaloraWebService.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT p FROM Product p JOIN p.productCategory pc JOIN pc.categoryTypes pct WHERE pct.type IN (:types)")
    Page<Product> findByType(@Param("types") List<CategoryType> types, Pageable pageable);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.attributes pa WHERE p.id = :id")
    Optional<Product> findProductById(@Param("id") String id);

    @Query("SELECT p FROM Product p JOIN FETCH p.attributes pa WHERE p.id = :id AND " +
            "(:size = pa.sizeType.clothingSize OR " +
            ":size = pa.sizeType.sneakersSize OR " +
            ":size = pa.sizeType.oneSize)")
    Optional<Product> findProductByIdAndSize(@Param("id") String id, @Param("size") Size size);

    @Query("SELECT p FROM Product p JOIN FETCH p.wishlists pw JOIN pw.user pwu WHERE pwu.id = :id")
    List<Product> findWishlistProductByUserId(@Param("id") String id);

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN FETCH p.carts c " +
            "JOIN FETCH p.attributes " +
            "WHERE c.user.id = :userId")
    List<Product> findCartProductByUserId(@Param("userId") String userId);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE ProductAttribute pa " +
            "SET pa.stock = pa.stock - :newStock " +
            "WHERE pa.product.id = :productId AND " +
            "(:size = pa.sizeType.clothingSize OR " +
            ":size = pa.sizeType.sneakersSize OR " +
            ":size = pa.sizeType.oneSize)")
    void updateStockByProductIdAndSize(@Param("productId") String productId, @Param("newStock") Integer newStock, @Param("size") Size size);

}
