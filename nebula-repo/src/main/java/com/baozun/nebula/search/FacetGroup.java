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


import java.util.List;

/**
 * Facet Group.
 * Represents a set of facets (e.g. brand, color, size etc).
 *
 * @author nic
 */
public interface FacetGroup {

    /**
     * Get unique identifier for this facet group
     * @return id
     */
    String getId();

    /**
     * Get title of the facet group. Should be localized to current language.
     * @return
     */
    String getTitle();

    /**
     * Get type of facet group.
     * @return type
     */
    String getType(); 

    /**
     * Get all belonging facets
     * @return facets
     */
    List<Facet> getFacets();

    /**
     * Indicate if current facet group represents a product category or not.
     * @return is category
     */
    boolean isCategory();
}