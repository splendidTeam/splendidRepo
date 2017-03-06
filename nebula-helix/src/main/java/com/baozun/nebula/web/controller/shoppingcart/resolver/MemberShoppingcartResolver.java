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
package com.baozun.nebula.web.controller.shoppingcart.resolver;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartAddManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartDeleteManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartQueryManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartUpdateManager;
import com.baozun.nebula.web.MemberDetails;
import com.feilong.core.util.CollectionsUtil;

import static com.feilong.core.util.CollectionsUtil.find;

/**
 * 会员购物车操作.
 *
 * @author weihui.tang
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月3日 下午1:35:48
 * @since 5.3.1
 */
@Component("memberShoppingcartResolver")
public class MemberShoppingcartResolver extends AbstractShoppingcartResolver{

    @Autowired
    private SdkShoppingCartDeleteManager sdkShoppingCartDeleteManager;

    @Autowired
    private SdkShoppingCartAddManager sdkShoppingCartAddManager;

    /** The sdk shopping cart update manager. */
    @Autowired
    private SdkShoppingCartUpdateManager sdkShoppingCartUpdateManager;

    @Autowired
    private SdkShoppingCartQueryManager sdkShoppingCartQueryManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#getShoppingCartLineCommandList(com.baozun.nebula.web.
     * MemberDetails, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public List<ShoppingCartLineCommand> getShoppingCartLineCommandList(MemberDetails memberDetails,HttpServletRequest request){
        return sdkShoppingCartQueryManager.findShoppingCartLineCommandList(memberDetails.getGroupId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.AbstractShoppingcartResolver#doAddShoppingCart(com.baozun.nebula.web.
     * MemberDetails, java.util.List, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ShoppingcartResult doAddShoppingCart(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLineCommandList,ShoppingCartLineCommand currentLine,HttpServletRequest request,HttpServletResponse response){
        currentLine.setMemberId(memberDetails.getGroupId());
        sdkShoppingCartAddManager.addCartLine(memberDetails.getGroupId(), currentLine);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.AbstractShoppingcartResolver#doUpdateShoppingCart(com.baozun.nebula.web.MemberDetails, java.util.List, java.lang.Long, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ShoppingcartResult doUpdateShoppingCart(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLineCommandList,Long shoppingcartLineId,HttpServletRequest request,HttpServletResponse response){
        List<ShoppingCartLineCommand> shoppingCartLineCommandListDB = getShoppingCartLineCommandList(memberDetails, request);
        ShoppingCartLineCommand shoppingCartLineCommandInDB = find(shoppingCartLineCommandListDB, "id", shoppingcartLineId);

        //----------------------------------------------------------------------------------------------------------

        //修改行在db 里面的sku id
        Long skuIdInDB = shoppingCartLineCommandInDB.getSkuId();

        //当前 shoppingcartLineId 在内存list中的对象
        ShoppingCartLineCommand currentShoppingCartLineCommand = find(shoppingCartLineCommandList, "id", shoppingcartLineId);

        //如果存在,那么表示不需要进行合并的动作
        if (null != currentShoppingCartLineCommand){
            //如果不需要合并,那么仅修改当前行数据 
            //相等表示不需要修改sku信息,不相等表示需要修改sku信息 
            Long newSkuId = skuIdInDB == currentShoppingCartLineCommand.getSkuId() ? null : currentShoppingCartLineCommand.getSkuId();
            sdkShoppingCartUpdateManager.updateCartLineSkuInfo(memberDetails.getGroupId(), shoppingcartLineId, newSkuId, currentShoppingCartLineCommand.getQuantity());
        }
        //如果不存在,那么表示已经被合并了
        else{
            ShoppingCartLineCommand combinedShoppingCartLineCommand = findCombinedShoppingCartLineCommand(shoppingCartLineCommandListDB, shoppingCartLineCommandList);
            sdkShoppingCartUpdateManager.updateCartLineQuantityAndDeleteOtherLineId(memberDetails.getGroupId(), combinedShoppingCartLineCommand.getId(), combinedShoppingCartLineCommand.getQuantity(), shoppingcartLineId);
        }
        return null;
    }

    /**
     * 找到被合并的行.
     *
     * @param shoppingCartLineCommandListDB
     *            the shopping cart line command list DB
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @return the shopping cart line command
     * @since 5.3.2.4
     */
    private ShoppingCartLineCommand findCombinedShoppingCartLineCommand(List<ShoppingCartLineCommand> shoppingCartLineCommandListDB,List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        //循环内存list,如果发现数量和db不相同,那么返回这条数据
        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLineCommandList){
            ShoppingCartLineCommand db = find(shoppingCartLineCommandListDB, "id", shoppingCartLineCommand.getId());
            if (db.getQuantity() != shoppingCartLineCommand.getQuantity()){
                return shoppingCartLineCommand;
            }
        }
        throw new BusinessException("not find Combined ShoppingCartLineCommand");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.resolver.AbstractShoppingcartResolver#doDeleteShoppingCartLine(com.baozun.nebula.web.
     * MemberDetails, java.util.List, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ShoppingcartResult doDeleteShoppingCartLine(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLineCommandList,ShoppingCartLineCommand currentLine,HttpServletRequest request,HttpServletResponse response){
        sdkShoppingCartDeleteManager.deleteShoppingCartLine(memberDetails.getGroupId(), currentLine.getId());
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.resolver.AbstractShoppingcartResolver#doToggleShoppingCartLineCheckStatus(com.baozun.
     * nebula.web.MemberDetails, java.util.List, java.util.List, boolean, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ShoppingcartResult doToggleShoppingCartLineCheckStatus(
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    List<ShoppingCartLineCommand> needChangeCheckedCommandList,
                    boolean checkStatus,
                    HttpServletRequest request,
                    HttpServletResponse response){
        List<Long> cartLineIdList = CollectionsUtil.getPropertyValueList(needChangeCheckedCommandList, "id");
        sdkShoppingCartUpdateManager.updateCartLineSettlementState(memberDetails.getGroupId(), cartLineIdList, checkStatus);

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.AbstractShoppingcartResolver#doUpdateShoppingCart(com.baozun.nebula.web.MemberDetails, java.util.List, java.util.Map, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ShoppingcartResult doBatchUpdateShoppingCart(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLineCommandList,Map<Long, Integer> shoppingcartLineIdAndCountMap,HttpServletRequest request,HttpServletResponse response){
        sdkShoppingCartUpdateManager.updateCartLineQuantity(memberDetails.getGroupId(), shoppingcartLineIdAndCountMap);
        return null;
    }
}
