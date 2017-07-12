/**
 * 
 */
package com.baozun.nebula.manager.returnapplication;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.web.controller.order.viewcommand.ReturnLineViewCommand;

/**
 * @author yaohua.wang@baozun.cn
 *
 */
public interface ReturnApplicationLineManager extends BaseManager{

    public List<ReturnLineViewCommand> findReturnLineViewCommandByLineIds(List<Long> orderLineIds);
}
