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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component("blDefaultErrorHandler")
public class DefaultErrorHandler implements ErrorHandler {

    private static final Log LOG = LogFactory.getLog(DefaultErrorHandler.class);
    @SuppressWarnings("unused")
    private String name;
    
    protected List<String> unloggedExceptionClasses = new ArrayList<String>();

    /* (non-Javadoc)
     * @see org.broadleafcommerce.core.workflow.ErrorHandler#handleError(org.broadleafcommerce.core.workflow.ProcessContext, java.lang.Throwable)
     */
    public void handleError(ProcessContext context, Throwable th) throws WorkflowException {
        context.stopProcess();

        boolean shouldLog = true;
        Throwable cause = th;
        while (true) {
            if (unloggedExceptionClasses.contains(cause.getClass().getName())) {
                shouldLog = false;
                break;
            }
            cause = cause.getCause();
            if (cause == null) {
                break;
            }
        }
        if (shouldLog) {
            LOG.error("An error occurred during the workflow", th);
        }
        
        throw new WorkflowException(th);
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
     */
    public void setBeanName(String name) {
        this.name = name;
    }

    public List<String> getUnloggedExceptionClasses() {
        return unloggedExceptionClasses;
    }

    public void setUnloggedExceptionClasses(List<String> unloggedExceptionClasses) {
        this.unloggedExceptionClasses = unloggedExceptionClasses;
    }
    
}
