package com.baozun.nebula.command;

import java.io.InputStream;

/**
 * 邮件附件
 * @author Justin Hu
 *
 */
public class EmailAttachmentCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5751192834939939301L;
	
	/**
	 * 文件名称
	 */
	private String name;
	
	/**
	 * 文件路径
	 */
	private String filePath;
		

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	
	

}
