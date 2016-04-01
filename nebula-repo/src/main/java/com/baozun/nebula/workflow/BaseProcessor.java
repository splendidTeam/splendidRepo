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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.OrderComparator;

/**
 * Base class for all Workflow Processors.  Responsible of keeping track of an ordered collection
 * of {@link Activity Activities}
 * 
 * @since March 1, 2005
 * @see Activity
 * 
 */
public abstract class BaseProcessor implements InitializingBean, BeanNameAware, BeanFactoryAware, Processor {

    protected BeanFactory beanFactory;
    protected String beanName;
    protected List<Activity<ProcessContext<? extends Object>>> activities = new ArrayList<Activity<ProcessContext<? extends Object>>>();
    
    protected ErrorHandler defaultErrorHandler;

    @Value("${workflow.auto.rollback.on.error}")
    private boolean autoRollbackOnError = true;
    
    /**
     * If set to true, this will allow an empty set of activities, thus creating a 'do-nothing' workflow
     */
    protected boolean allowEmptyActivities = false;
    

    /**
     * Sets name of the spring bean in the application context that this
     * processor is configured under
     * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
     */
    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;

    }

    /** Sets the spring bean factroy bean that is responsible for this processor.
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * Whether or not the ActivityStateManager should automatically attempt to rollback any RollbackHandlers registered.
     * If false, rolling back will require an explicit call to ActivityStateManagerImpl.getStateManager().rollbackAllState().
     * The default value is true.
     *
     * @return Whether or not the ActivityStateManager should automatically attempt to rollback
     */
    public boolean getAutoRollbackOnError() {
        return autoRollbackOnError;
    }

    /**
     * Set whether or not the ActivityStateManager should automatically attempt to rollback any RollbackHandlers registered.
     * If false, rolling back will require an explicit call to ActivityStateManagerImpl.getStateManager().rollbackAllState().
     * The default value is true.
     *
     * @param autoRollbackOnError Whether or not the ActivityStateManager should automatically attempt to rollback
     */
    public void setAutoRollbackOnError(boolean autoRollbackOnError) {
        this.autoRollbackOnError = autoRollbackOnError;
    }
    
    /**
     * Defaults to 'false'. This will prevent an exception from being thrown when no activities have been configured
     * for a processor, and thus will create a 'do-nothing' workflow.
     * @return the allowEmptyActivities
     */
    public boolean isAllowEmptyActivities() {
        return allowEmptyActivities;
    }
    
    /**
     * @param allowEmptyActivities the allowEmptyActivities to set
     */
    public void setAllowEmptyActivities(boolean allowEmptyActivities) {
        this.allowEmptyActivities = allowEmptyActivities;
    }

    /**
     * Called after the properties have been set, Ensures the list of activities
     *  is not empty and each activity is supported by this Workflow Processor
     *
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        if (!(beanFactory instanceof ListableBeanFactory)) {
            throw new BeanInitializationException("The workflow processor ["+beanName+"] " +
                    "is not managed by a ListableBeanFactory, please re-deploy using some derivative of ListableBeanFactory such as" +
            "ClassPathXmlApplicationContext ");
        }

        if (CollectionUtils.isEmpty(activities) && !isAllowEmptyActivities()) {
            throw new UnsatisfiedDependencyException(getBeanDesc(), beanName, "activities",
            "No activities were wired for this workflow");
        }
        
        //sort the activities based on their configured order
        OrderComparator.sort(activities);

        HashSet<String> moduleNames = new HashSet<String>();
        for (Iterator<Activity<ProcessContext<? extends Object>>> iter = activities.iterator(); iter.hasNext();) {
            Activity<? extends ProcessContext<? extends Object>> activity = iter.next();
            if ( !supports(activity)) {
                throw new BeanInitializationException("The workflow processor ["+beanName+"] does " +
                        "not support the activity of type"+activity.getClass().getName());
            }
        }
    }

    /**
     * Returns the bean description if the current bean factory allows it.
     * @return spring bean description configure via the spring description tag
     */
    protected String getBeanDesc() {
        return (beanFactory instanceof ConfigurableListableBeanFactory) ?
                ((ConfigurableListableBeanFactory) beanFactory).getBeanDefinition(beanName).getResourceDescription()
                : "Workflow Processor: " + beanName;
    }

    /**
     * Sets the collection of Activities to be executed by the Workflow Process
     * 
     * @param activities ordered collection (List) of activities to be executed by the processor
     */
    @Override
    public void setActivities(List<Activity<ProcessContext<? extends Object>>> activities) {
        this.activities = activities;
    }

    @Override
    public void setDefaultErrorHandler(ErrorHandler defaultErrorHandler) {
        this.defaultErrorHandler = defaultErrorHandler;
    }

    public List<Activity<ProcessContext<? extends Object>>> getActivities() {
        return activities;
    }
    
    public String getBeanName() {
        return beanName;
    }

    public ErrorHandler getDefaultErrorHandler() {
        return defaultErrorHandler;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
