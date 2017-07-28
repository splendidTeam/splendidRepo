package com.baozun.nebula.web.controller.member;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.command.MemberCommand;
import com.baozun.nebula.command.MemberFavoritesCommand;
import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.member.MemberGroupManager;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;
import com.feilong.core.Validator;

import loxia.dao.Pagination;
import loxia.dao.Sort;



@Controller
public class MemberController extends BaseController {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(MemberController.class);

	@Autowired
	private MemberManager memberManager;
	
	@Autowired
	private MemberGroupManager memberGroupManager;
	
	/** 图片服务器地址 */
	@Value("#{meta['upload.img.domain.base']}")
	private String				customBaseUrl		= "";

	/** 前台地址 */
	@Value("#{meta['frontend.url']}")
	private String				frontendBaseUrl		= "";

	/**
	 * 页面跳转
	 * 会员管理列表
	 * @param model
	 * @return
	 */
	@RequestMapping("/member/memberList.htm")
	public String MemberList(Model model,HttpServletRequest request,HttpServletResponse response) {
		List<MemberGroup> list=memberManager.findAllMemberGroup();
		model.addAttribute("memberList", list);
		return "member/member-list";
	}
	
	
	
	


	
	/**
	 * 页面跳转
	 * 点击会员头像及其登录名跳转到会员信息页面
	 * @param model
	 * @param memberId
	 * @return
	 */
	@RequestMapping("/member/view.htm")
	public String viewMember(Model model,@RequestParam("memberId") Long memberId ,HttpServletRequest request,HttpServletResponse response)
			throws Exception{

		// 获取当前用户信息
		MemberPersonalDataCommand memberPersonalDataCommand = memberManager.findMemberById(memberId);
		model.addAttribute("memberPersonalDataCommand", memberPersonalDataCommand);
		model.addAttribute("frontendBaseUrl", frontendBaseUrl);
		model.addAttribute("customBaseUrl", customBaseUrl);
		return "member/member-detail";
	}
	
	
	
	
	
	/**
	 * 页面跳转
	 * 跳转到会员列表页面通过Json获取数据库会员信息
	 * @param model
	 * @param memberId
	 * @return
	 */
	@RequestMapping("/member/memberList.json")
	@ResponseBody
	public Pagination<MemberCommand> memberListJson(Model model,@QueryBeanParam QueryBean queryBean) {
	
		    Sort[] sorts=queryBean.getSorts();
			
			if(sorts==null||sorts.length==0){
				Sort sort=new Sort("c.register_time","desc");
				sorts=new Sort[1];
				sorts[0]=sort;
			}
		    
	        Pagination<MemberCommand> args = memberManager.findMemberListByQueryMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
	       
	   
	        return args;
	}
	
	
	/**
	 * 页面跳转
	 * 启用禁用会员
	 * @param ids
	 * @param state
	 * @return
	 */
	@RequestMapping("/member/enableOrDisableMemberByIds.json")
	@ResponseBody
	public Object enableOrDisableManyMemberByIds(
			@RequestParam("ids") String ids,
			@RequestParam("state") Integer state,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		if (StringUtils.isNotBlank(ids)){

			String[] idArray = ids.split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String sid : idArray){
				idList.add(Long.parseLong(sid));
			}
			boolean isSuccess = true;
			Integer errorCode = null;
			int flag = memberManager.enableOrDisableMemberByIds(idList, state);

			if(flag != idList.size()){
				isSuccess = false;
				errorCode = ErrorCodes.SHOP_ENABLE_DISABLE_FAIL;
				throw new BusinessException(ErrorCodes.ROWCOUNT_NOTEXPECTED, new Integer[] { flag, idList.size()});
			}
		}

		return SUCCESS;
	} 
	/**
	 * 会员地址信息列表
	 * @param model
	 * @param queryBean
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/member/memberContactList.json")
	@ResponseBody
	public Pagination<ContactCommand> memberContactJson(Model model,@RequestParam("memberId") Long memberId ,
			@QueryBeanParam QueryBean queryBean, HttpServletRequest request,
			HttpServletResponse response){ 
		Sort[] sorts = new Sort[]{};
		Pagination<ContactCommand> result =memberManager.findContactCommandByQueryMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap(),memberId);

		return result;
	}
	/**
	 * 会员收藏夹列表
	 * @param model
	 * @param queryBean
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/member/memberFavoritesList.json")
	@ResponseBody
	public Pagination<MemberFavoritesCommand> memberFavoritesJson(Model model,@RequestParam("memberId") Long memberId ,
			@QueryBeanParam QueryBean queryBean, HttpServletRequest request,
			HttpServletResponse response){
		queryBean.getParaMap().put("memberId", memberId);
		Sort[] sorts = new Sort[]{};
		Pagination<MemberFavoritesCommand> result =memberManager.memberFavoritesList(queryBean.getPage(), sorts, queryBean.getParaMap(),memberId);

		return result;
	}
	 
	
	@RequestMapping("/member/findMemberById.json")
    @ResponseBody
    public Object  findMemberById(@RequestParam("memberId") Long memberId) {
	    MemberPersonalDataCommand memberPersonalDataCommand = memberManager.findMemberById(memberId);
	    if(Validator.isNotNullOrEmpty(memberPersonalDataCommand)){
	        return memberPersonalDataCommand;
	    }
	    return null;
           
    }

}
