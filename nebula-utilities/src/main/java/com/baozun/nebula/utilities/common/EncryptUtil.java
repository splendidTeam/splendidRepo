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
 *
 */
package com.baozun.nebula.utilities.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.convertor.Base64Convertor;
import com.baozun.nebula.utilities.common.encryptor.EncryptionException;
import com.baozun.nebula.utilities.common.encryptor.Encryptor;

import static com.feilong.core.date.DateExtensionUtil.formatDuration;

/**
 * 常用加解密，hash，digest
 * 
 * @author Benjamin.Liu
 *
 */
public class EncryptUtil{

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtil.class);

    public static final String ENCRYPTOR_PREFIX = "encryptor.";

    public static final String ENCRYPTORS = "encryptors";

    public static int DEFAULT_ITERATIONS = 1024;

    public static int DEFAULT_KEYLENGTH = 512;

    public static final String DEFAULT_ENCRYPT_ALGORITHM = "AES";

    public static final String DEFAULT_SIGNATURE_ALGORITHM = "SHA1withDSA";

    public static final String DEFAULT_RANDOM_ALGORITHM = "SHA1PRNG";

    public static final String DEFAULT_HASH_ALGORITHM = "SHA-512";

    public static final String DEFAULT_DIGEST_ALGORITHM = "MD5";

    public static final String DEFAULT_HASHSALT_ALGORITHM = "PBKDF2WithHmacSHA1";

    private static EncryptUtil instance = new EncryptUtil();

    private Map<String, Encryptor> encryptors = new HashMap<String, Encryptor>();

    private Encryptor encryptor = null;

    private Base64Convertor base64Convertor = new Base64Convertor();

    private ThreadLocal<MessageDigest> hasherContext = new ThreadLocal<MessageDigest>();

    private ThreadLocal<MessageDigest> digesterContext = new ThreadLocal<MessageDigest>();

    private SecretKeyFactory secretKeyFactory;

    private EncryptUtil(){

        Date beginDate = new Date();

        //init SecretKeyFactory
        String hashAlgorithm = null;
        try{
            hashAlgorithm = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration(ConfigurationUtil.HASHSALT_ALGORITHM_KEY);
            secretKeyFactory = SecretKeyFactory.getInstance(hashAlgorithm == null ? DEFAULT_HASHSALT_ALGORITHM : hashAlgorithm);
        }catch (NoSuchAlgorithmException e1){
            LOGGER.error("No algorithm found for digest with name {}", hashAlgorithm == null ? DEFAULT_HASHSALT_ALGORITHM : hashAlgorithm);
        }

        //init encryptor
        String[] encryptorArray = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration(ENCRYPTORS).split(",");
        for (String strEnc : encryptorArray){
            String encryptClassKey = ENCRYPTOR_PREFIX + strEnc;
            String encryptClass = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration(encryptClassKey);
            assert encryptClass != null : "Cannot find Class Definition for Encryptor:" + strEnc;
            try{

                Encryptor e = (Encryptor) Class.forName(encryptClass).newInstance();
                encryptors.put(strEnc, e);
            }catch (Exception e){
                LOGGER.error("Encryptor initialization fail. {} can not be initialized with Error {}", encryptClassKey, e.getClass());
            }
        }
        String encryptMethod = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration(ConfigurationUtil.ENCRYPT_ALGORITHM_KEY);
        encryptMethod = encryptMethod == null ? DEFAULT_ENCRYPT_ALGORITHM : encryptMethod;
        encryptor = encryptors.get(encryptMethod);

        if (LOGGER.isInfoEnabled()){
            LOGGER.info("use time: [{}]", formatDuration(beginDate));
        }

    }

    public static EncryptUtil getInstance(){
        return instance;
    }

    /**
     * 取得Hash值
     * 
     * @param plainText
     * @param salt
     * @return
     */
    public String hash(String plainText,String salt){
        return hash(plainText, salt, DEFAULT_ITERATIONS);
    }

    /**
     * 密码加密使用了新的迭代加盐hash算法进行加密
     * add by ruichao.gao
     * 
     * @param password
     * @param salt
     * @return
     */
    public String hashSalt(String password,String salt){
        try{
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(ConfigurationUtil.DEFAULT_ENCODING), DEFAULT_ITERATIONS, DEFAULT_KEYLENGTH);
            SecretKey key = secretKeyFactory.generateSecret(spec);
            byte[] encoded = key.getEncoded();
            return base64Convertor.format(encoded);
        }catch (InvalidKeySpecException | UnsupportedEncodingException e){
            LOGGER.error("Get Bytes error with UTF-8", e);
            throw new RuntimeException("Get Bytes error with UTF-8");
        }
    }

    /**
     * 取得Hash值
     * 
     * @param plainText
     * @param salt
     * @param iterations
     * @return
     */
    public String hash(String plainText,String salt,int iterations){
        byte[] bytes = hashNative(plainText, salt, iterations);
        if (bytes == null)
            return null;
        return base64Convertor.format(bytes);
    }

    private MessageDigest getHasher(){
        String hashAlgorithm = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration(ConfigurationUtil.HASH_ALGORITHM_KEY);
        try{
            return MessageDigest.getInstance(hashAlgorithm == null ? DEFAULT_HASH_ALGORITHM : hashAlgorithm);
        }catch (NoSuchAlgorithmException e){
            LOGGER.error("No algorithm found for digest with name {}", hashAlgorithm == null ? DEFAULT_HASH_ALGORITHM : hashAlgorithm);
            LOGGER.error("Digester for Hash initialzation fail, so no further hash operation can be success");
        }
        return null;
    }

    private MessageDigest getDigester(){
        String digestAlgorithm = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration(ConfigurationUtil.DIGEST_ALGORITHM_KEY);
        try{
            return MessageDigest.getInstance(digestAlgorithm == null ? DEFAULT_DIGEST_ALGORITHM : digestAlgorithm);
        }catch (NoSuchAlgorithmException e){
            LOGGER.error("No algorithm found for digest with name {}", digestAlgorithm == null ? DEFAULT_DIGEST_ALGORITHM : digestAlgorithm);
            LOGGER.error("Digester initialzation fail, so no further digest operation can be success");
        }
        return null;
    }

    private byte[] hashNative(String plainText,String salt,int iterations){
        if (plainText == null)
            return null;
        try{
            MessageDigest hasher = hasherContext.get();
            if (hasher == null){
                hasher = getHasher();
                hasherContext.set(hasher);
            }

            hasher.reset();
            if (salt != null)
                hasher.update(salt.getBytes(ConfigurationUtil.DEFAULT_ENCODING));
            hasher.update(plainText.getBytes(ConfigurationUtil.DEFAULT_ENCODING));

            byte[] bytes = hasher.digest();
            for (int i = 0; i < iterations; i++){
                hasher.reset();
                bytes = hasher.digest(bytes);
            }
            return bytes;
        }catch (UnsupportedEncodingException e){
            LOGGER.error("Get Bytes for digest data error with UTF-8");
            throw new RuntimeException("Get Bytes for digest data error with UTF-8");
        }
    }

    /**
     * 获得缺省的加解密器，默认是对称加密
     * 
     * @return
     */
    public Encryptor getEncryptor(){
        return encryptor;
    }

    /**
     * 获得加解密器
     * 
     * @param encType
     * @return
     */
    public Encryptor getEncryptor(String encType){
        return encryptors.get(encType);
    }

    /**
     * 加密，加密字符串是经过BASE64转码的
     * 
     * @param plainText
     * @param encType
     * @return
     * @throws EncryptionException
     */
    public String encrypt(String plainText,String encType) throws EncryptionException{
        Encryptor e = encryptors.get(encType);
        if (e == null)
            throw new EncryptionException("No proper Encryptor found for " + encType);
        return e.encrypt(plainText);
    }

    /**
     * 解密，默认加密字符串是经过BASE64转码的
     * 
     * @param cipherText
     * @param encType
     * @return
     * @throws EncryptionException
     */
    public String decrypt(String cipherText,String encType) throws EncryptionException{
        Encryptor e = encryptors.get(encType);
        if (e == null)
            throw new EncryptionException("No proper Encryptor found for " + encType);
        return e.decrypt(cipherText);
    }

    /**
     * 使用默认加解密器加密，加密字符串是经过BASE64转码的
     * 
     * @param plainText
     * @return
     * @throws EncryptionException
     */
    public String encrypt(String plainText) throws EncryptionException{
        if (encryptor == null)
            throw new EncryptionException("Encryptor does not initiate properly.");
        return encryptor.encrypt(plainText);
    }

    /**
     * 使用默认加解密器解密，默认加密字符串是经过BASE64转码的
     * 
     * @param cipherText
     * @return
     * @throws EncryptionException
     */
    public String decrypt(String cipherText) throws EncryptionException{
        if (encryptor == null)
            throw new EncryptionException("Encryptor does not initiate properly.");
        return encryptor.decrypt(cipherText);
    }

    public String sign(String data){
        //TODO
        return null;
    }

    public boolean verifySign(String signature,String data){
        //TODO
        return false;
    }

    public String digest(String data){
        if (data == null)
            return null;
        try{
            MessageDigest digester = digesterContext.get();
            if (digester == null){
                digester = getDigester();
                digesterContext.set(digester);
            }
            digester.reset();
            return StringUtil.bytes2String(digester.digest(data.getBytes(ConfigurationUtil.DEFAULT_ENCODING)));
        }catch (UnsupportedEncodingException e){
            LOGGER.error("Get Bytes for digest data error with UTF-8");
            throw new RuntimeException("Get Bytes for digest data error with UTF-8");
        }
    }

    public static SecureRandom getSecureRandom(){
        String randomAlgorithm = ConfigurationUtil.getInstance().getNebulaUtilityConfiguration(ConfigurationUtil.RANDOM_ALGORITHM_KEY);
        try{
            if (randomAlgorithm == null)
                randomAlgorithm = DEFAULT_RANDOM_ALGORITHM;
            SecureRandom random = SecureRandom.getInstance(randomAlgorithm);
            return random;
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("Get SecureRandom Implementation Error with Algorithem " + randomAlgorithm);
        }
    }

    /**
     * BASE64转码
     * 
     * @param source
     * @return
     */
    public String base64Encode(String source){

        if (StringUtils.isBlank(source))
            return null;
        else
            return base64Convertor.format(source.getBytes());
    }

    /**
     * BASE64解码
     * 
     * @param source
     * @return
     */
    public String base64Decode(String source){

        if (StringUtils.isBlank(source))
            return null;
        else
            return new String(base64Convertor.parse(source));
    }

}
