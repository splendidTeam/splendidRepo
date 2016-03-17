package com.baozun.nebula.manager.salesorder;

import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.web.command.PtsReturnOrderCommand;
public interface ReturnOrderAppManager extends BaseManager{
	
	/**
	 * 退换货列表
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	public Pagination<PtsReturnOrderCommand> findReturnApplicationListByQueryMapWithPage(Page page, Sort[] sorts,
					Map<String, Object> searchParam);

}
