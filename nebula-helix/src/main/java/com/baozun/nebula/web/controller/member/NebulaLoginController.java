package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.member.form.ForgetPasswordForm;
import com.baozun.nebula.web.controller.member.form.LoginForm;

public class NebulaLoginController extends NebulaBaseLoginController{

	//去登录页面，"/member/login" GET
	public String showLogin(@LoginMember MemberDetails memberDetails,HttpServletRequest request,Model model){
		//1.校验是否已经登录
		
		//1.1如果已登录 去首页或个人中心
		
		//1.2如果未登录 先取出cookie中记录的用户名 去登录页
		
		return "store.member.login";
	}
	
	//登录,"/member/login.json" POST
	public BackWarnEntity login(@ModelAttribute LoginForm loginForm,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response,Model model){
		//1.数据校验  validator
		
		//2.登录 返回 userdetails
		
		//3.判断用户是否激活
		//3.1如果未激活 去激活页面 
		//3.2如果已激活 继续
		
		//4.重置session
		
		//5.同步购物车

		//6.builder backWarnEntity返回
		
		return new BackWarnEntity();
	}
	
	//去忘记密码页面，"/member/forgetPassword"
	public String showForgetPassword(Model model){
		return "store.member.fortgetPassword";
	}
	
	//发送验证码,"/member/forgetPassword.json"
	public BackWarnEntity forgetPassword(@ModelAttribute ForgetPasswordForm forgetPasswordForm,BindingResult bindingResult,Model model){
		//1.数据校验 validator
		
		//2.判断是手机还是邮箱，分别调用发送验证码逻辑
		
		//3.builder backWarnEntity返回
		
		return new BackWarnEntity();
	}
	
	//登出 ,"/member/loginOut.json"
	public BackWarnEntity loginOut(HttpServletRequest request){
		//1.清空session
		
		//2.builder backWarnEntity返回
		
		return new BackWarnEntity();
	}
}
