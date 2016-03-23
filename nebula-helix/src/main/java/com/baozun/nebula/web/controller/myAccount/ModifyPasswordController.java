package com.baozun.nebula.web.controller.myAccount;

import com.baozun.nebula.web.controller.BaseController;

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

}
