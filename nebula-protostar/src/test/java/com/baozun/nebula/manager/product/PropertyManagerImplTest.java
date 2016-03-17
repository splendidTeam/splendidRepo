package com.baozun.nebula.manager.product;
 

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.PropertyCommand;
import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;
import com.baozun.nebula.command.product.PropertyValueCommand;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
/**
 * 
 * @author  lin.liu
 * 属性管理manager 测试
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class PropertyManagerImplTest {
	@Autowired
	private PropertyManager propertyManager;

	private static final Logger log = LoggerFactory
			.getLogger(PropertyManagerImplTest.class);
	  

	@Test
	public void testFindPropertyListByQueryMapWithPage() {
		Page page=new Page();
		page.setSize(10);
		page.setStart(0);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("lifecycle",0);
		Pagination<PropertyCommand> propertys=propertyManager.findPropertyListByQueryMapWithPage(page, null, map);
		log.info("------------------testFindPropertyListByQueryMapWithPage  ={}",propertys.getItems().size());
	}

	@Test
	public void testEnableOrDisablePropertyByIds() { 
		List<Long> ids=new ArrayList<Long>();
		ids.add(1L);
		ids.add(2L);
		Integer state=1;
		Integer result=propertyManager.enableOrDisablePropertyByIds(ids, state);
		log.info("------------------testEnableOrDisablePropertyByIds  ={}",result);

	}

	@Test
	public void testRemovePropertyByIds() {
		List<Long> ids=new ArrayList<Long>();
		ids.add(1L);
		ids.add(2L);
		Integer result=propertyManager.removePropertyByIds(ids);
		log.info("------------------testRemovePropertyByIds  ={}",result);

	}
   
	
	 
	@Test
	public void testFindPropertyListByIndustryId(){
		
		List<Property> list = propertyManager.findPropertyListByIndustryId(121L);
		
		log.info("listsize==",list.size());
	}
	
	@Test
	public void testcreateOrUpdateProperty(){
		Property property = new Property();
		property.setEditingType(1);
		property.setHasThumb(true);
		property.setIndustryId(121L);
		property.setIsColorProp(true);
		property.setIsSaleProp(true);
		property.setName("张三");
		property.setRequired(true);
		property.setSearchable(true);
		property.setSortNo(1);
		property.setValueType(1);
		property.setLifecycle(1);
		 propertyManager.createOrUpdateProperty(property);
	}

	@Test
	public void testUpdatePropertyByParamList(){
		propertyManager.updatePropertyByParamList("propertyId16SortNo1");
	}
	
	/**
	 * Test method for {@link com.baozun.nebula.manager.product.PropertyValueManager#findPropertyValueListByPropertyId(java.lang.Long)}.
	 */
	@Test
	public void testFindPropertyValueListByPropertyId() {
		List<PropertyValue> propertyValues = propertyManager.findPropertyValueList(1L);
		for(PropertyValue pv:propertyValues){
			log.info("====findPropertyValueListByPropertyId==== 方法返回{}", pv.getValue());
		}

	}

	/**
	 * Test method for {@link com.baozun.nebula.manager.product.PropertyValueManager#insertPropertyValueList(com.baozun.nebula.model.product.PropertyValue[])}.
	 */
	@Test
	public void testInsertPropertyValueList() {
		fail("Not yet implemented");
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void createOrUpdatePropertyTest(){
		ProfileConfigUtil.setMode("dev");
		com.baozun.nebula.command.product.PropertyCommand property = new com.baozun.nebula.command.product.PropertyCommand();
		property.setEditingType(1);
		property.setHasThumb(true);
		property.setIndustryId(121L);
		property.setIsColorProp(true);
		property.setIsSaleProp(true);
		property.setRequired(true);
		property.setSearchable(true);
		property.setSortNo(1);
		property.setValueType(1);
		property.setLifecycle(1);
		MutlLang name = new MutlLang();
		String[]  values = new String[]{"中文3","English3"};
		String[]  langs = new String[]{"zh_cn","en_us"};
		name.setLangs(langs);
		name.setValues(values);
		MutlLang gname = new MutlLang();
		String[]  gvalues = new String[]{"g中文3","gEnglish3"};
		gname.setLangs(langs);
		gname.setValues(gvalues);
		property.setName(name);
		property.setGroupName(gname);
		property.setId(46l);
		propertyManager.createOrUpdateProperty(property);
	}
	@Test
	@Transactional
	@Rollback(false)
	public void createOrUpdatePropertyTestSingle(){
		ProfileConfigUtil.setMode("dev");
		com.baozun.nebula.command.product.PropertyCommand property = new com.baozun.nebula.command.product.PropertyCommand();
		property.setEditingType(1);
		property.setHasThumb(true);
		property.setIndustryId(121L);
		property.setIsColorProp(true);
		property.setIsSaleProp(true);
		property.setRequired(true);
		property.setSearchable(true);
		property.setSortNo(1);
		property.setValueType(1);
		property.setLifecycle(1);
		SingleLang name = new SingleLang();
		name.setValue("中文4");
		SingleLang gname = new SingleLang();
		gname.setValue("g中文4");
		property.setName(name);
		property.setGroupName(gname);
		property.setId(46l);
		propertyManager.createOrUpdateProperty(property);
	}

	
	@Test
	@Transactional
	@Rollback(false)
	public void createOrUpdatePropertyValueByListTest(){
		ProfileConfigUtil.setMode("dev");
		PropertyValueCommand[] pvcs = new PropertyValueCommand[3];
		for (int i = 0; i < 3; i++) {
			PropertyValueCommand pvc = new PropertyValueCommand();
			if(i%2==0){
				pvc.setId(51l+i);
			}
			MutlLang name = new MutlLang();
			pvc.setSortNo(1+i);
			String[]  values = new String[]{"中文"+i,"English"+i};
			String[]  langs = new String[]{"zh_cn","en_us"};
			name.setLangs(langs);
			name.setValues(values);
			pvc.setValue(name);
			pvcs[i] =pvc;
		}
		propertyManager.createOrUpdatePropertyValueByList(pvcs, 21l);
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void createOrUpdatePropertyValueByListTestSingle(){
		ProfileConfigUtil.setMode("dev");
		PropertyValueCommand[] pvcs = new PropertyValueCommand[3];
		for (int i = 0; i < 3; i++) {
			PropertyValueCommand pvc = new PropertyValueCommand();
			pvc.setId(47l+i);
			SingleLang name = new SingleLang();
			pvc.setSortNo(6+i);
			name.setValue("中文"+(i+3));
			pvc.setValue(name);
			pvcs[i] =pvc;
		}
		propertyManager.createOrUpdatePropertyValueByList(pvcs, 21l);
	}
}
