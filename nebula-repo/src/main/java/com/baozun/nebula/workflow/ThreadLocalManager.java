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
*/
package com.baozun.nebula.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author D.C
 *
 */
public class ThreadLocalManager {

    private static final Log LOG = LogFactory.getLog(ThreadLocalManager.class);

    private static final ThreadLocal<ThreadLocalManager> THREAD_LOCAL_MANAGER = new ThreadLocal<ThreadLocalManager>() {
        @Override
        protected ThreadLocalManager initialValue() {
            ThreadLocalManager manager = new ThreadLocalManager();
            String checkOrphans = System.getProperty("ThreadLocalManager.notify.orphans");
            if ("true".equals(checkOrphans)) {
                manager.marker = new RuntimeException("Thread Local Manager is not empty - the following is the culprit call that setup the thread local but did not clear it.");
            }
            return manager;
        }
    };

    protected Map<Long, ThreadLocal> threadLocals = new LinkedHashMap<Long, ThreadLocal>();
    protected RuntimeException marker = null;

    public static void addThreadLocal(ThreadLocal threadLocal) {
        Long position;
        synchronized (threadLock) {
            count++;
            position = count;
        }
        THREAD_LOCAL_MANAGER.get().threadLocals.put(position, threadLocal);
    }

    public static <T> ThreadLocal<T> createThreadLocal(final Class<T> type) {
        return createThreadLocal(type, true);
    }

    public static <T> ThreadLocal<T> createThreadLocal(final Class<T> type, final boolean createInitialValue) {
        ThreadLocal<T> response = new ThreadLocal<T>() {
            @Override
            protected T initialValue() {
                addThreadLocal(this);
                if (!createInitialValue) {
                    return null;
                }
                try {
                    return type.newInstance();
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void set(T value) {
                super.get();
                super.set(value);
            }
        };
        return response;
    }

    public static void remove() {
        for (Map.Entry<Long, ThreadLocal> entry : THREAD_LOCAL_MANAGER.get().threadLocals.entrySet()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Removing ThreadLocal #" + entry.getKey() + " from request thread.");
            }
            entry.getValue().remove();
        }
        THREAD_LOCAL_MANAGER.get().threadLocals.clear();
        THREAD_LOCAL_MANAGER.remove();
    }

    public static void remove(ThreadLocal threadLocal) {
        Long removePosition = null;
        for (Map.Entry<Long, ThreadLocal> entry : THREAD_LOCAL_MANAGER.get().threadLocals.entrySet()) {
            if (entry.getValue().equals(threadLocal)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Removing ThreadLocal #" + entry.getKey() + " from request thread.");
                }
                entry.getValue().remove();
                removePosition = entry.getKey();
            }
        }
        THREAD_LOCAL_MANAGER.get().threadLocals.remove(removePosition);
    }

    private static Long count = 0L;
    private static final Object threadLock = new Object();

    @Override
    public String toString() {
        if (!threadLocals.isEmpty() && marker != null) {
            marker.printStackTrace();
        }
        return super.toString();
    }
}
