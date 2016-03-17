<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>

<script type="text/javascript" src="${base }/scripts/main.js"></script>
<script type="text/javascript" src="${base}/scripts/auth/role/create.js"></script>
</head>

<body>

<div class="content-box">
    
    <form name="roleForm" action="/role/save.json" method="post">
    <input name="id" value="${roleCommand.id }" type="hidden"/>
	<div class="ui-title1">
		<img src="${base}/images/wmi/blacks/32x32/user.png"><spring:message code="role.list.label.title"/>
	</div>
	<div class="ui-block">
		<div class="ui-block-title1">
			<c:if test="${isupdate=='false'}">
				<spring:message code="role.list.role.new" />
				<input type="hidden" id="isUpdate" name="isUpdate" value="0">
			</c:if>
			<c:if test="${isupdate=='true'}">
				<spring:message code="role.list.role.update" />
				<input type="hidden" id="isUpdate" name="isUpdate" value="1">
			</c:if>
		</div>


		
		<div class="">
		    
		    <div class="ui-block-line">
		         <label><spring:message code="role.list.role.short.name" /></label>
		         <div class="wl-right">
		              <input name="name" id="roleName" type="text" loxiaType="input" value="${roleCommand.name }" mandatory="true" placeholder="<spring:message code="role.list.role.short.name" />"/>
		         </div>
		    </div>
		    
		    
		     <div class="ui-block-line">
		         <label><spring:message code="role.list.dept.type" /></label>
		         <div class="wl-right ">    
		    		<opt:select name="orgTypeId" cssClass="ui-loxia-default" defaultValue="${roleCommand.orgTypeId }" expression="commonOption.orgType"/>
		         </div>
		    </div>
		    <div class="ui-block-line">
		         <label><spring:message code="role.list.label.status" /></label>
		         <div class="wl-right">    
		    			<opt:select name="lifecycle" cssClass="ui-loxia-default" defaultValue="${roleCommand.lifecycle }" expression="chooseOption.IS_AVAILABLE"/>
		    			
		         </div>
		    </div>
    
    
		    <div class="ui-block-line"  id="org_role">
		         <label><spring:message code="role.list.label.privilege" /></label>
		         
		         <div class="wl-right">
			         <c:choose>
					     <c:when test="${not empty roleCommand.privileges}">
					           <c:forEach var="longMap" items="${privilegeMap}">
						            <div class="wl-right priDiv" id="pri_${longMap.key}">
						                <c:forEach var="strMap" items="${longMap.value}">
						                     <span class="children-store-h" >${strMap.key}</span>
						                     <c:forEach var="pri" items="${strMap.value}">
						                          <span class="children-store" title="${pri.description}">
						                          <input type="checkbox" 
							                           <c:forEach var="map" items="${roleCommand.privileges}">
							                               <c:if test="${map.key==pri.id}">
																checked="checked"
															</c:if>
							                           </c:forEach>
						                           title="${pri.description}"
						                           value="${pri.id}"
						                           name="privilegeIds"
						                          />${pri.name}</span>
						                     </c:forEach>
						                     <div class="clear-line"></div>
						                </c:forEach>
									</div>
								</c:forEach>
						 </c:when>
						 <c:otherwise>
					           <c:forEach var="longMap" items="${privilegeMap}">
						            <div class="wl-right priDiv" id="pri_${longMap.key}">
						                <c:forEach var="strMap" items="${longMap.value}">
						                     <span class="children-store-h" >${strMap.key}</span>
						                     <c:forEach var="pri" items="${strMap.value}">
						                          <span class="children-store"  title="${pri.description}" >
						                          <input type="checkbox" 
						                           value="${pri.id}"
						                           name="privilegeIds"
						                          />${pri.name}</span>
						                     </c:forEach>
						                     <div class="clear-line"></div>
						                </c:forEach>
									</div>
								</c:forEach>
						 </c:otherwise>
					 </c:choose>

			     </div>
    		</div>
		</div>
	</div>
    </form>
    
    <div class="button-line">
         <input type="button" value="<spring:message code="btn.save" />" class="button orange saveForm" title="<spring:message code="btn.save" />"/>
         <input type="button" value="<spring:message code="btn.return" />" class="button return" title="<spring:message code="btn.return" />"/>
    </div>
</div>


</body>
</html>
