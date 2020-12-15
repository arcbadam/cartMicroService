package com.ibm.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.cart.entity.Cart;
import com.ibm.cart.entity.Product;
import com.ibm.cart.service.CartService;

@RestController
@RefreshScope
public class OrderController {
	
	@Autowired
	CartService cartService;
		
	
	@GetMapping(path = "/default")
	public String defaultMessage() {
		return "Welcome to Cart service";
	}

	@GetMapping(path = "/getCart", produces = "application/json")
	public ResponseEntity<List<Cart>> getAllCarts() {
		return new ResponseEntity<>(cartService.getAllCarts(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/addCart/{productId}", produces = "application/json")
	public ResponseEntity<String> addcart(@PathVariable int productId) {
		return new ResponseEntity<>(cartService.addProduct(productId), HttpStatus.OK);
	}
	
	@PostMapping(path = "/addCart", produces = "application/json")
	public ResponseEntity<String> addcartItems(@RequestBody Product dto) {
		return new ResponseEntity<>(cartService.addProductItems(dto), HttpStatus.OK);
	}
	
	@GetMapping(path = "/deleteCart/{productId}", produces = "application/json")
	public ResponseEntity<String> deleteCart(@PathVariable int productId) {
		return new ResponseEntity<>(cartService.deleteProduct(productId), HttpStatus.OK);
	}
	
	@GetMapping(path = "/clearCart", produces = "application/json")
	public ResponseEntity<String> clearCart() {
		return new ResponseEntity<>(cartService.clearCart(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/checkoutCart", produces = "application/json")
	public ResponseEntity<String> checkoutCart() {
		String msg = cartService.clearCart();
		
		return new ResponseEntity<>( msg, HttpStatus.OK );
	}

	@GetMapping(path = "/test")
	public String test() {
		return cartService.test();
	}
}
