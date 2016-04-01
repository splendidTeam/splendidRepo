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

import java.io.Serializable;

public interface ProcessContext<T> extends Serializable {

    /**
     * Activly informs the workflow process to stop processing
     * no further activities will be executed
     *
     * @return whether or not the stop process call was successful
     */
    public boolean stopProcess();

    /**
     * Is the process stopped
     *
     * @return whether or not the process is stopped
     */
    public boolean isStopped();

    /**
     * Provide seed information to this ProcessContext, usually
     * provided at time of workflow kickoff by the containing
     * workflow processor.
     * 
     * @param seedObject - initial seed data for the workflow
     */
    public void setSeedData(T seedObject);

    /**
     * Returns the seed information
     * @return
     */
    public T getSeedData();

}
