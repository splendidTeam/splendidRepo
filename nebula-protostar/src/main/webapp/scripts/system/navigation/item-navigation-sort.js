//国际化
$j.extend(loxia.regional['zh-CN'], {
	"ITEM_CATEGORY_TIPS" : "友情提示",
	"ITEM_CATEGORY_CHOOSE_LEAF" : "请选择叶子节点来查看商品！",
	"LABEL_ITEM_CODE" : "商品编码",
	"LABEL_ITEM_TITLE" : "商品名称",
	"LABEL_ITEM_CATEGORY" : "所属分类",
	"ITEM_CATEGORY_CHECK_ERROR" : "错误信息",
	"ITEM_CATEGORY_OPERATE_INFO" : "提示信息",
	"ITEM_CATEGORY_OPERATE_TIP_NOSEL" : "未选中商品或分类",
	"NO_CATEGORY" : "无",
	"RELATE_CATEGORY_NO_CATEGORY_DATA" : "暂无关联行业维护数据",
	"ITEM_CATEGORY_LIST_SUFFIX_TITLE" : "分类商品列表",
	"ITEM_CATEGORY_LIST_DEFAULT_TITLE" : "已分类商品",
	"CATEGORY_FIND" : " 共找到",
	"CATEGORY_RESULT" : "个结果",
	"SORT_UP" : "上移",
	"SORT_DOWN" : "下移",
	"LBAEL_ITEM_OPERATE" : "操作",
	"LBAEL_ITEM_OPERATE_SUCCESS" : "操作成功",
	"LBAEL_ITEM_OPERATE_FAILED" : "操作失败",
	"LABEL_ITEM_SORTNO" : "排序编号",
	"LABEL_ITEM_PICURL" : "商品图片",
	"ITEM_CONFIRM" : "确认操作",
	"ITEM_CONFIRM_ADD_SORT" : "确定添加排序吗？",
	"ITEM_CONFIRM_REMOVE_SORT" : "确定移除排序吗？",
//	"ITEM_CONFIRM_PCAN_NOT_ADD_SORT" : "非叶子节点不可添加排序",
	"ITEM_CONFIRM_PCAN_NOT_ADD_SORT" : "请选择正确的分类节点",
	"ITEM_CONFIRM_PCAN_NOT_ADD_ITEM" : "请先选择商品或分类",
	"SORT_OPERATER" : "排序操作",
	"INPUT_NUMBER" : "请输入数字"
});

// 批量删除url
var sortMutiDelUrl = base + '/navigation/removeSortItem.json';

// 添加排序url
var sortAddUrl = base + '/navigation/addSortItem.json';


var colNo = 0;

var count = 1;
// fenlei
var setting = {

	check : {
		enable : false,
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
		onClick : onClick
	}
};

//导航索引
var treeObj = null

// 函数
function onClick(event, treeId, treeNode) {
	if(treeNode.diy_type=='1'){
		nps.info(nps.i18n("ITEM_CATEGORY_OPERATE_INFO"),"只有分类类型导航才能进行商品排序");
		 return;
	}
	$j(".tag-change-in.sorted").html("");
	$j(".tag-change-in.block").find('textarea.itemCodes').val("");
	$j(".tag-change-in.block").find('textarea.showDesc').val("");
	//仅能排序非叶子节点？
//	if(treeNode.isParent){
		$j("#navigationId").val(treeNode.id);
		refreshSort(navigationId);
		refreshUnSortTable();
		showItemList(treeNode.name);
		//清空添加排序结果
		$j(".showDesc").val("");
		//重设返回导航链接
		$j("#backNavigation").attr("href","/base/navigation.htm?navigationId="+treeNode.id);
//	}
}

function showItemList(name) {
	var title = "";
	if (name) {
		title = "【" + name + "】" + nps.i18n("ITEM_CATEGORY_LIST_SUFFIX_TITLE");
	} else {
		title = nps.i18n("ITEM_CATEGORY_LIST_DEFAULT_TITLE");
	}
}


// 样式？重构
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
	var zTree = $j.fn.zTree.getZTreeObj("tree");
	var value = $j.trim(key.get(0).value);
	if (value == "") {
		$j("#search_result").html("");
		updateNodes(false);
	}

	if (key.hasClass("empty")) {
		value = "";
	}
	if (lastValue === value)
		return;
	lastValue = value;
	if (value === "")
		return;
	updateNodes(false);

	nodeList = zTree.getNodesByParamFuzzy("name", value);

	$j("#search_result").html(nps.i18n("CATEGORY_FIND") + nodeList.length + nps.i18n("CATEGORY_RESULT"));

	if (nodeList.length > 0) {
		$j.each(nodeList, function(i, node) {
			zTree.expandNode(node.getParentNode(), true, true, true);
		});
	}
	updateNodes(true);
	$j("#key").focus();
}
function updateNodes(highlight) {
	var zTree = $j.fn.zTree.getZTreeObj("tree");
	for ( var i = 0, l = nodeList.length; i < l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}

function drawCheckbox(data, args, idx) {
	return "<input name='chedkId' type='checkbox' class='checkId' data-style='"+loxia.getObject("code", data)+"'  value='" + loxia.getObject("id", data) + "'/>";
}
function formatCategoryNames(data, args, idx) {
	var propertyNameArray = loxia.getObject("categoryNames", data);
	var categoryNames = propertyNameArray;
	if (propertyNameArray == null || propertyNameArray == '') {
		categoryNames = nps.i18n("NO_CATEGORY");
	}
	var hiddenCategoryNameInput = "<input type='hidden' id='itemCategoryName_" + loxia.getObject("id", data)+ "' value='" + propertyNameArray + "' />";
	categoryNames += hiddenCategoryNameInput;
	return categoryNames;
}

//刷新已排序商品数据
function refreshSort(navigationId){
	var navigationId = $j("#navigationId").val();
	$j(".content-box").spin();
	$j(".pop-up-loading").show();
	var itemCtListUrl = base + "/navigation/" + navigationId +  "/itemSortedList.htm";
	$j.get(itemCtListUrl,function(result){
		$j(".tag-change-in.sorted").html("");
		$j(".tag-change-in.sorted").html(result);
		var isSuccess = $j("#isSuccess").val();
		if(isSuccess == "success"){
			$j(".content-box").spin(false);
			$j(".pop-up-loading").hide();
		}
		$j(".gbin1-list").sortable().bind("sortupdate", function() {});
	});
}

//刷新未排序商品列表  注：暂时找不到URL改变并刷新数据的方法，故重新创建
function refreshUnSortTable(){
	//请求未排序商品数据
	var  itemNoctListUrl = base + "/navigation/" + $j("#navigationId").val()  + "/itemUnsortedList.json";
	//清空
	$j("#unsortedTable").html();
	// 未排序商品列表
	$j("#unsortedTable").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		form : "searchForm",
		cols : [ {
			label : "<input type='checkbox'  />",
			width : "3%",
			template : "drawCheckbox"
		}, {
			name : "code",
			label : nps.i18n("LABEL_ITEM_CODE"),
			width : "10%",
		}, {
			name : "title",
			label : nps.i18n("LABEL_ITEM_TITLE"),
			width : "10%",
		}],
		dataurl : itemNoctListUrl
	});
	$j("#unsortedTable").data().uiLoxiasimpletable.options.currentPage = 1;
	
	//不调用，表示不显示 why
	$j("#unsortedTable").loxiasimpletable("refresh");
}



//删除商品排序
var delsort = function(){
	var itemCodes = "";
	var navigationId= $j("#navigationId").val();
	
	//遍历获取商品编号
	$j(".check-input").each(function(i, n) {
		if($j(this).hasClass("check-current")){
			itemCodes += $j(this).parent().find("span").eq(0).html()+",";
		}
	});
	
	if(itemCodes && navigationId){
		itemCodes = itemCodes.substring(0, itemCodes.length - 1);
		nps.confirm(nps.i18n("ITEM_CONFIRM"), nps.i18n("ITEM_CONFIRM_REMOVE_SORT"), function() {
			var json = {
				"itemCodes" : itemCodes,
				"navigationId" : navigationId,
				"_csrf"  :$j("#_csrf").val()
			};
			var data = nps.syncXhr(sortMutiDelUrl, json, {
				type : "POST"
			});
			if (data.isSuccess) {
				nps.info(nps.i18n("ITEM_CATEGORY_OPERATE_INFO"), nps.i18n("LBAEL_ITEM_OPERATE_SUCCESS"));
				refreshSort(navigationId);
				refreshUnSortTable();
			} else
				nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"),data.description);
		});
	}else{
		nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"), nps.i18n("ITEM_CONFIRM_PCAN_NOT_ADD_ITEM"));
		return;
	}
}
//生效商品排序
var saveItemSort = function(){
	
	var nodes = treeObj.transformToArray(treeObj.getNodes());
	//选中节点ID
	var navigationId = "";
	for ( var i = 0; i < nodes.length; i++) {
		if (nodes[i].isHover) {
			navigationId = nodes[i].id;
		}
	}
	if(!navigationId){
		nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"), nps.i18n("ITEM_CONFIRM_PCAN_NOT_ADD_SORT"));
		return;
	}
	//已排序商品信息
	var sequence = "";
	var lis = $j("#sortTable").children();
	for ( var i = 0; i < lis.length; i++) {
		var code = $j(lis[i]).children().eq(1).find("span")[0].innerHTML;
		sequence += code +","
	}
	sequence = sequence.substring(0, sequence.length - 1);
	$j("#navigationId").val(navigationId);
	$j("#sequnce").val(sequence);
	
	nps.submitForm('updateSortForm',{
		mode: 'async',
		successHandler : function(data){
			if(data.isSuccess){
				nps.info(nps.i18n("ITEM_CATEGORY_OPERATE_INFO"), nps.i18n("LBAEL_ITEM_OPERATE_SUCCESS"));
				refreshSort(navigationId);
				return;
			}else{
				nps.info(nps.i18n("ITEM_CATEGORY_OPERATE_INFO"), nps.i18n("LBAEL_ITEM_OPERATE_FAILED"));
				return;
			}
		}
	});
}



var changeClass = function(obj){
	//复选按钮框选中
	$j(obj).children('.check-input').toggleClass('check-current');
}

var changeClass1 = function(obj){
	//复选按钮框选中
	$j(obj).siblings("div").children().find('.check-input').toggleClass('check-current');
}

var changeClass2 = function(obj){
	//复选按钮框选中
	$j(obj).children().find('.check-input').toggleClass('check-current');
}

$j(document).ready(function() {
	
	$j.fn.zTree.init($j("#tree"), setting, zNodes);
	
	treeObj = $j.fn.zTree.getZTreeObj("tree");


	var nodes = treeObj.transformToArray(treeObj.getNodes());
	for ( var i = 0; i < nodes.length; i++) {
		if (nodes[i].isParent) {
			nodes[i].nocheck = true;
			//有必须要这做吗
			treeObj.refresh();
		}
	}
	
	//已排序商品信息拖拽
	$j('.gbin1-list').sortable().bind('sortupdate', function() {});
	

	// 筛选列表
	$j(".func-button.search").click(function() {
		// 置空分类条件
		$j("#categoryId").val("");
		var defaultTitle = nps.i18n("ITEM_CATEGORY_LIST_DEFAULT_TITLE");
		$j("#unsortedTable").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();
	});

	// 分组树筛选

	key = $j("#key");
	key.bind("focus", focusKey).bind("blur", blurKey).bind("propertychange", searchNode).bind("input", searchNode);

	// 添加排序
	$j(".button.orange.addsort").on("click", function() {
		//导航节点ID
		var navigationId = $j("#navigationId").val();
		
		var itemCodes = $j(".itemCodes").val();
		if(itemCodes){
			var codeArray = itemCodes.split("\n");
			itemCodes = "";
			for(var i=0;i<codeArray.length;i++){
				 var code= $j.trim(codeArray[i]);
				 if(code){
					 itemCodes += code+","
				 }
			}
		}
		$j(".checkId:checked").each(function(i, n) {
			itemCodes += $j(this).attr("data-style") +",";
		});
		
		nps.confirm(nps.i18n("ITEM_CONFIRM"), nps.i18n("ITEM_CONFIRM_ADD_SORT"), function() {
			var json = {
				"navigationId" : navigationId,
				"itemCodes":itemCodes,
				"_csrf"  :$j("#_csrf").val()
			};
			loxia.asyncXhr(sortAddUrl, json,{type: "POST",
        		success: function(data){  		
        			if(data.isSuccess){
        				$j(".itemCodes").val("");
        				var html ="";
        				$j.each(data.description,function(idx,obj){
        					html+=obj+"加入排序成功！\n";
        				});
        				$j(".showDesc").val(html);
        				
        				refreshSort(navigationId);
        				refreshUnSortTable();
        			}else{
        				$j(".showDesc").val(data.description);
        			}
        		}
        	});
		});
	});

	$j(".memberbase").on("click",function(){
		$j(".showDesc").val("");
		refreshUnSortTable();
	});
	
	//导航节点默认选中
	var navigationId = $j("#navigationId").val();
	if(navigationId){
//		console.dir(treeObj);
		var node = treeObj.getNodeByParam("id",navigationId)
		if(node){
			treeObj.selectNode(node);
			//treeObj.checkNode(node,true,true);
			treeObj.setting.callback.onClick(null, treeObj.setting.treeId, node);//调用事件  
		}
	}
});