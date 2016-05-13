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
package com.baozun.nebula.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.baozun.nebula.model.BaseModel;

/**
 * 商品-标签关系
 * @author dongliang.ma
 *
 */

@Entity
@Table(name = "T_PD_ITEM_TAG_RELATION")
@Deprecated
public class ItemTagRelation extends BaseModel{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3293898066173592524L;
	/**
	 * id
	 */
	private Long id;
	
	/**
	 * 商品id
	 */
	private Long itemId;
	
	/**
	 * 标签id
	 */
	private Long tagId;
	
	

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_ITEM_TAG_RELATION",sequenceName = "S_T_PD_ITEM_TAG_RELATION",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_ITEM_TAG_RELATION")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ITEM_ID")
	public Long getItemId(){
		return itemId;
	}

	
	public void setItemId(Long itemId){
		this.itemId = itemId;
	}

	@Column(name = "TAG_ID")
	public Long getTagId(){
		return tagId;
	}

	public void setTagId(Long tagId){
		this.tagId = tagId;
	}

	
}
