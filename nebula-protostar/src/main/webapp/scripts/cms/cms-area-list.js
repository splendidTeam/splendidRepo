$j.extend(loxia.regional['zh-CN'],{
	"AREA_INFO":"提示信息",
	"AREA_ASYNC_LOAD_FAIL":"异步加载失败",
	"AREA_CONFIRM":"操作提示",
	"AREA_PATH":"路径",
	"AREA_SURE_TO_DELETE":"确定删除选中的区域吗？",
	"AREA_SURE_TO_EFFECT":"确定生效选中的区域吗？",
	"AREA_OPER_SUCCESS":"操作成功",
	"AREA_OPER_FAIL":"操作失败",
	"AREA_ERROR_INFO":"错误提示：",
	"AREA_DATA_NOT_FOUND":"未查到数据",
	"AREA_COPY_PATH_CAN_NOT_BE_EMPTY":"复制路径不能为空!",
	"AREA_NAME":"区域名称",
	"AREA_EFFECT_STATE":"生效状况",
	"AREA_UPDATE_STATE":"修改状况",
	"AREA_TEMPLATE":"模板",
	"AREA_OPERATE":"操作",
	"AREA_UPDATE":"修改",
	"AREA_INVALID":"失效",
	"AREA_DELETE":"删除",
	"AREA_EFFECT":"生效",
	"AREA_CHOOSE_NODE":"请先在左侧选择要复制的结点!",
	"AREA_CANNOT_CHOOSE_PARENT":"不能选择非叶子结点!",
	"AREA_SURE_TO_INVALID":"确定失效选中的区域吗？",
	"AREA_CHOOSE_AREA":"未选择任何区域",
	"AREA_MUSTBESINGLE":"只能选中单个区域",
	"AREA_MUSTBESONAREA":"当前勾选的为父区域，请勾选子区域进行复制！",
	"AREA_MUST_DIFFERENT":"复制到的节点不能和源节点相同",
	"AREA_MUST_NOTSON":"目标结点不能是源结点的子结点",
	"AREA_EFFECT_SUCESS":"生效成功",
	"AREA_EFFECT_FAIL":"生效失败",
	"AREA_INVALID_SUCESS":"失效成功",
	"AREA_INVALID_FAIL":"失效失败",
	"AREA_COPY_SUCESS":"复制成功",
	"AREA_COPY_FAIL":"复制失败",
	"AREA_DEL_SUCESS":"删除成功",
	"AREA_DEL_FAIL":"删除失败",
	"AREA_SEARCH_RESULT" : "共找到 {0} 个结果",
	"AREA_EFFECT_PATH_NULL":"路径值为【{0}】的区域没有选择模板，不能发布",
});
var effectState=2;
var invalidState=1;
var modifyStatus_NO=0;
var modifyStatus_YES=1;
var key;
var tree;

//请求区域列表的url
var findAreaListUrl=base+'/cms/findAreaListForTable.json';

//请求列表树的url
var findAreaListForTreeUrl=base+"/cms/findAreaForTree.json";

//删除区域url
var deleteAreaUrl=base+"/cms/deleteAreaInstance.json";

//复制区域的url
var copyAreaUrl=base+"/cms/copyAreaIntance.json";

//生效区域的url
var effectAreaUrl=base+"/cms/effectArea.json";

//失效区域的url
var invalidAreaUrl=base+"/cms/invalidArea.json";

var addAreaPageUrl = base + "/cms/toCreateArea.htm";

//检查是否是父区域
var validateIsParentNodeUrl=base+'/cms/validateAreaIsParentNode.json';

function zTreeOnAsyncError(event, treeId, treeNode){ 
	nps.info(nps.i18n("AREA_INFO"),nps.i18n("AREA_ASYNC_LOAD_FAIL"));
} 

function zTreeOnAsyncSuccess(event, treeId, treeNode, msg){ 

} 

//点击左边树的节点，右边出现该节点的子节点详细信息的列表
function onClick(event, treeId, treeNode) {
	$j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
	$j("#table1").data().uiLoxiasimpletable.options.dataurl=findAreaListUrl+"?path="+encodeURI(treeNode.id);
	$j("#table1").loxiasimpletable("refresh");
}
	
function modifiedTemp(data, args, idx){
	var html='';
		
	if(modifyStatus_YES==loxia.getObject("modifyStatus", data)){
		html+='<span class="ui-pyesno ui-pyesno-yes">Y</span>';
	}else{
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
	}else{
		html+='<span class="ui-pyesno ui-pyesno-no">N</span>';
	}
		
	if(loxia.getObject("acttime", data)!=null&&loxia.getObject("acttime", data)!=''){
		html+=formatDate(loxia.getObject("acttime", data));
	}
	return html;
}

//获取日期格式
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
//		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
	}
}
	
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' class='chkcid' definitionpath='"+loxia.getObject("definitionPath", data)+"' value='"
				+ loxia.getObject("path", data) + "'/>";
}

function showRelativePath(data, args, idx){
	var path=loxia.getObject("path", data);
	var relativePath=getRelativePath(path,defaultPath);
	return relativePath;
}

function showDefRelativePath(data, args, idx){
	var path=loxia.getObject("definitionPath", data);
	var relativePath=getRelativePath(path,templateDefinitionRoot);
	return relativePath;
}

function getRelativePath(fullPath,key){
	if(fullPath==null||fullPath==""){
		return "";
	}
	var relativePath=fullPath.substring(key.length,fullPath.length);
	return relativePath;
}

function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}

function refreshTree(){
	var treeObj = tree.getTree();
	var nodes = treeObj.getSelectedNodes();
	if(nodes.length>0){
		var selectedNode=nodes[0];
		treeObj.reAsyncChildNodes(selectedNode,"refresh");
	}
	
//	var paths = param.paths.split(",");
//	for(var i=0;i<paths.length;i++){
//		var removedNode=treeObj.getNodesByParam("id",paths[i]);
//		treeObj.removeNode(removedNode[0]);
//	}
	
	
	

//	var childrenNodes=nodes[0].children;

//	if(nodes[0].getPreNode()!=null){
//		$j("#"+nodes[0].getPreNode().tId+"_span").click();
//	}else if(nodes[0].getNextNode()!=null){
//		$j("#"+nodes[0].getNextNode().tId+"_span").click();
//	}else{
//		$j("#"+nodes[0].getParentNode().tId+"_span").click();
//	}
//	treeObj.removeNode(nodes[0]);
}

function refreshTreeAfterCopy(copyToPath){
	var treeObj = tree.getTree(); 
	var parentPath=copyToPath.substring(0,copyToPath.lastIndexOf("/"));
	var parentNodes=treeObj.getNodesByParam("id",parentPath);
	
	if(parentNodes.length>0){
		var parentNode=parentNodes[0];
		treeObj.reAsyncChildNodes(parentNode,"refresh");
		$j("#"+parentNode.tId+"_span").click();
	}
//	var nodes = treeObj.getSelectedNodes();
//	if(nodes.length>0){
//		var selectedNode=nodes[0];
//		var parentNode=selectedNode.getParentNode();
//		treeObj.reAsyncChildNodes(parentNode,"refresh");
//	}
}
	
/**
* 得到选中的区域的 path组成的字符串
* @returns {String}
*/
function getSelectedPaths(){
	var data="";

    $j(".chkcid:checked").each(function(i,n){
    	if(i!=0){
            data+=",";
        }
        data+=$j(this).val();
    });
    	
    return data;
}
	
/**
 * 删除单行
 * @param data
 */
function deleteSingleArea(data){
	nps.confirm(nps.i18n("AREA_CONFIRM"),nps.i18n("AREA_SURE_TO_DELETE"),function(){
		var json={"paths":data.path};
		deleteArea(json);
	});
}

/**
 * 发送删除区域的请求
 * @param param
 */
function deleteArea(param){
	var backWarnEntity = loxia.syncXhr(deleteAreaUrl, param,{type: "GET"});
	refreshData();
	refreshTree();
	if(backWarnEntity.isSuccess){
		
    	return nps.info(nps.i18n("AREA_INFO"),nps.i18n("AREA_DEL_SUCESS"));
	}
	else{
		return nps.info(nps.i18n("AREA_INFO"),nps.i18n(backWarnEntity.exception.message));
	}

}

/**
 * 启用单行
 * @param data
 */
function enableSingleArea(data){
	if(""==data.definitionPath){
		nps.info(nps.i18n("AREA_ERROR_INFO"),nps.i18n("AREA_EFFECT_PATH_NULL",getRelativePath(data.path,defaultPath)));
		return ;
	}
	nps.confirm(nps.i18n("AREA_CONFIRM"),nps.i18n("AREA_SURE_TO_EFFECT"),function(){
		var json={"paths":data.path};
		enableArea(json);
	});
}

/**
 * 发送启用区域的请求
 * @param param
 */
function enableArea(param){
	asyncReq(effectAreaUrl,param,"AREA_EFFECT_SUCESS","AREA_EFFECT_FAIL");
}

/**
 * 禁用单独的区域
 * @param data
 */
function disableSingleArea(data){
	nps.confirm(nps.i18n("AREA_CONFIRM"),nps.i18n("AREA_SURE_TO_DELETE"),function(){
		var json={"paths":data.path};
		disableArea(json);
	});
}

/**
 * 发送禁用区域的请求
 * @param param
 */
function disableArea(param){
	asyncReq(invalidAreaUrl,param,"AREA_INVALID_SUCESS","AREA_INVALID_FAIL");
}

/**
 * 异步发送请求
 * @param url 请求url
 * @param param 请求参数
 */
function asyncReq(url,param,operSuccessKey,operSuccessFailKey){
	nps.asyncXhrGet(url, param,{successHandler:function(data, textStatus){
  		
		if (data.result=="success") {
			nps.info(nps.i18n("AREA_INFO"),nps.i18n(operSuccessKey));
		} else {
			nps.info(nps.i18n("AREA_INFO"),nps.i18n(operSuccessFailKey));
		}
		refreshData();
	}});
}

$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();

	$j("#table1").loxiasimpletable({
		page : true,
		size:10,
		nodatamessage:'<span>'+nps.i18n("AREA_DATA_NOT_FOUND")+'</span>',
		cols : [ {
			label : "<input type='checkbox'/>",
			template : "drawCheckbox",
			width:"5%"
		}, {
			name : "path",
			label : nps.i18n("AREA_PATH"),
			width:"20%",
			template : "showRelativePath"
			
		}, {
			name : "elementName",
			label : nps.i18n("AREA_NAME"),
			width:"15%"
			
		}, {
			name : "status",
			label : nps.i18n("AREA_EFFECT_STATE"),
			width:"15%",
			template : "publishedTemp",
		}, {
			name : "modifyStatus",
			label : nps.i18n("AREA_UPDATE_STATE"),
			width:"15%",
			template:"modifiedTemp"
		},  {
			name : "definitionPath",
			label : nps.i18n("AREA_TEMPLATE"),
			width:"15%",
			template:"showDefRelativePath"
		},
		{label:nps.i18n("AREA_OPERATE"),width:"10%", 
			type : "oplist",
            oplist: function(data){
                var list = [{label:nps.i18n("AREA_UPDATE"), type:"href", content:"/cms/toUpdateArea.htm?path=" + data.path},
                            {label:nps.i18n("AREA_EFFECT"), type:"jsfunc", content:"enableSingleArea"},
                            {label:nps.i18n("AREA_INVALID"), type:"jsfunc", content:"disableSingleArea"},
                            {label:nps.i18n("AREA_DELETE"), type:"jsfunc", content:"deleteSingleArea"}
                   ];
//                if(effectState!=data.status){
//                    list[1].label = nps.i18n("AREA_EFFECT");
//                    list[1].content = "enableSingleArea";
//                }
                return list;
            }
		}
		],
		dataurl : findAreaListUrl
	});
	
	refreshData();
	
	var treeId="tree"; //树的divId 在jsp中定义的  必需
	var rootName="area";//根节点的显示名称  必需
	var rootPath="/instance/area";//根节点的实际路径  必需
	
	var nodeType="cms:instance";  //构建树的节点的仓库类型
	var onClickHandler=onClick;   //点击树节点时候的响应事件， 必需
	var searchKeyId="key";		  //搜索输入框的id	
	var searchResultId="search_result";  //显示搜索结果的文本Id
	var checkEnable=false;    //节点是否能够被选择
	var chkStyle="radio";//如果可以被选择，选择的类型： checkbox radio
	var chkType = { "Y": "ps", "N": "ps" };//选择类型的参数  参见 ztree API setting.check.chkStyle 
	var selectType="cms:instance";//能够被选中的仓库类型
	var nodeLevel=0;//能够被选中的节点类型    0 父子节点均被选择    1父子节点均不被选择   2只选择父节点   3只选择子节点
	var type="get";
	
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
	}
	
	tree= new CmsTree(cmsTreeSetting);
	tree.getInit();
	
	if(tree.getTree().getNodes().length>0){
		$j("#tree_1_span").click();
	}
	
	$j(".button.copy").on("click",function(){
		
		var ztree = tree.getTree();
		var uri=null;
		var id=null;
		var isParent=null;
		var nodes_ = ztree.getSelectedNodes(true);
		var canCopy=false;
		
		var data="";
		$j(".chkcid:checked").each(function(i,n){
			if(i!=0){
				data+=",";
		    }
			data+=$j(this).val();
		    	
		});
		if(data.indexOf(",") >= 0 ){
			nps.info(nps.i18n("AREA_ERROR_INFO"),nps.i18n("AREA_MUSTBESINGLE"));
			return;
	   	}
		
		if(data==""){
			if(nodes_.length != 0){
//				$j("#defaultPath").text(defaultPath);
				var treeObj = tree.getTree();
				var nodes = treeObj.getSelectedNodes();
				var sourcePath=nodes[0].id;
				$j("#copy-source-path").val(getRelativePath(sourcePath,defaultPath));
				$j("#copy-to-path").val("/");
//				for (var i=0, l=nodes_.length; i<l; i++) {
//					uri = nodes_[i].uri ;
//					id = nodes_[i].id ;
//					isParent= nodes_[i].isParent;
//				}
//				
//				if(isParent){
//					nps.info(nps.i18n("AREA_ERROR_INFO"),nps.i18n("AREA_CANNOT_CHOOSE_PARENT"));
//					return ;
//				}
//				copyUri=defaultPath+uri;
			}else{
				nps.info(nps.i18n("AREA_ERROR_INFO"),nps.i18n("AREA_CHOOSE_NODE"));
				return ;
			}
		}else{
//			var json={"path":data};
//	    	var backWarnEntity = loxia.syncXhr(validateIsParentNodeUrl, json,{type: "GET"});
//	    	if(backWarnEntity.isSuccess){
//	    		copyUri=data;
//	    		$j("#copy-source-path").val(getRelativePath(data));
//				$j("#copy-to-path").val("/");
//	    	}
//	    	else{
//	        	nps.info(nps.i18n("AREA_ERROR_INFO"),nps.i18n("AREA_MUSTBESONAREA"));
//	        	return;
//	    	}
			copyUri=data;
    		$j("#copy-source-path").val(getRelativePath(data,defaultPath));
			$j("#copy-to-path").val("/");
		}
		
		
		$j("#copy-dialog").dialogff({type:'open',close:'in',width:'600px',height:'200px'});
	});
	
	$j(".button.orange.copyok").on("click",function(){
		var copyTo=$j("#copy-to-path").val().trim();
		var copyFrom =$j("#copy-source-path").val().trim();
		if(copyTo==""||copyTo=="/"){
			nps.info(nps.i18n("AREA_ERROR_INFO"),nps.i18n("AREA_COPY_PATH_CAN_NOT_BE_EMPTY"));
			return ;
		}
		copyTo=defaultPath+copyTo;
		copyFrom=defaultPath+copyFrom;
//		var treeObj = tree.getTree();
//		var nodes = treeObj.getSelectedNodes();
//		var sourcePath=nodes[0].id;

		if(copyTo==copyFrom){
			nps.info(nps.i18n("AREA_ERROR_INFO"),nps.i18n("AREA_MUST_DIFFERENT"));
			return ;
		}
		if(copyTo.indexOf(copyFrom+"/") >= 0 ){
			nps.info(nps.i18n("AREA_ERROR_INFO"),nps.i18n("AREA_MUST_NOTSON"));
			return ;
		}
		if(copyTo==""){
			nps.info(nps.i18n("AREA_ERROR_INFO"),nps.i18n("AREA_PATH_ISNOTNULL"));
			return ;
		}
		
		var params={"sourcePath":copyFrom,"targetPath":copyTo};
		nps.asyncXhrGet(copyAreaUrl, params,{successHandler:function(data, textStatus){
				if (data.result=="success") {
					nps.info(nps.i18n("AREA_INFO"),nps.i18n("AREA_COPY_SUCESS"));
				} else {
					nps.info(nps.i18n("AREA_INFO"),nps.i18n("AREA_COPY_FAIL"));
				}
	  		refreshData();
	  		refreshTreeAfterCopy(copyTo);
	  	}});
		
		$j("#copy-dialog").dialogff({type:'close'});
	});
	
	$j(".button.orange.copycancel").on("click",function(){
		
		$j("#copy-dialog").dialogff({type:'close'});
	});
	
	//绑定批量删除事件
	$j(".button.remove").on("click",function(){
		var data=getSelectedPaths();
    	if(data.length>0){
    		nps.confirm(nps.i18n("AREA_CONFIRM"),nps.i18n("AREA_SURE_TO_DELETE"),function(){
    			var json={"paths":data};
            	deleteArea(json);
    		});
    	}else{
    		nps.info(nps.i18n("AREA_ERROR_INFO"),nps.i18n("AREA_CHOOSE_AREA"));
    	}
	});
	
	//绑定批量生效事件
	$j(".button.enable").on("click",function(){
		var data=getSelectedPaths();
		var notAllow="";
		$j(".chkcid:checked").each(function(i,n){
			
	    	if($j(this).attr("definitionpath").trim()==""){
	    		notAllow+=getRelativePath($j(this).val(),defaultPath)+",";
	    	}
	    		
	    });
	   	if(notAllow!=""){
	   		notAllow=notAllow.substring(0,notAllow.length-1);
	   		nps.info(nps.i18n("AREA_ERROR_INFO"),nps.i18n("AREA_EFFECT_PATH_NULL",notAllow));
	   		return;
	   	}
    	if(data.length>0){
    		nps.confirm(nps.i18n("AREA_CONFIRM"),nps.i18n("AREA_SURE_TO_EFFECT"),function(){
    			var json={"paths":data};
    			enableArea(json);
    		});
    	}else{
    		nps.info(nps.i18n("AREA_ERROR_INFO"),nps.i18n("AREA_CHOOSE_AREA"));
    	}
	});
	
	//绑定批量失效事件
	$j(".button.disable").on("click",function(){
		var data=getSelectedPaths();
    	if(data.length>0){
    		nps.confirm(nps.i18n("AREA_CONFIRM"),nps.i18n("AREA_SURE_TO_INVALID"),function(){
    			var json={"paths":data};
    			disableArea(json);
    		});
    	}else{
    		nps.info(nps.i18n("AREA_ERROR_INFO"),nps.i18n("AREA_CHOOSE_AREA"));
    	}
	});
	
	//绑定批量生效事件
	$j(".button.add").on("click",function(){
		var treeObj = tree.getTree();
		var nodes = treeObj.getSelectedNodes();
		var parentNodePath=nodes[0].id;
		 var path = parentNodePath.substring(14);
		 path += "/";
		 window.location.href = addAreaPageUrl + "?parentNodePath=" + path;
	});
	
});
