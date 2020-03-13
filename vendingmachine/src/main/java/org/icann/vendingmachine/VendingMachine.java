package org.icann.vendingmachine;

import java.util.HashMap;
import java.util.Map;

import org.icann.vendingmachine.exception.NoExactChangeException;
import org.icann.vendingmachine.exception.NoInventoryException;
import org.icann.vendingmachine.exception.NotFullyPaidException;
import org.icann.vendingmachine.model.Coin;
import org.icann.vendingmachine.model.Product;
import org.icann.vendingmachine.model.ProductAndChange;

public class VendingMachine {
	// money received so far for the current transaction
	private int tenderedAmount = 0;
	// selected product for the current transaction
	private Product selectedProduct = null;
	
	// key is product, such as coke, soda; value is number of products the machine has
	private Map<Product, Integer> productInventory = new HashMap<Product, Integer>();
	// key is coin, such as penny, dime; value is the number of coins the machine has
	private Map<Coin, Integer> coinInventory = new HashMap<Coin, Integer>();
	
	
	/**
	 * Create new Vending Machine
	 * 
	 * @param products key is product, value is number
	 * @param coins key is coin, value is number
	 */
	public VendingMachine(Map<Product, Integer> products, Map<Coin, Integer> coins) {
		if (products == null) {
			// safety check
			products = new HashMap<Product, Integer>();
		}
		
		if (coins == null) {
			// safety check
			coins = new HashMap<Coin, Integer>();
		}
		
		// to add a (coin : 0) entry in the map, so we do not need to check for nulls later
		for (Coin coin : Coin.values()) {
			coinInventory.put(coin,  coins.get(coin) == null ? 0 : coins.get(coin));
		}
		
		// to add a (product : 0) entry in the map, so we do not need to check for nulls later
		for (Product product : Product.values()) {
			productInventory.put(product, products.get(product) == null ? 0 : products.get(product));
		}
	}
	
	/**
	 * 	Accepts coins of 1, 5, 10, 25 Cents i.e., penny, nickel, dime, and quarter
	 * 
	 * 	@param coin
	 */
	public int insertCoin(Coin coin) {
		log("coin received " + coin);
		
		tenderedAmount += coin.getValue();
		coinInventory.put(coin,  coinInventory.get(coin) + 1);
		
		return tenderedAmount;
	}
	
	/**
	 * Allow user to select products Coke (25), Pepsi (35), Soda (45)
	 * 
	 * @param product
	 * @return the price of the product selected
	 * @throws NoInventoryException if there is no inventory of the selected product
	 */
	public int selectProduct(Product product) throws NoInventoryException {
		if (productInventory.get(product) == 0) {
			log(product + " is sold out");
			
			throw new NoInventoryException(product + " is sold out, please chose another product or press refund");
		}
		
		selectedProduct = product;
		
		return product.getPrice();
	}
	
	/**
	 * Allow user to take refund by canceling the request
	 * 
	 * @return refund
	 */
	public Map<Coin, Integer> cancelPurchase() {
		Map<Coin, Integer> change = getCoinsForChange(tenderedAmount);
		
		log("cancelling purchase, tendered amount = " + tenderedAmount + ", change = " + change);
		
		// reduce coin inventory
		reduceCoinInventory(change);
		
		tenderedAmount = 0;		
		selectedProduct = null;
		
		return change;
	}
	
	/**
	 * Return selected product and remaining change if any
	 * 
	 * @return Product and change
	 * 
	 * @throws NoExactChangeException if no exact change could be given
	 * @throws NotFullyPaidException if tenderedAmount is less than the price of the selectedProduct
	 */
	public ProductAndChange confirmPurchase() throws NoExactChangeException, NotFullyPaidException {
		int changeInCents = tenderedAmount - selectedProduct.getPrice();
		
		if (changeInCents < 0) {
			log("Not fully paid, " + tenderedAmount + " < " + selectedProduct + ": " + selectedProduct.getPrice() );
			
			throw new NotFullyPaidException("Need " + (- changeInCents) + " cents to complete the purchase");
		}
		
		Map<Coin, Integer> change = getCoinsForChange(changeInCents);
		
		if (change == null) {
			log("No exact change for " + changeInCents);
			
			throw new NoExactChangeException("Machine cannot give you the exact change");
		}
		
		// reduce product inventory
		productInventory.put(selectedProduct, productInventory.get(selectedProduct) - 1);
		
		// reduce coin inventory
		reduceCoinInventory(change);
		
		tenderedAmount = 0;

		ProductAndChange pc = new ProductAndChange(selectedProduct, change);
		selectedProduct = null;
		
		log("purchase confirmed for " + pc);
		
		return pc; 
	}
	
	/**
	 * Allow supplier to withdraw money from machine
	 * 
	 * @return map of coins
	 */
	public Map<Coin, Integer> withdraw() {
		log("withdrawing all coins");
		
		Map<Coin, Integer> change =  new HashMap<Coin, Integer>();
		change.putAll(coinInventory);
		
		reduceCoinInventory(change);
		
		return change;
	}
	
	/**
	 * Allow supplier to refill products
	 * 
	 * @param map of products
	 */
	public void refillProduct(Map<Product, Integer> products) {
		log("refilling product: " + products);
		
		if (products != null) {
			for (Product product : products.keySet()) {
				log("adding " + products.get(product) + " " + product);
				
				productInventory.put(product, productInventory.get(product) + products.get(product));
			}
		}
	}
	
	/**
	 * Allow supplier to get count of product
	 * 
	 * @param product
	 * @return
	 */
	public int getProductCount(Product product) {
		int count = productInventory.get(product);
		
		log("getting product count for " + product + ": " + count);
		
		return count;
	}
	
	
	private Map<Coin, Integer> getCoinsForChange(int cents) {
		Map<Coin, Integer> coins = new HashMap<Coin, Integer>();
		
		while (cents > 0) {
			if (cents >= Coin.QUARTER.getValue() && coinInventory.get(Coin.QUARTER) >= cents/Coin.QUARTER.getValue()) {
				if (coins.get(Coin.QUARTER) != null) {
					coins.put(Coin.QUARTER, coins.get(Coin.QUARTER) + 1);
				} else {
					coins.put(Coin.QUARTER, 1);
				}
				
				cents -= Coin.QUARTER.getValue();
			} else if (cents >= Coin.DIME.getValue() && coinInventory.get(Coin.DIME) >= cents/Coin.DIME.getValue()) {
				if (coins.get(Coin.DIME) != null) {
					coins.put(Coin.DIME, coins.get(Coin.DIME) + 1);
				} else {
					coins.put(Coin.DIME, 1);
				}
				
				cents -= Coin.DIME.getValue();
			} else if (cents >= Coin.NICKLE.getValue() && coinInventory.get(Coin.NICKLE) >= cents/Coin.NICKLE.getValue()) {
				if (coins.get(Coin.NICKLE) != null) {
					coins.put(Coin.NICKLE, coins.get(Coin.NICKLE) + 1);
				} else {
					coins.put(Coin.NICKLE, 1);
				}
				
				cents -= Coin.NICKLE.getValue();
			} else if (cents >= Coin.PENNY.getValue() && coinInventory.get(Coin.PENNY) >= cents/Coin.PENNY.getValue()) {
				if (coins.get(Coin.PENNY) != null) {
					coins.put(Coin.PENNY, coins.get(Coin.PENNY) + 1);
				} else {
					coins.put(Coin.PENNY, 1);
				}

				cents -= Coin.PENNY.getValue();
			} else {
				return null;
			}
		}
		
		return coins;
	}
	
	private void reduceCoinInventory(Map<Coin, Integer> coins) {
		for (Coin coin : coins.keySet()) {
			coinInventory.put(coin, coinInventory.get(coin) - coins.get(coin));
		}
	}
	
	private void log(Object obj) {
		System.out.println(obj);
	}
}
