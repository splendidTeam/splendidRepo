<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/promotion/priority-edit.js"></script>
<script type="text/javascript" src="${base }/scripts/jquery/jqueryplugin/jquery.json.js"></script> 
<script type="text/javascript" src="${base }/scripts/main.js"></script>
<script type="text/javascript">
$j(window).load(function(){
	$j('#itable').tabledrag();
});
</script>
<style type="text/css">
	.ui-loxia-simple-table table .highlight td{
		background-color: pink;
	}

	.ui-loxia-simple-table table tr.highlight:hover td{
		background-color: pink;
	}
</style>
</head>

<body>
<%-- 优先级ID --%>
<input type="hidden" id="priority-id" value="${priority.id }" />
<%-- 查看 --%>
<input type="hidden" id="is-view" value="${isView }" />

<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/calc.png">  
	促销优先级-<c:if test="${isView }">查看</c:if><c:if test="${isView eq null }">编辑</c:if>
	
	
	<input type="button" value="返回" class="button return" title="返回" />
	<c:if test="${isUpdate}">
		<input type="button" value="创建分组" class="button orange btn-create" title="创建分组"/>
		<input type="hidden" value="创建共享分组" class="button orange btn-create-share" title="创建共享分组"/>
	</c:if>
    </div>
    
    <div class="ui-block">
    	
   	 	<div class="table-border0 border-grey" id="table1">
   	 		<table id="itable" class="tbl_priority_datas" cellspacing="0" cellpadding="0">
				<thead>
					<tr>
					    <c:if test="${!isUpdate}">
							<th width="5%">
								<div>优先级编号</div>
							</th>
						</c:if>
						<th  width="20%">
							<div>活动名称</div>
						</th>
						<th width="10%">
							<div>开始时间</div>
						</th>
						<th width="10%">
							<div>结束时间</div>
						</th>
						<th width="10%">
							<div>受益人群</div>
						</th>
						<th width="10%">
							<div>商品范围</div>
						</th>
						<c:if test="${!isAdd}">
							<th width="15%">
								<div>组名</div>
							</th>
						
							<th width="5%">
								<div>排他</div>
							</th>
						</c:if>	
						<c:if test="${!isView}">
							<th width="15%">
								<div>操作</div>
							</th>
						</c:if>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="item" items="${detailList }" varStatus="status">
				    <c:if test="${item.groupName eq null }">
						<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>'>
						    <c:if test="${!isUpdate}">
							    <td width ="5%" >
									<span>${item.priority }</span>
								</td>
							</c:if>
							<td width ="20%" >
								<span title="促销ID:${item.promotionId }">${item.promotionName }</span>
								<input name="parameters" id="parameters"  promotionId="${item.promotionId }" groupName="${item.groupName }" type="hidden" value="" />
							</td>
							<td width ="`10%" >
								<span><fmt:formatDate value="${item.promotionStartTime }" pattern="yyyy-MM-dd HH:mm:ss" /></span>
							</td>
							<td width ="10%" >
								<span><fmt:formatDate value="${item.promotionEndTime }" pattern="yyyy-MM-dd HH:mm:ss" /></span>
							</td>
							<td width ="10%" >
								<span>${item.audienceName }</span>
							</td>
							<td width ="10%" >
								<span>${item.scopeName }</span>
							</td>
							<c:if test="${!isAdd}">
								<td width ="15%" >
									<span>${item.groupName }</span>
								</td>
							
								<td width ="5%" >
									<input type="checkbox" <c:if test="${isView}">disabled="disabled"</c:if> id="pid_${item.promotionId }" <c:if test="${item.exclusiveMark==1 }">checked='checked'</c:if>>
								</td>
							</c:if>	
							
							<c:if test="${!isView}">
								<td width ="15%" > 
									<a class="func-button btn-up" href="javascript:void(0);">上移</a>
									<a class="func-button btn-down" href="javascript:void(0);" groupName="${item.groupName }" promotionId="${item.promotionId }">下移</a>
								</td>
							</c:if>
						</tr>
					</c:if>
					<c:if test="${item.groupName ne null }">
						<tr class='<c:if test="${status.index%2==0 }">even</c:if><c:if test="${status.index%2!=0 }">odd</c:if>'>
						    <c:if test="${!isUpdate}">
								   <td colspan ="6" width ="65%" >
							</c:if>
							<c:if test="${isUpdate}">
								   <td colspan ="5" width ="65%" >
							</c:if>
					            <table style="margin: 5 5 5 -10">
					                 <c:forEach var="comm" items="${item.promotionPriorityAdjustDetailCommandList }" varStatus="s">
							           <tr>
							                <c:if test="${!isUpdate}">
								              	<td width="8%" style="text-align: left">
													<span>${comm.priority }</span>
												</td>
											</c:if>
											<td width="30%" style="padding: 0 0 0 0">
												<span title="促销ID:${comm.promotionId }">${comm.promotionName }</span>
												<input name="parameters" id="parameters"  promotionId="${comm.promotionId }" groupName="${comm.groupName }" type="hidden" value="" />
											</td>
											<td width="15%">
												<span><fmt:formatDate value="${comm.promotionStartTime }" pattern="yyyy-MM-dd HH:mm:ss" /></span>
											</td>
											<td width="15%">
												<span><fmt:formatDate value="${comm.promotionEndTime }" pattern="yyyy-MM-dd HH:mm:ss" /></span>
											</td>
											<td width="15%" style="padding: 0 0 0 15">
												<span>${comm.audienceName }</span>
											</td>
											<td width="15%" style="padding: 0 0 0 15">
												<span>${comm.scopeName }</span>
											</td>
							            </tr>
						             </c:forEach>
					            </table>
					        </td>
					        <c:if test="${!isAdd}">
								<td width ="15%" >
									<c:if test="${item.groupType eq 0 }">
										<span><!-- 【单享】 --></span>
									</c:if>
									<c:if test="${item.groupType eq 1 }">
										<span><!-- 【共享】--></span>
									</c:if>
									<span>${item.groupName }</span>
								</td>
								<td width ="5%" >
								</td>
							</c:if>	
					 
							<c:if test="${!isView}">
								<td width ="15%" > 
								    <a class="func-button btn-ungroup" groupName="${item.groupName }" href="javascript:void(0);">解组</a>
									<a class="func-button btn-up" href="javascript:void(0);">上移</a>
									<a class="func-button btn-down" href="javascript:void(0);">下移</a>
								</td>
							</c:if>
						</tr>
					</c:if>
				</c:forEach>
				
			
				</tbody>
			</table>
		</div>   
    </div>
     
	<div id="div-info" class="ui-block"> 
		<div class="ui-block-line mt5">
			<label>优先级名称</label>
			<input id="name" placeHolder="优先级名称" loxiaType="input" mandatory="true" value="${priority.adjustName }" />
		</div>
	</div>
</div>
<div class="button-line">
<c:if test="${isView eq null }">
	<input type="button" value="保  存" class="button orange btn-save" title="保存" />
</c:if>
	<input type="button" value="返回" class="button return" title="返回" />
</div>


<%--- dialog  begin --%>
<div class="promotion-exclusive-dialog proto-dialog">
	<h5>创建促销活动分组</h5>
	<div class="proto-dialog-content">
		<div class="ui-block">
			<label>分组名称</label> 
			<input name="groupName" id="dialogGroupName" type="text" loxiaType="input" mandatory="true" checkmaster="" />
		</div>
		<div class="ui-block">
			<div id="dialog-table" class="border-grey" caption="可选分组列表"></div>
		</div>
	</div>
	<div class="proto-dialog-button-line">
		<input type="button" value="确定" class="button orange dialog-confirm"/>
		<input type="button" value="取消" class="button dialog-cencal" />	
	</div>
</div>
<%--- dialog  end --%>


</body>
</html>