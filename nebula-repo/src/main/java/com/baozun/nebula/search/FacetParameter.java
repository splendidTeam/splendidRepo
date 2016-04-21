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


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Facet Parameter.
 * Is used for representing a user facing facet.
 *
 * @author nic
 */
public class FacetParameter {

    // TODO: Optimize the implementation to minimize the parsing of the facet values

    /**
     * Parameter type
     */
    public enum ParameterType {
        SINGLEVALUE, 	//单选
        MULTISELECT,	//多选
        RANGE,			//范围
        LESS_THAN,		//小于
        GREATER_THAN	//大于
    }

    static private Pattern RANGE_PATTERN = Pattern.compile("([0-9]+[\\.0-9]*)\\-([0-9]+[\\.0-9]*)");
    static private Pattern LESS_THAN_PATTERN = Pattern.compile("<([0-9]+[\\.0-9]*)");
    static private Pattern GREATER_THAN_PATTERN = Pattern.compile(">([0-9]+[\\.0-9]*)");

    private String name;
    private List<String> values = new ArrayList<String>();
    private ParameterType type;
    private FacetType facetType;

    /**
     * Create new facet parameter
     * @param name
     */
    public FacetParameter(String name) {
        this.name = name;
    }

    /**
     * Create a new facet. The value is parsed to extract current type and values.
     * @param name
     * @param strValue
     */
    public FacetParameter(String name, String strValue) {
        this.name = name;

        Matcher rangeMatcher = RANGE_PATTERN.matcher(strValue);
        Matcher lessThanMatcher = LESS_THAN_PATTERN.matcher(strValue);
        Matcher greaterThanMatcher = GREATER_THAN_PATTERN.matcher(strValue);

        if (name.endsWith("_val") ) {
            this.name = this.name.replace("_val", "");
            this.type = ParameterType.SINGLEVALUE;
            this.values.add(strValue);
        }
        else if ( rangeMatcher.matches()) {
            this.type = ParameterType.RANGE;
            String min = rangeMatcher.group(1);
            String max = rangeMatcher.group(2);
            this.values.add(min);
            this.values.add(max);
        }
        else if ( lessThanMatcher.matches() ) {
            this.type = ParameterType.LESS_THAN;
            String value = lessThanMatcher.group(1);
            this.values.add(value);
        }
        else if ( greaterThanMatcher.matches() ) {
            this.type = ParameterType.GREATER_THAN;
            String value = greaterThanMatcher.group(1);
            this.values.add(value);
        }
        else  {
            // Multi-select
            this.type = ParameterType.MULTISELECT;
            StringTokenizer tokenizer = new StringTokenizer(strValue, "|");
            while (tokenizer.hasMoreTokens()) {
                values.add(tokenizer.nextToken());
            }
        }
    }

    /**
     * Name of the facet parameter. Is shown in the facet URL.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get all facet values.
     * @return facet values
     */
    public List<String> getValues() {
        return values;
    }

    /**
     * Check if specific value is included in this facet parameter (when having multi select facets).
     *
     * @param facetValue
     * @return contains value
     */
    public boolean containsValue(String facetValue) {
        if ( this.type == ParameterType.RANGE ) {
            Matcher rangeMatcher = RANGE_PATTERN.matcher(facetValue);
            if (rangeMatcher.matches() && this.values.size() == 2) {
                String min = rangeMatcher.group(1);
                String max = rangeMatcher.group(2);
                return (this.values.get(0).equals(min) && this.values.get(1).equals(max));
            }
            return false;
        }
        else {
            return this.values.contains(facetValue);
        }
    }

    /**
     * Get parameter type.
     * @return type
     */
    public ParameterType getType() {
        return type;
    }

    /**
     * Convert the facet parameter to URL format.
     * @return url fragment
     */
    public String toUrl() {
        return this.addValueToUrl(null);
    }

    /**
     * Convert the facet parameter to URL format.
     *
     * @param additionalValue
     * @return url fragment
     */
    public String addValueToUrl(String additionalValue) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        if ( type == ParameterType.SINGLEVALUE ) {
            sb.append("_val");
        }
        sb.append("=");
        if ( type == ParameterType.LESS_THAN ) {
            sb.append("<");
        }
        else if ( type == ParameterType.GREATER_THAN ) {
            sb.append(">");
        }
        String delimiter = "";
        if (type == ParameterType.MULTISELECT) {
            delimiter = "|";
        } else if (type == ParameterType.RANGE) {
            delimiter = "-";
        }

        for ( int i=0; i < this.values.size(); i++ ) {
            sb.append(this.values.get(i));
            if ( i < this.values.size()-1 ) {
                sb.append(delimiter);
            }
        }
        if ( additionalValue != null && type == ParameterType.MULTISELECT ) {
            sb.append(delimiter);
            sb.append(additionalValue);
        }
        return sb.toString();
    }

    /**
     * Remove a value from a facet URL fragment (used for breadcrumbs and de-selecting multi-value facets).
     * @param removedValue
     * @return url fragment
     */
    public String removeValueToUrl(String removedValue) {
        if ( ( this.values.size() == 1 && this.values.get(0).equals(removedValue) ) ||
                ( type == ParameterType.RANGE && this.values.size() == 2 ) ) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        if ( type == ParameterType.SINGLEVALUE ) {
            sb.append("_val");
        }
        sb.append("=");
        if ( type == ParameterType.LESS_THAN ) {
            sb.append("<");
        }
        else if ( type == ParameterType.GREATER_THAN ) {
            sb.append(">");
        }
        String delimiter = "";
        if (type == ParameterType.MULTISELECT) {
            delimiter = "|";
        } else if (type == ParameterType.RANGE) {
            delimiter = "-";
        }
        List<String> values = new ArrayList<String>();
        for ( String value : this.values ) {
            if ( type != ParameterType.MULTISELECT ||
                    (type == ParameterType.MULTISELECT && !value.equals(removedValue)) ) {
                values.add(value);
            }
        }
        for ( int i=0; i < values.size(); i++ ) {
            String value = values.get(i);
            sb.append(value);
            if ( i < values.size()-1 ) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

	public FacetType getFacetType() {
		return facetType;
	}

	public void setFacetType(FacetType facetType) {
		this.facetType = facetType;
	}
	
}
