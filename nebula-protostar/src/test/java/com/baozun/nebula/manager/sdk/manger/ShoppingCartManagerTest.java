package com.baozun.nebula.manager.sdk.manger;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartManager;

/**
 * 
 * @author 阳羽
 * @createtime 2013-12-2 下午02:49:50
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class ShoppingCartManagerTest {

	private static final Logger log = LoggerFactory.getLogger(ShoppingCartManagerTest.class);
	
	@Autowired
	private SdkShoppingCartManager sdkShoppingCartService;
	
//	@Test
//	public void testFindShoppingCart() {
//		ShoppingCartCommand shoppingCart = sdkShoppingCartService.findShoppingCart(new Long(100012));
//		if(null != shoppingCart){
//			System.out.println("totalDisCount= " + shoppingCart.getTotalDiscount());
//			System.out.println("totalPrice= " + shoppingCart.getTotalPrice());
//			if(null != shoppingCart){
//				List<ShoppingCartLineCommand> shoppingCartLines = shoppingCart.getShoppingCartLines();
//				if(null != shoppingCartLines && shoppingCartLines.size() > 0){
//					for(ShoppingCartLineCommand shoppingCartLine : shoppingCartLines){
//						System.out.println("itemId=" + shoppingCartLine.getItemId() + "\tquantity=" + shoppingCartLine.getQuantity()
//								+ "\tupc=" + shoppingCartLine.getExtentionCode() + "\tprice=" + shoppingCartLine.getUnitPrice());
//					}
//				}
//			}
//		}else{
//			System.out.println("the user not shopcart");
//		}
//		
//	}
//
//	@Test
//	public void testSaveShoppingCart() {
//		Long memberId=100011L;
//		ShoppingCartLineCommand shoppingCartLine = new ShoppingCartLineCommand();
//		shoppingCartLine.setItemId(3L);
//		shoppingCartLine.setQuantity(4);
//		shoppingCartLine.setExtentionCode("asdfdd");
//		shoppingCartLine.setUnitPrice(new BigDecimal(101));
//		shoppingCartLine.setDiscount(new BigDecimal(21));
//		int result= sdkShoppingCartService.saveShoppingCart(memberId, shoppingCartLine);
//		assertEquals(1,result);
//		log.info("shoppingCartService.saveShoppingCart result : "+result);
//	}
//
//	@Test
//	public void testUpdateShoppingCart() {
//		int result = sdkShoppingCartService.updateShoppingCart(100012L, "asdfghkl;", 5);
//		assertEquals(1,result);
//		log.info("shoppingCartService.updateShoppingCart result : "+result);
//	}
//
//	@Test
//	public void testRemoveShoppingCartLine() {
//		int result = sdkShoppingCartService.removeShoppingCartLine(100011L, "asdfdd");
//		log.info("shoppingCartService.removeShoppingCartLine result : "+result);
//	}
//
//	@Test
//	public void testEmptyShoppingCart() {
//		int result = sdkShoppingCartService.emptyShoppingCart(100012L);
//		log.info("shoppingCartService.emptyShoppingCart result : "+result);
//	}

}
