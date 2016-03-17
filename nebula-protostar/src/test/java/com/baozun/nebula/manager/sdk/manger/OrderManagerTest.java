//package com.baozun.nebula.manager.sdk.manger;
//
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.fail;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import loxia.dao.Page;
//import loxia.dao.Pagination;
//import loxia.dao.Sort;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.baozun.nebula.api.sales.CancelOrder;
//import com.baozun.nebula.api.sales.Consignee;
//import com.baozun.nebula.api.sales.OrderLine;
//import com.baozun.nebula.api.sales.OrderPromotion;
//import com.baozun.nebula.api.sales.ReturnOrder;
//import com.baozun.nebula.api.sales.SalesOrder;
//import com.baozun.nebula.model.salesorder.CancelOrderApp;
//import com.baozun.nebula.sdk.command.OrderLineCommand;
//import com.baozun.nebula.sdk.command.PayInfoCommand;
//import com.baozun.nebula.sdk.command.CancelOrderCommand;
//import com.baozun.nebula.sdk.command.ReturnOrderCommand;
//import com.baozun.nebula.sdk.command.SalesOrderCommand;
//import com.baozun.nebula.sdk.manager.OrderManager;
//
///**
// * 
// * @author 阳羽
// * @createtime 2013-11-29 上午10:23:01
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
//		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
//@ActiveProfiles("dev")
//public class OrderManagerTest {
//
//	private static final Logger log = LoggerFactory.getLogger(OrderManagerTest.class);
//	
//	@Autowired
//	private OrderManager sdkOrderService;
//	
//	private String testCode = "138605057969905174";
//	
//	@Test
//	public void testSaveOrder() {
//		SalesOrderCommand salesOrder=new SalesOrderCommand();
//		salesOrder.setMemberId(110L);
//		salesOrder.setMemberName("focus");
////		salesOrder.setGuestIdentify("wcg 2013");
//		salesOrder.setQuantity(2);
//		salesOrder.setTotal(new BigDecimal(1000));
//		salesOrder.setDiscount(new BigDecimal(0));
//		salesOrder.setLogisticsStatus(0);
//		salesOrder.setFinancialStatus(com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_FISTATUS_NO_PAYMENT);
//		salesOrder.setPayment(1);
//		salesOrder.setSource(89);
//		salesOrder.setIp("8.8.8.8");
//		salesOrder.setPayableFreight(new BigDecimal(12));
//		salesOrder.setActualFreight(new BigDecimal(9));
//		salesOrder.setExpress("sf11");
//		salesOrder.setReceiptCode("bz100");
//		salesOrder.setReceiptType(1);
//		salesOrder.setReceiptTitle("this is title");
//		salesOrder.setReceiptContent("th000 win");
//		salesOrder.setCreateTime(new Date());
//		
//		//送货信息
//		salesOrder.setName("天龙");
//		salesOrder.setCountry("中国");
//		salesOrder.setProvince("北京");
//		salesOrder.setCity("北京");
//		salesOrder.setArea("西城区");
//		salesOrder.setAddress("中南海");
//		salesOrder.setMobile("123456");
//		salesOrder.setTel("110");
//		salesOrder.setEmail("ztl@baozun.cn");
//		salesOrder.setPostcode("474412");
//		salesOrder.setAppointTimeQuantum("9:00 a.m-11:00 a.m");
//		salesOrder.setAppointTime("10:00 a.m");
//		salesOrder.setAppointType("ems");
//		
//		//订单折扣
////		salesOrder.setActivityId(999L);
////		salesOrder.setPromotionNo("order promotion  No  Str");
////		salesOrder.setPromotionType(1);
////		salesOrder.setDiscountAmount(new BigDecimal(10));
////		salesOrder.setCoupon("this is order coupon ");
////		salesOrder.setDescribe("th000 is winner");
//		
//		List<OrderLineCommand> lineList=new ArrayList<OrderLineCommand>();
//		lineList.add(getOrderLineCommand("10001","银鳞胸甲",false));
////		lineList.add(getOrderLineCommand("10002","霜之哀伤",false));
//		lineList.add(getOrderLineCommand("10003","蛋刀",false));
//		salesOrder.setOrderLines(lineList);
//		
//		
//		String code = sdkOrderService.saveOrder(salesOrder,null);
//		log.info("currentTime: " + System.currentTimeMillis());
//		log.info("order code is "+code);
//	}
//	
//	@Test
//	public void testCmdNull(){
//		SalesOrderCommand salesOrder=new SalesOrderCommand();
//		System.out.println("salesOrder.getActivityId(): "+salesOrder.getActivityId());
//		assertNull(salesOrder.getActivityId());
//	}
//	
//	private OrderLineCommand getOrderLineCommand(String extentionCode,String itemName,boolean isAct){
//		OrderLineCommand line1=new OrderLineCommand();
//		line1.setExtentionCode(extentionCode);
//		line1.setItemId(1001L);
//		line1.setCount(2);
//		line1.setMSRP(new BigDecimal(50));
//		line1.setSalePrice(new BigDecimal(45));
//		line1.setSubtotal(new BigDecimal(90));
//		line1.setDiscount(new BigDecimal(5));
//		line1.setItemName(itemName);
//		line1.setSaleProperty("蓝色品质，五金一件");
//		line1.setType(1);
//		line1.setGroupId(1);
//
//		//订单行折扣信息
//		if(isAct){
//			line1.setActivityId(1000L);
//			line1.setPromotionNo("promotion  No  Str");
//			line1.setPromotionType(1);
//			line1.setDiscountAmount(new BigDecimal(10));
//			line1.setCoupon("this is coupon ");
//			line1.setDescribe("this is line desc");
//		}
//		return line1;
//	}
//
//	@Test
//	public void testFindOrderByCode() {
//		Integer type = 1;
//		SalesOrderCommand salesOrderCommand = sdkOrderService.findOrderByCode(this.testCode, type);
//		if(null != salesOrderCommand){
//			log.info("id:" + salesOrderCommand.getId());
//			log.info("收货人姓名:" + salesOrderCommand.getName());
//			log.info("订单促销信息：" + salesOrderCommand.getDiscountAmount());
//			List<OrderLineCommand> orderLines = salesOrderCommand.getOrderLines();
//			if(null != orderLines){
//				log.info("订单行:" + orderLines.size());
//				for(OrderLineCommand orderLine : orderLines){
//					log.info("订单行促销信息:" + orderLine.getPromotionNo());
//				}
//			}
//			List<PayInfoCommand> payInfos = salesOrderCommand.getPayInfo();
//			if(null != payInfos){
//				log.info("支付信息：" + payInfos.size());
//			}
//		}
//	}
//
//	@Test
//	public void testFindOrders() {
//		Page page = new Page(1,100);
//		Sort[] sorts = Sort.parse("o.CREATE_TIME asc");
//		Map<String,Object> searchParam = new HashMap<String,Object>();
//
////		searchParam.put("sdkQueryType","1");
//		searchParam.put("memberName", "focus");
//		searchParam.put("name", "%天%");
//		Pagination<SalesOrderCommand>  pagination = sdkOrderService.findOrders(page, sorts, searchParam);
//		List<SalesOrderCommand> list = pagination.getItems();
//		if(null != list && list.size() > 0){
//			for(SalesOrderCommand salesOrderCommand : list){
//				StringBuilder sb = new StringBuilder();
//				sb.append("\n");
//				sb.append("订单Id: ").append(salesOrderCommand.getId()).append("  ");
//				sb.append("活动Id: ").append(salesOrderCommand.getActivityId()).append("  ");
//				sb.append("收货人姓名: ").append(salesOrderCommand.getName()).append("  \n");
//				sb.append("      订单行信息：\n");
//				for(OrderLineCommand line:salesOrderCommand.getOrderLines()){
//					sb.append("\t订单行id:").append(line.getId());
//					sb.append("\t订单行物品名称：").append(line.getItemName());
//					sb.append("\t订单行活动id:").append(line.getActivityId());
//					sb.append("  \n");
//				}
//				
//				log.info(sb.toString());
//			}
//		}
//	}
//
//	@Test
//	public void testSavePayOrder() {
//		
//	}
//
//	@Test
//	public void testUpdateOrderFinancialStatus() {
//		SalesOrderCommand salesOrderCommand = sdkOrderService.updateOrderFinancialStatus(this.testCode, com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_FISTATUS_FULL_PAYMENT);
//		log.info("更改后的状态：" + salesOrderCommand.getFinancialStatus());
//	}
//
//	@Test
//	public void testFindOrderAmount() {
//		List<OrderLineCommand> list = new ArrayList<OrderLineCommand>();
//		OrderLineCommand orderLine1 = new OrderLineCommand();
//		orderLine1.setCount(4);
//		orderLine1.setItemId(new Long(3));
//		
//		OrderLineCommand orderLine2 = new OrderLineCommand();
//		orderLine2.setCount(2);
//		orderLine2.setItemId(new Long(4));
//		
//		list.add(orderLine1);
//		list.add(orderLine2);
//		BigDecimal amount = sdkOrderService.findOrderAmount(list);
//		
//		log.info("amount=" + amount);
//	}
//
//	@Test
//	public void testSaveReturnOrder() {
//		ReturnOrderCommand returnOrderCommand = new ReturnOrderCommand();
//		returnOrderCommand.setOrderCode(this.testCode);
//		returnOrderCommand.setOrderLineId(54L);
//		returnOrderCommand.setMemberId(100001L);
//		returnOrderCommand.setHandleName("小");
//		returnOrderCommand.setMemberName("哈哈hS");
//		returnOrderCommand.setCount(2);
//		returnOrderCommand.setServiceType(1);
//		returnOrderCommand.setDescribe("东西有瑕疵,我想退货!");
//		returnOrderCommand.setIsReceipt(1);
//		returnOrderCommand.setPic("productDemo2.jpg");
//		returnOrderCommand.setName("测测测入");
//		returnOrderCommand.setAddress("万荣路sss");
//		returnOrderCommand.setMobile("13800000000");
//		returnOrderCommand.setStatus(2);
//		returnOrderCommand.setCreateTime(new Date());
//		returnOrderCommand.setVersion(new Date());
//		Integer rtval = sdkOrderService.saveReturnOrder(returnOrderCommand);
//		if (1 == rtval)
//			log.info("退换货保存成功!");
//		else
//			log.info("退换货保存失败");
//	}
//
//	@Test
//	public void testSaveCancelOrder() {
//		CancelOrderCommand cancelOrderCommand = new CancelOrderCommand();
//
//		cancelOrderCommand.setCreateTime(new Date());
//		cancelOrderCommand.setFeedback("");
//
//		cancelOrderCommand.setMemberName("ddd");
//		cancelOrderCommand.setMemberId(new Long(30));
//
//		cancelOrderCommand.setMessage("helloworld");
//		cancelOrderCommand.setMobile("45454543");
//		cancelOrderCommand.setOrderCode(this.testCode);
//		cancelOrderCommand.setReason("不需要2222");
//		cancelOrderCommand.setStatus(CancelOrderApp.APPLYCANCEL_NEW);
//		cancelOrderCommand.setVersion(new Date());
//
//		Integer rtval = sdkOrderService.saveCancelOrder(cancelOrderCommand);
//		if (1 == rtval)
//			log.info("保存成功!");
//		else
//			log.info("保存失败");
//	}
//
//	@Test
//	public void testUpdateOrderLogisticsStatus() {
//		int count = sdkOrderService.updateOrderLogisticsStatus(this.testCode, com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_FINISHED);
//		log.info("------------------" + count);
//	}
//
//	@Test
//	public void testFindReturnOrdersByQueryMapWithPage() {
//		Page page = new Page(1,10);
//		Sort[] sorts = Sort.parse("sro.CREATE_TIME asc");
//		Map<String,Object> searchParam = new HashMap<String,Object>();
//		//searchParam.put("memberId",100001L);
//		searchParam.put("sdkQueryType","1");
//		Pagination<ReturnOrderCommand> returnOrderPage = sdkOrderService.findReturnOrdersByQueryMapWithPage(page, sorts,searchParam);
//		if(null != returnOrderPage && returnOrderPage.getCount() > 0){
//			List<ReturnOrderCommand> orders = returnOrderPage.getItems();
//			for(ReturnOrderCommand returnOrder : orders){
//				log.info("order code : " + returnOrder.getOrderCode());
//			}
//		}
//	}
//
//	@Test
//	public void testFindCancelOrdersByQueryMapWithPage() {
//		Page page = new Page(1,10);
//		Sort[] sorts = Sort.parse("sco.CREATE_TIME asc");
//		Map<String,Object> searchParam = new HashMap<String,Object>();
//		//searchParam.put("memberId",100001L);
//		//searchParam.put("sdkQueryType","1");
//		Pagination<CancelOrderCommand> cancelOrderPage = sdkOrderService.findCancelOrdersByQueryMapWithPage(page, sorts,searchParam);
//		if(null != cancelOrderPage && cancelOrderPage.getCount() > 0){
//			List<CancelOrderCommand> orders = cancelOrderPage.getItems();
//			
//			for(CancelOrderCommand cancelOrder : orders){
//				log.info("order code2 : " + cancelOrder.getOrderCode());
//			}
//		}
//	}
//
//	@Test
//	public void testUpdateCancelOrders() {
//		int count = sdkOrderService.updateCancelOrders(111L, this.testCode, 2, "ccccc");
//		log.info("------------------" + count);
//	}
//
//	@Test
//	public void testUpdateReturnOrders() {
//		int count = sdkOrderService.updateReturnOrders(1111L, this.testCode, 1, "aabbb");
//		log.info("------------------" + count);
//	}
//
//	@Test
//	public void testUpdateOrders() {
//		SalesOrderCommand salesOrderCommand = new SalesOrderCommand();
//		salesOrderCommand.setCode("138561938814903133");
//		salesOrderCommand.setMemberId(1101L);
//		salesOrderCommand.setGuestIdentify("yangyu2222");
//		salesOrderCommand.setQuantity(2);
//		salesOrderCommand.setTotal(new BigDecimal(1000));
//		salesOrderCommand.setDiscount(new BigDecimal(0));
//		salesOrderCommand.setLogisticsStatus(0);
//		salesOrderCommand.setFinancialStatus(com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_FISTATUS_NO_PAYMENT);
//		salesOrderCommand.setPayment(1);
//		salesOrderCommand.setSource(1);
//		salesOrderCommand.setIp("811.811.228.822");
//		salesOrderCommand.setPayableFreight(new BigDecimal(12));
//		salesOrderCommand.setActualFreight(new BigDecimal(9));
//		salesOrderCommand.setExpress("sf111");
//		salesOrderCommand.setReceiptCode("bz100111");
//		salesOrderCommand.setReceiptType(1);
//		salesOrderCommand.setReceiptTitle("this is title");
//		salesOrderCommand.setReceiptContent("this is receipt content");
//		salesOrderCommand.setCreateTime(new Date());
//		
//		salesOrderCommand.setActivityId(10010L);
//		salesOrderCommand.setPromotionNo("promotion 222 No  Str");
//		salesOrderCommand.setPromotionType(2);
//		salesOrderCommand.setDiscountAmount(new BigDecimal(10));
////		salesOrderCommand.setCoupon("this is coupon222 ");
//		salesOrderCommand.setDescribe("this is desc222");
//		
//		
//		salesOrderCommand.setName("张天龙3333");
//		salesOrderCommand.setCountry("中国333");
//		salesOrderCommand.setProvince("北京333");
//		salesOrderCommand.setCity("北京222");
//		salesOrderCommand.setArea("西城区2222");
//		salesOrderCommand.setAddress("中南海1111");
//		salesOrderCommand.setMobile("22222");
//		
//		Integer	retval = sdkOrderService.updateOrders(salesOrderCommand);
//		
//		log.info("currentTime: " + System.currentTimeMillis());
//		log.info("update order info: "+retval);
//	}
//
//}
