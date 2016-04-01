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

import org.springframework.core.Ordered;

import com.baozun.nebula.workflow.state.RollbackHandler;

public abstract class BaseActivity<T extends ProcessContext<?>> implements Activity<T> {
    
    protected ErrorHandler errorHandler;
    protected String beanName;

    protected RollbackHandler<T> rollbackHandler;
    protected String rollbackRegion;
    protected Map<String, Object> stateConfiguration;
    protected boolean automaticallyRegisterRollbackHandler = false;
    protected int order = Ordered.LOWEST_PRECEDENCE;
    
    @Override
    public boolean shouldExecute(T context) {
        return true;
    }
    
    @Override
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    @Override
    public void setBeanName(final String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void setErrorHandler(final ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public RollbackHandler<T> getRollbackHandler() {
        return rollbackHandler;
    }

    @Override
    public void setRollbackHandler(RollbackHandler<T> rollbackHandler) {
        this.rollbackHandler = rollbackHandler;
    }

    @Override
    public String getRollbackRegion() {
        return rollbackRegion;
    }

    @Override
    public void setRollbackRegion(String rollbackRegion) {
        this.rollbackRegion = rollbackRegion;
    }

    @Override
    public Map<String, Object> getStateConfiguration() {
        return stateConfiguration;
    }

    @Override
    public void setStateConfiguration(Map<String, Object> stateConfiguration) {
        this.stateConfiguration = stateConfiguration;
    }

    @Override
    public boolean getAutomaticallyRegisterRollbackHandler() {
        return automaticallyRegisterRollbackHandler;
    }

    @Override
    public void setAutomaticallyRegisterRollbackHandler(boolean automaticallyRegisterRollbackHandler) {
        this.automaticallyRegisterRollbackHandler = automaticallyRegisterRollbackHandler;
    }
    
    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
