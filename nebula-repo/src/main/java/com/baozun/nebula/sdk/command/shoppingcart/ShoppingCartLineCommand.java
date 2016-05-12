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
import java.util.Set;

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionCouponCodeCommand;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.sdk.command.SkuProperty;

/**
 * The Class ShoppingCartLineCommand.
 */
public class ShoppingCartLineCommand extends BaseModel{

    /** The Constant serialVersionUID. */
    private static final long          serialVersionUID = -5071101827860973497L;

    /** The extention code. */
    private String                     extentionCode;

    /** 行分组 *. */
    private Long                       lineGroup;

    /** 该订单行是否属于预售订单 *. */
    private boolean                    isPresale;

    /** 是套餐行 t是标题行，f不是标题行。标题行是促销活动提示，非购物车行 *. */
    private boolean                    isCaptionLine;

    /** 标题 *. */
    private String                     lineCaption;

    /** 是套餐行 t是套餐行，f不是套餐行 *. */
    private boolean                    isSuitLine;

    /** 商品款的id *. */
    private Long                       skuId;

    /** 商品id *. */
    private Long                       itemId;

    /** 购物数量 *. */
    private Integer                    quantity;

    /** 商品名称 *. */
    private String                     itemName;

    /** 商品图片 *. */
    private String                     itemPic;

    /** 销售属性 *. */
    private String                     saleProperty;

    /** 库房id *. */
    private Long                       wareHoseId;

    /** 库房名称 *. */
    private String                     wareHoseName;

    /** 是否可见 1可见 /0不可见 *. */
    private Integer                    visibleMark;

    /** 是否限购 1限购 0不限购 *. */
    private Integer                    limitMark;

    /** 限购数量 *. */
    private Integer                    limit;

    /** 是否有库存 1有库存 0无库存 *. */
    private Integer                    stockMark;

    /** 库存数量 *. */
    private Integer                    stock;

    /** ***** 扩展engine所需要的字段begin ******. */

    private String                     productCode;                             // 商品code

    /** The lable ids. */
    private List<Long>                 lableIds;

    /** The brand id. */
    private String                     brandId;

    /** The category list. */
    private List<Long>                 categoryList;

    /** The store id. */
    private long                       storeId;

    /** The indstry id. */
    private Long                       indstryId;

    /** The state. */
    private String                     state;

    /** 商品单价. */
    private BigDecimal                 salePrice;

    /** 行促销(此商口多个数量的促销总和) 折扣-包含整单优惠分摊. */
    private BigDecimal                 discount         = BigDecimal.ZERO;

    /** 折扣单价-不包含整单优惠分摊. */
    private BigDecimal                 discountPrice    = BigDecimal.ZERO;;

    /** 套餐、行赠品类型，只有一个PromotionId 其他类型可能有多个活动 *. */
    private List<PromotionCommand>     promotionList;

    /** 是否赠品. */
    private boolean                    isGift;

    /** 1是行赠品,0是整单赠品. */
    private int                        giftType;

    /** 会员id. */
    private Long                       memberId;

    /** 加入时间. */
    private Date                       createTime;

    /** PK *. */
    private Long                       id;

    /** 结算状态 0未选中结算 1选中结算. */
    private Integer                    settlementState;

    /** 0代表非买品 1代表主卖品 与itemInfo中的type一致. */
    private Integer                    type;

    /** 购物车中商品经有效性检查引擎检查之后是否有效的字段 *. */
    private boolean                    isValid;

    /**
     * 有效性检查类型：1.代表下架 2.代表没有库存 这个字段是结合isValid=false来使用的
     */
    private Integer                    validType;

    /** 店铺id *. */
    private Long                       shopId;

    /** 店铺名称 *. */
    private String                     shopName;

    /** 销售属性 *. */
    private List<SkuProperty>          skuPropertys;

    /** 分组标签 *. */
    private Set<String>                comboIds;

    /** 吊牌价 *. */
    private BigDecimal                 listPrice;

    /** 购物车行 金额小计 *. */
    private BigDecimal                 subTotalAmt      = BigDecimal.ZERO;

    /** 行上的Coupon *. */
    private PromotionCouponCodeCommand couponCodeOnLine;

    /** 活动id. */
    private Long                       promotionId;

    /** 优惠记录id. */
    private Long                       settingId;

    /** 礼品显示类型，0不需要用户选择，1需要用户选择. */
    private Integer                    giftChoiceType;

    /** 用户不参与选择时，直接推送礼品的样数；用户参与选择时，直接推送礼品的样数. */
    private Integer                    giftCountLimited;

    /** 商城自定义参数，nebula不控制. */
    private Map<String, Object>        customParamMap;

    /**
     * 获得 promotion ids.
     *
     * @return the promotion ids
     */
    public String getPromotionIds(){
        String tmp = "";
        if (this.promotionList != null){
            for (PromotionCommand cmd : this.promotionList){
                if (tmp == "")
                    tmp = cmd.getPromotionId().toString();
                else
                    tmp = tmp + "," + cmd.getPromotionId().toString();
            }
        }
        return tmp;
    }

    /**
     * 获得 购物车行 金额小计 *.
     *
     * @return the 购物车行 金额小计 *
     */
    public BigDecimal getSubTotalAmt(){
        return subTotalAmt;
    }

    /**
     * 设置 购物车行 金额小计 *.
     *
     * @param subTotalAmt
     *            the new 购物车行 金额小计 *
     */
    public void setSubTotalAmt(BigDecimal subTotalAmt){
        this.subTotalAmt = subTotalAmt;
    }

    /**
     * 获得 吊牌价 *.
     *
     * @return the 吊牌价 *
     */
    public BigDecimal getListPrice(){
        return listPrice;
    }

    /**
     * 设置 吊牌价 *.
     *
     * @param listPrice
     *            the new 吊牌价 *
     */
    public void setListPrice(BigDecimal listPrice){
        this.listPrice = listPrice;
    }

    /**
     * 获得 商品图片 *.
     *
     * @return the 商品图片 *
     */
    public String getItemPic(){
        return itemPic;
    }

    /**
     * 设置 商品图片 *.
     *
     * @param itemPic
     *            the new 商品图片 *
     */
    public void setItemPic(String itemPic){
        this.itemPic = itemPic;
    }

    /**
     * Checks if is gift.
     *
     * @return true, if checks if is gift
     */
    public boolean isGift(){
        return isGift;
    }

    /**
     * 设置 gift.
     *
     * @param isGift
     *            the gift
     */
    public void setGift(boolean isGift){
        this.isGift = isGift;
    }

    /**
     * 获得 行促销(此商口多个数量的促销总和) 折扣-包含整单优惠分摊.
     *
     * @return the 行促销(此商口多个数量的促销总和) 折扣-包含整单优惠分摊
     */
    public BigDecimal getDiscount(){
        return discount;
    }

    /**
     * 设置 行促销(此商口多个数量的促销总和) 折扣-包含整单优惠分摊.
     *
     * @param discount
     *            the new 行促销(此商口多个数量的促销总和) 折扣-包含整单优惠分摊
     */
    public void setDiscount(BigDecimal discount){
        this.discount = discount;
    }

    /**
     * 获得 购物数量 *.
     *
     * @return the 购物数量 *
     */
    public Integer getQuantity(){
        return quantity;
    }

    /**
     * 设置 购物数量 *.
     *
     * @param quantity
     *            the new 购物数量 *
     */
    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

    /**
     * 获得 商品名称 *.
     *
     * @return the 商品名称 *
     */
    public String getItemName(){
        return itemName;
    }

    /**
     * 设置 商品名称 *.
     *
     * @param itemName
     *            the new 商品名称 *
     */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    /**
     * 获得 商品单价.
     *
     * @return the 商品单价
     */
    public BigDecimal getSalePrice(){
        return salePrice;
    }

    /**
     * 设置 商品单价.
     *
     * @param salePrice
     *            the new 商品单价
     */
    public void setSalePrice(BigDecimal salePrice){
        this.salePrice = salePrice;
    }

    /**
     * 获得 extention code.
     *
     * @return the extention code
     */
    public String getExtentionCode(){
        return extentionCode;
    }

    /**
     * 设置 extention code.
     *
     * @param extentionCode
     *            the extention code
     */
    public void setExtentionCode(String extentionCode){
        this.extentionCode = extentionCode;
    }

    /**
     * 获得 会员id.
     *
     * @return the 会员id
     */
    public Long getMemberId(){
        return memberId;
    }

    /**
     * 设置 会员id.
     *
     * @param memberId
     *            the new 会员id
     */
    public void setMemberId(Long memberId){
        this.memberId = memberId;
    }

    /**
     * 获得 加入时间.
     *
     * @return the 加入时间
     */
    public Date getCreateTime(){
        return createTime;
    }

    /**
     * 设置 加入时间.
     *
     * @param createTime
     *            the new 加入时间
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    /**
     * 获得 pK *.
     *
     * @return the pK *
     */
    public Long getId(){
        return id;
    }

    /**
     * 设置 pK *.
     *
     * @param id
     *            the new pK *
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 商品款的id *.
     *
     * @return the 商品款的id *
     */
    public Long getSkuId(){
        return skuId;
    }

    /**
     * 设置 商品款的id *.
     *
     * @param skuId
     *            the new 商品款的id *
     */
    public void setSkuId(Long skuId){
        this.skuId = skuId;
    }

    /**
     * 获得 销售属性 *.
     *
     * @return the 销售属性 *
     */
    public String getSaleProperty(){
        return saleProperty;
    }

    /**
     * 设置 销售属性 *.
     *
     * @param saleProperty
     *            the new 销售属性 *
     */
    public void setSaleProperty(String saleProperty){
        this.saleProperty = saleProperty;
    }

    /**
     * 获得 结算状态 0未选中结算 1选中结算.
     *
     * @return the 结算状态 0未选中结算 1选中结算
     */
    public Integer getSettlementState(){
        return settlementState;
    }

    /**
     * 设置 结算状态 0未选中结算 1选中结算.
     *
     * @param settlementState
     *            the new 结算状态 0未选中结算 1选中结算
     */
    public void setSettlementState(Integer settlementState){
        this.settlementState = settlementState;
    }

    /**
     * 获得 ***** 扩展engine所需要的字段begin ******.
     *
     * @return the ***** 扩展engine所需要的字段begin ******
     */
    public String getProductCode(){
        return productCode;
    }

    /**
     * 设置 ***** 扩展engine所需要的字段begin ******.
     *
     * @param productCode
     *            the new ***** 扩展engine所需要的字段begin ******
     */
    public void setProductCode(String productCode){
        this.productCode = productCode;
    }

    /**
     * 获得 lable ids.
     *
     * @return the lable ids
     */
    public List<Long> getLableIds(){
        return lableIds;
    }

    /**
     * 设置 lable ids.
     *
     * @param lableIds
     *            the lable ids
     */
    public void setLableIds(List<Long> lableIds){
        this.lableIds = lableIds;
    }

    /**
     * 获得 brand id.
     *
     * @return the brand id
     */
    public String getBrandId(){
        return brandId;
    }

    /**
     * 设置 brand id.
     *
     * @param brandId
     *            the brand id
     */
    public void setBrandId(String brandId){
        this.brandId = brandId;
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
     * 获得 store id.
     *
     * @return the store id
     */
    public long getStoreId(){
        return storeId;
    }

    /**
     * 设置 store id.
     *
     * @param storeId
     *            the store id
     */
    public void setStoreId(long storeId){
        this.storeId = storeId;
    }

    /**
     * 获得 indstry id.
     *
     * @return the indstry id
     */
    public Long getIndstryId(){
        return indstryId;
    }

    /**
     * 设置 indstry id.
     *
     * @param indstryId
     *            the indstry id
     */
    public void setIndstryId(Long indstryId){
        this.indstryId = indstryId;
    }

    /**
     * 获得 state.
     *
     * @return the state
     */
    public String getState(){
        return state;
    }

    /**
     * 设置 state.
     *
     * @param state
     *            the state
     */
    public void setState(String state){
        this.state = state;
    }

    /**
     * 设置 valid.
     *
     * @param isValid
     *            the valid
     */
    public void setValid(boolean isValid){
        this.isValid = isValid;
    }

    /**
     * Checks if is valid.
     *
     * @return true, if checks if is valid
     */
    public boolean isValid(){
        return isValid;
    }

    /**
     * 获得 有效性检查类型：1.
     *
     * @return the 有效性检查类型：1
     */
    public Integer getValidType(){
        return validType;
    }

    /**
     * 设置 有效性检查类型：1.
     *
     * @param validType
     *            the new 有效性检查类型：1
     */
    public void setValidType(Integer validType){
        this.validType = validType;
    }

    /**
     * 获得 0代表非买品 1代表主卖品 与itemInfo中的type一致.
     *
     * @return the 0代表非买品 1代表主卖品 与itemInfo中的type一致
     */
    public Integer getType(){
        return type;
    }

    /**
     * 设置 0代表非买品 1代表主卖品 与itemInfo中的type一致.
     *
     * @param type
     *            the new 0代表非买品 1代表主卖品 与itemInfo中的type一致
     */
    public void setType(Integer type){
        this.type = type;
    }

    /**
     * 获得 库房id *.
     *
     * @return the 库房id *
     */
    public Long getWareHoseId(){
        return wareHoseId;
    }

    /**
     * 设置 库房id *.
     *
     * @param wareHoseId
     *            the new 库房id *
     */
    public void setWareHoseId(Long wareHoseId){
        this.wareHoseId = wareHoseId;
    }

    /**
     * 获得 库房名称 *.
     *
     * @return the 库房名称 *
     */
    public String getWareHoseName(){
        return wareHoseName;
    }

    /**
     * 设置 库房名称 *.
     *
     * @param wareHoseName
     *            the new 库房名称 *
     */
    public void setWareHoseName(String wareHoseName){
        this.wareHoseName = wareHoseName;
    }

    /**
     * 获得 是否可见 1可见 /0不可见 *.
     *
     * @return the 是否可见 1可见 /0不可见 *
     */
    public Integer getVisibleMark(){
        return visibleMark;
    }

    /**
     * 设置 是否可见 1可见 /0不可见 *.
     *
     * @param visibleMark
     *            the new 是否可见 1可见 /0不可见 *
     */
    public void setVisibleMark(Integer visibleMark){
        this.visibleMark = visibleMark;
    }

    /**
     * 获得 是否限购 1限购 0不限购 *.
     *
     * @return the 是否限购 1限购 0不限购 *
     */
    public Integer getLimitMark(){
        return limitMark;
    }

    /**
     * 设置 是否限购 1限购 0不限购 *.
     *
     * @param limitMark
     *            the new 是否限购 1限购 0不限购 *
     */
    public void setLimitMark(Integer limitMark){
        this.limitMark = limitMark;
    }

    /**
     * 获得 是否有库存 1有库存 0无库存 *.
     *
     * @return the 是否有库存 1有库存 0无库存 *
     */
    public Integer getStockMark(){
        return stockMark;
    }

    /**
     * 设置 是否有库存 1有库存 0无库存 *.
     *
     * @param stockMark
     *            the new 是否有库存 1有库存 0无库存 *
     */
    public void setStockMark(Integer stockMark){
        this.stockMark = stockMark;
    }

    /**
     * 获得 库存数量 *.
     *
     * @return the 库存数量 *
     */
    public Integer getStock(){
        return stock;
    }

    /**
     * 设置 库存数量 *.
     *
     * @param stock
     *            the new 库存数量 *
     */
    public void setStock(Integer stock){
        this.stock = stock;
    }

    /**
     * 获得 限购数量 *.
     *
     * @return the 限购数量 *
     */
    public Integer getLimit(){
        return limit;
    }

    /**
     * 设置 限购数量 *.
     *
     * @param limit
     *            the new 限购数量 *
     */
    public void setLimit(Integer limit){
        this.limit = limit;
    }

    /**
     * 获得 商品id *.
     *
     * @return the 商品id *
     */
    public Long getItemId(){
        return itemId;
    }

    /**
     * 设置 商品id *.
     *
     * @param itemId
     *            the new 商品id *
     */
    public void setItemId(Long itemId){
        this.itemId = itemId;
    }

    /**
     * 获得 店铺id *.
     *
     * @return the 店铺id *
     */
    public Long getShopId(){
        return shopId;
    }

    /**
     * 设置 店铺id *.
     *
     * @param shopId
     *            the new 店铺id *
     */
    public void setShopId(Long shopId){
        this.shopId = shopId;
    }

    /**
     * 获得 店铺名称 *.
     *
     * @return the 店铺名称 *
     */
    public String getShopName(){
        return shopName;
    }

    /**
     * 设置 店铺名称 *.
     *
     * @param shopName
     *            the new 店铺名称 *
     */
    public void setShopName(String shopName){
        this.shopName = shopName;
    }

    /**
     * 获得 销售属性 *.
     *
     * @return the 销售属性 *
     */
    public List<SkuProperty> getSkuPropertys(){
        return skuPropertys;
    }

    /**
     * 设置 销售属性 *.
     *
     * @param skuPropertys
     *            the new 销售属性 *
     */
    public void setSkuPropertys(List<SkuProperty> skuPropertys){
        this.skuPropertys = skuPropertys;
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
     * 获得 行上的Coupon *.
     *
     * @return the 行上的Coupon *
     */
    public PromotionCouponCodeCommand getCouponCodeOnLine(){
        return couponCodeOnLine;
    }

    /**
     * 设置 行上的Coupon *.
     *
     * @param couponCodeOnLine
     *            the new 行上的Coupon *
     */
    public void setCouponCodeOnLine(PromotionCouponCodeCommand couponCodeOnLine){
        this.couponCodeOnLine = couponCodeOnLine;
    }

    /**
     * 获得 活动id.
     *
     * @return the 活动id
     */
    public Long getPromotionId(){
        return promotionId;
    }

    /**
     * 设置 活动id.
     *
     * @param promotionId
     *            the new 活动id
     */
    public void setPromotionId(Long promotionId){
        this.promotionId = promotionId;
    }

    /**
     * 获得 优惠记录id.
     *
     * @return the 优惠记录id
     */
    public Long getSettingId(){
        return settingId;
    }

    /**
     * 设置 优惠记录id.
     *
     * @param settingId
     *            the new 优惠记录id
     */
    public void setSettingId(Long settingId){
        this.settingId = settingId;
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
     * 获得 行分组 *.
     *
     * @return the 行分组 *
     */
    public Long getLineGroup(){
        return lineGroup;
    }

    /**
     * 设置 行分组 *.
     *
     * @param lineGroup
     *            the new 行分组 *
     */
    public void setLineGroup(Long lineGroup){
        this.lineGroup = lineGroup;
    }

    /**
     * 获得 套餐、行赠品类型，只有一个PromotionId 其他类型可能有多个活动 *.
     *
     * @return the 套餐、行赠品类型，只有一个PromotionId 其他类型可能有多个活动 *
     */
    public List<PromotionCommand> getPromotionList(){
        return promotionList;
    }

    /**
     * 设置 套餐、行赠品类型，只有一个PromotionId 其他类型可能有多个活动 *.
     *
     * @param promotionList
     *            the new 套餐、行赠品类型，只有一个PromotionId 其他类型可能有多个活动 *
     */
    public void setPromotionList(List<PromotionCommand> promotionList){
        this.promotionList = promotionList;
    }

    /**
     * 获得 1是行赠品,0是整单赠品.
     *
     * @return the 1是行赠品,0是整单赠品
     */
    public int getGiftType(){
        return giftType;
    }

    /**
     * 设置 1是行赠品,0是整单赠品.
     *
     * @param giftType
     *            the new 1是行赠品,0是整单赠品
     */
    public void setGiftType(int giftType){
        this.giftType = giftType;
    }

    /**
     * Checks if is suit line.
     *
     * @return true, if checks if is suit line
     */
    public boolean isSuitLine(){
        return isSuitLine;
    }

    /**
     * 设置 suit line.
     *
     * @param isSuitLine
     *            the suit line
     */
    public void setSuitLine(boolean isSuitLine){
        this.isSuitLine = isSuitLine;
    }

    /**
     * Checks if is caption line.
     *
     * @return true, if checks if is caption line
     */
    public boolean isCaptionLine(){
        return isCaptionLine;
    }

    /**
     * 设置 caption line.
     *
     * @param isCaptionLine
     *            the caption line
     */
    public void setCaptionLine(boolean isCaptionLine){
        this.isCaptionLine = isCaptionLine;
    }

    /**
     * 获得 标题 *.
     *
     * @return the 标题 *
     */
    public String getLineCaption(){
        return lineCaption;
    }

    /**
     * 设置 标题 *.
     *
     * @param lineCaption
     *            the new 标题 *
     */
    public void setLineCaption(String lineCaption){
        this.lineCaption = lineCaption;
    }

    /**
     * 获得 折扣单价-不包含整单优惠分摊.
     *
     * @return the 折扣单价-不包含整单优惠分摊
     */
    public BigDecimal getDiscountPrice(){
        return discountPrice;
    }

    /**
     * 设置 折扣单价-不包含整单优惠分摊.
     *
     * @param discountPrice
     *            the new 折扣单价-不包含整单优惠分摊
     */
    public void setDiscountPrice(BigDecimal discountPrice){
        this.discountPrice = discountPrice;
    }

    /**
     * 获得 商城自定义参数，nebula不控制.
     *
     * @return the 商城自定义参数，nebula不控制
     */
    public Map<String, Object> getCustomParamMap(){
        return customParamMap;
    }

    /**
     * 设置 商城自定义参数，nebula不控制.
     *
     * @param customParamMap
     *            the new 商城自定义参数，nebula不控制
     */
    public void setCustomParamMap(Map<String, Object> customParamMap){
        this.customParamMap = customParamMap;
    }

    /**
     * Checks if is presale.
     *
     * @return true, if checks if is presale
     */
    public boolean isPresale(){
        return isPresale;
    }

    /**
     * 设置 presale.
     *
     * @param isPresale
     *            the presale
     */
    public void setPresale(boolean isPresale){
        this.isPresale = isPresale;
    }

}
