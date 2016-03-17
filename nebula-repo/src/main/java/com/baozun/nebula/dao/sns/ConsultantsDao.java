/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.dao.sns;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.product.ConsultantCommand;
import com.baozun.nebula.model.sns.Consultants;

public interface ConsultantsDao extends GenericEntityDao<Consultants, Long> {

	@NativeQuery(model = ConsultantCommand.class, pagable = true)
	public Pagination<ConsultantCommand> findConsultants(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap);

	/**
	 * 解答咨询
	 * 
	 * @param id
	 *            咨询记录Id
	 * @param lifeCycle
	 *            咨询状态（要传入是因为不在sql中固定值）
	 * @param resolveNote
	 *            答案
	 * @param resolveId
	 *            解决者id
	 * @param publicMark
	 *            是否公开（如果公开，同时会更新publishId 和publicshTime）
	 * @return 修改影响的行数
	 */
	@NativeUpdate
	public Integer resolveConsultant(@QueryParam("id") Long id,
									@QueryParam("lifecycle") int lifeCycle,
									@QueryParam("resolvenote") String resolveNote,
									@QueryParam("resolveid") Long resolveId,
									@QueryParam("publishmark") int publicMark);
	
	/**
	 * 公开咨询
	 * @param id  咨询记录Id 
	 * @param publicMark 咨询记录公开标志
	 * @param resolveId  公开者Id
	 * @return
	 */
	@NativeUpdate
	public Integer publishConsultant(@QueryParam("id") Long id,
									@QueryParam("publishmark") int publicMark,
									@QueryParam("publishid") Long publishid);
	
	/**
	 * 取消公开咨询
	 * @param id
	 * @param publicMark
	 * @param unpublishid
	 * @return
	 */
	@NativeUpdate
	public Integer unpublishConsultant(@QueryParam("id") Long id,
									@QueryParam("publishmark") int publicMark,
									@QueryParam("unpublishid") Long unpublishid);
	
	@NativeUpdate
	public Integer updateConsultant(@QueryParam("id") Long id,
			@QueryParam("resolvenote") String resolveNote,
			@QueryParam("lastupdateid") Long lastupdateid,
			@QueryParam("publishmark") int publicMark);

	/**
	 * @param id
	 * @return
	 */
	@NativeQuery(model = ConsultantCommand.class)
	public ConsultantCommand findById(@QueryParam("id") Long id);
	
	/**
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ConsultantCommand.class)
	public List<ConsultantCommand> findConsultantsNoPage(Sort[] sorts, @QueryParam Map<String, Object> paraMap);
	
	/**
	 * 通过商品Id查询对商品咨询
	 * @param page
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = ConsultantCommand.class)
	Pagination<ConsultantCommand> findConsultantsListByItemId(Page page, @QueryParam("itemId") Long itemId, Sort[] sorts);

}
