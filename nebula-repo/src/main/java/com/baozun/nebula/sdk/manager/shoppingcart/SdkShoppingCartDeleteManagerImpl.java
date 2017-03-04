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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao;
import com.baozun.nebula.dao.shoppingcart.ShoppingCartLinePackageInfoDao;
import com.baozun.nebula.exception.NativeUpdateRowCountNotEqualException;
import com.baozun.nebula.sdk.utils.ManagerValidate;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
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
    private ShoppingCartLinePackageInfoDao shoppingCartLinePackageInfoDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartDeleteManager#deleteShoppingCartLine(java.lang.Long, java.lang.Long)
     */
    @Override
    public void deleteShoppingCartLine(Long memberId,Long shoppingCartLineId) throws NativeUpdateRowCountNotEqualException{
        Integer result = sdkShoppingCartLineDao.deleteByCartLineIdAndMemberId(memberId, shoppingCartLineId);
        ManagerValidate.isExpectedResult(1, result, "delete ShoppingCartLine,memberId:[{}],shoppingCartLineId:[{}]", memberId, shoppingCartLineId);

        //如果结果是0 表示没有对应的关系
        //避免先 查询 带来的并发影响

        //结果可能是多个对应关系 >=1
        shoppingCartLinePackageInfoDao.deleteByShoppingCartLineId(shoppingCartLineId);
    }

}
