package com.baozun.nebula.manager.delivery;

import java.util.List;
import java.util.Map;

import loxia.dao.Sort;

import com.baozun.nebula.model.delivery.DeliveryArea;

public interface DeliveryAreaManager {

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
	List<DeliveryArea> findEnableDeliveryAreaList(Sort[] sort);

	List<DeliveryArea> findDeliveryAreaByParentId(Long parentId);

	DeliveryArea findEnableDeliveryAreaByCode(String code);

	Map<String, Map<String, String>> findAllSubDeliveryAreaByParentId(
			Long parentId);
	
	/**
	 * 根据父类id和名称来查询当前区域
	 */
	public DeliveryArea findDeliveryAreaByNameAndParentId(String name, Long parentId);
	
	public DeliveryArea findDeliveryAreaById(Long id);

}
