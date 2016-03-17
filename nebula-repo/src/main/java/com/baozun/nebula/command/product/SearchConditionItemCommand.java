package com.baozun.nebula.command.product;

import java.util.Date;

import loxia.annotation.Column;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.command.i18n.LangProperty;

/**
 * 搜索条件选项
 * 
 * @author Justin Hu
 * 
 */
public class SearchConditionItemCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8195576919245959945L;

	/** PK. */
	private Long id;

	/**
	 * 选项名称(在页面上显示的搜索选项的具体值的名称)
	 */
	private LangProperty name;

	/**
	 * 显示图片
	 */
	private String img;

	/**
	 * 条件id 只在searchCondition为区间或价格类型是,此字段才有值
	 */
	private Long coditionId;

	/**
	 * 关联的商品属性id 与条件id二选一，互斥 是为了解决多个searchCondition都对应同一个propertyId的问题
	 * searchConditionItem与property-refrencs相对应的
	 */
	private Long propertyId;

	/**
	 * 关联属性值ID
	 */
	private Long propertyValueId;

	/**
	 * 搜索条件为区间类型时，最小值
	 */
	private Integer areaMin;
	/**
	 * 搜索条件为区间类型时，最大值
	 */
	private Integer areaMax;

	/** 创建时间. */
	private Date createTime;

	/** 修改时间 */
	private Date modifyTime;

	/**
	 * 生命周期
	 */
	private Integer lifecycle;

	/**
	 * 排序
	 */
	private Integer sort;

	/** 最后操作者 */
	private Long opeartorId;

	/** version. */
	private Date version;

	@Column("ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LangProperty getName() {
		return name;
	}

	public void setName(LangProperty name) {
		this.name = name;
	}

	@Column("IMG")
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Column("CODITION_ID")
	public Long getCoditionId() {
		return coditionId;
	}

	public void setCoditionId(Long coditionId) {
		this.coditionId = coditionId;
	}

	@Column("VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column("CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column("MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column("LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	@Column("SORT")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Column("OPEARTOR_ID")
	public Long getOpeartorId() {
		return opeartorId;
	}

	public void setOpeartorId(Long opeartorId) {
		this.opeartorId = opeartorId;
	}

	@Column("PROPERTY_VALUE_ID")
	public Long getPropertyValueId() {
		return propertyValueId;
	}

	public void setPropertyValueId(Long propertyValueId) {
		this.propertyValueId = propertyValueId;
	}

	@Column("AREA_MIN")
	public Integer getAreaMin() {
		return areaMin;
	}

	public void setAreaMin(Integer areaMin) {
		this.areaMin = areaMin;
	}

	@Column("AREA_MAX")
	public Integer getAreaMax() {
		return areaMax;
	}

	public void setAreaMax(Integer areaMax) {
		this.areaMax = areaMax;
	}

	@Column("PROPERTY_ID")
	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}
}
