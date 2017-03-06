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
package com.baozun.nebula.web.controller.order.viewcommand;

import java.math.BigDecimal;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 订单行包装信息显示的view command
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 * @see com.baozun.nebula.sdk.command.OrderLinePackageInfoCommand
 */
public class OrderLinePackageInfoViewCommand extends BaseViewCommand{

    private static final long serialVersionUID = -6352890939760833124L;

    /** 对应的关联关系id. */
    private Long orderLinePackageInfoId;

    /** 对应的包装信息id. */
    private Long packageInfoId;

    /** 对应的订单行id. */
    private Long orderLineId;

    //*****************************************************************

    /**
     * 包装类型：SCM定义的列表中选取.
     * 
     * <p>
     * reebok的特殊处理是要基于到商品行级别的，
     * </p>
     * 
     * <ul>
     * <li>specialType (1：需特殊处理核对 2：包裹填充物）（传1 如果reebok需要特殊处理）；</li>
     * <li>isSpecialPackaging（是否特殊包装（0：否 1：是））（传0 对于reebok）;</li>
     * <li>OrderLineGift（ type 50：特殊商品印刷 如果reebok需要特殊商品印刷 ;memo: recipeid）</li>
     * </ul>
     */
    private Integer type;

    /**
     * 包装金额(不用纠结名字,和接口统一).
     *
     * @since 5.3.2.11
     */
    private BigDecimal total;

    /**
     * 包装特征信息(可有可无).
     * 
     * <blockquote>
     * <p>
     * 和 type 字段一起起到辨识以及拆分订单行的作用,相同的 type 但是不同的featureInfo 那么会渲染成两行数据
     * </p>
     * 
     * <p>
     * 比如 reebok 有recipeid .
     * </p>
     * </blockquote>
     */
    private String featureInfo;

    /**
     * 扩展信息 (JSON字符串格式).
     * 
     * <p>
     * 通常这个字段仅仅用于解析渲染
     * </p>
     * 
     * <p>
     * 对于 reebok而言,传递的是定制商品的信息, 比如 左脚写的xin 右脚文字 jin,格式每个store 可能都不一样,只要确保可以解析即可
     * </p>
     */
    private String extendInfo;

    //******************************************************************************************
    /**
     * 获得 包装类型：SCM定义的列表中选取.
     * *
     * <p>
     * reebok的特殊处理是要基于到商品行级别的，
     * </p>
     * 
     * <ul>
     * <li>specialType (1：需特殊处理核对 2：包裹填充物）（传1 如果reebok需要特殊处理）；</li>
     * <li>isSpecialPackaging（是否特殊包装（0：否 1：是））（传0 对于reebok）;</li>
     * <li>OrderLineGift（ type 50：特殊商品印刷 如果reebok需要特殊商品印刷 ;memo: recipeid）</li>
     * </ul>
     * 
     * @return the type
     */
    public Integer getType(){
        return type;
    }

    /**
     * 设置 包装类型：SCM定义的列表中选取.
     * <p>
     * reebok的特殊处理是要基于到商品行级别的，
     * </p>
     * 
     * <ul>
     * <li>specialType (1：需特殊处理核对 2：包裹填充物）（传1 如果reebok需要特殊处理）；</li>
     * <li>isSpecialPackaging（是否特殊包装（0：否 1：是））（传0 对于reebok）;</li>
     * <li>OrderLineGift（ type 50：特殊商品印刷 如果reebok需要特殊商品印刷 ;memo: recipeid）</li>
     * </ul>
     * 
     * @param type
     *            the type to set
     */
    public void setType(Integer type){
        this.type = type;
    }

    /**
     * 获得 包装金额(不用纠结名字,和接口统一).
     *
     * @return the total
     */
    public BigDecimal getTotal(){
        return total;
    }

    /**
     * 设置 包装金额(不用纠结名字,和接口统一).
     *
     * @param total
     *            the total to set
     */
    public void setTotal(BigDecimal total){
        this.total = total;
    }

    /**
     * 获得 包装特征信息(可有可无).
     * 
     * <blockquote>
     * <p>
     * 和 type 字段一起起到辨识以及拆分订单行的作用,相同的 type 但是不同的featureInfo 那么会渲染成两行数据
     * </p>
     * 
     * <p>
     * 比如 reebok 有recipeid .
     * </p>
     * </blockquote>
     * 
     * @return the featureInfo
     */
    public String getFeatureInfo(){
        return featureInfo;
    }

    /**
     * 设置 包装特征信息(可有可无).
     * 
     * <blockquote>
     * <p>
     * 和 type 字段一起起到辨识以及拆分订单行的作用,相同的 type 但是不同的featureInfo 那么会渲染成两行数据
     * </p>
     * 
     * <p>
     * 比如 reebok 有recipeid .
     * </p>
     * </blockquote>
     * 
     * @param featureInfo
     *            the featureInfo to set
     */
    public void setFeatureInfo(String featureInfo){
        this.featureInfo = featureInfo;
    }

    /**
     * 获得 扩展信息 (JSON字符串格式).
     * 
     * <p>
     * 通常这个字段仅仅用于解析渲染
     * </p>
     * 
     * <p>
     * 对于 reebok而言,传递的是定制商品的信息, 比如 左脚写的xin 右脚文字 jin,格式每个store 可能都不一样,只要确保可以解析即可
     * </p>
     * 
     * @return the extendInfo
     */
    public String getExtendInfo(){
        return extendInfo;
    }

    /**
     * 设置 扩展信息 (JSON字符串格式).
     * 
     * <p>
     * 通常这个字段仅仅用于解析渲染
     * </p>
     * 
     * <p>
     * 对于 reebok而言,传递的是定制商品的信息, 比如 左脚写的xin 右脚文字 jin,格式每个store 可能都不一样,只要确保可以解析即可
     * </p>
     * 
     * @param extendInfo
     *            the extendInfo to set
     */
    public void setExtendInfo(String extendInfo){
        this.extendInfo = extendInfo;
    }

    /**
     * 获得 对应的包装信息id.
     *
     * @return the packageInfoId
     */
    public Long getPackageInfoId(){
        return packageInfoId;
    }

    /**
     * 设置 对应的包装信息id.
     *
     * @param packageInfoId
     *            the packageInfoId to set
     */
    public void setPackageInfoId(Long packageInfoId){
        this.packageInfoId = packageInfoId;
    }

    /**
     * 获得 对应的关联关系id.
     *
     * @return the orderLinePackageInfoId
     */
    public Long getOrderLinePackageInfoId(){
        return orderLinePackageInfoId;
    }

    /**
     * 设置 对应的关联关系id.
     *
     * @param orderLinePackageInfoId
     *            the orderLinePackageInfoId to set
     */
    public void setOrderLinePackageInfoId(Long orderLinePackageInfoId){
        this.orderLinePackageInfoId = orderLinePackageInfoId;
    }

    /**
     * 获得 对应的订单行id.
     *
     * @return the orderLineId
     */
    public Long getOrderLineId(){
        return orderLineId;
    }

    /**
     * 设置 对应的订单行id.
     *
     * @param orderLineId
     *            the orderLineId to set
     */
    public void setOrderLineId(Long orderLineId){
        this.orderLineId = orderLineId;
    }
}
