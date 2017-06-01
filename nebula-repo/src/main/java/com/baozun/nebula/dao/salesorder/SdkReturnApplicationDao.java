package com.baozun.nebula.dao.salesorder;

import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.OrderReturnCommand;
import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.model.salesorder.SoReturnApplication;



/**
 * 订单退换货处理接口
 * 退换货申请
 */
public interface SdkReturnApplicationDao extends GenericEntityDao<SoReturnApplication, Long> {

	/**
	 * 根据id查询退换货订单
	 */
	@NativeQuery(model = SoReturnApplication.class)
	SoReturnApplication findByApplicationId(@QueryParam("id") Long id);
	
	/**
	 * 根据orderLineId orderId 查询退换货订单
	 */
	@NativeQuery(model = SoReturnApplication.class)
	List<SoReturnApplication> findLastApplicationByOrderLineId(@QueryParam("orderLineId") Long orderLineId);
	
	/**
	 * 根据orderId查询退货单
	 * @param orderId
	 * @return
	 */
	@NativeQuery(model = SoReturnApplication.class)
	List<SoReturnApplication> findLastApplicationByOrderId(@QueryParam("orderId") Long orderId);
	
	/**
	 * 普通商品 退货 <br/>
	 * 根据orderlineid、退货状态、以及退换货类型 统计 普通商品 （当前订单行） 对应的退货单行商品的数量
	 */
	@NativeQuery(alias="qty",clazzes = Integer.class)
	Integer countItemByOrderLineIdAndStatus(@QueryParam("orderLineId") Long orderLineId,@QueryParam("statusArr") Integer[] statusArr);
	
	/**
	 * 根据退单ids查询SoReturnApplication
	 */
	@NativeQuery(model = SoReturnApplication.class)
	List<SoReturnApplication> findSoReturnAppsByIds(@QueryParam("returnIds") Set<Long> returnIds);

	/**
	 * 根据memberId查询SoReturnApplication
	 */
	@NativeQuery(model = SoReturnApplication.class)
	List<SoReturnApplication> findByApplicationByMemberId(@QueryParam("memberid") Long memberid);

	/**
	 * 分页查询
	 */
	@NativeQuery(model = SoReturnApplication.class, pagable = true)
	Pagination<SoReturnApplication> findByCodeOrName(int start, int size,@QueryParam("paraMap") Map<String, Object> paraMap);
	
	/**
	 * 分页条件查询退货信息
	 * @param start
	 * @param size
	 * @param paraMap
	 * @return
	 * @author 黄金辉
	 */
	@NativeQuery(model = SoReturnApplication.class, pagable = true,value="SoReturnApplication.findReturnByQueryMapWithPage")
	Pagination<SoReturnApplication> findReturnByQueryMapWithPage(Page page, Sort[] sorts,@QueryParam("paraMap") Map<String, Object> paraMap);

	/**
	 * 根据退货单编号查询退货单
	 * @param code
	 * @return
	 */
	@NativeQuery(model = SoReturnApplication.class)
	SoReturnApplication  findApplicationByCode(@QueryParam("code") String code);

	/*
	 * 查询需要导出的数据（OrderReturnCommand）
	 */
	@NativeQuery(model = OrderReturnCommand.class)
	List<OrderReturnCommand> findExpInfo(Sort[] sorts,@QueryParam("paraMap") Map<String, Object> paraMap);
	
	@NativeQuery(model=SoReturnApplication.class)
	List<SoReturnApplication> findApplicationByOrderId(@QueryParam("orderId") Long orderId);
	
	@NativeQuery(model=SoReturnApplication.class)
	List<SoReturnApplication> findApplicationByParam(@QueryParam Map<String, Object> paraMap);
	
	@NativeQuery( model = SoReturnApplication.class)
	List<SoReturnApplication> findByStatusAndSyntype(
			@QueryParam("status") int status, @QueryParam("syntype") int syntype);
	
	@NativeQuery(model = SoReturnApplication.class)
	List<SoReturnApplication> findByStatus(@QueryParam("status") int status);
	
	/**
	 * 根据退换货单ID，更新退换货单同步状态
	 * 
	 * @param id
	 * @param syntype
	 * @return
	 */
	@NativeUpdate(value = "SoReturnApplication.updateSoReturnApplicationSynType")
	Integer updateSoReturnApplicationSynType(@QueryParam("id") Long id,@QueryParam("syntype") int syntype);
	
	/**
	 * 根据退换货单SoReturnApplication的编码omsCode查询SoReturnApplication
	 * 
	 * @param platformcode
	 * @return
	 */
	@NativeQuery(model = SoReturnApplication.class, value = "SoReturnApplication.findByOmsCode")
	SoReturnApplication findByOmsCode(
			@QueryParam("omsCode") String omsCode);
	
	@NativeQuery(model = ReturnApplicationCommand.class, value = "SoReturnApplication.findReturnApplicationCommandByIds")
	List<ReturnApplicationCommand>  findReturnApplicationCommandByIds(@QueryParam("ids") List<Long> ids);

}
