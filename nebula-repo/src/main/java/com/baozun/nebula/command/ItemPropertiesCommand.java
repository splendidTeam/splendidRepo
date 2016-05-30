package com.baozun.nebula.command;

import java.io.Serializable;
import java.util.List;

import com.baozun.nebula.model.product.ItemPropertiesLang;
import com.baozun.nebula.model.product.PropertyValueLang;

public class ItemPropertiesCommand implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2880695280231803463L;

	private Long 				id;
	/**
	 * 商品属性值
	 */
	private Long				itemId;
	
	/**
	 * 商品编码
	 */
	private String				itemCode;

	/**
	 * 商品属性值
	 */
	private String				property_id;

	/**
	 * 商品属性名
	 */
	private String				propertyName;

	/**
	 * 商品属性值
	 */
	private String				propertyValue;

	/**
	 * 商品属性值
	 */
	private String				proValue;

	/**
	 * 排序
	 * 
	 * @return
	 */
	private Integer				proSort;

	/**
	 * 类型
	 * 
	 * @return
	 */
	private Integer				type;

	/**
	 * 是否颜色属性
	 * 
	 * @return
	 */
	private Boolean				is_color_prop;

	/**
	 * item_properties_id
	 * 
	 * @return
	 */
	private Long				item_properties_id;

	/**
	 * 属性值Id
	 */
	private Long				propertyValueId;
	
	/**
	 * 配图地址，绝对地址
	 */
	private String              thumb;
	/**
	 * 所属属性
	 */
	private Long				propertyId;

	/**
	 * 是否可用
	 */
	private Boolean				isEnabled			= false;
	
	/**
	 * 属性值的多语言（单行输入、自定义多选）
	 */
	private List<ItemPropertiesLang> itemPropertiesLangs;
	
	/**
	 * 属性值可选项的多语言（单选、可输入单选、多选）
	 */
	private List<PropertyValueLang> propertyValueLangs;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getProperty_id() {
		return property_id;
	}

	public void setProperty_id(String property_id) {
		this.property_id = property_id;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	
	/**
	 * @return the thumb
	 */
	public String getThumb() {
		return thumb;
	}

	/**
	 * @param thumb the thumb to set
	 */
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getProValue() {
		return proValue;
	}

	public void setProValue(String proValue) {
		this.proValue = proValue;
	}

	public Integer getProSort() {
		return proSort;
	}

	public void setProSort(Integer proSort) {
		this.proSort = proSort;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Boolean getIs_color_prop() {
		return is_color_prop;
	}

	public void setIs_color_prop(Boolean is_color_prop) {
		this.is_color_prop = is_color_prop;
	}

	public Long getItem_properties_id() {
		return item_properties_id;
	}

	public void setItem_properties_id(Long item_properties_id) {
		this.item_properties_id = item_properties_id;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Long getPropertyValueId() {
		return propertyValueId;
	}

	public void setPropertyValueId(Long propertyValueId) {
		this.propertyValueId = propertyValueId;
	}

	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}

	public List<ItemPropertiesLang> getItemPropertiesLangs() {
		return itemPropertiesLangs;
	}

	public void setItemPropertiesLangs(List<ItemPropertiesLang> itemPropertiesLangs) {
		this.itemPropertiesLangs = itemPropertiesLangs;
	}

	public List<PropertyValueLang> getPropertyValueLangs() {
		return propertyValueLangs;
	}

	public void setPropertyValueLangs(List<PropertyValueLang> propertyValueLangs) {
		this.propertyValueLangs = propertyValueLangs;
	}
	
}
