package com.baozun.nebula.sdk.manager.delivery;

import java.util.Map;

import com.baozun.nebula.model.delivery.AreaDeliveryMode;

/**
 * 
 * @Description
 * @author  <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-11-1
 */
public interface SdkAreaDeliveryModeManager {

	/**
	 * 通过AreaId查询AreaDeliveryMode
	 * @param id
	 * @return
	 */
	AreaDeliveryMode findAreaDeliveryModeByAreaId(Long areaId);
	
	/**
	 * 更新areaDeliveryMode
	 */
	void updateDeliveryMode(Map<String,Object> map);
	
	/**
	 * 保存areaDeliveryMode
	 * @param adm
	 * @return 
	 */
	AreaDeliveryMode saveDeliveryMode(AreaDeliveryMode adm);
	
	/**
	 * 通过id进行删除
	 * @param id
	 */
	void deleteDeliveryModeById(Long id);
}
