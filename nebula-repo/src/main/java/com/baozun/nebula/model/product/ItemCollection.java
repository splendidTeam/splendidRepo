/**
 * Copyright (c) 2016 Baozun All Rights Reserved.
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
package com.baozun.nebula.model.product;

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

import com.baozun.nebula.model.BaseModel;

/**
 * 商品集定义，包括商品集的规则定义和指定商品的排序
 * @author D.C
 * @since 2016年4月13日 下午1:26:23 
 */
@Entity
@Table(name = "t_pd_item_collection")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ItemCollection extends BaseModel {
	private static final long serialVersionUID = 3088013871822587744L;
	private Long id;
	/**
	 * 类型定义，可以用来隔离不同的业务领域，比如：推荐区、活动区、导航区等
	 */
	private String type;
	/**
	 * collection 标识码
	 */
	private String code;
	/**
	 * collection 名称
	 */
	private String name;

	/**
	 * facetParameters json数据
	 */
	private String facetParameters;

	/**
	 * 指定集合内的商品排序,ID英文逗号分割
	 */
	private String sequence;
	
	/** 创建时间. */
	private Date createTime;

	/** 修改时间 */
	private Date modifyTime;

	/** version. */
	private Date version;

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_t_pd_item_collection", sequenceName = "S_t_pd_item_collection", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_t_pd_item_collection")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Index(name = "IDX_ItemCollection_code")
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "FACET_PARAMETERS",length=1000)
	public String getFacetParameters() {
		return facetParameters;
	}

	public void setFacetParameters(String facetParameters) {
		this.facetParameters = facetParameters;
	}

	@Column(name = "SEQUENCE")
	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
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
