<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<script type="text/javascript" src="${base}/scripts/module/member-selector.js"></script>
</head>
<body>
<div>
<%-- 会员的id与会员名 将回传绑定在这个隐藏域中 --%>
<input type="hidden" id="member-selector-data" />
<h5><spring:message code='member.filter.memberSelect'/></h5>
<div class="proto-dialog-content p10">
	<form action="/member/memberList.json" id="frm-member-selector">
		<div class="ui-block">
			<div class="ui-block-content ui-block-content-lb">
				<table>
					<tr>
						<td><label><spring:message code="member.group.membername" /></label></td>
						<td>
							<span> 
								<input type="text" loxiaType="input" mandatory="false" placeholder="<spring:message code='member.group.loginname'/>" id="loginName" name="q_sl_loginName" />
							</span>
						</td>
						<td><label><spring:message code="member.list.email" /></label></td>
						<td>
							<span> 
								<input type="text" loxiaType="input" mandatory="false" placeholder="member@abc.com" id="loginEmail" name="q_sl_loginEmail" />
							</span>
						</td>
					</tr>
					<tr>
						<td><label><spring:message code="member.list.mobile" /></label></td>
						<td>
							<span> 
								<input type="text" loxiaType="input" mandatory="false" placeholder="13xxxxxxxxx" id="loginMobile" name="q_sl_loginMobile" />
							</span>
						</td>
						<td><label><spring:message code="member.list.group" /></label></td>
						<td>
							<select loxiaType="select" mandatory="false" id="groupId" name="q_long_groupId">
								<option value="">
									<spring:message code="member.group.label.unlimit" />
								</option>
								<c:forEach var="group" items="${groupList}">
									<option value="${group.id}">${group.name}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
				</table>
				<div class="button-line1">
					<a href="javascript:void(0);" class="func-button search" title="<spring:message code='user.list.filter.btn'/>">
						<span><spring:message code="user.list.filter.btn" /></span>
					</a>
				</div>
			</div>
		</div>
	</form>
	<div id="member-selector-table"></div>
</div>
<div class="proto-dialog-button-line">
	<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
</div>
</div>
</body>
</html>
