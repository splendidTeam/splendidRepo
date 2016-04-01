/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.workflow.state;

import java.util.Map;

import com.baozun.nebula.workflow.Activity;
import com.baozun.nebula.workflow.ProcessContext;
import com.baozun.nebula.workflow.WorkflowException;

/**
 * This exception is thrown to indicate a problem while trying to rollback
 * state for any and all activities during a failed workflow. Only those
 * activities that register their state with the ProcessContext will have
 * their state rolled back.
 *
 * @author Jeff Fischer
 */
public class RollbackFailureException extends WorkflowException {

    private static final long serialVersionUID = 1L;

    private Activity<? extends ProcessContext<?>> activity;
    private ProcessContext<?> processContext;
    private Map<String, Object> stateItems;

    public RollbackFailureException() {
    }

    public RollbackFailureException(Throwable cause) {
        super(cause);
    }

    public RollbackFailureException(String message) {
        super(message);
    }

    public RollbackFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public Activity<? extends ProcessContext<?>> getActivity() {
        return activity;
    }

    public void setActivity(Activity<? extends ProcessContext<?>> activity) {
        this.activity = activity;
    }

    public ProcessContext<?> getProcessContext() {
        return processContext;
    }

    public void setProcessContext(ProcessContext<?> processContext) {
        this.processContext = processContext;
    }

    public Map<String, Object> getStateItems() {
        return stateItems;
    }

    public void setStateItems(Map<String, Object> stateItems) {
        this.stateItems = stateItems;
    }
}
