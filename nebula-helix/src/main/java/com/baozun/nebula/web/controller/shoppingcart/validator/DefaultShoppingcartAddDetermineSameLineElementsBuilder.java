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

import java.util.List;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.manager.shoppingcart.extractor.PackageInfoElement;
import com.baozun.nebula.sdk.manager.shoppingcart.extractor.ShoppingcartAddDetermineSameLineElements;
import com.baozun.nebula.web.controller.shoppingcart.form.PackageInfoForm;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.lang.reflect.ConstructorUtil;
import com.feilong.core.util.CollectionsUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
@Component("shoppingcartAddDetermineSameLineElementsBuilder")
public class DefaultShoppingcartAddDetermineSameLineElementsBuilder implements ShoppingcartAddDetermineSameLineElementsBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultShoppingcartAddDetermineSameLineElementsBuilder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartAddDetermineSameLineElementsBuilder#build(com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm)
     */
    @Override
    public ShoppingcartAddDetermineSameLineElements build(ShoppingCartLineAddForm shoppingCartLineAddForm){
        ShoppingcartAddDetermineSameLineElements shoppingcartAddDetermineSameLineElements = new ShoppingcartAddDetermineSameLineElements();
        shoppingcartAddDetermineSameLineElements.setSkuId(shoppingCartLineAddForm.getSkuId());
        shoppingcartAddDetermineSameLineElements.setPackageInfoElementList(toPackageInfoElementList(shoppingCartLineAddForm.getPackageInfoFormList()));
        return shoppingcartAddDetermineSameLineElements;
    }

    /**
     * @param packageInfoFormList
     * @return
     */
    private List<PackageInfoElement> toPackageInfoElementList(List<PackageInfoForm> packageInfoFormList){
        return CollectionsUtil.collect(packageInfoFormList, transformer(PackageInfoElement.class, "type", "featureInfo"));
    }

    private static <I, O> Transformer<I, O> transformer(final Class<O> type,final String...includePropertyNames){
        return new Transformer<I, O>(){

            @Override
            public O transform(I inputBean){
                Validate.notNull(inputBean, "inputBean can't be null!");

                O outBean = ConstructorUtil.newInstance(type);

                PropertyUtil.copyProperties(outBean, inputBean, includePropertyNames);
                return outBean;
            }
        };
    }
}
