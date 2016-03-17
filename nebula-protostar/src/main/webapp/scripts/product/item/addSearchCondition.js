$j.extend(loxia.regional['zh-CN'],{ 
	 	"ITEM_CATEGORY_TIPS":"错误信息",
	 	"ITEM_CATEGORY_CHOOSE_LEAF":"必须选择叶子节点",
	 	"ITEM_CATEGORY_CHOOSE_NOT_ROOT":"ROOT节点不能被选择"
});

var conditionUrl=base+"/item/itemSearchCondition/manager.htm";
var propertyListUrl=base+"/item/itemSearchCondition/findPropertyByIndustryId.json";
var hiddenDiv;
var lastValue = "", nodeList = [], fontCss = {};
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


/**
 * 检验select是否选择
 * @param sel
 * @param obj
 * @returns
 */
function checkSelect(sel, obj){
	if(obj.val()==""){	
    	var errorMessage = nps.i18n("EMPTY");
   		obj.state(false,errorMessage);
   		return loxia.FAILURE;
    }
    return loxia.SUCCESS;
}

function checkInput(sel, obj){
	if(obj.val()==""){	
    	var errorMessage = nps.i18n("EMPTY");
   		obj.state(false,errorMessage);
   		return loxia.FAILURE;
    }
    return loxia.SUCCESS;
}

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

function onClick(event, treeId , treeNode) {
	var tempTreeId="";
	var tempTreeName="";
	
	if(treeNode.name=="ROOT"){
		nps.info(nps.i18n("ITEM_CATEGORY_TIPS"),nps.i18n("ITEM_CATEGORY_CHOOSE_NOT_ROOT"));
	}else if(treeId!="tree-left2"&&(treeNode.isParent||treeNode.id ==0)){
		alert(treeId);
		nps.info(nps.i18n("ITEM_CATEGORY_TIPS"),nps.i18n("ITEM_CATEGORY_CHOOSE_LEAF"));
	}else{
		var zTree = $j.fn.zTree.getZTreeObj(treeId);
		if(treeNode.checked){
			zTree.checkNode(treeNode, false, null , null);
		}else{
			zTree.checkNode(treeNode, true, null , null);
		}
		tempTreeId=treeNode.id;
		tempTreeName=treeNode.name;		
	}
}	

function onCheck(event, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("tree-left");
	nodes = zTree.getCheckedNodes(true);
	
	var tempTreeName="";

	if(treeNode.name=="ROOT"){
		nps.info(nps.i18n("ITEM_CATEGORY_TIPS"),nps.i18n("ITEM_CATEGORY_CHOOSE_NOT_ROOT"));
	}else if(treeId!="tree-left2"&&(treeNode.isParent||treeNode.id ==0)){
		alert(treeId);
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


$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	
	$j.fn.zTree.init($j("#categoryDemo"), categorySetting, zNodes);
	$j.fn.zTree.init($j("#tree-left"), setting, zNodes);
	$j.fn.zTree.init($j("#tree-left2"), setting, zNodes2);
	var treeObj = $j.fn.zTree.getZTreeObj("tree-left");
	var nodes = treeObj.transformToArray(treeObj.getNodes());
	for(var i = 0;i<nodes.length;i++){
		if(nodes[i].isParent){
			nodes[i].nocheck = true;
			treeObj.refresh();
		}
	}
//	var treeObj = $j.fn.zTree.getZTreeObj("tree-left2");
//	var nodes = treeObj.transformToArray(treeObj.getNodes());
//	for(var i = 0;i<nodes.length;i++){
//		if(nodes[i].isParent){
//			nodes[i].nocheck = true;
//			treeObj.refresh();
//		}
//	}
//分组树筛选
	key = $j("#key-left");
	key.bind("focus", focusKey)
	.bind("blur", blurKey)
	.bind("propertychange", searchNode)
	.bind("input", searchNode);
 	
 	var treeObjCategory = $j.fn.zTree.getZTreeObj("categoryDemo");
 	var treeCategoryNodes = treeObjCategory.transformToArray(treeObjCategory.getNodes());
 	for(var i = 0;i<treeCategoryNodes.length;i++){
 		if(treeCategoryNodes[i].isParent){
 			treeCategoryNodes[i].nocheck = true;
 			treeObjCategory.refresh();
 		}
 	}
 	
	$j("#addIndustry").live('click',function(){
		$j("#dialog-category-select-left").dialogff({type:'open',close:'in',width:'400px',height:'500px'});
	});
	
	$j("#addType").click(function(){
		$j("#dialog-category-select-left2").dialogff({type:'open',close:'in',width:'400px',height:'500px'});
	});
	
	$j("#submitButton").click(function(){
		nps.submitForm('submitForm1',{mode:'sync'});
	});

	$j("#canel").click(function(){
		window.location.href=conditionUrl+"?keepfilter=true";
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
				$j("#industry").val(text);
				$j("#industryId").val(ids.substring(1));
				$j("#dialog-category-select-left").dialogff({type : 'close'});
				
				var data2=nps.syncXhrPost(propertyListUrl,{industryId:$j("#industryId").val()});
				var list2=data2.propertyList;
				$j("#property").empty();
				$j("#property").append("<option value=''>未选择</option>");
				for(var i=0;i<list2.length;i++){
					$j("#property").append("<option value='"+list2[i].id+"'>"+list2[i].name+"</option>");
				}
				
				if(null!=loxia.byId("industry")){
					loxia.byId("industry").check();
				}
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
				$j("#category").val(text);
				$j("#categoryId").val(ids.substring(1));
				$j("#dialog-category-select-left2").dialogff({type : 'close'});
				loxia.byId("category").check();
			});
			
			$j("#view-block-category .btn-ok").click(function() {
				$j("#view-block-category").dialogff({type : 'close'});
			});
			$j("#view-block-all .btn-ok").click(function() {
				$j("#view-block-all").dialogff({type : 'close'});
			});
			
			$j("#type").change(function(){
				if(hiddenDiv==null){
					hiddenDiv="<div class='ui-block-line navi-param' style='display: block;'>";
					hiddenDiv+="<label>行业</label>";
					hiddenDiv+="<input type='text' mandatory='true' checkmaster='checkInput' readOnly='readOnly' value='' id='industry' placeholder='行业' loxiaType='input' aria-disabled='true'>";
					hiddenDiv+="<input type='hidden' value='' name='industryId' id='industryId' >";
					hiddenDiv+="<a href='javascript:void(0);' id='addIndustry' class='func-button view-block-item'>选择行业</a>";
					hiddenDiv+="</div>";
					hiddenDiv+="<div class='ui-block-line'>";
					hiddenDiv+="<label>商品属性</label>";
					hiddenDiv+="<select id='property' loxiaType='select' mandatory='true' name='propertyId' checkmaster='checkSelect'>";
					hiddenDiv+="<option value=''>未选择</option>";
					hiddenDiv+="</select>";
					hiddenDiv+="</div>";
				}
				if($j(this).val()==3){
					$j("#hiddenDiv").empty();
				}else{
					if($j("#hiddenDiv").html().trim()==""||$j("#hiddenDiv").html().trim()==null){
						$j("#hiddenDiv").html(hiddenDiv);
						loxia.initContext($j("#industry"));
						loxia.initContext($j("#property"));
					}
				}
			});
			
			if(null!=$j("#id").val()){
				$j("#dialog-category-select-left .btn-ok").click();
				$j("#dialog-category-select-left2 .btn-ok").click();
				$j("#property").val($j("#propertyId").val());
			}
			
			$j("#property").live('change',function(){
				if($j(this).val()!=""){
					$j("#name").val($j(this).find("option:selected").text());
				}
			});
			
});
