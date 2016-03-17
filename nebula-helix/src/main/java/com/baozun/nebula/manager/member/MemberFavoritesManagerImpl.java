/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
package com.baozun.nebula.manager.member;

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

import com.baozun.nebula.command.MemberFavoritesCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.member.MemberFavorites;
import com.baozun.nebula.model.product.Item;
import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkItemManager;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.solr.utils.Validator;


/**
 * @author chenguang.zhou
 * @date 2014-2-12 下午06:34:28
 */
@Transactional
@Service("MemberFavoritesManager")
public class MemberFavoritesManagerImpl implements MemberFavoritesManager{
	
	@Autowired
	private SdkMemberManager sdkMemberManager;
	
	@Autowired
	private SdkItemManager sdkItemManager;
	
	@Autowired
	private SdkSkuManager sdkSkuManager;

	@Override
	@Deprecated
	public Pagination<MemberFavoritesCommand> findFavoritesList(Page page,
			Long memberId, String beginTime, String endTime) {
		Sort[] sorts=Sort.parse("favorites.CREATE_TIME desc");
		Map<String,Object> paraMap=new HashMap<String, Object>();
		paraMap.put("memberId", memberId);
		if(Validator.isNotNullOrEmpty(beginTime)){
			paraMap.put("beginTime", beginTime);
		}
		if(Validator.isNotNullOrEmpty(endTime)){
			paraMap.put("endTime", endTime);
		}
		Pagination<MemberFavoritesCommand> memberFavoritesPage = sdkMemberManager.findFavoritesList(page, sorts, paraMap);
		
		return getItemImageByMemFavPage(memberFavoritesPage, ItemImage.IMG_TYPE_LIST);
	}

	@Override
	public Pagination<MemberFavoritesCommand> findFavoritesList(Page page,
			Long memberId, String beginTime, String endTime, String imageType) {
		Sort[] sorts=Sort.parse("favorites.CREATE_TIME desc");
		Map<String,Object> paraMap=new HashMap<String, Object>();
		paraMap.put("memberId", memberId);
		if(Validator.isNotNullOrEmpty(beginTime)){
			paraMap.put("beginTime", beginTime);
		}
		if(Validator.isNotNullOrEmpty(endTime)){
			paraMap.put("endTime", endTime);
		}
		Pagination<MemberFavoritesCommand> memberFavoritesPage = sdkMemberManager.findFavoritesList(page, sorts, paraMap);
		
		return getItemImageByMemFavPage(memberFavoritesPage, imageType);
	}
	
	@Override
	public MemberFavorites findMemberFavoritesByIdAndMemberId(Long id,Long memberId){
		// TODO Auto-generated method stub
		return sdkMemberManager.findMemberFavoritesByIdAndMemberId(id, memberId);
	}

	@Override
	public Integer removeFavoritesById(Long itemId, Long skuId, Long memberId){
		List<Long> itemIds = new ArrayList<Long>();
		List<Long> skuIds = null;
		itemIds.add(itemId);
		if(skuId != null){
			skuIds = new ArrayList<Long>();
			skuIds.add(skuId);
		}
		return sdkMemberManager.removeFavoritesByIds(itemIds, skuIds, memberId);
	}
	

	@Override
	@Deprecated
	public void removeFavoritesById(Long itemId, Long memberId) {
		List<Long> itemIds = new ArrayList<Long>();
		itemIds.add(itemId);
		sdkMemberManager.removeFavoritesByIds(itemIds, null, memberId);
	}

	@Override
	@Deprecated
	public void removeFavoritesByIds(List<Long> itemIds, Long memberId) {
		sdkMemberManager.removeFavoritesByIds(itemIds, null, memberId);
	}

	@Override
	public Integer removeFavoritesByIds(List<Long> itemIds, List<Long> skuIds, Long memberId){
		return sdkMemberManager.removeFavoritesByIds(itemIds, skuIds, memberId);
	}

	@Override
	public MemberFavoritesCommand createFavorites(Long memberId,Long itemId, Long skuId){
		// TODO Auto-generated method stub
		//验证用户是否存在
		 MemberCommand  memberCommand = sdkMemberManager.findMemberById(memberId);
		 if(memberCommand == null){
			 throw new BusinessException(ErrorCodes.USER_USER_NOTFOUND);
		 }
		//验证商品是否存在
		Item item = sdkItemManager.findItemById(itemId);
		Sku sku = sdkSkuManager.findSkuById(skuId);
		if(item == null && sku == null){
			throw new BusinessException(ErrorCodes.ITEM_NOT_EXIST);
		}
		//验证商品是是否被收藏了
		MemberFavoritesCommand memberFavoritesCommand = sdkMemberManager.findMemberFavoritesByMemberIdAndItemId(itemId, memberId, skuId);
		if(memberFavoritesCommand != null){
			throw new BusinessException(ErrorCodes.MEMBER_FAVORITES_EXISTS);
		}
		return sdkMemberManager.createFavorites(memberId, itemId, skuId);
	}
	
	/**
	 * 获取用户收藏的商品的图片
	 *
	 * @param memberFavoritesPage
	 * @return
	 */
	private Pagination<MemberFavoritesCommand> getItemImageByMemFavPage(Pagination<MemberFavoritesCommand> memberFavoritesPage, String imageType){
		List<MemberFavoritesCommand> memberFavoritesList = null;
		//有图片的收藏集合
		List<MemberFavoritesCommand> memberFavoritess = new ArrayList<MemberFavoritesCommand>();
		List<Long> itemIdList = new ArrayList<Long>();
		if(memberFavoritesPage != null && (memberFavoritesList = memberFavoritesPage.getItems()) != null && memberFavoritesList.size() > 0){
			//获得收藏商品的集合
			for(MemberFavoritesCommand memberFavoritesCommand : memberFavoritesList){
				itemIdList.add(memberFavoritesCommand.getItemId());
			}
			//获得商品的图片
			List<ItemImage> itemImageList = sdkItemManager.findItemImageByItemIds(itemIdList, imageType);
			// key: itemId|itemProperitesId, value:ItemImage对象 
			Map<String, List<ItemImage>> itemPropMap = new HashMap<String, List<ItemImage>>();
			for(ItemImage itemImage : itemImageList){
				String key = itemImage.getItemId()+"|"+itemImage.getItemProperties();
				List<ItemImage> tmpItemImageList = itemPropMap.get(key);
				if(tmpItemImageList == null){
					tmpItemImageList = new ArrayList<ItemImage>();
				}
				tmpItemImageList.add(itemImage);
				itemPropMap.put(key, tmpItemImageList);
			}
			
			for(MemberFavoritesCommand memberFavoritesCommand : memberFavoritesList){
				List<SkuProperty> skuPropertyList = memberFavoritesCommand.getSkuPropertys();
				Long itemPropertiesId = null;
				if(Validator.isNotNullOrEmpty(skuPropertyList)){
					for(SkuProperty skuProperty : skuPropertyList){
						if(skuProperty.getIsColorProp() && skuProperty.getItemProperties() != null){
							itemPropertiesId = skuProperty.getItemProperties().getId();
						}
					}
				}
				
				itemImageList = new ArrayList<ItemImage>();
				for(Map.Entry<String, List<ItemImage>> entry : itemPropMap.entrySet()){
					String key = entry.getKey();
					
					if(itemPropertiesId == null){
						if(String.valueOf(memberFavoritesCommand.getItemId()).equals(key.split("\\|")[0])){
							itemImageList.addAll(0,entry.getValue());
						}
					}else{
						if((memberFavoritesCommand.getItemId() + "|" + itemPropertiesId).equals(key)){
							itemImageList.addAll(0,entry.getValue());
						}
					}
				}
				memberFavoritesCommand.setItemImageList(itemImageList);
				memberFavoritess.add(memberFavoritesCommand);
			}
			memberFavoritesPage.setItems(memberFavoritess);
		}
		return memberFavoritesPage;
	}
}
