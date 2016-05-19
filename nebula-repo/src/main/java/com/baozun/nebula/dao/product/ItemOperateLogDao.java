package com.baozun.nebula.dao.product;

import java.util.List;

import com.baozun.nebula.model.product.ItemInfoLang;
import com.baozun.nebula.model.product.ItemOperateLog;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;


public interface ItemOperateLogDao extends GenericEntityDao<ItemOperateLog, Long>{
	
	/**
	 * 根据商品id查询
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = ItemOperateLog.class)
	List<Long> findByItemId(@QueryParam("itemId") Long itemId);
	
	/**
	 * 修改上下架信息
	 * @param id
	 * @param pushOperatorName
	 * @param activeTime
	 */
	@NativeQuery(model = ItemOperateLog.class)
	void updateItemOperateLog(@QueryParam("id") Long id,@QueryParam("soldOutOperatorName") String soldOutOperatorName,@QueryParam("pushOperatorName") String pushOperatorName,@QueryParam("activeTime") Long activeTime);
}
