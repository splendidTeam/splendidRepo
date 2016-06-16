$j.extend(loxia.regional['zh-CN'], {
	"PROPERT_OPERATOR_TIP" : "属性提示信息",
	"LABEL_ITEM_CODE" : "商品编码",
	"LABEL_ITEM_TITLE" : "商品名称",
	"LABEL_ITEM_CATEGORYNAMES" : "分类（默认分类用红色标注）",
	"LABEL_ITEM_LIFECYCLE" : "状态",
	"LABEL_ITEM_CREATETIME" : "创建时间",
	"LABEL_ITEM_MODIFYTIME" : "修改时间",
	"LABEL_ITEM_LISTTIME" : "最近上架时间",
	"LABEL_INIT_LISTTIME" : "初始上架时间",
	"LBAEL_ITEM_OPERATE" : "操作",
	"OPERATOR_DELETE" : "删除",
	"OPERATOR_MODIFY" : "修改",
	"TO_DISABLE" : "下架",
	"TO_ENABLE" : "上架",
	"INFO_CONFIRM_DISABLE_MEMBER" : "确认下架商品吗",
	"INFO_CONFIRM_ENABLE_MEMBER" : "确认上架商品吗",
	"INFO_STOP_SUCCESS" : "下架成功!",
	"INFO_START_SUCCESS" : "上架成功!",
	"INFO_TITLE_DATA" : "提示信息",
	"INFO_STOP_FAIL" : "下架失败!",
	"INFO_START_FAILURE" : "上架失败!",
	"NO_CATEGORY" : "无",
	"VALUE_SOURCE_1" : "QQ登录",
	"VALUE_SOURCE_2" : "自注册",
	"VALUE_SOURCE_3" : "微博登录",
	"CONFIRM_DELETE_ITEM" : "确认删除商品",
	"NPS_DELETE_SUCCESS" : "删除成功",
	"NPS_DELETE_FAILURE" : "删除失败",
	"ITEM_INFO_CONFIRM" : "提示",
	"ITEM_TIP_NOSEL_DEL" : "请选择要删除的商品",
	"ITEM_TIP_NOSEL_ENABLED" : "请选择要上架的商品",
	"ITEM_TIP_NOSEL_DISENABLED" : "请选择要下架的商品",
	"SHOP_MANAGER" : "图片管理",
	"INFO_BATCH_STOP_FAIL" : "批量下架可能没有完全成功(如果对同一商品重复下架时有可能出现此提示)",
	"INFO_BATCH_START_FAILURE" : "批量上架可能没有完全成功(如果对同一商品重复下架时有可能出现此提示)",
	"TO_ACTIVE":"定时上架",
	"ACTIVE_SUCCESS":"定时发布成功",
	"ACTIVE_FAIL":"定时发布失败",
	"ACTIVE_TIME":"定时上架时间",
	"STORE_MANAGER" : "库存管理",// add by hr 20140417
	"IMAGE_COUNT" : "图片数",// add by hr 20140708
	"ITEM_TYPE" : "商品类型",
	"IS_PRESENT":"是否赠品",
	"ITEM_TYPE_MAIN" : "主商品",
	"ITEM_TYPE_GIFT" : "非卖品",
	"ITEM_TYPE_PRESALE":"预售编辑",
	"ITEM_INVENTORY":"商品库存"
	
});
// Json格式动态获取数据库商品信息
var itemListUrl = base + '/item/itemList.json';
// 上架下架商品
var enableOrDisableItemUrl = base + '/item/enableOrDisableItem.json';

var deleteItemUrl = base + '/item/remove.json';

var activeItemUrl = base + '/item/active.json';

var enableOrDisableItemsUrl = base + '/item/enableOrDisableItems.json';

//商品导出和导入URL
var itemExportImportUrl = base + '/item/itemExportImport.htm';

Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};

// 分类列表
var categorySetting = {
	treeNodeKey : "id",
	treeNodeParentKey : "parentId",
	check : {
		enable : true,
		chkStyle : "radio",
		radioType : "all"
	},
	view : {
		dblClickExpand : false,
		showIcon : false,
		fontCss : function(treeId, treeNode) {
			if (treeNode.lifecycle == 0) {
				return {
					color : "#666"
				};
			} else {
				return {
					color : "#000"
				};
			}
		}
	},
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {
		onClick : categoryonClick,
		onCheck : categoryonCheck
	}
};

// 行业下拉列表
var industrySetting = {
	treeNodeKey : "id",
	treeNodeParentKey : "parentId",
	check : {
		enable : true,
		chkStyle : "radio",
		radioType : "all"
	},
	view : {
		dblClickExpand : false,
		showIcon : false,
		fontCss : function(treeId, treeNode) {
			if (treeNode.lifecycle == 0) {
				return {
					color : "#666"
				};
			} else {
				return {
					color : "#000"
				};
			}
		}
	},
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {
		onClick : industryonClick,
		onCheck : industryonCheck
	}
};

var activeIds = "";

// 分类点击函数 获得树结构
function categoryonClick(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("categoryDemo");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}
// 分类点击函数 获得树结构值
function categoryonCheck(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("categoryDemo"), nodes = zTree.getCheckedNodes(true), v = "";
	id = "";
	for (var i = 0, l = nodes.length; i < l; i++) {
		v += nodes[i].name + ",";
		id += nodes[i].id + ",";
	}
	if (v.length > 0)
		v = v.substring(0, v.length - 1);
	if (id.length > 0)
		id = id.substring(0, id.length - 1);
	var cityObj = $j("#categoryName");
	cityObj.attr("value", v);
	$j("#categoryId").attr("value", id);
	categoryHideMenu();
}

// 鼠标移动隐藏时间
function categoryHideMenu() {
	$j("#categoryMenuContent").fadeOut("fast");
	$j("body").unbind("mousedown", categoryOnBodyDown);
}
function categoryOnBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "categoryName" || event.target.id == "categoryMenuContent" || $j(
			event.target).parents("#categoryMenuContent").length > 0)) {
		categoryHideMenu();
	}
}

// 单击时间
function onClick(event, treeId, treeNode) {

	var tempTreeId = "";
	var tempTreeName = "";

	if (treeNode.isParent || treeNode.id == 0) {
		nps.info(nps.i18n("ITEM_CATEGORY_TIPS"), nps.i18n("ITEM_CATEGORY_CHOOSE_LEAF"));

	} else {

		tempTreeId = treeNode.id;
		tempTreeName = treeNode.name;
		$j("#categoryId").val(tempTreeId);

		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();

		showItemList(tempTreeName);
	}

}

// 行业树
function industryonClick(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("industryDemo");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}
// 获取行业属值
function industryonCheck(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("industryDemo"), nodes = zTree.getCheckedNodes(true), v = "";
	id = "";
	for (var i = 0, l = nodes.length; i < l; i++) {
		v += nodes[i].name + ",";
		id += nodes[i].id + ",";
	}
	if (v.length > 0)
		v = v.substring(0, v.length - 1);
	if (id.length > 0)
		id = id.substring(0, id.length - 1);
	var cityObj = $j("#industryName");
	cityObj.attr("value", v);
	$j("#industryId").attr("value", id);
	industryHideMenu();
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

// 获取id
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' name='chid' class='checkId' value='" + loxia.getObject("id", data) + "'/>";
}
// 刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}
// 获取日期
function formatDate(val) {
	if (val == null || val == '') {
		return "&nbsp;";
	} else {
		var date = new Date(val);
		return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":"
				+ date.getMinutes() + ":" + date.getSeconds();
	}
}
// 上架下架函数
function enableOrDisable(val, state) {
	var info = "";
	// 0==下架， 1=上架
	if (state != 1) {
		info = nps.i18n("INFO_CONFIRM_DISABLE_MEMBER");
	} else {
		info = nps.i18n("INFO_CONFIRM_ENABLE_MEMBER");
	}
	nps.confirm(nps.i18n("PROPERT_OPERATOR_TIP"), info, function() {
		var json = {
			"itemId" : val,
			"state" : state
		};
		nps.asyncXhrGet(enableOrDisableItemUrl, json, {
			successHandler : function(data, textStatus) {
				var backWarnEntity = data;
				if (backWarnEntity.isSuccess) {
					if (state != 1) {
						// 下架成功
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_STOP_SUCCESS"));
					} else {
						// 上架成功
						nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_START_SUCCESS"));
					}
				}
				refreshData();
			}
		});
	});
}
function formatCategoryNames(data, args, idx) {
	var propertyNameArray = null;
	
	if(categoryDisplayMode == "code") {
		propertyNameArray = loxia.getObject("categoryCodes", data);
	} else {//defaul display name
		propertyNameArray = loxia.getObject("categoryNames", data);
	}

	if (propertyNameArray == null || propertyNameArray == '') {
		return nps.i18n("NO_CATEGORY");
	}
	
   var name =loxia.getObject("defCategory", data);
   
   if(name!=null && name.trim()!=''){
	   propertyNameArray.remove(name);
	   propertyNameArray += "&nbsp;<font  color='red'>"+name+"</font>";
   }
   
	return propertyNameArray;
}

// 操作函数 change by hr 20140428
function drawEditItem(data) {

	var itemId = loxia.getObject("id", data);
	var result = "";

	var state = loxia.getObject("lifecycle", data);
	if (state == 1) {
		result = [ {
			label : nps.i18n("OPERATOR_MODIFY"),
			type : "href",
			content : base + "/item/updateItem.htm?itemId=" + itemId
		}, {
			label : nps.i18n("SHOP_MANAGER"),
			type : "href",
			content : base + "/i18n/itemImage/toAddItemImage.htm?itemId=" + itemId
		}, {
			label : nps.i18n("STORE_MANAGER"),
			type : "href",
			content : base + "/item/updateItemStore.htm?itemId=" + itemId
		}, {
			label : nps.i18n("TO_DISABLE"),
			type : "jsfunc",
			content : "fnDisenabledItem"
		}, {
			label : nps.i18n("OPERATOR_DELETE"),
			type : "jsfunc",
			content : "fnDeleteItem"
		},{
			label : nps.i18n("ITEM_TYPE_PRESALE"),
			type : "href",
			content : base + "/item/preSaleEdit.htm?itemId=" + itemId
		} ];
	} else {
		result = [ {
			label : nps.i18n("OPERATOR_MODIFY"),
			type : "href",
			content : base + "/item/updateItem.htm?itemId=" + itemId
		}, {
			label : nps.i18n("SHOP_MANAGER"),
			type : "href",
			content : base + "/i18n/itemImage/toAddItemImage.htm?itemId=" + itemId
		}, {
			label : nps.i18n("STORE_MANAGER"),
			type : "href",
			content : base + "/item/updateItemStore.htm?itemId=" + itemId
		}, {
			label : nps.i18n("TO_ENABLE"),
			type : "jsfunc",
			content : "fnEnabledItem"
		}, {
			label : nps.i18n("TO_ACTIVE"),
			type : "jsfunc",
			content : "fnActiveItem"
		}, {
			label : nps.i18n("OPERATOR_DELETE"),
			type : "jsfunc",
			content : "fnDeleteItem"
		},{
			label : nps.i18n("ITEM_TYPE_PRESALE"),
			type : "href",
			content : base + "/item/preSaleEdit.htm?itemId=" + itemId
		} ];
	}

	return result;

}
// 下架 add by hr 20140428
function fnDisenabledItem(data, args, caller) {
	enableOrDisable(data.id, 0);
}
// 上架 add by hr 20140428
function fnEnabledItem(data, args, caller) {
	enableOrDisable(data.id, 1);
}

// 删除 add by hr 20140428
function fnDeleteItem(data, args, caller) {
	nps.confirm(nps.i18n("CONFIRM_DELETE"), nps.i18n("CONFIRM_DELETE_ITEM"), function() {
		var json = {
			"ids" : data.id
		};
		var _d = loxia.syncXhr(deleteItemUrl, json, {
			type : "GET"
		});
		if (_d == 'success') {
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("NPS_DELETE_SUCCESS"));
			refreshData();
		} else {
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("NPS_DELETE_FAILURE"));
		}
	});
}

//启动定时上架
function fnActiveItem(data, args, caller) {
	activeIds=data.id;
	$j("#active-dialog").dialogff({type:'open',close:'in',width:'600px',height:'200px'});
}

// 商品个数template add by hr 20140708
function itemImageTemplate(data){
	return loxia.getObject("imageCount", data);
}
function itemTypeTemplate(data){
	var type = loxia.getObject("itemType", data);
	if(type == null || type == 1){
		return "普通商品";
	}else if(type == 3){
		return "捆绑类商品 ";
	}else if(type == 5){
		return "组商品";
	}else if(type == 7){
		return "虚拟商品";
	}
	return "";
}

// 获取行业树
$j(document).ready(function() {

	loxia.init({
		debug : true,
		region : 'zh-CN'
	});

	// 行业树
	$j.fn.zTree.init($j("#industryDemo"), industrySetting, industry_ZNodes);

	var treeObjIndustry = $j.fn.zTree.getZTreeObj("industryDemo");
	var treeIndustryNodes = treeObjIndustry.transformToArray(treeObjIndustry.getNodes());
	for (var i = 0; i < treeIndustryNodes.length; i++) {
		if (treeIndustryNodes[i].isParent) {
			treeIndustryNodes[i].nocheck = true;
			treeObjIndustry.refresh();
		}
	}

	var q_long_industryId = $j("input[name='q_long_industryId']").val().trim();

	if (q_long_industryId != null && q_long_industryId != '') {

		var industry_TreeObj = $j.fn.zTree.getZTreeObj("treeDemo");

		var industry_Nodes = industry_TreeObj.transformToArray(industry_TreeObj.getNodes());

		for (var i = 0; i < industry_Nodes.length; i++) {
			if (industry_Nodes[i].id == q_long_industryId) {

				industry_Nodes[i].checked = true;

				$j("#industryName").val(industry_Nodes[i].name);

				break;
			}
		}
		industry_TreeObj.refresh();
	}

	$j("#industryName").click(function() {
		var cityObj = $j(this);
		var cityOffset = $j(this).offset();
		$j("#industryMenuContent").css({
			left : cityOffset.left + "px",
			top : cityOffset.top + cityObj.outerHeight() + "px"
		}).slideDown("fast");

		$j("body").bind("mousedown", industryOnBodyDown);
	});

	// 分类
	$j.fn.zTree.init($j("#categoryDemo"), categorySetting, category_ZNodes);

	var treeObjCategory = $j.fn.zTree.getZTreeObj("categoryDemo");
	var treeCategoryNodes = treeObjCategory.transformToArray(treeObjCategory.getNodes());
	for (var i = 0; i < treeCategoryNodes.length; i++) {
		if (treeCategoryNodes[i].isParent) {
			treeCategoryNodes[i].nocheck = true;
			treeObjCategory.refresh();
		}
	}

	var q_long_categoryId = $j("input[name='q_long_categoryId']").val().trim();

	if (q_long_categoryId != null && q_long_categoryId != '') {

		var category_TreeObj = $j.fn.zTree.getZTreeObj("categoryDemo");

		var category_Nodes = category_TreeObj.transformToArray(category_TreeObj.getNodes());

		for (var i = 0; i < category_Nodes.length; i++) {
			if (category_Nodes[i].id == q_long_categoryId) {

				category_Nodes[i].checked = true;

				$j("#industryName").val(category_Nodes[i].name);

				break;
			}
		}
		category_TreeObj.refresh();
	}

	$j("#categoryName").click(function() {
		var cityObj = $j(this);
		var cityOffset = $j(this).offset();
		$j("#categoryMenuContent").css({
			left : cityOffset.left + "px",
			top : cityOffset.top + cityObj.outerHeight() + "px"
		}).slideDown("fast");

		$j("body").bind("mousedown", categoryOnBodyDown);
	});
	// 商品状态
	$j.ui.loxiasimpletable().typepainter.threeState = {
		getContent : function(data) {
			if (data == 0) {
				return "<span class='ui-pyesno ui-pyesno-no' title='下架'></span>";
			} else if (data == 1) {
				return "<span class='ui-pyesno ui-pyesno-yes' title='上架'></span>";
			} else if (data == 3) {
				return "<span class='ui-pyesno ui-pyesno-wait' title='新建'></span>";
			}
		},
		postHandle : function(context) {
			// do nothing
		}
	};
	
	// 商品状态
	$j.ui.loxiasimpletable().typepainter.twoState = {
		getContent : function(data) {
			if(data == null || data == 1){
				return "<span class='ui-pyesno ui-pyesno-no' title='否'></span>";
			}
			else if(data == 0){
				return "<span class='ui-pyesno ui-pyesno-yes' title='是'></span>";
			}
			return "";
			
		},
		postHandle : function(context) {
			// do nothing
		}
	};
	// 动态获取数据库商品信息表
	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		form : "searchForm",
		cols : [ {
			label : "<input type='checkbox' />",
			width : "3%",
			template : "drawCheckbox"
		}, {
			name : "code",
			label : nps.i18n("LABEL_ITEM_CODE"),
			width : "7%",
			sort : [ "tpit.code asc", "tpit.code desc" ]
		}, {
			name : "title",
			label : nps.i18n("LABEL_ITEM_TITLE"),
			width : "10%",
			sort : [ "tpit.title asc", "tpit.title desc" ]
		}, {
			name : "categoryNames",
			label : nps.i18n("LABEL_ITEM_CATEGORYNAMES"),
			width : "10%",
			template : "formatCategoryNames"

		}, {
			name : "lifecycle",
			label : nps.i18n("LABEL_ITEM_LIFECYCLE"),
			width : "4%",
			type : "threeState"
		}, {
			name : "createTime",
			label : nps.i18n("LABEL_ITEM_CREATETIME"),
			width : "10%",
			formatter : "formatDate",
			sort : [ "tpit.createTime asc", "tpit.createTime desc" ]
		}, {
			name : "modifyTime",
			label : nps.i18n("LABEL_ITEM_MODIFYTIME"),
			width : "8%",
			formatter : "formatDate",
			sort : [ "tpit.modifyTime asc", "tpit.modifyTime desc" ]
		}, {
			name : "listTime",
			label : updateListTimeFlag == "false" ? nps.i18n("LABEL_INIT_LISTTIME") : nps.i18n("LABEL_ITEM_LISTTIME"),
			width : "8%",
			formatter : "formatDate",
			sort : [ "tpit.listTime asc", "tpit.listTime desc" ]
		}, {
			name : "activeBeginTime",
			label : nps.i18n("ACTIVE_TIME"),
			width : "8%",
			formatter : "formatDate",
			sort : [ "tpit.activeBeginTime asc", "tpit.activeBeginTime desc" ]
		}, {
			name : "type",
			label : nps.i18n("IS_PRESENT"),
			width : "5%",
			type : "twoState"
		}, {
			name : "imageCount",
			label : nps.i18n("IMAGE_COUNT"),
			width : "5%",
			template : "itemImageTemplate",
			sort : [ "tpit.imageCount asc", "tpit.imageCount desc" ]
		}, {
			name : "itemType",
			label : nps.i18n("ITEM_TYPE"),
			width : "7%",
			template : "itemTypeTemplate",
		}, {
			name : "inventory",
			label : nps.i18n("ITEM_INVENTORY"),
			width : "5%",
			sort : [ "tpit.inventory asc", "tpit.inventory desc" ]
		}, {
			label : nps.i18n("LBAEL_ITEM_OPERATE"),
			width : "5%",
			type : "oplist",
			oplist : drawEditItem

		} ],
		dataurl : itemListUrl
	});
	refreshData();

	// 筛选
	$j(".func-button.search").click(function() {
		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();
	});
	// 新增
	$j(".button.orange.new").click(function() {
		window.location.href = base + "/item/createItemChoose.htm";
	});

	// 批量逻辑删除
	$j(".button.butch.delete").click(function() {
		var data = "";
		$j(".checkId:checked").each(function(i, n) {
			if (i != 0) {
				data += ",";
			}
			data += $j(this).val();

		});
		if (data == "") {
			nps.info(nps.i18n("ITEM_INFO_CONFIRM"), nps.i18n("ITEM_TIP_NOSEL_DEL"));
			return;
		}
		nps.confirm(nps.i18n("CONFIRM_DELETE"), nps.i18n("CONFIRM_DELETE_ITEM"), function() {
			var json = {
				"ids" : data
			};
			var _d = loxia.syncXhr(deleteItemUrl, json, {
				type : "GET"
			});
			if (_d == 'success') {
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("NPS_DELETE_SUCCESS"));
				refreshData();
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("NPS_DELETE_FAILURE"));
			}
		});
	});

	// 批量上架
	$j(".button.butch.enable").click(function() {
		var data = "";
		$j(".checkId:checked").each(function(i, n) {
			if (i != 0) {
				data += ",";
			}
			data += $j(this).val();

		});
		if (data == "") {
			nps.info(nps.i18n("ITEM_INFO_CONFIRM"), nps.i18n("ITEM_TIP_NOSEL_ENABLED"));
			return;
		}
		nps.confirm(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_CONFIRM_ENABLE_MEMBER"), function() {
			var json = {
				"itemIds" : data,
				"state" : 1
			};
			var _d = loxia.syncXhr(enableOrDisableItemsUrl, json, {
				type : "GET"
			});
			if (_d.isSuccess == true) {
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_START_SUCCESS"));
				refreshData();
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_BATCH_START_FAILURE"));
			}
		});
	});

	// 批量下架
	$j(".button.butch.disable2").click(function() {
		var data = "";
		$j(".checkId:checked").each(function(i, n) {
			if (i != 0) {
				data += ",";
			}
			data += $j(this).val();

		});
		if (data == "") {
			nps.info(nps.i18n("ITEM_INFO_CONFIRM"), nps.i18n("ITEM_TIP_NOSEL_DISENABLED"));
			return;
		}
		nps.confirm(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_CONFIRM_DISABLE_MEMBER"), function() {
			var json = {
				"itemIds" : data,
				"state" : 0
			};
			var _d = loxia.syncXhr(enableOrDisableItemsUrl, json, {
				type : "GET"
			});
			if (_d.isSuccess == true) {
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_STOP_SUCCESS"));
				refreshData();
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_BATCH_STOP_FAIL"));
			}
		});
	});
	
	$j("#activeOkBtn").click(function(){
		$j("#active-dialog").dialogff({type:'close'});
		var activeBeginTime = $j("#activeTime").val();
		var json = {
			"ids" : activeIds,
			"activeBeginTime" : activeBeginTime
		};
		var _d = loxia.syncXhr(activeItemUrl, json, {
			type : "POST"
		});
		if (_d.isSuccess == true) {
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("ACTIVE_SUCCESS"));
			refreshData();
			
		} else {
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("ACTIVE_FAIL"));
		}
		
		$j("#activeTime").val("");
		activeIds="";
	});
	
	$j("#activeCancleBtn").click(function(){
		$j("#active-dialog").dialogff({type:'close'});
		
	});
	
	$j(".button.butch.activeBtn").click(function(){
		$j(".checkId:checked").each(function(i, n) {
			if (i != 0) {
				activeIds += ",";
			}
			activeIds += $j(this).val();

		});
		if (activeIds == "") {
			nps.info(nps.i18n("ITEM_INFO_CONFIRM"), nps.i18n("ITEM_TIP_NOSEL_DISENABLED"));
			return;
		}
		$j("#active-dialog").dialogff({type:'open',close:'in',width:'600px',height:'200px'});
	});
	
	/** 商品导出和导入 **/
	$j('.itemExportImport').click(function(){
		window.location.href = itemExportImportUrl;
	});
});
