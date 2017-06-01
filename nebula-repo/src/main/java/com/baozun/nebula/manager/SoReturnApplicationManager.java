package com.baozun.nebula.manager;

import java.util.List;
import java.util.Map;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.OrderReturnCommand;
import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.command.ReturnApplicationViewCommand;
import com.baozun.nebula.command.ReturnLineViewCommand;
import com.baozun.nebula.model.salesorder.SoReturnApplication;
import com.baozun.nebula.sdk.command.SalesOrderCommand;

public interface SoReturnApplicationManager {

	/** 查询 当前订单行 已经退换过货的商品个数（退换货状态为已完成) <br/>
	 *  primaryLineId:orderlineid 或者packageOrderLineId*/
	public Integer countCompletedAppsByPrimaryLineId(Long primaryLineId,Integer type);
	
	/** 根据orderLineId  查询退换货单(时间最近的一个) */
	public SoReturnApplication findLastApplicationByOrderLineId(Long orderLineId);
	
	
	/** 根据订单id判断是不是 已经超过14天收货的订单*/
	public Boolean isFinishedAndOutDayOrderById(Long orderId);
	
	/** 新增退换货申请单
	 * @return */
	public ReturnApplicationCommand createReturnApplication(ReturnApplicationCommand appCommand,SalesOrderCommand orderCommand);
	
	/**
	 * 根据订单id查询最近的退货单
	 * @param orderId
	 * @return
	 */
	public SoReturnApplication findLastApplicationByOrderId(Long orderId);
	
	public Pagination<SoReturnApplication> findReturnByQueryMapWithPage(Page page, Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/** 查询退换货申请单  退换货applicationid */
	public SoReturnApplication findByApplicationId(Long id);
	
	  /** 客服审核  Status==1退回 ； Status==2 通过 */
	public SoReturnApplication auditSoReturnApplication(String returnCode,Integer Status,String description,String modifier,String omsCode,String returnAddress) throws Exception ;
	
	/**
	 * 退货信息导出
	 * @param paraMap
	 * @return
	 */
	public List<OrderReturnCommand> findExpInfo(Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过id集合查询Returnapplication 
	 * @param ids
	 * @return
	 */
	public List<ReturnApplicationCommand> findReturnApplicationCommandsByIds(List<Long> ids);
	
	/**
	 * 通过ReturnApplicationCommand集合获得returnApplicationViewCommand集合对象
	 * @param returnApplications
	 * @return
	 */
	public List<ReturnApplicationViewCommand> findReturnApplicationViewCommand(List<ReturnApplicationCommand> returnApplications);
	
	public  List<ReturnLineViewCommand> findReturnLineViewCommandByLineIds(List<Long> orderLineIds);
	
	
	
	

}
