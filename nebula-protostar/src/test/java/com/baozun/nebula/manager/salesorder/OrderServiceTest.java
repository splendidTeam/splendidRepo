//package com.baozun.nebula.manager.salesorder;
//
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import loxia.dao.Page;
//import loxia.dao.Pagination;
//import loxia.dao.Sort;
//
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
//import com.baozun.nebula.api.sales.OrderService;
//import com.baozun.nebula.api.sales.PayInfo;
//import com.baozun.nebula.api.sales.PayNo;
//import com.baozun.nebula.api.sales.ReturnOrder;
//import com.baozun.nebula.api.sales.SalesOrder;
//import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
//import com.baozun.nebula.model.salesorder.CancelOrderApp;
//import com.baozun.nebula.utils.JsonFormatUtil;
//
///**
// * 
// * @author 阳羽
// * @createtime 2013-11-21 下午12:50:47
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
//		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
//@ActiveProfiles("dev")
//public class OrderServiceTest {
//
//	private static final Logger log = LoggerFactory
//			.getLogger(OrderServiceTest.class);
//
//	@Autowired
//	private OrderService orderService;
//	
//	@Autowired
//	private SdkOrderLineDao orderLineDao;
//	
//	
//	private String testCode = "138511535865000505";
//
//	@Test
//	public void testSaveOrder() {
//		SalesOrder salesOrder=new SalesOrder();
//		salesOrder.setMemberId(110L);
//		//salesOrder.setMemberName("focus");
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
//		OrderPromotion orderPromotion = new OrderPromotion();
//		orderPromotion.setActivityId(999L);
//		orderPromotion.setPromotionNo("order promotion  No  Str");
//		orderPromotion.setPromotionType(1);
//		orderPromotion.setDiscountAmount(new BigDecimal(10));
//		orderPromotion.setCoupon("this is order coupon ");
//		orderPromotion.setDescribe("th000 is winner");
//		salesOrder.setOrderPromotion(orderPromotion);
//		
//		OrderPromotion promotion = new OrderPromotion();
//		promotion.setActivityId(1000L);
//		promotion.setPromotionNo("promotion  No  Str");
//		promotion.setPromotionType(1);
//		promotion.setDiscountAmount(new BigDecimal(10));
//		promotion.setCoupon("this is coupon ");
//		promotion.setDescribe("this is line desc");
//		
//		OrderLine line1=new OrderLine();
//		line1.setExtentionCode("upcOfLine1");
//		line1.setItemId(1001L);
//		line1.setCount(2);
//		line1.setMSRP(new BigDecimal(50));
//		line1.setSalePrice(new BigDecimal(45));
//		line1.setSubtotal(new BigDecimal(90));
//		line1.setDiscount(new BigDecimal(5));
//		line1.setItemName("银鳞胸甲 ");
//		line1.setSaleProperty("蓝色品质，五金一件");
//		line1.setType(1);
//		line1.setGroupId(1);
//		line1.setOrderPromotion(promotion);
//		
//		List<OrderLine> lineList=new ArrayList<OrderLine>();
//		lineList.add(line1);
//		salesOrder.setOrderLines(lineList);
//		
//		Consignee c=new Consignee();
//		c.setName("天龙");
//		c.setCountry("中国");
//		c.setProvince("北京");
//		c.setCity("北京");
//		c.setArea("西城区");
//		c.setAddress("中南海");
//		c.setMobile("123456");
//		salesOrder.setConsignee(c);
//		
//		String code = orderService.saveOrder(salesOrder);
//		log.info("currentTime: " + System.currentTimeMillis());
//		log.info("order code is "+code);
//		
//		this.testCode=code;
//	}
//
//	@Test
//	public void testFindOrderById() {
//		SalesOrder salesOrder = orderService.findOrderByCode(this.testCode);
//		if(salesOrder!=null)
//			log.info("order code: " + salesOrder.getCode());
//		Consignee consignee = salesOrder.getConsignee();
//		if(null != consignee)
//			log.info("name: " + consignee.getName());
//		List<PayInfo> payInfos = salesOrder.getPayInfo();
//		log.info("payInfo size: " + payInfos.size());
//		
//		List<OrderLine> orderLines = salesOrder.getOrderLines();
//		if(null != orderLines && orderLines.size() > 0){
//			for(OrderLine orderLine : orderLines){
//				log.info("orderLine promtion: " + orderLine.getOrderPromotion().getId());
//			}
//		}else{
//			log.info("orderLine is null");
//		}
//	}
//
//	@Test
//	public void testFindOrders() {
//		Page page = new Page(1,20);
//		Sort[] sorts = Sort.parse("o.CREATE_TIME asc");
//		Map<String,Object> searchParam = new HashMap<String,Object>();
////		searchParam.put("code",this.testCode);
//		searchParam.put("name","张");
//		Pagination<SalesOrder> orderPage = orderService.findOrders(page, sorts, searchParam);
//		System.out.println("count======" + orderPage.getCount());
//		if(null != orderPage && orderPage.getCount() > 0){
//
//			List<SalesOrder> salesOrders = orderPage.getItems();
//			System.out.println("salesOrders size: " + orderPage.getItems().size());
//			for(SalesOrder salesOrder : salesOrders){
//				log.info("order code : " + salesOrder.getCode());
//				log.info("consignee name : " + salesOrder.getConsignee().getName());
//				List<PayInfo> payInfos = salesOrder.getPayInfo();
//				if(null != payInfos && payInfos.size() > 0){
//					for(PayInfo payInfo : payInfos){
//						log.info("payType : " + payInfo.getPayType());
//					}
//				}
//			}
//		}
//	
//	}
//
//	@Test
//	public void testPayOrderWithOutPayNos() {
//		PayInfo payInfo=getPayInfo();
//		String code=this.testCode;
//		PayInfo paiedInfo=orderService.savePayOrder(code, payInfo);
//		if(paiedInfo!=null)
//			log.info("orderId: " + paiedInfo.getOrderId());
//	}
//	
//	@Test
//	public void testPayOrderWithPayNos() {
//		PayInfo payInfo=getPayInfo();
//		String code=this.testCode;
//		
//		List<PayNo> payNos=new ArrayList<PayNo>();
//		
//		PayNo payNo1=new PayNo();
//		payNo1.setPayNo(123111L);
//		payNo1.setCreateTime(new Date());
//		
//		PayNo payNo2=new PayNo();
//		payNo2.setPayNo(123111L);
//		payNo2.setCreateTime(new Date());
//		
//		payNos.add(payNo1);
//		payNos.add(payNo2);
//		payInfo.setPayNos(payNos);
//		
//		PayInfo paiedInfo=orderService.savePayOrder(code, payInfo);
//		if(paiedInfo!=null){
//			StringBuilder sb=new StringBuilder();
//			sb.append("===============================\n");
//			sb.append("paiedInfo.getOrderId() :"+paiedInfo.getOrderId()+"\n");
//			sb.append("paiedInfo.getId()      :"+paiedInfo.getId()+"\n");
//			for(PayNo payNo:paiedInfo.getPayNos()){
//				sb.append("    payNo.getId()         : "+payNo.getId()+"\n");
//				sb.append("    payNo.getPayDetailId(): "+payNo.getPayInfoId()+"\n");
//			}
//		}
//	}
//	
//	private PayInfo getPayInfo(){
//		PayInfo payInfo=new PayInfo();
//		payInfo.setPayNumerical(new BigDecimal(80));
//		payInfo.setPayMoney(new BigDecimal(80));
//		payInfo.setPayType(1);
//		payInfo.setPayDetail("this is pay detail");
//		payInfo.setThirdPayAccount("tianlong@gwy.gov");
//		payInfo.setThirdPayNo("110");
//		return payInfo;
//	}
//
//	@Test
//	public void testUpdateOrderFinancialStatus() {
//		orderService.updateOrderFinancialStatus(this.testCode, com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_FISTATUS_FULL_PAYMENT);
//	}
//
//	@Test
//	public void testFindOrderAmount() {
//
//		List<OrderLine> list = new ArrayList<OrderLine>();
//		OrderLine orderLine1 = new OrderLine();
//		orderLine1.setCount(4);
//		orderLine1.setItemId(new Long(3));
//		
//		OrderLine orderLine2 = new OrderLine();
//		orderLine2.setCount(2);
//		orderLine2.setItemId(new Long(4));
//		
//		list.add(orderLine1);
//		list.add(orderLine2);
//		BigDecimal amount = orderService.findOrderAmount(list);
//		
//		log.info("amount=" + amount);
//	}
//
//	@Test
//	public void testSaveReturnOrder() {
//		ReturnOrder returnOrder = new ReturnOrder();
//		returnOrder.setOrderCode(this.testCode);
//		returnOrder.setOrderLineId(54L);
//		returnOrder.setMemberId(100001L);
//		returnOrder.setHandleName("小");
//		returnOrder.setMemberName("哈哈hS");
//		returnOrder.setCount(2);
//		returnOrder.setServiceType(1);
//		returnOrder.setDescribe("东西有瑕疵,我想退货!");
//		returnOrder.setIsReceipt(0);
//		returnOrder.setPic("1.jpg");
//		returnOrder.setName("测测测入");
//		returnOrder.setAddress("万荣路sss");
//		returnOrder.setMobile("13800000000");
//		returnOrder.setStatus(2);
//		returnOrder.setCreateTime(new Date());
//		returnOrder.setVersion(new Date());
//		Integer rtval = orderService.saveReturnOrder(returnOrder);
//		if (1 == rtval)
//			log.info("退换货保存成功!");
//		else
//			log.info("退换货保存失败");
//	}
//
//	@Test
//	public void testFindReturnOrdersByMemberId() {
//		Page page = new Page(1,10);
//		Sort[] sorts = Sort.parse("sa.CREATE_TIME asc");
//		Pagination<ReturnOrder> returnOrderPage = orderService.findReturnOrdersByMemberId(page,sorts,100001L);
//		if(null != returnOrderPage && returnOrderPage.getCount() > 0){
//			List<ReturnOrder> orders = returnOrderPage.getItems();
//			for(ReturnOrder returnOrder : orders){
//				log.info("order code : " + returnOrder.getOrderCode());
//			}
//		}
//	}
//
//	@Test
//	public void testSaveCancelOrder() {
//		CancelOrder cancelOrder = new CancelOrder();
//
//		cancelOrder.setCreateTime(new Date());
//		cancelOrder.setFeedback("");
//
//		cancelOrder.setHandleId(null);
//		cancelOrder.setMemberId(new Long(30));
//
//		cancelOrder.setMessage("helloworld");
//		cancelOrder.setMobile("18067165172");
//		cancelOrder.setOrderCode(this.testCode);
//		cancelOrder.setReason("暂时不想买");
//		cancelOrder.setStatus(CancelOrderApp.APPLYCANCEL_NEW);
//		cancelOrder.setVersion(new Date());
//
//		Integer rtval = orderService.saveCancelOrder(cancelOrder);
//		if (1 == rtval)
//			log.info("保存成功!");
//		else
//			log.info("保存失败");
//	}
//
//	@Test
//	public void testFindCancelOrdersByMemberId() {
//		Page page = new Page(1,10);
//		Sort[] sorts = Sort.parse("sa.CREATE_TIME asc");
//		Pagination<CancelOrder> cancelOrderPage = orderService.findCancelOrdersByMemberId(page, sorts,new Long(31));
//		if(null != cancelOrderPage && cancelOrderPage.getCount() > 0){
//			List<CancelOrder> orders = cancelOrderPage.getItems();
//			for(CancelOrder cancelOrder : orders){
//				log.info("order code : " + cancelOrder.getOrderCode());
//			}
//		}
//	}
//
//	@Test
//	public void testUpdateOrderLogisticsStatus() {
//		orderService.updateOrderLogisticsStatus(this.testCode, com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_FINISHED);
//	}
//
//	@Test
//	public void testFindReturnOrdersByQueryMapWithPage() {
//		Page page = new Page(1,10);
//		Sort[] sorts = Sort.parse("sa.CREATE_TIME asc");
//		Map<String,Object> searchParam = new HashMap<String,Object>();
//		searchParam.put("memberId",100001L);
//		Pagination<ReturnOrder> returnOrderPage = orderService.findReturnOrdersByQueryMapWithPage(page, sorts,searchParam);
//		if(null != returnOrderPage && returnOrderPage.getCount() > 0){
//			List<ReturnOrder> orders = returnOrderPage.getItems();
//			for(ReturnOrder returnOrder : orders){
//				log.info("order code : " + returnOrder.getOrderCode());
//			}
//		}
//	}
//
//	@Test
//	public void testFindCancelOrdersByQueryMapWithPage() {
//		Page page = new Page(1,10);
//		Sort[] sorts = Sort.parse("sa.CREATE_TIME asc");
//		Map<String,Object> searchParam = new HashMap<String,Object>();
//		searchParam.put("memberId",new Long(30));
//		Pagination<CancelOrder> cancelOrderPage = orderService.findCancelOrdersByQueryMapWithPage(page, sorts,searchParam);
//		if(null != cancelOrderPage && cancelOrderPage.getCount() > 0){
//			List<CancelOrder> orders = cancelOrderPage.getItems();
//			for(CancelOrder cancelOrder : orders){
//				log.info("order code : " + cancelOrder.getOrderCode());
//			}
//		}
//	}
//
//	@Test
//	public void testUpdateCancelOrders() {
//		int count = orderService.updateCancelOrders(111L, this.testCode, 2, "bbbb");
//		log.info("------------------" + count);
//	}
//
//	@Test
//	public void testUpdateReturnOrders() {
//		int count = orderService.updateReturnOrders(1111L, this.testCode, 1, "aa");
//		log.info("------------------" + count);
//	}
//
//	@Test
//	public void testUpdateOrders() {
//
//		SalesOrder salesOrder=new SalesOrder();
//		salesOrder.setMemberId(110L);
//		salesOrder.setGuestIdentify("yangyu");
//		salesOrder.setQuantity(2);
//		salesOrder.setTotal(new BigDecimal(1000));
//		salesOrder.setCode(this.testCode);
//		salesOrder.setDiscount(new BigDecimal(0));
//		salesOrder.setLogisticsStatus(0);
//		salesOrder.setFinancialStatus(com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_FISTATUS_NO_PAYMENT);
//		salesOrder.setPayment(1);
//		
//		salesOrder.setSource(1);
//		
//		Consignee c=new Consignee();
//		c.setName("张天龙");
//		c.setCountry("中国");
//		c.setProvince("北京");
//		c.setCity("北京");
//		c.setArea("西城区");
//		c.setAddress("中南海");
//		c.setMobile("18067165172");
//		c.setModifyTime(new Date());
//		salesOrder.setConsignee(c);
//		
//
//		log.info("result= " + orderService.updateOrders(salesOrder));
//
//	}
//	
//	@Test
//	public void testFindOrderDetailListByOrderIds() {
//		List<Long> list =new ArrayList<Long>();
//		list.add(54L);
//		log.info("result= " + JsonFormatUtil.format(orderLineDao.findOrderDetailListByOrderIds(list)));
//	}
//
//}
