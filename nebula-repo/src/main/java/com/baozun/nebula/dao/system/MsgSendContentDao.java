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
import com.baozun.nebula.model.system.MsgSendContent;

/**
 * MsgSendContentDao
 * @author  Justin
 *
 */
public interface MsgSendContentDao extends GenericEntityDao<MsgSendContent,Long>{

	/**
	 * 获取所有MsgSendContent列表
	 * @return
	 */
	@NativeQuery(model = MsgSendContent.class)
	List<MsgSendContent> findAllMsgSendContentList();
	
	/**
	 * 通过ids获取MsgSendContent列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = MsgSendContent.class)
	List<MsgSendContent> findMsgSendContentListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取MsgSendContent列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = MsgSendContent.class)
	List<MsgSendContent> findMsgSendContentListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取MsgSendContent列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = MsgSendContent.class)
	Pagination<MsgSendContent> findMsgSendContentListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
}
