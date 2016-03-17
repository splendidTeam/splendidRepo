$j.extend(loxia.regional['zh-CN'],{
	"COMPONENT_TIPS":"友情提示",
	"COMPONENT_COPY_NOSEL":"请先在左侧选择要复制的结点!",
	"COMPONENT_COPY_TARGET_NOEMPTY":"目标路径不能为空",
	"COMPONENT_OPERATE":"操作",
	"COMPONENT_MODIFY":"修改",
	"COMPONENT_DISABLE":"失效",
	"COMPONENT_ENABLE":"生效",
	"COMPONENT_DELETE":"删除",
	
	"COMPONENT_STATE_DISABLE":"已失效",
	"COMPONENT_STATE_ENABLE":"已生效",
	"COMPONENT_STATE_WAIT":"即将生效",
	"COMPONENT_ASYNLOAD_FAILURE":"加载失败",
	
	"COMPONENT_SEARCH_RESULT":"共找到 {0} 个结果",
	"LABEL_COMPONENT_PATH":"路径",
	"LABEL_COMPONENT_NAME":"组件名",
	"LABEL_COMPONENT_STATUS":"生效状况",
	"LABEL_COMPONENT_MODIFYSTATUS":"修改状况",
	"LABEL_COMPONENT_TEMPLATE":"模板",
	"CONFIRM_DELETE":"确认删除",
	"CONFIRM_DELETE_COMPONENT":"确认删除组件",
	"INFO_TITLE_DATA":"提示",
	"NPS_DELETE_SUCCESS":"删除成功",
	"NPS_DELETE_FAILURE":"该组件已被删除",
	"COMPONENT_INFO_CONFIRM":"提示",
	"COMPONENT_TIP_NOSEL":"请选择组件",
	"CONFIRM_DISABLE":"确认失效",
	"CONFIRM_DISABLE_COMPONENT":"确认失效组件",
	"CONFIRM_ENABLE":"确认生效",
	"CONFIRM_ENABLE_COMPONENT":"确认生效组件",
	"NPS_DISABLE_SUCCESS":"失效成功",
	"NPS_DISABLE_FAILURE":"失效失败",
	"NPS_ENABLE_SUCCESS":"生效成功",
	"NPS_ENABLE_FAILURE":"生效失败",
	
	"NPS_COPY_SUCCESS":"复制成功",
	"NPS_COPY_FAILURE":"复制失败",
	"COMPONENT_ERROR_INFO":"错误提示",
	"COMPONENT_MUSTBESINGLE":"只能选中单个区域",
	"COMPONENT_MUSTBE_CHILDENODE":"当前勾选的为父区域，请勾选子区域进行复制！",
	"COMPONENT_MUST_DIFFERENT":"复制到的节点不能和源节点相同",
	"COMPONENT_MUST_NOTSON":"目标节点点不能复制到源结点下",
	"COMPONENT_CANNOT_CHOOSE_PARENT":"不能选择非叶子结点!",
	"COMPONENT_CHOOSE_NODE":"请先在左侧选择要复制的结点!",
	
	"COMPONENT_COPY_TARGET_SLASH_CHECK":"需以'/'开头,且路径中不能出现'//'",
	"COMPONENT_COPY_TARGET_SLASH_NO_BEEND":"不能以'/结尾'",
	"COMPONENT_COPY_TARGET_ILLEGAL":"目标路径不合法:",
	"COMPONENT_COPY_TARGET_VIOLATIONS":"目标路径不符合规则:",
	
	"NPS_EFFECT_PATH_L":"路径为【",
	"NPS_EFFECT_PATH_R":"】的组件没有选择模板，不能生效",
	"COMPONENT_COPY_PATH":"路径为【",
	"NO_EXISTS":"】的组件不存在或已被他人删除,无法复制",
	
	"NPS_EFFECT_ISNOT_ALLOW":"该页面没有选择模板，不能生效",

});
var effectState=2;
var componentTreeUrl=base+'/cms/findComponentForTree.json';
var componentTableUrl=base+'/cms/findComponentListForTable.json';
var deleteComponentUrl=base+'/cms/deleteComponentInstance.json';

//生效组件的url
var effectComponentUrl=base+"/cms/effectComponent.json";

//复制地址
var copyComponentUrl = base+'/cms/copyComponentIntance.json';

//失效组件的url
var invalidComponentUrl=base+"/cms/invalidComponent.json";

//检查是否是父区域
//var validateComponentNodeIsParentUrl=base+'/cms/validateComponentNodeIsParent.json';

//跳转到新增页面
var createComponentUrl = base + "/cms/toCreateComponent.htm";

//跳转到修改页面
var toUpdateComponentUrl = base +"/cms/toUpdateComponent.htm";

var checkNodePathIsExistUrl = base +"/common/checkNodeIsExists.json";


var tree;


function filter(treeId, parentNode, childNodes) { 
	if (!childNodes) return null; 
	for (var i=0, l=childNodes.length; i<l; i++) { 
	childNodes[i].name = childNodes[i].name.replace('',''); 
	} 
	return childNodes; 
} 
//刷新树
function refreshTree(){
	var treeObj = tree.getTree();
	
	var node = treeObj.getNodeByTId("tree_1");
	
	treeObj.reAsyncChildNodes(node,"refresh");
}

function onClick(event, treeId, treeNode)  {
	 var tempSearchComponentUrl=componentTableUrl+"?path="+encodeURI(treeNode.path);

	 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
	 $j("#table1").data().uiLoxiasimpletable.options.dataurl=tempSearchComponentUrl;
	 $j("#table1").loxiasimpletable("refresh");
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
		form:"searchForm",
		cols : [ {
			label : "<input type='checkbox' />",
			width : "5%",
			template : "drawCheckbox"
		}, {
			name : "path",
			label : nps.i18n("LABEL_COMPONENT_PATH"),
			width : "20%",
			template:"pathTemp"
			
		}, {
			name : "elementName",
			label : nps.i18n("LABEL_COMPONENT_NAME"),
			width : "15%"
		   
		}, {
			name : "status",
			label : nps.i18n("LABEL_COMPONENT_STATUS"),
			width : "15%",
			template:"publishedTemp"

		}, {
			name : "modifyStatus",
			label : nps.i18n("LABEL_COMPONENT_MODIFYSTATUS"),
			width : "15%",
			template:"modifiedTemp"

		},{
			name : "definitionPath",
			label : nps.i18n("LABEL_COMPONENT_TEMPLATE"),
			width : "20%"
		},
		
		{label:nps.i18n("COMPONENT_OPERATE"),width:"10%", 
			type : "oplist",
            oplist: function(data){
                var list = [
                            {label:nps.i18n("COMPONENT_MODIFY"), type:"href", content:toUpdateComponentUrl+"?path=" + data.path},
                            {label:nps.i18n("COMPONENT_ENABLE"), type:"jsfunc", content:"enableComponent"},
                            {label:nps.i18n("COMPONENT_DISABLE"), type:"jsfunc", content:"disableComponent"},
                    		{label:nps.i18n("COMPONENT_DELETE"), type:"jsfunc", content:"deleteComponent"}
                   ];
               
                return list;
            }
		
		}],
		dataurl : componentTableUrl+"?path="+componentRoot
	});
	refreshData();
	
	
	/*var treeId="tree";
	var asyncUrl=componentTreeUrl;
	var autoParams=["path"];
	var onClickHandler=onClick;
	var type="get";*/
	
	var treeId="tree"; //树的divId 在jsp中定义的  必需
	var rootName=componentRootName;//根节点的显示名称  必需
	var rootPath=componentRoot;//根节点的实际路径  必需
	
	var nodeType=instanceNodeType;  //构建树的节点的仓库类型
	var onClickHandler=onClick;   //点击树节点时候的响应事件， 必需
	var searchKeyId="key";		  //搜索输入框的id	
	var searchResultId="search_result";  //显示搜索结果的文本Id
	var checkEnable=false;    //节点是否能够被选择
	var chkStyle="radio";//如果可以被选择，选择的类型： checkbox radio
	var chkType = { "Y": "ps", "N": "ps" };//选择类型的参数  参见 ztree API setting.check.chkStyle 
	var selectType=instanceNodeType;//能够被选中的仓库类型
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
	
	/*if(tree.getTree().getNodes().length>0){
		$j("#tree_1_span").click();
	}*/
	
	/*
	key = $j("#key");
	key.bind("focus", focusKey)
	.bind("blur", blurKey)
	.bind("propertychange", searchNode)
	.bind("input", searchNode);*/
	
	//绑定批量失效事件
	$j(".button.disable").click(function(){
		 var data="";
		   	$j(".checkId:checked").each(function(i,n){
		    	if(i!=0){
		        	data+=",";
		        	}
		    	data+=$j(this).val();
		    	
		    });
			 if(data==""){
			    		nps.info(nps.i18n("COMPONENT_INFO_CONFIRM"),nps.i18n("COMPONENT_TIP_NOSEL"));
			    		return ;
			    }
		    nps.confirm(nps.i18n("CONFIRM_DISABLE"),nps.i18n("CONFIRM_DISABLE_COMPONENT"), function(){
		    	var json={"paths":data};
		    	var backWarnEntity = loxia.syncXhr(invalidComponentUrl, json,{type: "GET"});
		    	if(backWarnEntity.isSuccess){
		        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("NPS_DISABLE_SUCCESS"));
		        	refreshData();
		    	}
		    	else{
		        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("NPS_DISABLE_FAILURE"));
		    	}
		    });
	
	});
	
	//绑定批量生效事件
	$j(".button.enable").click(function(){
		 var data="";
		 var noTempCount =0;
		 var tempData ="";
		   	$j(".checkId:checked").each(function(i,n){
		    	if(i!=0){
		        	data+=",";
		        	}
		    	
		    	data+=$j(this).val();
		    	if($j(this).attr("tempValue")==""){
		    		if(noTempCount!=0){
			    		tempData+=",";
			    	}
		    		noTempCount++;
		    		tempData +=$j(this).val().substring(componentRoot.length,$j(this).val().length);
		    	}
		    	
		    });
			if(data==""){
			    	nps.info(nps.i18n("COMPONENT_INFO_CONFIRM"),nps.i18n("COMPONENT_TIP_NOSEL"));
			    	return ;
			}
			if(noTempCount>0){
				 nps.info(nps.i18n("COMPONENT_ERROR_INFO"),nps.i18n("NPS_EFFECT_PATH_L")+tempData+nps.i18n("NPS_EFFECT_PATH_R"));
				 return ;
			}
			 
		    nps.confirm(nps.i18n("CONFIRM_ENABLE"),nps.i18n("CONFIRM_ENABLE_COMPONENT"), function(){
		    	var json={"paths":data};
		    	var backWarnEntity = loxia.syncXhr(effectComponentUrl, json,{type: "GET"});
		    	if(backWarnEntity.isSuccess){
		        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("NPS_ENABLE_SUCCESS"));
		        	refreshData();
		    	}
		    	else{
		        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("NPS_ENABLE_FAILURE"));
		    	}
		    });
	
	});
	//删除button remove
	$j(".button.remove").click(function(){
		 var data="";
		   	$j(".checkId:checked").each(function(i,n){
		    	if(i!=0){
		        	data+=",";
		        	}
		    	data+=$j(this).val();
		    	
		    });
			if(data==""){
			    nps.info(nps.i18n("COMPONENT_INFO_CONFIRM"),nps.i18n("COMPONENT_TIP_NOSEL"));
			    return ;
			}
		    nps.confirm(nps.i18n("CONFIRM_DELETE"),nps.i18n("CONFIRM_DELETE_COMPONENT"), function(){
		    	var json={"paths":data};
		    	var backWarnEntity = loxia.syncXhr(deleteComponentUrl, json,{type: "GET"});

	        	refreshData();
	        	refreshTree();
		    	if(backWarnEntity.isSuccess){
		        	
		        	return nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("NPS_DELETE_SUCCESS"));
		    	}
		    	else{
		    		return nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n(backWarnEntity.exception.message));
		    	}
		    });
	
	});
	//新增
	 $j(".button.orange.add").click(function(){
		 
		//var treeObj = $j.fn.zTree.getZTreeObj("tree");
		var treeObj = tree.getTree();
		var nodes = treeObj.getSelectedNodes();
		var parentNodePath='';
		if(null==nodes||nodes.length == 0){
			parentNodePath = componentRoot;
		}else{
			if(nodes[0].id=="0"){
				parentNodePath = componentRoot;
			}else{
				parentNodePath=nodes[0].id;
			}
		}
		window.location.href = createComponentUrl + "?path=" + parentNodePath;
		
     });  
	 
	 //复制
	 
	 $j(".button.copy").on("click",function(){
			
    	//var ztree = $j.fn.zTree.getZTreeObj("tree");
		 var ztree = tree.getTree();
		//var isParent=null;
		var nodes_ = ztree.getSelectedNodes(true);
		
		var data="";
		$j(".checkId:checked").each(function(i,n){
			if(i!=0){
				data+=",";
		    }
			data+=$j(this).val();
		    	
		});
		if(data.indexOf(",") >= 0 ){
			nps.info(nps.i18n("COMPONENT_ERROR_INFO"),nps.i18n("COMPONENT_MUSTBESINGLE"));
			return;
	   	}
		
		if(data==""){
			if(nodes_.length != 0){
				//var treeObj = $j.fn.zTree.getZTreeObj("tree");
				var treeObj = tree.getTree();
				var nodes = treeObj.getSelectedNodes();
				var sourcePath=nodes[0].id;
				$j("#copy-source-path").val(getRelativePath(sourcePath));
				$j("#copy-to-path").val("/");
				for (var i=0, l=nodes_.length; i<l; i++) {
					uri = nodes_[i].uri ;
					id = nodes_[i].id ;
				}
				
			}else{
				nps.info(nps.i18n("COMPONENT_ERROR_INFO"),nps.i18n("COMPONENT_CHOOSE_NODE"));
				return ;
			}
		}else{
	    		//copyUri=data;
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
			return nps.info(nps.i18n("COMPONENT_ERROR_INFO"),nps.i18n("COMPONENT_COPY_TARGET_ILLEGAL")+checkCopytoIslegal(copyTo));
		}
		
		copyTo=componentRoot+copyTo;
		copyFrom=componentRoot+copyFrom;

		
		
		if(checkCopyIsRule(copyTo,copyFrom)!=""){
			return nps.info(nps.i18n("COMPONENT_ERROR_INFO"),nps.i18n("COMPONENT_COPY_TARGET_VIOLATIONS")+checkCopyIsRule(copyTo,copyFrom));
		}

		var params={"sourcePath":copyFrom,"targetPath":copyTo};
		nps.asyncXhrGet(copyComponentUrl, params,{successHandler:function(data, textStatus){
				if (data.isSuccess) {
					nps.info(nps.i18n("COMPONENT_TIPS"),nps.i18n("NPS_COPY_SUCCESS"));
					$j("#copy-dialog").dialogff({type:'close'});
					refreshTree();
					$j("#table1").data().uiLoxiasimpletable.options.dataurl=componentTableUrl+"?path="+componentRoot;
			  		refreshData();
				} else {
			  		return nps.info(nps.i18n("COMPONENT_ERROR_INFO"),nps.i18n(data.exception.message));
				}
	  	}});
		
	 });
	 $j(".button.orange.copycancel").on("click",function(){
			
			$j("#copy-dialog").dialogff({type:'close'});
		});
});

function checkCopytoIslegal(copyTo){
	var warninfo="";
	if(copyTo==""||copyTo=="/"){
		warninfo =nps.i18n("COMPONENT_COPY_TARGET_NOEMPTY");
		return warninfo;
	}
	if(copyTo.charAt(0)!="/"||copyTo.indexOf("//") >= 0){
		warninfo =nps.i18n("COMPONENT_COPY_TARGET_SLASH_CHECK");
		return warninfo;
	}
	
	if(copyTo.length>1&&copyTo.substring(copyTo.length-1,copyTo.length)=="/"){
		warninfo =nps.i18n("COMPONENT_COPY_TARGET_SLASH_NO_BEEND");
		return warninfo;
	}
	return warninfo;
}

function checkCopyIsRule(copyTo,copyFrom){
	var warninfo ="";
	if(copyTo==copyFrom){
		warninfo = nps.i18n("COMPONENT_MUST_DIFFERENT");
		return warninfo;
	}
	if(copyTo.indexOf(copyFrom+"/") >= 0 ){
		warninfo = nps.i18n("COMPONENT_MUST_NOTSON");
		return warninfo;
	}
	
	var tempCopyFrom =copyFrom;
	var tempCopyTo =copyTo.substring(0,copyTo.lastIndexOf("/"));
	
	
	var checkSourceJson ={"path":tempCopyFrom};
	var _result1 = loxia.syncXhr(checkNodePathIsExistUrl, checkSourceJson,{type: "GET"});
	
	if(!_result1){
		tempCopyFrom = tempCopyFrom.substring(componentRoot.length,tempCopyFrom.length);
		warninfo = nps.i18n("COMPONENT_COPY_PATH")+tempCopyFrom+nps.i18n("NO_EXISTS");
		return warninfo;
	}
	
	//--
	var checkTargetJson ={"path":tempCopyTo};
	var _result2 = loxia.syncXhr(checkNodePathIsExistUrl, checkTargetJson,{type: "GET"});
	
	if(!_result2){
		tempCopyTo = tempCopyTo.substring(componentRoot.length,tempCopyTo.length);
		warninfo = nps.i18n("COMPONENT_COPY_PATH")+tempCopyTo+nps.i18n("NO_EXISTS");
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
		html = tempStr.substring(componentRoot.length,tempStr.length);
	}
	return html;
}


function modifiedTemp(data, args, idx){
	var html='';
	
	if(loxia.getObject("modifyStatus", data)){
		
		html+='<span class="ui-pyesno ui-pyesno-yes">Y</span>';
	}
	else{
		html+='<span class="ui-pyesno ui-pyesno-no">N</span>';
	}
	
	if(loxia.getObject("modifytime", data)!=null&&loxia.getObject("modifytime", data)!=''){
		html+=formatDate(loxia.getObject("modifytime", data));
	}
	
	return html;
}

function publishedTemp(data, args, idx){
	var html='';
	
	if(loxia.getObject("status", data)==effectState){
		
		html+='<span class="ui-pyesno ui-pyesno-yes">Y</span>';
	}
	else{
		html+='<span class="ui-pyesno ui-pyesno-no">N</span>';
	}
	
	if(loxia.getObject("acttime", data)!=null&&loxia.getObject("acttime", data)!=''){
		html+=formatDate(loxia.getObject("acttime", data));
	}
	
	return html;
}



function formatDate(date, format) {  
    if (!date) return;  
    if (!format) format = "yyyy-MM-dd";  
    switch(typeof date) {  
        case "string":  
            date = new Date(date.replace(/-/, "/"));  
            break;  
        case "number":  
            date = new Date(date);  
            break;  
    }   
    if (!date instanceof Date) return;  
    var dict = {  
        "yyyy": date.getFullYear(),  
        "M": date.getMonth() + 1,  
        "d": date.getDate(),  
        "H": date.getHours(),  
        "m": date.getMinutes(),  
        "s": date.getSeconds(),  
        "MM": ("" + (date.getMonth() + 101)).substr(1),  
        "dd": ("" + (date.getDate() + 100)).substr(1),  
        "HH": ("" + (date.getHours() + 100)).substr(1),  
        "mm": ("" + (date.getMinutes() + 100)).substr(1),  
        "ss": ("" + (date.getSeconds() + 100)).substr(1)  
    };      
    return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function() {  
        return dict[arguments[0]];  
    });                  
}

//
function getRelativePath(path){
	var relativePath=path.substring(componentRoot.length,path.length);
	return relativePath;
}

function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' class='checkId' name='id' tempValue='"+ loxia.getObject("definitionPath", data) + "' value='"+ loxia.getObject("path", data) + "'/>";
}
//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}

//删除component
function deleteComponent(data){
    nps.confirm(nps.i18n("CONFIRM_DELETE"),nps.i18n("CONFIRM_DELETE_COMPONENT"), function(){
    	var json={"paths":data.path};
    	var backWarnEntity = loxia.syncXhr(deleteComponentUrl, json,{type: "GET"});
    	if(backWarnEntity.isSuccess){
        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("NPS_DELETE_SUCCESS"));
    	}
    	else{
        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("NPS_DELETE_FAILURE"));
        	
    	}
  		refreshData();
    	refreshTree();
    });
}

//单条失效component
function disableComponent(data){
    nps.confirm(nps.i18n("CONFIRM_DISABLE"),nps.i18n("CONFIRM_DISABLE_COMPONENT"), function(){
    	var json={"paths":data.path};
    	var backWarnEntity = loxia.syncXhr(invalidComponentUrl, json,{type: "GET"});
    	if(backWarnEntity.isSuccess){
        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("NPS_DISABLE_SUCCESS"));
        	refreshData();
    	}
    	else{
        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("NPS_DISABLE_FAILURE"));
    	}
    });
}

//单条生效component
function enableComponent(data){
	
	//判断是否有模板
	if(data.definitionPath==""){
		nps.info(nps.i18n("COMPONENT_ERROR_INFO"),nps.i18n("NPS_EFFECT_ISNOT_ALLOW"));
		return;
	}
	
    nps.confirm(nps.i18n("CONFIRM_ENABLE"),nps.i18n("CONFIRM_ENABLE_COMPONENT"), function(){
    	var json={"paths":data.path};
    	var backWarnEntity = loxia.syncXhr(effectComponentUrl, json,{type: "GET"});
    	if(backWarnEntity.isSuccess){
        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("NPS_ENABLE_SUCCESS"));
        	refreshData();
    	}
    	else{
        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("NPS_ENABLE_FAILURE"));
    	}
    });
}








	