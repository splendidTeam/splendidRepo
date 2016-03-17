package com.baozun.nebula.web.controller.cms;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baozun.nebula.sdk.manager.SdkCmsModuleInstanceManager;
import com.baozun.nebula.web.controller.BaseController;

@Controller
public class ModuleController extends BaseController {

	private final Logger	log	= LoggerFactory.getLogger(ModuleController.class);
	@Autowired
	private SdkCmsModuleInstanceManager sdkCmsModuleInstanceManager;
	
	@RequestMapping(value = "/cms/module/{code}.htm")
	public String geModuleData(@PathVariable String code , HttpServletResponse response) {
		String data = sdkCmsModuleInstanceManager.getModuleMap().get(code);
		if(data == null || data.equals("")){
			response.setStatus(404);
		}else{
			try {
				PrintWriter pw	 = response.getWriter();
				pw.write(data);
			} catch (IOException e) {
				log.error("geModuleData", e);
			}
		}
		return null;
	}
}
