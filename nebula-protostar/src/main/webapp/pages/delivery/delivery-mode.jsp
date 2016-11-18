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
<script type="text/javascript" src="${base}/scripts/delivery/delivery-mode.js"></script>

<SCRIPT type="text/javascript">	
//只展开ROOT
var zNodes = [
			  { id:0,pId:null, name:"ROOT",code:"ROOT",lifecycle:"1", open:true,sortNo:0,drag:true},   
     	      <c:forEach var="category" items="${areaList}" varStatus="status">
     	     	<c:set var="isRoot" value="${null eq category.parentId}"></c:set>
     	     	{id:${category.id}, pId:${isRoot?0:category.parentId}, name:"${category.area}", code:"${category.code}", sortNo:${category.sortNo}, drag:true, open:false, lifecycle:${category.status} } 
     	     	<c:if test="${!status.last}">,</c:if>
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
	<div></div>
    <div class="content-box width-percent100">
       
	       <div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/tag.png"><spring:message code="delivery.area"/></div>
           <div class="ui-block ui-block-fleft w240">
                    <div class="ui-block-content ui-block-content-lb">
                        <div class="tree-control">
                            <input type="text" id="key" loxiatype="input" placeholder="<spring:message code="shop.property.keyword"/>" />
                            <div><span id="search_result"></span></div>
                        </div>
                            <ul id="categoryTree" class="ztree"></ul>

                    </div>
            </div>
        
            <div class="ui-block ml240 cate-update" style="padding-left: 10px;" id="cate-update">
	            <div class="ui-block-title1"><spring:message code="delivery.area.logisticsCode"/></div>
		           <div id="currentNodeDiv" class="ui-block-content border-grey" style="margin-bottom: 10px;">
		           	<input  id="modeId" type="hidden"/> 
		           	<input id="areaId" type="hidden"/>
			          <div class="ui-block-line">
				         <label><spring:message code="delivery.area.logisticsCode"/></label>
				           <input type="text" id="logisticsCode" loxiatype="input" placeholder="<spring:message code="delivery.area.logisticsCode"/>"  />
			         </div>
			         <c:if test="${i18nOnOff == true}">
			          	<c:forEach items="${i18nLangs}" var="i18nLang">
				         <div class="ui-block-line">
					            <label><spring:message code="delivery.area.logisticsCompany"/></label>
					            <input type="text" id="logisticsCompany" class="mutl-lang" lang="${i18nLang.key}" loxiatype="input" placeholder="<spring:message code="delivery.area.logisticsCompany"/>" />
					            <span>${i18nLang.value}</span>
				        </div>
				        </c:forEach>
			        </c:if>
			         <c:if test="${i18nOnOff == false}">
			          <div class="ui-block-line">
				           <label><spring:message code="delivery.area.logisticsCompany"/></label>
				            <input type="text" id="logisticsCompany"  loxiatype="input" placeholder="<spring:message code="delivery.area.logisticsCompany"/>" />
			        </div>
			         </c:if>
			        
			             <div class="ui-block-line">
             			 <label>
              				 <spring:message code="delivery.area.supportcod"/>
             				 </label>
             				 <opt:select id="supportcod" name="supportcod" loxiaType="select" expression="chooseOption.TRUE_OR_FALSE" />
           			  </div>
			        
			        
			           <div class="ui-block-line">
             			 <label>
              				 <spring:message code="delivery.area.commonDelivery"/>
             				 </label>
             				 <opt:select id="commonDelivery" name="commonDelivery" loxiaType="select" expression="chooseOption.TRUE_OR_FALSE" />
           			  </div>
           			  
			          <div class="ui-block-line">
				         <label><spring:message code="delivery.area.commonDeliveryStartTime"/></label>
				         <input type="text" id="commomStartTime" loxiatype="input" placeholder="<spring:message code="delivery.area.commonDeliveryStartTime"/>"  />
				         <span><spring:message code="delivery.area.timeformat"/></span>  
			         </div>
			         
			          <div class="ui-block-line">
				         <label><spring:message code="delivery.area.commonDeliveryEndTime"/></label>
				         <input type="text" id="commomEndTime" loxiatype="input" placeholder="<spring:message code="delivery.area.commonDeliveryEndTime"/>"  />
				         <span><spring:message code="delivery.area.timeformat"/></span>
			         </div>
			         
			          <div class="ui-block-line">
             			 <label>
              				 <spring:message code="delivery.area.firstDayDelivery"/>
             				 </label>
             				 <opt:select id="firstDayDelivery" name="firstDayDelivery" loxiaType="select" expression="chooseOption.TRUE_OR_FALSE" />
           			  </div>
           			  
			          <div class="ui-block-line">
				         <label><spring:message code="delivery.area.firstDeliveryStartTime"/></label>
				         <input type="text" id="firstStartTime" loxiatype="input" placeholder="<spring:message code="delivery.area.firstDeliveryStartTime"/>"  />
				         <span><spring:message code="delivery.area.timeformat"/></span>  
			         </div>
			          <div class="ui-block-line">
				         <label><spring:message code="delivery.area.firstDeliveryEndTime"/></label>
				         <input type="text" id="firstEndTime" loxiatype="input" placeholder="<spring:message code="delivery.area.firstDeliveryEndTime"/>"  />
				         <span><spring:message code="delivery.area.timeformat"/></span>  
			         </div>
			       
			          <div class="ui-block-line">
             			 <label>
              				 <spring:message code="delivery.area.secondDayDelivery"/>
             				 </label>
             				 <opt:select id="secondDayDelivery" name="secondDayDelivery" loxiaType="select" expression="chooseOption.TRUE_OR_FALSE" />
           			  </div>
           			 
			          <div class="ui-block-line">
				         <label><spring:message code="delivery.area.secondDeliveryStartTime"/></label>
				         <input type="text" id="secondStartTime" loxiatype="input" placeholder="<spring:message code="delivery.area.secondDeliveryStartTime"/>"  />
				         <span><spring:message code="delivery.area.timeformat"/></span>  
			         </div>
			         
			         <div class="ui-block-line">
				         <label><spring:message code="delivery.area.secondDeliveryEndTime"/></label>
				         <input type="text" id="secondEndTime" loxiatype="input" placeholder="<spring:message code="delivery.area.secondDeliveryEndTime"/>"  />
				         <span><spring:message code="delivery.area.timeformat"/></span>  
			         </div>
			         
			          <div class="ui-block-line">
             			 <label>
              				 <spring:message code="delivery.area.thirdDayDelivery"/>
             				 </label>
             				 <opt:select id="thirdDayDelivery" name="thirdDayDelivery" loxiaType="select" expression="chooseOption.TRUE_OR_FALSE" />
           			  </div>
           			 
			          <div class="ui-block-line">
				         <label><spring:message code="delivery.area.thirdDeliveryStartTime"/></label>
				         <input type="text" id="thirdDeliveryStartTime" loxiatype="input" placeholder="<spring:message code="delivery.area.thirdDeliveryStartTime"/>"  />
				         <span><spring:message code="delivery.area.timeformat"/></span>  
			         </div>
			         
			         <div class="ui-block-line">
				         <label><spring:message code="delivery.area.thirdDeliveryEndTime"/></label>
				         <input type="text" id="thirdDeliveryEndTime" loxiatype="input" placeholder="<spring:message code="delivery.area.thirdDeliveryEndTime"/>"  />
				         <span><spring:message code="delivery.area.timeformat"/></span>  
			         </div>			         
			         
			         <div class="ui-block-line">
				         <label><spring:message code="delivery.area.remark"/></label>
				           <input type="text" id="remark" loxiatype="input" placeholder="<spring:message code="delivery.area.remark"/>"  />
			         </div>
			         
			        <div class="button-line1">
			               <a href="javascript:void(0);" class="func-button persist" id="save_node" title="<spring:message code="btn.save"/>"><spring:message code="btn.save"/></a>
                           <a href="javascript:void(0);" class="func-button delete" id="delete_node" title="<spring:message code="btn.delete"/>"><spring:message code="btn.delete"/></a>
			        </div>
				</div>
	         </div>
           
     
            <div class="ui-block ml240 cate-add" style="padding-left: 10px;">
	            <div class="ui-block-title1"><spring:message code="area.property.lable.category"/></div>
		           <div id="currentNodeDiv" class="ui-block-content border-grey" style="margin-bottom: 10px;">
			          <div class="ui-block-line">
				         <label><spring:message code="delivery.area.code"/></label>
				           <input type="text" id="tree_code" loxiatype="input" placeholder="<spring:message code="delivery.area.code"/>"  />
			         </div>
			         <c:if test="${i18nOnOff == true}">
			          	<c:forEach items="${i18nLangs}" var="i18nLang">
				         <div class="ui-block-line">
					            <label><spring:message code="delivery.area.name"/></label>
					            <input type="text" id="tree_name_zh_cn" class="mutl-lang" lang="${i18nLang.key}" loxiatype="input" placeholder="<spring:message code="delivery.area.name"/>" />
					            <span>${i18nLang.value}</span>
				        </div>
				        </c:forEach>
			        </c:if>
			         <c:if test="${i18nOnOff == false}">
			          <div class="ui-block-line">
				           <label><spring:message code="delivery.area.name"/></label>
				            <input type="text" id="tree_name_zh_cn"  loxiatype="input" placeholder="<spring:message code="delivery.area.name"/>" />
			        </div>
			          </c:if>
			        <div class="button-line1">
			               <a href="javascript:void(0);" class="func-button persist" id="save_area" title="<spring:message code="btn.save"/>"><spring:message code="btn.save"/></a>
			                <a href="javascript:void(0);" class="func-button delete" id="remove_area" title="<spring:message code="btn.delete"/>"><spring:message code="btn.delete"/></a>
			        </div>

	               </div>
	         </div>
             
             
             <div class="ui-block ml240 area-insert" style="padding-left: 10px;">
                  <div class="ui-block-title1"><spring:message code="area.property.lable.insert"/></div>
		          <div id="separateDiv" class="ui-block-content border-grey" style="margin-bottom: 10px;">
		                <div class="ui-block-line">
				            <label><spring:message code="delivery.area.code"/></label>
				            <input type="text" id="add_code" loxiatype="input" placeholder="<spring:message code="delivery.area.code"/>"  />
			            </div>
			            <c:if test="${i18nOnOff == true}">
				            <c:forEach items="${i18nLangs}" var="i18nLang">
				            <div class="ui-block-line">
					            <label><spring:message code="delivery.area.name"/></label>
					            <input type="text" id="add_name_zh_cn"  class="mutl-lang" lang="${i18nLang.key}" loxiatype="input" placeholder="<spring:message code="delivery.area.name"/>" />
				            	<span>${i18nLang.value}</span>
				            </div>
				            </c:forEach>
			            </c:if>
			            <c:if test="${i18nOnOff == false}">
			               <div class="ui-block-line">
				            <label><spring:message code="delivery.area.name"/></label>
				            <input type="text" id="add_name_zh_cn" loxiatype="input" placeholder="<spring:message code="delivery.area.name"/>" />
			               </div>
			            </c:if>
                        <div class="button-line1">
			               <a  href="javascript:void(0);" class="func-button insert" id="insertSibling" title="" />
			                  <spring:message code="product.property.lable.add"/>
			               </a>
			  
                           <a  href="javascript:void(0);" class="func-button insertLeaf" id="addLeaf" title="" />
                              <spring:message code="product.property.lable.addchild"/>
                           </a>
                         </div>
                      </div>
		     </div>
		     <div class='error-information' style="display:none;"><h5></h5><p></p></div>

      </div>
</body>
</html>
