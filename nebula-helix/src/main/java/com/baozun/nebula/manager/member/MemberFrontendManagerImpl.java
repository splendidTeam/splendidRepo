package com.baozun.nebula.manager.member;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.command.MemberFavoritesCommand;
import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.command.coupon.CouponCommand;
import com.baozun.nebula.dao.system.ChooseOptionDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.member.Contact;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberFavorites;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.model.member.MemberGroupRelation;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.sdk.manager.OrderManager;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.sdk.utils.SdkDateUtils;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.feilong.core.DatePattern;
import com.feilong.core.text.DateFormatUtil;

@Transactional
@Service("MemberFrontendManager")
public class MemberFrontendManagerImpl implements MemberFrontendManager {

	@Autowired
	private ChooseOptionDao chooseOptionDao;
	@Autowired
	private OrderManager sdkOrderService;

	@Autowired
	private LogisticsManager logisticsManager;
	@Autowired
	private SdkMemberManager sdkMemberManager;

	/**
	 * 通过memberId获得所有会员详细信息,通过groupCode获取ChooseOption里面的字段值
	 * 
	 * @param memberID
	 * @param groupCode
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public MemberCommand findMemberById(Long memberId) {
		// TODO Auto-generated method stub
		MemberCommand member = sdkMemberManager.findMemberById(memberId);

		return member;
	}

	@Override
	public MemberFrontendCommand registerMember(MemberFrontendCommand memberFrontendCommand) {
		MemberCommand memberCommand = null;
		MemberFrontendCommand retMemberCommand = null;
		if (null == memberFrontendCommand.getId() || memberFrontendCommand.getId() == 0) {
			boolean flag = true;
			flag = validateLoginName(memberFrontendCommand.getLoginName());
			if (flag) {
				throw new BusinessException(ErrorCodes.USER_USERNAME_EXISTS);
			}
			flag = validateLoginEmail(memberFrontendCommand.getLoginEmail());
			if (flag) {
				throw new BusinessException(ErrorCodes.EMAIL_ALLREADY_REGISTER);
			}
			// memberFrontendCommand.setIsaddgroup(0);//未加入组
			memberFrontendCommand.setLifecycle(1);// 有效
			memberCommand = (MemberCommand) ConvertUtils.convertTwoObject(new MemberCommand(), memberFrontendCommand);
			memberCommand = sdkMemberManager.saveMember(memberCommand);
			if (null != memberCommand) {
				retMemberCommand = (MemberFrontendCommand) ConvertUtils.convertTwoObject(new MemberFrontendCommand(),
						memberCommand);
				memberFrontendCommand.setId(retMemberCommand.getId());
				// 保存用户的行为信息
				saveMemberConductInfo(memberFrontendCommand);
				// 保存用户的详细信息
				saveMemberPersonDataInfo(memberFrontendCommand);
				return retMemberCommand;
			}
		} else {
			memberCommand = (MemberCommand) ConvertUtils.convertTwoObject(new MemberCommand(), memberFrontendCommand);
			memberCommand = sdkMemberManager.saveMember(memberCommand);
			retMemberCommand = (MemberFrontendCommand) ConvertUtils.convertTwoObject(new MemberFrontendCommand(),
					memberCommand);
			if (null != memberCommand) {
				return retMemberCommand;
			}
		}
		return null;
	}

	private void saveMemberConductInfo(MemberFrontendCommand memberFrontendCommand) {
		MemberConductCommand memberConductCommand = null;
		memberConductCommand = sdkMemberManager.findMemberConductCommandById(memberFrontendCommand.getId());
		if (null == memberConductCommand) {
			memberConductCommand = new MemberConductCommand();
			memberConductCommand.setId(memberFrontendCommand.getId());
		}
		memberConductCommand.setLoginCount(memberFrontendCommand.getMemberConductCommand().getLoginCount());
		memberConductCommand.setRegisterIp(memberFrontendCommand.getMemberConductCommand().getRegisterIp());
		memberConductCommand.setLoginIp(memberFrontendCommand.getMemberConductCommand().getLoginIp());
		memberConductCommand.setLoginTime(memberFrontendCommand.getMemberConductCommand().getLoginTime());
		memberConductCommand.setRegisterTime(memberFrontendCommand.getMemberConductCommand().getRegisterTime());

		sdkMemberManager.saveMemberConduct(memberConductCommand);
	}

	private void saveMemberPersonDataInfo(MemberFrontendCommand retMemberCommand) {
		MemberPersonalDataCommand dataCommand = null;
		dataCommand = sdkMemberManager.findMemberPersonDataCommandById(retMemberCommand.getId());
		if (null == dataCommand) {
			dataCommand = new MemberPersonalDataCommand();
			dataCommand.setId(retMemberCommand.getId());
		}
		dataCommand.setEmail(retMemberCommand.getLoginEmail());
		dataCommand.setSex(retMemberCommand.getSex());
		dataCommand.setBirthday(DateFormatUtil.parse(retMemberCommand.getBirthday(), DatePattern.COMMON_DATE));
		dataCommand.setLocalRealName(retMemberCommand.getRealName());
		sdkMemberManager.saveMemberPersonDataCommand(dataCommand);
	}

	private boolean validateLoginName(String loginName) {
		MemberCommand memberCommand = sdkMemberManager.findMemberByLoginName(loginName);
		if (null == memberCommand) {
			return false;
		}
		return true;
	}

	private boolean validateLoginEmail(String loginEmail) {
		MemberCommand memberCommand = sdkMemberManager.findMemberByLoginEmail(loginEmail);
		if (null == memberCommand)
			return false;
		return true;
	}

	@Override
	public Pagination<SalesOrderCommand> memberOrderList(Page page, Long memberId) {

		Sort[] sorts = Sort.parse("o.CREATE_TIME desc");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("memberId", memberId);
		paraMap.put("sdkQueryType", 1);
		return sdkOrderService.findOrders(page, sorts, paraMap);
	}

	@Override
	public MemberFrontendCommand loginSystem(MemberFrontendCommand memberFrontendCommand) {
		MemberCommand memberCommand = sdkMemberManager.findMemberByLoginNameAndPasswd(
				memberFrontendCommand.getLoginName(), memberFrontendCommand.getPassword());
		if (null != memberCommand) {
			// 更新用户行为信息
			MemberConductCommand conductCommand = sdkMemberManager.findMemberConductCommandById(memberCommand.getId());
			conductCommand.setLoginIp(memberFrontendCommand.getMemberConductCommand().getLoginIp());
			conductCommand.setLoginTime(memberFrontendCommand.getMemberConductCommand().getLoginTime());
			conductCommand.setLoginCount(conductCommand.getLoginCount() + 1);
			sdkMemberManager.saveMemberConduct(conductCommand);
			return (MemberFrontendCommand) ConvertUtils.convertTwoObject(new MemberFrontendCommand(), memberCommand);
		}
		return null;
	}

	@Override
	public SalesOrderCommand memberOrderSkip(String orderCode) {
		return sdkOrderService.findOrderByCode(orderCode, 1);
	}

	@Override
	public LogisticsCommand findLogisticInfo(Long orderId) {
		LogisticsCommand logisticsCommand = logisticsManager.findLogisticsByOrderId(orderId);
		return logisticsCommand;
	}

	@Override
	public MemberFrontendCommand findFrontendMemberLoginName(String loginName) {
		MemberCommand memberCommand = sdkMemberManager.findMemberByLoginName(loginName);
		if (null != memberCommand)
			return (MemberFrontendCommand) ConvertUtils.convertTwoObject(new MemberFrontendCommand(), memberCommand);
		return null;
	}

	@Override
	public MemberFrontendCommand findFrontendMemberLoginEmail(String loginEmail) {
		MemberCommand memberCommand = sdkMemberManager.findMemberByLoginEmail(loginEmail);
		if (null != memberCommand)
			return (MemberFrontendCommand) ConvertUtils.convertTwoObject(new MemberFrontendCommand(), memberCommand);
		return null;
	}

	@Override
	public Pagination<CouponCommand> memberCouponList(Page page, Long memberId) {
		Sort[] sorts = Sort.parse("tacr.create_time");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("memberId", memberId);
		Pagination<CouponCommand> paginationCouponCommand = sdkMemberManager.findCouponCommandList(page, sorts,
				paraMap);

		List<CouponCommand> items = paginationCouponCommand.getItems();
		List<CouponCommand> newItems = new ArrayList<CouponCommand>();
		for (CouponCommand couponCommand : items) {
			Date now = new Date();
			// TODO
			// 需要修改
			if (couponCommand.getEndTime().getTime() < now.getTime()) {
				couponCommand.setStatus("已失效");
			} else {
				if (couponCommand.getUsedTime() != null) {
					couponCommand.setStatus("已使用");
				} else {
					couponCommand.setStatus("未使用");
				}
			}

			newItems.add(couponCommand);
		}
		paginationCouponCommand.setItems(newItems);
		return paginationCouponCommand;
	}

	@Override
	public ContactCommand saveOrUpdateContact(ContactCommand contactCommand) {
		return sdkMemberManager.saveContactCommand(contactCommand);
	}

	@Override
	public List<ContactCommand> findContactInfos(Long memberId) {
		return sdkMemberManager.findAllContactListByMemberId(memberId);
	}

	@Override
	public ContactCommand findContactById(Long id) {
		return sdkMemberManager.findContactById(id);
	}

	@Override
	public Pagination<MemberFavoritesCommand> memberFavoritesList(Page page, Long memberId) {
		Sort[] sorts = Sort.parse("tmf.CREATE_TIME desc");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("memberId", memberId);

		return sdkMemberManager.memberFavoritesList(page, sorts, paraMap);
	}

	@Override
	public void deleteMemberFavorites(MemberFavorites memberFavorites) {
		MemberFavoritesCommand memberFavoritesCommand = new MemberFavoritesCommand();
		memberFavoritesCommand = (MemberFavoritesCommand) ConvertUtils.convertTwoObject(memberFavoritesCommand,
				memberFavorites);
		sdkMemberManager.deleteMemberFavorites(memberFavoritesCommand);
	}

	@Override
	public Pagination<OrderLineCommand> memberCommentList(Page page, Long memberId, Long orderId) {
		Sort[] sorts = Sort.parse("ss.CREATE_TIME desc");
		Map<String, Object> searchParam = new HashMap<String, Object>();
		searchParam.put("memberId", memberId);
		if (orderId != null) {
			searchParam.put("orderId", orderId);
		}

		return sdkOrderService.findNotEvaultionOrderLineQueryMapWithPage(page, sorts, searchParam);
	}

	@Override
	public MemberFavorites findMemberFavoritesByMemberIdAndItemId(Long itemId, Long memberId, Long skuId) {
		MemberFavoritesCommand memberFavoritesCommand = sdkMemberManager.findMemberFavoritesByMemberIdAndItemId(itemId,
				memberId, skuId);
		MemberFavorites memberFavorites = null;
		if (memberFavoritesCommand != null) {
			memberFavorites = new MemberFavorites();
			memberFavorites = (MemberFavorites) ConvertUtils.convertTwoObject(memberFavorites, memberFavoritesCommand);
		}
		return memberFavorites;
	}

	@Override
	public SalesOrderCommand findByOrderId(Long orderId) {
		return sdkOrderService.findOrderById(orderId, 0);
	}

	@Override
	public Integer updateOrderLineEvalunationStatus(Long orderId, Long skuId) {
		return sdkOrderService.updateOrderLineEvaulationStatus(skuId, orderId);
	}

	@Override
	public MemberPersonalDataCommand saveMemberPersonDataCommand(MemberPersonalDataCommand memberPersonalDataCommand) {
		return sdkMemberManager.saveMemberPersonDataCommand(memberPersonalDataCommand);
	}

	@Override
	public MemberPersonalDataCommand findMemberPersonDataCommandById(Long id) {
		return sdkMemberManager.findMemberPersonDataCommandById(id);
	}

	@Override
	public Integer updateContactIsDefault(Long memberId, Long contactId, boolean isDefault) {
		Integer count = 0;
		// 将会员所有的地址设置为非默认
		count = sdkMemberManager.updateContactIsDefault(memberId, null, Contact.NOTDEFAULT);
		if (count < 1) {
			throw new BusinessException(ErrorCodes.SET_DEFAULT_ADDRESS_FAILURE);
		} else {
			// 成功后则将该contact记录设置为默认
			count = sdkMemberManager.updateContactIsDefault(null, contactId, isDefault);
		}
		return count;
	}

	/**
	 * 封装memberDetails对象
	 * 
	 * @param member
	 */
	@Override
	public MemberDetails getMemberDetails(Member member) {
		MemberPersonalData personalData = sdkMemberManager.findMemberPersonData(member.getId());
		String nickname = StringUtils.EMPTY;
		String realname = StringUtils.EMPTY;
		if (null != personalData) {
			nickname = personalData.getNickname();
			realname = personalData.getLocalRealName();
		}
		List<Long> groupIds = new ArrayList<Long>();
		List<MemberGroupRelation> memberGroupRelations = sdkMemberManager
				.findMemberGroupRelationListByMemberId(member.getId());
		if (null != memberGroupRelations && memberGroupRelations.size() > 0) {
			for (MemberGroupRelation memberGroupRelation : memberGroupRelations) {
				groupIds.add(memberGroupRelation.getGroupId());
			}
		}

		List<MemberGroup> groups = sdkMemberManager.findMemberGroupListByIds(groupIds);

		Set<String> comboIds = new HashSet<String>();
		comboIds = sdkMemberManager.getMemComboIdsByGroupIdMemberId(groupIds, member.getId());
		return new MemberDetails(member.getLoginName(), member.getLoginEmail(), member.getLoginMobile(), member.getId(),
				nickname, groups, comboIds, realname);
	}
}
