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
package com.baozun.nebula.sdk.manager.shoppingcart.behaviour.createline;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.salesorder.SdkOrderLinePackageInfoDao;
import com.baozun.nebula.model.packageinfo.PackageInfo;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.model.salesorder.OrderLinePackageInfo;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;
import com.baozun.nebula.sdk.manager.packageinfo.PackageInfoManager;
import com.feilong.tools.jsonlib.JsonUtil;

import static com.feilong.core.Validator.isNullOrEmpty;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Transactional
@Service("orderLinePackInfoCreateManager")
public class DefaultOrderLinePackInfoCreateManager implements OrderLinePackInfoCreateManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderLinePackInfoCreateManager.class);

    @Autowired
    private PackageInfoManager packageInfoManager;

    @Autowired
    private SdkOrderLinePackageInfoDao sdkOrderLinePackageInfoDao;

    //---------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.behaviour.createline.OrderLinePackInfoCreateManager#saveOrderLinePackInfo(com.baozun.nebula.model.salesorder.OrderLine, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    public void saveOrderLinePackInfo(OrderLine orderLine,ShoppingCartLineCommand shoppingCartLineCommand){
        Validate.notNull(orderLine, "orderLine can't be null!");
        Validate.notNull(shoppingCartLineCommand, "shoppingCartLineCommand can't be null!");

        //---------------------------------------------------------------------
        List<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList = shoppingCartLineCommand.getShoppingCartLinePackageInfoCommandList();

        //没有包装信息
        if (isNullOrEmpty(shoppingCartLinePackageInfoCommandList)){
            LOGGER.debug("orderLine:[{}] don't has shoppingCartLinePackageInfoCommandList,no need to save OrderLine PackInfo", orderLine.getId());
            return;
        }

        //--------------------------------------------------------------------------------------------------
        Long orderLineId = orderLine.getId();

        for (ShoppingCartLinePackageInfoCommand shoppingCartLinePackageInfoCommand : shoppingCartLinePackageInfoCommandList){
            Long packageInfoId = determinePackageInfoId(shoppingCartLinePackageInfoCommand);

            OrderLinePackageInfo orderLinePackageInfo = buildOrderLinePackageInfo(orderLineId, packageInfoId);
            sdkOrderLinePackageInfoDao.save(orderLinePackageInfo);

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("save OrderLinePackageInfo:[{}]", JsonUtil.format(orderLinePackageInfo));
            }
        }
        LOGGER.debug("save OrderLine:[{}],packInfo over~~", orderLineId);
    }

    /**
     * 判断packageInfoId.
     * 
     * @param shoppingCartLinePackageInfoCommand
     * @return 如果 {@link ShoppingCartLinePackageInfoCommand#getPackageInfoId()} 不是null,表示是购物车模式的, 那么直接拿这个数据保存;<br>
     *         如果是null 标识是立即购买模式的, 那么会插入一条数据,拿返回的结果id
     * @since 5.3.2.27
     */
    private Long determinePackageInfoId(ShoppingCartLinePackageInfoCommand shoppingCartLinePackageInfoCommand){
        Long packageInfoId = shoppingCartLinePackageInfoCommand.getPackageInfoId();
        if (null != packageInfoId){
            return packageInfoId;
        }

        //添加 package  可能是立即购买
        LOGGER.debug("shoppingCartLinePackageInfoCommand no packageInfoId,may be isImmediatelyBuy,will save savePackageInfo");

        PackageInfo packageInfo = packageInfoManager.savePackageInfo(shoppingCartLinePackageInfoCommand);
        return packageInfo.getId();
    }

    //---------------------------------------------------------------------

    /**
     * @param orderLineId
     * @param packageInfoId
     * @return
     */
    protected OrderLinePackageInfo buildOrderLinePackageInfo(Long orderLineId,Long packageInfoId){
        OrderLinePackageInfo orderLinePackageInfo = new OrderLinePackageInfo();
        orderLinePackageInfo.setCreateTime(new Date());
        orderLinePackageInfo.setOrderLineId(orderLineId);
        orderLinePackageInfo.setPackageInfoId(packageInfoId);
        return orderLinePackageInfo;
    }
}
