/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
 */
package com.baozun.nebula.model.promotion;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;
/**
 * @author: pengfei.fang
 * @Description: 促销信息，用于如购物车，列表等促销公告
 * @date:2016年01月06日
 */
@Entity
@Table(name = "T_PRM_PROMOTIONINFORMATION",uniqueConstraints = { @UniqueConstraint(columnNames = { "CATEGORY_CODE" }) })
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PromotionInformation extends BaseModel{
	
	private static final long	serialVersionUID	= 1859669460593516366L;

	private Long				id;

	/** 分类code */
	private String				categoryCode;

	/** 促销内容 */
	private String				content;

	private Date				createTime;

	private Date				modifyTime;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PRM_PROMOTIONINFOMATION",sequenceName = "S_T_PRM_PROMOTIONINFOMATION",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PRM_PROMOTIONINFOMATION")
	public Long getId(){
		return id;
	}

	
	public void setId(Long id){
		this.id = id;
	}

	@Column(name = "CATEGORY_CODE")
	public String getCategoryCode(){
		return categoryCode;
	}

	
	public void setCategoryCode(String categoryCode){
		this.categoryCode = categoryCode;
	}

	@Lob
	@Column(name = "CONTENT")
	public String getContent(){
		return content;
	}

	
	public void setContent(String content){
		this.content = content;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime(){
		return createTime;
	}

	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime(){
		return modifyTime;
	}

	
	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}
	
	
	

}
