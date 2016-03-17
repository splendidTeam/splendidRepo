$j.extend(loxia.regional['zh-CN'],{
	"RESOURCE_INFO":"提示信息",
	"RESOURCE_ERROR_INFO":"错误提示：",
	"RESOURCE_SEARCH_RESULT" : "共找到 {0} 个结果",
	"RESOURCE_ADD_SUCCESS":"资源添加成功",
	"RESOURCE_DEL_SUCCESS":"资源 {0} 删除成功",
	"RESOURCE_CONFIRM":"操作提示",
	"RESOURCE_SURE_TO_DELETE":"确定删除该资源吗？",
	"RESOURCE_SURE_TO_DELETEF_FOLDER":"此文件夹下包含文件和文件夹。若删除该文件夹，此文件夹下的数据将一并删除且时间较长，是否继续？",
	"RESOURCE_UPDATE_SUCCESS":"资源更新成功",
	"RESOURCE_PLEASE_INPUT_NAME":"请输入资源的名称",
	"RESOURCE_PLEASE_INPUT_FILE":"请输入资源文件的位置",
	"RESOURCE_ALREADY_EXISTS":"资源已经存在",
	"RESOURCE_SYNC_SUCCESS":"资源发布成功",
	"RESOURCE_SURE_TO_SYNC":"确认发布资源?"
});

// 请求列表树的url
var findResourcesListForTreeUrl=base+"/cms/findResourceForTree.json";

// 删除资源的url
var deleteResourceUrl= base+"/cms/deleteResource.json";

// 更新文件内容的url
var updateFileContentUrl=base+"/cms/updateFileContent.htm";

//检查资源是否存在的url
var validateResourceExistUrl=base+"/cms/validateResourceExists.json";

//发布资源Url
var syncCommonResourceUrl = base + "/cms/syncCommonResource.json";

var log, className = "dark";
var tree;
var lastUpdatePath="lastUpdatePath";
var lastType="lastType";

function mycallback(url){
}

function mybeforeSend(){
}

function mycomplete(){
}

//查询url中带有的参数的值
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null){ 
		return decodeURI(r[2]); 
	}
	return null;
}

//根据不同的树节点类型显示不同的div块
function showDivByTreeNode(treeNode){
	if(treeNode==null){
		return;
	}
	var type=treeNode.type;
	
	$j("#addNewResourceDiv").hide();
	$j("#updateFileDiv").hide();
	$j("#saveResource").hide();
	$j(".func-button.view").hide();
	
	if(type==fileType){
		$j("#updateFileDiv").show();
		$j("#saveResource").show();
		$j(".func-button.view").show();
	}else{
		$j("#addNewResourceDiv").show();
	}
}

//单击左侧树
function onClick(event, treeId, treeNode)  {
		$j("#tree_fid").val(treeNode.name).attr("readonly","readonly");
		$j(".func-button.view").attr("href",treeNode.id);
		$j("#tree_state").val(treeNode.type);
		$j("#tree_sort").val(treeNode.sort);
		$j("#tree_fid").attr("treeId",treeId);
		$j("#tree_state").attr("disabled","disabled"); 
		$j("#tree_fid").data("treeNode",treeNode);
		$j("#tree_fid").data("event",event);
		$j("#filePath").val(treeNode.path);
		$j(".imgUploadComponet").attr("role",treeNode.path);
		if(treeNode.id==0){
			$j(".percent70-content-right").css("display","none");
		}else{
			$j(".percent70-content-right").css("display","block");
		}
		
		showDivByTreeNode(treeNode);
}
function onNodeCreated(e, treeId, treeNode) {
		var zTree = $j.fn.zTree.getZTreeObj("tree");
		var node = zTree.getNodeByParam("id", treeId, null);
		zTree.moveNode(treeNode,  node  , "prev");
}

var lastValue = "", nodeList = [], fontCss = {};

var newCount = 1;
function addRoot(e) {
	var zTree = $j.fn.zTree.getZTreeObj("tree"),
	isParent = e.data.isParent,
	nodes = zTree.getSelectedNodes(),
	treeNode = nodes[0];
	if (treeNode&& treeNode.getParentNode()!=null) {
		treeNode=treeNode.getParentNode();
		var new_id = treeNode.id *10  +  treeNode.children.length+1;
		treeNode = zTree.addNodes(treeNode, {id:new_id, pId:treeNode.id, isParent:false, name:$j("#add_name").val()});
		onNodeCreated(e,new_id,nodes[0]);
	} else {
		treeNode = zTree.addNodes(null, {id:(100 + newCount), pId:0, isParent:false, name:$j("#add_name").val()});
		
	}
	if (treeNode) {
		zTree.editName(treeNode[0]);
	} else {
		alert("叶子节点被锁定，无法增加子节点");
	}
};

function afterAddSuccess(){
	$j("#name").val("");//将 name 输入框清空
	refreshTree();
	
}

function refreshTree(){
	var treeObj = tree.getTree();
	var nodes = treeObj.getSelectedNodes();
	if(nodes.length>0){
		var selectedNode=nodes[0];
		treeObj.reAsyncChildNodes(selectedNode,"refresh");
	}
}

function refreshParentNode(){
	var treeObj = tree.getTree();
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

function getTreeNodeSpanId(tid){
	return "#"+tid+"_span";
}

function validateUpdateForm(){
	if(!isNotBlank($j("#updateFile").val())){
		nps.info(nps.i18n("RESOURCE_INFO"),nps.i18n("RESOURCE_PLEASE_INPUT_FILE"));
		return false;
	}
	return true;
}

function validateAddResourceForm(){
	if(!isNotBlank($j("#name").val())){
		nps.info(nps.i18n("RESOURCE_INFO"),nps.i18n("RESOURCE_PLEASE_INPUT_NAME"));
		return false;
	}
	
	if(fileType==$j("#add_resourceType").val()){
		if(!isNotBlank($j("#fileToUpload").val())){
			nps.info(nps.i18n("RESOURCE_INFO"),nps.i18n("RESOURCE_PLEASE_INPUT_FILE"));
			return false;
		}
	}
	
	//检测是否已经存在该节点
	var parentPath=$j("#parentPath").val()+"/";
	var name=$j("#name").val();
	var path=parentPath+name;
	var json={"path":path};
	var backWarnEntity = loxia.syncXhr(validateResourceExistUrl, json,{type: "GET"});			
	if(backWarnEntity.isSuccess == true){
		nps.info(nps.i18n("RESOURCE_ERROR_INFO"),nps.i18n("RESOURCE_ALREADY_EXISTS"));
		return false;
	}
	
	return true;
}

// 添加子节点
function add(e) {
	
	var zTree = $j.fn.zTree.getZTreeObj("tree"),
	isParent = e.data.isParent,
	nodes = zTree.getSelectedNodes(),
	treeNode = nodes[0];
	
	// 设置hidden 的value
	
	$j("#parentPath").val(treeNode.id);
	if(!validateAddResourceForm()){
		return;
	}
	// 提交表单，成功后，刷新树
	nps.submitForm('addResourceForm',{mode: 'sync',  type: "POST"});
	
};

//判断字符串是不是空
function isNotBlank(str){
	if(""!=str&&null!=str){
		return true;
	}else{
		return false;
	}
}

//当选择文件时，如果没有输入文件名称，那么将文件名称的值作为默认值
function onAddResourceSelected(){
	var inputedName=$j("#name").val();
	if(isNotBlank(inputedName)){
		return;
	}
	var selectedPath=$j("#fileToUpload").val();
	if(isNotBlank(selectedPath)){
		var index=selectedPath.lastIndexOf("\\");
		if(index!=-1){
			var fileName=selectedPath.substring(index+1);
			if(isNotBlank(fileName)){
				$j("#name").val(fileName);
			}
		}else{
			$j("#name").val(selectedPath);
		}
	}
}

//删除资源
function removeResource(e){
	var nodes = tree.getTree().getSelectedNodes();
	var node=nodes[0];
	var path=node.path;
	var type=node.type;
	
	var tip="RESOURCE_SURE_TO_DELETE";
	if(folderType==type&&true==node.isParent){
		tip="RESOURCE_SURE_TO_DELETEF_FOLDER";
	}
	
	nps.confirm(nps.i18n("RESOURCE_CONFIRM"),nps.i18n(tip),function(){
		
		var json={"path":path};
		loxia.asyncXhr(deleteResourceUrl, json,{type: "GET",
			success: function(data){    			
				if(data.isSuccess == true){
					var name=node.name;
					nps.info(nps.i18n("RESOURCE_INFO"),nps.i18n("RESOURCE_DEL_SUCCESS",name));
					refreshParentNode();
				}
			}	
		});
	});
	
	
}

//发布（同步）资源
function syncCommonResource(e){
	var nodes = tree.getTree().getSelectedNodes();
	var node=nodes[0];
	var path=node.path;
	
	nps.confirm(nps.i18n("RESOURCE_CONFIRM"),nps.i18n("RESOURCE_SURE_TO_SYNC"),function(){
		
		var json={"paths":path};
		loxia.asyncXhr(syncCommonResourceUrl, json,{type: "POST",
			success: function(data){    			
				if(data.isSuccess == true){
					var name=node.name;
					nps.info(nps.i18n("RESOURCE_INFO"),nps.i18n("RESOURCE_SYNC_SUCCESS",name));
					refreshParentNode();
				}
			}	
		});
	});
}

/**
 * 打开上次操作的节点
 * @param lastUpdatePath
 */
function openNode(lastUpdatePath){
	if(!isNotBlank(lastUpdatePath)){
		return;
	}
	
	var index=1;
	var parentPathStr=null;
	var curPathStr=null;
	var parentNode=null
	while(true){
		index=lastUpdatePath.indexOf("/",index);
		if(index==-1){
			break;
		}
		curPathStr=lastUpdatePath.substring(0,index);
		var curNode=tree.getTree().getNodeByParam("id",curPathStr,parentNode);
		var flag=tree.getTree().expandNode(curNode,true,false,true,true);
		index++;
		parentNode=curNode;
	}
	
	var node=tree.getTree().getNodeByParam("id",curPathStr,null);
	var nodeTid=node.tId;
	$j(getTreeNodeSpanId(nodeTid)).click();
}

$j(document).ready(function(){
	loxia.init({
        debug : true,
        region : 'zh-CN'
    });
    nps.init();
    
    var treeId="tree";
    var rootName="resources";
    var rootPath="/resources";
	var nodeType="nt:folder,nt:file";
	var onClickHandler=onClick;
	var searchKeyId="key";
	var searchResultId="search_result";
	var checkEnable=false;
	var chkStyle="checkbox";
	var chkType = { "Y": "ps", "N": "ps" };
	var selectType="nt:folder,nt:file";
	var nodeLevel=0;
	var type="get";
//	var excludePath="/resources/test/a";

	var cmsTreeSetting={
			"treeId":treeId,
			"rootName":rootName,
			"rootPath":rootPath,
			"nodeType":nodeType,
			"onClickHandler":onClickHandler,
			"searchKeyId":searchKeyId,
			"searchResultId":searchResultId,
			"checkEnable":checkEnable,
			"chkStyle":chkStyle,
			"chkType":chkType,
			"selectType":selectType,
			"nodeLevel":nodeLevel,
//			"excludePath":excludePath
	}
	
	tree= new CmsTree(cmsTreeSetting);

	tree.getInit();
	
	var lastTypeValue=getQueryString(lastType);
	if(isNotBlank(lastTypeValue)){
		if("add"==lastTypeValue){
			nps.info(nps.i18n("RESOURCE_INFO"),nps.i18n("RESOURCE_ADD_SUCCESS"));
		}
		
		if("update"==lastTypeValue){
			nps.info(nps.i18n("RESOURCE_INFO"),nps.i18n("RESOURCE_UPDATE_SUCCESS"));
		}
//		location.href = base +"/cms/toResources.htm";
	}
	
	$j("#fileToUpload").change(onAddResourceSelected);
	
	$j("#addParent").bind("click", {isParent:true}, addRoot);
	$j("#addLeaf").bind("click", {isParent:false}, add);
	$j("#remove_element").bind("click",removeResource);
	$j("#sync_element").bind("click",syncCommonResource);
	
	$j("#add_resourceType").change(function(){
		var type=$j("#add_resourceType").val();
		if(type==fileType){
			$j("#addFileDiv").show();
		}else{
			$j("#addFileDiv").hide();
		}
	});
	
	$j("#addFileDiv").hide();
	$j("#addNewResourceDiv").hide();
	$j("#updateFileDiv").hide();
	
	if(tree.getTree().getNodes().length>0){
		$j("#tree_1_span").click();
	}
	
	var lastUpdatePathValue=getQueryString(lastUpdatePath);
	if(isNotBlank(lastUpdatePathValue)){
		var lastUpdateNode=tree.getTree().getNodeByParam("path",lastUpdatePathValue);
//		openNode(lastUpdatePathValue);
//		var nodeTid=lastUpdateNode.tId;
//		alert(getTreeNodeSpanId(nodeTid)+"  "+$j(getTreeNodeSpanId(nodeTid)).length);
//		$j(getTreeNodeSpanId(nodeTid)).click();
	}
	
	// 新增节点
	$j("#saveResource").click(function(){
		if(!validateUpdateForm()){
			return;
		}
		nps.submitForm('updateFileContentForm',{mode: 'sync',  type: "POST"});
	});				
	
});