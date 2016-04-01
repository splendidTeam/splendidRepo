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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.baozun.nebula.workflow.Activity;
import com.baozun.nebula.workflow.ProcessContext;

@Service
public class ActivityStateManagerImpl implements ActivityStateManager {

    private static ActivityStateManager ACTIVITY_STATE_MANAGER;

    public static ActivityStateManager getStateManager() {
        return ACTIVITY_STATE_MANAGER;
    }

    protected Map<String, Stack<StateContainer>> stateMap = Collections.synchronizedMap(new HashMap<String, Stack<StateContainer>>());

    @PostConstruct
    public void init() {
        ACTIVITY_STATE_MANAGER = this;
    }

    @Override
    public void clearAllState() {
        RollbackStateLocal rollbackStateLocal = getRollbackStateLocal();
        stateMap.remove(rollbackStateLocal.getThreadId() + "_" + rollbackStateLocal.getWorkflowId());
    }

    @Override
    public void clearRegionState(String region) {
        RollbackStateLocal rollbackStateLocal = getRollbackStateLocal();
        Stack<StateContainer> containers = stateMap.get(rollbackStateLocal.getThreadId() + "_" + rollbackStateLocal.getWorkflowId());
        if (containers != null) {
            while (!containers.empty()) {
                String myRegion = containers.pop().getRegion();
                if ((region == null && myRegion == null) || (region != null && region.equals(myRegion))) {
                    break;
                }
            }
        }
    }

    @Override
    public void registerState(RollbackHandler rollbackHandler, Map<String, Object> stateItems) {
        registerState(null, null, null, rollbackHandler, stateItems);
    }

    @Override
    public void registerState(Activity<? extends ProcessContext> activity, ProcessContext processContext, RollbackHandler rollbackHandler, Map<String, Object> stateItems) {
        registerState(activity, processContext, null, rollbackHandler, stateItems);
    }

    @Override
    public void registerState(Activity<? extends ProcessContext> activity, ProcessContext processContext, String region, RollbackHandler rollbackHandler, Map<String, Object> stateItems) {
        RollbackStateLocal rollbackStateLocal = getRollbackStateLocal();
        Stack<StateContainer> containers = stateMap.get(rollbackStateLocal.getThreadId() + "_" + rollbackStateLocal.getWorkflowId());
        if (containers == null) {
            containers = new Stack<StateContainer>();
            stateMap.put(rollbackStateLocal.getThreadId() + "_" + rollbackStateLocal.getWorkflowId(), containers);
        }

        StateContainer stateContainer = new StateContainer();
        stateContainer.setRollbackHandler(rollbackHandler);
        stateContainer.setStateItems(stateItems);
        stateContainer.setRegion(region);
        stateContainer.setActivity(activity);
        stateContainer.setProcessContext(processContext);

        containers.push(stateContainer);
    }

    @Override
    public void rollbackAllState() throws RollbackFailureException {
        RollbackStateLocal rollbackStateLocal = getRollbackStateLocal();
        Stack<StateContainer> containers = stateMap.get(rollbackStateLocal.getThreadId() + "_" + rollbackStateLocal.getWorkflowId());
        if (containers != null) {
            while (!containers.empty()) {
                StateContainer stateContainer = containers.pop();
                stateContainer.getRollbackHandler().rollbackState(stateContainer.getActivity(), stateContainer.getProcessContext(), stateContainer.getStateItems());
            }
        }
    }

    @Override
    public void rollbackRegionState(String region) throws RollbackFailureException {
        RollbackStateLocal rollbackStateLocal = getRollbackStateLocal();
        Stack<StateContainer> containers = stateMap.get(rollbackStateLocal.getThreadId() + "_" + rollbackStateLocal.getWorkflowId());
        if (containers != null) {
            while (!containers.empty()) {
                StateContainer stateContainer = containers.pop();
                if ((region == null && stateContainer.getRegion() == null) || (region != null && region.equals(stateContainer.getRegion()))) {
                    stateContainer.getRollbackHandler().rollbackState(stateContainer.getActivity(), stateContainer.getProcessContext(), stateContainer.getStateItems());
                }
            }
        }
    }

    protected RollbackStateLocal getRollbackStateLocal() {
        RollbackStateLocal rollbackStateLocal = RollbackStateLocal.getRollbackStateLocal();
        if (rollbackStateLocal == null) {
            throw new IllegalThreadStateException("Unable to perform ActivityStateManager operation, as the RollbackStateLocal instance is not set on the current thread! ActivityStateManager methods may not be called outside the scope of workflow execution.");
        }
        return rollbackStateLocal;
    }

    private class StateContainer {

        private String region;
        private RollbackHandler rollbackHandler;
        private Map<String, Object> stateItems;
        private Activity<? extends ProcessContext> activity;
        private ProcessContext processContext;

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public RollbackHandler getRollbackHandler() {
            return rollbackHandler;
        }

        public void setRollbackHandler(RollbackHandler rollbackHandler) {
            this.rollbackHandler = rollbackHandler;
        }

        public Map<String, Object> getStateItems() {
            return stateItems;
        }

        public void setStateItems(Map<String, Object> stateItems) {
            this.stateItems = stateItems;
        }

        public Activity<? extends ProcessContext> getActivity() {
            return activity;
        }

        public void setActivity(Activity<? extends ProcessContext> activity) {
            this.activity = activity;
        }

        public ProcessContext getProcessContext() {
            return processContext;
        }

        public void setProcessContext(ProcessContext processContext) {
            this.processContext = processContext;
        }
    }
}
