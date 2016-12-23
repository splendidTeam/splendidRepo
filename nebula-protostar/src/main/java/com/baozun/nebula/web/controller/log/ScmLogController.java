package com.baozun.nebula.web.controller.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.product.MsgSendRecordCommand;
import com.baozun.nebula.model.system.MsgReceiveContent;
import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
*
* @Title: ScmReceivedLogController.java
* @author: weijun.zhang
* @date: 2016年8月9日 下午6:10:04
* @version 
*/
@Controller
@RequestMapping(value="/backlog")
public class ScmLogController extends  BaseController{
   
	@Autowired
	private SdkMsgManager sdkMsgManager;
    
	
	
	
	@RequestMapping("/scmSendLog/list.htm")
	public String sendList(){
		return "log/scmSendLog-list";
	}
	
	/**
	 * @Description: 分页获取 t_sys_msg_send_record, t_sys_msg_send_content列表
	 * @param queryBean
	 * @return
	 * Pagination<MsgReceiveContent>
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("/scmSendLog/page.json")
	public Pagination<MsgSendRecordCommand> findMsgSendContentListByQueryMapWithPage(@QueryBeanParam QueryBean queryBean){
		Sort[] sorts = queryBean.getSorts();
		if (sorts == null || sorts.length == 0){
			Sort sort = new Sort("record.create_time", "desc");
			sorts = new Sort[1];
			sorts[0] = sort;
		}
		return  sdkMsgManager.findMsgSendRecordAndContentListByQueryMapWithPage(queryBean.getPage(),sorts, queryBean.getParaMap());
	}
		
	
	
	
	@RequestMapping("/scmReceivedLog/list.htm")
	public String receivedList(){
		return "log/scmReceivedLog-list";
	}
	
	/**
	 * @Description: 分页获取 t_sys_msg_receive_content列表
	 * @param queryBean
	 * @return
	 * Pagination<MsgReceiveContent>
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("/scmReceivedLog/page.json")
	public Pagination<MsgReceiveContent> findMsgReceiveContentListByQueryMapWithPage(@QueryBeanParam QueryBean queryBean){
		Sort[] sorts = queryBean.getSorts();
		if (sorts == null || sorts.length == 0){
			Sort sort = new Sort("send_time", "desc");
			sorts = new Sort[1];
			sorts[0] = sort;
		}
		return  sdkMsgManager.findMsgReceiveContentListByPage(queryBean.getPage(),sorts, queryBean.getParaMap());
	}
		
	}
	

