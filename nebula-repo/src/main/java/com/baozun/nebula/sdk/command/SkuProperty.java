package com.baozun.nebula.sdk.command;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.product.ItemProperties;

public class SkuProperty  implements Command{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2190752157846556115L;
	private String	id;
	private String	pId;
	private String	value;
	private String	pName;
	// 是否颜色属性
	private Boolean	isColorProp;
	// item properties id
	private ItemProperties itemProperties;

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	@Override
	public String toString() {
		return "Pro [id=" + id + ", pId=" + pId + ", value=" + value + ", pName=" + pName + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getIsColorProp() {
		return isColorProp;
	}

	public void setIsColorProp(Boolean isColorProp) {
		this.isColorProp = isColorProp;
	}

	public ItemProperties getItemProperties() {
		return itemProperties;
	}

	public void setItemProperties(ItemProperties itemProperties) {
		this.itemProperties = itemProperties;
	}
}
