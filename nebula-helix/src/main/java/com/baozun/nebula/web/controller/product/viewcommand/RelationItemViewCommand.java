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

import java.math.BigDecimal;
import java.util.List;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 相关商品，比如推荐商品、最近浏览商品等
 *
 */
public class RelationItemViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = -7068802000352169607L;

	/** 商品id. */
    private Long itemId;

    /** 商品code. */
    private String itemCode;

    /** 商品名称. */
    private String itemName;

    /** 商品图片URL, 可能有多个. */
    private List<String> imageUrl;

    /** 吊牌价(原单价). */
    private BigDecimal listPrice;

    /** 销售价格. */
    private BigDecimal salePrice;
    
    /** 商品的颜色属性（或者同款商品）, 非必须. 暂不考虑 */
    private RelationItemColorSwatchViewCommand colorSwatch;
    
    /** 商品的扩展数据. {@link ItemExtraViewCommand} */
    private ItemExtraViewCommand extra;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public List<String> getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(List<String> imageUrl) {
		this.imageUrl = imageUrl;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public RelationItemColorSwatchViewCommand getColorSwatch() {
		return colorSwatch;
	}

	public void setColorSwatch(RelationItemColorSwatchViewCommand colorSwatch) {
		this.colorSwatch = colorSwatch;
	}

	public ItemExtraViewCommand getExtra() {
		return extra;
	}

	public void setExtra(ItemExtraViewCommand extra) {
		this.extra = extra;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((colorSwatch == null) ? 0 : colorSwatch.hashCode());
		result = prime * result + ((extra == null) ? 0 : extra.hashCode());
		result = prime * result
				+ ((imageUrl == null) ? 0 : imageUrl.hashCode());
		result = prime * result
				+ ((itemCode == null) ? 0 : itemCode.hashCode());
		result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
		result = prime * result
				+ ((itemName == null) ? 0 : itemName.hashCode());
		result = prime * result
				+ ((listPrice == null) ? 0 : listPrice.hashCode());
		result = prime * result
				+ ((salePrice == null) ? 0 : salePrice.hashCode());
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
		RelationItemViewCommand other = (RelationItemViewCommand) obj;
		if (colorSwatch == null) {
			if (other.colorSwatch != null)
				return false;
		} else if (!colorSwatch.equals(other.colorSwatch))
			return false;
		if (extra == null) {
			if (other.extra != null)
				return false;
		} else if (!extra.equals(other.extra))
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
			return false;
		if (itemCode == null) {
			if (other.itemCode != null)
				return false;
		} else if (!itemCode.equals(other.itemCode))
			return false;
		if (itemId == null) {
			if (other.itemId != null)
				return false;
		} else if (!itemId.equals(other.itemId))
			return false;
		if (itemName == null) {
			if (other.itemName != null)
				return false;
		} else if (!itemName.equals(other.itemName))
			return false;
		if (listPrice == null) {
			if (other.listPrice != null)
				return false;
		} else if (!listPrice.equals(other.listPrice))
			return false;
		if (salePrice == null) {
			if (other.salePrice != null)
				return false;
		} else if (!salePrice.equals(other.salePrice))
			return false;
		return true;
	}
    
}
