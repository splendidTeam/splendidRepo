<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@include file="/pages/commons/common-javascript.jsp" %>
<link rel="stylesheet" type="text/css" href="${base}/scripts/uploadify/uploadify.css" media="screen" />
<script type="text/javascript">
var $ = jQuery.noConflict();
</script>
<%-- <script type="text/javascript" src="${base}/scripts/jquery-1.6.2.min.js"></script> --%>
<script type="text/javascript" src="${base}/scripts/uploadify/jquery.uploadify-3.1.js"></script>
<script type="text/javascript" src="${base}/scripts/area.js"></script>
<style type="text/css">  
        #editTable {
            border: 1px solid #B1CDE3;  
            padding:0;   
            margin:0 auto;  
            border-collapse: collapse;  
        }  
          
        #editTable td {  
            border: 1px solid #B1CDE3; 
            background:#fff; 
            font-size:12px;  
            padding: 10px 10px 10px 10px;  
            color: #4f6b72;  
        } 
        input {
        	width:400px;font-size:15px;padding: 5px;
        } 
        textarea {
        	width:400px;font-size:15px;padding: 5px; height:80px;
        }
        select {
        	height:30px;font-size:13px;padding: 5px;color:#000000;
        }
        
        .button.orange {
		    background-color: #e84117;
		}
        .button {
		    background-color: #333;
		    background-repeat: no-repeat;
		    border: 0 none;
		    border-radius: 4px;
		    color: #f1f1f1;
		    cursor: pointer;
		    display: inline-block;
		    font-size: 12px;
		    padding: 8px 10px;
		    text-align: center;
		}
		a {
		    box-sizing: border-box;
		    color: #767676;
		    text-decoration: none;
		}
		
		.content-box {
		    background: none repeat scroll 0 0 #fff;
		    display: block;
		    height: auto;
		    overflow: hidden;
		    padding: 10px;
		    position: relative;
		}
		.width-percent100 {
		    width: 100% !important;
		}
		
		.ui-title1 {
		    color: #2d3033;
		    font-size: 24px;
		    line-height: 32px;
		    margin: 0 0 5px;
		    padding: 0 0 5px 37px;
		    text-align: left;
		    width: 100%;
		    font-weight: bold;
		}
</style>
<script type="text/javascript" src="${base}/scripts/offlineStore/updateStore_new.js?${version}"></script>
<div class="content-box width-percent100">
	<div class="ui-title1">
		<img src="${base }/images/wmi/blacks/32x32/cube.png">
		<c:if test="${type=='add' }">
			新增店铺
		</c:if>
		<c:if test="${type=='update' }">
			修改店铺
		</c:if>
	</div>
</div>
<input type="hidden" id="detail_id_type" value="${type}"/>
<!--section-->
<div>
	<div id="box4">
		<div id="store_store_details" style="clear: both" class="border-grey ui-loxia-simple-table">
			<form id="storeForm">
				<table class="editorTable" id="editTable" cellspacing="0"
					cellpadding="0">
					<!-- <tr><td colspan="7">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" id="store_name" name="store_name" value="" /> 
						 <a class="nin_button bisCancelBtn" id="search_store" href="javascript:;">查询店铺</a> 
						  <a class="nin_button bisCancelBtn" id="add_store_new" href="javascript:;">新增店铺</a>
					</td></tr> -->
					<tr class="editorheader">
						<th nowrap="nowrap" width="100" sort="asc">列名</th>
						<th nowrap="nowrap" sort="asc" width="500">内容</th>
					</tr>
					<tbody id="store_details">
						<tr>
							<td>店铺名称</td>
							<td><input type="text" id="detail_name" name="detail_name" value="${store.name }"/>
								<input type="hidden" id="detail_id" name="detail_id" value="${store.id }"/>
							</td>
						</tr>
						<tr>
							<td>详细地址</td>
							<td><textarea id="detail_full_address" name="detail_full_address">${store.fullAddress }</textarea></td>
						</tr>
						<tr>
							<td>店铺英文名称</td>
							<td><input type="text" id="detail_ename" name="detail_ename" value="${store.ename }"/></td>
						</tr>
						<tr>
							<td>英文地址1</td>
							<td><textarea id="detail_ename1" name="detail_ename1">${store.ename1 }</textarea></td>
						</tr>
						<tr>
							<td>英文地址2</td>
							<td><textarea id="detail_ename2" name="detail_ename2">${store.ename2 }</textarea></td>
						</tr>
						<tr>
							<td>所在省份</td>
							<td>
								<select name="_province" id="_province"  class="_province" dataType="Require" msg="选择省份">
									<option value="选择省份">请选择省份</option>
								</select> 
								<input type="hidden" id="detail_province" name="detail_province" value="${store.province}"/>
							</td>
						</tr>
						<tr>
							<td>所在城市</td>
							<td>
								<select name="_city" id="_city" class="_city" dataType="Require" msg="选择城市">
									<option value="选择城市" selected>请选择城市</option>
								</select> 
								<input type="hidden" id="detail_city" name="detail_city" value="${store.city}"/>
							</td>
						</tr>
						<tr>
							<td>所在区</td>
							<td>
								<select name="_district" id="_district" dataType="Require" msg="选择地区">
									<option value="选择地区" selected>选择地区</option>
								</select>
							</td>
						</tr>
						
						
						<script type="text/javascript">
						$(document).ready(function(){
/* 							$("#_province option[value='${store.province}']").attr("selected", true);	
							addCity(null, 6);
							$("#_city option[value='4']").attr("selected", true);	
							addData();
							$("#_district option[value='3']").attr("selected", true);	 */
							
							$.each($("#_province").find("option"),function(index,_province){
								if($(_province).text()=="${store.province}"){
									$(_province).prop("selected",true);
									addCity(null, $(_province).val());
								}
							})
						
							
							
							
							$.each($("#_city").find("option"),function(index,_city){
								if($(_city).text()=="${store.city}"){
									$(_city).prop("selected",true);
									addData();
								}
							})
							
							$.each($("#_district").find("option"),function(index,_district){
								if($(_district).text()=="${store.district}"){
									$(_district).prop("selected",true);
								}
							})
							
						});
						
						

						</script>
						
						<%-- <tr>
							<td>简要地址</td>
							<td><textarea id="detail_address" name="detail_address">${store.address }</textarea></td>
						</tr> --%>
						<%-- <tr>
							<td>楼层</td>
							<td><input type="text" id="detail_extension" name="detail_extension" value="${store.extension }"/></td>
						</tr> --%>
						<tr>
							<td>电话</td>
							<td><input type="text" id="detail_phone" name="detail_phone" value="${store.phone }"/></td>
						</tr>
						<tr>
							<td>营业时间</td>
							<td><input type="text" id="detail_hours" name="detail_hours" value="${store.hours }"/></td>
						</tr>
						<tr>
							<td style="border: 0px; min-width:300px;">
								<input type="file" id="storeImage-upload" name="file" />
							</td>
							<td style="border: 0px;">
								<a href="javascript:if(checkImport('#storeImage-upload-queue')){$('#storeImage-upload').uploadify('upload','*')}">上传文件</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a href="javascript:$('#storeImage-upload').uploadify('cancel','*');">取消上传</a>
							</td>
						</tr>
						<tr>
							<td>店铺图片</td>
							<td>
								<div id="storebanner_image_url"><img src="${base}/offlineStore/getStoreImage.json?path=${store.storeImage}"/></div>
								<input type="hidden" id="storeImagePath" name="storeImagePath" value="${store.storeImage}" />
							</td>
						</tr>
						<tr>
							<td style="border: 0px; min-width:300px;">
								<input type="file" id="mapImage-upload" name="file" />
							</td>
							<td style="border: 0px;">
								<a href="javascript:if(checkImport('#mapImage-upload-queue')){$('#mapImage-upload').uploadify('upload','*')}">上传文件</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a href="javascript:$('#mapImage-upload').uploadify('cancel','*');">取消上传</a>
							</td>
						</tr>
						<tr>
							<td>位置图片</td>
							<td>
								<div id="mapbanner_image_url"><img src="${base}/offlineStore/getStoreImage.json?path=${store.mapImage}"/></div>
								<input type="hidden" id="mapImagePath" name="mapImagePath" value="${store.mapImage}" />
							</td>
						</tr>
						<tr>
							<td colspan="2" style="padding-left: 400px;">
								<a class="button orange new" id="sub_store" href="javascript:;">提交</a> 
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>

		<br />
		<div class="clear group_edit_bottom">
			
		</div>
	</div>
</div>
<input type="hidden" id="sessionId" value="${pageContext.session.id }" />
<%-- <form id="myForm" method="post" action="${base}/banner-index-update.htm">
	<input type="hidden" name="banner_id" id="banner_id" />
</form> --%>

<!--section_end-->
