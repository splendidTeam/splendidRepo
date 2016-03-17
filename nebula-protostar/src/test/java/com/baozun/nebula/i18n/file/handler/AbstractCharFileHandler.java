/**
 * 
 */
package com.baozun.nebula.i18n.file.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 字符类型的文件处理类
 * @author xianze.zhang
 *@creattime 2013-6-26
 */
public abstract class AbstractCharFileHandler implements FileHandler{
	private static final Logger	log	= LoggerFactory.getLogger(AbstractCharFileHandler.class);
	
	public static final String ENTER_LINE = "\r\n";
	protected BufferedReader fileReader;
	private File file;
	
	@Override
	public void doHandle(File file) throws Exception{
		// TODO Auto-generated method stub
		
		init(file);
		execute();
		finish();
	}
	private void init(File file) throws Exception{
		this.setFile(file);
		doInit();
		openFile();
	}
	private void finish() throws Exception{
		doFinish();
		closeFile();
	}
	protected abstract void doInit() throws Exception;
	protected abstract void doFinish() throws Exception;
	
	private void openFile() throws FileNotFoundException{
		fileReader = new BufferedReader(new FileReader(file));
	}
	private void closeFile() throws IOException{
		fileReader.close();
	}
	protected void execute()throws Exception{
		executeByLine();
	}
	
	protected void executeByLine() throws Exception{
		String line = null;
		while ((line = fileReader.readLine()) != null){
			executeLine(line);
		}
	}
	protected abstract void executeLine(String line) throws Exception;
	
	public File getFile(){
		return file;
	}
	
	public void setFile(File file){
		this.file = file;
	}
	
}
