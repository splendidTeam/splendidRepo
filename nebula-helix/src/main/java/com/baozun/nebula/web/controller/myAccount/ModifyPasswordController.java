package com.baozun.nebula.web.controller.myAccount;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.sdk.command.member.MemberCommand;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.member.form.MemberPasswordForm;

/**
 * <pre>
 * 
 * myaccount中修改密码的操作：
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

public class ModifyPasswordController extends BaseController {

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(ModifyPasswordController.class);

    @Autowired
    private SdkMemberManager sdkMemberManager;

    @Autowired
    private MemberManager memberManager;

    @Autowired
    private MemberExtraManager memberExtraManager;

    /**
     * <pre>
     * 该controller返回的是json数据
     *  修改密码 status：
     *   0（修改成功） 1（修改失败）、2（旧密码验证不通过）、3,两次输入的密码不一致
     * 
     * </pre>
     * 
     * @RequestMapping(value = "/myaccount/editPwd.json")
     * @ResponseBody
     * 
     */

    public Map<String, Object> editPwd(
	    @ModelAttribute("memberPasswordForm") MemberPasswordForm memberPasswordForm,
	    BindingResult result, Model model, HttpServletRequest request,
	    HttpServletResponse response) {
	Map<String, Object> map = new HashMap<String, Object>();

	MemberDetails memberDetails = getUserDetails(request);

	Long memberId = memberDetails.getMemberId();

	// 验证密码规则(前段js也需要校验)
	// new MemberPasswordFormValidator().validate(memberPasswordForm,
	// result);
	// if (result.hasErrors()) {
	// map.put("status", "1");
	// return map;
	// }

	// 检查旧密码是否正确
	String oldPassword = memberPasswordForm.getOldPassword();
	boolean flag = this.checkOldPassword(memberId, oldPassword);

	// 需要检查两次输入的密码是否一致，在前台通过js校验即可?
	String confirmPassword = memberPasswordForm.getConfirmPassword();

	boolean flag1 = true;
	if (confirmPassword.equals(oldPassword)) {
	    flag1 = flag1;
	} else {
	    flag1 = false;
	    map.put("status", "3");
	}

	if (flag && flag1) {
	    // 检查密码正确
	    memberPasswordForm.setMemberId(memberId);
	    try {
		// 修改密码
		boolean updatePasswd = memberManager.updatePasswd(memberId,
			oldPassword, memberPasswordForm.getNewPassword(),
			memberPasswordForm.getConfirmPassword());

		if (updatePasswd) {
		    map.put("status", "0");// 修改密码成功
		    MemberExtraManager
		} else {
		    map.put("status", "1");// 修改密码失败
		}
	    } catch (Exception e) {
		map.put("status", "1");
		e.printStackTrace();
	    }
	} else {
	    // 旧密码验证不通过
	    map.put("status", "2");
	}
	return map;
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

	// TODO 对页面传过来的密码进行解密加密，在两者进行比较
	if (password != null && StringUtils.isNotEmpty(password)) {
	    if (password.equals(oldPassword)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * 获取登录用户的详细信息
     * 
     * @param request
     * @return
     */
    protected MemberDetails getUserDetails(HttpServletRequest request) {
	return (MemberDetails) request.getSession().getAttribute(
		SessionKeyConstants.MEMBER_CONTEXT);
    }

    /**
     * 去修改密码页面
     * 
     * @RequestMapping(value = "/myaccount/editPwd.htm", method =
     *                       RequestMethod.GET)
     * 
     */
    public String toEditPasswordPage(HttpServletRequest request,
	    HttpSession session) {
	return "myaccount.editPwd";
    }

}
