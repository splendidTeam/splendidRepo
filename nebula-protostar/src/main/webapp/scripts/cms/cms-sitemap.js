$j.extend(loxia.regional['zh-CN'],{
	"CONFIRM_DELETE_PAGE":"确认删除？",
	"NPS_DELETE_SUCCESS":"删除成功",
	"NPS_DELETE_FAILURE":"该页面已经被删除",
	"PAGE_INFO_CONFIRM":"提示",
	"PAGE_TIP_NOSEL":"请选择要操作的页面",
	"PAGE_ASYNLOAD_FAILURE":"异步加载失败!",
	"PAGE_PATH":"路径",
	"PAGE_NAME":"页面名称",
	"PAGE_PUBLISH_STATUS":"发布状况",
	"PAGE_UPDATE_STATUS":"修改状况",
	"PAGE_ISVALID":"是否在有效期",
	"PAGE_TEMPLATE":"模板",
	"PAGE_OPERATING":"操作",
	"PAGE_UPDATE":"修改",
	"PAGE_PUBLISH":"发布",
	"PAGE_PUBLISHANDBACKUP":"发布并备份",
	"PAGE_REPUBLISH":"取消发布",
	"PAGE_DELETE":"删除",
	"PAGE_SELECT_COPY":"请勾选或者在左侧选择要复制的子页面结点!",
	"COMPONENT_PRECOPY_TARGET":"结点【",
	"NO_EXISTS":"】不存在,可能已被删除,无法复制",
	"PAGE_NODE_ISNOTSON":"不能选择非子页面结点!",
	"PAGE_PATH_ISNOTNULL":"名称不能为空!",
	"PAGE_VALID":"已生效：",
	"PAGE_BEINVALID":"即将生效：",
	"PAGE_ISINVALID":"已失效：",
	"PAGE_MUSTBESINGLE":"只能选择一个页面进行复制！",
	"PAGE_MUSTBESONPAGE":"你当前勾选的为父页面，请勾选子页面进行复制！",
	"PAGE_MUST_DIFFERENT":"复制到的节点不能和源节点相同",
	"PAGE_MUST_NOTSON":"目标结点不能是源结点的子结点",
	"PAGE_FIND":" 共找到",
	"PAGE_RESULT":"个结果",
	"CONFIRM_PUBLISH_PAGE":"确认发布？",
	"NPS_PUBLISH_SUCCESS":"发布成功",
	"NPS_PUBLISH_AND_BACKUP_SUCCESS":"发布成功,备份成功",
	"NPS_PUBLISH_ISNOT_ALLOW":"该页面没有选择模板，不能发布",
	"NPS_PUBLISH_PATH_L":"路径为【",
	"NPS_PUBLISH_PATH_R":"】的页面没有选择模板，不能发布",
	"CONFIRM_UNINSTALL_PAGE":"确认取消发布？",
	"NPS_UNINSTALL_SUCCESS":"取消发布成功",
	"NPS_UNINSTALL_FAILURE":"取消发布失败",
	"PAGE_PATH_ERROR":"路径错误",
	"PAGE_PATH_UPDATE":"路径必须用/开头表示在page目录结构下",
	"PAGE_COPY_SUCCESS":"复制成功",
	"TIME_ISNULL":"未设置有效时间段",
	"EFF_TIME_ISNULL":"未设置有效开始时间",
	"VALID_TIME_ISNULL":"未设置有效结束时间"
});
var tabJson ="/cms/findPageListForTable.json";
var searchPageUrl =base+tabJson+"?path=/instance/page";
var deletePageUrl = base+'/cms/deletePageInstance.json';
var copyPageUrl = base+'/cms/copyPageIntance.json';
var validateIsParentNodeUrl=base+'/cms/validateIsParentNode.json';
var uninstallPageUrl = base+'/cms/uninstallPage.json';
var publishPageUrl = base+'/cms/publishPage.json';
var pageRoot = "/instance/page";
var templateDefinitionRoot = "/definition";
var checkNodePathIsExistUrl = base +"/common/checkNodeIsExists.json";
var copyUri;
var log, className = "dark";

function onClick(event, treeId, treeNode)  {
 	 searchPageUrl=base+tabJson+"?path="+treeNode.path;
 	 
 	 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
 	 $j("#table1").data().uiLoxiasimpletable.options.dataurl=searchPageUrl;
	 refreshData();
}
//path截取
function showRelativePath(data, args, idx){
	var path=loxia.getObject("path", data);
	var relativePath=getRelativePath(path,pageRoot);
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
var key;

/**树结束**/


$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();

	
	
	 $j("#table1").loxiasimpletable({
	    	nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
	    	page : true,
			size : 10,
			form:"searchForm",
			cols:
			[
				{label:"<input type='checkbox'/>",width:"4%", template:"drawCheckbox",},
				{name:"path",label:nps.i18n("PAGE_PATH"),width:"18%",template : "showRelativePath"},
				{name:"elementName",label:nps.i18n("PAGE_NAME"),width:"12%",},
				{name:"status",label:nps.i18n("PAGE_PUBLISH_STATUS"),width:"10%",template : "publishedTemp",},
				{name:"modifyStatus",label:nps.i18n("PAGE_UPDATE_STATUS"),width:"10%",template:"modifiedTemp",},
				{name:"status",label:nps.i18n("PAGE_ISVALID"),width:"8%",template:"threeState",},
				{name:"definitionPath",label:nps.i18n("PAGE_TEMPLATE"),width:"30%",template:"showDefRelativePath"},
				{label:nps.i18n("PAGE_OPERATING"),width:"8%", 
					type : "oplist",
		            oplist: function(data){
		                var list = [{label:nps.i18n("PAGE_UPDATE"), type:"href", content:"/cms/toUpdatePage.htm?path="+data.path},
		                            {label:nps.i18n("PAGE_PUBLISH"), type:"jsfunc", content:"publishPage"},
		                            {label:nps.i18n("PAGE_PUBLISHANDBACKUP"), type:"jsfunc", content:"publishAndBackUpPage"},
		                            {label:nps.i18n("PAGE_REPUBLISH"), type:"jsfunc", content:"uninstallPage"},
		                    		{label:nps.i18n("PAGE_DELETE"), type:"jsfunc", content:"deletePage"}
		                   ];
		                if(!data.published){
		                    //list[1].label = "发布";
		                    //list[1].content = "enableProp";
		                }
		                return list;
		            }
				}
			],
			dataurl :searchPageUrl
		});
	
	refreshData();
	
	var treeId="tree"; //树的divId 在jsp中定义的  必需
	var rootName="page";//根节点的显示名称  必需
	var rootPath="/instance/page";//根节点的实际路径  必需
	
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
    //复制
	$j(".button.copy").on("click",function(){
		
		  var data="";
		   	$j(".checkId:checked").each(function(i,n){
		    	if(i!=0){
		        	data+=",";
		        	}
		    	data+=$j(this).val();
		    	
		    });
		   	if(data.indexOf(",") >= 0 ){
	   			nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("PAGE_MUSTBESINGLE"));
	   			return;
	   		}
			 if(data==""){
				    var ztree = $j.fn.zTree.getZTreeObj("tree");
					var uri=null;
					//var isParent=null;
					var nodes_ = ztree.getSelectedNodes(true);
					if(nodes_.length == 0){
						nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("PAGE_SELECT_COPY"));
						return ;
					}else{
						//取到选择的行业名称
						for (var i=0, l=nodes_.length; i<l; i++) {
							uri = nodes_[i].path ;
							//isParent= nodes_[i].isParent;
						}
						/**
						if(isParent){
							nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("PAGE_NODE_ISNOTSON"));
							return ;
						}
						**/
						copyUri=getRelativePath(uri,pageRoot);
						$j("#copy-source-path").val(copyUri);
						
					}
			 }else{
		    	/**var json={"path":data};
		    	var backWarnEntity = loxia.syncXhr(validateIsParentNodeUrl, json,{type: "GET"});
		    	if(backWarnEntity.isSuccess){
		    		copyUri=data;
		    		$j("#copy-source-path").val(data);
		    	}
		    	else{
		        	nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("PAGE_MUSTBESONPAGE"));
		        	return;
		    	}
		    	***/
		    	copyUri=getRelativePath(data,pageRoot);
	    		$j("#copy-source-path").val(copyUri);
			 }
		
			 $j("#copy-dialog").dialogff({type:'open',close:'in',width:'600px',height:'200px'});
	});
	//新增
	 $j(".button.orange.add").click(function(){
		    var ztree = $j.fn.zTree.getZTreeObj("tree");
			var node_ = ztree.getSelectedNodes(true);
			var parentPath="";
			if(node_.length > 0){
				parentPath=node_[0].path+"/";
			}
			
			window.location.href=base+"/cms/toCreatePage.htm?parentPath="+parentPath;
			
		   // window.location.href=base+"/cms/toCreatePage.htm";
    });  
	//删除
	$j(".button.remove").click(function(){
		    var data="";
		   	$j(".checkId:checked").each(function(i,n){
		    	if(i!=0){
		        	data+=",";
		        	}
		    	var path=$j(this).val();
		    	data+=path;
		    	
		    });
			 if(data==""){
			    		nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("PAGE_TIP_NOSEL"));
			    		return ;
			    }
		    nps.confirm(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("CONFIRM_DELETE_PAGE"), function(){
		    	var json={"paths":data};
		    	var backWarnEntity = loxia.syncXhr(deletePageUrl, json,{type: "GET"});
		    	if(backWarnEntity.isSuccess){
		        	nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("NPS_DELETE_SUCCESS"));
		    	}
		    	else{
		    	    nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n(backWarnEntity.exception.message));
		    	}
		    	refreshData();
	        	refreshTree();		
		    });
	
	});
	//发布
	$j(".button.publish").click(function(){
		    var data="";
		    var notAllow="";
		   	$j(".checkId:checked").each(function(i,n){
		    	if(i!=0){
		        	data+=",";
		        	}
		    	data+=$j(this).val();
		    	
		    	if($j(this).attr("tempValue")=="")
		    		notAllow+=$j(this).val()+"，";
		    		
		    });
		   	if(notAllow!=""){
		   		nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("NPS_PUBLISH_PATH_L")+notAllow.substring(0, notAllow.length-1)+nps.i18n("NPS_PUBLISH_PATH_R"));
		   		return;
		   	}
			 if(data==""){
			    		nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("PAGE_TIP_NOSEL"));
			    		return ;
			    }
			var isBackUp="F";
		    nps.confirm(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("CONFIRM_PUBLISH_PAGE"), function(){
		    	var json={"paths":data,"isBackUp":isBackUp};
		    	var backWarnEntity = loxia.syncXhr(publishPageUrl, json,{type: "GET"});
		    	if(backWarnEntity.isSuccess){
		        	nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("NPS_PUBLISH_SUCCESS"));
		        	refreshData();
		    	}else{
		    		return nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n(backWarnEntity.exception.message));
		    	}
		    });  
	
	});
	//发布并备份
	$j(".button.publishAndBackUp").click(function(){
		    var data="";
		    var notAllow="";
		   	$j(".checkId:checked").each(function(i,n){
		    	if(i!=0){
		        	data+=",";
		        	}
		    	data+=$j(this).val();
		    	
		    	if($j(this).attr("tempValue")=="")
		    		notAllow+=$j(this).val()+"，";
		    		
		    });
		   	if(notAllow!=""){
		   		nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("NPS_PUBLISH_PATH_L")+notAllow.substring(0, notAllow.length-1)+nps.i18n("NPS_PUBLISH_PATH_R"));
		   		return;
		   	}
			if(data==""){
			    		nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("PAGE_TIP_NOSEL"));
			    		return ;
			    }
			var isBackUp="T";
		    nps.confirm(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("CONFIRM_PUBLISH_PAGE"), function(){
		    	var json={"paths":data,"isBackUp":isBackUp};
		    	var backWarnEntity = loxia.syncXhr(publishPageUrl, json,{type: "GET"});
		    	if(backWarnEntity.isSuccess){
		        	nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("NPS_PUBLISH_AND_BACKUP_SUCCESS"));
		        	refreshData();
		    	}else{
		    		return nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n(backWarnEntity.exception.message));
		    	}
		    });  
	
	});
	
	//取消发布
	$j(".button.republish").click(function(){
		    var data="";
		   	$j(".checkId:checked").each(function(i,n){
		    	if(i!=0){
		        	data+=",";
		        	}
		    	data+=$j(this).val();
		    	
		    });
			 if(data==""){
			    		nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("PAGE_TIP_NOSEL"));
			    		return ;
			    }
		    nps.confirm(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("CONFIRM_UNINSTALL_PAGE"), function(){
		    	var json={"paths":data};
		    	var backWarnEntity = loxia.syncXhr(uninstallPageUrl, json,{type: "GET"});
		    	if(backWarnEntity.isSuccess){
		        	nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("NPS_UNINSTALL_SUCCESS"));
		        	refreshData();   	        	
		    	}
		    	else{
		    		return nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n(backWarnEntity.exception.message));
		    	}
		    });
	
	});
	//复制弹出框操作
	$j(".button.orange.copyok").on("click",function(){
		var copyTo=$j("#copy-to-path").val().trim();
		var copyFrom =$j("#copy-source-path").val().trim();
		
		if(copyTo==copyFrom){
			nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("PAGE_MUST_DIFFERENT"));
			return ;
		}
		if(copyTo.indexOf(copyFrom+"/") >= 0 ){
			nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("PAGE_MUST_NOTSON"));
			return ;
		}
		if(copyTo.charAt(0)!="/"){
			$j("#copy-to-path").val("/"+copyTo);
			nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("PAGE_PATH_UPDATE"));
			return;
		}
		if(copyTo=="/"){
			nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("PAGE_PATH_ISNOTNULL"));
			return ;
		}
		if(copyTo.indexOf("//") >= 0){
			nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("PAGE_PATH_ERROR"));
			return ;
		}
		
		var checkJson1 ={"path":pageRoot+copyUri};
		var _result1 = loxia.syncXhr(checkNodePathIsExistUrl, checkJson1,{type: "GET"});
		if(!_result1){
			return nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("COMPONENT_PRECOPY_TARGET")+copyUri+nps.i18n("NO_EXISTS"));
		}
		
		var checkJson ={"path":pageRoot+copyTo.substring(0,copyTo.lastIndexOf("/"))};
		var _result = loxia.syncXhr(checkNodePathIsExistUrl, checkJson,{type: "GET"});
		if(!_result){
			return nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("COMPONENT_PRECOPY_TARGET")+copyTo.substring(0,copyTo.lastIndexOf("/"))+nps.i18n("NO_EXISTS"));
		}
		
		var json={"sourcePath":copyUri,"targetPath":copyTo};
  		var backWarnEntity = loxia.syncXhr(copyPageUrl, json,{type: "GET"});
  		if(backWarnEntity.isSuccess){
  			nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("PAGE_COPY_SUCCESS"));
  			$j("#copy-dialog").dialogff({type:'close'});
  			refreshTree();
  			refreshData();
  		}else{
  			return nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n(backWarnEntity.exception.message));
  		}
		
	});
	
	$j(".button.orange.copycancel").on("click",function(){
		
		$j("#copy-dialog").dialogff({type:'close'});
	});
});
//单条删除
function deletePage(data){
    nps.confirm(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("CONFIRM_DELETE_PAGE"), function(){
    	var json={"paths":data.path};
    	var backWarnEntity = loxia.syncXhr(deletePageUrl, json,{type: "GET"});
    	if(backWarnEntity.isSuccess){
        	nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("NPS_DELETE_SUCCESS"));
    	}else{
    		nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("NPS_DELETE_FAILURE"));
    	}
    	refreshData();
    	refreshTree();	
    });
}
//单条发布
function publishPage(data){
	if(data.definitionPath==""){
		nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("NPS_PUBLISH_ISNOT_ALLOW"));
		return;
	}
    nps.confirm(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("CONFIRM_PUBLISH_PAGE"), function(){
    	var json={"paths":data.path,"isBackUp":"F"};
    	var backWarnEntity = loxia.syncXhr(publishPageUrl, json,{type: "GET"});
    	if(backWarnEntity.isSuccess){
        	nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("NPS_PUBLISH_SUCCESS"));
        	refreshData();
    	}else{
    		return nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n(backWarnEntity.exception.message));
    	}
    });
}
//单条发布并备份
function publishAndBackUpPage(data){
	if(data.definitionPath==""){
		nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("NPS_PUBLISH_ISNOT_ALLOW"));
		return;
	}
    nps.confirm(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("CONFIRM_PUBLISH_PAGE"), function(){
    	var json={"paths":data.path,"isBackUp":"T"};
    	var backWarnEntity = loxia.syncXhr(publishPageUrl, json,{type: "GET"});
    	if(backWarnEntity.isSuccess){
        	nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("NPS_PUBLISH_AND_BACKUP_SUCCESS"));
        	refreshData();
    	}
    	else{
    		return nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n(backWarnEntity.exception.message));
    	}
    });
}

//单条取消发布
function uninstallPage(data){
    nps.confirm(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("CONFIRM_UNINSTALL_PAGE"), function(){
    	var json={"paths":data.path};
    	var backWarnEntity = loxia.syncXhr(uninstallPageUrl, json,{type: "GET"});
    	if(backWarnEntity.isSuccess){
        	nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n("NPS_UNINSTALL_SUCCESS"));
        	refreshData();
    	}
    	else{
    		return nps.info(nps.i18n("PAGE_INFO_CONFIRM"),nps.i18n(backWarnEntity.exception.message));
    	}
    });
}
//修改状况
function modifiedTemp(data, args, idx){
	var html='';
	//0为未修改 1为已修改
	if(loxia.getObject("modifyStatus", data)==0){
		html+='<span class="ui-pyesno ui-pyesno-no">N</span>';
	}else{
		html+='<span class="ui-pyesno ui-pyesno-yes">Y</span>';
	}
	if(loxia.getObject("modifytime", data)!=null&&loxia.getObject("modifytime", data)!=''){
		//发布时间
		var modify = new Date(parseInt(loxia.getObject("modifytime", data)));
		html+= formatDate(modify);
	}
	
	return html;
} 
//发布状况
function publishedTemp(data, args, idx){
	var html='';
	//1为未发布 2为已发布
	if(loxia.getObject("status", data)==1){
		html+='<span class="ui-pyesno ui-pyesno-no">N</span>';
	}else{
		html+='<span class="ui-pyesno ui-pyesno-yes">Y</span>';
	}
	if(loxia.getObject("acttime", data)!=null&&loxia.getObject("acttime", data)!=''){
		//发布时间
		var act = new Date(parseInt(loxia.getObject("acttime", data)));
		html+= formatDate(act) ;
	}
	
	return html;
}
//是否在有效期
function threeState(data, args, idx){
	var now = new Date();
	//生效时间
	var effStr ="";
	if(loxia.getObject("effectTime", data)!=null&&loxia.getObject("effectTime", data)!=''){
		var eff = new Date(parseInt(loxia.getObject("effectTime", data)));
		effStr = formatDate(eff);
	}
	//失效时间
	var invaStr ="";
	if(loxia.getObject("invalidTime", data)!=null&&loxia.getObject("invalidTime", data)!=''){
		var inva = new Date(parseInt(loxia.getObject("invalidTime", data)));
		invaStr = formatDate(inva) ;
	}
	if(effStr=="" && invaStr==""){
		return "<span class='ui-pyesno ui-pyesno-no' title='"+nps.i18n("TIME_ISNULL")+"'></span>";
	}else if(effStr==""){
		return "<span class='ui-pyesno ui-pyesno-no' title='"+nps.i18n("EFF_TIME_ISNULL")+"'></span>";
	}else if(invaStr==""){
		return "<span class='ui-pyesno ui-pyesno-no' title='"+nps.i18n("VALID_TIME_ISNULL")+"'></span>";
	}else if(loxia.getObject("effectTime", data) <=now && now<= loxia.getObject("invalidTime", data)){
		return "<span class='ui-pyesno ui-pyesno-yes' title='"+nps.i18n("PAGE_VALID")+effStr+" To "+invaStr+"'></span>";
	}else if(loxia.getObject("effectTime", data) >now){
		return "<span class='ui-pyesno ui-pyesno-wait' title='"+nps.i18n("PAGE_BEINVALID")+effStr+" To "+invaStr+"'></span>";
	}else if(now> loxia.getObject("invalidTime", data)){
		return "<span class='ui-pyesno ui-pyesno-no' title='"+nps.i18n("PAGE_ISINVALID")+effStr+" To "+invaStr+"'></span>";
	}
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
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' class='checkId' name='id' tempValue='"+ loxia.getObject("definitionPath", data) + "' value='"+ loxia.getObject("path", data) + "'/>";
}
//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}
//刷新树
function refreshTree(){
	var treeObj = $j.fn.zTree.getZTreeObj("tree");
	//根节点
	var node = treeObj.getNodeByTId("tree_1");
	treeObj.reAsyncChildNodes(node,"refresh");
}
