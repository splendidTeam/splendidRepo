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

/**
 * 普通的立即购买.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public class CommonImmediatelyBuyForm extends AbstractCommonAddForm implements ImmediatelyBuyForm{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6517134078670491779L;

    /**
     * 
     */
    public CommonImmediatelyBuyForm(){
        super();
    }

    /**
     * @param skuId
     * @param count
     */
    public CommonImmediatelyBuyForm(Long skuId, Integer count){
        super();
        this.skuId = skuId;
        this.count = count;
    }

    /**
     * @param skuId
     * @param count
     * @param packageInfoFormList
     */
    public CommonImmediatelyBuyForm(Long skuId, Integer count, List<PackageInfoForm> packageInfoFormList){
        super();
        this.skuId = skuId;
        this.count = count;
        this.packageInfoFormList = packageInfoFormList;
    }
}
