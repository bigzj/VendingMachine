package org.icann.vendingmachine.factory.impl;

import org.icann.vendingmachine.VendingMachine;
import org.icann.vendingmachine.factory.IVendingMachineFactory;
import org.icann.vendingmachine.impl.CoinSodaVendingMachine;
import org.icann.vendingmachine.service.IConsumerService;
import org.icann.vendingmachine.service.ISupplierService;
import org.icann.vendingmachine.service.impl.CoinConsumerService;
import org.icann.vendingmachine.service.impl.SodaSupplierService;

/**
 * Factory method to create different type of vending machines and injecting the correct consumer and supplier services
 * 
 * @author Jin
 *
 */
public class VendingMachineFactory implements IVendingMachineFactory {
	
	public VendingMachine createVendingMachine(String type) {
		VendingMachine vendingMachine = null;
		
		 if ("soda".equals(type)) {
			 IConsumerService consumerService = new CoinConsumerService();
			 ISupplierService supplierService = new SodaSupplierService();
			 
			 System.out.println("CoinSodaVendingMachine created");
			 
			 vendingMachine = new CoinSodaVendingMachine(consumerService, supplierService);
		 } else {
			 throw new IllegalArgumentException("No such vending machine implemented");
		 }
		 
		 return vendingMachine;
	}
}
