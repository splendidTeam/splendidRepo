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
package com.baozun.nebula.web.controller.shoppingcart.form;

import java.util.List;

import com.baozun.nebula.web.controller.BaseForm;

/**
 * 购物车行 添加的form.
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * <ol>
 * <li>封装成对象,便于将来扩展</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.baozun.nebula.model.shoppingcart.ShoppingCartLine
 * @since 5.3.2.11-Personalise
 */
public class ShoppingCartLineAddForm extends BaseForm{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1175182005329799871L;

    /** 买的哪个skuid(必填). */
    private Long skuId;

    /** 买几个(必填). */
    private Integer count;

    /** 包装信息. */
    private List<PackageInfoForm> packageInfoFormList;

    /**
     * 获得 买的哪个skuid(必填).
     *
     * @return the skuId
     */
    public Long getSkuId(){
        return skuId;
    }

    /**
     * 设置 买的哪个skuid(必填).
     *
     * @param skuId
     *            the skuId to set
     */
    public void setSkuId(Long skuId){
        this.skuId = skuId;
    }

    /**
     * 获得 买几个(必填).
     *
     * @return the count
     */
    public Integer getCount(){
        return count;
    }

    /**
     * 设置 买几个(必填).
     *
     * @param count
     *            the count to set
     */
    public void setCount(Integer count){
        this.count = count;
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
