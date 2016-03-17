package com.baozun.nebula.utilities.common.convertor;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;


public class MapAndStringConvertor {

	/**
	 * 用于将map转换成字符串
	 * For Alipay
	 * @param params
	 * @return
	 */
	public static String getToBeSignedString(Map<String, String> params){
		List<String> keys = new ArrayList<String>(params.keySet());

		// key 排序
		Collections.sort(keys);

		StringBuilder builder = new StringBuilder();
		int size = keys.size();
		for (int i = 0; i < size; ++i){
			String key = keys.get(i);
			String value = params.get(key);

			builder.append(key + "=" + value);
			// 最后一个不要拼接 &
			if (i != size - 1){
				builder.append("&");
			}
		}
		return builder.toString();
	}
	
	/**
	 * 解析支付宝返回的xml信息.
	 * 
	 * @param alipayResult
	 *            the alipay result
	 * @return the map
	 * @throws DocumentException
	 *             the document exception
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public static Map<String, String> convertResultToMap(String alipayResult)
			throws DocumentException {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		Document document = reader.read(new InputSource(new StringReader(
				alipayResult)));
		Element root = document.getRootElement();
		Iterator<Element> iterator = root.elementIterator();
		while (iterator.hasNext()) {
			 Element e = (Element) iterator.next();
			 List<Element> elementList = new ArrayList<Element>();
			 elementList = e.elements();
			 if(elementList.size()>0){
				 for(Element iter:elementList){
					 map.putAll(DomToMap(iter));
				 }
			 }else{
				 map.put(e.getName(), e.getTextTrim());
			 }
		}
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String,String> DomToMap(Element e){
		Map<String,String> resultMap = new HashMap<String,String>();
		Boolean isEndNode = true;
		while(isEndNode){
			List<Element> eList = e.elements();
			if(eList.size()>0){
				for(Element el:eList){
					resultMap.putAll(DomToMap(el));
				}
				isEndNode = false;
			}else{
				resultMap.put(e.getName(), e.getText());
				isEndNode = false;
			}
		}
		return resultMap;
	}
}
