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

import com.baozun.nebula.dao.bundle.BundleSkuDao;
import com.baozun.nebula.dao.product.ItemImageDao;
import com.baozun.nebula.dao.product.ItemInfoDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.model.bundle.Bundle;
import com.baozun.nebula.model.bundle.BundleElement;
import com.baozun.nebula.model.bundle.BundleSku;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.ItemInfo;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.web.command.BundleValidateResult;
import com.baozun.nebula.web.command.bundle.BundleCommand;
import com.baozun.nebula.web.command.bundle.BundleElementCommand;
import com.baozun.nebula.web.command.bundle.BundleItemCommand;
import com.baozun.nebula.web.command.bundle.BundleSkuCommand;
@Transactional
@Service
public class NebulaBundleManagerImpl implements NebulaBundleManager {
	private static final Logger LOG=LoggerFactory.getLogger(NebulaBundleManagerImpl.class);
	
	@Autowired
	private BundleSkuDao bundleSkuDao;
	
	@Autowired
	private ItemInfoDao itemInfoDao;
	
	@Autowired
	private ItemImageDao itemImageDao;
	
	@Autowired
	private SkuDao skuDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<BundleCommand> findBundleCommandByItemId(Long itemId,
			boolean showMainElementFlag, boolean showItemInfoFlag) {
		List<BundleCommand> bundleCommandList = null;
		//1、TODO 根据itemId,lifeCycle查询bundle （只需要查询出可售的bundle）
		bundleCommandList = null;
		if(bundleCommandList != null){
			//2 填充bundleCommand的信息
			fillBundleCommandInfo(bundleCommandList,showMainElementFlag,showItemInfoFlag);
			
			//TODO  校验bundle
			//3、查询bundleElementCommands:findBundleElementCommandByBundleId(bundleId);
		}
		
		return bundleCommandList;
	}

	@Override
	@Transactional(readOnly = true)
	public BundleCommand findBundleCommandByBundleId(Long boundleId,
			boolean showMainElementFlag, boolean showItemInfoFlag) {
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
	
	private List<BundleElementCommand> findBundleElementCommandByBundleId(Long bundleId){
		//TODO salesProperties(调用pdp提供的接口),items
		return null;
	}
	/**
	 * 分别给每个bundle填充其基本信息
	 * @param bundles ： bundle集合
	 * @param showMainElementFlag : 是否添加主款
	 * @param showItemInfoFlag : 是否添加bundle商品的扩展信息
	 */
    private void fillBundleCommandInfo(List<BundleCommand> bundles,boolean showMainElementFlag, boolean showItemInfoFlag){
    	for(BundleCommand bundle : bundles){
    		fillBundleElement(bundle , showMainElementFlag);
    		if(showItemInfoFlag){
    			//TODO 调用查询ItemInfoCommand的接口，后期确认
    			bundle.setItemInfoCommand(null);
    		}
    	}
    	
    }
    /**
     * 为bundle填充BundleElement信息
     * @param bundle ： Bundle对象
     * @param showMainElementFlag ： 是否添加主款
     */
    private void fillBundleElement(BundleCommand bundle , boolean showMainElementFlag){
    	
    	List<BundleElementCommand> bundleElementList = new ArrayList<BundleElementCommand>();
    	
		List<BundleSku> skus  = null;//bundleSkuDao.findByBundleId(bundle.getBundleId());
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
				element.setElementId(sku.getBundleElementId());
				bundleElementList.add(element);
			}
		}
		
		fillBundleElementInfo(bundleElementList,map,showMainElementFlag);
		
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
    private void fillBundleElementInfo(List<BundleElementCommand> bundleElementList,Map<Long,List<BundleSku>> map,boolean showMainElementFlag){
    	
    	Iterator<BundleElementCommand> iterator = bundleElementList.iterator();
    	while(iterator.hasNext()){
    		BundleElementCommand bundleElementCommand = iterator.next();
    		 List<BundleSku> elementSkus = map.get(bundleElementCommand.getElementId());
			 //TODO findBundleElementByBundleId: BundleElement(包含price、isMain。。。。)
    		 BundleElement bundleElement = null;
    		 
			 if(showMainElementFlag && bundleElement.isMainElement()){
				 //BundleSku按ItemId分组
				 fillSalesPropertiesAndItems(bundleElementCommand,convertToMap(elementSkus));
			 }else{
				 bundleElementList.remove(bundleElementCommand);
			 }
    	}
    }
    /**
     * BundleSku集合按照itemId分组 . key : itemId ; value : List<BundleSku>
     * @param elementSkus
     * @return
     */
    private Map<Long,List<BundleSku>> convertToMap(List<BundleSku> elementSkus){
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
     *    <li>填充商品的属性 :参考　{@link com.baozun.nebula.web.command.bundle.BundleElementCommand#salesProperties}</li>
     *     <li>填充BundleItemCommand :参考　{@link com.baozun.nebula.web.command.bundle.BundleElementCommand#items}</li>
     * </ul>
     * @param bundleElementCommand :　bundleElement对象
     * @param elementSkus : bundleElement包含的sku列表
     */
    private void fillSalesPropertiesAndItems(BundleElementCommand bundleElementCommand ,Map<Long,List<BundleSku>> elementSkus){
    	//TODO 填充SalesProperties
    	
    	//填充BundleItemCommand
    	fillBundleItemCommand(bundleElementCommand,elementSkus);
    }
    
    /**
     * 填充bundleElementCommand中的BundleItemCommand信息
     * @param bundleElementCommand :　bundleElement对象
     * @param elementSkus : bundleElement包含的sku列表
     */
    private void fillBundleItemCommand(BundleElementCommand bundleElementCommand ,Map<Long,List<BundleSku>> elementSkus){
    	 List<BundleItemCommand> items = new ArrayList<BundleItemCommand>();
    	 Set<Long> keys = elementSkus.keySet();
    	 for (Long key : keys) {
			items.add(packagingBundleItemCommandInfo(key,elementSkus.get(key)));
		}
    	
    	 bundleElementCommand.setItems(items);
    }
    /**
     * 封装BundleItemCommand信息
     * @param itemId
     * @param skus
     * @return
     */
    private BundleItemCommand packagingBundleItemCommandInfo(Long itemId,List<BundleSku> skus){
    	
    	ItemInfo itemInfo = itemInfoDao.findItemInfoByItemId(itemId);
    	
    	
    	
    	BundleItemCommand bundleItemCommand = new BundleItemCommand();
    	
    	bundleItemCommand.setImageUrl(getImageUrl(itemId));
    	bundleItemCommand.setItemId(itemId);
    	//通过BundleId查询bundle  
    	Bundle bundle = null;
    	//通过Bundle中的priceType来设置价格
    	if(bundle.getBundleType().intValue() == Bundle.PRICE_TYPE_FIXEDPRICE){
    		//通过bundleElementId查询bundleElement
    		BundleElement bundleElement = null;
    		bundleItemCommand.setListPrice(bundleElement.getSalesPrice());
        	bundleItemCommand.setSalesPrice(bundleElement.getSalesPrice());
    	}
    	bundleItemCommand.setTitle(itemInfo.getTitle());
    	
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
    		//TODO skuCommand里面的库存取那里的值 需要讨论
    		//skuCommand.setQuantity(sku);
    		skuCommand.setSkuId(sku.getSkuId());
    		//定制价格 一口价（）
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
        	}
    		
    		skuCommand.setProperties(getSkuProperties(sku.getSkuId()));
		}
    	
    	return bundleSkus ;
    }
    /**
     * 通过skuId封装
     * @param skuId
     * @return
     */
    private Map<Long,String> getSkuProperties(Long skuId){
    	Map<Long,String> map = new HashMap<Long, String>();
    	//TODO 
    	return map;
    }
    
    /**
     * 找到列表的封面图片URL
     * @param itemId
     * @return
     */
    private String getImageUrl(Long itemId){
    	List<Long> itemIds = new ArrayList<Long>();
    	itemIds.add(itemId);
    	List<ItemImage> list = itemImageDao.findItemImageByItemIds(itemIds,"1");
    	Integer temp = -1;
    	String  imageUrl = "";
    	for (int i = 0 ; i<list.size() ;i++) {
			if(i == 0){
			   temp = list.get(i).getPosition();
			   imageUrl = list.get(i).getPicUrl();
			}else{
				if(list.get(i).getPosition() < temp){
					temp = list.get(i).getPosition();
					imageUrl = list.get(i).getPicUrl();
				}
			}
		}
    	return imageUrl;
    }
}
