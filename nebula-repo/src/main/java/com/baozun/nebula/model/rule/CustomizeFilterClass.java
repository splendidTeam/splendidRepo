package com.baozun.nebula.model.rule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

@Entity
@Table(name = "T_PRM_CUSTOMIZEFILTERCLASSES")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class CustomizeFilterClass extends BaseModel{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1059040381849327184L;

	private Long id;
	
	private Integer scopeType;
	
	private String scopeName;
	
	private String serviceName;
	
	private Integer cacheSecond = 0;
	
	private Integer lifecycle;
	
	private Long shopId;
	
	private Date cacheVersion;
	
	private Date version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PRM_CUSTOMIZECLASSES",sequenceName = "S_T_PRM_CUSTOMIZECLASSES",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PRM_CUSTOMIZECLASSES")
	public Long getId(){
		return id;
	}

	
	public void setId(Long id){
		this.id = id;
	}

	@Column(name = "SCOPE_TYPE")
	public Integer getScopeType(){
		return scopeType;
	}

	
	public void setScopeType(Integer scopeType){
		this.scopeType = scopeType;
	}

	@Column(name = "SCOPE_NAME")
	public String getScopeName(){
		return scopeName;
	}

	
	public void setScopeName(String scopeName){
		this.scopeName = scopeName;
	}

	@Column(name = "SERVICE_NAME")
	public String getServiceName(){
		return serviceName;
	}

	
	public void setServiceName(String serviceName){
		this.serviceName = serviceName;
	}
	
	@Column(name = "CACHE_SECOND")
	public Integer getCacheSecond(){
		return cacheSecond;
	}

	
	public void setCacheSecond(Integer cacheSecond){
		this.cacheSecond = cacheSecond;
	}
	
	@Column(name = "LIFECYCLE")
	public Integer getLifecycle(){
		return lifecycle;
	}

	
	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	@Column(name = "SHOP_ID")
	public Long getShopId(){
		return shopId;
	}

	
	public void setShopId(Long shopId){
		this.shopId = shopId;
	}
	
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "CACHEVERSION")
	public Date getCacheVersion() {
		return cacheVersion;
	}


	public void setCacheVersion(Date cacheVersion) {
		this.cacheVersion = cacheVersion;
	}
}

	
