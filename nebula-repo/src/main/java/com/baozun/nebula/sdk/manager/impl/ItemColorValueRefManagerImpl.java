package com.baozun.nebula.sdk.manager.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.product.InstationMessageTemplateDao;
import com.baozun.nebula.dao.product.ItemColorRefDao;
import com.baozun.nebula.dao.product.ItemColorValueRefDao;
import com.baozun.nebula.dao.product.PropertyValueDao;
import com.baozun.nebula.model.product.ItemColorReference;
import com.baozun.nebula.model.product.ItemColorValueRef;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.system.InstationMessageTemplate;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.ItemColorValueRefManager;
import com.baozun.nebula.solr.utils.Validator;
import com.baozun.nebula.manager.CacheManager;
/** 
* @ClassName: ItemColorRefManagerImpl 
* @Description: (查询商品颜色对照表数据) 
* @author gewei.lu <gewei.lu@baozun.cn> 
* @date 2016年1月7日 下午2:17:50 
*  
*/
@Transactional
@Service("itemColorValueRefManager")
public class ItemColorValueRefManagerImpl implements ItemColorValueRefManager {
	
	@Autowired
	private ItemColorRefDao itemColorRefDao;
	
	@Autowired
	private CacheManager 	cacheManager;
	
	
	@Autowired
	private  PropertyValueDao propertyValueDao;
	
	@Autowired
	private  ItemColorValueRefDao  itemColorValueRefDao;
	
	
	@Autowired
	private InstationMessageTemplateDao   instationMessageTemplateDao;
	
	
	/*<p>Title: findItemColorValueReflist</p> 
	* <p>Description: </p> 
	* @param page
	* @param sorts
	* @param paraMap
	* @return 
	* @see com.baozun.nebula.sdk.manager.ItemColorValueRefManager#findItemColorValueReflist(loxia.dao.Page, loxia.dao.Sort[], java.util.Map) 
	* @date 2016年1月13日 上午10:48:50 
	* @author GEWEI.LU   
	*/
	@Override
	@Transactional(readOnly=true)
	public Pagination<ItemColorValueRef> findItemColorValueReflist(Page page, Sort[] sorts, Map<String, Object> paraMap) {
		return itemColorRefDao.findItemColorRefList(page, sorts,paraMap);
	}

	/*<p>Title: saveColorReflist</p> 
	* <p>Description: </p> 
	* @param itemColorRef
	* @return 
	* @see com.baozun.nebula.sdk.manager.ItemColorValueRefManager#saveColorReflist(com.baozun.nebula.model.product.ItemColorValueRef) 
	* @date 2016年1月13日 上午10:48:53 
	* @author GEWEI.LU   
	*/
	@Override
	@Transactional
	public ItemColorValueRef saveColorReflist(ItemColorValueRef itemColorRef) {
		return itemColorRefDao.save(itemColorRef);
	}

	/*<p>Title: delectColorReflist</p> 
	* <p>Description: </p> 
	* @param id
	* @return 
	* @see com.baozun.nebula.sdk.manager.ItemColorValueRefManager#delectColorReflist(java.lang.Long) 
	* @date 2016年1月13日 上午10:48:56 
	* @author GEWEI.LU   
	*/
	@Override
	@Transactional
	public int delectColorReflist(Long id) {
		return itemColorRefDao.deletetemColorRef(id);
	}
	
	/**
	 * 缓存取值
	 */
	/*<p>Title: itemColorValueReflistMap</p> 
	* <p>Description: </p> 
	* @return 
	* @see com.baozun.nebula.sdk.manager.ItemColorValueRefManager#itemColorValueReflistMap() 
	* @date 2016年1月13日 上午10:48:46 
	* @author GEWEI.LU   
	*/
	@Override
	public Map<String, String> itemColorValueReflistMap(){
		Map<String, String> result= cacheManager.getMapObject(Constants.ITEM_COLOR_VALUE_REFERENCE,Constants.BRAND_ITEM_COLOR_VALUE_REFERENCE);
		if(Validator.isNotNullOrEmpty(result)){
			return result;
		}
		result = new HashMap<String, String>();
		List<ItemColorValueRef> colorRefList =itemColorRefDao.itemColorValueReflistMap();
		for (ItemColorValueRef color : colorRefList) {
			result.put(color.getItemColor(), color.getFilterColor()+"|"+color.getFilterColorValue());
		}
		cacheManager.setMapObject(Constants.ITEM_COLOR_VALUE_REFERENCE, Constants.BRAND_ITEM_COLOR_VALUE_REFERENCE, result, 60*5);
		return result;
	}

	/*<p>Title: saveItemColorValueRef</p> 
	* <p>Description: </p> 
	* @param itemColorReference
	* @return 
	* @see com.baozun.nebula.sdk.manager.ItemColorValueRefManager#saveItemColorValueRef(com.baozun.nebula.model.product.ItemColorReference) 
	* @date 2016年1月13日 上午10:48:36 
	* @author GEWEI.LU   
	*/
	@Override
	@Transactional
	public ItemColorReference saveItemColorValueRef(ItemColorReference itemColorReference) {
		return itemColorValueRefDao.save(itemColorReference);
	}
	
	
	/*<p>Title: findItempropertyValue</p> 
	* <p>Description: </p> 
	* @param mapvalue
	* @param valuecolor
	* @return 
	* @see com.baozun.nebula.sdk.manager.ItemColorValueRefManager#findItempropertyValue(java.util.Map, java.lang.String) 
	* @date 2016年1月13日 上午10:48:32 
	* @author GEWEI.LU   
	*/
	@Override
	@Transactional
	public PropertyValue findItempropertyValue(Map<String,String> mapvalue,String valuecolor) {
		return propertyValueDao.findpropervalueId(mapvalue,valuecolor);
	}

	/*<p>Title: findItItemColorReferenceList</p> 
	* <p>Description: </p> 
	* @param paraMap
	* @return 
	* @see com.baozun.nebula.sdk.manager.ItemColorValueRefManager#findItItemColorReferenceList(java.util.Map) 
	* @date 2016年1月13日 上午10:31:11 
	* @author GEWEI.LU   
	*/
	@Override
	public List<ItemColorReference> findItItemColorReferenceList(Map<String, Long> paraMap) {
		return itemColorValueRefDao.findItItemColorReferenceList(paraMap);
	}
	/*<p>Title: findTempletByQueryMapWithPage</p> 
	* <p>Description: </p> 
	* @param page
	* @param sorts
	* @param paraMap
	* @return 
	* @see com.baozun.nebula.sdk.manager.ItemColorValueRefManager#findTempletByQueryMapWithPage(loxia.dao.Page, loxia.dao.Sort[], java.util.Map) 
	* @date 2016年1月15日 下午4:01:47 
	* @author GEWEI.LU   
	*/
	@Override
	public Pagination<InstationMessageTemplate> findTempletByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
		Pagination<InstationMessageTemplate> res = instationMessageTemplateDao.findTempletByQueryMapWithPage(page, sorts, paraMap);
		return res;
	}
	
	/** 
	* @Title: saveinstationMessageTemplate 
	* @Description:(添加模板) 
	* @param @param instationMessageTemplate
	* @param @return    设定文件 
	* @return InstationMessageTemplate    返回类型 
	* @throws 
	* @date 2016年1月15日 下午4:05:02 
	* @author GEWEI.LU   
	*/
	public InstationMessageTemplate saveinstationMessageTemplate(InstationMessageTemplate instationMessageTemplate) {
		return instationMessageTemplateDao.save(instationMessageTemplate);
	}
	
	
	/** 
	* @Title: delectinstationMessageTemplate 
	* @Description:(删除模板) 
	* @param @param id    设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年1月15日 下午4:07:49 
	* @author GEWEI.LU   
	*/
	public void delectinstationMessageTemplate(Long id) {
		 instationMessageTemplateDao.deleteByPrimaryKey(id);
	}
	
	
	/** 
	* @Title: updateinstationMessageTemplate 
	* @Description:(修改) 
	* @param @param id    设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年1月15日 下午4:11:47 
	* @author GEWEI.LU   
	*/
	public void updateinstationMessageTemplate(Long type,Long id) {
		 instationMessageTemplateDao.updateinstationMessageTemplate(type,id);
	}
	
	
	/*<p>Title: finInstationMessageTemplatelist</p> 
	* <p>Description: </p> 
	* @return 
	* @see com.baozun.nebula.sdk.manager.ItemColorValueRefManager#finInstationMessageTemplatelist() 
	* @date 2016年1月15日 下午7:23:36 
	* @author GEWEI.LU   
	*/
	public List<InstationMessageTemplate> finInstationMessageTemplatelist(){
		return instationMessageTemplateDao.finInstationMessageTemplatelist();
	}
	
	
	/** 
	* @Title: findTempletByid 
	* @Description:(编辑) 
	* @param @param id
	* @param @return    设定文件 
	* @return InstationMessageTemplate    返回类型 
	* @throws 
	* @date 2016年1月19日 下午7:55:48 
	* @author GEWEI.LU   
	*/
	public InstationMessageTemplate  findTempletByid(Long id){
		return instationMessageTemplateDao.findTempletByid(id);
	}
	
}
