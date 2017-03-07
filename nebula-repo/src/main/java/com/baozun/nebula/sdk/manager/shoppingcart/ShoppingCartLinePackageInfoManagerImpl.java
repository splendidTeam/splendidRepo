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

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.shoppingcart.ShoppingCartLinePackageInfoDao;
import com.baozun.nebula.model.packageinfo.PackageInfo;
import com.baozun.nebula.model.shoppingcart.ShoppingCartLinePackageInfo;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;
import com.baozun.nebula.sdk.manager.packageinfo.PackageInfoManager;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
@Transactional
@Service("shoppingCartLinePackageInfoManager")
public class ShoppingCartLinePackageInfoManagerImpl implements ShoppingCartLinePackageInfoManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartLinePackageInfoManagerImpl.class);

    /**  */
    @Autowired
    private PackageInfoManager packageInfoManager;

    /**  */
    @Autowired
    private ShoppingCartLinePackageInfoDao shoppingCartLinePackageInfoDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.ShoppingCartLinePackageInfoManager#savePackageInfo(java.lang.Long, java.util.List)
     */
    @Override
    public void savePackageInfo(Long shoppingCartLineId,List<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList){
        for (ShoppingCartLinePackageInfoCommand shoppingCartLinePackageInfoCommand : shoppingCartLinePackageInfoCommandList){
            Long packageInfoId = shoppingCartLinePackageInfoCommand.getPackageInfoId();
            if (null == packageInfoId){//如果没有packageInfoId 那么就创建一个,如果有那么就使用传入的,这样支持固定的包装类型
                PackageInfo packageInfoDb = packageInfoManager.savePackageInfo(shoppingCartLinePackageInfoCommand);
                packageInfoId = packageInfoDb.getId();//新的id
            }

            //----------------------------------------------------------------------------
            ShoppingCartLinePackageInfo shoppingCartLinePackageInfo = buildShoppingCartLinePackageInfo(shoppingCartLineId, packageInfoId);
            shoppingCartLinePackageInfoDao.save(shoppingCartLinePackageInfo);
        }

    }

    /**
     * @param shoppingCartLineId
     * @param packageInfoId
     * @return
     */
    private static ShoppingCartLinePackageInfo buildShoppingCartLinePackageInfo(Long shoppingCartLineId,Long packageInfoId){
        ShoppingCartLinePackageInfo shoppingCartLinePackageInfo = new ShoppingCartLinePackageInfo();
        shoppingCartLinePackageInfo.setPackageInfoId(packageInfoId);
        shoppingCartLinePackageInfo.setShoppingCartLineId(shoppingCartLineId);
        shoppingCartLinePackageInfo.setCreateTime(new Date());
        return shoppingCartLinePackageInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.ShoppingCartLinePackageInfoManager#deleteByShoppingCartLineId(java.lang.Long)
     */
    @Override
    public void deleteByShoppingCartLineId(Long shoppingCartLineId){
        Validate.notNull(shoppingCartLineId, "shoppingCartLineId can't be null!");

        //如果结果是0 表示没有对应的关系
        //避免先 查询 带来的并发影响

        //结果可能是多个对应关系 >=1
        shoppingCartLinePackageInfoDao.deleteByShoppingCartLineId(shoppingCartLineId);
    }
}
