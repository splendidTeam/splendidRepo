package com.baozun.nebula.utilities.integration.payment.param;

public class PaymentRequestParam {

	public static final String MOBILE_CREATE_DIRECT_STR = "<direct_trade_create_req><subject>${subject}</subject><out_trade_no>${out_trade_no}</out_trade_no><total_fee>${total_fee}</total_fee><seller_account_name>${seller_account_name}</seller_account_name><call_back_url>${call_back_url}</call_back_url><notify_url>${notify_url}</notify_url><out_user>${out_user}</out_user><merchant_url>${merchant_url}</merchant_url><pay_expire>${pay_expire}</pay_expire></direct_trade_create_req>";
	
	public static final String MOBILE_AUTH_EXECUTE_STR = "<auth_and_execute_req><request_token>${request_token}</request_token></auth_and_execute_req>";
	
}
