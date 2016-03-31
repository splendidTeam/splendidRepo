package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.command.MemberConductCommand;
import com.baozun.nebula.command.MemberFavoritesCommand;
import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.command.coupon.CouponCommand;
import com.baozun.nebula.command.product.ConsultantCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberCryptoguard;
import com.baozun.nebula.model.member.MemberFavorites;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.model.member.MemberGroupRelation;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.model.product.ItemRate;
import com.baozun.nebula.model.sns.Consultants;
import com.baozun.nebula.sdk.command.ItemRateCommand;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.member.MemberCommand;

public interface SdkMemberManager extends BaseManager{

	/**
	 * 根据会员id查询会员信息
	 * 
	 * @param id
	 * @return
	 */
	public MemberCommand findMemberById(Long id);

	/**
	 * 保存会员信息
	 * 
	 * @param memberCommand
	 * @return
	 */
	public MemberCommand saveMember(MemberCommand memberCommand);

	/**
	 * 根据loginName查询会员信息
	 * 
	 * @param loginName
	 * @return
	 */
	public MemberCommand findMemberByLoginName(String loginName);

	/**
	 * 根据登录邮箱查询会员信息
	 * 
	 * @param loginName
	 * @return
	 */
	public MemberCommand findMemberByLoginEmail(String loginEmail);

	/**
	 * 根据登录手机查询会员信息
	 * 
	 * @param loginName
	 * @return
	 */
	public MemberCommand findMemberByLoginMobile(String loginMobile);

	/**
	 * 根据thirdPartyIdentify,source查询会员信息
	 * 
	 * @param thirdPartyIdentify
	 * @param source
	 * @return
	 */
	public Member findThirdMemberByThirdIdAndSource(String thirdPartyIdentify,Integer source);

	/**
	 * 根据用户名和密码查询会员信息
	 * 
	 * @param loginName
	 * @param password
	 * @return
	 */
	public MemberCommand findMemberByLoginNameAndPasswd(String loginName,String password);

	/**
	 * 分页查询优惠券信息
	 * 
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	public Pagination<CouponCommand> findCouponCommandList(Page page,Sort[] sorts,Map<String, Object> searchParam);

	/**
	 * 根据id查询联系人地址信息
	 * 
	 * @param id
	 * @return
	 */
	// public ContactCommand findContactById(Long id);

	/**
	 * 保存联系人地址信息
	 * 
	 * @param contactCommand
	 * @return
	 */
	public ContactCommand saveContactCommand(ContactCommand contactCommand);

	/**
	 * 根据会员id查询联系人地址信息
	 * 
	 * @param memberId
	 * @return
	 */
	public List<ContactCommand> findAllContactListByMemberId(Long memberId);

	/**
	 * 根据会员Id查询收货地址列表，支持分页和排序
	 * 
	 * @param page
	 * @param sorts
	 * @param memberId
	 * @return
	 */
	public Pagination<ContactCommand> findContactsByMemberId(Page page,Sort[] sorts,Long memberId);

	/**
	 * 查询该会员的所有收藏列表
	 * 
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	public Pagination<MemberFavoritesCommand> memberFavoritesList(Page page,Sort[] sorts,Map<String, Object> searchParam);

	/**
	 * 根据itemId和会员Id查询收藏信息
	 * 
	 * @param itemId
	 * @param memberId
	 * @return
	 */
	public MemberFavoritesCommand findMemberFavoritesByMemberIdAndItemId(Long itemId,Long memberId,Long skuId);

	/**
	 * 删除该收藏信息
	 * 
	 * @param memberFavoritesCommand
	 */
	public void deleteMemberFavorites(MemberFavoritesCommand memberFavoritesCommand);

	/**
	 * 保存用户行为信息
	 * 
	 * @param memberConductCommand
	 * @return
	 */
	public MemberConductCommand saveMemberConduct(MemberConductCommand memberConductCommand);

	/**
	 * 保存用户详细信息
	 * 
	 * @param memberPersonalDataCommand
	 * @return
	 */
	public MemberPersonalDataCommand saveMemberPersonDataCommand(MemberPersonalDataCommand memberPersonalDataCommand);

	public MemberConductCommand findMemberConductCommandById(Long id);

	public MemberPersonalDataCommand findMemberPersonDataCommandById(Long id);

	public Integer updateContactIsDefault(Long memberId,Long contactId,boolean isDefault);

	/**
	 * 根据会员id和couponNo查询优惠券信息
	 * 
	 * @param memberId
	 * @param couponNo
	 * @return
	 */
	public CouponCommand findByMemberIdAndCardNo(Long memberId,String couponNo);

	/** sprint7新增 **/

	/**
	 * 注册会员
	 * 
	 * @param loginName
	 * @param email
	 * @param mobile
	 * @param passwd
	 * @param repasswd
	 * @return
	 */
	public Member register(Member sourceMember);

	/**
	 * 重写上面的注册方法<br/>
	 * 参数Member中的pwd是明文，需要处理为密文
	 * 
	 * @param member
	 * @return
	 */
	Member rewriteRegister(Member member);

	/**
	 * 验证用户名重复
	 * 
	 * @param loginName
	 * @return
	 */
	public boolean validUserName(String username);

	/**
	 * 验证登录邮箱重复
	 * 
	 * @param email
	 * @return
	 */
	public boolean validEmail(String email);

	/**
	 * 验证登录手机重复
	 * 
	 * @param mobile
	 * @return
	 */
	public boolean validMobile(String mobile);

	/**
	 * 查询会员密码保护信息列表
	 * 
	 * @param memberId
	 * @return
	 */
	public List<MemberCryptoguard> findCryptoguardList(Long memberId);

	/**
	 * 发送邮箱的重置密码url
	 * 
	 * @param memberId
	 * @return TODO
	 */
	public String sendPassResetCodeByEmail(Long memberId);

	/**
	 * 发送短信的重置密码验证码
	 * 
	 * @param memberId
	 * @return
	 */
	public String sendPassResetCodeBySms(Long memberId);

	/**
	 * 验证密保
	 * 
	 * @param cryptoguardList
	 * @return
	 */
	public boolean checkCryptoguard(List<MemberCryptoguard> cryptoguardList);

	/**
	 * 验证邮箱重置密码url
	 * 
	 * @param code
	 *            校验码
	 * @param memberId
	 *            会员id
	 * @return
	 */
	public boolean checkEmailUrl(String code,Long memberId);

	/**
	 * 查询会员个人资料
	 * 
	 * @param memberId
	 * @return
	 */
	public MemberPersonalData findMemberPersonData(Long memberId);

	/**
	 * 重置密码
	 * 
	 * @param memberId
	 * @param newPwd
	 * @return
	 */
	public boolean resetPasswd(Long memberId,String newPwd);

	/**
	 * 修改密码
	 * 
	 * @param memberId
	 * @param pwd
	 * @param newPwd
	 * @param reNewPwd
	 * @return
	 */
	public boolean updatePasswd(Long memberId,String pwd,String newPwd,String reNewPwd);

	/**
	 * 保存密码保护
	 * 
	 * @param memberCryptoguard
	 */
	public void saveCryptoguard(MemberCryptoguard memberCryptoguard);

	/**
	 * 删除密码保护
	 * 
	 * @param memberId
	 * @return
	 */
	public void removeCryptoguardByMemberId(Long memberId);

	/**
	 * 保存个人资料(昵称需要重复性检查)
	 * 
	 * @param personData
	 * @return
	 */
	public MemberPersonalData savePersonData(MemberPersonalData personData);

	/**
	 * 验证昵称重复
	 * 
	 * @param nickname
	 * @return
	 */
	public boolean checkNickname(String nickname);

	/**
	 * 发送绑定邮箱的特殊url
	 * 
	 * @param memberId
	 * @param email
	 * @param domain
	 */
	public void sendBindEmailUrl(Long memberId,String email,String domain);

	/**
	 * 绑定邮箱，通过加密后的校验码校验后才可以
	 * 
	 * @param memberId
	 * @param checkSum
	 * @param email
	 * @return
	 */
	public boolean bindEmail(Long memberId,String checkSum,String email);

	/**
	 * 发送绑定手机的SMS验证码
	 * 
	 * @param mobile
	 *            手机号码
	 * @param memberId
	 * @return
	 */
	public String sendBindMobileCode(String mobile,Long memberId);

	/**
	 * 绑定手机
	 * 
	 * @param mobile
	 *            手机号码
	 * @param memberId
	 * @return
	 */
	public boolean bindMobile(String mobile,Long memberId);

	/**
	 * 分页查询联系人地址信息 从源码上看，目前这个方法和下面的方法 findContactList 的实现和参数都是完全一样的，因此可以删除一个
	 * 
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	@Deprecated
	public Pagination<ContactCommand> findContactCommandByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> searchParam);

	/**
	 * 查询收货人地址列表
	 * 
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	public Pagination<ContactCommand> findContactList(Page page,Sort[] sorts,Map<String, Object> searchParam);

	/**
	 * 创建或修改收货人地址
	 * 
	 * @param contact
	 * @return
	 */
	public ContactCommand createOrUpdateContact(ContactCommand contact);

	/**
	 * 删除收货人地址
	 * 
	 * @param contactId
	 * @param memberId
	 * @return
	 */
	public Integer removeContactById(Long contactId,Long memberId);

	/**
	 * 查询单个收货人地址
	 * 
	 * @param contactId
	 * @return
	 * @deprecated {@link com.baozun.nebula.sdk.manager.SdkMemberManager#findContactById(Long, Long)}
	 */
	public ContactCommand findContactById(Long contactId);

	/**
	 * 查询单个收货人地址
	 * 
	 * @param contactId
	 * @param memberId
	 * @return
	 */
	public ContactCommand findContactById(Long contactId,Long memberId);

	/**
	 * 查询该会员的所有收藏列表
	 * 
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	public Pagination<MemberFavoritesCommand> findFavoritesList(Page page,Sort[] sorts,Map<String, Object> searchParam);

	/**
	 * 创建收藏
	 * 
	 * @param memberId
	 *            会员Id
	 * @param itemId
	 *            商品id
	 * @param skuId
	 *            skuid
	 * @return
	 */
	public MemberFavoritesCommand createFavorites(Long memberId,Long itemId,Long skuId);

	/**
	 * 批量删除收藏 (在实现的时候要注意验证会员id 是否正确)
	 * 
	 * @param itemIds
	 *            要商品id
	 * @param skuIds
	 *            要删除的skuId集合
	 * @param memberId
	 *            会员Id
	 * @return 受影响的行数
	 */
	public Integer removeFavoritesByIds(List<Long> itemIds,List<Long> skuIds,Long memberId);

	/**
	 * 查询评价列表，评价列表包含了商品订单行、评价情况
	 * 
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	public Pagination<ItemRateCommand> findItemRateList(Page page,Sort[] sorts,Map<String, Object> searchParam);

	/**
	 * 查看评价详情
	 * 
	 * @param id
	 * @return
	 */
	public RateCommand findItemRateById(Long rateId);

	/**
	 * 创建评价
	 * 
	 * @param itemRateCommand
	 * @return
	 */
	public ItemRate createRate(RateCommand itemRateCommand);

	/**
	 * 检查商品交易是否完成根据用户名和商品itemId
	 * 
	 * @param itemRateCommand
	 * @return
	 */
	public List<OrderLineCommand> findOrderLineCompletionByItemIdOrUserId(Long memberId,Long itemId);

	/**
	 * 查询咨询列表(包含咨询以及对应商品的信息)
	 * 
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	public Pagination<ConsultantCommand> findConsultantsList(Page page,Sort[] srots,Map<String, Object> searchParam);

	/**
	 * 创建咨询
	 * 
	 * @param cmd
	 * @return
	 */
	public Consultants createConsultants(Consultants consultants);

	/**
	 * 根据收藏id和会员id查询收藏信息
	 * 
	 * @param id
	 * @param memberId
	 * @return
	 */
	public MemberFavorites findMemberFavoritesByIdAndMemberId(Long id,Long memberId);

	/**
	 * 包含用户名、手机、邮箱
	 * 
	 * @param loginName
	 * @return
	 */
	public Member findMember(String loginName);

	/**
	 * 根据手机号码查询个人详细信息
	 * 
	 * @param mobile
	 * @return
	 */
	public MemberPersonalData findMemberPersonDataByMobile(String mobile);

	/**
	 * 发送激活链接邮件
	 * 
	 * @param activeEmailUrl
	 */
	public void sendActiveByEmail(Long memberId,String domain);

	/**
	 * 校验激活链接
	 * 
	 * @param memberId
	 * @param checkSum
	 */
	public void validEmailActiveUrl(Long memberId,String checkSum);

	/**
	 * 根据会员id查询出所属分组
	 * 
	 * @param memberId
	 * @return
	 */
	public List<MemberGroupRelation> findMemberGroupRelationListByMemberId(Long memberId);

	/**
	 * 批量查询会员分组的list
	 * 
	 * @param ids
	 * @return
	 */
	public List<MemberGroup> findMemberGroupListByIds(List<Long> ids);

	/**
	 * 查询评价内容分页，根据用户信息
	 * 
	 * @param page
	 * @param sorts
	 * @param searchParam
	 * @return
	 */
	public Pagination<RateCommand> findItemRateListByMemberId(Page page,Sort[] sorts,Long memberId);

	public Set<String> getMemComboIdsByGroupIdMemberId(List<Long> groupIds,Long memberId);

	/**
	 * 根据会员id更新他对应的groupId
	 * 
	 * @param memberId
	 * @param groupId
	 * @return
	 */
	int updateMemberGroupIdById(Long memberId,Long groupId);
}
