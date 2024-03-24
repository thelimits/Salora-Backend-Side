package com.Salora.SaloraWebService.Repository;

import com.Salora.SaloraWebService.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, String> {
}
