package com.baozun.nebula.tools.svnkit;

public class TagsInfo {

	private String pomPath;
	private String srcUrl;
	private String distUrl;

	private String revision;
	
	private String  msg;

	public String getPomPath() {
		return pomPath;
	}

	public void setPomPath(String pomPath) {
		this.pomPath = pomPath;
	}

	public String getSrcUrl() {
		return srcUrl;
	}

	public void setSrcUrl(String srcUrl) {
		this.srcUrl = srcUrl;
	}

	public String getDistUrl() {
		return distUrl;
	}

	public void setDistUrl(String distUrl) {
		this.distUrl = distUrl;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	
}
