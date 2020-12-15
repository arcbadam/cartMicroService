package com.ibm.cart.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.ibm.cart.entity.Cart;


@Component
public interface CartDAO extends JpaRepository<Cart, Integer> {

}
