package com.baozun.nebula.manager.salesorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.dao.auth.UserDao;
import com.baozun.nebula.dao.member.MemberDao;
import com.baozun.nebula.dao.system.ChooseOptionDao;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.sdk.command.CancelOrderCommand;
import com.baozun.nebula.sdk.manager.OrderManager;
import com.baozun.nebula.web.command.CancelApplicationCommand;
@Transactional
@Service
public class CancelOrderAppManagerImpl implements CancelOrderAppManager {
	@Autowired
	private OrderManager sdkOrderService;
	@Autowired
	private ChooseOptionDao chooseOptionDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private MemberDao memberDao;
	
	private static Map<Integer,String> statusMap=new HashMap<Integer,String>();
	private static List<String> statusCodes=new ArrayList<String>();
	private static final String statusCode="CANCEL_ORDER_STATUS";

	@SuppressWarnings("unchecked")
	@Override
	public Pagination<CancelApplicationCommand> findCancelApplicationListByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> queryMap) {
		Pagination<CancelOrderCommand> cancelOrderPagination = sdkOrderService.findCancelOrdersByQueryMapWithPage(page, sorts, queryMap);
		Pagination<CancelApplicationCommand> cancelApplicationCommandPagination=new Pagination<CancelApplicationCommand>();
		cancelApplicationCommandPagination=(Pagination<CancelApplicationCommand>) ConvertUtils.convertModelToApi(cancelApplicationCommandPagination, cancelOrderPagination);
		
		statusCodes.add(statusCode);
		List<ChooseOption> chooseOptions= chooseOptionDao.findChooseOptionValue(statusCodes);
		for(ChooseOption chooseOption:chooseOptions){
			statusMap.put(Integer.parseInt(chooseOption.getOptionValue()),chooseOption.getOptionLabel());
		}
		List<CancelOrderCommand> cancelOrderItems = cancelOrderPagination.getItems();
		List<CancelApplicationCommand> applicationCommands= cancelApplicationCommandPagination.getItems();
		for(int i=0;i<applicationCommands.size();i++){
			CancelApplicationCommand tempCommand=new CancelApplicationCommand();
			tempCommand=(CancelApplicationCommand) ConvertUtils.convertModelToApi(tempCommand, cancelOrderItems.get(i));
			tempCommand.setStatusInfo(statusMap.get(tempCommand.getStatus()));
			applicationCommands.set(i, tempCommand);
			
		}
		
		
		return cancelApplicationCommandPagination;
	}

}
