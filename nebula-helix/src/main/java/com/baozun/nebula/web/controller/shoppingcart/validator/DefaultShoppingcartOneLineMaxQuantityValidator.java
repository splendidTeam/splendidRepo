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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.builder.DefaultShoppingcartOneLineMaxQuantityBuilder;
import com.baozun.nebula.web.controller.shoppingcart.builder.ShoppingcartOneLineMaxQuantityBuilder;

/**
 * 默认实现.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1.9
 */
@Component("shoppingcartOneLineMaxQuantityValidator")
public class DefaultShoppingcartOneLineMaxQuantityValidator implements ShoppingcartOneLineMaxQuantityValidator{

    @Autowired(required = false)
    private ShoppingcartOneLineMaxQuantityBuilder shoppingcartOneLineMaxQuantityBuilder = new DefaultShoppingcartOneLineMaxQuantityBuilder();

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartOneLineMaxQuantityValidator#isGreaterThanThreshold(com.baozun.nebula.web.MemberDetails, java.lang.Long, java.lang.Integer)
     */
    @Override
    public boolean isGreaterThanMaxQuantity(MemberDetails memberDetails,Long skuId,Integer totalCount){
        Long memberId = null == memberDetails ? null : memberDetails.getMemberId();

        return totalCount > shoppingcartOneLineMaxQuantityBuilder.build(memberId, skuId);
    }

}
