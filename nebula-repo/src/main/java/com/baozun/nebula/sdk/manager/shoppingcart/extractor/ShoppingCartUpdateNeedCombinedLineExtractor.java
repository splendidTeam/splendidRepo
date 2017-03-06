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
 * 购物车修改的时候相同行提取器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
public interface ShoppingCartUpdateNeedCombinedLineExtractor{

    /**
     * 从主行list中查找相同行.
     * 
     * <p>
     * <b>场景:</b> 在修改购物车行的时候,如果修改了sku,那么需要看看现在的购物车里面有没有相同的,<br>
     * 如果有SKUid 相同,并且包装信息相同,那么后续流程可能需要合并数量,而不是无脑的修改);<br>
     * 如果找不到相同的行,那么后续流程可能就是修改数据了.
     * </p>
     * 
     * <h3>判断的原则:</h3>
     * <blockquote>
     * <p>
     * 在购物车行 shoppingCartLineCommandList 里面查找相同 skuId, 且lineGroup 相同, 且relatedItemId 相同,且packageInfoFormList 相同,且不是当前的currentLineId
     * </p>
     * </blockquote>
     *
     * @param shoppingCartLineCommandList
     * @param shoppingcartUpdateDetermineSameLineElements
     *            判定相同line 的元素
     * @return 如果找不到相同行,那么返回null
     */
    ShoppingCartLineCommand extractor(List<ShoppingCartLineCommand> shoppingCartLineCommandList,ShoppingcartUpdateDetermineSameLineElements shoppingcartUpdateDetermineSameLineElements);
}
