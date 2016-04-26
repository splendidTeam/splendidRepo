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

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 店铺相关信息.
 *
 * @author feilong
 * @version 5.3.1 2016年4月25日 下午1:44:33
 * @see com.baozun.nebula.model.baseinfo.Shop
 * @see com.baozun.nebula.model.auth.Organization
 * @since 5.3.1
 */
public class ShopSubViewCommand extends BaseViewCommand{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5228786808770368403L;

    /** 店铺的id. */
    private Long              id;

    /**
     * 店铺的名称,see {@link com.baozun.nebula.model.auth.Organization#getName()}.
     */
    private String            name;

    /**
     * 店铺的code,see {@link com.baozun.nebula.model.auth.Organization#getCode()},冗余,通常在购物车页面不会用这个值.
     */
    private String            code;

    /** 状态,see {@link com.baozun.nebula.model.auth.Organization#getLifecycle()}. */
    private Integer           lifecycle;

    /**
     * 获得 店铺的id.
     *
     * @return the id
     */
    public Long getId(){
        return id;
    }

    /**
     * 设置 店铺的id.
     *
     * @param id
     *            the id to set
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 店铺的名称,see {@link com.baozun.nebula.model.auth.Organization#getName()}.
     *
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * 店铺的名称,see {@link com.baozun.nebula.model.auth.Organization#getName()}.
     *
     * @param name
     *            the name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * 店铺的code,see {@link com.baozun.nebula.model.auth.Organization#getCode()},冗余,通常在购物车页面不会用这个值.
     * 
     * @return the code
     */
    public String getCode(){
        return code;
    }

    /**
     * 店铺的code,see {@link com.baozun.nebula.model.auth.Organization#getCode()},冗余,通常在购物车页面不会用这个值.
     *
     * @param code
     *            the code to set
     */
    public void setCode(String code){
        this.code = code;
    }

    /**
     * 状态,see {@link com.baozun.nebula.model.auth.Organization#getLifecycle()}
     *
     * @return the lifecycle
     */
    public Integer getLifecycle(){
        return lifecycle;
    }

    /**
     * 状态,see {@link com.baozun.nebula.model.auth.Organization#getLifecycle()}
     *
     * @param lifecycle
     *            the lifecycle to set
     */
    public void setLifecycle(Integer lifecycle){
        this.lifecycle = lifecycle;
    }

}
