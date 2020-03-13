package org.icann.vendingmachine.model;

import java.util.Map;

public class ProductAndChange {
	private Product product;
	private Map<Coin, Integer> change;
	
	public ProductAndChange(Product product, Map<Coin, Integer> change) {
		this.product = product;
		this.change = change;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public Map<Coin, Integer> getChange() {
		return change;
	}
	
	@Override
	public String toString() {
		return product + ", " + change;
	}
}
