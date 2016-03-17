package com.baozun.nebula.sdk.manager.impl;

import org.junit.Before;
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

import com.baozun.nebula.command.i18n.MutlLang;
import com.baozun.nebula.command.i18n.SingleLang;
import com.baozun.nebula.command.product.SearchConditionItemCommand;
import com.baozun.nebula.sdk.manager.SdkSearchConditionItemManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class SdkSearchConditionItemManagerImplTest {
	public static final Logger log = LoggerFactory
			.getLogger(SdkSearchConditionItemManagerImplTest.class);
	
	@Autowired
	SdkSearchConditionItemManager sdkSearchConditionItemManager;
	
	@Before
	public void before(){
		ProfileConfigUtil.setMode("dev");
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void createOrUpdateConditionItemTest() {
//		id:9
//		pid:5
//		propertyId:22
//		propertyValueId:24
//		name:大童(7-16岁)123
//		sort:1223
		SearchConditionItemCommand searchCondition = new SearchConditionItemCommand();
		searchCondition.setPropertyId(22l);
		searchCondition.setPropertyValueId(24l);
		searchCondition.setSort(12);
		searchCondition.setId(9l);
		MutlLang name = new MutlLang();
		String[]  values = new String[]{"中文2","English"};
		String[]  langs = new String[]{"zh_cn","en_us"};
		name.setLangs(langs);
		name.setValues(values);
		searchCondition.setName(name);
		sdkSearchConditionItemManager.createOrUpdateConditionItem(searchCondition);
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void createOrUpdateSearchConditionTestSingle() {
		SearchConditionItemCommand searchCondition = new SearchConditionItemCommand();
		searchCondition.setPropertyId(22l);
		searchCondition.setPropertyValueId(24l);
		searchCondition.setSort(12);
		//searchCondition.setId(9l);
		SingleLang name = new SingleLang();
		name.setValue("中文4");
		searchCondition.setName(name);
		sdkSearchConditionItemManager.createOrUpdateConditionItem(searchCondition);
	}
}
