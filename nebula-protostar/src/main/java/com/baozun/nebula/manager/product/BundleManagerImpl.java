/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.product.BundleCommand;
import com.baozun.nebula.command.product.BundleElementCommand;
import com.baozun.nebula.command.product.BundleItemCommand;
import com.baozun.nebula.command.product.BundleSkuCommand;
import com.baozun.nebula.dao.product.BundleDao;
import com.baozun.nebula.dao.product.BundleElementDao;
import com.baozun.nebula.dao.product.BundleSkuDao;
import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.bundle.Bundle;
import com.baozun.nebula.model.bundle.BundleElement;
import com.baozun.nebula.model.bundle.BundleSku;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.web.command.BundleElementViewCommand;
import com.baozun.nebula.web.command.BundleItemViewCommand;
import com.baozun.nebula.web.command.BundleSkuViewCommand;

/**
 * @author yue.ch
 * @time 2016年5月25日 下午6:02:58
 */
@Service
public class BundleManagerImpl implements BundleManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(BundleManagerImpl.class);
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private BundleElementDao bundleElementDao;
	
	@Autowired
	private BundleSkuDao bundleSkuDao;
	
	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private SkuDao skuDao;
	
	@Autowired
	private SdkSkuManager sdkSkuManager;

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.BundleManager#createOrUpdate(com.baozun.nebula.model.bundle.Bundle)
	 */
	@Override
	@Transactional
	public Bundle createOrUpdate(BundleCommand bundle) {
		if(bundle == null) {
			throw new BusinessException(ErrorCodes.ITEM_BUNDLE_EXPANDINFO_NULL);
		}
		
		Bundle b = null;
		
		Long id = bundle.getId();
		if(id != null) { // update
			// 清空从表数据
			bundleElementDao.deleteByBundleId(id);
			bundleSkuDao.deleteByBundleId(id);
			
			b = bundleDao.getByPrimaryKey(id);
			b.setAvailableQty(bundle.getAvailableQty());
			b.setCreateTime(bundle.getCreateTime());
			b.setItemId(bundle.getItemId());
			b.setModifyTime(new Date());
			b.setPriceType(bundle.getPriceType());
			b.setSyncWithInv(bundle.getSyncWithInv());
		} else { // create
			Long itemId = bundle.getItemId();
			
			// 校验指定的item是否已经存在bundle扩展信息
			if(bundleDao.findBundleByBundleItemId(itemId, null) != null) {
				throw new BusinessException(ErrorCodes.ITEM_BUNDLE_PRODUCT_CODE_REPEAT, new Object[]{itemId});
			}
			
			b = new Bundle();
			b.setItemId(itemId);
			b.setAvailableQty(bundle.getAvailableQty());
			b.setPriceType(bundle.getPriceType());
			b.setSyncWithInv(bundle.getSyncWithInv());
			b.setCreateTime(new Date());
		}
		
		bundleDao.save(b);
		bundle.setId(b.getId());
		
		saveBundleElementAndSku(bundle);
		
		return bundle;
	}
	
	private void saveBundleElementAndSku(BundleCommand bundle) {
		
		Long id = bundle.getId();
		
		List<BundleElementCommand> bundleElementCommands = bundle.getBundleElementCommands();
		// bundle商品必须由一个主卖品和至少一个以上的捆绑成员组成
		if(bundleElementCommands == null || bundleElementCommands.size() < 2) {
			throw new BusinessException(ErrorCodes.ITEM_BUNDLE_ELEMENT_LOST);
		}
		
		for(BundleElementCommand bec : bundleElementCommands) {
			bec.setBundleId(id);
			BundleElement be = new BundleElement();
			be.setBundleId(bundle.getId());
			be.setIsMainElement(bec.getIsMainElement());
			be.setSalesPrice(bec.getSalesPrice());
			be.setSortNo(bec.getSortNo());
			bundleElementDao.save(be);
			bec.setId(be.getId());
			
			List<BundleItemCommand> bundleItemCommands = bec.getItems();
			// 每个捆绑成员必须包含至少一个商品
			if(bundleItemCommands == null || bundleItemCommands.size() == 0) {
				throw new BusinessException(ErrorCodes.ITEM_BUNDLE_ELEMENT_ITEM_LOST);
			}
			
			for(BundleItemCommand bc : bundleItemCommands) {
				List<BundleSkuCommand> bundleSkuCommands = bc.getBundleSkus();
				// 每个捆绑成员必须包含至少一个sku
				if(bundleSkuCommands == null || bundleSkuCommands.size() == 0) {
					throw new BusinessException(ErrorCodes.ITEM_BUNDLE_ELEMENT_SKU_LOST);
				}
				
				for(BundleSkuCommand bsc : bundleSkuCommands) {
					bsc.setBundleElementId(bec.getId());
					bsc.setBundleId(id);
					bsc.setItemId(bc.getItemId());
					// 如果成员是款
					if(bec.getIsStyle()) {
						if(bec.getStyle() == null) {
							throw new BusinessException(ErrorCodes.ITEM_BUNDLE_ELEMENT_STYLE_LOST);
						} else {
							bsc.setStyle(bec.getStyle());
						}
					}
					BundleSku bs = (BundleSku) ConvertUtils.convertTwoObject(new BundleSku(), bsc);
					bundleSkuDao.save(bs);
					bsc.setId(bs.getId());
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.BundleManager#loadBundleSku(com.baozun.nebula.web.command.BundleElementViewCommand[])
	 */
	@Override
	public List<BundleElementViewCommand> loadBundleElements(BundleElementViewCommand[] commands) {
		List<BundleElementViewCommand> result = new ArrayList<BundleElementViewCommand>();
		result.addAll(Arrays.asList(commands));
		Collections.sort(result);
		Iterator<BundleElementViewCommand> iterator = result.iterator();
		while(iterator.hasNext()) {
			BundleElementViewCommand command = iterator.next();
			String styleCode = command.getStyleCode();
			if(StringUtils.isNotBlank(styleCode)) { // 成员为同款商品
				List<ItemCommand> itemCommands = itemDao.findItemCommandByStyle(styleCode);
				if(itemCommands != null) {
					List<BundleItemViewCommand> bundleItemViewCommands = new ArrayList<BundleItemViewCommand>();
					for(ItemCommand ic : itemCommands) {
						BundleItemViewCommand bivc = new BundleItemViewCommand();
						bivc.setItemId(ic.getId());
						bivc.setItemCode(ic.getCode());
						bivc.setSalesPrice(ic.getSalePrice());
						bundleItemViewCommands.add(bivc);
					}
					command.setBundleItemViewCommands(bundleItemViewCommands);
				}
			} else { // 成员为单个商品
				String itemCode = command.getItemCode();
				ItemCommand ic = itemDao.findItemCommandByCode(itemCode);
				BundleItemViewCommand bivc = new BundleItemViewCommand();
				bivc.setItemId(ic.getId());
				bivc.setItemCode(itemCode);
				bivc.setSalesPrice(ic.getSalePrice());
				command.setBundleItemViewCommands(Arrays.asList(bivc));
			}
		}
		
		for(BundleElementViewCommand command : result) {
			List<BundleItemViewCommand> bundleItemViewCommands = command.getBundleItemViewCommands();
			if(bundleItemViewCommands == null) {
				continue;
			}
			
			for(BundleItemViewCommand bivc : bundleItemViewCommands) {
				List<Sku> skus = skuDao.findSkuByItemId(bivc.getItemId());
				List<BundleSkuViewCommand> bundleSkuViewCommands = new ArrayList<BundleSkuViewCommand>();
				for(Sku sku : skus) {
					BundleSkuViewCommand bsvc = new BundleSkuViewCommand();
					bsvc.setSkuId(sku.getId());
					bsvc.setOriginalSalesPrice(sku.getSalePrice());
					bsvc.setProperty(getSkuPropertyStrForBundle(sku));
					bsvc.setIsParticipation(true);
					
					bundleSkuViewCommands.add(bsvc);
				}
				bivc.setBundleSkuViewCommands(bundleSkuViewCommands);
			}
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.BundleManager#findBundleCommandByBundleItemId(java.lang.Long)
	 */
	@Override
	public BundleCommand findBundleCommandByBundleItemId(Long bundleItemId) {
		BundleCommand result = bundleDao.findBundleByBundleItemId(bundleItemId, null);
		if(result != null) {
			List<BundleElement> bundleElements = bundleElementDao.findByBundleId(result.getId());
			List<BundleElementCommand> becs = new ArrayList<BundleElementCommand>();
			for(BundleElement be : bundleElements) {
				BundleElementCommand bec = new BundleElementCommand();
				bec.setBundleId(be.getBundleId());
				bec.setId(be.getId());
				bec.setIsMainElement(be.getIsMainElement());
				bec.setSalesPrice(be.getSalesPrice());
				bec.setSortNo(be.getSortNo());
				List<BundleItemCommand> bundleItems = new ArrayList<BundleItemCommand>();
				List<BundleSku> bundleSkus = bundleSkuDao.findByBundleElementId(bec.getId());
				String style = bundleSkus.get(0).getStyle();
				if(StringUtils.isNoneBlank(style)) { // bundleElement为同款商品
					bec.setIsStyle(true);
					bec.setStyle(style);
					
					List<BundleSkuCommand> bscs = new ArrayList<BundleSkuCommand>();
					Map<Long, BundleItemCommand> bicMap = new HashMap<Long, BundleItemCommand>();
					for(BundleSku sku : bundleSkus) {
						Long itemId = sku.getItemId();
						ItemCommand itemCommand = itemDao.findItemCommandById(itemId);
						
						BundleItemCommand bic = bicMap.get(itemId);
						if(bic == null) {
							bic = new BundleItemCommand();
							bic.setItemCode(itemCommand.getCode());
							bic.setItemId(itemId);
							bic.setBundleSkus(new ArrayList<BundleSkuCommand>());
							bicMap.put(itemId, bic);
						}
						
						BundleSkuCommand bsc = new BundleSkuCommand();
						bsc.setBundleElementId(sku.getBundleElementId());
						bsc.setBundleId(sku.getBundleId());
						bsc.setId(sku.getId());
						bsc.setItemId(sku.getItemId());
						bsc.setSalesPrice(sku.getSalesPrice());
						bsc.setSkuId(sku.getSkuId());
						bscs.add(bsc);
						bic.getBundleSkus().addAll(bscs);
					}
					
					bundleItems.addAll(bicMap.values());
				} else { // bundleElement为单个商品
					BundleItemCommand bic = new BundleItemCommand();
					Long itemId = bundleSkus.get(0).getItemId();
					ItemCommand itemCommand = itemDao.findItemCommandById(itemId);
					bic.setItemCode(itemCommand.getCode());
					bic.setItemId(itemId);
					List<BundleSkuCommand> bscs = new ArrayList<BundleSkuCommand>();
					for(BundleSku sku : bundleSkus) {
						BundleSkuCommand bsc = new BundleSkuCommand();
						bsc.setBundleElementId(sku.getBundleElementId());
						bsc.setBundleId(sku.getBundleId());
						bsc.setId(sku.getId());
						bsc.setItemId(sku.getItemId());
						bsc.setSalesPrice(sku.getSalesPrice());
						bsc.setSkuId(sku.getSkuId());
						bscs.add(bsc);
					}
					bic.setBundleSkus(bscs);
					
					bundleItems.add(bic);
				}
				
				bec.setItems(bundleItems);
				
				becs.add(bec);
			}
			
			result.setBundleElementCommands(becs);
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.manager.product.BundleManager#
	 * getSkuPropertyStrForBundle(com.baozun.nebula.model.product.Sku)
	 */
	@Override
	public String getSkuPropertyStrForBundle(Sku sku) {
		// 拼接sku属性字符串
		List<SkuProperty> skuProperties = sdkSkuManager.getSkuPros(sku.getProperties());
		if(skuProperties != null) {
			StringBuilder sb = new StringBuilder();
			for (SkuProperty property : skuProperties) {
				sb.append(property.getValue()).append("-");
			}
			return sb.toString().replaceAll("-$", "");
		} else {
			return null;
		}
	}

}
