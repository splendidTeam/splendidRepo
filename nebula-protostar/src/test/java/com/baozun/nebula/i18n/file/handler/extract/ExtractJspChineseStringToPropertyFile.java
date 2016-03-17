/**
 * 
 */
package com.baozun.nebula.i18n.file.handler.extract;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baozun.nebula.i18n.file.handler.AbstractExtractStringToFile;

/**
 * 抽取jsp中的中文，到一个外部文件
 * @author xianze.zhang
 * @creattime 2013-6-21
 */
public class ExtractJspChineseStringToPropertyFile extends AbstractExtractStringToFile{
	
	/**
	 * jsp中中文的正则表达式，
	 */
	public static final String CHINESE_PATTERN_STRING = "[\u4E00-\u9FA5]+";
	/**
	 * 验证的map，若在map的key中已经有的中文，则不会提取
	 */
	private Map<String,String> checkMap;

	private String headString;

	private Pattern p = Pattern.compile(ExtractJspChineseStringToPropertyFile.CHINESE_PATTERN_STRING);
	
	public ExtractJspChineseStringToPropertyFile(String headString){
		this.headString = headString;
	}

	public ExtractJspChineseStringToPropertyFile(){
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.i18n.AbstractCharFileHandler#executeLine(java.lang.String)
	 */
	@Override
	protected void executeLine(String line) throws Exception{
		// TODO Auto-generated method stub
		Matcher m = p.matcher(line);
		while (m.find()){
			String value = m.group();
			if(checkMap.get(value)==null){
				toFileWrite.write(headString + "=" + value + ENTER_LINE);
				checkMap.put(value, headString);
			}
			line = m.replaceFirst("");
			m = p.matcher(line);
		}
	}

	public Map<String, String> getCheckMap(){
		return checkMap;
	}

	public void setCheckMap(Map<String, String> checkMap){
		this.checkMap = checkMap;
	}

	
	public String getHeadString(){
		return headString;
	}

	
	public void setHeadString(String headString){
		this.headString = headString;
	}

}
