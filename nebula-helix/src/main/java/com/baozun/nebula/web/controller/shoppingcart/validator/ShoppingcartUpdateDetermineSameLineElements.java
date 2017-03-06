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
package com.baozun.nebula.web.controller.shoppingcart.validator;

import java.io.Serializable;
import java.util.List;

import com.baozun.nebula.web.controller.shoppingcart.form.PackageInfoForm;

/**
 * 购物车判断相同行要素.
 * 
 * <p>
 * 判断的原则: 在购物车行list里面查找,相同 skuId, 且lineGroup 相同, 且relatedItemId 相同,且packageInfoFormList 相同,且不是当前的currentLineId
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
public class ShoppingcartUpdateDetermineSameLineElements implements Serializable{

    /**  */
    private static final long serialVersionUID = 288232184048495608L;

    /**
     * 当前行的id.
     */
    private Long currentLineId;

    /** skuid. */
    private Long skuId;

    /** 行分组 *. */
    private Long lineGroup;

    /**
     * 对应关联关系的商品id.
     * 
     * <p>
     * 普通商品下单,这个属性是没有值,<br>
     * 如果商品是bundle商品,比如3个不同的item 的不同sku 进行下单,那么会有3条 soline记录,在需要显示订单行的场合,比如订单明细页面,下单之后的邮件模板,<br>
     * 为了和以往的数据兼容(尽量不更改或者少更改以往的SQL),那么soline里面的 itemId,存放这个sku原本的itemId,而relatedItemId存放的是衍生的itemId
     * </p>
     */
    private Long relatedItemId;

    /**
     * 包装信息.
     */
    private List<PackageInfoForm> packageInfoFormList;

    /**
     * 获得 当前行的id.
     *
     * @return the currentLineId
     */
    public Long getCurrentLineId(){
        return currentLineId;
    }

    /**
     * 设置 当前行的id.
     *
     * @param currentLineId
     *            the currentLineId to set
     */
    public void setCurrentLineId(Long currentLineId){
        this.currentLineId = currentLineId;
    }

    /**
     * 获得 skuid.
     *
     * @return the skuId
     */
    public Long getSkuId(){
        return skuId;
    }

    /**
     * 设置 skuid.
     *
     * @param skuId
     *            the skuId to set
     */
    public void setSkuId(Long skuId){
        this.skuId = skuId;
    }

    /**
     * 获得 行分组 *.
     *
     * @return the lineGroup
     */
    public Long getLineGroup(){
        return lineGroup;
    }

    /**
     * 设置 行分组 *.
     *
     * @param lineGroup
     *            the lineGroup to set
     */
    public void setLineGroup(Long lineGroup){
        this.lineGroup = lineGroup;
    }

    /**
     * 获得 对应关联关系的商品id.
     * 
     * <p>
     * 普通商品下单,这个属性是没有值,<br>
     * 如果商品是bundle商品,比如3个不同的item 的不同sku 进行下单,那么会有3条 soline记录,在需要显示订单行的场合,比如订单明细页面,下单之后的邮件模板,<br>
     * 为了和以往的数据兼容(尽量不更改或者少更改以往的SQL),那么soline里面的 itemId,存放这个sku原本的itemId,而relatedItemId存放的是衍生的itemId
     * </p>
     * 
     * @return the relatedItemId
     */
    public Long getRelatedItemId(){
        return relatedItemId;
    }

    /**
     * 设置 对应关联关系的商品id.
     * 
     * <p>
     * 普通商品下单,这个属性是没有值,<br>
     * 如果商品是bundle商品,比如3个不同的item 的不同sku 进行下单,那么会有3条 soline记录,在需要显示订单行的场合,比如订单明细页面,下单之后的邮件模板,<br>
     * 为了和以往的数据兼容(尽量不更改或者少更改以往的SQL),那么soline里面的 itemId,存放这个sku原本的itemId,而relatedItemId存放的是衍生的itemId
     * </p>
     * 
     * @param relatedItemId
     *            the relatedItemId to set
     */
    public void setRelatedItemId(Long relatedItemId){
        this.relatedItemId = relatedItemId;
    }

    /**
     * 获得 包装信息.
     *
     * @return the packageInfoFormList
     */
    public List<PackageInfoForm> getPackageInfoFormList(){
        return packageInfoFormList;
    }

    /**
     * 设置 包装信息.
     *
     * @param packageInfoFormList
     *            the packageInfoFormList to set
     */
    public void setPackageInfoFormList(List<PackageInfoForm> packageInfoFormList){
        this.packageInfoFormList = packageInfoFormList;
    }

}
