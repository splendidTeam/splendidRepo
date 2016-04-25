/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
*
 
* This software is the confidential and proprietary information of Baozun.
 
* You shall not disclose such Confidential Information and shall use it only in
 
* accordance with the terms of the license agreement you entered into
 
* with Baozun.
 
*
 
* BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 
* SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 
* IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 
* PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 
* SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 
* THIS SOFTWARE OR ITS DERIVATIVES.
 
*
 
*/
package com.baozun.nebula.web.controller.bundle.viewcommand;

import java.math.BigDecimal;
import java.util.List;
import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 捆绑类商品视图模型
 * 
 * <ul>
 * 	<li>捆绑类商品与普通商品一样存在商品扩展信息</li>
 * 	<li>捆绑类商品不存在属性，所以最多只可能存在一套图片</li>
 * 	<li>
 * 		捆绑类商品存在三种价格，商城视需求可以采用listPrice或者originalSalesPrice作为原价的设置：
 * 		<ol>
 * 			<li>listPrice -- 吊牌价</li>
 * 			<li>originalSalesPrice -- 原销售价</li>
 * 			<li>salesPrice -- 在捆绑类商品中设置的销售价</li>
 * 		</ol>
 * 	</li>
 * </ul>
 * @author yue.ch
 *
 */
public class BundleViewCommand extends BaseViewCommand {
	
	private static final long serialVersionUID = -542377171565361284L;

	/**
	 * 捆绑类商品ID
	 */
	private Long id ;
	
	/**
	 * 捆绑类商品对应的商品ID
	 */
	private Long itemId ;
	
	/**
	 * 捆绑类商品的编码
	 */
	private String code ;
	
	/**
	 * 最小吊牌价
	 */
	private BigDecimal minListPrice;
	
	/**
	 * 最大吊牌价
	 */
	private BigDecimal maxListPrice;
	
	/**
	 * 最小原销售价
	 */
	private BigDecimal minOriginalSalesPrice ;
	/**
	 * 最大原销售价
	 */
	private BigDecimal maxOriginalSalesPrice ;
	/**
	 * 最小销售价
	 */
	private BigDecimal minSalesPrice ;
	/**
	 * 最大销售价
	 */
	private BigDecimal maxSalesPrice ;
	/**
	 * 捆绑类商品成员
	 */
	private List<BundleElementViewCommand> bundleElementViewCommands;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getMinListPrice() {
		return minListPrice;
	}

	public void setMinListPrice(BigDecimal minListPrice) {
		this.minListPrice = minListPrice;
	}

	public BigDecimal getMaxListPrice() {
		return maxListPrice;
	}

	public void setMaxListPrice(BigDecimal maxListPrice) {
		this.maxListPrice = maxListPrice;
	}

	public BigDecimal getMinOriginalSalesPrice() {
		return minOriginalSalesPrice;
	}

	public void setMinOriginalSalesPrice(BigDecimal minOriginalSalesPrice) {
		this.minOriginalSalesPrice = minOriginalSalesPrice;
	}

	public BigDecimal getMaxOriginalSalesPrice() {
		return maxOriginalSalesPrice;
	}

	public void setMaxOriginalSalesPrice(BigDecimal maxOriginalSalesPrice) {
		this.maxOriginalSalesPrice = maxOriginalSalesPrice;
	}

	public BigDecimal getMinSalesPrice() {
		return minSalesPrice;
	}

	public void setMinSalesPrice(BigDecimal minSalesPrice) {
		this.minSalesPrice = minSalesPrice;
	}

	public BigDecimal getMaxSalesPrice() {
		return maxSalesPrice;
	}

	public void setMaxSalesPrice(BigDecimal maxSalesPrice) {
		this.maxSalesPrice = maxSalesPrice;
	}

	public List<BundleElementViewCommand> getBundleElementViewCommands() {
		return bundleElementViewCommands;
	}

	public void setBundleElementViewCommands(
			List<BundleElementViewCommand> bundleElementViewCommands) {
		this.bundleElementViewCommands = bundleElementViewCommands;
	}
}
