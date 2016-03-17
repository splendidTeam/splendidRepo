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
<script type="text/javascript" src="${base}/scripts/product/industry/industry.js"></script>
<SCRIPT type="text/javascript">	
//<spring:message code="industry.list.expand"/>ROOT
var zNodes =[           
	{ id:0, name:"ROOT",state:"1", open:true,root:"true"},
	<c:forEach var="industry" items="${industryList}" varStatus="status">
	{id:${industry.id}, pId:${null eq industry.parentId?0:industry.parentId}, name: "${industry.name}", state:"${industry.lifecycle}",open:${0 eq industry.parentId?true:false}}<c:if test="${!status.last}">,</c:if>
	</c:forEach>
	
];
</SCRIPT>

</head>
<body>

    <div class="content-box width-percent100">
    
       <div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/wrench_plus_2.png">
           <spring:message code="industry.list.manager"/>
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
         <div class="ui-block-title1"><spring:message code="industry.list.select"/></div>
            <div class="ui-block-content border-grey" style="margin-bottom: 10px;">
            
               <div class="ui-block-line">
                 <label>
                   <spring:message code="shop.update.industryname"/>
                 </label>
                       <input type="text" loxiaType="input" mandatory="true" value="" name="tree_fid" id="tree_fid" placeholder="<spring:message code="shop.update.industryname"/>" />
               </div>
                
                
              <div class="ui-block-line">
                  <label>
                    <spring:message code="user.list.filter.state"/>
                  </label>
                    <select id="tree_state" name="lifecycle" loxiaType="select">
                             <option value="1"><spring:message code="role.list.label.enable"/></option>
					         <option value="0"><spring:message code="role.list.label.disable"/></option>
					</select>
              </div>
              
              
            <div class="button-line1">
                 <a href="javascript:void(0);" class="func-button persist" id="save_father_Name"><spring:message code="btn.save"/></a> 
				 <a href="javascript:void(0);" class="func-button delete" id="remove_element"><spring:message code="btn.delete"/></a>
            </div>
        </div>
        
        
        <div class="ui-block-title1"><spring:message code="industry.list.newindustry"/></div>
        <div class="ui-block-content border-grey">
        
            <div class="ui-block-line">
                       <label><spring:message code="shop.update.industryname"/></label>
                       
                       <input type="text" loxiaType="input" mandatory="true" name="add_name" value="" id="add_name" placeholder="<spring:message code="shop.update.industryname"/>" />

             </div>
             
             
          
            
            
            <div class="button-line1">
					<a href="javascript:void(0);" class="func-button persist" id="addLeaf"><spring:message code="product.property.lable.childindustry"/></a>
		   </div>
            
            
            
        </div>
    </div>
</div>
</body>
</html>
 
