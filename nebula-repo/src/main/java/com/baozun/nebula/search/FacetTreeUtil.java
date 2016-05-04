package com.baozun.nebula.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * facet后台转成树状结构
 * 
 * @author long.xia
 *
 */
public class FacetTreeUtil {

	private static final String TREE_LEVEL_SEPARATOR = "-";

	/**
	 * 创建facet树，为navigation和category服务
	 * @param facetGroup
	 * @return
	 */
	public static List<Facet> createFacetTree(FacetGroup facetGroup) {
		// 一级节点
		List<Long> lv1Nodes = new ArrayList<Long>();
		// 所有节点数据
		List<Facet> allNodeData = new ArrayList<Facet>();
		Map<Long, List<Long>> childrenMapping = null;
		// 根节点
		Facet rootNode = null;
		
		formatData(facetGroup, lv1Nodes, allNodeData);
		
		initMenuData(allNodeData, childrenMapping, rootNode);
		
		List<Facet> result = constructTree(lv1Nodes, allNodeData, childrenMapping);
		
		return result;
	}

	/**
	 * 格式化List<FacetField>数据,取的所有的一级节点和所有的节点，为后面生成树状结构准备
	 */
	private static void formatData(FacetGroup facetGroup, List<Long> lv1Nodes, List<Facet> allNodeData) {

		List<Facet> facets = facetGroup.getFacets();
		
		//facet value:存放的应该是tree的级别形式的数据
		//1/10/1010
		for (Facet facet : facets) {
			String[] cTmp = facet.getValue().split(TREE_LEVEL_SEPARATOR);
			Facet facetTmp = new Facet();

			// ----设置id和parentid
			// 一级分类
			if (cTmp.length == 1) {
				lv1Nodes.add(Long.parseLong(cTmp[0]));
				facetTmp.setId(Long.parseLong(cTmp[0]));
				facetTmp.setCount(facet.getCount());
				facetTmp.setParentId(-1L);
			} else {
				// 数组下标从0开始，要-2才是上一级
				facetTmp.setParentId(Long.parseLong(cTmp[cTmp.length - 2]));
				facetTmp.setId(Long.parseLong(cTmp[cTmp.length - 1]));
				facetTmp.setCount(facet.getCount());
			}
			
			facetTmp.setValue(facet.getValue());
			allNodeData.add(facetTmp);
		}
	}
	
	private static void initMenuData(List<Facet> allNodeData,Map<Long, List<Long>> childrenMapping,Facet rootNode ) {
		Collections.sort(allNodeData, new Comparator<Facet>() {
			@Override
			public int compare(Facet o1, Facet o2) {
				return (int) (o1.getId() - o2.getId());
			};
		});
		childrenMapping = constructMapping(allNodeData, rootNode);
	}
	
	
	private static Map<Long, List<Long>> constructMapping(List<Facet> allNodeData,Facet rootNode) {
		Map<Long, List<Long>> result = new HashMap<Long, List<Long>>();
		List<Long> childrenIds = null;
		Long parentId = null, childId = null;
		for (Facet dto : allNodeData) {
			parentId = dto.getParentId();
			childId = dto.getId();
			if (parentId == null) {
				// 对于ROOT 节点 需要把自身放在 map中
				childrenIds = new LinkedList<Long>();
				if (!result.containsKey(childId)) {
					result.put(childId, childrenIds);
				}
				rootNode = dto;
				continue;
			}
			if(rootNode!=null && dto.getParentId().equals(rootNode.getId())){
				rootNode.getChildrens().add(dto);
			}
			if (!result.containsKey(parentId)) {
				childrenIds = new LinkedList<Long>();
				childrenIds.add(childId);
				result.put(parentId, childrenIds);
			} else {
				result.get(parentId).add(childId);
			}
		}
		return result;
	}
	
	private static List<Facet> constructTree(List<Long> children,List<Facet> allNodeData,Map<Long, List<Long>> childrenMapping) {

		List<Facet> result = new ArrayList<Facet>();
		Facet dto = null;
		for (Long id : children) {
			int index = searchRecursively(id,allNodeData);
			//没有找到分类
			if(index == -1){
				continue;
			}
			dto = new Facet(allNodeData.get(index));
			if(childrenMapping!=null){
				if (childrenMapping.containsKey(dto.getId())) {
					List<Long> temp = childrenMapping.get(dto.getId());
					dto.getChildrens().addAll(constructTree(temp,allNodeData,childrenMapping));
				}
			}
			
			result.add(dto);
		}
		return result;
	}
	
	private static int doSearchRecursively(int low, int high, Long key,List<Facet> allNodeData) {
		int mid, result;
		if (low <= high) {
			mid = (low + high) / 2;
			result = key.compareTo(allNodeData.get(mid).getId());
			if (result < 0) {
				return doSearchRecursively(low, mid - 1, key,allNodeData);
			} else if (result > 0) {
				return doSearchRecursively(mid + 1, high, key,allNodeData);
			} else if (result == 0) {
				return mid;
			}
		}
		return -1;
	}

	private static int searchRecursively(Long key,List<Facet> allNodeData) {
		if (allNodeData == null)
			return -1;
		return doSearchRecursively(0, allNodeData.size() - 1, key,allNodeData);
	}
	
}
