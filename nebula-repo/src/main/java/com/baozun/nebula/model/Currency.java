package com.baozun.nebula.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

/**
 * 货币单位。系统中可能存在多种货币单位，但目前系统仅支持一种货币单位，且此货币单位必须是本位币。
 * 
 * @author benjamin
 */
@Entity
@Table(name = "T_MA_CURRENCY")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Currency extends BaseModel{

	private static final long	serialVersionUID	= -4977513502140770410L;

	@Transient
	public Class<?> getModelClass(){
		return Currency.class;
	}

	/**
	 * PK
	 */
	private Long		id;

	/**
	 * Version
	 */
	private Date		version;

	/**
	 * 生命周期
	 */
	private Integer		lifeCycleStatus	= DEFAULT_STATUS;

	/**
	 * 创建时间
	 */
	private Date		createTime		= new Date();

	/**
	 * 货币单位编码
	 */
	private String		code;

	/**
	 * 货币单位名称
	 */
	private String		name;

	/**
	 * 货币单位标准符号
	 */
	private String		stdSymbol;

	/**
	 * 是否是本位币
	 */
	private Boolean		isLocalCurrency;

	/**
	 * 该单位和本位币的换算关系
	 */
	private BigDecimal	directQuotation;

	// Constructors

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_CURR",sequenceName = "S_T_MA_CURRENCY",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_CURR")
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion(){
		return version;
	}

	public void setVersion(Date version){
		this.version = version;
	}

	@Column(name = "LIFE_CYCLE_STATUS")
	@Index(name = "IDX_CURRENCY_LIFECYCLESTATUS")
	public Integer getLifeCycleStatus(){
		return lifeCycleStatus;
	}

	public void setLifeCycleStatus(Integer lifeCycleStatus){
		this.lifeCycleStatus = lifeCycleStatus;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	@Column(name = "CODE",length = 20)
	public String getCode(){
		return code;
	}

	public void setCode(String code){
		this.code = code;
	}

	@Column(name = "NAME",length = 20)
	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	@Column(name = "STD_SYMBOL",length = 5)
	public String getStdSymbol(){
		return stdSymbol;
	}

	public void setStdSymbol(String stdSymbol){
		this.stdSymbol = stdSymbol;
	}

	@Column(name = "IS_LOCAL_CURRENCY")
	public Boolean getIsLocalCurrency(){
		return isLocalCurrency;
	}

	public void setIsLocalCurrency(Boolean isLocalCurrency){
		this.isLocalCurrency = isLocalCurrency;
	}

	@Column(name = "DIRECT_QUOTATION",precision = 15,scale = 5)
	public BigDecimal getDirectQuotation(){
		return directQuotation;
	}

	public void setDirectQuotation(BigDecimal directQuotation){
		this.directQuotation = directQuotation;
	}

}
