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


/**
 * <p>
 * Marker interface that all modules should use when adding activities to workflows. This is used for logging to
 * the user on startup that a module has modified a particular workflow and the final ordering of the configured workflow.
 * This logging is necessary for users that might be unaware of all of the activities that different modules could be
 * injecting into their workflows, since it's possible they they might want to change the ordering of their particular
 * activities as well.</p>
 * 
 * <p>When writing a module activity, the ordering should be configured in the 100 range (3100, 3200, etc) so that users
 * who use your module can configure custom activities in-between framework <b>and</b> module activities.</p>
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public interface ModuleActivity {

    /**
     * The name of the module that this activity came from (for instance: 'inventory')
     * @return
     */
    public String getModuleName();

}
