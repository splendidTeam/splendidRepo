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
<script type="text/javascript" src="${base}/scripts/consultant/consultant-list.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
</head>
<body>
	<%---------------------------------------- 权限控制 ----------------------------------------%>
	<input type="hidden" id="hid_pri" value="<acl:check aclElement='ACL_CONSULTANT_OPERATION'>true</acl:check>" />
	
	<div class="content-box width-percent100">
		<%-- 正文部分  定义整个正文的css --%>

		<div class="ui-block ">
		<div class="ui-title1 unReslovedTitleDiv">
			<%-- 标题横行部分  --%>
			<img src="../images/wmi/blacks/32x32/tag.png"><spring:message code='consultant.list.unresolved.switch'/>

			<input type="button" value="<spring:message code='cms.definition.export'/>" class="button orange unResloveExportBtn"   title="<spring:message code='cms.definition.export'/>" />
		</div>
		<div class="ui-title1 reslovedTitleDiv">
			<%-- 标题横行部分  --%>
			<img src="../images/wmi/blacks/32x32/tag.png"><spring:message code='consultant.list.resolved.switch'/>

			<input type="button" value="<spring:message code='cms.definition.export'/>" class="button orange resloveExportBtn"   title="<spring:message code='cms.definition.export'/>" />
			<acl:check aclElement='ACL_CONSULTANT_OPERATION'><input type="button" value="<spring:message code='consultant.list.batch.unpublish'/>" class="button batchUnpublishBtn"   title="<spring:message code='consultant.list.batch.unpublish'/>" /></acl:check>
			<acl:check aclElement='ACL_CONSULTANT_OPERATION'><input type="button" value="<spring:message code='consultant.list.batch.publish'/>" class="button orange batchPublishBtn"   title="<spring:message code='consultant.list.batch.publish'/>" /></acl:check>
		</div>
			<div class="ui-block-content" style="padding-top: 0">
				<div class="ui-tag-change">
					<ul class="tag-change-ul">
						<li class="memberbase" val="0"><spring:message code='consultant.list.unresolved'/></li>
						<li class="memberbase" val="1"><spring:message code='consultant.list.resolved'/></li>
					</ul>
					<div class="tag-change-content">
						<div class="tag-change-in">
							<div class="ui-block">
								<form action="/product/consultantList.json" id="unresolvedSearchForm" class="consultant-form">
									<%-------------------------------- 商品标签搜索 待解决 --------------------------------%>
									<%-- 咨询状态： 1 - 待回复， 2 - 延迟回复， 3 - 已回复 --%>
									<input type="hidden" name="q_int_lifecycle"	id="unresolved_lifecycle" value="1" />								
									
									<div class="ui-block-content ui-block-content-lb">
										<table>
											<tr>
												<td><label><spring:message code='consultant.list.consultTime'/></label>
												</td>
												<td><input type="text" id="unresolved_StartDate" name="q_date_createDateStart"
													loxiaType="date" mandatory="false" 
														<c:if test="${not empty  c_un_q_date_createDateStart }">value="${c_un_q_date_createDateStart }"</c:if>
													/>
												</td>

												<td><label>——</label>
												</td>
												<td><input type="text" id="unresolved_EndDate" name="q_date_createDateEnd"
													loxiaType="date" mandatory="false"
														<c:if test="${not empty  c_un_q_date_createDateEnd }">value="${c_un_q_date_createDateEnd }"</c:if>
													/>
												</td>
											</tr>
											<tr>
												<td><label><spring:message code='item.code'/></label></td>
												<td><input name="q_sll_code" type="text" id="unresolved_itemCode"
													loxiatype="input" placeholder="<spring:message code='consultant.list.placeholder.itemCode'/>" 
														<c:if test="${not empty  c_un_q_sll_code }">value="${c_un_q_sll_code }"</c:if>
													 />
												</td>

												<td><label><spring:message code='item.name'/></label>
												</td>
												<td><input name="q_sl_title" type="text" id="unresolved_itemName"
													loxiatype="input" placeholder="<spring:message code='consultant.list.placeholder.itemName'/>" 
														<c:if test="${not empty  c_un_q_sl_title }">value="${c_un_q_sl_title }"</c:if>
													/>
												</td>
											</tr>
										</table>
										<div class="button-line1">
											<a href="javascript:void(0);" class="func-button search"
												title="<spring:message code='btn.search'/>"><span><spring:message code='btn.search'/></span></a>
											<a href="javascript:void(0);" class="func-button reset"
												title="<spring:message code='btn.reset'/>"><span><spring:message code='btn.reset'/></span></a>
										</div>
									</div>
								</form>
							</div>
							<div class="border-grey ui-loxia-simple-table" id="unResloveTable" caption="<spring:message code='consultant.list.unresolved.table'/>">
							
							</div>

						</div>
						<%-- 已解决 --%>
						<div class="tag-change-in">
							
							<div class="ui-block">
								<form action="/product/consultantList.json" id="resolvedSearchForm" class="consultant-form">
									<%-------------------------------- 商品标签搜索 已解决 --------------------------------%>
									<%-- 咨询状态： 1 - 待回复， 2 - 延迟回复， 3 - 已回复 --%>
									<input type="hidden" name="q_int_lifecycle"	id="resolved_lifecycle" value="3" />		
									<div class="ui-block-content ui-block-content-lb">
										<table>
											<tr>
												<td><label><spring:message code='consultant.list.mark'/></label>
												</td>
												<td>
													<opt:select id="resolved_publishMark" name="q_int_publishmark" loxiaType="select" expression="chooseOption.CONSULTANT_PUBLISH_MARK" nullOption="please.select" />
												</td>
											</tr>
											<tr>
						                        <td><label><spring:message code='consultant.list.replyTime'/></label></td>
						                        <td> <input loxiaType="date"
						                                mandatory="false" id="resolved_startDate"
						                                name="q_date_resolvetimeStart"
															<c:if test="${not empty  c_re_q_date_resolvetimeStart }">value="${c_re_q_date_resolvetimeStart }"</c:if>
						                           	/>
						                        </td>
						                        <td align="center"><label>——</label></td>
						                         <td><input id="resolved_endDate" name="q_date_resolvetimeEnd"
						                            loxiaType="date"
						                            mandatory="false"
						                            	<c:if test="${not empty  c_re_q_date_resolvetimeEnd }">value="${c_re_q_date_resolvetimeEnd }"</c:if>
						                            />
						                        </td>
						                    </tr>
											<tr>
												<td><label><spring:message code='item.code'/></label>
												</td>
												<td><input name="q_sll_code" type="text" id="resolved_itemCode"
													loxiatype="input" placeholder="<spring:message code='consultant.list.placeholder.itemCode'/>" 
														<c:if test="${not empty  c_re_q_sll_code }">value="${c_re_q_sll_code }"</c:if>
													/>
												</td>

												<td><label><spring:message code='item.name'/></label>
												</td>
												<td><input name="q_sl_title" type="text" id="resolved_itemName"
													loxiatype="input" placeholder="<spring:message code='consultant.list.placeholder.itemName'/>" 
														<c:if test="${not empty  c_re_q_sl_title }">value="${c_re_q_sl_title }"</c:if>
													/>
												</td>

											</tr>
										</table>
										<div class="button-line1">
											<a href="javascript:void(0);" class="func-button search"
												title="<spring:message code='btn.search'/>"><span><spring:message code='btn.search'/></span></a>
											<a href="javascript:void(0);" class="func-button reset"
												title="<spring:message code='btn.reset'/>"><span><spring:message code='btn.reset'/></span></a>	
										</div>
									</div>
								</form>
							</div>
							<div class="border-grey ui-loxia-simple-table" id="reslovedTable"
								caption="<spring:message code='consultant.list.resolved.table'/>"></div>
						</div>

					</div>
				</div>
			

			</div>
			<div class="ui-title1 unReslovedTitleDiv">
				<input type="button" value="<spring:message code='cms.definition.export'/>" class="button orange unResloveExportBtn"   title="<spring:message code='cms.definition.export'/>" />
			</div>
			<div class="ui-title1 reslovedTitleDiv">
				<input type="button" value="<spring:message code='cms.definition.export'/>" class="button orange resloveExportBtn"   title="<spring:message code='cms.definition.export'/>" />
				<acl:check aclElement='ACL_CONSULTANT_OPERATION'><input type="button" value="<spring:message code='consultant.list.batch.unpublish'/>" class="button batchUnpublishBtn"   title="<spring:message code='consultant.list.batch.unpublish'/>" /></acl:check>
				<acl:check aclElement='ACL_CONSULTANT_OPERATION'><input type="button" value="<spring:message code='consultant.list.batch.publish'/>" class="button orange batchPublishBtn"   title="<spring:message code='consultant.list.batch.publish'/>" /></acl:check>
			</div>
		</div>
		<div id="categoryContent" class="menuContent"
			style="z-index: 999; display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
			<ul id="categoryTree" class="ztree"
				style="margin-top: 0; width: 180px; height: 100%;"></ul>
		</div>

	<%-- 回答问题弹出层  开始--%>
	<div id="answer_dialog" class="proto-dialog">
		 <h5><spring:message code='consultant.list.itemConsultant'/></h5>
		 <div class="proto-dialog-content p10">	
		   <div class="ui-block-line">
		   		<label><spring:message code='item.name'/>：</label>
				<div class="pt7">
					<a href="javascript:void(0);" class="blue" id="answer_dialog_itemName"></a>
				</div>
			</div>
			<div class="ui-block-line">
		   		<label><spring:message code='user.modify.label.username'/>：</label>
				<div class="pt7 grey">
					<a href="javascript:void(0);" id="answer_dialog_memberName"></a> <spring:message code='consultant.list.at'/> 
					<span id="answer_dialog_createTime"></span> <spring:message code='consultant.list.to'/> <a href="javascript:void(0);" id="answer_dialog_itemCode"></a> <spring:message code='consultant.list.of'/>
				</div>
			</div>
			
			<div class="ui-block-line">
		   		<label><spring:message code='consultant.list.content'/>：</label>
				<div>
					<p class="mt5" id="answer_dialog_content"></p>
				</div>
			</div>
			<div class="ui-block-line">
		   		<label><spring:message code='consultant.list.declaration'/>：</label>
				<div>
					<p class="mt5">
						<spring:message code='consultant.list.declaration.a'/><br />
						<spring:message code='consultant.list.declaration.b'/><br />
						<spring:message code='consultant.list.declaration.c'/><br />
					</p>
				</div>
			</div>
			<div class="ui-block-line bold pt10">
		   		<label><spring:message code='consultant.list.replyContent'/>：</label>
				<div class="pt7">
					&nbsp;
				</div>
			</div>
			<form>
				<input type="hidden" id="answer_dialog_id">
				<textarea id="answer_dialog_answer"  loxiaType="input" mandatory="true" aria-disabled="false" class="ui-loxia-default ui-corner-all mt10" rows="3" cols="30" style="width:100%; height:100px; resize:none"></textarea>
				<div id="loxiaTip-r" class="loxiaTip-r" style="display: none">
					<div class="arrow"></div>
					<div class="inner ui-corner-all"
						style="padding: .3em .7em; width: auto;"></div>
				</div>
			</form>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="<spring:message code='consultant.list.submit'/>" class="button orange copyok" />
		 	  <input type="button" value="<spring:message code='btn.cancel'/>" class="button black copycancel" />
		 	  <label><input type="checkbox" id="answer_dialog_publishMark" /><spring:message code='consultant.list.publish'/></label>
		 </div>
	</div>
	<%-- 回答问题弹出层 结束 --%>
	
	<%-- 修改答案弹出层  开始--%>
	<div id="update_dialog" class="proto-dialog">
		 <h5><spring:message code='consultant.list.itemConsultant'/></h5>
		 <div class="proto-dialog-content p10" style="height: 380px;">	
		   <div class="ui-block-line">
		   		<label><spring:message code='item.name'/>：</label>
				<div class="pt7">
					<a href="javascript:void(0);" class="blue" id="update_dialog_itemName"></a>
				</div>
			</div>
			<div class="ui-block-line">
		   		<label><spring:message code='user.modify.label.username'/>：</label>
				<div class="pt7 grey">
					<a href="javascript:void(0);" id="update_dialog_memberName"></a> <spring:message code='consultant.list.at'/>
					<span id="update_dialog_createTime"></span> <spring:message code='consultant.list.to'/> <a href="javascript:void(0);" id="update_dialog_itemCode"></a> <spring:message code='consultant.list.of'/>
				</div>
			</div>
			<div class="ui-block-line">
		   		<label><spring:message code='consultant.list.content'/>：</label>
				<div>
					<p class="mt5" id="update_dialog_content"></p>
				</div>
			</div>
			<div class="ui-block-line">
		   		<label><spring:message code='consultant.list.lastReply'/>：</label>
				<div>
					<p class="yellow mt5" id="update_dialog_lastAnswer"></p>
				</div>
			</div>
			<div class="ui-block-line">
		   		<label><spring:message code='consultant.list.declaration'/>：</label>
				<div>
					<p class="mt5">
						<spring:message code='consultant.list.declaration.a'/><br />
						<spring:message code='consultant.list.declaration.b'/><br />
						<spring:message code='consultant.list.declaration.c'/><br />
					</p>
				</div>
			</div>
			<div class="ui-block-line bold pt10">
		   		<label><spring:message code='consultant.list.replyContent'/>：</label>
				<div class="pt7">
					&nbsp;
				</div>
			</div>
			<form style="height: 150px;">
				<input type="hidden" id="update_dialog_id">
				<textarea id="update_dialog_answer" aria-disabled="false" class="ui-loxia-default ui-corner-all mt10" rows="3" cols="30" style="width:100%; height:100px; resize:none"></textarea>
				<div id="update-loxiaTip-r" class="loxiaTip-r" style="display: none">
					<div class="arrow"></div>
					<div class="inner ui-corner-all" style="padding: .3em .7em; width: auto;"></div>
				</div>
			</form>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="提交更新" class="button orange copyok" />
		 	  <input type="button" value="取消" class="button black copycancel" />
		 	  <label><input type="checkbox" id="update_dialog_publishMark" /><spring:message code='consultant.list.publish'/></label>
		 </div>
	</div>
	<%-- 修改答案弹出层 结束 --%>
	
<%--	<!-- 会员信息查询 dialog -->
	<div id="member-dialog" class="proto-dialog">
		 <h5>会员信息查询</h5>
		 <div class="proto-dialog-content">
			<div class="proto-dialog-content">		
	 
		             <form id="dialogSearchForm">
						    <div class="ui-block">
						    	<div class="ui-block-content ui-block-content-lb">
						             <table>
						                <tr>
						                    <td><label>账号</label>
						                    	</td>
						                    <td>
						                    	<input loxiatype="input" name="q_string_code" placeholder=""/>
						                    	</td>
						                       
						                    <td><label>名称</label>
						                    	</td>
						                    <td>
						                    	<input loxiatype="input" placeholder=""/>
						                    </td>
						                    <td><label>类型</label></td>	
						                     <td><select loxiatype="select">
						                                <option value="1">自有会员</option>
						                                <option value="0">第三方会员</option>
						                        </select>
						                      </td>
						                      
						                    </tr>
						                    
						                    <tr>
						                        <td><label>注册时间</label></td>
						                        <td> <input loxiaType="date"
						                                mandatory="false" id="createStartDate"
						                                name="q_date_createDate"></input>
						                        </td>
						                        <td align="center"><label>——</label></td>
						                         <td><input id="createEndDate"
						                            loxiaType="date"
						                            mandatory="false"></input>
						                        </td> 	
						                </tr>
						                </table>
						        <div class="button-line1">
						                <a href="javascript:void(0);" class="func-button search" title="搜索"><span>搜索</span></a>
						        </div>
						    </div>
						 </div>
					</form>
		    	<div class="ui-block">
			    	<div id="table3" class="border-grey" caption="商品列表" >
			    	</div>   
		    	</div> 	
			</div>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="确定" class="button orange dialogPass"/>
		 	  
		 	  <input type="button" value="取消" class="button orange dialogDelete"/>
		 </div>
	</div> 
--%>
</div>
</body>
</html>
