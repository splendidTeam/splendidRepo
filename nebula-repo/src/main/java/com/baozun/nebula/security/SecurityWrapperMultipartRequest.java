/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.security;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

/**
 * @author D.C
 * @date 2015年10月8日 下午8:46:33
 */
public class SecurityWrapperMultipartRequest extends SecurityWrapperRequest implements MultipartHttpServletRequest {

	public SecurityWrapperMultipartRequest(HttpServletRequest request) {
		super(request);
	}
	
	private MultipartHttpServletRequest getMultipartHttpServletRequest() {
		return WebUtils.getNativeRequest(this.getRequest(), MultipartHttpServletRequest.class);
		
	}
	public Iterator<String> getFileNames() {
		return this.getMultipartHttpServletRequest().getFileNames();
	}

	public MultipartFile getFile(String name) {
		return this.getMultipartHttpServletRequest().getFile(name);
	}

	public List<MultipartFile> getFiles(String name) {
		return this.getMultipartHttpServletRequest().getFiles(name);
	}

	public Map<String, MultipartFile> getFileMap() {
		return this.getMultipartHttpServletRequest().getFileMap();
	}

	public MultiValueMap<String, MultipartFile> getMultiFileMap() {
		return this.getMultipartHttpServletRequest().getMultiFileMap();
	}

	public String getMultipartContentType(String paramOrFileName) {
		return this.getMultipartHttpServletRequest().getMultipartContentType(paramOrFileName);
	}

	public HttpMethod getRequestMethod() {
		return this.getMultipartHttpServletRequest().getRequestMethod();
	}

	public HttpHeaders getRequestHeaders() {
		return this.getMultipartHttpServletRequest().getRequestHeaders();
	}

	public HttpHeaders getMultipartHeaders(String paramOrFileName) {
		return this.getMultipartHttpServletRequest().getMultipartHeaders(paramOrFileName);
	}
}
