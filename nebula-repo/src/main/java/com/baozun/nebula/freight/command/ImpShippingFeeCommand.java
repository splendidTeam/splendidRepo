package com.baozun.nebula.freight.command;

import java.math.BigDecimal;

import com.baozun.nebula.command.Command;

public class ImpShippingFeeCommand implements Command{

	
	private static final long serialVersionUID = -4128944016781162853L;

	
	/** 物流方式name*/
	private String distributionModeName;
	
	/**  目的地name:可能是省-市-区-县-镇*/
	private String destAreaName;
	
	/** 省 name*/
	private String				province;
	
	/** 市name */
	private String			    city;
	
	/** 区name*/
	private String			    area;
	
	/** 镇name*/
	private String				town;

	/** 首件运费的单位*/
	private Integer firstPartUnit;
	
	/** 续件运费的单位*/
	private Integer subsequentPartUnit;
	
	/** 首件运费价格*/
	private BigDecimal firstPartPrice;
	
	/** 续件运费价格*/
	private BigDecimal subsequentPartPrice;
	
	/** 对应了 基础 类型的 基础价格*/
	private BigDecimal basePrice;
	
	public String getDistributionModeName() {
		return distributionModeName;
	}
	
	public void setDistributionModeName(String distributionModeName) {
		this.distributionModeName = distributionModeName;
	}
	
	public String getDestAreaName() {
		return destAreaName;
	}
	
	public void setDestAreaName(String destAreaName) {
		this.destAreaName = destAreaName;
	}
	
	public Integer getFirstPartUnit() {
		return firstPartUnit;
	}
	
	public void setFirstPartUnit(Integer firstPartUnit) {
		this.firstPartUnit = firstPartUnit;
	}
	
	public Integer getSubsequentPartUnit() {
		return subsequentPartUnit;
	}
	
	public void setSubsequentPartUnit(Integer subsequentPartUnit) {
		this.subsequentPartUnit = subsequentPartUnit;
	}
	
	public BigDecimal getFirstPartPrice() {
		return firstPartPrice;
	}
	
	public void setFirstPartPrice(BigDecimal firstPartPrice) {
		this.firstPartPrice = firstPartPrice;
	}
	
	public BigDecimal getSubsequentPartPrice() {
		return subsequentPartPrice;
	}
	
	public void setSubsequentPartPrice(BigDecimal subsequentPartPrice) {
		this.subsequentPartPrice = subsequentPartPrice;
	}
	
	public BigDecimal getBasePrice() {
		return basePrice;
	}
	
	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}
	
	
	
}
