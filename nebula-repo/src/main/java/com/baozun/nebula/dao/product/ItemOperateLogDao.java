package com.baozun.nebula.dao.product;

import java.util.List;

import com.baozun.nebula.model.product.ItemInfoLang;
import com.baozun.nebula.model.product.ItemOperateLog;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;


public interface ItemOperateLogDao extends GenericEntityDao<ItemOperateLog, Long>{
	
	/**
	 * 根据商品id查询最新的一条记录（根据createTime）
	 * @param itemId
	 * @return
	 */
	@NativeQuery(alias = "id",clazzes = Long.class)
	Long findByItemId(@QueryParam("itemId") Long itemId);
	
}
