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
package com.baozun.nebula.freight.memory;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.Command;

/**
 * 物流列表  对应了 每一个物流方式支持的地址区域
 * @author Tianlong.Zhang
 *
 */
public class DistributionCommand implements Command{

	private static final long serialVersionUID = 6965942198415976935L;
	
	/**
	 * 物流方式Id
	 */
	private Long distributionModeId;
	
	/**
	 * 物流方式名字
	 */
	private String distributionModeName;
	
	/**
	 * 物流方式支持的白名单
	 */
	private List<SupportedAreaCommand> whiteAreaList;
	
	/**
	 * 物流方式支持的黑名单列表
	 *  key 为 分组号Id 
	 *  
	 * 
	 */
	private Map<Long,List<SupportedAreaCommand>> blackAreaListMap;

	/**
	 * @return the distributionModeId
	 */
	public Long getDistributionModeId() {
		return distributionModeId;
	}

	/**
	 * @param distributionModeId the distributionModeId to set
	 */
	public void setDistributionModeId(Long distributionModeId) {
		this.distributionModeId = distributionModeId;
	}

	/**
	 * @return the distributionModeName
	 */
	public String getDistributionModeName() {
		return distributionModeName;
	}

	/**
	 * @param distributionModeName the distributionModeName to set
	 */
	public void setDistributionModeName(String distributionModeName) {
		this.distributionModeName = distributionModeName;
	}

	/**
	 * @return the whiteAreaList
	 */
	public List<SupportedAreaCommand> getWhiteAreaList() {
		return whiteAreaList;
	}

	/**
	 * @param whiteAreaList the whiteAreaList to set
	 */
	public void setWhiteAreaList(List<SupportedAreaCommand> whiteAreaList) {
		this.whiteAreaList = whiteAreaList;
	}

	/**
	 * @return the blackAreaListMap
	 */
	public Map<Long, List<SupportedAreaCommand>> getBlackAreaListMap() {
		return blackAreaListMap;
	}

	/**
	 * @param blackAreaListMap the blackAreaListMap to set
	 */
	public void setBlackAreaListMap(
			Map<Long, List<SupportedAreaCommand>> blackAreaListMap) {
		this.blackAreaListMap = blackAreaListMap;
	}
	
}
