/**
 * 
 */
package com.baozun.nebula.utils.convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * 流转化工具类
 * 
 * @author xianze.zhang
 * @creattime 2013-6-7
 */
public class InputStreamConvertUtil{

	public static final String	DEFAULT_ENCODING	= "utf-8";

	/**
	 * 根据指定默认编码方式，将流转化为字符串
	 * 
	 * @param in
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String getStringFromStream(InputStream in,String encoding) {
//		StringBuilder data = new StringBuilder();
		// int i = -1;
		// while ((i=in.read()) != -1){
		// data.append((char)i);
		// }
		ByteArrayOutputStream   baos   =   new   ByteArrayOutputStream(); 
		try{
	        int   i=-1; 
	        while((i=in.read())!=-1){ 
	        	baos.write(i); 
	        }
	        return   baos.toString(InputStreamConvertUtil.DEFAULT_ENCODING);
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			in.close();
			baos.close();
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       return null;
//		try{
//			int i = -1;
//			byte[] b = new byte[1024];
//			while ((i = in.read(b)) != -1){
//				data.append(new String(b, 0, i, encoding));
//			}
//			in.close();
//		}catch (IOException e){
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return data.toString();
	}

	/**
	 * 将一个字符串转化为输入流
	 */
	public static InputStream getStreamFromString(String inputString,String encoding){
		if (inputString != null && !inputString.trim().equals("")){
			try{
				ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(inputString.getBytes(encoding));
				return tInputStringStream;
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 采用默认编码方式，将字符串转化为流
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static InputStream getStreamFromString(String inputString){
		return getStreamFromString(inputString, DEFAULT_ENCODING);
	}
	/**
	 * 采用默认编码方式，将流转化为字符串
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String getStringFromStream(InputStream in){
		return getStringFromStream(in, DEFAULT_ENCODING);
	}

}
