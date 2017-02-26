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

import org.apache.commons.collections4.Predicate;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;
import com.baozun.nebula.web.controller.shoppingcart.form.PackageInfoForm;
import com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.predicate.BeanPredicateUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.util.CollectionsUtil.find;

/**
 * 默认的相同行提取器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
@Component("shoppingCartSameLineExtractor")
public class DefaultShoppingCartSameLineExtractor implements ShoppingCartSameLineExtractor{

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.validator.SameLineExtractor#getSameLine(java.util.List, com.baozun.nebula.web.controller.shoppingcart.form.ShoppingCartLineAddForm)
     */
    @Override
    public ShoppingCartLineCommand getSameLine(List<ShoppingCartLineCommand> mainLines,final ShoppingCartLineAddForm shoppingCartLineAddForm){
        Predicate<ShoppingCartLineCommand> sameLinePredicate = buildSameLinePredicate(shoppingCartLineAddForm);
        return find(mainLines, sameLinePredicate);
    }

    /**
     * 构造相同行Predicate.
     * 
     * @param shoppingCartLineAddForm
     * @return
     */
    protected Predicate<ShoppingCartLineCommand> buildSameLinePredicate(final ShoppingCartLineAddForm shoppingCartLineAddForm){
        return new Predicate<ShoppingCartLineCommand>(){

            @Override
            public boolean evaluate(ShoppingCartLineCommand shoppingCartLineCommand){
                Long skuId = shoppingCartLineCommand.getSkuId();
                //-------------------------------------------------------------------------------------------------------------
                //如果 skuid 不相等, 那么直接是false
                if (!skuId.equals(shoppingCartLineAddForm.getSkuId())){
                    return false;
                }

                //-------------------------------------------------------------------------------------------------------------
                List<PackageInfoForm> packageInfoFormList = shoppingCartLineAddForm.getPackageInfoFormList();
                List<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList = shoppingCartLineCommand.getShoppingCartLinePackageInfoCommandList();

                return comparePackageInfo(packageInfoFormList, shoppingCartLinePackageInfoCommandList);
            }

            /**
             * 比较包装信息.
             * 
             * @param packageInfoFormList
             * @param shoppingCartLinePackageInfoCommandList
             * @return
             */
            protected boolean comparePackageInfo(List<PackageInfoForm> packageInfoFormList,List<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList){
                //如果都没有包装信息,那么返回true 
                if (isNullOrEmpty(packageInfoFormList) && isNullOrEmpty(shoppingCartLinePackageInfoCommandList)){
                    return true;
                }

                //-------------------------------------------------------------------------------------------------------------
                //如果都有包装信息,那么计算逻辑
                if (isNotNullOrEmpty(packageInfoFormList) && isNotNullOrEmpty(shoppingCartLinePackageInfoCommandList)){
                    //size 不一致,那么可能有的多包装, 判定不是同一行
                    if (packageInfoFormList.size() != shoppingCartLinePackageInfoCommandList.size()){
                        return false;
                    }
                    //-------------------------------------------------------------------------------------------------------------
                    //循环判定
                    for (PackageInfoForm packageInfoForm : packageInfoFormList){
                        Predicate<ShoppingCartLinePackageInfoCommand> equalPredicate = BeanPredicateUtil.equalPredicate(toMap(//
                                        "type",
                                        packageInfoForm.getType(),
                                        "featureInfo",
                                        packageInfoForm.getFeatureInfo()));
                        final ShoppingCartLinePackageInfoCommand shoppingCartLinePackageInfoCommand = CollectionsUtil.find(shoppingCartLinePackageInfoCommandList, equalPredicate);

                        //如果有一个找不到,那么返回false
                        if (null == shoppingCartLinePackageInfoCommand){
                            return false;
                        }
                    }

                    //如果都找到,都匹配,那么返回true
                    return true;
                }

                return false;
            }
        };
    }
}
