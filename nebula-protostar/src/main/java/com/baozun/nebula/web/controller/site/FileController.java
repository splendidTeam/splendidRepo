package com.baozun.nebula.web.controller.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baozun.nebula.web.controller.BaseController;

@Controller
public class FileController extends BaseController{
	
	@RequestMapping("/site/images.htm")
	public String images(){
		return "/site/images";
	}
	
}
