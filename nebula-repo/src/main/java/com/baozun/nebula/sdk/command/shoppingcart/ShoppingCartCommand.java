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
package com.baozun.nebula.sdk.command.shoppingcart;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.sdk.command.UserDetails;

/**
 * The Class ShoppingCartCommand.
 *
 * @see com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand
 * @see com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop
 */
public class ShoppingCartCommand extends BaseModel{

    /** The Constant serialVersionUID. */
    private static final long                serialVersionUID = 7847538393318014437L;

    /** The id. */
    private Long                             id;

    /** 购物车行信息 *. */
    private List<ShoppingCartLineCommand>    shoppingCartLineCommands;

    /** 根据店铺封装shopCart对象. */
    private Map<Long, ShoppingCartCommand>   shoppingCartByShopIdMap;

    /** 基于店铺统计. */
    private List<ShopCartCommandByShop>      summaryShopCartList;

    //*****************************************************************************************************

    /** 应付金额 *. */
    private BigDecimal                       originPayAmount;

    /** 应付运费 *. */
    private BigDecimal                       originShoppingFee;

    /** 实付运费 *. */
    private BigDecimal                       currentShippingFee;

    /** 实付金额 *. */
    private BigDecimal                       currentPayAmount;

    //*****************************************************************************************************

    /** 优惠券code *. */
    private List<String>                     coupons;

    /** 优惠券code *. */
    private List<PromotionCouponCodeCommand> couponCodeCommands;

    /** 当前时间 *. */
    private Date                             currentTime;

    /** 会员信息 *. */
    private UserDetails                      userDetails;

    /** 整单商品数量. */
    private Integer                          orderQuantity;

    /** 判断购物车是否需要更新持久化到行表 当原购物车中的行，由于促销活动的变化， 只导致合并行。不存在拆行。. */
    private Boolean                          needPersistence;

    /** 购物车促销简介信息. */
    private List<PromotionBrief>             cartPromotionBriefList;

    /** 会员下单时使用的积分. */
    private BigDecimal                       points;

    /** 会员可获得的积分. */
    private BigDecimal                       earnPoints;

    /** 来自PTS配置的展示在购物车页的促销活动描述文字. */
    private String                           promotionMsg;

    /** 整单合计优惠金额（优惠券 + 促销活动等）. */
    private BigDecimal                       couponPayAmount;

    //*****************************************************************************************************
    /**
     * 获得 基于店铺统计.
     *
     * @return the 基于店铺统计
     */
    public List<ShopCartCommandByShop> getSummaryShopCartList(){
        return summaryShopCartList;
    }

    /**
     * 设置 基于店铺统计.
     *
     * @param summaryShopCartList
     *            the new 基于店铺统计
     */
    public void setSummaryShopCartList(List<ShopCartCommandByShop> summaryShopCartList){
        this.summaryShopCartList = summaryShopCartList;
    }

    /**
     * 获得 根据店铺封装shopCart对象.
     *
     * @return the 根据店铺封装shopCart对象
     */
    public Map<Long, ShoppingCartCommand> getShoppingCartByShopIdMap(){
        return shoppingCartByShopIdMap;
    }

    /**
     * 设置 根据店铺封装shopCart对象.
     *
     * @param shoppingCartByShopIdMap
     *            the new 根据店铺封装shopCart对象
     */
    public void setShoppingCartByShopIdMap(Map<Long, ShoppingCartCommand> shoppingCartByShopIdMap){
        this.shoppingCartByShopIdMap = shoppingCartByShopIdMap;
    }

    /**
     * 获得 购物车行信息 *.
     *
     * @return the 购物车行信息 *
     */
    public List<ShoppingCartLineCommand> getShoppingCartLineCommands(){
        return shoppingCartLineCommands;
    }

    /**
     * 设置 购物车行信息 *.
     *
     * @param shoppingCartLineCommands
     *            the new 购物车行信息 *
     */
    public void setShoppingCartLineCommands(List<ShoppingCartLineCommand> shoppingCartLineCommands){
        this.shoppingCartLineCommands = shoppingCartLineCommands;
    }

    /**
     * 设置 id.
     *
     * @param id
     *            the id
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 id.
     *
     * @return the id
     */
    public Long getId(){
        return id;
    }

    /**
     * 获得 应付金额 *.
     *
     * @return the 应付金额 *
     */
    public BigDecimal getOriginPayAmount(){
        return originPayAmount;
    }

    /**
     * 设置 应付金额 *.
     *
     * @param originPayAmount
     *            the new 应付金额 *
     */
    public void setOriginPayAmount(BigDecimal originPayAmount){
        this.originPayAmount = originPayAmount;
    }

    /**
     * 获得 应付运费 *.
     *
     * @return the 应付运费 *
     */
    public BigDecimal getOriginShoppingFee(){
        return originShoppingFee;
    }

    /**
     * 设置 应付运费 *.
     *
     * @param originShoppingFee
     *            the new 应付运费 *
     */
    public void setOriginShoppingFee(BigDecimal originShoppingFee){
        this.originShoppingFee = originShoppingFee;
    }

    /**
     * 获得 实付运费 *.
     *
     * @return the 实付运费 *
     */
    public BigDecimal getCurrentShippingFee(){
        return currentShippingFee;
    }

    /**
     * 设置 实付运费 *.
     *
     * @param currentShippingFee
     *            the new 实付运费 *
     */
    public void setCurrentShippingFee(BigDecimal currentShippingFee){
        this.currentShippingFee = currentShippingFee;
    }

    /**
     * 获得 实付金额 *.
     *
     * @return the 实付金额 *
     */
    public BigDecimal getCurrentPayAmount(){
        return currentPayAmount;
    }

    /**
     * 设置 实付金额 *.
     *
     * @param currentPayAmount
     *            the new 实付金额 *
     */
    public void setCurrentPayAmount(BigDecimal currentPayAmount){
        this.currentPayAmount = currentPayAmount;
    }

    /**
     * 获得 购物车促销简介信息.
     *
     * @return the 购物车促销简介信息
     */
    public List<PromotionBrief> getCartPromotionBriefList(){
        return cartPromotionBriefList;
    }

    /**
     * 设置 购物车促销简介信息.
     *
     * @param cartPromotionBriefList
     *            the new 购物车促销简介信息
     */
    public void setCartPromotionBriefList(List<PromotionBrief> cartPromotionBriefList){
        this.cartPromotionBriefList = cartPromotionBriefList;
    }

    /**
     * 获得 优惠券code *.
     *
     * @return the 优惠券code *
     */
    public List<String> getCoupons(){
        return coupons;
    }

    /**
     * 设置 优惠券code *.
     *
     * @param coupons
     *            the new 优惠券code *
     */
    public void setCoupons(List<String> coupons){
        this.coupons = coupons;
    }

    /**
     * 获得 优惠券code *.
     *
     * @return the 优惠券code *
     */
    public List<PromotionCouponCodeCommand> getCouponCodeCommands(){
        return couponCodeCommands;
    }

    /**
     * 设置 优惠券code *.
     *
     * @param couponCodeCommands
     *            the new 优惠券code *
     */
    public void setCouponCodeCommands(List<PromotionCouponCodeCommand> couponCodeCommands){
        this.couponCodeCommands = couponCodeCommands;
    }

    /**
     * 获得 当前时间 *.
     *
     * @return the 当前时间 *
     */
    public Date getCurrentTime(){
        return currentTime;
    }

    /**
     * 设置 当前时间 *.
     *
     * @param currentTime
     *            the new 当前时间 *
     */
    public void setCurrentTime(Date currentTime){
        this.currentTime = currentTime;
    }

    /**
     * 获得 会员信息 *.
     *
     * @return the 会员信息 *
     */
    public UserDetails getUserDetails(){
        return userDetails;
    }

    /**
     * 设置 会员信息 *.
     *
     * @param userDetails
     *            the new 会员信息 *
     */
    public void setUserDetails(UserDetails userDetails){
        this.userDetails = userDetails;
    }

    /**
     * 获得 整单商品数量.
     *
     * @return the 整单商品数量
     */
    public Integer getOrderQuantity(){
        return orderQuantity;
    }

    /**
     * 设置 整单商品数量.
     *
     * @param orderQuantity
     *            the new 整单商品数量
     */
    public void setOrderQuantity(Integer orderQuantity){
        this.orderQuantity = orderQuantity;
    }

    /**
     * 获得 判断购物车是否需要更新持久化到行表 当原购物车中的行，由于促销活动的变化， 只导致合并行。不存在拆行。.
     *
     * @return the 判断购物车是否需要更新持久化到行表 当原购物车中的行，由于促销活动的变化， 只导致合并行。不存在拆行。
     */
    public Boolean getNeedPersistence(){
        return needPersistence;
    }

    /**
     * 设置 判断购物车是否需要更新持久化到行表 当原购物车中的行，由于促销活动的变化， 只导致合并行。不存在拆行。.
     *
     * @param needPersistence
     *            the new 判断购物车是否需要更新持久化到行表 当原购物车中的行，由于促销活动的变化， 只导致合并行。不存在拆行。
     */
    public void setNeedPersistence(Boolean needPersistence){
        this.needPersistence = needPersistence;
    }

    /**
     * 获得 来自PTS配置的展示在购物车页的促销活动描述文字.
     *
     * @return the 来自PTS配置的展示在购物车页的促销活动描述文字
     */
    public String getPromotionMsg(){
        return promotionMsg;
    }

    /**
     * 设置 来自PTS配置的展示在购物车页的促销活动描述文字.
     *
     * @param promotionMsg
     *            the new 来自PTS配置的展示在购物车页的促销活动描述文字
     */
    public void setPromotionMsg(String promotionMsg){
        this.promotionMsg = promotionMsg;
    }

    /**
     * 获得 会员下单时使用的积分.
     *
     * @return the 会员下单时使用的积分
     */
    public BigDecimal getPoints(){
        return points;
    }

    /**
     * 设置 会员下单时使用的积分.
     *
     * @param points
     *            the new 会员下单时使用的积分
     */
    public void setPoints(BigDecimal points){
        this.points = points;
    }

    /**
     * 获得 会员可获得的积分.
     *
     * @return the 会员可获得的积分
     */
    public BigDecimal getEarnPoints(){
        return earnPoints;
    }

    /**
     * 设置 会员可获得的积分.
     *
     * @param earnPoints
     *            the new 会员可获得的积分
     */
    public void setEarnPoints(BigDecimal earnPoints){
        this.earnPoints = earnPoints;
    }

    /**
     * 获得 整单合计优惠金额（优惠券 + 促销活动等）.
     *
     * @return the 整单合计优惠金额（优惠券 + 促销活动等）
     */
    public BigDecimal getCouponPayAmount(){
        return couponPayAmount;
    }

    /**
     * 设置 整单合计优惠金额（优惠券 + 促销活动等）.
     *
     * @param couponPayAmount
     *            the new 整单合计优惠金额（优惠券 + 促销活动等）
     */
    public void setCouponPayAmount(BigDecimal couponPayAmount){
        this.couponPayAmount = couponPayAmount;
    }

}
