package com.baozun.nebula.web.filter;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baozun.nebula.command.product.FilterNavigationCommand;
import com.baozun.nebula.manager.navigation.NavigationHelperManager;
import com.baozun.nebula.utils.cache.GuavaAbstractLoadingCache;
import com.feilong.core.Validator;
import com.feilong.servlet.http.RequestUtil;
import com.google.common.base.Optional;

/**
 * 导航的filter，将商品集合的导航跳转到对应的collection中去
 * 
 * @author long.xia
 * @author dianchao.song 添加了1分钟缓存，可以缓解每次拦截时的数据库连接的占用
 */
public class NavigationFilter implements Filter {

    private WebApplicationContext webApplicationContext;

    private NavigationHelperManager navigationHelperManager;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();

        webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        navigationHelperManager = webApplicationContext.getBean(NavigationHelperManager.class);

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try{
            if (!RequestUtil.isAjaxRequest(request)){
                Optional<String> url = cache.getValue(request.getRequestURI() + "<>" + request.getQueryString());
                if (url.isPresent()){
                    request.getRequestDispatcher(url.get()).forward(request, response);
                }else{
                    chain.doFilter(request, response);
                }
            }else{
                chain.doFilter(request, response);
            }
        }catch (ExecutionException e){
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

    private GuavaAbstractLoadingCache<String, Optional<String>> cache = new GuavaAbstractLoadingCache<String, Optional<String>>() {

        @Override
        protected Optional<String> fetchData(String key) {
            String[] keys = key.split("<>");
            String pathURI = keys[0];
            pathURI = pathURI.endsWith("/") ? pathURI.substring(0, pathURI.length() - 1) : pathURI;
            String queryStr = keys.length == 2 ? keys[1] : null;

            FilterNavigationCommand filterNavigation = navigationHelperManager.matchNavigationByUrl(pathURI, queryStr);

            //如果导航是实际的商品列表导航页面，那需要跳转到导航的列表页，
            if (Validator.isNotNullOrEmpty(filterNavigation)){
                StringBuilder forwardURL = new StringBuilder("/sys/navigation?nid=" + filterNavigation.getNavId());
                if (Validator.isNotNullOrEmpty(filterNavigation.getCollectionId())){
                    forwardURL.append("&cid=").append(filterNavigation.getCollectionId());
                }
                return Optional.fromNullable(forwardURL.toString());
            }else{
                return Optional.fromNullable(null);
            }
        }
    };
}
