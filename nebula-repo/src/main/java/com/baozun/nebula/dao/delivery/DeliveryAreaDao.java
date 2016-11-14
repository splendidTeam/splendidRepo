package com.baozun.nebula.dao.delivery;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.model.delivery.DeliveryArea;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Sort;

/**
 * @Description 三级下拉地址
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016年8月16日 下午1:28:01
 */
public interface DeliveryAreaDao extends GenericEntityDao<DeliveryArea, Long> {

	/**
	 * 
	 * @Description
	 * @param parentId
	 * @return
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-1
	 */
	@NativeQuery(model = DeliveryArea.class)
	List<DeliveryArea> findDeliveryAreaByParentId(@QueryParam("parentId") Long parentId);

	/**
	 * 
	 * @Description 查询DeliveryArea信息
	 * @param sort
	 * @return
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-1
	 */
	@NativeQuery(model = DeliveryArea.class)
	List<DeliveryArea> findEnableDeliveryAreaList(@QueryParam("lang")String name, Sort[] sort);

	/**
	 * 
	 * @Description 根据code查询地址
	 * @param code
	 * @return
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-1
	 */
	@NativeQuery(model = DeliveryArea.class)
	DeliveryArea findEnableDeliveryAreaByCode(@QueryParam("code") String code);

	/**
	 * 
	 * @Description 根据父类id和名称来查询当前区域
	 * @param name
	 * @param parentId
	 * @return DeliveryArea
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-1
	 */
	@NativeQuery(model = DeliveryArea.class)
	DeliveryArea findDeliveryAreaByNameAndParentId(@QueryParam("name")String name, @QueryParam("parentId")Long parentId);

	/**
	 * 更新DeliveryArea
	 */
	@NativeUpdate
	void updateDeliveryArea(@QueryParam Map<String, Object> paraMap);
}
