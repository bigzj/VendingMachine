package org.icann.vendingmachine.exception;

/**
 * Thrown when the tendered amount is less than the price of the selected product
 * 
 * @author Jin
 *
 */
public class NotFullyPaidException extends Exception {
	private static final long serialVersionUID = 7764744320467408342L;

	public NotFullyPaidException(String message) {
		super(message);
	}
}
