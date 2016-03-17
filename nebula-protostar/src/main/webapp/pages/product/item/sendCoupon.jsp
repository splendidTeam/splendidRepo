<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager" /></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet"
	href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript"
	src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript"
	src="${base}/scripts/product/item/sendCoupon.js"></script>
</head>
<body>
	<div class="content-box width-percent100">
		<div class="ui-block">
			<div class="ui-title1 title_unchecked">
				<img src="${base}/images/wmi/blacks/32x32/tag.png">派发优惠券
			</div>
			<div class="ui-title1 title_checked" style="display: none">
				<img src="${base}/images/wmi/blacks/32x32/tag.png">派发优惠券
			</div>
			<div class="ui-block-content" style="padding-top: 0">
				<div class="ui-tag-change">
					<ul class="tag-change-ul">
						<li class="memberbase">文本框会员群发</li>
						<li class="memberbase">筛选会员群发</li>
					</ul>
					<div class="tag-change-content">
						<div class="tag-change-in">
							<!-- 开始 -->
							<div class="ui-block-content ui-block-content-lb">
								<table>
									<tr>
										<td><input style="margin-right: 0px;" type="radio"
											name="radio" value="1">ID <input
											style="margin-right: 0px;" type="radio" name="radio" value="2">邮箱
											<input style="margin-right: 0px;" type="radio" name="radio"
											value="3">用户名 &nbsp;&nbsp;<span style="color: red;">(多个以英文逗号分隔)</span>
										</td>
									</tr>
									<tr>
									</tr>
									<tr>
										<textarea style="width: 100%; height: 300px;" rows="25"
											cols="200" name="massMeber" id="text"></textarea>
									</tr>
								</table>
							</div>
							<!-- 结束-->

							<div class=""
								style="padding: 10px; border: 1px solid #DEDEDE; clear: both; display: block; overflow: hidden;">
								<div class="">
									<span> 派发: <select  id="coupontype" name="q_int_lifeCycle"
										class="ui-loxia-default ui-corner-all" aria-disabled="false">
											<option value="0">请选择</option>
											<c:forEach var="item" items="${couponTypeList}">
												<option value="${item.id}">${item.couponName}</option>
											</c:forEach>
									</select>优惠券
									</span> <span style="margin: 0px 0px 0px 20px;"> <input
										id="box" type="checkbox">站内信提示用户
									</span>
									<%--  
						                 <span id="em" style="margin: 0px 0px 0px 20px;display: none;">
										  用户提示类型:
									         <select id="lifeCycle" onchange="checktype(this.value)" name="q_int_lifeCycle" class="ui-loxia-default ui-corner-all" aria-disabled="false">
									             <option value="">请选择</option>
									             <option value="1">模板发送</option>
						                         <option value="2">短信发送</option>
						                         <option value="3">邮件发送</option>
						                     </select>
						                 </span>
						                  --%>

									<span id="tip" style="margin: 0px 0px 0px 15px; display: none;">
										站内信模板选择: <select name="q_int_lifeCycle"
										class="ui-loxia-default ui-corner-all" aria-disabled="false" id="messagetemplate">
											<option value="0">请选择</option>
											<c:forEach var="templatelist" items="${templatelist}">
												<option value="${templatelist.id}">${templatelist.title}</option>
											</c:forEach>
									</select>
									</span>

													<%--  <span id="sem" style="margin: 0px 0px 0px 15px;display: none;">
									     邮件发送:
								         <select id="lifeCycle" name="q_int_lifeCycle" class="ui-loxia-default ui-corner-all" aria-disabled="false">
								             <option value="0">请选择</option>
					                         <c:forEach var="emailTemplatelist" items="${emailTemplatelist}">
										       <option value="${emailTemplatelist.code}">${emailTemplatelist.name}</option>
									         </c:forEach>
					                     </select>
					                 </span> --%>

								</div>
								<div style="text-align: right;">
									<input type="button" value="派发优惠券" title="派发优惠券"
										class="button orange confirmsend  sendcoupon" />
								</div>
							</div>

						</div>
						<div class="tag-change-in">
							<!-- 开始 -->
							<div class="content-box width-percent100">
								<form action="/member/memberList.json" id="searchForm">
									<div class="ui-block">
										<div class="ui-block-content ui-block-content-lb">
											<table>
												<tr>
													<td><label><spring:message
																code="member.group.membername" /></label></td>
													<td><span><input type="text" loxiaType="input"
															mandatory="false"
															placeholder="<spring:message code='member.group.loginname'/>"
															id="loginName" name="q_sl_loginName"></input></span></td>
													<td><label><spring:message
																code="member.list.email" /></label></td>
													<td><span> <input type="text" loxiaType="input"
															mandatory="false" placeholder="member@abc.com"
															id="loginEmail" name="q_sl_loginEmail"></input></span></td>
													<td><label><spring:message
																code="member.list.mobile" /></label></td>
													<td><span><input type="text" loxiaType="input"
															mandatory="false" placeholder="13xxxxxxxxx"
															id="loginMobile" name="q_sl_loginMobile"></input></span></td>
												</tr>
												<tr>
													<td><label><spring:message
																code="member.group.source" /></label></td>

													<td><span> <opt:select name="q_long_Source"
																id="Source" loxiaType="select"
																expression="chooseOption.MEMBER_SOURCE"
																nullOption="role.list.label.unlimit" />
													</span></td>

													<td><label><spring:message
																code="member.list.group" /></label></td>

													<td><select loxiaType="select" mandatory="false"
														id="groupId" name="q_long_groupId">
															<option value=""><spring:message
																	code="member.group.label.unlimit" />
															</option>
															<c:forEach var="item" items="${memberList}">
																<option value="${item.id}">${item.name}</option>
															</c:forEach>
													</select></td>

													<td><label><spring:message
																code="member.group.type" /></label></td>

													<td><span> <opt:select name="q_long_Type"
																id="Type" loxiaType="select"
																expression="chooseOption.MEMBER_TYPE"
																nullOption="role.list.label.unlimit" />
													</span></td>
												</tr>
												<tr>
													<td><label><spring:message
																code="itemcategory.list.filter.createtime" /></label></td>
													<td><span><input type="text" id="startTime"
															name="q_date_startTime" value="" loxiaType="date"
															mandatory="false" /></span></td>
													<td><label>——</label></td>
													<td><span><input type="text" id="endTime"
															name="q_date_endTime" value="" loxiaType="date"
															mandatory="false" /></span></td>
													<td><label><spring:message
																code="role.list.label.status" /></label></td>

													<td><span> <opt:select name="q_int_lifeCycle"
																id="lifeCycle" loxiaType="select"
																expression="chooseOption.IS_AVAILABLE"
																nullOption="role.list.label.unlimit" />
													</span></td>
												</tr>
											</table>
											<div class="button-line1">
												<a href="javascript:void(0);" class="func-button search"
													title="<spring:message code='user.list.filter.btn'/>"><span><spring:message
															code="user.list.filter.btn" /></span></a>
											</div>
										</div>
									</div>
								</form>
								<div class="ui-block">
									<div class="border-grey" id="table1"
										caption="<spring:message code='member.group.member.list'/>"></div>
								</div>
								<div class=""
									style="padding: 10px; border: 1px solid #DEDEDE; clear: both; display: block; overflow: hidden;">
									<div class="">
										<span> 派发: <select id="usercoupontype"
											name="q_int_lifeCycle" class="ui-loxia-default ui-corner-all"
											aria-disabled="false">
												<option value="0">请选择</option>
												<c:forEach var="item" items="${couponTypeList}">
													<option value="${item.id}">${item.couponName}</option>
												</c:forEach>
										</select>优惠券
										</span> <span style="margin: 0px 0px 0px 20px;"> <input 
											id="boxc" name="checkbox" type="checkbox">站内信提示用户
										</span>
															<%--  
						                 <span id="em" style="margin: 0px 0px 0px 20px;display: none;">
										  用户提示类型:
									         <select id="lifeCycle" onchange="checktype(this.value)" name="q_int_lifeCycle" class="ui-loxia-default ui-corner-all" aria-disabled="false">
									             <option value="">请选择</option>
									             <option value="1">模板发送</option>
						                         <option value="2">短信发送</option>
						                         <option value="3">邮件发送</option>
						                     </select>
						                 </span>
						                  --%>

										<span id="tipc"
											style="margin: 0px 0px 0px 15px; display: none;">
											站内信模板选择: <select id="usermessagetemplate" name="q_int_lifeCycle"
											class="ui-loxia-default ui-corner-all" aria-disabled="false">
												<option value="0">请选择</option>
												<c:forEach var="templatelist" items="${templatelist}">
													<option value="${templatelist.id}">${templatelist.title}</option>
												</c:forEach>
										</select>
										</span>
					
															<%--  <span id="sem" style="margin: 0px 0px 0px 15px;display: none;">
										     邮件发送:
									         <select id="lifeCycle" name="q_int_lifeCycle" class="ui-loxia-default ui-corner-all" aria-disabled="false">
									             <option value="0">请选择</option>
						                         <c:forEach var="emailTemplatelist" items="${emailTemplatelist}">
											       <option value="${emailTemplatelist.code}">${emailTemplatelist.name}</option>
										         </c:forEach>
						                     </select>
						                 </span> --%>

									</div>
									<div style="text-align: right;">
										<input type="button" value="派发优惠券" title="派发优惠券"
											class="button orange confirmsend  usersendcoupon" />
									</div>
								</div>
							</div>
							<!-- 结束-->
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>