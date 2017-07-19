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
package com.baozun.nebula.web.controller.shoppingcart.viewcommand;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.shoppingcart.ShoppingCartLine;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.BaseViewCommand;
import com.baozun.nebula.web.controller.shoppingcart.converter.ShoppingcartViewCommandConverter;

/**
 * 购物车里面的每行明细.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年4月25日 上午11:13:26
 * @see com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand
 * @see com.baozun.nebula.model.shoppingcart.ShoppingCartLine
 * @see com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao
 * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartManager#findShoppingCartLinesByMemberId(Long, Integer)
 * @since 5.3.1
 */
public class ShoppingCartLineSubViewCommand extends BaseViewCommand{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6308223304441399376L;

    //---------------------------------------------------------------

    /**
     * 购物车行的唯一标识,如果是会员购物车,那么此处的id={@link ShoppingCartLine#id},如果是游客的购物车,那么自己算出id,以便对这个id进行删除/修改.
     */
    private Long id;

    /** 状态. */
    private Status status;

    //---------------------------------------------------------------

    /** 添加时间,此处的时间通常用于页面购物车行的排序,仅此而已. */
    private Date addTime;

    /**
     * 哪一个组,以往相同sku添加到购物车,那么购物车是一行记录,数量是2;
     * <p>
     * 但是我们要实现,两行记录,每行的数量是1, 因为可能其中的一个sku是bundle里面的,另外一个不是
     * </p>
     * 此处主要是为了区分按组显示.
     */
    private Integer group;

    //---------------------------------------------------------------

    /** 买的什么商品id. */
    private Long itemId;

    /** 买的什么商品code. */
    private String itemCode;

    /** 商品名称是什么. */
    private String itemName;

    /** 买的哪个sku. */
    private Long skuId;

    /** 外部编码 {@link Sku#outid},是扣减库存以及和后端对接数据的核心参数. */
    private String extentionCode;

    //XXX feilong 销售属性map 是什么,此处应该可以和 PDP 骨架里面的相关view Command 通用 
    //参见 标准架构里面的  Map<PropertySubViewCommand, List<PropertyValueSubViewCommand>> salesPropertiesMap  
    //但是可能的结构是  Map<PropertySubViewCommand, PropertyValueSubViewCommand> salesPropertiesMap  
    //也可能的结构是  Map<String, String> salesPropertiesMap  
    /** The map. */
    private Map<String, SkuProperty> propertiesMap;

    /** 数量几个. */
    private Integer quantity;

    /**
     * 库存数量.
     * 
     * @see com.baozun.nebula.sdk.command.SkuCommand#getAvailableQty()
     * @since 5.3.1.8
     */
    private Integer stock;

    //**************************************************************

    /** 商品图片. */
    private String itemPic;

    //---------------------------------------------------------------

    /**
     * 是否选中,主要用来渲染view里面的checkbox checked状态.
     * 
     * 参见 {@link com.baozun.nebula.model.shoppingcart.ShoppingCartLine#getSettlementState()} 结算状态,表结构里面是使用int来标识的,"0未选中结算,1选中结算"
     * ,其实就两个状态,在前端view里面 重新设计为boolean,true为选中,false为不选中
     * 
     * @see com.baozun.nebula.model.shoppingcart.ShoppingCartLine#getSettlementState()
     * @see com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand#getSettlementState()
     */
    private boolean checked;

    /**
     * 是否是赠品.
     * 
     * 参见 {@link com.baozun.nebula.model.shoppingcart.ShoppingCartLine#isGift()}
     */
    private boolean isGift;

    //------------------------价格信息---------------------------------------

    /** 销售价. */
    private BigDecimal salePrice;

    /** 吊牌价(原单价). */
    private BigDecimal listPrice;

    /** 购物车行 金额小计 *. */
    private BigDecimal subTotalAmt = ZERO;

    //---------------------------------------------------------------

    /**
     * 对应的包装信息.
     * 
     * @since 5.3.2.13
     */
    private List<ShoppingCartLinePackageInfoViewCommand> shoppingCartLinePackageInfoViewCommandList;

    //---------------------------------------------------------------
    /**
     * 自定义用于显示的 属性map.
     * 
     * <p>
     * 一般情况下,上述参数完全够用了, <br>
     * 但是二班情况,比如渲染购物车的时候,需要显示行的某个非销售属性,或者判断个分类之类的,
     * 
     * <br>
     * 商城 可以自定义 {@link ShoppingcartViewCommandConverter} 实现类, 重写 {@link com.baozun.nebula.web.controller.shoppingcart.NebulaShoppingCartController#buildShoppingCartViewCommand(MemberDetails, HttpServletRequest)
     * buildShoppingCartViewCommand}
     * 方法,再批量去设置每行属性,性能也不会太差
     * </p>
     * 
     * @since 5.3.2.18
     */
    private Map<String, Object> customViewParamMap;

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
     * 获得 购物车行的唯一标识,如果是会员购物车,那么此处的id={@link ShoppingCartLine#id},如果是游客的购物车,那么自己算出id,以便对这个id进行删除/修改.
     *
     * @return the id
     */
    public Long getId(){
        return id;
    }

    /**
     * 设置 购物车行的唯一标识,如果是会员购物车,那么此处的id={@link ShoppingCartLine#id},如果是游客的购物车,那么自己算出id,以便对这个id进行删除/修改.
     *
     * @param id
     *            the id to set
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 添加时间,此处的时间通常用于页面购物车行的排序,仅此而已.
     *
     * @return the addTime
     */
    public Date getAddTime(){
        return addTime;
    }

    /**
     * 设置 添加时间,此处的时间通常用于页面购物车行的排序,仅此而已.
     *
     * @param addTime
     *            the addTime to set
     */
    public void setAddTime(Date addTime){
        this.addTime = addTime;
    }

    /**
     * 获得 买的什么商品id.
     *
     * @return the itemId
     */
    public Long getItemId(){
        return itemId;
    }

    /**
     * 设置 买的什么商品id.
     *
     * @param itemId
     *            the itemId to set
     */
    public void setItemId(Long itemId){
        this.itemId = itemId;
    }

    /**
     * 获得 买的什么商品code.
     *
     * @return the itemCode
     */
    public String getItemCode(){
        return itemCode;
    }

    /**
     * 设置 买的什么商品code.
     *
     * @param itemCode
     *            the itemCode to set
     */
    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
    }

    /**
     * 获得 商品名称是什么.
     *
     * @return the itemName
     */
    public String getItemName(){
        return itemName;
    }

    /**
     * 设置 商品名称是什么.
     *
     * @param itemName
     *            the itemName to set
     */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    /**
     * 获得 买的哪个sku.
     *
     * @return the skuId
     */
    public Long getSkuId(){
        return skuId;
    }

    /**
     * 设置 买的哪个sku.
     *
     * @param skuId
     *            the skuId to set
     */
    public void setSkuId(Long skuId){
        this.skuId = skuId;
    }

    /**
     * 获得 外部编码 {@link Sku#outid},是扣减库存以及和后端对接数据的核心参数.
     *
     * @return the extentionCode
     */
    public String getExtentionCode(){
        return extentionCode;
    }

    /**
     * 设置 外部编码 {@link Sku#outid},是扣减库存以及和后端对接数据的核心参数.
     *
     * @param extentionCode
     *            the extentionCode to set
     */
    public void setExtentionCode(String extentionCode){
        this.extentionCode = extentionCode;
    }

    /**
     * 获得 数量几个.
     *
     * @return the quantity
     */
    public Integer getQuantity(){
        return quantity;
    }

    /**
     * 设置 数量几个.
     *
     * @param quantity
     *            the quantity to set
     */
    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

    /**
     * 获得 商品图片.
     *
     * @return the itemPic
     */
    public String getItemPic(){
        return itemPic;
    }

    /**
     * 设置 商品图片.
     *
     * @param itemPic
     *            the itemPic to set
     */
    public void setItemPic(String itemPic){
        this.itemPic = itemPic;
    }

    /**
     * 获得 是否选中,主要用来渲染view里面的checkbox checked状态.
     *
     * @return the checked
     */
    public boolean getChecked(){
        return checked;
    }

    /**
     * 设置 是否选中,主要用来渲染view里面的checkbox checked状态.
     *
     * @param checked
     *            the checked to set
     */
    public void setChecked(boolean checked){
        this.checked = checked;
    }

    /**
     * 获得 是否是赠品.
     *
     * @return the isGift
     */
    public boolean getIsGift(){
        return isGift;
    }

    /**
     * 设置 是否是赠品.
     *
     * @param isGift
     *            the isGift to set
     */
    public void setIsGift(boolean isGift){
        this.isGift = isGift;
    }

    /**
     * 获得 销售价.
     *
     * @return the salePrice
     */
    public BigDecimal getSalePrice(){
        return salePrice;
    }

    /**
     * 设置 销售价.
     *
     * @param salePrice
     *            the salePrice to set
     */
    public void setSalePrice(BigDecimal salePrice){
        this.salePrice = salePrice;
    }

    /**
     * 获得 吊牌价(原单价).
     *
     * @return the listPrice
     */
    public BigDecimal getListPrice(){
        return listPrice;
    }

    /**
     * 设置 吊牌价(原单价).
     *
     * @param listPrice
     *            the listPrice to set
     */
    public void setListPrice(BigDecimal listPrice){
        this.listPrice = listPrice;
    }

    /**
     * 获得 哪一个组,以往相同sku添加到购物车,那么购物车是一行记录,数量是2;
     * <p>
     * 但是我们要实现,两行记录,每行的数量是1, 因为可能其中的一个sku是bundle里面的,另外一个不是
     * </p>
     * 此处主要是为了区分按组显示.
     *
     * @return the group
     */
    public Integer getGroup(){
        return group;
    }

    /**
     * 设置 哪一个组,以往相同sku添加到购物车,那么购物车是一行记录,数量是2;
     * <p>
     * 但是我们要实现,两行记录,每行的数量是1, 因为可能其中的一个sku是bundle里面的,另外一个不是
     * </p>
     * 此处主要是为了区分按组显示.
     *
     * @param group
     *            the group to set
     */
    public void setGroup(Integer group){
        this.group = group;
    }

    /**
     * 获得 map.
     *
     * @return the map
     */
    public Map<String, SkuProperty> getPropertiesMap(){
        return propertiesMap;
    }

    /**
     * 设置 map.
     *
     * @param propertiesMap
     *            the new map
     */
    public void setPropertiesMap(Map<String, SkuProperty> propertiesMap){
        this.propertiesMap = propertiesMap;
    }

    /**
     * 获得 状态.
     *
     * @return the status
     */
    public Status getStatus(){
        return status;
    }

    /**
     * 设置 状态.
     *
     * @param status
     *            the status to set
     */
    public void setStatus(Status status){
        this.status = status;
    }

    /**
     * 获得 库存数量.
     *
     * @return the stock
     * @since 5.3.1.8
     */
    public Integer getStock(){
        return stock;
    }

    /**
     * 设置 库存数量.
     *
     * @param stock
     *            the stock to set
     * @since 5.3.1.8
     */
    public void setStock(Integer stock){
        this.stock = stock;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * 获得 对应的包装信息.
     *
     * @return the shoppingCartLinePackageInfoViewCommandList
     * @since 5.3.2.13
     */
    public List<ShoppingCartLinePackageInfoViewCommand> getShoppingCartLinePackageInfoViewCommandList(){
        return shoppingCartLinePackageInfoViewCommandList;
    }

    /**
     * 设置 对应的包装信息.
     *
     * @param shoppingCartLinePackageInfoViewCommandList
     *            the shoppingCartLinePackageInfoViewCommandList to set
     * @since 5.3.2.13
     */
    public void setShoppingCartLinePackageInfoViewCommandList(List<ShoppingCartLinePackageInfoViewCommand> shoppingCartLinePackageInfoViewCommandList){
        this.shoppingCartLinePackageInfoViewCommandList = shoppingCartLinePackageInfoViewCommandList;
    }

    /**
     * 自定义用于显示的 属性map.
     * 
     * <p>
     * 一般情况下,上述参数完全够用了, <br>
     * 但是二班情况,比如渲染购物车的时候,需要显示行的某个非销售属性,或者判断个分类之类的,
     * 
     * <br>
     * 商城 可以自定义 {@link ShoppingcartViewCommandConverter} 实现类, 重写 {@link com.baozun.nebula.web.controller.shoppingcart.NebulaShoppingCartController#buildShoppingCartViewCommand(MemberDetails, HttpServletRequest)
     * buildShoppingCartViewCommand}
     * 方法,再批量去设置每行属性,性能也不会太差
     * </p>
     *
     * @return the 自定义用于显示的 属性map
     * @since 5.3.2.18
     */
    public Map<String, Object> getCustomViewParamMap(){
        return customViewParamMap;
    }

    /**
     * 自定义用于显示的 属性map.
     * 
     * <p>
     * 一般情况下,上述参数完全够用了, <br>
     * 但是二班情况,比如渲染购物车的时候,需要显示行的某个非销售属性,或者判断个分类之类的,
     * 
     * <br>
     * 商城 可以自定义 {@link ShoppingcartViewCommandConverter} 实现类, 重写 {@link com.baozun.nebula.web.controller.shoppingcart.NebulaShoppingCartController#buildShoppingCartViewCommand(MemberDetails, HttpServletRequest)
     * buildShoppingCartViewCommand}
     * 方法,再批量去设置每行属性,性能也不会太差
     * </p>
     *
     * @param customViewParamMap
     *            the new 自定义用于显示的 属性map
     * @since 5.3.2.18
     */
    public void setCustomViewParamMap(Map<String, Object> customViewParamMap){
        this.customViewParamMap = customViewParamMap;
    }
}
