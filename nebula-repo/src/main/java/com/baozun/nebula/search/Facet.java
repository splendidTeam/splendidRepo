/**
 * Copyright (c) 2016 Baozun All Rights Reserved.
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
package com.baozun.nebula.search;


/**
 * Facet
 *
 * @author nic
 */
public interface Facet {

    /**
     * Get facet title. Should be localized to current language.
     * @return
     */
    String getTitle();

    /**
     * Get URL to activate this facet on current result set.
     * @return url
     */
    String getUrl();

    /**
     * Get number of products in current result set.
     * @return count
     */
    int getCount();

    /**
     * Indicate if the facet has been selected (for multi-select).
     * @return selected
     */
    boolean isSelected();
    
    String	getValue();
}
