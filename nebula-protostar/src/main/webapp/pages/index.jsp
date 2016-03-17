<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<title>${title}</title>

<script type="text/javascript">

menus = <c:out value="${menus}" default="[]" escapeXml="false"></c:out>;

</script>
<script type="text/javascript" src="${base}/scripts/jquery/jqueryplugin/jquery.cookie.js"></script>
<script type="text/javascript" src="${base}/scripts/index.js"></script>
</head>

<body style="overflow-y:hidden; background:#f5f5f5;">
	 <header>
     		<div class="head-inform">
            	 <span>P</span>rotostar
            </div>
            
            
            
               <div class="login-inform">
            	 
            	 <div class="login-name-inform">
                      <span class="head-dia-cur head-cust-inform">
                      		<span>${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.realName}</span>
                      </span>
                      
                      <span class="head-dia-cur head-store">
                          	<span>
								<c:forEach var="item" items="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.grantedOrgnizations}" varStatus="status"> 
									<c:choose>
										<c:when test="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.currentOrganizationId eq item.id}">
											 ${item.name}
											
										</c:when>
									</c:choose>
	                          	</c:forEach> 
							</span>
                      </span>
                      
                     
                      <span class="head-dia-cur head-end"></span>
                      <span class="head-dia-cur head-update"></span>
                      
                      
                 </div>
                 
                 <div class="search-text"><input type="text"/><a href="javascript:void(0);"></a></div>
                 
                 
                 <a href="javascript:void(0);" class="head-control-word head-control-refresh"><spring:message code="refresh" /></a>
                 <a href="javascript:void(0);" class="head-control-word head-control-print"><spring:message code="print" /></a>
                 <a href="javascript:void(0);" class="head-control-word head-control-info"><spring:message code="info" /></a>
                   <a href="javascript:void(0)" class="head-control-word lang-setting">中文</a>
                 
				 <div class="head-right-control-dialog">
				 	  <input type="button" class="button orange updateuser" value="修改信息"/>
				 </div>
				 
				 <div class="head-right-control-dialog" style="height:auto;">
                      <ul class="store-list">

                           <c:forEach var="item" items="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.grantedOrgnizations}" varStatus="status"> 
							<c:choose>
								<c:when test="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.currentOrganizationId eq item.id}">
									 <li><a href="changeOrg.htm?orgId=${item.id}" style="color:red">${item.name}</a></li>
								</c:when>
								<c:otherwise>
									<li><a href="changeOrg.htm?orgId=${item.id}">${item.name}</a></li>
								</c:otherwise>
							</c:choose>
                          	</c:forEach> 
                         
                      </ul>
				 </div>
				 
				 <div class="head-right-control-dialog">
				 	  <p> <spring:message code="logout" /></p>
				 </div>
				 
				 <div class="head-right-control-dialog">
				 	  <p><spring:message code="modifyuserconfig" /></p>
				 </div>
				 
            </div>
            

            <dl id="main-menu">
               
            </dl>
     </header>
     
     
     
     <div class="content">
     <iframe src="dashboard.htm" class="table-if"></iframe>
     </div>
     
	 <!-- 选择语言窗口 -->   
	 <div id="dialog-lang-select" class="proto-dialog">
			<h5>设置语言</h5>
			<div class="proto-dialog-content p10">
				<div style="margin-left: 18%">
		       		<opt:select  loxiaType="select" cssClass="i18n-langs"  expression="chooseOption.I18N_LANGS" />
				</div>
			</div>
	        <div class="proto-dialog-button-line">
				<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange lang-btn-ok" /> 
	        </div>
	</div>
</body>
</html>
