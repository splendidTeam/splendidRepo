<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="industry.list.manager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/system/navigation/navigation.js"></script>
<SCRIPT type="text/javascript">	
var zNodes =[           
	{ id:0, name:"ROOT",state:"1", open:true,root:"true"},
	<c:forEach var="navigation" items="${navigationList}" varStatus="status">
		{
			id:${navigation.id}, 
			pId:${null eq navigation.parentId?0:navigation.parentId}, 
			name: "${navigation.name}", 
			state:"${navigation.lifecycle}",
			diy_type:"${navigation.type}",
			diy_param:"${navigation.param}",
			diy_sort:"${navigation.sort}",
			diy_url:"${navigation.url}",
			diy_isNewWin:${navigation.isNewWin},
			open:${0 eq navigation.parentId?true:false}
		}<c:if test="${!status.last}">,</c:if>
	</c:forEach>
	
];

var category_ZNodes = [
		{id:0, pId:null, name:"ROOT",
			  code:"ROOT", sortNo:1,
			  open:true, lifecycle:1,root:"true"}, 
     <c:forEach var="category" items="${categoryList}" varStatus="status">
     	
     	{id:${category.id}, pId:${category.parentId}, name:"${category.name}", 
     		code:"${category.code}", sortNo:${category.sortNo}, 
     		open:false, lifecycle:${category.lifecycle}}
     	<c:if test="${!status.last}">,</c:if>
     </c:forEach>
];
</SCRIPT>

</head>
<body>
<%-- <h1><a href="/member/custom/add-group.htm">会员自定义分组入口（测试）</a></h1>
<h1><a href="/member/combo/add.htm">会员自定义组合入口（测试）</a></h1> --%>
    <div class="content-box width-percent100">
    
       <div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/wrench_plus_2.png">
           <spring:message code="navigation.list.manager"/>
       </div>
       
        <div class="ui-block ui-block-fleft w240">
			 <div class="ui-block-content ui-block-content-lb">
				<div class="tree-control">
					<input type="text" id="key" loxiatype="input" placeholder="<spring:message code="shop.property.keyword"/>" />
					<div><span id="search_result"></span></div>
					
				</div>
				<ul id="tree" class="ztree"></ul>

			</div>
		</div>
    
     <div class="ui-block ml240" style="padding-left: 10px;">
         <div class="ui-block-title1"><spring:message code="navigation.list.select"/></div>
         <div class="ui-block-content border-grey nav-update" style="margin-bottom: 10px;">
         
            
            
            <c:if test="${i18nOnOff == true}">
	         	<c:forEach items="${i18nLangs}" var="i18nLang">
		         	<div class="ui-block-line">
			            <label><spring:message code="navigation.list.name"/></label>
			            <input type="text" id="tree_name_zh_cn" mandatory="true" class="mutl-lang" lang="${i18nLang.key}" loxiatype="input" placeholder="<spring:message code="navigation.list.name"/>" />
			            <span>${i18nLang.value}</span>
		        	</div>
		     	</c:forEach>
	         </c:if>
	         <c:if test="${i18nOnOff == false}">
	          	<div class="ui-block-line">
	              <label>
	                <spring:message code="navigation.list.name"/>
	              </label>
	                <input type="text" loxiaType="input" mandatory="true" value="" name="tree_name_zh_cn" id="tree_name_zh_cn" placeholder="<spring:message code="navigation.list.name"/>" />
	            </div>
	         </c:if>
            
            
            
            <div class="ui-block-line">
              <label>
                <spring:message code="navigation.list.type"/>
              </label>
              <opt:select id="update-type" name="update-type" loxiaType="select" expression="chooseOption.NAVIGATION_TYPE" />
              <a href="#" class="func-button persist" id="toItemSort">进入排序</a>
            </div>
            <div class="ui-block-line">
              <label>
                <spring:message code="navigation.list.url"/>
              </label>
                <input type="text" loxiaType="input" value="" name="update-url" id="update-url" placeholder="<spring:message code="navigation.list.url"/>" />
            </div>
            <div class="ui-block-line navi-param">
              <label>
                <spring:message code="navigation.list.parameter"/>
              </label>
                <input type="text" disabled="disabled" loxiaType="input" value="" name="update-parameter" id="update-parameter" placeholder="<spring:message code="item.list.catagory"/>" />
                <a href="javascript:void(0);" class="func-button persist select-category" title="update">选择分类与属性</a>
            </div>
            <div class="ui-block-line">
              <label>
                <spring:message code="navigation.list.status"/>
              </label>
              <opt:select id="update-status" name="update-status" loxiaType="select" expression="chooseOption.NAVIGATION_LIFECYCLE" />
            </div>
            <div class="ui-block-line">
              <label>
                <spring:message code="navigation.list.newWindow"/>
              </label>
              <label>
                <input type="checkbox" name="update-newWindow" id="update-newWindow" />
               </label>
            </div>
           
           
            <c:if test="${i18nOnOff == true}">
	         	<c:forEach items="${i18nLangs}" var="i18nLang">
		         	<div class="ui-block-line">
			            <label>seo搜索标题</label>
			            <input type="text" style='width: 600px'  class="seoTitle" lang="${i18nLang.key}" loxiatype="input"  />
			            <span>${i18nLang.value}</span>
		        	</div>
		     	</c:forEach>
	         </c:if>
           
            <c:if test="${i18nOnOff == true}">
	         	<c:forEach items="${i18nLangs}" var="i18nLang">
		         	<div class="ui-block-line">
			            <label>seo搜索关键字</label>
			            <input type="text" style='width: 600px'  class="seoKeyWords" lang="${i18nLang.key}" loxiatype="input"  />
			            <span>${i18nLang.value}</span>
		        	</div>
		     	</c:forEach>
	         </c:if>           

            <c:if test="${i18nOnOff == true}">
	         	<c:forEach items="${i18nLangs}" var="i18nLang">
		         	<div class="ui-block-line">
			            <label>seo搜索描述</label>
			             <textarea rows="10px" class="seoDescription" loxiaType="input"  lang="${i18nLang.key}"  style="width: 600px;">seo搜索描述</textarea>
			            <span>${i18nLang.value}</span>
		        	</div>
		     	</c:forEach>
	         </c:if>   

            <c:if test="${i18nOnOff == true}">
	         	<c:forEach items="${i18nLangs}" var="i18nLang">
		         	<div class="ui-block-line">
			            <label>Extention</label>
			             <textarea rows="10px"  class="seoExtntion" loxiaType="input" style="width: 600px;" lang="${i18nLang.key}" >Extention</textarea>
			            <span>${i18nLang.value}</span>
		        	</div>
		     	</c:forEach>
	         </c:if>   

      
         <div class="button-line1">
              <a href="javascript:void(0);" class="func-button persist" id="save_father_Name"><spring:message code="btn.save"/></a> 
	 		  <a href="javascript:void(0);" class="func-button delete" id="remove_element"><spring:message code="btn.delete"/></a>
         </div>
     </div>
        
        
        <div class="ui-block-title1"><spring:message code="navigation.list.new.title"/></div>
        <div class="ui-block-content border-grey nav-add">
        
            <c:if test="${i18nOnOff == true}">
	         	<c:forEach items="${i18nLangs}" var="i18nLang">
		         	<div class="ui-block-line">
			            <label><spring:message code="navigation.list.name"/></label>
			            <input type="text" id="add_name_zh_cn" mandatory="true" class="mutl-lang" lang="${i18nLang.key}" loxiatype="input" placeholder="<spring:message code="navigation.list.name"/>" />
			            <span>${i18nLang.value}</span>
		        	</div>
		     	</c:forEach>
	         </c:if>
	         <c:if test="${i18nOnOff == false}">
	          	<div class="ui-block-line">
	              <label>
	                <spring:message code="navigation.list.name"/>
	              </label>
	                <input type="text" loxiaType="input" mandatory="true" value="" name="add_name_zh_cn" id="add_name_zh_cn" placeholder="<spring:message code="navigation.list.name"/>" />
	            </div>
	         </c:if>
            <div class="ui-block-line">
              <label>
                <spring:message code="navigation.list.type"/>
              </label>
              <opt:select id="add-type" name="add-type" loxiaType="select" expression="chooseOption.NAVIGATION_TYPE" />
            </div>
            <div class="ui-block-line">
              <label>
                <spring:message code="navigation.list.url"/>
              </label>
                <input type="text"  loxiaType="input" value="" name="add-url" id="add-url" placeholder="<spring:message code="navigation.list.url"/>" />
            </div>
            <div class="ui-block-line navi-param">
              <label>
                <spring:message code="navigation.list.parameter"/>
              </label>
                <input type="text" disabled="disabled" loxiaType="input" value="" name="add-parameter" id="add-parameter" placeholder="<spring:message code="item.list.catagory"/>" />
                <a href="javascript:void(0);" class="func-button persist select-category" title="add"><spring:message code="navigation.list.category"/></a>
            </div>
            <div class="ui-block-line">
              <label>
                <spring:message code="navigation.list.newWindow"/>
              </label>
              <label>
                <input type="checkbox" name="add-newWindow" id="add-newWindow" />
               </label>
            </div>
            
            <c:if test="${i18nOnOff == true}">
	         	<c:forEach items="${i18nLangs}" var="i18nLang">
		         	<div class="ui-block-line">
			            <label>seo搜索标题</label>
			            <input type="text" style='width: 600px'  class="seoTitle" lang="${i18nLang.key}" loxiatype="input"  />
			            <span>${i18nLang.value}</span>
		        	</div>
		     	</c:forEach>
	         </c:if>
           
            <c:if test="${i18nOnOff == true}">
	         	<c:forEach items="${i18nLangs}" var="i18nLang">
		         	<div class="ui-block-line">
			            <label>seo搜索关键字</label>
			            <input type="text" style='width: 600px'  class="seoKeyWords" lang="${i18nLang.key}" loxiatype="input"  />
			            <span>${i18nLang.value}</span>
		        	</div>
		     	</c:forEach>
	         </c:if>           

            <c:if test="${i18nOnOff == true}">
	         	<c:forEach items="${i18nLangs}" var="i18nLang">
		         	<div class="ui-block-line">
			            <label>seo搜索描述</label>
			             <textarea rows="10px" class="seoDescription" loxiaType="input"  lang="${i18nLang.key}"  style="width: 600px;">seo搜索描述</textarea>
			            <span>${i18nLang.value}</span>
		        	</div>
		     	</c:forEach>
	         </c:if>   

            <c:if test="${i18nOnOff == true}">
	         	<c:forEach items="${i18nLangs}" var="i18nLang">
		         	<div class="ui-block-line">
			            <label>Extention</label>
			             <textarea rows="10px"  class="seoExtntion" loxiaType="input" style="width: 600px;" lang="${i18nLang.key}" >Extention</textarea>
			            <span>${i18nLang.value}</span>
		        	</div>
		     	</c:forEach>
	         </c:if>   
            

            <div class="button-line1">
					<a href="javascript:void(0);" class="func-button persist" id="addLeaf"><spring:message code="btn.save"/></a>
		   </div>
            
        </div>
    </div>
</div>
<div id="categoryMenuContent" class="menuContent" style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
	<div class="ui-block-title1">选择分类</div>
	<ul id="categoryDemo" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
	
	
	<div>
		<div class="ui-block-title1">选择属性</div>
		<div id="propertiesDiv">
		<c:forEach items="${dynamicPropertyCommand}" var="command">
			<div class="propertyDiv" style="padding-bottom:10px;">
				<span>${command.property.name }</span>
				<c:forEach items="${command.propertyValueList}" var="propertyValue">
					<input type="checkbox" value="${propertyValue.id}" lang="${command.property.id}" class="propertySelectEvent">
					<label>${propertyValue.value}</label>
				</c:forEach>
			</div>
		</c:forEach>
	</div>
	
	<div style="text-align:center;">
	<button id="selectCategoryBtn">确定</button>
	<br>
	</div>
	<input type="hidden" value="${navigationId}" id="navigationId">
	
</div>
</body>
</html>
 
