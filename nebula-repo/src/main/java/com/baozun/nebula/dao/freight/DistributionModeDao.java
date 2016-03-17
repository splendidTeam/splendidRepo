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

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.freight.DistributionMode;

/**
 * @author Tianlong.Zhang
 *
 */
public interface DistributionModeDao extends GenericEntityDao<DistributionMode, Long>{
	
	/**
	 * 获取所有的 物流方式
	 * @return
	 */
	@NativeQuery(model = DistributionMode.class)
	public List<DistributionMode> getAllDistributionMode();
	
	@NativeUpdate
	public Integer updateDistributionMode(@QueryParam("id") Long id,@QueryParam("name") String name);
	
	@NativeQuery(model = DistributionMode.class)
	public DistributionMode findDistributionModeById(@QueryParam("id") Long id);
	
	
	/**
	 * 通过templateId 获取所有支持的 物流方式
	 * @return
	 */
	@NativeQuery(model = DistributionMode.class)
	public List<DistributionMode> getDistributionModeListByTemplateId(@QueryParam("templateId") Long templateId);
	
	/**
	 * 根据物流方式名字查询
	 * 
	 * @return
	 */
	@NativeQuery(model = DistributionMode.class)
	DistributionMode findDistributionModeByName(@QueryParam("name") String name);
	/**
	 * 
	* @author 何波
	* @Description: 获取所有物流方式
	* @return   
	* List<DistributionMode>   
	* @throws
	 */
	@NativeQuery(model = DistributionMode.class)
	List<DistributionMode> getAllDistributionModeList();
}
