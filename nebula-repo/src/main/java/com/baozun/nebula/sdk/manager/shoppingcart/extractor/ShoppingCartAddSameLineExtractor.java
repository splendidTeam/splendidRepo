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
package com.baozun.nebula.sdk.manager.shoppingcart.extractor;

import java.util.List;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 购物车添加的时候相同行提取器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
public interface ShoppingCartAddSameLineExtractor{

    /**
     * 从主行list中提取相同行.
     * 
     * <h3>场景:</h3>
     * <blockquote>
     * 
     * <p>
     * 添加购物车行时,如果已经有相同的行(SKUid 相同,且包装信息相同),那么后续流程可能需要合并数量,而<b>不是无脑的增加行</b>;<br>
     * 如果找不到相同的行,那么后续流程可能就是新增一行了.
     * </p>
     * 
     * </blockquote>
     * 
     * @param mainLines
     *            已有的购物车主行
     * @param shoppingcartAddDetermineSameLineElements
     *            判断相同行的元素因子
     * @return 如果找不到相同行,那么返回null
     */
    ShoppingCartLineCommand extractor(List<ShoppingCartLineCommand> mainLines,ShoppingcartAddDetermineSameLineElements shoppingcartAddDetermineSameLineElements);
}
