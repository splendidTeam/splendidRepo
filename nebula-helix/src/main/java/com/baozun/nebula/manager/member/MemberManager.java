package com.baozun.nebula.manager.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.exception.PasswordNotMatchException;
import com.baozun.nebula.exception.UserExpiredException;
import com.baozun.nebula.exception.UserNotExistsException;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.model.member.MemberCryptoguard;
import com.baozun.nebula.model.member.MemberPersonalData;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.controller.NebulaReturnResult;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

public interface MemberManager extends BaseManager{

    /**
     * 查询会员个人资料
     * 
     * @param memberId
     * @return
     */
    MemberPersonalData findMemberPersonData(Long memberId);

    /**
     * 修改密码
     * 
     * @param memberId
     * @param pwd
     * @param newPwd
     * @param reNewPwd
     * @return
     */
    boolean updatePasswd(Long memberId,String pwd,String newPwd,String reNewPwd);

    /**
     * 保存密码保护
     * 
     * @param memberCryptoguardList
     */
    public void saveCryptoguard(List<MemberCryptoguard> memberCryptoguardList);

    /**
     * 根据会员查询密码保护
     * 
     * @param memberId
     * @return
     */
    List<MemberCryptoguard> findCryptoguardList(Long memberId);

    /**
     * 保存个人资料(昵称需要重复性检查)
     * 
     * @param personData
     * @return
     */
    MemberPersonalData savePersonData(MemberPersonalData personData);

    /**
     * 验证昵称重复
     * 
     * @param nickname
     * @return
     */
    boolean checkNickname(String nickname);

    /**
     * 发送绑定邮箱的特殊url
     * 
     * @param memberId
     * @param email
     * @param path
     */
    void sendBindEmailUrl(Long memberId,String email,String path);

    /**
     * 绑定邮箱，通过加密后的校验码校验后才可以
     * 
     * @param memberId
     * @param cryptCode
     *            加密后校验码
     * @param email
     * @return
     */
    boolean bindEmail(Long memberId,String cryptCode,String email);

    /**
     * 发送绑定手机的SMS验证码
     * 
     * @param mobile
     *            手机号码
     * @param memberId
     * @return
     */
    String sendBindMobileCode(String mobile,Long memberId);

    /**
     * 绑定手机
     * 
     * @param mobile
     *            手机号码
     * @param memberId
     * @return
     */
    boolean bindMobile(String mobile,Long memberId);

    /**
     * 登录 (包含用户名、手机、邮箱)
     * 
     * @param loginName
     * @param loginPwd
     * @return
     */
    MemberCommand login(MemberFrontendCommand memberCommand) throws UserNotExistsException,UserExpiredException,PasswordNotMatchException;

    /**
     * 包含用户名、手机、邮箱
     * 
     * @param loginName
     * @return
     */
    Member findMember(String loginName);

    /**
     * 根据用户名查询MemberCommand(用户名包含登录名、登录邮箱、登录手机)
     * 
     * @return Member
     * @param loginName
     * @author 冯明雷
     * @time 2016年3月30日上午10:42:13
     */
    MemberCommand findMemberCommandByLoginName(String loginName);

    /**
     * 会员注册
     * 
     * @param member
     * @return
     */
    Member register(MemberFrontendCommand memberCommand);

    /**
     * 会员注册 <br/>
     * 保存 Member & MemberPersonalData & MemberConductCommand
     * 
     * @param memberCommand
     * @return
     */
    Member rewriteRegister(MemberFrontendCommand memberCommand);

    /**
     * 用户名
     * 
     * @param loginName
     * @return
     */
    MemberCommand findMemberByUsername(String username);

    /**
     * 根据手机号码查询用户详细信息
     * 
     * @param mobile
     * @return
     */
    MemberPersonalData findMemberPersonDataByMobile(String mobile);

    /**
     * 找出会员的全部详细信息
     * 
     * @param memberId
     * @return
     */
    MemberCommand findMemberById(Long memberId);

    /**
     * @param memberCommand
     * @return
     */
    MemberCommand saveMember(MemberCommand memberCommand);

    /**
     * 发送激活邮件
     * 
     * @param email
     */
    void sendActiveByEmail(Long memberId,String domain);

    /**
     * 验证邮件url是否有效,有效则添加会员loginEmail字段的值,验证成功后。登录成功同步cookie中购物车数据
     * 
     * @param email
     * @param activeUrl
     */
    void validEmailActiveUrl(Long memberId,String checkSum);

    /**
     * 注册绑定手机号码，绑定成功后登录，登录成功同步cookie中购物车数据
     * 
     * @param mobile
     *            手机号码
     * @param memberId
     * @return
     */
    boolean bindMobileAndSynShopppingCart(String mobile,Long memberId);

    /**
     * 查询评价内容分页，根据用户信息
     * 
     * @param page
     * @param sorts
     * @param searchParam
     * @return
     */
    Pagination<RateCommand> findItemRateListByMemberId(Page page,Sort[] sorts,Long memberId);

    /**
     * 验证邮件url是否有效,有效则添加会员loginEmail字段的值,验证成功后。登录成功同步cookie中购物车数据
     * 
     * @param email
     * @param activeUrl
     */
    boolean validEmailActiveUrl(String t,String q,String s);

    /**
     * @author 何波 @Description: 绑定用户邮箱 @param memberId void @throws
     */
    void bindMemberEmail(Long memberId,String email);

    /**
     * 检查email，mobile等是否合法,重复问题
     * 
     * @param mfc
     *            MemberFrontendCommand
     * @return
     */
    NebulaReturnResult checkRegisterData(MemberFrontendCommand mfc);

    /**
     * 设置注册会员附加信息<br/>
     * 首次生命状态,注册来源，会员类型,MemberConductCommand等
     * 
     * @param memberFrontendCommand
     * @param clientIp
     * @param registerClientIdentificationMechanisms   5.3.2.18新增参数
     */
    
    void setupMemberReference(MemberFrontendCommand memberFrontendCommand,String clientIp,String registerClientIdentificationMechanisms);

    /**
     * 根据memberIds批量查询member
     * 
     * @param ids
     * @return
     */
    List<MemberCommand> findMembersByIds(List<Long> ids);

}