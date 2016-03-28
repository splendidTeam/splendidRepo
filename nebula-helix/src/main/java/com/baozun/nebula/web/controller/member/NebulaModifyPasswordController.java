package com.baozun.nebula.web.controller.member;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.manager.member.MemberExtraManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.member.form.MemberPasswordForm;
import com.baozun.nebula.web.controller.member.validator.MemberPasswordFormValidator;

/**
 * <pre>
 * 
 * 修改密码的操作：
 * 
 * 跳转到修改密码页面：
 * 未点击提交时：
 * 确认新密码栏，离焦事件触发两次输入密码一致性的比较事件，若是两次密码不相同，则页面提示 点击提交时：
 * 	1，验证输入的旧密码是否正确
 * 	2，正确：表单提交至controller，controller调用service层的方法，对对应用户下的密码进行修改。最终跳转至修改成功该页面。
 * 	3，若不正确，则继续回到修改密码页面。
 * 
 * </pre>
 * 
 * @author Wanrong.Wang
 * 
 * @Controller
 * 
 */

public class NebulaModifyPasswordController extends BaseController {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory
	    .getLogger(NebulaModifyPasswordController.class);

    /* 修改密码的页面定义 */
    public static final String VIEW_MODIFY_PASSWORD = "member.editPassword";

    @Autowired
    private SdkMemberManager sdkMemberManager;

    @Autowired
    private MemberManager memberManager;

    @Autowired
    private MemberExtraManager memberExtraManager;

    @Autowired
    @Qualifier("memberPasswordFormValidator")
    private MemberPasswordFormValidator memberPasswordFormValidator;

    /**
     * 去修改密码页面(此步骤的操作需要在登录状态下)
     * 
     * @NeedLogin
     * @RequestMapping(value = "/member/editPassword.htm", method =
     *                       RequestMethod.GET)
     * 
     */
    public String showEditPassword() {
	return VIEW_MODIFY_PASSWORD;
    }

    /**
     * 修改密码
     * 
     * @NeedLogin
     * @RequestMapping(value = "/member/editPassword.json", method =
     *                       RequestMethod.POST)
     * 
     * @param memberDetails
     * @param memberPasswordForm
     * @param result
     * @param model
     * @param request
     * @param response
     * @return
     */
    public NebulaReturnResult editPassword(
	    @LoginMember MemberDetails memberDetails,
	    @ModelAttribute("memberPasswordForm") MemberPasswordForm memberPasswordForm,
	    BindingResult result, Model model, HttpServletRequest request,
	    HttpServletResponse response) {

	DefaultReturnResult returnResult = new DefaultReturnResult();
	DefaultResultMessage defaultResultMessage = new DefaultResultMessage();

	// 获取当前登录用户的id
	Long memberId = memberDetails.getMemberId();
	LOGGER.info("[The member try to modify password,memberId: ] {} [{}]",
		memberId, new Date());

	// 校验页面数据
	memberPasswordFormValidator.validate(memberPasswordForm, result);
	if (result.hasErrors()) {
	    returnResult.setResult(false);
	    returnResult.setResultMessage(defaultResultMessage);

	    return returnResult;
	}

	// 验证旧密码是否正确（需要解密）
	String oldPassword = decryptSensitiveDataEncryptedByJs(
		memberPasswordForm.getOldPassword(), request);
	boolean flag = this.checkOldPassword(memberId, oldPassword);

	if (flag) {
	    // 若旧密码正确（确认用户未发生变化）
	    memberPasswordForm.setMemberId(memberId);
	    // 修改密码
	    boolean updatePasswd = memberManager.updatePasswd(memberId,
		    oldPassword, memberPasswordForm.getNewPassword(),
		    memberPasswordForm.getConfirmPassword());

	    // 如果修改成功
	    if (updatePasswd) {
		LOGGER.info(
			"[The member have modified password success,memberId: ] {} [{}]",
			memberId, new Date());
		// 重置记住密码的数据
		String short4 = new Date().getTime() + "";
		memberExtraManager.rememberPwd(memberId, short4);

		returnResult.setResult(true);
	    }
	} else {
	    // 旧密码错误
	    returnResult.setResult(false);
	    LOGGER.info(
		    "[The member try to modify password but failed ,memberId: ] {} [{}]",
		    memberId, new Date());
	}
	return returnResult;
    }

    /**
     * 检查旧密码是否正确
     * 
     * @param memberId
     * @param oldPassword
     * @return
     */
    protected boolean checkOldPassword(Long memberId, String oldPassword) {
	// 根据用户id查询出数据库里的密码信息
	MemberCommand memberCommand = memberManager.findMemberById(memberId);
	String password = memberCommand.getPassword();

	// 对页面传过来的密码进行解密加密，再两者进行比较
	String codePassword = EncryptUtil.getInstance().hash(oldPassword,
		memberCommand.getLoginName());

	// 确认数据库里查询到的密码的存在性
	if (password != null && StringUtils.isNotEmpty(password)) {
	    if (password.equals(codePassword)) {
		return true;
	    }
	}
	return false;
    }
}
