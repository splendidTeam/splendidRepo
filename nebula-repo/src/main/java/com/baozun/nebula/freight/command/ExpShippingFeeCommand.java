package com.baozun.nebula.freight.command;


import com.baozun.nebula.command.Command;

public class ExpShippingFeeCommand implements Command{

	
	private static final long serialVersionUID = -4128944016781162853L;

	
	/** 物流方式name*/
	private String distributionModeName;
	
	/** 省 name*/
	private String				province;
	
	/** 市name */
	private String			    city;
	
	/** 区name*/
	private String			    area;
	
	/** 镇name*/
	private String				town;

	/** 首件运费的单位*/
	private String firstPartUnit;
	
	/** 续件运费的单位*/
	private String subsequentPartUnit;
	
	/** 首件运费价格*/
	private String firstPartPrice;
	
	/** 续件运费价格*/
	private String subsequentPartPrice;
	
	/** 模板name*/
	private String templateName;
	
	
	public String getDistributionModeName() {
		return distributionModeName;
	}
	
	public void setDistributionModeName(String distributionModeName) {
		this.distributionModeName = distributionModeName;
	}
	
	

	public String getFirstPartUnit() {
		return firstPartUnit;
	}

	public void setFirstPartUnit(String firstPartUnit) {
		this.firstPartUnit = firstPartUnit;
	}

	public String getSubsequentPartUnit() {
		return subsequentPartUnit;
	}

	public void setSubsequentPartUnit(String subsequentPartUnit) {
		this.subsequentPartUnit = subsequentPartUnit;
	}


	public String getFirstPartPrice() {
		return firstPartPrice;
	}

	public void setFirstPartPrice(String firstPartPrice) {
		this.firstPartPrice = firstPartPrice;
	}

	public String getSubsequentPartPrice() {
		return subsequentPartPrice;
	}

	public void setSubsequentPartPrice(String subsequentPartPrice) {
		this.subsequentPartPrice = subsequentPartPrice;
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

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	
	
}

