package com.baozun.nebula.dao.returnapplication;

import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.OrderReturnCommand;
import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.model.returnapplication.ReturnApplication;



/**
 * 订单退换货处理接口
 * 退换货申请
 */
public interface SdkReturnApplicationDao extends GenericEntityDao<ReturnApplication, Long> {

	/**
	 * 根据id查询退换货订单
	 */
	@NativeQuery(model = ReturnApplication.class,value="SdkReturnApplicationDao.findByApplicationId")
	ReturnApplication findByApplicationId(@QueryParam("id") Long id);
	
	/**
	 * 根据orderLineId orderId 查询退换货订单
	 */
	@NativeQuery(model = ReturnApplication.class,value="SdkReturnApplicationDao.findLastApplicationByOrderLineId")
	List<ReturnApplication> findLastApplicationByOrderLineId(@QueryParam("orderLineId") Long orderLineId);
	
	/**
	 * 根据orderId查询退货单
	 * @param orderId
	 * @return
	 */
	@NativeQuery(model = ReturnApplication.class,value="SdkReturnApplicationDao.findLastApplicationByOrderId")
	List<ReturnApplication> findLastApplicationByOrderId(@QueryParam("orderId") Long orderId);
	
	/**
	 * 普通商品 退货 <br/>
	 * 根据orderlineid、退货状态、以及退换货类型 统计 普通商品 （当前订单行） 对应的退货单行商品的数量
	 */
	@NativeQuery(alias="qty",clazzes = Integer.class,value="SdkReturnApplicationDao.countItemByOrderLineIdAndStatus")
	Integer countItemByOrderLineIdAndStatus(@QueryParam("orderLineId") Long orderLineId,@QueryParam("statusArr") Integer[] statusArr);
	
	/**
	 * 根据退单ids查询ReturnApplication
	 */
	@NativeQuery(model = ReturnApplication.class,value="SdkReturnApplicationDao.findSoReturnAppsByIds")
	List<ReturnApplication> findSoReturnAppsByIds(@QueryParam("returnIds") Set<Long> returnIds);

	/**
	 * 根据memberId查询ReturnApplication
	 */
	@NativeQuery(model = ReturnApplication.class,value="SdkReturnApplicationDao.findByApplicationByMemberId")
	List<ReturnApplication> findByApplicationByMemberId(@QueryParam("memberid") Long memberid);

	/**
	 * 分页查询
	 */
	@NativeQuery(model = ReturnApplication.class, pagable = true,value="SdkReturnApplicationDao.findByCodeOrName")
	Pagination<ReturnApplication> findByCodeOrName(int start, int size,@QueryParam("paraMap") Map<String, Object> paraMap);
	
	/**
	 * 分页条件查询退货信息
	 * @param start
	 * @param size
	 * @param paraMap
	 * @return
	 * @author 黄金辉
	 */
	@NativeQuery(model = ReturnApplication.class, pagable = true,value="SdkReturnApplicationDao.findReturnByQueryMapWithPage")
	Pagination<ReturnApplication> findReturnByQueryMapWithPage(Page page, Sort[] sorts,@QueryParam("paraMap") Map<String, Object> paraMap);

	/**
	 * 根据退货单编号查询退货单
	 * @param code
	 * @return
	 */
	@NativeQuery(model = ReturnApplication.class,value="SdkReturnApplicationDao.findApplicationByCode")
	ReturnApplication  findApplicationByCode(@QueryParam("code") String code);

	/*
	 * 查询需要导出的数据（OrderReturnCommand）
	 */
	@NativeQuery(model = OrderReturnCommand.class,value="SdkReturnApplicationDao.findExpInfo")
	List<OrderReturnCommand> findExpInfo(Sort[] sorts,@QueryParam("paraMap") Map<String, Object> paraMap);
	
	@NativeQuery(model=ReturnApplication.class,value="SdkReturnApplicationDao.findApplicationByOrderId")
	List<ReturnApplication> findApplicationByOrderId(@QueryParam("orderId") Long orderId);
	
	@NativeQuery(model=ReturnApplication.class,value="SdkReturnApplicationDao.findApplicationByParam")
	List<ReturnApplication> findApplicationByParam(@QueryParam Map<String, Object> paraMap);
	
	@NativeQuery( model = ReturnApplication.class,value="SdkReturnApplicationDao.findByStatusAndSyntype")
	List<ReturnApplication> findByStatusAndSyntype(@QueryParam("status") int status, @QueryParam("syntype") int syntype);
	
	@NativeQuery(model = ReturnApplication.class,value="SdkReturnApplicationDao.findByStatus")
	List<ReturnApplication> findByStatus(@QueryParam("status") int status);
	
	/**
	 * 根据退换货单ReturnApplication的编码omsCode查询ReturnApplication
	 * 
	 * @param platformcode
	 * @return
	 */
	@NativeQuery(model = ReturnApplication.class, value = "SdkReturnApplicationDao.findByOmsCode")
	ReturnApplication findByOmsCode(@QueryParam("omsCode") String omsCode);
	
	@NativeQuery(model = ReturnApplicationCommand.class, value = "SdkReturnApplicationDao.findReturnApplicationCommandByIds")
	List<ReturnApplicationCommand>  findReturnApplicationCommandByIds(@QueryParam("ids") List<Long> ids);

}
