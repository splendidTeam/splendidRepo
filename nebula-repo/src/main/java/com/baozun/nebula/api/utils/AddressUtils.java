package com.baozun.nebula.api.utils;

import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utilities.library.address.Address;
import com.baozun.nebula.utilities.library.address.AddressUtil;

/**
 * 
 * 地址转换工具类
 * */
public class AddressUtils {

	public static final String		SYMBOL_ARROW		= "->";
	public static final String		SYMBOL_LINE			= "-";
	
	/**
	 * 获得完整的地址名称（陕西省->榆林市->绥德县）
	 * @param areaId
	 * @param symbol
	 * @return
	 */
	public static String getFullAddressName(Long areaId, String symbol){
		String areaName = "";
		Address area=AddressUtil.getAddressById(areaId);
    	if(Validator.isNotNullOrEmpty(area)){
    		areaName = area.getName();
    		while(Validator.isNotNullOrEmpty(area) && area.getpId()>1){
    			area = AddressUtil.getAddressById(area.getpId());
    			areaName = area.getName()+symbol+areaName;
    		}
    	}
    	return areaName;
	}

}
