package com.baozun.nebula.dao.system;

import java.util.Map;

import com.baozun.nebula.model.system.SysItemOperateLog;

import loxia.annotation.NativeQuery;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;


/**
 * 商品操作日志相关
 * @author jay.yang
 *
 */
public interface SysItemOperateLogDao extends GenericEntityDao<SysItemOperateLog, Long>{

	@NativeQuery(model = SysItemOperateLog.class)
	Pagination<SysItemOperateLog> findItemOperaterLog(Page paramPage,
			Sort[] sort, Map<String, Object> paramMap);

}
