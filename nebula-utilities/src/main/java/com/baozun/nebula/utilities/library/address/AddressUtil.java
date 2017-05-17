/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.utilities.library.address;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utilities.common.ResourceUtil;
import com.feilong.core.Validator;

/**
 * 快递地址常用工具 通过读取nebula/full-address.json来获取 省市县以及街道的地址信息
 * 
 * nebula/address.json 只保存了省市县的地址信息
 * 
 * 国际化文件名:address_zh_CN.json
 * 
 * 通过zookeeper服务加载地址信息 当pts添加一种语言时,
 * zookeeper服务通过watch重新加载地址信息到内存中.此时系统会读取并解析configurations/address/
 * 下对应的address_语言标识.json文件
 * 
 * @author Tianlong.Zhang
 * 
 */
public class AddressUtil {
	private final static Logger log = LoggerFactory.getLogger(AddressUtil.class);

	/** 子区域Map key为父区域的Id ,value 为父区域下所有的子区域的列表 **/
	private static Map<Long, List<Address>> subAddressMap = new HashMap<Long, List<Address>>();

	/** 区域Map key为Id , value 为该 Id 代表的区域信息 **/
	private static Map<Long, Address> addressMap = new HashMap<Long, Address>();

	/** 用于保存地址的信息,名称以及idList **/
	private static Map<String, List<Address>> addressNameMap = new HashMap<String, List<Address>>();

	/** 包含 省市县，乡镇（街道） 的信息的文件 **/
	private static final String CONFIG_PATH_FULL = "configurations/address/full-address.json";

	/** 包含 省市县 的信息的文件 **/
	private static final String CONFIG_PATH = "configurations/address/address.json";

	/** 精简后的地址JSON 字符串 **/
	// private static String addressJson = null;

	private static final Long ROOT_ID = 1L;
	
	private static final Long COUNTY_ID = 0L;

	/*********************************** 国际化 ***********************************/
	/** 多语言地址信息文件存放的目录 **/
	private static final String LANGUAGE_CONFIG_PATH = "configurations/address/";

	/** 多语言地址信息的文件前缀 **/
	private static final String LANGUAGE_CONFIG_FILE_PREFIX = "address_";

	/** 多语言地址信息的文件后缀 **/
	private static final String LANGUAGE_CONFIG_FILE_SUFFIX = ".json";

	/** 多语言开关, 默认为不开启国际化, 设置为false时, languageList无效 **/
	private static Boolean i18nOffOn = false;
	
	/** 物流配送地址开关，默认不开启；开启后获取地址信息方式切换至从DB获取  **/
	private static Boolean deliveryModeOn = false;

	/** 语言集合 **/
	private static List<String> languageList = null;

	/** 子区域Map key:language ,value:key为父区域的Id ,value 为父区域下所有的子区域的列表 **/
	private static Map<String, Map<Long, List<Address>>> subAddressLangMap = new HashMap<String, Map<Long, List<Address>>>();

	/** 区域Map key: language , value: key为Id , value 为该 Id 代表的区域信息 **/
	private static Map<String, Map<Long, Address>> addressLangMap = new HashMap<String, Map<Long, Address>>();

	/** 用于保存地址的信息,名称以及idList **/
	private static Map<String, Map<String, List<Address>>> addressNameLangMap = new HashMap<String, Map<String, List<Address>>>();

	/**
	 * 根据pid来获取该pid对应区域下的子区域。 eg: 传入pid 0(代表中国) ,返回 中国的所有省份信息
	 * 
	 * @param pid
	 * @return
	 */
	public static List<Address> getSubAddressByPid(Long pid) {
		if (i18nOffOn) {
			Map<Long, List<Address>> subAddressMap = subAddressLangMap.get(LangUtil.ZH_CN);
			if (subAddressMap != null) {
				return subAddressMap.get(pid);
			}
			return null;
		} else {
			return subAddressMap.get(pid);
		}
	}

	/**
	 * 根据地址名称获取address list
	 * 
	 * @param pid
	 * @return
	 */
	public static List<Address> getAddressesByName(String name) {
		if (i18nOffOn) {
			Map<String, List<Address>> addressNameMap = addressNameLangMap.get(LangUtil.getCurrentLang());
			if (addressNameMap != null) {
				return addressNameMap.get(name);
			}
			return null;
		} else {
			return addressNameMap.get(name);
		}
	}

	/**
	 * 根据id来获取该id代表的区域信息 eg : id 为310000 ，返回上海的信息 多语言时使用
	 * 
	 * @param id
	 * @return
	 */
	public static Address getAddressById(Long id) {
		return getAddressById(id, LangUtil.getCurrentLang());
	}

	/**
	 * 根据id和 currentLang 来获取该id代表的区域信息.
	 * <p>
	 * eg : id 为310000 ，返回上海的信息 多语言时使用
	 * </p>
	 * <p>
	 * 该方法适用于 wormhole这种没有request语言环境的节点
	 * </p>
	 * 
	 * @param id
	 * @return
	 * @since 5.3.1
	 */
	public static Address getAddressById(Long id, String currentLang) {
		if (i18nOffOn) {
			Map<Long, Address> addressMap = addressLangMap.get(currentLang);
			if (addressMap != null) {
				return addressMap.get(id);
			}
			return null;
		} else {
			return addressMap.get(id);
		}
	}

	/**
	 * 得到省的地址信息列表
	 */
	public static List<Address> getProviences() {
		return getSubAddressByPid(ROOT_ID);
	}

	/**
	 * 得到市的地址信息列表
	 * 
	 * @param currentLang
	 *            多语言情况时,传当前的语言, 否则传null
	 */
	public static List<Address> getCities(Long provienceId) {
		return getSubAddressByPid(provienceId);
	}

	/**
	 * 得到县的地址信息列表
	 */
	public static List<Address> getCounties(Long cityId) {
		return getSubAddressByPid(cityId);
	}

	/**
	 * 得到乡/街道的地址信息列表
	 * 
	 * @param countyId
	 * @return
	 */
	public static List<Address> getTowns(Long countyId) {
		return getSubAddressByPid(countyId);
	}

	/**
	 * 生成包含省市区的json字符串（不含乡镇）
	 * 
	 * @param language
	 *            当前语言
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static String generateAddressJson(String language)
			throws JsonGenerationException, JsonMappingException, IOException {

		String json = "";
		if (i18nOffOn) {
			json = getFileContent(LANGUAGE_CONFIG_PATH + LANGUAGE_CONFIG_FILE_PREFIX + language + LANGUAGE_CONFIG_FILE_SUFFIX);
		} else {
			json = getFileContent(CONFIG_PATH);
		}
		ObjectMapper mapper = new ObjectMapper();
		Map<String, List<String>> map = (Map<String, List<String>>) mapper.readValue(json, Map.class);
		Map<Long, List<Address>> genSubAddressMap = new HashMap<Long, List<Address>>();
		for (String key : map.keySet()) {
			Address address = string2Address(Long.parseLong(key), map.get(key));
			gatherSubAddressMap(genSubAddressMap, address);
		}

		Map<Long, Map<Long, String>> outmap = new HashMap<Long, Map<Long, String>>();

		for (Long pid : genSubAddressMap.keySet()) {
			Map<Long, String> addressMap = new HashMap<Long, String>();

			for (Address address : genSubAddressMap.get(pid)) {
				addressMap.put(address.getId(), address.getName());
			}

			outmap.put(pid, addressMap);
		}

		return mapper.writeValueAsString(outmap);
	}

	public static void generateJsFile(String jsPath, Map<String, Map<String, String>> map){
		JSONObject json = (JSONObject) JSONObject.toJSON(map);
		String jsonString = Validator.isNotNullOrEmpty(json) ? json.toJSONString() : "{}";
		StringBuilder content = new StringBuilder();
		content.append("var districtJson = ").append(jsonString).append(";");
		File file = new File(jsPath);
		BufferedWriter bw = null;
		try {
			// 创建文件夹
			if (!file.getParentFile().exists()) {
				if(!file.getParentFile().mkdirs()){
					throw new RuntimeException("make dir Parent File Fail !");
				}
			}
			// 创建js文件
			if (!file.exists()) {
				if(!file.createNewFile()){
					throw new RuntimeException("create New File Fail !");
				}
			}
			// 写入内容
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			bw.write(content.toString());
			bw.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}
	
	/**
	 * 生成js文件
	 * 
	 * @param jsPath
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void generateJsFile(String jsPath, Boolean i18nOffOn, List<String> languageList)
			throws JsonGenerationException, JsonMappingException, IOException {
		if (i18nOffOn == null) {
			throw new IllegalArgumentException("i18nOnOff must be not null");
		}

		if (i18nOffOn && languageList == null) {
			throw new IllegalArgumentException("languageList must be not null");
		}

		setI18nOffOn(i18nOffOn);
		setLanguageList(languageList);
		init();
		if (i18nOffOn) {
			for (String language : languageList) {
				StringBuilder content = new StringBuilder(generateAddressJson(language));
				content.insert(0, "var districtJson = ");
				FileWriter f = new FileWriter(jsPath.split("\\.")[0] + "_" + language + "." + jsPath.split("\\.")[1]);
				f.write(content.toString());
				f.close();
			}
		} else {
			StringBuilder content = new StringBuilder(generateAddressJson(null));
			content.insert(0, "var districtJson = ");
			FileWriter f = new FileWriter(jsPath);
			f.write(content.toString());
			f.close();
		}

	}

	/**
	 * 初始化地址数据
	 */
	public static void init() {

		String json = "";

		// 加载多语言的地址信息
		if (i18nOffOn) {
			if (languageList == null || languageList.isEmpty()) {
				log.error("languageList is not set");
				throw new RuntimeException("languageList is not set");
			}

			for (String language : languageList) {
				json = getFileContent(LANGUAGE_CONFIG_PATH + LANGUAGE_CONFIG_FILE_PREFIX + language + LANGUAGE_CONFIG_FILE_SUFFIX);
				loadI18n(json, language);
			}
		} else {
			json = getFileContent(CONFIG_PATH_FULL);
			load(json);
		}

		if (log.isDebugEnabled()) {
			log.debug("Address util initialization is completed!");
		}
	}
	
	public static void initDeliveryArea(Map<String, Map<String, String>> map, String language, String jsPath) {
		setDeliveryModeOn(true);
		String path = jsPath+"area.min.js";
		if (i18nOffOn) {
			if (languageList == null || languageList.isEmpty()) {
				log.error("languageList is not set");
				throw new RuntimeException("languageList is not set");
			}
			path = jsPath+"/area."+language+".min.js";
		}
		generateJsFile( path, map );
		if(Validator.isNotNullOrEmpty(map)){
    		Map<Long, List<Address>> tmpSubAddressMap = new HashMap<Long, List<Address>>();
    		Map<Long, Address> tmpAddressMap = new HashMap<Long, Address>();
    		Map<String, List<Address>> tmpAddressNameMap = new HashMap<String, List<Address>>();
    	    for (String parentId : map.keySet()) {
	            for(String key : map.get(parentId).keySet()){
	                Address address = new Address();
	                address.setId(Long.parseLong(key));
	                address.setName(map.get(parentId).get(key));
	                address.setpId(Long.parseLong(parentId));
	                address.setSpelling(key+"");
	                
	                gatherSubAddressMap(tmpSubAddressMap, address);

	                tmpAddressMap.put(address.getId(), address);

	                List<Address> addressList = tmpAddressNameMap.get(address.getName());
	                // 如果找不到，则新增
	                if (addressList == null) {
	                    addressList = new ArrayList<Address>();
	                    addressList.add(address);
	                } else {
	                    // 如果找到了，则追加
	                    addressList.add(address);
	                }
	                tmpAddressNameMap.put(address.getName(), addressList);
	            }
    	    }
    	    subAddressLangMap.put(language, tmpSubAddressMap);
    	    addressLangMap.put(language, tmpAddressMap);
    	    addressNameLangMap.put(language, tmpAddressNameMap);
		}
	}

	/**
	 * 加载地址数据 系统为单语言时使用
	 * 
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	private static void load(String data) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, List<String>> map = (Map<String, List<String>>) mapper.readValue(data, Map.class);
			for (String key : map.keySet()) {
				Address address = string2Address(Long.parseLong(key), map.get(key));
				gatherSubAddressMap(subAddressMap, address);
				addressMap.put(address.getId(), address);

				List<Address> idList = addressNameMap.get(address.getName());
				// 如果找不到，则新增
				if (idList == null) {
					idList = new ArrayList<Address>();
					idList.add(address);
					addressNameMap.put(address.getName(), idList);
				}
				// 如果找到了，则追加
				else {
					idList.add(address);
				}
			}
		} catch (JsonParseException e) {
			log.error(e.getMessage());
		} catch (JsonMappingException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 加载地址数据 系统为多语言时使用
	 * 
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	private static void loadI18n(String data, String language) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, List<String>> map = (Map<String, List<String>>) mapper.readValue(data, Map.class);
			Map<Long, List<Address>> tmpSubAddressMap = new HashMap<Long, List<Address>>();
			Map<Long, Address> tmpAddressMap = new HashMap<Long, Address>();
			Map<String, List<Address>> tmpAddressNameMap = new HashMap<String, List<Address>>();
			for (String key : map.keySet()) {
				Address address = string2Address(Long.parseLong(key), map.get(key));
				gatherSubAddressMap(tmpSubAddressMap, address);

				tmpAddressMap.put(address.getId(), address);

				List<Address> addressList = tmpAddressNameMap.get(address.getName());
				// 如果找不到，则新增
				if (addressList == null) {
					addressList = new ArrayList<Address>();
					addressList.add(address);
					tmpAddressNameMap.put(address.getName(), addressList);
				}
				// 如果找到了，则追加
				else {
					addressList.add(address);
				}
			}
			subAddressLangMap.put(language, tmpSubAddressMap);
			addressLangMap.put(language, tmpAddressMap);
			addressNameLangMap.put(language, tmpAddressNameMap);
		} catch (JsonParseException e) {
			log.error(e.getMessage());
		} catch (JsonMappingException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 获取文件内容
	 * 
	 * @param filePath
	 * @return
	 */
	private static String getFileContent(String filePath) {

		BufferedReader br = null;
		InputStream in = null;
		try {

			in = ResourceUtil.getResourceAsStream(filePath);
			if (in == null) {
				log.error("{} file is not find.", filePath);
				throw new RuntimeException(filePath + " file is not find.");
			}
			br = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String tmp = null;
			StringBuilder content = new StringBuilder();
			while ((tmp = br.readLine()) != null) {
				content.append(tmp);
			}

			String json = content.toString();

			return json;

		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
			return null;
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		} finally {
			try {
				if (null != br) {
					br.close();
				}

				if (null != in) {
					in.close();
				}

			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}

	private static Address string2Address(long id, List<String> info) {
		Address address = new Address();
		address.setId(id);
		address.setName(info.get(0));
		address.setpId(Long.parseLong(info.get(1)));
		address.setSpelling(info.get(2));

		return address;
	}

	private static void gatherSubAddressMap(Map<Long, List<Address>> map, Address address) {
		Long pid = address.getpId();
		if (null != map.get(pid)) {
			map.get(pid).add(address);
		} else {
			List<Address> list = new ArrayList<Address>();
			list.add(address);
			map.put(pid, list);
		}
	}

	public static void setI18nOffOn(Boolean i18nOffOn) {
		AddressUtil.i18nOffOn = i18nOffOn;
	}

	public static void setLanguageList(List<String> languageList) {
		AddressUtil.languageList = languageList;
	}

	/**
	 * @return the deliveryModeOn
	 */
	public static Boolean getDeliveryModeOn() {
		return deliveryModeOn;
	}

	/**
	 * @param deliveryModeOn the deliveryModeOn to set
	 */
	public static void setDeliveryModeOn(Boolean deliveryModeOn) {
		AddressUtil.deliveryModeOn = deliveryModeOn;
	}

    public static List<Address> getAllProviences(){
        return getSubAddressByPid(COUNTY_ID);
    }

//	public static void main(String[] args) throws Exception {
//		List<String> langList = new ArrayList<String>();
//		langList.add("zh_CN");
//		langList.add("en_US");
//		generateJsFile("d://adddress.js", false, langList);
//	}
}
