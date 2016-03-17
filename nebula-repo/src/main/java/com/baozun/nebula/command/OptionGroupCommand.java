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
package com.baozun.nebula.command;

import java.util.Date;


/**
 * 通用选项
 * @author xingyu.liu
 *
 */
public class OptionGroupCommand  implements Command{

	private static final long serialVersionUID = -5402979346490462126L;

	/**
	 * PK
	 */
	private Long				id;
	
	/**
	 * 分组编码
	 */
	private String				groupCode;
	
	/**
	 * 分组描述
	 */
	private String				groupDesc;
	
	/**
	 * 语言
	 */
	private String				labelLang;
	
	/**
	 * option元素的value
	 */
	private String				optionValue;
	
	/**
	 * option元素的label
	 */
	private String				optionLabel;
	
	/**
	 * 排序号
	 */
	private Integer				sortNo;
	
	/**
	 * 生命周期
	 */
	private Integer 			lifecycle;
	
	/**
	 * 是否系统选项
	 */
	private Boolean				isSystem;
	
	/**
	 * VERSION
	 */
	private Date 				version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public String getLabelLang() {
		return labelLang;
	}

	public void setLabelLang(String labelLang) {
		this.labelLang = labelLang;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public String getOptionLabel() {
		return optionLabel;
	}

	public void setOptionLabel(String optionLabel) {
		this.optionLabel = optionLabel;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Boolean getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

}
