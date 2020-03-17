package org.icann.vendingmachine.impl;

import java.util.Map;

import org.icann.vendingmachine.VendingMachine;
import org.icann.vendingmachine.model.Coin;
import org.icann.vendingmachine.service.IConsumerService;
import org.icann.vendingmachine.service.ISupplierService;
import org.icann.vendingmachine.service.impl.SodaSupplierService;

/**
 * One of the the concrete class for VendingMachine, only has two coin related methods, all other methods are in the parent VendingMachine class
 * 
 * @author Jin
 *
 */
public class CoinSodaVendingMachine extends VendingMachine {
	public CoinSodaVendingMachine(IConsumerService consumerService, ISupplierService supplierService) {
		super(consumerService, supplierService);
	
		// to add a (coin : 0) entry in the map, so we do not need to check for nulls later
		for (Coin coin : Coin.values()) {
			coinInventory.put(coin,  0);
		}
	}
	
	/**
	 * 	Accepts coins of 1, 5, 10, 25 Cents i.e., penny, nickel, dime, and quarter
	 * 
	 * 	@param coin
	 */
	public int insertCoin(Coin coin) {
		System.out.println("coin received " + coin);
		
		tenderedAmount += coin.getValue();
		coinInventory.put(coin,  coinInventory.get(coin) + 1);

		return tenderedAmount;
	}
	
	/**
	 * Allow supplier to refill products
	 * 
	 * @param map of products
	 */
	public void refillCoin(Map<Coin, Integer> coins) {
		((SodaSupplierService)supplierService).refillCoin(coins, this);
	}
}
