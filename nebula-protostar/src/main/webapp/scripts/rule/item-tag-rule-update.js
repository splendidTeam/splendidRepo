/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'],{
	"NO_DATA":"无数据",
	"INFO_TITLE_DATA":"提示信息",
	 "LABEL_ITEM_CODE":"商品编码",
	 "LABEL_ITEM_TITLE":"商品名称",
	 "LABEL_ITEM_CATEGORY":"所属分类",
	 "LABEL_ITEM_SALE_PRICE":"标价",
	 "LABEL_ITEM_LIST_PRICE":"贴牌价",
	 "EXIST_MESSAGE":"已包含此项，不必重复添加",
	"INFO_NO_NAME":"请输入组合名称",
	"INFO_NO_DATA":"包含列表不能为空",
	"INFO_SUCCESS":"修改成功",
	"INFO_REPEATED_NAME":"组合名称已被使用，请更换其他名称",
	"INFO_SYSTEM_ERROR":"系统错误，请联系管理员",
	
	"INFO_REPEATED_ITEM":"您已经勾选过部分商品，是否合并？",
	"INFO_REPEATED_CATE":"您已经勾选过部分分类，是否合并？",
	"INFO_REPEATED_CUSTOM":"您已经勾选过部分自定义组合，是否合并？",
	"INFO_INCLUDE_ERROR2":"排除列表中含有不被包含的商品，请重新编辑",
	"INFO_INCLUDE_ERROR3":"选全体，排除列表不能为空，请重新编辑",
});



/* ------------------------------------------------- 全局变量 ------------------------------------------------- */
var SEARCH_RESULT_INPUT = null;	//包含列表与排除列表的查询结果输入框
var IS_CATEGORY_IN = true;	// 分类类型时，根据此变量判断是包含还是排除

/* ------------------------------------------------- URL ------------------------------------------------- */
//已分类商品列表url
var itemCtListUrl = base + '/product/scope/itemSelect.json';
//保存自定义组合
var updateComboUrl = base + "/product/scope/updateCombo.json";
//查询list
var PREVIOUS_URL = base + "/product/productcombolist.htm";

var CHECK_CATEGORY_URL = base + "/product/combo/check.json";

var USER_DEFINED_LIST_URL = base + '/product/combo/userDefinedList.json';


//fenlei
var setting = {
		check : {
			enable: true
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

//函数
//样式？重构
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
	
	if(treeNode.isParent||treeNode.id ==0){
		nps.info(nps.i18n("ITEM_CATEGORY_TIPS"),nps.i18n("ITEM_CATEGORY_CHOOSE_LEAF"));
	}else{
		tempTreeId=treeNode.id;
		tempTreeName=treeNode.name;		
	}
}	

function onCheck(event, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("tree-left");
	nodes = zTree.getCheckedNodes(true);
	
	var tempTreeName="";

	if(treeNode.isParent||treeNode.id ==0){
		nps.info(nps.i18n("ITEM_CATEGORY_TIPS"),nps.i18n("ITEM_CATEGORY_CHOOSE_LEAF"));
	}else{
		tempTreeName=treeNode.name;
		id="";
		for (var i=0, l=nodes.length; i<l; i++) {	
			id+= nodes[i].id + ",";
		}
		if (id.length > 0 ) id = id.substring(0, id.length-1); 
	}
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
function judgeInclude(obj,id) {
	var flag=true;
	if(obj != null){
		obj.each(function(i, dom) {
			incStr = $j(dom).attr("data");
			if($j.trim(id)==$j.trim(incStr)){
				flag=false;
				return false;
			}
		});
	}
	 if(!flag) return 'true';
}
/**-------------------------------right----------------------------------------*/
//table1
function drawCheckbox(data, args, idx){
	return "<input name='chedkId' type='checkbox' class='checkId'  value='" + loxia.getObject("id", data)+"'/>";
}

function formatCategoryNames(data, args, idx){
	var propertyNameArray =loxia.getObject("categoryNames", data);
	
	var categoryNames =propertyNameArray;
	
	if(propertyNameArray==null||propertyNameArray==''){
		categoryNames= nps.i18n("NO_CATEGORY");
	}
	var hiddenCategoryNameInput ="<input type='hidden' id='itemCategoryName_"+loxia.getObject("id", data)+"' value='"+propertyNameArray+"' />";
	categoryNames +=hiddenCategoryNameInput;
	
	return categoryNames;
}
function getTitle(data, args, idx){
	var title =loxia.getObject("title", data);
	
	var name =title;
	
	if(title==null||title==''){
		name= nps.i18n("NO_CATEGORY");
	}	
	var hiddenNameInput ="<input type='hidden' id='title_"+loxia.getObject("id", data)+"' value='"+title+"' />";
	name +=hiddenNameInput;
	return name;
}

function getCode(data, args, idx){
	var code =loxia.getObject("code", data);
	var hiddenNameInput ="<input type='hidden' id='code_"+loxia.getObject("id", data)+"' value='"+code+"' />";
	code +=hiddenNameInput;
	return code;
}
function getSalePrice(data, args, idx){
	var title =loxia.getObject("salePrice", data);
	
	var name =title;
	
	if(title==null||title==''){
		name= nps.i18n("NO_CATEGORY");
	}	
	var hiddenNameInput ="<input type='hidden' id='salePrice_"+loxia.getObject("id", data)+"' value='"+title+"' />";
	name +=hiddenNameInput;
	return name;
}

function refreshData(){
	$j("#table1").loxiasimpletable("refresh");
    
}


/* ------------------------------------------------- ready ------------------------------------------------- */
$j(document).ready(function() {

	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();  
    
    //分组树
	$j.fn.zTree.init($j("#tree-left"), setting, zNodes);
	
	var treeObj = $j.fn.zTree.getZTreeObj("tree-left");
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
    
	//商品列表
	$j("#table1").loxiasimpletable({
		page : true,
		size : 10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [ {
			label : "<input type='checkbox'  />",
			width : "3%",
			template:"drawCheckbox"
		},{
			name : "code",
			label : nps.i18n("LABEL_ITEM_CODE"),
			width : "10%",
			template : "getCode"
		},
		{
			name : "title",
			label : nps.i18n("LABEL_ITEM_TITLE"),
			width : "10%",
			template:"getTitle",
		},  {
			name : "categoryNames",
			label : nps.i18n("LABEL_ITEM_CATEGORY"),
			width : "20%",
			template : "formatCategoryNames"
		},
		{
			name : "salePrice",
			label : nps.i18n("LABEL_ITEM_SALE_PRICE"),
			width : "10%",
			template:"getSalePrice",
		}, {
			name : "listPrice",
			label : nps.i18n("LABEL_ITEM_LIST_PRICE"),
			width : "12%",
		} ],
		dataurl : itemCtListUrl
	});
	
	
	refreshData();
	//筛选
	$j(".func-button.search").click(function(){
		 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	});
	
	
	//删除按钮
	$j(".tbl-result tbody").on("click", ".btn-remove", function() {
		$j(this).parent().parent().remove();
	});
		
	
	$j(".slt-type").change();	//隐藏查询按钮
	
	//商品‘查询’按钮
	$j("#item-select-lable .btn-search").click(function() {
		$j("#dialog-item-select").dialogff({type:'open',close:'in',width:'1000px',height:'600px'});
	});
	//分类包含‘查询’按钮
	$j("#result-include .btn-search").click(function() {
		IS_CATEGORY_IN = true;
		$j('#select-all-item-id').show();
		$j("#dialog-category-select-left").dialogff({type:'open',close:'in',width:'400px',height:'500px'});
	});
	
	//分类排除查询
	$j("#result-exclude .btn-search").click(function() {
		IS_CATEGORY_IN = false;
		var val = + $j(this).siblings(".slt-type").val();
		$j('#select-all-item-id').hide();
		if (val == 0) {	//全场
			
		} else if (val == 1) {	//商品类型
			$j("#dialog-item-select").dialogff({type:'open',close:'in',width:'1000px',height:'600px'});
		} else if (val == 2) {	//分类类型
			$j("#dialog-category-select-left").dialogff({type:'open',close:'in',width:'400px',height:'500px'});
		}
	});
	//自定义‘查询’按钮
	$j("#combo-select-lable .btn-search").click(function() {
		$j("#dialog-combo-select").dialogff({type:'open',close:'in',width:'530px',height:'300px'});
	});
	
	//自定义浮层下拉框
	$j("#dialog-combo-select select").change(function() {
		var type = + $j("#dialog-combo-select select").val();
//		alert(type);
		var chks = $j("#dialog-combo-select :checkbox");
		chks.parent().parent().hide();
		switch (type) {
		case 1:
			chks.filter("[data_type='1']").parent().parent().show();
			break;
		case 2:
			chks.filter("[data_type='2']").parent().parent().show();
			break;
		case 3:
			chks.filter("[data_type='3']").parent().parent().show();
			break;
		case 4:
			chks.filter("[data_type='4']").parent().parent().show();
			break;
		default:
			break;
		}
	});
	$j("#dialog-combo-select select").change();
	
	//分类选择器‘确定’按钮点击事件
	$j("#dialog-category-select-left .btn-ok").click(function() {
		if(IS_CATEGORY_IN){
			
			var val =  $j("#category-select-lable .slt-type-inc .slt-type").val();
			inc = $j("#category-select-lable #tbl-result-include tbody td[data]");
			var zTree = $j.fn.zTree.getZTreeObj("tree-left");
			var isConflict = false;
			if("true" == judgeInclude(inc,0)){//已选全场
				nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_REPEATED_CUSTOM"), function() {
					$j("#dialog-category-select-left :checkbox").prop("checked", false);
					$j("#dialog-category-select-left .tree-control").show();
					zTree.checkAllNodes(false);
					$j("#dialog-category-select-left").dialogff({type : 'close'});
				});
			}else if($j("#dialog-category-select-left #select-all-item").attr("checked")=="checked"){//全场
				isConflict = true;
//			alert(inc.length);
				if (isConflict && inc.length>0) {
					nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_REPEATED_CUSTOM"), function() {
						if("true" == judgeInclude(inc,0)){//已选全场
							return false;
						}else{//加全场，删其他
							$j("#category-select-lable #tbl-result-include tbody td[data]").parent().remove();
							
							ids = 0 ;
							text = "全场";
							var html = "<tr><td tip='";
							html += "全场' data='0'>全场";
							html += "</td>";
							html += "<td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a>";
							$j("#category-select-lable #tbl-result-include").find("tbody").append(html);
						}
						/* 清空选择器的输入项 */
						$j("#dialog-category-select-left :checkbox").prop("checked", false);
						$j("#dialog-category-select-left .tree-control").show();
						$j("#dialog-category-select-left").dialogff({type : 'close'});
					});
				}else{
					ids = 0 ;
					text = "全场";
					var html = "<tr><td tip='";
					html += "全场' data='0'>全场";
					html += "</td>";
					html += "<td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a>";
					$j("#category-select-lable #tbl-result-include").find("tbody").append(html);
					$j("#dialog-category-select-left :checkbox").prop("checked", false);
					$j("#dialog-category-select-left .tree-control").show();
					$j("#dialog-category-select-left").dialogff({type : 'close'});
				}
			}else{
				var ids = "";
				var text = "";
				
				nodes = zTree.getCheckedNodes(true);
				for (var i=0, l=nodes.length; i<l; i++) {	
					ids = nodes[i].id ;
					if("true" == judgeInclude(inc,ids)){
						isConflict = true;
					}
				}
				
				if (isConflict) {
					nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_REPEATED_CATE"), function() {
						for (var i=0, l=nodes.length; i<l; i++) {	
							ids = nodes[i].id ;
							text = nodes[i].name;
							var html = "<tr><td tip='";
							html += "分类：" + text + "' data='" + ids + "'>分类：" + text;
							html += "</td>";
							html += "<td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a>";
							if( "true" == judgeInclude(inc,ids)){
							}else{
								$j("#category-select-lable #tbl-result-include").find("tbody").append(html);
							}
						}
						$j("#dialog-category-select-left").dialogff({type : 'close'});
						zTree.checkAllNodes(false);
					});
				}
				
				if (! isConflict) {
					for (var i=0, l=nodes.length; i<l; i++) {	
						ids = nodes[i].id ;
						text = nodes[i].name;
						var html = "<tr><td tip='";
						html += "分类：" + text + "' data='" + ids + "'>分类：" + text;
						html += "</td>";
						html += "<td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a>";
						$j("#category-select-lable #tbl-result-include").find("tbody").append(html);
					}
					$j("#dialog-category-select-left").dialogff({type : 'close'});
					zTree.checkAllNodes(false);
					
				}
			}
		}else{
			//排除的分类
			inc = $j("#category-select-lable #tbl-result-exclude tbody td[data]");;
			var zTree = $j.fn.zTree.getZTreeObj("tree-left");
			var isConflict = false;
			if("true" == judgeInclude(inc,0)){//已选全场
				nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_REPEATED_CUSTOM"), function() {
					$j("#dialog-category-select-left :checkbox").prop("checked", false);
					$j("#dialog-category-select-left .tree-control").show();
					zTree.checkAllNodes(false);
					$j("#dialog-category-select-left").dialogff({type : 'close'});
				});
			}else if($j("#dialog-category-select-left #select-all-item").attr("checked")=="checked"){//全场
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_EXCLUDE_ALL"));
				return;
			}else{
				var ids = "";
				var text = "";
				nodes = zTree.getCheckedNodes(true);
				for (var i=0, l=nodes.length; i<l; i++) {	
					ids = nodes[i].id ;
					if("true" == judgeInclude(inc,ids)){
						isConflict = true;
					}
				}
				if (isConflict) {
					nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_REPEATED_CATE"), function() {
						for (var i=0, l=nodes.length; i<l; i++) {	
							ids = nodes[i].id ;
							text = nodes[i].name;
							var html = "<tr><td data_isCategory='true' tip='";
							html += "分类：" + text + "' data='" + ids + "'>分类：" + text;
							html += "</td><td></td>";
							html += "<td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a>";
							if( "true" == judgeInclude(inc,ids)){
							}else{
								$j("#category-select-lable #tbl-result-exclude").find("tbody").append(html);
							}
						}
						$j("#dialog-category-select-left").dialogff({type : 'close'});
						zTree.checkAllNodes(false);
					});
				}
				if (! isConflict) {
					for (var i=0, l=nodes.length; i<l; i++) {	
						ids = nodes[i].id ;
						text = nodes[i].name;
						var html = "<tr><td  data_isCategory='true' tip='";
						html += "分类：" + text + "' data='" + ids + "'>分类：" + text;
						html += "</td><td></td>";
						html += "<td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a>";
						$j("#category-select-lable #tbl-result-exclude").find("tbody").append(html);
					}
					$j("#dialog-category-select-left").dialogff({type : 'close'});
					zTree.checkAllNodes(false);
				}
			}
			
		}
	});
	
	//全场cheched
	$j("#dialog-category-select-left #select-all-item").change(function() { 
		if($j(this).attr("checked")=="checked"){
			$j("#dialog-category-select-left .tree-control").hide();
		}else{
			$j("#dialog-category-select-left .tree-control").show();
		}
		}); 

	
	//商品选择器‘确定’按钮点击事件
	$j("#dialog-item-select .btn-ok").click(function() {
		var pval = + $j("#scope-parent-type").val();
		var val = + $j("#item-select-lable .slt-type").val();
		var itemid="",name="",price="";
		var itemInc = $j("#item-select-lable #tbl-result-include tbody td[data]");
		var cateInc = $j("#category-select-lable #tbl-result-exclude tbody td[data]");
		var tempInc = itemInc,tempLable = null;
		if('1'==pval){
			tempInc = itemInc;
			tempLable = $j("#item-select-lable .tbl-result");
    	}else if('2'==pval){
    		tempInc = cateInc;
			tempLable = $j("#category-select-lable #tbl-result-exclude");
    	}
		var isConflict = false;
		$j(".checkId:checked").each(function(i,n){
			itemid = $j(this).val()
			if( "true" == judgeInclude(tempInc,itemid)){
				isConflict = true;
			}
		});
		if (isConflict) {
			nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_REPEATED_ITEM"), function() {
				$j(".checkId:checked").each(function(i,n){
					itemid = $j(this).val()
					name =$j("#title_"+$j(this).val()).val();
					price = $j("#salePrice_"+$j(this).val()).val();
					var code = $j("#code_"+$j(this).val()).val();
					var url;
					if(pdp_base_url.indexOf("code")>-1){
						url = pdp_base_url.substring(0,pdp_base_url.length-4);
						url = url.replace("(@)",code);
					}else{
						url = pdp_base_url.substring(0,pdp_base_url.length-6);
						url = url.replace("(@)",itemid);
					}
					var name_url = '<a href="'+url+'" target="_blank" class="func-button" >'+'[' + code + '] '+ name + '</a>';
					var html = "<tr><td tip='";
					html += "商品：" +'[' + code + '] '+ name + "' data='" + itemid + "'>商品：" + name_url;
					html += "</td>";
					html +="<td>"+price+"</td>";
					html += "<td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a>";
					if("true" == judgeInclude(tempInc,itemid)){
						return true;
					}else{
						tempLable.find("tbody").append(html);
					}
				});
				/* 清空选择器的输入项 */
				$j("#dialog-item-select table :text").val("");
				$j("#dialog-item-select table select").val("");
				$j("#table1").loxiasimpletable("refresh");
				$j("#dialog-item-select").dialogff({type : 'close'});
			});
		}
		if (! isConflict) {
			$j(".checkId:checked").each(function(i,n){
				itemid = $j(this).val();
				name =$j("#title_"+$j(this).val()).val();
				price = $j("#salePrice_"+$j(this).val()).val();
				var code = $j("#code_"+$j(this).val()).val();
				var html = "<tr><td tip='";
				var url;
				if(pdp_base_url.indexOf("code")>-1){
					url = pdp_base_url.substring(0,pdp_base_url.length-4);
					url = url.replace("(@)",code);
				}else{
					url = pdp_base_url.substring(0,pdp_base_url.length-6);
					url = url.replace("(@)",itemid);
				}
				var name_url = '<a href="'+url+'" target="_blank" class="func-button" >'+'[' + code + '] '+ name + '</a>';
				html += "商品：" +'[' + code + '] '+ name + "' data='" + itemid + "'>商品：" + name_url;
				html += "</td>";
				html +="<td>"+price+"</td>";
				html += "<td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a>";
				tempLable.find("tbody").append(html);
			});
			/* 清空选择器的输入项 */
			$j("#dialog-item-select table :text").val("");
			$j("#dialog-item-select table select").val("");
			$j("#table1").loxiasimpletable("refresh");
			$j("#dialog-item-select").dialogff({type : 'close'});
		}
	});
	
	
	//自定选择器‘确定’按钮点击事件
	$j("#dialog-combo-select .btn-ok").click(function() {
		var val =  $j("#combo-select-lable .slt-type").val();
		var text = "";
		var ids = "";
		inc = $j("#combo-select-lable #tbl-result-include tbody td[data]");
		
		var isConflict = false;
		$j("#dialog-combo-select :checked:visible").each(function(i, dom) {
			ids = dom.value ;
			if( "true" == judgeInclude(inc,ids)){
				isConflict = true;
			}
		});
		
		if (isConflict) {
			nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_REPEATED_CUSTOM"), function() {
				$j("#dialog-combo-select :checkbox:checked:visible").each(function(i, dom) {
					text = $j(dom).parent().text();
					text = $j.trim(text);
					ids = dom.value ;
					
					var html = "<tr><td tip='";
					html += "组合：" + text + "' data='" + ids + "'>组合：" + text;
					html +="<input class='input-type' id='type-"+val+"' type='hidden' value='"+val+"'  />" ;
					html += "</td>";
					html += "<td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a>";
					if( "true" == judgeInclude(inc,ids)){
						return true;
					}else{
						$j("#combo-select-lable .tbl-result").find("tbody").append(html);
					}
				});
				/* 清空选择器的输入项 */
				$j("#dialog-combo-select :checkbox").prop("checked", false);
				$j("#dialog-combo-select").dialogff({type : 'close'});
			});
		}
		
		if (! isConflict) {
			$j("#dialog-combo-select :checkbox:checked:visible").each(function(i, dom) {
				text = $j(dom).parent().text();
				text = $j.trim(text);
				ids = dom.value ;
				
				var html = "<tr><td tip='";
				html += "组合：" + text + "' data='" + ids + "'>组合：" + text;
				html +="<input class='input-type' id='type-"+val+"' type='hidden' value='"+val+"'  />" ;
				html += "</td>";
				html += "<td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a>";
				$j("#combo-select-lable .tbl-result").find("tbody").append(html);
			});
			/* 清空选择器的输入项 */
			$j("#dialog-combo-select :checkbox").prop("checked", false);
			$j("#dialog-combo-select").dialogff({type : 'close'});
		}
	});
	
	
	//父类型下拉框更改事件,当选时，隐藏其他
	$j(".slt-parent-type").change(function() {
		var itemLable = $j("#item-select-lable");
		var cateLable = $j("#category-select-lable");
		var comboLable = $j("#combo-select-lable");
		var val = + $j(this).val();
		if (val == 101) {	//商品类型
			itemLable.show();
			cateLable.hide();  
			comboLable.hide();   
		} else if (val == 102) {	//分类类型
			itemLable.hide();
			cateLable.show();  
			comboLable.hide(); 
		} else if (val == 104) {	//自定义分组类型
			itemLable.hide();
			cateLable.hide();  
			comboLable.show();
		}
	});
	//保存按钮
	$j(".btn-save").click(function() {
		var type = + $j("#scope-parent-type").val();
		var name = $j.trim($j("#scope-name").val());
		var id = $j.trim($j("#scope-id").val());
		var inc = null, exc = null,expression="",expressionText="";
		if(1 == type){
			inc = $j("#item-select-lable #tbl-result-include tbody td[data]");
		}else if(2 == type){
			inc = $j("#category-select-lable #tbl-result-include tbody td[data]");
			exc = $j("#category-select-lable #tbl-result-exclude tbody td[data]");
		}else if(3 == type){
			inc = $j("#custom-select-lable #tbl-result-include tbody td[data]");
		}else if(4 == type){
			inc = $j("#combo-select-lable #tbl-result-include tbody td[data]");
		}
		if (name.length == 0) {
			$j("#scope-name").blur().focus();
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_NAME"));
			return;
		}
		if (inc.length == 0) {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_DATA"));
			return;
		}
		var incStr = "";
		var incText = "【包含】";
		var excStr = "";
		var excText = "";
		inc.each(function(i, dom) {
			incStr += $j(dom).attr("data") + ",";
			incText += $j.trim($j(dom).attr("tip")) + ",";
//			alert(incText);
		});
		incStr = incStr.substring(0, incStr.length - 1);
		incText = incText.substring(0, incText.length - 1);
		expression +=incStr;
		expressionText +=incText;
		cateExp = "";
		itemExp = "";
		if(2 == type){
			if(exc != null && exc.length != 0){
				excText = "【排除】";
				exc.each(function(i, dom) {
					
					var id = $j(dom).attr("data");
					var isCate = $j(dom).attr("data_isCategory");
					if (isCate == "true") {
						cateExp += ","+ id ;
					} else { 
						itemExp += ","+ id ;
					}
					excText += $j(dom).attr("tip") + ",";
				});
				excText = excText.substring(0, excText.length - 1);
				expressionText +=";" + excText;
			}
			cateExp = cateExp.length > 0 ? cateExp.substring(1) : cateExp;
			itemExp = itemExp.length > 0 ? itemExp.substring(1) : itemExp;
			expression +="; " + cateExp + "; "+itemExp;
		}
		//alert($j(inc.get(0)).attr("data"));
//		if (type == 2 && $j(inc.get(0)).attr("data") == 0 && exc.length == 0) {
//			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_INCLUDE_ERROR3"));
//			return;
//		}
		if (type == 2 && $j(inc.get(0)).attr("data") != 0 && exc.length > 0) {	//不是全体,发送验证
			var json = {
					categorys: incStr,
					items: itemExp
			};
			var data = nps.syncXhrPost(CHECK_CATEGORY_URL, json);
			if (! data.isSuccess) {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_INCLUDE_ERROR2"));
				return;
			}
		}
		var json = {
				'id':id,
				'type' : type,
				'name' : name,
				'expression' : expression,
				'text': expressionText
					
		};
		
		// 提交表单
		nps.asyncXhrPost(updateComboUrl, json, {
			   successHandler : function(rs, textStatus) {
				   if (rs.isSuccess) {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SUCCESS"));
						$j("#scope-name").val("");
						if(type == 1){
							$j("#item-select-lable tbody").empty();
						}else if(type == 2){
							$j("#category-select-lable tbody").empty();
						}else if(type == 3){
							$j("#custom-select-lable tbody").empty();
						}else if(type == 4){
							$j("#combo-select-lable tbody").empty();
						}
					} else {
						if(rs.resultCode == 'comboNameExist'){
							nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_REPEATED_NAME"));
						}else{
							nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SYSTEM_ERROR"));
						}
					}
			   }
		});
	});
	
	//‘返回’按钮
	$j(".btn-return").click(function() {
		window.location.href = PREVIOUS_URL;
	});
	
	
	
	/***********************************************自定义筛选器****************************************************/
	// 查询
	$j('#custom-select-lable').on('click', '.btn-search', function(){
		var data = nps.syncXhrPost(USER_DEFINED_LIST_URL);
		var customSelectLable = $j('#custom-select-lable');
		var filterIds = customSelectLable.find('table tbody tr').find('td:first').map(function(){
			return $j(this).attr('data');
		});
		
		var filterIsMap = {};
		for(var i=0; i<filterIds.length; i++){
			var filterId = filterIds[i];
			filterIsMap[filterId]=filterId;
		}
		
		var customSelectDialog = $j("#dialog-custom-select");
		var div =customSelectDialog.find(".combo-list");
		div.empty();
		$j.each(data, function(i, obj) {
			var html = '<span class="children-store" style="width: 145px;">' 
				+ '<label><input type="checkbox" name="chk-group" ';
			if(filterIsMap[obj.id] != undefined){
				html += 'checked="checked"';
			}
			html += ' value="'+obj.id + '" scopeName="'+obj.scopeName+'" />' + obj.scopeName + '</label></span>';
			div.append(html);
		});
		
		customSelectDialog.find(".btn-search").click();
		customSelectDialog.dialogff({type:'open',close:'in',width:'530px',height:'300px'});
	});
	
	// 确定
	$j('#dialog-custom-select').on('click', '.btn-ok', function(){
		var customSelectDialog = $j("#dialog-custom-select");
		var customSelectLable = $j('#custom-select-lable');
		
		var filterIds = customSelectLable.find('table tbody tr').find('td:first').map(function(){
			return $j(this).attr('data');
		});
		
		var filterIsMap = {};
		for(var i=0; i<filterIds.length; i++){
			var filterId = filterIds[i];
			filterIsMap[filterId]=filterId;
		}
		
		var htmlArr = new Array();
		customSelectDialog.find('.combo-list').find('input:checkbox:checked').each(function(i, obj){
			var id  = $j(obj).val();
			var scopeName = $j(obj).attr('scopeName');
			if(filterIsMap[id] == undefined){
				htmlArr.push("<tr><td tip='");
				htmlArr.push("自定义筛选："+scopeName+"' data='" + id + "'>自定义筛选 : " + scopeName);
//				htmlArr.push("<input class='input-type' id='type-"+val+"' type='hidden' value='"+val+"'  />");
				htmlArr.push("</td>");
				htmlArr.push("<td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a>");
			}
		});
		customSelectLable.find('table tbody').append(htmlArr.join(''));
		customSelectDialog.dialogff({type : 'close'});
	});
	
	
	//浮层‘查询’按钮
	$j("#dialog-custom-select .btn-search").click(function() {
		var name = $j.trim($j("#dialog-custom-select .txt-name").val());
		var chks = $j("#dialog-custom-select :checkbox");
		chks.parent().parent().hide();
		var reg = new RegExp("^.*" + name + ".*$", "i");
		chks.each(function(i, dom) {
			if (reg.test($j.trim($j(dom).parent().text()))) {
				$j(dom).parent().parent().show();
			}
		});
	});
});