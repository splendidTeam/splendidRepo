$j.extend(loxia.regional['zh-CN'], {
	"LABEL_AREA_DESIGNATE" : "区域代称",
	"LABEL_AREA_AREAID" : "目的地名称",
	"LABEL_AREA_TYPE" : "类型",
	"LABEL_AREA_GROUPNO" : "分组编号",
	"LBAEL_AREA_OPERATE" : "操作",
	

	"INFO_TITLE_DATA" : "提示信息"
});
// Json格式动态获取数据库信息
var supportedAreaListUrl = base + '/freight/supportedAreaList.json';
//return 物流方式界面
var returnDistributionUrl = base + '/freight/distributionMode.htm';
//ztree 异步加载数据
var ztreeAsyncLoadDataUrl = base + '/freight/ztreeAsyncLoadData.json';

//搜索区域地址
var searchAreaTableUrl = base + '/freight/searchAreaData.json';

// 获取id
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' name='chid' class='checkId' value='" + loxia.getObject("id", data) + "' disabled='disabled'/>";
}
// 刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}
//类型名单
function drawTypeName(data) {
	var typeName = loxia.getObject("type", data);
	var result = "";
	if(typeName == "white"){
		result = "白名单";
	}else{
		result = "黑名单";
	}
	return result;
}
//类型名单
function drawAreaName(data) {
	var typeName = loxia.getObject("type", data);
	var areaName = loxia.getObject("area", data);
	var result = "";
	if(typeName == "white"){
		result = areaName;
	}else{
		result = "<img src='"+base+"/images/wmi/blacks/16x16/16x16newArray.png'/>"+areaName;
	}
	return result;
}

//省份
var setting = {
		check : {
			enable: false
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
				enable : true,
				 idKey: "id",  
				 pIdKey: "pId",  
				 rootPId: 0 
			}
		},
		async : {
			 enable: true,    
             url:getUrl,    
//             otherParam: ["id", "1", "name", "test","isParent":true]
//           dataType: "text",//默认text  
//           type:"get",//默认post           
		},
		callback : {
			asyncSuccess: zTreeOnAsyncSuccess,//异步加载成功的fun  
			beforeClick: beforeClickZtree ,
			onClick : onClick,
			onCheck : onCheck
		}
	};

function filter(treeId, parentNode, childNodes) {
	var nodes = zNodes;
	if(childNodes){
		for(var i=0; i<childNodes.length; i++){
			nodes.push(childNodes[i]);
		}
	}
	return nodes;
 }

//单击事件   
function beforeClickZtree(treeId, treeNode){   
//    alert(treeNode.id+","+treeNode.name);   
//	var treeObj = $j.fn.zTree.getZTreeObj("tree");
//	treeObj.reAsyncChildNodes(treeNode, "refresh");

}   


function getUrl(treeId, treeNode) {
	var param = "id="+treeNode.id ;
	return ztreeAsyncLoadDataUrl+"?" + param;
}


function zTreeOnAsyncSuccess(event, treeId, treeNode, msg){ }   
function onClick(event, treeId, treeNode) {
	
}	

function onCheck(event, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("tree");
	nodes = zTree.getCheckedNodes(true);
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
	
	$j("#search_result_left").html("匹配地址找到"+ nodeList.length+ "个结果");
	
	if (nodeList.length > 0) {
	$j.each(nodeList, function(i, node){      
 		 zTree.expandNode(node.getParentNode(),true, true, true);
		}); 
	}
	updateNodes(true);
	$j("#key-left").focus();
}
function updateNodes(highlight) {
	var zTree = $j.fn.zTree.getZTreeObj("tree");
	for( var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}
var zNodes = [];

function refreshAreaTree() {
	var ztree = $j.fn.zTree.getZTreeObj("tree");
	ztree.refresh(); // 重置树
}

$j(document).ready(function() {

	loxia.init({
		debug : true,
		region : 'zh-CN'
	});

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
//分组树筛选
	key = $j("#key-left");
	key.bind("focus", focusKey)
	.bind("blur", blurKey)
	.bind("propertychange", searchNode)
	.bind("input", searchNode);
	
	
	
	//窗口地址查询
	$j(".dialog-btn-search").click(function() {
		$j(".tbl-result tbody").empty();
		var areaname = $j("#key-name");
		var value = $j.trim(areaname.get(0).value);
		var zTree = $j.fn.zTree.getZTreeObj("tree");
		var nodeList = zTree.getNodesByParamFuzzy("name", value);
		var html = "";
		if (nodeList.length > 0) {
			$j.each(nodeList, function(i, node){      
				var name = node.name;
				var parent = node.getParentNode();
				while(parent!= null && parent.id!=1){
					name =parent.name+"->"+name;
					parent = parent.getParentNode();
				}
		 		 html += "<tr><td>"+node.id+"</td><td>"+name+"</td></tr>";
			}); 
		}
		$j(".tbl-result tbody").append(html);
	});
	
	//窗口‘确定’按钮点击事件
	$j("#dialog-address-search .dialog-btn-ok").click(function() {
		$j("#key-name").val("");
		$j(".tbl-result tbody").empty();
    	$j("#dialog-address-search").dialogff({type : 'close'});
    	refreshAreaTree();
	});
	
	
	//地址查询
	$j(".search-address").click(function() {
		dialogRefreshData();
		$j("#dialog-address-search").dialogff({type:'open',close:'in',width:'700px',height:'500px'});
	});
	
	// 动态获取数据库区域信息表
	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		cols : [
//		        {
//			label : "<input type='checkbox' disabled='disabled'/>",
//			width : "5%",
//			template : "drawCheckbox"
//		},
//		{
//			name : "designate",
//			label : nps.i18n("LABEL_AREA_DESIGNATE"),
//			width : "10%"
//		},
		{
			name : "area",
			label : nps.i18n("LABEL_AREA_AREAID"),
			width : "10%",
			template : "drawAreaName"
		}, {
			name : "type",
			label : nps.i18n("LABEL_AREA_TYPE"),
			width : "10%",
			template : "drawTypeName"
		}, {
			name : "groupNo",
			label : nps.i18n("LABEL_AREA_GROUPNO"),
			width : "10%"
		}
//		,  {
//			label : nps.i18n("LBAEL_AREA_OPERATE"),
//			width : "15%",
//			type : "oplist",
//			oplist : drawEditItem
//
//		} 
		],
		dataurl : supportedAreaListUrl+"?distributionId="+distributionId
	});
	refreshData();

	// 筛选
//	$j(".func-button.search").click(function() {
//		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
//		refreshData();
//	});
	
	//返回
	$j(".return-back").click(function(){
		window.location.href = returnDistributionUrl;
	});
	
	
	
//--------------------------------- 弹出窗口 ----------------------------------------------
	
	/** *** area table ***** */
	$j("#area-table").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		form : "dialogSearchForm",
		cols : [ 
		{
			name : "id",
			label : "区域编号",
			width : "15%",
			
		}, {
			name : "name",
			label : "区域名称",
			width : "20%",
			
		}],
		dataurl : searchAreaTableUrl
	});

	//筛选
	$j('#dialog-address-search').on('click', ".func-button.search", function(){ 
		 dialogRefreshData();
    });  
	
	/**
	 * 确定
	 */
	$j('#dialog-address-search').on('click', '.confirm', function(){
		$j(".dialog-close").click();
	});
	/**
	 * 取消
	 */
	$j('.cencal').click(function(){
		$j("#name").val("");
		dialogRefreshData();
		$j(".dialog-close").click();
	});
});

/**
 * 刷新数据
 */
function dialogRefreshData() {
	$j("#area-table").loxiasimpletable("refresh");
}