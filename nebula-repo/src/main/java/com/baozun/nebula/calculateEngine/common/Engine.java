/**
 * 
 */
package com.baozun.nebula.calculateEngine.common;

import java.util.List;


/**
 * @author jun.lu
 *@creattime 2014-03-13
 */
public interface Engine{
	public <T> void buildEngine(List<T> scopeList);
}
