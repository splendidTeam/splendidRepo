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
package com.baozun.nebula.sdk.manager.shoppingcart.extractor;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
public abstract class AbstractShoppingcartDetermineSameLineElements implements Serializable{

    /**  */
    private static final long serialVersionUID = -7391444847056444666L;

    /** skuid. */
    private Long skuId;

    /**
     * 包装信息.
     */
    private List<PackageInfoElement> packageInfoElementList;

    //-------

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
     * 获得 包装信息.
     *
     * @return the packageInfoElementList
     */
    public List<PackageInfoElement> getPackageInfoElementList(){
        return packageInfoElementList;
    }

    /**
     * 设置 包装信息.
     *
     * @param packageInfoElementList
     *            the packageInfoElementList to set
     */
    public void setPackageInfoElementList(List<PackageInfoElement> packageInfoElementList){
        this.packageInfoElementList = packageInfoElementList;
    }

}
