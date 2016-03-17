package com.baozun.nebula.solr.utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
  
public class PinYinUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(PinYinUtil.class); 
	
	/** 
	* 将汉字转换为全拼,输出所有音调的拼音
	*  
	* @param src 
	* @return String 
	*/ 
	public static String getPinYinAll(String str)
	{
		char ch[]=str.toCharArray();
		//设置汉字拼音输出的格式  
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat(); 
		//输出设置，大小写，音标方式等
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		StringBuffer strBu=new StringBuffer();
		String[] pinyinArray=null;
	   try{
			for(int j=0;j<ch.length;j++)
			{
				// 判断能否为汉字字符  //是中文或者a-z或者A-Z转换拼音  
				if (Character.toString(ch[j]).matches("[\\\u4E00-\\\u9FA5]+"))  
				{  
				     pinyinArray =PinyinHelper.toHanyuPinyinStringArray(ch[j]);
				     for(int i=0;pinyinArray!=null&&i<pinyinArray.length;i++){
				    	 strBu.append(pinyinArray[i]).append(" ");
				     }
				     
				}else{
					strBu.append(String.valueOf(ch[j]));
				}
					
				}
			}catch (Exception e)  
			{  
				logger.info("将汉字转换为全拼,输出所有音调的拼音时出错：{}",str,e);
			}  
		return strBu.toString();

	}
	
	
	/** 
	* 将汉字转换为全拼,输出所有音调的拼音
	*  
	* @param src 
	* @return String 
	*/ 
	public static String getPinYinAll2(String str)
	{
		char ch[]=str.toCharArray();
		//设置汉字拼音输出的格式  
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat(); 
		//输出设置，大小写，音标方式等  
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		StringBuffer strBu=new StringBuffer();
		String[] pinyinArray=null;
	   try{
			for(int j=0;j<ch.length;j++)
			{
				// 判断能否为汉字字符  //是中文或者a-z或者A-Z转换拼音  
				if (Character.toString(ch[j]).matches("[\\\u4E00-\\\u9FA5]+"))  
				{  
				     pinyinArray =PinyinHelper.toHanyuPinyinStringArray(ch[j],format);
				     for(int i=0;pinyinArray!=null&&i<pinyinArray.length;i++){
				    	 strBu.append(pinyinArray[i]).append(" ");
				     }
				     
				}else{
					strBu.append(String.valueOf(ch[j]));
				}
					
				}
			}catch (Exception e)  
			{  
				logger.info("将汉字转换为全拼,输出所有音调的拼音时出错：{},{}",str,e);
			}  
		return strBu.toString();

	}
	
	/** 
	* 将汉字转换为全拼 
	*  
	* @param src 
	* @return String 
	*/  
	public static String getPinYin(String src)  
	{  
		char[] ch = src.toCharArray();  
		String[] strArray = new String[ch.length];  
		// 设置汉字拼音输出的格式  
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();  
		//输出设置，大小写，音标方式等  
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
//		format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
		StringBuffer strBu = new StringBuffer();  
		try  
			{  
				for (int i = 0; i < ch.length; i++)  
				{  
					// 判断能否为汉字字符  //是中文或者a-z或者A-Z转换拼音 
					if (Character.toString(ch[i]).matches("[\\\u4E00-\\\u9FA5]+"))  
					{  
					   strArray = PinyinHelper.toHanyuPinyinStringArray(ch[i], format);// 将汉字的几种全拼都存到strArray数组中  
					   strBu.append(strArray[0]);// 取出该汉字全拼的第一种读音并连接到字符串strBu后  
					}  
					else  
					{  
					   // 如果不是汉字字符，间接取出字符并连接到字符串strBu后  
					   strBu.append(Character.toString(ch[i]));  
					}  
		          }  
		     }  
		catch (BadHanyuPinyinOutputFormatCombination e)  
		{   
		   logger.info("将汉字转换为全拼时出错：{}",src,e);
		}  
		return strBu.toString();  
	}   
	  
	/** 
	* 提取每个汉字的首字母 
	*  
	* @param str 
	* @return String 
	*/  
	public static String getPinYinHeadChar(String str)  
	{  
		String convert = "";  
		for (int j = 0; j < str.length(); j++)  
		{  
			char word = str.charAt(j);  
			// 提取汉字的首字母  
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);  
			if (pinyinArray != null)  
			{  
			   convert += pinyinArray[0].charAt(0);
			 }  else  
			 {  
			    convert += word;  
			 }  
		 }  
		return convert;  
	}   
	  
	/** 
	* 将字符串转换成ASCII码 
	*  
	* @param cnStr 
	* @return String 
	*/  
	public static String getCnASCII(String cnStr)  
	{  
		 StringBuffer strBuf = new StringBuffer();  
		// 将字符串转换成字节序列  
		byte[] bGBK = cnStr.getBytes();  
		for (int i = 0; i < bGBK.length; i++)  
		{  
			// 将每个字符转换成ASCII码  
			strBuf.append(Integer.toHexString(bGBK[i] & 0xff));  
		}  
		return strBuf.toString();  
	}   
	  
	
	
	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变，特殊字符丢失 支持多音字，生成方式如（长沙市长:cssc,zssz,zssc,cssz）
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	public static String converterToFirstSpell(String chines) {
		StringBuffer pinyinName = new StringBuffer();
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					// 取得当前汉字的所有全拼
					String[] strs = PinyinHelper.toHanyuPinyinStringArray(
							nameChar[i], defaultFormat);
					if (strs != null) {
						for (int j = 0; j < strs.length; j++) {
							// 取首字母
							pinyinName.append(strs[j].charAt(0));
							if (j != strs.length - 1) {
								pinyinName.append(",");
							}
						}
					}
					// else {
					// pinyinName.append(nameChar[i]);
					// }
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName.append(nameChar[i]);
			}
			pinyinName.append(" ");
		}
		// return pinyinName.toString();
		return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
	}

	/**
	 * 汉字转换位汉语全拼，英文字符不变，特殊字符丢失
	 * 支持多音字，生成方式如（重当参:zhongdangcen,zhongdangcan,chongdangcen
	 * ,chongdangshen,zhongdangshen,chongdangcan）
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	public static String converterToSpell(String chines) {
		StringBuffer pinyinName = new StringBuffer();
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					// 取得当前汉字的所有全拼
					String[] strs = PinyinHelper.toHanyuPinyinStringArray(
							nameChar[i], defaultFormat);
					if (strs != null) {
						for (int j = 0; j < strs.length; j++) {
							pinyinName.append(strs[j]);
							if (j != strs.length - 1) {
								pinyinName.append(",");
							}
						}
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName.append(nameChar[i]);
			}
			pinyinName.append(" ");
		}
		// return pinyinName.toString();
		return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
	}

	/**
	 * 去除多音字重复数据
	 * 
	 * @param theStr
	 * @return
	 */
	private static List<Map<String, Integer>> discountTheChinese(String theStr) {
		// 去除重复拼音后的拼音列表
		List<Map<String, Integer>> mapList = new ArrayList<Map<String, Integer>>();
		// 用于处理每个字的多音字，去掉重复
		Map<String, Integer> onlyOne = null;
		String[] firsts = theStr.split(" ");
		// 读出每个汉字的拼音
		for (String str : firsts) {
			onlyOne = new Hashtable<String, Integer>();
			String[] china = str.split(",");
			// 多音字处理
			for (String s : china) {
				Integer count = onlyOne.get(s);
				if (count == null) {
					onlyOne.put(s, new Integer(1));
				} else {
					onlyOne.remove(s);
					count++;
					onlyOne.put(s, count);
				}
			}
			mapList.add(onlyOne);
		}
		return mapList;
	}


	/**
	 * 解析并组合拼音，对象合并方案(推荐使用)
	 * 
	 * @return
	 */
	private static String parseTheChineseByObject(
			List<Map<String, Integer>> list) {
		Map<String, Integer> first = null; // 用于统计每一次,集合组合数据
		// 遍历每一组集合
		for (int i = 0; i < list.size(); i++) {
			// 每一组集合与上一次组合的Map
			Map<String, Integer> temp = new Hashtable<String, Integer>();
			// 第一次循环，first为空
			if (first != null) {
				// 取出上次组合与此次集合的字符，并保存
				for (String s : first.keySet()) {
					for (String s1 : list.get(i).keySet()) {
						String str = s + s1;
						temp.put(str, 1);
					}
				}
				// 清理上一次组合数据
				if (temp != null && temp.size() > 0) {
					first.clear();
				}
			} else {
				for (String s : list.get(i).keySet()) {
					String str = s;
					temp.put(str, 1);
				}
			}
			// 保存组合数据以便下次循环使用
			if (temp != null && temp.size() > 0) {
				first = temp;
			}
		}
		String returnStr = "";
		if (first != null) {
			// 遍历取出组合字符串
			for (String str : first.keySet()) {
				returnStr += (str + ",");
			}
		}
		if (returnStr.length() > 0) {
			returnStr = returnStr.substring(0, returnStr.length() - 1);
		}
		return returnStr;
	}


	
	public static void main(String[] args)
	{  
		String cnStr = "克比 abc";  
		System.out.println(converterToSpell(cnStr));
		System.out.println(getPinYin(cnStr));
		System.out.println(getPinYinAll2(cnStr));
		System.out.println(getPinYinHeadChar(cnStr));  
		System.out.println(getCnASCII(cnStr));  
		System.out.println(getPinYinAll(cnStr));
		
		
	}  

	
	

}
