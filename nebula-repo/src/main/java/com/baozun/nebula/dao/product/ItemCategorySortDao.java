/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Baozun. You
 * shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Baozun.
 * 
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 */
package com.baozun.nebula.dao.product;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.sdk.command.ItemSortCommand;

/**
 * 商品排序ItemCategorySortDao
 * 
 * 2014-2-10 下午5:11:38
 * 
 * @author <a href="xinyuan.guo@baozun.cn">郭馨元</a>
 * 
 */
public interface ItemCategorySortDao extends GenericEntityDao<ItemCategory, Long> {
	@NativeQuery(model = ItemSortCommand.class)
	Pagination<ItemSortCommand> findItemSortByQueryMapWithPage(Page page, Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	@NativeQuery(model = ItemCategory.class)
	ItemCategory findItemAbove(@QueryParam("sortNo") Integer sortNo, @QueryParam("idList") List<Long> categoryId);

	@NativeQuery(model = ItemCategory.class)
	ItemCategory findItemUnder(@QueryParam("sortNo") Integer sortNo, @QueryParam("idList") List<Long> categoryId);

	@NativeUpdate
	Integer deleteSort(@QueryParam("itemId") Long itemId, @QueryParam("categoryId") Long categoryId);

	@NativeQuery(model = ItemCategory.class)
	ItemCategory findItemBottom(@QueryParam("categoryId") Long categoryId);
}
