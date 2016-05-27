package com.baozun.nebula.command.i18n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.model.i18n.I18nLang;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.utils.Validator;
import com.baozun.nebula.utils.spring.SpringUtil;

public class MutlLang extends LangProperty {

	private static final long serialVersionUID = -4877220898281372352L;
	
	//private static Integer i18nSize;

	private static List<String> i18nLangs;

	private String[] values;

	private String[] langs;
	
	private Map<String, Object> langValues;

	private String defaultValue;
	
	private static String defaultLang;

	public String[] getValues() {
		check();
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
		setDefaultValue(getDefaultValue());
	}

	public String[] getLangs() {
		check();
		return langs;
	}

	public void setLangs(String[] langs) {
		this.langs = langs;
		setDefaultValue(getDefaultValue());
	}

	public Map<String, Object> getLangValues() {
		if(this.langValues == null) {
			initLangValues();
		}
		return langValues;
	}
	
	private void initLangValues() {
		check();
		if(this.langValues == null) {
			this.langValues = new HashMap<String, Object>();
			if (this.values != null) {
				for (int i = 0; i < this.values.length; i++) {
					if (this.langs != null) {
						String lang = this.langs[i];
						langValues.put(lang, this.values[i]);
					}
				}
			}
		}
	}

	public void setLangValues(Map<String, Object> langValues) {
		this.langValues = langValues;
	}

	private void check() {
		if (values != null && langs != null) {
			if (values.length != langs.length) {
				throw new RuntimeException("多语言国际化信息对应不正确");
			}
		}
		if (values == null && langs != null) {
			throw new RuntimeException("多语言国际化信息对应不正确");
		}
		if (values != null && langs == null) {
			throw new RuntimeException("多语言国际化信息对应不正确");
		}
	}

	public String getDefaultValue() {
		if (this.values != null) {
			for (int i = 0; i < this.values.length; i++) {
				if (this.langs != null) {
					String lang = this.langs[i];
					if (defaultLang().equals(lang)) {
						this.defaultValue = this.values[i];
						return this.defaultValue;
					}
				}
			}
		}
		return null;
	}

	public static String defaultLang() {
		if (defaultLang == null) {
			List<I18nLang> langs = getI18nLangs();
			for (I18nLang i18nLang : langs) {
				Integer d = i18nLang.getDefaultlang();
				if (d != null && d == 1) {
					defaultLang = i18nLang.getKey();
					break;
				}
			}
		}
		return defaultLang;
	}

	private static List<I18nLang> getI18nLangs() {
		SdkI18nLangManager sdkI18nLangManager = (SdkI18nLangManager) SpringUtil
				.getBean("sdkI18nLangManager");
		List<I18nLang> langs = sdkI18nLangManager.geti18nLangCache();
		return langs;
	}

	public static int i18nSize() {
		/*if (i18nSize == null) {
			List<I18nLang> langs = getI18nLangs();
			if (Validator.isNullOrEmpty(langs)) {
				return 0;
			}
			i18nSize = langs.size();
			return i18nSize;
		} else {
			return i18nSize;
		}*/
		//以上方法有缺陷:
		//多语言开启，从一种语言变到多个语言时i18nSize没有随之改变
		List<I18nLang> langs = getI18nLangs();
		return Validator.isNullOrEmpty(langs) ? 0 : langs.size();
	}

	public static List<String> i18nLangs() {
		if (i18nLangs == null) {
			i18nLangs = new ArrayList<String>();
			List<I18nLang> langs = getI18nLangs();
			if (Validator.isNullOrEmpty(langs)) {
				return i18nLangs;
			}
			for (I18nLang i18nLang : langs) {
				i18nLangs.add(i18nLang.getKey());
			}
			return i18nLangs;
		} else {
			return i18nLangs;
		}
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	

}
