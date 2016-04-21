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
package com.baozun.nebula.web.controller.product.resolver;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.web.controller.product.viewcommand.ImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemPropertyViewCommand;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年4月20日 下午5:15:53 
 * @version   
 */
@Component
public class ItemPropertyViewCommandResolverImpl implements
		ItemPropertyViewCommandResolver {
	
	@Autowired
	private ItemDetailManager										itemDetailManager;
	
	private static final String          KEY_PROPS_SALE		    	= "salePropCommandList";
    
    private static final String          KEY_PROPS_GENERAL 			= "generalGroupPropMap";

    

	/* 
	 * @see com.baozun.nebula.web.controller.product.resolver.ItemPropertyViewCommandResolver#resolve(java.lang.Long, java.util.Map)
	 */
	@Override
	public ItemPropertyViewCommand resolve(ItemBaseInfoViewCommand baseInfoViewCommand,
			Map<String, List<ImageViewCommand>> images) {
		
		Map<String, Object> dynamicProperty = itemDetailManager.gatherDynamicProperty(baseInfoViewCommand.getId());
		
		//TODO
		return null;
	}

}
