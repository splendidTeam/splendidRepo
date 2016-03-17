package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.ItemColorReference;
import com.baozun.nebula.model.product.ItemColorValueRef;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.system.InstationMessageTemplate;

/** 
* @ClassName: ItemColorValueRefManager 
* @Description: (查询商品颜色对照表数据) 
* @author gewei.lu <gewei.lu@baozun.cn> 
* @date 2016年1月7日 下午2:17:50 
*  
*/

public interface ItemColorValueRefManager extends BaseManager{
	/** 
	* @Title: Emailtemplatelogjsonlist 
	* @Description:  (查询商品颜色对照表数据) 
	* @param   page
	* @param   params
	* @param      设定文件 
	* @return Pagination<ItemColorValueRef>    返回类型 
	* @throws 
	*/
	public Pagination<ItemColorValueRef> findItemColorValueReflist(Page page, Sort[] sorts, Map<String, Object> paraMap);
	
	/** 
	* @Title: saveColorReflist 
	* @Description: (添加商品颜色对照表数据) 
	* @param @param ItemColorValueRef
	* @param @return    设定文件 
	* @return ItemColorValueRef    返回类型 
	* @throws 
	*/
	public ItemColorValueRef saveColorReflist(ItemColorValueRef ItemColorValueRef);
	
	
	/** 
	* @Title: delectColorReflist 
	* @Description: (删除商品颜色对照表数据) 
	* @param @param id
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws 
	*/
	public int delectColorReflist(Long id);
	
	
	/** 
	* @Title: itemColorValueReflistMap 
	* @Description: (缓存取LIST) 
	* @param  
	* @param @return    设定文件 
	* @return Map    返回类型 
	* @throws 
	*/
	public Map<String, String> itemColorValueReflistMap();
	
	/** 
	* @Title: ItemColorValueRef 
	* @Description: (插入数据) 
	* @param  
	* @param @return    设定文件 
	* @return Map    返回类型 
	* @throws 
	*/
	public ItemColorReference saveItemColorValueRef(ItemColorReference itemColorReference);
	
	
	/** 
	* @Title: findItempropertyValue 
	* @Description: (查数据) 
	* @param  
	* @param @return    设定文件 
	* @return Map    返回类型 
	* @throws 
	*/
	 
	public PropertyValue findItempropertyValue(Map<String,String> mapvalue,String valuecolor);
	
	
	/** 
	* @Title: findItItemColorReferenceList 
	* @Description:(查询是否有一存在的数据) 
	* @param @param paraMap
	* @param @return    设定文件 
	* @return List<ItemColorReference>    返回类型 
	* @throws 
	* @date 2016年1月13日 上午10:28:23 
	* @author GEWEI.LU   
	*/
	public List<ItemColorReference> findItItemColorReferenceList(Map<String, Long> paraMap);
	
	
	/** 
	* @Title: findTempletByQueryMapWithPage 
	* @Description:( 查找模板列表) 
	* @param @param page
	* @param @param sorts
	* @param @param paraMap
	* @param @return    设定文件 
	* @return Pagination<InstationMessageTemplate>    返回类型 
	* @throws 
	* @date 2016年1月15日 下午4:00:34 
	* @author GEWEI.LU   
	*/
	Pagination<InstationMessageTemplate> findTempletByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
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
	public InstationMessageTemplate saveinstationMessageTemplate(InstationMessageTemplate instationMessageTemplate);
	
	
	
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
	public InstationMessageTemplate  findTempletByid(Long id);
	
	
	/** 
	* @Title: delectinstationMessageTemplate 
	* @Description:(删除模板) 
	* @param @param id    设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年1月15日 下午4:07:49 
	* @author GEWEI.LU   
	*/
	public void delectinstationMessageTemplate(Long id);
	
	
	/** 
	* @Title: updateinstationMessageTemplate 
	* @Description:(修改) 
	* @param @param id    设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年1月15日 下午4:11:47 
	* @author GEWEI.LU   
	*/
	public void updateinstationMessageTemplate(Long type,Long id) ;
	
	
	/** 
	* @Title: finInstationMessageTemplatelist 
	* @Description:(查询可用的站内信息模板) 
	* @param @return    设定文件 
	* @return InstationMessageTemplate    返回类型 
	* @throws 
	* @date 2016年1月15日 下午7:22:35 
	* @author GEWEI.LU   
	*/
	public List<InstationMessageTemplate> finInstationMessageTemplatelist() ;
}
