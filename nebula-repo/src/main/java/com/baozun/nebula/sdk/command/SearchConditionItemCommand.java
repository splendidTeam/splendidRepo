package com.baozun.nebula.sdk.command;

import java.util.Date;

import com.baozun.nebula.model.BaseModel;

public class SearchConditionItemCommand  extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5387423365062616587L;


	/** PK. */
	private Long				id;
	
	
	/**
	 * 选项名称(当关联的propertyValueId无值是，才会有效，否则name取关联的propertyValue的name)
	 */
	private String				name;
	
	/**
	 * 显示图片
	 */
	private String				img;
	
	/**
	 * 条件id
	 */
	private Long 				coditionId;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the img
	 */
	public String getImg() {
		return img;
	}

	/**
	 * @param img the img to set
	 */
	public void setImg(String img) {
		this.img = img;
	}

	/**
	 * @return the coditionId
	 */
	public Long getCoditionId() {
		return coditionId;
	}

	/**
	 * @param coditionId the coditionId to set
	 */
	public void setCoditionId(Long coditionId) {
		this.coditionId = coditionId;
	}

	/**
	 * @return the propertyValueId
	 */
	public Long getPropertyValueId() {
		return propertyValueId;
	}

	/**
	 * @param propertyValueId the propertyValueId to set
	 */
	public void setPropertyValueId(Long propertyValueId) {
		this.propertyValueId = propertyValueId;
	}

	/**
	 * @return the areaMin
	 */
	public Integer getAreaMin() {
		return areaMin;
	}

	/**
	 * @param areaMin the areaMin to set
	 */
	public void setAreaMin(Integer areaMin) {
		this.areaMin = areaMin;
	}

	/**
	 * @return the areaMax
	 */
	public Integer getAreaMax() {
		return areaMax;
	}

	/**
	 * @param areaMax the areaMax to set
	 */
	public void setAreaMax(Integer areaMax) {
		this.areaMax = areaMax;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param modifyTime the modifyTime to set
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * @return the lifecycle
	 */
	public Integer getLifecycle() {
		return lifecycle;
	}

	/**
	 * @param lifecycle the lifecycle to set
	 */
	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	/**
	 * @return the sort
	 */
	public Integer getSort() {
		return sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	/**
	 * @return the opeartorId
	 */
	public Long getOpeartorId() {
		return opeartorId;
	}

	/**
	 * @param opeartorId the opeartorId to set
	 */
	public void setOpeartorId(Long opeartorId) {
		this.opeartorId = opeartorId;
	}

	/**
	 * 关联属性值ID
	 */
	private Long 				propertyValueId;
    /**
     * 关联属性ID
     */
    private Long                propertyId;
	
	/**
	 * 关联属性值名称
	 */
    private String           propertyValueName;
    /**
     * 关联属性名
     */
    private String                propertyName;
	
    
    
	public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyValueName() {
        return propertyValueName;
    }

    public void setPropertyValueName(String propertyValueName) {
        this.propertyValueName = propertyValueName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
	 * 搜索条件为区间类型时，最小值
	 */
	private Integer				areaMin;
	/**
	 * 搜索条件为区间类型时，最大值
	 */
	private	Integer				areaMax;
	
	/** 创建时间. */
	private Date				createTime;

	/** 修改时间 */
	private Date				modifyTime;
	
	/**
	 * 生命周期
	 */
	private Integer				lifecycle;
	
	/**
	 * 排序
	 */
	private Integer				sort;
	
	/** 最后操作者 */
	private Long				opeartorId;
}
