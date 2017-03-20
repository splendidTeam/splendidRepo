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
package com.baozun.nebula.sdk.manager.packageinfo;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.packageinfo.PackageInfoDao;
import com.baozun.nebula.model.packageinfo.PackageInfo;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Transactional
@Service("packageInfoManager")
public class PackageInfoManagerImpl implements PackageInfoManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageInfoManagerImpl.class);

    /**  */
    @Autowired
    private PackageInfoDao packageInfoDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.packageinfo.PackageInfoManager#savePackageInfo(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand)
     */
    @Override
    public PackageInfo savePackageInfo(ShoppingCartLinePackageInfoCommand shoppingCartLinePackageInfoCommand){
        return packageInfoDao.save(toPackageInfo(shoppingCartLinePackageInfoCommand));
    }

    /**
     * @param shoppingCartLinePackageInfoCommand
     * @return
     */
    protected PackageInfo toPackageInfo(ShoppingCartLinePackageInfoCommand shoppingCartLinePackageInfoCommand){
        PackageInfo packageInfo = new PackageInfo();

        packageInfo.setExtendInfo(shoppingCartLinePackageInfoCommand.getExtendInfo());
        packageInfo.setFeatureInfo(shoppingCartLinePackageInfoCommand.getFeatureInfo());
        packageInfo.setTotal(shoppingCartLinePackageInfoCommand.getTotal());
        packageInfo.setType(shoppingCartLinePackageInfoCommand.getType());
        packageInfo.setCreateTime(new Date());
        return packageInfo;
    }
}
