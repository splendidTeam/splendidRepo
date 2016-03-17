<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<title>${title}</title>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/member/member-list.js"></script>
<script type="text/javascript" src="${base}/scripts/member/member-detail.js"></script>
<script type="text/javascript">
	var frontendBaseUrl = '<c:out value="${ frontendBaseUrl }" escapeXml="" default="[]"></c:out>';
	var customBaseUrl = '<c:out value="${ customBaseUrl }" escapeXml="" default="[]"></c:out>';
</script>
</head>
<body>

<div class="content-box width-percent100">
   
	<div class="ui-title1">
	<img src="${base}/images/wmi/blacks/32x32/user.png">
	<spring:message code="member.detail.info"/></div>
	<div class="ui-tag-change">
		 <div class="tag-change-content">
		 	<div style="float:left;">
		 		<a style='cursor:pointer;'><img src='${base}/images/main/portrait.gif' width=100 ></a>
		 	</div>
		 	<div  style="float:left;margin-left:10px;">
							
						    <div class="ui-block-line ui-block-line-37">
								<label> <spring:message code="member.detail.loginName"/></label>
								<label> ${memberPersonalDataCommand.loginName}</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label><spring:message code="member.list.email"/></label>
						        <label> ${memberPersonalDataCommand.loginEmail}</label>
						         <label> <a href="mailto:sss@aa.com"><spring:message code="member.detail.sendmail"/></a></label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label><spring:message code="member.list.mobile"/></label>
						        <label>${memberPersonalDataCommand.loginMobile}</label>
						         <label> <a href=""><spring:message code="member.detail.validateProvince"/></a></label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label> <spring:message code="member.group.source"/></label>
						      <label>${memberPersonalDataCommand.sourceName}</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label> <spring:message code="member.detail.lifecycle"/></label>
						       	<label>${memberPersonalDataCommand.lifeCycleName}</label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.group.type"/></label>
						      	<label>${memberPersonalDataCommand.typeName}</label>
						    </div>
		 	</div>
		 </div>
	
	</div>	
	
	<div class="ui-tag-change">
               <ul class="tag-change-ul">
                 
                   <li class="memberinfo" ><spring:message code="member.detail.personalInfo"/></li>
                    <li class="memberactioninfo"><spring:message code="member.detail.conduct"/></li>                    
                   <li class="memberaddress" ><spring:message code="member.detail.personalAddress"/></li>
                   <li class="memberfavorite"><spring:message code="member.detail.personalfavorite"/></li>
               </ul> 
               <div class="tag-change-content">
               
                   
                    <div class="tag-change-in"  >
							<div class="ui-block" style="">
						    <div class="ui-block-title1"><spring:message code="member.detail.personalInfo"/></div>
						   
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label> <spring:message code="member.detail.nickName"/></label>
						        <label> ${memberPersonalDataCommand.nickname}</label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label> <spring:message code="user.list.filter.realname"/>(<spring:message code="member.detail.local"/>)</label>
						        <label> ${memberPersonalDataCommand.localRealName}</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label><spring:message code="user.list.filter.realname"/>(<spring:message code="member.detail.international"/>)</label>
						      <label> ${memberPersonalDataCommand.intelRealName} </label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label> <spring:message code="member.detail.sex"/></label>
						       	<label> ${memberPersonalDataCommand.sexName}</label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.bloodType"/></label>
						      	<label>${memberPersonalDataCommand.bloodType}</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.birthday"/></label>
						      	<label>
						      	<fmt:formatDate value="${memberPersonalDataCommand.birthday}"  pattern="yyyy-MM-dd HH:mm:ss"/>
						      	</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.marrige"/></label>
						      	<label>${memberPersonalDataCommand.marriage}</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.country"/></label>
						      	<label>${memberPersonalDataCommand.country}</label>
						    </div>
							<%-- 						    
							<div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.province"/>/ <spring:message code="member.detail.mayor"/>/ <spring:message code="member.detail.county"/>/<spring:message code="member.detail.district"/></label>
						      	<label> ${memberPersonalDataCommand.province}/${memberPersonalDataCommand.city}/${memberPersonalDataCommand.area}</label>
						    </div> 
						    --%>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.address"/></label>
						      	<label> ${memberPersonalDataCommand.address}</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>   <spring:message code="member.detail.identityType"/></label>
						      	<label>${memberPersonalDataCommand.credentialsTypeName}</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.identityNo"/></label>
						      	<label>${memberPersonalDataCommand.credentialsNo}</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="user.modify.label.email"/></label>
						      	<label>${memberPersonalDataCommand.email}</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.mobile"/></label>
						      	<label>${memberPersonalDataCommand.short2}</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="user.list.filter.mobile"/></label>
						      	<label>${memberPersonalDataCommand.mobile}</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.qq"/></label>
						      	<label>${memberPersonalDataCommand.QQ}</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.weibo"/></label>
						      	<label>${memberPersonalDataCommand.weibo}</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.weixin"/></label>
						      	<label>${memberPersonalDataCommand.weixin}</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.degree"/></label>
						      	<label>${memberPersonalDataCommand.edu}</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="shop.property.industry"/></label>
						      	<label>${memberPersonalDataCommand.industy}</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.postition"/></label>
						      	<label>${memberPersonalDataCommand.position}</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>   <spring:message code="member.detail.salary"/></label>
						      	<label>${memberPersonalDataCommand.salary}</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>   <spring:message code="member.detail.workExperience"/></label>
						      	<label>${memberPersonalDataCommand.workingLife}</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.companyName"/></label>
						      	<label>${memberPersonalDataCommand.company}</label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.interesting"/></label>
						      	<label>${memberPersonalDataCommand.interest}</label>
						    </div>
						    

							</div>
                    </div>
                     <div class="tag-change-in"  >
							<div class="ui-block" style="">
						    <div class="ui-block-title1"><spring:message code="member.detail.conduct"/></div>
						    <div class="ui-block-line ui-block-line-37">
								<label> <spring:message code="member.detail.loginCount"/></label>
								<label>${memberPersonalDataCommand.loginCount}</label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label><spring:message code="member.detail.registerTime"/></label>
						        <label> 
						        <fmt:formatDate value="${memberPersonalDataCommand.registerTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						        </label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label><spring:message code="member.detail.lastLoginTtime"/></label>
						        <label>
						         <fmt:formatDate value="${memberPersonalDataCommand.loginTime}"  pattern="yyyy-MM-dd HH:mm:ss"/>
						        </label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.lastPayment"/></label>
						      <label>${memberPersonalDataCommand.payTime}</label>
						    </div>
						    <div class="ui-block-line ui-block-line-37">
						        <label><spring:message code="member.detail.ip"/></label>
						       	<label>${memberPersonalDataCommand.registerIp}</label>
						       	 <label> <a href=""><spring:message code="member.detail.validateProvince"/></a></label>
						    </div>
						    
						    <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.loginIp"/>ip地址</label>
						      	<label> ${memberPersonalDataCommand.loginIp}</label>
						      	 <label> <a href=""><spring:message code="member.detail.validateProvince"/></a></label>
						    </div>
						     <div class="ui-block-line ui-block-line-37">
						        <label>  <spring:message code="member.detail.accumulativeAmount"/></label>
						      	<label>${memberPersonalDataCommand.cumulativeConAmount}</label>
						    </div>
							</div>
                    </div>       
                    
                    <input type="hidden" name="memberId" id="memberId" value="${memberPersonalDataCommand.memberId}">             
                    <div class="tag-change-in"  >
							<div class="ui-block" style=""> 
						    <div class="ui-block">
								<div class="border-grey" id="table2" caption="<spring:message code="member.detail.personalAddress"/>"></div>
							 
							</div>
							</div> 
                    </div>
                    
                    <div class="tag-change-in"  >
							<div class="ui-block" style=""> 
						    <div class="ui-block">
								<div class="border-grey" id="table3" caption="<spring:message code="member.detail.personalfavorite"/>"></div>
							</div>
							</div> 
                    </div>
                    
               </div>
          </div>
	

	
	
</div>
</body>
</html>
