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
import java.util.List;

import com.baozun.nebula.model.product.Category;

/**
 * @author caihong.wu
 *
 * @date 2013-7-2上午11:19:08
 */
public class ShopCommand implements Command{

	private static final long	serialVersionUID	= -5678462939387355398L;

	/**
	 * PK
	 */
	private Long				shopid;
	
	private Long                organizationid;
	/**
	 * 店铺名称
	 */
	private String				shopname;

	/**
	 * 店铺编码
	 */
	private String				shopcode;

	/**
	 * 描述
	 */
	private String				description;

	/**
	 * 状态
	 */
	private Integer				lifecycle;

	private Date				createTime;

	/**
	 * 关联行业
	 */
	private List<Category>		categoryList;
	/**
	 * 选中的行业
	 */
	private String     				industrys;
	
	private String                   jsonMap;
	
	
	
	public String getJsonMap() {
        return jsonMap;
    }

    public void setJsonMap(String jsonMap) {
        this.jsonMap = jsonMap;
    }

    public String getIndustrys(){
		return industrys;
	}

	public void setIndustrys(String industrys){
		this.industrys = industrys;
	}

	public Long getShopid(){
		return shopid;
	}
	
	public void setShopid(Long shopid){
		this.shopid = shopid;
	}

	public Long getOrganizationid(){
		return organizationid;
	}

	
	public void setOrganizationid(Long organizationid){
		this.organizationid = organizationid;
	}

	public String getShopname(){
		return shopname;
	}

	public void setShopname(String shopname){
		this.shopname = shopname;
	}

	public String getShopcode(){
		return shopcode;
	}

	public void setShopcode(String shopcode){
		this.shopcode = shopcode;
	}

	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Integer getLifecycle(){
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	public List<Category> getCategoryList(){
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList){
		this.categoryList = categoryList;
	}

	public static long getSerialversionuid(){
		return serialVersionUID;
	}

}
