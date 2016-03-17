package com.baozun.nebula.dao.member;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.MemberFavoritesCommand;
import com.baozun.nebula.model.member.MemberFavorites;

public interface MemberFavoritesDao extends GenericEntityDao<MemberFavorites, Long> {
	/**
	 * 查询该会员的所有收藏列表
	 * @param page
	 * @param sorts
	 * @param paraMap
	 */
	@NativeQuery(model = MemberFavoritesCommand.class)
	public Pagination<MemberFavoritesCommand> memberFavoritesList(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 查询该会员的所有收藏列表
	 * @param page
	 * @param sorts
	 * @param paraMap
	 */
	@NativeQuery(model = MemberFavoritesCommand.class)
	public Pagination<MemberFavoritesCommand> findFavoritesList(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	/**
	 * 根据itemId和会员Id查询收藏信息
	 * @param memberId
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = MemberFavorites.class)
	public MemberFavorites findMemberFavoritesByMemberIdAndItemId(@QueryParam Map<String, Long> paramMap);
	
	/**
	 * 通过收藏Id和用户Id查询收藏信息
	 * @param id
	 * @param memberId
	 * @return	:MemberFavorites
	 * @date 2014-2-12 下午05:38:40
	 */
	@NativeQuery(model = MemberFavorites.class)
	public MemberFavorites findMemberFavoritesByIdAndMemberId(@QueryParam("id") Long id, @QueryParam("memberId") Long memberId);
	
	/**
	 * 批量删除收藏
	 * @param itemIds
	 * @param memberId
	 * @return	:Integer
	 * @date 2014-2-12 下午06:06:11
	 */
	@NativeUpdate
	public Integer removeFavoritesByIds(@QueryParam("itemIds") List<Long> itemIds, @QueryParam("skuIds") List<Long> skuIds, @QueryParam("memberId") Long memberId);

	
}
