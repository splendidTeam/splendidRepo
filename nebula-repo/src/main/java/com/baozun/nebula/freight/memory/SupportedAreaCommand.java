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

import com.baozun.nebula.command.Command;

/**
 * @author Tianlong.Zhang
 *
 */
public class SupportedAreaCommand implements Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3632098095600922716L;

	/**
	 * id
	 */
	private Long id;

	/**
	 * 物流方式Id
	 */
	private Long distributionModeId;
	
	/**
	 * 类型 ， 黑名单，白名单 等
	 */
	private String type;
	
	/**
	 * 分组编号，用于确定同一个黑名单和白名单的组合
	 */
	private Long groupNo;
	
	/**
	 * 目的地id: 可代表地址的所有层级，如省，市，区，镇的id
	 */
	private String areaId;
	
	/**
	 * 目的地名字: 可代表地址的所有层级，如省，市，区，镇的名字
	 */
	private String area;
	
	/**
	 * 代称，表示某些区域的代称 如：江浙沪   等
	 */
	private String designate;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the groupNo
	 */
	public Long getGroupNo() {
		return groupNo;
	}

	/**
	 * @param groupNo the groupNo to set
	 */
	public void setGroupNo(Long groupNo) {
		this.groupNo = groupNo;
	}

	/**
	 * @return the areaId
	 */
	public String getAreaId() {
		return areaId;
	}

	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public void setDesignate(String designate) {
		this.designate = designate;
	}

	public String getDesignate() {
		return designate;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
}
