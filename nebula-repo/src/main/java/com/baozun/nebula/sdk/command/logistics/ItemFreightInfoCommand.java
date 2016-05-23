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
package com.baozun.nebula.sdk.command.logistics;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;

/**
 * @author Tianlong.Zhang
 *
 */
public class ItemFreightInfoCommand extends BaseModel implements Command{

    private static final long serialVersionUID = -5355240422147586451L;

    /**
     * 商品Id
     */
    private Long              itemId;

    /**
     * 该商品的数量（如果是按件计费的话）
     */
    private Integer           count;

    /**
     * 单个商品重量（如果是按照重量计费）
     */
    private Double            weight;

    /**
     * @return the itemId
     */
    public Long getItemId(){
        return itemId;
    }

    /**
     * @param itemId
     *            the itemId to set
     */
    public void setItemId(Long itemId){
        this.itemId = itemId;
    }

    /**
     * @return the count
     */
    public Integer getCount(){
        return count;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setCount(Integer count){
        this.count = count;
    }

    public void setWeight(Double weight){
        this.weight = weight;
    }

    public Double getWeight(){
        return weight;
    }

}
