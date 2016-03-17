<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="item.update.manage"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/freight/distribution/supported-area-list.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
<script type="text/javascript">
	var distributionId = '<c:out value="${distributionMode.id }" escapeXml="" default=""></c:out>';
	
	var zNodes = [
	            	{id:1, pId:-1, name:"中国", 
	             		 sortNo:1, 
	             		open:true, isParent:true}
	            	,
	             		
	                <c:forEach var="address" items="${addresslist}" varStatus="status">
	                 	{id:${address.id}, pId:${address.pId}, name:"${address.name}", 
	                 	 sortNo:${address.id}, 
	                 		open:false,isParent:true}
	                 	<c:if test="${!status.last}">,</c:if>
	                 </c:forEach>
	            ]; 
</script>

</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/twitter_2.png">支持区域
	 	 <input type="button" value="返回" class="button return-back" />
         <input type="button" value="导入支持区域" class="button orange" 
         onclick="javascript:location.href='/freight/toImportSupportedArea.htm?distributionId=${distributionMode.id }'" />
         <input type="button" value="地址查询" class="button orange search-address" />
    </div>
 
  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
            <table>
                <tr>
                    <td><label>物流名称</label></td>
                    <td><span>${distributionMode.name }</span></td>
   
                </tr>
            </table>
        </div>
    </div>
 
    <div class="ui-block">
    <input type="hidden" value="${distributionMode.id }" id="distributionModeId"/>
    <div id="table1" class="border-grey" caption="支持区域列表" ></div>   
    </div>
    
    <div class="button-line">
     <input type="button" value="地址查询" class="button orange search-address" />
         <input type="button" value="导入支持区域" class="button orange" 
         onclick="javascript:location.href='/freight/toImportSupportedArea.htm?distributionId=${distributionMode.id }'" />
    <input type="button" value="返回" class="button return-back" />
    </div>

</div>

<!--  查询窗口 -->

<%--- dialog  begin --%>
<div id="dialog-address-search" class="proto-dialog">
	<h5>地址查询</h5>
	<div class="proto-dialog-content">
		<div class="ui-tag-change">
			<ul class="tag-change-ul">
				<li class="memberbase">地址列表</li>
				<li class="memberbase bitch">地址树</li>
			</ul>
			<div class="tag-change-content">
				<div class="tag-change-in">
					
					<div class="ui-block">
						<div class="ui-block">
							
							<div class="ui-block-content ui-block-content-lb">
								<form action="/freight/searchAreaData.json" id="dialogSearchForm">
										<table>
											<tbody>
												<tr>
													<td><label>区域名称</label></td>
													<td><span id="searchkeytext"> <input type="text" mandatory="false" id="name" name="q_sl_name" placeholder="区域名称" class="ui-loxia-default ui-corner-all" aria-disabled="false"></span></td>
													<td><label>区域编号</label></td>
													<td><span id="searchkeytext"> <input type="text" mandatory="false" id="areaId" name="q_long_areaId" placeholder="区域编号" class="ui-loxia-default ui-corner-all" aria-disabled="false"></span></td>
													
												</tr>
											</tbody>
										</table>
										<div class="button-line1">
											<a href="javascript:void(0);" class="func-button search"><span>搜索</span></a>
										</div>
								</form>	
							</div>
						</div>
						<div class="border-grey" id="area-table" caption="区域列表 "></div>
						
					</div>
				</div>
				<div class="tag-change-in">
					
					<div class="ui-block-content ui-block-content-lb">
						<div class="tree-control" >
                    		<input type="text" id="key-left" loxiatype="input" placeholder="<spring:message code='item.search.keyword'/>" />
                    		<div><span id="search_result_left"></span></div>
                
                		</div>
                		<ul id="tree" class="ztree"></ul>
					</div>
					
				</div>
			</div>	
		</div>
	</div>
	<div class="proto-dialog-button-line">
	<%-- 
		<input type="button" value="确定" class="button orange confirm"/>
	--%> 
		<input type="button" value="取消" class="button cencal" />	
	</div>
</div>
<%--- dialog  end --%>

</body>
</html>
