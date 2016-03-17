$j.extend(loxia.regional['zh-CN'],{
	"PROPERT_OPERATOR_TIP":"属性提示信息",
	 "LABEL_ITEM_CODE":"商品编码",
	 "INFO_TITLE_DATA":"提示信息",
	 "OPERATE_ENABLED":"启用",
	 "OPERATE_DISENABLED":"禁用",
	 "CONFIRM_DELETE":"是否删除",
	 "DELETE_SUCCESS":"删除成功",
	 "DELETE_FAILURE":"删除失败",
	 "PLEASE_SELECT_REC":"请选择推荐信息",
	 "CONFIRM_ENABLED":"确定启用推荐吗?",
	 "CONFIRM_DISENABLED":"确定禁用推荐吗?",
	 "ENABLED_FAILURE":"启用失败",
	 "DISENABLED_FAILURE":"禁用失败",
	 "ENABLED_SUCCESS":"启用成功",
	 "DISENABLED_SUCCESS":"禁用成功",
	 "SELECT_CATEGORY":"请选择分类",
	 "PLEASE_SAVE_DATA":"请先保存,再更换分类,否则数据会丢失,是否更换分类?"
   
});

/** 推荐类型 */
var type = '2';

var hasUpdate = false;

var param = '';

var paramName = '';

var cursorHand= '/images/wmi/blacks/16x16/cursor_hand.png';

//************************ url ************************
var recommandItemListUrl = base + '/recommand/recommanditemList.json';

var findItemListByIdsUrl = base + '/item/findItemListByIds.json';
//新建
var createRecommandItemUrl = base+"/recommand/createRecommandItem.htm";
//修改
var updateRecommandItemUrl = base + '/recommand/updateRecommandItem.htm';
// 启用或禁用
var enabledOrDisenabledRecUrl = base + '/recommand/enabledOrDisenabledRecItem.json';
//删除
var removeRecommandItemUrl = base + '/recommand/removeRecommandItem.json';
//批量删除
var batchRemoveRecItemUrl = base + '/recommand/batchRemoveRecItem.json';

var findItemByCodeUrl = base + '/item/findItemCommandByCode.json';

/**
 * 封装 一下 分类树 自有属性
 * 
 * @type
 */
var categoryRecTree = {

	/**
	 * 树元素id
	 * 
	 * @type String
	 */
	ztreeElementId : "categoryRecTree",

	/**
	 * 搜索框 选择器
	 * 
	 * @type String
	 */
	ztreeSearchElementSelector : "#keyWord",

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
var categorySetting = {
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
		fontCss : getRecFontCss
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
			var id = treeNode.id;
			var code = treeNode.code;
			if(code != 'ROOT'){
				if(hasUpdate){
					nps.confirm(nps.i18n("INFO_TITLE_DATA"), nps.i18n("PLEASE_SAVE_DATA"), function(){
						$j('#param').val(id);
						param = id;
						paramName = treeNode.name;
						getPublicRecommandItem(id);
						hasUpdate = false;
					});
				}else{
					$j('#param').val(id);
					param = id;
					paramName = treeNode.name;
					getPublicRecommandItem(id);
				
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
function getRecFontCss(treeId, treeNode) {
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
function focusRecKey(e) {
	if (key.hasClass("empty")) {
		key.removeClass("empty");
	}
}
function blurRecKey(e) {
	if (key.get(0).value === "") {
		key.addClass("empty");
	}
}

/**
 * 搜索节点方法 1.搜索到含有相应关键字的节点 2.展开该段节点 3.将字置为黄色
 */
function searchRecNode(e) {
	var zTree = $j.fn.zTree.getZTreeObj("categoryTree");
	var value = $j.trim(key.get(0).value);

	if (value == "") {
		$j("#searchResult").html("");
		updateCategoryRecNodes(false);
	}
	if (key.hasClass("empty")) {
		value = "";
	}
	if (lastValue === value)
		return;
	lastValue = value;
	if (value === "")
		return;
	updateCategoryRecNodes(false);

	nodeList = zTree.getNodesByParamFuzzy("name", value);

	$j("#search_result").html(nps.i18n("INFO_CATEGORY_SEARCH_RESULT", [ nodeList.length ]));

	if (nodeList.length > 0) {
		$j.each(nodeList, function(i, node) {
			zTree.expandNode(node.getParentNode(), true, true, true);
		});
	}
	updateCategoryRecNodes(true);
	$j("#key").focus();

}

/**
 * 将搜索到的节点展开的方法
 */
function updateCategoryRecNodes(highlight) {
	var zTree = $j.fn.zTree.getZTreeObj("categoryRecTree");
	for (var i = 0, l = nodeList.length; i < l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}

/**
 * 添加推荐商品
 * @param itemCodes
 */
function addRecommand(itemIds){
	
	var json = {"itemIds": itemIds};
	var data = loxia.syncXhr(findItemListByIdsUrl, json, {type:"POST"});
	var $tr = $j('#public-recommand-item-table').find('tbody').eq(0).find('tr');

	var html = '';
	$j.each(data, function(index, itemCommand){
		if(($tr.length+index)%2 == 1){
			html += '<tr class="odd">';
		}else{
			html += '<tr class="even">';
		}
		html += '<td><input type="checkbox" value=""/>';
		html += '<input name="recommandItems.id" type="hidden" value="" />';
		html += '<input name="recommandItems.type" type="hidden" value="'+type+'" />';
		html += '<input name="recommandItems.param" type="hidden" value="'+param+'" />';
		html += '<input name="recommandItems.itemId" type="hidden" value="'+itemCommand.id+'" />';
		html += '</td>';
		html += '<td title="点击此处可以拖动"><div class="cursor-hand"></div></td>';
		html += '<td>'+itemCommand.code+'</td>';
		html += '<td>'+itemCommand.title+'</td>';
		html += '<td>'+paramName+'</td>';
		html += '<td><a href="javascript:void(0);" class="func-button delete-recommand"><span>删除</span></a></td>';
		html += '</tr>';
	});
	hasUpdate = true;
	$j('#category-recommand-item-table').find('tbody').eq(0).append(html);
}

/**
 * 获取推荐商品列表
 */
function getPublicRecommandItem(param){
	var json='';
	if(param == ''){
		json = {"type":type};
	}else{
		json = {"type":type, "param":param};
	}
	loxia.asyncXhr(recommandItemListUrl, json,{
		type:"POST",
		success:function(data){
			//console.log(data);
			var html = '';
			$j.each(data, function(index, recommandItemCommand){
				if(index%2 == 1){
					html += '<tr class="odd">';
				}else{
					html += '<tr class="even">';
				}
				html += '<td><input type="checkbox" value="'+recommandItemCommand.id+'"/>';
				html += '<input name="recommandItems.id" type="hidden" value="'+recommandItemCommand.id+'" />';
				html += '<input name="recommandItems.type" type="hidden" value="'+recommandItemCommand.type+'" />';
				html += '<input name="recommandItems.param" type="hidden" value="'+recommandItemCommand.param+'" />';
				html += '<input name="recommandItems.itemId" type="hidden" value="'+recommandItemCommand.itemId+'" />';
				html += '</td>';
				html += '<td title="点击此处可以拖动"><div class="cursor-hand"></div></td>';
				html += '<td>'+recommandItemCommand.code+'</td>';
				html += '<td>'+recommandItemCommand.title+'</td>';
				html += '<td>'+recommandItemCommand.paramName+'</td>';
				html += '<td><a href="javascript:void(0);" class="func-button delete-recommand"><span>删除</span></a></td>';
				html += '</tr>';
			});
			$j('#category-recommand-item-table').find('tbody').eq(0).html(html);
		}
	});
}

$j(document).ready(function() {	
	// 加载树
	$j.fn.zTree.init($j("#" + categoryRecTree.ztreeElementId), categorySetting, categoryZnodes);

	// 跟节点 默认点击
	$j("#" + categoryRecTree.ztreeElementId + "_1_span").click();

	// 搜索选择器
	key = $j("#keyWord");
	key.bind("focus", focusRecKey).bind("blur", blurRecKey).bind("propertychange", searchRecNode).bind("input", searchRecNode);
	
	getPublicRecommandItem('');
	
	 //新增
	 $j(".content-box .add").click(function(){
		if(param == ''){
			nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("SELECT_CATEGORY"));
			return;
		}
		$j('#itemCodes').val("");
		dialogRefreshData();
		$j('.filtrate-item-dialog').dialogff({type:'open',close:'in',width:'900px', height:'500px'});
	 });
	 
	 

		/**逻辑删除(单个)*/
	$j("#category-recommand-item-table").on("click",".delete-recommand",function(){
		if(param == ''){
			nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("SELECT_CATEGORY"));
			return;
		}
		var currTrObj = $j(this).parents('tr');
		nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("CONFIRM_DELETE"), function(){
			currTrObj.remove();
			hasUpdate = true;
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("DELETE_SUCCESS"));
			return;
		});
	});
		 
	/** 批量逻辑删除 */
	$j(".button.delete").click(function(){
		if(param == ''){
			nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("SELECT_CATEGORY"));
			return;
		}
		
		var checkedRecommand = $j('#category-recommand-item-table').find('tbody').eq(0).find('input:checkbox:checked');
		if(checkedRecommand.length < 1){
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("PLEASE_SELECT_REC"));
			return;
		}
		nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("CONFIRM_DELETE"), function(){
			checkedRecommand.parents('tr').remove();
			hasUpdate = true;
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("DELETE_SUCCESS"));
			return;
		});
	});
	
	/** 全选 */
	$j('#category-recommand-item-table thead').on('click', 'input:checkbox', function(){
		if($j(this).attr('checked') == 'checked'){
			$j(this).parents('table').find('tbody').eq(0).find('input:checkbox').attr('checked', 'checked');
		}else{
			$j(this).parents('table').find('tbody').eq(0).find('input:checkbox').removeAttr('checked');
		}
	});
	 
	 
	 /** 保存 */
	$j('.button.save').click(function(){
		
		if(param == ''){
			nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("SELECT_CATEGORY"));
			return;
		}
		$j('#type').val(type);
		$j('#param').val(param);
		
		nps.submitForm('recommandForm',{mode: 'async', 
			successHandler : function(data){
			if(data.isSuccess == true){
				nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("SAVE_SUCCESS"));
				hasUpdate = false;
			}else{
				return nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("SAVE_FAILURE"));
			}
		}});
	});
});

