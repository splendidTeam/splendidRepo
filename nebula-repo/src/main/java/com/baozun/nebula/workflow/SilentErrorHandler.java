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

import org.springframework.stereotype.Component;

@Component("nbSilentErrorHandler")
public class SilentErrorHandler implements ErrorHandler {

    @SuppressWarnings("unused")
    private String name;

    /* (non-Javadoc)
     * @see org.broadleafcommerce.core.workflow.ErrorHandler#handleError(org.broadleafcommerce.core.workflow.ProcessContext, java.lang.Throwable)
     */
    public void handleError(ProcessContext context, Throwable th) throws WorkflowException {
        context.stopProcess();
        throw new WorkflowException(th);
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
     */
    public void setBeanName(String name) {
        this.name = name;
    }

}
