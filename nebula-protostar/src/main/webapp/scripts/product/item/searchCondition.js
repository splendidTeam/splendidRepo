$j.extend(loxia.regional['zh-CN'],{  
	    "LABEL_NAME":"筛选条件名称",
	    "LABEL_TYPE":"类型", 
	    "LABEL_PROPERTY":"商品属性",	   
	    "LABEL_SORT":"顺序",
	    "LABEL_STATE":"状态",
	    "LABEL_CONFIG":"操作",
	    "LABEL_CATEGORY":"导航",
	    "TO_UPDATE":"修改",
	    "TO_ENABLE":"启用",
	    "TO_DELETE":"删除",
	    "TO_SETTING":"设置选项",
	    "PRODUCT_INFO_CONFIRM":"确认信息",
	    "PRODUCT_CONFIRM_DISABLE_USER":"确认使条件失效",
	    "PRODUCT_CONFIRM_ENABLE_USER":"确认启用条件", 
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
	 	"INFO_SYSTEM_ERROR":"系统错误" ,
	 	"PRODUCT_CONFIRM_DELETE":"确认删除",
	 	"INFO_DELETE_SUCCESS":"删除成功",
	 	"ITEM_CATEGORY_TIPS":"错误信息",
	 	"ITEM_CATEGORY_CHOOSE_LEAF":"必须选择叶子节点",
	 	"ITEM_CATEGORY_CHOOSE_NOT_ROOT":"不能选择ROOT节点"
});
//鼠标移动隐藏时间
function categoryHideMenu() {
	$j("#categoryMenuContent").fadeOut("fast");
	$j("body").unbind("mousedown", categoryOnBodyDown);
}
//商品组合查询
var searchConditionUrl = base+'/item/itemSearchCondition/searchConditionList.json';
  
var endisableSearchConditionUrl= base+'/item/itemSearchCondition/endisableSearchCondition.json';
var removeSearchConditionUrl= base+'/item/itemSearchCondition/removeSearchCondition.json';
var removeSearchConditionsUrl= base+'/item/itemSearchCondition/removeSearchConditionByIds.json';

var comboProductActionURl=base+'/product/productcomboedit.htm';
var industryListUrl=base+"/item/itemSearchCondition/findIndustryByCategoryId.json";
var propertyListUrl=base+"/item/itemSearchCondition/findPropertyByIndustryId.json";
var addConditionUrl="/i18n/item/itemSearchCondition/managerAddCondition.htm";
var settingConditionUrl="/item/itemSearchCondition/managerSetting.htm";
//查看
var VIEW_URL=base+'/product/combo/view.json';

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
//fenlei
var setting = {
		check : {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},
		view : {
			showIcon : false,
			fontCss : getFontCss
		},
		edit : {
			enable : false,
			showRenameBtn : false
		},
		data : {
			keep : {
				parent : false,
				leaf : false
			},
			key : {
				title : "name"
			},
			simpleData : {
				enable : true
			}
		},
		callback : {
			onClick : onClick,
			onCheck : onCheck
		}
	}; 

function drawPropertyName(data, args, idx){
	var result=""; 
	var name=loxia.getObject("propertyName", data);
	if(name!=null&&name!=""){
		return name;
	}
	return "";
}

/**
 * 批量删除
 */
function confirmDelete(){
	var checkbox=$j("input[name='id']");
	var data=""; 
	  $j.each(checkbox, function(i,val){   
		  if(val.checked){
			  data=data+$j(this).val()+",";
		  }
	 }); 
	  if(data!=""){
		  data=data.substr(0,data.length-1);
		  
		
			nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_DELETE"),function(){
				
					var json={"ids":data}; 
				  	 nps.asyncXhrPost(removeSearchConditionsUrl, json,{successHandler:function(data, textStatus){
						var backWarnEntity = data;
		  				if (backWarnEntity.isSuccess) {
		  					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
		  					refreshData();
		  				} else {
		  					nps.info(nps.i18n("INFO_TITLE_DATA"),backWarnEntity.description);
		  				}
					 }});
			});
	
	  }  
}

function drawEditor(data, args, idx){
	var result="";  
	var id=loxia.getObject("id", data);
	var state=loxia.getObject("lifecycle", data);
	var modify="<a href='"+ addConditionUrl+"?id="+id+"' class='func-button modify'>"+nps.i18n("TO_UPDATE")+"</a>";
	var to_delete="<a href='javascript:void(0);' data='" + id +"' class='func-button delete'>"+nps.i18n("TO_DELETE")+"</a>";
	var to_setting="<a href='"+settingConditionUrl+"?pid="+id+"' data='" + id +"' class='func-button setting'>"+nps.i18n("TO_SETTING")+"</a>";
	if(state==0){
		result+=modify+to_delete+"<a href='javascript:void(0);' val='"+id+"' class='func-button enable'>"+nps.i18n("TO_ENABLE")+"</a>"+to_setting;
	}else if(state=1){ 
		result+=modify+to_delete+"<a href='javascript:void(0);' val='"+id+"' class='func-button disable'>"+nps.i18n("DRAW_NO")+"</a>"+to_setting;
	} 
	return result;
} 
function categoryOnBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "categoryName" || event.target.id == "categoryMenuContent" || $j(event.target).parents("#categoryMenuContent").length>0)) {
		categoryHideMenu();
	}
} 
function drawExpressionText(data, args, idx){	 
	var expressionText = loxia.getObject("groupExpressionText", data);
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

//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}
function drawCheckbox(data, args, idx){
	var result=""; 
	var state=loxia.getObject("lifecycle", data);
	if(state==1 ){
		return "<input name='limitValue' type='checkbox' class='limitValue'  checked='false'  value='' disabled='disabled'/>";
	}
	return "<input name='limitValue' type='checkbox' class='limitValue'  value='' disabled='disabled'/>";
	 
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

function checkboxs(data, args, idx){
	return "<input type='checkbox' name='id' class='chkcid'  value='" + loxia.getObject("id", data)+"'/>";
}

function typeNames(data, args, idx){
	var type=loxia.getObject("type", data);
	var result;
	if(type==1){
		result="一般类型";
	}else if(type==2){
		result="区间类型";
	}else if(type==3){
		result="价格类型";
	}
	return result;
}
	
	
	
function getFontCss(treeId, treeNode) {
		return (!!treeNode.highlight) ? {
			color : "#333",
			"background-color" : "yellow"
		} : {
			color : "#333",
			"font-weight" : "normal",
			"background-color" : ""
		};
}

function onClick(event, treeId, treeNode) {
	var tempTreeId="";
	var tempTreeName="";
	
	if(treeNode.name=="ROOT"){
		nps.info(nps.i18n("ITEM_CATEGORY_TIPS"),nps.i18n("ITEM_CATEGORY_CHOOSE_NOT_ROOT"));
	}else if(treeId!="treeDemo"&&(treeNode.isParent||treeNode.id ==0)){
		nps.info(nps.i18n("ITEM_CATEGORY_TIPS"),nps.i18n("ITEM_CATEGORY_CHOOSE_LEAF"));
	}else{
		var zTree = $j.fn.zTree.getZTreeObj(treeId);
		tempTreeId=treeNode.id;
		tempTreeName=treeNode.name;	
		if(treeNode.checked){
			zTree.checkNode(treeNode, false, null , null);

			if(treeId=="treeDemo"){
				$j("#categoryId").val("");
				$j("#category").val("");
				hideMenu();
			}else{
				$j("#industryId").val("");
				$j("#industry").val("");
				hideMenu();
				var data2=nps.syncXhrPost(propertyListUrl,{industryId:$j("#industryId").val()});
				var list2=data2.propertyList;
				$j("#property").empty();
				$j("#property").append("<option value=''>未选择</option>");
				
			}
		}else{
			zTree.checkNode(treeNode, true, null , null);

			if(treeId=="treeDemo"){
				$j("#categoryId").val(tempTreeId);
				$j("#category").val(tempTreeName);
				hideMenu();
			}else{
				$j("#industryId").val(tempTreeId);
				$j("#industry").val(tempTreeName);
				hideMenu();
				var data2=nps.syncXhrPost(propertyListUrl,{industryId:$j("#industryId").val()});
				var list2=data2.propertyList;
				$j("#property").empty();
				$j("#property").append("<option value=''>未选择</option>");
				for(var i=0;i<list2.length;i++){
					$j("#property").append("<option value='"+list2[i].id+"'>"+list2[i].name+"</option>");
				}
			}
		}
		
		
		
	}
}	

function onCheck(event, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj(treeId);
	tempTreeId=treeNode.id;
	tempTreeName=treeNode.name;	
	if(!treeNode.checked){
		zTree.checkNode(treeNode, false, null , null);

		if(treeId=="treeDemo"){
			$j("#categoryId").val("");
			$j("#category").val("");
			hideMenu();
		}else{
			$j("#industryId").val("");
			$j("#industry").val("");
			hideMenu();
			var data2=nps.syncXhrPost(propertyListUrl,{industryId:$j("#industryId").val()});
			var list2=data2.propertyList;
			$j("#property").empty();
			$j("#property").append("<option value=''>未选择</option>");
			
		}
	}else{
		zTree.checkNode(treeNode, true, null , null);

		if(treeId=="treeDemo"){
			$j("#categoryId").val(tempTreeId);
			$j("#category").val(tempTreeName);
			hideMenu();
		}else{
			$j("#industryId").val(tempTreeId);
			$j("#industry").val(tempTreeName);
			hideMenu();
			var data2=nps.syncXhrPost(propertyListUrl,{industryId:$j("#industryId").val()});
			var list2=data2.propertyList;
			$j("#property").empty();
			$j("#property").append("<option value=''>未选择</option>");
			for(var i=0;i<list2.length;i++){
				$j("#property").append("<option value='"+list2[i].id+"'>"+list2[i].name+"</option>");
			}
		}
	}
	
	
	hideMenu();
}
	
function focusKey(e) {
	if (key.hasClass("empty")) {
		key.removeClass("empty");
	}
}
function blurKey(e) {
	if (key.get(0).value === "") {
		key.addClass("empty");
	}
}
var lastValue = "", nodeList = [], fontCss = {};
function searchNode(e) {
	
	var zTree = $j.fn.zTree.getZTreeObj("tree-left");
	var value = $j.trim(key.get(0).value);
	if(value==""){
		$j("#search_result_left").html("");
		updateNodes(false);
	}
		
		if (key.hasClass("empty")) {
			value = "";
		}
		if (lastValue === value) return;
		lastValue = value;
		if (value === "") return;
		updateNodes(false);
		
	nodeList = zTree.getNodesByParamFuzzy("name", value);
	
	$j("#search_result_left").html(nps.i18n("CATEGORY_FIND")+ nodeList.length+ nps.i18n("CATEGORY_RESULT"));
	
	if (nodeList.length > 0) {
	$j.each(nodeList, function(i, node){      
 		 zTree.expandNode(node.getParentNode(),true, true, true);
		}); 
	}
	updateNodes(true);
	$j("#key-left").focus();
}
function updateNodes(highlight) {
	var zTree = $j.fn.zTree.getZTreeObj("tree-left");
	for( var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}

//移除包含项
function deleteLeftRow(val){
	var tableObj = $j("#tbl-result-include");
	var firstTr = tableObj.find('tbody>tr:first'); 
	tr_id = firstTr.attr("id");
	$j('#row_id'+val).remove();
}
//判断添加项是否已选
function judgeInclude(type,ids) {
	var all = $j("#inc-scope-info-all").val();
	var item = $j("#inc-scope-info-item").val();
	var cate = $j("#inc-scope-info-category").val();
	if(""!= all && all == 0){
		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("EXIST_MESSAGE"));
		return "";
	}
	
}
function hideMenu() {
	$j("#menuContent").fadeOut("fast");
	$j("#menuContent2").fadeOut("fast");
	$j("body").unbind("mousedown", onBodyDown);
	$j("body").unbind("mousedown", onBodyDown2);
}
function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "category" || event.target.id == "menuContent" || $j(event.target).parents("#menuContent").length>0)) {
		hideMenu();
	}
}
function onBodyDown2(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "industry" || event.target.id == "menuContent2" || $j(event.target).parents("#menuContent2").length>0)) {
		hideMenu();
	}
}


$j(document).ready(function() {
	
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();

	searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});
	
	
	
	//通过loxiasimpletable动态获取数据
	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form : "searchForm",
		cols : [
		{label:"<input type='checkbox'/>",width:"5%", template:"checkboxs"},
		{
			name : "name",
			label : nps.i18n("LABEL_NAME"),
			width : "10%"
		}, {
			name : "type",
			label : nps.i18n("LABEL_TYPE"),
			width : "10%" ,
			template:"typeNames"
		},{
			name : "navigationName",
			label : nps.i18n("LABEL_CATEGORY"),
			width : "10%" 
		}, {
			name : "propertyName",
			label : nps.i18n("LABEL_PROPERTY"),
			width : "15%" ,
			template:"drawPropertyName"
		},  {
			name : "sort",
			label : nps.i18n("LABEL_SORT"),
			width : "10%" 
		},  {
			name : "lifecycle",
			label : nps.i18n("LABEL_STATE"),
			width : "10%",
			type:"yesno"
		},{
			label : nps.i18n("LABEL_CONFIG"),
			width : "15%", 			 
			template : "drawEditor"
		}
		
		],
		dataurl : searchConditionUrl
	});
	 
//	$j("#category").click(function(){
//		$j("#dialog-category-select-left").dialogff({type:'open',close:'in',width:'400px',height:'500px'});
//	});
	$j("#category").click(function() {
		var cityObj = $j(this);
		var cityOffset = $j(this).offset();
		$j("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

		$j("body").bind("mousedown", onBodyDown);
	});
	$j("#industry").click(function(){
		var cityObj = $j(this);
		var cityOffset = $j(this).offset();
		$j("#menuContent2").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

		$j("body").bind("mousedown", onBodyDown2);
	});
	
	
	//筛选数据
	 $j(".func-button.search").click(function(){
			 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
			 refreshData();
	   }); 
	
 	 $j(".button.orange.addCondition").click(function(){
	        window.location.href=addConditionUrl;
	 });
 	
 	//分类
 	$j.fn.zTree.init($j("#treeDemo"), setting, zNodes);
	$j.fn.zTree.init($j("#tree-left2"), setting, zNodes2);
 	
	var treeObj = $j.fn.zTree.getZTreeObj("treeDemo");
	var nodes = treeObj.transformToArray(treeObj.getNodes());
//	for(var i = 0;i<nodes.length;i++){
//		if(nodes[i].isParent){
//			nodes[i].nocheck = true;
//			treeObj.refresh();
//		}
//	}
	var treeObj = $j.fn.zTree.getZTreeObj("tree-left2");
	var nodes = treeObj.transformToArray(treeObj.getNodes());
	for(var i = 0;i<nodes.length;i++){
		if(nodes[i].isParent){
			nodes[i].nocheck = true;
			treeObj.refresh();
		}
	}
//分组树筛选
	key = $j("#key-left");
	key.bind("focus", focusKey)
	.bind("blur", blurKey)
	.bind("propertychange", searchNode)
	.bind("input", searchNode);
 	
		   // 禁用单行
		   $j("#table1").on("click",".func-button.disable",function(){
		        var curObject=$j(this);
		        nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_DISABLE_USER"), function(){

		            var json={"id":curObject.attr("val"),"activeMark":0};

		        	nps.syncXhr(endisableSearchConditionUrl, json,{type: "POST"});
		        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISABLE_SUCCESS")); 
		        	refreshData();
		            	
		        });
		    });

		    
	  // 启用单行
		   $j("#table1").on("click",".func-button.enable",function(){
		        var curObject=$j(this);
		        nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_ENABLE_USER"), function(){

		            var json={"id":curObject.attr("val"),"activeMark":1};

		        	nps.syncXhr(endisableSearchConditionUrl, json,{type: "POST"});
		        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ENABLE_SUCCESS")); 
		        	refreshData(); 
		            	
		        });
		    });
		   
		   //删除一行
		   $j("#table1").on("click",".func-button.delete",function(){
		        var curObject=$j(this);
		        nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_DELETE"), function(){

		            var json={"id":curObject.attr("data")};

		        	nps.syncXhr(removeSearchConditionUrl, json,{type: "POST"});
		        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS")); 
		        	refreshData(); 
		            	
		        });
		    });
		   
		   //多行删除
		   $j("#deleteAll").click(function(){
			   confirmDelete();
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
					var type = combo.comboType;
					var name = combo.comboName;
					var typeName = "";
					if (type == 1) {
						$j("#view-block-item .include-list tbody").empty();
						typeName = "商品";
						$j("#view-block-item table").find("th[id='item-price']").show();
						$j("#view-block-item .txt-type").val(typeName);
						$j("#view-block-item .txt-name").val(name);
						
						$j.each(details.itemList, function(i, obj) {
							var html = "<tr class='" + ((i%2==0)?"even":"odd") + "'><td>" 
							+ obj.title + "</td>";
							html +="<td>" + obj.salePrice + "</td>";
							html +="</tr>";
							$j("#view-block-item .include-list tbody").append(html);
						});
						
						$j("#view-block-item").dialogff({type:'open',close:'in',width:'1000px',height:'550px'});
					
					} else if (type == 2) {
						$j("#view-block-category .include-list tbody").empty();
						$j("#view-block-category .exclude-list tbody").empty();
						typeName = "分类";
						$j("#view-block-category table").find("th[id='item-price']").hide();
						$j("#view-block-category table").find("th[id='exc-item-price']").show();
						$j("#view-block-category .txt-type").val(typeName);
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
						
						if (details.itemList) {
							$j.each(details.itemList, function(i, obj) {
								var html = "<tr class='" + ((i%2==0)?"even":"odd") + "'><td>" 
									+ obj.title + "</td>";
								if(type == 2){
									html +="<td>" + obj.salePrice + "</td>";
								}
								html +="</tr>";
								$j("#view-block-category .exclude-list tbody").append(html);
							});
						}
						
						$j("#view-block-category").dialogff({type:'open',close:'in',width:'1000px',height:'550px'});
					
					} else if (type == 4) {
						$j("#view-block-all .include-list tbody").empty();
						typeName = "自定义";
						$j("#view-block-all table").find("th[id='item-price']").hide();
						$j("#view-block-all .txt-type").val(typeName);
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
			
			//浮层确定
			$j("#dialog-category-select-left2 .btn-ok").click(function() {
				var text = "";
				var ids = "";
				var zTree = $j.fn.zTree.getZTreeObj("tree-left2");
				nodes = zTree.getCheckedNodes(true);

				for (var i=0, l=nodes.length; i<l; i++) {	
		        	if(""!= nodes[i].id){
		        		ids +=",";
		        	}
		        	ids += nodes[i].id ;
					
					if("" !=text){
						text +=",";
					}
					text += nodes[i].name;
				}
//				text = text.substring(0, text.length - 1);
//				ids = ids.substring(0, ids.length - 1);
				$j("#industry").val(text);
				$j("#industryId").val(ids.substring(1));
				$j("#dialog-category-select-left2").dialogff({type : 'close'});
				
				
			});
			
			//浮层确定
			$j("#dialog-category-select-left .btn-ok").click(function() {
				var text = "";
				var ids = "";
				var zTree = $j.fn.zTree.getZTreeObj("tree-left");
				nodes = zTree.getCheckedNodes(true);

				for (var i=0, l=nodes.length; i<l; i++) {	
		        	if(""!= nodes[i].id){
		        		ids +=",";
		        	}
		        	ids += nodes[i].id ;
					
					if("" !=text){
						text +=",";
					}
					text += nodes[i].name;
				}
//				text = text.substring(0, text.length - 1);
//				ids = ids.substring(0, ids.length - 1);

				$j("#category").val(text);
				$j("#categoryId").val(ids.substring(1));
				$j("#dialog-category-select-left").dialogff({type : 'close'});
				
				
			});
			
			$j("#dialog-category-select-left .selectCategorys-left").on("click", function(){
				var zTree = $j.fn.zTree.getZTreeObj("tree-left");
				nodes = zTree.getCheckedNodes(true);

				var id="",name="",parentNode;
				for (var i=0, l=nodes.length; i<l; i++) {	
		        	if(""!= id){
		        		id +=",";
		        	}
					id += nodes[i].id ;
					
					if("" !=name){
						name +=",";
					}
					name += nodes[i].name;
				}
		    	$j("#scope-info-left").attr("value",name);
		    	$j("#scope-info-id-left").attr("value",id);
		    	$j("#dialog-category-select-left").dialogff({type : 'close'});
			});
			
			$j("#property").change(function(){
				$j("#propertyId_").val($j(this).val());
			});
			
			var cate=$j("#categoryId").val();
			var indu=$j("#industryId").val();
			var prop=$j("#propertyId_").val();
			
			if(cate!=""){
				var treeObj = $j.fn.zTree.getZTreeObj("treeDemo");
				var node = treeObj.getNodeByParam("id", cate, null);
				treeObj.checkNode(node, true, null , true);
			}
			
			if(indu!=""){
				var treeObj2 = $j.fn.zTree.getZTreeObj("tree-left2");
				var node2 = treeObj2.getNodeByParam("id", indu, null);
				treeObj2.checkNode(node2, true, null , true);
			}
			
			if(prop!=""){
				$j("#property").val(prop);
			}
			
			refreshData();
			
});

