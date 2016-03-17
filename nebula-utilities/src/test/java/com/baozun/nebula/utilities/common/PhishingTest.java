package com.baozun.nebula.utilities.common;


import java.util.HashMap;
import java.util.Map;

import com.baozun.nebula.utilities.common.command.PaymentServiceReturnCommand;
import com.baozun.nebula.utilities.common.condition.RequestParam;
import com.baozun.nebula.utilities.common.convertor.MapAndStringConvertor;
import com.baozun.nebula.utilities.common.convertor.PayParamConvertorAdaptor;
import com.baozun.nebula.utilities.common.convertor.RequestToCommand;
import com.baozun.nebula.utilities.integration.payment.PaymentAdaptor;
import com.baozun.nebula.utilities.integration.payment.PaymentFactory;
import com.baozun.nebula.utilities.integration.payment.PaymentResult;
import com.baozun.nebula.utilities.integration.payment.PaymentServiceStatus;


public class PhishingTest {

	public static void main(String[] args) {
//		 StringBuilder sb = new StringBuilder();
//		 sb.append("https://mapi.alipay.com/gateway.do");
//		 sb.append("?");
//		 //sb.append("service=query_timestamp");notify_verify
//		 sb.append("service=notify_verify");
//		 sb.append("&");
//		 sb.append("partner=2088201564809153");
//		 sb.append("&notify_id=RqPnCoPT3K9%2Fvwbh3I74n6F0dnhLMaYJuh%2FKBU%2FpuVG%2B3h8rDTNFDwVTYXWYqqq9ar8T");
//		 
//		 URL url;
//		try {
//			url = new URL(sb.toString());
//			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//			urlConnection.setRequestMethod("POST");
//			BufferedReader bufferedReader = new BufferedReader(
//					new InputStreamReader(urlConnection.getInputStream()));
//
//			String notifyVerifyResult = bufferedReader.readLine();
//			System.out.println(notifyVerifyResult);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//			

		// Alipay4PaymentAdaptor alipay4PaymentAdaptor = new
		// Alipay4PaymentAdaptor();
		// boolean isSuccess = false;
		// Map<String,String> parm = new HashMap<String,String>();
		// Map<String,Object> resultMap = new HashMap<String,Object>();
		// BigDecimal amt = new BigDecimal(0.01).divide(new
		// BigDecimal(1),2,BigDecimal.ROUND_HALF_UP);
		// try {
		// parm.put("trade_no", "123");
		// parm.put("out_order_no", "1234");
		// alipay4PaymentAdaptor.init();
		// alipay4PaymentAdaptor.closePaymentRequest(amt, true,parm);
		// } catch (PaymentException e) {
		// e.printStackTrace();
		// resultMap.put("fail", e.toString());
		// }
		//
		// sb.append("https://mapi.alipay.com/gateway.do?sign=6179bf91738fe096cfcf16deabcb7dd2&trade_no=123&sign_type=MD5&service=close_trade&out_order_no=1234&trade_role=buyer");
		// sb.append("https://mapi.alipay.com/gateway.do?subject=a&sign_type=MD5&notify_url=http%3A%2F%2Fwww.baidu.com&out_trade_no=123&default_login=Y&return_url=http%3A%2F%2Fwww.baidu.com&sign=488fa6850f82bb5b441df6c1966689a7&_input_charset=UTF-8&need_ctu_check=N&total_fee=0.01&error_notify_url=http%3A%2F%2Fwww.baidu.com&service=create_direct_pay_by_user&partner=2088401678374334&seller_email=sanguozhangliao%40163.com&anti_phishing_key=KPryvM9sixq9YvTsAA%3D%3D&payment_type=1");
		// KPryvMSBtCL93TIslw==
		// KPryvMSBtCL7qG-X_Q==
//		 URL url = null;
//		 Document doc = null;
//		 try{
//		 url = new URL(sb.toString());
//		 SAXReader reader = new SAXReader();
//		 doc = reader.read(url.openStream());
//		 }catch (MalformedURLException e){
//		 e.printStackTrace();
//		 }catch (DocumentException e){
//		 e.printStackTrace();
//		 }catch (IOException e){
//		 e.printStackTrace();
//		 }
//		 System.out.println(doc.asXML().toString());
		// String payType = "Alipay";
		// PaymentFactory service = PaymentFactory.getInstance();
		// PaymentAdaptor paymentAdaptor = service.getPaymentAdaptor("Alipay");
		// PaymentRequest paymentRequest =
		// paymentAdaptor.newPaymentRequest(httpType, orderNo, amt, addition);
		// paymentRequest.getRequestURL();
		// PaymentResult paymentResult = paymentAdaptor.getPaymentResult(null);
		// paymentResult.getMessage();
		// paymentResult.getPaymentServiceSatus();
		// paymentResult.getPaymentStatusInformation();

		PaymentFactory factory = PaymentFactory.getInstance();
		PaymentAdaptor paymentAdaptor = factory
				.getPaymentAdaptor(PaymentFactory.PAY_TYPE_ALIPAY);
		PayParamConvertorAdaptor payParamConvertorAdaptor = factory
				.getPaymentCommandToMapAdaptor(PaymentFactory.PAY_TYPE_ALIPAY);
		
//		PaymentAdaptor paymentAdaptor = factory.getPaymentAdaptor(PaymentFactory.PAY_TYPE_ALIPAY_BANK);
//		PayParamConvertorAdaptor payParamConvertorAdaptor = factory
//				.getPaymentCommandToMapAdaptor(PaymentFactory.PAY_TYPE_ALIPAY_BANK);
		
//		PaymentAdaptor paymentAdaptor = factory
//				.getPaymentAdaptor(PaymentFactory.PAY_TYPE_ALIPAY_CREDIT);
//		PayParamConvertorAdaptor payParamConvertorAdaptor = factory
//				.getPaymentCommandToMapAdaptor(PaymentFactory.PAY_TYPE_ALIPAY_CREDIT);
		
		
//		PaymentAdaptor paymentAdaptor = factory
//				.getPaymentAdaptor(PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT);
//		PayParamConvertorAdaptor payParamConvertorAdaptor = factory
//				.getPaymentCommandToMapAdaptor(PaymentFactory.PAY_TYPE_ALIPAY_CREDIT_INT);
//		
//		PayParamCommand command = new PayParamCommand();
//		command.setEncoding("UTF-8");
//		command.setOrderNo("12111464");
//		command.setNotify_url("http://www.baidu.com");
//		command.setReturn_url("http://www.baidu.com");
//		command.setTotalFee("100.00");
//		command.setSubject("上海宝尊电商");
//		command.setInternationalCard(true);
//		command.setDefault_bank("cybs-master");//cybs-visa/cybs-master
//		
//		Map<String, String> addition = new HashMap<String, String>();
//		try {
//			addition = payParamConvertorAdaptor.commandConvertorToMapForCreatUrl(command);
//		} catch (PaymentParamErrorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PaymentRequest paymentRequest = paymentAdaptor.newPaymentRequest(RequestParam.HTTP_TYPE_GET, addition);
//		System.out.println(paymentRequest.getRequestURL());
		
//		String url = "https://mapi.alipay.com/gateway.do?subject=%E4%B8%8A%E6%B5%B7%E5%AE%9D%E5%B0%8A%E7%94%B5%E5%95%86&sign_type=MD5&notify_url=http%3A%2F%2Fwww.demo.com%2Fpay%2FgetASyncNotifications%2F3.htm&defaultbank=BOCB2C&out_trade_no=139038209178701769&return_url=http%3A%2F%2Fwww.demo.com%2Fpay%2FgetSyncNotifications%2F3.htm&sign=73fab38a7bc27daad5a77632148c4c5f&_input_charset=UTF-8&it_b_pay=45m&total_fee=0.01&error_notify_url=http%3A%2F%2Fwww.baidu.com&service=create_direct_pay_by_user&paymethod=bankPay&partner=2088201564809153&anti_phishing_key=KPrzY9IGG9iM9PgLGw%3D%3D&seller_email=alipay-test12%40alipay.com&payment_type=1";
//		
//		PaymentAdaptor paymentAdaptor = factory.getPaymentAdaptor(PaymentFactory.PAY_TYPE_UNIONPAY);
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> result = new HashMap<String, String>();
/*		params.put("service", "close_trade");
		params.put("partner", "2088201564809153");
		params.put("_input_charset", "UTF-8");*/
		
		
		params.put("out_trade_no", "2014020913000046");
		params.put("req_id", "123123123123128");
		params.put("total_fee", "0.01");
		
		
		
		
//		params.put("trade_no", "2014081120000013");
//		result = paymentAdaptor.closePaymentRequest(params);
//		System.out.println(result.get("errorMessage"));
//		PaymentAdaptor paymentAdaptor = factory.getPaymentAdaptor(PaymentFactory.PAY_TYPE_UNIONPAY);
//		Map<String, String> params = new HashMap<String, String>();
/*		params.put("acqCode","");
		params.put("commodityDiscount","");
		params.put("commodityName","");
		params.put("commodityQuantity","");
		params.put("commodityUnitPrice","");
		params.put("customerIp","127.0.0.1");
		params.put("customerName","");
		params.put("defaultBankNumber","");
		params.put("defaultPayType","");
		params.put("merCode","");
		params.put("merReserved","");
		params.put("orderAmount","1");
		params.put("orderCurrency","156");
		params.put("orderNumber","139202568548604828");
		params.put("orderTime",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		params.put("origQid","201402101748102871672");
		params.put("transType","31");
		params.put("transferFee","");
		params.put("version","1.0.0");
		params.put("commodityUrl","");*/
		/*PaymentRequest paymentRequest = paymentAdaptor.newPaymentRequestForMobileCreateDirect(params);
		
		
		String a = HttpClientUtil.getHttpMethodResponseBodyAsStringIgnoreCharSet(paymentRequest.getRequestURL(),HttpMethodType.GET,"utf-8");
		try {
			a = java.net.URLDecoder.decode(a,"UTF-8");
			System.out.println(a);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] str = a.split("&");
		Map<String,String> res = new HashMap<String,String>();
		for(String t:str){
			res.put(t.split("=")[0].toString(), t.replace(t.split("=")[0].toString()+"=", ""));
		}
		PaymentResult paymentResult1 = paymentAdaptor.getPaymentResultForMobileCreateDirect(res);
		if(paymentResult1.getPaymentServiceSatus().equals(PaymentServiceStatus.SUCCESS)){
			Map<String, String> resultMap;
			try {
				resultMap = MapAndStringConvertor.convertResultToMap(res.get("res_data").toString());
				PaymentRequest paymentResult2 = paymentAdaptor.newPaymentRequestForMobileAuthAndExecute(resultMap);
				System.out.println(paymentResult2.getRequestURL());
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println(paymentResult1.getMessage());
		}*/
		//String syn = "out_trade_no=2014020913000045&request_token=requestToken&result=success&trade_no=2014081800125950&sign=a3d031f59e173571f493c2d35bc05547&sign_type=MD5";
		/**********************************/
		result.put("out_trade_no", "2014020913000046");
		result.put("request_token", "requestToken");
		result.put("result", "success");
		result.put("trade_no", "2014081800176650");
		result.put("sign", "1257a8a4334da6bffde3f989f4d8a4d2");
//		result.put("sign_type", "MD5");
		PaymentResult paymentResult = new PaymentResult();
		String sign = result.get("sign").toString();
		String result1 = result.get("result").toString();

		result.remove("sign");
		String toBeSignedString = MapAndStringConvertor
				.getToBeSignedString(result);
		String localSign = Md5Encrypt.md5(
				toBeSignedString + "j5xhf616fvbym3umt42kmsrdjywgifx4",
				"UTF-8");

		// 返回函数进行加密比较
		if (sign.equals(localSign)) {
			if (RequestParam.ALIPAYSUCCESS.equals(result1)) {
				paymentResult
						.setPaymentServiceSatus(PaymentServiceStatus.SUCCESS);
			} else {
				paymentResult
						.setPaymentServiceSatus(PaymentServiceStatus.FAILURE);
			}
		} else {
			paymentResult
					.setPaymentServiceSatus(PaymentServiceStatus.UNDEFINED);
			paymentResult.setMessage("sign not match");
		}

		PaymentServiceReturnCommand paymentServiceReturnCommand = new PaymentServiceReturnCommand();
		RequestToCommand requestToCommand = new RequestToCommand();
		paymentResult.setPaymentStatusInformation(paymentServiceReturnCommand);
		
		
		
//		System.out.println(paymentRequest.getRequestURL());

		//PaymentRequest paymentRequest = paymentAdaptor.newPaymentRequestForMobileCreateDirect(params);
		//System.out.println(paymentRequest.getRequestURL());
		/*PaymentResult paymentResult = paymentAdaptor.closePaymentRequest(params);
		System.out.println(paymentResult.getPaymentServiceSatus());
*/
/*		PaymentResult paymentResult = paymentAdaptor.getOrderInfo(params);
		System.out.println(paymentResult.getMessage());*/

	}

}
