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

import com.baozun.nebula.workflow.ThreadLocalManager;

/**
 * Handles the identification of the outermost workflow and the current thread so that the StateManager can
 * operate on the appropriate RollbackHandlers.
 *
 * @author Jeff Fischer
 */
public class RollbackStateLocal {

    private static final ThreadLocal<RollbackStateLocal> THREAD_LOCAL = ThreadLocalManager.createThreadLocal(RollbackStateLocal.class, false);

    public static RollbackStateLocal getRollbackStateLocal() {
        return THREAD_LOCAL.get();
    }

    public static void setRollbackStateLocal(RollbackStateLocal rollbackStateLocal) {
        THREAD_LOCAL.set(rollbackStateLocal);
    }

    private String threadId;
    private String workflowId;

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }
}
