package com.baozun.nebula.web.controller.member;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.baozun.nebula.manager.member.MemberStatusFlowProcessor;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.model.member.MemberGroupRelation;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.event.LoginSuccessEvent;
import com.baozun.nebula.web.controller.shoppingcart.handler.ShoppingcartLoginSuccessHandler;
import com.baozun.nebula.web.interceptor.LoginForwardHandler;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.servlet.http.SessionUtil;

public abstract class NebulaAbstractLoginController extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(NebulaAbstractLoginController.class);
    
    /**
     * 5.3.2.18 登录客户端识别码规定绑定名称
     * @since 5.3.2.18
     */
    public static final String CLIENT_IDENTIFICATION_MECHANISMS = NebulaAbstractLoginController.class.getName()+"clientIdentificationMechanisms";
    
    /**
     * 5.3.2.18 注册客户端识别码规定绑定名称
     * @since 5.3.2.18
     */
    public static final String REGISTER_CLIENT_IDENTIFICATION_MECHANISMS = NebulaAbstractLoginController.class.getName()+"registerClientIdentificationMechanisms";

    @Autowired
    private MemberStatusFlowProcessor memberStatusFlowProcessor;

    @Autowired
    private ShoppingcartLoginSuccessHandler shoppingcartLoginSuccessHandler;

    @Autowired
    private SdkMemberManager sdkMemberManager;

    @Autowired
    @Qualifier("loginForwardHandler")
    private LoginForwardHandler loginForwardHandler;

    /**
     * 重置会话
     * 
     * @param request
     * @param details
     */
    protected void resetSession(HttpServletRequest request){
        SessionUtil.replaceSession(request);
    }

    /**
     * 认证成功，未激活用户也认为是认证成功，如果需要特殊处理未激活用户请重写此方法
     * 
     * @param memberDetails
     * @param request
     * @param response
     */
    protected NebulaReturnResult onAuthenticationSuccess(MemberDetails memberDetails,HttpServletRequest request,HttpServletResponse response){
        resetSession(request);
        request.getSession().setAttribute(SessionKeyConstants.MEMBER_CONTEXT, memberDetails);
        // 触发登录成功事件，用于异步处理其他的业务
        eventPublisher.publish(new LoginSuccessEvent(memberDetails, getClientContext(request, response)));

        DefaultReturnResult defaultReturnResult = new DefaultReturnResult();
        //执行Processor
        String url = memberStatusFlowProcessor.process(memberDetails, request);

        //如果url为空 获取from url 也可为空
        if (Validator.isNullOrEmpty(url)){
            shoppingcartLoginSuccessHandler.onLoginSuccess(memberDetails, request, response);
            try{
                url = loginForwardHandler.getForwardURL(request);
            }catch (UnsupportedEncodingException e){
                LOG.error("Decode backUrl faild");
            }
        }

        //返回链接
        defaultReturnResult.setReturnObject(url);

        return defaultReturnResult;
    }

    /**
     * 构造MemberDetails
     * 
     * @return MemberDetails
     * @param memberCommand
     * @author 冯明雷
     * @time 2016年4月1日下午5:07:04
     */
    protected MemberDetails constructMemberDetails(MemberCommand memberCommand,HttpServletRequest request){
        Long memberId = memberCommand.getId();

        MemberDetails memberDetails = new MemberDetails();
        PropertyUtil.copyProperties(memberDetails, memberCommand);
        //获取member status
        memberDetails.setStatus(getPendingStatus(memberCommand, request));

        memberDetails.setMemberId(memberId);
        memberDetails.setNickName(memberCommand.getLoginName());

        //分组id
        List<Long> groupIds = new ArrayList<Long>();

        //根据会员id查询出所属分组
        List<MemberGroupRelation> memberGroupRelations = sdkMemberManager.findMemberGroupRelationListByMemberId(memberId);
        if (Validator.isNotNullOrEmpty(memberGroupRelations)){
            groupIds = CollectionsUtil.getPropertyValueList(memberGroupRelations, "groupId");
        }

        //根据分组id批量查询会员分组的list
        if (groupIds != null){
            List<MemberGroup> groups = sdkMemberManager.findMemberGroupListByIds(groupIds);
            memberDetails.setGroups(groups);
        }

        //根据会员ID，会员分组，获取会员组合的Id
        Set<String> comboIds = new HashSet<String>();
        comboIds = sdkMemberManager.getMemComboIdsByGroupIdMemberId(groupIds, memberId);
        memberDetails.setMemComboList(comboIds);

        return memberDetails;
    }

    /**
     * 设置用户的分组
     * 
     * @return String
     * @param memberId
     * @param request
     * @author 冯明雷
     * @time 2016年5月18日下午1:31:26
     */
    protected void getMemComboListByMemberId(MemberDetails memberDetails,Long memberId){

    }

    /**
     * <h3>本方法需要商城去重写</h3>
     * 根据memberId查询MemberBehaviorStatus表中是否存在用户行为记录<br/>
     * 根据用户行为记录判断用户是否需要去激活，或者是绑定、完善信息等
     * 
     * @return List<String>
     * @param memberCommand
     * @author 冯明雷
     * @time 2016年4月1日下午5:10:28
     */
    protected List<String> getPendingStatus(MemberCommand memberCommand,HttpServletRequest request){
        //通过memberId 以及 各商城关注点  查询MemberBehaviorStatus表记录

        //根据MemberBehaviorStatus表中的数据去判断是否需要激活、绑定、完善信息等

        return null;
    }

}
