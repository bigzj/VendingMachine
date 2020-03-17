package org.icann.vendingmachine.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.icann.vendingmachine.VendingMachine;
import org.icann.vendingmachine.exception.NoExactChangeException;
import org.icann.vendingmachine.exception.NoInventoryException;
import org.icann.vendingmachine.exception.NotFullyPaidException;
import org.icann.vendingmachine.model.Coin;
import org.icann.vendingmachine.model.Product;
import org.icann.vendingmachine.model.ProductAndCoinChange;
import org.icann.vendingmachine.service.IConsumerService;

/**
 * Concrete class for consumer service for the coin soda vending machine
 * 
 * @author Jin
 *
 */
public class CoinConsumerService implements IConsumerService {
	/**
	 * 	Accepts coins of 1, 5, 10, 25 Cents i.e., penny, nickel, dime, and quarter
	 * 
	 * 	@param coin
	 */
	public int insertCoin(Coin coin, VendingMachine vendingMachine) {
		System.out.println("coin received " + coin);
		
		vendingMachine.setTenderedAmount(vendingMachine.getTenderedAmount() + coin.getValue());
		vendingMachine.getCoinInventory().put(coin,  vendingMachine.getCoinInventory().get(coin) + 1);
		
		return vendingMachine.getTenderedAmount();
	}
	
	/**
	 * Allow user to select products
	 * 
	 * @param product
	 * @return the price of the product selected
	 * @throws NoInventoryException if there is no inventory of the selected product
	 */
	public int selectProduct(Product product, Map<Product, Integer> productInventory, VendingMachine vendingMachine) throws NoInventoryException {
		if (productInventory.get(product) == null || productInventory.get(product) == 0) {
			System.out.println(product + " is sold out");
			
			throw new NoInventoryException(product + " is sold out, please chose another product or press refund");
		}
		
		vendingMachine.setSelectedProduct(product);
		
		return product.getPrice();
	}
	
	/**
	 * Return selected product and remaining change if any
	 * 
	 * @return Product and change
	 * 
	 * @throws NoExactChangeException if no exact change could be given
	 * @throws NotFullyPaidException if tenderedAmount is less than the price of the selectedProduct
	 */
	public ProductAndCoinChange confirmPurchase(VendingMachine vendingMachine) throws NoExactChangeException, NotFullyPaidException {
		int tenderedAmount = vendingMachine.getTenderedAmount();
		Product product = vendingMachine.getSelectedProduct();
		
		int changeInCents =  tenderedAmount - product.getPrice();
		
		if (changeInCents < 0) {
			System.out.println("Not fully paid, " + tenderedAmount + " < " + product + ": " + product.getPrice() );
			
			throw new NotFullyPaidException("Need " + (- changeInCents) + " cents to complete the purchase");
		}
		
		Map<Coin, Integer> change = getCoinsForChange(changeInCents, vendingMachine.getCoinInventory());
		
		if (change == null) {
			System.out.println("No exact change for " + changeInCents);
			
			throw new NoExactChangeException("Machine cannot give you the exact change");
		}
		
		// reduce product inventory
		vendingMachine.getProductInventory().put(product, vendingMachine.getProductInventory().get(product) - 1);
		
		// reduce coin inventory
		reduceCoinInventory(change, vendingMachine);
		
		tenderedAmount = 0;

		ProductAndCoinChange pc = new ProductAndCoinChange(product, change);
		vendingMachine.setSelectedProduct(null);
		
		System.out.println("purchase confirmed for " + pc);
		
		return pc; 
	}
	
	/**
	 * Allow user to take refund by canceling the request
	 * 
	 * @return refund
	 */
	public Map<Coin, Integer> cancelPurchase(VendingMachine vendingMachine) {
		Map<Coin, Integer> change = getCoinsForChange(vendingMachine.getTenderedAmount(), vendingMachine.getCoinInventory());
		
		System.out.println("cancelling purchase, tendered amount = " + vendingMachine.getTenderedAmount() + ", change = " + change);
		
		// reduce coin inventory
		reduceCoinInventory(change, vendingMachine);
		
		vendingMachine.setTenderedAmount(0);
		vendingMachine.setSelectedProduct(null);
		
		return change;
	}
	
	private void reduceCoinInventory(Map<Coin, Integer> coins, VendingMachine vendingMachine) {
		for (Coin coin : coins.keySet()) {
			vendingMachine.getCoinInventory().put(coin, vendingMachine.getCoinInventory().get(coin) - coins.get(coin));
		}
	}
	
	private Map<Coin, Integer> getCoinsForChange(int cents, Map<Coin, Integer> coinInventory) {
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


}
