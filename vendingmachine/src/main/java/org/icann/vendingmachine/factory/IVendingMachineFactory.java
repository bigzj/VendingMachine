package org.icann.vendingmachine.factory;

import org.icann.vendingmachine.VendingMachine;

/**
 * Interface for the vending machine factory
 * 
 * @author Jin
 *
 */
public interface IVendingMachineFactory {
	public VendingMachine createVendingMachine(String type);
}
