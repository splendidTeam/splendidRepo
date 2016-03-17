/**
 * 
 */
package com.baozun.nebula.i18n.file.handler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baozun.nebula.i18n.file.handler.AbstractCharFileHandler;

/**
 * @author xianze.zhang
 * @creattime 2013-6-21
 */
public abstract class AbstractExtractStringToFile extends AbstractCharFileHandler{


	protected FileWriter			toFileWrite;

	protected String				toFile;
	
	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.i18n.AbstractCharFileHandler#doInit()
	 */
	@Override
	protected void doInit() throws Exception{
		// TODO Auto-generated method stub
		toFileWrite = new FileWriter(toFile, true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.i18n.AbstractCharFileHandler#doFinish()
	 */
	@Override
	protected void doFinish() throws Exception{
		// TODO Auto-generated method stub
		toFileWrite.close();
	}


	
	public String getToFile(){
		return toFile;
	}

	
	public void setToFile(String toFile){
		this.toFile = toFile;
	}

}
