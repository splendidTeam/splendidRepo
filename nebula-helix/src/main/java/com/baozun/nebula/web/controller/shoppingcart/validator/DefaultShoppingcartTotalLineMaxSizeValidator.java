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

import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.Constants;

/**
 * The Class DefaultShoppingcartTotalLineMaxSizeValidator.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1.9
 */
public class DefaultShoppingcartTotalLineMaxSizeValidator implements ShoppingcartTotalLineMaxSizeValidator{

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartTotalLineMaxSizeValidator#isGreaterThanMaxSize(com.baozun.nebula.web.MemberDetails, java.lang.Integer)
     */
    @Override
    public boolean isGreaterThanMaxSize(MemberDetails memberDetails,Integer totalLineSize){
        return totalLineSize > Constants.SHOPPING_CART_SKU_MAX_COUNT;
    }
}
