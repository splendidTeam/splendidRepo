package com.baozun.nebula.utilities;

import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.utilities.common.CollectionUtil;

public class CollectionUtilTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int layer = 0;
		
		List<List<Long>> dimvalue = new ArrayList<List<Long>> (); 
		
		List<List<Long>> result = new ArrayList<List<Long>> (); 
		
		List<Long> curList = new ArrayList<Long>();
		
        List<Long> v1 = new ArrayList<Long>();
        v1.add(1L);
        //v1.add(2L);
        List<Long> v2 = new ArrayList<Long>();
        v2.add(21L);
        v2.add(22L);
        List<Long> v3 = new ArrayList<Long>();
        v3.add(31L);
        v3.add(32L);
        
        dimvalue.add(v1);
        dimvalue.add(v2);
        dimvalue.add(v3);
        
        CollectionUtil.descartesT(dimvalue, result,(int) layer, curList);
        
        int i = 1;
        for (List<Long> list : result)
        {
            System.out.println(i++ + ":" + list.toString());
        }
	}

}
