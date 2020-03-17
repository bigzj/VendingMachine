package org.icann.vendingmachine;

import java.util.HashMap;
import java.util.Map;

import org.icann.vendingmachine.exception.NoExactChangeException;
import org.icann.vendingmachine.exception.NoInventoryException;
import org.icann.vendingmachine.exception.NotFullyPaidException;
import org.icann.vendingmachine.model.Coin;
import org.icann.vendingmachine.model.Product;
import org.icann.vendingmachine.model.ProductAndCoinChange;
import org.icann.vendingmachine.service.IConsumerService;
import org.icann.vendingmachine.service.ISupplierService;

/**
 * Abstract class provides common operations for all types of vending machines
 * 
 * @author Jin
 *
 */
public abstract class VendingMachine {
	// service class handles all consumer related operations
	protected IConsumerService consumerService = null;
	// service class handles all supplier related operations
	protected ISupplierService supplierService = null;
	
	// key is product, such as coke, soda; value is number of products the machine has
	protected Map<Product, Integer> productInventory = new HashMap<Product, Integer>();
	
	// key is coin, such as penny, dime; value is the number of coins the machine has
	// could be all zeros for machines that does not take coins
	protected Map<Coin, Integer> coinInventory = new HashMap<Coin, Integer>();
	
	// selected product for the current transaction
	protected Product selectedProduct = null;

	// money received so far for the current transaction
	protected int tenderedAmount = 0;

	// constructor
	public VendingMachine(IConsumerService consumerService, ISupplierService supplierService) {
		this.consumerService = consumerService;
		this.supplierService = supplierService;
	}
	
	/**
	 * Allow user to select products
	 * 
	 * @param product
	 * @return the price of the product selected
	 * @throws NoInventoryException if there is no inventory of the selected product
	 */
	public int selectProduct(Product product) throws NoInventoryException {
		return consumerService.selectProduct(product, productInventory, this);
	}
	
	/**
	 * Return selected product and remaining change if any
	 * 
	 * @return Product and change
	 * 
	 * @throws NoExactChangeException if no exact change could be given
	 * @throws NotFullyPaidException if tenderedAmount is less than the price of the selectedProduct
	 */
	public ProductAndCoinChange confirmPurchase() throws NoExactChangeException, NotFullyPaidException {
		return consumerService.confirmPurchase(this);
	}
	
	/**
	 * Allow user to take refund by canceling the request
	 * 
	 * @return refund
	 */
	public Map<Coin, Integer> cancelPurchase() {
		return consumerService.cancelPurchase(this);
	}
	
	
	/**
	 * Allow supplier to withdraw money from machine
	 * 
	 * @return map of coins
	 */
	public Map<Coin, Integer> withdraw() {
		return supplierService.withdraw(this);
	}
	
	/**
	 * Allow supplier to refill products
	 * 
	 * @param map of products
	 */
	public void refillProduct(Map<Product, Integer> products) {
		supplierService.refillProduct(products, this);
	}
	
	/**
	 * Allow supplier to get count of product
	 * 
	 * @param product
	 * @return
	 */
	public int getProductCount(Product product) {
		int count = productInventory.get(product);
		
		System.out.println("getting product count for " + product + ": " + count);
		
		return count;
	}
	
	// getters and setters below
	public Map<Product, Integer> getProductInventory() {
		return productInventory;
	}

	public void setProductInventory(Map<Product, Integer> productInventory) {
		this.productInventory = productInventory;
	}

	public Product getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(Product selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public int getTenderedAmount() {
		return tenderedAmount;
	}

	public void setTenderedAmount(int tenderedAmount) {
		this.tenderedAmount = tenderedAmount;
	}
	
	public Map<Coin, Integer> getCoinInventory() {
		return coinInventory;
	}

	public void setCoinInventory(Map<Coin, Integer> coinInventory) {
		this.coinInventory = coinInventory;
	}
}
