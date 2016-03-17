/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.baozun.nebula.manager.baseinfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.dao.auth.OrganizationDao;
import com.baozun.nebula.dao.product.CommonPropertyDao;
import com.baozun.nebula.dao.product.IndustryDao;
import com.baozun.nebula.dao.product.PropertyDao;
import com.baozun.nebula.dao.product.PropertyValueDao;
import com.baozun.nebula.dao.product.ShopCommandDao;
import com.baozun.nebula.dao.product.ShopDao;
import com.baozun.nebula.dao.product.ShopPropertyDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.baseinfo.Shop;
import com.baozun.nebula.model.product.CommonProperty;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.ShopProperty;
import com.baozun.nebula.web.UserDetails;

/**
 * @author yi.huang
 * @date 2013-7-2 上午10:17:43
 */
@Transactional
@Service("shopManager")
public class ShopManagerImpl implements ShopManager{

	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(ShopManagerImpl.class);

	@Autowired
	private ShopDao				shopDao;

	@Autowired
	private ShopCommandDao		shopCommandDao;

	@Autowired
	private OrganizationDao		organizationDao;

	@Autowired
	private ShopPropertyDao		shopPropertyDao;

	@Autowired
	private PropertyValueDao	propertyValueDao;

	@Autowired
	private PropertyDao			propertyDao;

	@Autowired
	private CommonPropertyDao			commonPropertyDao;

	@Autowired
	private IndustryDao			industryDao;

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.shop.ShopManager#findByOrgaTypeId(java.lang.Long)
	 */
	@Override
	public List<ShopCommand> findShopListByQueryMap(Map<String, Object> paraMap, Sort[] sorts){
		return shopCommandDao.findShopListByOrgaTypeId(paraMap,sorts);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ShopManager#removeShopByIds(java.lang.Long[])
	 */
	@Override
	public Integer removeShopByIds(Long[] shopIds){
		// int idsLength = shopIds.length;
		Integer result = shopDao.removeShopByIds(shopIds);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ShopManager#enableOrDisableShopByIds(java.lang.Long[], java.lang.Integer)
	 */
	@Override
	public Integer enableOrDisableShopByIds(Long[] shopIds,Integer type){
		// int idsLength = shopIds.length;
		Integer result = shopDao.enableOrDisableShopByIds(shopIds, type);
		return result;
	}

	@Override
	public List<Industry> findAllIndustryList(Sort[] sorts){
		return shopDao.findIndustryList(sorts);
	}

	/**
	 * 增加店铺
	 * 
	 * @param name
	 * @param code
	 * @param description
	 * @param lifecycle
	 * @param industryAttr
	 * @return
	 */
	private Shop addShop(Shop shop,Organization organization,ShopProperty[] shopProperties){
		try{
			// 增加Organization对象，并获取返回的组织记录对象
			Organization getOrganization = organizationDao.save(organization);
			shop.setOrgId(getOrganization.getId());
			Shop getShop = shopDao.save(shop);
			// 获取属性并在shopProperty中插入相关记录
			for(ShopProperty shopProperty:shopProperties){
                shopDao.createShopproperty(shopProperty.getIndustryId(), shop.getId(), shopProperty.getPropertyId());
           }
			return getShop;
		}catch (Exception e){
			throw new BusinessException(ErrorCodes.PRODUCT_ADD_SHOP_FAIL);
		}
	}

	/**
	 * 修改店铺
	 * 
	 * @param orgId
	 * @param shopId
	 * @param name
	 * @param code
	 * @param description
	 * @param lifecycle
	 * @param industryAttr
	 * @return
	 */
	private Shop updateShop(Shop shop,Organization organization,ShopProperty[] shopProperties){
		try{
			shop.setOrgId(organization.getId());
			// 修改组织表内容
			Integer flag = shopDao.updateOrganization(organization.getId(), organization.getName(), organization.getCode(), organization
					.getDescription(), organization.getLifecycle());
			// 首先刪除該店铺下面的所有属性，然後增加相关的属性
			shopDao.removeShopPropertyByshopId(shop.getId());
			for(ShopProperty shopProperty:shopProperties){
	             shopDao.createShopproperty(shopProperty.getIndustryId(), shop.getId(), shopProperty.getPropertyId());
	        }
			return shop;
		}catch (Exception e){
			throw new BusinessException(ErrorCodes.PRODUCT_UPDATE_SHOP_FAIL);
		}
	}

	@Override
	public Shop createOrUpdateShop(Shop shop,Organization organization,ShopProperty[] shopProperties){
		Long id = null;
		Shop getShop = null;
		/** shopId!=null：修改操作， 反之为增加操作 */
		if (shop.getId() == null && organization.getId() == null){
			getShop = this.addShop(shop, organization, shopProperties);
		}else{
			getShop = this.updateShop(shop, organization, shopProperties);
		}
		return getShop;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ShopManager#findShopById(java.lang.Long)
	 */
	@Override
	public ShopCommand findShopById(Long id){

		return shopDao.findShopById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ShopManager#findShopPropertyByPropertyId(java.lang.Long)
	 */
	@Override
	public ShopProperty findShopPropertyByPropertyId(Long propertyId){
		return shopPropertyDao.findShopPertyByPropertyId(propertyId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ShopManager#findPropertyValueList(java.lang.Long)
	 */
	@Override
	public List<PropertyValue> findPropertyValueList(Long propertyId){
		return propertyValueDao.findPropertyValueListById(propertyId);
	}

	

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ShopManager#enableOrDisablePropertyByIds(java.util.List, java.lang.Integer)
	 */
	@Override
	public boolean enableOrDisablePropertyByIds(List<Long> ids,Integer state){

		Integer result = shopDao.enableOrDisablePropertyByIds(ids, state);
		return result == 1;

	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ShopManager#findPropertyById(java.lang.Long)
	 */
	@Override
	public Property findPropertyById(Long propertyId){
		List<Long> ids = new ArrayList<Long>();
		ids.add(propertyId);
		Property p = propertyDao.findPropertyById(propertyId);
		return p;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ShopManager#findPropertyListByIndustryIdAndShopId(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<Property> findPropertyListByIndustryIdAndShopId(Long industryId,Long shopId,Sort[] sorts){
		return shopDao.findPropertyListByIndustryIdAndShopId(industryId, shopId,sorts);
	}

	/**
	 * 获取行业属性列表集合
	 * 
	 * @param industryId
	 * @return caihong.wu
	 */
	private List<Property> findSysPropertyListByIndustryId(Long industryId){
		return shopDao.findSysPropertyListByIndustryId(industryId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ShopManager#removePropertyByIds(java.util.List, java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean removePropertyByIds(List<Long> propertyIds,Long shopId,Long industryId){
		// 1 更新property的 lifecycle
		Integer result = shopDao.removePropertyByIds(propertyIds);
		// 2 删除shopproperty中的对应关系
		Integer result1 = shopPropertyDao.removeShopProperty(propertyIds, shopId, industryId);
		if (result != propertyIds.size()){
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ShopManager#findCreatePropertyDefaultSortNo(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Integer findCreatePropertyDefaultSortNo(Long shopId,Long industryId){

		return shopDao.findCreatePropertyDefaultSortNo(shopId, industryId);
	}

	@Override
	public Integer validateShopCode(String code){
		return organizationDao.validateShopCode(code);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.IndustryManager#findIndustryListByShopId(java.lang.Long)
	 */
	@Override
	public List<Industry> findIndustryListByShopId(Long shopId){

		return industryDao.findIndustryListByShopId(shopId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ShopManager#createOrUpdateProperty(com.baozun.nebula.model.product.Property, java.lang.Long,
	 * java.lang.Integer)
	 */
	@Override
	public boolean createOrUpdateProperty(Property property,Long shopId,Integer type){
		
		if (type == 1){// 保存
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("name", property.getName());
			m.put("editingType", property.getEditingType());
			m.put("valueType", property.getValueType());
			m.put("isSaleProp", property.getIsSaleProp());
			m.put("isColorProp", property.getIsColorProp());
			m.put("required", property.getRequired());
			m.put("searchable", property.getSearchable());
			m.put("hasThumb", property.getHasThumb());
			m.put("sortNo", property.getSortNo());
			m.put("lifecycle", property.getLifecycle());
			m.put("industryId", property.getIndustryId());
			m.put("groupName", property.getGroupName());

			Integer result = propertyDao.updatePropertyById(property.getId(), m);
			// 更新行业属性扩展表
			
			Integer commonPropertyResult = commonPropertyDao.updateCommonPropertyById(propertyDao.getByPrimaryKey(property.getId()) != null ? propertyDao.getByPrimaryKey(property.getId()).getCommonPropertyId() : 0, m);
			
			return result == 1 || commonPropertyResult == 1;
		}else{
			CommonProperty commonProperty = new CommonProperty();
			commonProperty.setCreateTime(new Date());
			CommonProperty cProperty = commonPropertyDao.save(commonProperty);
			
			property.setCreateTime(new Date());
			property.setIsCommonIndustry(false);
			property.setCommonPropertyId(cProperty.getId());
			Property p = propertyDao.save(property);
			Long propertyId = p.getId();
			Long industryId = p.getIndustryId();

			Integer result = shopPropertyDao.addShopProperty(industryId, propertyId, shopId);
			return result == 1;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.ShopManager#validatePropertyName(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean validatePropertyName(String propertyName,Integer industryId){
		Integer result = shopDao.countRepeatNameNo(propertyName, industryId);
		return result == 0;
	}

	@Override
	public List<ShopProperty> findShopPropertyByshopId(Long shopId){
		return shopPropertyDao.findShopPropertyByshopId(shopId);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.baseinfo.ShopManager#createOrUpdatePropertyValueByList(com.baozun.nebula.model.product.PropertyValue[])
	 */
	@Override
	public void createOrUpdatePropertyValueByList(PropertyValue[] propertyValues,Long propertyId) {
		//Long propertyId = propertyValues[0].getPropertyId();
		List<PropertyValue> propertyValue = propertyValueDao.findPropertyValueListById(propertyId);
		List<Long> ids = new ArrayList<Long>();//数据库中的ID
		for(PropertyValue pv: propertyValue){
			Long id = pv.getId();
			ids.add(id);
		}
		List<Long> idnew = new ArrayList<Long>();//存放页面修改后的ID
			for(PropertyValue pv:propertyValues){
				if(StringUtils.isNotBlank(pv.getValue())){
					Long id = pv.getId();
					String value = pv.getValue();
					String thumb = pv.getThumb();
					Integer sortNo = pv.getSortNo();
					if(null!=id){
						idnew.add(id);
						propertyValueDao.updateById(id,value, thumb, sortNo);
					}else {
						Calendar calendar = Calendar.getInstance();
						Date date = calendar.getTime();
						pv.setModifyTime(date);
						pv.setCreateTime(date); 
						pv.setPropertyId(propertyId);
						pv.setThumb(thumb);
						propertyValueDao.save(pv);
					}
				}
			}
			ids.removeAll(idnew);
			propertyValueDao.deleteById(ids);
	}
	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.baseinfo.ShopManager#findShopByOrgId(Long id)
	 */
	@Override
	public ShopCommand findShopByOrgId(Long id) {
		ShopCommand shopCommand =shopDao.findShopByOrgId(id);
		return shopCommand;
	}

	@Override
	public Long getShopId(UserDetails userDetails) {
		Long shopId = 0L;

		// 查询orgId
		Long currentOrgId = userDetails.getCurrentOrganizationId();
		// 根据orgId查询shopId
		if (currentOrgId != null) {
			ShopCommand shopCommand = findShopByOrgId(currentOrgId);
			if (shopCommand != null) {
				shopId = shopCommand.getShopid();
			}
		}
		return shopId;
	}

	@Override
	public boolean validatePropertyName(String name,
			Integer propertyId, String lang) {
		 Integer count= shopDao.countRepeatNameNoi18n(name, propertyId, lang);
		 return count == 0;
	}

	@Override
	public List<Industry> findAllEnabledIndustryByShopId(Long shopId) {
		return shopDao.findAllEnabledIndustryByShopId(shopId);
	}

}
