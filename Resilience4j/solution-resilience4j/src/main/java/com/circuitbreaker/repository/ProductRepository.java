package com.circuitbreaker.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.circuitbreaker.model.Product;

public class ProductRepository {
	private List<Product> products;
	
	public ProductRepository() {
        products = Arrays.asList(
                new Product(1, "iPhone", 300),
                new Product(2, "Android", 300),
                new Product(3, "XBOX ONE", 400),
                new Product(4, "PS5", 400)
        );
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public List<Product> findByQuantity(int qty) {
        List<Product> result = new ArrayList<Product>();
		for(Product p : this.products) {
        	if(p.getQuantity() == qty) {
        		result.add(p);
        	}
        }
		return result;
	}
}
