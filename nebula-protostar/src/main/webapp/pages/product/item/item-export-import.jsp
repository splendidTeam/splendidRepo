<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/product/item/item-export-import.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery.form.js"></script>
<%--
<script type="text/javascript" src="${base}/scripts/ajaxfileupload.js"></script>
 --%>

<style type="text/css">
.dashedwp { border-top:#ccc 1px dashed; padding:12px 0; margin:16px 0; }
label.normal { font-weight:normal; padding:3px 6px 3px; margin-right:12px; }
label.selected { font-weight:bold; padding:3px 6px 3px; margin-right:12px; background:#d8edf3; }
.leftlocation { text-align:left; padding-left:125px; }
.infocustom { color: #31708f; background-color: #d9edf7; border-color: #bce8f1; padding: 15px; margin: 10px 0; border: 1px solid transparent; border-radius: 4px; }
.textarea_info { position:absolute; left:420px; top:0; color:#666; }
</style>


<%-- 
<link rel="stylesheet" type="text/css" href="${base}/scripts/uploadify3/uploadify.css" media="screen" />
<script type="text/javascript" src="${base}/scripts/uploadify3/jquery.uploadify.min.js"></script> 
--%>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png">商品导出和导入</div>
	<div class="ui-block"></div>
	<div class="ui-block">
		<div class="ui-block-content" style="padding-top:0">
			<div class="ui-tag-change">
				<ul class="tag-change-ul" style="font-size:16px">
					<li class="memberbase">STEP 1. 商品的导出</li>
					<li class="memberbase">STEP 2. 商品的导入</li>
					<li class="memberbase">SKU 1. 商品的导出</li>
					<li class="memberbase">SKU 2. 商品的导入</li>
				</ul>
				<input type="hidden" id="batchThreshold" value="${batchThreshold}" />
				<div class="tag-change-content">
					<div class="tag-change-in">
				        <form id="exportItemForm" action="${ pagebase }/item/itemExport.htm" method="post">
				          <input type="hidden" name="industryId" id="industryId" value="" />
				          <div class="ui-block">
				            <div class="infocustom">此处商品的修改，采用先导出，然后编辑，再次导入的模式。不采用自行创建Excel表的导入。</div>
				            <div class="ui-block-content">
				              <div class="ui-block-line">
				                <label><b>商品所属行业</b></label>
				                <div>
				                  <select id="industry" class="ui-loxia-default ui-corner-all">
				                    <c:forEach items="${ industryList }" var="industry">
										<option value="${ industry.id  }">${ industry.name }</option>
				                    </c:forEach>
				                  </select>
				                  <span class="red">首先切换商品所属行业！</span>
				                  <%-- <div style=" padding:12px 0;"><a href="javascript:void(0)" id="downLoadTmplOfSkuInfo" class="func-button">下载该行业模板</a></div> --%>
				                </div>
				              </div>
				              <div class="dashedwp">
				                <div class="ui-block-line">
				                  <label><b>基本信息</b></label>
				                  <div>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="code" checked="checked" onclick="return false">商品编码</label>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="title">商品名称</label>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="subTitle">商品副标题</label>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="itemStyle">商品款式</label>
				                    <%-- <label class="normal"><input type="checkbox" name="selectCodes" value="">商品默认分类</label> --%>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="categoryCodes">商品分类</label>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="itemType">商品类型</label>
				                  </div>
				                </div>
				                <div class="ui-block-line">
				                  <label><b>一般属性信息</b></label>
				                  <div class="commonProperty">
				                    <%--<label class="normal"><input type="checkbox" name="selectCodes" value="">尺码表</label> --%>
				                  </div>
				                </div>
				                <div class="ui-block-line">
				                  <label><b>商品价格</b></label>
				                  <div>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="salePrice">销售价</label>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="listPrice">挂牌价</label>
				                  </div>
				                </div>
				                <%--
				                <div class="ui-block-line">
				                  <label><b>销售属性信息</b></label>
				                  <div class="salesProperty">
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="">尺码</label>
				                  </div>
				                </div>
				                --%>
				                <div class="ui-block-line">
				                  <label><b>SEO搜索</b></label>
				                  <div>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="seoTitle">SEO搜索标题</label>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="seoKeyWords">SEO搜索关键字</label>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="seoDesc">SEO搜索描述</label>
				                  </div>
				                </div>
				                <div class="ui-block-line">
				                  <label><b>商品描述</b></label>
				                  <div>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="sketch">商品概述</label>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="description">商品详细描述</label>
				                  </div>
				                </div>
				              </div>
				              <div class="dashedwp">
				                <div class="ui-block-line">
				                  <label><b>具体商品编码</b></label>
				                  <div style="position:relative;">
				                    <textarea name="itemCodes" class="ui-loxia-default ui-corner-all" style="width:400px; height:200px;"></textarea>
				                    <div class="textarea_info"> <span class="red">非必填项！</span><br>
				                      <br>为空，导出部分只有字段标题，没有数据。<br>填写商品编码，导出部分含有商品的已有数据。<br>
				                      <br>
				                      <strong>最多填写${batchThreshold}条数据</strong>，每条编码换行填写，格式如下：<br>
				                      Nb_Sample_Code_001<br>
				                      Nb_Sample_Code_002<br>
				                      Nb_Sample_Code_003<br>
				                      Nb_Sample_Code_004<br>
				                      Nb_Sample_Code_005
				                    </div>
				                  </div>
				                </div>
				              </div>
				            </div>
				          </div>
				        </form>
				        <div class="button-line leftlocation">
				          <input type="button" value="确定导出" title="" class="button orange export"/>
				          <input type="button" value="返回" title="" class="button back"/>
				        </div>
					</div>
					<div class="tag-change-in">
					        <form id="importItemForm" method="post" enctype="multipart/form-data">
					          <%--<input type="hidden" name="industryId" id="import-industry-id" value="" /> --%>
					          <div class="ui-block">
					            <div class="infocustom">此处商品的修改，采用先导出，然后编辑，再次导入的模式。不采用自行创建Excel表的导入。</div>
					            <div class="ui-block-content">
					              <%--
					              	<div class="ui-block-line">
					                <label><b>商品所属行业</b></label>
					                <div>
					                  <select id="import-industry" class="ui-loxia-default ui-corner-all">
					                    <c:forEach items="${ industryList }" var="industry">
											<option value="${ industry.id  }">${ industry.name }</option>
					                    </c:forEach>
					                  </select>
					                  <span class="red">首先切换商品所属行业！</span> 
					                  </div>
					                </div>
					                --%>
					              <div <%--class="dashedwp"--%>>
					                <div class="ui-block-line ">
					                  <label><b>商品上传</b></label>
					                  <div style="margin-top:6px;">
					                    <input type="file" name="excelFile" value="选择文件">
					                  </div>
					                </div>
					              </div>
					            </div>
					          </div>
					        </form>
					        <div class="button-line leftlocation">
					          <input type="button" value="确定导入" title="" class="button orange import"/>
					          <input type="button" value="返回" title="" class="button back"/>
					        </div>
					</div>
					
					
					<div class="tag-change-in">
					      <form id="exportSkuForm" action="${ pagebase }/sku/skuExport.htm" method="post">
				          <input type="hidden" name="skuIndustryId" id="skuIndustryId" value="" />
				          <div class="ui-block">
				            <div class="infocustom">此处商品的修改，采用先导出，然后编辑，再次导入的模式。不采用自行创建Excel表的导入。</div>
				            <div class="ui-block-content">
				              <div class="ui-block-line">
				                <label><b>商品所属行业</b></label>
				                <div>
				                  <select id="skuIndustry" class="ui-loxia-default ui-corner-all">
				                    <c:forEach items="${ industryList }" var="industry">
										<option value="${ industry.id  }">${ industry.name }</option>
				                    </c:forEach>
				                  </select>
				                  <span class="red">首先切换商品所属行业！</span>
				                  <%-- <div style=" padding:12px 0;"><a href="javascript:void(0)" id="downLoadTmplOfSkuInfo" class="func-button">下载该行业模板</a></div> --%>
				                </div>
				              </div>
				              <div class="dashedwp">
				                <div class="ui-block-line">
				                  <label><b>基本信息</b></label>
				                  <div>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="code" checked="checked" onclick="return false">商品编码</label>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="upc" checked="checked" onclick="return false">UPC编码</label>
				                   <label class="normal"><input type="checkbox" name="selectCodes" value="salePrice">销售价</label>
				                    <label class="normal"><input type="checkbox" name="selectCodes" value="listPrice">挂牌价</label>
				                  </div>
				                </div>
				               
				                <div class="ui-block-line">
				                  <label><b>销售属性信息</b></label>
				                  <div class="salesProperty">
				                  	<c:forEach items="${ propertyList }" var="property">
					                    <label class="normal"><input type="checkbox" name="selectCodes" value="${property.id }">${property.name }</label>
				                  	</c:forEach>
				                  </div>
				                </div>
				                
				              </div>
				              
				              <div class="dashedwp">
				                <div class="ui-block-line">
				                  <label><b>具体商品编码</b></label>
				                  <div style="position:relative;">
				                    <textarea name="itemCodes" class="ui-loxia-default ui-corner-all" style="width:400px; height:200px;"></textarea>
				                    <div class="textarea_info"> <span class="red">非必填项！</span><br>
				                      <br>为空，导出部分只有字段标题，没有数据。<br>填写商品编码，导出部分含有商品的已有数据。<br>
				                      <br>
				                      <strong>最多填写${batchThreshold}条数据</strong>，每条编码换行填写，格式如下：<br>
				                      Nb_Sample_Code_001<br>
				                      Nb_Sample_Code_002<br>
				                      Nb_Sample_Code_003<br>
				                      Nb_Sample_Code_004<br>
				                      Nb_Sample_Code_005
				                    </div>
				                  </div>
				                </div>
				              </div>
				            </div>
				          </div>
				        </form>
				        <div class="button-line leftlocation">
				          <input type="button" value="确定导出" title="" class="button orange skuexport"/>
				          <input type="button" value="返回" title="" class="button back"/>
				        </div>
					</div>
					<div class="tag-change-in">
					        <form id="importSkuForm" method="post" enctype="multipart/form-data">
					          <div class="ui-block">
					            <div class="infocustom">此处商品的修改，采用先导出，然后编辑，再次导入的模式。不采用自行创建Excel表的导入。</div>
					            <div class="ui-block-content">
					              
					              <div <%--class="dashedwp"--%>>
					                <div class="ui-block-line ">
					                  <label><b>商品上传</b></label>
					                  <div style="margin-top:6px;">
					                    <input type="file" name="skuExcelFile" value="选择文件">
					                  </div>
					                </div>
					              </div>
					            </div>
					          </div>
					        </form>
					        <div class="button-line leftlocation">
					          <input type="button" value="确定导入" title="" class="button orange skuImport"/>
					          <input type="button" value="返回" title="" class="button back"/>
					        </div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
