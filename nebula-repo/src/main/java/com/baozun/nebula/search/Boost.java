package com.baozun.nebula.search;

/**
 * solr权重打分相关参数
 * @author jumbo
 *
 */
public class Boost {

	private String 	bq;
	
	/**qf对默认查询增加权重比值*/
	private String	qf;
	
	/**pf查询字段*/
	private String	pf;
	
	/**bf用函数计算某个字段的权重*/
	private String	bf;
	
	/**指定query parser,默认 为 edismax*/
	private String	deftype = DeftypeEnum.edismax.toString();

	public String getBq() {
		return bq;
	}

	public String getQf() {
		return qf;
	}

	public String getPf() {
		return pf;
	}

	public String getBf() {
		return bf;
	}

	public String getDeftype() {
		return deftype;
	}

	public void setBq(String bq) {
		this.bq = bq;
	}

	public void setQf(String qf) {
		this.qf = qf;
	}

	public void setPf(String pf) {
		this.pf = pf;
	}

	public void setBf(String bf) {
		this.bf = bf;
	}

	public void setDeftype(String deftype) {
		this.deftype = deftype;
	}
	
    public enum DeftypeEnum {
    	edismax,dismax,lucene
    }
}
