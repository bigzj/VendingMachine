package org.icann.vendingmachine;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.icann.vendingmachine.exception.NoExactChangeException;
import org.icann.vendingmachine.exception.NoInventoryException;
import org.icann.vendingmachine.exception.NotFullyPaidException;
import org.icann.vendingmachine.model.Coin;
import org.icann.vendingmachine.model.Product;
import org.icann.vendingmachine.model.ProductAndChange;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VendingMachineTest {
	private static VendingMachine vm;
	
	@BeforeClass
	public static void setUp() {
		Map<Product, Integer> products = new HashMap<Product, Integer>();
		products.put(Product.COKE, 3);
		products.put(Product.SODA, 3);
		
		Map<Coin, Integer> coins = new HashMap<Coin, Integer>();
		coins.put(Coin.QUARTER, 3);
		coins.put(Coin.DIME, 3);
		coins.put(Coin.NICKLE, 3);
		coins.put(Coin.PENNY, 3);
		
		vm = new VendingMachine(products, coins); 
	}
	
    @AfterClass
    public static void tearDown() {
    	vm = null;
    }
    
    @Test
    public void _1_testInsertCoin() {
    	assertEquals(25, vm.insertCoin(Coin.QUARTER));
    	assertEquals(35, vm.insertCoin(Coin.DIME));
    	assertEquals(40, vm.insertCoin(Coin.NICKLE));
    	assertEquals(41, vm.insertCoin(Coin.PENNY));
    }
    
    @Test (expected = NoInventoryException.class)
    public void _2_testSelectNotInInventoryProduct() throws NoInventoryException {
   		vm.selectProduct(Product.PEPSI);
    }
    
    @Test
    public void _3_testSelectInInventoryProduct() throws NoInventoryException {
   		assertEquals(Product.SODA.getPrice(), vm.selectProduct(Product.SODA));
    }
    
    @Test (expected = NotFullyPaidException.class)
    public void _4_testConfirmPurchaseNotFullyPaid() throws NotFullyPaidException, NoExactChangeException {
    		vm.confirmPurchase();
    }
    
    @Test
    public void _5_testCancelPruchase() {
    	Map<Coin, Integer> expectedResult = new HashMap<Coin, Integer>();
    	expectedResult.put(Coin.QUARTER, 1);
    	expectedResult.put(Coin.DIME, 1);
    	expectedResult.put(Coin.NICKLE, 1);
    	expectedResult.put(Coin.PENNY, 1);
    	
    	assertEquals(expectedResult, vm.cancelPurchase());
    }
    
    @Test
    public void _6_testWithdraw() {
    	Map<Coin, Integer> expectedResult = new HashMap<Coin, Integer>();
    	expectedResult.put(Coin.QUARTER, 3);
    	expectedResult.put(Coin.DIME, 3);
    	expectedResult.put(Coin.NICKLE, 3);
    	expectedResult.put(Coin.PENNY, 3);
    	
    	assertEquals(expectedResult, vm.withdraw());
    }
    
    @Test (expected = NoExactChangeException.class)
    public void _7_testNoExactChangeException() throws NotFullyPaidException, NoExactChangeException, NoInventoryException {
    	vm.insertCoin(Coin.QUARTER);
    	vm.insertCoin(Coin.QUARTER);
    	
    	vm.selectProduct(Product.SODA);
    	
    	//need to return 5 cents as change, but the machine does not have
    	vm.confirmPurchase();
    }
    
    @Test
    public void _8_testConfirmPurchaseWithChange() throws NotFullyPaidException, NoExactChangeException, NoInventoryException {
    	vm.selectProduct(Product.COKE);
    	
    	Map<Coin, Integer> change = new HashMap<Coin, Integer>();
    	change.put(Coin.QUARTER,  1);
    	
    	ProductAndChange pc = vm.confirmPurchase();
    	
    	assertEquals(Product.COKE, pc.getProduct());
    	
    	int numberOfQuarters = (int)pc.getChange().get(Coin.QUARTER);
    	assertEquals(1, numberOfQuarters);
    }
    
    @Test
    public void _9_testRefillProductAndGetProductCount() {
		Map<Product, Integer> products = new HashMap<Product, Integer>();
		products.put(Product.COKE, 3);
		products.put(Product.PEPSI, 3);
		products.put(Product.SODA, 3);
		
		vm.refillProduct(products);
		
		assertEquals(5, vm.getProductCount(Product.COKE));
		assertEquals(3, vm.getProductCount(Product.PEPSI));
		assertEquals(6, vm.getProductCount(Product.SODA));
    }
}
