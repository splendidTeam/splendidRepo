/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.dao.shoppingcart;

import java.util.Date;
import java.util.List;

import com.baozun.nebula.model.shoppingcart.ShoppingCartLine;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

/**
 * 购物车行信息Dao
 * 
 * @author Tianlong.Zhang
 * 
 */
public interface SdkShoppingCartLineDao extends GenericEntityDao<ShoppingCartLine, Long>{

    /**
     * 根据shoppingCartId查询购物车行的信息
     * 
     * @param shoppingCartId
     * @return
     */
    @NativeQuery(model = ShoppingCartLineCommand.class)
    List<ShoppingCartLineCommand> findShopCartLineByMemberId(@QueryParam("memberId") Long memberId,@QueryParam("settlementState") Integer settlementState);

    /**
     * 获取当前会员的赠品行
     * 
     * @param memberId
     * @param settlementState
     * @return
     */
    @NativeQuery(model = ShoppingCartLineCommand.class)
    List<ShoppingCartLineCommand> findShopCartGiftLineByMemberId(@QueryParam("memberId") Long memberId,@QueryParam("settlementState") Integer settlementState);

    /**
     * 根据shoppingCartLineIds查询购物车行的信息
     * 
     * @param ids
     * @return
     */
    @NativeQuery(model = ShoppingCartLineCommand.class)
    List<ShoppingCartLineCommand> findShopCartLineByIds(@QueryParam("ids") List<Long> ids);

    /**
     * 修改购物车中物品的数量
     * 
     * @param shoppingCartId
     * @param itemId
     * @param quantity
     * @return
     */
    @NativeUpdate
    Integer updateCartLineQuantity(@QueryParam("memberId") Long memberId,@QueryParam("extentionCode") String extentionCode,@QueryParam("quantity") Integer quantity);

    /**
     * 新增物品的数量
     * 
     * @param shoppingCartId
     * @param itemId
     * @param quantity
     * @return
     * @deprecated 我去,名字叫addXXX, xml里面实现却是 update
     */
    @Deprecated
    @NativeUpdate
    Integer addCartLineQuantity(@QueryParam("memberId") Long memberId,@QueryParam("extentionCode") String extentionCode,@QueryParam("quantity") Integer quantity);

    @NativeUpdate
    Integer deleteByMemberId(@QueryParam("memberId") Long memberId);

    @NativeUpdate
    Integer deleteByExtentionCodeAndMemberId(@QueryParam("memberId") Long memberId,@QueryParam("extentionCode") String extentionCode);

    /**
     * 更改购物车行的的选中不选中状态.
     * 
     * @param memberId
     *            会员id
     * @param cartLineIdList
     *            购物车行list
     * @param settleState
     *            选中不选中状态, 1 是选中,0是不选中
     * @return
     * @since 5.3.2.13
     */
    @NativeUpdate
    Integer updateCartLineSettlementState(@QueryParam("memberId") Long memberId,@QueryParam("cartLineIdList") List<Long> cartLineIdList,@QueryParam("settleState") Integer settleState);

    @NativeQuery(model = ShoppingCartLineCommand.class)
    ShoppingCartLineCommand findShopCartLine(@QueryParam("memberId") Long memberId,@QueryParam("extentionCode") String extentionCode);

    @NativeUpdate
    int insertShoppingCartLine(
                    @QueryParam("extentionCode") String extentionCode,
                    @QueryParam("skuId") Long skuId,
                    @QueryParam("quantity") Integer quantity,
                    @QueryParam("memberId") Long memberId,
                    @QueryParam("createTime") Date createTime,
                    @QueryParam("settlementState") Integer settlementState,
                    @QueryParam("shopId") Long shopId,
                    @QueryParam("isGift") Boolean isGift,
                    @QueryParam("promotionId") Long promotionId,
                    @QueryParam("lineGroup") Long lineGroup);

    /**
     * 带lineGroup购物车行插入
     * 
     * @param extentionCode
     * @param skuId
     * @param quantity
     * @param memberId
     * @param createTime
     * @param settlementState
     * @param shopId
     * @param lineGroup
     */
    @NativeUpdate
    void insertShoppingCartLineWithLineGroup(
                    @QueryParam("extentionCode") String extentionCode,
                    @QueryParam("skuId") Long skuId,
                    @QueryParam("quantity") Integer quantity,
                    @QueryParam("memberId") Long memberId,
                    @QueryParam("createTime") Date createTime,
                    @QueryParam("settlementState") Integer settlementState,
                    @QueryParam("shopId") Long shopId,
                    @QueryParam("lineGroup") String lineGroup,
                    @QueryParam("isGift") Boolean isGift,
                    @QueryParam("promotionId") Long promotionId);

    /**
     * 根据memberId查询购物车商品数量
     * 
     * @param memberId
     * @return
     */
    @NativeQuery(clazzes = Integer.class,alias = "count")
    Integer findShopCartLineCountByMemberId(@QueryParam("memberId") Long memberId,@QueryParam("settlementState") Integer settlementState);

    /**
     * @param memberId
     * @param shoppingCartLineId
     * @return
     */
    @NativeUpdate
    Integer deleteByCartLineIdAndMemberId(@QueryParam("memberId") Long memberId,@QueryParam("shoppingCartLineId") Long shoppingCartLineId);

    /**
     * 根据购物车行修改
     * 
     * @param userId
     * @param extentionCode
     * @param curQuantity
     * @return
     */
    @NativeUpdate
    Integer updateCartLineQuantityByLineId(@QueryParam("memberId") Long memberId,@QueryParam("cartLineId") Long cartLineId,@QueryParam("quantity") Integer quantity);

    /**
     * 修改订单行 sku 信息.
     * 
     * @param memberId
     *            哪个会员
     * @param cartLineId
     *            哪个订单行
     * @param newSkuId
     *            新sku id,如果是null,那么不修改sku 信息
     * @param quantity
     *            数量是多少
     * @return 影响的行数
     * @since 5.3.2.3
     */
    @NativeUpdate
    Integer updateCartLineSkuInfo(@QueryParam("memberId") Long memberId,@QueryParam("cartLineId") Long cartLineId,@QueryParam("newSkuId") Long newSkuId,@QueryParam("quantity") Integer quantity);

    /**
     * 根据lineGrop和skuId获取购物车行
     * 
     * @param memberId
     * @param skuId
     * @param lineGroup
     * @return
     */
    @NativeQuery(model = ShoppingCartLineCommand.class)
    List<ShoppingCartLineCommand> findShopCartLineByLineGroupAndSkuId(@QueryParam("memberId") Long memberId,@QueryParam("skuId") Long skuId,@QueryParam("lineGroup") String lineGroup);

    /**
     * 更新促销信息
     * 
     * @param id
     * @param lineGroup
     * @param gift
     * @param promotionId
     */
    @NativeUpdate
    void updateCartLinePromotionInfo(@QueryParam("id") Long id,@QueryParam("lineGroup") Long lineGroup,@QueryParam("gift") boolean gift,@QueryParam("promotionId") Long promotionId);

    /**
     * 通过用户ID和活动ID删除用户已选中的赠品行
     * 
     * @param memberId
     * @param promotionId
     */
    @NativeUpdate
    void deleteGiftLineByMemberIdAndPromotionId(@QueryParam("memberId") Long memberId,@QueryParam("promotionId") Long promotionId);
}
