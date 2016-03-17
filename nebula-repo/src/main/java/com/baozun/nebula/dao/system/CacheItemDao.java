/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.dao.system;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import org.springframework.jdbc.core.RowMapper;

import com.baozun.nebula.command.cache.CacheItemCommand;
import com.baozun.nebula.model.system.CacheConfig;

/**
 * CacheItemDao
 * @author  yuelou.zhang
 *
 */
public interface CacheItemDao extends GenericEntityDao<CacheConfig,Long>{

	/**
	 * 获取全部的缓存项
	 */
	@NativeQuery(model = CacheConfig.class)
	List<CacheItemCommand> findAllCacheItem(RowMapper<CacheItemCommand> rowMapper,@QueryParam Map<String, Object> paraMap);
	
}
