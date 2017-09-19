/**
 * Copyright (c) 2013 Jumbomart All Rights Reserved.
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
package com.baozun.nebula.manager.product;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.manager.BaseManager;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * ItemSolrManager
 *
 * @author: shiyang.lv
 * @date: 2014年4月29日
 **/
public interface ItemSolrSettingManager extends BaseManager{

    /**
     * 分页查询Solr数据组装成ItemCommand
     * 
     * @param page
     * @param sorts
     * @param paraMap
     * @param shopId
     * @return
     */
    public Pagination<ItemCommand> findItemListByQueryMap(Page page,Sort[] sorts,Map<String, Object> paraMap,Long shopId);

    /**
     * 删除所有
     * 
     * @param shopId
     * @return
     */
    public Boolean deleteAll(Long shopId);

    /**
     * 重建所有
     * 
     * @param shopId
     * @return
     */
    public Boolean reRefreshAll(Long shopId);

    /**
     * 根据商品 编码集合批量操作索引(只刷上架商品的索引, 下架商品,新建商品是没有索引数据)
     * type = 1: 修改商品索引
     * type = 0: 删除商品索引
     * 
     * @param itemCodeList
     * @param shopId
     * @param type
     *            1: 修改商品索引 <br>
     *            0: 删除商品索引
     * @return
     */
    public Boolean batchOperationItemSolrIndex(List<String> itemCodeList,Long shopId,Integer type);
}
