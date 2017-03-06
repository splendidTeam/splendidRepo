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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * The Class SdkShoppingCartSyncManagerImpl.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月23日 下午6:24:22
 * @since 5.3.1
 */
@Transactional
@Service("sdkShoppingCartSyncManager")
public class SdkShoppingCartSyncManagerImpl implements SdkShoppingCartSyncManager{

    /** The sdk shopping cart line dao. */
    @Autowired
    private SdkShoppingCartLineDao sdkShoppingCartLineDao;

    /**  */
    @Autowired
    private SdkShoppingCartAddManager sdkShoppingCartAddManager;

    /**  */
    @Autowired
    private SdkShoppingCartUpdateManager sdkShoppingCartUpdateManager;

    /**  */
    @Autowired
    private SdkShoppingCartQueryManager sdkShoppingCartQueryManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkShoppingCartSyncManager#syncShoppingCart(java.lang.Long, java.util.List)
     */
    @Override
    public void syncShoppingCart(Long memberId,List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        Validate.notNull(memberId, "memberId can't be null!");
        Validate.notEmpty(shoppingCartLineCommandList, "shoppingLines can't be null!");

        //由于涉及到包装信息, 且是个list,直接查询不方便对比, 所以先把这哥们儿的所有购物车拿出来,使用java 来对比到底是要 插入还是更新

        //关于 并发问题: 理论上是基于memberId 并发问题可能有(如果同时打开多个浏览器来操作),但是场景很少,
        //出现并发问题的影响: 可能用户购物车会出现2条相同的数据, 影响是个人, 但也可个人删除

        List<ShoppingCartLineCommand> shoppingCartLineCommandListInDB = sdkShoppingCartQueryManager.findShoppingCartLineCommandList(memberId);

        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLineCommandList){
            if (shoppingCartLineCommand.isGift()){ // 不同步赠品数据
                continue;
            }
            syncShoppingCart(memberId, shoppingCartLineCommand, shoppingCartLineCommandListInDB);
        }
    }

    /**
     * 将一条购物车行 同步到DB.
     *
     * @param memberId
     * @param shoppingCartLineCommand
     * @param shoppingCartLineCommandListInDB
     * @since 5.3.2.11-Personalise
     */
    private void syncShoppingCart(Long memberId,ShoppingCartLineCommand shoppingCartLineCommand,List<ShoppingCartLineCommand> shoppingCartLineCommandListInDB){
        Integer quantity = shoppingCartLineCommand.getQuantity();
        Validate.isTrue(quantity >= 0, "quantity must >= 0,but:%s", quantity);

        ShoppingCartLineCommand cartLineInDb = findInDb(memberId, shoppingCartLineCommand, shoppingCartLineCommandListInDB);
        boolean isInDB = null != cartLineInDb;

        if (isInDB){ //如果数据库购物车表中会员有该商品，则将把该商品的数量相加
            sdkShoppingCartUpdateManager.updateCartLineQuantityByLineId(memberId, cartLineInDb.getId(), cartLineInDb.getQuantity() + quantity);
        }else{
            sdkShoppingCartAddManager.addCartLine(memberId, shoppingCartLineCommand);
        }
    }

    /**
     * 在DB中查找.
     *
     * @param memberId
     * @param shoppingCartLineCommand
     * @return
     * @since 5.3.2.11-Personalise
     */
    protected ShoppingCartLineCommand findInDb(Long memberId,ShoppingCartLineCommand shoppingCartLineCommand,List<ShoppingCartLineCommand> shoppingCartLineCommandListInDB){
        String extentionCode = shoppingCartLineCommand.getExtentionCode();
        return sdkShoppingCartLineDao.findShopCartLine(memberId, extentionCode);
    }
}
