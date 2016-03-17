<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="product.property.lable.manager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>

<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/product/category/category-manager.js"></script>

<SCRIPT type="text/javascript">	
//只展开ROOT
var zNodes = [
         { id:0,pId:null, name:"ROOT",code:"ROOT",lifecycle:"1", open:true,sortNo:0,drag:true},        
	     <c:forEach var="category" items="${categoryList}" varStatus="status">
	     	<c:set var="isRoot" value="${null eq category.parentId}"></c:set>
	     	{id:${category.id}, pId:${isRoot?0:category.parentId}, name:"${category.name}", code:"${category.code}", sortNo:${category.sortNo}, drag:${!isRoot}, open:${isRoot?true:false}, lifecycle:${category.lifecycle} } <c:if test="${!status.last}">,</c:if>
	     </c:forEach>
];
</SCRIPT>

<style type="text/css">
.i18n-lang{
	display: none;
}
</style>

</head>
<body>

    <div class="content-box width-percent100">
       
	       <div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/tag.png"><spring:message code="product.property.lable.manager"/></div>
           <div class="ui-block ui-block-fleft w240">
                    <div class="ui-block-content ui-block-content-lb">
                        <div class="tree-control">
                            <input type="text" id="key" loxiatype="input" placeholder="<spring:message code="shop.property.keyword"/>" />
                            <div><span id="search_result"></span></div>
                        </div>
                            <ul id="categoryTree" class="ztree"></ul>

                    </div>
            </div>
        
            <div class="ui-block ml240 cate-update" style="padding-left: 10px;">
	            <div class="ui-block-title1"><spring:message code="product.property.lable.category"/></div>
		           <div id="currentNodeDiv" class="ui-block-content border-grey" style="margin-bottom: 10px;">
			          <div class="ui-block-line">
				         <label><spring:message code="itemcategory.code"/></label>
				           <input type="text" id="tree_code" loxiatype="input" placeholder="<spring:message code="itemcategory.code"/>"  />
			         </div>
			         <c:if test="${i18nOnOff == true}">
			          	<c:forEach items="${i18nLangs}" var="i18nLang">
				         <div class="ui-block-line">
					            <label><spring:message code="itemcategory.name"/></label>
					            <input type="text" id="tree_name_zh_cn" class="mutl-lang" lang="${i18nLang.key}" loxiatype="input" placeholder="<spring:message code="itemcategory.name"/>" />
					            <span>${i18nLang.value}</span>
				        </div>
				        </c:forEach>
			        </c:if>
			         <c:if test="${i18nOnOff == false}">
			          <div class="ui-block-line">
				           <label><spring:message code="itemcategory.name"/></label>
				            <input type="text" id="tree_name_zh_cn"  loxiatype="input" placeholder="<spring:message code="itemcategory.name"/>" />
			        </div>
			          </c:if>
			        <div class="button-line1">
			               <a href="javascript:void(0);" class="func-button persist" id="save_node" title="<spring:message code="btn.save"/>"><spring:message code="btn.save"/></a>
                           <a href="javascript:void(0);" class="func-button delete" id="remove_element" title="<spring:message code="btn.delete"/>"><spring:message code="btn.delete"/></a>
			        </div>

	               </div>
	         </div>
             
             
             <div class="ui-block ml240 cate-add" style="padding-left: 10px;">
                  <div class="ui-block-title1"><spring:message code="product.property.lable.insert"/></div>
		          <div id="separateDiv" class="ui-block-content border-grey" style="margin-bottom: 10px;">
		                <div class="ui-block-line">
				            <label><spring:message code="itemcategory.code"/></label>
				            <input type="text" id="add_code" loxiatype="input" placeholder="<spring:message code="itemcategory.code"/>"  />
			            </div>
			            <c:if test="${i18nOnOff == true}">
				            <c:forEach items="${i18nLangs}" var="i18nLang">
				            <div class="ui-block-line">
					            <label><spring:message code="itemcategory.name"/></label>
					            <input type="text"  class="mutl-lang" lang="${i18nLang.key}" loxiatype="input" placeholder="<spring:message code="itemcategory.name"/>" />
				            	<span>${i18nLang.value}</span>
				            </div>
				            </c:forEach>
			            </c:if>
			            <c:if test="${i18nOnOff == false}">
			               <div class="ui-block-line">
				            <label><spring:message code="itemcategory.name"/></label>
				            <input type="text" id="add_name_zh_cn" loxiatype="input" placeholder="<spring:message code="itemcategory.name"/>" />
			               </div>
			            </c:if>
                        <div class="button-line1">
			               <a  href="javascript:void(0);" class="func-button insert" id="insertSibling" title="<spring:message code="product.property.lable.add"/>" />
			                  <spring:message code="product.property.lable.add"/>
			               </a>
			  
                           <a  href="javascript:void(0);" class="func-button insertLeaf" id="addLeaf" title="<spring:message code="product.property.lable.addchild"/>" />
                              <spring:message code="product.property.lable.addchild"/>
                           </a>
                         </div>
                      </div>
		     </div>
		     <div class='error-information' style="display:none;"><h5></h5><p></p></div>

      </div>
</body>
</html>
