package com.baozun.nebula.security.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationEncryptorTest {

	final private Logger logger = LoggerFactory.getLogger(this.getClass());

	// <!-- 配置文件加密工具 start -->
	// <bean id="environmentVariablesConfiguration"
	// class="org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig">
	// <property name="algorithm" value="PBEWITHSHA256AND256BITAES-CBC-BC" />
	// <property name="passwordEnvName" value="APP_ENCRYPTION_PASSWORD" />
	// </bean>
	//
	// <bean id="bouncyCastleProvider"
	// class="org.bouncycastle.jce.provider.BouncyCastleProvider" />
	//
	// <bean id="configurationEncryptor"
	// class="org.jasypt.encryption.pbe.StandardPBEStringEncryptor">
	// <property name="provider" ref="bouncyCastleProvider" />
	// <property name="config" ref="environmentVariablesConfiguration" />
	// </bean>
	// <!-- 配置文件加密工具 end -->
	private StandardPBEStringEncryptor encryptor;

	@Before
	public void init() {
		EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
		config.setAlgorithm("PBEWITHSHA256AND256BITAES-CBC-BC");
		config.setPasswordEnvName("APP_ENCRYPTION_PASSWORD");

		BouncyCastleProvider provider = new BouncyCastleProvider();

		encryptor = new StandardPBEStringEncryptor();
		encryptor.setProvider(provider);
		encryptor.setConfig(config);
	}

	@Test
	public void testEncryptor() {
		String propertyKey = "user_samsung1234";

		String foo = encryptor.encrypt(propertyKey);
		logger.info("密文：" + foo);

		foo = encryptor.decrypt(foo);
		logger.info("明文：" + foo);
	}
	
	@Test
	public void testDecryptor() {

		String foo = encryptor.decrypt("U6bp7XgyPfckBcpfrI9InjMMJ0PrTjbZc0aI8YpKxGc=");
		logger.info("明文1：" + foo);
		
		foo = encryptor.decrypt("fu9mPQte2GcJGP/BCc0PvHWsnV7baOUVoTnPvHxV4cQ=");
		logger.info("明文2：" + foo);
		
		foo = encryptor.decrypt("S1rkAXZTbLDBlw+VVJk2cwZtzlr8nszXadowGMvUI4w=");
		logger.info("明文3：" + foo);
	}
}
