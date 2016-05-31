package com.baozun.nebula.sdk.manager;

import java.math.BigDecimal;

import com.baozun.nebula.manager.BaseManager;

/**
 * 
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月13日 下午4:56:05
 * @since 5.3.1
 */
public interface SdkPayCodeManager extends BaseManager{

    void savaPayCode(String subOrdinate,BigDecimal payMoney);
}
