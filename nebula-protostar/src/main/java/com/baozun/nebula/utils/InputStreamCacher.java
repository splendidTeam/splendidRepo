package com.baozun.nebula.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * @author lxy
 * 类说明 
 */
public class InputStreamCacher {
	private static final Logger	log						= LoggerFactory.getLogger(InputStreamCacher.class);
	/**
	 * 将InputStream中的字节保存到ByteArrayOutputStream中。
	 */
	private ByteArrayOutputStream byteArrayOutputStream = null;
	
	public InputStreamCacher(InputStream inputStream) {
		byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];  
		int len;  
		try {
			while ((len = inputStream.read(buffer)) > -1 ) {  
				byteArrayOutputStream.write(buffer, 0, len);  
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}finally{
			try {
				byteArrayOutputStream.flush();
				byteArrayOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public InputStream getInputStream() {
		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}
}
