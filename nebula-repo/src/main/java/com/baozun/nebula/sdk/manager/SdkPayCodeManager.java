package com.baozun.nebula.sdk.manager;

import java.math.BigDecimal;

import com.baozun.nebula.manager.BaseManager;

/**
 * 
 *
 * @author feilong
 * @version 5.3.1 2016年5月13日 下午4:56:05
 * @since 5.3.1
 */
public interface SdkPayCodeManager extends BaseManager{

    void savaPayCode(BigDecimal payMoney,String subOrdinate);
}
