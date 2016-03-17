$j.extend(loxia.regional['zh-CN'],{
	"DIALOG_TIPS":"提示信息",
	"DIALOG_ERROR_INFO":"错误信息",

	"LABEL_DIALOG_PATH":"路径",
	"LABEL_DIALOG_DESCRIPTION":"描述",
	"LABEL_DIALOG_EXTEND":"继承",
	"LABEL_TYPE_ISDIALOG":"类型",
	"DIALOG_OPERATE":"操作",
	
	"PATH_EXISTS":"目录已存在",
	"FOLDER_NAME_NOTEMPTY":"请输入次目录的名称",
	
	
	"DIALOG_MODIFY":"修改",
	"DIALOG_DELETE":"删除",

	"DIALOG_COPY_NOSEL":"请先在左侧选择要复制的结点!",
	"DIALOG_COPY_TARGET_NOEMPTY":"目标路径不能为空",

	"CONFIRM_DELETE":"确认删除",
	"CONFIRM_DELETE_DIALOG":"确认删除定义?",
	"INFO_TITLE_DATA":"提示",
	"NPS_DELETE_SUCCESS":"删除成功",
	"NPS_DELETE_FAILURE":"该组件已被删除",
	"DIALOG_INFO_CONFIRM":"提示",
	"DIALOG_TIP_NOSEL":"请在右侧表中选择属性定义",
	
	"NPS_COPY_SUCCESS":"复制成功",
	"NPS_COPY_FAILURE":"复制失败",
		
	"NPS_CREATE_FOLDER_SUCCESS":"添加目录成功",
	"NPS_CREATE_FOLDER_FAILURE":"添加目录失败",
	
	"DIALOG_NODE_MUSTBESINGLE":"只能选中单个节点",
	
	"DIALOG_MUSTBESINGLE":"只能选中单个定义",
	"DIALOG_MUSTBE_CHILDNODE":"当前勾选的为父节点，请勾选子节点进行复制！",
	"DIALOG_MUST_DIFFERENT":"复制到的节点不能和源节点相同",
	"DIALOG_MUST_NOTSON":"目标节点点不能复制到源结点下",
	"DIALOG_CANNOT_CHOOSE_PARENT":"不能选择非叶子结点!",
	"DIALOG_CHOOSE_NODE":"请先在左侧选择要复制的结点!",
	
	"DIALOG_COPY_TARGET_SLASH_CHECK":"需以'/'开头,且路径中不能出现'//'",
	"DIALOG_COPY_TARGET_SLASH_NO_BEEND":"不能以'/结尾'",
	"DIALOG_COPY_TARGET_ILLEGAL":"目标路径不合法:",
	"DIALOG_COPY_TARGET_VIOLATIONS":"目标路径不符合规则:",
	
	"DIALOG_COPY_PATH":"路径为【",
	"NO_EXISTS":"】的定义不存在或已被他人删除,无法复制",
	"FOLDER_PATH_NO_SLASH":"路径名不能有'/'、'\\'或者'//'",
	"SINGER_FOLDER_DELETE":"该项属于目录，删除后，该目录下定义也将被删除，确认删除?",
	"BUTCH_FOLDER_DELETE":"所选条目中，含有目录节点，删除后该节点下的定义也将被删除，确认删除?",
	"PARENTNODE_ISDELETE":"父目录已被删除,需重新选择节点",
	"ONLY_CHOOSE_DIALOG":"只能复制属性定义",
	"DIALOG_NONODE_BESEL":"请在左侧树选择你要添加目录所属的父目录",
	"DIALOG_PARENTPATH_MUSTBE_FOLDER":"不能选择定义节点作为父目录",
	"DIALOG_NODE_MUSTBE_FOLDER":"不能将定义作为父目录",
	"TYPE_DEFITION_DIALOG":"定义",
	"TYPE_FOLDER_DIALOG":"目录",
	"DIALOG_COPY_TARGET_PATH_NOTDEF":"目标路径不能是定义的子目录"

});


//列表页
var toDialogUrl = base +"/cms/toDialog.htm";


//跳转到新增页面
var toCreateDialogUrl =base+"/cms/toCreateDialog.htm";

//跳转到修改页面
var toUpdateDialogUrl = base +"/cms/toUpdateDialog.htm";

//列表
var dialogTableUrl=base+'/cms/findDialogList.json';

//复制地址
var copyDialogUrl = base+'/cms/copyDialogDefinition.json';

//校验节点是否存在
var checkNodePathIsExistUrl = base +"/common/checkNodeIsExists.json";

//删除属性定义
var deleteDialogUrl=base+'/cms/deleteDialog.json';

//
var createFolderUrl = base +'/cms/createFolder.json';

//var copyUri;

var tree;



//刷新树
function refreshTree(){
	var treeObj = tree.getTree();
	
	var node = treeObj.getNodeByTId("tree_1");
	
	treeObj.reAsyncChildNodes(node,"refresh");
}

//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}


function zTreeOnAsyncError(event, treeId, treeNode){
	nps.info(nps.i18n("COMPONENT_TIPS"),nps.i18n("COMPONENT_ASYNLOAD_FAILURE"));
} 

function zTreeOnAsyncSuccess(event, treeId, treeNode, msg){ 

} 

var log, className = "dark";


function onClick(event, treeId, treeNode)  {
	 var tempSearchComponentUrl=dialogTableUrl+"?path="+encodeURI(treeNode.path);

	 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
	 $j("#table1").data().uiLoxiasimpletable.options.dataurl=tempSearchComponentUrl;
	 $j("#table1").loxiasimpletable("refresh");
}
function onNodeCreated(e, treeId, treeNode){
		//var zTree = $j.fn.zTree.getZTreeObj("tree");
		var zTree = tree.getTree();
		var node = zTree.getNodeByParam("id", treeId, null);
		zTree.moveNode(treeNode,  node  , "prev");
}

var key;


/**树结束**/



$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	
	
	 $j("#table1").loxiasimpletable({
			page : true,
			size : 10,
			nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
			cols : [ {
				label : "<input type='checkbox' />",
				width : "5%",
				template : "drawCheckbox"
			}, {
				name : "path",
				label : nps.i18n("LABEL_DIALOG_PATH"),
				width : "20%",
				template:"pathTemp"
				
			}, {
				name : "description",
				label : nps.i18n("LABEL_DIALOG_DESCRIPTION"),
				width : "20%"
			   
			},	{
				name : "extend",
				label : nps.i18n("LABEL_DIALOG_EXTEND"),
				width : "20%",
				template:"extendTemp"

			},	{
				name : "type",
				label : nps.i18n("LABEL_TYPE_ISDIALOG"),
				width : "15%",
				template:"typeTemp"
			},		
			{label:nps.i18n("DIALOG_OPERATE"),width:"10%", 

				template : "drawEditOpt" 

			}],
			dataurl : dialogTableUrl+"?path="+defPathRoot
		});
		refreshData();
		
		var treeId="tree"; //树的divId 在jsp中定义的  必需
		var rootName=dialogRootName;//根节点的显示名称  必需
		var rootPath=defPathRoot;//根节点的实际路径  必需
		
		var nodeType=defaultNodeType +","+definitionNodeType;  //构建树的节点的仓库类型
		var onClickHandler=onClick;   //点击树节点时候的响应事件， 必需
		var searchKeyId="key";		  //搜索输入框的id	
		var searchResultId="search_result";  //显示搜索结果的文本Id
		var checkEnable=false;    //节点是否能够被选择
		var chkStyle="radio";//如果可以被选择，选择的类型： checkbox radio
		var chkType = { "Y": "ps", "N": "ps" };//选择类型的参数  参见 ztree API setting.check.chkStyle 
		var selectType=definitionNodeType;//能够被选中的仓库类型
		var nodeLevel=1;//能够被选中的节点类型    0 父子节点均被选择    1父子节点均不被选择   2只选择父节点   3只选择子节点
		//var type="get";
		
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
		};
		
		tree= new CmsTree(cmsTreeSetting);

		tree.getInit();

	//新增
	$j(".button.orange.add").click(function(){
		
		var treeObj = tree.getTree();
		var nodes = treeObj.getSelectedNodes();
		var parentNodePath='';
		if(null==nodes||nodes.length == 0){
			parentNodePath = defPathRoot;
		}else{
			//cms:dialog下不能再建dialog
			if(nodes[0].type==definitionNodeType){
				nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("DIALOG_PARENTPATH_MUSTBE_FOLDER"));
				return;
			}
			
			if(nodes[0].id=="0"){
				parentNodePath = defPathRoot;
			}else{
				parentNodePath=nodes[0].path;
			}
		}
		window.location.href = toCreateDialogUrl + "?path=" + parentNodePath;

     });
	 
	//复制浮层
	$j(".button.copy").on("click",function(){
		 
		 var ztree = tree.getTree();
		 var nodes_ = ztree.getSelectedNodes(true);
		 var data="";
		 var tempType="";
		 
		 $j(".checkId:checked").each(function(i,n){
			 if(i!=0){
				data+=",";
				tempType +=",";
			 }
			 data+=$j(this).val();
			 tempType +=$j(this).attr("tempType");
		    });
		if(data.indexOf(",") >= 0 ){
			nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("DIALOG_MUSTBESINGLE"));
			return;
	   	}
		
		if(data==""){
			if(nodes_.length != 0){
				
				if(nodes_.length >1){
					nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("DIALOG_MUSTBESINGLE"));
					return;
				}
				/*
				if(nodes_[0].isParent){
					nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("DIALOG_MUSTBE_CHILDNODE"));
					return;
				}*/
				
				//是定义，并且不是父节点
				if(nodes_[0].type!=definitionNodeType){
					nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("ONLY_CHOOSE_DIALOG"));
					return;
				}else{
					if(nodes_[0].isParent){
						nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("DIALOG_MUSTBE_CHILDNODE"));
						return;
					}
				}
				
				var sourcePath=nodes_[0].id;
				$j("#copy-source-path").val(getRelativePath(sourcePath));
				$j("#copy-to-path").val("/");
				
				
			}else{
				nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("DIALOG_CHOOSE_NODE"));
				return ;
			}
		}else{
			if(tempType!=definitionNodeType){
				nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("ONLY_CHOOSE_DIALOG"));
				return;
			}else{
				//根据data查询该数据是否是子节点
				
				var checkDataJson ={"path":data,"page.size":15,"page.start":1};
				
				var _r  = nps.syncXhr(dialogTableUrl, checkDataJson,{type: "GET"});
				if(_r.items.length >0){
					nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("DIALOG_MUSTBE_CHILDNODE"));
					return ;
				}
				
			}
	    	$j("#copy-source-path").val(getRelativePath(data));
			$j("#copy-to-path").val("/");

		}
		
		$j("#copy-dialog").dialogff({type:'open',close:'in',width:'600px',height:'200px'});
	});
		    
	//复制确认
	$j(".button.orange.copyok").on("click",function(){
		var copyTo=$j("#copy-to-path").val().trim();
		var copyFrom =$j("#copy-source-path").val().trim();
		
		if(checkCopytoIslegal(copyTo)!=""){
			return nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("DIALOG_COPY_TARGET_ILLEGAL")+checkCopytoIslegal(copyTo));
		}
		
		copyTo=defPathRoot+copyTo;
		copyFrom=defPathRoot+copyFrom;

		if(checkCopyIsRule(copyTo,copyFrom)!=""){
			return nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("DIALOG_COPY_TARGET_VIOLATIONS")+checkCopyIsRule(copyTo,copyFrom));
		}
		
		var tempCopyTo =copyTo.substring(0,copyTo.lastIndexOf("/"));
		
		var treeObj = tree.getTree();
		
		/*var type =null;
		
		var nodesArray=treeObj.transformToArray(treeObj.getNodes());
		
		for(var i = 0 ; i < nodesArray.length ; i++){
			if(nodesArray[i].path==tempCopyTo){
				
				type = nodesArray[i].type;
				break;
			}
		}*/
		
		var nodes = treeObj.getNodesByParam("path", tempCopyTo, null);
		if(nodes==undefined||(nodes.length>0&&nodes[0]!=undefined&&nodes[0].type==definitionNodeType)){
			return nps.info(nps.i18n("DIALOG_COPY_TARGET_PATH_NOTDEF"));
		}

		var params={"sourcePath":copyFrom,"targetPath":copyTo};
		nps.asyncXhrGet(copyDialogUrl, params,{successHandler:function(data, textStatus){
				if (data.isSuccess) {
					nps.info(nps.i18n("DIALOG_TIPS"),nps.i18n("NPS_COPY_SUCCESS"));
					$j("#copy-dialog").dialogff({type:'close'});
					refreshTree();
					$j("#table1").data().uiLoxiasimpletable.options.dataurl=dialogTableUrl+"?path="+defPathRoot;
			  		refreshData();
				} else {
			  		return nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("NPS_COPY_FAILURE"));
				}
	  	}});
			
	});
		 
	$j(".button.orange.copycancel").on("click",function(){
				
		$j("#copy-dialog").dialogff({type:'close'});
	});
	
	//新增目录
	$j(".button.orange.addfolder").click(function(){
		var ztree = tree.getTree();
		var nodes_ = ztree.getSelectedNodes(true);
		var sourcePath =null;
		if(nodes_.length != 0){
			if(nodes_.length >1){
				nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("DIALOG_NODE_MUSTBESINGLE"));
				return;
			}
			
			if(nodes_[0].type==definitionNodeType){
				nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("DIALOG_NODE_MUSTBE_FOLDER"));
				return;
			}
			
			sourcePath = nodes_[0].path;
		}else{
			nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("DIALOG_NONODE_BESEL"));
			return;
		}
		
		$j("#parent-folder-path").html(getRelativePath(sourcePath)+"/");
		$j("#folder-name").val("");
		$j("#addfolder-dialog").dialogff({type:'open',close:'in',width:'600px',height:'200px'});
		
	});
	
	//新增目录确认
	$j(".button.orange.addFolderok").on("click",function(){
		var folderName=$j("#folder-name").val().trim();
		var pFolderPath =$j("#parent-folder-path").html().trim();
		
		if(folderName ==""){
			return nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("FOLDER_NAME_NOTEMPTY"));
		}
		
		if((folderName.indexOf('/')>=0||folderName.indexOf('\\')>=0||folderName.indexOf("//") >= 0)){
			return nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("FOLDER_PATH_NO_SLASH"));
		}
		var parentPath = defPathRoot+pFolderPath;
		var fullPath = defPathRoot+pFolderPath + folderName;
		
		var json1={"path":parentPath};
	 	var _d1 = loxia.syncXhr(checkNodePathIsExistUrl, json1,{type: "GET"});
	 	if(!_d1){
	 		
	 		nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("PARENTNODE_ISDELETE"));
	 		//关浮层
	 		$j("#addfolder-dialog").dialogff({type:'close'});
	 		
	 		refreshData();
        	refreshTree();
	 		return ;
	 	}
		
		var json2={"path":fullPath};
	 	var _d2 = loxia.syncXhr(checkNodePathIsExistUrl, json2,{type: "GET"});
	 	if(_d2){
	 		
	 		return nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("PATH_EXISTS"));
	 	}
	 	var _r = loxia.syncXhr(createFolderUrl, json2,{type: "GET"});
	 	
		if (_r.isSuccess) {
			nps.info(nps.i18n("DIALOG_TIPS"),nps.i18n("NPS_CREATE_FOLDER_SUCCESS"));
			$j("#addfolder-dialog").dialogff({type:'close'});
			refreshTree();
			$j("#table1").data().uiLoxiasimpletable.options.dataurl=dialogTableUrl+"?path="+defPathRoot;
	  		refreshData();
		} else {
	  		return nps.info(nps.i18n("DIALOG_ERROR_INFO"),nps.i18n("NPS_CREATE_FOLDER_FAILURE"));
		}
	  	
			
	});
		 
	$j(".button.orange.cancelAddFolder").on("click",function(){
				
		$j("#addfolder-dialog").dialogff({type:'close'});
	});
	
	
	//删除button remove
	$j(".button.remove").click(function(){
		 var data="";
		 var tempType="";
		   	$j(".checkId:checked").each(function(i,n){
		    	if(i!=0){
		        	data+=",";
		        	tempType+=",";
		        }
		    	data+=$j(this).val();
		    	tempType +=$j(this).attr("tempType");
		    });
			if(data==""){
			    nps.info(nps.i18n("DIALOG_INFO_CONFIRM"),nps.i18n("DIALOG_TIP_NOSEL"));
			    return ;
			}
			
			var confirmInfo =nps.i18n("CONFIRM_DELETE_DIALOG");
			
			if(tempType.indexOf(defaultNodeType)>=0){
				confirmInfo =nps.i18n("BUTCH_FOLDER_DELETE");
			}
			
			
		    nps.confirm(nps.i18n("DIALOG_DELETE"),confirmInfo, function(){
		    	var json={"paths":data};
		    	var backWarnEntity = loxia.syncXhr(deleteDialogUrl, json,{type: "GET"});
	        	refreshData();
	        	refreshTree();
		    	if(backWarnEntity.isSuccess){
		        	
		        	return nps.info(nps.i18n("DIALOG_TIPS"),nps.i18n("NPS_DELETE_SUCCESS"));
		    	}
		    	else{
		    		return nps.info(nps.i18n("DIALOG_TIPS"),nps.i18n(backWarnEntity.exception.message));
		    	}
		    });
	
	});
	
	
	 
});


function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' class='checkId' name='path' tempType='"+loxia.getObject("type", data)+"' value='"+ loxia.getObject("path", data) + "'/>";
}

function drawEditOpt(data, args, idx){
	var result="";
	var relativePath=loxia.getObject("path", data).replace(defPathRoot,"");
	if(data.type==definitionNodeType){
		result+="<a href='javascript:void(0);' pathVal='"+loxia.getObject("path", data)+"' typeVal='"+loxia.getObject("type", data)+"' class='func-button delete' onclick='javascript:deleteDialog(this)'>"+nps.i18n("DIALOG_DELETE")+
				"</a>"
				+"<a class='func-button update' href='"+toUpdateDialogUrl+"?path="+relativePath+"'>"+nps.i18n("DIALOG_MODIFY")+"</a>";
	}else{
		result+="<a href='javascript:void(0);' pathVal='"+loxia.getObject("path", data)+"' typeVal='"+loxia.getObject("type", data)+"' class='func-button delete' onclick='javascript:deleteDialog(this)'>"+nps.i18n("DIALOG_DELETE")+
		"</a>";
	}
	
	return result;
}

function checkCopytoIslegal(copyTo){
	var warninfo="";
	if(copyTo==""||copyTo=="/"){
		warninfo =nps.i18n("DIALOG_COPY_TARGET_NOEMPTY");
		return warninfo;
	}
	if(copyTo.charAt(0)!="/"||copyTo.indexOf("//") >= 0){
		warninfo =nps.i18n("DIALOG_COPY_TARGET_SLASH_CHECK");
		return warninfo;
	}
	
	if(copyTo.length>1&&copyTo.substring(copyTo.length-1,copyTo.length)=="/"){
		warninfo =nps.i18n("DIALOG_COPY_TARGET_SLASH_NO_BEEND");
		return warninfo;
	}
	return warninfo;
}

function checkCopyIsRule(copyTo,copyFrom){
	var warninfo ="";
	if(copyTo==copyFrom){
		warninfo = nps.i18n("DIALOG_MUST_DIFFERENT");
		return warninfo;
	}
	if(copyTo.indexOf(copyFrom+"/") >= 0 ){
		warninfo = nps.i18n("DIALOG_MUST_NOTSON");
		return warninfo;
	}
	
	var tempCopyFrom =copyFrom;
	var tempCopyTo =copyTo.substring(0,copyTo.lastIndexOf("/"));
	
	
	var checkSourceJson ={"path":tempCopyFrom};
	var _result1 = loxia.syncXhr(checkNodePathIsExistUrl, checkSourceJson,{type: "GET"});
	
	if(!_result1){
		tempCopyFrom = tempCopyFrom.substring(defPathRoot.length,tempCopyFrom.length);
		warninfo = nps.i18n("DIALOG_COPY_PATH")+tempCopyFrom+nps.i18n("NO_EXISTS");
		return warninfo;
	}
	
	//--
	var checkTargetJson ={"path":tempCopyTo};
	var _result2 = loxia.syncXhr(checkNodePathIsExistUrl, checkTargetJson,{type: "GET"});
	
	if(!_result2){
		tempCopyTo = tempCopyTo.substring(defPathRoot.length,tempCopyTo.length);
		warninfo = nps.i18n("DIALOG_COPY_PATH")+tempCopyTo+nps.i18n("NO_EXISTS");
		return warninfo;
	}
	
	return warninfo;
}


//检测字符串是否为空
function checkStrIsNotNull(str) {
	if (str != null && str.trim() != "") {
		return true;
	} else {
		return false;
	}
}

//路径转换
function pathTemp(data, args, idx){
	var html='';
	var tempStr =loxia.getObject("path", data);
	if(checkStrIsNotNull(tempStr)){
		html = tempStr.substring(defPathRoot.length,tempStr.length);
	}
	return html;
}

//是否定义
function typeTemp(data, args, idx){
var html='';
	
	if(loxia.getObject("type", data)==definitionNodeType){
		
		html+=nps.i18n("TYPE_DEFITION_DIALOG");
	}
	else{
		html+=nps.i18n("TYPE_FOLDER_DIALOG");
	}
	
	return html;
}


//继承路径转换
function extendTemp(data, args, idx){
	var html='';
	var tempStr =loxia.getObject("extend", data);
	if(checkStrIsNotNull(tempStr)){
		html = tempStr.substring(defPathRoot.length,tempStr.length);
	}
	return html;
}


function getRelativePath(path){
	var relativePath=path.substring(defPathRoot.length,path.length);
	return relativePath;
}

//删除dialog
function deleteDialog(obj){
	var type =obj.getAttribute('typeVal');
	var path= obj.getAttribute('pathVal');
	var confirmInfo =nps.i18n("CONFIRM_DELETE_DIALOG");
	
	if(type!=definitionNodeType){
		confirmInfo =nps.i18n("SINGER_FOLDER_DELETE");
	}
	
    nps.confirm(nps.i18n("CONFIRM_DELETE"),confirmInfo, function(){
    	var json={"paths":path};
    	var backWarnEntity = loxia.syncXhr(deleteDialogUrl, json,{type: "GET"});
    	if(backWarnEntity.isSuccess){
        	nps.info(nps.i18n("DIALOG_TIPS"),nps.i18n("NPS_DELETE_SUCCESS"));
    	}
    	else{
        	nps.info(nps.i18n("DIALOG_TIPS"),backWarnEntity.exception.message);
        	
    	}
  		refreshData();
    	refreshTree();
    });
}






	