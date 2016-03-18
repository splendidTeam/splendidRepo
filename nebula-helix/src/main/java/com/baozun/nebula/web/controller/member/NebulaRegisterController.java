package com.baozun.nebula.web.controller.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.member.form.RegisterForm;

public class NebulaRegisterController {
	
	//去注册页面 ,"/member/register"
	public String showRegister(Model model){
		return "store.member.register";
	}
	
	//注册，“/member/register.json”
	public BackWarnEntity register(@ModelAttribute RegisterForm registerForm,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response,Model model){
		//1数据校验 validator
		
		//2.用户注册
//		memberManager.register(registerForm.toMemberFrontendCommand(), shoppingLines);
		
		//3.用户登录
		
		//4.同步购物车
		
		//5.重置session
		
		//6.builder backWarnEntity返回
		
		return new BackWarnEntity();
	}
	
}
