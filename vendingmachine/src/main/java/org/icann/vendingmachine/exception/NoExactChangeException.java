package org.icann.vendingmachine.exception;

public class NoExactChangeException extends Exception {
	private static final long serialVersionUID = -3709662266855025039L;

	public NoExactChangeException(String message) {
		super(message);
	}
}
