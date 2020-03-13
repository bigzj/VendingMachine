package org.icann.vendingmachine.exception;

public class NotFullyPaidException extends Exception {
	private static final long serialVersionUID = 7764744320467408342L;

	public NotFullyPaidException (String message) {
		super(message);
	}
}
