package com.baozun.nebula.utilities.common;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 该类取值逻辑已经调整为profile无关，项目在打包时已经将对应profile配置做了区分打包
 * 
 * @author D.C
 *         2016年6月12日下午4:18:43
 */
public class ProfileConfigUtil{

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileConfigUtil.class);

    /**
     * 当前模式(dev,test,production)
     */
    private static String mode = "";

    private static StringEncryptor stringEncryptor;

    /**
     * getProfilePath方法中会对此路径进行处理，加上profile的路径
     * 如
     * 原路径为：config/abc.properties
     * 返回：config/dev/abc.properties
     */
    private static final String[] filterPath = { "config/" };

    /**
     * 用于缓存查询过的配置文件
     */
    private static Map<String, Properties> proMap = new HashMap<String, Properties>();

    public static void setMode(String m){
        LOGGER.debug("------------setModel:" + m);
        //mode=m;
    }

    public static String getMode(){
        return mode;
    }

    public void setStringEncryptor(StringEncryptor stringEncryptor){
        ProfileConfigUtil.stringEncryptor = stringEncryptor;
    }

    private static void checkModel(){
        /*
         * if(StringUtils.isBlank(mode)||mode.equalsIgnoreCase("null")){
         * throw new RuntimeException("profile获取在初始化之前，这是不被允许的！请联系NEBULA的相关人员！");
         * }
         */
    }

    /**
     * 获取profile对应的路径
     * 
     * @param source
     * @return
     */
    public static String getProfilePath(String source){
        //source与mode都不为空
        //if(StringUtils.isNotBlank(source) &&StringUtils.isNotBlank(mode)){
        for (String path : filterPath){
            //必须以此字符为前辍
            if (source.startsWith(path)){
                return source.replaceFirst(path, path + mode + "/");
            }
        }
        //}

        return source;
    }

    /**
     * @deprecated 推荐使用注入的方式获取属性文件的值，如：@Value("#{meta['order.subOrdinate.head']}")
     *             通过路径返回properties文件,会进行profile处理
     * @param source
     * @return
     */
    @Deprecated
    public static Properties findPro(String source){
        checkModel();
        String path = getProfilePath(source);

        Properties pro = proMap.get(path);
        if (pro == null){

            InputStream is = ResourceUtil.getResourceAsStream(path, ProfileConfigUtil.class);

            pro = new Properties();
            try{
                pro.load(is);
                pro = enableEncryptProperties(pro);
                proMap.put(path, pro);
            }catch (Exception e){
                LOGGER.error("配置文件 [" + path + "] 加载异常", e);
            }

        }

        return pro;
    }

    private static Properties enableEncryptProperties(Properties pro){
        if (stringEncryptor == null){
            return pro;
        }

        // 如果配置了加密类，则对配置文件中的加密属性提供解密能力
        EncryptableProperties encryptableProperties = new EncryptableProperties(stringEncryptor);
        encryptableProperties.putAll(pro);
        return encryptableProperties;
    }

    /**
     * @deprecated 推荐使用注入的方式获取属性文件的值，如：@Value("#{meta['order.subOrdinate.head']}")
     *             通过路径返回properties文件,不会进行profile路径处理
     * @param source
     * @return
     */
    @Deprecated
    public static Properties findCommonPro(String source){

        String path = source;

        Properties pro = proMap.get(path);
        if (pro == null){

            InputStream is = ResourceUtil.getResourceAsStream(path, ProfileConfigUtil.class);

            pro = new Properties();
            try{
                pro.load(is);
                pro = enableEncryptProperties(pro);
                proMap.put(path, pro);
            }catch (Exception e){
                LOGGER.error("配置文件 [" + source + "] 加载异常", e);
            }
        }

        return pro;
    }
}
