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
 * @since 5.3.2.13
 */
public class ShoppingCartLineAddForm extends AbstractCommonAddForm{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1175182005329799871L;

    /**
     * 
     */
    public ShoppingCartLineAddForm(){
        super();
    }

    /**
     * @param skuId
     * @param count
     */
    public ShoppingCartLineAddForm(Long skuId, Integer count){
        super();
        this.skuId = skuId;
        this.count = count;
    }

    /**
     * @param skuId
     * @param count
     * @param packageInfoFormList
     */
    public ShoppingCartLineAddForm(Long skuId, Integer count, List<PackageInfoForm> packageInfoFormList){
        super();
        this.skuId = skuId;
        this.count = count;
        this.packageInfoFormList = packageInfoFormList;
    }

}
