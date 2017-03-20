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

import org.apache.commons.collections4.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;
import com.feilong.core.util.predicate.BeanPredicateUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.util.CollectionsUtil.find;

/**
 * 购物车行的包装信息条件.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
public class ShoppingCartLinePackageInfoPredicate implements Predicate<List<ShoppingCartLinePackageInfoCommand>>{

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartLinePackageInfoPredicate.class);

    private List<PackageInfoElement> packageInfoElementList;

    /**
     * @param packageInfoElementList
     */
    public ShoppingCartLinePackageInfoPredicate(List<PackageInfoElement> packageInfoElementList){
        super();
        this.packageInfoElementList = packageInfoElementList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections4.Predicate#evaluate(java.lang.Object)
     */
    @Override
    public boolean evaluate(List<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList){
        //如果都没有包装信息,那么返回true 
        if (isNullOrEmpty(packageInfoElementList) && isNullOrEmpty(shoppingCartLinePackageInfoCommandList)){
            return true;
        }

        //-------------------------------------------------------------------------------------------------------------
        //如果都有包装信息,那么计算逻辑
        if (!(isNotNullOrEmpty(packageInfoElementList) && isNotNullOrEmpty(shoppingCartLinePackageInfoCommandList))){
            return false;
        }

        //size 不一致,那么可能有的多包装, 判定不是同一行
        if (packageInfoElementList.size() != shoppingCartLinePackageInfoCommandList.size()){
            return false;
        }

        //-------------------------------------------------------------------------------------------------------------
        //循环判定
        for (PackageInfoElement packageInfoElement : packageInfoElementList){
            Predicate<ShoppingCartLinePackageInfoCommand> equalPredicate = BeanPredicateUtil.equalPredicate(toMap("type", packageInfoElement.getType(), "featureInfo", packageInfoElement.getFeatureInfo()));
            ShoppingCartLinePackageInfoCommand shoppingCartLinePackageInfoCommand = find(shoppingCartLinePackageInfoCommandList, equalPredicate);

            //如果有一个找不到,那么返回false
            if (null == shoppingCartLinePackageInfoCommand){
                return false;
            }
        }

        //如果都找到,都匹配,那么返回true
        return true;
    }
}
