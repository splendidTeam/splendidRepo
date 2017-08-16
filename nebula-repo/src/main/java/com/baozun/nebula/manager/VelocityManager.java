package com.baozun.nebula.manager;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.feilong.tools.jsonlib.JsonUtil;

import static com.feilong.core.util.SortUtil.sortMapByKeyAsc;

@Service
public class VelocityManager{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(VelocityManager.class);

    public static final String DEFAULT_ENCODING = "UTF-8";

    private boolean initFlag = false;

    private String pathPrefix = "";

    public VelocityManager(){
        Velocity.setProperty("resource.loader", "class");
        Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try{
            Velocity.init();
            initFlag = true;
        }catch (Exception e){
            LOGGER.error("Init Velocity Error", e);
        }
    }

    public String parseVMContent(String templateContent,Map<String, Object> contextParameters){
        if (!initFlag)
            throw new RuntimeException("Velocity initialize failed");

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("Start parsing velocity template");
            LOGGER.debug("Template content: {},Parameters: {}", templateContent, JsonUtil.format(sortMapByKeyAsc(contextParameters)));
        }

        //---------------------------------------------------------------------

        try{
            VelocityContext context = new VelocityContext();
            for (String key : contextParameters.keySet()){
                context.put(key, contextParameters.get(key));
            }
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "jumbovm", templateContent);
            String result = writer.getBuffer().toString();

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("Parse result is: {}", result);
            }
            return result;
        }catch (Exception e){
            throw new RuntimeException("Parse Velocity Template Error", e);
        }
    }

    public String parseVMTemplate(String templateFileName,Map<String, Object> contextParameters){
        if (!initFlag)
            throw new RuntimeException("Velocity initialize failed");

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("Start parsing velocity template");
            LOGGER.debug("Template name: {},Parameters: {}", templateFileName, JsonUtil.format(sortMapByKeyAsc(contextParameters)));
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

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("Parse result is: {}", result);
            }
            return result;
        }catch (Exception e){
            throw new RuntimeException("Parse Velocity Template Error", e);
        }
    }

    public String getPathPrefix(){
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix){
        this.pathPrefix = pathPrefix;
    }
}
