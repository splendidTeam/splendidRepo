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
package com.baozun.nebula.model.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;

/**
 * 用于页面端  select 组件的 option选项
 * 
 * @author Justin
 *
 */
@Entity
@Table(name = "T_SYS_CHOOSE_OPTION")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ChooseOption extends BaseModel implements Command  {

	private static final long serialVersionUID = -8595879505664196069L;

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

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_CHOOSE_OPTION",sequenceName = "S_T_SYS_CHOOSE_OPTION",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_CHOOSE_OPTION")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "GROUP_CODE",length = 50)
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	@Column(name = "GROUP_DESC",length = 100)
	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	
	@Column(name = "LABEL_LANG",length = 20)
	public String getLabelLang() {
		return labelLang;
	}

	public void setLabelLang(String labelLang) {
		this.labelLang = labelLang;
	}

	

	@Column(name = "OPTION_VALUE",length = 100)
	public String getOptionValue() {
		return optionValue;
	}


	
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	@Column(name = "OPTION_LABEL",length = 200)
	public String getOptionLabel() {
		return optionLabel;
	}

	public void setOptionLabel(String optionLabel) {
		this.optionLabel = optionLabel;
	}
	@Column(name = "SORT_NO")
	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	@Column(name = "LIFECYCLE")
    @Index(name = "IDX_CHOOSE_OPTION_LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}
	
	
	@Column(name = "IS_SYSTEM")
    @Index(name = "IDX_CHOOSE_OPTION_IS_SYSTEM")
	public Boolean getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
}
