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
import com.baozun.nebula.command.bundle.BundleElementCommand;
import com.baozun.nebula.command.bundle.BundleItemCommand;
import com.baozun.nebula.command.bundle.BundleSkuCommand;
import com.baozun.nebula.dao.bundle.BundleDao;
import com.baozun.nebula.dao.bundle.BundleElementDao;
import com.baozun.nebula.dao.bundle.BundleSkuDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.model.bundle.Bundle;
import com.baozun.nebula.model.bundle.BundleElement;
import com.baozun.nebula.model.bundle.BundleSku;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.Sku;
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
	public BundleValidateResult validateBundle(Long bundleId, List<Long> skuIds, int quantity) {
		return null;
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
	 * @param showMainElementFlag
	 *            ： 是否包含主款
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
	 *            <ul>
	 *            <li>false : 屏蔽对应的BundleElement</li>
	 *            </ul>
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
