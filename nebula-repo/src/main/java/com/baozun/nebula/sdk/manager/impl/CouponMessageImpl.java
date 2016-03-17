package com.baozun.nebula.sdk.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.annotation.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.member.MemberDao;
import com.baozun.nebula.dao.product.CouponSendUserLogDao;
import com.baozun.nebula.dao.product.InstationMessageRefDao;
import com.baozun.nebula.dao.promotion.PromotionCouponCodeDao;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.system.CouponSendUserLog;
import com.baozun.nebula.model.system.InstationSendMessageRef;
import com.baozun.nebula.model.system.PromotionAllCodeCommand;
import com.baozun.nebula.sdk.manager.CouponMessage;

/**
 * @ClassName: CouponMessageManagerImpl
 * @Description:(发送优惠券和站内信息)
 * @author GEWEI.LU
 * @date 2016年1月16日 上午10:34:03
 */
@Transactional
@Service("couponMessage")
public class CouponMessageImpl implements CouponMessage {
	@Autowired
	public CouponSendUserLogDao couponSendUserLogDao;
	@Autowired
	public InstationMessageRefDao instationMessageRefDao;

	@Autowired
	private PromotionCouponCodeDao couponCodeDao;

	@Autowired
	private MemberDao memberDao;

	@Override
	public InstationSendMessageRef saveInstationSendMessageRef(
			InstationSendMessageRef instationSendMessageRef) {
		return instationMessageRefDao.save(instationSendMessageRef);
	}

	@Override
	public List<CouponSendUserLog> findCouponSendUserLog(Long promotioncouponid) {
		// Auto-generated method stub
		return couponSendUserLogDao.findCouponSendUserLog(promotioncouponid);
	}

	@Override
	public void saveCouponSendUserLog(Long memberid,Long promotioncouponcodeid,String promotioncouponcode,Long promotioncouponid,String promotioncouponname) {
		// Auto-generated method stub
		couponSendUserLogDao.saveCouponSendUserLog(memberid, promotioncouponcodeid, promotioncouponcode, promotioncouponid, promotioncouponname);
	}

	@Override
	public Map<Object, Object> synthesizeCouponoperation(
			Map<String, String> parameter) {
		Map<Object, Object> messagemap = new HashMap<Object, Object>();
		String coupontype = parameter.get("coupontype");
		String textvalue = parameter.get("textvalue");
		String type = parameter.get("type");
		String tmpid = parameter.get("tmpid");
		String[] synthesize = textvalue.split(",");
		List<Long> couponid = new ArrayList<Long>();
		// 根据优惠券类型得到相对应的所有code信息（剩下的）
		List<PromotionAllCodeCommand> PromotionCouponCodelist = new ArrayList<PromotionAllCodeCommand>();
		// 查询已发出的优惠券
		List<CouponSendUserLog> couponSendUserLoglist = new ArrayList<CouponSendUserLog>();
		try {
			// 得到用户信息
			List<Member> MemberList = findMemberList(synthesize, type);
			// 不存在的用户
			String nullityuserlist = checkuser(MemberList, synthesize, type);
			int count = 0;
			if (MemberList != null && MemberList.size() > 0) {
				for (Member member : MemberList) {
					// 查询已发出的优惠券
					couponSendUserLoglist = couponSendUserLogDao.findCouponSendUserLog(Long.valueOf(coupontype));
					if (couponSendUserLoglist != null
							&& couponSendUserLoglist.size() > 0) {
						for (CouponSendUserLog couponSendUserLog : couponSendUserLoglist) {
							couponid.add(couponSendUserLog.getPromotioncouponcodeid());
						}
					}
					// 根据优惠券类型得到相对应的所有code信息（剩下的）
					PromotionCouponCodelist = couponCodeDao.findPromotionCouponCodeListByidlist(Long.valueOf(coupontype), couponid);
					// 发送的用户比实际可用的优惠券要大
					if (PromotionCouponCodelist.size() < (MemberList.size() - count)) {
						// 剩余的优惠券
						messagemap.put("markmes", "0");
						messagemap.put("nullityuserlist", "发送用户的数量大于效优惠券的张数");
						break;
					} else {
						if (PromotionCouponCodelist != null
								&& PromotionCouponCodelist.size() > 0) {
							// 保存发送优惠券的记录
							this.saveCouponSendUserLog(member.getId(),PromotionCouponCodelist.get(0).getId(),PromotionCouponCodelist.get(0).getCouponCode(),PromotionCouponCodelist.get(0).getCouponId(),PromotionCouponCodelist.get(0).getCouponName());
							// 发送站内信
							if (tmpid != null) {
								InstationSendMessageRef instationSendMessageRef = new InstationSendMessageRef();
								instationSendMessageRef.setMemberid(member.getId());
								instationSendMessageRef.setMessageTemplateid(Long.valueOf(tmpid));
								instationSendMessageRef.setCreateTime(new Date());
								this.saveInstationSendMessageRef(instationSendMessageRef);
							}
						}
						// 优惠券已发完
						else {
							messagemap.put("markmes", "0");
							messagemap.put("nullityuserlist", "该类型的优惠券已为空");
							break;
						}
					}
					count++;
					messagemap.put("markmes", "1");
					messagemap.put("nullityuserlist", nullityuserlist);
				}
			} else {
				couponSendUserLoglist = couponSendUserLogDao.findCouponSendUserLog(Long.valueOf(coupontype));
				if (couponSendUserLoglist != null&& couponSendUserLoglist.size() > 0) {
					for (CouponSendUserLog couponSendUserLog : couponSendUserLoglist) {
						couponid.add(couponSendUserLog.getPromotioncouponcodeid());
					}
				}
				PromotionCouponCodelist = couponCodeDao.findPromotionCouponCodeListByidlist(Long.valueOf(coupontype), couponid);
				messagemap.put("markmes", "1");
				messagemap.put("nullityuserlist","还剩余" + PromotionCouponCodelist.size() + "张优惠券");
			}
		} catch (NumberFormatException e) {
			messagemap.put("markmes", "0");
			messagemap.put("nullityuserlist","发送失败");
			e.printStackTrace();
		}
		return messagemap;
	}

	/**
	 * @Title: synthesizeCouponoperation
	 * @Description:(记录发送站内信信息操作)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2016年1月19日 上午11:31:54
	 * @author GEWEI.LU
	 */
	@Override
	public Map<Object, Object> synthesizeInstationSendMessage(
			Map<String, String> parameter) {
		Map<Object, Object> messagemap = new HashMap<Object, Object>();
		String textvalue = parameter.get("textvalue");
		String type = parameter.get("type");
		String tmpid = parameter.get("tmpid");
		String[] synthesize = textvalue.split(",");
		try {
			List<Member> MemberList = this.findMemberList(synthesize, type);
			if(MemberList!=null && MemberList.size()>0){
				// 不存在的用户
				String nullityuserlist = checkuser(MemberList, synthesize, type);
				if (MemberList != null && MemberList.size() > 0) {
					for (Member member : MemberList) {
						InstationSendMessageRef instationSendMessageRef = new InstationSendMessageRef();
						instationSendMessageRef.setMemberid(member.getId());
						instationSendMessageRef.setMessageTemplateid(Long
								.valueOf(tmpid));
						instationSendMessageRef.setCreateTime(new Date());
						this.saveInstationSendMessageRef(instationSendMessageRef);
					}
				}
				messagemap.put("nullityuserlist", nullityuserlist);
				messagemap.put("markmes", "1");
			}else{
				messagemap.put("nullityuserlist", "请输入有效的用户");
				messagemap.put("markmes", "1");
			}
			
		} catch (NumberFormatException e) {
			messagemap.put("markmes", "0");
			e.printStackTrace();
		}
		return messagemap;
	}

	/**
	 * 通过ids获取Member列表
	 * 
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public List<Member> findMemberList(String[] synthesize, String type) {
		List<Long> synthesizes = new ArrayList<Long>();
		List<String> synthestypeize = new ArrayList<String>();
		if (type.equals("1")) {
			for (String syn : synthesize) {
				synthesizes.add(Long.valueOf(syn));
			}
			return memberDao.findMemberList(synthesizes, type);
		} else {
			for (String syn : synthesize) {
				synthestypeize.add(syn);
			}
			return memberDao.findMembertypeList(synthestypeize, type);
		}
	}

	public String checkuser(List<Member> menberlist, String[] synthesize,String type) {
		String sb = "";
		try {
			// 查询有效的用
			List<String> ret = new ArrayList<String>();
			// 无效的用户
			// 前端所输入的所有用户
			List<String> synthesizelist = new ArrayList<String>();
			for (int i = 0; i < synthesize.length; i++) {
				synthesizelist.add(synthesize[i]);
			}
			if (menberlist != null && menberlist.size() > 0) {
				for (Member member : menberlist) {
					// 1ID
					if (type.equals("1")) {
						ret.add(member.getId().toString());
					}
					// 邮箱
					if (type.equals("2")) {
						ret.add(member.getLoginEmail());
					}
					// 用户名
					if (type.equals("3")) {
						ret.add(member.getLoginName());
					}
				}
				for (int i = 0; i < synthesizelist.size(); i++) {
					if (!ret.contains(synthesizelist.get(i))) {
						sb += synthesizelist.get(i) + ",";
					}
				}
			}
			if (sb.length() > 0) {
				return sb.substring(0, sb.length() - 1);
			} else {
				return sb;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sb = "保存出现异常";
			return sb;
		}
	}
}
