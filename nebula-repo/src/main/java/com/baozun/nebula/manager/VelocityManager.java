package com.baozun.nebula.manager;

import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;

@Service
public class VelocityManager {
	public static final String	DEFAULT_ENCODING	= "UTF-8";

	protected Log				log					= LogFactory.getLog(getClass());

	private boolean				initFlag			= false;

	private String				pathPrefix			= "";

	
	public VelocityManager(){
		Velocity.setProperty("resource.loader", "class");
		Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		try{
			Velocity.init();
			initFlag = true;
		}catch (Exception e){
			e.printStackTrace();
			log.error("Init Velocity Error");
		}
	}

	public String parseVMContent(String templateContent,Map<String, Object> contextParameters){
		if (!initFlag)
			throw new RuntimeException("Velocity initialize failed");
		if (log.isDebugEnabled()){
			log.debug("Start parsing velocity template");
			log.debug("Template content: " + templateContent);
			log.debug("Parameters: " + contextParameters);
		}
		try{
			VelocityContext context = new VelocityContext();
			for (String key : contextParameters.keySet()){
				context.put(key, contextParameters.get(key));
			}
			StringWriter writer = new StringWriter();
			Velocity.evaluate(context, writer, "jumbovm", templateContent);
			String result = writer.getBuffer().toString();
			if (log.isDebugEnabled()){
				log.debug("Parse result is: ");
				log.debug(result);
			}
			return result;
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("Parse Velocity Template Error");
		}
	}

	public String parseVMTemplate(String templateFileName,Map<String, Object> contextParameters){
		if (!initFlag)
			throw new RuntimeException("Velocity initialize failed");
		if (log.isDebugEnabled()){
			log.debug("Start parsing velocity template");
			log.debug("Template name: " + templateFileName);
			log.debug("Parameters: " + contextParameters);
		}
		try{
			Template template = Velocity.getTemplate(pathPrefix + templateFileName, DEFAULT_ENCODING);
			VelocityContext context = new VelocityContext();
			for (Object o : contextParameters.keySet()){
				String key = (String) o;
				context.put(key, contextParameters.get(key));
			}
			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			String result = writer.getBuffer().toString();
			if (log.isDebugEnabled()){
				log.debug("Parse result is: ");
				log.debug(result);
			}
			return result;
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("Parse Velocity Template Error");
		}
	}

	public String getPathPrefix(){
		return pathPrefix;
	}

	public void setPathPrefix(String pathPrefix){
		this.pathPrefix = pathPrefix;
	}
}
