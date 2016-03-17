$j.extend(loxia.regional['zh-CN'], {
	"ITEM_CATEGORY_TIPS":"友情提示",
	"ITEM_CATEGORY_CHOOSE_LEAF":"请选择叶子节点来查看商品！",
	"LABEL_ITEM_CODE":"商品编码",
	"LABEL_ITEM_NAME":"商品名称",
	"LABEL_ITEM_SALE_PRICE":"销售价",
	"LABEL_ITEM_LIST_PRICE":"吊牌价",
	"ITEM_CATEGORY_CHECK_ERROR":"错误信息",
	"ITEM_CATEGORY_OPERATE_INFO":"提示信息",
    "ITEM_CONFIRM_BIND":"确认加入分类",
    "ITEM_CONFIRM_BIND_SUCCESS":"选择成功",
	"NO_CATEGORY":"无",
	"ITEM_CATEGORY_LIST_SUFFIX_TITLE":"分类商品列表",
	"ITEM_CATEGORY_LIST_DEFAULT_TITLE":"已分类商品",
	"CATEGORY_FIND":" 共找到",
  });

//常量 url,setting等

//已分类商品列表url
var itemCtListUrl = base + '/item/itemPromotionSelect.json';

//未分类商品列表url
var itemNoctListUrl = base + '/item/itemNoctList.json';

//加入分组
var bindItemUrl = base +'/item/bindItemCategory.json';

//脱离分组
var unBindItemUrl = base +'/item/unBindItemCategory.json';

var validateUnBindSelUrl = base +'/item/validateUnBindByItemIdsAndCategoryId.json';



//fenlei
var setting = {

		check : {
			enable: true,
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
			//onClick : onClick,
			onCheck : onCheck
		}
	};


//函数
function onClick(event, treeId, treeNode) {
	var tempTreeId="";
	var tempTreeName="";
	
	if(treeNode.isParent||treeNode.id ==0){
		nps.info(nps.i18n("ITEM_CATEGORY_TIPS"),nps.i18n("ITEM_CATEGORY_CHOOSE_LEAF"));
	}else{
		tempTreeId=treeNode.id;
		tempTreeName=treeNode.name;
		$j("#categoryId").val(tempTreeId);
		
		//emptySearch();
		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();

		showItemList(tempTreeName);
	}
}	

function onCheck(event, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("tree");
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
		
		$j("#categoryId").val(id);
		
		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();

		showItemList(tempTreeName);
	}
}

function showItemList(name){
	
	var title="";
	if(null == name||'' == name){
		
		title = nps.i18n("ITEM_CATEGORY_LIST_DEFAULT_TITLE");
			
	}else{
		title="【"+name+"】"+nps.i18n("ITEM_CATEGORY_LIST_SUFFIX_TITLE");
			
	}
	$j("#table1 .ui-loxia-table-title").html(title);
	$j("#table1").attr("caption",title);
	$j("#table1").data().uiLoxiasimpletable.options.title=title;
}



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


var lastValue = "", nodeList = [], fontCss = {};


function drawCheckbox(data, args, idx){
	return "<input name='chedkId' type='checkbox' class='checkId'  value='" + loxia.getObject("id", data)+"'/>";
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
//刷新表格数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}

$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	
	searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});
	
	//分组树
	$j.fn.zTree.init($j("#tree"), setting, zNodes);
	
	var treeObj = $j.fn.zTree.getZTreeObj("tree");
	var nodes = treeObj.transformToArray(treeObj.getNodes());
	for(var i = 0;i<nodes.length;i++){
		if(nodes[i].isParent){
			nodes[i].nocheck = true;
			treeObj.refresh();
		}
	}
	
	//商品列表

	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
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
			sort:["tpit.code asc","tpit.code desc"]
		},
		{
			name : "title",
			label : nps.i18n("LABEL_ITEM_NAME"),
			width : "10%",
			template:"getTitle",
			sort:["tpifo.title asc","tpifo.title desc"]
		}, {
			name : "salePrice",
			label : nps.i18n("LABEL_ITEM_SALE_PRICE"),
			width : "10%",
			template:"getSalePrice",
			sort:["tpifo.sale_price asc","tpifo.sale_price desc"]
		}, {
			name : "listPrice",
			label : nps.i18n("LABEL_ITEM_LIST_PRICE"),
			width : "12%",
			sort:["tpifo.list_price asc","tpifo.list_price desc"]
		} ],
		dataurl : itemCtListUrl
	});
	
	refreshData();
	
	// 筛选列表
	$j(".func-button.search").click(function() {
		//置空分类条件
		$j("#categoryId").val("");
		var defaultTitle = nps.i18n("ITEM_CATEGORY_LIST_DEFAULT_TITLE");
		$j("#table1 .ui-loxia-table-title").html(defaultTitle);
		$j("#table1").attr("caption",defaultTitle);
		$j("#table1").data().uiLoxiasimpletable.options.title=defaultTitle;
		
		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		
		refreshData();
	});
	
	//选择目录
	$j(".func-button.selectCategorys").on("click", function(){
		var zTree = $j.fn.zTree.getZTreeObj("tree");
		nodes = zTree.getCheckedNodes(true);
		
		var arr = new Array();

		//getParentNode();treeNode.id ==0父节点

		var id,name,parentNode;
		for (var i=0, l=nodes.length; i<l; i++) {	
			id = nodes[i].id ;
			name = nodes[i].name;
			if(nodes[i].isParent||nodes[i].id ==0){
			}else{
				parentNode = nodes[i].getParentNode();
				name = parentNode.name+"->"+name;
			}
			arr[i]={"categoryid":id,
					"description":name};
			alert(name);
		}
		if (id.length > 0 ) id = id.substring(0, id.length-1); 
		
		var json = {
				'type' : "category",
				'list' : arr
				
		};
		// 提交表单
		var _d = nps.syncXhr("URL", json,{type: "POST"});
    	if(_d.isSuccess){
    		nps.info(nps.i18n("ITEM_CATEGORY_OPERATE_INFO"),nps.i18n("ITEM_CONFIRM_BIND_SUCCESS"));
        	refreshData();
    	}
    	
	});
	
	
	//选择商品
	$j(".func-button.selectItems").on("click", function(){
		
		var arr = new Array();
		var itemid,description,price;
		
    	$j(".checkId:checked").each(function(i,n){
        	
        	itemid = $j(this).val()
        	description =$j("#title_"+$j(this).val()).val();
        	price = $j("#salePrice_"+$j(this).val()).val();
        	alert(description);
        	arr[i]={"itemid":itemid,
					"description":name,
					"price":price};
        });
    	
    	var json = {
    			'type' : "product",
    			'list' : arr
    	};
    	// 提交表单
    	var _d = nps.syncXhr("URL", json,{type: "POST"});
    	if(_d.isSuccess){
    		nps.info(nps.i18n("ITEM_CATEGORY_OPERATE_INFO"),nps.i18n("ITEM_CONFIRM_BIND_SUCCESS"));
    		refreshData();
    	}
    });
	
});
