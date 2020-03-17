package org.icann.vendingmachine.service;

import java.util.Map;

import org.icann.vendingmachine.VendingMachine;
import org.icann.vendingmachine.model.Coin;
import org.icann.vendingmachine.model.Product;

/**
 * Interface for supplier service
 * 
 * @author Jin
 *
 */
public interface ISupplierService {
	public Map<Coin, Integer> withdraw(VendingMachine vendingMachine);	
	public void refillProduct(Map<Product, Integer> products, VendingMachine vendingMachine);
	public int getProductCount(Product product, VendingMachine vendingMachine);
}
