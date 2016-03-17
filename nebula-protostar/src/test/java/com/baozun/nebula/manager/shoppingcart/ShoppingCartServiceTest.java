//package com.baozun.nebula.manager.shoppingcart;
//
//import static org.junit.Assert.*;
//import java.math.BigDecimal;
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import com.baozun.nebula.api.shoppingcart.ShoppingCart;
//import com.baozun.nebula.api.shoppingcart.ShoppingCartLine;
//import com.baozun.nebula.api.shoppingcart.ShoppingCartService;
//
///**
// * 
// * @author 阳羽
// * @createtime 2013-11-26 下午01:17:28
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
//		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
//@ActiveProfiles("dev")
//public class ShoppingCartServiceTest {
//
//	private static final Logger log = LoggerFactory.getLogger(ShoppingCartServiceTest.class);
//	
//	@Autowired
//	private ShoppingCartService shoppingCartService;
//	
//	@Test
//	public void testFindShoppingCart() {
//		ShoppingCart shoppingCart = shoppingCartService.findShoppingCart(new Long(10001));
//		if(null != shoppingCart){
//			System.out.println("totalDisCount= " + shoppingCart.getTotalDiscount());
//			System.out.println("totalPrice= " + shoppingCart.getTotalPrice());
//			if(null != shoppingCart){
//				List<ShoppingCartLine> shoppingCartLines = shoppingCart.getShoppingCartLines();
//				if(null != shoppingCartLines && shoppingCartLines.size() > 0){
//					for(ShoppingCartLine shoppingCartLine : shoppingCartLines){
//						System.out.println("itemId=" + shoppingCartLine.getItemId() + "\tquantity=" + shoppingCartLine.getQuantity()
//								+ "\tupc=" + shoppingCartLine.getExtentionCode() + "\tprice=" + shoppingCartLine.getUnitPrice());
//					}
//				}
//			}
//		}else{
//			System.out.println("the user not shopcart");
//		}
//	}
//
//	@Test
//	public void testSaveShoppingCart() {
//		Long memberId=100012L;
//		ShoppingCartLine shoppingCartLine = new ShoppingCartLine();
//		shoppingCartLine.setItemId(4L);
//		shoppingCartLine.setQuantity(3);
//		shoppingCartLine.setExtentionCode("asdfdd");
//		shoppingCartLine.setUnitPrice(new BigDecimal(10));
//		shoppingCartLine.setDiscount(new BigDecimal(2));
//		int result=shoppingCartService.saveShoppingCart(memberId, shoppingCartLine);
//		assertEquals(1,result);
//		log.info("shoppingCartService.saveShoppingCart result : "+result);
//	}
//
//	@Test
//	public void testUpdateShoppingCart() {
//		int result = shoppingCartService.updateShoppingCart(100012L, "asdfghkl;", 5);
//		assertEquals(1,result);
//		log.info("shoppingCartService.updateShoppingCart result : "+result);
//	}
//
//	@Test
//	public void testRemoveShoppingCartLine() {
//		int result = shoppingCartService.removeShoppingCartLine(100012L, "asdfghkl;");
//		log.info("shoppingCartService.removeShoppingCartLine result : "+result);
//		
//	}
//
//	@Test
//	public void testEmptyShoppingCart() {
//		int result = shoppingCartService.emptyShoppingCart(100012L);
//		log.info("shoppingCartService.emptyShoppingCart result : "+result);
//	}
//
//}
