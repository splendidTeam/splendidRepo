package com.baozun.nebula.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;

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
	 * 获取文件的扩展名
	 * @param fileName
	 * @return
	 */
	public static String queryFileExt(String fileName){
		
		int index=fileName.lastIndexOf(".");
		if(index==-1)
			return "";
		
		return fileName.substring(index+1);
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
	

	public static boolean deletefile(String delpath) throws Exception {  
		  try {  
		  
		   File file = new File(delpath);  
		   // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true  
		   if (!file.isDirectory()) {  
		    file.delete();  
		   } else if (file.isDirectory()) {  
		    String[] filelist = file.list();  
		    for (int i = 0; i < filelist.length; i++) {  
		     File delfile = new File(delpath + "/" + filelist[i]);  
		     if (!delfile.isDirectory()) {  
		      delfile.delete();  
	
		     } else if (delfile.isDirectory()) {  
		      deletefile(delpath + "/" + filelist[i]);  
		     }  
		    }  
		   
		    file.delete();  
		   }  
		  
		  } catch (FileNotFoundException e) {  
		   System.out.println("deletefile() Exception:" + e.getMessage());  
		  }  
		  return true;  
		 }  
	
	
	/**
	 * 下载EXCEL
	 * @param response
	 * @param path
	 * @throws IOException
	 */
	public static void downloadExcel(HttpServletResponse response, String path) {
		File file = new File(Thread.currentThread().getContextClassLoader().getResource(path).getPath());

		response.setHeader("Content-type", "application/force-download");
		response.setHeader("Content-Transfer-Encoding", "Binary");
		response.setHeader("Content-length", "" + file.length());
		response.setHeader("Content-Type", "application/octet-stream");
		response.setHeader("Content-Disposition", "inline;filename=\"" + path + "\"");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
		
		try {
			OutputStream os = null;
			InputStream is = null;
			try {
				is = new FileInputStream(file);
				os = response.getOutputStream();
				byte[] buf = new byte[1024];
				while (is.read(buf) != -1) {
					os.write(buf);
				}
			} finally {
				is.close();
				os.close();
			}
		} catch (Exception e) {
			throw new BusinessException(ErrorCodes.FILE_READ_FAIL);
		}
	}

}
