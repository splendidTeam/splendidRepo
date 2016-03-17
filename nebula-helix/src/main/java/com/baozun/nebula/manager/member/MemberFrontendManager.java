package com.baozun.nebula.manager.member;

import java.util.List;

import loxia.dao.Page;
import loxia.dao.Pagination;

import com.baozun.nebula.command.CommentLogCommand;
import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.command.MemberFavoritesCommand;
import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.command.coupon.CouponCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberFavorites;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.command.MemberFrontendCommand;

public interface MemberFrontendManager extends BaseManager {
	
	/**
	 * 找出会员的全部详细信息
	 * 
	 * @param memberId
	 * @return
	 */

	public MemberCommand findMemberById(Long memberId);
	
	/***
	 * 根据
	 * @param page
	 * @param sorts
	 * @param memberId
	 * @return
	 */
	public Pagination<SalesOrderCommand>   memberOrderList(Page page,Long memberId);
	

	/**
	 * 会员注册
	 * @param member
	 */
	public MemberFrontendCommand registerMember(MemberFrontendCommand memberFrontendCommand);
	
	/**
	 * 验证邮箱是否已经注册
	 * @param loginEmail
	 * @return
	 */
	public MemberFrontendCommand findFrontendMemberLoginEmail(String loginEmail);
	
	/**
	 * 登录
	 * @param loginName
	 * @param password
	 * @return
	 */
	public MemberFrontendCommand loginSystem(MemberFrontendCommand memberFrontendCommand);
	
	public MemberFrontendCommand findFrontendMemberLoginName(String loginName);

	/**
	 * 查询该订单号的详细信息
	 * @param orderCode
	 * @return
	 */
	public SalesOrderCommand memberOrderSkip(String orderCode);
	
	/**
	 * 根据orderId 获取物流信息
	 * @param orderId
	 */
	public LogisticsCommand findLogisticInfo(Long orderId);
	
	/**
	 * 根据会员Id查询优惠劵
	 * @param memberId 
	 */
	public Pagination<CouponCommand> memberCouponList(Page page,Long memberId);
	
	/**
	 * 保存联系人资料
	 * @param contactCommand
	 * @return
	 */
	public ContactCommand saveOrUpdateContact(ContactCommand contactCommand);
	
	/**
	 * 根据memberId查询联系人信息
	 * @param memberId
	 * @return
	 */
	public List<ContactCommand> findContactInfos(Long memberId);
	
	/**
	 * 根据id查询contact info
	 * @param id
	 * @return
	 */
	public ContactCommand findContactById(Long id);
	/**
	 * 根据会员Id查询优惠劵
	 * @param page
	 * @param memberId
	 * @return
	 */
	public Pagination<MemberFavoritesCommand> memberFavoritesList(Page page,Long memberId);
	/***
	 * 查找
	 * @param itemId
	 * @param memberId
	 * @return
	 */
	public MemberFavorites findMemberFavoritesByMemberIdAndItemId(Long itemId,Long memberId, Long skuId);
	
	public void deleteMemberFavorites(MemberFavorites memberFavorites);
	
	
	
	/***
	 * 查询未评论的商品
	 * @param page 
	 * @param memberId
	 */
	public Pagination<OrderLineCommand> memberCommentList(Page page,
			Long memberId, Long orderId);


	/**
	 * 查询订单
	 * @param orderId
	 * @return
	 */
	public SalesOrderCommand findByOrderId(Long orderId);
	
	
	public Integer updateOrderLineEvalunationStatus(Long orderId,Long skuId);
	
	/**
	 * 保存或修改用户基本信息
	 * @param memberPersonalDataCommand
	 * @return
	 */
	public MemberPersonalDataCommand saveMemberPersonDataCommand(MemberPersonalDataCommand memberPersonalDataCommand);
	
	public MemberPersonalDataCommand findMemberPersonDataCommandById(Long id);
	
	public Integer updateContactIsDefault(Long memberId,Long contactId,boolean isDefault);
	
	/**
	 * 封装memberDetails对象
	 * @param member
	 */
	public MemberDetails getMemberDetails(Member member);
}
