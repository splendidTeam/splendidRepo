<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${base}/scripts/product/recommand/recommand-dialog.js"></script>
<script type="text/javascript">
var zNodes = <c:out value="${ categoryJson }" default="[]" escapeXml="false" />
</script>
<%--- dialog  begin --%>
<div class="filtrate-item-dialog proto-dialog">
	<h5>商品筛选器</h5>
	<div class="proto-dialog-content">
		<div class="ui-tag-change">
			<ul class="tag-change-ul">
				<li class="memberbase">筛选器添加</li>
				<li class="memberbase bitch">批量添加</li>
			</ul>
			<div class="tag-change-content">
				<div class="tag-change-in">
					<div class="ui-block ui-block-fleft w240">
						<div class="ui-block-content ui-block-content-lb">
							<div class="tree-control">
								<input type="text" id="key" loxiatype="input" placeholder="<spring:message code="shop.property.keyword"/>" />
								<div><span id="search_result"></span></div>
							</div>
							<ul id="categoryTree" class="ztree"></ul>
						</div>
					</div>
					<div class="ui-block ml240">
						<div class="ui-block">
							<%-- 
							<div class="ui-title1" style="height: 35px;">
								<input type="button" value="添加推荐" class="button orange add-recommand" title="添加推荐"/>
							</div> 
							--%>
							<div class="ui-block-content ui-block-content-lb">
								<form action="/recommand/findItemInfoList.json" id="dialogSearchForm">
										<table>
											<tbody>
												<tr>
													<td><label>商品编码</label></td>
													<td><span id="searchkeytext"> <input type="text" mandatory="false" id="code" name="q_sl_code" placeholder="商品编码" class="ui-loxia-default ui-corner-all" aria-disabled="false"></span></td>
													<td><label>商品名称</label></td>
													<td><span id="searchkeytext"> <input type="text" mandatory="false" id="name" name="q_sl_title" placeholder="商品名称" class="ui-loxia-default ui-corner-all" aria-disabled="false"></span></td>
													<td><input id="categoryCode" type="hidden" mandatory="false" name="q_string_categoryCode" ></td>
												</tr>
											</tbody>
										</table>
										<div class="button-line1">
											<a href="javascript:void(0);" class="func-button search"><span>搜索</span></a>
										</div>
								</form>	
							</div>
						</div>
						<div class="border-grey" id="item-table" caption="商品列表 "></div>
						<%--
						<div class="button-line">
							<input type="button" value="添加推荐" class="button orange add-recommand" title="添加推荐"/>
						</div>
						--%>
					</div>
				</div>
				<div class="tag-change-in">
					<textarea loxiaType="input" id="itemCodes" selectonfocus="true" checkmaster="" style="width: 760px; height: 150px;"></textarea>
					<br />分割符:
					<select class="splitStr">
						<option value="1">换行符</option>
						<option value="2">逗号(,)</option>
					</select>
					<%-- <span style="display: block;margin-top: 5px; color: red;">不同的商品编码, 请用不同的行(每行一个商品编码)</span> --%>
				</div>
			</div>	
		</div>
	</div>
	<div class="proto-dialog-button-line">
		<input type="button" value="确定" class="button orange confirm"/>
		<input type="button" value="取消" class="button cencal" />	
	</div>
</div>
<%--- dialog  end --%>

