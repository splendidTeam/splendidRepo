package com.baozun.nebula.search.comparatable;

import java.util.Comparator;

import com.baozun.nebula.search.Facet;

/**
 * facet的排序工具类
 * 
 * @author 冯明雷
 * @version 1.0
 * @time 2016年5月4日 下午6:13:34
 */
public class FacetComparer implements Comparator<Facet>{

	@Override
	public int compare(Facet facet1,Facet facet2){
		int flag = 0 - facet1.getSortNo().compareTo(facet2.getSortNo());
		return flag;
	}
}
