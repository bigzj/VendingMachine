package org.icann.vendingmachine.exception;

/**
 * Thrown when the machine does not have the exact change needed for the transaction
 * 
 * @author Jin
 *
 */
public class NoExactChangeException extends Exception {
	private static final long serialVersionUID = -3709662266855025039L;

	public NoExactChangeException(String message) {
		super(message);
	}
}
