package com.baozun.nebula.utilities.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LangUtil{

    public static final Logger                 log          = LoggerFactory.getLogger(LangUtil.class);

    public static final String                 ZH_CN        = "zh_CN";

    public static final String                 EN_US        = "en_US";

    public static final String                 I18_LANG_KEY = "clientlanguage";

    private String                             lang;

    private static final ThreadLocal<LangUtil> context      = new ThreadLocal<LangUtil>(){

                                                                @Override
                                                                protected LangUtil initialValue(){
                                                                    return new LangUtil();
                                                                }
                                                            };

    public static LangUtil getCurrentContext(){
        return context.get();

    }

    public static String getCurrentLang(){
        return getCurrentContext().lang;
    }

    public static void setCurrentLang(String lang){
        getCurrentContext().lang = lang;
    }

    public static void remove(){
        context.remove();
    }

}
