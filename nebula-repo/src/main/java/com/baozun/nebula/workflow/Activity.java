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

import java.util.Map;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.core.Ordered;

import com.baozun.nebula.workflow.state.RollbackHandler;

/**
 * <p>
 * Interface to be used for workflows. Usually implementations will subclass {@link BaseActivity}.
 * </p>
 * 
 * Important note: if you are writing a 3rd-party integration module or adding a module outside of the core, your
 * activity should implement the {@link ModuleActivity} interface as well. This ensures that there is proper logging
 * for users that are using your module so that they know exactly what their final workflow configuration looks like.
 *
 * @author Phillip Verheyden (phillipuniverse)
 * @param <T>
 * @see {@link BaseActivity}
 * @see {@link ModuleActivity}
 * @see {@link BaseProcessor}
 * @see {@link SequenceProcessor}
 */
public interface Activity<T extends ProcessContext<?>> extends BeanNameAware, Ordered {

    /**
     * Called by the encompassing processor to activate
     * the execution of the Activity
     * 
     * @param context - process context for this workflow
     * @return resulting process context
     * @throws Exception
     */
    public T execute(T context) throws Exception;

    /**
     * Determines if an activity should execute based on the current values in the {@link ProcessContext}. For example, a
     * context might have both an {@link Order} as well as a String 'status' of what the order should be changed to. It is
     * possible that an activity in a workflow could only deal with a particular status change, and thus could return false
     * from this method.
     * 
     * @param context
     * @return
     */
    public boolean shouldExecute(T context);

    /**
     * Get the fine-grained error handler wired up for this Activity
     * @return
     */
    public ErrorHandler getErrorHandler();

    public void setErrorHandler(final ErrorHandler errorHandler);
    
    public String getBeanName();

    /**
     * Retrieve the RollbackHandler instance that should be called by the ActivityStateManager in the
     * event of a workflow execution problem. This RollbackHandler will presumably perform some
     * compensating operation to revert state for the activity.
     *
     * @return the handler responsible for reverting state for the activity
     */
    public RollbackHandler<T> getRollbackHandler();

    /**
     * Set the RollbackHandler instance that should be called by the ActivityStateManager in the
     * event of a workflow execution problem. This RollbackHandler will presumably perform some
     * compensating operation to revert state for the activity.
     *
     * @param rollbackHandler the handler responsible for reverting state for the activity
     */
    public void setRollbackHandler(RollbackHandler<T> rollbackHandler);

    /**
     * Retrieve the optional region label for the RollbackHandler. Setting a region allows
     * partitioning of groups of RollbackHandlers for more fine grained control of rollback behavior.
     * Explicit calls to the ActivityStateManager API in an ErrorHandler instance allows explicit rollback
     * of specific rollback handler regions. Note, to disable automatic rollback behavior and enable explicit
     * rollbacks via the API, the workflow.auto.rollback.on.error property should be set to false in your implementation's
     * runtime property configuration.
     *
     * @return the rollback region label for the RollbackHandler instance
     */
    public String getRollbackRegion();

    /**
     * Set the optional region label for the RollbackHandler. Setting a region allows
     * partitioning of groups of RollbackHandlers for more fine grained control of rollback behavior.
     * Explicit calls to the ActivityStateManager API in an ErrorHandler instance allows explicit rollback
     * of specific rollback handler regions. Note, to disable automatic rollback behavior and enable explicit
     * rollbacks via the API, the workflow.auto.rollback.on.error property should be set to false in your implementation's
     * runtime property configuration.
     *
     * @param rollbackRegion the rollback region label for the RollbackHandler instance
     */
    public void setRollbackRegion(String rollbackRegion);

    /**
     * Retrieve any user-defined state that should accompany the RollbackHandler. This configuration will be passed to
     * the RollbackHandler implementation at runtime.
     *
     * @return any user-defined state configuratio necessary for the execution of the RollbackHandler
     */
    public Map<String, Object> getStateConfiguration();

    /**
     * Set any user-defined state that should accompany the RollbackHandler. This configuration will be passed to
     * the RollbackHandler implementation at runtime.
     *
     * @param stateConfiguration any user-defined state configuration necessary for the execution of the RollbackHandler
     */
    public void setStateConfiguration(Map<String, Object> stateConfiguration);

    /**
     * Whether or not this activity should automatically register a configured RollbackHandler with the ActivityStateManager.
     * It is useful to adjust this value if you plan on using the ActivityStateManager API to register RollbackHandlers
     * explicitly in your code. The default value is false.
     *
     * @return Whether or not to automatically register a RollbackHandler with the ActivityStateManager
     */
    public boolean getAutomaticallyRegisterRollbackHandler();

    /**
     * Whether or not this activity should automatically register a configured RollbackHandler with the ActivityStateManager.
     * It is useful to adjust this value if you plan on using the ActivityStateManager API to register RollbackHandlers
     * explicitly in your code. The default value is false.
     *
     * @param automaticallyRegisterRollbackHandler Whether or not to automatically register a RollbackHandler with the ActivityStateManager
     */
    public void setAutomaticallyRegisterRollbackHandler(boolean automaticallyRegisterRollbackHandler);
}
