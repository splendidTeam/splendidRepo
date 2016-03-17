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

import java.util.List;

import loxia.dao.Page;
import loxia.dao.Pagination;

import com.baozun.nebula.command.MemberFavoritesCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.member.MemberFavorites;


/**
 * @author chenguang.zhou
 * @date 2014-2-12 下午06:32:58
 */
public interface MemberFavoritesManager extends BaseManager{
	/**
	 * 通过收藏Id和用户Id查询收藏信息
	 * @param id
	 * @return	:MemberFavorites
	 * @date 2014-2-12 下午05:29:29
	 */
	public MemberFavorites findMemberFavoritesByIdAndMemberId(Long id, Long memberId);
	
	/**
	 * 通过商品Id和用户Id删除收藏(单个)
	 * @param itemId
	 * @param memberId	
	 * @date 2014-2-13 上午09:16:05
	 * @deprecated {@link com.baozun.nebula.manager.member.MemberFavoritesManager#removeFavoritesById(Long, Long, Long)}
	 *	不支持skuId级别的收藏删除
	 */
	public void removeFavoritesById(Long itemId, Long memberId);

	/**
	 * 通过商品Id和用户Id删除收藏(单个)
	 * @param itemId
	 * @param skuId
	 * 			到款到色的收藏时, 传skuId
	 * @param memberId	
	 * @date 2014-2-13 上午09:16:05
	 */
	public Integer removeFavoritesById(Long itemId, Long skuId, Long memberId);
	
	/**
	 * 通过商品Id集合和用户Id删除收藏(批量)
	 * @param itemIdList
	 * @param memberId	
	 * @date 2014-2-12 下午06:49:10
	 * @deprecated {@link com.baozun.nebula.manager.member.MemberFavoritesManager#removeFavoritesByIds(List, List, Long)} 
	 * 不支持skuId级别的收藏删除
	 */
	public void removeFavoritesByIds(List<Long> itemIds, Long memberId);

	/**
	 * 通过商品Id集合和用户Id删除收藏(批量)
	 * @param itemIdList
	 * @param skuIds
	 * 			到款到色的收藏时, 传skuId集合, 否则null
	 * @param memberId	
	 * @date 2014-2-12 下午06:49:10
	 */
	public Integer removeFavoritesByIds(List<Long> itemIds, List<Long> skuIds, Long memberId);
	
	/**
	 * 根据会员Id查询收藏
	 * @param page
	 * @param memberId
	 * @param beginTime
	 * @param endTime
	 * @return	:Pagination<MemberFavoritesCommand>
	 * @date 2014-2-13 下午02:59:06
	 * @deprecated {@link com.baozun.nebula.manager.member.MemberFavoritesManager#findFavoritesList(Page, Long, String, String, String)}
	 * 	商品数据类型是列表页图片
	 */
	public Pagination<MemberFavoritesCommand> findFavoritesList(Page page,Long memberId, String beginTime, String endTime);
	
	/**
	 * 根据会员Id查询收藏
	 * @param page
	 * @param memberId
	 * @param beginTime
	 * @param endTime
	 * @param imageType : 商品图片类型
	 * @return	:Pagination<MemberFavoritesCommand>
	 * @date 2014-2-13 下午02:59:06
	 */
	public Pagination<MemberFavoritesCommand> findFavoritesList(Page page,Long memberId, String beginTime, String endTime, String imageType);
	
	/**
	 * 添加收藏
	 * @param memberId
	 * @param itemId
	 * @param skuId
	 * @param properties
	 * @param propertiesName
	 * @return	:MemberFavorites
	 * @date 2014-2-12 下午06:47:38
	 */
	public MemberFavoritesCommand createFavorites(Long memberId, Long itemId, Long skuId);
	
}
