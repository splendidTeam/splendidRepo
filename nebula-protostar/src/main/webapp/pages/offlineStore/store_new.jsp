<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>

<script type="text/javascript" src="${base}/scripts/offlineStore/store_new.js"></script>
<div class="content-box width-percent100">
	<div class="ui-title1">
		<img src="${base }/images/wmi/blacks/32x32/cube.png">店铺管理
	</div>
</div>

<!--section-->
<div class="content-box width-percent100">

	<div id="box4">
		<div id="store_storeList" style="clear: both" class="border-grey ui-loxia-simple-table">
			<table class="editorTable" id="editTable" cellspacing="0"
				cellpadding="0">
				<tr><td colspan="7">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="text" id="store_name" name="store_name" class="ui-loxia-default ui-corner-all" value="" /> 
					 <a class="button orange" id="search_store" href="javascript:;">查询店铺</a> 
					  <a class="button orange" id="add_store_new" href="javascript:void(0);" onclick="toAddStore();">新增店铺</a>
				</td></tr>
				<tr class="editorheader">
					<th nowrap="nowrap" width="100" sort="asc">店铺id</th>
					<th nowrap="nowrap" sort="asc" width="300">店铺名称</th>
					<th nowrap="nowrap" sort="asc">店铺地址</th>
					<th nowrap="nowrap" width="200" sort="asc">店铺所在省</th>
					<th nowrap="nowrap" width="200" sort="asc">店铺所在城市</th>
					<th nowrap="nowrap" width="200" sort="asc">店铺所在区</th>
					<th nowrap="nowrap" width="250" sort="asc" width="250" align="left"
						style="cursor: pointer;">操作 <!-- <a class="nin_button bisCancelBtn" id="index_banner_save_order" href="javascript:;" onclick="saveOrder()">保存帧位 </a> -->
					</th>
				</tr>
				<tbody id="stores_list">
						
				</tbody>
			</table>
		</div>

		<br />
		<div class="clear group_edit_bottom"></div>
	</div>
</div>

