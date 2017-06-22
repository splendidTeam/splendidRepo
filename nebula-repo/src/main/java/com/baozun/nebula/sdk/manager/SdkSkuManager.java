/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.sdk.manager;

import java.math.BigDecimal;
import java.util.List;

import com.baozun.nebula.exception.NativeUpdateRowCountNotEqualException;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.SkuCommand;
import com.baozun.nebula.sdk.command.SkuProperty;

/**
 * The Interface SdkSkuManager.
 */
public interface SdkSkuManager extends BaseManager{

    /**
     * 通过skuId查询SKU.
     *
     * @param skuId
     *            the sku id
     * @return :Sku
     * @date 2014-2-17 下午08:15:56
     */
    Sku findSkuById(Long skuId);

    /**
     * 动态属性转换.
     *
     * @param properties
     *            the properties
     * @return the sku pros
     */
    List<SkuProperty> getSkuPros(String properties);

    /**
     * 增加sku的库存.
     *
     * @param extentionCode
     *            the extention code
     * @param count
     *            the count
     */
    void addSkuInventory(String extentionCode,Integer count);

    /**
     * 通过outId查询sku集合(lifecycle=1).
     *
     * @param outIdList
     *            the out id list
     * @return the list< sku>
     */
    List<Sku> findSkuByOutIds(List<String> outIdList);


    /**
     * 同步商品价格(sku级别).
     *
     *
     *<p>在执行sql的时候，如果返回值不是1，那么会抛出  NativeUpdateRowCountNotEqualException 异常,如有需要可以捕获</p>
     * @param salesPrice
     *            the sales price
     * @param listPrice
     *            the list price
     * @param extentionCode
     *            the extention code
     * @return the integer
     * @since 5.3.2.18 change return type from Integer to void
     */
    void syncSkuPriceByExtentionCode(BigDecimal salesPrice,BigDecimal listPrice,String extentionCode) ;

    /**
     * 同步商品的sku价格(item级别).
     *
     * @param salesPrice
     *            the sales price
     * @param listPrice
     *            the list price
     * @param itemCode
     *            the item code
     * @return the integer
     */
    Integer updateSkuPriceByItemCode(BigDecimal salesPrice,BigDecimal listPrice,String itemCode);

    /**
     * 通过extentionCode查询sku信息.
     *
     * @param extentionCode
     *            the extention code
     * @return the sku
     */
    Sku findSkuByExtentionCode(String extentionCode);

    /**
     * 获取该尺寸下的虚拟库存.
     *
     * @param skuId
     *            the sku id
     * @param extCode
     *            the ext code
     * @return the sku command
     */
    SkuCommand findSkuQSVirtualInventoryById(Long skuId,String extCode);
	/**
	 * @Title: findSkuByOutId
	 * @Description: 通过outId查询sku(lifecycle=1).
	 * @param outIdList
	 * @return
	 * @return: Sku
	 */
	Sku findSkuByOutId(String outId);
}
