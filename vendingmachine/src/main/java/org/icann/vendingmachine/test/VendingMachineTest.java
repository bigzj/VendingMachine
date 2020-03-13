package org.icann.vendingmachine.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.icann.vendingmachine.VendingMachine;
import org.icann.vendingmachine.exception.NoInventoryException;
import org.icann.vendingmachine.model.Coin;
import org.icann.vendingmachine.model.Product;

public class VendingMachineTest {
	public static void main(String args[]) {
		Map<Product, Integer> products = new HashMap<Product, Integer>();
		Map<Coin, Integer> coins = new HashMap<Coin, Integer>();
		
		VendingMachine vm = new VendingMachine(products, coins);
		
		vm.insertCoin(Coin.DIME);
		printMap(vm.cancelPurchase());
		
		
		
		try {
			vm.selectProduct(Product.COKE);
		} catch (NoInventoryException nie) {
			System.out.println(nie.getMessage());
		}
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void printMap(Map map) {
		Iterator<Entry> it = map.entrySet().iterator();
		
		while (it.hasNext()) {
			Entry entry = it.next();
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

}
