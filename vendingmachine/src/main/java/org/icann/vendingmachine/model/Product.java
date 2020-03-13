package org.icann.vendingmachine.model;

public enum Product {
	COKE(25), PEPSI(35), SODA(45);
	
	private int price;
	
	private Product(int price) {
		this.price = price;
	}
	
	public int getPrice() {
		return this.price;
	}

}
