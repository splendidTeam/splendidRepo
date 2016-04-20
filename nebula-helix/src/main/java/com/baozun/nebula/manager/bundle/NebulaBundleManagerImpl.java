package com.baozun.nebula.manager.bundle;

import java.util.ArrayList;
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
import com.baozun.nebula.model.bundle.Bundle;
import com.baozun.nebula.model.bundle.BundleSku;
import com.baozun.nebula.web.command.BundleValidateResult;

@Transactional
@Service
public class NebulaBundleManagerImpl implements NebulaBundleManager {
	private static final Logger LOG=LoggerFactory.getLogger(NebulaBundleManagerImpl.class);
	
	@Autowired
	private BundleSkuDao bundleSkuDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private BundleElementDao bundleElementDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<BundleCommand> findBundleCommandByItemId(Long itemId) {
		
		//1、 根据itemId,lifeCycle查询bundle （只需要查询出可售的bundle）
		List<BundleCommand> bundles = bundleDao.findBundlesByItemId(itemId, 1);
		if(bundles != null){
			//2 填充bundleCommand的信息
			fillBundleCommandInfo(bundles);
			
			//TODO  校验bundle
			//3、查询bundleElementCommands:findBundleElementCommandByBundleId(bundleId);
		}
		
		return bundles;
	}

	@Override
	@Transactional(readOnly = true)
	public BundleCommand findBundleCommandByBundleId(Long boundleId) {
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public BundleValidateResult validateBundle(Long bundleId,
			List<Long> skuIds, int quantity) {
		return null;
	}
	
	@Override
	public Pagination<BundleCommand> findBundleCommandByPage(Page page,
			Sort[] sorts) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 分别给每个bundle填充其基本信息
	 * @param bundles ： bundle集合
	 */
    private void fillBundleCommandInfo(List<BundleCommand> bundles){
    	for(BundleCommand bundle : bundles){
    		fillBundleElement(bundle);
    	}
    	
    }
    /**
     * 为bundle填充BundleElement信息
     * @param bundle ： Bundle对象
     * @param showMainElementFlag ： 是否添加主款
     */
    private void fillBundleElement(BundleCommand bundle){
    	
    	List<BundleElementCommand> bundleElementList = new ArrayList<BundleElementCommand>();
    	
		List<BundleSku> skus  = bundleSkuDao.findByBundleId(bundle.getId());
		//BundleSku集合按照bundleElementId分组 . key : bundleElementId ; value : List<BundleSku>
		Map<Long,List<BundleSku>> map = new HashMap<Long,List<BundleSku>>();
		for(BundleSku sku : skus){
			if(map.containsKey(sku.getBundleElementId())){
				map.get(sku.getBundleElementId()).add(sku);
			}else{
				
				List<BundleSku> list = new ArrayList<BundleSku>();
				list.add(sku);
				map.put(sku.getBundleElementId(), list);
				
				BundleElementCommand element = new BundleElementCommand();
				element.setId(sku.getBundleElementId());
				bundleElementList.add(element);
			}
		}
		
		fillBundleElementInfo(bundleElementList,map, bundle);
		
		bundle.setBundleElementCommands(bundleElementList);
    }
    /**
     * 分别给每个BundleElement填充信息
     * @param bundleElementList ：bundleElement列表 
     * @param map ： BundleSku集合按照bundleElementId分组 . key : bundleElementId ; value : List<BundleSku>
     * @param showMainElementFlag ： 是否添加主款
     * <ul>
     *    <li>false : 屏蔽对应的BundleElement</li>
     * </ul>
     */
    private void fillBundleElementInfo(List<BundleElementCommand> bundleElementList,Map<Long,List<BundleSku>> map,BundleCommand bundle){
    	
    	Iterator<BundleElementCommand> iterator = bundleElementList.iterator();
    	while(iterator.hasNext()){
    		 BundleElementCommand bundleElementCommand = iterator.next();
    		 List<BundleSku> elementSkus = map.get(bundleElementCommand.getId());
    		 
			 //BundleSku按ItemId分组
			 fillItems(bundleElementCommand,convertToItemMap(elementSkus), bundle);
			 
    	}
    }
    /**
     * BundleSku集合按照itemId分组 . key : itemId ; value : List<BundleSku>
     * @param elementSkus
     * @return
     */
    private Map<Long,List<BundleSku>> convertToItemMap(List<BundleSku> elementSkus){
    	Map<Long,List<BundleSku>> map = new HashMap<Long, List<BundleSku>>();
    	for (BundleSku bundleSku : elementSkus) {
			if(map.containsKey(bundleSku.getItemId())){
				map.get(bundleSku.getItemId()).add(bundleSku);
			}else{
				List<BundleSku> list = new ArrayList<BundleSku>();
				list.add(bundleSku);
				map.put(bundleSku.getItemId(), list);
			}
		}
    	return map;
    }
    /**
     * <ul>
     *     <li>填充BundleItemCommand :参考　{@link com.baozun.nebula.web.command.bundle.BundleElementCommand#items}</li>
     * </ul>
     * @param bundleElementCommand :　bundleElement对象
     * @param elementSkus : bundleElement包含的sku列表  key : itemId ; value : List<BundleSku>
     */
    private void fillItems(BundleElementCommand bundleElementCommand ,Map<Long,List<BundleSku>> elementSkus ,BundleCommand bundle){
    	//填充BundleItemCommand
    	fillBundleItemCommand(bundleElementCommand,elementSkus, bundle);
    }
    
    /**
     * 填充bundleElementCommand中的BundleItemCommand信息
     * @param bundleElementCommand :　bundleElement对象
     * @param elementSkus : bundleElement包含的sku列表
     */
    private void fillBundleItemCommand(BundleElementCommand bundleElementCommand ,Map<Long,List<BundleSku>> elementSkus,BundleCommand bundle){
    	 List<BundleItemCommand> items = new ArrayList<BundleItemCommand>();
    	 Set<Long> keys = elementSkus.keySet();
    	 for (Long key : keys) {
			items.add(packagingBundleItemCommandInfo(key,elementSkus.get(key) , bundle));
		}
    	
    	 bundleElementCommand.setItems(items);
    }
    /**
     * 封装BundleItemCommand信息
     * @param itemId
     * @param skus
     * @return
     */
    private BundleItemCommand packagingBundleItemCommandInfo(Long itemId,List<BundleSku> skus ,BundleCommand bundle){
    	
    	BundleItemCommand bundleItemCommand = new BundleItemCommand();
    	
    	bundleItemCommand.setItemId(itemId);
    	
    	bundleItemCommand.setBundleSkus(packagingBundleSkuCommands(skus,bundle));
    	
    	return bundleItemCommand;
    }
    /**
     * 封装BundleSkuCommand信息
     * @param skus
     * @param bundle
     * @return
     */
    private List<BundleSkuCommand> packagingBundleSkuCommands(List<BundleSku> skus ,Bundle bundle){
    	List<BundleSkuCommand> bundleSkus = new ArrayList<BundleSkuCommand>();
    	for (BundleSku sku : skus) {
    		BundleSkuCommand skuCommand = new BundleSkuCommand();
    		
    		ConvertUtils.convertTwoObject(sku,skuCommand);
    		/*//定制价格 一口价（）
    		if(bundle.getBundleType().intValue() == Bundle.PRICE_TYPE_CUSTOMPRICE || 
    				bundle.getBundleType().intValue() == Bundle.PRICE_TYPE_FIXEDPRICE){
        		skuCommand.setListPrice(sku.getSalesPrice());
        		skuCommand.setSalesPrice(sku.getSalesPrice());
        	}
    		//按照实际价格
    		if(bundle.getBundleType().intValue() == Bundle.PRICE_TYPE_REALPRICE){
    			Sku skuu = skuDao.findSkuById(sku.getSkuId());
        		skuCommand.setListPrice(skuu.getListPrice());
        		skuCommand.setSalesPrice(skuu.getSalePrice());
        	}*/
    		
    		
		}
    	
    	return bundleSkus ;
    }
}
