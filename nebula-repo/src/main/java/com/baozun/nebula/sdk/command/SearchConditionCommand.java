package com.baozun.nebula.sdk.command;

import java.util.Date;
import java.util.List;

import com.baozun.nebula.model.BaseModel;

public class SearchConditionCommand  extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -908739805423916281L;


	/** PK. */
	private Long				id;
	
	/**
	 * 条件名称
	 */
	private String				name;
	
	/**
	 * 类型
	 * 1.一般类型
	 * 2.区间类型
	 */
	private Integer				type;
	
	/**
	 * 关联的商品属性id
	 */
	private Long 				propertyId;
	
	/**
	 * 条件描述
	 */
	private String				description;
	
	/** 创建时间. */
	private Date				createTime;

	/** 修改时间 */
	private Date				modifyTime;
	
	/**
	 * 生命周期
	 */
	private Integer				lifecycle;
	
	/**
	 * 搜索条件对应的分类Id
	 */
	@Deprecated
	private Long 				categoryId;
	
	/**
	 * 排序
	 */
	private Integer				sort;
	
	/** 最后操作者 */
	private Long				opeartorId;
	
	/** version. */
	private Date				version;
	
	/**
	 * 关联的商品属性名
	 */
	private String propertyName;
    /**
     * 关联的行业属性名
     */
	private String industryName;
    /**
     * 关联的行业属性名
     */
    private Long industryId;
    /**
     * 关联的类型名
     */
    @Deprecated
    private String categoryName;
    
    
    /**
     * 导航id
     */
    private Long  navigationId;
    
    /**
     * 导航名称
     */
    private String	navigationName;
    
    

    public String getNavigationName() {
		return navigationName;
	}

	public void setNavigationName(String navigationName) {
		this.navigationName = navigationName;
	}

	public Long getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Long industryId) {
        this.industryId = industryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    private List<SearchConditionItemCommand> itemList; 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Long getOpeartorId() {
		return opeartorId;
	}

	public void setOpeartorId(Long opeartorId) {
		this.opeartorId = opeartorId;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	public void setItemList(List<SearchConditionItemCommand> itemList) {
		this.itemList = itemList;
	}

	public List<SearchConditionItemCommand> getItemList() {
		return itemList;
	}

	public Long getNavigationId() {
		return navigationId;
	}

	public void setNavigationId(Long navigationId) {
		this.navigationId = navigationId;
	}
	
}
