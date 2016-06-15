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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
			for(BundleItemCommand bc : bundleItemCommands) {
				List<BundleSkuCommand> bundleSkuCommands = bc.getBundleSkus();
				for(BundleSkuCommand bsc : bundleSkuCommands) {
					bsc.setBundleElementId(bec.getId());
					bsc.setBundleId(id);
					bsc.setItemId(bc.getItemId());
					// 如果成员是款
					if(bec.isStyle()) {
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
					
					List<SkuProperty> skuProperties = sdkSkuManager.getSkuPros(sku.getProperties());
					StringBuilder sb = new StringBuilder();
					for(SkuProperty property : skuProperties) {
						sb.append(property.getValue()).append("-");
					}
					bsvc.setProperty(sb.toString().replaceAll("-$", ""));
					
					bundleSkuViewCommands.add(bsvc);
				}
				bivc.setBundleSkuViewCommands(bundleSkuViewCommands);
			}
		}
		
		return result;
	}

}
