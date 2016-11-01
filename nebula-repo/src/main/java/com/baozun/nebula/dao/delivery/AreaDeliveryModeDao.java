package com.baozun.nebula.dao.delivery;

import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.delivery.AreaDeliveryMode;

/**
 * @Description 区域配送
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016年8月16日 下午1:28:01
 */
public interface AreaDeliveryModeDao extends GenericEntityDao<AreaDeliveryMode, Long> {

	/**
	 * 根据areaId查询该地区的配送信息
	 * @param areaId
	 * @return
	 */
	@NativeQuery(model = AreaDeliveryMode.class, value = "AreaDeliveryMode.findAreaDeliveryModeByAreaId")
	AreaDeliveryMode findAreaDeliveryModeByAreaId(@QueryParam("areaId") Long areaId);

	/**
	 * 更新AreaDeliveryMode
	 */
	@NativeUpdate
	void updateAreaDeliveryMode(@QueryParam Map<String, Object> paraMap);

}
