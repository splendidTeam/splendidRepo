/**
 * Copyright (c) 2013 Jumbomart All Rights Reserved.
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
package com.baozun.nebula.web.controller.system;

import java.util.ArrayList;
import java.util.List;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.system.SchedulerTaskManager;
import com.baozun.nebula.model.system.SchedulerTask;
import com.baozun.nebula.sdk.manager.SdkSchedulerTaskManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

/**
 * SchedulerTaskContorller
 *
 * @author: shiyang.lv
 * @date: 2014年5月29日
 **/
@Controller
public class SchedulerTaskContorller extends BaseController{
    
    @Autowired
    private SchedulerTaskManager schedulerTaskManager;
    
    @Autowired
    private SdkSchedulerTaskManager sdkSchedulerTaskManager;
    /**
     * 
     * @param model
     * @param queryBean
     * @return
     */
    @RequestMapping("/system/schedulerTask/manager.htm")
    public String itemSearchConditionManager(Model model,QueryBean queryBean) {
        return "system/schedulertask/schedulertask-list";
    }
    
    /**
     * 页面跳转 列表页面通过Json获取信息
     * 
     * @param model
     * @param memberId
     * @return
     */
    @RequestMapping("/system/schedulerTask/schedulerTaskList.json")
    @ResponseBody
    public Pagination<SchedulerTask> schedulerTaskList(Model model,
            @QueryBeanParam QueryBean queryBean) {

        Sort[] sorts = queryBean.getSorts();

        if (sorts == null || sorts.length == 0) {
            Sort sort = new Sort("id", "desc");
            sorts = new Sort[1];
            sorts[0] = sort;
        }; 
        
        Pagination<SchedulerTask> searchConditions = sdkSchedulerTaskManager
                .findSchedulerTaskListByQueryMapWithPage(queryBean.getPage(), sorts, queryBean.getParaMap());
        return searchConditions;
    }
    
    /**
     * 删除
     * @param ids
     * @return
     */
    @RequestMapping("/system/schedulerTask/deleteSchedulerTaskByIds.json")
    @ResponseBody
    public Object deleteSchedulerTaskByIds(String ids){
        String[] array=ids.split(",");
        List<Long> idList=new ArrayList<Long>();
        for(String id:array){
            idList.add(Long.valueOf(id));
        }
        sdkSchedulerTaskManager.removeSchedulerTaskByIds(idList);
        
        return BaseController.SUCCESS;
    }
    
    /**
     * 启用
     * @param ids
     * @return
     */
    @RequestMapping("/system/schedulerTask/enableSchedulerTaskByIds.json")
    @ResponseBody
    public Object enableSchedulerTaskByIds(String ids){
        String[] array=ids.split(",");
        List<Long> idList=new ArrayList<Long>();
        for(String id:array){
            idList.add(Long.valueOf(id));
        }
        schedulerTaskManager.enableSchedulerTaskByIds(idList);
        
        return BaseController.SUCCESS;
    }
    
    /**
     * 禁用
     * @param ids
     * @return
     */
    @RequestMapping("/system/schedulerTask/unEnableSchedulerTaskByIds.json")
    @ResponseBody
    public Object unEnableSchedulerTaskByIds(String ids){
        String[] array=ids.split(",");
        List<Long> idList=new ArrayList<Long>();
        for(String id:array){
            idList.add(Long.valueOf(id));
        }
        schedulerTaskManager.unEnableSchedulerTaskByIds(idList);
        
        return BaseController.SUCCESS;
    }
    
    /**
     * 修改或者新增页面跳转
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/system/schedulerTask/toSaveOrUpdateSchedulerTask.htm")
    public String schedulerTaskVoSaveOrUpdate(Model model,Long id) {
        
        if(id!=null){
            SchedulerTask schedulerTaskVo=schedulerTaskManager.findSchedulerTaskById(id);
            model.addAttribute("schedulerTaskVo",schedulerTaskVo);
            if(null==schedulerTaskVo){
                throw new BusinessException(ErrorCodes.TASK_NOT_EXIST);
            }
            return "system/schedulertask/schedulertask-edit";
        }
        
        return "system/schedulertask/schedulertask-add";
    }
    
    /**
     * 保存修改或者新增
     * @param model
     * @param schedulerTask
     * @return
     */
    @RequestMapping("/system/schedulerTask/saveSchedulerTask.htm")
    public String itemSearchConditionManagerSave(Model model,SchedulerTask schedulerTask) {
        schedulerTaskManager.saveSchedulerTask(schedulerTask);
        
        return "redirect:/system/schedulerTask/manager.htm?keepfilter=true";
    }
}

