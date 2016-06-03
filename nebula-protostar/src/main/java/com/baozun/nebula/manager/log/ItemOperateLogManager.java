package com.baozun.nebula.manager.log;

import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.log.ItemOperateLogCommand;
import com.baozun.nebula.manager.BaseManager;

/**
 * 商品操作日志manager
 * @author dong.cheng
 *
 */
public interface ItemOperateLogManager extends BaseManager{

	/**
	 * 商品上下架操作日志分页查询
	 * @param page
	 * @param paraMap
	 * @return
	 */
	Pagination<ItemOperateLogCommand> findItemByconditions(Page page,Sort[] sorts,Map<String, Object> paraMap);
}
