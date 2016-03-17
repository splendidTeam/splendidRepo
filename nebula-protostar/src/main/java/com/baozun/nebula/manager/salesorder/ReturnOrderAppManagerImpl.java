package com.baozun.nebula.manager.salesorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.dao.system.ChooseOptionDao;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.ReturnOrderCommand;
import com.baozun.nebula.sdk.manager.OrderManager;
import com.baozun.nebula.web.command.PtsReturnOrderCommand;

@Transactional
@Service("ReturnOrderAppManager")
public class ReturnOrderAppManagerImpl implements ReturnOrderAppManager {
	
	private static final String  statusCode="RETURN_ORDER_STATUS";
	
	private static final String  servicetypeCode="RETURN_ORDER_SERVICETYPE";
	
	private static final String SDK_QUERY_TYPE_KEY="sdkQueryType";
	
	private static final String SDK_QUERY_TYPE_VAL="1";
	
	@Autowired
	private OrderManager sdkOrderService;
	
	@Autowired
	private SdkOrderLineDao sdkOrderLineDao;
	
	@Autowired
	private ChooseOptionDao chooseOptionDao;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Pagination<PtsReturnOrderCommand> findReturnApplicationListByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> searchParam) {
		
		searchParam.put(SDK_QUERY_TYPE_KEY,SDK_QUERY_TYPE_VAL);
		
		Pagination<ReturnOrderCommand> paginationReturnOrder =sdkOrderService.
				findReturnOrdersByQueryMapWithPage(page, sorts, searchParam);
				
		Pagination<PtsReturnOrderCommand> result = new Pagination<PtsReturnOrderCommand>();
		
		result=(Pagination<PtsReturnOrderCommand>)ConvertUtils.convertTwoObject(result,paginationReturnOrder);
		
		List<ReturnOrderCommand> items = paginationReturnOrder.getItems();

		List<PtsReturnOrderCommand> rItems =result.getItems();
		
		List<Long> orderDetailIds =new ArrayList<Long>();
		Set<Long> set=new HashSet<Long>();
		PtsReturnOrderCommand roc =null;
		for (int i = 0; i < items.size();i ++) {
			roc =new PtsReturnOrderCommand();
			roc =(PtsReturnOrderCommand)ConvertUtils.convertTwoObject(roc,
					items.get(i));
			if(null != roc.getOrderLineId()){
				set.add(roc.getOrderLineId());
			}
		}
		Iterator<Long> it=set.iterator();
		while(it.hasNext()){
			orderDetailIds.add(it.next());
		}
		//为了获取商品信息
		List<OrderLineCommand> tempRocList =sdkOrderLineDao.findOrderDetailListByIds(orderDetailIds);
		
		List<String> groupCodes=new ArrayList<String>();
		groupCodes.add(statusCode);
		groupCodes.add(servicetypeCode);
		List<ChooseOption> optionList=chooseOptionDao.findChooseOptionValue(groupCodes);
		
		Map<String,String> optionMap=new HashMap<String,String>();
		for(ChooseOption co:optionList){
			optionMap.put(co.getGroupCode()+"-"+co.getOptionValue(), co.getOptionLabel());
		}

		for(int i =0 ;i < rItems.size();i ++){
			roc =new PtsReturnOrderCommand();
			roc =(PtsReturnOrderCommand)ConvertUtils.convertTwoObject(roc,
					items.get(i));
			for(int j =0; j<tempRocList.size();j++){
				
				if(null!=roc.getOrderLineId()&&roc.getOrderLineId().equals(tempRocList.get(j).getId())){
					roc.setItemPic(tempRocList.get(j).getItemPic());
					roc.setItemName(tempRocList.get(j).getItemName());
					roc.setItemCount(tempRocList.get(j).getCount());
				}
			}
			roc.setAppStatus(optionMap.get(statusCode+"-"+items.get(i).getStatus()));
			roc.setServiceName(optionMap.get(servicetypeCode+"-"+items.get(i).getServiceType()));
			rItems.set(i, roc);
		}
		
		return result;
	}

}
