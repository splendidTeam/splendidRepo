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
package com.baozun.nebula.manager.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemImageCommand;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.RecommandItemDao;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.RecommandItem;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.utils.Validator;

/**
 * @author Tianlong.Zhang
 * 
 */
@Transactional
@Service("itemRecommandManager")
public class ItemRecommandManagerImpl implements ItemRecommandManager {

	// 商品推荐数
	private static int			RECOMMAND_ITEM_SIZE	= 6;

	@Autowired
	private RecommandItemDao	recommandItemDao;

	@Autowired
	private ItemDao				itemDao;

	@Autowired
	private SdkItemManager		sdkItemManager;

	@Override
	public List<ItemCommand> getRecommandItemByTypeAndParam(Integer type, Long param, String imgType) {

		List<ItemCommand> itemCmdList = null;

		// 先找到符合条件的推荐商品Id
		List<RecommandItem> recList = recommandItemDao.findRecItemByTypeAndParam(type, param);

		itemCmdList = convertRecommandItem2ItemCommand(recList, imgType);

		return itemCmdList;
	}

	/**
	 * 
	 * @param recList
	 * @param type : 图片类型
	 * @return
	 */
	private List<ItemCommand> convertRecommandItem2ItemCommand(List<RecommandItem> recList, String type) {
		List<ItemCommand> resultList = null;
		List<ItemCommand> itemCmdList = null;

		if (Validator.isNotNullOrEmpty(recList)) {
			List<Long> recItemIdList = new ArrayList<Long>();

			for (RecommandItem recItem : recList) {
				Long recItemId = recItem.getItemId();
				recItemIdList.add(recItemId);
			}

			// 根据商品找到商品基本信息
			itemCmdList = sdkItemManager.findItemCommandByItemIds(recItemIdList);
			// List<Item> itemList = itemDao.findItemListByIds(recItemIdList);

			// picUrlMap key： itemId value：picUrl
			Map<Long, String> picUrlMap = getItemPicMap(recItemIdList, type);

			// 遍历 itemList，将item 转化为itemCommand ，同时设置imgUrl
			if (Validator.isNotNullOrEmpty(itemCmdList)) {
				resultList = new ArrayList<ItemCommand>(itemCmdList.size());
				// itemCmdList = new ArrayList<ItemCommand>();
				for (ItemCommand itemCmd : itemCmdList) {
					// ItemCommand ic = new ItemCommand();
					// ic = (ItemCommand) ConvertUtils.convertFromTarget(ic, item);
					String picUrl = picUrlMap.get(itemCmd.getId());
					// ic.setPicUrl(picUrl);
					// itemCmdList.add(ic);
					if (null != picUrl) {
						itemCmd.setPicUrl(picUrl);
					}

				}

				Set<Long> idSet = new HashSet<Long>();
				for (Long id : recItemIdList) {
					for (ItemCommand itemCmd : itemCmdList) {
						if (id.equals(itemCmd.getId()) && (!idSet.contains(itemCmd.getId()))) {
							resultList.add(itemCmd);
							idSet.add(itemCmd.getId());
						}
					}
				}
			}

		}

		return resultList;
	}

	private Map<Long, String> getItemPicMap(List<Long> itemIdList, String type) {

		// picUrlMap key： itemId value：picUrl
		Map<Long, String> picUrlMap = new HashMap<Long, String>();

		// 根据商品找到 对应的列表图
		List<ItemImageCommand> cmdList = sdkItemManager.findItemImagesByItemIds(itemIdList, type);

		if (Validator.isNotNullOrEmpty(cmdList)) {
			for (ItemImageCommand cmd : cmdList) {
				if (Validator.isNotNullOrEmpty(cmd)) {
					List<ItemImage> imgList = cmd.getItemIamgeList();

					if (Validator.isNotNullOrEmpty(imgList)) {
						Long itemId = cmd.getItemId();
						String imgStr = imgList.get(0).getPicUrl();
						// imgStr = sdkItemManager.convertItemImageWithDomain(imgStr);

						picUrlMap.put(itemId, imgStr);
					}
				}
			}

		}
		return picUrlMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.manager.product.ItemRecommandManager#getRecommandItemByItemIds(java.util.List)
	 */
	@Override
	public List<ItemCommand> getRecommandItemByItemIds(List<Long> itemIdList, String type) {

		// 搭配类型
		List<RecommandItem> recItemList = recommandItemDao.findRecItemByTypeAndParams(RecommandItem.TYPE_ITEM,
				itemIdList);

		/**
		 * 先取搭配类型 如果不够 6个 再取公共类型
		 */
		if ((Validator.isNullOrEmpty(recItemList))
				|| (Validator.isNotNullOrEmpty(recItemList) && recItemList.size() < RECOMMAND_ITEM_SIZE)) {
			List<RecommandItem> publicRecItemList = recommandItemDao.findRecItemByTypeAndParam(
					RecommandItem.TYPE_PUBLIC, RecommandItem.PARAM_SHOPPING_CART);
			recItemList.addAll(publicRecItemList);
		}

		List<ItemCommand> itemCmdList = convertRecommandItem2ItemCommand(recItemList, type);

		return itemCmdList;
	}

	@Override
	public List<ItemCommand> getRecommandItemByItemId(Long itemId, String type) {
		// 搭配类型
		List<RecommandItem> recItemList = recommandItemDao.findRecItemByTypeAndParam(RecommandItem.TYPE_ITEM, itemId);
		if(null != recItemList && recItemList.size() < 6 ){
			List<RecommandItem> pdpItemRecList = recommandItemDao.findRecItemByTypeAndParam(RecommandItem.TYPE_PUBLIC, RecommandItem.PARAM_PDP);
			recItemList.addAll(pdpItemRecList);
		}
		List<ItemCommand> itemCmdList = convertRecommandItem2ItemCommand(recItemList, type);
		return itemCmdList;
	}

	@Override
	public List<RecommandItem> findRecommandItemByTypeAndParam(Integer type, Long param) {
		return recommandItemDao.findRecItemByTypeAndParam(type, param);
	}

}
