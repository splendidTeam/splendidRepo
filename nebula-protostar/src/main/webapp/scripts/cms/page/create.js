$j.extend(loxia.regional['zh-CN'], {
	"PAGE_MANAGER_TIPS":"友情提示：",
	"PAGE_MANAGER_FAIL":"操作失败：",
	"PAGE.ADD.AREANAME.DISABALE":"站点path不可用",
	"PAGE.ADD.AREANAME.ENALE":"站点path可用",
	"PAGE.ADD.SUCCESS":"保存成功",
	"PAGE.ADD.FAILURE":"保存失败",
	"PATH_EXISTS":"站点path已存在",
	"NO_SAVE":"请先保存基本信息",
	"DATE_VALIDATE_FAIL":"日期不符合规则，必须为yyyy-MM-dd",
	"PAGE_PROPERTY_SUCCESS":"页面属性添加成功",
	"PAGE_PROPERTY_FAIL":"页面属性添加失败",
	"PAGE_AREA_SUCCESS":"页面区域添加成功",
	"PAGE_AREA_FAIL":"页面区域添加失败",
	"PAGE_PROPERTY_EMPTY":"必须项不能为空",
	"SELECT_TEMPLATE":"请选择模版",
	"TEMPLATE_SAVE_NOT_SELECT_NOTVIEW":"保存基本信息时未选择模版路径，不能查看模版",
	"TEMPLATE_SAVE_NOT_SELECT_NOTPRVIEW":"保存基本信息时未选择模版路径，不能预览",
	"TEMPLATE_NOT_EXSIT":"模版路径不存在",
	"LOCKED_MESSAGE":"锁定区域不能新增或者修改",
	"PAGE_AREA_EMPTY":"区域或者组件路径不能为空",
	"PAGE_AREA_COMPONENT":"区域或者组件的路径不存在",
	"PAGE_CSS_EMPTY":"CSS内容和CSS文件不能同时为空",
	"PAGE_JS_EMPTY":"JS内容和JS文件不能同时为空",
	"SELECT_TEMPLATE_INFO":"选择此模板后模板信息不可修改，是否继续保存",
	"TIME_ERROR":"结束日期必须大于开始日期",
	"NO_SELECT_TEMPLATE":"保存基本信息是未选择模版，不能修改其他信息"
});

var curSelTreeObj=null;
var isSave=false;
var pathable=true;
var proptery=null;
var chlidproptery=null;
var inputPropertyNode=null;
var tree;
var index=0;
var listData="";
//验证url是否重复
var validatePageUrl =  "/cms/validatePageUrl.json";
//动态获取属性
var getPagePropetryUrl="/cms/getPageDynamicPropertyFields.json";
//得到区域和组件的模版
//var areaAndComponentZNodeUrl="/cms/queryAreaOrComponZNode.json";
//获取模版树
//var templateZNodeUrl="/cms/queryTemplateZNode.json";
//动态获取区域信息
var getTemplateAreasInfoUrl="/cms/getPageTemplateAreasInfo.json";
//查看模板
var viewTemplateUrl = base +'/cms/viewInstanceTemplate.htm';
//预览
var previewUrl = base +'/cms/previewPage.htm';
//验证vm文件
var validateTemplateUrl = base +'/cms/validateInstanceTemplateByPath.json';
//预览检验
var previewPagePathUrl=base +"/cms/previewPagePath.json";
//检验区域或者组件路径
var checkElementPathUrl=base+"/cms/checkElementBlockPathExists.json";
//检验模版的路径
var checkTemplatePathUrl=base+"/cms/checkTemplatePathExists.json";

//获取子属性
var getChildPropertyUrl=base+"/cms/findDialogFieldsByPath.json";

//获取模版的type
var getPageTemplateTypeUrl=base+"/cms/getPageTemplateType.json";





/************templatesetting begin************/
var treeId="tempTree"; //树的divId 在jsp中定义的  必需
var rootName="template";//根节点的显示名称  必需
var rootPath="/definition/template";//根节点的实际路径  必需
var nodeType="nt:unstructured,cms:definitions";  //构建树的节点的仓库类型
var onClickHandler=onClickTemp;   //点击树节点时候的响应事件， 必需
var checkEnable=true;    //节点是否能够被选择
var chkStyle="radio";//如果可以被选择，选择的类型： checkbox radio
var chkType = { "Y": "ps", "N": "ps" };//选择类型的参数  参见 ztree API setting.check.chkStyle 
var selectType="cms:definitions";//能够被选中的仓库类型
var nodeLevel=0;//能够被选中的节点类型    0 父子节点均被选择    1父子节点均不被选择   2只选择父节点   3只选择子节点
var type="get";
var cmsTempalteTreeSetting={
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
};

/************templatesetting end************/

/************area begin************/

var instanceTreeId="areaOrComp"; //树的divId 在jsp中定义的  必需
var instanceRootName="instance";//根节点的显示名称  必需
var instanceRootPath="/instance";//根节点的实际路径  必需
var instanceNodeType="cms:instance";  //构建树的节点的仓库类型
var instanceOnClickHandler=onClick;   //点击树节点时候的响应事件， 必需
var instanceCheckEnable=true;    //节点是否能够被选择
var instanceChkStyle="radio";//如果可以被选择，选择的类型： checkbox radio
var instanceChkType = { "Y": "ps", "N": "ps" };//选择类型的参数  参见 ztree API setting.check.chkStyle 
var instanceRadioType="all";
var instanceSelectType="cms:instance";//能够被选中的仓库类型
var instanceNodeLevel=3;//能够被选中的节点类型    0 父子节点均被选择    1父子节点均不被选择   2只选择父节点   3只选择子节点
var excludePath="/instance/page";//过滤该路径的所有的节点
var cmsAreaTreeSetting={
		"treeId":instanceTreeId,
		"rootName":instanceRootName,
		"rootPath":instanceRootPath,
		"nodeType":instanceNodeType,
		"onClickHandler":instanceOnClickHandler,
		"checkEnable":instanceCheckEnable,
		"chkStyle":instanceChkStyle,
		"chkType":instanceChkType,
		"selectType":instanceSelectType,
		"radioType":instanceRadioType,
		"nodeLevel":instanceNodeLevel,
		"excludePath":excludePath
};

/************area begin************/




/*//区域Setting
var setting = {

	check : {
		enable: true,
		chkStyle: "radio",
		radioType: "all"
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
		onClick : onClick
	},
	async: { 
		enable: true, 
		url:areaAndComponentZNodeUrl, 
		autoParam:["id=path"], 
		type:"get",// 默认post
		dataFilter: filter // 异步返回后经过Filter
	}
};*/


//验证数组是否有空变红色边框
function validateEmpty(arrays){
	for(var i=0;i<arrays.length;i++){
		if($j(arrays[i]).val()==""){
			validateElementEmpty(arrays[i]);
		}
	}
}

//验证单元素是否有空变红色边框
function validateElementEmpty(element){
	if($j(element).val().trim()==""){
		$j(element).attr("class","ui-loxia-default ui-corner-all ui-loxia-highlight ui-loxia-error");
	}
}

//验证url是否存在
function checkPathisExists(val, obj){
	var path = ($j("#parentPath").val()||"") + val;
	 var pattern = new RegExp("/");
	 if(pattern.test(val)){
		 return nps.i18n("PAGE.ADD.AREANAME.DISABALE");
	 }
	var r = loxia.syncXhr(validatePageUrl, {"path":path},{type: "POST"});
	if(!r.isSuccess)
		return nps.i18n("PAGE.ADD.AREANAME.DISABALE");
	return loxia.SUCCESS;
}
//验证路径是否存在
function checkPath(val,obj){
	if(val==""||val.length==0){
		return nps.i18n("PAGE_AREA_EMPTY");
	}
	
	
	if(val.indexOf("/")!=0||(val.length>1 && val.substring(val.length-1,val.length)=="/")){
		return nps.i18n("PAGE_AREA_COMPONENT");
	}	
	var r = loxia.syncXhr(checkElementPathUrl,{"elementBlockPath":val},{type: "POST"});
		if(r.isSuccess){
			return nps.i18n("PAGE_AREA_COMPONENT");
		}
	return loxia.SUCCESS;	
}
//验证模版路径
function checkTemplatePath(val,obj){
	if(val==""||val.length==0){
		return loxia.SUCCESS;
	}
	if(val.indexOf("/")!=0||(val.length>=1 && val.substring(val.length-1,val.length)=="/")){
		return nps.i18n("TEMPLATE_NOT_EXSIT");
	}
	var r = loxia.syncXhr(checkTemplatePathUrl,{"templatePath":val},{type: "POST"});
	if(r.isSuccess){
		return nps.i18n("TEMPLATE_NOT_EXSIT");
	}
	return loxia.SUCCESS;
	
}

//验证备份节点数
function checkBackupNodCount(val, obj){
	if(val=="" || val.length==0){
		return loxia.SUCCESS;
	}
	if(parseInt(val)!=val){
		return nps.i18n("只能是1-10的数字");
	}else if(parseInt(val)<1 || parseInt(val)>10){
		return nps.i18n("只能是1-10的数字");
	}
	return loxia.SUCCESS;
}



function onClick(event, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("areaOrComp");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
	//refreshData();
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
		//refreshData();
	}
	
	function checkPathExists(){
		var parentPath=$j("#parentPath").val();
		var path=$j("#name").val();
		if(path.trim()=="")return ;
		var fullpath=parentPath+path;
		var json={"path":fullpath};
		var _d = loxia.syncXhr(validatePageUrl, json,{type: "GET"});
		return  _d.isSuccess;
		
	}

	//验证表单
	function pageFormValidate(form){  
		 var flag =false;
		 var result = "";
	    if(!checkPathExists()){
			flag = true;
	        result+=nps.i18n("PATH_EXISTS");
	    }
	   
	    var definitionPath = $j.trim($j("#definitionPath").attr("value"));
		if(definitionPath.length > 0){
			if(definitionPath.indexOf("/")!=0 ||
					(definitionPath.length >=1 && definitionPath.substring(definitionPath.length-1,definitionPath.length)=="/")){
				flag = true;
				result+=nps.i18n("TEMPLATE_NOT_EXSIT");
			}
			var json = {"templatePath" : definitionPath};
			var _d = loxia.syncXhr(checkTemplatePathUrl,json,{type: "GET"});
			if(!_d.isSuccess){
				result += nps.i18n("TEMPLATE_NOT_EXSIT");
			}
		}
		
		if(flag){
			return result;
		}    
	    return loxia.SUCCESS;
	}

//保存基本信息
function saveBaseInfo(){	
	if(proptery!=null){
		var divNode=$j("#property");
		divNode.html("");
	}
	var startTime="";
	var endTime="";
	if($j("#startTime").val()!=""){
	 startTime=parseDate($j("#startTime").val());
	}
	if($j("#endTime").val()!=""){
		endTime=parseDate($j("#endTime").val());
		
	}
	if($j("#startTime").val()!="" && $j("#endTime").val()!=""){
		if(endTime.getTime()<startTime.getTime()){
			return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("TIME_ERROR"));
		}
	}
		
	nps.submitForm('pageForm',{mode:'async',
		successHandler : function(data){
			if(data.isSuccess == true){
				var definitionPathValue= $j("#definitionPath").val().trim();
				if(definitionPathValue.length>0 && definitionPathValue!=""){
					bindClick();
				}else{
					cancelbindClick();
				}
				isSave=true;				
				
		    	//获取属性    
				var templatePath={"templatePath":$j("#definitionPath").val()};
		    	loxia.asyncXhr(getPagePropetryUrl, templatePath,{type: "GET",
		    		success: function(data){    			
		    			var divNode=$j("#property");
		    			divNode.html("");
		    		    proptery=eval(data);//转换json格式    
		    			var  html="";
		    			if(proptery.length>0){
			    			for(var i=0;i<proptery.length;i++){ 		    				
			    				//<input type="ghd" clas="sort" value=i
			    				if(proptery[i].required==true){
			    					if(proptery[i].type==2 || proptery[i].type==3){
			    						html =	"<div class='ui-block-line'> <label>"+proptery[i].fieldName+"</label><div > <input type='text' id='"+proptery[i].code+
			    								"' name='"+proptery[i].code+"' loxiaType='input'  value='' onkeydown='return false;' placeholder='该属性为必填'  mandatory='true'/>";
				    				}else{
				    					html =	"<div class='ui-block-line'> <label>"+proptery[i].fieldName+"</label><div > <input type='text' id='"+proptery[i].code+
				    							"' name='"+proptery[i].code+"' loxiaType='"+(proptery[i].editer=='date'?'date':'input')+"' value=''  placeholder='该属性为必填'  mandatory='true'/>";
				    				}
				    			}else{
				    				if(proptery[i].type==2 || proptery[i].type==3){
				    					html =	"<div class='ui-block-line'> <label>"+proptery[i].fieldName+"</label><div > <input type='text' id='"+proptery[i].code+
				    							"' name='"+proptery[i].code+"' loxiaType='input' value='' onkeydown='return false;' placeholder=''  mandatory='false'/>";
				    				}else{
				    					html =	"<div class='ui-block-line'> <label>"+proptery[i].fieldName+"</label><div > <input type='text' id='"+proptery[i].code+
				    							"' name='"+proptery[i].code+"' loxiaType='"+(proptery[i].editer=='date'?'date':'input')+"' value='' placeholder=''  mandatory='false'/>";
				    				}
			    				  
			    				}
			    				if(proptery[i].type==2 || proptery[i].type==3){
				    				html +=	"<a  class='func-button ml5' href='javascript:void(0)'> 编辑</a><span id='childproperyPath'"+i+
				    						" style='display:none'>"+proptery[i].path+"</span><span id='childproperyType'"+i+" style='display:none'>"+proptery[i].type+
				    						"</span>";
			    				
			    				}
				    			html+="</div></div>";
			    				divNode.append(html);
			    			}
		    			}else{
		    				html="未找到数据";
		    				divNode.append(html);
		    			}
		    			$j("#property").find("[loxiaType]").each(function(){
		    				loxia.initLoxiaWidget(this);
						});
		    			
		    		}	
		    	});
		    	
		    	
		    	
		    	//根据选择的模版路径读取区域信息			    	
		    	 var parentNodePath = $j("#parentPath").val();
				 var path = $j("#name").val();
				 var fullPath = parentNodePath + path;
				
				 
				loxia.asyncXhr(getTemplateAreasInfoUrl, {"path":fullPath},{type: "GET",
		    		success: function(data){			    			
		    			var areaDefinition=$j("#areaDefinitions");
		    			areaDefinition.html("");
		    			var elementBlocks = eval(data);//转换json格式    	
		    			for(var i = 0;i < elementBlocks.length; i++){
		    				var sort = (i + 1);
							var locked = elementBlocks[i].locked;
							if( elementBlocks[i].customFunctions=="" ||  elementBlocks[i].customFunctions==null ){
								if(locked){
									var newDiv =    '<div id="area"><span style="color:red"> 该区域为锁定区域，不能被新增或者修改</span><input type="hidden" name="lockeds" value="' + locked + '"/>'; 
													/*+'<div class="new-list-add locked "' + 
													' style="width:62%;">'; */
									
									 areaDefinition.append('<div class="ui-block-content border-grey" style="margin-bottom:10px;"><div class="ui-block-line "><label>' +
											 				sort + '</label>' + newDiv + '</div></div>');
								}else{
									var newDiv =    '<div id="area"><input type="hidden" name="lockeds" value="' +	locked + '"/>' + 
								 					'<div class="new-list-add addAreaChildren"' +
								 					' style="width:62%;">'+
								 					'<span' + ' sort="' + sort + '" locked="' + locked + '" '  + '" ' +
												 	'parameters="parameters">+新增</span></div>';  
									
								    areaDefinition.append('<div class="ui-block-content border-grey" style="margin-bottom:10px;"><div class="ui-block-line "><label>' + 
										 				 sort + '</label>' + newDiv + '</div></div>');
								}
								continue;
							}
							
							var functionName = elementBlocks[i].customFunctions[0].functionName;
							var html = $j('');
							var start = '<div class="ui-block-content border-grey" style="margin-bottom:10px;"> <div class="ui-block-line "><input type="hidden" name="functionNames" value="' +
										functionName + '"/> <input type="hidden" name="lockeds" value="' + locked + '"/> <label class="">';
	    					var titleEnd = '</label><div id="area1">';
	    					var end = '</div></div></div>';
	    	
	    				
							if(locked){//锁定
								 var customFunctions = elementBlocks[i].customFunctions;
		    					 var content = "<div><span style='color:red'> 该区域为锁定区域，不能被新增或者修改</span></div>";
		    					 for(var k = 0; k < customFunctions.length; k++){
		    						 var parameters = customFunctions[k].parameters;
		    						 if(parameters[0].length>0 || parameters[0]!="" ){
		    						 content +=  '<input type="hidden" name="sorts" value="' + sort +  '"/>' + 
		    						 			 '<div style="margin-bottom:10px;">' +
    								 			 '<input type="text"' +  'name="parameters"' + 'value="' + parameters[0] + '" checkmaster="checkPath"  readonly="readonly" style="width:62%;" loxiaType="input" placeHolder="区域或组件path路径">' + 
    								 			 '<span class="common-ic findArea1 zoom-ic"></span><span class="common-ic delete-ic new-list-delete1"></span>'+  
    								 			 '</div>';
		    						 }
		    					 }
		    					 areaDefinition.append(start + sort + titleEnd + content  +  end);
			    					 
								}else{
									var content = "";
			    					 var customFunctions = elementBlocks[i].customFunctions;
			    					 var count = 0;
			    					 for(var k = 0; k < customFunctions.length; k++){
			    						 var parameters = customFunctions[k].parameters;
			    						 count++;
			    						 if(parameters[0].length>0 || parameters[0]!="" ){
			    						 content += '<div style="margin-bottom:10px;"> '+
	    								 			'<input type="text"' +  'name="parameters"' + 'value="' + parameters[0] + '" checkmaster="checkPath" style="width:62%;" loxiaType="input" placeHolder="区域或组件path路径">' +
	    								 			'<input type="hidden" name="sorts" value="' + sort +  '"/>' +
	    								 			'<span class="common-ic findArea zoom-ic"></span><span class="common-ic delete-ic new-list-delete"></span>'+
	    								 			'</div>';
			    						 }
			    						 if(count == customFunctions.length){
			    							 content = content + '<div class="new-list-add addAreaChildren"' + 
			    							 		   ' style="width:62%;"><span' + ' sort="' + sort + '" locked="' + locked + '" '  + '" ' +
			    							 		   'parameters="parameters">+新增</span></div>';
			    						 }
			    						
			    					 }
				    					 areaDefinition.append(start + sort + titleEnd + content).append(end);
				    				}
						}
		    			$j("#areaDefinitions").find("[loxiaType]").each(function(){
		    				loxia.initLoxiaWidget(this);
						});
		    		}	
		    	});
				return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("PAGE.ADD.SUCCESS"));				
			}else{
				return nps.info(nps.i18n("PAGE_MANAGER_FAIL"),nps.i18n(data.exception.message));
			}
			
		}});
}

function changeOrg(){
	var selVal=$j("input[name='rad_user_org']:checked").val();
	if(selVal=='系统'){
		var html=' <li>系统</li><li>店铺系统一</li><li>店铺系统二</li><li>店铺常规一</li><li>店铺常规二</li>';
		$j(".org_list").html(html);		
	}else{
		$j(".org_list").html("<li>"+selVal+"</li>");
	}
	
	refreshRoleSel();
	$j("#org_role").show();
}

function refreshAreaOrCompTree(){	
	var ztree = $j.fn.zTree.getZTreeObj("areaOrComp");	
	ztree.refresh();		//重置树
}

function refreshRoleSel(){
	$j(".ui-tag-change").each(function(){
		$j(this).find("li").eq(0).addClass("selected");
		$j(this).find(".tag-change-content").find(".tag-change-in").eq(0).show();
		
		$j(this).find("li").on("click",function(){
			var thisindex=$j(".ui-tag-change ul li").index($j(this));
			$j(this).addClass("selected").siblings("li").removeClass("selected");
			$j(".tag-change-in").eq(thisindex).css("display","block").siblings(".tag-change-in").css("display","none");
		});
	});
}

//增加点击事件效果
function  bindClick(){
	
	$j(".ui-tag-change").each(function(){		
		$j(this).find(".tag-change-ul").find("li").on("click",function(){
            if($j(this).find("a").length == 0){
                var thisindex=$j(".ui-tag-change .tag-change-ul li").index($j(this));
                $j(this).addClass("selected").siblings("li").removeClass("selected");
                $j(".tag-change-in").eq(thisindex).addClass("block").siblings(".tag-change-in").removeClass("block");
		}});
});
}

//覆盖main.js中的li点击事件效果
function  cancelbindClick(){
	
	$j(".ui-tag-change").each(function(){
		$j(this).find(".tag-change-ul").find("li").unbind();
		$j(this).find(".tag-change-ul").find("li").on("click",function(){	
				if($j(this).index()!=0){					
					$j(".tag-change-ul").find("li").eq(0).addClass("selected").siblings().removeClass("selected");			
	                $j(".tag-change-in").eq(0).addClass("block").siblings(".tag-change-in").removeClass("block");
	                if(!isSave)
	                	return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("NO_SAVE"));
	                if($j("#definitionPath").val().trim().length<=0 || $j("#definitionPath").val().trim()==""){
	                	return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("NO_SELECT_TEMPLATE"));
	                }
				}	
		});
});
}


//检测字符串是否为空
function checkStrIsNotNull(str) {
	if (str != null && str.trim() != "") {
		return true;
	} else {
		return false;
	}
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
	    								"<div > <input type='text' id='' name='"+chlidproptery[i].code+"'" +
	    								" loxiaType='"+(chlidproptery[i].editer=='date'?'date':'input')+"'" +
	    								" value='"+(inputPropertyNodeValue[chlidproptery[i].code] == undefined?'':inputPropertyNodeValue[chlidproptery[i].code])+"'" +
	    							    " placeholder='该属性为必填'  mandatory='true'/>";
	    				  
	    				}else{
	    						html="<div class='ui-block-line'> <label>"+chlidproptery[i].fieldName+"</label>" +
	    							"<div > <input type='text' id='' name='"+chlidproptery[i].code+"'" +
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
		index=0; //属性的序号
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
    						"' placeholder='"+(listData[i].required?"该属性必填":"")+"' aria-disabled='false'></div></div>";
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
    								"<input id='' type='text' name='"+listData[0].code+"' value='"+(currObj[listData[0].code]==undefined?'':currObj[listData[0].code])+"' " +
    								" loxiaType='"+(listData[0].editer=='date'?'date':'input')+"' mandatory='"+listData[0].required+"' placeholder='"+(listData[0].required?"该属性必填":"")+"'/>"+
    								"<span class='new-list-add delete fRight display-inline width-auto clear-none mt0 ml5'>删除</span>";
    					if(listData.length>1){//长度大于1才显示收起或者展开
    						html+="<span class='new-list-add hideOrShow fRight display-inline width-auto clear-none mt0'>点击展开/收起</span>";
    					}
    						
    					html+=" </div></div>" +
    							"<div id='listContent' style='display:none'  class='listContent'>";
    					
    					for(var j=1;j<listData.length;j++){					
    						html +=  "<div class='ui-block-line'> <label>"+listData[j].fieldName+
    								"</label><div ><input type='text' id='' name='"+listData[j].code+
    								"' value='"+(currObj[listData[j].code]==undefined?'':currObj[listData[j].code])+"'  size='50' loxiaType='"+(listData[j].editer=='date'?'date':'input')+
    								"' placeholder='"+(listData[j].required?"该属性必填":"")+"' mandatory='"+listData[j].required+"' aria-disabled='false'></div></div>";
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


$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
    cancelbindClick();
    
    
    //预览
    $j(".button.preview").click(function(){
    	var path=$j("#parentPath").val().trim()+$j("#name").val().trim();
    	
    	var definitionPath = $j("#definitionPath").val();
    	if(!isSave){
    		return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("NO_SAVE"));
    	}else if(!checkStrIsNotNull(definitionPath)){
    		return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("TEMPLATE_SAVE_NOT_SELECT_NOTPRVIEW"));
    	}
    	
    	var validatedata={"path":path};
    	var data = loxia.syncXhr(previewPagePathUrl,validatedata,{type: "GET"});
    	if(data.isSuccess){
    		//模板属性
    		var getTypeJson ={"definitionPath":definitionPath};
    		var type = loxia.syncXhr(getPageTemplateTypeUrl,getTypeJson,{type: "GET"});
    		//动态
    		if(type==2){
    			$j("#dynamicPath").val(path);
    			$j("#dynamicTemplateParam-dialog").dialogff({type:'open',close:'in',width:'600px',height:'300px'});
    		}else{
            	loxia.openPage(previewUrl+"?path="+path);
    		}
    		
    	}else{
    		return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n(data.exception.message));
    	}
    });
    
    
    
    
  //预览对话框取消
	 $j("#dynamicTemplateParam-dialog").on("click","input.button.orange.dynamicTplateSelCancel",function(){	    	
		   $j("#dynamicTemplateParam-dialog").dialogff({type:'close'});
	    });
	 
	 
	 //预览对话框确定
	 $j("#dynamicTemplateParam-dialog").on("click","input.button.orange.dynamicTplateSelok",function(){		 	
	  $j("#dynamicTemplateParam-dialog").dialogff({type:'close'});	  
	  var tempPreviewComponentUrl=previewUrl;
	  //解析textarea
	  var params =$j("#dynamic-params").val().trim();

	  tempPreviewComponentUrl+="?path="+$j("#dynamicPath").val();
	  if(checkStrIsNotNull(params)){
		  tempPreviewComponentUrl+="&"+params;
	  }
	  
	  loxia.openPage(tempPreviewComponentUrl);
	 });
	 
	 
	 
	 
    
    
    
    //查看模板
    $j(".button.viewTemplate").click(function(){
    	
    	var parentNodePath = $j("#parentPath").val();
		var path = $j("#name").val();
		var fullPath = "/instance/page"+parentNodePath +path;
		var tempViewTemplateUrl="";
		var definitionPath = $j("#definitionPath").val();
    	if(!isSave){
    		return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("NO_SAVE"));
    	}else if(!checkStrIsNotNull(definitionPath)){
    		return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("TEMPLATE_SAVE_NOT_SELECT_NOTVIEW"));
    	}	
		var validateVmJson ={"path":fullPath};
		var vmFileIsExist = loxia.syncXhr(validateTemplateUrl,validateVmJson,{type: "GET"});
		
		if(vmFileIsExist.isSuccess){
			tempViewTemplateUrl = viewTemplateUrl +'?path='+fullPath;    		
        	loxia.openPage(tempViewTemplateUrl);
		}else{
			return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),vmFileIsExist.exception.message);
		}

    });

    $j(".orgSelect").change(function(){

    	var selVal=$j(".orgSelect").val(); 	
    	
    	if(selVal=='系统'){
    		if($j(".orgRel").attr("checked")=="checked"){
    			$j(".org_list").html("<li>系统(联动选择所有店铺)</li>");
        	}else{
    			var html=' <li>系统</li><li>店铺1</li><li>店铺2</li><li>店铺3</li>';
    			$j(".org_list").html(html);
    		}
    	}
    	else{
    		$j(".org_list").html("<li>"+selVal+"</li>");
    	}

    	refreshRoleSel();
    	$j("#org_role").show();
    });
   
    
    $j(".button.orange.selok").on("click",function(){
		var ztree = $j.fn.zTree.getZTreeObj("areaOrComp");
		var uri=null;
		//var id=null;
		//var type=null;
		var nodes_ = ztree.getCheckedNodes(true);
		if(nodes_.length == 0){
			nps.info("错误提示：","请选择区域或组件!");
			return false;
		}else{
			//取到选择的行业名称
			for (var i=0, l=nodes_.length; i<l; i++) {
				uri =  nodes_[i].id.substring("/instance".length) ;
				id = nodes_[i].id ;
				type=nodes_[i].type;
			}
			
			//var selectObj=curSelTreeObj.parent().children().eq(0);
			var inputObj=curSelTreeObj.parent().children().eq(0);
			inputObj.attr("class","ui-loxia-default ui-corner-all");//清楚错误样式
			//selectObj.val(type);
			inputObj.focus();
			inputObj.val(uri);
		}
		
		$j("#areaOrcomp-dialog").dialogff({type:'close'});
		
	});
    
    $j(".button.orange.selcancel").on("click",function(){
    	
    	$j("#areaOrcomp-dialog").dialogff({type:'close'});
		
    	refreshAreaOrCompTree();
    });
    
    
    $j(".button.orange.tempselok").on("click",function(){
		var ztree = $j.fn.zTree.getZTreeObj("tempTree");
		var uri=null;
		//var id=null;
		//var type=null;
		var nodes_ = ztree.getCheckedNodes(true);
		if(nodes_.length == 0){		
			nps.info("错误提示：","请选择模板!");
			return false;
		}else{
			//取到选择的行业名称
			for (var i=0, l=nodes_.length; i<l; i++) {
				uri = nodes_[i].id.substring("/definition".length) ;
				id = nodes_[i].id ;				
			}
			
			var inputObj=curSelTreeObj.parent().children().eq(0);
			
			inputObj.val(uri);
			inputObj.attr("class","ui-loxia-default ui-corner-all ui-loxia-highlight");//清楚错误样式
			inputObj.focus();
			inputObj.blur();
		}
		
		$j("#template-dialog").dialogff({type:'close'});
		
	});
    
    $j(".button.orange.tempselcancel").on("click",function(){
    	$j("#template-dialog").dialogff({type:'close'});  	
    	
    });

});

//解析字符串为日期
function parseDate(str){
	var data=str.trim().split(" ");
	var date=data[0].split("-");
	var time=data[1].split(":");
	var year=date[0];
	var month=date[1];
	var day=date[2];
	var hour=time[0];
	var minitue=time[1];
	var seconde=time[2];	
	var result=new Date(year,month,day,hour,minitue,seconde);
	return result;
	
}

$j(document).ready(function(){

	$j("#edittable1").bind("rowchanged", function(event,data){
		console.log("rowchanged");
	});
	
	$j("#edittable2").bind("rowchanged", function(event,data){
		console.log("rowchanged");
	});
	
	$j("#edittable3").bind("rowchanged", function(event,data){
		console.log("rowchanged");
	});

	//提交基本信息
	$j("#baseInfoSave").on("click",function(){
		if($j("#definitionPath").val().trim()!="" && $j("#definitionPath").val().trim().length>0){		
			nps.confirm(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("SELECT_TEMPLATE_INFO"), function(){
				saveBaseInfo();
			});
		}else{
			saveBaseInfo();
		}
    });
	
	
	
	
	//提交属性
	$j("#propertySave").on("click",function(){
		var parentNodePath = $j("#parentPath").val();
		 var path = $j("#name").val();
		 var fullPath = parentNodePath + path;
		 $j("#propertyPath").attr("value",fullPath);
		
		
		nps.submitForm('propertyForm',{mode:'async',
    		successHandler : function(data){
				if(data.isSuccess == true){
					return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("PAGE_PROPERTY_SUCCESS"));
				}else{
					return nps.info(nps.i18n("PAGE_MANAGER_FAIL"),nps.i18n(data.exception.message));
				}	
    		}});
    });
	
	
	//提交区域
	$j("#areaSave").on("click",function(){	
		var path=$j("#parentPath").val()+$j("#name").val();
		$j("#pageContentPath").attr("value",path);
			
		var functionNameNode=$j("input[name=functionNames]");
		var lockNode=$j("input[name=lockeds]");
		var parameterNode=$j("input[name=parameters]");
		var sortNode=$j("input[name=sorts]");
		if(functionNameNode.length>0){
			validateEmpty(functionNameNode);
			for(var i=0;i<functionNameNode.length;i++){
				var curNode=$j(functionNameNode[i]);
				if(curNode.val().trim()==""){
					curNode.focus();
					 return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("PAGE_AREA_EMPTY"));
				}
			}
		}
		if(lockNode.length>0){
			validateEmpty(lockNode);
			for(var i=0;i<lockNode.length;i++){
			 var curNode=$j(lockNode[i]);
				if(curNode.val().trim()==""){
					curNode.focus();
				 return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("PAGE_AREA_EMPTY"));
				}
			}
		}
		if(parameterNode.length>0){
			validateEmpty(parameterNode);
			for(var i=0;i<parameterNode.length;i++){
				var curNode=$j(parameterNode[i]);
				if(curNode.val().trim()==""){
					curNode.focus();
					 return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("PAGE_AREA_EMPTY"));
				}
			}
		}
		if(sortNode.length>0){
			validateEmpty(sortNode);
			for(var i=0;i<sortNode.length;i++){
				var curNode=$j(sortNode[i]);
				if(curNode.val().trim()==""){
					curNode.focus();
					 return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("PAGE_AREA_EMPTY"));
				}
			}
		}	
		
		
		nps.submitForm('pageContentForm',{mode:'async',
    		successHandler : function(data){
				if(data.isSuccess == true){
					return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("PAGE_AREA_SUCCESS"));
				}else{
					//data.exception.message
					return nps.info(nps.i18n("PAGE_MANAGER_FAIL"),nps.i18n(data.exception.message));
				}	
    		}});
    });

    //返回按钮
     $j(".button.return").on("click",function(){    	
    	 location.href='toPage.htm';
    });
    
	 $j("#editArea").on("click","div.addAreaChildren",function(){
		 	var sort = $j(this).children().attr("sort").trim();
			var html=$j('<div style="margin-bottom:10px;"><input type="text" loxiaType="input"  ' +  'name="parameters"' + 
	    			' style="width:62%;" placeHolder="区域或组件path路径">'+
	    			'<input type="hidden" name="sorts" value="' + sort +  '"/>' +
	    			'<span class="common-ic findArea zoom-ic"></span><span class="common-ic delete-ic new-list-delete"></span></div>');
		    loxia.initContext(html[0]);
	    	$j(this).before(html[0]);
	 });
     //新增locked提示
	 $j("#editArea").on("click","div.new-list-add.locked",function(){
		 return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("LOCKED_MESSAGE"));
	 });
	 
	  //选择locked违规提示
	    $j("#editArea").on("click","span.findArea1",function(){
	    	 return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("LOCKED_MESSAGE"));
	    });
	    
	    
	    //删除区域或者组件
		 $j("#editArea").on("click","span.new-list-delete",function(){
			 curSelTreeObj=$j(this);
			 var inputValue=$j.trim(curSelTreeObj.parent().children().eq(0).val().trim());
			 if(inputValue.length!=0){
				nps.confirm(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("CONFIRM_DELETE"), function(){
					curSelTreeObj.parent().remove();
				});
			 }else{
			 curSelTreeObj.parent().remove();
			 }
		 });
		 
		 //删除locked违规提示
		 $j("#editArea").on("click","span.new-list-delete1",function(){
		     return nps.info(nps.i18n("PAGE_MANAGER_TIPS"),nps.i18n("LOCKED_MESSAGE"));
		 });
		
    
    
    //编剧区域获取模版
    $j("#editArea").on("click","span.findArea",function(){
    	curSelTreeObj=$j(this);
    	 tree= new CmsTree(cmsAreaTreeSetting);
		 tree.getInit();
		
    	
    	$j("#areaOrcomp-dialog").dialogff({type:'open',close:'in',width:'600px',height:'300px'});
    });
	
	 $j(".sel-dialog").on("click",function(){
		 	curSelTreeObj=$j(this);
		 	/*var json = loxia.syncXhr(templateZNodeUrl,{},{type: "GET"});
	    	
	    	//初始化树
	    	$j.fn.zTree.init($j("#tempTree"), tempSetting, json);*/
		 	tree= new CmsTree(cmsTempalteTreeSetting);
		 	tree.getInit();
	    	$j("#template-dialog").dialogff({type:'open',close:'in',width:'600px',height:'300px'});
	 });
	 
	 /*************************编辑字属性*********************************/
	 
	 //编辑子属性按钮
	 
	 $j("#property").on("click","a.func-button.ml5",function(){
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
	 
	 /*************************编辑字属性*********************************/
	 
	 
		/********列表子属性开始********************************************/
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
				
				divNode.find("[loxiaType]").each(function(){
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

