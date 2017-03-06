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

import java.util.List;

import org.apache.commons.collections4.PredicateUtils;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.feilong.core.util.predicate.BeanPredicate;

import static com.feilong.core.util.CollectionsUtil.find;
import static com.feilong.core.util.predicate.BeanPredicateUtil.equalPredicate;

/**
 * 默认的相同行提取器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
@Component("shoppingCartAddSameLineExtractor")
public class DefaultShoppingCartAddSameLineExtractor implements ShoppingCartAddSameLineExtractor{

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingCartAddSameLineExtractor#extractor(java.util.List, com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartAddDetermineSameLineElements)
     */
    @Override
    public ShoppingCartLineCommand extractor(List<ShoppingCartLineCommand> mainLines,ShoppingcartAddDetermineSameLineElements shoppingcartAddDetermineSameLineElements){
        return find(mainLines, PredicateUtils.<ShoppingCartLineCommand> allPredicate(//
                        equalPredicate("skuId", shoppingcartAddDetermineSameLineElements.getSkuId()),

                        new BeanPredicate<>("shoppingCartLinePackageInfoCommandList", new ShoppingCartLinePackageInfoPredicate(shoppingcartAddDetermineSameLineElements.getPackageInfoElementList()))));

    }
}
