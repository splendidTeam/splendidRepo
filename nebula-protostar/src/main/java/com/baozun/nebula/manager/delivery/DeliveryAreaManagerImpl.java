package com.baozun.nebula.manager.delivery;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import loxia.dao.Sort;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.delivery.DeliveryAreaDao;
import com.baozun.nebula.model.delivery.DeliveryArea;
import com.feilong.core.Validator;

@Transactional
@Service("deliveryAreaManager")
public class DeliveryAreaManagerImpl implements DeliveryAreaManager {

	@Autowired
	private DeliveryAreaDao deliveryAreaDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public void saveArea(String areaData) {
		JSONObject jsonObject = JSONObject.fromObject(areaData);

		Map<String, List<Map<String, String>>> mapJson = JSONObject
				.fromObject(jsonObject);

		for (Entry<String, List<Map<String, String>>> entry : mapJson
				.entrySet()) {

			// 获取parentCode
			String parentCode = entry.getKey();
			// 通过parentCode获取parentId
			DeliveryArea parentArea = deliveryAreaDao
					.findEnableDeliveryAreaByCode(parentCode);
			Long parentId = parentArea.getId();
			// level从2开始。1是中国
			Integer sortNo = 1;
			Object strval1 = entry.getValue();
			JSONObject jsonObjectStrval1 = JSONObject.fromObject(strval1);
			Map<String, String> mapJsonObjectStrval1 = JSONObject
					.fromObject(jsonObjectStrval1);
			for (Map.Entry<String, String> ety : mapJsonObjectStrval1
					.entrySet()) {
				Date date = new Date();

				// 获得自己的Code
				String code = ety.getKey();
				DeliveryArea deliverArea = new DeliveryArea();
				deliverArea.setCode(code);
				deliverArea.setParentId(parentId);
				deliverArea.setArea(ety.getValue());
				deliverArea.setSortNo(sortNo);
				deliverArea.setCreateTime(date);
				deliverArea.setModifyTime(date);
				deliverArea.setStatus(1);
				deliverArea.setVersion(date);
				// 当前地区的级别为其父区域级别+1
				deliverArea.setLevel(deliveryAreaDao.getByPrimaryKey(parentId) == null ? null
								: deliveryAreaDao.getByPrimaryKey(parentId)
										.getLevel() + 1);
				deliveryAreaDao.save(deliverArea);
				sortNo++;
			}
		}
	}

	@Override
	public List<DeliveryArea> findEnableDeliveryAreaList(Sort[] sort) {
		List<DeliveryArea> list = deliveryAreaDao.findEnableDeliveryAreaList(sort);
		return list;
	}

	@Override
	public List<DeliveryArea> findDeliveryAreaByParentId(Long parentId) {
		return deliveryAreaDao.findDeliveryAreaByParentId(parentId);
	}

	@Override
	public DeliveryArea findEnableDeliveryAreaByCode(String code) {
		return deliveryAreaDao.findEnableDeliveryAreaByCode(code);
	}

	@Override
	public Map<String, Map<String, String>> findAllSubDeliveryAreaByParentId(
			Long parentId) {
		List<DeliveryArea> parentDeliveryArea = deliveryAreaDao
				.findDeliveryAreaByParentId(parentId);
		Map<String, Map<String, String>> map = null;
		if (Validator.isNotNullOrEmpty(parentDeliveryArea)) {
			map = new HashMap<String, Map<String, String>>();
			Map<String, String> countryMap = new HashMap<String, String>();
			for (DeliveryArea area : parentDeliveryArea) {
				findAllSubDeliveryAreaByParentId(area, map);
				countryMap.put(area.getCode(), area.getArea());
			}
			map.put("abc", countryMap);
		}
		return map;
	}

	private void findAllSubDeliveryAreaByParentId(
			DeliveryArea parentDeliveryArea,
			Map<String, Map<String, String>> areaMap) {
		List<DeliveryArea> areaList = deliveryAreaDao
				.findDeliveryAreaByParentId(parentDeliveryArea.getId());
		if (Validator.isNotNullOrEmpty(areaList)) {
			Map<String, String> map = new HashMap<String, String>();
			for (DeliveryArea area : areaList) {
				map.put(area.getCode(), area.getArea());
				findAllSubDeliveryAreaByParentId(area, areaMap);
			}
			areaMap.put(parentDeliveryArea.getCode(), map);
		}
		return;
	}

	@Override
	public DeliveryArea findDeliveryAreaByNameAndParentId(String name,Long parentId) {
		return deliveryAreaDao.findDeliveryAreaByNameAndParentId(name, parentId);
	}
	
	public DeliveryArea findDeliveryAreaById(Long id){
		return deliveryAreaDao.getByPrimaryKey(id);
	}
}
