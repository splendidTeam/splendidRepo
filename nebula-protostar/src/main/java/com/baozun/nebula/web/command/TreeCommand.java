/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baozun.nebula.command.Command;

/**
 * 所有tree 的父类,如果是简单的tree (没有其他附加属性) 也可以直接拿这个treeCommand 来使用.
 * 
 * @param <T>
 *            泛型, id 和parentId 类型
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 Jun 24, 2013 2:55:29 PM
 */
public class TreeCommand<T> implements Command{

    /** The Constant serialVersionUID. */
    private static final long              serialVersionUID = 1184663529658130239L;

    /** current id, 可以是Number String 或者其他类型.. */
    private T                              id;

    /** parent id ,可以是Number String 或者其他类型.. */
    private T                              parentId;

    /** code. */
    private String                         code;

    /** name,用于拼接. */
    private String                         name;

    /** title. */
    private String                         title;

    /** 顺序. */
    private int                            sortNo;

    /** children TreeCommand List. */
    private List<? extends TreeCommand<T>> childrenTreeCommandList;

    /**
     * Gets the current id, 可以是Number String 或者其他类型.
     * 
     * @return the id
     */
    public T getId(){
        return id;
    }

    /**
     * Gets the name,用于拼接.
     * 
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * Gets the title.
     * 
     * @return the title
     */
    public String getTitle(){
        return title;
    }

    /**
     * Gets the parent id ,可以是Number String 或者其他类型.
     * 
     * @return the parentId
     */
    public T getParentId(){
        return parentId;
    }

    /**
     * Sets the current id, 可以是Number String 或者其他类型.
     * 
     * @param id
     *            the id to set
     */
    public void setId(T id){
        this.id = id;
    }

    /**
     * Sets the name,用于拼接.
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Sets the title.
     * 
     * @param title
     *            the title to set
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * Sets the parent id ,可以是Number String 或者其他类型.
     * 
     * @param parentId
     *            the parentId to set
     */
    public void setParentId(T parentId){
        this.parentId = parentId;
    }

    /**
     * Gets the code.
     * 
     * @return the code
     */
    public String getCode(){
        return code;
    }

    /**
     * Sets the code.
     * 
     * @param code
     *            the code to set
     */
    public void setCode(String code){
        this.code = code;
    }

    /**
     * Gets the 顺序.
     * 
     * @return the sortNo
     */
    public int getSortNo(){
        return sortNo;
    }

    /**
     * Sets the 顺序.
     * 
     * @param sortNo
     *            the sortNo to set
     */
    public void setSortNo(int sortNo){
        this.sortNo = sortNo;
    }

    /**
     * Gets the children TreeCommand List.
     * 
     * @return the childrenTreeCommandList
     */
    public List<? extends TreeCommand<T>> getChildrenTreeCommandList(){
        return childrenTreeCommandList;
    }

    /**
     * Sets the children TreeCommand List.
     * 
     * @param childrenTreeCommandList
     *            the childrenTreeCommandList to set
     */
    public void setChildrenTreeCommandList(List<? extends TreeCommand<T>> childrenTreeCommandList){
        this.childrenTreeCommandList = childrenTreeCommandList;
    }

    /**
     * 递归构建
     * 
     * @param leafTreeCommand
     *            叶子节点
     * @param parentTreeCommand
     *            父节点
     */
    public static <T> void recursionConstract(TreeCommand<T> leafTreeCommand,TreeCommand<T> parentTreeCommand){
        @SuppressWarnings("unchecked")
        List<TreeCommand<T>> childrenTreeCommandList = (List<TreeCommand<T>>) parentTreeCommand.getChildrenTreeCommandList();
        if (CollectionUtils.isEmpty(childrenTreeCommandList)){
            childrenTreeCommandList = new ArrayList<TreeCommand<T>>();
        }

        T parentId = leafTreeCommand.getParentId();
        if (parentId.equals(parentTreeCommand.getId())){
            childrenTreeCommandList.add(leafTreeCommand);
            parentTreeCommand.setChildrenTreeCommandList(childrenTreeCommandList);
        }else{
            for (TreeCommand<T> childrenTreeCommand : childrenTreeCommandList){
                recursionConstract(leafTreeCommand, childrenTreeCommand);
            }
        }
    }

}
