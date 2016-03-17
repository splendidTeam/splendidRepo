package com.baozun.nebula.manager.product;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Sort;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.model.product.ItemTag;
import com.baozun.nebula.utils.JsonFormatUtil;
import com.baozun.nebula.utils.query.bean.QueryBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
									"classpath*:loxia-hibernate-context.xml",
									"classpath*:loxia-service-context.xml",
									"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class ItemTagManagerTest {

	@Autowired
	private ItemTagManager itemTagManager ;
	
	private static final Logger	log	= LoggerFactory.getLogger(ItemTagManagerTest.class);
	
	@Test
	public void testBindItemTag() {
		
		Long[] itemIds ={2788L};
		Long[] tagIds ={3L};
		
		itemTagManager.bindItemTag(itemIds, tagIds);
	}

	@Test
	public void testUnBindItemTag() {
		Long[] itemIds ={2788L};
		Long tagId =3L;
		itemTagManager.unBindItemTag(itemIds, tagId);
	}

	@Test
	public void testFindEffectItemTagListByQueryMap() {
		
		Map<String, Object> paraMap =new  HashMap<String, Object>();
		
		QueryBean queryBean =new QueryBean();
		Sort[] sorts=queryBean.getSorts();
		
		if(sorts==null||sorts.length==0){
			Sort sort=new Sort("name asc","desc");
			sorts=new Sort[1];
			sorts[0]=sort;
		}
		
		itemTagManager.findEffectItemTagListByQueryMap(paraMap, sorts);
		
	}

	@Test
	public void testFindItemTagListByQueryMapWithPage() {
		
		QueryBean queryBean = new QueryBean();
		Sort[] sorts=queryBean.getSorts();
		
		Page page =new Page();
		page.setSize(5);
		page.setStart(0);
		
		queryBean.setPage(page);
		
		Long shopId =256L;
		
		if(sorts==null||sorts.length==0){
			Sort sort=new Sort("tpit.create_time","desc");
			sorts=new Sort[1];
			sorts[0]=sort;
		}
		itemTagManager
				.findItemTagListByQueryMapWithPage(queryBean.getPage()
						,sorts, queryBean.getParaMap(), shopId);
		
	}

	@Test
	public void testFindItemNoTagListByQueryMapWithPage() {
		QueryBean queryBean = new QueryBean();
		Sort[] sorts=queryBean.getSorts();
		
		Page page =new Page();
		page.setSize(5);
		page.setStart(0);
		
		queryBean.setPage(page);
		
		Long shopId =256L;
		
		if(sorts==null||sorts.length==0){
			Sort sort=new Sort("tpit.create_time","desc");
			sorts=new Sort[1];
			sorts[0]=sort;
		}
		itemTagManager
				.findItemNoTagListByQueryMapWithPage(queryBean.getPage()
						,sorts, queryBean.getParaMap(), shopId);
	}

	@Test
	public void testFindTagListByTagIds() {
		Long[] tagIds ={1L,2L,3L};
		log.debug("---:{}", JsonFormatUtil.
				format(itemTagManager.findTagListByTagIds(tagIds)));
	}

	@Test
	public void testFindItemTagRelationListByItemId() {
		
		Long itemIds =2788L;
		log.debug("---:{}", JsonFormatUtil.
				format(itemTagManager.findItemTagRelationListByItemId(itemIds)));
		
	}

	@Test
	public void testValidateUnBindByItemIdsAndTagId() {
		Long[] itemIds ={2788L};
		Long tagId =8L;
		log.debug("----sss:{}", itemTagManager.validateUnBindByItemIdsAndTagId(itemIds, tagId));
	}

	@Test
	public void testCreateOrUpdateItemTag() {
		ItemTag itemTag =new ItemTag();
		itemTag.setCreateTime(new Date());
		itemTag.setLifecycle(ItemTag.LIFECYCLE_ENABLE);
		itemTag.setName("zzz");
		itemTag.setType(Integer.valueOf(1));
		itemTag.setVersion(new Date());
		itemTagManager.createOrUpdateItemTag(itemTag);
	}

	@Test
	public void testValidateTagName() {
		log.debug("---sssss:{}", itemTagManager.validateTagName("zzz"));
	}

	@Test
	public void testRemoveTagByIds() {
		
		List<Long> ids =new ArrayList<Long>();
		ids.add(10L);
		ids.add(11L);
		
		log.debug("----sssssss:{}", itemTagManager.removeTagByIds(ids));
	}

}
