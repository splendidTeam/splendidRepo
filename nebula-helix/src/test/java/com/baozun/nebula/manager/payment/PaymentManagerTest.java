package com.baozun.nebula.manager.payment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.payment.manager.PayManager;
/**
 * 
 * @author 阳羽
 * @createtime 2014-1-14 下午03:09:08
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class PaymentManagerTest {
	
	private static final Logger log = LoggerFactory.getLogger(PaymentManagerTest.class);
	
	@Autowired
	private PayManager paymentManager;
	
	@Test
	public void testSavePayInfos(){
		Map<String, Object> result = new HashMap<String,Object>();
		Map map = new HashMap();
		map.put("out_trade_no","138934267642509414");
		map.put("payment_type","1");
		map.put("trade_no","837238238");
		map.put("total_fee","45.50");
		map.put("buyer_id","333kkk");
		result.put("resultMap",map);
//		paymentManager.savePayInfos(result);
	}
	
	@Test
	public void test(){
		Map<String,String> params = new HashMap<String,String>();
		params.put("is_success","T");
		params.put("out_trade_no","138968857801102901");
		
		params.put("notify_id","aaaabbbc");
		params.put("notify_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		params.put("payment_type","1");
		params.put("trade_no","2014011443807778");
		
		params.put("trade_status","TRADE_FINISHED");
		params.put("buyer_id","yanghuifu");
		params.put("total_fee","0.01");
		params.put("notify_type","trade_status_sync");
		
//		String toBeSignedString = Validator.getToBeString(params);
//		String localSign = Md5Encrypt.md5(toBeSignedString + "umz4aea6g97skeect0jtxigvjkrimd0o", "UTF-8");
//		
//		params.put("sign",localSign);
//		
//		params.put("sign_type","MD5");
//		
//		StringBuffer sb = new StringBuffer();
//		for(Map.Entry<String,String> entry : params.entrySet()){
//			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
//		}
//		System.out.println("--------" + sb.toString());
	}
	
//	@Test
//	public void testFindPayInfoByOrderIdAndPayStatus(){
//		PayInfoCommand payInfoCommand = paymentManager.findPayInfoByOrderIdAndPayTypeAndPayStatus(new Long(366),1, false);
//		if(null != payInfoCommand)
//			System.out.println("--------------" + payInfoCommand.getPayMoney());
//		else
//			System.out.println("payInfo is null");
//	}
}
