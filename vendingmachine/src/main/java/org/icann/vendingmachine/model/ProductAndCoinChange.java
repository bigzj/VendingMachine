package org.icann.vendingmachine.model;

import java.util.Map;

/**
 * The return type of the completePurchase() method. It has a product and a map for the coins and count as the change.
 * For machines that does not accept coin or cash, the change could be null
 *  
 * @author Jin
 *
 */
public class ProductAndCoinChange {
	private Product product;
	private Map<Coin, Integer> change;
	
	public ProductAndCoinChange(Product product, Map<Coin, Integer> change) {
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
