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
package com.baozun.nebula.manager.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.product.BundleCommand;
import com.baozun.nebula.command.product.BundleCommand.BundleStatus;
import com.baozun.nebula.command.product.BundleElementCommand;
import com.baozun.nebula.command.product.BundleItemCommand;
import com.baozun.nebula.command.product.BundleSkuCommand;
import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.dao.product.BundleDao;
import com.baozun.nebula.dao.product.BundleElementDao;
import com.baozun.nebula.dao.product.BundleSkuDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.SdkSkuInventoryDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.model.bundle.Bundle;
import com.baozun.nebula.model.bundle.BundleElement;
import com.baozun.nebula.model.bundle.BundleSku;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.product.SkuInventory;
import com.baozun.nebula.web.command.BundleValidateResult;
import com.feilong.core.Validator;
import com.feilong.tools.jsonlib.JsonUtil;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * 
 * @author zhaojun.fang
 * @date : 2016年8月26日15:52
 *
 */

@Transactional
@Service
public class NebulaGroupManagerImpl implements NebulaGroupManager {
	private static final Logger LOG = LoggerFactory.getLogger(NebulaGroupManagerImpl.class);

	@Autowired
	private BundleSkuDao bundleSkuDao;

	@Autowired
	private BundleDao bundleDao;

	@Autowired
	private BundleElementDao bundleElementDao;

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private SdkSkuInventoryDao sdkSkuInventoryDao;

	@Autowired
	private CacheManager cacheManager;

	@Override
	@Transactional(readOnly = true)
	public List<BundleCommand> findGroupCommandByMainItemId(Long itemId, Boolean flag) {

		List<BundleCommand> result = null;
		
		List<Long> bundleItemIds = bundleDao.findBundleItemIdByMainItemId(itemId);
		if (Validator.isNotNullOrEmpty(bundleItemIds)) {
			result = new ArrayList<BundleCommand>();
			for (Long bundleItemId : bundleItemIds) {
				result.add(getBundleByGroupItemId(bundleItemId, flag, false));
			}
		}

		return result;
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<BundleCommand> findGroupCommandByMainStyle(String style, Boolean flag) {
		
		List<BundleCommand> result = null;
		
		List<Long> bundleItemIds = bundleDao.findBundleItemIdByMainStyle(style);
		if (Validator.isNotNullOrEmpty(bundleItemIds)) {
			result = new ArrayList<BundleCommand>();
			for (Long bundleItemId : bundleItemIds) {
				result.add(getBundleByGroupItemId(bundleItemId, flag, false));
			}
		}

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public BundleCommand findBundleCommandByGroupItemCode(String groupItemCode, Boolean flag) {
		ItemCommand itemCommand = itemDao.findItemCommandByCode(groupItemCode);
		Long itemId = itemCommand.getId();
		return getBundleByGroupItemId(itemId, flag, false);
	}

	@Override
	@Transactional(readOnly = true)
	public Pagination<BundleCommand> findGroupCommandByPage(Page page, Sort[] sorts, Boolean flag) {
		LOG.debug("paramater : page [{}] , sort [{}] , flag [{}]  , {}", JsonUtil.format(page), JsonUtil.format(sorts),
				flag, new Date());
		Pagination<BundleCommand> pagination = bundleDao.findBundlesByPage(page, sorts);
		List<BundleCommand> bundles = pagination.getItems();
		if (Validator.isNotNullOrEmpty(bundles)) {
			// 2 填充bundleCommand的基本信息
			fillBundleCommandList(bundles);
			if (flag) {
				// 3如果group中的所有element失效，那么就踢掉该group
				LOG.debug("start delete invalid bundled preparation. groups size : [{}]", bundles.size());
				removeInvalidBundleInfo(bundles);
				LOG.debug("end remove invalid bundled . groups size : [{}]", bundles == null ? 0 : bundles.size());
			}
		} else {
			LOG.error("find bundles is null");
			LOG.error("parametar : page {}  sorts{}  [{}]", JsonUtil.format(page), JsonUtil.format(sorts), new Date());
		}
		pagination.setItems(bundles);
		LOG.debug("pagination : {} , current time : {}", JsonUtil.format(pagination), new Date());
		return pagination;
	}

	@Override
	@Transactional(readOnly = true)
	public BundleValidateResult validateGroup(Long groupItemId, List<Long> skuIds, int quantity) {
		LOG.debug("[VALIDATE_BUNDLE] paramater : bundleItemId={} , skuIds={} , quantity={} , [{}]", groupItemId,
				JsonUtil.format(skuIds), quantity, new Date());
		
		BundleValidateResult result = null;
		
		BundleCommand bundleCommand = getBundleByGroupItemId(groupItemId, false, true);
		if(bundleCommand != null) {
			result = validateBundle(bundleCommand, skuIds, quantity);
		} else {
			LOG.debug("[VALIDATE_BUNDLE] bundle is null: bundleItemId={}", groupItemId);
			result = new BundleValidateResult(BundleStatus.BUNDLE_NOT_EXIST.getStatus(), groupItemId, null, null);
		}

		// 如果校验结果有任何异常，刷新group缓存。
		if (BundleStatus.BUNDLE_CAN_SALE.getStatus() != result.getType()) {
			LOG.debug("[VALIDATE_BUNDLE] bundle validate failed, refresh cache: cache-key=\"{}\"", CacheKeyConstant.BUNDLE_CACHE_KEY.concat(" ").concat(String.valueOf(groupItemId)));
			try {
				// 删除以group类商品id为key的缓存
				cacheManager.removeMapValue(CacheKeyConstant.BUNDLE_CACHE_KEY, String.valueOf(groupItemId));
			} catch (Exception e) {
				LOG.error("remove group from cache error: bundleItemId=" + groupItemId, e);
			}
		}

		return result;
	}

	/**
	 * 校验group选中的一个sku组合是否可以购买
	 * <ul>
	 * <li>group本身是一个商品,校验该商品是否上架状态</li>
	 * <li>购买的sku是否是上架状态</li>
	 * <li>购买的sku对应的商品是否是上架状态</li>
	 * <li>购买的group是否库存足够</li>
	 * <li>购买的sku是否库存足够</li>
	 * </ul>
	 */
	private BundleValidateResult validateBundle(BundleCommand bundleCommand, List<Long> skuIds, int quantity) {

		BundleValidateResult result = new BundleValidateResult();
		result.setType(BundleStatus.BUNDLE_CAN_SALE.getStatus());
		
		// 1 ,=============group验证是否上架校验================
		if (!validateBundleLifecycle(bundleCommand, result)) {
			return result;
		}

		// 根据groupId查询所有的skuId
		Long bundleId = bundleCommand.getId();
		Long bundleItemId = bundleCommand.getItemId();
		List<BundleSku> bundleSkus = bundleSkuDao.findByBundleId(bundleId);
		Map<Long, BundleSku> bundleSkuMap = skuIdToBundleSku(bundleSkus);
		for(Long skuId : skuIds) {
			Sku sku = skuDao.findSkuById(skuId);
			// 如果sku未上架
			if(!Item.LIFECYCLE_ENABLE.equals(sku.getLifecycle())) {
				LOG.debug("the sku lifecycle not equal '" + Item.LIFECYCLE_ENABLE + "': skuId={},lifecycle={} [{}]", skuId, sku.getLifecycle(), new Date());
				buildValidateResult(result, BundleStatus.BUNDLE_SKU_CANNOT_SALE.getStatus(), bundleItemId, sku.getItemId(), skuId);
				return result;
			}
			
			// 如果sku没有参与捆绑装
			if(!bundleSkuMap.containsKey(skuId)) {
				LOG.debug("the sku is not in bundle: skuId={},bundleId={} [{}]", skuId, bundleId, new Date());
				buildValidateResult(result, BundleStatus.BUNDLE_SKU_CANNOT_SALE.getStatus(), bundleItemId, sku.getItemId(), skuId);
				return result;
			}
			
			Item item = itemDao.findItemById(sku.getItemId());
			// 如果商品未上架
			if(!Item.LIFECYCLE_ENABLE.equals(item.getLifecycle())) {
				LOG.debug("the item lifecycle not equal '" + Item.LIFECYCLE_ENABLE + "': itemId={},lifecycle={} [{}]", item.getId(), item.getLifecycle(), new Date());
				buildValidateResult(result, BundleStatus.BUNDLE_SKU_CANNOT_SALE.getStatus(), bundleItemId, item.getId(), null);
				return result;
			}
			
			// 校验库存
			if(!validateBundleInventory(bundleCommand, sku, quantity, result)) {
				return result;
			}
		}

		return result;
	}

	/**
	 * 校验group本身的lifecycle是否上架
	 * 
	 * @param command
	 * @param result
	 * @return
	 */
	private boolean validateBundleLifecycle(BundleCommand command, BundleValidateResult result) {
		
		Integer lifecycle = command.getLifecycle();
		
		if (Item.LIFECYCLE_DELETED.equals(lifecycle)) {// bundle不存在
			buildValidateResult(result, BundleStatus.BUNDLE_NOT_EXIST.getStatus(), command.getId(), null, null);
			LOG.debug("the bundle lifecycle equal '" + Item.LIFECYCLE_DELETED + "' meanings bundle has deleted: [{}]",
					"bundleItem.getLifecycle():" + lifecycle);
			return false;
		}
		if (Item.LIFECYCLE_UNACTIVE.equals(lifecycle)) {// bundle未上架
			buildValidateResult(result, BundleStatus.BUNDLE_NOT_PUTAWAY.getStatus(), command.getId(), null, null);
			LOG.debug("the bundle lifecycle equal '" + Item.LIFECYCLE_UNACTIVE + "' meanings bundle has not Putaway: [{}]",
					"bundleItem.getLifecycle():" + lifecycle);
			return false;
		}
		if (Item.LIFECYCLE_DISABLE.equals(lifecycle)) {// bundle已下架
			buildValidateResult(result, BundleStatus.BUNDLE_SOLD_OUT.getStatus(), command.getId(), null, null);
			LOG.debug("the bundle lifecycle equal '" + Item.LIFECYCLE_DISABLE + "' meanings bundle has sold out: [{}]",
					"bundleItem.getLifecycle():" + lifecycle);
			return false;
		}
		
		return true;
	}

	/**
	 * key : skuId , value : BundleSku
	 * 
	 * @param bundleSkus
	 * @return
	 */
	private Map<Long, BundleSku> skuIdToBundleSku(List<BundleSku> bundleSkus) {
		Map<Long, BundleSku> skuIdToItemId = new HashMap<Long, BundleSku>();
		for (BundleSku sku : bundleSkus) {
			if (!skuIdToItemId.containsKey(sku.getSkuId())) {
				skuIdToItemId.put(sku.getSkuId(), sku);
			}
		}
		return skuIdToItemId;
	}

	/**
	 * 构建校验结果对象
	 * 
	 * @return
	 */
	private void buildValidateResult(BundleValidateResult result, int type, Long bundleItemId, Long itemId, Long skuId) {
		result.setBundleItemId(bundleItemId);
		result.setItemId(itemId);
		result.setSkuId(skuId);
		result.setType(type);
	}

	/**
	 * <h3>剔除掉无效的group</h3>
	 * <ul>
	 * <li>如果group中的某个商品不是上架状态,那么该group就应该移除掉</li>
	 * </ul>
	 * 
	 * @param bundles
	 */
	private void removeInvalidBundleInfo(List<BundleCommand> bundles) {
		Iterator<BundleCommand> iterator = bundles.iterator();
		while (iterator.hasNext()) {
			BundleCommand bundle = iterator.next();

			if (needRemoveInvalidBundle(bundle)) {
				LOG.debug("***********************************");
				LOG.debug("current bundle invailed , bundleId [{}] current time : [{}]", bundle.getId(), new Date());
				LOG.debug("***********************************");
				iterator.remove();
			}
		}
	}

	/**
	 * <h3>校验是否需要删除group</h3>
	 * <p>
	 * 校验的范围如下 ：
	 * </p>
	 * <ul>
	 * <ol>
	 * <li>group本身的商品就失效</li>
	 * <li></li>
	 * </ol>
	 * </ul>
	 * <h3>注意 ： 该方法不会校验库存的信息</h3>
	 * 
	 * @return 返回结果 布尔类型
	 *         <ul>
	 *         <li>true : 需要删除</li>
	 *         <li>false : 不需要删除</li>
	 *         </ul>
	 * @param bundle
	 * @return
	 */
	private boolean needRemoveInvalidBundle(BundleCommand bundle) {
		List<BundleElementCommand> bundleElementCommands = bundle.getBundleElementCommands();
		if (!Item.LIFECYCLE_ENABLE.equals(bundle.getLifecycle())|| !isEnabled(bundleElementCommands)) {
			return true;
		}
		return false;
	}

	/**
	 * <h3>校验group是否有效</h3>
	 * <p>
	 * 校验的范围如下 ：
	 * </p>
	 * <ul>
	 * <ol>
	 * <li>只要有一个element中的商品还有效 ,group就有效</li>
	 * </ol>
	 * </ul>
	 * <h3>注意 ： 该方法不会校验库存的信息</h3>
	 * 
	 * @return 返回结果 布尔类型
	 *         <ul>
	 *         <li>true : 有效</li>
	 *         <li>false : 失效</li>
	 *         </ul>
	 */
	private boolean isEnabled(List<BundleElementCommand> bundleElementCommands) {
		Boolean removeFlag = Boolean.TRUE;

		for (BundleElementCommand bundleElementCommand : bundleElementCommands) {
			removeFlag = validateBundleElement(bundleElementCommand);
			if (removeFlag) {
				break;
			}
		}
		return removeFlag;
	}

	/**
	 * <p>
	 * 校验的步骤 ：
	 * </p>
	 * <ul>
	 * <ol>
	 * <li>踢掉所有不是上架状态的商品</li>
	 * <li>踢掉商品中所有不是上架状态的sku</li>
	 * <li>如果商品是上架状态,但是该商品没有一个上架的sku,那么该商品也需要踢掉</li>
	 * </ol>
	 * </ul>
	 * 
	 * @param bundleElementCommand
	 * @return
	 */
	private boolean validateBundleElement(BundleElementCommand bundleElementCommand) {

		List<BundleItemCommand> bundleItem = bundleElementCommand.getItems();

		Iterator<BundleItemCommand> iterator = bundleItem.iterator();
		while (iterator.hasNext()) {
			BundleItemCommand bundleItemCommand = iterator.next();
			// 1 踢掉所有不是上架状态的商品
			if (!Item.LIFECYCLE_ENABLE.equals(bundleItemCommand.getLifecycle())) {
				iterator.remove();
				continue;
			}
			// 2 踢掉商品中所有不是上架状态的sku
			List<BundleSkuCommand> skus = bundleItemCommand.getBundleSkus();
			Iterator<BundleSkuCommand> iterator2 = skus.iterator();
			while (iterator2.hasNext()) {
				BundleSkuCommand bundleSkuCommand = (BundleSkuCommand) iterator2.next();
				if (!Item.LIFECYCLE_ENABLE.equals(bundleSkuCommand.getLifeCycle())) {
					iterator2.remove();
					continue;
				}
			}
			// 3 如果商品是上架状态,但是该商品没有一个上架的sku,那么该商品也需要踢掉
			if (Validator.isNullOrEmpty(bundleItemCommand.getBundleSkus())) {
				iterator.remove();
				continue;
			}
		}

		if (Validator.isNullOrEmpty(bundleElementCommand.getItems())) {
			return false;
		}

		return true;
	}

	/**
	 * 分别给每个bundle填充其基本信息
	 * 
	 * @param bundles
	 *            ： bundle集合
	 */
	private void fillBundleCommandList(List<BundleCommand> bundles) {
		for (BundleCommand bundle : bundles) {
			fillBundleCommand(bundle);
		}

	}

	/**
	 * 为bundle填充信息
	 * 
	 * @param bundle
	 *            ： Bundle对象
	 */
	private void fillBundleCommand(BundleCommand bundle) {

		List<BundleElementCommand> bundleElementList = new ArrayList<BundleElementCommand>();

		List<BundleSku> skus = bundleSkuDao.findByBundleId(bundle.getId());
		// BundleSku集合按照bundleElementId分组 . key : bundleElementId ; value :
		// List<BundleSku>
		Map<Long, List<BundleSku>> map = new HashMap<Long, List<BundleSku>>();
		for (BundleSku sku : skus) {
			if (map.containsKey(sku.getBundleElementId())) {
				map.get(sku.getBundleElementId()).add(sku);
			} else {

				List<BundleSku> list = new ArrayList<BundleSku>();
				list.add(sku);
				map.put(sku.getBundleElementId(), list);

				BundleElementCommand element = new BundleElementCommand();
				element.setId(sku.getBundleElementId());
				bundleElementList.add(element);
			}
		}

		fillBundleElementInfo(bundleElementList, map, bundle);
		Item item = itemDao.findItemById(bundle.getItemId());
		if (Validator.isNullOrEmpty(item)) {
			LOG.error("find item is null by itemId : [{}] , bundleId : [{}] {}. so set item lifecycle is 2",
					bundle.getItemId(), bundle.getId(), new Date());
			throw new BusinessException("bundle item is null : [itemId=" + bundle.getItemId() + "]");
		}
		// 如果item == null 就设置为逻辑删除的状态 2
		bundle.setLifecycle(item == null ? Item.LIFECYCLE_DELETED : item.getLifecycle());

		bundle.setBundleElementCommands(bundleElementList);
	}

	/**
	 * 分别给每个BundleElement填充信息
	 * 
	 * @param bundleElementList
	 *            ：bundleElement列表
	 * @param map
	 *            ： BundleSku集合按照bundleElementId分组 . key : bundleElementId ;
	 *            value : List<BundleSku>
	 * 
	 */
	private void fillBundleElementInfo(List<BundleElementCommand> bundleElementList, Map<Long, List<BundleSku>> map,
			BundleCommand bundle) {

		Iterator<BundleElementCommand> iterator = bundleElementList.iterator();
		while (iterator.hasNext()) {
			BundleElementCommand bundleElementCommand = iterator.next();
			List<BundleSku> elementSkus = map.get(bundleElementCommand.getId());
			BundleElement bundleElement = bundleElementDao.findById(bundleElementCommand.getId());
			ConvertUtils.convertTwoObject(bundleElementCommand, bundleElement);
			// BundleSku按ItemId分组
			fillItems(bundleElementCommand, convertToItemMap(elementSkus), bundle);
		}
		// 按照 排序因子sortNo排序 升序
		sort(bundleElementList);
	}

	private void sort(List<BundleElementCommand> bundleElementList) {
		Collections.sort(bundleElementList, new Comparator<BundleElementCommand>() {

			@Override
			public int compare(BundleElementCommand o1, BundleElementCommand o2) {

				return o1.getSortNo().compareTo(o2.getSortNo());
			}
		});
	}

	/**
	 * BundleSku集合按照itemId分组 . key : itemId ; value : List<BundleSku>
	 * 
	 * @param elementSkus
	 * @return
	 */
	private Map<Long, List<BundleSku>> convertToItemMap(List<BundleSku> elementSkus) {
		Map<Long, List<BundleSku>> map = new HashMap<Long, List<BundleSku>>();
		for (BundleSku bundleSku : elementSkus) {
			if (map.containsKey(bundleSku.getItemId())) {
				map.get(bundleSku.getItemId()).add(bundleSku);
			} else {
				List<BundleSku> list = new ArrayList<BundleSku>();
				list.add(bundleSku);
				map.put(bundleSku.getItemId(), list);
			}
		}
		return map;
	}

	/**
	 * <ul>
	 * <li>填充BundleItemCommand :参考
	 * {@link com.baozun.nebula.web.command.bundle.BundleElementCommand#items}
	 * </li>
	 * </ul>
	 * 
	 * @param bundleElementCommand
	 *            : bundleElement对象
	 * @param elementSkus
	 *            : bundleElement包含的sku列表 key : itemId ; value : List<BundleSku>
	 */
	private void fillItems(BundleElementCommand bundleElementCommand, Map<Long, List<BundleSku>> elementSkus,
			BundleCommand bundle) {
		// 填充BundleItemCommand
		fillBundleItemCommand(bundleElementCommand, elementSkus, bundle);
	}

	/**
	 * 填充bundleElementCommand中的BundleItemCommand信息
	 * 
	 * @param bundleElementCommand
	 *            : bundleElement对象
	 * @param elementSkus
	 *            : bundleElement包含的sku列表
	 */
	private void fillBundleItemCommand(BundleElementCommand bundleElementCommand,
			Map<Long, List<BundleSku>> elementSkus, BundleCommand bundle) {
		List<BundleItemCommand> items = new ArrayList<BundleItemCommand>();
		Set<Long> keys = elementSkus.keySet();
		for (Long key : keys) {
			items.add(packagingBundleItemCommandInfo(key, elementSkus.get(key), bundle));
		}
		if (Validator.isNotNullOrEmpty(bundleElementCommand)) {
			bundleElementCommand.setItems(items);
		}
	}

	/**
	 * 封装BundleItemCommand信息
	 * 
	 * @param itemId
	 * @param skus
	 * @return
	 */
	private BundleItemCommand packagingBundleItemCommandInfo(Long itemId, List<BundleSku> skus, BundleCommand bundle) {

		Item item = itemDao.findItemById(itemId);
		if (Validator.isNullOrEmpty(item)) {
			LOG.error("get item is null by itemId : [{}] , bundleId : [{}] , {}. so set item lifecycle is 2", itemId,
					bundle.getId(), new Date());
			throw new BusinessException("item is null : [itemId=" + itemId + "]");
		}
		
		BundleItemCommand bundleItemCommand = new BundleItemCommand();
		bundleItemCommand.setLifecycle(item.getLifecycle());
		bundleItemCommand.setItemId(itemId);
		bundleItemCommand.setItemCode(item.getCode());
		List<BundleSkuCommand> skuCommands = packagingBundleSkuCommands(skus, bundle);
		bundleItemCommand.setBundleSkus(skuCommands);

		return bundleItemCommand;
	}

	/**
	 * 封装BundleSkuCommand信息
	 * 
	 * @param skus
	 * @param bundle
	 * @return
	 */
	private List<BundleSkuCommand> packagingBundleSkuCommands(List<BundleSku> skus, Bundle bundle) {
		List<BundleSkuCommand> bundleSkus = new ArrayList<BundleSkuCommand>();
		LOG.debug("bundle price type is {} , availableQty is {}  , syncWithInv is {} , bundleId [{}] , {}",
				bundle.getPriceType(), bundle.getAvailableQty(), bundle.getSyncWithInv(), bundle.getId(), new Date());
		for (BundleSku sku : skus) {
			BundleSkuCommand skuCommand = new BundleSkuCommand();

			ConvertUtils.convertTwoObject(skuCommand, sku);

			Sku skuu = skuDao.findSkuById(sku.getSkuId());
			if (Validator.isNullOrEmpty(skuu)) {
				LOG.error("get Sku is null by skuId : [{}] {}. ", sku.getSkuId(), new Date());
				continue;
			}
			skuCommand.setProperties(skuu.getProperties());
			skuCommand.setExtentionCode(skuu.getOutid());
			skuCommand.setLifeCycle(skuu.getLifecycle());

			// 定制价格 一口价（）
			if (bundle.getPriceType().intValue() == Bundle.PRICE_TYPE_CUSTOMPRICE
					|| bundle.getPriceType().intValue() == Bundle.PRICE_TYPE_FIXEDPRICE) {
				skuCommand.setSalesPrice(sku.getSalesPrice());
			}
			// 按照实际价格
			if (bundle.getPriceType().intValue() == Bundle.PRICE_TYPE_REALPRICE) {
				skuCommand.setSalesPrice(skuu.getSalePrice());
			}
			skuCommand.setOriginalSalesPrice(skuu.getSalePrice());
			skuCommand.setListPrice(skuu.getListPrice());

			Integer availableQty = bundle.getAvailableQty();
			// 如果捆绑装单独维护了库存
			if (availableQty != null) {
				// 如果不需要同步扣减单品库存 ,那么就以捆绑装设置的库存为准；否则取捆绑装库存与sku实际可用库存的最小值
				if (!bundle.getSyncWithInv()) {
					skuCommand.setQuantity(availableQty);
				} else {
					SkuInventory inventory = sdkSkuInventoryDao.findSkuInventoryByExtentionCode(skuu.getOutid());
					int qty = 0;
					if (inventory != null && inventory.getAvailableQty() != null) {
						qty = inventory.getAvailableQty();
					}
					LOG.debug(
							"Math.min(availableQty, qty) : availableQty [{}] , qty [{}] ,  min : {} , skuId [{}] , bundleId [{}],{}",
							availableQty, qty, Math.min(availableQty, qty), sku.getSkuId(), bundle.getId(), new Date());
					skuCommand.setQuantity(Math.min(availableQty, qty));
				}
			} else {
				SkuInventory inventory = sdkSkuInventoryDao.findSkuInventoryByExtentionCode(skuu.getOutid());
				skuCommand.setQuantity(inventory == null ? 0 : inventory.getAvailableQty());
			}

			bundleSkus.add(skuCommand);
		}

		return bundleSkus;
	}

	/**
	 * 根据组商品的商品id查询捆绑装
	 * @param groupItemId 商品id
	 * @param isValidate 是否校验捆绑类商品的可用性
	 * @param isIgnoreCache 是否忽略缓存，为true时直接从数据库查询，false时将会尝试从缓存中加载数据
	 * @return
	 */
	private BundleCommand getBundleByGroupItemId(Long groupItemId, boolean isValidate, boolean isIgnoreCache) {
		
		LOG.debug("[GET_BUNDLE_BY_BUNDLE_ITEM_ID] bundleItemId={}", groupItemId);
		
		// 两种情况不从缓存中取数据也不更新缓存：
		// 1.明确指定忽略缓存的
		// 2.不校验捆绑类商品可用性的
		if(!isIgnoreCache && isValidate){
			// 如果缓存中存在，直接返回
			try {
				LOG.debug("[GET_BUNDLE_BY_BUNDLE_ITEM_ID] get bundle from cache: cache-key=\"{}\"", CacheKeyConstant.BUNDLE_CACHE_KEY.concat(" ").concat(String.valueOf(groupItemId)));
				BundleCommand result = (BundleCommand) cacheManager.getMapObject(CacheKeyConstant.BUNDLE_CACHE_KEY,
						String.valueOf(groupItemId));
				if (result != null) {
					LOG.debug("[GET_BUNDLE_BY_BUNDLE_ITEM_ID] found bundle in cache: bundle={}", JsonUtil.format(result));
					return result;
				} else {
					LOG.debug("[GET_BUNDLE_BY_BUNDLE_ITEM_ID] not found bundle in cache, will query from database.");
				}
			} catch (Exception e) {
				LOG.error("get bundle from cache error: cache-key=\"" + CacheKeyConstant.BUNDLE_CACHE_KEY.concat(" ").concat(String.valueOf(groupItemId)) + "\"", e);
			}
		}

		BundleCommand bundle = bundleDao.findBundleByBundleItemId(groupItemId, isValidate ? Item.LIFECYCLE_ENABLE : null);

		if (Validator.isNullOrEmpty(bundle)) {
			LOG.debug("[GET_BUNDLE_BY_BUNDLE_ITEM_ID] bundle is null: bundleItemId={}", groupItemId);
			throw new BusinessException("bundle extension information for item (itemId = '" + groupItemId + "') is null!");
		}
		
		fillBundleCommand(bundle);
		
		if (isValidate && needRemoveInvalidBundle(bundle)) {
			// 如果group中的所有商品失效，那么就踢掉该group
			LOG.debug("[GET_BUNDLE_BY_BUNDLE_ITEM_ID] the bundle invalid, so it removed: bundleItemId={}", groupItemId);
			return null;
		}
//		LOG.debug("[GET_BUNDLE_BY_BUNDLE_ITEM_ID] found bundle in database: bundle={}", JsonUtil.format(bundle));

		if(!isIgnoreCache && isValidate){
			// 添加到缓存中
			try {
				LOG.debug("[GET_BUNDLE_BY_BUNDLE_ITEM_ID] put bundle to cache: cache-key=\"{}\"", CacheKeyConstant.BUNDLE_CACHE_KEY.concat(" ").concat(String.valueOf(groupItemId)));
				cacheManager.setMapObject(CacheKeyConstant.BUNDLE_CACHE_KEY, String.valueOf(groupItemId), bundle,
						CacheKeyConstant.BUNDLE_CACHE_TIME);
			} catch (Exception e) {
				LOG.error("put bundle to cache error: cache-key=\"" + CacheKeyConstant.BUNDLE_CACHE_KEY.concat(" ").concat(String.valueOf(groupItemId)) + "\"", e);
			}
		}

		return bundle;
	}
	
	/**
	 * group库存校验
	 */
	private boolean validateBundleInventory(BundleCommand bundleCommand, Sku sku, int quantity, BundleValidateResult result) {
		Long bundleItemId = bundleCommand.getItemId();
		Integer bundleQty = bundleCommand.getAvailableQty();
		// bundle设置了独立的库存
		if(bundleQty != null) {
			if(!bundleCommand.getSyncWithInv()){
				if(bundleQty < quantity) { 
					LOG.debug("[BUNDLE_INVENTORY_VALIDATE_FAILURE] the bundle inventory is not enough: bundleId={},bundleQty={},salesQuantity={}", bundleCommand.getId(), bundleQty, quantity);
					buildValidateResult(result, BundleStatus.BUNDLE_NO_INVENTORY.getStatus(), bundleItemId, null, null);
					return false;
				}
			} else {
				if(bundleQty < quantity) {
					LOG.debug("[BUNDLE_INVENTORY_VALIDATE_FAILURE] the bundle inventory is not enough: bundleId={},bundleQty={},salesQuantity={}", bundleCommand.getId(), bundleQty, quantity);
					buildValidateResult(result, BundleStatus.BUNDLE_NO_INVENTORY.getStatus(), bundleItemId, null, null);
					return false;
				} else {
					return validateSkuInventory(bundleItemId, sku, quantity, result);
				}
			}
			
		} else { 
			return validateSkuInventory(bundleItemId, sku, quantity, result);
		}
		
		return true;
	}
	
	/**
	 * group sku库存校验
	 */
	private boolean validateSkuInventory(Long groupItemId, Sku sku, int quantity, BundleValidateResult result) {
		SkuInventory skuInventory = sdkSkuInventoryDao.findSkuInventoryByExtentionCode(sku.getOutid());
		// 如果sku不存在库存记录，则默认该sku实际可用库存为0
		int inventory = skuInventory == null ? 0 : skuInventory.getAvailableQty();
		if(inventory < quantity) {
			LOG.debug("[BUNDLE_SKU_INVENTORY_VALIDATE_FAILURE] the sku inventory is not enough: skuId={},inventory={},salesQuantity={}", sku.getId(), inventory, quantity);
			buildValidateResult(result, BundleStatus.BUNDLE_SKU_NO_INVENTORY.getStatus(), groupItemId, sku.getItemId(), sku.getId());
			return false;
		}
		
		return true;
	}

}