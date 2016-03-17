package com.baozun.nebula.web.controller.promotion;

import java.util.List;

import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.promotion.PriorityCommand;
import com.baozun.nebula.manager.member.MemberManager;
import com.baozun.nebula.manager.promotion.PromotionPriorityManager;
import com.baozun.nebula.model.member.MemberGroup;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 
 * @author - 项硕
 */
@Controller
public class PromotionPriorityController extends BaseController {
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(PromotionPriorityController.class);
	
	@Autowired
	private PromotionPriorityManager priorityManager;
	@Autowired
	private MemberManager memberManager;	

	/**
	 * 前往页面
	 * 促销渠道优先级列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/promotion/activityPriorityList.htm", method = RequestMethod.GET)
	public String priorityChannelList(Model model) {
		List<MemberGroup> list=memberManager.findAllMemberGroup();
		model.addAttribute("groupList", list);
		return "promotion/promotion-priority-channel-list";
	}

	/**
	 * 获取促销渠道优先级列表数据
	 * @return
	 */
	@RequestMapping(value = "/promotion/activityPriorityList.json", method = RequestMethod.GET)
	@ResponseBody
	public List<PriorityCommand> getPromotionPriorityChannelList(
			@QueryBeanParam QueryBean queryBean) {
		Sort[] sorts = queryBean.getSorts();
		if (null == sorts || sorts.length == 0) {
			sorts = Sort.parse("priority asc");
		}
		List<PriorityCommand> list = 
				priorityManager.findPromotionPriorityChanelAdjustListByQueryMapWithPage(
						queryBean.getPage(), sorts, queryBean.getParaMap());
        return list;
	}
}
