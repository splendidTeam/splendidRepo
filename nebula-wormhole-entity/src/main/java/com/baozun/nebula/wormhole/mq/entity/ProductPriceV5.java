package com.baozun.nebula.wormhole.mq.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品价格 SCM告知官方商城商品价格的变更信息
 * 
 * @author Justin Hu
 * 
 */

public class ProductPriceV5 implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7804003089192991656L;

	/**
	 * 价格级别 指价格是按商品来还是按SKU来，
	 * 官方商城需要配置一种模式，
	 * 并检查消息是否符合该模式，
	 * 不能同时支持两种模式
	 *  1 为商品
	 *  2 为SKU
	 */
	private Integer				priceLevel;

	/**
	 * 供应商商品编码 ,即货号
	 */
	private String				supplierSkuCode;

	/**
	 * oms商品编码,即宝尊编码
	 */
	private String				jmCode;

	/**
	 * sku唯一标识(到尺码,颜色)
	 */
	private String				extentionCode;

	/**
	 * 吊牌价
	 */
	private BigDecimal			listPrice;

	/**
	 * 销售价
	 */
	private BigDecimal			salesPrice;

	public String getJmCode() {
		return jmCode;
	}

	public void setJmCode(String jmCode) {
		this.jmCode = jmCode;
	}

	public String getSupplierSkuCode() {
		return supplierSkuCode;
	}

	public void setSupplierSkuCode(String supplierSkuCode) {
		this.supplierSkuCode = supplierSkuCode;
	}

	public Integer getPriceLevel() {
		return priceLevel;
	}

	public void setPriceLevel(Integer priceLevel) {
		this.priceLevel = priceLevel;
	}

	public String getExtentionCode() {
		return extentionCode;
	}

	public void setExtentionCode(String extentionCode) {
		this.extentionCode = extentionCode;
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
