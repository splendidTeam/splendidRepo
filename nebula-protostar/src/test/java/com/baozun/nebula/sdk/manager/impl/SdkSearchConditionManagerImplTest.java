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
import com.baozun.nebula.command.product.SearchConditionCommand;
import com.baozun.nebula.sdk.manager.SdkSearchConditionManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml", "classpath*:spring.xml" })
@ActiveProfiles("dev")
public class SdkSearchConditionManagerImplTest {
	public static final Logger log = LoggerFactory
			.getLogger(SdkSearchConditionManagerImplTest.class);

	@Autowired
	SdkSearchConditionManager sdkSearchConditionManager;

	@Before
	public void before(){
		ProfileConfigUtil.setMode("dev");
	}
	
	
	@Test
	@Transactional
	@Rollback(false)
	public void createOrUpdateSearchConditionTest() {
		SearchConditionCommand searchCondition = new SearchConditionCommand();
		searchCondition.setCategoryId(16l);
		searchCondition.setPropertyId(29l);
		searchCondition.setSort(12);
		searchCondition.setType(1);
		searchCondition.setId(7l);
		MutlLang name = new MutlLang();
		String[]  values = new String[]{"中文1","English"};
		String[]  langs = new String[]{"zh_cn","en_us"};
		name.setLangs(langs);
		name.setValues(values);
		searchCondition.setName(name);
		sdkSearchConditionManager
				.createOrUpdateSearchCondition(searchCondition);
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void createOrUpdateSearchConditionTestSingle() {
		SearchConditionCommand searchCondition = new SearchConditionCommand();
		searchCondition.setCategoryId(16l);
		searchCondition.setPropertyId(29l);
		searchCondition.setSort(12);
		searchCondition.setType(1);
		searchCondition.setId(7l);
		SingleLang name = new SingleLang();
		name.setValue("中文2");
		searchCondition.setName(name);
		sdkSearchConditionManager
				.createOrUpdateSearchCondition(searchCondition);
	}
}
