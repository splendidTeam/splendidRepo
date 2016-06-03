//zTree setting
var setting = {
	check : {
		enable : true,
		chkStyle : "radio",
		radioType : "all"
	},
	view : {
		dblClickExpand : true,
		showIcon : false,
		fontCss : getFontCss
	},
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {
		onClick : onClick,
		beforeRemove : beforeRemove,
		onRemove : onRemove,
		onCheck : onCheck,
	}
};

var key,lastValue = "", nodeList = [], fontCss = {};
function onCheck(event, treeId, treeNode){
	
}

function onClick(event, treeId, treeNode){
	var treeObj = $j.fn.zTree.getZTreeObj("industrytreeDemo");
	var nodes = treeObj.transformToArray(treeObj.getNodes());
	for(var i = 0;i<nodes.length;i++){
		if(!nodes[i].isParent){
			nodes[i].checked = false;
			treeObj.refresh();
		}
	}
		treeNode.checked = true;
		treeObj.refresh();
		onCheck(event, treeId, treeNode);
}

function beforeRemove(event, treeId, treeNode){
	
}

function onRemove(event, treeId, treeNode){
	
}

//*******************************默认分类
function defaultOnClick1(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	
	hideMenu("defaultMenuContent");
	return false;
}

function defaultOnCheck1(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree"),
	nodes = zTree.getCheckedNodes(true),
	v = "",
	categoryHtml = "";
	var defaultHtml = $j("#chooseDefaultCategory").html();
	var id = $j('#chooseDefaultCategory').children('div').attr('class');
	$j("#chooseDefaultCategory").html("");
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
		var inode =$j("."+nodes[i].id+"");
		if(inode.length==0){
			categoryHtml = "<div class="+nodes[i].id +">"+nodes[i].name + 
			"<input type='hidden' name='defaultCategoryId'  value='"+nodes[i].id+"' />"+
			"<a href='javascript:void(0);'id="+nodes[i].id+" style='float:right;margin-right: 760px;text-decoration: underline;color:#F8A721' onclick='delDefaultCategroy(this.id)'>"+nps.i18n("DELETE_THIS_CATEGORY")+"</a><br/></div>";
			$j("#chooseDefaultCategory").append(categoryHtml);
		}else{
			$j("#chooseDefaultCategory").html(defaultHtml);
			zTree.checkNode(treeNode, !treeNode.checked, null, false);
			
			var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree"),
			nodes = zTree.getCheckedNodes(false);
			for (var i=0, l=nodes.length; i<l; i++) {
				if(id==nodes[i].id){
					nodes[i].checked= true;
				}
			}
			zTree.refresh();
			
			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("NOT_REPEATEDLY_RELEVANCE_CATEGORY"));
			return;
		}
		
	}
}

//删除默认分类
function delDefaultCategroy(id){
	$j("."+id+"").remove();
	var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree"),
	nodes = zTree.getCheckedNodes(true);
	for (var i=0, l=nodes.length; i<l; i++) {
		if(id==nodes[i].id){
			nodes[i].checked= false;
		}
	}
	zTree.refresh();
}

function onBodyDownDefault(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "industry" || event.target.id == "defaultMenuContent" || $j(event.target).parents("#defaultMenuContent").length>0)) {
		hideMenu("defaultMenuContent");
	}
}
/**********************industrytreeDemo 展示  end********************/

/**
* 搜索节点方法
* 1.搜索到含有相应关键字的节点 
* 2.展开该段节点 
* 3.将字置为黄色
*/
function searchNode(e) {
	var zTree = $j.fn.zTree.getZTreeObj("industrytreeDemo");
	var value = $j.trim(key.get(0).value);
	if(value==""){
		$j("#search_result").html("");
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
	
	$j("#search_result").html(nps.i18n("INDUSTRY_FIND")+ nodeList.length+ nps.i18n("INDUSTRY_RESULT"));
	
	if (nodeList.length > 0) {
	$j.each(nodeList, function(i, node){      
		 zTree.expandNode(node.getParentNode(),true, true, true);
		}); 
	}
	updateNodes(true);
	$j("#key").focus();
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
/**
* 将搜索到的节点字体置为黄色
*/
function getFontCss(treeId, treeNode) {
	return (!!treeNode.highlight) ? {color:"#333","background-color":"yellow"} : {color:"#333", "font-weight":"normal","background-color":""};		
}
/**
* 将搜索到的节点展开的方法
*/
function updateNodes(highlight) {
	var zTree = $j.fn.zTree.getZTreeObj("industrytreeDemo");
	for( var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}
/**********************industrytreeDemo 搜索  end********************/

$j(document).ready(function(){
	$j(".select-btn input[type='radio']").on("change",function(){
		var modal = $j(this).closest(".select-btn").next(".pro-modal");
		if($j(this).prop("checked")){
			if($j(this).hasClass("normalpro") || $j(this).hasClass("virtualpro")){
				modal.show();
			}else{
				modal.hide();
			}
		}
	})
	
	$j(".select-btn input[type='radio']").trigger('change');
	
	// 初使化普通商品/虚拟商品行业选择树
	$j.fn.zTree.init($j("#industrytreeDemo"), setting, zNodes);
    
    key = $j("#key");
	key.bind("focus", focusKey)
	.bind("blur", blurKey)
	.bind("propertychange", searchNode)
	.bind("input", searchNode);
	
	var treeObj = $j.fn.zTree.getZTreeObj("industrytreeDemo");
	//1.将所有的节点转换为简单 Array 格式
	var nodes = treeObj.transformToArray(treeObj.getNodes());
	var booleanFlag = true;
	for(var i = 0;i<nodes.length;i++){
		//2.如果此节点为父节点 或者 为ROOT节点 ，则让此节点没有radio选框
		if(nodes[i].isParent || nodes[i].id == 0){
			nodes[i].nocheck = true;
		}else{
			if(booleanFlag){
				//3.第一个叶子节点的 radio为checked，然后把booleanFlag设置为false
				nodes[i].checked = true;
				var node = nodes[i].getParentNode();
				console.log(node.name);
				if(null != node){
					node.open = true;
					firstNodeId=nodes[i].id;
					firstNodeName=nodes[i].name;
					booleanFlag = false;
				}
				//onCheck(event, treeId, nodes[i]);
				
			}
		}
		treeObj.refresh();
	}
	
	// 下一步
	$j(".button.orange.next").on("click",function(){
		var itemType = $j(".select-btn input[type='radio']:checked").val();

		if(itemType == '1' || itemType == '7') {
			// 如果是普通商品或者虚拟商品，校验是否选择了行业
			var ztree = $j.fn.zTree.getZTreeObj("industrytreeDemo");
			var nodes_ = ztree.getCheckedNodes(true);
			if (nodes_.length > 0) {
				var industryId = nodes_[0].id;
				window.location.href = base + "/item/" + (itemType == '1' ? "createNormalItem" : "createVirtualItem") + ".htm?industryId=" + industryId;
			} else {
				nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("SYSTEM_ITEM_SELECT_INDUSTRY"));
			}
		} else if(itemType == '3') {
			window.location.href = base + "/item/createBundleItem.htm";
		} else if(itemType == '5') {
			// TODO 去往搭配商品编辑页面
		} else {
			
		}
	});
	
	// 返回
	$j(".button.return").on("click",function(){
		window.location.href = base + "/item/itemList.htm";
	});
});