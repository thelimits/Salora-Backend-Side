package com.Salora.SaloraWebService.Repository;

import com.Salora.SaloraWebService.Model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishlistRepository extends JpaRepository<Wishlist, String> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Wishlist w WHERE w.user.id = :id AND w.product.id = :productId")
    void deleteWishlist(@Param("id") String id, @Param("productId") String productId);
}
