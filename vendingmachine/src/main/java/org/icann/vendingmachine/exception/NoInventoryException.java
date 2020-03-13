package org.icann.vendingmachine.exception;

/**
 * Thrown when the machine does not have the selected product
 * 
 * @author Jin
 *
 */
public class NoInventoryException extends Exception {
	private static final long serialVersionUID = -296432769892035797L;

	public NoInventoryException(String message) {
		super(message);
	}
}
