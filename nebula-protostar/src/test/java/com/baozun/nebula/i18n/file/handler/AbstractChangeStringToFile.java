/**
 * 
 */
package com.baozun.nebula.i18n.file.handler;

import java.io.FileWriter;
import java.util.Map;

/**
 * @author xianze.zhang
 * @creattime 2013-6-21
 */
public abstract class AbstractChangeStringToFile extends AbstractCharFileHandler{

	protected FileWriter			resultFileWriter;

	protected String				basePath;

	protected String				resultPath;

	protected Map<String, String> changeMap;
	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.i18n.AbstractCharFileHandler#doInit()
	 */
	@Override
	protected void doInit() throws Exception{
		String path = resultPath + this.getFile().getPath().replace('\\', '/').replaceFirst(basePath, "");
		resultFileWriter = new FileWriter(path);
	}

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.i18n.AbstractCharFileHandler#doFinish()
	 */
	@Override
	protected void doFinish() throws Exception{
		// TODO Auto-generated method stub
		resultFileWriter.close();
	}

	public String getResultPath(){
		return resultPath;
	}

	
	public void setResultPath(String resultPath){
		this.resultPath = resultPath;
	}

	
	public FileWriter getResultFileWriter(){
		return resultFileWriter;
	}

	
	public void setResultFileWriter(FileWriter resultFileWriter){
		this.resultFileWriter = resultFileWriter;
	}

	
	public String getBasePath(){
		return basePath;
	}

	
	public void setBasePath(String basePath){
		this.basePath = basePath;
	}

	
	public Map<String, String> getChangeMap(){
		return changeMap;
	}

	
	public void setChangeMap(Map<String, String> changeMap){
		this.changeMap = changeMap;
	}

}
