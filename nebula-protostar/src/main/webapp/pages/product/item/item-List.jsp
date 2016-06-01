<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="item.update.manage"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/product/item/item-list.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
<script type="text/javascript">
var categoryDisplayMode = "${categoryDisplayMode}";
var updateListTimeFlag = "${updateListTimeFlag}";
var category_ZNodes = [
		{id:0, pId:-1, name:"ROOT",
			  code:"ROOT", sortNo:1,
			  open:true, lifecycle:1},  
              <c:forEach var="category" items="${categoryList}" varStatus="status">
              	
              	{id:${category.id}, pId:${category.parentId}, name:"${category.name}", 
              		code:"${category.code}", sortNo:${category.sortNo}, 
              		open:false, lifecycle:${category.lifecycle}}
              	<c:if test="${!status.last}">,</c:if>
              </c:forEach>
         ];
var  industry_ZNodes = [
                        
               <c:forEach var="industry" items="${industrylist}" varStatus="status">
               	{id:${industry.id}, pId:${industry.parentId},lable:"${industry.id}", name: "${industry.name}",lifecycle:"${industry.lifecycle}",open:false}
               	<c:if test="${!status.last}">,</c:if>
               </c:forEach>
          ];
</script>

</head>
<body> 
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="item.update.manage"/>
         <input type="button"   value="<spring:message code='btn.all.delete'/>" class="button butch delete"  title="<spring:message code='btn.all.delete'/>"/>
         <input type="button" value="<spring:message code="item.list.mutiActive"/>" class="button butch activeBtn"   title="<spring:message code="item.list.mutiActive"/>"/>
         <input type="button" value="<spring:message code="btn.all.bottomcarriage"/>" class="button butch disable2"   title="<spring:message code="btn.all.bottomcarriage"/>"/>
         <input type="button" value="<spring:message code="btn.all.topcarriage"/>" class="button butch enable"   title="<spring:message code="btn.all.topcarriage"/>"/>
          
         <input type="button" value="<spring:message code='item.list.create'/>" class="button orange new"   title="<spring:message code='item.list.create'/>"/>
         <input type="button" value="<spring:message code="item.list.importItem"/>" class="button orange" onclick="javascript:location.href='/item/toImportSku.htm'"  title="<spring:message code="item.list.importItem"/>"/>
         <input type="button" value="<spring:message code="item.list.import.export"/>" class="button orange itemExportImport" title="<spring:message code="item.list.import.export"/>"/>
         <input type="button" value="<spring:message code="item.list.importImages"/>" class="button orange" onclick="javascript:location.href='/itemImage/toImportSkuImg.htm'"  title="<spring:message code="item.list.importImages"/>"/>
    </div>
    <form id="searchForm">
    <div class="ui-block">
            <div class="ui-block-content ui-block-content-lb">
                <table>
                <tr>
                        <td><label><spring:message code="item.code"/></label></td>
                        <td><span id="searchkeytext"> <input type="text"
								loxiaType="input" mandatory="false" id="code"
								name="q_sl_code" placeholder="<spring:message code='item.code'/>"></input>
						    </span>
						</td>
                        <td><label><spring:message code="item.name"/></label></td>
                       <td><span id="searchkeytext"><input type="text"
								loxiaType="input" mandatory="false" id="title"
								name="q_sl_title" placeholder="<spring:message code='item.name'/>">
						    </span></input>
						</td>
                        <td><label><spring:message code="role.list.label.status"/></label></td>
                        <td> <span id="searchkeytext"> <opt:select name="q_int_lifecycle" loxiaType="select" id="lifecycle" expression="chooseOption.ITEM_STATUS" nullOption="role.list.label.unlimit"  />
						</span> </td>
						<td ><label><spring:message code="product.property.lable.industry"/></label></td>

                       <td>
                       <input type="hidden"
								id="industryId" name="q_long_industryId" mandatory="true" /> <input 
								type="text" loxiaType="input" id="industryName"
								mandatory="false" placeholder="<spring:message code='shop.property.industry'/>"/>
						</td>
                       
                    </tr>
                    <tr>
                        <td><label><spring:message code="item.list.createTime"/></label></td>
                        <td>
                           <input type="text" id="createStartDate" name="q_date_createStartDate" loxiaType="date" mandatory="false"></input>
			            </td>
                        <td align="center"><label>——</label></td>
                         <td>
			            	<input type="text" name="q_date_createEndDate" id="createEndDate" loxiaType="date" mandatory="false"></input>
			            </td>


                   		 <td><label><spring:message code="item.list.catagory"/></label></td>	
	                     <td>
	                     <input type="hidden"
								id="categoryId" name="q_long_categoryId" mandatory="true" /> <input 
								type="text" loxiaType="input" id="categoryName"
								mandatory="false" placeholder="<spring:message code='item.list.catagory'/>"/>
						</td>
						<td>是否已上传图片</td>	
	                    <td>
	                    	<select loxiaType="select" name="q_string_imgCount">
	                    		<option value="">不限</option>
	                    		<option value="1">已上传</option>
	                    		<option value="2">末上传</option>
	                    	</select>
	                    </td>
                        
                    </tr>
                    
                    <tr>
                    
                    	<td><label><spring:message code="itemcategory.listtime"/></label></td>
                         <td>
                            <input type="text" id="listStartDate" name="q_date_listStartDate" loxiaType="date" mandatory="false"></input>
			            </td>
                        <td align="center"><label>——</label></td>

                         <td>
			            	<input type="text" name="q_date_listEndDate" id="listEndDate" loxiaType="date" mandatory="false"></input>
			            </td>
                         <td><label>商品类型</label></td>	
	                     <td>
	                     	<select loxiaType="select" name="q_int_type">
	                    		<option value="">不限</option>
	                    		<option value="1">主商品</option>
	                    		<option value="0">赠品</option>
	                    	</select>
	                     </td>
	                     <td></td>
	                     <td></td>
	                     <td></td>
	                     <td></td>
                      
                    </tr>
                    
                </table>
                <div class="button-line1">
                <a href="javascript:void(0);"
                   class="func-button search"><span><spring:message code="user.list.filter.btn"/></span></a>
                </div>
            </div>
 
   	 		
    		</div>
    </form>
    <div class="ui-block">
		<div id="table1" class="border-grey"
				caption="<spring:message code='item.list.itemList'/><span style='font-size:14px;margin-left: 30px;'><span class='ui-pyesno ui-pyesno-wait'></span><span class='item-list-table-status'>新建</span><span class='ui-pyesno ui-pyesno-yes' title='上架'></span><span class='item-list-table-status'>上架</span><span class='ui-pyesno ui-pyesno-no' title='下架'></span><span class='item-list-table-status'>下架</span></span>"></div>
    </div>
    <div class="button-line">
                
          <input type="button" value="<spring:message code='item.list.create'/>" class="button orange new"  title="<spring:message code='item.list.create'/>" />
          <input type="button" value="<spring:message code="btn.all.topcarriage"/>" class="button butch enable"   title="<spring:message code="btn.all.topcarriage"/>"/>
          <input type="button" value="<spring:message code="btn.all.bottomcarriage"/>" class="button butch disable2"   title="<spring:message code="btn.all.bottomcarriage"/>"/>
          <input type="button" value="<spring:message code="item.list.mutiActive"/>" class="button butch activeBtn"   title="<spring:message code="item.list.mutiActive"/>"/>
          <input type="button" value="<spring:message code="btn.all.delete"/>" class="button butch delete"   title="<spring:message code="btn.all.delete"/>"/>
    </div>

</div>

    <div id="categoryMenuContent" class="menuContent" style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
		<ul id="categoryDemo" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
	</div>

    <div id="industryMenuContent" class="menuContent" style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
		<ul id="industryDemo" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
	</div>

	<div id="active-dialog" class="proto-dialog">
		<h5><spring:message code="item.list.activeItem"/></h5>
		 <div class="proto-dialog-content">
			 <div class="ui-block-line">
					
					<label><spring:message code="item.list.activeBeginTime"/>：</label>
					<div> 
					<input id ="activeTime" loxiaType="date" mandatory="false" showtime="true" placeholder="<spring:message code="item.list.listTime"/>" value="${banner_publish_time}" name="publishTime" class="fLeft mt5"/>
					</div>
			 </div>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="<spring:message code="btn.confirm"/>" class="button orange copyok" id = "activeOkBtn"/>
		 	  
		 	  <input type="button" value="<spring:message code="btn.cancel"/>" class="button orange copycancel" id="activeCancleBtn"/>
		 </div>
		
	</div>

</body>
</html>
