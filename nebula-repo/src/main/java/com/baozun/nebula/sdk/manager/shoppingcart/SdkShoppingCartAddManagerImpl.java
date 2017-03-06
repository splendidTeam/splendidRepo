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

import com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao;
import com.baozun.nebula.dao.shoppingcart.ShoppingCartLinePackageInfoDao;
import com.baozun.nebula.model.packageinfo.PackageInfo;
import com.baozun.nebula.model.shoppingcart.ShoppingCartLine;
import com.baozun.nebula.model.shoppingcart.ShoppingCartLinePackageInfo;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;
import com.baozun.nebula.sdk.manager.packageinfo.PackageInfoManager;

import static com.feilong.core.Validator.isNotNullOrEmpty;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
@Transactional
@Service("sdkShoppingCartAddManager")
public class SdkShoppingCartAddManagerImpl implements SdkShoppingCartAddManager{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SdkShoppingCartAddManagerImpl.class);

    /**  */
    @Autowired
    private PackageInfoManager packageInfoManager;

    /**  */
    @Autowired
    private ShoppingCartLinePackageInfoDao shoppingCartLinePackageInfoDao;

    /** The sdk shopping cart line dao. */
    @Autowired
    private SdkShoppingCartLineDao sdkShoppingCartLineDao;

    /**  */
    @Autowired
    private SdkShoppingCartUpdateManager sdkShoppingCartUpdateManager;

    /**
     * 添加或者更新购物车.
     * 
     * <p>
     * 如果有，就修改数量<br>
     * 如果没有，就添加一条记录
     * </p>
     *
     * @param memberId
     * @param shoppingCartLineCommand
     */
    @Override
    public void addOrUpdateCartLine(Long memberId,ShoppingCartLineCommand shoppingCartLineCommand){
        Validate.notNull(memberId, "memberId can't be null!");
        Validate.notNull(shoppingCartLineCommand, "shoppingCartLineCommand can't be null!");

        if (isUpdate(shoppingCartLineCommand)){ // 更新
            sdkShoppingCartUpdateManager.updateCartLineQuantityByLineId(memberId, shoppingCartLineCommand.getId(), shoppingCartLineCommand.getQuantity());
        }else{
            addCartLine(memberId, shoppingCartLineCommand);
        }
    }

    /**
     * 判断请求是都是更新请求.
     * 
     * <p>
     * 目前的逻辑是,如果有 lineId 那么判断为更新;<br>
     * 通常 新增的行是没有lineid的(游客的另说,游客登录同步请看{@link SdkShoppingCartSyncManager})<br>
     * 那么有 lineId 的行就是修改了<br>
     * 
     * --上述逻辑是简单粗暴的做法
     * </p>
     *
     * @param shoppingCartLineCommand
     * @return
     */
    protected boolean isUpdate(ShoppingCartLineCommand shoppingCartLineCommand){
        return null != shoppingCartLineCommand.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartAddManager#addCartLine(java.lang.Long, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    public void addCartLine(Long memberId,ShoppingCartLineCommand shoppingCartLineCommand){
        ShoppingCartLine shoppingCartLine = saveShoppingCartLine(memberId, shoppingCartLineCommand);

        // 保存 包装信息
        List<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList = shoppingCartLineCommand.getShoppingCartLinePackageInfoCommandList();
        if (isNotNullOrEmpty(shoppingCartLinePackageInfoCommandList)){
            savePackageInfo(shoppingCartLine.getId(), shoppingCartLinePackageInfoCommandList);
        }
    }

    /**
     * 保存包装信息.
     *
     * @param shoppingCartLineId
     * @param shoppingCartLinePackageInfoCommandList
     */
    protected void savePackageInfo(Long shoppingCartLineId,List<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList){
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
    protected ShoppingCartLinePackageInfo buildShoppingCartLinePackageInfo(Long shoppingCartLineId,Long packageInfoId){
        ShoppingCartLinePackageInfo shoppingCartLinePackageInfo = new ShoppingCartLinePackageInfo();
        shoppingCartLinePackageInfo.setPackageInfoId(packageInfoId);
        shoppingCartLinePackageInfo.setShoppingCartLineId(shoppingCartLineId);
        shoppingCartLinePackageInfo.setCreateTime(new Date());
        return shoppingCartLinePackageInfo;
    }

    /**
     * 单纯的保存.
     * 
     * <p>
     * 通常用于同步购物车,以及添加购物车的逻辑内
     * </p>
     *
     * @param memberId
     * @param shoppingCartLineCommand
     * @return
     */
    private ShoppingCartLine saveShoppingCartLine(Long memberId,ShoppingCartLineCommand shoppingCartLineCommand){
        String extentionCode = shoppingCartLineCommand.getExtentionCode();
        Validate.notBlank(extentionCode, "extentionCode can't be blank!");

        // 保存 ShoppingCartLine
        ShoppingCartLine shoppingCartLine = new ShoppingCartLine();

        shoppingCartLine.setCreateTime(new Date());
        shoppingCartLine.setExtentionCode(extentionCode);
        shoppingCartLine.setGift(shoppingCartLineCommand.isGift());
        //shoppingCartLine.setItemId(itemId);

        shoppingCartLine.setLineGroup(String.valueOf(shoppingCartLineCommand.getLineGroup()));

        shoppingCartLine.setMemberId(memberId);
        shoppingCartLine.setPromotionId(shoppingCartLineCommand.getPromotionId());
        shoppingCartLine.setQuantity(shoppingCartLineCommand.getQuantity());
        //shoppingCartLine.setRelatedItemId(relatedItemId);
        shoppingCartLine.setSettlementState(shoppingCartLineCommand.getSettlementState());
        shoppingCartLine.setShopId(shoppingCartLineCommand.getShopId());
        shoppingCartLine.setSkuId(shoppingCartLineCommand.getSkuId());
        return sdkShoppingCartLineDao.save(shoppingCartLine);

        //        int insertShoppingCartLineResult = sdkShoppingCartLineDao.insertShoppingCartLine(
        //                        extentionCode,
        //                        shoppingCartLineCommand.getSkuId(),
        //                        shoppingCartLineCommand.getQuantity(),
        //                        memberId,
        //                        new Date(),
        //                        shoppingCartLineCommand.getSettlementState(),
        //                        shoppingCartLineCommand.getShopId(),
        //                        shoppingCartLineCommand.isGift(),
        //                        shoppingCartLineCommand.getPromotionId(),
        //                        shoppingCartLineCommand.getLineGroup());
        //        if (1 != insertShoppingCartLineResult){
        //            LOGGER.error("insertShoppingCartLine memberId:[{}],extentionCode:[{}],result is:[{}], not expected 1", memberId, extentionCode, insertShoppingCartLineResult);
        //            throw new NativeUpdateRowCountNotEqualException(1, insertShoppingCartLineResult);
        //        }
    }
}
