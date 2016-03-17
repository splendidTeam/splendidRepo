<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="item.update.manage" /></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/product/item/recommand-item-update.js"></script>
<%-- <script type="text/javascript" src="${base}/scripts/search-filter.js"></script> --%>
<script type="text/javascript"> 
var category_ZNodes  = [
                		{id:0, name:"ROOT",state:"0", open:true,root:"true",nocheck:true},
                        <c:forEach var="category" items="${categoryList}" varStatus="status">
                           {id:${category.id}, pId:${category.parentId}, 
                           		name:"${category.name}",
                           		code:"${category.code}", sortNo:${category.sortNo}, 
                   		        <c:if test="${not empty recommandItemCommand.type && recommandItemCommand.type == 2 && category.id == recommandItemCommand.param}">
                   		            checked:true,
                   		        </c:if>
                           		drag:false, open:false,
                           		lifecycle:${category.lifecycle} } 
                           	<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                       ];
</script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1">
		<img src="${base}/images/wmi/blacks/32x32/cube.png">${ not empty isUpdate?"修改推荐":"新增推荐" }
	</div>
	<form name="recommandItemForm" action="/recommand/saveRecommandItem.json" method="post">
		<div class="ui-block">
	        <div class="ui-block-line ">
         		<label ><spring:message code="item.update.code"/></label>
	            <div>
	           		<input type="text" class="fLeft itemCode"  name="itemCodes" flag="0" loxiaType="input" value="${ recommandItemCommand.code }" mandatory="true" placeholder="<spring:message code='item.update.code'/>"/>
	           		<input type="hidden" class="fLeft" name="id" value="${ recommandItemCommand.id }"/>
	         	</div>
	         	<div id="loxiaTip-r" class="loxiaTip-r" style="display:none">
			 		<div class="arrow"></div>
			    	<div class="inner ui-corner-all codetip" style="padding: .3em .7em; width: auto;"></div>
		     	</div>
		   </div>
	    </div>
		<div class="ui-block">
	        <div class="ui-block-line ">
         		<label >推荐类型</label>
	            <div>
	           		<opt:select name="type" expression="chooseOption.RECOMMAND_TYPE" defaultValue='${ not empty recommandItemCommand.type?recommandItemCommand.type:"1" }' otherProperties="loxiaType=\"select\" "/>
	         	</div>
	         	<div id="loxiaTip-r" class="loxiaTip-r" style="display:none">
			 	<div class="arrow"></div>
			    	<div class="inner ui-corner-all codetip" style="padding: .3em .7em; width: auto;"></div>
		     	</div>
		   </div>
	    </div>
		<div class="ui-block">
	        <div class="ui-block-line ">
         		<label >推荐参数</label>
	            <div id="rec_param" ${ not(recommandItemCommand.type == '1' || empty recommandItemCommand.type)?"style='display: none;'":"" }>
					<opt:select name="param" expression="chooseOption.RECOMMAND_PARAM" defaultValue="${ recommandItemCommand.param }" otherProperties="loxiaType=\"select\" "/>
				</div>
				<div id="rec_category" ${ recommandItemCommand.type != '2'?"style='display: none;'":"" }>
					<input name="" loxiaType="input" placeholder="商品分类" id="categoryName" value="${ recommandItemCommand.categoryName }" />
					<input name="categoryId" id="categoryId" type="hidden" value="${ recommandItemCommand.param }"/>
				</div>
				<div id="rec_item" ${ recommandItemCommand.type != '3'?"style='display: none;'":"" }>
					<input name="paramCode" flag="1" class="itemCode" loxiaType="input" placeholder="商品编码" value="${ recommandItemCommand.itemCode }"  />
				</div>
				<div id="loxiaTip-c" class="loxiaTip-r" style="display:none; margin:-25px 0 0 280px; height: 50px">
		 		<div class="arrow"></div>
			    	<div class="inner ui-corner-all codetip" style="padding: .3em .7em; width: auto;"></div>
		     	</div>
	         	
		   </div>
	    </div>
	    <div class="ui-block">
	        <div class="ui-block-line ">
         		<label>排序</label>
	            <div>
	           		<input type="text" class="fLeft"  name="sort" loxiaType="number" value="${ recommandItemCommand.sort }" mandatory="true" placeholder=""/>
	         	</div>
		   </div>
	    </div>
		<div class="button-line">
			<input type="button" value="<spring:message code='btn.save'/>" class="button orange submit" title="<spring:message code='btn.save'/>"/>
			<input type="button" value="<spring:message code='btn.return'/>" class="button return"  title="<spring:message code='btn.return'/>" />
		</div>
	</form>    
</div>
	<div id="categoryMenuContent" class="menuContent" style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
		<ul id="categoryDemo" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
	</div>
</body>
</html>
