package com.baozun.nebula.utils.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 递归读取文件
 * @author Justin Hu
 *
 */
public class RecursionReadFile {

	private int fileLevel; 
	
	private List<File> fileList=new ArrayList<File>();
	
	/**
	 * 文件类型,如jar,class等
	 */
	private String fileType=null;
	
	public RecursionReadFile(String dirPath){
		
		readFile(dirPath);
	}
	
	public RecursionReadFile(String dirPath,String fileType){
		this.fileType=fileType;
		readFile(dirPath);
	}
	
	
	public void readFile(String dirPath) {  
        // 建立当前目录中文件的File对象  
        File file = new File(dirPath);  
        // 取得代表目录中所有文件的File对象数组  
        File[] list = file.listFiles();  
        // 遍历file数组  
        for (int i = 0; i < list.length; i++) {  
                if (list[i].isDirectory()) {  
                        fileLevel ++;  
                        // 递归子目录  
                        readFile(list[i].getPath());  
                        fileLevel --;  
                } else {  
                        
                		//使用后辍进行过滤
                        if(fileType!=null&&list[i].getName().endsWith("."+fileType))
                        	fileList.add(list[i]);
                }  
        }  
	}

	public List<File> getFileList() {
		return fileList;
	}
	
	
}
