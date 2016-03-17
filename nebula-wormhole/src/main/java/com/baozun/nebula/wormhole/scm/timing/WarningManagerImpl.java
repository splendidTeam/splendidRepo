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
package com.baozun.nebula.wormhole.scm.timing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.system.WarningConfig;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.logistics.LogisticsCommand;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.sdk.manager.OrderManager;
import com.baozun.nebula.sdk.manager.SdkWarningConfigManager;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.wormhole.scm.manager.WarningPartManager;

/**
 * WarningManagerImpl
 *
 * @author: shiyang.lv
 * @date: 2014年5月16日
 **/
@Service("warningManager")
public class WarningManagerImpl implements WarningManager {

    @Autowired
    private OrderManager sdkOrderService;
    
    @Autowired
    private WarningPartManager warningPartManager;
    
    @Autowired
    private LogisticsManager logisticsManager;
    
    /**
 	 * 告警配置
 	 */
 	@Autowired
 	private SdkWarningConfigManager sdkWarningConfigManager;
    
    @Override
    public void payOrderNoChangStatus() {
    	//获取告警配置时间
    	WarningConfig wc = sdkWarningConfigManager.findWarningConfigByCode(WarningConfig.ORDER_NO_CHANGE_WARNING_CODE);
    	Long time = null;
    	if(wc != null){
    		String timeParam = wc.getTimeParam();
    		time=System.currentTimeMillis()-convertTimeParam(timeParam);
    	}else{
    		//24小时之前时间
            time=System.currentTimeMillis()-86400000L;
    	}
        Date yestoday=new Date(time);
        //邮件配置时间
        //设置查询条件（创建时间大于24小时）
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("endDate",yestoday);
        
        //财务状态已经付款
        List<Integer> pay=new ArrayList<Integer>();
        pay.add(SalesOrder.SALES_ORDER_FISTATUS_COD);
        pay.add(SalesOrder.SALES_ORDER_FISTATUS_PART_PAYMENT);
        pay.add(SalesOrder.SALES_ORDER_FISTATUS_FULL_PAYMENT);
        queryMap.put("financialStatusList",pay);
        
        //物流状态为新建的
        queryMap.put("logisticsStatus",SalesOrder.SALES_ORDER_STATUS_NEW);
        List<SalesOrderCommand> orders = sdkOrderService.findOrdersWithOutPage(null, queryMap);
        if(orders!=null&&!orders.isEmpty()){
            for(SalesOrderCommand salesOrderCommand:orders){
                //发送告警邮件
                warningPartManager.warnPayOrderNoChangStatus(salesOrderCommand,"payOrderNoChangStatus");
            }
        }

    }

    @Override
    public void notCancelOrderNoFinish() {
    	//获取告警配置时间
    	WarningConfig wc = sdkWarningConfigManager.findWarningConfigByCode(WarningConfig.NOT_CANCEL_ORDER_NO_FINISH_WARNING_CODE);
    	Long time = null;
    	if(wc!=null){
    		String timeParam = wc.getTimeParam();
       	 	time=System.currentTimeMillis()-convertTimeParam(timeParam);
    	}else{
    		//24小时之前时间
            time=System.currentTimeMillis()-864000000L;
    	}
    	
        Date beforeDate=new Date(time);
        
        //设置查询条件（创建时间大于240小时）
        Map<String,Object> queryMap = new HashMap<String, Object>();
        queryMap.put("endDate",beforeDate);
        
        //物流状态为未完成，并且没有被取消的
        List<Integer> logistics=new ArrayList<Integer>();
        logistics.add(SalesOrder.SALES_ORDER_STATUS_FINISHED);
        logistics.add(SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED);
        logistics.add(SalesOrder.SALES_ORDER_STATUS_CANCELED);
        queryMap.put("logisticsStatusNotInList",logistics);
        List<SalesOrderCommand> orders = sdkOrderService.findOrdersWithOutPage(null, queryMap);
        
        if(orders!=null&&!orders.isEmpty()){
            for(SalesOrderCommand salesOrderCommand:orders){
                //发送告警邮件
                warningPartManager.warnPayOrderNoChangStatus(salesOrderCommand,"notCancelOrderNoFinish");
            }
        }
    }

    @Override
    public void fullInventorySyncLater() {
    	//获取告警配置时间
    	WarningConfig wc = sdkWarningConfigManager.findWarningConfigByCode(WarningConfig.FULL_INVENTORY_SYNC_LATER_WARNING_CODE);
    	Long time = null;
    	if(wc != null) {
    		String timeParam = wc.getTimeParam();
    		time=System.currentTimeMillis()-convertTimeParam(timeParam);
    	}else{
    		 //48小时之前时间
            time=System.currentTimeMillis()-172800000L;
    	}
    	
        Date beforeTime=new Date(time);
        
        //最后创建时间在48小时之前
        //并且最后修改时间也在48小时之前
        //并且没有结束
        Map<String, Object> queryMap=new HashMap<String, Object>();
        queryMap.put("endDate", beforeTime);
        queryMap.put("endModifyDate", beforeTime);
        queryMap.put("notFinish", true);
        
        //如果在这段时间内没任何记录新增或者被修改
        List<LogisticsCommand> commands=logisticsManager.findLogisticsListByQueryMap(queryMap);
        if(commands!=null&&!commands.isEmpty()){
            for(LogisticsCommand command:commands){
                //发送告警邮件
                warningPartManager.warnFullInventorySyncLater(command,"fullInventorySyncLater");
            }
        }
    }

    private static Long convertTimeParam(String timeParam){
    	if(Validator.isNullOrEmpty(timeParam)){
    		return 0l;
    	}
    	//毫秒
    	Long time = 0l;
    	String prefix =timeParam.substring(0,timeParam.length()-1);
    	Long data=Long.parseLong(prefix); 
    	String suffix = timeParam.substring(timeParam.length()-1,timeParam.length());
    	if(suffix.equals("y")){
    		time = data*365*24*3600*1000;
    	}else if(suffix.equals("M")){
    		time = data*30*24*3600*1000;
    	}else if(suffix.equals("d")){
    		time = data*24*3600*1000;
    	}else if(suffix.equals("h")){
    		time = data*3600*1000;
    	}else if(suffix.equals("m")){
    		time = data*60*1000;
    	}else if(suffix.equals("s")){
    		time = data*1000;
    	}
		return time;
    }
}

