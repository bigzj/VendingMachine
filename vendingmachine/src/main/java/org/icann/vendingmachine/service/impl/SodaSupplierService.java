package org.icann.vendingmachine.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.icann.vendingmachine.VendingMachine;
import org.icann.vendingmachine.model.Coin;
import org.icann.vendingmachine.model.Product;
import org.icann.vendingmachine.service.ISupplierService;

/**
 * Concrete class for supplier service for the coin soda vending machine
 * 
 * @author Jin
 *
 */
public class SodaSupplierService implements ISupplierService {
	/**
	 * Allow supplier to withdraw money from machine
	 * 
	 * @return map of coins
	 */
	public Map<Coin, Integer> withdraw(VendingMachine vendingMachine) {
		System.out.println("withdrawing all coins");
		
		Map<Coin, Integer> change =  new HashMap<Coin, Integer>();
		
		Map<Coin, Integer> coinInventory = vendingMachine.getCoinInventory();
		change.putAll(coinInventory);
		
		for (Coin coin : Coin.values()) {
			coinInventory.put(coin,  0);
		}
		
		return change;
	}
	
	/**
	 * Allow supplier to refill products
	 * 
	 * @param map of products
	 */
	public void refillProduct(Map<Product, Integer> products, VendingMachine vendingMachine) {
		System.out.println("refilling product: " + products);
		
		Map<Product, Integer> productInventory = vendingMachine.getProductInventory();
		
		if (products != null) {
			for (Product product : products.keySet()) {
				System.out.println("adding " + products.get(product) + " " + product);
				
				if (productInventory.get(product) != null) {
					productInventory.put(product, productInventory.get(product) + products.get(product));	
				} else {
					productInventory.put(product,  products.get(product));
				}
			}
		}
	}
	
	/**
	 * Allow supplier to refill products
	 * 
	 * @param map of products
	 */
	public void refillCoin(Map<Coin, Integer> coins, VendingMachine vendingMachine) {
		System.out.println("refilling coins: " + coins);
		
		if (coins != null) {
			for (Coin coin : coins.keySet()) {
				System.out.println("adding " + coins.get(coin) + " " + coin);
				
				vendingMachine.getCoinInventory().put(coin, vendingMachine.getCoinInventory().get(coin) + coins.get(coin));
			}
		}
	}
	
	/**
	 * Allow supplier to get count of product
	 * 
	 * @param product
	 * @return
	 */
	public int getProductCount(Product product, VendingMachine vendingMachine) {
		int count = vendingMachine.getProductInventory().get(product);
		
		System.out.println("getting product count for " + product + ": " + count);
		
		return count;
	}
}
