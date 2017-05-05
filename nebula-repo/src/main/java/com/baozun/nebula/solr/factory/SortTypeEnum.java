package com.baozun.nebula.solr.factory;

import com.baozun.nebula.solr.utils.SolrOrderSort;
import com.feilong.core.lang.EnumUtil;

/**
 * solr的排序枚举，商城可以重写
 * @author 冯明雷
 * @version 1.0
 * @time 2016年4月22日  下午5:38:04
 * @author zl.shi
 * @since zl.shi: 2017.5.5
 */
public enum SortTypeEnum{

	/** 销售量 倒序. */
	SALESCOUNT_DESC("orders_desc"){

		public SolrOrderSort[] getSolrOrderSort(){
			return new SolrOrderSort[] { new SolrOrderSort("salesCount", SolrOrderSort.DESC) };
		}
	},
	/** 销售量 升序. */
	SALESCOUNT_ASC("orders_asc"){
		
		public SolrOrderSort[] getSolrOrderSort(){
			return new SolrOrderSort[] { new SolrOrderSort("salesCount", SolrOrderSort.ASC) };
		}
	},

	/** 价格从低到高. */
	SALESPRICE_ASC("salesprice_asc"){

		public SolrOrderSort[] getSolrOrderSort(){
			return new SolrOrderSort[] {new SolrOrderSort("sale_price", SolrOrderSort.ASC) };
		}
	},
	/** 价格从高到低. */
	SALESPRICE_DESC("salesprice_desc"){

		public SolrOrderSort[] getSolrOrderSort(){
			return new SolrOrderSort[] {new SolrOrderSort("sale_price", SolrOrderSort.DESC) };
		}
	},
	/** 最近上架时间升序. */
	ONSHELVESTIME_ASC("newest_asc"){

		public SolrOrderSort[] getSolrOrderSort(){
			return new SolrOrderSort[] {new SolrOrderSort("activeBeginTime", SolrOrderSort.ASC) };
		}
	},
	
	/** 最近上架时间倒序. */
	ONSHELVESTIME_DESC("newest_desc"){

		public SolrOrderSort[] getSolrOrderSort(){
			return new SolrOrderSort[] {new SolrOrderSort("activeBeginTime", SolrOrderSort.DESC) };
		}
	},
	
	/** 
	 * 默认排序 店铺权重，品类系数
	 * @since Nebula5.3.2.14
	 */
    DEFAULT_ORDER("default_order"){

        public SolrOrderSort[] getSolrOrderSort(){
            return new SolrOrderSort[] {new SolrOrderSort("default_sort", SolrOrderSort.DESC) };
        }
    },
    
    /**
     * 折扣升序
     * @since Nebula5.3.2.14
     */
    DISCOUNT_ASC("dynamic_sort_discount_asc"){

        public SolrOrderSort[] getSolrOrderSort(){
            return new SolrOrderSort[] {new SolrOrderSort("dynamic_sort_discount", SolrOrderSort.ASC) };
        }
    },
    
    /**
     * 折扣降序
     * @since Nebula5.3.2.14
     */
    DISCOUNT_DESC("dynamic_sort_discount_desc"){

        public SolrOrderSort[] getSolrOrderSort(){
            return new SolrOrderSort[] {new SolrOrderSort("dynamic_sort_discount", SolrOrderSort.DESC) };
        }
    },
    /**
     * 手机专项价格降序
     * @since Nebula5.3.2.14
     */
    MOBILEPRICE_DESC("dynamic_sort__mobile_exclusive_price_desc"){

        public SolrOrderSort[] getSolrOrderSort(){
            return new SolrOrderSort[] {new SolrOrderSort("dynamic_sort__mobile_exclusive_price", SolrOrderSort.DESC) };
        }
    },
    
    /**
     * 手机专项价格升序
     * @since Nebula5.3.2.14
     */
    MOBILEPRICE_ASC("dynamic_sort__mobile_exclusive_price_asc"){

        public SolrOrderSort[] getSolrOrderSort(){
            return new SolrOrderSort[] {new SolrOrderSort("dynamic_sort__mobile_exclusive_price", SolrOrderSort.ASC) };
        }
    }
    ;

	/**
	 * Instantiates a new sort type enum.
	 * 
	 * @param order
	 *            the order
	 */
	private SortTypeEnum(String order){
		this.order = order;
	}

	/** 顺序. */
	private String	order;

	/**
	 * Gets the 顺序.
	 * 
	 * @return the order
	 */
	public String getOrder(){
		return order;
	}

	/**
	 * Sets the 顺序.
	 * 
	 * @param order
	 *            the order to set
	 */
	public void setOrder(String order){
		this.order = order;
	}

	/**
	 * Gets the sort.
	 * 
	 * @param normalChannelId
	 *            the normal channel id
	 * @return the sort
	 */
	public abstract SolrOrderSort[] getSolrOrderSort();

	/**
	 * Gets the single instance of SortTypeEnum.
	 * 
	 * @param order
	 *            the order
	 * @return single instance of SortTypeEnum
	 */
	public static SortTypeEnum getInstance(String order){
		SortTypeEnum sortTypeEnum=EnumUtil.getEnumByPropertyValue(SortTypeEnum.class, "order", order);
		if(sortTypeEnum==null){
			return DEFAULT_ORDER;
		}
		return sortTypeEnum;
	}
	
	
	
}
