package com.baozun.nebula.web.command;

import java.io.Serializable;
/**
 * <h3>bundle校验的结果对象</h3>
 * <p>通过type可以确认校验不通过的类型  ： 参考{@link com.baozun.nebula.command.bundle.BundleCommand.BundleStatus}}</p>
 * <ul>
 *    <li>如果bundle本身的基本信息（bundle库存，bundle状态）校验不通过，那么bundleId有值，itemId\skuId 都为空</li>
 *    <li>如果bundle本身的基本信息校验通过,但是bundle中的某个商品不可售,那么bundleId\itemId有值 ，skuId为空</li>
 *    <li>如果bundle本身的基本信息校验通过并且bundle的商品信息也校验通过,但是bundle中的某个sku库存不足, 那么 bundleId itemId skuId 都有值</li>
 * </ul>
 *
 * @Description : com.baozun.nebula.web.commandBundleValidateResult.java
 * @Company  : BAOZUN
 * @author :  jiaolong.chen
 * @data : 2016年4月19日下午1:28:11
 */
public class BundleValidateResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8061407680767825616L;

	/**
	 * 校验结果的类型
	 * 参考{@link com.baozun.nebula.command.bundle.BundleCommand.BundleStatus}}
	 */
	private int type ;
	
	/**
	 * 如果由于bundle库存不足或者不可售导致其校验不通过,赋值
	 */
	private Long bundleId;
	/**
	 * 如果bundle的某个商品不可售,那么其代表的就是不可售商品的主键,赋值
	 */
	private Long itemId;
	/**
	 * 如果bundle的某个商品其中的某个sku库存不足,那么其代表的就是sku的主键,赋值
	 */
	private Long skuId;

	public BundleValidateResult(int type, Long bundleId, Long itemId, Long skuId) {
		this.type = type;
		this.bundleId = bundleId;
		this.itemId = itemId;
		this.skuId = skuId;
	}
	
	public BundleValidateResult() {
	
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getBundleId() {
		return bundleId;
	}

	public void setBundleId(Long bundleId) {
		this.bundleId = bundleId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	
	
}
