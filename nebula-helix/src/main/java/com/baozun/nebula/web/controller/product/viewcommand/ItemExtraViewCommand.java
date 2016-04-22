/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.product.viewcommand;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 商品的一些扩展信息，比如销量、收藏量、评论量、评分等。
 *
 */
public class ItemExtraViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = -7858212364897809214L;

	/** 销量  .*/
	private Long sales;
	
	/** 收藏数量   .*/
	private Long favoriteCount;
	
	/** 评论数量  .*/
	private Long reviewCount;
	
	/** 商品评分     .*/
	private Float rate;

	public Long getSales() {
		return sales;
	}

	public void setSales(Long sales) {
		this.sales = sales;
	}

	public Long getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(Long favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public Long getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Long reviewCount) {
		this.reviewCount = reviewCount;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((favoriteCount == null) ? 0 : favoriteCount.hashCode());
		result = prime * result + ((rate == null) ? 0 : rate.hashCode());
		result = prime * result
				+ ((reviewCount == null) ? 0 : reviewCount.hashCode());
		result = prime * result + ((sales == null) ? 0 : sales.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemExtraViewCommand other = (ItemExtraViewCommand) obj;
		if (favoriteCount == null) {
			if (other.favoriteCount != null)
				return false;
		} else if (!favoriteCount.equals(other.favoriteCount))
			return false;
		if (rate == null) {
			if (other.rate != null)
				return false;
		} else if (!rate.equals(other.rate))
			return false;
		if (reviewCount == null) {
			if (other.reviewCount != null)
				return false;
		} else if (!reviewCount.equals(other.reviewCount))
			return false;
		if (sales == null) {
			if (other.sales != null)
				return false;
		} else if (!sales.equals(other.sales))
			return false;
		return true;
	}
	
}
