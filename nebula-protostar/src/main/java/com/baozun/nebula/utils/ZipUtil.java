/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Jumbo.
 * 
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 * 
 */
package com.baozun.nebula.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 * 
 *         压缩/解压工具类
 */
public class ZipUtil {

	protected static final Logger log = LoggerFactory.getLogger(ZipUtil.class);

	/**
	 * 默认编码为utf-8
	 */
	private static String DFT_CHARSET = "utf-8";

	/**
	 * 使用默认编码压缩
	 * 
	 * @throws IOException
	 */
	public static byte[] gzipString(String source) throws IOException {
		return gzipString(source, DFT_CHARSET);
	}

	/**
	 * GZIP压缩
	 * 
	 * @param source 压缩前字符串
	 * @param charset 字符串编码
	 * @return 压缩后的字节码
	 * @throws IOException
	 */
	public static byte[] gzipString(String source, String charset) throws IOException {
		byte[] result = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(source.getBytes(charset));
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		GZIPOutputStream gzout = new GZIPOutputStream(bout);
		byte[] buf = new byte[1024];// 设定读入缓冲区尺寸
		int num;
		while ((num = bin.read(buf)) != -1) {
			gzout.write(buf, 0, num);
		}
		gzout.flush();
		gzout.close();
		result = bout.toByteArray();
		bout.close();
		bin.close();

		return result;
	}

	/**
	 * 使用默认编码解压GZIP
	 * 
	 * @throws IOException
	 */
	public static String ungzipBytes(byte[] bytes) throws IOException {
		return unzipBytes(new GZIPInputStream(new ByteArrayInputStream(bytes)), DFT_CHARSET);
	}

	/**
	 * ZIP解压
	 * 
	 * @param bytes 压缩后的字节码
	 * @param charset 字节码解压后生成字符串所需的编码（保持与压缩前字符串编码相同，否则可能乱码）
	 * @return 用指定编码生成的解压字符串
	 * @throws IOException
	 */
	private static String unzipBytes(InflaterInputStream bin, String charset) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		unzipBytes(bin, bout);

		return bout.toString(charset);
	}

	/**
	 * 传统ZIP解压(文件-文件)
	 */
	public static void unzipBytes(File in, File out) throws IOException {
		unzipBytes(new FileInputStream(in), out);
	}

	/**
	 * 传统ZIP解压(字节-文件)
	 */
	public static void unzipBytes(InputStream fin, File out) throws IOException {
		unzipBytes(new ZipInputStream(fin), new FileOutputStream(out));
	}

	public static int unzipFiles(InputStream fin, File out, Pattern fileReg) throws IOException {
		ZipInputStream zis = new ZipInputStream(fin);
		ZipEntry ze;
		int unzipCount = 0;
		
		if (!out.exists() && !out.mkdirs()) 
			throw new IOException("创建解压目录 \"" + out.getAbsolutePath() + "\" 失败");
		
		while ((ze = zis.getNextEntry()) != null) {
			try{
				if (ze.isDirectory() == false && (fileReg == null || (fileReg != null && fileReg.matcher(ze.getName()).matches()))){
					File child = new File(out, ze.getName());
					child.getParentFile().mkdirs();
					FileOutputStream output = new FileOutputStream(child);
					byte[] buffer = new byte[10240];
					int bytesRead = 0;
					while ((bytesRead = zis.read(buffer)) > 0) 
						output.write(buffer, 0, bytesRead);
					
					output.flush();
					output.close();
					unzipCount++;
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			
		}
		zis.close();
		
		return unzipCount;
	}

	/**
	 * 解压(流-流)
	 */
	public static void unzipBytes(InflaterInputStream bin, OutputStream bout) throws IOException {
		byte[] buf = new byte[1024];// 设定读入缓冲区尺寸
		int read = 0;
		while ((read = bin.read(buf)) != -1)
			bout.write(buf, 0, read);

		bout.close();
		bin.close();
	}
}
