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
package com.baozun.nebula.web.controller.product.viewcommand;

import java.math.BigDecimal;
import java.util.Date;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 商品的基本信息
 * 
 * <p>
 * 这里只定义商品最基础的信息。此处的价格，是定义在商品级别的价格，针对sku的价格在{@link SkuViewCommand}中定义.
 * 除此之外，还会根据这两个价格构造{@link PriceViewCommand}用于初始进入pdp时价格的显示。
 * </p>
 * 
 * @see com.baozun.nebula.web.controller.product.viewcommand.SkuViewCommand
 * @see com.baozun.nebula.web.controller.product.viewcommand.PriceViewCommand
 */
public class ItemBaseInfoViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = 6763136630445642478L;

	/** 商品id. */
    private Long id;

    /** 商品code. */
    private String code;
    
    /** 销售价 */
	private BigDecimal salePrice;

	/** 吊牌价(原单价) */
	private BigDecimal listPrice;

    /** 商品名称. */
    private String title;

    /** 副标题. */
    private String subTitle;
    
    /** 商品概述 . */
	private String sketch;

    /** 商品详细描述. */
    private String description;
    
    /** 商品类型(0代表赠品, 1代表主卖品). */
	private Integer type;
	
	/** 分组style */
	private String style;
	
	/** 商品激活开始时间(用于定时上架的商品). */
	private Date activeBeginTime;
	
    /** seo搜索描述. */
    private String seoDescription;

    /** seo搜索关键字. */
    private String seoKeywords;

    /** seoTitle. */
    private String seoTitle;

    /**
     * 生命周期.
     * 
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4">
     * <tr style="background-color:#ccccff">
     * <th align="left">字段</th>
     * <th align="left">说明</th>
     * </tr>
     * <tr valign="top">
     * <td>0</td>
     * <td>下架</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>1</td>
     * <td>上架</td>
     * </tr>
     * <tr valign="top">
     * <td>2</td>
     * <td>逻辑删除(很少使用)</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>3</td>
     * <td>新建</td>
     * </tr>
     * </table>
     * </blockquote>
     */
    private Integer lifecycle;


    /**
     * 获得 商品id.
     *
     * @return the id
     */
    public Long getId(){
        return id;
    }

    /**
     * 设置 商品id.
     *
     * @param id
     *            the id to set
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 商品code.
     *
     * @return the code
     */
    public String getCode(){
        return code;
    }

    /**
     * 设置 商品code.
     *
     * @param code
     *            the code to set
     */
    public void setCode(String code){
        this.code = code;
    }

    public String getSketch() {
		return sketch;
	}

	public void setSketch(String sketch) {
		this.sketch = sketch;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Date getActiveBeginTime() {
		return activeBeginTime;
	}

	public void setActiveBeginTime(Date activeBeginTime) {
		this.activeBeginTime = activeBeginTime;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	/**
     * 获得 商品名字,这个名字是从 xxx表的xxx列中取的.
     *
     * @return the title
     */
    public String getTitle(){
        return title;
    }

    /**
     * 设置 商品名字,这个名字是从 xxx表的xxx列中取的.
     *
     * @param title
     *            the title to set
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * 获得 副标题.
     *
     * @return the subTitle
     */
    public String getSubTitle(){
        return subTitle;
    }

    /**
     * 设置 副标题.
     *
     * @param subTitle
     *            the subTitle to set
     */
    public void setSubTitle(String subTitle){
        this.subTitle = subTitle;
    }

    /**
     * 获得 商品详细描述.
     *
     * @return the description
     */
    public String getDescription(){
        return description;
    }

    /**
     * 设置 商品详细描述.
     *
     * @param description
     *            the description to set
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * 获得 seo搜索描述.
     *
     * @return the seoDescription
     */
    public String getSeoDescription(){
        return seoDescription;
    }

    /**
     * 设置 seo搜索描述.
     *
     * @param seoDescription
     *            the seoDescription to set
     */
    public void setSeoDescription(String seoDescription){
        this.seoDescription = seoDescription;
    }

    /**
     * 获得 seo搜索关键字.
     *
     * @return the seoKeywords
     */
    public String getSeoKeywords(){
        return seoKeywords;
    }

    /**
     * 设置 seo搜索关键字.
     *
     * @param seoKeywords
     *            the seoKeywords to set
     */
    public void setSeoKeywords(String seoKeywords){
        this.seoKeywords = seoKeywords;
    }

    /**
     * 获得 seoTitle.
     *
     * @return the seoTitle
     */
    public String getSeoTitle(){
        return seoTitle;
    }

    /**
     * 设置 seoTitle.
     *
     * @param seoTitle
     *            the seoTitle to set
     */
    public void setSeoTitle(String seoTitle){
        this.seoTitle = seoTitle;
    }

    /**
     * 获得 生命周期.
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4">
     * <tr style="background-color:#ccccff">
     * <th align="left">字段</th>
     * <th align="left">说明</th>
     * </tr>
     * <tr valign="top">
     * <td>0</td>
     * <td>下架</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>1</td>
     * <td>上架</td>
     * </tr>
     * <tr valign="top">
     * <td>2</td>
     * <td>逻辑删除(很少使用)</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>3</td>
     * <td>新建</td>
     * </tr>
     * </table>
     * </blockquote>
     * 
     * @return the lifecycle
     */
    public Integer getLifecycle(){
        return lifecycle;
    }

    /**
     * 设置 生命周期.
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4">
     * <tr style="background-color:#ccccff">
     * <th align="left">字段</th>
     * <th align="left">说明</th>
     * </tr>
     * <tr valign="top">
     * <td>0</td>
     * <td>下架</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>1</td>
     * <td>上架</td>
     * </tr>
     * <tr valign="top">
     * <td>2</td>
     * <td>逻辑删除(很少使用)</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>3</td>
     * <td>新建</td>
     * </tr>
     * </table>
     * </blockquote>
     * 
     * @param lifecycle
     *            the lifecycle to set
     */
    public void setLifecycle(Integer lifecycle){
        this.lifecycle = lifecycle;
    }



}
