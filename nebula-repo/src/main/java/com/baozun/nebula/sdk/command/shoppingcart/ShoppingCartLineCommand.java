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
 * 
 * <h3>特殊说明:</h3>
 * 
 * <blockquote>
 * <p>
 * 理论上内存对象{@code --->} db,和{@code db---->}内存对象 两者使用的 command 应该是不一样的, <br>
 * 不过历史到现在都是使用的这一个公用类,为了最大化的减少开发成本,暂时维持现状,<br>
 * but 这不是最佳实践 -- by feilong
 * </p>
 * </blockquote>
 * 
 * @see com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand
 * @see com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop
 * @see com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand
 */
public class ShoppingCartLineCommand extends BaseModel{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5071101827860973497L;

    //*********************************************************************************
    /** PK *. */
    private Long id;

    /** 会员id. */
    private Long memberId;

    //*********************************************************************************

    /** 店铺id . */
    private Long shopId;

    /** 店铺名称 . */
    private String shopName;

    //*********************************************************************************
    /** skuId . */
    private Long skuId;

    /** The extention code. */
    private String extentionCode;

    /** 购物数量 . */
    private Integer quantity;
    //*********************************************************************************

    /** 商品id . */
    private Long itemId;

    /** 商品code. */
    private String productCode;

    /** 商品名称 . */
    private String itemName;

    /** 商品图片 *. */
    private String itemPic;

    /**
     * 对应关联关系的商品id.
     * 
     * <p>
     * 普通商品下单,这个属性是没有值,<br>
     * 如果商品是bundle商品,比如3个不同的item 的不同sku 进行下单,那么会有3条 soline记录,在需要显示订单行的场合,比如订单明细页面,下单之后的邮件模板,<br>
     * 为了和以往的数据兼容(尽量不更改或者少更改以往的SQL),那么soline里面的 itemId,存放这个sku原本的itemId,而relatedItemId存放的是衍生的itemId
     * </p>
     * 
     * @since 5.3.1
     */
    private Long relatedItemId;

    /**
     * skuIds.
     * 
     * <p>
     * 普通商品下单,这个属性是没有值,<br>
     * 如果商品是bundle商品,比如3个不同的item 的不同sku 进行下单,那么skuids会有3个值
     * </p>
     * 
     * @since 5.3.1
     */
    private Long[] skuIds;

    //*********************************************************************************
    /** 结算状态 0未选中结算 1选中结算. */
    private Integer settlementState;

    /**
     * 销售属性.
     * 
     * @see com.baozun.nebula.model.product.Sku#getProperties()
     */
    private String saleProperty;

    /**
     * 销售属性 .
     * 
     * @see com.baozun.nebula.sdk.manager.SdkSkuManager#getSkuPros(String)
     */
    private List<SkuProperty> skuPropertys;

    //*********************************************************************************

    /** 吊牌价. */
    private BigDecimal listPrice;

    /** 商品单价. */
    private BigDecimal salePrice;

    /**
     * 购物车行 金额小计.
     * 
     * @see com.baozun.nebula.model.salesorder.OrderLine#setSubtotal(BigDecimal)
     */
    private BigDecimal subTotalAmt = BigDecimal.ZERO;

    /** 加入时间. */
    private Date createTime;

    /**
     * 0代表非买品 1代表主卖品 与itemInfo中的type一致.
     * 
     * @see com.baozun.nebula.model.product.ItemInfo#getType()
     */
    private Integer type;

    //*********************************************************************************

    /** 行分组 *. */
    private Long lineGroup;

    /** 该订单行是否属于预售订单 *. */
    private boolean isPresale;

    /** 是套餐行 t是标题行，f不是标题行。标题行是促销活动提示，非购物车行 *. */
    private boolean isCaptionLine;

    /** 标题 *. */
    private String lineCaption;

    /** 是套餐行 t是套餐行，f不是套餐行 *. */
    private boolean isSuitLine;

    /** 是否限购 1限购 0不限购 *. */
    private Integer limitMark;

    /** 限购数量 *. */
    private Integer limit;

    /**
     * 库存数量.
     * 
     * @see com.baozun.nebula.sdk.command.SkuCommand#getAvailableQty()
     */
    private Integer stock;

    /** ***** 扩展engine所需要的字段begin ******. */

    /** The category list. */
    private List<Long> categoryList;

    /** 行促销(此商口多个数量的促销总和) 折扣-包含整单优惠分摊. */
    private BigDecimal discount = BigDecimal.ZERO;

    /** 套餐、行赠品类型，只有一个PromotionId 其他类型可能有多个活动 *. */
    private List<PromotionCommand> promotionList;

    /** 是否赠品. */
    //TODO 赠品不是不进购物车的吗 by feilong
    private boolean isGift;

    /** 1是行赠品,0是整单赠品. */
    private int giftType;

    /** 分组标签 *. */
    private Set<String> comboIds;

    /** 行上的Coupon *. */
    private PromotionCouponCodeCommand couponCodeOnLine;

    /** 活动id. */
    private Long promotionId;

    /** 优惠记录id. */
    private Long settingId;

    /** 礼品显示类型，0不需要用户选择，1需要用户选择. */
    private Integer giftChoiceType;

    /** 用户不参与选择时，直接推送礼品的样数；用户参与选择时，直接推送礼品的样数. */
    private Integer giftCountLimited;

    /**
     * 对应的包装信息.
     * 
     * @since 5.3.2.13
     */
    private List<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList;

    //***************************************************************************************************

    /** 购物车中商品经有效性检查引擎检查之后是否有效的字段. */
    private boolean isValid;

    /**
     * 有效性检查类型：1.代表下架 2.代表没有库存 这个字段是结合isValid=false来使用的
     */
    private Integer validType;

    //***************************************************************************************************

    /** 商城自定义参数，nebula不控制. */
    private Map<String, Object> customParamMap;

    //**********************************************************************************************
    
    /**
     * 是否可见 1可见 /0不可见 *.
     * 
     * @deprecated 没有引用, by feilong 2016-05-13
     */
    @Deprecated
    private Integer visibleMark;

    /**
     * 是否有库存 1有库存 0无库存 *.
     * 
     * @deprecated 没有引用, by feilong 2016-05-13
     */
    @Deprecated
    private Integer stockMark;

    /**
     * 折扣单价-不包含整单优惠分摊.
     * 
     * @deprecated 感觉没有用到
     */
    @Deprecated
    private BigDecimal discountPrice = BigDecimal.ZERO;

    /**
     * The lable ids.
     * 
     * @deprecated 目前存放的是 {@link com.baozun.nebula.model.product.ItemTagRelation#getTagId()}的集合,参见
     *             {@link com.baozun.nebula.sdk.manager.impl.SdkEngineManagerImpl#packShoppingCartLine(ShoppingCartLineCommand)},不过没有什么用, by
     *             feilong 2016-05-13
     */
    @Deprecated
    private List<Long> lableIds;

    /**
     * The brand id.
     * 
     * @deprecated 没有引用, by feilong 2016-05-13
     */
    @Deprecated
    private String brandId;

    /**
     * The store id.
     * 
     * @deprecated 目前值就＝ {@link #shopId},重复了,而且没有引用, by feilong 2016-05-13
     */
    @Deprecated
    private long storeId;

    /**
     * The indstry id.
     * 
     * @deprecated 单词写错了, 应该是 industryId ,这种历史原因 先标注deprecated 不建议使用这个字段 , by feilong 2016-05-13
     */
    @Deprecated
    private Long indstryId;

    /**
     * The state.
     * 
     * @deprecated 没有用到, by feilong 2016-07-13
     */
    @Deprecated
    private String state;

    //***************************************************************************************************

    /**
     * 库房id .
     * 
     * @deprecated 没有引用, by feilong 2016-07-13
     */
    @Deprecated
    private Long wareHoseId;

    /**
     * 库房名称 .
     * 
     * @deprecated 没有引用, by feilong 2016-07-13
     */
    @Deprecated
    private String wareHoseName;
    
  //***************************************************************************************************

    /** 杂项 可以存放商品信息. 
     * @since 5.3.2.18
     * */
    private String misc;

    //**********************************************************************************************
    /**
     * 获得 promotion ids.
     *
     * @return the promotion ids
     */
    
    /**
     * hanssem扩展字段
     * @return
     */
    
    /*** 单个商品重量(如果是按照重量计费)*/
    private Double            weight;
    
    /*** 单个商品体积*/
    private Double volume;
    
    /**物流方式id*/
    private Long distributionModeId;
    
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
     * 获得 购物车行 金额小计.
     *
     * @return the 购物车行 金额小计
     */
    public BigDecimal getSubTotalAmt(){
        return subTotalAmt;
    }

    /**
     * 设置 购物车行 金额小计 .
     *
     * @param subTotalAmt
     *            the new 购物车行 金额小计
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
     * 获得 购物数量.
     *
     * @return the 购物数量
     */
    public Integer getQuantity(){
        return quantity;
    }

    /**
     * 设置 购物数量.
     *
     * @param quantity
     *            the new 购物数量
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
     * 获得 pK.
     *
     * @return the pK
     */
    public Long getId(){
        return id;
    }

    /**
     * 设置 pK.
     *
     * @param id
     *            the new pK
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 销售属性.
     *
     * @return the 销售属性
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
     * 获得 商品code.
     *
     * @return the 商品code
     */
    public String getProductCode(){
        return productCode;
    }

    /**
     * 设置 商品code.
     *
     * @param productCode
     *            the new 商品code
     */
    public void setProductCode(String productCode){
        this.productCode = productCode;
    }

    /**
     * 获得 lable ids.
     *
     * @return the lable ids
     * @deprecated 目前存放的是 {@link com.baozun.nebula.model.product.ItemTagRelation#getTagId()}的集合,参见
     *             {@link com.baozun.nebula.sdk.manager.impl.SdkEngineManagerImpl#packShoppingCartLine(ShoppingCartLineCommand)},不过没有什么用, by
     *             feilong 2016-05-13
     */
    @Deprecated
    public List<Long> getLableIds(){
        return lableIds;
    }

    /**
     * 设置 lable ids.
     *
     * @param lableIds
     *            the lable ids
     * @deprecated 目前存放的是 {@link com.baozun.nebula.model.product.ItemTagRelation#getTagId()}的集合,参见
     *             {@link com.baozun.nebula.sdk.manager.impl.SdkEngineManagerImpl#packShoppingCartLine(ShoppingCartLineCommand)},不过没有什么用, by
     *             feilong 2016-05-13
     */
    @Deprecated
    public void setLableIds(List<Long> lableIds){
        this.lableIds = lableIds;
    }

    /**
     * 获得 brand id.
     *
     * @return the brand id
     * @deprecated 没有引用, by feilong 2016-05-13
     */
    @Deprecated
    public String getBrandId(){
        return brandId;
    }

    /**
     * 设置 brand id.
     *
     * @param brandId
     *            the brand id
     * @deprecated 没有引用, by feilong 2016-05-13
     */
    @Deprecated
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
     * @deprecated 目前值就＝ {@link #getShopId()},重复了,而且没有引用, by feilong 2016-05-13
     */
    @Deprecated
    public long getStoreId(){
        return storeId;
    }

    /**
     * 设置 store id.
     *
     * @param storeId
     *            the store id
     * @deprecated 目前值就＝ {@link #getShopId()},重复了,而且没有引用, by feilong 2016-05-13
     */
    @Deprecated
    public void setStoreId(long storeId){
        this.storeId = storeId;
    }

    /**
     * 获得 indstry id.
     *
     * @return the indstry id
     * @deprecated 单词写错了, 应该是 industryId ,这种历史原因 先标注deprecated 不建议使用这个字段 , by feilong 2016-05-13
     */
    @Deprecated
    public Long getIndstryId(){
        return indstryId;
    }

    /**
     * 设置 indstry id.
     *
     * @param indstryId
     *            the indstry id
     * @deprecated 单词写错了, 应该是 industryId ,这种历史原因 先标注deprecated 不建议使用这个字段 , by feilong 2016-05-13
     */
    @Deprecated
    public void setIndstryId(Long indstryId){
        this.indstryId = indstryId;
    }

    /**
     * 获得 state.
     *
     * @return the state
     * @deprecated 没有用到, by feilong 2016-07-13
     */
    @Deprecated
    public String getState(){
        return state;
    }

    /**
     * 设置 state.
     *
     * @param state
     *            the state
     * @deprecated 没有用到, by feilong 2016-07-13
     */
    @Deprecated
    public void setState(String state){
        this.state = state;
    }

    /**
     * 购物车中商品经有效性检查引擎检查之后是否有效的字段.
     *
     * @param isValid
     *            the valid
     */
    public void setValid(boolean isValid){
        this.isValid = isValid;
    }

    /**
     * 购物车中商品经有效性检查引擎检查之后是否有效的字段.
     *
     * @return true, if checks if is valid
     */
    public boolean isValid(){
        return isValid;
    }

    /**
     * 有效性检查类型：1.代表下架 2.代表没有库存 这个字段是结合isValid=false来使用的
     *
     * @return the 有效性检查类型：1.代表下架 2.代表没有库存 这个字段是结合isValid=false来使用的
     */
    public Integer getValidType(){
        return validType;
    }

    /**
     * 有效性检查类型：1.代表下架 2.代表没有库存 这个字段是结合isValid=false来使用的
     *
     * @param validType
     *            有效性检查类型：1.代表下架 2.代表没有库存 这个字段是结合isValid=false来使用的
     */
    public void setValidType(Integer validType){
        this.validType = validType;
    }

    /**
     * 获得 0代表非买品 1代表主卖品 与itemInfo中的type一致.
     *
     * @return the 0代表非买品 1代表主卖品 与itemInfo中的type一致
     * @see com.baozun.nebula.model.product.ItemInfo#getType()
     */
    public Integer getType(){
        return type;
    }

    /**
     * 设置 0代表非买品 1代表主卖品 与itemInfo中的type一致.
     *
     * @param type
     *            the new 0代表非买品 1代表主卖品 与itemInfo中的type一致
     * @see com.baozun.nebula.model.product.ItemInfo#getType()
     */
    public void setType(Integer type){
        this.type = type;
    }

    /**
     * 获得 库房id.
     *
     * @return the 库房id
     * @deprecated 没有引用, by feilong 2016-07-13
     */
    @Deprecated
    public Long getWareHoseId(){
        return wareHoseId;
    }

    /**
     * 设置 库房id .
     *
     * @param wareHoseId
     *            the new 库房id
     * @deprecated 没有引用, by feilong 2016-07-13
     */
    @Deprecated
    public void setWareHoseId(Long wareHoseId){
        this.wareHoseId = wareHoseId;
    }

    /**
     * 获得 库房名称 .
     *
     * @return the 库房名称
     * @deprecated 没有引用, by feilong 2016-07-13
     */
    @Deprecated
    public String getWareHoseName(){
        return wareHoseName;
    }

    /**
     * 设置 库房名称 .
     *
     * @param wareHoseName
     *            the new 库房名称
     * @deprecated 没有引用, by feilong 2016-07-13
     */
    @Deprecated
    public void setWareHoseName(String wareHoseName){
        this.wareHoseName = wareHoseName;
    }

    /**
     * 获得 是否可见 1可见 /0不可见 *.
     *
     * @return the 是否可见 1可见 /0不可见 *
     * @deprecated 没有引用, by feilong 2016-05-13
     */
    @Deprecated
    public Integer getVisibleMark(){
        return visibleMark;
    }

    /**
     * 设置 是否可见 1可见 /0不可见 *.
     *
     * @param visibleMark
     *            the new 是否可见 1可见 /0不可见 *
     * @deprecated 没有引用, by feilong 2016-05-13
     */
    @Deprecated
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
     * @deprecated 没有引用, by feilong 2016-05-13
     */
    @Deprecated
    public Integer getStockMark(){
        return stockMark;
    }

    /**
     * 设置 是否有库存 1有库存 0无库存 *.
     *
     * @param stockMark
     *            the new 是否有库存 1有库存 0无库存 *
     * @deprecated 没有引用, by feilong 2016-05-13
     */
    @Deprecated
    public void setStockMark(Integer stockMark){
        this.stockMark = stockMark;
    }

    /**
     * 获得 库存数量.
     *
     * @return the 库存数量
     * @see com.baozun.nebula.sdk.command.SkuCommand#getAvailableQty()
     */
    public Integer getStock(){
        return stock;
    }

    /**
     * 设置 库存数量 .
     *
     * @param stock
     *            the new 库存数量
     * @see com.baozun.nebula.sdk.command.SkuCommand#getAvailableQty()
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
     * @deprecated 感觉没有用到
     */
    @Deprecated
    public BigDecimal getDiscountPrice(){
        return discountPrice;
    }

    /**
     * 设置 折扣单价-不包含整单优惠分摊.
     *
     * @param discountPrice
     *            the new 折扣单价-不包含整单优惠分摊
     * @deprecated 感觉没有用到
     */
    @Deprecated
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

    /**
     * 获得 skuId .
     *
     * @return the skuId
     */
    public Long getSkuId(){
        return skuId;
    }

    /**
     * 设置 skuId .
     *
     * @param skuId
     *            the skuId to set
     */
    public void setSkuId(Long skuId){
        this.skuId = skuId;
    }

    /**
     * 对应关联关系的商品id.
     * 
     * <p>
     * 普通商品下单,这个属性是没有值,<br>
     * 如果商品是bundle商品,比如3个不同的item 的不同sku 进行下单,那么会有3条 soline记录,在需要显示订单行的场合,比如订单明细页面,下单之后的邮件模板,<br>
     * 为了和以往的数据兼容(尽量不更改或者少更改以往的SQL),那么soline里面的 itemId,存放这个sku原本的itemId,而relatedItemId存放的是衍生的itemId
     * </p>
     * 
     * @return the relatedItemId
     */
    public Long getRelatedItemId(){
        return relatedItemId;
    }

    /**
     * 对应关联关系的商品id.
     * 
     * <p>
     * 普通商品下单,这个属性是没有值,<br>
     * 如果商品是bundle商品,比如3个不同的item 的不同sku 进行下单,那么会有3条 soline记录,在需要显示订单行的场合,比如订单明细页面,下单之后的邮件模板,<br>
     * 为了和以往的数据兼容(尽量不更改或者少更改以往的SQL),那么soline里面的 itemId,存放这个sku原本的itemId,而relatedItemId存放的是衍生的itemId
     * </p>
     * 
     * @param relatedItemId
     *            the relatedItemId to set
     */
    public void setRelatedItemId(Long relatedItemId){
        this.relatedItemId = relatedItemId;
    }

    /**
     * 获得 skuIds .
     * <p>
     * 普通商品下单,这个属性是没有值,<br>
     * 如果商品是bundle商品,比如3个不同的item 的不同sku 进行下单,那么skuids会有3个值
     * </p>
     * 
     * @return the skuIds
     */
    public Long[] getSkuIds(){
        return skuIds;
    }

    /**
     * 设置 skuIds .
     * <p>
     * 普通商品下单,这个属性是没有值,<br>
     * 如果商品是bundle商品,比如3个不同的item 的不同sku 进行下单,那么skuids会有3个值
     * </p>
     * 
     * @param skuIds
     *            the skuIds to set
     */
    public void setSkuIds(Long[] skuIds){
        this.skuIds = skuIds;
    }

    /**
     * 获得 对应的包装信息.
     *
     * @return the shoppingCartLinePackageInfoCommandList
     * @since 5.3.2.13
     */
    public List<ShoppingCartLinePackageInfoCommand> getShoppingCartLinePackageInfoCommandList(){
        return shoppingCartLinePackageInfoCommandList;
    }

    /**
     * 设置 对应的包装信息.
     *
     * @param shoppingCartLinePackageInfoCommandList
     *            the shoppingCartLinePackageInfoCommandList to set
     * 
     * @since 5.3.2.13
     */
    public void setShoppingCartLinePackageInfoCommandList(List<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList){
        this.shoppingCartLinePackageInfoCommandList = shoppingCartLinePackageInfoCommandList;
    }

    
    public Double getWeight(){
        return weight;
    }

    
    public void setWeight(Double weight){
        this.weight = weight;
    }

    
    public Double getVolume(){
        return volume;
    }

    
    public void setVolume(Double volume){
        this.volume = volume;
    }

    
    public Long getDistributionModeId(){
        return distributionModeId;
    }

    
    public void setDistributionModeId(Long distributionModeId){
        this.distributionModeId = distributionModeId;
    }
    /**
     * 获取 杂项信息
     * @return String
     * @since 5.3.2.18
     */
    public String getMisc(){
        return misc;
    }

    /**
     * 设置 杂项信息
     * @param String
     * @since 5.3.2.18
     */
    public void setMisc(String misc){
        this.misc = misc;
    }
    
}
