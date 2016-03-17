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
package com.baozun.nebula.command;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Tianlong.Zhang
 * 
 */
public class ItemFromSolrCommand extends ItemCommand {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7016990612293696321L;

	/**
	 * 子标题
	 */
	private String				subTitle;

	private String				shopName;

	private Integer				industrySortNo;

	private Date				delistTime;

	private Boolean				itemIsDisplay;

	private List<String>		imgList;

	private Map<String, String>	categoryCodeMap;

	/**
	 * @return the subTitle
	 */
	public String getSubTitle() {
		return subTitle;
	}

	/**
	 * @param subTitle
	 *            the subTitle to set
	 */
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	/**
	 * @return the shopName
	 */
	public String getShopName() {
		return shopName;
	}

	/**
	 * @param shopName
	 *            the shopName to set
	 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public void setIndustrySortNo(Integer industrySortNo) {
		this.industrySortNo = industrySortNo;
	}

	public Integer getIndustrySortNo() {
		return industrySortNo;
	}

	public void setDelistTime(Date delistTime) {
		this.delistTime = delistTime;
	}

	public Date getDelistTime() {
		return delistTime;
	}

	public void setItemIsDisplay(Boolean itemIsDisplay) {
		this.itemIsDisplay = itemIsDisplay;
	}

	public Boolean getItemIsDisplay() {
		return itemIsDisplay;
	}

	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}

	public List<String> getImgList() {
		return imgList;
	}

	public Map<String, String> getCategoryCodeMap() {
		return categoryCodeMap;
	}

	public void setCategoryCodeMap(Map<String, String> categoryCodeMap) {
		this.categoryCodeMap = categoryCodeMap;
	}

}
