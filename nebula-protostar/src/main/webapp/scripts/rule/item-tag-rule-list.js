$j.extend(loxia.regional['zh-CN'],{  
	    "LABEL_PRODUCT_CREATETIME":"创建时间",
	    "LABEL_PRODUCT_CREATEMEMBER":"创建人员", 
	    "LABEL_PRODUCT_OPERATE":"操作",	   
	    "LABEL_PRODUCT_GROUPNAME":"筛选器名称",
	    "LABEL_PRODUCT_TYPE":"生效",
	    "LABEL_PRODUCT_GROUPEXPRESSIONTEXT":"表达式名称",
	    "LABEL_PRODUCT_GROUPEXPRESSION":"商品组合表达式",
	    "TO_UPDATE":"修改",
	    "TO_ENABLE":"启用",
	    "PRODUCT_INFO_CONFIRM":"确认信息",
	    "PRODUCT_CONFIRM_DISABLE_USER":"确认变该组合失效",
	    "PRODUCT_CONFIRM_ENABLE_USER":"确认启用该组合", 
	    "INFO_TITLE_DATA":"提示信息",
	    "INFO_ENABLE_SUCCESS":"启用成功",
	    "INFO_DISABLE_SUCCESS":"失效成功", 
	    "INFO_ENABLE_FAIL":"启用失败",
	    "INFO_DISABLE_FAIL":"失效失败",
	    "USER_FORM_CHECK_ERROR":"错误信息",
	    "NO_CATEGORY":"无",  
	    "NO_DATA":"无数据",
	    "DRAW_FIND":"查看",
	    "DRAW_NO":"失效",
	    "INFO_COMBO_INEXISTED":"该筛选器不存在" ,
	 	"INFO_SYSTEM_ERROR":"系统错误" 
});
//鼠标移动隐藏时间
function categoryHideMenu() {
	$j("#categoryMenuContent").fadeOut("fast");
	$j("body").unbind("mousedown", categoryOnBodyDown);
}
//商品组合查询
var itemTagRuleListUrl = base+'/product/customProductComboList.json'; 
  
var enableOrDisableProductGroupUrl= base+'/product/enableOrDisableProductGroup.json';

var comboProductActionURl=base+'/product/productcomboedit.htm';
//查看
var VIEW_URL=base+'/product/combo/view.json';

function drawEditor(data, args, idx){
	var result="";  
	var id=loxia.getObject("id", data);
	 var state=loxia.getObject("lifecycle", data);
	 var modify="<a href='javascript:void(0);' val='"+id+"' class='func-button modify'>"+nps.i18n("TO_UPDATE")+"</a>";
	 var view="<a href='javascript:void(0);' data='" + id +"' class='func-button view'>"+nps.i18n("DRAW_FIND")+"</a>";
	 if(state==0){
		  result+=view+modify+"<a href='javascript:void(0);' val='"+id+"' class='func-button enable'>"+nps.i18n("TO_ENABLE")+"</a>";
	  }else if(state=1){ 
		  result+=view+modify+"<a href='javascript:void(0);' val='"+id+"' class='func-button disable'>"+nps.i18n("DRAW_NO")+"</a>";
	 } 
	return result;
} 
function categoryOnBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "categoryName" || event.target.id == "categoryMenuContent" || $j(event.target).parents("#categoryMenuContent").length>0)) {
		categoryHideMenu();
	}
} 
function drawExpressionText(data, args, idx){	 
	var expressionText = loxia.getObject("text", data);
	var text =""; 
	if(expressionText==null)
	return text;
	 
	var moveText="";
	if(expressionText.indexOf("【排除】")!=-1){ 
	  expressionText = expressionText.replace("【排除】","<br/>【排除】"); 
	}
	 moveText=expressionText;
	if(expressionText.length>50){
		text +=expressionText.substring(0,50)+"......";
	}else{
		text +=expressionText;
	}  
	text= "<label class='movetitletd'>"+ 
		"<div custtalk='"+moveText+"' class='movetitle'>" 
		+text+
     "</div> </label>";
	
	return text;
}
//分类列表
var categorySetting = {
		treeNodeKey : "id",
      treeNodeParentKey : "parentId",
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},  
		view: {
			dblClickExpand: false,
			showIcon:false,
			fontCss:function(treeId,treeNode){
				if(treeNode.lifecycle==0){
					return {color:"#666"};
				}else{
					return {color:"#000"};
				}
			}
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onClick: categoryonClick,
			onCheck: categoryonCheck
		}
	};
 
//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}

function drawCheckbox(data, args, idx){
	var state=loxia.getObject("lifecycle", data);
	if(state==1 ){
		return "<span class='ui-pyesno ui-pyesno-yes limitValue' value='" + loxia.getObject("limitValue", data)+"'></span>";
	}
	return "<span class='ui-pyesno ui-pyesno-no limitValue'   value='" + loxia.getObject("limitValue", data)+"'></span>";
	 
}
//分类点击函数 获得树结构
function categoryonClick(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("categoryDemo");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}
//分类点击函数 获得树结构值
function categoryonCheck(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("categoryDemo"),
	nodes = zTree.getCheckedNodes(true),
	v = "";
	id="";
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
		id+= nodes[i].id + ",";
	}
	if (v.length > 0 ) v = v.substring(0, v.length-1);
	if (id.length > 0 ) id = id.substring(0, id.length-1); 
	var cityObj = $j("#categoryName"); 
	cityObj.attr("value", v);
	$j("#categoryId").attr("value",id);
	categoryHideMenu();
}

//获取日期格式
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
} 

//通过loxiasimpletable动态获取数据
$j(document).ready(function() {
	
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();

	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [
		 {
			name : "name",
			label : nps.i18n("LABEL_PRODUCT_GROUPNAME"),
			width : "9%" 
			
		}, {
			name : "lifecycle",
			label : nps.i18n("LABEL_PRODUCT_TYPE"),
			width : "5%" ,
			template:"drawCheckbox"
		}, {
			name : "text",
			label : nps.i18n("LABEL_PRODUCT_GROUPEXPRESSIONTEXT"),
			width : "15%" ,
			template:"drawExpressionText"
		}, 
		/**
		 * 何波 
		 * {
			name : "expression",
			label : nps.i18n("LABEL_PRODUCT_GROUPEXPRESSION"),
			width : "15%" 
		}, */
		{
			name : "createTime",
			label : nps.i18n("LABEL_PRODUCT_CREATETIME"),
			width : "9%",
			formatter:"formatDate",
			sort:["r.create_time asc","r.create_time desc"]
		}, {
			name : "createName",
			label : nps.i18n("LABEL_PRODUCT_CREATEMEMBER"),
			width : "9%" 
		},{
			name : nps.i18n("LABEL_PRODUCT_OPERATE"),
			label : nps.i18n("LABEL_PRODUCT_OPERATE"),
			width : "10%", 			 
			template : "drawEditor"
		} ],
		dataurl : itemTagRuleListUrl
	});
	refreshData();
	 
	//筛选数据
	 $j(".func-button.search").click(function(){
			 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
			 refreshData();
	   }); 
	
 	 $j(".button.orange.addcomboproduct").click(function(){
	        window.location.href=comboProductActionURl+"?cmbno=&type=create";
	    });
	 //分类
		$j.fn.zTree.init($j("#categoryDemo"), categorySetting, category_ZNodes);    
		
		$j("#categoryName").click(function() {
			var cityObj = $j(this);
			var cityOffset = $j(this).offset();
			$j("#categoryMenuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

			$j("body").bind("mousedown", categoryOnBodyDown);
		});
		
		  
		   // 禁用单行
		   $j("#table1").on("click",".func-button.disable",function(){
		        var curObject=$j(this);
		        nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_DISABLE_USER"), function(){

		            var json={"id":curObject.attr("val"),"lifecycle":0};

		        	var _d = nps.syncXhr(enableOrDisableProductGroupUrl, json,{type: "GET"});
		        	if(_d.isSuccess){
		        		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISABLE_SUCCESS")); 
		        		  refreshData(); 
		        	}
		        	else{
		        		nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),nps.i18n("INFO_DISABLE_FAIL"));
		        	}
		            	
		        });
		    });

		    
	  // 启用单行
		   $j("#table1").on("click",".func-button.enable",function(){
		        var curObject=$j(this);
		        nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_ENABLE_USER"), function(){

		            var json={"id":curObject.attr("val"),"lifecycle":1};

		        	var _d = nps.syncXhr(enableOrDisableProductGroupUrl, json,{type: "GET"});
		        	if(_d.isSuccess){
		        		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ENABLE_SUCCESS")); 
		        		  refreshData(); 
		        	}
		            	
		        	else{
		        		nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),nps.i18n("INFO_ENABLE_FAIL"));
		        	}
		            	
		        });
		    });
		   refreshData();  
		    	
		   //-------------------------------------------------移动弹出表达式名称--------------------------------
			var ssetim=0;
			$j("<div class='moveoutdialog' style='display:none; clear:both; position:absolute; width:500px; height:auto; padding:20px; background:#fff; border:1px solid #888888; line-height:1.5; z-index:50; border-radius:5px;'></div>").appendTo("body").mouseenter(function(){
				clearTimeout(ssetim);
			}).mouseleave(function(){
				ssetim=setTimeout(function(){$j(".moveoutdialog").stop(true,true).fadeOut(100);},500);
			});
		
			$j(".ui-block").on("mouseenter",".movetitle",function(e){
				e.stopPropagation();
				clearTimeout(ssetim);
				var ex=parseInt($j(this).parent("label").offset().left);
				var ey=parseInt($j(this).parent("label").offset().top);
				
				var thiscust=$j(this).attr("custtalk"); 
				$j(".moveoutdialog").stop(true,true).fadeOut(100).html("").append($j(this).attr("custtalk"));

				$j(".moveoutdialog").css({"left":ex,"top":ey+60}).stop(true,true).fadeIn(300);
			}).on("mouseleave",".movetitle",function(e){
				ssetim=setTimeout(function(){$j(".moveoutdialog").stop(true,true).fadeOut(100);},500);
			});
			
			
			
			//查看	
			$j(".content-box").on("click", ".view", function() {
				
				var id = $j(this).attr("data");
				var data = nps.syncXhrPost(VIEW_URL, {id:id});
				if (data.isSuccess) {
					var details = data.details;
					var combo = data.combo;
					var type = combo.type;
					var name = combo.name;
					if (type == 1) {
						$j("#view-block-item .include-list tbody").empty();
						$j("#view-block-item table").find("th[id='item-price']").show();
						$j("#view-block-item .txt-type").attr('disabled', 'disabled').val(type);
						$j("#view-block-item .txt-name").val(name);
						
						$j.each(details.itemList, function(i, obj) {
							var title= obj.title;
							var code= obj.code;
							var itemid = obj.id;
							var url;
							var codeHtml ='[' + code + '] ';
							if(pdp_base_url.indexOf("code")>-1){
								url = pdp_base_url.substring(0,pdp_base_url.length-4);
								url = url.replace("(@)",code);
							}else{
								url = pdp_base_url.substring(0,pdp_base_url.length-6);
								url = url.replace("(@)",itemid);
							}
							
							var name_url = '<a href="'+url+'" target="_blank" class="func-button" >'+codeHtml+title + '</a>';
							var html = "<tr class='" + ((i%2==0)?"even":"odd") + "'><td>" 
							+name_url+ "</td>";
							html +="<td>" + obj.salePrice + "</td>";
							html +="</tr>";
							$j("#view-block-item .include-list tbody").append(html);
						});
						
						$j("#view-block-item").dialogff({type:'open',close:'in',width:'1000px',height:'550px'});
					
					} else if (type == 2) {
						$j("#view-block-category .include-list tbody").empty();
						$j("#view-block-category .exclude-list tbody").empty();
						$j("#view-block-category table").find("th[id='item-price']").hide();
						$j("#view-block-category table").find("th[id='exc-item-price']").show();
						$j("#view-block-category .txt-type").attr('disabled', 'disabled').val(type);
						$j("#view-block-category .txt-name").val(name);
						
						$j.each(details.categoryList, function(i, obj) {
							if (obj.id == 0) {	//全场
								obj.name = "全场";
							}
							var html = "<tr class='" + ((i%2==0)?"even":"odd") + "'><td>" 
							+ obj.name + "</td>";
							html +="</tr>";
							$j("#view-block-category .include-list tbody").append(html);
						});
						
						if (details.excCategoryList) {
							$j.each(details.excCategoryList, function(i, obj) {
								var html = "<tr class='" + ((i%2==0)?"even":"odd") + "'><td>" 
									+ obj.name + "</td>";
								html +="<td></td>";
								html +="</tr>";
								$j("#view-block-category .exclude-list tbody").append(html);
							});
						}
						if (details.itemList) {
							$j.each(details.itemList, function(i, obj) {
								
								var itemid = obj.id;
								var url;
								var codeHtml='[' + obj.code + '] ';
								if(pdp_base_url.indexOf("code")>-1){
									url = pdp_base_url.substring(0,pdp_base_url.length-4);
									url = url.replace("(@)",obj.code);
								}else{
									url = pdp_base_url.substring(0,pdp_base_url.length-6);
									url = url.replace("(@)",itemid);
								}
								var name_url = '<a href="'+url+'" target="_blank" class="func-button" >'+codeHtml+obj.title + '</a>';
								var html = "<tr class='" + ((i%2==0)?"even":"odd") + "'><td>" 
									+ name_url + "</td>";
								if(type == 2){
									html +="<td>" + obj.salePrice + "</td>";
								}
								html +="</tr>";
								$j("#view-block-category .exclude-list tbody").append(html);
							});
						}
						
						$j("#view-block-category").dialogff({type:'open',close:'in',width:'1000px',height:'550px'});
					
					}else if(type == 3){
						$j("#view-block-custom .include-list tbody").empty();
						$j("#view-block-custom table").find("th[id='item-price']").hide();
						$j("#view-block-custom .txt-type").attr('disabled', 'disabled').val(type);
						$j("#view-block-custom .txt-name").val(name);
						
						$j.each(details.customizeFilterClassList, function(i, details) {
							var html = "<tr class='" + ((i%2==0)?"even":"odd") + "'><td>" 
							+ details.scopeName + "</td></tr>";
							$j("#view-block-custom .include-list tbody").append(html);
						});
						
						$j("#view-block-custom").dialogff({type:'open',close:'in',width:'1000px',height:'550px'});
					}else if (type == 4) {
						$j("#view-block-all .include-list tbody").empty();
						$j("#view-block-all table").find("th[id='item-price']").hide();
						$j("#view-block-all .txt-type").attr('disabled', 'disabled').val(type);
						$j("#view-block-all .txt-name").val(name);
						
						$j.each(data.detailsList, function(i, details) {
							var html = "<tr class='" + ((i%2==0)?"even":"odd") + "'><td>" 
							+ details.name + "</td>";
							html +="</tr>";
							$j("#view-block-all .include-list tbody").append(html);
						});
						
						$j("#view-block-all").dialogff({type:'open',close:'in',width:'1000px',height:'550px'});
					}
				} else {
					if (data.errorCode == 7003) {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_COMBO_INEXISTED"));
					} else {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SYSTEM_ERROR"));
					}
				}
			});
			
			//浮层确定
			$j("#view-block-item .btn-ok").click(function() {
				$j("#view-block-item").dialogff({type : 'close'});
			});
			$j("#view-block-category .btn-ok").click(function() {
				$j("#view-block-category").dialogff({type : 'close'});
			});
			$j("#view-block-custom .btn-ok").click(function() {
				$j("#view-block-custom").dialogff({type : 'close'});
			});
			$j("#view-block-all .btn-ok").click(function() {
				$j("#view-block-all").dialogff({type : 'close'});
			});
	  //修改	
	  $j("#table1").on("click",".func-button.modify",function(){
	        var curObject=$j(this);
	        var id = curObject.attr("val");
	        var url =comboProductActionURl+"?cmbno="+id+"&type=modify";
            var json={"comboId":id};
        	var result = nps.syncXhr(base+"/product/combo/validatItemTagRule.json", json,{type: "GET"});
        	if(result==true || result=="true"){
        		window.location.href=url;
        	}else{
        		nps.info(nps.i18n("INFO_TITLE_DATA"),"商品筛选器已经在生效的促销活动引用,不能修改!");
        	}
	    });
});

