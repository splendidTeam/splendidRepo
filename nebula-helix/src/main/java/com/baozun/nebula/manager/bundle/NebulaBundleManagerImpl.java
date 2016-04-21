package com.baozun.nebula.manager.bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.bundle.BundleCommand;
import com.baozun.nebula.command.bundle.BundleCommand.BundleStatus;
import com.baozun.nebula.command.bundle.BundleElementCommand;
import com.baozun.nebula.command.bundle.BundleItemCommand;
import com.baozun.nebula.command.bundle.BundleSkuCommand;
import com.baozun.nebula.dao.bundle.BundleDao;
import com.baozun.nebula.dao.bundle.BundleElementDao;
import com.baozun.nebula.dao.bundle.BundleSkuDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.SdkSkuInventoryDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.model.bundle.Bundle;
import com.baozun.nebula.model.bundle.BundleElement;
import com.baozun.nebula.model.bundle.BundleSku;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.product.SkuInventory;
import com.baozun.nebula.web.command.BundleValidateResult;
import com.feilong.tools.jsonlib.JsonUtil;

@Transactional
@Service
public class NebulaBundleManagerImpl implements NebulaBundleManager {
	private static final Logger LOG = LoggerFactory.getLogger(NebulaBundleManagerImpl.class);

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

	@Override
	@Transactional(readOnly = true)
	public List<BundleCommand> findBundleCommandByItemId(Long itemId) {

		// 1、 根据itemId,lifeCycle查询bundle （只需要查询出可售的bundle）
		List<BundleCommand> bundles = bundleDao.findBundlesByItemId(itemId, 1);
		if (bundles != null) {
			// 2 填充bundleCommand的基本信息
			fillBundleCommandList(bundles);
			// 3如果bundle中的某个商品失效，那么就踢掉该bundle
			removeInvalidBundle(bundles);
		} else {
			LOG.error("find bundles is null");
			LOG.error("itemId : [{}]  showMainElementFlag : [{}]  lifeCycle : [{}]", itemId, 1);
		}

		return bundles;
	}

	@Override
	@Transactional(readOnly = true)
	public BundleCommand findBundleCommandByBundleId(Long boundleId) {
		BundleCommand bundle = bundleDao.findBundlesById(boundleId, 1);
		fillBundleCommand(bundle);
		return bundle;
	}

	@Override
	@Transactional(readOnly = true)
	public BundleValidateResult validateBundle(Long bundleId,
			List<Long> skuIds, int quantity) {
		
		BundleValidateResult result = null;
		//查询bundle的所有相关信息
		BundleCommand command = bundleDao.findBundleById(bundleId);
		//bundle本身就是一个特殊的商品
		Item bundleItem = itemDao.findItemById(command.getItemId());
		//校验bundle
		isBundleEnough(bundleId,skuIds,quantity,result,command,bundleItem);
		
		return result;
	}
	
	/**
	 * 校验bundle
	 * @param bundleId
	 * @param skuIds
	 * @param quantity
	 * @param result
	 * @param command
	 * @param bundleItemInfo
	 */
	private void isBundleEnough(Long bundleId,List<Long> skuIds, int quantity,
			BundleValidateResult result,BundleCommand command,Item bundleItem){
		
		if(command == null || bundleItem == null){//bundle不存在
			result.setType(BundleStatus.BUNDLE_NOT_EXIST.getStatus());
			result.setBundleId(bundleId);
		}else{
			if(bundleItem.getLifecycle() == 2){//bundle不存在
				result.setType(BundleStatus.BUNDLE_NOT_EXIST.getStatus());
				result.setBundleId(bundleId);
			}else if(bundleItem.getLifecycle() == 3){//bundle未上架
				result.setType(BundleStatus.BUNDLE_NOT_PUTAWAY.getStatus());
				result.setBundleId(bundleId);
			}else if(bundleItem.getLifecycle() == 0){//bundle已下架
				result.setType(BundleStatus.BUNDLE_SOLD_OUT.getStatus());
				result.setBundleId(bundleId);
			}else if(bundleItem.getLifecycle() == 1){//=============bundle验证通过,bundle已上架================
				//根据bundleId查询所有的skuId
				List<BundleSku> bundleSkus = bundleSkuDao.findByBundleId(command.getId());
				//bundle的所有skuId
				Map<Long,Long> bundleSkusIdAndItemId = new HashMap<Long, Long>();
				
				if(bundleSkus.size() > 0){
					for (BundleSku bundleSku : bundleSkus) {
						bundleSkusIdAndItemId.put(bundleSku.getSkuId(),bundleSku.getItemId());
					}
				}
				
				if(bundleSkusIdAndItemId.size() > 0){
					if(command.getAvailableQty() == null){//只判断sku的库存足不足
						isSkuInventoryEnough(bundleId,skuIds,quantity,bundleSkusIdAndItemId,result);
					}else{
						if(command.getSyncWithInv()){//即要判断bundle的availableQty是否满足，又要判断每个单品是否满足
							if(command.getAvailableQty() < quantity){//bundle库存不足
								result.setType(BundleStatus.BUNDLE_NO_INVENTORY.getStatus());
								result.setBundleId(bundleId);
							}else{//判断sku的库存足不足
								isSkuInventoryEnough(bundleId,skuIds,quantity,bundleSkusIdAndItemId,result);
							}
						}else{//只要判断bundle的availableQty是否满足
							if(command.getAvailableQty() < quantity){//bundle库存不足
								result.setType(BundleStatus.BUNDLE_NO_INVENTORY.getStatus());
								result.setBundleId(bundleId);
							}else{//正常
								result.setType(BundleStatus.BUNDLE_CAN_SALE.getStatus());
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 判断sku的库存足不足
	 * @param bundleId
	 * @param skuIds
	 * @param quantity
	 * @param bundleSkusIdAndItemId
	 * @param result
	 */
	private void isSkuInventoryEnough(Long bundleId,List<Long> skuIds, 
			int quantity,Map<Long,Long> bundleSkusIdAndItemId,BundleValidateResult result){
		//判断bundle库存是否足
		boolean inventoryFlag = true;
		for(Long skuId : skuIds){
			if(bundleSkusIdAndItemId.containsKey(skuId)){
				Sku sku = skuDao.findSkuById(skuId);
				SkuInventory skuInventory = sdkSkuInventoryDao.findSkuInventoryByExtentionCode(sku.getOutid());
				if(sku != null && skuInventory != null){
					if(sku.getLifecycle() == 0 || sku.getLifecycle() == 2 || sku.getLifecycle() == 3){
						result.setType(BundleStatus.BUNDLE_ITEM_NOT_EXIST.getStatus());
						result.setSkuId(skuId);
						result.setItemId(bundleSkusIdAndItemId.get(skuId));
						result.setBundleId(bundleId);
						inventoryFlag = false;
					}else if(sku.getLifecycle() == 1){
						if(skuInventory.getAvailableQty() < quantity){
							result.setType(BundleStatus.BUNDLE_ITEM_NO_INVENTORY.getStatus());
							result.setSkuId(skuId);
							result.setItemId(bundleSkusIdAndItemId.get(skuId));
							result.setBundleId(bundleId);
							inventoryFlag = false;
						}
					}
				}else{
					result.setType(BundleStatus.BUNDLE_ITEM_NOT_EXIST.getStatus());
					result.setSkuId(skuId);
					result.setItemId(bundleSkusIdAndItemId.get(skuId));
					result.setBundleId(bundleId);
					inventoryFlag = false;
				}
			}else{
				result.setType(BundleStatus.BUNDLE_ITEM_NOT_EXIST.getStatus());
				result.setSkuId(skuId);
				result.setItemId(bundleSkusIdAndItemId.get(skuId));
				result.setBundleId(bundleId);
				inventoryFlag = false;
			}
		}
		if(inventoryFlag){//sku有库存
			result.setType(BundleStatus.BUNDLE_CAN_SALE.getStatus());
		}
	}

	@Override
	public Pagination<BundleCommand> findBundleCommandByPage(Page page, Sort[] sorts) {
		Pagination<BundleCommand> pagination = bundleDao.findBundlesByPage(page, sorts);
		List<BundleCommand> bundles = pagination.getItems();
		if (bundles != null) {
			// 2 填充bundleCommand的基本信息
			fillBundleCommandList(bundles);
			// 3如果bundle中的某个商品失效，那么就踢掉该bundle
			removeInvalidBundle(bundles);
		} else {
			LOG.error("find bundles is null");
			LOG.error("parametar : page [{}]  sorts[{}]", JsonUtil.format(page), JsonUtil.format(sorts));
		}
		pagination.setItems(bundles);
		return pagination;
	}

	/**
	 * <h3>剔除掉无效的bundle</h3>
	 * <ul>
	 * <li>如果bundle中的某个商品不是上架状态,那么该bundle就应该移除掉</li>
	 * </ul>
	 * 
	 * @param bundles
	 */
	public void removeInvalidBundle(List<BundleCommand> bundles) {
		Iterator<BundleCommand> iterator = bundles.iterator();
		while (iterator.hasNext()) {
			boolean removeFlag = false;
			BundleCommand bundle = iterator.next();
			List<BundleElementCommand> bundleElement = bundle.getBundleElementCommands();
			for (BundleElementCommand bundleElementCommand : bundleElement) {
				if (!removeFlag) {
					List<BundleItemCommand> bundleItem = bundleElementCommand.getItems();
					for (BundleItemCommand bundleItemCommand : bundleItem) {
						Item item = itemDao.findItemById(bundleItemCommand.getItemId());
						// lifecycle == 1 上架
						if (item.getLifecycle().intValue() != 1) {
							removeFlag = true;
							break;
						}
					}
				}

			}
			if (removeFlag) {
				iterator.remove();
			}
		}
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

		bundle.setBundleElementCommands(bundleElementList);
		bundle.setMaxOriginalSalesPrice(bundle.getMaxOriginalSalesPrice());
		bundle.setMinOriginalSalesPrice(bundle.getMinOriginalSalesPrice());
		bundle.setMaxSalesPrice(bundle.getMaxSalesPrice());
		bundle.setMinSalesPrice(bundle.getMinSalesPrice());
		bundle.setMaxListPrice(bundle.getMaxListPrice());
		bundle.setMinListPrice(bundle.getMinListPrice());
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

		bundleElementCommand.setItems(items);
	}

	/**
	 * 封装BundleItemCommand信息
	 * 
	 * @param itemId
	 * @param skus
	 * @return
	 */
	private BundleItemCommand packagingBundleItemCommandInfo(Long itemId, List<BundleSku> skus, BundleCommand bundle) {

		BundleItemCommand bundleItemCommand = new BundleItemCommand();

		bundleItemCommand.setItemId(itemId);
		List<BundleSkuCommand> skuCommands = packagingBundleSkuCommands(skus, bundle);
		bundleItemCommand.setBundleSkus(skuCommands);

		bundleItemCommand.setMinOriginalSalesPrice(bundleItemCommand.getMinOriginalSalesPrice());
		bundleItemCommand.setMaxOriginalSalesPrice(bundleItemCommand.getMaxOriginalSalesPrice());
		bundleItemCommand.setMinSalesPrice(bundleItemCommand.getMinSalesPrice());
		bundleItemCommand.setMaxSalesPrice(bundleItemCommand.getMaxSalesPrice());
		bundleItemCommand.setMaxListPrice(bundleItemCommand.getMaxListPrice());
		bundleItemCommand.setMinListPrice(bundleItemCommand.getMinListPrice());

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
		for (BundleSku sku : skus) {
			BundleSkuCommand skuCommand = new BundleSkuCommand();

			ConvertUtils.convertTwoObject(skuCommand, sku);

			Sku skuu = skuDao.findSkuById(sku.getSkuId());
			// 定制价格 一口价（）
			if (bundle.getBundleType().intValue() == Bundle.PRICE_TYPE_CUSTOMPRICE
					|| bundle.getBundleType().intValue() == Bundle.PRICE_TYPE_FIXEDPRICE) {
				skuCommand.setOriginalSalesPrice(skuu.getSalePrice());
				skuCommand.setSalesPrice(sku.getSalesPrice());
			}
			// 按照实际价格
			if (bundle.getBundleType().intValue() == Bundle.PRICE_TYPE_REALPRICE) {
				skuCommand.setOriginalSalesPrice(skuu.getSalePrice());
				skuCommand.setSalesPrice(skuu.getSalePrice());
			}
			skuCommand.setListPrice(skuu.getListPrice());
		}

		return bundleSkus;
	}

}
