package com.baozun.nebula.curator.invoke;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.curator.watcher.IWatcherInvoke;
import com.baozun.nebula.sdk.manager.SdkLimitManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionManager;

import static com.feilong.core.date.DateExtensionUtil.formatDuration;

/**
 * 引擎相关的Watch,在invoke方法中用于调用引擎初始化方法
 * 
 * @see com.baozun.nebula.zk.EngineWatchInvoke
 * 
 * @author chengchao
 *
 */
public class EngineWatchInvoke implements IWatcherInvoke{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineWatchInvoke.class);

    @Autowired
    private SdkPromotionManager sdkPromotionManager;

    @Autowired
    private SdkLimitManager sdkLimitManager;

    //---------------------------------------------------------------------

    @Override
    public void invoke(String path,byte[] data){
        LOGGER.info(path + ":invoke");
        try{
            TimeUnit.SECONDS.sleep(3);
        }catch (InterruptedException e){
            LOGGER.error(e.getMessage());
        }

        Date beginDate = new Date();

        //促销规则
        sdkPromotionManager.publishPromotion(beginDate);

        if (LOGGER.isInfoEnabled()){
            LOGGER.info("sdkPromotionManager publishPromotion use time: [{}]", formatDuration(beginDate));
        }

        //---------------------------------------------------------------------

        beginDate = new Date();

        //限购规则
        sdkLimitManager.publishLimit(beginDate);

        if (LOGGER.isInfoEnabled()){
            LOGGER.info("sdkLimitManager publishLimit use time: [{}]", formatDuration(beginDate));
        }

    }

}
