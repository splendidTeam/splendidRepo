//package com.baozun.nebula.api.logistics;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.baozun.nebula.api.utils.ConvertUtils;
//import com.baozun.nebula.dao.salesorder.LogisticsDao;
//
//
//@Transactional
//@Service("logisticsService")
//public class LogisticsServiceImpl implements LogisticsService {
//
//
//	@Autowired
//	private LogisticsDao logisticsDao;
//	
//	@Override
//	public List<Province> findProvinceList() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	@Override
//	public List<City> findCities(Long provinceId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	@Override
//	public List<Area> findAreas(Long provinceId, Long cityId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	@Override
//	public List<LogisticsType> findLogisticsTypes(Long areaId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	@Override
//	public BigDecimal findFreight(Long areaId, String typeCode) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	@Override
//	public Logistics findLogisticsByOrderId(Long orderId) {
//		
//		return (Logistics)ConvertUtils.convertModelToApi(new Logistics(), logisticsDao.findLogisticsByOrderId(orderId));
//	}
//
//
//
//
//	
//}
