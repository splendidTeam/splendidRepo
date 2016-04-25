package com.baozun.nebula.manager.bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
	public List<BundleCommand> findBundleCommandByItemId(Long itemId , Boolean ...flag) {

		// 1、 根据itemId,lifeCycle查询bundle （只需要查询出可售的bundle）
		List<BundleCommand> bundles = bundleDao.findBundlesByItemId(itemId, 1);
		if (bundles != null) {
			// 2 填充bundleCommand的基本信息
			fillBundleCommandList(bundles);
			if(flag == null || flag.length == 0 || Boolean.TRUE.equals(flag[0])){
				// 3如果bundle中的某个商品失效，那么就踢掉该bundle
				removeInvalidBundleInfo(bundles);
			}
		} else {
			LOG.error("find bundles is null");
			LOG.error("itemId : [{}]  showMainElementFlag : {}  lifeCycle : {}  [{}]", itemId, 1 , new Date());
		}

		return bundles;
	}

	@Override
	@Transactional(readOnly = true)
	public BundleCommand findBundleCommandByBundleId(Long boundleId , Boolean ...flag) {
		BundleCommand bundle = bundleDao.findBundlesById(boundleId, null);
		fillBundleCommand(bundle);
		if((flag == null || flag.length == 0 || Boolean.TRUE.equals(flag[0])) && needRemoveInvalidBundle(bundle)){
			// 3如果bundle中的某个商品失效，那么就踢掉该bundle
			return null;
		}
		return bundle;
	}
	
	@Override
	public Pagination<BundleCommand> findBundleCommandByPage(Page page, Sort[] sorts , Boolean ...flag) {
		Pagination<BundleCommand> pagination = bundleDao.findBundlesByPage(page, sorts);
		List<BundleCommand> bundles = pagination.getItems();
		if (bundles != null) {
			// 2 填充bundleCommand的基本信息
			fillBundleCommandList(bundles);
			if(flag == null || flag.length == 0 || Boolean.TRUE.equals(flag[0])){
				// 3如果bundle中的某个商品失效，那么就踢掉该bundle
				removeInvalidBundleInfo(bundles);
			}
		} else {
			LOG.error("find bundles is null");
			LOG.error("parametar : page {}  sorts{}  [{}]", JsonUtil.format(page), JsonUtil.format(sorts) , new Date());
		}
		pagination.setItems(bundles);
		return pagination;
	}


	@Override
	@Transactional(readOnly = true)
	public BundleValidateResult validateBundle(Long bundleId,
			List<Long> skuIds, int quantity) {
		
		return validateBundleInfo(bundleId,skuIds,quantity);
	}
	
	/**
	 * 校验bundle的库存和状态
	 * @param bundleId
	 * @param skuIds
	 * @param quantity
	 * @param result
	 * @param command
	 * @param bundleItemInfo
	 */
	private BundleValidateResult validateBundleInfo(Long bundleId,List<Long> skuIds, int quantity){
		BundleValidateResult result = new BundleValidateResult();
		//查询bundle的所有相关信息
		BundleCommand command = bundleDao.findBundlesById(bundleId,null);
		
		Item bundleItem = itemDao.findItemById(command.getItemId());
		
		if(bundleItem == null || command == null){//bundle不存在
			buildValidateResult( result ,BundleStatus.BUNDLE_NOT_EXIST.getStatus() , bundleId,null, null);
			return result ;
		}
        
		return validateBundle(result,bundleItem,command,skuIds,quantity);
	}
	/**
	 * 校验bundle选中的一个sku组合是否可以购买
	 * <ul>
	 *   <li>bundle本身是一个商品,校验该商品是否上架状态</li>
	 *   <li>购买的sku是否是上架状态</li>
	 *   <li>购买的sku对应的商品是否是上架状态</li>
	 *   <li>购买的bundle是否库存足够</li>
	 *   <li>购买的sku是否库存足够</li>
	 * </ul>
	 * @param result
	 * @param bundleItem
	 * @param command
	 * @param skuIds
	 * @param quantity
	 * @return
	 */
	private BundleValidateResult validateBundle(BundleValidateResult result , Item bundleItem , BundleCommand command,List<Long> skuIds , int quantity){
		
		//1 ,=============bundle验证是否上架校验================
		if(!validateBundleLifeCycle( command , bundleItem , result)){
			return result;
		}
		
		//根据bundleId查询所有的skuId
		List<BundleSku> bundleSkus = bundleSkuDao.findByBundleId(command.getId());
		
		if(bundleSkus == null || bundleSkus.size() == 0){
			buildValidateResult( result ,BundleStatus.BUNDLE_ITEM_NOT_EXIST.getStatus() , command.getId(),null, null);
			return result ;
		}
		
		Map<Long,Long> skuIdToItemId = skuIdToItemId(bundleSkus,skuIds);
		
		Map<Long,BundleSku> bundleSkuMap = skuIdToBundleSku(bundleSkus);
		
		for (Long skuId : skuIds) {
			//2 ,=============sku 以及其所在的商品 的状态校验通过================
			if(skuIdToItemId.get(skuId) == null){
				buildValidateResult( result ,BundleStatus.BUNDLE_ITEM_NOT_EXIST.getStatus() , command.getId(),null, skuId);
				break;
			}
			//item sku lifecycle校验
			Item item = itemDao.findItemById(skuIdToItemId.get(skuId));
			Sku sku = skuDao.findSkuById(skuId);
			if(item == null || sku == null || item.getLifecycle().intValue() != 1 || sku.getLifecycle().intValue() != 1){
				buildValidateResult( result ,BundleStatus.BUNDLE_ITEM_NOT_EXIST.getStatus() , command.getId(),skuIdToItemId.get(skuId), skuId);
				break;
			}
			//3 ,=============sku 的库存状态校验通过================
			//库存校验
			if(!validateBundleInventory(command ,  quantity , bundleSkuMap.get(skuId) ,  sku , result) ){
				break;
			}
			
		}

		return result;
		
	}
	/**
	 * 校验bundle本身的lifecycle是否上架
	 * @param command
	 * @param bundleItem
	 * @param result
	 * @return
	 */
	private boolean validateBundleLifeCycle(BundleCommand command ,Item bundleItem ,BundleValidateResult result ){
		if(bundleItem.getLifecycle().intValue() == 2){//bundle不存在
			buildValidateResult( result ,BundleStatus.BUNDLE_NOT_EXIST.getStatus() , command.getId(),null, null);
			return false ;
		}
		if(bundleItem.getLifecycle().intValue() == 3){//bundle未上架
			buildValidateResult( result ,BundleStatus.BUNDLE_NOT_PUTAWAY.getStatus() , command.getId(),null, null);
			return false ;
		}
		if(bundleItem.getLifecycle().intValue() == 0){//bundle已下架
			buildValidateResult( result ,BundleStatus.BUNDLE_SOLD_OUT.getStatus() , command.getId(),null, null);
			return false ;
		}
		return true;
	}
	
	/**
	 * key : skuId ; value : itemId
	 * @param bundleSkus
	 * @param skuIds
	 * @return
	 */
	private Map<Long,Long> skuIdToItemId(List<BundleSku> bundleSkus , List<Long> skuIds){
		Map<Long,Long> skuIdToItemId = new HashMap<Long, Long>();
		for (BundleSku sku : bundleSkus) {
			if(!skuIdToItemId.containsKey(sku.getSkuId()) && skuIds.contains(sku.getSkuId())){
				skuIdToItemId.put(sku.getSkuId(), sku.getItemId());
			}
		}
		return skuIdToItemId;
	}
	/**
	 * key : skuId , value : BundleSku
	 * @param bundleSkus
	 * @return
	 */
	private Map<Long,BundleSku> skuIdToBundleSku(List<BundleSku> bundleSkus){
		Map<Long,BundleSku> skuIdToItemId = new HashMap<Long, BundleSku>();
		for (BundleSku sku : bundleSkus) {
			if(!skuIdToItemId.containsKey(sku.getSkuId())){
				skuIdToItemId.put(sku.getSkuId(), sku);
			}
		}
		return skuIdToItemId;
	}
	
	/**
	 * <li>bundle本身库存是否足够</li>
	 * <li>校验单个sku的库存是否足够</li>
	 * @param result
	 * @param bundle
	 * @param quantity
	 * @param sku
	 * @return
	 */
	private boolean validateBundleInventory(BundleCommand bundle , int quantity ,BundleSku bundleSku , Sku sku ,BundleValidateResult result ){
		Integer availableQty = bundle.getAvailableQty();
		// 如果捆绑装单独维护了库存
		if(availableQty != null) {
			// 如果不需要同步扣减单品库存 ,那么就以捆绑装设置的库存为准；否则取捆绑装库存与sku实际可用库存的最小值
			if(!bundle.getSyncWithInv() && availableQty.intValue() < quantity){
				buildValidateResult( result ,BundleStatus.BUNDLE_NO_INVENTORY.getStatus() , bundle.getId(),null, null);
				return false;
			}
			if(bundle.getSyncWithInv()){
				SkuInventory inventory = sdkSkuInventoryDao.findSkuInventoryByExtentionCode(sku.getOutid());
				int qty = 0 ;
				if(inventory != null && inventory.getAvailableQty() != null){
					qty = inventory.getAvailableQty();
				}
				
				if(availableQty <= qty && availableQty < quantity){
					buildValidateResult( result ,BundleStatus.BUNDLE_NO_INVENTORY.getStatus() , bundle.getId(),null, null);
					return false;
				}
				
				if(availableQty > qty && qty < quantity){
					buildValidateResult( result ,BundleStatus.BUNDLE_ITEM_NO_INVENTORY.getStatus() , bundle.getId(),bundleSku.getItemId(), bundleSku.getSkuId());
					return false;
				}
			}
		} 
		
		SkuInventory inventory = sdkSkuInventoryDao.findSkuInventoryByExtentionCode(sku.getOutid());
		if(inventory == null ||inventory.getAvailableQty().intValue() < quantity){
			buildValidateResult( result ,BundleStatus.BUNDLE_ITEM_NO_INVENTORY.getStatus() , bundle.getId(),bundleSku.getItemId(), bundleSku.getSkuId());
			return false;
		}
		
	   return true;
	}
	
	/**
	 * 构建校验结果对象
	 * @param result
	 * @param type
	 * @param bundleId
	 * @param itemId
	 * @param skuId
	 * @return
	 */
	private void buildValidateResult(BundleValidateResult result ,int type , Long bundleId,Long itemId, Long skuId){
		result.setBundleId(bundleId);
		result.setItemId(itemId);
		result.setSkuId(skuId);
		result.setType(type);
	}

	/**
	 * <h3>剔除掉无效的bundle</h3>
	 * <ul>
	 * <li>如果bundle中的某个商品不是上架状态,那么该bundle就应该移除掉</li>
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
				LOG.debug("current bundle invailed , bundleId [{}] current time : [{}]" , bundle.getId(), new Date());
				LOG.debug("***********************************");
				iterator.remove();
			}
		}
	}
	/**
	 * <h3>校验是否需要删除bundle</h3>
	 * <p>校验的范围如下 ： </p>
	 * <ul>
	 *   <ol>
	 *   	<li>bundle本身的商品就失效</li>
	 *   	<li>最少有一个element中的商品都失效了 ,bundle就失效</li>
	 *   </ol>
	 * </ul>
	 * <h3>注意 ： 该方法不会校验库存的信息</h3>
	 * @return 　返回结果 布尔类型
	 * <ul>
	 *   <li>true : 需要删除 </li>
	 *   <li>false : 不需要删除 </li>
	 * </ul>
	 * @param bundle
	 * @return
	 */
   private boolean needRemoveInvalidBundle(BundleCommand bundle){
	   List<BundleElementCommand> bundleElementCommands = bundle.getBundleElementCommands();
	   if (bundle.getLifeCycle() != 1 || !isEnabled(bundleElementCommands)){
		   return true;
	   }
	  return false; 
   }
	/**
	 * <h3>校验bundle是否有效</h3>
	 * <p>校验的范围如下 ： </p>
	 * <ul>
	 *   <ol>
	 *   	<li>最少有一个element中的商品都失效了 ,bundle就失效</li>
	 *   </ol>
	 * </ul>
	 * <h3>注意 ： 该方法不会校验库存的信息</h3>
	 * @return 　返回结果 布尔类型
	 * <ul>
	 *   <li>true : 有效 </li>
	 *   <li>false : 失效</li>
	 * </ul>
	 */
	private boolean isEnabled(List<BundleElementCommand> bundleElementCommands){
		Boolean removeFlag = Boolean.TRUE;
		
		for (BundleElementCommand bundleElementCommand : bundleElementCommands) {
			removeFlag = validateBundleElement( bundleElementCommand);
			if(!removeFlag){
				break;
			}
		}
		return removeFlag;
	}
	
	/**
	 * <p>校验的步骤 ： </p>
	 * <ul>
	 *   <ol>
	 *   	<li>踢掉所有不是上架状态的商品</li>
	 *      <li>踢掉商品中所有不是上架状态的sku</li>
	 *      <li>如果商品是上架状态,但是该商品没有一个上架的sku,那么该商品也需要踢掉</li>
	 *   </ol>
	 * </ul>
	 * @param bundleElementCommand
	 * @return
	 */
	private boolean validateBundleElement(BundleElementCommand bundleElementCommand){

		List<BundleItemCommand> bundleItem = bundleElementCommand.getItems();
		
		Iterator<BundleItemCommand> iterator = bundleItem.iterator();
		while (iterator.hasNext()) {
			BundleItemCommand bundleItemCommand = iterator.next();
			//1 踢掉所有不是上架状态的商品
			if (bundleItemCommand.getLifecycle().intValue() != 1) {
				iterator.remove();
				continue;
			}
			//2 踢掉商品中所有不是上架状态的sku
			List<BundleSkuCommand> skus = bundleItemCommand.getBundleSkus();
			Iterator<BundleSkuCommand> iterator2 = skus.iterator();
			while (iterator2.hasNext()) {
				BundleSkuCommand bundleSkuCommand = (BundleSkuCommand) iterator2.next();
				if(bundleSkuCommand.getLifeCycle().intValue() != 1){
					iterator2.remove();
					continue;
				}
			}
			//3 如果商品是上架状态,但是该商品没有一个上架的sku,那么该商品也需要踢掉
			if(bundleItemCommand.getBundleSkus() == null || bundleItemCommand.getBundleSkus().size() == 0){
				iterator.remove();
				continue;
			}
		}
		
		if(bundleElementCommand.getItems() == null || bundleElementCommand.getItems().size() == 0){
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
		
        bundle.setLifeCycle(itemDao.findItemById(bundle.getItemId()).getLifecycle());
       
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
        Item item = itemDao.findItemById(itemId);
        bundleItemCommand.setLifecycle(item.getLifecycle());
		bundleItemCommand.setItemId(itemId);
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
		for (BundleSku sku : skus) {
			BundleSkuCommand skuCommand = new BundleSkuCommand();

			ConvertUtils.convertTwoObject(skuCommand, sku);

			Sku skuu = skuDao.findSkuById(sku.getSkuId());
			skuCommand.setProperties(skuu.getProperties());
			skuCommand.setExtentionCode(skuu.getOutid());
			skuCommand.setLifeCycle(skuu.getLifecycle());
			
			// 定制价格 一口价（）
			if (bundle.getPriceType().intValue() == Bundle.PRICE_TYPE_CUSTOMPRICE
					|| bundle.getBundleType().intValue() == Bundle.PRICE_TYPE_FIXEDPRICE) {
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
			if(availableQty != null) {
				// 如果不需要同步扣减单品库存 ,那么就以捆绑装设置的库存为准；否则取捆绑装库存与sku实际可用库存的最小值
				if(!bundle.getSyncWithInv()){
					skuCommand.setQuantity(availableQty);
				}else{
					SkuInventory inventory = sdkSkuInventoryDao.findSkuInventoryByExtentionCode(skuu.getOutid());
					int qty = 0 ;
					if(inventory != null && inventory.getAvailableQty() != null){
						qty = inventory.getAvailableQty();
					}
					skuCommand.setQuantity(Math.min(availableQty, qty));
				}
			} else {
				skuCommand.setQuantity(sdkSkuInventoryDao.findSkuInventoryByExtentionCode(skuu.getOutid()).getAvailableQty());
			}
			
			bundleSkus.add(skuCommand);
		}

		return bundleSkus;
	}

}
