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
package com.baozun.nebula.sdk.manager.shoppingcart;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao;
import com.baozun.nebula.dao.shoppingcart.ShoppingCartLinePackageInfoDao;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;
import com.feilong.core.util.CollectionsUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
@Transactional
@Service("sdkShoppingCartQueryManager")
public class SdkShoppingCartQueryManagerImpl implements SdkShoppingCartQueryManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(SdkShoppingCartQueryManagerImpl.class);

    /** The sdk shopping cart line dao. */
    @Autowired
    private SdkShoppingCartLineDao sdkShoppingCartLineDao;

    @Autowired
    private ShoppingCartLinePackageInfoDao shoppingCartLinePackageInfoDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartQueryManager#findShoppingCartLineCommandList(java.lang.Long)
     */
    @Override
    public List<ShoppingCartLineCommand> findShoppingCartLineCommandList(Long memberId){
        //该sql 其实返回的是主商品行 不带赠品的
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = sdkShoppingCartLineDao.findShopCartLineByMemberId(memberId, null);
        return handleShoppingCartLineCommandList(shoppingCartLineCommandList);
    }

    /**
     * 加工.
     * 
     * <p>
     * 此处方式对原来的代码侵入较小
     * </p>
     * 
     * @param shoppingCartLineCommandList
     * @param shoppingCartLinePackageInfoCommandList
     * @return
     * @since 5.3.2.11-Personalise
     */
    private List<ShoppingCartLineCommand> handleShoppingCartLineCommandList(List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        if (isNullOrEmpty(shoppingCartLineCommandList)){
            return Collections.emptyList();
        }
        
        List<Long> shoppingCartLineIdList = CollectionsUtil.getPropertyValueList(shoppingCartLineCommandList, "id");
        List<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList = shoppingCartLinePackageInfoDao.findShoppingCartLinePackageInfoCommandList(shoppingCartLineIdList);
        if (isNullOrEmpty(shoppingCartLinePackageInfoCommandList)){
            return shoppingCartLineCommandList;
        }
        
        //按照行分个组
        Map<Long, List<ShoppingCartLinePackageInfoCommand>> shoppingCartLineIdAndShoppingCartLinePackageInfoCommandListMap = CollectionsUtil.group(shoppingCartLinePackageInfoCommandList, "shoppingCartLineId");
        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLineCommandList){
            List<ShoppingCartLinePackageInfoCommand> list = shoppingCartLineIdAndShoppingCartLinePackageInfoCommandListMap.get(shoppingCartLineCommand.getId());

            if (isNotNullOrEmpty(list)){
                shoppingCartLineCommand.setShoppingCartLinePackageInfoCommandList(list);
            }
        }
        return null;
    }
}
