package com.baozun.nebula.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @deprecated pls use {@link com.feilong.servlet.http.SessionUtil}
 */
@Deprecated
public class SessionUtil{

    private final static Logger log = LoggerFactory.getLogger(SessionUtil.class);

    public static HttpSession replaceSession(HttpServletRequest request){
        // 当session存在时返回该session，否则不会新建session，返回null
        HttpSession session = request.getSession(false);
        if (null != session){
            // getSession()/getSession(true)：当session存在时返回该session，否则新建一个session并返回该对象
            session = request.getSession();
            log.debug("old session: {}", session.getId());
            Map<String, Object> map = new HashMap<String, Object>();
            Enumeration enumeration = session.getAttributeNames();
            while (enumeration.hasMoreElements()){
                String key = (String) enumeration.nextElement();
                map.put(key, session.getAttribute(key));
            }
            session.invalidate();
            session = request.getSession();
            log.debug("new session: {}", session.getId());
            for (String key : map.keySet()){
                session.setAttribute(key, map.get(key));
            }
        }else{
            // 是null 新建一个
            session = request.getSession();
        }
        return session;
    }
}
