package com.baozun.nebula.model.i18n;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.baozun.nebula.model.BaseModel;

@Entity
@Table(name = "T_I18N_LANG")
public class I18nLang extends BaseModel {
	private static final long	serialVersionUID	= 3985567625209179127L;
	
	/** 唯一标识pk */
	private Long				id;
	/** 语言标识 */
	private String				key;
	/** 语言名称 */
	private String				value;
	/** 分词器类型 */
	private String				tokenizer;
	/** 是否默认语言 1默认 0 不是 */
	private Integer				defaultlang;
	/** 语言顺序 */
	private Integer				sort;
	/** 是否启用或禁用 */
	private Integer				lifecycle;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_I18N_LANG", sequenceName = "S_T_I18N_LANG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_I18N_LANG")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "KEY")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "VALUE")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "TOKENIZER")
	public String getTokenizer() {
		return tokenizer;
	}

	public void setTokenizer(String tokenizer) {
		this.tokenizer = tokenizer;
	}

	@Column(name = "DEFAULT_LANG")
    @Index(name = "IDX_I18N_LANG_DEFAULT_LANG")
	public Integer getDefaultlang() {
		return defaultlang;
	}

	public void setDefaultlang(Integer defaultlang) {
		this.defaultlang = defaultlang;
	}

	@Column(name = "SORT")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Column(name = "LIFECYCLE")
    @Index(name = "IDX_I18N_LANG_LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

}
