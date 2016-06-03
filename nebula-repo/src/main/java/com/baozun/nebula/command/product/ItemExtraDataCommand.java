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
 *
 */
package com.baozun.nebula.command.product;

import com.baozun.nebula.command.Command;

/**   
 * 商品的一些扩展信息，比如销量、收藏量、评论量、评分等。
 * @Description 
 * @author dongliang ma
 * @date 2016年5月6日 下午5:59:30 
 * @version   
 */
public class ItemExtraDataCommand implements Command{

	/** */
	private static final long serialVersionUID = -548438031016340713L;

	/** 商品评分     .*/
	private Float rankavg;
	
	/** 销量  .*/
	private Integer salesCount;
	
	/** 收藏数量  .*/
	private Integer favoredCount;
	
	/** 评论数量  .*/
	private Long reviewCount;

	/**
	 * @return the rankavg
	 */
	public Float getRankavg() {
		return rankavg;
	}

	/**
	 * @param rankavg the rankavg to set
	 */
	public void setRankavg(Float rankavg) {
		this.rankavg = rankavg;
	}

	/**
	 * @return the salesCount
	 */
	public Integer getSalesCount() {
		return salesCount;
	}

	/**
	 * @param salesCount the salesCount to set
	 */
	public void setSalesCount(Integer salesCount) {
		this.salesCount = salesCount;
	}

	/**
	 * @return the favoredCount
	 */
	public Integer getFavoredCount() {
		return favoredCount;
	}

	/**
	 * @param favoredCount the favoredCount to set
	 */
	public void setFavoredCount(Integer favoredCount) {
		this.favoredCount = favoredCount;
	}

	/**
	 * @return the reviewCount
	 */
	public Long getReviewCount() {
		return reviewCount;
	}

	/**
	 * @param reviewCount the reviewCount to set
	 */
	public void setReviewCount(Long reviewCount) {
		this.reviewCount = reviewCount;
	}
	
	
}
