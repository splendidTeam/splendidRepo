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

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao;
import com.baozun.nebula.exception.NativeUpdateRowCountNotEqualException;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.utils.ManagerValidate;
import com.feilong.core.bean.ConvertUtil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.util.CollectionsUtil.getPropertyValueList;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Transactional
@Service("sdkShoppingCartDeleteManager")
public class SdkShoppingCartDeleteManagerImpl implements SdkShoppingCartDeleteManager{

    /**  */
    private static final Logger LOGGER = LoggerFactory.getLogger(SdkShoppingCartDeleteManagerImpl.class);

    /**  */
    @Autowired
    private SdkShoppingCartLineDao sdkShoppingCartLineDao;

    @Autowired
    private SdkShoppingCartQueryManager sdkShoppingCartQueryManager;

    /**  */
    @Autowired
    private ShoppingCartLinePackageInfoManager shoppingCartLinePackageInfoManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartDeleteManager#clearShoppingCart(java.lang.Long)
     */
    @Override
    public void clearShoppingCart(Long memberId){
        Validate.notNull(memberId, "memberId can't be null!");

        List<ShoppingCartLineCommand> lineCommandList = sdkShoppingCartQueryManager.findShoppingCartLineCommandList(memberId);
        Validate.notEmpty(lineCommandList, "lineCommandList can't be null/empty!");

        List<Long> lineIds = getPropertyValueList(lineCommandList, "id");

        //-----------------------------------------------------------
        sdkShoppingCartLineDao.deleteByMemberId(memberId);

        shoppingCartLinePackageInfoManager.deleteByShoppingCartLineIds(toArray(lineIds, Long.class));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartDeleteManager#deleteShoppingCartLine(java.lang.Long, java.lang.Long)
     */
    @Override
    public void deleteShoppingCartLine(Long memberId,Long shoppingCartLineId) throws NativeUpdateRowCountNotEqualException{
        deleteShoppingCartLine(memberId, toArray(shoppingCartLineId));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartDeleteManager#deleteShoppingCartLine(java.lang.Long, java.lang.Long[])
     */
    @Override
    public void deleteShoppingCartLine(Long memberId,Long[] shoppingCartLineIds) throws NativeUpdateRowCountNotEqualException{
        Validate.notNull(memberId, "memberId can't be null!");

        Validate.notEmpty(shoppingCartLineIds, "shoppingCartLineIds can't be null/empty!");
        Validate.noNullElements(shoppingCartLineIds, "shoppingCartLineIds can't has null element!");

        //----------------------
        Integer result = sdkShoppingCartLineDao.deleteLines(memberId, shoppingCartLineIds);
        ManagerValidate.isExpectedResult(shoppingCartLineIds.length, result, "delete ShoppingCartLine,memberId:[{}],shoppingCartLineIds:[{}]", memberId, ConvertUtil.toString(shoppingCartLineIds, null));

        //----------------------
        shoppingCartLinePackageInfoManager.deleteByShoppingCartLineIds(shoppingCartLineIds);
    }

}
