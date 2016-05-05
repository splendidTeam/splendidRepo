/**
 * Copyright (c) 2016 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.IllegalItemStateException;
import com.baozun.nebula.exception.IllegalItemStateException.IllegalItemState;
import com.baozun.nebula.manager.product.ItemDetailManager;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.sdk.command.ItemBaseCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.web.controller.product.converter.ShopdogItemImageViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.ShopdogItemPropertyCommandConverter;
import com.baozun.nebula.web.controller.product.converter.ShopdogItemViewCommandConverter;
import com.baozun.nebula.web.controller.product.converter.ShopdogSkuViewCommandConverter;
import com.baozun.nebula.web.controller.product.resolver.ItemColorSwatchViewCommandResolver;
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemColorSwatchViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemPropertyViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogItemPropertyViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogItemViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ShopdogResultCommand;
import com.feilong.core.Validator;
import com.feilong.core.date.DateUtil;
import com.feilong.tools.jsonlib.JsonUtil;



/**
 * 驻店宝商品详情页controller
 * @author xingyu.liu
 *
 */
public class ShopdogPdpController extends NebulaBasePdpController {
	
	/**
	 * log定义
	 */
	private static final Logger	LOG										= LoggerFactory.getLogger(ShopdogPdpController.class);
	
	@Autowired
	private ItemColorSwatchViewCommandResolver    colorSwatchViewCommandResolver;
	
	@Autowired
	@Qualifier("shopdogItemViewCommandConverter")
	private ShopdogItemViewCommandConverter    shopdogItemViewCommandConverter;
	
	@Autowired
	@Qualifier("shopdogItemImageViewCommandConverter")
	private ShopdogItemImageViewCommandConverter    shopdogItemImageViewCommandConverter;
	
	@Autowired
	@Qualifier("shopdogSkuViewCommandConverter")
	private ShopdogSkuViewCommandConverter shopdogSkuViewCommandConverter;

	@Autowired
	@Qualifier("shopdogItemPropertyCommandConverter")
	private ShopdogItemPropertyCommandConverter		shopdogItemPropertyCommandConverter;
	
	@Autowired
	private ItemDetailManager						detailManager;
	
	/**
	 * shopdog商品接口
	 * <p>
	 * 用于生成shopdog构造pdp页面所需要的数据。参数itemCode和barCode二选一, 如果从列表页面进入pdp，则需要传入itemCode, 如果扫码方式进入pdp，则会传入extCode。
	 * 此处的extCode对应Nebula系统中的extentionCode。所以当shopdog传入extCode时，需要先转成itemCode（或itemId）再进行统一处理。
	 * </p>
	 * 
	 * @param itemCode 商品编码
	 * @param extCode 外部对接编码
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/item.json", method = RequestMethod.GET)
	@ResponseBody
	public ShopdogResultCommand getItem(
			@RequestParam(value = "itemCode", required = false) String itemCode, 
			@RequestParam(value = "extCode", required = false) String extCode,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
				
	        ShopdogResultCommand result = new ShopdogResultCommand();
	        List<ShopdogItemViewCommand> items =null;
			if(Validator.isNotNullOrEmpty(itemCode)){
				items=  buildPdpViewCommand(itemCode);
			}else if(Validator.isNotNullOrEmpty(extCode)){
				Item item =detailManager.findItemByExtentionCode(extCode);
				if(Validator.isNotNullOrEmpty(item)){
					items=  buildPdpViewCommand(item.getCode());
				}else{
					LOG.warn("[PDP_SHOW_PDP] can not find item by extCode. extCode:{}", extCode);
				}
			}else{
				LOG.warn("[PDP_SHOW_PDP] itemCode and extCode shouldn't all be empty.");
			}
			
			result.setData(items);
			
			return result;
				
			} catch (IllegalItemStateException e) {
				
				LOG.error("[PDP_SHOW_PDP] Item state illegal. itemCode:{}, {}", itemCode, e.getState().name());
				
				throw new BusinessException("Show pdp error.");
			}
		
	}
	
	
    /**
	 * 构造PdpViewCommand
	 * @throws IllegalItemStateException 
	 */
	protected List<ShopdogItemViewCommand> buildPdpViewCommand(String itemCode) throws IllegalItemStateException{
		
		List<ShopdogItemViewCommand>  shopdogItemViewCommands = new ArrayList<ShopdogItemViewCommand>();
		
		//获取商品信息
		ShopdogItemViewCommand shopdogItemViewCommand = buildShopdogItemViewCommand(itemCode);
		shopdogItemViewCommands.add(shopdogItemViewCommand);
		
		//获取同款
		ItemBaseInfoViewCommand itemBaseInfo = getAndValidateItemBaseInfo(itemCode);
		List<ItemColorSwatchViewCommand> itemColorSwatchViewCommands = buildItemColorSwatchViewCommands(itemBaseInfo);
		if(Validator.isNotNullOrEmpty(itemColorSwatchViewCommands)){
			for(ItemColorSwatchViewCommand itemColorSwatchViewCommand:itemColorSwatchViewCommands){
				ShopdogItemViewCommand res = buildShopdogItemViewCommand(itemColorSwatchViewCommand.getItemCode());
				shopdogItemViewCommands.add(res);
			}
		}
 		
		return shopdogItemViewCommands;
	}
	
	public String getMainPicType(){
		return ItemImage.IMG_TYPE_LIST;
	}
	
	protected ShopdogItemViewCommand buildShopdogItemViewCommand(String itemCode) throws IllegalItemStateException{
		
		ItemBaseInfoViewCommand itemBaseInfo = getAndValidateItemBaseInfo(itemCode);
		
		//商品基本信息
		ShopdogItemViewCommand shopdogItemViewCommand =  shopdogItemViewCommandConverter.convert(itemBaseInfo);
		
		List<ItemImageViewCommand> images = buildItemImageViewCommand(itemBaseInfo.getId());
	    //商品全部图
		List<ShopdogItemImageViewCommand> shopdogItemImageViewCommands = shopdogItemImageViewCommandConverter.convert(buildItemImageViewCommand(itemBaseInfo.getId()));
		shopdogItemViewCommand.setAllPictures(shopdogItemImageViewCommandConverter.convert(buildItemImageViewCommand(itemBaseInfo.getId())));
		
		//主图
		shopdogItemViewCommand.setMainPicture(getMainUrls(shopdogItemImageViewCommands));
		
		//设置销售属性
		ItemPropertyViewCommand itemPropertyViewCommand =itemPropertyViewCommandResolver.resolve(itemBaseInfo, images);
		ShopdogItemPropertyViewCommand shopdogItemPropertyViewCommand =shopdogItemPropertyCommandConverter.convert(itemPropertyViewCommand);
		
		if(Validator.isNotNullOrEmpty(shopdogItemPropertyViewCommand)){
			shopdogItemViewCommand.setSalesProperties(shopdogItemPropertyViewCommand.getSalesProperties());
		}
		//sku
		shopdogItemViewCommand.setSkus(shopdogSkuViewCommandConverter.convert(buildSkuViewCommand(itemBaseInfo.getId())));
	
		return shopdogItemViewCommand;
		
	}

	private List<String> getMainUrls(List<ShopdogItemImageViewCommand> shopdogItemImageViewCommands) {
		
		return shopdogItemImageViewCommands.get(0).getImages().get(getMainPicType());
	}

	/**
	 * 获取并校验商品基本信息 
	 * @param itemBaseInfo
	 * @return
	 */
	protected ItemBaseInfoViewCommand getAndValidateItemBaseInfo(String itemCode) throws IllegalItemStateException {
		// 取得商品的基本信息
		ItemBaseInfoViewCommand itemBaseInfo = buildItemBaseInfoViewCommand(itemCode);
		
		// 商品不存在
		if (Validator.isNullOrEmpty(itemBaseInfo)) {
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item not exists. itemCode:{}.", itemCode);
            throw new IllegalItemStateException(IllegalItemState.ITEM_NOT_EXISTS);
        }
		
		if(LOG.isInfoEnabled()) {
			LOG.info("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] itemCode:{}, baseInfo:{}", itemCode, JsonUtil.format(itemBaseInfo));
		}
		
		Integer lifecycle = itemBaseInfo.getLifecycle();
		if(Item.LIFECYCLE_DELETED == lifecycle) {
			// 商品逻辑删除
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item logical deleted. itemCode:{}, lifecycle:{}.", itemCode, lifecycle);
            throw new IllegalItemStateException(IllegalItemState.ITEM_LIFECYCLE_LOGICAL_DELETED);
		} else if(Item.LIFECYCLE_UNACTIVE == lifecycle) {
			// 商品新建状态
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item status new. itemCode:{}, lifecycle:{}.", itemCode, lifecycle);
            throw new IllegalItemStateException(IllegalItemState.ITEM_LIFECYCLE_NEW);
		} else if(Item.LIFECYCLE_DISABLE == lifecycle) {
			// 商品未上架
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item status offSale. itemCode:{}, lifecycle:{}.", itemCode, lifecycle);
            throw new IllegalItemStateException(IllegalItemState.ITEM_LIFECYCLE_OFFSALE);
		}
		
		Date activeBeginTime = itemBaseInfo.getActiveBeginTime();
		if (Validator.isNotNullOrEmpty(activeBeginTime) && !DateUtil.isAfter(new Date(), activeBeginTime)) {
			// 商品未上架
 			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item before active begin time. itemCode:{}, activeBeginTime:{}.", itemCode, activeBeginTime);
            throw new IllegalItemStateException(IllegalItemState.ITEM_BEFORE_ACTIVE_TIME);
        }
		
		if(Constants.ITEM_TYPE_PREMIUMS == itemBaseInfo.getType()) {
			// 商品是赠品
			LOG.error("[PDP_BUILD_VALIDATE_ITEM_BASEINFO_COMMAND] Item is gift. itemCode:{}, type:{}.", itemCode, itemBaseInfo.getType());
            throw new IllegalItemStateException(IllegalItemState.ITEM_ILLEGAL_TYPE_GIFT);
		}
		
		return itemBaseInfo;
	}
	
	/**
	 * 构造商品基本信息
	 * @param itemCode 商品编码
	 * @return {@link ItemBaseInfoViewCommand}
	 */
	protected ItemBaseInfoViewCommand buildItemBaseInfoViewCommand(String itemCode) {
		
		ItemBaseInfoViewCommand itemBaseInfoViewCommand = new ItemBaseInfoViewCommand();
		
		ItemBaseCommand itemBaseCommand = itemDetailManager.findItemBaseInfoByCode(itemCode);
		if(itemBaseCommand != null){
			BeanUtils.copyProperties(itemBaseCommand, itemBaseInfoViewCommand);
			itemBaseInfoViewCommand.setId(itemBaseCommand.getItemId());
		}
		
		return itemBaseInfoViewCommand;
	}
	
	protected List<ItemColorSwatchViewCommand> buildItemColorSwatchViewCommands(ItemBaseInfoViewCommand baseInfoViewCommand){
		return colorSwatchViewCommandResolver.resolve(baseInfoViewCommand, itemImageViewCommandConverter);
	}
    
}
