$j.extend(loxia.regional['zh-CN'],{ 
	"INDUSTRY_FIND":" 共找到",
	"INDUSTRY_RESULT":"个结果",
	"INDUSTRY_MESSAGE":"提示信息",
	"INDUSTRY_EXIST":"该行业已经存在",
	"INDUSTRY_EDIT_SUCCESS":"修改成功",
	"INDUSTRY_CHILD_SUCCRSS":"添加成功",
	"INDUSTRY_SELECT":"请先选择一个行业",
	"INDUSTRY_DELETE_SUCCRSS":"删除成功",
	"INDUSTRY_EDIT_NAME":"请输入要修改的名称",
	"INDUSTRY_EDIT_ROOT":"根节点无法进行修改",
	"INDUSTRY_NAME":"请输入你要添加行业的名称",
	"INDUSTRY_DELETE_ROOT":"根节点无法进行删除",
	"INDUSTRY_INSERT_PLACE":"请选择要插入的位置",
	"INDUSTRY_CHILD_EXIST":"子节点中已存在该行业",
	"INDUSTRY_HAVE_CHILD":"该行业有子节点无法删除",
	"INDUSTRY_ADMINISTRATOR":"操作失败请联系管理员",
	"INDUSTRY_DELETE_CONFIRM":"您确认要删除该行业？",
	"INDUSTRY_DISABLE_CHILD":"无效节点不允许添加子节点",
	"INDUSTRY_DISABLE_FATHER":"禁用该行业会同时禁用其子行业，确认禁用吗？",	
});

/**
 * ZTREE配置信息
 */
var setting = {
	
	check: {
		enable: false
	},
	view: {
		showIcon:false,
		fontCss: getFontCss
	},
	edit: {
		enable: false,
		showRenameBtn: false
	},
	data: {
		keep: {
			parent:false,
			leaf:false
		},
		key: {
			title: "name"
		},
		simpleData: {
			enable: true
		}
	},
	callback: {
		onClick: onClick
	}
};

var key,lastValue = "", nodeList = [], fontCss = {};

/**
 * 点击节点时触发Ztree的onClick事件
 * 1.为根节点时右侧名称置为空 
 * 2.非根节点右侧填写相应内容
 */
function onClick(event, treeId, treeNode)  {
	
	
	
	loxia.byId($j("input[name=tree_fid]")).state(null);
	loxia.byId($j("input[name=add_name]")).state(null);

	
	
	var isRoot = (treeNode.id==0);
	// 保存,删除,插入同级分类 按钮 如果 选择的是 root 那么 不可用
	$j("#save_father_Name,#remove_element").attr("disabled", isRoot ? "disabled" : false);
	
	if(isRoot){
		$j("#tree_fid").val("");
		$j("#tree_state").val(1);
	}else{
		$j("#tree_fid").val(treeNode.name);
		$j("#tree_state").val(treeNode.state);
		$j("#tree_sort").val(treeNode.sort);
		$j("#tree_fid").attr("treeId",treeId);
		$j("#tree_fid").data("treeNode",treeNode);
		$j("#tree_fid").data("event",event);
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

/**
 * 搜索节点方法
 * 1.搜索到含有相应关键字的节点 
 * 2.展开该段节点 
 * 3.将字置为黄色
 */
function searchNode(e) {
	var zTree = $j.fn.zTree.getZTreeObj("tree");
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

/**
 * 将搜索到的节点展开的方法
 */
function updateNodes(highlight) {
	var zTree = $j.fn.zTree.getZTreeObj("tree");
	for( var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}

/**
 * 将搜索到的节点字体置为黄色
 */
function getFontCss(treeId, treeNode) {
	return (!!treeNode.highlight) ? {color:"#333","background-color":"yellow"} : {color:"#333", "font-weight":"normal","background-color":""};		
}

/**
 * 验证行业是否重名
 */
function validateIndustryName(name,pId){
	var json={"pId":pId,"name":name};
	var _d = loxia.syncXhr(base+'/industry/validateIndustryName.json', json,{type: "GET"});
	if(_d.isSuccess){
		return true;
	}else{
		$j("#add_name").focus();
		return false;
	}
	
	
	
	
}

/**
 * 添加子节点
 * 1.验证行业名是否为空,为空则返回并将光标定位在输入框
 * 2.验证是否重名
 * 3.验证该节点是否无效节点，如果无效则不允许添加子节点
 * 4.验证当前是否选中要添加的节点位置
 */
function add(e) {
	if($j("#add_name").val()==""||$j("#add_name").val()==null){
		nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_NAME"));
		$j("#add_name").focus();
		return false;
	}
	
	var zTree = $j.fn.zTree.getZTreeObj("tree"),
	isParent = e.data.isParent,
	nodes = zTree.getSelectedNodes(),
	treeNode = nodes[0];
	
	if(!validateIndustryName($j("#add_name").val(),treeNode.id)){
		nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_EXIST"));
		return ;
	}

	if(treeNode.state==0){
		nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_DISABLE_CHILD"));
		return false;
	}
	
	if (treeNode) {
		var json={"parentId":treeNode.id,"lifecycle":1,"name":$j("#add_name").val(),"id":-1,"ids":"-1"};
		 nps.asyncXhrPost(base+'/industry/saveIndustry.json', json, {
			   successHandler : function(data, textStatus) {
			 		var backWarnEntity =  data ;
	  				if (backWarnEntity.isSuccess) {
	  					treeNode = zTree.addNodes(treeNode, {id:backWarnEntity.description, pId:treeNode.id, isParent:isParent, name:$j("#add_name").val(),state:1});
	  					zTree.editName(treeNode[0]);
	  					$j("#key").focus();
	  					$j("#"+treeNode[0].tId+"_span").click();
	  					$j("#add_name").val("");
	  					nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_CHILD_SUCCRSS"));
	  				} else {
	  					nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_ADMINISTRATOR"));
	  			}
	  		}
	  	 });
	} else {
		nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_INSERT_PLACE"));
	}
};



$j(document).ready(function(){
	$j.fn.zTree.init($j("#tree"), setting, zNodes);
	key = $j("#key");
	key.bind("focus", focusKey)
	.bind("blur", blurKey)
	.bind("propertychange", searchNode)
	.bind("input", searchNode);
	$j("#addLeaf").bind("click", {isParent:false}, add);
	$j("#tree_1_span").click();
	
	var treeObj = $j.fn.zTree.getZTreeObj("tree");
	treeObj.expandAll(true)
	var allNodes = treeObj.transformToArray(treeObj.getNodes());

	for(var i=0; i<allNodes.length; i++){
		if(allNodes[i].state==0){
			$j("#"+allNodes[i].tId+"_span").addClass('light-grey');
		}
	}

	/**
	 * 修改节点名称
	 * 1.验证修改名称是否为空
	 * 2.验证是否重名
	 * 3.如果当前节点是无效节点，则需要将子节点也置为无效
	 */
	$j("#save_father_Name").click(function(){
		if($j("#tree_fid").val()==null||$j("#tree_fid").val()==""){
			nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_EDIT_NAME"));
			$j("#tree_fid").focus();
			return false;
		}
		var zTree = $j.fn.zTree.getZTreeObj("tree");
		var nodes = zTree.getSelectedNodes();
		var sortNode ;
		if (nodes.length>0) {	
			if(nodes[0].id!=0){
			
			if(nodes[0].name!= $j("#tree_fid").val()){	
				if(!validateIndustryName($j("#tree_fid").val(),nodes[0].pId)){
					nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_EXIST"));
					return ;
				}
			}	
			if($j("#tree_state").val()==0 && nodes[0].children!=null && nodes[0].children.length>0 ){
				var allN = treeObj.transformToArray(nodes[0].children),ids ="";
				for(var i=0; i<allN.length; i++){
					ids += allN[i].id+","
				}
				nps.confirm(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_DISABLE_FATHER"), function(){			
					var json={"id":nodes[0].id,"lifecycle":$j("#tree_state").val(),"name":$j("#tree_fid").val(),"parentId":-1,"ids":ids};
					nps.asyncXhrPost(base+'/industry/saveIndustry.json', json, {
						   successHandler : function(data, textStatus) {
							var backWarnEntity =  data ;
							if (backWarnEntity.isSuccess){
				  			nodes[0].name = $j("#tree_fid").val();
							nodes[0].state = $j("#tree_state").val();
							$j("#"+nodes[0].tId+"_span").addClass('light-grey');
							
							
							for(var i=0; i<allN.length; i++){
									allN[i].state=0;
									zTree.updateNode(allN[i]);
									$j("#"+allN[i].tId+"_span").addClass('light-grey');
							}				
							zTree.updateNode(nodes[0]);
							nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_EDIT_SUCCESS"));
				  		 }
				  		}
				  	 });
				});
			}else{
				var json={"id":nodes[0].id,"lifecycle":$j("#tree_state").val(),"name":$j("#tree_fid").val(),"parentId":-1,"ids":"-1"};
		  		
				nps.asyncXhrPost(base+'/industry/saveIndustry.json', json, {
					   successHandler : function(data, textStatus) {	
							var backWarnEntity =  data ;
							if (backWarnEntity.isSuccess){
		  					nodes[0].name = $j("#tree_fid").val();
							nodes[0].state = $j("#tree_state").val();
							if(nodes[0].state==0){
								$j("#"+nodes[0].tId+"_span").addClass('light-grey');
							}else{
								$j("#"+nodes[0].tId+"_span").removeClass('light-grey')
							}
							zTree.updateNode(nodes[0]);
							nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_EDIT_SUCCESS"));
			  		 }
			  		}
			  	 });
			}
			}else{
				nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_EDIT_ROOT"));
			}
		}else{
			nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_SELECT"));
		}
	});	
	
	/**
	 * 删除节点
	 * 1.根节点不允许删除
	 * 2.如果该节点下含有子节点也不允许删除
	 */
	$j("#remove_element").click(function(){
		 	var treeObj = $j.fn.zTree.getZTreeObj("tree");
			var nodes = treeObj.getSelectedNodes();
				if (nodes && nodes.length>0) {
				  if(nodes[0].id!=0){
					if(!nodes[0].isParent){
					nps.confirm(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_DELETE_CONFIRM"), function(){

					var json={"id":nodes[0].id};
					nps.asyncXhrPost(base+'/industry/removeIndustry.json', json, {
						   successHandler : function(data, textStatus) {	
							var backWarnEntity = data;
							if(backWarnEntity.isSuccess){
			  					if(nodes[0].getPreNode()!=null){
				  					$j("#"+nodes[0].getPreNode().tId+"_span").click();
				  				}else if(nodes[0].getNextNode()!=null){
				  					$j("#"+nodes[0].getNextNode().tId+"_span").click();
				  				}else{
				  					$j("#"+nodes[0].getParentNode().tId+"_span").click();
				  				}
				  				treeObj.removeNode(nodes[0]);
				  				nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_DELETE_SUCCRSS"));
							}else{
					  			nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_ADMINISTRATOR"));
					  		}
				  		}
				  	 });
					});
					}else{
						nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_HAVE_CHILD"));
					}
				  }else{
					  nps.info(nps.i18n("INDUSTRY_MESSAGE"), nps.i18n("INDUSTRY_DELETE_ROOT"));	
				  }
			}else{
				nps.info(nps.i18n("INDUSTRY_MESSAGE"),nps.i18n("INDUSTRY_SELECT"));
			}
		});	
});