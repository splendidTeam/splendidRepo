package com.baozun.nebula.constant;

import java.util.HashMap;
import java.util.Map;


/**
 * 退换货 常量
 */
public class SoReturnConstants {

	/**
	 * SoReturnApplication退换货申请表<br>
	 * returnReason属性值域<br>
	 * 退换货申请原因<br>
	 */
	/**不想买了*/
	public static final String CHEANGE_MIND="R001A";
	/** 商品质量问题 */
	public static final String DAMAGED_GOOD = "R002A";
	/** 包装破损 */
	public static final String DAMAGED_PACKAGE = "R003A";
	/** 尺码与商品描述不符*/
	public static final String SIZE_UNMATCH = "R004A";
	
	/** 颜色、图案、款式与商品描述不符*/
	public static final String PRODUCT_UNMATCH = "R005A";
	
	/** 其他原因*/
	public static final String OTHER_REASON = "R006A";

	/**
	 * SoReturnApplication退换货申请表<br>
	 * isNeededReturnInvoice属性值域<br>
	 * 是否退货发票<br>
	 * Y表示需要退回发票:RETURN_APPLICATION_NEEDEDRETURNINVOICE<br>
	 * N表示不需要退回发票:RETURN_APPLICATION_NOTNEEDEDRETURNINVOICE(已过期)<br>
	 * 不在使用此值域<br>
	 * 现在实际情况用户无论选不选需要发票，官网商城都会开据发票，所以现在统一值域为：Y<br>
	 */
	public static final String NEEDED_RETURNINVOICE = "Y";
	public static final String NOTNEEDED_RETURNINVOICE = "N";

	/** 
	 * SoReturnApplication退换货申请表<br>
	 * type属性值域<br>
	 * 退换货申请类型<br>
	 */
	/** 退货申请 */
	public static final int TYPE_RETURN = 1;
	/** 换货申请 */
	public static final int TYPE_EXCHANGE = 2;

	/**
	 * SoReturnApplication退换货申请表<br>
	 * status属性值域<br>
	 * 退换货申请状态<br>
	*/
	/** 待审核  （即新建）*/
	public static final int AUDITING = 0;
	/** 拒绝退货  */
	public static final int REFUS_RETURN = 1;
	/** 待发货 （即客服审核通过） */
	public static final int TO_DELIVERY  = 2;
	/** 已发货 */
	public static final int DELIVERIED = 3;
	/** 同意退款 */
	public static final int AGREE_REFUND = 4;
	/** 已完成 */
	public static final int RETURN_COMPLETE = 5;
	/** 取消退货*/
	public static final int RETURN_CANCEL = 6;
	
	/**
	 * key : 退换货数字状态 
	 * value ： 退换货状态中文描述 
	 * */
	public static Map<Integer,String> getStatusDescMap(){
		Map<Integer,String>  map =  new HashMap<Integer,String>();
		    map.put(new Integer(AUDITING), "待审核");
		    map.put(new Integer(REFUS_RETURN), "拒绝退货");
		map.put(new Integer(TO_DELIVERY), "待发货");
		map.put(new Integer(DELIVERIED), "已发货");
		map.put(new Integer(AGREE_REFUND), "同意退款");
		    map.put(new Integer(RETURN_COMPLETE), "已完成");
		    map.put(new Integer(RETURN_CANCEL), "取消退货");
		return map ;
	}
	

	/**
	 * SoReturnApplication退换货申请表<br>
	 * refundStatus属性值域<br>
	 * 退款状态<br>
	 * 待处理:REFUND_TYPE_WAIT<br>
	 *      同意退款:REFUND_TYPE_HANDLING<br>
	 * 拒绝退款退换:REFUND_TYPE_SUCCESS<br>
	 * 已完成:REFUND_TYPE_FAIL<br>
	 */
	public static final int REFUND_TYPE_WAIT = 0;
	public static final int REFUND_TYPE_HANDLING = 1;
	public static final int REFUND_TYPE_SUCCESS = 2;
	public static final int REFUND_TYPE_FAIL = 3;

	/**
	 * SoReturnApplication退换货申请表<br>
	 * synType属性值域<br>
	 * MQ状态<br>
	 * 未发送MQ:IS_NOT_SEND_MQ<br>
	 * 发送MQ:IS_SEND_MQ<br>
	 * 发送异常:SEND_MQ_ERROR
	 */
	public static final int IS_NOT_SEND_MQ = 0;
	public static final int IS_SEND_MQ = 1;
	public static final int SEND_MQ_ERROR = 2;

}
