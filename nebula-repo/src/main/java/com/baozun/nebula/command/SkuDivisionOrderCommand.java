package com.baozun.nebula.command;

import java.math.BigDecimal;



public class SkuDivisionOrderCommand  implements Command{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3332392845068830199L;
	private Long id;
	private String code;
	private String name;
	private BigDecimal listPrice;
	private BigDecimal salesPrice;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getListPrice() {
		return listPrice;
	}
	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}
	public BigDecimal getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}
	
	
}
