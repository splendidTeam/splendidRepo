$j.extend(loxia.regional['zh-CN'], {
	"TEMPLATE_INFO" : "提示信息",
	"TEMPLATE_ASYNC_LOAD_FAIL" : "异步加载失败",
	"TEMPLATE_ERROR_INFO":"错误提示：",
	"TEMPLATE_CONFIRM" : "操作确认：",
	"TEMPLATE_DEL_SUCCESS":"模板定义 {0} 删除成功",
	"TEMPLATE_UPDATE_SUCCESS":"模板定义 {0} 更新成功",
	"TEMPLATE_UPDATE_FAIL":"模板定义 {0} 更新失败",
	"TEMPLATE_ADD_SUCCESS":"模板定义添加成功",
	"TEMPLATE_FLODER_ADD_SUCCESS":"模板目录{0}添加成功",
	"TEMPLATE_FLODER_DEL_SUCCESS":"模板目录{0}删除成功",
	"TEMPLATE_NAME_ALREADY_EXISTS":"名称已经存在",
	"TEMPLATE_PATH_WORNG":"模板文件路径不合法",
	"TEMPLATE_DLGPATH_WORNG":"属性定义路径不合法",
	"TEMPLATE_NAME_IS_EMPTY":"输入的名称为空",
	"TEMPLATE_LOAD_ERROR":"加载模板定义出错",
	"TEMPLATE_NAME_IS_EMPTY":"名称不能为空",
	"TEMPLATE_HANDLERBEAN_IS_EMPTY":"处理类不能为空",
	"TEMPLATE_SURE_TO_DELETE_DEF":"确认删除该模板定义?",
	"TEMPLATE_SURE_TO_DELETE_FOLDER":"确认删除该目录?删除该目录会级联删除目录下所有模板和子目录。",
	"TEMPLATE_NAME_INVALID":"名称不合法",
	"TEMPLATE_DEF_NEED_UPDATE":"此模板属性已修改，是否保存?"
	

});

var definitionType = "cms:definitions";

var getDefinitionUrl = base + "/cms/findDefinition.json";

var addDefinitionUrl = base + "/cms/createDefinition.json";

var updateDefinitionUrl = base + "/cms/updateDefinition.json";

var exportDefinitionUrl = base + "/cms/exportDefinition.json";

var deleteDefinitionUrl = base + "/cms/deleteDefinition.json";

var createDefinitionFloderUrl = base + "/cms/createDefinitionFloder.json";

var deleteDefinitionFloderUrl = base + "/cms/deleteDefinitionFloder.json";

//检查资源是否存在的url
var validateResourceExistUrl=base+"/cms/validateResourceExists.json";

var toUpdateDialogUrl = base +"/cms/toUpdateDialog.htm?path=";

var log, className = "dark";

var folderType="nt:folder";

var fileType="nt:file";

var dialogCaller=null;

//用户是否在本地修改模板定义
var userUpdatedFlag=false;

//用户是否已经提交修改请求
var exeUpdateFlag=false;

//是否已经询问过用户 要保存修改
var askedFlag=false;

//当父节点是模板定义时候，是否隐藏创建子模板定义DIV
var hideCreateDiv=true;

//左侧树定义
var definitionTree;

//选择定义路径的树的定义
var selectDefTree;

//选择对话框的树的定义
var selectDialogTree;

//选择定义路径的树setting配置
var defPathDlgTreeId = "selectDefTree"; // 树的divId 在jsp中定义的 必需
var defPathDlgRootName = "ROOT";// 根节点的显示名称 必需
var defPathDlgRootPath = "/resources/element";// 根节点的实际路径 必需
var defPathDlgNodeType = "nt:folder,nt:file"; // 构建树的节点的仓库类型
var defPathDlgOnClickHandler = onClickTemp; // 点击树节点时候的响应事件， 必需
var defPathDlgCheckEnable = true; // 节点是否能够被选择
var defPathDlgChkStyle = "radio";// 如果可以被选择，选择的类型： checkbox radio
var defPathDlgChkType = "all";// 选择类型的参数 参见 ztree API setting.check.chkStyle
var defPathDlgSelectType = "nt:file";// 能够被选中的仓库类型
var defPathDlgCNodeLevel = 3;// 能够被选中的节点类型 0 父子节点均被选择 1父子节点均不被选择 2只选择父节点
// 3只选择子节点

var defPathDlgTreeSetting = {
	"treeId" : defPathDlgTreeId,
	"rootName" : defPathDlgRootName,
	"rootPath" : defPathDlgRootPath,
	"nodeType" : defPathDlgNodeType,
	"onClickHandler" : defPathDlgOnClickHandler,
	"checkEnable" : defPathDlgCheckEnable,
	"chkStyle" : defPathDlgChkStyle,
	"chkType" : defPathDlgChkType,
	"selectType" : defPathDlgSelectType,
	"nodeLevel" : defPathDlgCNodeLevel
};

//选择dialog路径的树setting配置
var dlgPathDlgTreeId = "dlgTree"; // 树的divId 在jsp中定义的 必需
var dlgPathDlgRootName = "ROOT";// 根节点的显示名称 必需
var dlgPathDlgRootPath = "/definition/dialog";// 根节点的实际路径 必需
var dlgPathDlgNodeType = "nt:unstructured,cms:dialog"; // 构建树的节点的仓库类型
var dlgPathDlgOnClickHandler = onClickTemp; // 点击树节点时候的响应事件， 必需
var dlgPathDlgCheckEnable = true; // 节点是否能够被选择
var dlgPathDlgChkStyle = "radio";// 如果可以被选择，选择的类型： checkbox radio
var dlgPathDlgChkType = "all";// 选择类型的参数 参见 ztree API setting.check.chkStyle
var dlgPathDlgSelectType = "cms:dialog";// 能够被选中的仓库类型
var dlgPathDlgCNodeLevel = 3;// 能够被选中的节点类型 0 父子节点均被选择 1父子节点均不被选择 2只选择父节点
// 3只选择子节点

var dlgPathDlgTreeSetting = {
	"treeId" : dlgPathDlgTreeId,
	"rootName" : dlgPathDlgRootName,
	"rootPath" : dlgPathDlgRootPath,
	"nodeType" : dlgPathDlgNodeType,
	"onClickHandler" : dlgPathDlgOnClickHandler,
	"checkEnable" : dlgPathDlgCheckEnable,
	"chkStyle" : dlgPathDlgChkStyle,
	"chkType" : dlgPathDlgChkType,
	"selectType" : dlgPathDlgSelectType,
	"nodeLevel" : dlgPathDlgCNodeLevel
};

//左侧 模板定义管理树 参数配置
var treeId = "definitionTree";
var rootName = "definition";
var rootPath = "/definition";
var rootNodeType = "nt:unstructured";
var nodeType = "nt:unstructured,cms:definitions";
var onClickHandler = onClick;
var searchKeyId = "key";
var searchResultId = "searchResult";
var checkEnable = false;
var excludePath = "/definition/dialog";

var definitionTreeSetting = {
	"treeId" : treeId,
	"rootName" : rootName,
	"rootPath" : rootPath,
	"rootNodeType" : rootNodeType,
	"nodeType" : nodeType,
	"onClickHandler" : onClickHandler,
	"searchKeyId" : searchKeyId,
	"searchResultId" : searchResultId,
	"checkEnable" : checkEnable,
	"excludePath" : excludePath
}

function zTreeBeforeClick(treeId, treeNode, clickFlag) {
	if(userUpdatedFlag){
		if(exeUpdateFlag|| (askedFlag&&!exeUpdateFlag)){
			exeUpdateFlag=false;
			return true;
		}
		askedFlag =true;
		nps.confirm(nps.i18n("TEMPLATE_CONFIRM"),nps.i18n("TEMPLATE_DEF_NEED_UPDATE"),function(){
			updateDefinition();
			userUpdatedFlag=false;
			exeUpdateFlag=true;
		});
		
		return false;
	}else{
		return true;
	}
};

function onClickTemp(event, treeId, treeNode) {
	
}

function showDivByTreeNode(treeNode) {
	if (treeNode == null) {
		return;
	}
	var state = treeNode.state;

	if (state == 1) {
		$j("#handlerDiv").hide();
	} else {
		$j("#handlerDiv").show();
	}
}

function onInputPropertyChanged(){
	userUpdatedFlag=true;
	exeUpdateFlag=false;
	askedFlag=false;
}

/**
 * 绑定修改输入框的修改事件
 */
function bindUpdateInputValueChanged(){
	$j("#description").bind("valuechanged",onInputPropertyChanged);
	$j("#templatePath").bind("valuechanged",onInputPropertyChanged);
	$j("#dialogPath").bind("valuechanged",onInputPropertyChanged);
	$j("#handlerBean").bind("valuechanged",onInputPropertyChanged);
	$j("#type").bind("valuechanged",onInputPropertyChanged);
	
}

/**
 * 清除页面上所有输入的值
 */
function clearAllInputValue() {
	$j(":input:not(:button)").val("");
}

/**
 * 发送同步请求获得definition
 * 
 * @param path
 * @returns
 */
function getDefinition(path) {
	var json = {
		"path" : path
	};
	
	var definition = loxia.syncXhr(getDefinitionUrl, json, {
		type : "GET"
	});
	return definition;
}

// 左侧树点击事件
function onClick(event, treeId, treeNode) {
	clearAllInputValue();
	
	//移除以前检查不合格的输入框的错误提示css
	$j(".ui-loxia-error").removeClass("ui-loxia-error");
	
	if (null != treeNode) {
		$j("#name").val(treeNode.name).attr("readonly", "readonly");
		$j("#path").val(treeNode.path);
		$j("#parentPath").val(treeNode.path);
		var nodeType = treeNode.type;

		if (definitionType == treeNode.type) {// 定义类型
			$j("#definitionDiv").show();
			$j("#folderDiv").hide();
			var definition=getDefinition(treeNode.path);
			
			if(null!=definition.exception){// 加载模板定义出错
				nps.info(nps.i18n("TEMPLATE_ERROR_INFO"),nps.i18n("TEMPLATE_LOAD_ERROR"));
				return;
			}
			
			$j("#id").val(definition.id);
			$j("#type").val(definition.type);
			$j("#description").val(definition.description);
			$j("#templatePath").val(getShortPath(definition.templatePath,defPathDlgRootPath));
			$j("#dialogPath").val(getShortPath(definition.dialogPath,dlgPathDlgRootPath));
			$j("#handlerBean").val(definition.handlerBean);
			
			if(definition.type==staticType){
				$j("#handlerDiv").hide();
			}else{
				$j("#handlerDiv").show();
			}
			
			//隐藏创建子模板DIV
			if(hideCreateDiv){
				$j("#addnewDefinitionDiv").hide();
			}
			
			userUpdatedFlag=false;
			
		} else {// 目录类型
			$j("#definitionDiv").hide();
			$j("#folderDiv").show();
			
			$j("#addnewDefinitionDiv").show();
			
			$j("#newDefinitionHandlerDiv").hide();
		}

	}
}

function isNodeExists(path){
	var json={"path":path};
	var backWarnEntity = loxia.syncXhr(validateResourceExistUrl, json,{type: "GET"});			
	if(backWarnEntity.isSuccess == true){
//		nps.info(nps.i18n("TEMPLATE_ERROR_INFO"),nps.i18n("TEMPLATE_NAME_ALREADY_EXISTS"));
		return false;
	}
	
	return true;
}

function checkName(value, obj){
	var parentNodePath = $j("#parentPath").val().trim();
	 var path = $j("#newDefinitionName").val().trim();
	 if(path.indexOf("/")==0){
		 return nps.i18n("TEMPLATE_NAME_INVALID");
	 }
	 var fullPath = parentNodePath +"/"+path;
	 var result=isNodeExists(fullPath);

	 if(result){
		 return loxia.SUCCESS;
	 }else{
		 obj.state(false,nps.i18n("TEMPLATE_NAME_ALREADY_EXISTS"));
	 	return nps.i18n("TEMPLATE_NAME_ALREADY_EXISTS");
	 }
}

function updateDefinition(){
	if(validateUpdateDefForm()){
		changePathsWhenSubmit("templatePath","dialogPath");
		nps.asyncXhr(updateDefinitionUrl,
			    "updateDefForm",
			    { type: "POST", 
			successHandler : function(data){   			
				if(data.isSuccess == true){
					var nodes=definitionTree.getTree().getSelectedNodes();
					var nodeName=nodes[0].name;
					nps.info(nps.i18n("TEMPLATE_INFO"),nps.i18n("TEMPLATE_UPDATE_SUCCESS",nodeName));
					refreshCurrentNode(false);
				}else{
					nps.info(nps.i18n("TEMPLATE_INFO"),nps.i18n("TEMPLATE_UPDATE_FAIL",nodeName));
				}
				userUpdatedFlag=false;
		    }});
		userUpdatedFlag=false;
	}
}

function updateTemplateHandler(){
	updateDefinition();
}
	
/**
 * 打开选择定义路径对话框
 */
function openSelectDefinitionPathDialog(inputId){
	dialogCaller=$j("#"+inputId);
	selectDefTree = new CmsTree(defPathDlgTreeSetting);
	selectDefTree.getInit();
	selectDefTree.getTree().setting.callback.onAsyncSuccess=onDefTreeAsyncSuccess;
	$j("#selectTemplateDialogDiv").dialogff({
		type : 'open',
		close : 'in',
		width : '600px',
		height : '300px'
	});
}

/**
 * 打开选择dialog路径对话框
 * @param inputId
 */
function selectDialogPathDialog(inputId){
	dialogCaller=$j("#"+inputId);
	selectDialogTree = new CmsTree(dlgPathDlgTreeSetting);
	selectDialogTree.getInit();
	$j("#selectDlgDialogDiv").dialogff({
		type : 'open',
		close : 'in',
		width : '600px',
		height : '300px'
	});
}

/**
 * 弹出的对话框 确定按钮事件
 * @param tree  选择树
 * @param dialogId 对话框divId
 */
function closeDialogOK(tree,dialogId,rootPath){
	var nodes=tree.getTree().getCheckedNodes(true);
	if(nodes.length>0){
		var path=nodes[0].path;
		var beginIndex=path.indexOf(rootPath);
		if(beginIndex>=0){
			path=path.substring(rootPath.length);
		}
		
		dialogCaller.val(path);
		dialogCaller.removeClass("ui-loxia-error");
	}
	
	closeDialog(dialogId);
}

function getShortPath(fullPath,rootPath){
	var beginIndex=fullPath.indexOf(rootPath);
	if(beginIndex>=0){
		fullPath=fullPath.substring(rootPath.length);
	}
	
	return fullPath;
}

function getFullPath(shorPath,rootPath){
	return rootPath+shorPath;
}

var key;

/**
 * 提交时候修改path的值 加上前缀
 * @param defPathId
 * @param dlgPathId
 */
function changePathsWhenSubmit(defPathId,dlgPathId){
	var templatePath=$j("#"+defPathId).val();
	templatePath=getFullPath(templatePath,defPathDlgRootPath);
	$j("#"+defPathId).val(templatePath);
	
	var dialogPath=$j("#"+dlgPathId).val();
	if(isNotBlank(dialogPath)){
		dialogPath=getFullPath(dialogPath,dlgPathDlgRootPath);
		$j("#"+dlgPathId).val(dialogPath);
	}
}

//添加子模板
function addDefinition() {
	
	if(validateAddDefForm()){
		changePathsWhenSubmit("newDefinitionPath","newDefinitionDlgPath");
		nps.asyncXhr(addDefinitionUrl,
			    "addDefForm",
			    { type: "POST", 
					successHandler: function(data, textStatus){    			
					if(data.isSuccess == true){
						var nodes=definitionTree.getTree().getSelectedNodes();
						var nodeName=nodes[0].name;
						nps.info(nps.i18n("TEMPLATE_INFO"),nps.i18n("TEMPLATE_ADD_SUCCESS"));
						refreshCurrentNode(true);
//						clearAllInputValue();
					}else{
						
					}
			    }
			   }
		);
	}

};

/**
 * 刷新当前节点数据
 */
function refreshCurrentNode(hasChild){
	var treeObj = definitionTree.getTree();
	var nodes = treeObj.getSelectedNodes();
	if(nodes.length>0){
		var selectedNode=nodes[0];
		if(hasChild){
			selectedNode.isParent=hasChild;
		}
		treeObj.reAsyncChildNodes(selectedNode,"refresh");
		var tid=selectedNode.tId;
		$j(getTreeNodeSpanId(tid)).click();
	}
}

/**
 * 刷新当前节点的父节点，重新加载父节点下的数据
 */
function refreshParentNode(){
	var treeObj = definitionTree.getTree();
	var nodes = treeObj.getSelectedNodes();
	if(nodes.length>0){
		var selectedNode=nodes[0];
		var parentNode=selectedNode.getParentNode();
		if(null!=parentNode){
			treeObj.reAsyncChildNodes(parentNode,"refresh");
			var parentTid=parentNode.tId;
			$j(getTreeNodeSpanId(parentTid)).click();
		}
	}
}

function validateDefPath(value, obj){
	if((value == '/' && value.length == 1) || value.length == 0 || value.indexOf("/")!=0 ||
			(value.length > 1 && value.substring(value.length-1,value.length)=="/")){
			return nps.i18n("TEMPLATE_PATH_WORNG");
	}
	return loxia.SUCCESS;
}

function validateDlgPath(value, obj){
	if((value == '/' && value.length == 1) || (value.length > 1 && value.indexOf("/")!=0) ||
			(value.length > 1 && value.substring(value.length-1,value.length)=="/")){
			return nps.i18n("TEMPLATE_DLGPATH_WORNG");
	}
	return loxia.SUCCESS;
}

function validateUpdateDefForm(){
	var result="";
	
	if($j("#type").val()==dynamicType){
		if(!isNotBlank($j("#handlerBean").val().trim())){
			nps.info(nps.i18n("TEMPLATE_ERROR_INFO"),nps.i18n("TEMPLATE_HANDLERBEAN_IS_EMPTY"));
			$j("#handlerBean").addClass("ui-loxia-error");
			return false;
		}
	}
	
	var templatePath=$j("#templatePath").val();
//	templatePath=getFullPath(templatePath,defPathDlgRootPath);
	var validateDefPathResult=validateDefPath(templatePath);
	if(loxia.SUCCESS!=validateDefPathResult){
		nps.info(nps.i18n("TEMPLATE_ERROR_INFO"),validateDefPathResult);
		$j("#templatePath").addClass("ui-loxia-error");
		return false;
	}
	
	var dialogPath=$j("#dialogPath").val();
	if(isNotBlank(dialogPath)){
//		dialogPath=getFullPath(dialogPath,dlgPathDlgRootPath);
		var validateDlgPathResult=validateDlgPath(dialogPath);
		if(loxia.SUCCESS!=validateDlgPathResult){
			nps.info(nps.i18n("TEMPLATE_ERROR_INFO"),validateDlgPathResult);
			$j("#dialogPath").addClass("ui-loxia-error");
			return false;
		}
	}
	
	return true;
}

function validateAddDefForm(){
	
	if(!isNotBlank($j("#newDefinitionPath").val().trim())){
		$j("#newDefinitionPath").addClass("ui-loxia-error");
	}

	if(!isNotBlank($j("#newDefinitionName").val().trim())){
		nps.info(nps.i18n("TEMPLATE_ERROR_INFO"),nps.i18n("TEMPLATE_NAME_IS_EMPTY"));
		$j("#newDefinitionName").addClass("ui-loxia-error");
		return false;
	}
	var path=$j("#newDefinitionName").val().trim();
	if(path.indexOf("/")==0){
		nps.info(nps.i18n("TEMPLATE_ERROR_INFO"),nps.i18n("TEMPLATE_NAME_INVALID"));
		$j("#newDefinitionName").addClass("ui-loxia-error");
		return false;
	 }
	
	if($j("#newDefinitionType").val()==dynamicType){
		if(!isNotBlank($j("#newHandlerBean").val().trim())){
			nps.info(nps.i18n("TEMPLATE_ERROR_INFO"),nps.i18n("TEMPLATE_HANDLERBEAN_IS_EMPTY"));
			$j("#newHandlerBean").show();
			$j("#newHandlerBean").addClass("ui-loxia-error");
			return false;
		}
	}
	
	var templatePath=$j("#newDefinitionPath").val();
//	templatePath=getFullPath(templatePath,defPathDlgRootPath);
	var validateDefPathResult=validateDefPath(templatePath);
	if(loxia.SUCCESS!=validateDefPathResult){
		nps.info(nps.i18n("TEMPLATE_ERROR_INFO"),validateDefPathResult);
		$j("#newDefinitionPath").addClass("ui-loxia-error");
		return false;
	}
	
	
	var dialogPath=$j("#newDefinitionDlgPath").val();
//	dialogPath=getFullPath(dialogPath,dlgPathDlgRootPath);
	var validateDlgPathResult=validateDlgPath(dialogPath);
	if(loxia.SUCCESS!=validateDlgPathResult){
		nps.info(nps.i18n("TEMPLATE_ERROR_INFO"),validateDlgPathResult);
		$j("#newDefinitionDlgPath").addClass("ui-loxia-error");
		return false;
	}
	
	return true;
}

//判断字符串是不是空
function isNotBlank(str){
	if(""!=str&&null!=str){
		return true;
	}else{
		return false;
	}
}

function getTreeNodeSpanId(tid){
	return "#"+tid+"_span";
}

function showHandlerClassDiv(typeId, handlerDivId, handlerInput) {
	var type = $j("#" + typeId).val();
	if (type == dynamicType) {
		$j("#" + handlerDivId).show();
	} else {
		$j("#" + handlerDivId).hide();
		$j("#" + handlerInput).val("");//将handler的输入框清空
	}
}

/**
 * 关闭弹出的对话框
 * @param dialogId
 */
function closeDialog(dialogId){
	$j("#"+dialogId).dialogff({
		type : 'close'
	});
}

/**
 * 异步加载成功
 */
function onDefTreeAsyncSuccess(event, treeId, treeNode, msg){
//	console.log("treeNode.length "+treeNode.children.length)
//	console.log("msg"+msg);
	var flag=false;
	var folderArray=new Array();
	var tree = selectDefTree.getTree();
	var objs= JSON.parse(msg); 
	for(x in objs){
		
		if(folderType==objs[x].type){
			var nodes=tree.getNodesByParam("path", objs[x].path, null);
			if(nodes.length!=0){
				folderArray[x]=nodes[0];
			}
		}
		
		if(fileType==objs[x].type){
			flag=true;
		}
	}
	
	if(flag){
		for(k in folderArray){
			selectDefTree.getTree().removeNode(folderArray[k]);
		}
	}
	
}



$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();

	definitionTree = new CmsTree(definitionTreeSetting);
	definitionTree.getInit();
	
	definitionTree.getTree().setting.callback.beforeClick=zTreeBeforeClick;
	

	$j("#newDefinitionHandlerDiv").hide();
	$j("#handlerDiv").hide();

	bindUpdateInputValueChanged();
	if (null != definitionTree.getRootNode()) {
		$j("#" + treeId + "_1_span").click();
	}

	// 绑定右上 当前节点类型的选择事件
	$j("#type").change(function() {
		showHandlerClassDiv("type", "handlerDiv","handlerBean");
	});

	// 绑定右下 新建节点类型的选择事件
	$j("#newDefinitionType").change(function() {
		showHandlerClassDiv("newDefinitionType", "newDefinitionHandlerDiv","newHandlerBean");
	});
	
	//修改模板定义按钮点击事件
	$j("#updateTemplateBtn").on("click",updateTemplateHandler);
	
	//绑定修改定义路径path的按钮事件
	$j("#selectDefinitionBtn").on("click",function(){
		openSelectDefinitionPathDialog("templatePath");
	});
	
	//绑定新增模板定义按钮事件
	$j("#addTemplateBtn").on("click",addDefinition);
	
	//绑定删除模板定义按钮事件
	$j("#deleteTemplateBtn").on("click",function(){
		var path=$j("#path").val();
		var name=$j("#name").val();
		var json = {"paths":path};
		nps.confirm(nps.i18n("TEMPLATE_CONFIRM"),nps.i18n("TEMPLATE_SURE_TO_DELETE_DEF"),function(){
			userUpdatedFlag=false;
			nps.asyncXhr(deleteDefinitionUrl, json, {
				type : "GET",
				successHandler: function(data, textStatus){    		
					if(data.isSuccess == true){
						var nodes=definitionTree.getTree().getSelectedNodes();
						var nodeName=nodes[0].name;
						nps.info(nps.i18n("TEMPLATE_INFO"),nps.i18n("TEMPLATE_DEL_SUCCESS",name));
						refreshParentNode();
					}
				}
			});
		});
		userUpdatedFlag=false;
	});
	
	//绑定添加子目录按钮事件
	$j("#addFloderBtn").on("click",function(){
		$j("#createFloderDialog").dialogff({
			type : 'open',
			close : 'in',
			width : '600px',
			height : '175px'
		});
	});
	
	//绑定添加子目录对话框确定按钮事件
	$j(".button.orange.createFloderOK").on("click",function(){
		var parentPath=$j("#parentPath").val();
		var name=$j("#floderName").val();
		var json= {
				"parentPath":parentPath,
				"name":name
		};
		//检查输入
		if(!isNotBlank(name)){
			nps.info(nps.i18n("TEMPLATE_ERROR_INFO"),nps.i18n("TEMPLATE_NAME_IS_EMPTY"));
			return;
		}else{
			
		}
		closeDialog("createFloderDialog");
		nps.asyncXhrGet(createDefinitionFloderUrl, json, {
			successHandler: function(data, textStatus){    			
				if(data.isSuccess == true){
					var nodes=definitionTree.getTree().getSelectedNodes();
					var nodeName=nodes[0].name;
					nps.info(nps.i18n("TEMPLATE_INFO"),nps.i18n("TEMPLATE_FLODER_ADD_SUCCESS",name));
					refreshCurrentNode(true);
				}
			}
		});
	});
	
	//绑定添加子目录对话框取消按钮事件
	$j(".button.orange.createFloderCancel").on("click",function(){
		closeDialog("createFloderDialog");
	});
	
	//绑定删除目录事件
	$j("#deleteFolderBtn").on("click",function(){
		nps.confirm(nps.i18n("TEMPLATE_CONFIRM"),nps.i18n("TEMPLATE_SURE_TO_DELETE_FOLDER"),function(){
			var path=$j("#path").val();
			var name=$j("#name").val();
			var json={"path":path};
			
			nps.asyncXhr(deleteDefinitionFloderUrl, json, {
				type : "GET",
				successHandler: function(data, textStatus){      			
					if(data.isSuccess == true){
						var nodes=definitionTree.getTree().getSelectedNodes();
						var nodeName=nodes[0].name;
						nps.info(nps.i18n("TEMPLATE_INFO"),nps.i18n("TEMPLATE_FLODER_DEL_SUCCESS",name));
						refreshParentNode();
					}
				}
			});
		});
		
	});
	
	//绑定新增定义路径path的按钮事件
	$j("#newDefinitionSelectPathBtn").on("click",function(){
		openSelectDefinitionPathDialog("newDefinitionPath");
	})
	
	//绑定修改diaologPath按钮的事件
	$j("#selectDialogPathBtn").on("click",function(){
		selectDialogPathDialog("dialogPath");
	});
	
	//绑定新增diaologPath按钮的事件
	$j("#newDefinitionDlgPathBtn").on("click",function(){
		selectDialogPathDialog("newDefinitionDlgPath");
	});
	
	//导出按钮事件
	$j("#exportTemplateBtn").on("click",function(){
		var nodes=definitionTree.getTree().getSelectedNodes();
		var path=nodes[0].path; 
		
		var json = {"path":path};
		var data=nps.syncXhr(exportDefinitionUrl, json, {type : "GET"});

		if(data.exception != null){
			return nps.info(nps.i18n("TEMPLATE_ERROR_INFO"),nps.i18n(data.exception.message));
		}else{
			window.location=exportDefinitionUrl+"?path="+path;
		}
		
	
		
	});
	
	//选择定义路径path对话框确定按钮绑定事件
	$j(".button.orange.tempselok").on("click", function() {
		closeDialogOK(selectDefTree,"selectTemplateDialogDiv",defPathDlgRootPath);
	});

	//选择定义路径path对话框取消按钮绑定事件
	$j(".button.orange.tempselcancel").on("click", function() {
		closeDialog("selectTemplateDialogDiv");
	});

	//选择dialog路径path对话框确定按钮绑定事件
	$j(".button.orange.dlgselok").on("click", function() {
		closeDialogOK(selectDialogTree,"selectDlgDialogDiv",dlgPathDlgRootPath);
	});

	//选择dialog路径path对话框取消按钮绑定事件
	$j(".button.orange.dlgselcancel").on("click", function() {
		closeDialog("selectDlgDialogDiv");
	});
	
	$j(".common-ic.dialog.update-ic").on("click", function() {
		var dlgPath=$j("#dialogPath").val().trim();
		window.location=toUpdateDialogUrl +dlgPath;
	});
});