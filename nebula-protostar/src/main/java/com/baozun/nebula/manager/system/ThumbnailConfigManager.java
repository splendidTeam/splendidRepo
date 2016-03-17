package com.baozun.nebula.manager.system;

import java.util.Map;


/**
 * 缩略图参数配置业务接口
 * @author yue.ch
 *
 */
public interface ThumbnailConfigManager {
    
    public enum ThumbnailConfigEnum {
        
        CONFIG_BACKGROUND {
            public String getConfigName(){
                return "background";
            }
            
            public String getScript4Get(){
                return "get_background.sh";
            }
            
            public String getScript4Set(){
                return "modify_background.sh";
            }
        },
        CONFIG_IMAGE_SIZE {
            public String getConfigName(){
                return "image_size";
            }
            
            public String getScript4Get(){
                return "get_image_size.sh";
            }
            
            public String getScript4Set(){
                return "modify_image_size.sh";
            }
        },
        CONFIG_IMAGE_EXT {
            public String getConfigName(){
                return "image_ext";
            }
            
            public String getScript4Get(){
                return "get_image_ext.sh";
            }
            
            public String getScript4Set(){
                return "modify_image_ext.sh";
            }
        };
        
        public abstract String getConfigName();
        public abstract String getScript4Get();
        public abstract String getScript4Set();
    }
    
    /**
     * 获取所有的缩略图相关配置参数值
     * @return
     */
    Map<String, String> getAllConfigValue();
    
    /**
     * 获取配置参数值
     * @param config 配置项
     * @return
     */
    String getConfigValue(ThumbnailConfigEnum config);
    
    /**
     * 修改配置参数值
     * @param config 配置项
     * @param value 配置值
     */
    void updateConfigValue(ThumbnailConfigEnum config, String value);
}
