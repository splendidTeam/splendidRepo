package com.baozun.nebula.command.rule;

import java.util.Date;

import com.baozun.nebula.model.BaseModel;

public class CustomizeFilterClassCommand extends BaseModel {

	/**
	 * mdl
	 */
	private static final long serialVersionUID = 5372775661578552515L;
	
	private Long id;
	
	private Integer scopeType;
	
	//筛选器类型名
	private String scopeTypeName;
	
	//筛选器名称
	private String scopeName;
	
	private String serviceName;
	
	private Integer cacheSecond;
	
	private Integer lifecycle;
	
	//预留：店铺id
	private Long shopId;
	
	private Date cacheVersion;
	
	//预留:创建时间
	private Date version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getScopeType() {
		return scopeType;
	}

	public void setScopeType(Integer scopeType) {
		this.scopeType = scopeType;
	}

	public String getScopeTypeName() {
		return scopeTypeName;
	}

	public void setScopeTypeName(String scopeTypeName) {
		this.scopeTypeName = scopeTypeName;
	}

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Integer getCacheSecond() {
		return cacheSecond;
	}

	public void setCacheSecond(Integer cacheSecond) {
		this.cacheSecond = cacheSecond;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Date getCacheVersion() {
		return cacheVersion;
	}

	public void setCacheVersion(Date cacheVersion) {
		this.cacheVersion = cacheVersion;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	public CustomizeFilterClassCommand(Long id, Integer scopeType,
			String scopeTypeName, String scopeName, String serviceName,
			Integer cacheSecond, Integer lifecycle, Long shopId,
			Date cacheVersion, Date version) {
		super();
		this.id = id;
		this.scopeType = scopeType;
		this.scopeTypeName = scopeTypeName;
		this.scopeName = scopeName;
		this.serviceName = serviceName;
		this.cacheSecond = cacheSecond;
		this.lifecycle = lifecycle;
		this.shopId = shopId;
		this.cacheVersion = cacheVersion;
		this.version = version;
	}

	public CustomizeFilterClassCommand() {
		super();
	}

	@Override
	public String toString() {
		return "CustomizeFilterClassCommand [id=" + id + ", scopeType="
				+ scopeType + ", scopeTypeName=" + scopeTypeName
				+ ", scopeName=" + scopeName + ", serviceName=" + serviceName
				+ ", cacheSecond=" + cacheSecond + ", lifecycle=" + lifecycle
				+ ", shopId=" + shopId + ", cacheVersion=" + cacheVersion
				+ ", version=" + version + "]";
	}
	
	

}
