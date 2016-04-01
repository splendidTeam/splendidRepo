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
 * Convenience class for creating an empty workflow. Useful when a user wants to remove workflow behavior from Broadleaf.
 * For instance, a user might want to subclass {@link OrderService} and provide their own implementation of addItem, but
 * wants to invoke the super implementation of this method to obtain all functionality <i>except</i> executing the workflow
 * since they want to take charge of the entire process themselves.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public class EmptySequenceProcessor extends SequenceProcessor {

    @Override
    protected ProcessContext createContext(Object seedData) {
        return null;
    }

}
