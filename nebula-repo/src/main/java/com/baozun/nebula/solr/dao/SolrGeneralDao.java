package com.baozun.nebula.solr.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.solr.command.ItemForSolrI18nCommand;
import com.baozun.nebula.solr.utils.Validator;

/**
 * 最基本的创建索引、删除索引、查找索引类 基本保存对象为solrForSolrCommand
 * 可在查询的List<SkuForSolrCommand>基础上选择合适的分页算法
 * SkuItemRepositoryImpl适合在sku、item定义明确情况下使用，该类脱胎于nike项目的solr模块 使用的分页为Pagination
 * 
 * @author jumbo
 * 
 */
@Component
public class SolrGeneralDao{

    private static final Logger log = LoggerFactory.getLogger(SolrGeneralDao.class);

    /**
     * @since 5.3.2.20 change required = false
     * @see <a href="http://jira.baozun.cn/browse/NB-817">NB-817</a>
     */
    @Autowired(required = false)
    protected SolrServer solrServer;

    private DocumentObjectBinder binder;

    public SolrServer getSolrServer(){
        return solrServer;
    }

    public void setSolrServer(SolrServer solrServer){
        this.solrServer = solrServer;
    }

    public void setBinder(DocumentObjectBinder binder){
        this.binder = binder;
    }

    /**
     * 保存对象集合
     */
    public Boolean batchUpdateIndex(List<ItemForSolrCommand> itemList){
        boolean flag = false;
        try{
            if ((Validator.isNotNullOrEmpty(itemList))){
                List<String> ids = new ArrayList<String>();
                for (ItemForSolrCommand itemForSolrCommand : itemList){
                    ids.add(String.valueOf(itemForSolrCommand.getId()));
                }
                solrServer.deleteById(ids);
                solrServer.addBeans(itemList);
                solrServer.commit();
                flag = true;
                log.debug("SolrGeneralDao batchUpdateIndex(List<ItemForSolrCommand> itemList) commint . ");
            }else{
                log.debug("SolrGeneralDao batchUpdateIndex(List<ItemForSolrCommand> itemList) error ,  itemList is null. ");
            }
        }catch (Exception e){
            try{
                solrServer.rollback();
            }catch (SolrServerException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }catch (IOException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            e.printStackTrace();
            log.error("SolrGeneralDao batchUpdateIndex exception . ");
        }
        return flag;
    }

    /**
     * 保存对象集合
     */
    public Boolean updateIndex(List<ItemForSolrCommand> itemList){
        boolean flag = false;
        try{
            if ((Validator.isNotNullOrEmpty(itemList))){
                solrServer.addBeans(itemList);
                solrServer.commit();
                flag = true;
                log.debug("SolrGeneralDao batchUpdateIndex(List<ItemForSolrCommand> itemList) commint . ");
            }else{
                log.debug("SolrGeneralDao batchUpdateIndex(List<ItemForSolrCommand> itemList) error ,  itemList is null. ");
            }
        }catch (Exception e){
            try{
                solrServer.rollback();
            }catch (SolrServerException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }catch (IOException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            e.printStackTrace();
            log.error("SolrGeneralDao batchUpdateIndex exception . ");
        }
        return flag;
    }

    public Boolean updateIndexI18n(List<ItemForSolrI18nCommand> itemList){
        boolean flag = false;
        try{
            if ((Validator.isNotNullOrEmpty(itemList))){
                solrServer.addBeans(itemList);
                solrServer.commit();
                flag = true;
                log.debug("SolrGeneralDao batchUpdateIndex(List<ItemForSolrCommand> itemList) commint . ");
            }else{
                log.debug("SolrGeneralDao batchUpdateIndex(List<ItemForSolrCommand> itemList) error ,  itemList is null. ");
            }
        }catch (Exception e){
            try{
                solrServer.rollback();
            }catch (SolrServerException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }catch (IOException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            e.printStackTrace();
            log.error("SolrGeneralDao batchUpdateIndex exception . ");
        }
        return flag;
    }

    /**
     * 根据传入的关键字进行查找所有符合要求的索引 queryString的格式为 id:123 该方法可以通用查询单个域某个值的索引
     * 
     * @param queryString
     * @return
     * @throws SolrServerException
     */
    public List<ItemForSolrCommand> queryByField(String queryString,String sortString,Integer startNum,Integer rowsNum) throws SolrServerException{

        List<ItemForSolrCommand> itemForSolrCommandList = new ArrayList<ItemForSolrCommand>();

        if (queryString.equals("") & (sortString.equals("")) || (queryString == null) & (sortString == null)){
            itemForSolrCommandList = queryAll();
            log.debug("SolrGeneralDao queryByField(String queryString,String sortString,Integer startNum,Integer rowsNum) error ," + "  queryString is empty or null and sortString is empty or null . ");
        }else{
            ModifiableSolrParams params = new ModifiableSolrParams();
            // 查询关键词，*:*代表所有属性、所有值，即所有index
            if (queryString == null || sortString == null){
                itemForSolrCommandList = queryAll();
                log.debug("SolrGeneralDao queryByField(String queryString,String sortString,Integer startNum,Integer rowsNum) error ," + "  queryString is empty or null , sortString is empty or null . ");
            }
            if (queryString.equals("")){
                queryString = "*:*";
            }
            params.set("q", queryString);
            // 分页，start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
            if (startNum < 0){
                startNum = 0;
            }
            if (rowsNum < 1){
                rowsNum = 1;
            }
            if (startNum > rowsNum){
                int t = startNum;
                startNum = rowsNum;
                rowsNum = t;
            }
            params.set("start", startNum);
            params.set("rows", rowsNum);
            // 排序，，比如score desc，id desc(or asc)
            if (sortString.equals("")){
                sortString = "score desc";
            }
            params.set("sort", sortString);
            // 返回信息 * 为全部 这里是全部加上score，如果不加下面就不能使用score
            params.set("fl", "*,score");

            QueryResponse response = solrServer.query(params);
            itemForSolrCommandList = response.getBeans(ItemForSolrCommand.class);
            log.debug("SolrGeneralDao queryByField success . ");
        }

        return itemForSolrCommandList;
    }

    /**
     * 根据传入的SolrQuery进行查询索引 范围List<ItemForSolrCommand>
     * 
     * @return
     * @throws SolrServerException
     */
    public QueryResponse queryBysolrQuery(SolrQuery solrQuery) throws SolrServerException{
        QueryResponse response = new QueryResponse();
        if ((Validator.isNotNullOrEmpty(solrQuery))){
            response = solrServer.query(solrQuery, METHOD.POST);
            log.debug("SolrGeneralDao queryBysolrQuery(SolrQuery solrQuery) success . ");
        }
        return response;

    }

    /**
     * 以SolrDocumentList格式返回所有查询索引
     * 
     * @return
     * @throws SolrServerException
     */
    public SolrDocumentList queryAllSolrDocumentList() throws SolrServerException{

        SolrDocumentList solrDocumentList = new SolrDocumentList();

        ModifiableSolrParams params = new ModifiableSolrParams();
        // 查询关键词，*:*代表所有属性、所有值，即所有index
        params.set("q", "*:*");
        // 分页，start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
        params.set("start", 0);
        params.set("rows", Integer.MAX_VALUE);
        // 排序，，如果按照id 排序，，那么将score desc 改成 id desc(or asc)
        params.set("sort", "score desc");
        // 返回信息 * 为全部 这里是全部加上score，如果不加下面就不能使用score
        params.set("fl", "*,score");

        QueryResponse response = solrServer.query(params);
        GroupResponse groupResponse = response.getGroupResponse();

        if (null != groupResponse){

            List<GroupCommand> groupCommandList = groupResponse.getValues();
            for (GroupCommand groupCommand : groupCommandList){

                List<Group> groupList = groupCommand.getValues();

                for (Group group : groupList){
                    solrDocumentList = group.getResult();

                }

            }

        }
        log.debug("SolrGeneralDao queryAll success . ");

        return solrDocumentList;
    }

    public SolrDocumentList toSolrDocumentList(ModifiableSolrParams params) throws SolrServerException{
        SolrDocumentList solrDocumentList = new SolrDocumentList();
        QueryResponse response = solrServer.query(params);

        solrDocumentList = response.getResults();
        return solrDocumentList;
    }

    /**
     * 根据传入查询条件删除solr索引 queryString格式为name:abc,id:123 逗号表示条件之间为与关系
     * 如果只有一个条件则为“name:abc”,多个条件用逗号分隔开
     * 
     * @param queryString
     */
    public void deleteByQuery(String queryString){
        // 由于*:*是空所有索引，为了安全起见，此处不允许执行这种格式的删除。
        if (Validator.isNotNullOrEmpty(queryString)){
            try{
                if (!queryString.equals("*:*")){
                    solrServer.deleteByQuery(queryString);
                    solrServer.commit();
                    log.debug("SolrGeneralDao deleteByQuery success . ");
                }else{
                    log.debug("SolrGeneralDao deleteByQuery not allow delete by *:* . ");
                }
            }catch (SolrServerException e){
                e.printStackTrace();
                log.error("SolrGeneralDao deleteByQuery SolrServerException . ");

            }catch (IOException e){
                e.printStackTrace();
                log.error("SolrGeneralDao deleteByQuery IOException . ");

            }
        }else{
            log.debug("SolrGeneralDao deleteByQuery error , queryString is null . ");
        }

    }

    /**
     * 清空所有索引
     */
    public Boolean cleanAll(){
        boolean flag = false;
        try{
            solrServer.deleteByQuery("*:*");
            solrServer.commit();
            log.debug("SolrGeneralDao cleanAll success . ");
            flag = true;
        }catch (SolrServerException e){
            try{
                solrServer.rollback();
            }catch (SolrServerException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }catch (IOException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            e.printStackTrace();
            log.error("SolrGeneralDao cleanAll SolrServerException . ");
        }catch (IOException e){
            try{
                solrServer.rollback();
            }catch (SolrServerException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }catch (IOException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            e.printStackTrace();
            log.error("SolrGeneralDao cleanAll IOException . ");
        }
        return flag;
    }

    /**
     * 根据传入id集合删除索引
     * 
     * @param solrId
     */
    public Boolean deleteByIds(List<String> ids){
        boolean flag = false;
        try{
            if ((Validator.isNotNullOrEmpty(ids))){
                // 删除传入id的索引
                for (String id : ids){
                    solrServer.deleteByQuery("id:" + id);
                }
                solrServer.commit();
                log.debug("SolrGeneralDao deleteByIds success . ");
                flag = true;
            }else{
                log.error("SolrGeneralDao deleteByIds error , ids is null . ");
            }
        }catch (SolrServerException e){
            try{
                solrServer.rollback();
            }catch (SolrServerException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }catch (IOException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            e.printStackTrace();
            log.error("SolrGeneralDao deleteByIds SolrServerException . ");

        }catch (IOException e){
            try{
                solrServer.rollback();
            }catch (SolrServerException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }catch (IOException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            e.printStackTrace();
            log.error("SolrGeneralDao deleteByIds IOException . ");

        }
        return flag;
    }

    /**
     * 
     * @param obj
     */
    public void createOrUpdateObjectIndex(Object obj){

        try{

            if (Validator.isNotNullOrEmpty(obj)){
                solrServer.addBean(obj);
                solrServer.commit();
                log.debug("SolrGeneralDao createOrUpdateObjectIndex(Object obj) commint . ");
            }else{
                log.debug("SolrGeneralDao createOrUpdateObjectIndex(Object obj) error ,obj is null . ");
            }

        }catch (Exception e){
            try{
                solrServer.rollback();
            }catch (SolrServerException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }catch (IOException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            e.printStackTrace();
            log.error("SolrGeneralDao createOrUpdateIndex exception . ");
        }

    }

    /**
     * 
     * @param objList
     */
    public void batchUpdateObjectIndex(List<Object> objList){
        try{
            if (Validator.isNotNullOrEmpty(objList)){
                solrServer.addBeans(objList);
                solrServer.commit();
                log.debug("SolrGeneralDao batchUpdateObjectIndex(List<Object> objList) commint . ");
            }else{
                log.debug("SolrGeneralDao batchUpdateObjectIndex(List<Object> objList) error ,objList id null. ");
            }

        }catch (Exception e){
            try{
                solrServer.rollback();
            }catch (SolrServerException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }catch (IOException ex){
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            e.printStackTrace();
            log.error("SolrGeneralDao batchUpdateIndex exception . ");
        }

    }

    /**
     * 查询对象集合
     * 
     * @return
     * @throws SolrServerException
     */
    public List<ItemForSolrCommand> queryAll() throws SolrServerException{

        List<ItemForSolrCommand> itemForSolrCommandList = new ArrayList<ItemForSolrCommand>();

        ModifiableSolrParams params = new ModifiableSolrParams();
        // 查询关键词，*:*代表所有属性、所有值，即所有index    
        params.set("q", "*:*");
        // 分页，start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。    
        params.set("start", 0);
        params.set("rows", Integer.MAX_VALUE);
        // 排序，，如果按照id 排序，，那么将score desc 改成 id desc(or asc)    
        params.set("sort", "score desc");
        // 返回信息 * 为全部 这里是全部加上score，如果不加下面就不能使用score    
        params.set("fl", "*,score");

        QueryResponse response = solrServer.query(params);
        itemForSolrCommandList = response.getBeans(ItemForSolrCommand.class);
        log.debug("SolrGeneralDao queryAll success . ");

        return itemForSolrCommandList;
    }

    /**
     * SolrQuery分组调用的方法
     * 
     * @param params
     * @return
     * @throws SolrServerException
     */
    public QueryResponse query(SolrParams params) throws SolrServerException{
        return solrServer.query(params);
    }

    public DocumentObjectBinder getBinder(){
        return solrServer.getBinder();
    }
}
