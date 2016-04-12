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
<script type="text/javascript" src="/scripts/ckeditor/4-4-5/ckeditor.js"></script>
<script type="text/javascript">	
var itemCodeValidMsg = "${itemCodeValidMsg}";
</script>
<script type="text/javascript" src="${base}/scripts/product/item/add-item.js"></script>
<SCRIPT type="text/javascript">
var pdValidCode = "${pdValidCode}";
var zNodes =
[
	{id:0, name:"ROOT",state:"0", open:true,root:"true",nocheck:true},
	<c:forEach var="industry" items="${industryList}" varStatus="status">
	<c:if test="${industry.isShow}">
		{
			id:${industry.id}, 
			pId:${industry.pId},
			name: "${industry.indu_name}",
			open:${industry.open}
			<c:if test="${industry.noCheck}">
				,nocheck:true
			</c:if>
		}
		<c:if test="${!status.last}">,</c:if>
		</c:if>
	</c:forEach>
];
/* var defaultCategoryzNodes  = [
							{id:0, pId:-1, name:"ROOT",	  code:"ROOT", sortNo:1,	  open:true, lifecycle:1},  
                              <c:forEach var="category" items="${categoryList}" varStatus="status">
                              
                              	{id:${category.id}, pId:${category.parentId}, 
                              		name:"${category.name}",
                              		code:"${category.code}", sortNo:${category.sortNo}, 
                      		        <c:if test="${category.id == defaultItemCategory.categoryId}">
                      		            checked:true,
                      		        </c:if>
                              		drag:false, open:true,
                              		lifecycle:${category.lifecycle} } 
                              	<c:if test="${!status.last}">,</c:if>
                              </c:forEach>
                            ]; */

var categoryzNodes  = [
			{id:0, pId:-1, name:"ROOT",	  code:"ROOT", sortNo:1,	  open:true, lifecycle:1},  
              <c:forEach var="category" items="${categoryList}" varStatus="status">
              	
              	{id:${category.id}, pId:${category.parentId}, 
              		name:"${category.name}",
              		code:"${category.code}", sortNo:${category.sortNo}, 
              		drag:false, open:false,
              		lifecycle:${category.lifecycle} } 
              	<c:if test="${!status.last}">,</c:if>
              </c:forEach>
         ];
          var baseUrl='${base}'; 
</SCRIPT>
<script type="text/javascript">
		
		<c:if test="${param.imageUpload==1}">
		editor1.on( 'pluginsLoaded', function(ev)
			{
				if ( !CKEDITOR.dialog.exists( 'myDialog' ) )
				{
					CKEDITOR.dialog.add( 'myDialog', function( editor )
							{
								return {
									title : '图片上传',
									minWidth : 450,
									minHeight : 200,
									contents : [
										{
											id : 'tab1',
											label : 'First Tab',
											title : 'First Tab',
											elements :
											[
												{
													type:'vbox',
													height:'250px',
													children:[
																{	type:'html',
																	style:'width:95%;',
																	html:'<iframe id ="uploadIfr" frameborder="0" name="uploadIfr" width="300" height="100" src="/common/upload.jsp"></iframe>'
																}
															]
												}
											]
										}
									]
								};
							} );
				}
				editor1.addCommand( 'myDialogCmd', new CKEDITOR.dialogCommand( 'myDialog' ) );
				editor1.ui.addButton( 'MyButton',
					{
						label : '图片上传',
						title : '图片上传',
						command : 'myDialogCmd'
					} );
			});
		</c:if>
</script>
<style type="text/css">
.i18n-lang {
	display: none;
}

.cke_button_myDialogCmd .cke_icon {
	display: none !important;
}

.cke_button_myDialogCmd .cke_label {
	display: inline !important;
}
</style>
</head>
<body>

<div class="content-box width-percent100">
    
   <form id="itemForm" name="itemForm" action="/i18n/item/saveItem.json" method="post">
    <input type="hidden" id="industryId" name="itemCommand.industryId"  value=""/>
    <input type="hidden" id="jsonSku" name="itemCommand.jsonSku"  value=""/>
    
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="item.add.manage"/></div>
	<div class="ui-block">
	
	<div id="first">
	<div class="ui-block border-grey">
	   <div class="ui-block-title1"><spring:message code="item.add.industry"/></div>
	    <div class="ui-block ui-block-fleft w240">
        <div class="ui-block-content ui-block-content-lb">
            <div class="tree-control">
                <input type="text" id="key" loxiatype="input" placeholder="<spring:message code='item.add.keyword'/>" />
                <div><span id="search_result"></span></div>
            </div>
            <ul id="industrytreeDemo" class="ztree"></ul>

        </div>
    </div>
	  <div class="button-line">
         <input type="button" value="<spring:message code='system.property.next'/>" class="button orange next" title="<spring:message code='system.property.next'/>"/>
         <input type="button" value="<spring:message code='btn.return'/>" class="button return" title="<spring:message code='btn.return'/>"/>
         
      </div>
	</div>
	</div>
	
	<div id="second" style="display: none">
	
		<div class="ui-block">
	        <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
	            <table>
	                <tr>
	                    <td><label><spring:message code="item.add.selectIndustry"/></label></td>
	                    <td><span id="chooseIndustry"></span></td>
	                    
	                </tr>
	            </table>
	        </div>
	    </div>
	
	
	<div class="ui-block ">
		
	
		 <div class="ui-block-title1"><spring:message code="item.add.addItem"/></div>
	   <div class="ui-block-title1" style="background:#fff;color:#000;"><spring:message code="item.add.info"/></div>
	
	   <div class="ui-block-content border-grey">
	   <div class="ui-block-line ">
         <label ><spring:message code="item.add.code"/></label>
          <div >
              <input type="text" class="fLeft" id="code" name="itemCommand.code"  style="width: 600px" loxiaType="input" value="" mandatory="true" placeholder="<spring:message code='item.add.code'/>"/>
         </div>
         <div id="loxiaTip-r" class="loxiaTip-r" style="display:none">
		 	  <div class="arrow"></div>
		      <div class="inner ui-corner-all codetip" style="padding: .3em .7em; width: auto;"></div>
	     </div>
	   </div>
	   
	  <c:if test="${i18nOnOff == true}">
	  <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	   <div class="ui-block-line ">
         <label ><spring:message code="item.update.name"/></label>
         <input type="text" id="title" name="itemCommand.title.values[${status.index}]" style='width: 600px' loxiaType="input" value="" mandatory="true" placeholder="<spring:message code='item.update.name'/>"/>
         <input class="i18n-lang" type="text" name="itemCommand.title.langs[${status.index}]" value="${i18nLang.key}" />
         <span>${i18nLang.value}</span>
	   </div>
	   	</c:forEach>
	   <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	     <div class="ui-block-line ">
         <label>商品副标题</label>
         <input type="text" id="subTitle" name="itemCommand.subTitle.values[${status.index}]" style='width: 600px' loxiaType="input" value="${ subTilte }" mandatory="false" placeholder="商品副标题"/>
	     <input class="i18n-lang" type="text" name="itemCommand.subTitle.langs[${status.index}]" value="${i18nLang.key}" />
         <span>${i18nLang.value}</span>
	     </div>
	   	</c:forEach>
	    </c:if>
	    <c:if test="${i18nOnOff == false}">
	     <div class="ui-block-line ">
         <label ><spring:message code="item.update.name"/></label>
         <input type="text" id="title" name="itemCommand.title.value" style='width: 600px' loxiaType="input" value="${title}" mandatory="true" placeholder="<spring:message code='item.update.name'/>"/>
	     </div>
	     <div class="ui-block-line ">
         <label>商品副标题</label>
         <input type="text" id="subTitle" name="itemCommand.subTitle.value" style='width: 600px' loxiaType="input" value="${ subTilte }" mandatory="false" placeholder="商品副标题"/>
	     </div>
	   	</c:if>
	   	<c:if test="${isStyleEnable}">
	     	<div class="ui-block-line ">
		         <label ><spring:message code='item.styleName'/></label>
		          <div >
		              <input type="text" id="style" name="itemCommand.style" style="width: 600px" loxiaType="input" value="" mandatory="true" placeholder="<spring:message code='item.add.name'/>"/>
		         </div>
		   </div>
      
		</c:if>
	   
	   
	    <div class="ui-block-line " style = "display:none">
         <label ><spring:message code="item.update.defaultCategory"/></label>
          <div >
              <input type="text" id="defaultCategory" loxiaType="button" readonly placeholder="<spring:message code='item.update.clickCategory'/>"  />
         </div>
	   </div>
	   
	   <div class="ui-block-line ">
         <label></label>
          <div id="chooseDefaultCategory">
          	<c:if test="${ !empty defaultItemCategory }">
			  <div class="${defaultItemCategory.categoryId }">${defaultItemCategory.categoryName }
				  <input type='hidden' name='defaultCategoryId'  value="${defaultItemCategory.categoryId }" />
				  <a href="javascript:void(0);" id="${defaultItemCategory.categoryId }" style="float:right;margin-right: 760px;text-decoration: underline;color:#F8A721"
				   onclick='delDefaultCategroy(this.id)'><spring:message code="item.update.deleteThis"/></a><br/>
			   </div>
			</c:if>
         </div>
        </div>
	   
	   
	   <div class="ui-block-line ">
         <label ><spring:message code="item.add.classification"/></label>
          <div >
              <input type="text" id="category" loxiaType="button" readonly placeholder="<spring:message code='item.add.clickCategory'/>"  />
         </div>
	   </div>
	   
	   <div class="ui-block-line ">
         <label></label>
          <div  id="chooseCategory">
         </div>
	   </div>
	   <div class="ui-block-line ">
         <label></label>
          <div id="chooseDefaultCategoryHid">
			<input type='hidden' name='defCategroyId' id='defCategroyId'  value="" />
         </div>
        </div>
	   <div style="margin-top: 10px"></div>
	   <div class="ui-block-line ">
         <label>商品类型</label>
         <div>
         	<select loxiaType="select" name="itemCommand.type">
				<option value="1">主卖品</option>
         		<option value="0">非卖品</option>
			</select>
         </div>
	   </div>
	   </div>
		<div style="margin-top: 10px"></div>
	   	
	 <div class="ui-block-title1" style="background:#fff;color:#000;"><spring:message code="item.add.generalProperty"/></div>
	
	   <div class="ui-block-content border-grey">
	      <div id="notSalepropertySpace">
		  </div>
	   </div>
	   
	    <div style="margin-top: 10px"></div>
		
	   <div class="ui-block-title1" style="background:#fff;color:#000;"><spring:message code="item.update.itemprice"/></div>
	<!-- <select class="ui-loxia-default ui-corner-all" aria-disabled="false"><option><spring:message code="item.update.red"/></option></select> -->
	   <div class="ui-block-content border-grey">
	         <div class="ui-block-line ">
		         <label ><spring:message code="item.update.salesprice"/></label>
		          <div >
<!-- 		            <select style="width:160px;height:25px" id="salePrice" name="salePrice" onmouseover="selectSalePrices()"> -->
<!-- 				    </select> -->
				    <input style="width:160px;height:25px" decimal='2' loxiaType='number' mandatory='true' id="salePrice" name="itemCommand.salePrice" />
		         </div>
		     </div>
		     <div class="ui-block-line ">
		         <label ><spring:message code="item.update.stickerprice"/></label>
		          <div >
<!-- 		            <select style="width:160px;height:25px" id="listPrice" name="listPrice" onmouseover="selectListPrices()"> -->
<!-- 			    	</select> -->
			    	<input style="width:160px;height:25px" decimal='2' loxiaType='number'  id="listPrice" name="itemCommand.listPrice" />
		         </div>
		     </div>
	   	</div>
	   	
	   	<div style="margin-top: 10px"></div>
	   	
	    <div class="ui-block-title1" style="background:#fff;color:#000;"><spring:message code="item.add.sales"/></div>
	    <c:if test="${i18nOnOff == true}">
	    
        <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
        <c:set var="lang" value="${i18nLang.key}"></c:set>
   	    <c:set value="${status.index}" var="i"></c:set>
		    <div class="ui-block-content border-grey saleInfo" lang="${i18nLang.key}"  <c:if test="${i>0 }"> style="margin-top: 5px;" </c:if> >
		    <div id="propertySpace">
	
		    </div>
		   
	        <div id="exten" class="ui-block-line" style="display:none;">
	          <div  id="extension">
	             <table id="extensionTable" class="border-grey extensionTable" style="padding:5px;">
	               
	              </table>
	         	</div>
	        </div>
		   
		    <div class="ui-block-line ">
		    	<label></label>
	           <div style="clear:both;">
	              	<a href="javascript:void(0)" class="func-button extension" style="height:30px;"><span><spring:message code="item.add.encodingSettings"/> </span></a>
	         </div>
		   </div>
		    
		  </div>
	  </c:forEach>
	  </c:if>
	  
	   <c:if test="${i18nOnOff == false}">
	    <div class="ui-block-content border-grey saleInfo">
	    <div id="propertySpace">

	    </div>
	   
        <div id="exten" class="ui-block-line  " style="display:none;">
          <div  id="extension">
             <table id="extensionTable" class="border-grey" style="padding:5px;">
               
              </table>
         	</div>
        </div>
	   
	    <div class="ui-block-line ">
	    	<label></label>
           <div style="clear:both;">
              	<a href="javascript:void(0)" class="func-button extension" style="height:30px;"><span><spring:message code="item.add.encodingSettings"/> </span></a>
         </div>
	   </div>
	    
	  </div>
	  </c:if>
	  
	  <%-- SEO --%>
	  <div style="margin-top: 10px"></div>
		
	   <div class="ui-block-title1" style="background:#fff;color:#000; width:600px">seo<spring:message code="product.property.lable.search"/></div>
	
	   <div class="ui-block-content border-grey">
		  <c:if test="${i18nOnOff == true}">
	   	  <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
		  <div class="ui-block-line ">
	        <label style="">seo<spring:message code="item.update.seoTitle"/></label>
			<input loxiaType="input" style='width: 600px' name="itemCommand.seoTitle.values[${status.index}]" value="${ seoTitle }" />
			<input class="i18n-lang" type="text" name="itemCommand.seoTitle.langs[${status.index}]" value="${i18nLang.key}" />
        	<span>${i18nLang.value}</span>			
	      </div>
	      </c:forEach>
	       <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	      <div class="ui-block-line ">
	        <label style="">seo<spring:message code="item.update.seoKeywords"/></label>
			<input loxiaType="input" name="itemCommand.seoKeywords.values[${status.index}]" value="${ seoKeywords }" style="width:600px" />
			<input class="i18n-lang" type="text" name="itemCommand.seoKeywords.langs[${status.index}]" value="${i18nLang.key}" />
        	<span>${i18nLang.value}</span>				
	      </div>
	      </c:forEach>
	      <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	      <div class="ui-block-line ">
	        <label style="">seo<spring:message code="item.update.seoDesc"/></label>
	        <textarea rows="10px" name="itemCommand.seoDescription.values[${status.index}]" loxiaType="input" style="width: 600px;">${ seoDescription }</textarea>
	        <input class="i18n-lang" type="text" name="itemCommand.seoDescription.langs[${status.index}]" value="${i18nLang.key}" />
        	<span>${i18nLang.value}</span>		
	      </div>
	     </c:forEach>
	      </c:if>
	      
	      <c:if test="${i18nOnOff == false}">
		  <div class="ui-block-line ">
	        <label style="">seo<spring:message code="item.update.seoTitle"/></label>
			<input loxiaType="input" style='width: 600px' name="itemCommand.seoTitle.value" value="${seoTitle}" />			
	      </div>
	      <div class="ui-block-line ">
	        <label style="">seo<spring:message code="item.update.seoKeywords"/></label>
			<input loxiaType="input" name="itemCommand.seoKeywords.value" value="${ seoKeywords }" style="width:600px" />			
	      </div>
	      <div class="ui-block-line ">
	        <label style="">seo<spring:message code="item.update.seoDesc"/></label>
	        <textarea rows="10px" name="itemCommand.seoDescription.value" loxiaType="input" style="width: 600px;">${ seoDescription }</textarea>
	      </div>
	      </c:if>
	      
	   </div>
	   
	   	
	  <div style="margin-top: 10px"></div>
		
	   <div class="ui-block-title1" style="background:#fff;color:#000;"><spring:message code="item.add.description"/></div>
	
	   <div class="ui-block-content border-grey">
	   	 	
	   <c:if test="${i18nOnOff == true}">
	    <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
		 <div class="ui-block-line ">
	        <label style=""><spring:message code="item.update.sketch"/></label>
			<textarea rows="10px" name="itemCommand.sketch.values[${status.index}]" loxiaType="input" style="width: 600px;">${ sketch }</textarea>			
	     	<input class="i18n-lang" type="text" name="itemCommand.sketch.langs[${status.index}]" value="${i18nLang.key}" />
        	<span>${i18nLang.value}</span>		
	     </div>
	     </c:forEach>
	      
	    <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	     <div class="ui-block-line ">
        <label style=""><spring:message code="item.update.lable.description"/></label>
          <div style="float: left;margin-left:0px">
          <textarea id="editor${status.index}" name="itemCommand.description.values[${status.index}]" rows="20" cols="120">
			${description}
		 </textarea>
		 <input class="i18n-lang" type="text" name="itemCommand.description.langs[${status.index}]" value="${i18nLang.key}" />
		 </div> 
		  <span style="display: block; float: left;" >${i18nLang.value}</span>
          </div>
	    </c:forEach> 
	 </c:if>
	     
     <c:if test="${i18nOnOff == false}">
	  <div class="ui-block-line ">
        <label style=""><spring:message code="item.update.sketch"/></label>
           <div>
				<textarea rows="10px" name="itemCommand.sketch.value" loxiaType="input" style="width: 600px;">${ sketch }</textarea>
           </div>
    	</div>

	   <div class="ui-block-line ">
        <label style=""><spring:message code="item.update.lable.description"/></label>
           <div>
		    <textarea id="editor" name="itemCommand.description.value" rows="20" cols="120">
			${description}
		  </textarea>
          </div>
    	</div>
    	</c:if>
	   	</div>
	   <div style="margin-top: 10px"></div>

	  
	
	
	 <div class="button-line">
	         <input type="button" value="<spring:message code='btn.save'/>" class="button orange submit" title="<spring:message code='btn.save'/>"/>
	         <input type="button" value="<spring:message code='item.add.previous'/>" class="button back"  title="<spring:message code='item.add.previous'/>" />
	     
	</div>
	
	<div style="margin-top: 20px"></div>
	

	</div>
<%-- 	<input type="hidden" loxiaType="input" name="thumbnailConfig" id="thumbnailConfig" value="${thumbnailConfig[0].optionValue }" /> --%>
   </form>
    
    </div>
</div>

<div id="menuContent" class="menuContent" style="display:none; position: absolute; background-color:#f0f6e4;border: 1px solid #617775;padding:3px;">
	<ul id="treeDemo" class="ztree" style="margin-top:0; width:auto; height: 100%;"></ul>
</div>
<div id="defaultMenuContent" class="menuContent" style="display:none; position: absolute; background-color:#f0f6e4;border: 1px solid #617775;padding:3px;">
	<ul id="defaultCategoryTree" class="ztree" style="margin-top:0; width:auto; height: 100%;"></ul>
</div>
</body>
</html>
