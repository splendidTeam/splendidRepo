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
package com.baozun.nebula.workflow;

import java.util.List;


public interface Processor {

    public boolean supports(Activity<? extends ProcessContext<?>> activity);
    
    public ProcessContext<?> doActivities() throws WorkflowException;
    
    public ProcessContext<?> doActivities(Object seedData) throws WorkflowException;
    
    public void setActivities(List<Activity<ProcessContext<?>>> activities);
    
    public void setDefaultErrorHandler(ErrorHandler defaultErrorHandler);
    
    public void setProcessContextFactory(ProcessContextFactory<Object, Object> processContextFactory);

}
