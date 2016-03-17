package com.baozun.nebula.manager.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baozun.nebula.exception.BusinessException;

/**
 * 缩略图参数配置业务实现类
 * @author yue.ch
 *
 */
@Service("thumbnailConfigManager")
public class ThumbnailConfigManagerImpl implements ThumbnailConfigManager {
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private String osName;
    private String basePath;
    
    @PostConstruct
    public void init(){
        Properties props = System.getProperties();
        osName = props.getProperty("os.name");  
        
        URL url = Thread.currentThread().getContextClassLoader().getResource("thumbnail");
        try {
            basePath = new File(url.toURI()).getCanonicalPath().concat(File.separator);
        } catch (Exception e) {
            logger.error("ERROR: Get the thumbnail configuration file path failed! [{}]", e.getMessage());
            basePath = "";
        } 
    }
    
    /*
     * (non-Javadoc)
     * @see com.baozun.nebula.manager.system.ThumbnailConfigManager#getAllConfigValue()
     */
    @Override
    public Map<String, String> getAllConfigValue(){
        Map<String, String> result = new HashMap<String, String>();
        ThumbnailConfigEnum[] configEnums = ThumbnailConfigEnum.values();
        for(ThumbnailConfigEnum e : configEnums){
            result.put(e.getConfigName(), getConfigValue(e));
        }
        
        return result;
    }
    
    
    /*
     * (non-Javadoc)
     * @see com.baozun.nebula.manager.system.ThumbnailConfigManager#getConfigValue(java.lang.String)
     */
    @Override
    public String getConfigValue(ThumbnailConfigEnum config) {
        String[] command = getCommand4Get(config);
        String[] exec_result;
        
        try {
            exec_result = exec(command);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        
        if(StringUtils.isNotBlank(exec_result[0])){
            logger.error("ERROR: Get thumbnail config value error! [{}]", exec_result[0]);
            throw new BusinessException(7011, new String[]{exec_result[0]});
        } else {
            return exec_result[1];
        }
    }

    /*
     * (non-Javadoc)
     * @see com.baozun.nebula.manager.system.ThumbnailConfigManager#updateConfigValue(java.lang.String, java.lang.String)
     */
    @Override
    public void updateConfigValue(ThumbnailConfigEnum config, String value) {
        String[] command = getCommand4Set(config, value);
        String[] exec_result;
        
        try {
            exec_result = exec(command);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        
        if(StringUtils.isNotBlank(exec_result[0])){
            logger.error("ERROR: Updating thumbnail config error! [{}]", exec_result[0]);
            throw new BusinessException(7012, new String[]{exec_result[0]});
        }
    }
    
    /**
     * 调用shell命令
     * @param command
     * @return 执行结果 - [error_msg, success_msg]
     * @throws IOException 
     * @throws IOException 
     * @throws ExecutionException 
     * @throws InterruptedException 
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    @SuppressWarnings("unchecked")
    private String[] exec(String[] command) throws IOException, InterruptedException, ExecutionException {
        Runtime run = Runtime.getRuntime();
        
        Process proc = null;
        try {
            proc = run.exec(command);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        
        ExecutorService pool = Executors.newFixedThreadPool(2);
        
        Callable c1 = new MyCallable(proc.getErrorStream());
        Callable c2 = new MyCallable(proc.getInputStream());

        Future f1 = pool.submit(c1); 
        Future f2 = pool.submit(c2); 
        
        pool.shutdown(); 
        
        String error_msg = (String)f1.get();
        String success_msg = (String)f2.get();
        
        return new String[]{error_msg, success_msg};
    }

    static class MyCallable implements Callable<String> {
        private InputStream is;

        public MyCallable(InputStream is) {
            this.is = is;
        }

        @Override
        public String call() throws IOException {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(is));
                String line = System.getProperty("line.separator");
                String s;
                while ((s = br.readLine()) != null) {
                    sb.append(s).append(line);
                }
            } catch (IOException e) {
                throw e;
            } finally {
                if(br != null){
                    br.close();
                }
            }
            
            return sb.toString();
        }
    }
    
    private String getScriptBasePath() throws IOException, URISyntaxException{
        URL url = Thread.currentThread().getContextClassLoader().getResource("thumbnail");
        String path = new File(url.toURI()).getCanonicalPath();
        return path.endsWith(File.separator) ? path : path.concat("/");
    }
    
    private String[] getCommand4Get(ThumbnailConfigEnum config){
        if("Linux".equals(osName)){
            return new String[]{"/bin/bash", "-c", basePath.concat(config.getScript4Get())};
        } 
//        else if (osName.startsWith("Windows")){
//            return new String[]{"cmd.exe", "/c", getScriptBasePath().concat("get_").concat(config).concat(".bat")};
//        } 
        else {
            throw new BusinessException(7010, new String[]{osName});
        }
    }
    
    private String[] getCommand4Set(ThumbnailConfigEnum config, String value){
        if("Linux".equals(osName)){
            return new String[]{"/bin/bash", "-c", basePath.concat(config.getScript4Set()).concat(" \"").concat(value).concat("\"")};
        } 
//        else if (osName.startsWith("Windows")){
//            return new String[]{"cmd.exe", "/c", getScriptBasePath().concat("modify_").concat(config).concat(".bat"), "\"".concat(value).concat("\"")};
//        } 
        else {
            throw new BusinessException(7010, new String[]{osName});
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, URISyntaxException{
        Runtime run = Runtime.getRuntime();
        
        Process proc = null;
        try {
            proc = run.exec(new String[]{"cmd", "/c", "c:/test.bat"});
        } catch (IOException e) {
            throw e;
        }
        
        ExecutorService pool = Executors.newFixedThreadPool(2);
        
        Callable c1 = new MyCallable(proc.getErrorStream());
        Callable c2 = new MyCallable(proc.getInputStream());

        Future f1 = pool.submit(c1); 
        Future f2 = pool.submit(c2); 
        
        String error_msg = (String)f1.get();
        String success_msg = (String)f2.get();
        
        pool.shutdown(); 
        
        System.out.println(error_msg);
        System.out.println(success_msg);
    }
}


