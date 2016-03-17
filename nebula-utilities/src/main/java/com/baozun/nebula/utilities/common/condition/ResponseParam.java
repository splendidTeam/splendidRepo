package com.baozun.nebula.utilities.common.condition;

public class ResponseParam {
	
	//=====================================支付宝================================================================
	public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";//交易创建，等待买家付款。
	
	public static final String TRADE_CLOSED = "TRADE_CLOSED";//在指定时间段内未支付时关闭的交易；在交易完成全额退款成功时关闭的交易。
	
	public static final String TRADE_SUCCESS = "TRADE_SUCCESS";//交易成功，且可对该交易做操作,普通即时到账的交易成功状态。
	
	public static final String TRADE_PENDING = "TRADE_PENDING";//等待卖家收款（买家付款后，如果卖家账号被冻结）。
	
	public static final String TRADE_FINISHED = "TRADE_FINISHED";//交易成功且结束，即不可再做任何操作.开通了高级即时到账或机票分销产品后的交易成功状态
	
	
	//=====================================银联================================================================
	/**
	 * 银联返回参数
	 */
	public static final String TRADE_SUCCESS_UNION = "00";//银联交易成功代码
	
	public static final String TRADE_UNION_SUCCESS = "0";//银联交易成功信息
	
	public static final String TRADE_UNION_FAIL = "1";//银联交易失败信息
	
	public static final String TRADE_UNION_PROCESSING  = "2";//银联交易处理中信息
	
	public static final String TRADE_UNION_UNKNOWORDER = "3";//银联交易无此交易信息

}
