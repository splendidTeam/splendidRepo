package com.baozun.nebula.freight.command;

import com.baozun.nebula.command.Command;

public class ExpSupportedAreaCommand implements Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2111033425858133570L;

	/** 省 name*/
	private String				province;
	
	/** 市name */
	private String			    city;
	
	/** 区/县name*/
	private String			    county;
	
	/** 镇name*/
	private String				town;
	
	private String   			type;
	/** 物流名字*/
	private String   			distributionName;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getDistributionName() {
		return distributionName;
	}
	public void setDistributionName(String distributionName) {
		this.distributionName = distributionName;
	}
	
}

