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
package com.baozun.nebula.dao.freight;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.freight.memory.SupportedAreaCommand;
import com.baozun.nebula.model.freight.SupportedArea;

/**
 * @author Tianlong.Zhang
 *
 */
public interface SupportedAreaDao extends GenericEntityDao<SupportedArea, Long> {
	
	@NativeQuery(model=SupportedArea.class)
	List<SupportedArea> getAllSuppoertedArea();
	
//	@NativeUpdate
//	Integer updateSupportedArea();
	
	@NativeUpdate
	Integer deleteSupportedAreas(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 分页查询SupportedAreaCommand
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	@NativeQuery(model=SupportedAreaCommand.class)
	public Pagination<SupportedAreaCommand> findSupportedAreaByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> searchParam);
	
	@NativeUpdate
	public Integer deleteSupportedAreasByDistributionId(@QueryParam("distributionId") Long distributionId);
	
	@NativeQuery(model=SupportedAreaCommand.class)
	public List<SupportedAreaCommand> findSupportedAreasByDistributionId(@QueryParam("distributionId") Long distributionId);
	

}
