package com.baozun.nebula.sdk.manager;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;

/**
 * 
 *
 * @author feilong
 * @version 5.3.1 2016年5月13日 下午3:04:33
 * @since 5.3.1
 */
public interface SdkConsigneeManager extends BaseManager{

    /**
     * 保存收货人信息.
     * 
     * @param orderId
     * @param shopId
     * @param salesOrderCommand
     */
    void saveConsignee(Long orderId,Long shopId,SalesOrderCommand salesOrderCommand);

}
