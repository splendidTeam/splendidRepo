$j.extend(loxia.regional['zh-CN'],{
	"TO_VIEW":"查看",
	"TO_UPDATE":"修改",
	"TO_ENABLE":"启用",
	"TO_DEL":"删除",
	"PROPERT_OPERATOR_TIP":"提示信息",
	"PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的功能么？",
	"PROPERT_CONFIRM_DELETE_SEL_PROPERT_MENU":"确定要删除选定的菜单么？",
	"INFO_TITLE_DATA":"提示信息",
	"INFO_DELETE_SUCCESS":"删除成功!",
    "INFO_DELETE_FAIL":"删除失败!",
    "INFO_START_SUCCESS":"启用成功!",
    "INFO_START_FAIL":"启用失败!",
    "INFO_CANCLE_START_SUCCESS":"取消启用成功!",
    "INFO_CANCEL_START_FAIL":"取消启用失败!",
	"TO_CANCEL":"禁用",
	"fname":"功能名称",
	"furl":"功能utl",
	"oper":"操作",
	"oper-add":"请选择对应的菜单进行新增",
	"oper-update":"请选择对应的菜单进行修改",
	"oper-del":"请选择对应的菜单进行删除",
	"oper-sql":"请选择对应的菜单进行sql导出",
	"add-s":"新增成功",
	"update-s":"修改成功",
	"MENU_UPDATE_ORGTYPE_CHANGE":"您选择变更该功能的组织类型，原先拥有该功能的角色将会失去该功能权限，确认修改?"
});

//分类树
var setting = {
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
	 setMenuInfo(treeNode);
};

function onCheck(event, treeId, treeNode) {
	 setMenuInfo(treeNode);
};
var operUrl;
var cnode;
function setMenuInfo(treeNode){
	 var id  = treeNode.id;
	 if(id == 0){
		 //父节点
		 $j("#table1").loxiasimpletable("refresh",[]);
		 setMenuEditInfoNull($j(".detail"));
		 $j("#table1").attr("priId","");
		 $j("#dialog-menu-dialog .edit").attr("parentId",id);
		 return;
	 }
	 cnode = treeNode;
	 var url = treeNode.murl;
	 var name  = treeNode.name;
	 var detail = $j(".detail");
	 var data = nps.syncXhrPost(base+"/menu/getMenuByUrl.json", {'url': url,'name':name,'id':id});
	 if(data != null){
		 detail.find(".label").val(data.label);
		 operUrl = data.url;
		 detail.find(".url").val(operUrl);
		 detail.find(".orgtype option[value="+data.orgType+"]").attr("selected",true);;
		 detail.find(".name").val(data.name);
		 detail.find(".acl").val(data.acl);
		 detail.find(".gname option[value="+data.groupName+"]").attr("selected",true);;
		 detail.find(".icon").val(data.icon);
		 detail.find(".sort").val(data.sortNo);
		 detail.find(".desc").val(data.description);
		 detail.attr("mid",data.id);
		 
		 //
		 detail.find(".orgtypeHid").val(data.orgType);
		 var urls = data.privilegeUrls;
		 $j("#table1").loxiasimpletable("refresh",urls);
		 $j("#table1").attr("priId",data.priId);
		 if(urls != null && urls.length>0){
			 $j("#dialog-function-dialog .edit").attr("prId",urls[0].privilege.id);
		 }
		 $j("#dialog-menu-dialog .edit").attr("parentId",data.id);
		 $j(".detail").attr("mid",data.id);
		 $j("#dialog-menu-dialog .edit").attr("mid",data.id);
	 }
}

function setMenuEditInfo(detail,data){
	 var treeNode = $j(".detail");
	 var url = treeNode.find(".url").val();
	 var name = treeNode.find(".name").val();
	 var id = treeNode.attr("mid");
	 if(data == null){
		 data = nps.syncXhrPost(base+"/menu/getMenuByUrl.json", {'url': url,'name':name,'id':id}); 
	 }
	 if(data != null){
		 detail.find(".label").val(data.label);
		 detail.find(".url").val(data.url);
		 detail.find(".orgtype option[value="+data.orgType+"]").attr("selected",true);
		 detail.find(".name").val(data.name);
		 detail.find(".acl").val(data.acl);
		 detail.find(".gname").val(data.groupName);
		 detail.find(".icon").val(data.icon);
		 detail.find(".sort").val(data.sortNo);
		 detail.find(".desc").val(data.description);
	 }
}
function setMenuEditInfoNull(detail){
	 detail.find("input").val(null);
	 detail.find(".desc").val(null);
}
function drawEditor(data){
	var id=loxia.getObject("id", data);
	var url=loxia.getObject("url", data);
	if(operUrl == url){
		return "";
	}
	var update="<a href='javascript:void(0);' val='"+id+"' class='func-button editor'>"+nps.i18n("TO_UPDATE")+"</a>";
    var del="<a href='javascript:void(0);' val='"+id+"' class='func-button delete'>"+nps.i18n("TO_DEL")+"</a>";
	return update+del;
}


// 刷新数据
function refreshData() {
	 var pid =  $j("#table1").attr("priId");
	 var data = nps.syncXhrPost(base+"/menu/getPrivilegeUrlPid.json", {'pid': pid});
	 $j("#table1").loxiasimpletable("refresh",data);
	 if(data != null && data.length>0){
		 $j("#dialog-function-dialog .edit").attr("prId",data[0].privilege.id);
	 }
}

/**
 * 批量删除优惠劵
 */
function confirmDelete(id){
	nps.confirm(nps.i18n("PROPERT_OPERATOR_TIP"),nps.i18n("PROPERT_CONFIRM_DELETE_SEL_PROPERT"),function(){
		var json={"id":id}; 
	  	 nps.asyncXhrPost(base+"/menu/delFunction.json", json,{successHandler:function(data, textStatus){
				if (data.isSuccess) {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
					refreshData();
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_FAIL"));
				}
		 }});
	});
}
function focusKey(e) {
	if (key.hasClass("empty")) {
		key.removeClass("empty");
	}
};
function blurKey(e) {
	if (key.get(0).value === "") {
		key.addClass("empty");
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

function validateInput(cls){
	var result = true;
	if(cls==null || cls==""){
		return result; 
	}
	$j(cls).each(function(){
		var val =$j(this).val();
		if($j(this).hasClass("ui-loxia-error")){
			result = false;
		}
		if(val==null || $j.trim(val)==""){
			$j(this).addClass("ui-loxia-error");
			result = false;
		}
	 });
	return result; 
}
function delErrorInfo(cls){
	if(cls==null || cls==""){
		return; 
	}
	$j(cls).each(function(){
		$j(this).removeClass("ui-loxia-error");
	 });
}

function removeNode(id){
	var treeObj = $j.fn.zTree.getZTreeObj("tree-left");
	var node = treeObj.getNodeByParam("id", id, null);
	treeObj.removeNode(node);
}
function editNode(pid,menu,add){
	var newNode ={
		  id:menu.id, 
		  pId:menu.parentId,
		  name:menu.label,
		  murl:menu.url,
		  open:true
	};
	var treeObj = $j.fn.zTree.getZTreeObj("tree-left");
	var node = treeObj.getNodeByParam("id", pid, null);
	if(add){
		treeObj.addNodes(node,newNode);
	}else{
		node.name = menu.label;
		treeObj.updateNode(node);
	}
	
}
// 通过loxiasimpletable动态获取数据
$j(document).ready(function() {
	
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	$j.fn.zTree.init($j("#tree-left"), setting, menuList);
	 //菜单树查询
	 key = $j("#key-left");
	 key.bind("focus", focusKey)
	.bind("blur", blurKey)
	.bind("propertychange", searchNode)
	.bind("input", searchNode);
	 $j("#table1").loxiasimpletable({
			cols : [{
					name : "description",
					label : nps.i18n("fname"),
					width : "20%"
				}, {
					name : "url",
					label : nps.i18n("furl"),
					width : "20%"
				},{
					label :nps.i18n("oper"),
					width : "15%", 			 
					template:"drawEditor" 
				}]
	});
	 $j(".gname-select").change(function(){
		 var me = $j(this);
		 var p = me.parent();
		 p.find("input").val(me.val());
		 p.find("input").removeClass("ui-loxia-error");
	 });
     //删除
	 $j("#table1").on("click",".delete",function(){
		 var me=$j(this);
		 confirmDelete(me.attr("val"));
	 });
	 //导菜单sql
	 $j(".exportsql").on("click",function(){
		 if(cnode==null){
			 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("oper-sql"));
			 return;
		 }
		 var id  = cnode.id;
		 var url = cnode.murl;
		 var name  = cnode.name;
		 var data = nps.syncXhrPost(base+"/menu/generateMenuSql.json", {'url': url,'name':name,'id':id});
		 $j("#dialog-sql-dialog").find(".sql").val(data);
		 $j("#dialog-sql-dialog").dialogff({type:'open',close:'in',width:'1000px',height:'410px'});
	 });
	//修改
	 $j(".update").on("click",function(){
		 
		 var  dialog = $j("#dialog-menu-dialog");
		 var parentId= dialog.find(".edit").attr("parentId");
		 if(typeof(parentId)=='undefined'|| parentId==null || parentId==""){
			 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("oper-update"));
			 return;
		 }
		 delErrorInfo("#dialog-menu-dialog .input_add");
		 var id = $j(".detail").attr("mid");
		 var edit = dialog.find(".edit").attr("mid",id);
		 edit.find(".url").attr("readonly","readonly");
		 edit.find(".acl").attr("readonly","readonly");
		 setMenuEditInfo(dialog.find(".edit"),null);
		 dialog.dialogff({type:'open',close:'in',width:'1000px',height:'300px'});
	 });
	 //新建
	 $j(".add").on("click",function(){
		 var  dialog = $j("#dialog-menu-dialog");
		 var parentId= dialog.find(".edit").attr("parentId");
		 if(typeof(parentId)=='undefined'|| parentId==null || parentId==""){
			 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("oper-add"));
			 return;
		 }
		 dialog.find(".url").removeAttr("readonly");
		 dialog.find(".acl").removeAttr("readonly");
		 delErrorInfo("#dialog-menu-dialog .input_add");
		 dialog.find(".edit").attr("mid","");
		 setMenuEditInfoNull(dialog.find(".edit"),null);
		 dialog.dialogff({type:'open',close:'in',width:'1000px',height:'300px'});
	 });
	 
	 //菜单删除
	 $j("#currentNodeDiv .delete").on("click",function(){
		 var prId =  $j("#table1").attr("priId");
		 var menuId = $j("#dialog-menu-dialog .edit").attr("parentId");
		 if(typeof(menuId)=='undefined'|| menuId==null || menuId==""){
			 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("oper-del"));
			 return;
		 }
		 var json = {"menuId":menuId,"prId":prId};
		 nps.confirm(nps.i18n("PROPERT_OPERATOR_TIP"),nps.i18n("PROPERT_CONFIRM_DELETE_SEL_PROPERT_MENU"),function(){
			 nps.asyncXhrPost(base+"/menu/delMenu.json", json,{successHandler:function(data, textStatus){
					if (data.isSuccess) {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
						setMenuEditInfoNull($j(".detail"));
						removeNode(menuId);
						$j("#table1").loxiasimpletable("refresh",[]);
						$j("#table1").attr("priId","");
						$j("#dialog-menu-dialog .edit").attr("parentId","");
					} else {
						nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
					}
			 }});
		 });
		 
	 });
	//确定按钮
	 $j("#dialog-menu-dialog .btn-ok").on("click",function(){
		 var dialog =  $j("#dialog-menu-dialog");
		 var detail = $j("#dialog-menu-dialog .edit");
		 if(validateInput("#dialog-menu-dialog .input_add")==false){
			 return;
		 }
		 var parentId = detail.attr("parentId");
		 if(parentId == 0){
			 parentId = null; 
		 }
		 var label = detail.find(".label").val();
		 var url =  detail.find(".url").val();
		 if(url.substring(0,1)!="/"){
				nps.info(nps.i18n('INFO_TITLE_DATA'), "url必须以/开头");
				return;
		 }
		 var orgType= detail.find(".orgtype").val();
		 var name = detail.find(".name").val();
		 var acl = detail.find(".acl").val();
		 var groupName = detail.find(".gname").val();
		 //var icon =  detail.find(".icon").val();
		 var sortNo = detail.find(".sort").val();
		 if(sortNo <0 ){
			nps.info(nps.i18n('INFO_TITLE_DATA'), "显示排序不能小于0");
			return; 
		 }
		 
		 var description = detail.find(".desc").val();
		 var json = {
				 "label":label,
				 "url":url,
				 "orgType":orgType,
				 "name":name,
				 "acl":acl,
				 "groupName":groupName,
				// "icon":icon,
				 "sortNo":sortNo,
				 "description":description,
				 "parentId":parentId
		 	};
		 var id = detail.attr("mid");
		 if(id != null && id!=""){
			 var prid =  $j("#table1").attr("priId");
			 json.id = id;
			 json.priId = prid;
		 }
		 
		 var origOrgtype =$j(".detail").find(".orgtypeHid").val();
		 
		 if(origOrgtype!=null&&origOrgtype!=''&&origOrgtype!=orgType){
			nps.confirm(nps.i18n('INFO_TITLE_DATA'), nps.i18n('MENU_UPDATE_ORGTYPE_CHANGE'), function callBackUpdate(){
				createOrUpdate(json,dialog,id,parentId);
			});
			return;
		 }
		 createOrUpdate(json,dialog,id,parentId);
		 
	 });
	 
	 function createOrUpdate(json,dialog,id,parentId){
		 nps.asyncXhrPost(base+"/menu/edit.json", json,{successHandler:function(data, textStatus){
				if (data.isSuccess) {
					var menu = data.description;
					json.id = menu.id;
					if(id != null && id!=""){
						editNode(id,menu,false);
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("update-s"));
					}else{
						editNode(parentId,menu,true);
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("add-s"));
					}
					setMenuEditInfo($j(".detail"),json);
					refreshData();
					dialog.dialogff({type:'close'});
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
				}
		 }});
	 }
	 
	 

	 //功能新增
	 $j(".addUrl").on("click",function(){
		 var menuId = $j("#dialog-menu-dialog .edit").attr("parentId");
		 if(typeof(menuId)=='undefined'|| menuId==null || menuId==""){
			 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("oper-add"));
			 return;
		 }
		 var  dialog = $j("#dialog-function-dialog");
		 dialog.find(".edit").attr("fid","");
		 setMenuEditInfoNull(dialog.find(".edit"));
		 
		 dialog.dialogff({type:'open',close:'in',width:'500px',height:'230px'});
	 });
	 
	 //功能修改
	 $j("#table1").on("click",".editor",function(){
		 var me = $j(this);
		 var tr = me.parent().parent().find("span");
		 var dialog = $j("#dialog-function-dialog");
		 var edit = dialog.find(".edit");
		 edit.attr("fid",me.attr("val"));
		 edit.find(".desc").val(tr.eq(0).html());
		 edit.find(".url").val(tr.eq(1).html());
		 dialog.dialogff({type:'open',close:'in',width:'500px',height:'230px'});
	 });
	//确定按钮
	 $j("#dialog-function-dialog .btn-ok").on("click",function(){
		 var dialog = $j("#dialog-function-dialog");
		 var detail = dialog.find(".edit");
		 var parentId = detail.attr("prId");
		 if(typeof(parentId)=='undefined'|| parentId==null || parentId==""){
			 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("oper-add"));
			 return;
		 }
		 if(validateInput("#dialog-function-dialog .input_add")==false){
			 return;
		 }
		 var url =  detail.find(".url").val();
		 if(url.substring(0,1)!="/"){
				nps.info(nps.i18n('INFO_TITLE_DATA'), "url必须以/开头");
				return;
		 }
		 var description = detail.find(".desc").val();
		 var json = {
				 "url":url,
				 "description":description,
				 "parentId":parentId
		 	};
		 var id = detail.attr("fid");
		 if(id != null && id!=""){
			 json.id = id;
		 }
		 nps.asyncXhrPost(base+"/menu/editFunction.json", json,{successHandler:function(data, textStatus){
				if (data.isSuccess) {
					if(id != null && id!=""){
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("update-s"));
					}else{
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("add-s"));
					}
					refreshData();
					dialog.dialogff({type:'close'});
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
				}
		 }});
		 
	 });
	 //导出sql关闭btn-close
	 $j("#dialog-sql-dialog").on("click",".btn-close",function(){
		 $j("#dialog-sql-dialog").dialogff({type:'close'});
		 
	 });
	 
}); 




