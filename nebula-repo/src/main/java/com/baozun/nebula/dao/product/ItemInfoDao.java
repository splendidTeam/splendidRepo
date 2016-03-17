/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
 */
package com.baozun.nebula.dao.product;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.rule.MiniItemAtomCommand;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.ItemInfoLang;

/**
 * 商品信息
 * 
 * @author xingyu.liu
 * 
 */
public interface ItemInfoDao extends GenericEntityDao<ItemInfo, Long> {

	/**
	 * 根据itemId查询ItemInfo
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = ItemInfo.class)
	ItemInfo findItemInfoByItemId(@QueryParam("itemId") Long itemId);

	/**
	 * 通过itemIds获取ItemInfo列表
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ItemInfo.class)
	List<ItemInfo> findItemInfoListByItemIds(@QueryParam("itemIds") List<Long> itemIds);

	/**
	 * 通过itemIds获取ItemInfo列表
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ItemCommand.class)
	List<ItemCommand> findItemCommandListByItemIds(@QueryParam("itemIds") List<Long> itemIds);
	
	@NativeQuery(model = ItemCommand.class)
	List<ItemCommand> findItemCommandListByItemIdsI18n(@QueryParam("itemIds") List<Long> itemIds,@QueryParam("lang")String lang);

	/**
	 * 获取所有ItemInfo列表
	 * 
	 * @return
	 */
	@NativeQuery(model = ItemInfo.class)
	List<ItemInfo> findAllItemInfoList();

	/**
	 * 通过ids获取ItemInfo列表
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = ItemInfo.class)
	List<ItemInfo> findItemInfoListByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 通过参数map获取ItemInfo列表
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ItemInfo.class)
	List<ItemInfo> findItemInfoListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 分页获取ItemInfo列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = ItemInfo.class)
	Pagination<ItemInfo> findItemInfoListByQueryMapWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap);

	/**
	 * 通过ids批量启用或禁用ItemInfo 设置lifecycle =0 或 1
	 * 
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableItemInfoByIds(@QueryParam("ids") List<Long> ids, @QueryParam("state") Integer state);

	/**
	 * 通过ids批量删除ItemInfo 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeItemInfoByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 通过参数map获取有效的ItemInfo列表 强制加上lifecycle =1
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = ItemInfo.class)
	List<ItemInfo> findEffectItemInfoListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 分页获取有效的ItemInfo列表 强制加上lifecycle =1
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = ItemInfo.class)
	Pagination<ItemInfo> findEffectItemInfoListByQueryMapWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap);

	/**
	 * 修改最后一次选择的属性的ID
	 * 
	 * @param lastSelectPropertyId
	 * @param itemId
	 * @return :Integer
	 * @date 2014-2-20 上午11:12:44
	 */
	@NativeUpdate
	Integer updateItemInfoLSPIdByItemId(@QueryParam("lastSelectPropertyId") Long lastSelectPropertyId, @QueryParam("itemId") Long itemId);

	/**
	 * 修改最后一次选择的属性值的ID
	 * 
	 * @param lastSelectPropertyId
	 * @param itemId
	 * @return :Integer
	 * @date 2014-2-20 上午11:12:44
	 */
	@NativeUpdate
	Integer updateItemInfoLSPVIdByItemId(@QueryParam("lastSelectPropertyValueId") Long lastSelectPropertyValueId, @QueryParam("itemId") Long itemId);

	/**
	 * 根据id列表查询简要的item信息
	 * 
	 * @param idList
	 * @return
	 */
	@NativeQuery(model = MiniItemAtomCommand.class, withGroupby = true)
	List<MiniItemAtomCommand> findMiniTagRuleCommandByIdList(@QueryParam("idList") List<Long> idList);

	/**
	 * 同步商品的价格(item级别)
	 * 
	 * @param salesPrice
	 * @param listPrice
	 * @param itemCode
	 */
	@NativeUpdate
	Integer syncItemPriceByCode(@QueryParam("salesPrice") BigDecimal salesPrice, @QueryParam("listPrice") BigDecimal listPrice, @QueryParam("itemCode") String itemCode);

	@NativeUpdate
	Integer updateActiveBeginTime(@QueryParam("idList") List<Long> idList, @QueryParam("activeBeginTime") Date activeBeginTime);

	/**
	 * 
	 * @author 何波
	 * @Description: 根据分类id 查询商品
	 * @param cateIds
	 * @return List<ItemInfo>
	 * @throws
	 */
	@NativeQuery(model = ItemInfo.class)
	List<ItemInfo> findItemInfosByCateIds(@QueryParam("cateIds") List<Long> cateIds);

	/**
	 * @author 何波
	 * @Description:根据itemIds查询ItemInfo
	 * @param itemIds
	 * @return
	 */
	@NativeQuery(model = ItemInfo.class)
	List<ItemInfo> findItemInfosByItemIds(@QueryParam("itemIds") List<Long> itemIds);

	/**
	 * 
	 * @author 何波
	 * @Description: 处理全场优惠价格设置
	 * @param cateIds
	 * @param itemIds
	 * @return List<ItemInfo>
	 * @throws
	 */
	@NativeQuery(model = ItemInfo.class)
	List<ItemInfo> findCallItemInfos(@QueryParam("cateIds") List<Long> cateIds, @QueryParam("itemIds") List<Long> itemIds);

	/**
	 * 
	 * @author 何波
	 * @Description: 根据skuid 查询商品信息
	 * @param itemIds
	 * @return List<ItemInfo>
	 * @throws
	 */
	@NativeQuery(model = ItemInfo.class)
	List<ItemInfo> findItemInfosBySkuids(@QueryParam("skuIds") List<Long> skuIds);
	

	/**
	 * 修改商品信息国际化
	 * @param id
	 * @param valueId
	 * @param value
	 * @return
	 */
	@NativeUpdate
	int updateItemInfoLang(@QueryParam Map<String, Object> params);
	
	@NativeQuery(model = ItemInfoLang.class)
	List<ItemInfoLang> findItemInfoLangList(@QueryParam("ids")List<Long> ids,@QueryParam("langs") List<String> langs);
	
	@NativeQuery(model = ItemInfoLang.class)
	ItemInfoLang findItemInfoLang(@QueryParam("id")Long id,@QueryParam("lang") String lang);

}
