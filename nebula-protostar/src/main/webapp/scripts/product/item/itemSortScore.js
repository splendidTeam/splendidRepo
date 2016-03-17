$j.extend(loxia.regional['zh-CN'],{
	"TO_VIEW":"查看",
	"TO_UPDATE":"修改",
	"TO_ENABLE":"启用",
	"TO_DEL":"删除",
	"PROPERT_OPERATOR_TIP":"属性提示信息",
	"PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的排序么？",
	"INFO_TITLE_DATA":"提示信息",
	"INFO_DELETE_SUCCESS":"删除记录成功!",
    "INFO_DELETE_FAIL":"删除记录失败!",
    "INFO_START_SUCCESS":"启用成功!",
    "INFO_START_FAIL":"启用失败!",
    "INFO_CANCLE_START_SUCCESS":"取消启用成功!",
    "INFO_CANCEL_START_FAIL":"取消启用失败!",
	"TO_CANCEL":"禁用"
});

// 活动列表
var sortScoreUrl = base+'/item/sortScoreList.json'; 
// 启用
var ACTIVATION_URL = base + "/item/enableSortScore.json";

//分类树
var setting = {
	check : {
		enable: true,
		chkStyle: "radio",
		radioType:"all"
	},
	view : {
		showIcon : false,
		selectedMulti: false,
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
//行业分类树
var industrsetting = {
	check : {
		enable: true,
		chkStyle: "radio",
		radioType:"all"
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
		onClick : industronClick,
		onCheck : industronCheck
	}
};
function getFontCss(treeId, treeNode) {
	return (!!treeNode.highlight) ? {
		color : "#333",
		"background-color" : "yellow"
	} : {
		color : "#333",
		"font-weight" : "normal",
		"background-color" : ""
	};
};
function onClick(event, treeId, treeNode) {
};

function onCheck(event, treeId, treeNode) {
};
function industronClick(event, treeId, treeNode) {
};

function industronCheck(event, treeId, treeNode) {
	//根据行业获取属性
	var data = nps.syncXhrPost(base+"/property/propertyListByIndustryid.json", {'industryId': treeNode.id});
	var list = data.description;
	if(list!=null && list!=""){
		$j("#per_select").empty();
		var op ="";
		for ( var index = 0; index < list.length; index++) {
			 var per = list[index];
			 op+="<option value='"+per.id+"'>"+per.name+"</option>";
		}
		$j("#per_select").append(op);
	}else{
		$j("#per_select").empty();
	}
	
};

function drawEditor(data){
	var id=loxia.getObject("id", data);
	//lifecycle
	var activeMark=loxia.getObject("lifecycle",data);
	var update="<a href='javascript:void(0);' val='"+id+"' class='func-button editor'>"+nps.i18n("TO_UPDATE")+"</a>";
	var enable ;
	if(activeMark == 0){
		enable="<a href='javascript:void(0);' val='"+id+"' class='func-button enable'>"+nps.i18n("TO_ENABLE")+"</a>";
	}else{
		enable="<a href='javascript:void(0);' val='"+id+"' class='func-button disable'>"+nps.i18n("TO_CANCEL")+"</a>";
	}
    var del="<a href='javascript:void(0);' val='"+id+"' class='func-button delete'>"+nps.i18n("TO_DEL")+"</a>";
	
	return update+enable+del;
}


// 刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}

// 获取日期格式
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		if(val.createTime==null||val.createTime==''){
			return "&nbsp;";
		}
		var date=new Date(val.createTime);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
} 

function lifecycle(data){
	var type = loxia.getObject("lifecycle", data);	
	var result = "";
	if(type == 1){
		result = "有效";
	}else{
		result = "无效";
	}
 	return result; 
} 
function operator(data){
	var type = loxia.getObject("operator", data);	
	var result = "";
	if(type == "GREATER"){
		result = "大于";
	}
	if(type == "LESS"){
		result = "小于";
	}
	if(type == "EQUAL"){
		result = "等于";
	}
 	return result; 
} 
 
function type(data){
	var type = loxia.getObject("type", data);	
	var result = "";
	if(type == 1){
		result = "分类";
	}
	if(type == 2){
		result = "属性";
	}
	if(type == 3){
		result = "吊牌价";
	}
	if(type == 4){
		result = "销量";
	}
	if(type == 5){
		result = "收藏";
	}
	if(type == 6){
		result = "销售价";
	}
 	return result; 
} 



/**
 * checkbox
 */
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' name='id' value='"
			+ loxia.getObject("id", data) + "'/>";
}

/**
 * 有效或无效优惠劵
 */
function updateActive(val,activeMark){
	var msg = "";
	if(activeMark== 1){
		msg = "确定要启用排序规则?";
	}else{
		msg = "确定要禁用排序规则?";
	}
	nps.confirm(nps.i18n("INFO_TITLE_DATA"),msg,function(){
		var json={"ids":val,"state":activeMark}; 
	  	nps.asyncXhrPost(ACTIVATION_URL, json,{successHandler:function(data, textStatus){
			var backWarnEntity = data;
				if (backWarnEntity.isSuccess) {
					if(activeMark==1){
						nps.info(nps.i18n("INFO_TITLE_DATA"),"启用成功");
					}else{
						nps.info(nps.i18n("INFO_TITLE_DATA"),"禁用成功");
					}
					refreshData();
				} else {
					if(activeMark==1){
						nps.info(nps.i18n("INFO_TITLE_DATA"),"启用失败");
					}else{
						nps.info(nps.i18n("INFO_TITLE_DATA"),"禁用失败");
					}
				}
		 }});
	  	
	});
}
/**
 * 批量删除优惠劵
 */
function confirmDelete(id){
	nps.confirm(nps.i18n("PROPERT_OPERATOR_TIP"),nps.i18n("PROPERT_CONFIRM_DELETE_SEL_PROPERT"),function(){
		var json ;
		if(id != null){
			 json={"ids":id}; 
		}else{
			var checkbox=$j("input[name='id']");
			var data=""; 
			  $j.each(checkbox, function(i,val){   
				  if(val.checked){
					  data=data+$j(this).val()+",";
				  }
			 }); 
			  if(data!=""){
				  data=data.substr(0,data.length-1);
			  }  
			  if(data == ""){
				  nps.info(nps.i18n("INFO_TITLE_DATA"),"请选择需要删除的排序规则");
				  return;
			  }
			 json={"ids":data}; 
			 
		}
	  	 nps.asyncXhrPost(base+"/item/removeSortScore.json", json,{successHandler:function(data, textStatus){
			var backWarnEntity = data;
				if (backWarnEntity.isSuccess) {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
					refreshData();
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),backWarnEntity.description);
				}
		 }});
	});
}
function focusKey(e) {
	if (key.hasClass("empty")) {
		key.removeClass("empty");
	}
	if (hkey.hasClass("empty")) {
		hkey.removeClass("empty");
	}
};
function blurKey(e) {
	if (key.get(0).value === "") {
		key.addClass("empty");
	}
	if (hkey.get(0).value === "") {
		hkey.addClass("empty");
	}
};
var lastValue = "", nodeList = [], fontCss = {};
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
};
function updateNodes(highlight) {
	var zTree = $j.fn.zTree.getZTreeObj("tree-left");
	for( var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
};
var hlastValue = "", hnodeList = [], hfontCss = {};
function hsearchNode(e) {
	var zTree = $j.fn.zTree.getZTreeObj("industr-tree-left");
	var value = $j.trim(hkey.get(0).value);
	if(value==""){
		$j("#search_result_left").html("");
		hupdateNodes(false);
	}
		
	if (hkey.hasClass("empty")) {
		value = "";
	}
	if (hlastValue === value) return;
	hlastValue = value;
	if (value === "") return;
	hupdateNodes(false);
		
	hnodeList = zTree.getNodesByParamFuzzy("name", value);
	
	$j("#hsearch_result_left").html(nps.i18n("CATEGORY_FIND")+ hnodeList.length+ nps.i18n("CATEGORY_RESULT"));
	
	if (hnodeList.length > 0) {
	$j.each(hnodeList, function(i, node){      
 		 zTree.expandNode(node.getParentNode(),true, true, true);
		}); 
	}
	hupdateNodes(true);
	$j("#hkey-left").focus();
};
function hupdateNodes(highlight) {
	var zTree = $j.fn.zTree.getZTreeObj("industr-tree-left");
	for( var i=0, l=hnodeList.length; i<l; i++) {
		hnodeList[i].highlight = highlight;
		zTree.updateNode(hnodeList[i]);
	}
};

function showTab(i) {
	
	if(i==1){
		$j(".ui-tag-change").each(function(){
			$j(this).find(".tag-change-ul").find("li").eq(1).removeClass("selected");
			$j(this).find(".tag-change-ul").find("li").eq(2).removeClass("selected");
			$j(this).find(".tag-change-ul").find("li").eq(0).addClass("selected");
			$j(this).find(".tag-change-content").find(".tag-change-in").eq(0).addClass("block");
			$j(".tag-change-in").eq(0).addClass("block").siblings(".tag-change-in").removeClass("block");
		});
	}  
	if(i==2){
		$j(".ui-tag-change").each(function(){
			$j(this).find(".tag-change-ul").find("li").eq(0).removeClass("selected");
			$j(this).find(".tag-change-ul").find("li").eq(2).removeClass("selected");
			$j(this).find(".tag-change-ul").find("li").eq(1).addClass("selected");
			$j(this).find(".tag-change-content").find(".tag-change-in").eq(1).addClass("block");
			$j(".tag-change-in").eq(1).addClass("block").siblings(".tag-change-in").removeClass("block");
		});
	}  
	if(i==3){
		$j(".ui-tag-change").each(function(){
			$j(this).find(".tag-change-ul").find("li").eq(0).removeClass("selected");
			$j(this).find(".tag-change-ul").find("li").eq(1).removeClass("selected");
			$j(this).find(".tag-change-ul").find("li").eq(2).addClass("selected");
			$j(this).find(".tag-change-content").find(".tag-change-in").eq(2).addClass("block");
			$j(".tag-change-in").eq(2).addClass("block").siblings(".tag-change-in").removeClass("block");
		});
	}
};

// 通过loxiasimpletable动态获取数据
$j(document).ready(function() {
	
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	$j.fn.zTree.init($j("#tree-left"), setting, zNodes);
	$j.fn.zTree.init($j("#industr-tree-left"), industrsetting, industrZNodes);
	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>查询数据为空</span>',
		form:"searchForm",
		cols : [{
				label : "<input type='checkbox'  />",
				width : "5%",
				template : "drawCheckbox"
			},{
				name : "type",
				label : "类型",
				width : "10%",
				template:"type" 
			}, {
				name : "operatorText",
				label : "计算表达式",
				width : "20%"
			}, {
				name : "score",
				label : "权重分数",
				width : "10%" ,
			}, {
				name : "lifecycle",
				label : "状态",
				width : "10%" , 
				type: "yesno"
			} ,{
				label :"操作",
				width : "15%", 			 
				template:"drawEditor" 
			}],
		dataurl : sortScoreUrl
	});
	//加载数据
	refreshData();
	// 筛选数据
	$j(".func-button.search").click(function(){
		 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	});  
	//执行批量删除
	$j(".button.delete.batch").click(function() {
		confirmDelete(null);
	});
     //启用
	$j("#table1").on("click",".func-button.enable",function(){
		  var curObject=$j(this);
		  updateActive(curObject.attr("val"),1);
	 });
     //禁用
	 $j("#table1").on("click",".func-button.disable",function(){
		 var curObject=$j(this);
		 updateActive(curObject.attr("val"),0);
	 });
     //删除
	 $j("#table1").on("click",".func-button.delete",function(){
		 var curObject=$j(this);
		 confirmDelete(curObject.attr("val"));
	 });
	 
	//修改
	 $j("#table1").on("click",".func-button.editor",function(){
		 var zTree = $j.fn.zTree.getZTreeObj("tree-left");
		 var id=$j(this).attr("val");
		 var data = nps.syncXhrPost(base+"/item/getSortScore.json", {id: id});
		 if(data==null){
			 nps.info(nps.i18n("INFO_TITLE_DATA"),"获取排序规则失败");
			 return;
		 }
		 var type = data.type;
		 var score = data.score;
		 var oper = data.operator;
		 var param = data.param;
		 $j("#ui-tag-change-id").attr("optId",id);
		 $j("#sort_title").text("默认排序引擎管理-修改");
		 if(type==1){
			 var node = zTree.getNodeByParam("id",data.categoryId);//treeObj是tree对象
			 zTree.checkNode(node, true, false);
			 zTree.expandNode(node, true, false, true);
			 showTab(1);
			 $j("#cate_score").val(score);
		 }else if(type==2){
			 showTab(2);
			//属性
			 var perId = data.propertyId;
			 var perporty = nps.syncXhrPost(base+ "/property/getPropertyByid.json", {'id': perId});
			//根据行业获取属性
			var data = nps.syncXhrPost(base+"/property/propertyListByIndustryid.json", {'industryId': perporty.industryId});
			var list = data.description;
			if(list!=null && list!=""){
				$j("#per_select").empty();
				var op ="";
				for ( var index = 0; index < list.length; index++) {
					 var per = list[index];
					 op+="<option value='"+per.id+"'>"+per.name+"</option>";
				}
				$j("#per_select").append(op);
			}else{
				$j("#per_select").empty();
			}
			 $j("#per_select option[value='"+perId+"']").attr("selected",true);
			 //操作
			$j("#per_select_oper option[value='"+oper+"']").attr("selected",true);
			//参数
			$j("#per_param").val(param);
			//权值
			$j("#per_score").val(score);
		 }else{
			//显示其他编辑界面 
			showTab(3);
			//初始化类型
			 $j("#other_select option[value='"+type+"']").attr("selected",true);
			//操作
			 $j("#other_select_oper option[value='"+oper+"']").attr("selected",true);
			//参数
			$j("#param").val(param);
			//权值
			$j("#other_score").val(score);
		 }
		 $j("#dialog-item-select").dialogff({type:'open',close:'in',width:'700px',height:'600px'});
	 });
	 //新建
	 $j(".addItemSortScore").on("click",function(){
		 var zTree = $j.fn.zTree.getZTreeObj("tree-left");
		 zTree.checkAllNodes(false);
		 $j("#sort_title").text("默认排序引擎管理-新增");
		 $j("#cate_score").val(null);
		 $j("#param").val(null);
		 $j("#per_param").val(null);
		 $j("#other_score").val(null);
		 $j("#per_score").val(null);
		 showTab(1);
		 $j("#ui-tag-change-id").attr("optId",null);
		 $j("#dialog-item-select").dialogff({type:'open',close:'in',width:'700px',height:'600px'});
	 });
	 //分组树查询
	 key = $j("#key-left");
	 key.bind("focus", focusKey)
	.bind("blur", blurKey)
	.bind("propertychange", searchNode)
	.bind("input", searchNode);
	 //行业树查询
	 hkey = $j("#hkey-left");
	 hkey.bind("focus", focusKey)
	.bind("blur", blurKey)
	.bind("propertychange", hsearchNode)
	.bind("input", hsearchNode);
	
	//确定按钮
	 $j("#confirm_sort").on("click",function(){
		 var score;
		 var tabType = $j(".tag-change-ul .selected");
		 var type = tabType.attr("type");
		 var data;
		 var id = $j("#ui-tag-change-id").attr("optId");
		 //分类
		 if(type == 1){
			 var zTree = $j.fn.zTree.getZTreeObj("tree-left");
			 var nodes = zTree.getCheckedNodes(true);
			 if(nodes==null ||nodes.length==0){
				 nps.info(nps.i18n("INFO_TITLE_DATA"),"请选择分类");
				 return;
			 }
			 score = $j("#cate_score").val(); 
			 if($j("#cate_score").hasClass("ui-loxia-error")){
				 return;
			 }
			 if(score==null || score==""){
				 $j("#cate_score").addClass("ui-loxia-error");
				 return;
			 }
			 var cateId = nodes[0].id;
			 if(id!=null && id !=""){
				 data = {'id':id,'type':type,"score":score,"categoryId":cateId};
			 }else{
				 data = {'type':type,"score":score,"categoryId":cateId};
			 }
			
		 }else if(type == 2){
			//属性 
			 score =$j("#per_score").val();
			 if($j("#per_score").hasClass("ui-loxia-error")){
				 return;
			 }
			 if(score==null || score==""){
				 $j("#per_score").addClass("ui-loxia-error");
				 return;
			 }
			 var oper = $j("#per_select_oper option:selected").val();
			 var param = $j("#per_param").val();
			 if($j("#per_param").hasClass("ui-loxia-error")){
				 return;
			 }
			 if(param==null || param==""){
				 $j("#per_param").addClass("ui-loxia-error");
				 return;
			 }
			 //属性id
			 var proid = $j("#per_select option:selected").val();
			 if($j("#per_select option:selected").hasClass("ui-loxia-error")){
				 return;
			 }
			 if(proid==null || proid==""){
				 $j("#per_select").addClass("ui-loxia-error");
				 return;
			 }
			 if(id!=null && id !=""){
				 data = {'id':id,'type':type,"score":score,"operator":oper,"param":param,"propertyId":proid};
			 }else{
				 data = {'type':type,"score":score,"operator":oper,"param":param,"propertyId":proid};
			 }
		 }else{
			//其他
			//获取用户选择的下拉类型
			 type = $j("#other_select option:selected").val();
			 score =$j("#other_score").val();
			 if($j("#other_score").hasClass("ui-loxia-error")){
				 return;
			 }
			 if(score==null || score==""){
				 $j("#other_score").addClass("ui-loxia-error");
				 return;
			 }
			 var oper = $j("#other_select_oper option:selected").val();
			 var param = $j("#param").val();
			 if($j("#param").hasClass("ui-loxia-error")){
				 return;
			 }
			 if(param==null || param==""){
				 $j("#param").addClass("ui-loxia-error");
				 return;
			 }
			 if(id!=null && id !=""){
				 data = {'id':id,'type':type,"score":score,"operator":oper,"param":param};
			 }else{
				 data = {'type':type,"score":score,"operator":oper,"param":param};
			 }
			
		 }
		 //保存操作
		 nps.asyncXhrPost(base+"/item/addSortScore.json", data,{successHandler:function(data, textStatus){
			var backWarnEntity = data;
			if (backWarnEntity.isSuccess) {
				 if(id!=null && id !=""){
					 nps.info(nps.i18n("INFO_TITLE_DATA"),"修改成功");
				 }else{
					 nps.info(nps.i18n("INFO_TITLE_DATA"),"添加成功");
				 }
				 $j("#dialog-item-select").dialogff({type : 'close'});
				 if(!typeof(zTree)=='undefined'){
					 zTree.checkAllNodes(false);
				 }
				 $j("#cate_score").val(null);
				 $j("#param").val(null);
				 $j("#per_param").val(null);
				 $j("#other_score").val(null);
				 $j("#per_score").val(null);
				 refreshData();
			} else {
				 if(id!=null && id !=""){
					 nps.info(nps.i18n("INFO_TITLE_DATA"),"修改失败");
				 }else{
					 nps.info(nps.i18n("INFO_TITLE_DATA"),"添加失败");
				 }
			}
		}});
		
	});
	 $j("#cancel").on("click",function(){
		 var zTree = $j.fn.zTree.getZTreeObj("tree-left");
		 var nodes = zTree.checkAllNodes(true);
		 $j("#dialog-item-select").dialogff({type : 'close'});
		 zTree.checkAllNodes(false);
	});
	//删除其他类型中的分类和属性选项
	 jQuery("#other_select option[value='1']").remove();
	 jQuery("#other_select option[value='2']").remove(); 
}); 



