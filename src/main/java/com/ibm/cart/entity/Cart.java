package com.ibm.cart.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "cart")
public class Cart {

	@Id
	private int productID;
	private int quantity;
	private String description;
	private double price;
	private String transId;
	
	public Cart() {
	}
	public Cart(int productID, int quantity, String description, double price, String transId) {
		super();
		this.productID = productID;
		this.quantity = quantity;
		this.description = description;
		this.price = price;
		this.transId = transId;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}

}
