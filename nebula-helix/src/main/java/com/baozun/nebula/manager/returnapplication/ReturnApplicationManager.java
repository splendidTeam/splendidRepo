/**
 * 
 */
package com.baozun.nebula.manager.returnapplication;

import java.util.List;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.web.controller.order.viewcommand.ReturnApplicationViewCommand;

/**
 * @author yaohua.wang@baozun.cn
 *
 */
public interface ReturnApplicationManager extends BaseManager{

    /**
     * 通过ReturnApplicationCommand集合获得returnApplicationViewCommand集合对象
     * 
     * @param returnApplications
     * @return
     */
    public List<ReturnApplicationViewCommand> findReturnApplicationViewCommand(List<ReturnApplicationCommand> returnApplications);
}
