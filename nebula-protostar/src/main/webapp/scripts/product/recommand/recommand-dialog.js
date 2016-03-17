$j.extend(loxia.regional['zh-CN'], {
	"PROPERT_OPERATOR_TIP" : "属性提示信息",
	"INPUT_NOT_EMPTY" : "输入不能为空",
	"LABEL_ITEM_CODE" : "商品编码",
	"INFO_TITLE_DATA" : "提示信息",
	"SAVE_SUCCESS" : "保存成功",
	"SAVE_FAILURE" : "保存失败",
	"ITEM_CODE_NAME" : "商品编码",
	"INFO_CATEGORY_SEARCH_RESULT" : "共找到 {0} 个结果",
	"PLEASE_SELECT_ITEM":"请选择要推荐的商品",
	"ITEM_CODE_NOT_EXISTS":"商品编码({0})不存在或分割符不正确",
	"SELECT_CHILREN_CATEGORY":"请选择子分类",
	"NO_DATA":"未找到数据",
	"REPEAT_REC_ITEM":"请不要推荐重复商品"

});
// ************************ url ************************
var findItemInfoListJsonUrl = base + "/recommand/findItemInfoList.json";

var findItemListByCodesUrl = base + "/item/findItemListByCodes.json";

/**
 * 封装 一下 分类树 自有属性
 * 
 * @type
 */
var treeCategory = {

	/**
	 * 树元素id
	 * 
	 * @type String
	 */
	ztreeElementId : "categoryTree",

	/**
	 * 搜索框 选择器
	 * 
	 * @type String
	 */
	ztreeSearchElementSelector : "#key",

	/**
	 * 高亮的node list
	 * 
	 * @type
	 */
	highlightNodeList : [],

	/**
	 * 高亮节点
	 * 
	 * @param zTree
	 *            zTree
	 * 
	 * @param flag
	 *            true代表高亮,false 代表取消高亮
	 */
	highlightNodes : function(zTree, flag) {
		for (var i = 0, l = this.highlightNodeList.length; i < l; i++) {
			var node = this.highlightNodeList[i];
			node.highlight = flag;
			zTree.updateNode(node);
		}
	}
};

// *****************************************************************
var setting = {
	edit : {
		enable : false,
		showRemoveBtn : false,
		showRenameBtn : false
	},
	view : {
		showLine : true,
		showIcon : false,
		nameIsHTML : true,
		selectedMulti : false,
		// 设置树 节点样式
		fontCss : getFontCss
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
		/**
		 * 树 点击事件
		 * 
		 * @param event
		 *            标准的 js event 对象
		 * @param treeId
		 *            对应 zTree 的 treeId，便于用户操控
		 * @param treeNode
		 *            被点击的节点 JSON 数据对象
		 * @param clickFlag
		 *            节点被点击后的选中操作类型 详细建 ztree api
		 */
		onClick : function(event, treeId, treeNode, clickFlag) {
			nps.error();
			var code = treeNode.code;
			var isRoot = ("ROOT" == code);
			if(!isRoot){
				if (treeNode.children == undefined) {
					$j('#categoryCode').val(code);
					dialogRefreshData();
				}else{
					$j('#categoryCode').val("");
					nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("SELECT_CHILREN_CATEGORY"));
				}
			}
		},
		/**
		 * 是否允许折叠操作
		 * 
		 * @param treeId
		 * @param treeNode
		 * @returns
		 */
		beforeCollapse : function(treeId, selectNode) {
			nps.error();
			var code = selectNode.code;

			var isRoot = ("root" == code);
			var isCanCollapse = !isRoot;// 根目录 不允许折叠
			
			return isCanCollapse;
		}
	}
};

var key, lastValue = "", nodeList = [], fontCss = {};

/**
 * 将搜索到的节点字体置为黄色
 */
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
// ------------搜索选择器--------------
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

/**
 * 搜索节点方法 1.搜索到含有相应关键字的节点 2.展开该段节点 3.将字置为黄色
 */
function searchNode(e) {
	var zTree = $j.fn.zTree.getZTreeObj("categoryTree");
	var value = $j.trim(key.get(0).value);

	if (value == "") {
		$j("#search_result").html("");
		updateCategoryNodes(false);
	}
	if (key.hasClass("empty")) {
		value = "";
	}
	if (lastValue === value)
		return;
	lastValue = value;
	if (value === "")
		return;
	updateCategoryNodes(false);

	nodeList = zTree.getNodesByParamFuzzy("name", value);

	$j("#search_result").html(nps.i18n("INFO_CATEGORY_SEARCH_RESULT", [ nodeList.length ]));

	if (nodeList.length > 0) {
		$j.each(nodeList, function(i, node) {
			zTree.expandNode(node.getParentNode(), true, true, true);
		});
	}
	updateCategoryNodes(true);
	$j("#key").focus();

}

/**
 * 将搜索到的节点展开的方法
 */
function updateCategoryNodes(highlight) {
	var zTree = $j.fn.zTree.getZTreeObj("categoryTree");
	for (var i = 0, l = nodeList.length; i < l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}

/**
 * 刷新数据
 */
function dialogRefreshData() {
	$j("#item-table").loxiasimpletable("refresh");
}

/**
 * 添加到推荐列表template
 */
function addRecommandTemplate(data){
	var itemId=loxia.getObject("id", data);
	var result = '<a href="javascript:void(0);" itemId="'+itemId+'" class="func-button dialog-add-recommand"><span>添加推荐</span></a>';
	return result;
}

//获取id
function drawCheckbox(data, args, idx){
	return "<input type='checkbox' name='chid' class='checkId' value='"+loxia.getObject("id", data)+"'/>";
}

// ------------------------------------

$j(document).ready(function() {
	/** **************************************DIALOG BEGIN******************************************* */
	// 加载树
	$j.fn.zTree.init($j("#" + treeCategory.ztreeElementId), setting, zNodes);

	// 跟节点 默认点击
	$j("#" + treeCategory.ztreeElementId + "_1_span").click();

	// 搜索选择器
	key = $j("#key");
	key.bind("focus", focusKey).bind("blur", blurKey).bind("propertychange", searchNode).bind("input", searchNode);

	/** *** item table ***** */
	// 动态获取数据库商品信息表
	$j("#item-table").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		form : "dialogSearchForm",
		cols : [ {
			label : "<input type='checkbox' />",
			width : "5%",
			template : "drawCheckbox",
		}, {
			name : "code",
			label : "商品编码",
			width : "10%",
			sort: ["tpi.code asc","tpi.code desc"]
		}, {
			name : "title",
			label : "商品名称",
			width : "20%",
			sort: ["tpii.title asc","tpii.title desc"]
		}, {
			label : "操作",
			width : "10%",
			template :"addRecommandTemplate"
		}],
		dataurl : findItemInfoListJsonUrl
	});
	//dialogRefreshData();
	
	/**
	 * 取消
	 */
	$j('.cencal').click(function(){
		$j(".dialog-close").click();
	});
	
	/**
	 * 添加到推荐列表
	 */
	$j('.filtrate-item-dialog').on('click', '.add-recommand', function(){
		var checkboxObj = $j('.filtrate-item-dialog').find('table tbody tr td').find('input:checkbox:checked');
		var itemIds = new Array();
		if(checkboxObj.length > 0){
			for(var i=0; i<checkboxObj.length; i++){
				itemIds[i] = checkboxObj[i].value;
			}
			addRecommand(itemIds);
		}else{
			nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n('PLEASE_SELECT_ITEM'));
			return;
		}
	});
	
	//筛选
	$j('.filtrate-item-dialog').on('click', ".func-button.search", function(){ 
		 dialogRefreshData();
    });  
	
	
	$j('.ui-tag-change').on('click', '.memberbase', function(){
		if($j(this).hasClass('bitch')){
			$j('#itemCodes').val('');
		}else{
			$j('#code').val('');
			$j('#name').val('');
			dialogRefreshData();
		}
	});
	
	/**
	 * 确定
	 */
	$j('.filtrate-item-dialog').on('click', '.confirm', function(){
		var itemCodeText = $j('#itemCodes').val();
		if(itemCodeText != ""){
			var splitValue = $j('.splitStr').val();
			var itemCodes = '';
			if(splitValue == 1){
				itemCodes = itemCodeText.split('\n');
			}else if(splitValue == 2){
				itemCodes = itemCodeText.split(',');
			}else{
				return;
			}
//			console.log(itemCodes);
			
			for(var i=0; i<itemCodes.length-1; i++){
				if(itemCodes[i] == itemCodes[i+1]){
					nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n('REPEAT_REC_ITEM', [notExistedItemCode]));
					return;
				}
			}
			
			var json = {"itemCodes": itemCodes};
			var itemList = loxia.syncXhr(findItemListByCodesUrl, json, {type: "GET"});
			var itemIds = new Array();
			var notExistedItemCode = new Array();
			if(itemList.length != itemCodes.length){
				$j.each(itemCodes, function(index, itemCode){
					var flag = false;
					$j.each(itemList, function(i, item){
						if(itemCode == item.code){
							flag = true;
						}
					});
					if(!flag){
						notExistedItemCode[notExistedItemCode.length] = itemCode;
					}
				});
				nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n('ITEM_CODE_NOT_EXISTS', [notExistedItemCode]));
				return;
			}
			
			$j.each(itemList, function(index, item){
				itemIds[index] = item.id;
			});
			addRecommand(itemIds);
		}else{
			var checkboxObj = $j('.filtrate-item-dialog').find('table tbody tr td').find('input:checkbox:checked');
			var itemIds = new Array();
			if(checkboxObj.length > 0){
				for(var i=0; i<checkboxObj.length; i++){
					itemIds[i] = checkboxObj[i].value;
				}
				addRecommand(itemIds);
			}
		}
		$j(".dialog-close").click();
	});
	
	/**
	 * 单个添加到推荐列表
	 */
	$j('.filtrate-item-dialog').on('click', '.dialog-add-recommand', function(){
		var itemId = $j(this).attr('itemId');
		addRecommand(itemId);
	});

	/** **************************************DIALOG END******************************************* */

});