package com.baozun.nebula.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-test.xml" })
@ActiveProfiles("dev")
public class ProfileConfigUtilTest extends AbstractJUnit4SpringContextTests {

	@Resource(name = "configurationEncryptor")
	PBEStringEncryptor pbeStringEncryptor;

	private static List<String> properties;

	@Before
	public void before() {
		// 设置需要加密的属性
		properties = new ArrayList<String>();
		properties.add("dataSource.password=user_speedo");
	}


	// @Test
	public void testFindPro() {
		ProfileConfigUtil.setMode("dev");
		Properties properties = ProfileConfigUtil.findPro("config/metainfo.properties");
		System.out.println(properties.get("test001"));
		System.out.println(properties.get("test002"));
		System.out.println("===============over");
	}

	@Test
	public void encryptProperties() {
		for (String kv : properties) {
			encrypt(kv);
		}
	}

	private void encrypt(String kv) {
		logger.info("明文：" + kv);

		String[] kAndV = kv.split("=");
		String foo = pbeStringEncryptor.encrypt(kAndV[1]);
		logger.info(kAndV[0] + "=ENC(" + foo + ")");
	}

	  
}
