package com.baozun.nebula.utils.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

/**
 * 文件相关
 * @author Justin Hu
 *
 */
public class FileUtils {
	
	/**
	 * 第一个字符是否为斜杠，true为斜杠
	 * @param source
	 * @return
	 */
	public static boolean isSlashFirst(String source){
		
		if(source.charAt(0)=='/')
			return true;
		
		else
			
			return false;
	}
	
	/**
	 * 清掉第一个斜杠 '/'
	 * @param source
	 * @return
	 */
	public static String clearFirstSlash(String source){
		
		if(StringUtils.isBlank(source))
			return null;
		
		if(isSlashFirst(source)){	//判断第一个字符是否为'/',是则清除这个斜杠
			
			return source.substring(1);
			
		}
		
		return source;
	}
	
	/**
	 * 加上第一个斜杠 '/'，前提是没有斜杠
	 * @param source
	 * @return
	 */
	public static String addFirstSlash(String source){
		
		if(StringUtils.isBlank(source))
			return null;
		
		if(!isSlashFirst(source)){	//判断第一个字符是否为'/',是则清除这个斜杠
			
			return "/"+source;
			
		}
		
		return source;
	}

	/**
	 * 写文件
	 * @param inputStream
	 * @param wirtePath
	 * @throws Exception
	 */
	public static void writeFile(InputStream inputStream,String wirtePath,String fileName)throws Exception{
	
		int bytesum = 0;
        int byteread = 0;
		
		FileOutputStream fs=null;
		File writePathFile=new File(wirtePath);
		writePathFile.mkdirs();
		
		File file=new File(wirtePath+addFirstSlash(fileName));
		if(file.exists()){	//如果文件已存在则删除
			file.delete();
		}
		try {
		fs = new FileOutputStream(file);
		 byte[] buffer = new byte[1204];
         while ((byteread = inputStream.read(buffer)) != -1) {
             bytesum += byteread;
            
             fs.write(buffer, 0, byteread);
         }
        // System.out.println("size:"+bytesum);
		}
		catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		finally{
			fs.close();
		}
	}
	

	

}
