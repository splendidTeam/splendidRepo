package com.baozun.nebula.manager.cms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.sdk.manager.cms.SdkCmsPageTemplateManager;
import com.baozun.nebula.utils.FileUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class PageInstanceManagerTest {
	
	private static final Logger	log	= (Logger) LoggerFactory.getLogger(PageInstanceManagerTest.class);


	
	@Autowired
	private SdkCmsPageTemplateManager sdkCmsPageTemplateManager;
	
	

	@Test
	public void export() throws Exception{
		String html="";
		
		html=FileUtils.readFile("c:/html.txt", "ISO-8859-1");
		
		html=sdkCmsPageTemplateManager.addTemplateBase(html);
		System.out.println(html);
		

		html=sdkCmsPageTemplateManager.processTemplateBase(html);
		System.out.println(html);
	}
}
