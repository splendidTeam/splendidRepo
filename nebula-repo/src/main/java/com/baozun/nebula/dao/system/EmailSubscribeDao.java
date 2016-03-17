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
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;
import com.baozun.nebula.model.system.EmailSubscribe;

/**
 * EmailSubscribeDao
 * @author  lv
 *
 */
public interface EmailSubscribeDao extends GenericEntityDao<EmailSubscribe,Long>{

	/**
	 * 获取所有EmailSubscribe列表
	 * @return
	 */
	@NativeQuery(model = EmailSubscribe.class)
	List<EmailSubscribe> findAllEmailSubscribeList();
	
	/**
	 * 通过ids获取EmailSubscribe列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = EmailSubscribe.class)
	List<EmailSubscribe> findEmailSubscribeListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取EmailSubscribe列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = EmailSubscribe.class)
	List<EmailSubscribe> findEmailSubscribeListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取EmailSubscribe列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = EmailSubscribe.class)
	Pagination<EmailSubscribe> findEmailSubscribeListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
}
