package com.baozun.nebula.web.controller.emailSubscribe;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.system.EmailSubscribe;
import com.baozun.nebula.sdk.manager.SdkEmailSubscribeManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.utilities.DateUtil;

import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * 邮件订阅管理
 * @author zhaojun.fang
 *
 */
@Controller
public class EmailSubscribeController extends BaseController{

		@Autowired
	    private SdkEmailSubscribeManager sdkEmailSubscribeManager; 
		
	    /**
	     * 
	     * @param model
	     * @param queryBean
	     * @return
	     */
	    @RequestMapping("/email/subscribe/manager.htm")
	    public String itemSearchConditionManager(Model model,QueryBean queryBean) {
	        return "emailsubscribe/emailSubscribe-list";
	    }
	    
	    /**
	     * 页面跳转 列表页面通过Json获取信息
	     * 
	     * @param model
	     * @param memberId
	     * @return
	     */
	    @RequestMapping("/email/subscribe/list.json")
	    @ResponseBody
	    public Pagination<EmailSubscribe> emailSubscribeList(Model model,
	            @QueryBeanParam QueryBean queryBean) {

	        Sort[] sorts = queryBean.getSorts();

	        if (sorts == null || sorts.length == 0) {
	            Sort sort = new Sort("id", "desc");
	            sorts = new Sort[1];
	            sorts[0] = sort;
	        }; 
	        
	        Pagination<EmailSubscribe> searchConditions = sdkEmailSubscribeManager
	                .findEmailSubscribeListByQueryMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
	        return searchConditions;
	    }
	    
	    /**
	     * 删除
	     * @param ids
	     * @return
	     */
	    @RequestMapping("/email/subscribe/deleteEmailSubscribeByIds.json")
	    @ResponseBody
	    public Object deleteEmailSubscribeByIds(String ids){
	        String[] array=ids.split(",");
	        List<Long> idList=new ArrayList<Long>();
	        for(String id:array){
	            idList.add(Long.valueOf(id));
	        }
	        sdkEmailSubscribeManager.deleteAllByPrimaryKey(idList);
	        
	        return BaseController.SUCCESS;
	    }
	    
	    
	    /**
	     * 修改或者新增页面跳转
	     * @param model
	     * @param id
	     * @return
	     */
	    @RequestMapping("/email/subscribe/toSaveOrUpdateEmailSubscribe.htm")
	    public String emailSubscribeSaveOrUpdate(Model model,Long id) {
	        
	        if(id!=null){
	        	EmailSubscribe emailSubscribe=sdkEmailSubscribeManager.getEmailSubscribeById(id);
	            model.addAttribute("emailSubscribe",emailSubscribe);
	            if(null==emailSubscribe){
	                throw new BusinessException(ErrorCodes.TASK_NOT_EXIST);
	            }
	            return "emailsubscribe/emailSubscribe-edit";
	        }
	        
	        return "emailsubscribe/emailSubscribe-add";
	    }
	    
	    /**
	     * 保存修改或者新增
	     * @param model
	     * @param schedulerTask
	     * @return
	     */
	    @RequestMapping("/email/subscribe/saveEmailSubscribe.htm")
	    public String saveEmailSubscribe(Model model,EmailSubscribe emailSubscribe,@RequestParam("sendDate") String sendDate) {
	    	try {
				emailSubscribe.setDate(DateUtil.parse(sendDate, "yyyy-MM-dd"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
	        sdkEmailSubscribeManager.saveEmailSubscribe(emailSubscribe);
	        
	        return "redirect:/email/subscribe/manager.htm?keepfilter=true";
	    }
}
