package com.baozun.nebula.dao.product;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.log.ItemOperateLogCommand;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemInfoLang;
import com.baozun.nebula.model.product.ItemOperateLog;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;


public interface ItemOperateLogDao extends GenericEntityDao<ItemOperateLog, Long>{
	
	/**
	 * 根据商品id查询最新的一条记录（根据createTime）
	 * @param itemId
	 * @return
	 */
	@NativeQuery(alias = "id",clazzes = Long.class)
	Long findByItemId(@QueryParam("itemId") Long itemId);
	
	/**
	 * 商品上下架操作日志分页查询
	 * @param page
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ItemOperateLogCommand.class)
	Pagination<ItemOperateLogCommand> findItemByconditions(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
}
