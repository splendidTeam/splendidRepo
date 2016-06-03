package com.baozun.nebula.manager.log;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.log.ItemOperateLogCommand;
import com.baozun.nebula.dao.product.ItemOperateLogDao;

@Transactional
@Service("itemOperateLogManager")
public class ItemOperateLogManagerImpl implements ItemOperateLogManager{

	private static final Logger			LOG											= LoggerFactory.getLogger(ItemOperateLogManagerImpl.class);
	
	@Autowired
	private ItemOperateLogDao itemOperateLogDao;
	
	
	
	
	
	/**
	 * 商品上下架操作日志分页查询
	 */
	@Override
	public Pagination<ItemOperateLogCommand> findItemByconditions(Page page,
			Sort[] sorts, Map<String, Object> paraMap) {
		return itemOperateLogDao.findItemByconditions(page,sorts,paraMap);
	}
}
