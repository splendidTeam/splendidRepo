package com.baozun.nebula.constant;

import java.util.HashMap;
import java.util.Map;


/**
 * 退换货 常量
 */
public class SoReturnConstants {


	
	/**
	 * key : 退换货数字状态 
	 * value ： 退换货状态中文描述 
	 * */
	public static Map<Integer,String> getStatusDescMap(){
		Map<Integer,String>  map =  new HashMap<Integer,String>();
//		    map.put(new Integer(AUDITING), "待审核");
//		    map.put(new Integer(REFUS_RETURN), "拒绝退货");
//		map.put(new Integer(TO_DELIVERY), "待发货");
//		map.put(new Integer(DELIVERIED), "已发货");
//		map.put(new Integer(AGREE_REFUND), "同意退款");
//		    map.put(new Integer(RETURN_COMPLETE), "已完成");
//		    map.put(new Integer(RETURN_CANCEL), "取消退货");
		return map ;
	}
	

}
