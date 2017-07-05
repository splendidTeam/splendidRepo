package com.baozun.nebula.curator.invoke;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.curator.watcher.IWatcherInvoke;
import com.baozun.nebula.sdk.manager.SdkDeliveryAreaManager;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.library.address.AddressUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.date.DateExtensionUtil.formatDuration;

import loxia.dao.Sort;

/**
 * @see com.baozun.nebula.zk.I18nLangWatchInvoke
 * 
 * @author chengchao
 *
 */
public class I18nLangWatchInvoke implements IWatcherInvoke{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(I18nLangWatchInvoke.class);

    @Autowired
    private SdkI18nLangManager sdkI18nLangManager;

    @Autowired
    private SdkMataInfoManager sdkMataInfoManager;

    @Autowired
    private SdkDeliveryAreaManager deliveryAreaManager;

    @Override
    public void invoke(String path,byte[] data){
        LOGGER.info(path + ":invoke");
        try{
            TimeUnit.SECONDS.sleep(3);
        }catch (InterruptedException e){
            LOGGER.error(e.getMessage());
        }
        sdkI18nLangManager.loadI18nLangs();

        doWithAddress();
    }

    /**
     * 
     * @since 5.3.2.18
     */
    private void doWithAddress(){
        // 设置要加载的地址信息
        AddressUtil.setI18nOffOn(LangProperty.getI18nOnOff());

        List<String> languageList = sdkI18nLangManager.getAllEnabledI18nKeyList();
        AddressUtil.setLanguageList(languageList);

        //---------------------------------------------------------------
        String delivery_mode_on_off = sdkMataInfoManager.findValue("delivery_mode_on_off");
        if (null == delivery_mode_on_off || delivery_mode_on_off.equals("false") || !delivery_mode_on_off.equals("true")){
            AddressUtil.init();
            return;
        }

        //---------------------------------------------------------------
        AddressUtil.setDeliveryModeOn(true);

        String jsPath = getJsPath();

        // 顺序 ,一般先有父 再有 子
        Sort[] sorts = Sort.parse("PARENT_ID asc,sort_no asc");
        for (String language : languageList){
            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("begin load language:[{}] address", language);
            }

            Date beginDate = new Date();

            Map<String, Map<String, String>> map = deliveryAreaManager.findAllDeliveryAreaByLang(language, sorts);
            AddressUtil.initDeliveryArea(map, language, jsPath);

            if (LOGGER.isInfoEnabled()){
                LOGGER.info("load:[{}] use time:[{}]", language, formatDuration(beginDate));
            }
        }
    }

    /**
     * @return
     * @since 5.3.2.18
     */
    private String getJsPath(){
        Properties properties = ProfileConfigUtil.findPro("config/metainfo.properties");
        if (isNotNullOrEmpty(properties) && isNotNullOrEmpty(properties.getProperty("delivery.area.js"))){
            return StringUtils.trim(properties.getProperty("delivery.area.js"));
        }
        return EMPTY;
    }

}
