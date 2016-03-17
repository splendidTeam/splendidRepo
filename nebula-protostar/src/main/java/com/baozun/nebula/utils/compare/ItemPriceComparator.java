package com.baozun.nebula.utils.compare;

import java.util.Comparator;

import com.baozun.nebula.model.product.ItemInfo;

public class ItemPriceComparator  implements Comparator<ItemInfo> {

	@Override
	public int compare(ItemInfo o1, ItemInfo o2) {
		if(o1==null || o2==null){
			
		}
		if(o1.getSalePrice().compareTo(o2.getSalePrice())== 1){
			return  1;
		}
		if(o1.getSalePrice().compareTo(o2.getSalePrice())== -1){
			return  -1;
		}
		return 0;
	}

}
