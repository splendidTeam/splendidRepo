/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Baozun. You
 * shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Baozun.
 * 
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 */
package com.baozun.nebula.web.controller.promotion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.curator.ZkOperator;
import com.baozun.nebula.curator.invoke.EngineWatchInvoke;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.baseinfo.ShopManager;
import com.baozun.nebula.sdk.manager.SdkLimitManager;
import com.baozun.nebula.utils.property.PropertyUtil;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 
 * @author 项硕
 */
@Controller
public class LimitController extends BaseController {
	
	@Autowired
	private SdkLimitManager sdkLimitManager;
	@Autowired
	private ShopManager shopManager;
	@Autowired
	private ZkOperator zkOperator;
	
	@Value("#{meta['frontend.url']}")
	private  String  frontend_url;
	@Value("#{meta['pdpPrefix']}")
	private  String  pdpPrefix;
	@Value("#{meta['pdp.param.type']}")
	private  String  pdp_param_type;
	
	private String getPdpInfoUrl(){
		return frontend_url.trim()+pdpPrefix.trim()+pdp_param_type.trim();
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * 页面跳转到限购编辑列表
	 */
	@RequestMapping(value = "/limitation/limitationEditList.htm")
	public String promotionEdit(Model model) {
		return "/limit/limit-edit-list";
	}
	
	/**
	 * 页面跳转限购启用列表
	 */
	@RequestMapping(value = "/limitation/limitationPublishList.htm")
	public String limitationPublish(Model model) {
		return "/limit/limit-enable-list";
	}
	
	/**
	 * 页面跳转 列表页面通过Json获取数据库会员信息
	 * @param model
	 * @param memberId
	 * @return
	 */
	@RequestMapping("/limit/limitEditList.json")
	@ResponseBody
	public Pagination<LimitCommand> limitList(Model model,
			@QueryBeanParam QueryBean queryBean,String type) { 
		Long shopId = shopManager.getShopId(getUserDetails());
		Sort[] sorts = queryBean.getSorts();
		if (sorts == null || sorts.length == 0) {
			Sort sort = new Sort("h.create_time", "desc");
			sorts = new Sort[1];
			sorts[0] = sort;
		};

		Pagination<LimitCommand> limitCommand = sdkLimitManager
		.findLimitCommandConditionallyWithPage(queryBean.getPage(), sorts, queryBean.getParaMap(),type,shopId);
		return limitCommand;
	}
	
/**********************************************************************************************************************************/
	
	/**
	 * 前往编辑页面
	 * @param id	限购ID
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/limit/edit.htm", method = RequestMethod.GET)
	public String goEditPage(@RequestParam(required = false) Long id, Model model) {
		if (null != id) {
			LimitCommand cmd = sdkLimitManager.findLimitCommandById(id);
			model.addAttribute("limit", cmd);
		}
		model.addAttribute("pdp_base_url", PropertyUtil.getPdsBasesUrl(getPdpInfoUrl()));
		return "/limit/limit-edit";
	}

	/**
	 * 编辑限购-第一步
	 * @param cmd
	 * @return
	 */
	@RequestMapping(value = "/limit/stepOne.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity stepOne(LimitCommand cmd) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			UserDetails user = getUserDetails();
			cmd.setShopId(shopManager.getShopId(user));
			Long id = sdkLimitManager.saveOrUpdateLimitHead(cmd, user.getUserId());
			rs.setIsSuccess(true);
			rs.setDescription(id);
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
		}
		return rs;
	}
	
	/**
	 * 编辑限购-第二步
	 * @param cmd
	 * @return
	 */
	@RequestMapping(value = "/limit/stepTwo.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity stepTwo(LimitCommand cmd) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			UserDetails user = getUserDetails();
			Long id = sdkLimitManager.saveOrUpdateLimitAudience(cmd, user.getUserId());
			rs.setIsSuccess(true);
			rs.setDescription(id);
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
		}
		return rs;
	}
	
	/**
	 * 编辑限购-第三步
	 * @param cmd
	 * @return
	 */
	@RequestMapping(value = "/limit/stepThree.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity stepThree(LimitCommand cmd) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			UserDetails user = getUserDetails();
			Long id = sdkLimitManager.saveOrUpdateLimitScope(cmd, user.getUserId());
			rs.setIsSuccess(true);
			rs.setDescription(id);
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
		}
		return rs;
	}
	
	/**
	 * 编辑限购-第四步
	 * @param cmd
	 * @return
	 */
	@RequestMapping(value = "/limit/stepFour.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity stepFour(LimitCommand cmd) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			UserDetails user = getUserDetails();
			Long id = sdkLimitManager.saveOrUpdateLimitCondition(cmd, user.getUserId());
			rs.setIsSuccess(true);
			rs.setDescription(id);
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
		}
		return rs;
	}

	/**
	 * 由于前端改变了商品范围，删除限购条件
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/limit/deleteStep.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity deleteStep(@RequestParam Long id) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			sdkLimitManager.deleteStep(id);
			return SUCCESS;
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
		}
		return rs;
	}
	
	/**
	 * 前往查看页面
	 * @param id	限购ID
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/limit/view.htm", method = RequestMethod.GET)
	public String goViewPage(@RequestParam Long id, Model model) {
		LimitCommand cmd = sdkLimitManager.findLimitCommandById(id);
		model.addAttribute("limit", cmd);
		model.addAttribute("isView", true);
		model.addAttribute("pdp_base_url", PropertyUtil.getPdsBasesUrl(getPdpInfoUrl()));
		return "/limit/limit-edit";
	}

	/**
	 * 启用前冲突检查
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/limit/beforeActivation.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity beforeActivation(@RequestParam Long id) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			List<LimitCommand> list = sdkLimitManager.checkBeforeActivation(id, shopManager.getShopId(getUserDetails()));
			rs.setIsSuccess(true);
			rs.setDescription(list);
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
		}
		return rs;
	}
	
	/**
	 * 启用限购
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/limit/activate.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity activateLimit(@RequestParam Long id) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			sdkLimitManager.activateLimit(id, getUserDetails().getUserId());
			zkOperator.noticeZkServer(zkOperator.getPath(EngineWatchInvoke.PATH_KEY));
			return SUCCESS;
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
		}
		return rs;
	}

	/**
	 * 禁用限购
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/limit/inactivate.json", method = RequestMethod.POST)
	@ResponseBody
	public BackWarnEntity inactivateLimit(@RequestParam Long id) {
		BackWarnEntity rs = new BackWarnEntity(false, null);
		try {
			sdkLimitManager.cancelLimit(id, getUserDetails().getUserId());
			zkOperator.noticeZkServer(EngineWatchInvoke.PATH_KEY);
			return SUCCESS;
		} catch (BusinessException e) {
			e.printStackTrace();
			rs.setDescription(getMessage(e.getErrorCode()));
		} catch (Exception e) {
			e.printStackTrace();
			rs.setDescription(getMessage(ErrorCodes.SYSTEM_ERROR));
		}
		return rs;
	}
}
