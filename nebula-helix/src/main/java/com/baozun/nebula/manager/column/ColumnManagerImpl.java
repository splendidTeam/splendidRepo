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
package com.baozun.nebula.manager.column;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.ItemImageCommand;
import com.baozun.nebula.command.column.ColumnComponentCommand;
import com.baozun.nebula.command.column.ColumnModuleCommand;
import com.baozun.nebula.command.column.ColumnPageCommand;
import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.dao.column.ColumnPublishedDao;
import com.baozun.nebula.dao.product.CategoryDao;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.model.column.ColumnComponent;
import com.baozun.nebula.model.column.ColumnModule;
import com.baozun.nebula.model.column.ColumnPublished;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.manager.SdkColumnManager;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.utilities.common.Validator;

/**
 * @author Tianlong.Zhang
 * 
 */
@Service("columnManager")
@Transactional
public class ColumnManagerImpl implements ColumnManager {

	private static final Logger	log	= LoggerFactory.getLogger(ColumnManager.class);

	@Autowired
	private ColumnPublishedDao	columnPublishedDao;

	@Autowired
	private SdkColumnManager	sdkColumnManager;

	@Autowired
	private SdkItemManager		sdkItemManager;

	@Autowired
	private CategoryDao			categoryDao;

	@Autowired
	private CacheManager		cacheManager;

	@Override
	public ColumnPageCommand findColumnModuleMapByPageCode(String pageCode) {

		ColumnPageCommand result = sdkColumnManager.findColumnModuleMapByPageCode(pageCode);

		result = fillCmdWihtItemAndCategory(result);

		return result;
	}

	private ColumnPageCommand fillCmdWihtItemAndCategory(ColumnPageCommand result) {
		Map<String, ColumnModuleCommand> resultMap = result.getColumnModuleMap();
		if (Validator.isNotNullOrEmpty(resultMap)) {
			List<Long> categoryIds = new ArrayList<Long>();
			List<Long> itemIds = new ArrayList<Long>();

			for (String key : resultMap.keySet()) {
				ColumnModuleCommand moduleCommand = resultMap.get(key);
				Integer type = moduleCommand.getType();

				List<ColumnComponentCommand> components = moduleCommand.getComponentList();

				if (Validator.isNotNullOrEmpty(components)) {

					for (ColumnComponentCommand component : components) {
						Long targetId = component.getTargetId();

						// component.setImg(sdkItemManager.convertItemImageWithDomain(component.getImg()));
						if (ColumnModule.TYPE_CATEGORY.equals(type)) {
							categoryIds.add(targetId);
						} else if (ColumnModule.TYPE_ITEM.equals(type)) {
							itemIds.add(targetId);
						}

					}
				}
			}

			// 根据 categoryIds itemIds 查出 CategoryList 和ItemCommandList
			Long[] categoryIdArray = new Long[categoryIds.size()];
			List<Category> categroyList = categoryDao.findCategoryListByCategoryIds(categoryIds
					.toArray(categoryIdArray));

			List<ItemCommand> itemList = sdkItemManager.findItemCommandByItemIds(itemIds);
			itemList = sdkItemManager.fillItemRankAvg(itemList);

			// 将 CategoryList 和ItemCommandList 转化为 map
			Map<Long, Category> categoryMap = new HashMap<Long, Category>();
			Map<Long, ItemCommand> itemMap = new HashMap<Long, ItemCommand>();

			if (Validator.isNotNullOrEmpty(categroyList)) {
				for (Category c : categroyList) {
					Long key = c.getId();
					categoryMap.put(key, c);
				}
			}

			if (Validator.isNotNullOrEmpty(itemList)) {
				for (ItemCommand itemCommand : itemList) {
					Long key = itemCommand.getId();
					itemMap.put(key, itemCommand);
				}
			}

			// 将查出的 item 和 category 的值设置进去
			for (String key : resultMap.keySet()) {
				ColumnModuleCommand moduleCommand = resultMap.get(key);
				Integer type = moduleCommand.getType();

				List<ColumnComponentCommand> components = moduleCommand.getComponentList();

				Map<Long, String> picUrlMap = getItemPicMap(itemIds);

				if (Validator.isNotNullOrEmpty(components)) {

					for (ColumnComponentCommand component : components) {
						Long targetId = component.getTargetId();

						if (ColumnModule.TYPE_CATEGORY.equals(type)) {
							component.setCategory(categoryMap.get(targetId));
						} else if (ColumnModule.TYPE_ITEM.equals(type)) {
							ItemCommand item = itemMap.get(targetId);
							if(item !=  null){
								ItemCommand itemCommand = new ItemCommand();
								itemCommand = (ItemCommand) ConvertUtils.convertFromTarget(itemCommand, itemMap.get(targetId));
	
								if (itemCommand != null) {// 可能发生 itemMap.get(targetId) 的情况
									String picUrl = picUrlMap.get(itemCommand.getId());
									itemCommand.setPicUrl(picUrl);
									component.setItemCommand(itemCommand);
								}
							}
						}

					}
				}

			}

		}
		return result;
	}

	private Map<Long, String> getItemPicMap(List<Long> itemIdList) {

		// picUrlMap key： itemId value：picUrl
		Map<Long, String> picUrlMap = new HashMap<Long, String>();

		// 根据商品找到 对应的列表图
		List<ItemImageCommand> cmdList = sdkItemManager.findItemImagesByItemIds(itemIdList, ItemImage.IMG_TYPE_LIST);

		if (Validator.isNotNullOrEmpty(cmdList)) {
			for (ItemImageCommand cmd : cmdList) {
				if (Validator.isNotNullOrEmpty(cmd)) {
					List<ItemImage> imgList = cmd.getItemIamgeList();

					if (Validator.isNotNullOrEmpty(imgList)) {
						Long itemId = cmd.getItemId();
						String imgStr = imgList.get(0).getPicUrl();
						picUrlMap.put(itemId, imgStr);
					}
				}
			}

		}
		return picUrlMap;
	}

	@Override
	public ColumnPageCommand getPublishedPageByCode(String pageCode) {
//		ColumnPageCommand columnPageCommand = cacheManager.getMapObject(CacheKeyConstant.COLUMN_KEY, pageCode);
//		if(columnPageCommand != null){
//			return columnPageCommand;
//		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pageCode", pageCode);

 		List<ColumnModule> cmList = sdkColumnManager.findColumnModuleListByQueryMap(paramMap);

		Map<String, Integer> cmMap = new HashMap<String, Integer>();
		for (ColumnModule cm : cmList) {
			cmMap.put(cm.getCode(), cm.getType());
		}

		ColumnPageCommand pageCmd = new ColumnPageCommand();
		pageCmd.setCode(pageCode);
		Map<String, ColumnModuleCommand> columnModuleMap = new HashMap<String, ColumnModuleCommand>();

		List<ColumnPublished> publishList = columnPublishedDao.findColumnPublishedListByQueryMap(paramMap);
		ObjectMapper mapper = new ObjectMapper();
		if (Validator.isNotNullOrEmpty(publishList)) {
			for (ColumnPublished cp : publishList) {
				String moduleCode = cp.getModuleCode();
				String value = cp.getValue();
				List<ColumnComponent> ccList = null;

				List<ColumnComponentCommand> ccCmdList = new ArrayList<ColumnComponentCommand>();
				try {
					ccList = mapper.readValue(value, new TypeReference<List<ColumnComponent>>() {
					});
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}

				if (Validator.isNotNullOrEmpty(ccList)) {
					for (ColumnComponent cc : ccList) {
						ColumnComponentCommand ccCmd = new ColumnComponentCommand();
						ccCmd = (ColumnComponentCommand) ConvertUtils.convertFromTarget(ccCmd, cc);
						String ext = cc.getExt();
						if(StringUtils.isNotBlank(ext)){
							Map<String, Object> extMap = null;
							try {
								extMap = mapper.readValue(ext, new TypeReference<HashMap<String,Object>>(){});
							} catch (JsonParseException e) {
								log.error(e.getMessage(), e);
							} catch (JsonMappingException e) {
								log.error(e.getMessage(), e);
							} catch (IOException e) {
								log.error(e.getMessage(), e);
							}
							ccCmd.setExtMap(extMap);
						}
						ccCmdList.add(ccCmd);
					}
				}

				ColumnModuleCommand moduleCmd = new ColumnModuleCommand();
				moduleCmd.setCode(moduleCode);
				moduleCmd.setType(cmMap.get(cp.getModuleCode()));
				moduleCmd.setComponentList(ccCmdList);

				columnModuleMap.put(moduleCode, moduleCmd);
			}

			pageCmd.setColumnModuleMap(columnModuleMap);

			pageCmd = fillCmdWihtItemAndCategory(pageCmd);
			// 将数据加到缓存中
			cacheManager.setMapObject(CacheKeyConstant.COLUMN_KEY, pageCode, pageCmd, TimeInterval.SECONDS_PER_WEEK);
			return pageCmd;
		}

		return null;
	}

}
