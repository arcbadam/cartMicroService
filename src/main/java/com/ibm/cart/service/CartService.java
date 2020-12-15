package com.ibm.cart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ibm.cart.client.ProductValidationClient;
import com.ibm.cart.entity.Cart;
import com.ibm.cart.entity.Product;
import com.ibm.cart.repository.CartDAO;

@Service
public class CartService {

	@Autowired
	private CartDAO cartDAO;
	@Autowired
	private ProductValidationClient productvalidatorClient;
	@Autowired
	private LoadBalancerClient lbClient;

	@Autowired
	private DiscoveryClient discoveryClient;

	public List<Cart> getAllCarts() {
		return cartDAO.findAll();
	}

	public Cart getCart(int cartId) {
		Optional<Cart> carts = cartDAO.findById(cartId);
		Cart cart = null;
		if (carts != null) {
			cart = carts.get();
		}
		return cart;
	}

	public String addProduct(int productId) { ResponseEntity<Product> response = productvalidatorClient.validateProduct(productId);
	if(HttpStatus.NOT_FOUND.equals(response.getStatusCode() )){
		return "Product #"+ productId + " not valid !!";
	}
	Product product = response.getBody();
	System.out.println(product);
	boolean exists = cartDAO.existsById(productId);
	System.out.println("exists:::::::::::::::::::::::::::"+exists);
	String transId = null;
	if (!exists) {
		if(this.getAllCarts() != null && this.getAllCarts().size()==0) {
			transId = getRandom();
		}else {
			transId = this.getAllCarts().get(0).getTransId();
		}
		cartDAO.save(new Cart(productId, 1, product.getProductDesc(), product.getProductPrice(),transId));
	} else {
		Cart cart = getCart(productId);
		System.out.println("this.getAllOrders()::::::::::::"+this.getAllCarts().size());
		if(this.getAllCarts() != null && this.getAllCarts().size()==0) {
			transId = getRandom();
		}else {
			transId = this.getAllCarts().get(0).getTransId();
		}
		cartDAO.save(new Cart(productId, cart.getQuantity() + 1, product.getProductDesc(),
				cart.getPrice() + product.getProductPrice(),transId));
	} return "Product id " + productId + " added successfully.";
	}
	
	public String addProductItems(Product product) { 
		ResponseEntity<Product> response = productvalidatorClient.validateProduct(product.getProductCode());
		if(HttpStatus.NOT_FOUND.equals(response.getStatusCode() )){
			return "Product #"+ product.getProductCode() + " not valid !!";
		}
		//Product product = response.getBody();
		System.out.println(product);
		boolean exists = cartDAO.existsById(product.getProductCode());
		System.out.println("exists:::::::::::::::::::::::::::"+exists);
		String transId = null;
		if (!exists) {
			if(this.getAllCarts() != null && this.getAllCarts().size()==0) {
				transId = getRandom();
			}else {
				transId = this.getAllCarts().get(0).getTransId();
			}
			cartDAO.save(new Cart(product.getProductCode(), product.getQty(), product.getProductDesc(), product.getProductPrice(),transId));
		} else {
			Cart cart = getCart(product.getProductCode());
			System.out.println("this.getAllOrders()::::::::::::"+this.getAllCarts().size());
			if(this.getAllCarts() != null && this.getAllCarts().size()==0) {
				transId = getRandom();
			}else {
				transId = this.getAllCarts().get(0).getTransId();
			}
			cartDAO.save(new Cart(product.getProductCode(), product.getQty(), product.getProductDesc(),
					cart.getPrice() + product.getProductPrice(),transId));
		} 
		return "Product id " + product.getProductCode() + " added successfully.";
	}

	private String getRandom() {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "0123456789"
				+ "abcdefghijklmnopqrstuvxyz";     
		StringBuilder sb = new StringBuilder(20);
		for (int i = 0; i < 20; i++) { 
			int index 
			= (int)(AlphaNumericString.length()
					* Math.random()); 
			sb.append(AlphaNumericString 
					.charAt(index));
		}
		return sb.toString();
	}

	public String deleteProduct(int productId) {
		// Product product = productvalidatorClient.validateProduct(productId);
		
		Cart cart = getCart(productId);
		if (cart == null) {

		}  else {
			cartDAO.deleteById(productId);
		}

		return "Product id " + productId + " deleted successfully.";
	}

	public String clearCart() {
		cartDAO.deleteAll();
		return "Cart cleared successfully.";
	}

	/*
	 * public String checkoutCart() { cartDAO.deleteAll(); return
	 * "Cart checkout successfully."; }
	 */
	public String test() {

		ServiceInstance instance = lbClient.choose("products");
		//String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/default";
		List<ServiceInstance> instances = discoveryClient.getInstances("products");
		System.out.println(instances);
		for (ServiceInstance inst : instances) {
			System.out.println(inst.getHost() + ":" + inst.getPort());
		}

		instances = discoveryClient.getInstances("user-authentication");
		for (ServiceInstance inst : instances) {
			System.out.println(inst.getHost() + ":" + inst.getPort());
		}
		System.out.println(productvalidatorClient);
		System.out.println(productvalidatorClient.test());
		System.out.println(productvalidatorClient.validateProduct(6));
		String url = "http://"+instances.get(0).getHost() + ":" + instances.get(0).getPort()+"/default";
		System.out.println(instance + "  ::: URL is  " + url);
		RestTemplate restTemplate = new RestTemplate();

		HttpEntity<String> discountEntityResponse = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		String response = discountEntityResponse.getBody();
		return response; 
		//return productvalidatorClient.test();
	}
}
