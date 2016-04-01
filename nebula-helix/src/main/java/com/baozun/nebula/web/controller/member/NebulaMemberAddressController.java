/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.member;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.Contact;
import com.baozun.nebula.sdk.manager.SdkMemberManager;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.bind.LoginMember;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.PageForm;
import com.baozun.nebula.web.controller.member.converter.MemberAddressViewCommandConverter;
import com.baozun.nebula.web.controller.member.form.MemberAddressForm;
import com.baozun.nebula.web.controller.member.validator.MemberAddressFormValidator;
import com.baozun.nebula.web.controller.member.viewcommand.MemberAddressViewCommand;

import loxia.dao.Pagination;

/**
 * 会员地址簿相关的控制器，里面主要控制如下操作：
 * showMemberAddress ：显示地址簿列表
 * addMemberAddress： 新增地址
 * deleteMemberAddress：删除地址
 * setDefaultAddress：设置默认地址
 * updateMemberAddress:更细地址
 * 
 * 并没有提供查看地址明细方法，因为假定在显示地址簿时就已获取所有相关信息，不用再读取一遍。如果商城需要，自己重载。
 * 
 * @author hengheng.wang
 *
 */
public class NebulaMemberAddressController extends BaseController {

	/**
	 * log 定义
	 */
	private static final Logger LOG = LoggerFactory.getLogger(NebulaMemberAddressController.class);

	/* Model 对应的键值定义 */
	public static final String MODEL_KEY_MEMBER_ADDRESS_LIST = "memberAddressList";
	
	public static final String MODEL_KEY_MEMBER_ADDRESS = "memberAddress";

	/* View 的默认定义 */
	public static final String VIEW_MEMBER_ADDRESS_LIST = "member.address.list";

	/**
	 * 会员业务管理类
	 */
	@Autowired
	private MemberManager memberManager;
	
	/**
	 * 
	 */
	@Autowired
	private SdkMemberManager sdkMemberManager;

	@Autowired
	@Qualifier("memberAddressViewCommandConverter")
	private MemberAddressViewCommandConverter memberAddressViewCommandConverter;
	
	/**
	 * 地址信息Form的校验器
	 */
	@Autowired
	@Qualifier("memberAddressFormValidator")
	private MemberAddressFormValidator memberAddressFormValidator;

	/**
	 * 显示会员地址信息，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/address/list", method =
	 *                       RequestMethod.GET)
	 * @NeedLogin
	 * 
	 * @param memberDetails
	 * @param httpRequest
	 * @param httpResponse
	 * @param model
	 * @return
	 */
	public String showMemberAddress(@LoginMember MemberDetails memberDetails, @ModelAttribute("page") PageForm pageForm,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) {
		// 因为有NeedLogin控制，进来的一定是已经登录的有效用户
		assert memberDetails != null : "Please Check NeedLogin Annotation";

		LOG.info("[MEM_VIEW_ADDRESS] {} [{}] \"\"", memberDetails.getLoginName(), new Date());
		// 获取会员地址信息
		// 方法调用从MemberContactManager中拿了出来，直接使用了SdkMemberManager中新增的方法
		// 因此MemberContactManager中相关方法应该被标注为过期。另外要注意这里的方法调用会员Id应该是默认值，因此不能使用后端查询用的方法
		
		// 疑问：pageForm是否需要判断空？
		Pagination<ContactCommand> contacts = sdkMemberManager.findContactsByMemberId(
				pageForm.getPage(), pageForm.getSorts(),memberDetails.getMemberId());

		model.addAttribute(MODEL_KEY_MEMBER_ADDRESS_LIST, memberAddressViewCommandConverter.convert(contacts));

		return VIEW_MEMBER_ADDRESS_LIST;
	}	

	/**
	 * 新增收货地址，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/address/add", method =
	 *                       RequestMethod.POST)
	 * @NeedLogin
	 * 
	 * @param memberDetails
	 * @param memberAddressForm
	 * @param httpRequest
	 * @param httpResponse
	 * @param model
	 * @return
	 */
	public NebulaReturnResult addMemberAddress(@LoginMember MemberDetails memberDetails,
			@ModelAttribute("memberAddress") MemberAddressForm memberAddressForm, BindingResult bindingResult, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, Model model) {
		// 因为有NeedLogin控制，进来的一定是已经登录的有效用户
		assert memberDetails != null : "Please Check NeedLogin Annotation";

		DefaultReturnResult defaultReturnResult = DefaultReturnResult.SUCCESS;
		
		LOG.info("[MEM_ADD_ADDRESS] {} [{}] \"待新增地址信息的用户Id{}\"", memberDetails.getLoginName(), new Date(),memberDetails.getMemberId());
		
		LOG.info("[MEM_ADD_ADDRESS] 校验对象memberAddressForm的必需字段  --start");
		//校验过程		
		memberAddressFormValidator.validate(memberAddressForm, bindingResult);
		if(bindingResult.hasErrors()){
			NebulaReturnResult nebulaReturnResult = getResultFromBindingResult(bindingResult);
			return nebulaReturnResult;
		}	
		LOG.info("[MEM_ADD_ADDRESS] 校验对象memberAddressForm的必需字段  --end");
		//Form转contact
		ContactCommand contact = memberAddressForm.toContactCommand();
		contact.setMemberId(memberDetails.getMemberId());
		ContactCommand contactCommand = sdkMemberManager.saveContactCommand(contact);
		// 这里将编辑好的Address作为返回值放入返回对象的returnObject中	
		MemberAddressViewCommand memberAddressViewCommand = memberAddressViewCommandConverter.convert(contactCommand);
		model.addAttribute(MODEL_KEY_MEMBER_ADDRESS,memberAddressViewCommand);		
		return defaultReturnResult;		
	}
	
	
	
	/**
	 * 更新收货地址
	 * @NeedLogin
	 * 
	 * @param memberDetails
	 * @param memberAddressForm
	 * @param bindingResult
	 * @param httpRequest
	 * @param httpResponse
	 * @param model
	 * @return
	 */
	public NebulaReturnResult updateMemberAddress(@LoginMember MemberDetails memberDetails,
			@ModelAttribute("memberAddress") MemberAddressForm memberAddressForm, BindingResult bindingResult, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, Model model) {
		// 因为有NeedLogin控制，进来的一定是已经登录的有效用户
		assert memberDetails != null : "Please Check NeedLogin Annotation";

		DefaultReturnResult defaultReturnResult = DefaultReturnResult.SUCCESS;

		LOG.info("[MEM_UPDATE_ADDRESS] {} [{}] \"待更新地址信息的用户Id{}\"", memberDetails.getLoginName(), new Date(),memberDetails.getMemberId());
		
		//校验过程
		LOG.info("[MEM_UPDATE_ADDRESS] 校验对象memberAddressForm的必需字段  --start");
		memberAddressFormValidator.validate(memberAddressForm, bindingResult);
		if(bindingResult.hasErrors()){
			NebulaReturnResult nebulaReturnResult = getResultFromBindingResult(bindingResult);
			return nebulaReturnResult;
		}
		LOG.info("[MEM_UPDATE_ADDRESS] 校验对象memberAddressForm的必需字段  --end");
		//查询出原来的地址对象
		ContactCommand command=sdkMemberManager.findContactById(memberAddressForm.getId(),memberDetails.getMemberId());
		if(null == command) {
			DefaultResultMessage defaultResultMessage = new DefaultResultMessage();
			defaultReturnResult.setResult(false);
			defaultResultMessage.setMessage(getMessage("memberaddress.emptyaddress"));
			defaultReturnResult.setResultMessage(defaultResultMessage);
			return defaultReturnResult;
		}
		//Form转contact
		ContactCommand contact = memberAddressForm.toContactCommand(command);
		//另外字段添加
		contact.setMemberId(memberDetails.getMemberId());
		contact.setId(memberAddressForm.getId());		
		ContactCommand contactCommand = sdkMemberManager.saveContactCommand(contact);
		// 这里将编辑好的Address作为返回值放入返回对象的returnObject中
		MemberAddressViewCommand memberAddressViewCommand = memberAddressViewCommandConverter.convert(contactCommand);
		model.addAttribute(MODEL_KEY_MEMBER_ADDRESS,memberAddressViewCommand);
		return defaultReturnResult;		
	}

	/**
	 * 设置默认地址，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/address/setdefault", method =
	 *                       RequestMethod.GET)
	 * @NeedLogin
	 * 
	 * @param memberDetails
	 * @param addressId
	 * @param httpRequest
	 * @param httpResponse
	 * @param model
	 * @return
	 */
	public NebulaReturnResult setDefaultAddress(@LoginMember MemberDetails memberDetails,
			@ModelAttribute("addressId") Long addressId, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, Model model) {
		// 因为有NeedLogin控制，进来的一定是已经登录的有效用户
		assert memberDetails != null : "Please Check NeedLogin Annotation";

		DefaultReturnResult defaultReturnResult = DefaultReturnResult.SUCCESS;

		LOG.info("[MEM_DEFAULT_ADDRESS] {} [{}] \"待修改的地址Id{}\"", memberDetails.getLoginName(), new Date(),addressId);
		//校验addressId有效
		LOG.info("[MEM_DEFAULT_ADDRESS] 校验设置默认地址的id是否属于这个会员  --start");
		ContactCommand contact=sdkMemberManager.findContactById(addressId,memberDetails.getMemberId());
		if(null == contact) {
			DefaultResultMessage defaultResultMessage = new DefaultResultMessage();
			defaultReturnResult.setResult(false);
			defaultResultMessage.setMessage(getMessage("memberaddress.emptyaddress"));
			defaultReturnResult.setResultMessage(defaultResultMessage);
			return defaultReturnResult;
		}
		LOG.info("[MEM_DEFAULT_ADDRESS] 校验设置默认地址的id是否属于这个会员  --end");
		sdkMemberManager.updateContactIsDefault(memberDetails.getMemberId(),addressId,Contact.ISDEFAULT);
		return defaultReturnResult;
	}

	/**
	 * 删除地址，默认推荐配置如下
	 * 
	 * @RequestMapping(value = "/member/address/remove", method =
	 *                       RequestMethod.GET)
	 * @NeedLogin
	 * 
	 * @param memberDetails
	 * @param addressId
	 * @param httpRequest
	 * @param httpResponse
	 * @param model
	 * @return
	 */
	public NebulaReturnResult deleteMemberAddress(@LoginMember MemberDetails memberDetails,
			@ModelAttribute("addressId") Long addressId, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, Model model) {
		// 因为有NeedLogin控制，进来的一定是已经登录的有效用户
		assert memberDetails != null : "Please Check NeedLogin Annotation";

		//校验addressId有效，验证方法是直接删除对应会员的地址，如果删除成功说明地址Id有效，否则无效
		LOG.debug("Try to delete address[{}] for member{}", addressId, memberDetails.getLoginName());
		Integer result = sdkMemberManager.removeContactById(addressId,memberDetails.getMemberId());
		if(1 == result) {
			LOG.debug("Delete address[{}] for member{} success", addressId, memberDetails.getLoginName());
			LOG.info("[MEM_ADDRESS_DELETE] {} [{}] \"Address[{}] is deleted.\"", memberDetails.getLoginName(), new Date(), addressId);
			return DefaultReturnResult.SUCCESS;
		}else if(0 == result){
			LOG.debug("Delete address[{}] for member{} failed. no record found", addressId, memberDetails.getLoginName());
			DefaultReturnResult returnResult= new DefaultReturnResult();
			DefaultResultMessage defaultResultMessage = new DefaultResultMessage();
			returnResult.setResult(false);
			defaultResultMessage.setMessage(getMessage("memberaddress.emptyaddress"));
			returnResult.setResultMessage(defaultResultMessage);
			return returnResult;
		}else{
			throw new RuntimeException("Should not happen");
		}		
	}
}
