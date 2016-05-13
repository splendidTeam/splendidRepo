package com.baozun.nebula.manager.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.product.FilterNavigationCommand;
import com.baozun.nebula.dao.baseinfo.NavigationDao;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.model.baseinfo.Navigation;
import com.feilong.core.Validator;

@Transactional(readOnly=true)
@Service
public class NavigationHelperManagerImpl implements NavigationHelperManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(NavigationHelperManagerImpl.class);

	private static final  String	CACHE_KEY_FILTER_NAV = "cache_key_filter_nav";
	
	private static final  String	CACHE_FIELD_FILTER_NAV = "cache_field_filter_nav";
	
	@Autowired
	private NavigationDao				navigationDao;
	
	@Autowired
	private CacheManager 				cacheManager;

	
	@Override
	public FilterNavigationCommand matchNavigationByUrl(String uri, String queryStr) {
		// TODO Auto-generated method stub
		
//		Map<Long, MetaDataCommand> navMetaMap = facetFilterHelper.getNavigationMetaMap(lang);
		
		Map<String,List<FilterNavigationCommand>> resultMap = this.getAllNavigationMap();
		
		List<FilterNavigationCommand> nodes = resultMap.get(uri);
		if(Validator.isNullOrEmpty(nodes)){
			return null;
		}
		
		//如果根据URI只匹配到一个navigation，那就不需要进行下一步的操作
		if(nodes.size() ==1){
			return nodes.get(0);
		}
		
		
		FilterNavigationCommand resultNavTmp = nodes.get(0);
		int tmpCount = this.matchParamCount(queryStr, resultNavTmp.getParams());
		
		for(int i=1;i<nodes.size();i++){
			//如果没有参数，那么navigation里面匹配的也要是没有参数的navigation Id
			if(Validator.isNullOrEmpty(queryStr) && Validator.isNullOrEmpty(nodes.get(i).getParams())){
				resultNavTmp = nodes.get(i);
				break;
			}
			
			//如果匹配的更加精确，那么需要做最终的返回
			int c = this.matchParamCount(queryStr, nodes.get(i).getParams());
			if(c > tmpCount){
				tmpCount = c;
				resultNavTmp = nodes.get(i);
			}
			
			
		}
		
		return resultNavTmp;
	}
	
	/**
	 * 匹配同样参数的个数
	 * @param queryStr
	 * 			浏览器中的参数字符串
	 * @param paramStr
	 * 			导航表中的导航参数字符串
	 * @return
	 */
	private int matchParamCount(String queryStr, String paramStr){
		if(Validator.isNullOrEmpty(queryStr) || Validator.isNullOrEmpty(paramStr)){
			return 0;
		}
		String[] tmp = queryStr.split("&");
		String[] paramArray = paramStr.split("&");
		
		int count = 0;
		for(String s : tmp){
			if(ArrayUtils.contains(paramArray, s)){
				count ++;
			}
		}
		return count;
		
	}
	
	/**
	 * 获取所有的导航信息，
	 * @return 
	 * 		key：导航中url的URI（去除末尾的"/"）
	 */
	public Map<String,List<FilterNavigationCommand>> getAllNavigationMap(){
		
		Map<String,List<FilterNavigationCommand>> resultMap = null;
		try{
			resultMap = cacheManager.getMapObject(CACHE_KEY_FILTER_NAV, CACHE_FIELD_FILTER_NAV);
		}catch(Exception e){
			LOG.error("get cache error : ",e);
		}
		
		if(Validator.isNullOrEmpty(resultMap)){
			
			resultMap = new HashMap<String,List<FilterNavigationCommand>>();
			
			List<Navigation> allNode = navigationDao.findAllNavigationList(null);
			for(Navigation node : allNode){
				//只有商品列表页才需要查找导航对应的 itemcollection
				if(Navigation.TYPE_ITEM_LIST.equals(node.getType()) && Validator.isNotNullOrEmpty(node.getUrl())){
					String[] tmp = this.splitNavUrl(node.getUrl());
					String uri = tmp[0];
					String params = tmp.length == 2? tmp[1]:null;
					
					if(Validator.isNotNullOrEmpty(resultMap.get(tmp[0]))){
						FilterNavigationCommand navTmp = new FilterNavigationCommand(node.getId(),node.getCollectionId(),uri,params);
						resultMap.get(tmp[0]).add(navTmp);
					}else{
						List<FilterNavigationCommand> list = new ArrayList<FilterNavigationCommand>();
						FilterNavigationCommand navTmp = new FilterNavigationCommand(node.getId(),node.getCollectionId(),uri,params);
						list.add(navTmp);
						resultMap.put(tmp[0], list);
					}
				}
			}
			
			for(Entry<String, List<FilterNavigationCommand>> entry : resultMap.entrySet()){
				Collections.sort(entry.getValue());  
			}
			
			try{
				cacheManager.setMapObject(CACHE_KEY_FILTER_NAV,CACHE_FIELD_FILTER_NAV, resultMap, 60*5);
			}catch(Exception e){
				LOG.error("set cache error : ",e);
			}
			
		}
		return resultMap;
	}
	
	
	/**
	 * 将导航表中的url分割成 uri和params
	 * @param navUrl
	 * @return
	 */
	private String[] splitNavUrl(String navUrl){
		String[] result = null;
		if(Validator.isNotNullOrEmpty(navUrl)){
			
			//去除#后面的东西
			int endIndex = navUrl.indexOf("#");
			endIndex = endIndex<0?endIndex = navUrl.length():endIndex;
			navUrl = navUrl.substring(0,endIndex);
			
			if(Validator.isNotNullOrEmpty(navUrl)){
				result = navUrl.split("\\?");
				if(result[0].endsWith("/")){
					result[0] = result[0].substring(0, result[0].length()-1);
				}
			}
		}
		
		return result;
	}
	
}
