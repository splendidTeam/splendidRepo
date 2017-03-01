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

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.packageinfo.PackageInfo;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
public interface PackageInfoManager extends BaseManager{

    /**
     * 保存包装信息.
     * 
     * @param shoppingCartLinePackageInfoCommand
     * @return 如果ShoppingCartLinePackageInfoCommand 已经有 packageid 那么会抛出异常,不可以重复保存,需要自行判断是否需要调用这个方法
     */
    PackageInfo savePackageInfo(ShoppingCartLinePackageInfoCommand shoppingCartLinePackageInfoCommand);
}
