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
	"LABEL_ITEM_INVENTORY" : "商品库存",//新增商品库存
	"LABEL_ITEM_PICURL" : "商品图片",
	"ITEM_CONFIRM" : "确认操作",
	"ITEM_CONFIRM_ADD_SORT" : "确定添加排序吗？",
	"ITEM_CONFIRM_REMOVE_SORT" : "确定移除排序吗？",
	"ITEM_CONFIRM_PCAN_NOT_ADD_SORT" : "非叶子节点不可添加排序"
});

// 常量 url,setting等

// 已分类商品列表url
var itemCtListUrl = base + '/item/itemSortedList.json';

// 未分类商品列表url
var itemNoctListUrl = base + '/item/itemUnsortedList.json';

// 排序上移url
var sortUpUrl = base + '/item/sortUp.json';

// 排序下移url
var sortDownUrl = base + '/item/sortDown.json';

// 排序删除url
var sortDelUrl = base + '/item/sortDel.json';

// 批量删除url
var sortMutiDelUrl = base + '/item/sortMutiDel.json';

// 添加排序url
var sortAddUrl = base + '/item/sortAdd.json';

// 加入分组
var bindItemUrl = base + '/item/bindItemCategory.json';

// 脱离分组
var unBindItemUrl = base + '/item/unBindItemCategory.json';

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

// 函数
function onClick(event, treeId, treeNode) {

	var tempTreeId = "";
	var tempTreeName = "";

//	if (treeNode.isParent || treeNode.id == 0) {
//		nps.info(nps.i18n("ITEM_CATEGORY_TIPS"), nps.i18n("ITEM_CATEGORY_CHOOSE_LEAF"));
//
//	} else {

		tempTreeId = treeNode.id;
		tempTreeName = treeNode.name;
		$j("#categoryId").val(tempTreeId);

		// emptySearch();

		$j("#sortedTable").data().uiLoxiasimpletable.options.currentPage = 1;
		$j("#unsortedTable").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();

		showItemList(tempTreeName);
	//}

}

function showItemList(name) {

	var title = "";
	if (null == name || '' == name) {

		title = nps.i18n("ITEM_CATEGORY_LIST_DEFAULT_TITLE");

	} else {
		title = "【" + name + "】" + nps.i18n("ITEM_CATEGORY_LIST_SUFFIX_TITLE");

	}
	$j("#sortedTable .ui-loxia-table-title").html(title);
	$j("#sortedTable").attr("caption", title);
	$j("#sortedTable").data().uiLoxiasimpletable.options.title = title;
}

function industryHideMenu() {
	$j("#industryMenuContent").fadeOut("fast");
	$j("body").unbind("mousedown", industryOnBodyDown);
}
function industryOnBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "industryName" || event.target.id == "industryMenuContent" || $j(
			event.target).parents("#industryMenuContent").length > 0)) {
		industryHideMenu();
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
	return "<input name='chedkId' type='checkbox' class='checkId'  value='" + loxia.getObject("id", data) + "'/>";
}
function formatCategoryNames(data, args, idx) {
	var propertyNameArray = loxia.getObject("categoryNames", data);//获取所属分类数据

	var categoryNames = propertyNameArray;

	if (propertyNameArray == null || propertyNameArray == '') {
		categoryNames = nps.i18n("NO_CATEGORY");
	}
	var hiddenCategoryNameInput = "<input type='hidden' id='itemCategoryName_" + loxia.getObject("id", data)
			+ "' value='" + propertyNameArray + "' />";
	categoryNames += hiddenCategoryNameInput;

	return categoryNames;
}
function formatDate(val) {
	if (val == null || val == '') {
		return "&nbsp;";
	} else {
		var date = new Date(val);
		return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":"
				+ date.getMinutes() + ":" + date.getSeconds();
	}
}

// 刷新表格数据
function refreshData() {
	$j("#sortedTable").loxiasimpletable("refresh");
	$j("#unsortedTable").loxiasimpletable("refresh");
}

// 操作函数
function drawEditItem(data) {

	var itemId = loxia.getObject("id", data);
	var result = "<a href='javascript:void(0);' val='" + loxia.getObject("id", data)
			+ "' class='func-button update' onclick='sortUp(" + itemId + ")'>" + nps.i18n("SORT_UP") + "</a>"
			+ "<a href='javascript:void(0);' val='" + loxia.getObject("id", data)
			+ "' class='func-button enable' onclick='sortDown(" + itemId + ")'>" + nps.i18n("SORT_DOWN") + "</a>"
			+ "<a href='javascript:void(0);' val='" + loxia.getObject("id", data)
			+ "' class='func-button delete' onclick='sortDel(" + itemId + ")'>" + nps.i18n("OPERATOR_DELETE") + "</a>";
	return result;
}

//操作函数
function getNextNo(data) {

	var page = $j("#sortedTable").data().uiLoxiasimpletable.options.currentPage;
	
	if(colNo >= page * 15 || colNo <= (page - 1) * 15 || colNo >= count){
		colNo = (page-1) * 15 + 1;
	}else{
		colNo ++;
	}
	count = $j("#sortedTable").data().uiLoxiasimpletable.options.count;
	return colNo;
}

function simplePost(url, id, cid) {
	var json = {
			"id" : id,
			"categoryId": cid
		};
		var _d = nps.syncXhr(url, json, {
			type : "POST"
		});
		if (_d.isSuccess) {
			nps.info(nps.i18n("ITEM_CATEGORY_OPERATE_INFO"), nps.i18n("LBAEL_ITEM_OPERATE_SUCCESS"));
			refreshData();
		} else
			nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"), _d.exception.message);
}

function sortUp(id) {
	simplePost(sortUpUrl, id,$j("#categoryId").val());
}

function sortDown(id) {
	simplePost(sortDownUrl, id,$j("#categoryId").val());
}

function sortDel(id) {
	simplePost(sortDelUrl, id);
}

$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});

	searchFilter.init({
		formId : 'searchForm',
		searchButtonClass : '.func-button.search'
	});

	// 分组树
	$j.fn.zTree.init($j("#tree"), setting, zNodes);

	var treeObj = $j.fn.zTree.getZTreeObj("tree");
	var nodes = treeObj.transformToArray(treeObj.getNodes());
	for ( var i = 0; i < nodes.length; i++) {
		if (nodes[i].isParent) {
			nodes[i].nocheck = true;
			treeObj.refresh();
		}
	}

	// 已排序商品列表
	$j("#sortedTable").loxiasimpletable({
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
			name : "name",
			label : nps.i18n("LABEL_ITEM_TITLE"),
			width : "10%",
		}, {
			name : "pirUrl",
			label : nps.i18n("LABEL_ITEM_PICURL"),
			width : "10%",
		}, {
			name : "categoryName",
			label : nps.i18n("LABEL_ITEM_CATEGORY"),
			width : "12%",
		}, {
			name : "inventory",//新增商品 库存字段
			label : nps.i18n("LABEL_ITEM_INVENTORY"),
			width : "10%",
		}, {
			name : "sortNo",
			label : nps.i18n("LABEL_ITEM_SORTNO"),
			width : "11%",
			template : "getNextNo"
		}, {
			label : nps.i18n("LBAEL_ITEM_OPERATE"),
			width : "10%",
			template : "drawEditItem"
		} ],
		dataurl : itemCtListUrl
	});
	//未排序页面
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
			sort : [ "tpit.code asc", "tpit.code desc" ]
		}, {
			name : "name",
			label : nps.i18n("LABEL_ITEM_TITLE"),
			width : "10%",
			sort : [ "tpifo.title asc", "tpifo.title desc" ]
		}, {
			name : "pirUrl",
			label : nps.i18n("LABEL_ITEM_PICURL"),
			width : "10%",
		} ],
		dataurl : itemNoctListUrl
	});
	refreshData();

	// 筛选列表
	$j(".func-button.search").click(function() {
		// 置空分类条件
		$j("#categoryId").val("");
		var defaultTitle = nps.i18n("ITEM_CATEGORY_LIST_DEFAULT_TITLE");
		$j("#sortedTable .ui-loxia-table-title").html(defaultTitle);
		$j("#sortedTable").attr("caption", defaultTitle);
		$j("#sortedTable").data().uiLoxiasimpletable.options.title = defaultTitle;

		$j("#sortedTable").data().uiLoxiasimpletable.options.currentPage = 1;
		$j("#unsortedTable").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();
	});

	// 分组树筛选

	key = $j("#key");
	key.bind("focus", focusKey).bind("blur", blurKey).bind("propertychange", searchNode).bind("input", searchNode);

	// 添加排序
	$j(".button.orange.addsort").on("click", function() {

		var itemIds = "";
		var categoryId;

		$j(".checkId:checked").each(function(i, n) {
			if (i != 0) {
				itemIds += ",";
			}
			itemIds += $j(this).val();
		});

		var checkedNum = 0;
		
		var isCanAdd =false;

		for ( var i = 0; i < nodes.length; i++) {
			if (nodes[i].isHover) {
				checkedNum++;
				categoryId = nodes[i].id;
				if(!nodes[i].isParent){
					isCanAdd =true;
				}
			}
		}
		
		if(!isCanAdd){
			nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"), nps.i18n("ITEM_CONFIRM_PCAN_NOT_ADD_SORT"));
			return;
		}
		

		if (itemIds == "") {
			nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"), nps.i18n("ITEM_CATEGORY_OPERATE_TIP_NOSEL"));
			return;
		}

		nps.confirm(nps.i18n("ITEM_CONFIRM"), nps.i18n("ITEM_CONFIRM_ADD_SORT"), function() {

			var json = {
				"ids" : itemIds,
				"categoryId" : categoryId
			};
			var _d = nps.syncXhr(sortAddUrl, json, {
				type : "POST"
			});
			if (_d.isSuccess) {
				nps.info(nps.i18n("ITEM_CATEGORY_OPERATE_INFO"), nps.i18n("LBAEL_ITEM_OPERATE_SUCCESS"));
				refreshData();
			} else
				nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"), _d.exception.message);
		});
	});

	// 移除排序
	$j(".button.orange.delsort").on("click", function() {

		var itemIds = "";

		$j(".checkId:checked").each(function(i, n) {
			if (i != 0) {
				itemIds += ",";
			}
			itemIds += $j(this).val();
		});

		nps.confirm(nps.i18n("ITEM_CONFIRM"), nps.i18n("ITEM_CONFIRM_REMOVE_SORT"), function() {

			var json = {
				"ids" : itemIds,
			};
			var _d = nps.syncXhr(sortMutiDelUrl, json, {
				type : "POST"
			});
			if (_d.isSuccess) {
				nps.info(nps.i18n("ITEM_CATEGORY_OPERATE_INFO"), nps.i18n("LBAEL_ITEM_OPERATE_SUCCESS"));
				refreshData();
			} else
				nps.info(nps.i18n("ITEM_CATEGORY_CHECK_ERROR"), _d.exception.message);
		});
	});

	$j(".memberbase").on("click",function(){
		$j("#sortedTable").data().uiLoxiasimpletable.options.currentPage = 1;
		$j("#unsortedTable").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();
	});
});