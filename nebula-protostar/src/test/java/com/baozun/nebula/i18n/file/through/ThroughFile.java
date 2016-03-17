/**
 * 
 */
package com.baozun.nebula.i18n.file.through;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.i18n.file.handler.FileHandler;


/**
 * @author xianze.zhang
 *@creattime 2013-7-4
 */
public class ThroughFile{
	private static final Logger	log	= LoggerFactory.getLogger(ThroughFile.class);
	private FileHandler defaultFileHandler;
	/**
	 * 递归遍历文件,并且处理
	 * @param file
	 */
	public void throughFileAndHandle(File file){
		if(file.isFile()){
			handleFile(file);
			return;
		}
		File[] childFiles = file.listFiles();
		for(File childFile:childFiles){
			throughFileAndHandle(childFile);
		}
		
	}
	protected void handleFile(File file){
		log.debug(file.getPath());
		if(checkFile(file)){
			try{
				getFileHandler(file).doHandle(file);
			}catch (Exception e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	protected FileHandler getFileHandler(File file){
		return defaultFileHandler;
	}
	protected boolean checkFile(File file){
		return true;
	}
	protected boolean checkFileByName(String fileName,String regex){
		if(fileName.matches(regex)){
			return true;
		}else{
			return false;
		}
		
	}
	
	public FileHandler getDefaultFileHandler(){
		return defaultFileHandler;
	}
	
	public void setDefaultFileHandler(FileHandler defaultFileHandler){
		this.defaultFileHandler = defaultFileHandler;
	}
	
}
