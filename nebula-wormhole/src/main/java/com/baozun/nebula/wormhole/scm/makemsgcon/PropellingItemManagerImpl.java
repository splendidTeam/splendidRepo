package com.baozun.nebula.wormhole.scm.makemsgcon;

import java.util.ArrayList;
import java.util.List;

import loxia.dao.Page;
import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.wormhole.mq.entity.sku.OnSaleSkuV5;
import com.baozun.nebula.wormhole.scm.handler.PropellingItemHandler;

/**
 * 推送商品相关信息 只保存到MsgSendContent表中,补偿机制会从表中取出发送 实现类
 * 
 * @author chenguang.zhou
 * @date 2014年5月13日 下午2:09:27
 */
@Service("propellingItemManager")
@Transactional
public class PropellingItemManagerImpl implements PropellingItemManager {
	
	private static Logger log = LoggerFactory.getLogger(PropellingItemManagerImpl.class);

	@Autowired
	private SdkItemManager			sdkItemManager;

	@Autowired
	private PropellingCommonManager	propellingCommonManager;
	
	@Autowired(required=false)
	private PropellingItemHandler	propellingItemHandler;

	@Override
	public MsgSendContent propellingOnSalesItems(MsgSendRecord msr) {
		
		try {
			/****** 在售商品的extentionCode集合 ******/
			List<OnSaleSkuV5> ossList = new ArrayList<OnSaleSkuV5>();
			MsgSendContent msgSendContent = null;
			String ext = msr.getExt();
			if(StringUtils.isBlank(ext)){
				ext = "1";
			}
			Page page = new Page(Integer.valueOf(ext), 100);
			Sort[] sorts = Sort.parse("tps.id asc");
			List<Sku> skuList = sdkItemManager.findOnSalesItemListWithPage(page, sorts);
//			List<Long> itemIds = new ArrayList<Long>();
//			for (Item item : itemList) {
//				itemIds.add(item.getId());
//			}
			/****** 通过商品id集合, 获取可用sku集合 lifecycle = 1 ******/
//			List<Sku> skuList = sdkItemManager.findEffectSkuByItemIds(itemIds);
			log.debug("on sale product synchroized interface total count is {}.", skuList.size());
			for (Sku sku : skuList) {
				OnSaleSkuV5 onSaleSkuV5 = new OnSaleSkuV5();
				onSaleSkuV5.setExtensionCode(sku.getOutid());
				ossList.add(onSaleSkuV5);
			}
			
			/** 扩展点 **/
			if( null != propellingItemHandler){
				ossList = propellingItemHandler.propellingOnSalesItems(ossList);
			}
			/****** 保存到MsgSendContent中 ******/
			msgSendContent = propellingCommonManager.saveMsgBody(ossList, msr.getId());
			return msgSendContent;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
