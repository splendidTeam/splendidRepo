/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.product.SkuInventoryChangeLogCommand;
import com.baozun.nebula.dao.auth.UserDao;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.product.SkuInventoryChangeLog;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.SdkSkuInventoryChangeLogManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.feilong.core.Validator;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年6月6日 上午10:05:44 
 * @version   
 */

@Service
@Transactional
public class SkuInventoryChangeLogManagerImpl implements SkuInventoryChangeLogManager {
	
	@Autowired
	private SdkSkuInventoryChangeLogManager 		sdkSkuInventoryChangeLogManager;
	
	@Autowired
	private SdkSkuManager							sdkSkuManager;
	
	@Autowired
	private SdkItemManager							sdkItemManager;
	
	@Autowired
	private SdkMemberManager						sdkMemberManager;
	
	@Autowired
	private UserDao									userDao;

	/* 
	 * @see com.baozun.nebula.manager.product.SkuInventoryChangeLogManager#findSkuInventoryChangeLogListByQueryMapWithPage(loxia.dao.Page, loxia.dao.Sort[], java.util.Map)
	 */
	@Override
	public Pagination<SkuInventoryChangeLogCommand> findSkuInventoryChangeLogListByQueryMapWithPage(Page page, Sort[] sorts, Map<String, Object> paraMap) {
		Pagination<SkuInventoryChangeLogCommand> result =sdkSkuInventoryChangeLogManager.findSkuInventoryChangeLogListByQueryMapWithPage(page, sorts, paraMap);
		if(Validator.isNotNullOrEmpty(result)&&Validator.isNotNullOrEmpty(result.getItems())){
			List<String> upcs =new ArrayList<String>();
			List<Long> frontUserIds =new ArrayList<Long>();
			List<Long> backUserIds =new ArrayList<Long>();
			for(SkuInventoryChangeLogCommand command : result.getItems()){
				upcs.add(command.getExtentionCode());
				if(Validator.isNotNullOrEmpty(command.getOperator())){
					if(command.getOperator().equals(-1)){
						continue ;
					}
					if(command.getSource().equals(SkuInventoryChangeLog.SOURCE_FRONTEND)){
						frontUserIds.add(command.getOperator());
					}else if(command.getSource().equals(SkuInventoryChangeLog.SOURCE_PTS)){
						backUserIds.add(command.getOperator());
					}
				}
			}
			List<Sku> skus =sdkSkuManager.findSkuByOutIds(upcs);
			//获取itemId
			Map<String, Long> skuCodeAndItemIdMap =new HashMap<String, Long>();
			List<Long> itemIds =new ArrayList<Long>();
			for (Sku sku : skus) {
				skuCodeAndItemIdMap.put(sku.getOutid(), sku.getItemId());
				itemIds.add(sku.getItemId());
			}
			//获取item信息(code,title,lifecycle)
			Map<Long, ItemCommand> itemIdAndInfoMap =new HashMap<Long, ItemCommand>();
			List<ItemCommand> itemCommands = sdkItemManager.findItemCommandByItemIds(itemIds);
			for (ItemCommand itemCommand : itemCommands) {
				itemIdAndInfoMap.put(itemCommand.getId(), itemCommand);
			}
			//操作者账号
			Map<Long, MemberCommand> frontIdAndInfoMap =new HashMap<Long, MemberCommand>();
			if(Validator.isNotNullOrEmpty(frontUserIds)){
				List<MemberCommand> findMembersByIds = sdkMemberManager.findMembersByIds(frontUserIds);
				for (MemberCommand memberCommand : findMembersByIds) {
					frontIdAndInfoMap.put(memberCommand.getId(), memberCommand);
				}
			}
			Map<Long, User> backIdAndInfoMap =new HashMap<Long, User>();
			if(Validator.isNotNullOrEmpty(backUserIds)){
				List<User> userList=userDao.findUserListByIds(backUserIds);
				for (User user : userList) {
					backIdAndInfoMap.put(user.getId(), user);
				}
			}
			Long currentItemId =null;
			ItemCommand currentItemCommand =null;
			for(SkuInventoryChangeLogCommand command : result.getItems()){
				//商品信息
				if(Validator.isNotNullOrEmpty(skuCodeAndItemIdMap)){
					currentItemId =skuCodeAndItemIdMap.get(command.getExtentionCode());
					if(Validator.isNotNullOrEmpty(currentItemId)&&
							Validator.isNotNullOrEmpty(itemIdAndInfoMap)&&
							Validator.isNotNullOrEmpty(currentItemCommand =itemIdAndInfoMap.get(currentItemId))){
						command.setItemCode(currentItemCommand.getCode());
						command.setItemTitle(currentItemCommand.getTitle());
						command.setLifecycle(currentItemCommand.getLifecycle());
					}
				}
				//操作者账号
				if(Validator.isNotNullOrEmpty(command.getOperator())){
					if(command.getOperator().equals(-1)){
						continue ;
					}
					if(command.getSource().equals(SkuInventoryChangeLog.SOURCE_FRONTEND)){
						if(Validator.isNotNullOrEmpty(frontIdAndInfoMap)&&Validator.isNotNullOrEmpty(frontIdAndInfoMap.get(command.getOperator()))){
							command.setOperatorLabel(frontIdAndInfoMap.get(command.getOperator()).getLoginEmail());
						}
					}else if(command.getSource().equals(SkuInventoryChangeLog.SOURCE_PTS)){
						if(Validator.isNotNullOrEmpty(backIdAndInfoMap)&&Validator.isNotNullOrEmpty(backIdAndInfoMap.get(command.getOperator()))){
									command.setOperatorLabel(backIdAndInfoMap.get(command.getOperator()).getUserName());
						}
					}
				}
			}
			
		}
		
		return result;
	}

}
