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
import java.util.List;
import java.util.Set;

import com.baozun.nebula.command.Command;

/**
 * The Class PromotionSKUDiscAMTBySetting.
 */
public class PromotionSKUDiscAMTBySetting implements Command{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID       = -4788633479644853170L;

    /** The shop id. */
    private Long              shopId;

    /** The promotion id. */
    private Long              promotionId;

    /** The promotion name. */
    private String            promotionName;

    /** Normal常规Step阶Choice选购NormalStep常规加阶梯NormalChoice常规加选购. */
    private String            conditionType;

    /** The setting id. */
    private Long              settingId;

    /** The setting type tag. */
    private String            settingTypeTag;

    /** The setting name. */
    private String            settingName;

    /** The setting expression. */
    private String            settingExpression;

    /** The sku id. */
    private Long              skuId;

    /** The item id. */
    private Long              itemId;

    /** The item name. */
    private String            itemName;

    /** The sales price. */
    private BigDecimal        salesPrice             = BigDecimal.ZERO;

    /** The markdown price. */
    private BigDecimal        markdownPrice          = BigDecimal.ZERO;

    /** The qty. */
    private Integer           qty;

    /** 指那些常规的，可以落到SKU上的优惠，和QTY有关的话，已经计算过QTY的. */
    private BigDecimal        discountAmount         = BigDecimal.ZERO;

    /** 如果没有SKU值的话，就是整单的. */
    private Set<String>       couponCodes;

    /** 整单的shipping. */
    private BigDecimal        shippingDiscountAmount = BigDecimal.ZERO;

    /** 是否是整单优惠设置，. */
    private boolean           baseOrder              = false;

    /** The free shipping mark. */
    private boolean           freeShippingMark       = false;

    /** 分组标签 *. */
    private Set<String>       comboIds;

    /** The category list. */
    private List<Long>        categoryList;

    /** 促销类型. */
    private String            promotionType;

    /** 是否为礼品. */
    private boolean           giftMark               = false;

    /** 礼品显示类型，0不需要用户选择，1需要用户选择. */
    private Integer           giftChoiceType         = 0;

    /** 用户不参与选择时，直接推送礼品的样数；用户参与选择时，直接推送礼品的样数. */
    private Integer           giftCountLimited       = 1;

    /** 0非卖品，不扣库存。多用做赠品。1或null为正常商品. */
    private Integer           type                   = 1;

    /** 1 商品已上架 0 商品未上架. */
    private String            state;

    /**
     * 
     */
    public PromotionSKUDiscAMTBySetting(){
        super();
    }

    /**
     * 获得 促销类型.
     *
     * @return the 促销类型
     */
    public String getPromotionType(){
        return promotionType;
    }

    /**
     * 设置 促销类型.
     *
     * @param promotionType
     *            the new 促销类型
     */
    public void setPromotionType(String promotionType){
        this.promotionType = promotionType;
    }

    /**
     * 获得 category list.
     *
     * @return the category list
     */
    public List<Long> getCategoryList(){
        return categoryList;
    }

    /**
     * 设置 category list.
     *
     * @param categoryList
     *            the category list
     */
    public void setCategoryList(List<Long> categoryList){
        this.categoryList = categoryList;
    }

    /**
     * 获得 分组标签 *.
     *
     * @return the 分组标签 *
     */
    public Set<String> getComboIds(){
        return comboIds;
    }

    /**
     * 设置 分组标签 *.
     *
     * @param comboIds
     *            the new 分组标签 *
     */
    public void setComboIds(Set<String> comboIds){
        this.comboIds = comboIds;
    }

    /**
     * 获得 整单的shipping.
     *
     * @return the 整单的shipping
     */
    public BigDecimal getShippingDiscountAmount(){
        return shippingDiscountAmount;
    }

    /**
     * 设置 整单的shipping.
     *
     * @param disc
     *            the new 整单的shipping
     */
    public void setShippingDiscountAmount(BigDecimal disc){
        this.shippingDiscountAmount = disc;
    }

    /**
     * 获得 setting expression.
     *
     * @return the setting expression
     */
    public String getSettingExpression(){
        return settingExpression;
    }

    /**
     * 设置 setting expression.
     *
     * @param exp
     *            the setting expression
     */
    public void setSettingExpression(String exp){
        this.settingExpression = exp;
    }

    /**
     * 获得 setting name.
     *
     * @return the setting name
     */
    public String getSettingName(){
        return settingName;
    }

    /**
     * 设置 setting name.
     *
     * @param name
     *            the setting name
     */
    public void setSettingName(String name){
        this.settingName = name;
    }

    /**
     * 获得 setting type tag.
     *
     * @return the setting type tag
     */
    public String getSettingTypeTag(){
        return settingTypeTag;
    }

    /**
     * 设置 setting type tag.
     *
     * @param tag
     *            the setting type tag
     */
    public void setSettingTypeTag(String tag){
        this.settingTypeTag = tag;
    }

    /**
     * 获得 sku id.
     *
     * @return the sku id
     */
    public Long getSkuId(){
        return skuId;
    }

    /**
     * 设置 sku id.
     *
     * @param skuId
     *            the sku id
     */
    public void setSkuId(Long skuId){
        this.skuId = skuId;
    }

    /**
     * 获得 如果没有SKU值的话，就是整单的.
     *
     * @return the 如果没有SKU值的话，就是整单的
     */
    public Set<String> getCouponCodes(){
        return couponCodes;
    }

    /**
     * 设置 如果没有SKU值的话，就是整单的.
     *
     * @param couponCodes
     *            the new 如果没有SKU值的话，就是整单的
     */
    public void setCouponCodes(Set<String> couponCodes){
        this.couponCodes = couponCodes;
    }

    /**
     * 获得 shop id.
     *
     * @return the shop id
     */
    public Long getShopId(){
        return shopId;
    }

    /**
     * 设置 shop id.
     *
     * @param shopId
     *            the shop id
     */
    public void setShopId(Long shopId){
        this.shopId = shopId;
    }

    /**
     * 获得 setting id.
     *
     * @return the setting id
     */
    public Long getSettingId(){
        return settingId;
    }

    /**
     * 设置 setting id.
     *
     * @param id
     *            the setting id
     */
    public void setSettingId(Long id){
        this.settingId = id;
    }

    /**
     * 获得 promotion id.
     *
     * @return the promotion id
     */
    public Long getPromotionId(){
        return promotionId;
    }

    /**
     * 设置 promotion id.
     *
     * @param promotionId
     *            the promotion id
     */
    public void setPromotionId(Long promotionId){
        this.promotionId = promotionId;
    }

    /**
     * 获得 item id.
     *
     * @return the item id
     */
    public Long getItemId(){
        return itemId;
    }

    /**
     * 设置 item id.
     *
     * @param itemId
     *            the item id
     */
    public void setItemId(Long itemId){
        this.itemId = itemId;
    }

    /**
     * 获得 item name.
     *
     * @return the item name
     */
    public String getItemName(){
        return itemName;
    }

    /**
     * 设置 item name.
     *
     * @param itemName
     *            the item name
     */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    /**
     * 获得 sales price.
     *
     * @return the sales price
     */
    public BigDecimal getSalesPrice(){
        return salesPrice;
    }

    /**
     * 设置 sales price.
     *
     * @param salesPrice
     *            the sales price
     */
    public void setSalesPrice(BigDecimal salesPrice){
        this.salesPrice = salesPrice;
    }

    /**
     * 获得 qty.
     *
     * @return the qty
     */
    public Integer getQty(){
        return qty;
    }

    /**
     * 设置 qty.
     *
     * @param qty
     *            the qty
     */
    public void setQty(Integer qty){
        this.qty = qty;
    }

    /**
     * 获得 指那些常规的，可以落到SKU上的优惠，和QTY有关的话，已经计算过QTY的.
     *
     * @return the 指那些常规的，可以落到SKU上的优惠，和QTY有关的话，已经计算过QTY的
     */
    public BigDecimal getDiscountAmount(){
        return discountAmount;
    }

    /**
     * 设置 指那些常规的，可以落到SKU上的优惠，和QTY有关的话，已经计算过QTY的.
     *
     * @param discountAmount
     *            the new 指那些常规的，可以落到SKU上的优惠，和QTY有关的话，已经计算过QTY的
     */
    public void setDiscountAmount(BigDecimal discountAmount){
        this.discountAmount = discountAmount;
    }

    /**
     * 获得 promotion name.
     *
     * @return the promotion name
     */
    public String getPromotionName(){
        return promotionName;
    }

    /**
     * 设置 promotion name.
     *
     * @param promotionName
     *            the promotion name
     */
    public void setPromotionName(String promotionName){
        this.promotionName = promotionName;
    }

    /**
     * 获得 是否为礼品.
     *
     * @return the 是否为礼品
     */
    public boolean getGiftMark(){
        return giftMark;
    }

    /**
     * 设置 是否为礼品.
     *
     * @param giftMark
     *            the new 是否为礼品
     */
    public void setGiftMark(boolean giftMark){
        this.giftMark = giftMark;
    }

    /**
     * 获得 是否是整单优惠设置，.
     *
     * @return the 是否是整单优惠设置，
     */
    public boolean getBaseOrder(){
        return baseOrder;
    }

    /**
     * 设置 是否是整单优惠设置，.
     *
     * @param baseOrder
     *            the new 是否是整单优惠设置，
     */
    public void setBaseOrder(boolean baseOrder){
        this.baseOrder = baseOrder;
    }

    /**
     * 获得 free shipping mark.
     *
     * @return the free shipping mark
     */
    public boolean getFreeShippingMark(){
        return freeShippingMark;
    }

    /**
     * 设置 free shipping mark.
     *
     * @param freeShippingMark
     *            the free shipping mark
     */
    public void setFreeShippingMark(boolean freeShippingMark){
        this.freeShippingMark = freeShippingMark;
    }

    /**
     * 获得 normal常规Step阶Choice选购NormalStep常规加阶梯NormalChoice常规加选购.
     *
     * @return the normal常规Step阶Choice选购NormalStep常规加阶梯NormalChoice常规加选购
     */
    public String getConditionType(){
        return conditionType;
    }

    /**
     * 设置 normal常规Step阶Choice选购NormalStep常规加阶梯NormalChoice常规加选购.
     *
     * @param conditionType
     *            the new normal常规Step阶Choice选购NormalStep常规加阶梯NormalChoice常规加选购
     */
    public void setConditionType(String conditionType){
        this.conditionType = conditionType;
    }

    /**
     * 获得 礼品显示类型，0不需要用户选择，1需要用户选择.
     *
     * @return the 礼品显示类型，0不需要用户选择，1需要用户选择
     */
    public Integer getGiftChoiceType(){
        return giftChoiceType;
    }

    /**
     * 设置 礼品显示类型，0不需要用户选择，1需要用户选择.
     *
     * @param giftChoiceType
     *            the new 礼品显示类型，0不需要用户选择，1需要用户选择
     */
    public void setGiftChoiceType(Integer giftChoiceType){
        this.giftChoiceType = giftChoiceType;
    }

    /**
     * 获得 用户不参与选择时，直接推送礼品的样数；用户参与选择时，直接推送礼品的样数.
     *
     * @return the 用户不参与选择时，直接推送礼品的样数；用户参与选择时，直接推送礼品的样数
     */
    public Integer getGiftCountLimited(){
        return giftCountLimited;
    }

    /**
     * 设置 用户不参与选择时，直接推送礼品的样数；用户参与选择时，直接推送礼品的样数.
     *
     * @param giftCountLimited
     *            the new 用户不参与选择时，直接推送礼品的样数；用户参与选择时，直接推送礼品的样数
     */
    public void setGiftCountLimited(Integer giftCountLimited){
        this.giftCountLimited = giftCountLimited;
    }

    /**
     * 获得 0非卖品，不扣库存。多用做赠品。1或null为正常商品.
     *
     * @return the 0非卖品，不扣库存。多用做赠品。1或null为正常商品
     */
    public Integer getType(){
        return type;
    }

    /**
     * 设置 0非卖品，不扣库存。多用做赠品。1或null为正常商品.
     *
     * @param type
     *            the new 0非卖品，不扣库存。多用做赠品。1或null为正常商品
     */
    public void setType(Integer type){
        this.type = type;
    }

    /**
     * 获得 1 商品已上架 0 商品未上架.
     *
     * @return the 1 商品已上架 0 商品未上架
     */
    public String getState(){
        return state;
    }

    /**
     * 设置 1 商品已上架 0 商品未上架.
     *
     * @param state
     *            the new 1 商品已上架 0 商品未上架
     */
    public void setState(String state){
        this.state = state;
    }

    /**
     * 获得 markdown price.
     *
     * @return the markdown price
     */
    public BigDecimal getMarkdownPrice(){
        return markdownPrice;
    }

    /**
     * 设置 markdown price.
     *
     * @param markdownPrice
     *            the markdown price
     */
    public void setMarkdownPrice(BigDecimal markdownPrice){
        this.markdownPrice = markdownPrice;
    }
}
