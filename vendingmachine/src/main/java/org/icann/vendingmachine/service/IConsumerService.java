package org.icann.vendingmachine.service;

import java.util.Map;

import org.icann.vendingmachine.VendingMachine;
import org.icann.vendingmachine.exception.NoExactChangeException;
import org.icann.vendingmachine.exception.NoInventoryException;
import org.icann.vendingmachine.exception.NotFullyPaidException;
import org.icann.vendingmachine.model.Coin;
import org.icann.vendingmachine.model.Product;
import org.icann.vendingmachine.model.ProductAndCoinChange;

/**
 * Interface for consumer service
 * 
 * @author Jin
 *
 */
public interface IConsumerService {
	public int selectProduct(Product product, Map<Product, Integer> productInventory, VendingMachine vendingMachine) throws NoInventoryException;
	public Map<Coin, Integer> cancelPurchase(VendingMachine vendingMachine);
	public ProductAndCoinChange confirmPurchase(VendingMachine vendingMachine) throws NoExactChangeException, NotFullyPaidException;
}
