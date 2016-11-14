package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.dao.Sort;

import com.baozun.nebula.command.delivery.ContactDeliveryCommand;
import com.baozun.nebula.model.delivery.DeliveryArea;

public interface SdkDeliveryAreaManager {

	/**
	 * 保存area信息，
	 * 
	 * @param areaData
	 *            （txt文档中的内容）
	 */
	void saveArea(String areaData);

	/**
	 * 查询地区级别查询地区
	 * 
	 */
	List<DeliveryArea> findEnableDeliveryAreaList(String lang, Sort[] sort);

	List<DeliveryArea> findDeliveryAreaByParentId(Long parentId);

	DeliveryArea findEnableDeliveryAreaByCode(String code);

	Map<String, Map<String, String>> findAllSubDeliveryAreaByParentId(Long parentId);
	
	/**
	 * 根据父类id和名称来查询当前区域
	 */
	DeliveryArea findDeliveryAreaByNameAndParentId(String name, Long parentId);
	
	/**
	 * 
	 * @Description
	 * @param id
	 * @return
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-4
	 */
	DeliveryArea findDeliveryAreaById(Long id);

	/**
	 * @Description
	 * @param language
	 * @return
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-4
	 */
	Map<String, Map<String, String>> findAllDeliveryAreaByLang(String language, Sort[] sort);

	/**
	 * @Description
	 * @param code
	 * @return
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-8
	 */
	ContactDeliveryCommand findContactDeliveryByDeliveryAreaCode(String code);

	/**
	 * @Description
	 * @param map
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-14
	 */
	void updateDeliveryArea(Map<String, Object> map);

	/**
	 * @Description
	 * @param deliveryArea
	 * @return
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-14
	 */
	DeliveryArea insertDeliveryArea(DeliveryArea deliveryArea);

	/**
	 * @Description
	 * @param id
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-14
	 */
	void deleteDeliveryAreaById(Long id);

}
