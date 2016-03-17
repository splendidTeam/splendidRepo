$j.extend(loxia.regional['zh-CN'], {
	"AREA_MANAGER_TIPS":"友情提示：",
	"AREA.ADD.AREANAME.DISABALE":"区域path不可用",
	"AREA.ADD.AREANAME.ENALE":"区域path可用",
	"AREA.UPDATE.SUCCESS":"更新区域成功",
	"AREA.UPDATE.FAILURE":"更新区域失败",
	"AREA.EDIT.SUCCESS":"编辑区域成功",
	"AREA.EDIT.FAILURE":"编辑区域失败",
	"PATH_EXISTS":"区域path已存在",
	"AREA.ADD.MSG":"请先保存基本信息",
	"AREA.EDIT.PROPERTY.SUCCESS":"更新实例动态属性成功",
	"AREA.EDIT.PROPERTY.FAILURE":"更新实例动态属性失败",
	"LOCKED_MESSAGE":"锁定区域不能新增或者修改",
	"PAGE_AREA_EMPTY":"区域或者组件路径不能为空",
	"AREA.DELETE.TIP":"确定删除吗?",
	"AREA_NOT_UPDATE_PATH":"Path不能被修改",
	"AREA_NOT_UPDATE_TEMPLATE":"模版不能被修改",
	"AREA_MANAGER_SAVE_MSG":"保存基本信息时未选择模版路径，不能编辑其它信息",
	"AREA_MANAGER_SAVE_MSG_NOT_PREVIEW":"保存基本信息时未选择模版路径，不能预览",
	"AREA_MANAGER_SAVE_MSG_NOT_VIEW":"保存基本信息时未选择模版路径，不能查看模版",
	"AREA_TEMPLATE_SEL":"请选择模版",
	"TEMPLATE_PATH_DISABLE" : "模版路径不可用",
	"AREA_PATH_DISABLE":"区域路径不可用",
	"COMP_PATH_DISABLE":"组件路径不可用",
	"PATH_DISABLE" : "路径不可用",
	"AREA.SAVE.TIP.MSG":"选择此模板后模板信息不可修改，是否继续保存"
		
});

//验证path URL
var validateAreaNameUrl = base +  "/cms/validateAreaName.json";

//跳转到area列表页面
var toAreaUrl = base + "/cms/toArea.htm";

//获取模版的区域信息
var getTemplateAreasInfoUrlUrl = base + "/cms/getTemplateAreasInfo.json";

//更新动态属性
var updateAreaDynamicProperty = base + "/cms/updateAreaDynamicProperty.json";

//获取模版的动态属性字段
var getDynamicPropertyFieldsUrl = base +'/cms/getAreaTemplateFields.json';

//预览模版
var previewAreaUrl = base + '/cms/previewArea.htm';

//查看模板
var viewTemplateUrl = base +'/cms/viewInstanceTemplate.htm';

//校验模板
var validateTemplateUrl = base +'/cms/validateInstanceTemplateByPath.json';

var validatePreviewAreaPathUrl = base + '/cms/validatePreviewAreaPath.json';

//验证区域或组件路径
var validateAreaOrCompPathsUrl =  base+"/cms/checkElementBlockPathExists.json";
//验证模版路径
var validateDefinitionPathUrl = '/cms/validateAreaDefinitionPath.json';

//获取子属性
var getChildPropertyUrl=base+"/cms/findDialogFieldsByPath.json";

//获取模版的类型
var  getAreaTemplateTypeUrl = base+"/cms/getAreaTemplateType.json";

var definitonPathPrefix = '/definition';
var curSelTreeObj = null;
var isSave = false;	
var saveCount = 0;
var inputPropertyNode=null;
var index=0;

/**模版配置开始**/
var treeId="tempTree"; //树的divId 在jsp中定义的  必需
var rootName="ROOT";//根节点的显示名称  必需
var rootPath="/definition/area";//根节点的实际路径  必需
var nodeType="nt:unstructured,cms:definitions";  //构建树的节点的仓库类型
var onClickHandler=onClickTemp;   //点击树节点时候的响应事件， 必需
var searchKeyId="key";		  //搜索输入框的id	
var checkEnable=true;    //节点是否能够被选择
var chkStyle="radio";//如果可以被选择，选择的类型： checkbox radio
var chkType = { "Y": "ps", "N": "ps" };//选择类型的参数  参见 ztree API setting.check.chkStyle 
var selectType="cms:definitions";//能够被选中的仓库类型
var nodeLevel=3;//能够被选中的节点类型    0 父子节点均被选择    1父子节点均不被选择   2只选择父节点   3只选择子节点
var type="get";

var cmsTreeSetting={
		"treeId":treeId,
		"rootName":rootName,
		"rootPath":rootPath,
		"nodeType":nodeType,
		"onClickHandler":onClickHandler,
		"checkEnable":checkEnable,
		"chkStyle":chkStyle,
		"chkType":chkType,
		"selectType":selectType,
		"nodeLevel":nodeLevel
}
/**模版配置结束**/

/**区域或组件树配置开始**/
var areaOrcompTreeId = "areaOrComp"; //树的divId 在jsp中定义的  必需
var areaOrcompRootName = "ROOT";//根节点的显示名称  必需
var areaOrcompRootPath = "/instance";//根节点的实际路径  必需
var areaOrcompNodeType = "cms:instance";  //构建树的节点的仓库类型
var areaOrcompOnClickHandler = onClickTemp;   //点击树节点时候的响应事件， 必需
var areaOrcompCheckEnable = true;    //节点是否能够被选择
var areaOrcompChkStyle = "radio";//如果可以被选择，选择的类型： checkbox radio
var areaOrcompChkType = "all";//选择类型的参数  参见 ztree API setting.check.chkStyle 
var areaOrcompSelectType = "cms:instance";//能够被选中的仓库类型
var areaOrcompCNodeLevel = 3;//能够被选中的节点类型    0 父子节点均被选择    1父子节点均不被选择   2只选择父节点   3只选择子节点
var areaOrcompExcludePath = "/instance/page";


/**区域或组件树配置结束**/

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

function validateTemplatePath(value,obj){
	if($j.trim(value).length > 0){
		if((value == '/' && value.length == 1) || value.indexOf("/")!=0 ||
				(value.length > 1 && value.substring(value.length-1,value.length)=="/")){
			return nps.i18n("TEMPLATE_PATH_DISABLE");
		}
		var json = {"definitionPath" : (definitonPathPrefix + value)};
		var _d = loxia.syncXhr(validateDefinitionPathUrl,json,{type: "GET"});
		if(!_d.isSuccess){
			return nps.i18n("TEMPLATE_PATH_DISABLE");
		}
	}
	return loxia.SUCCESS;
}

function onClick(event, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("areaOrComp");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
	// refreshData();
}

//验证数组是否有空变红色边框
function validateEmpty(arrays){
	for(var i = 0;i < arrays.length; i++){
		if($j.trim($j(arrays[i]).val()) == ''){
			$j((arrays[i])).attr("class","ui-loxia-default ui-corner-all ui-loxia-highlight ui-loxia-error");
		}
	}
}

function validatePath(value, obj){
	if((value == '/' && value.length == 1) || value.length == 0 || value.indexOf("/")!=0 ||
			(value.length > 1 && value.substring(value.length-1,value.length)=="/")){
			return nps.i18n("PATH_DISABLE");
	}
	 var json = {"elementBlockPath" : value};
 	 var _d = loxia.syncXhr(validateAreaOrCompPathsUrl, json,{type: "GET"});
 	 if(_d.isSuccess){
 		//不存在
 		var areaPatter = new RegExp("/area/");
 		var compPatter = new RegExp("/component/");
 	 	if(areaPatter.test(value)){
 	 		return nps.i18n("AREA_PATH_DISABLE");
 	 	}else if(compPatter.test(value)){
 	 		return nps.i18n("COMP_PATH_DISABLE");
 	 	}else{
 	 		return nps.i18n("PATH_DISABLE");
 	 	}
 	 }
 	 return loxia.SUCCESS;
}

//验证单元素是否有空变红色边框
function validateElementEmpty(element){
	if($j.trim($j(element).val()) == ''){
		$j(element).attr("class","ui-loxia-default ui-corner-all ui-loxia-highlight ui-loxia-error");
	}
}

function filter(treeId, parentNode, childNodes) { 
	if (!childNodes) return null; 
	for (var i=0, l=childNodes.length; i<l; i++) { 
	childNodes[i].name = childNodes[i].name.replace('',''); 
	} 
	return childNodes; 
}

function onClickTemp(event, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("tempTree");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
	// refreshData();
}

/**
 * 验证是否重复
 * 返回true表示重复
 */
function checkPathExists(){
    var parentNodePath = $j("#parentNodePath").val();
	 var path = $j("#name").val();
	 if(path.trim()=="")return;
	 var fullPath = parentNodePath + path;
	 var json={"path":fullPath};
 	 var _d = loxia.syncXhr(validateAreaNameUrl, json,{type: "GET"});
 	 return _d.isSuccess;
}

function userFormValidate(form) {
	console.log("xxx");
	return loxia.SUCCESS;
}

function refreshAreaOrCompTree() {
	var ztree = $j.fn.zTree.getZTreeObj("areaOrComp");
	ztree.refresh(); // 重置树
}
function checkStrIsNotNull(str) {
	if (str != null && str.trim() != "") {
		return true;
	} else {
		return false;
	}
}

$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	
    //预览
    $j(".button.preview").click(function(){
    	var parentNodePath = $j("#parentNodePath").val();
		var path = $j("#name").val();
		var fullPath = parentNodePath + path;
    	var definitionPath = $j("#definitionPath").val();
    	if(!checkStrIsNotNull(definitionPath) || !isSave){
    		return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("AREA_MANAGER_SAVE_MSG_NOT_PREVIEW"));
    	}
     	var validatedata = {"path":fullPath};
    	var data = loxia.syncXhr(validatePreviewAreaPathUrl,validatedata,{type: "GET"});
    	if(data.isSuccess){
    		//模板属性
    		var getTypeJson ={"definitionPath": definitonPathPrefix + definitionPath};
    		var type = loxia.syncXhr(getAreaTemplateTypeUrl,getTypeJson,{type: "GET"});
    		
    		//动态
    		if(type==2){
    			$j("#dynamicPath").val(fullPath);
    			$j("#dynamicTemplateParam-dialog").dialogff({type:'open',close:'in',width:'600px',height:'300px'});
    		}else{
    			loxia.openPage(previewAreaUrl + "?path=" + fullPath);
    		}
    	}else{
    		return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n(data.exception.message));
    	}
    });
    
    //
	 $j("#dynamicTemplateParam-dialog").on("click","input.button.orange.dynamicTplateSelCancel",function(){	    	
		   $j("#dynamicTemplateParam-dialog").dialogff({type:'close'});
	    });
	 
	 
	 //
	 $j("#dynamicTemplateParam-dialog").on("click","input.button.orange.dynamicTplateSelok",function(){	 
		  $j("#dynamicTemplateParam-dialog").dialogff({type:'close'});
		  var tempPreviewAreaUrl = previewAreaUrl;
		  //解析textarea
		  //解析textarea
		  var params =$j("#dynamic-params").val().trim();
		  tempPreviewAreaUrl += "?path="+$j("#dynamicPath").val();
		  if(checkStrIsNotNull(params)){
			  tempPreviewAreaUrl += "&"+params;
		  }
		  loxia.openPage(tempPreviewAreaUrl);
		  
	    });
	 
    //查看模板
    $j(".button.viewTemplate").click(function(){
    	var parentNodePath = $j("#parentNodePath").val();
		var path = $j("#name").val();
		var fullPath = parentNodePath + path;
    	var tempViewTemplateUrl="";
    	var definitionPath = $j("#definitionPath").val();
    	if(!checkStrIsNotNull(definitionPath) || !isSave){
    		return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("AREA_MANAGER_SAVE_MSG_NOT_VIEW"));
    	}	
		var validateVmJson ={"path":fullPath};
		var vmFileIsExist = loxia.syncXhr(validateTemplateUrl,validateVmJson,{type: "GET"});
		if(vmFileIsExist.isSuccess){
			tempViewTemplateUrl = viewTemplateUrl +'?path='+fullPath;
        	loxia.openPage(tempViewTemplateUrl);
		}else{
			return nps.info(nps.i18n("AREA_MANAGER_TIPS"),vmFileIsExist.exception.message);
		}
    	
    });
	
	 //提示不能被修改
	 
	 $j("#name").on("click",function(){
			 $j("#loxiaTip-r").show();
				 $j("#parentPathMessage").html(nps.i18n("AREA_NOT_UPDATE_PATH"));
				 setTimeout(function(){ 
					$j("#loxiaTip-r").hide();
				 },2000); 	
			
		});
	//提示不能被修改
	 $j(".fLeft.NoEdit").on("click",function(){
		 $j("#loxiaTip-r1").show();
			 $j("#definitionPathMessge").html(nps.i18n("AREA_NOT_UPDATE_TEMPLATE"));
			 setTimeout(function(){ 
				$j("#loxiaTip-r1").hide();
			 },2000); 	
		
	});
	 
	$j(".button.orange.selok").on("click", function() {
		var ztree = $j.fn.zTree.getZTreeObj("areaOrComp");
		var name = null;
		var pid = null;
		var id = null;
		var uri = null;
		var nodes_ = ztree.getCheckedNodes(true);
		if (nodes_.length == 0) {
			nps.info("错误提示：", "请选择区域或组件!");
			return false;
		} else {
			// 取到选择的行业名称
			for ( var i = 0, l = nodes_.length; i < l; i++) {
				id = nodes_[i].id;
				pid = nodes_[i].pId;
				name = nodes_[i].name;
				uri = pid.substring("/instance".length,id.length) + "/" + name;
			}
			var inputObj = curSelTreeObj.parent().children().eq(0);
			inputObj.val(uri);
			inputObj.focus();
//			inputObj.blur();
			inputObj.attr("class","ui-loxia-default ui-corner-all");
		}

		$j("#areaOrcomp-dialog").dialogff({
			type : 'close'
		});

	});

	$j(".button.orange.selcancel").on("click", function() {

		$j("#areaOrcomp-dialog").dialogff({
			type : 'close'
		});

		refreshAreaOrCompTree();
	});

	
    //新增locked提示
	 $j("#editArea").on("click","div.new-list-add.locked",function(){
		 return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("LOCKED_MESSAGE"));
	 });
	 
	 $j("#editArea").on("click","span.common-ic.delete-ic.locked",function(){
		 return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("LOCKED_MESSAGE"));
	 });
	 
	  //选择locked违规提示
	 $j("#editArea").on("click","span.findArea1",function(){
	    	 return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("LOCKED_MESSAGE"));
	 });
		
	    
	$j(".button.orange.tempselok").on("click", function() {
		var ztree = $j.fn.zTree.getZTreeObj("tempTree");
		var name = null;
		var pid = null;
		var id = null;
		var uri = null;
		var nodes_ = ztree.getCheckedNodes(true);
		if (nodes_.length == 0) {
			nps.info("错误提示：", "请选择模板!");
			return false;
		} else {
			// 取到选择的行业名称
			for ( var i = 0, l = nodes_.length; i < l; i++) {
				id = nodes_[i].id;
				pid = nodes_[i].pId;
				name = nodes_[i].name;
				uri = pid.substring(definitonPathPrefix.length,pid.length) + "/" + name;
			}

			var inputObj = curSelTreeObj.parent().children().eq(0);
			inputObj.val(uri);
			inputObj.focus();
//			inputObj.blur();
			inputObj.attr("class","ui-loxia-default ui-corner-all ui-loxia-highlight");
		}

		$j("#template-dialog").dialogff({
			type : 'close'
		});

	});

	$j(".button.orange.tempselcancel").on("click", function() {
		$j("#template-dialog").dialogff({
			type : 'close'
		});

	});

});
//根据dialogPath 获取绘制编辑属性tag页
function drawDynamicProperty(definitionPath,url){
	
	var json ={"definitionPath":definitionPath};
	if(checkStrIsNotNull(definitionPath)){
		
		loxia.asyncXhr(url, json,{type: "GET",
    		success: function(data){    			
    			
    			var dialogFiledArray = eval(data);//转换json格式
    			var propertyHtml="";
    			var rowFiled="";
    			var startTag = "<div class='ui-block-line'>";
    			var endTag = "</div></div>";
    			if(dialogFiledArray.length > 0){
	    			for(var i=0 ;i<dialogFiledArray.length;i++){
	    				var mandatoryValue = false;
	    				if(null!=dialogFiledArray[i].required&&dialogFiledArray[i].required){
	    					mandatoryValue =true;
	    				}
	    				propertyHtml += startTag;
	    				if(dialogFiledArray[i].type == '2' || dialogFiledArray[i].type == '3'){
	    					rowFiled=
						         "<label>"+dialogFiledArray[i].fieldName+"</label>"
						         +"<div>"
						              +"<input onkeydown='return false' type='text'  name='propertyValues'" +
						              " loxiaType='input' " +
						             " placeholder='" + (mandatoryValue == true ? '该属性为必填' : '') + "' "
						              + " mandatory='" +mandatoryValue+ 
						              		"'/>";
	    				}else{
	    					rowFiled=
						         "<label>"+dialogFiledArray[i].fieldName+"</label>"
						         +"<div>"
						              +"<input type='text'  name='propertyValues'" +
						              " loxiaType='"+(dialogFiledArray[i].editer=='date'?'date':'input')+ "' " +
						              " placeholder='" + (mandatoryValue == true ? '该属性为必填' : '') + "' "
						              + " mandatory='" +mandatoryValue+ 
					              		"'/>";
	    				}
	    				rowFiled +=  "<input type='hidden' loxiaType='input' name='propertyCodes' value='" +
	              		dialogFiledArray[i].code
			              +"' mandatory='" +mandatoryValue+
		              		"'/>";
	    				propertyHtml+=rowFiled;
	    				if(dialogFiledArray[i].type == '2' || dialogFiledArray[i].type == '3'){
	    					propertyHtml+="<a  class='func-button ml5' href='javascript:void(0)'> 编辑</a><span id='childproperyPath'"+i+" style='display:none'>"+dialogFiledArray[i].path+"</span><span id='childproperyType'"+i+" style='display:none'>"+dialogFiledArray[i].type+"</span>";
	    				}
	       				propertyHtml += endTag;
	    			}
    			}else{
    				propertyHtml = "未找到数据";
    			}
    			$j("#dialogFields").html(propertyHtml);
    			
    			$j("#dialogFields").find("[loxiaType]").each(function(){
    				loxia.initLoxiaWidget(this);
    			});
    			
    		}	
    	});
		
	}
}

function addEvent(){
	$j(".ui-tag-change").each(function(){
		if(!$j(this).find(".tag-change-ul").find("li").hasClass("selected")){
			$j(this).find(".tag-change-ul").find("li").eq(0).addClass("selected");
			$j(this).find(".tag-change-content").find(".tag-change-in").eq(0).addClass("block");
		}
		$j(this).find(".tag-change-ul").find("li").on("click",function(){
            if($j(this).find("a").length == 0){
                var thisindex=$j(".ui-tag-change .tag-change-ul li").index($j(this));
                $j(this).addClass("selected").siblings("li").removeClass("selected");
                $j(".tag-change-in").eq(thisindex).addClass("block").siblings(".tag-change-in").removeClass("block");
            }
		});
});
}

//显示子属性的div
function showChildProperties(propertyPath,type){
	var divChildNode=$j("#childrenMapPropDialogContent");
	var listChildNode=$j("#childrenListPropDialogContent");
	if(type==2){
		var dialogFieldPath={"dialogFieldPath":propertyPath};
		loxia.asyncXhr(getChildPropertyUrl, dialogFieldPath,{type: "GET",
    		success: function(data){  			
    			divChildNode.html("");
    		    chlidproptery=eval(data);//转换json格式    
    			var  html="";
    			var inputPropertyNodeValue="";
    			try{
    				inputPropertyNodeValue=eval('('+inputPropertyNode.val()+')');
    			}catch(e){
    				inputPropertyNodeValue="";
    			}
    			if(chlidproptery.length>0){
	    			for(var i=0;i<chlidproptery.length;i++){ 
	    				if(chlidproptery[i].required==true){
	    						html="<div class='ui-block-line'> <label>"+chlidproptery[i].fieldName+"</label>" +
	    								"<div > <input type='text' id='"+chlidproptery[i].code+"' name='"+chlidproptery[i].code+"'" +
	    								" loxiaType='"+(chlidproptery[i].editer=='date'?'date':'input')+"'" +
	    								" value='"+(inputPropertyNodeValue[chlidproptery[i].code] == undefined?'':inputPropertyNodeValue[chlidproptery[i].code])+"'" +
	    							    " placeholder='该属性为必填'  mandatory='true'/>";
	    				  
	    				}else{
	    						html="<div class='ui-block-line'> <label>"+chlidproptery[i].fieldName+"</label>" +
	    							"<div > <input type='text' id='"+chlidproptery[i].code+"' name='"+chlidproptery[i].code+"'" +
	    							" loxiaType='"+(chlidproptery[i].editer=='date'?'date':'input')+"'" +
	    							"  value='"+(inputPropertyNodeValue[chlidproptery[i].code] == undefined?'':inputPropertyNodeValue[chlidproptery[i].code])+"'" +
	    							" placeholder=''  mandatory='false'/>";
	    					
	    				}
		    			html+="</div></div>";
		    			divChildNode.append(html);
	    			}
    			}else{
    				html="未找到数据";
    				divChildNode.append(html);
    			}
    			$j("#childrenMapPropDialogContent").find("[loxiaType]").each(function(){
    				loxia.initLoxiaWidget(this);
				});
    			
    		}	
    	});		
		$j("#childrenPropMap-dialog").dialogff({type:'open',close:'in',width:'600px',height:'300px'});

	}
	else if(type==3){
		index=0;  //属性的序号
		var dialogFieldPath={"dialogFieldPath":propertyPath};
		loxia.asyncXhr(getChildPropertyUrl, dialogFieldPath,{type: "GET",
    		success: function(data){    			
    			var contentNode=$j("#listContent");
    			contentNode.html("");
    		    listData=eval(data);
    		    $j("#listchildPropertyDiv").find("input[class='firstInput']").siblings("label").eq(0).html(listData[0].fieldName);
      			$j("#listchildPropertyDiv").find("input[class='firstInput']").attr("name",listData[0].code);
      			$j("#listchildPropertyDiv").find("input[class='firstInput']").attr("loxiaType",listData[0].editer=="date"?"date":"input");
      			$j("#listchildPropertyDiv").find("input[class='firstInput']").attr("mandatory",listData[0].required);
      			$j("#listchildPropertyDiv").find("input[class='firstInput']").attr("placeholder",listData[0].required?'该属性必填':'');
      			var tempNode=$j("#listchildPropertyDiv").find("input[class='firstInput']").siblings("span").eq(1);
    			if(listData.length<=1){
    				tempNode.hide();
    			}else{
    				tempNode.show();
    			}
    			for(var i=1;i<listData.length;i++){
    				var html="";
    				html="<div class='ui-block-line'> <label>"+listData[i].fieldName+"</label><div ><input type='text' id='' mandatory='"+listData[i].required+
    					  "' name='"+listData[i].code+"' value=''  size='50' loxiaType='"+(listData[i].editer=='date'?'date':'input')+
    					  "' placeholder='' aria-disabled='false'></div></div>";
    				contentNode.append(html);
    			}
    		
    			/********加载输入框内是否有数据*****/
    			
    			if(inputPropertyNode.val()==""){
    				index++;
					var html="<div class='ui-block-line  ui-block-content border-grey' id='listchildPropertyDiv' name='listchildPropertyDiv'>"+
								"<label class=''>"+index+"</label>"+										            
								"<div id='top'>"+
								"<div class='ui-block-line'>"+
								"<label>"+listData[0].fieldName+"</label>"+
								"<input id='' type='text' name='"+listData[0].code+"' value='' " +
								" loxiaType='"+(listData[0].editer=="date"?"date":"input")+"'  mandatory='"+listData[0].required+"' placeholder=''"+(listData[0].required?"该属性必填":"")+"'' />"+
								"<span class='new-list-add delete fRight display-inline width-auto clear-none mt0 ml5'>删除</span>";
					if(listData.length>1){//长度大于1才显示收起或者展开
						html += "<span class='new-list-add hideOrShow fRight display-inline width-auto clear-none mt0'>点击展开/收起</span>";
					}
					
					html += " </div></div>"+					
							"<div id='listContent'   class='listContent'>";
					
					for(var j=1;j<listData.length;j++){					
						html += "<div class='ui-block-line'> <label>"+listData[j].fieldName+
								"</label><div ><input type='text' id='' name='"+listData[j].code+
								"' value=''  size='50' loxiaType='"+(listData[j].editer=="date"?"date":"input")+
								"' placeholder=''"+(listData[j].required?"该属性必填":"")+"'' mandatory='"+listData[j].required+"' aria-disabled='false'></div></div>";
					}
					html+="</div></div>";
					listChildNode.append(html);
					listChildNode.append("<br/>");
					$j("#childrenListPropDialogContent").find("[loxiaType]").each(function(){
    					loxia.initLoxiaWidget(this);
    				});
    			}else{
    				var intputValue=eval('('+inputPropertyNode.val()+')');
    				for(var i=0;i<intputValue.length;i++){
    					
    					index++;
    					var currObj=intputValue[i];
    					var html="<div class='ui-block-line  ui-block-content border-grey' id='listchildPropertyDiv' name='listchildPropertyDiv'>"+
    								"<label class=''>"+index+"</label>"+										            
    								"<div id='top'>"+
    								"<div class='ui-block-line'>"+
    								"<label>"+listData[0].fieldName+"</label>"+
    								"<input id='' type='text' name='"+listData[0].code+"' value='"+currObj[listData[0].code]+"' " +
    								" loxiaType='"+(listData[0].editer=='date'?'date':'input')+"'  mandatory='"+listData[0].required+"' placeholder='' />"+
    								"<span class='new-list-add delete fRight display-inline width-auto clear-none mt0 ml5'>删除</span>";
						if(listData.length>1){//长度大于1才显示收起或者展开
							html += "<span class='new-list-add hideOrShow fRight display-inline width-auto clear-none mt0'>点击展开/收起</span>";
						}
						
						html += " </div></div>"+					
								"<div id='listContent' style='display:none'  class='listContent'>";
    					
    					for(var j=1;j<listData.length;j++){					
    						html += "<div class='ui-block-line'> <label>"+listData[j].fieldName+
    								"</label><div ><input type='text' id='' name='"+listData[j].code+
    								"' value='"+currObj[listData[j].code]+"'  size='50' loxiaType='"+(listData[j].editer=='date'?'date':'input')+
    								"' placeholder='' mandatory='"+listData[j].required+"' aria-disabled='false'></div></div>";
    					}
    					html+="</div></div>";
    					listChildNode.append(html);
    					listChildNode.append("<br/>");
    				}
    				$j("#childrenProplist-dialog").find("[loxiaType]").each(function(){
    					loxia.initLoxiaWidget(this);
    				});
    			}
    		}});
		
		listChildNode.html("");		
		$j("#childrenProplist-dialog").dialogff({type:'open',close:'in',width:'800px',height:'400px'});
	}	
}

function saveBaseInfo(){
	 var areaDefinition = $j("#areaDefinitions");
	 areaDefinition.html("");
	 nps.submitForm('userForm',{mode: 'async', 
			successHandler : function(data){
			if(data.isSuccess == true)
			{
				tempPath = $j.trim($j("#definitionPath").attr("value"));
				isSave=true;	
				addEvent();
				$j(".common-ic.sel-dialog.zoom-ic").remove();
				$j("#definitionPath").attr("readonly","readonly");
				$j("#definitionPath").attr("class","fLeft ui-loxia-default ui-corner-all");
				//提示不能被修改
				 $j("#definitionPath").on("click",function(){
					 $j("#loxiaTip-r1").show();
						 $j("#definitionPathMessge").html(nps.i18n("AREA_NOT_UPDATE_TEMPLATE"));
						 setTimeout(function(){ 
							$j("#loxiaTip-r1").hide();
						 },2000); 	
					
				});
				if(checkStrIsNotNull(tempPath)){
					getAreaInfo();
				}
				return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("AREA.UPDATE.SUCCESS"));
			}else
			{
				return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("AREA.UPDATE.FAILURE"));
			}
	   }});
}

function getAreaInfo(){
	 var parentNodePath = $j("#parentNodePath").val();
	 var path = $j("#name").val();
	 var fullPath = parentNodePath + path;
	//根据选择的模版路径读取区域信息
	var areaDefinition = $j("#areaDefinitions");
	areaDefinition.html("");
	var locked = '';
	//根据选择的模版路径读取区域信息
	var elementBlocks = nps.syncXhr(getTemplateAreasInfoUrlUrl, {"path":fullPath},{type: "GET"});
	for(var i=0 ;i < elementBlocks.length;i++){
		var sort = (i + 1);
		locked = elementBlocks[i].locked;
		if(locked){
			if( elementBlocks[i].customFunctions=="" ||  elementBlocks[i].customFunctions==null){
				 var newDiv =   '<div id="area1"><span style="color:red"> 该区域为锁定区域，不能被新增或者修改</span><input type="hidden" name="lockeds" value="'
					 + locked 
					 + '"/>';
				 areaDefinition.append('<div class="ui-block-content border-grey" style="margin-bottom:10px;"><div class="ui-block-line "><label>' + sort + '</label>' + newDiv + '</div></div>');
				 continue;
			}
		}else{
			if( elementBlocks[i].customFunctions=="" ||  elementBlocks[i].customFunctions==null){
				 var newDiv =   '<div id="area1"><input type="hidden" name="lockeds" value="'
					 + locked 
					 + '"/>'
					 +   '<div class="new-list-add addAreaChildren"' 
					 + ' style="width:62%;">' 
					 + '<span' 
					 + ' sort="'
					 + sort 
					 +'" locked="' 
					 + locked 
					 + '" ' 
					 + 'parameters="parameters">+新增</span></div>'; 
				 areaDefinition.append('<div class="ui-block-content border-grey" style="margin-bottom:10px;"><div class="ui-block-line "><label>' + sort + '</label>' + newDiv + '</div></div>');
				 continue;
			}
		}
	
		var html = $j('');
		var start = '<div class="ui-block-content border-grey" style="margin-bottom:10px;"> <div class="ui-block-line "> <input type="hidden" name="lockeds" value="' + locked + '"/> <label class="">';
		var titleEnd = '</label><div id="area1">';
		var end = '</div></div></div>';
		if(locked){//锁定
				 var customFunctions = elementBlocks[i].customFunctions;
				 var content = '<div><span style="color:red"> 该区域为锁定区域，不能被新增或者修改</span></div>';
				 for(var k = 0; k < customFunctions.length; k++){
					 var parameters = customFunctions[k].parameters;
					 if(parameters[0] != '' || parameters[0].length > 0){
						 content +=  '<input type="hidden" name="sorts" value="'
							 	   + sort
							 	   +  '"/>'
							 	   +  '<div style="margin-bottom:10px;">' 
							 	   +'<input type="text"'
							 	   + 'name="parameters"' 
							 	   + 'value="'
							 	   + parameters[0] 
						 		   + '"  readonly="readonly" style="width:62%;" loxiaType="input" placeHolder="区域或组件path路径"/>'
						 		   + '<span class="common-ic findArea1 zoom-ic"></span><span class="common-ic delete-ic locked"></span>' 
						 		   + '</div>';
					 }
				 }
				 areaDefinition.append(start + sort + titleEnd + content +  end);
		}else{
				 var content = "";
				 var customFunctions = elementBlocks[i].customFunctions;
				 var count = 0;
				 for(var k = 0; k < customFunctions.length; k++){
					 var parameters = customFunctions[k].parameters;
					 count++;
					 if(parameters[0] != '' || parameters[0].length > 0){
						 content += '<div style="margin-bottom:10px;">' 
							 		+'<input type="text"'
							 		+  'name="parameters"' 
							 		+ 'value="' 
							 		+ parameters[0]
						 			+ '" checkmaster="validatePath" style="width:62%;"  loxiaType="input" placeHolder="区域或组件path路径"/>' 
						 			+'<input type="hidden" name="sorts" value="' 
						 			+ sort 
						 			+  '"/>' 
						 			+ '<span class="common-ic findArea zoom-ic"></span><span class="common-ic delete-ic new-list-delete"></span>'
						 			+ '</div>';
					 }
					 if(count == customFunctions.length){
						 content = content + '<div class="new-list-add addAreaChildren"'
						 		+ ' style="width:62%;"><span' 
						 		+ ' sort="' 
						 		+ sort 
						 		+ '" locked="'
						 		+ locked  
						 		+'" ' 
						 		+ 'parameters="parameters">+新增</span></div>';
					 }
					
				 }
				 areaDefinition.append(start + sort + titleEnd + content).append(end);
			}
	}
	$j("#areaDefinitions").find("[loxiaType]").each(function(){
		loxia.initLoxiaWidget(this);
	});	
}


$j(document)
		.ready(
				function() {
					 if($j.trim($j("#definitionPath").val()).length == 0){
						 $j(this).find(".tag-change-ul").find("li").unbind();
						 $j(".ui-tag-change").each(function(){
								
								if(!$j(this).find(".tag-change-ul").find("li").hasClass("selected")){
									$j(this).find(".tag-change-ul").find("li").eq(0).addClass("selected");
								}
								
								$j(this).find(".tag-change-ul").find("li").on("click",function(){
						            if($j(this).find("a").length == 0){
						                var thisindex=$j(".ui-tag-change .tag-change-ul li").index($j(this));
						                if(thisindex == 0)
						                	return ;
						                if(!isSave)
						                		return  nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("AREA_MANAGER_SAVE_MSG"));
						            }
								});
						});
					 }else if($j.trim($j("#definitionPath").val()).length > 0){
						 addEvent();
						 //获取区域信息
						 isSave = true;
						 getAreaInfo();
					 }
					
					/**保存区域基本信息**/
					$j(".button.orange.save").eq(0).on("click", function() {
						 var parentNodePath = $j("#parentNodePath").val();
						 var path = $j("#name").val();
						 var fullPath = parentNodePath + path;
						 $j("#path").attr("value",fullPath);
						 $j("#areaPath").attr("value",fullPath);
						 var definitPath = $j.trim($j("#definitionPath").val());
						 if(!checkStrIsNotNull(definitPath)){
							 nps.submitForm('userForm',{mode: 'async', 
									successHandler : function(data){
									if(data.isSuccess == true){
										return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("AREA.UPDATE.SUCCESS"));
									}else{
										return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("AREA.UPDATE.FAILURE"));
									}
							   }});
						 }else{
							 	var controllTip =  $j("#controllTip").attr("title");
							 	if(saveCount == 0 && controllTip.length == 0){
							 		nps.confirm(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("AREA.SAVE.TIP.MSG"),function(){
							 			saveBaseInfo();
							 			drawDynamicProperty($j("#definitionPath").val(),getDynamicPropertyFieldsUrl);
							 			saveCount++;
							 		});
							 	}else{
							 		saveBaseInfo();
							 	}
						 }
					});
					
					 $j("#editArea").on("click","div.addAreaChildren",function(){
						 	var sort = $j(this).children().attr("sort").trim();
							var html=$j('<div style="margin-bottom:10px;">'
					    			+'<input type="text" loxiaType="input" '
					    			+  'name="parameters" ' 
					    			+' checkmaster="validatePath" style="width:62%;" placeHolder="区域或组件path路径">'
					    			+ '<input type="hidden" name="sorts" value="'
					    			+ sort 
					    			+  '"/>' 
					    			+'<span class="common-ic findArea zoom-ic"></span><span class="common-ic delete-ic new-list-delete"></span></div>');
						    loxia.initContext(html[0]);
					    	$j(this).before(html[0]);
					 });
					 
					/**保存编辑区域信息**/
					$j(".button.orange.save").eq(1).on("click", function() {
						$j("#cssFilePaths").attr("value",$j.trim($j("#cssFilePaths").val()));
						$j("#cssFileContent").attr("value",$j.trim($j("#cssFileContent").val()));
						$j("#jsFilePaths").attr("value",$j.trim($j("#jsFilePaths").val()));
						$j("#jsFileContent").attr("value",$j.trim($j("#jsFileContent").val()));
						var parameterNodes=$j("input[name=parameters]");
						if(parameterNodes.length>0){
							validateEmpty(parameterNodes);
							for(var i=0;i<parameterNodes.length;i++){
								var curNode=$j(parameterNodes[i]);
								if(curNode.val().trim()=='' || curNode.val().trim().length == 0){
									return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("PAGE_AREA_EMPTY"));	
								}
							}
						}
						 
						 nps.submitForm('areaContentForm',{mode: 'async', 
								successHandler : function(data){
							if(data.isSuccess == true){
								return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("AREA.EDIT.SUCCESS"));
							}else{
								return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("AREA.EDIT.FAILURE"));
							}
						}});
					});
					 
					/**保存编辑属性信息**/
					$j(".button.orange.save").eq(2).on("click", function() {
						 var parentNodePath = $j("#parentNodePath").val();
						 var path = $j("#name").val();
						 var fullPath = parentNodePath + path;
						 $j("#propertyPath").attr("value",fullPath);
						 nps.submitForm('areaDynmicPropertyForm',{mode: 'async', 
								successHandler : function(data){
							if(data.isSuccess == true)
							{
								return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("AREA.EDIT.PROPERTY.SUCCESS"));
							}else
							{
								return nps.info(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("AREA.EDIT.PROPERTY.FAILURE"));
							}	
						}});
					});
					
					$j("#editArea").on("click", "span.findArea", function() {
						var fullPath = $j("#parentNodePath").val() + $j("#name").val();
						var excludePath = areaOrcompExcludePath + "," + fullPath;
						var areaOrcomponentTreeSetting={
								"treeId":areaOrcompTreeId,
								"rootName":areaOrcompRootName,
								"rootPath":areaOrcompRootPath,
								"nodeType":areaOrcompNodeType,
								"onClickHandler":areaOrcompOnClickHandler,
								"checkEnable":areaOrcompCheckEnable,
								"chkStyle":areaOrcompChkStyle,
								"chkType":areaOrcompChkType,
								"selectType":areaOrcompSelectType,
								"nodeLevel":areaOrcompCNodeLevel,
								"excludePath":excludePath
						}
						tree= new CmsTree(areaOrcomponentTreeSetting);
						tree.getInit();
						curSelTreeObj = $j(this);
						$j("#areaOrcomp-dialog").dialogff({
							type : 'open',
							close : 'in',
							width : '600px',
							height: '300px'
						});
					});

					/**选择模版**/
					$j(".sel-dialog").on("click", function() {
						tree= new CmsTree(cmsTreeSetting);
						tree.getInit();
						curSelTreeObj = $j(this);
						$j("#template-dialog").dialogff({
							type : 'open',
							close : 'in',
							width : '600px',
							height: '300px'
						});
					});
					
					/**返回到列表页面**/
					$j(".button.return").on("click", function() {
						 window.location.href = toAreaUrl;
					});
					
				    //删除区域或者组件
					 $j("#editArea").on("click","span.new-list-delete",function(){
						 curSelTreeObj=$j(this);
						 var inputVal = curSelTreeObj.parent().children().eq(0).attr("value");
						 if(inputVal.length > 0){
							nps.confirm(nps.i18n("AREA_MANAGER_TIPS"),nps.i18n("AREA.DELETE.TIP"),function(){
								 curSelTreeObj.parent().remove();
							});
						 }else{
							 curSelTreeObj.parent().remove();
						 }
					 });
					 
					 
					 /*************************编辑字属性*********************************/
					 
					 //编辑子属性按钮
					 
					 $j("#dialogFields").on("click","a.func-button.ml5",function(){
						var currentNode=$j(this);
						inputPropertyNode=currentNode.siblings("input").eq(0);//保存那个input后面的编辑被点击
						var propertyPath=currentNode.siblings("span").eq(0).html();//$j("#childproperyPath").html();
						var propertyType=currentNode.siblings("span").eq(1).html();//$j("#childproperyType").html();
						showChildProperties(propertyPath, propertyType);
						 
					 });
					 
					 //字属性复层的取消按钮
					 $j("#childrenPropMap-dialog").on("click","input.button.orange.chiProSelCancel",function(){	    	
						   $j("#childrenPropMap-dialog").dialogff({type:'close'});
					    });
					 
					 
					 //字属性复层的确定按钮
					 $j("#childrenPropMap-dialog").on("click","input.button.orange.chiProSelok",function(){	 
						 var result="";
						var childPropertiesNodes= $j("#childrenMapPropDialogContent").find("input[type=text]");
						
						/************验证必填开始********/
						var count=0;
						childPropertiesNodes.each(function(){
							var curInput=$j(this);
							if(curInput.attr("mandatory")=='true' && curInput.val().trim()==""){
								curInput.addClass("ui-loxia-error");
								count++;
							}
						});
						
						if(count>0){
							return false;
						}
						/************验证必填结束********/
						
						if(childPropertiesNodes.length>0){
							result+="{";
							childPropertiesNodes.each(function(){
								var curInput=$j(this);
								result+="'"+curInput.attr("name")+"'"+":"+"'"+curInput.val()+"',";
							});
							result=result.substring(0,result.length-1);
							result+="}";
						}
						  inputPropertyNode.val(result);
						  inputPropertyNode.focus();
						  inputPropertyNode.blur();
						  $j("#childrenPropMap-dialog").dialogff({type:'close'});
					    });
					 
					 /*************************组合编辑子属性*********************************/
					 
					 
					/**********************列表子属性开始*********************************/
					 //新增
					 $j("#childrenProplist-dialog").on("click","div.new-list-add.addChildrenProp",function(){
						 	index++;
							var listChildNode=$j("#childrenListPropDialogContent");
							var divNode=$j("#listchildPropertyDiv").clone();
							//alert(divNode);
							divNode.attr("style","display:block");
							divNode.find("div[class='listContent']").attr("style","display:block");
							divNode.find("label[class='propertyIndex']").eq(0).html(index);
							if(listData.length>1){
								divNode.find("span").eq(1).show();
							}else{
								divNode.find("span").eq(1).remove();
							}
							
							
							listChildNode.append(divNode);
							listChildNode.append("<br/>");
							
							listChildNode.find("[loxiaType]").each(function(){
			    				loxia.initLoxiaWidget(this);
							});
							
							
					 });
					 
					 //展开or收起
					 $j("#childrenProplist-dialog").on("click","span.new-list-add.hideOrShow",function(){
						 //alert("aa");
						 var obj =$j(this).parent().parent().siblings().eq(1);
						   	if(obj.is(":hidden")){
						   		obj.show();
						   	}else{
						   		obj.hide();
						   	}
						 });
					 
					 //删除
					 $j("#childrenProplist-dialog").on("click","span.new-list-add.delete.ml5",function(){
							$j(this).parent().parent().parent().remove();
						 });
					 
					 
					//列表子属性复层的确定按钮
					 $j("#childrenProplist-dialog").on("click","input.button.orange.chiProSelok",function(){	 
						 var result="";			
						var divCount=$j("#childrenListPropDialogContent").find("div[name='listchildPropertyDiv']");
						/************验证必填开始********/
						var count=0;
						divCount.each(function(){
							var childPropertiesNodes= $j(this).find("input[type=text]");
							childPropertiesNodes.each(function(){					
								var curInput=$j(this);
									if(curInput.attr("mandatory")=='true' && curInput.val().trim()==""){
										curInput.addClass("ui-loxia-error");
										count++;
									}
								});
						});
						if(count>0){
							return false;
						}
						/************验证必填结束********/
						if(divCount.length>0){
							result+="[";
							divCount.each(function(){
								var childPropertiesNodes= $j(this).find("input[type=text]");
								result+="{";
								childPropertiesNodes.each(function(){
									
									var curInput=$j(this);
									result+="'"+curInput.attr("name")+"'"+":"+"'"+curInput.val()+"',";
								});
								result=result.substring(0,result.length-1);
								result+="},";
							});
							result=result.substring(0,result.length-1);
							result+="]";
							
						}
						  inputPropertyNode.val(result);
						  inputPropertyNode.focus();
						  inputPropertyNode.blur();
						  $j("#childrenProplist-dialog").dialogff({type:'close'});
					    });
					 
					 //列表字属性复层的取消按钮
					 $j("#childrenProplist-dialog").on("click","input.button.orange.chiProSelCancel",function(){	    	
						   $j("#childrenProplist-dialog").dialogff({type:'close'});
					    });
					 /**************列表子属性结束***************/					 
				});

