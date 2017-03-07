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
package com.baozun.nebula.web.controller.shoppingcart.builder;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.List;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.shoppingcart.extractor.PackageInfoElement;
import com.baozun.nebula.sdk.manager.shoppingcart.extractor.ShoppingcartUpdateDetermineSameLineElements;
import com.baozun.nebula.web.controller.shoppingcart.form.PackageInfoForm;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineUpdateSkuForm;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.lang.reflect.ConstructorUtil;
import com.feilong.core.util.CollectionsUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
@Component("shoppingcartUpdateDetermineSameLineElementsBuilder")
public class DefaultShoppingcartUpdateDetermineSameLineElementsBuilder implements ShoppingcartUpdateDetermineSameLineElementsBuilder{

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingcartUpdateDetermineSameLineElementsBuilder#buildShoppingcartUpdateDetermineSameLineElements(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand,
     * com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineUpdateSkuForm)
     */
    @Override
    public ShoppingcartUpdateDetermineSameLineElements build(ShoppingCartLineCommand currentShoppingCartLineCommand,ShoppingCartLineUpdateSkuForm shoppingCartLineUpdateSkuForm){
        Validate.notNull(currentShoppingCartLineCommand, "currentShoppingCartLineCommand can't be null!");
        Validate.notNull(shoppingCartLineUpdateSkuForm, "shoppingCartLineUpdateSkuForm can't be null!");

        ShoppingcartUpdateDetermineSameLineElements shoppingcartUpdateDetermineSameLineElements = new ShoppingcartUpdateDetermineSameLineElements();
        shoppingcartUpdateDetermineSameLineElements.setCurrentLineId(currentShoppingCartLineCommand.getId());
        shoppingcartUpdateDetermineSameLineElements.setLineGroup(currentShoppingCartLineCommand.getLineGroup());
        shoppingcartUpdateDetermineSameLineElements.setPackageInfoElementList(toPackageInfoElementList(shoppingCartLineUpdateSkuForm.getPackageInfoFormList()));
        shoppingcartUpdateDetermineSameLineElements.setRelatedItemId(currentShoppingCartLineCommand.getRelatedItemId());
        shoppingcartUpdateDetermineSameLineElements.setSkuId(defaultIfNull(currentShoppingCartLineCommand.getSkuId(), currentShoppingCartLineCommand.getSkuId()));
        return shoppingcartUpdateDetermineSameLineElements;
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
