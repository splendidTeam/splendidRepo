$j.extend(loxia.regional['zh-CN'],{
    "COMPONENT_MANAGER_TIPS":"友情提示",
    "COMPONENT_TEMPLATE_SEL":"请选择模板",
    "COMPONENT_TEMPLATE_NOEXISTS":"模板文件不存在",
	"COMPONENT_EDIT_BASEINFO_SUCCESS":"更新组件成功",
	"COMPONENT_EDIT_BASEINFO_FAILURE":"更新组件失败",
	"COMPONENT_EDIT_SUCCESS":"编辑组件成功",
	"COMPONENT_EDIT_FAILURE":"编辑组件失败",
	"COMPONENT_EDIT_PROPERTY_SUCCESS":"编辑属性成功",
	"COMPONENT_EDIT_PROPERTY_FAILURE":"编辑属性失败",
	"COMPONENT_MANAGER_MSG":"请先保存基本信息",

	"COMPONENT_COMPONEN_DEFPATH_ILLEAGE":"模板定义路径不合法",
	"COMPONENT_TEMPLTE_NOMODIFY":"选择此模板后模板信息不可修改，是否继续更新?",
	"PATH_EXISTS":"路径已存在",

	"COMPONENT_NOT_UPDATE_PATH":"路径不能修改",
	"COMPONENT_NOT_UPDATE_DEFPATH":"模板路径不能修改",
	
	"COMPONENT_UPDATE_NOTEMPLATE_NOPREIVEW":"组件无模板或更新信息时可能未选择模板，无法预览",
	"COMPONENT_UPDATE_NOTEMPLATE_NOVIEW":"组件无模板或更新信息时可能未选择模板，无法查看",
	"COMPONENT_MANAGER_SAVE_MSG":"该组件无模版，不能编辑其它信息",
	
	"COMPONENT_LABEL_EDITPROP":"编辑",
	"COMPONENT_PROP_NODATA":"未找到数据",
	"COMPONENT_PROP_DELETE":"删除",
	"COMPONENT_PROP_SHOWORHIDE":"点击展开/收起"
    
});

var curSelTreeObj=null;
var chlidproptery=null;
var inputPropertyNode=null;
var index=0;
var listData="";

var queryComponentZNodeJson = base +'/cms/queryComponentZNode.json';

//var templatePath = '/definition/component';

var validateComponentNameUrl = base +"/cms/validateComponentName.json";

//获取编辑属性集合(dialogFileds)
//var queryDialogFiledsJson = base +'/cms/queryDialogFiledsJson.json';

//获取编辑属性集合(dialogFileds)
var getDynamicPropertyFieldsUrl = base +'/cms/getComponentDynamicPropertyFields.json';

var componentListUrl= base +'/cms/toComponent.htm';

var dialogPath=null;

//var componentRoot = "/instance/component";

var isClick =false;

var tree;

//预览
var previewComponentUrl = base +'/cms/previewComponent.htm';

//校验预览
var validatePreviewComponentPathUrl = base +'/cms/validatePreviewComponentPathUrl.json';

//查看模板
var viewTemplateUrl = base +'/cms/viewInstanceTemplate.htm';

//校验模板

var validateTemplateUrl = base +'/cms/validateInstanceTemplateByPath.json';

//校验模板定义
var validateComponentDefPathUrl= base +'/cms/validateComponentDefPath.json';

//
var  getComponentTemplateTypeUrl =base+"/cms/getComponentTemplateType.json";

//获取子属性
var getChildPropertyUrl=base+"/cms/findDialogFieldsByPath.json";

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

	
function onClick(event, treeId, treeNode) {
	
		var zTree = tree.getTree();
		zTree.checkNode(treeNode, !treeNode.checked, null, true);
		return false;
		//refreshData();
	}


//检测字符串是否为空
function checkStrIsNotNull(str) {
	if (str != null && str.trim() != "") {
		return true;
	} else {
		return false;
	}
}

//验证单元素是否有空变红色边框
function validateElementEmpty(element){
	if($j(element).val().trim()==""){
		$j(element).attr("class","ui-loxia-default ui-corner-all ui-loxia-highlight ui-loxia-error");
	}
}

//表单校验
function userFormValidate(form) {
	 
	 var warninfo ="";
	 var flag =false;
	 
	
 	 //校验模板定义路径
 	 var tempDefinitionPath = $j("#tempDefinitionPath").val();
	
 	 //路径必须以"/"开头，且无需以"/"结尾,可以不填
 	 
 	if(""!=$j.trim(tempDefinitionPath)){
 		
 		if(tempDefinitionPath.indexOf("/")!=0||
 				(tempDefinitionPath.length>=1&&tempDefinitionPath.substring(tempDefinitionPath.length-1,tempDefinitionPath.length)=="/")){
 			flag =true;
 			warninfo += nps.i18n("COMPONENT_COMPONEN_DEFPATH_ILLEAGE");
 		}else{
 			var definitionPath =$j("#difinition_root").val()+tempDefinitionPath;
 			var json={"path":definitionPath};
 			
 			var _d = loxia.syncXhr(validateComponentDefPathUrl, json,{type: "GET"});
 		 	if(!(_d.isSuccess)){
 		 		flag =true;
 		 		warninfo += _d.exception.message;
 		 	}else{
 		 		$j("#definitionPath").val(definitionPath);
 		 	}
 		}
 		
	}else{
		$j("#definitionPath").val("");
	}
 	if(flag){
 			$j("#definitionPath").val("");
			return warninfo;
		}
	
 	return loxia.SUCCESS;
}

//校验模板
function checkDefPath(val, obj){
	
	var tempDefinitionPath = $j.trim(val);
	
	if(""==tempDefinitionPath){
		$j("#definitionPath").val("");
	    return loxia.SUCCESS;
	}

	
	//"/"必须为第一个字符或者---->字符长度大于1时，最后一个字符必须不为"/"
	if(tempDefinitionPath.indexOf("/")!=0||tempDefinitionPath.indexOf("//") >= 0||
			(tempDefinitionPath.length>1&&tempDefinitionPath.substring(tempDefinitionPath.length-1,tempDefinitionPath.length)=="/")){
		$j("#definitionPath").val("");
		return nps.i18n("COMPONENT_COMPONEN_DEFPATH_ILLEAGE");
	}
	
	var definitionPath =$j("#difinition_root").val()+tempDefinitionPath;

	var json={"path":definitionPath};
	
	var _d = loxia.syncXhr(validateComponentDefPathUrl, json,{type: "GET"});
 	if(!(_d.isSuccess)){
 		$j("#definitionPath").val("");
 		return _d.exception.message;
 	}
 	$j("#definitionPath").val(definitionPath);
    return loxia.SUCCESS;
}


//增加点击事件效果
function  bindClick(){		
	$j(".ui-tag-change").each(function(){		
		$j(this).find(".tag-change-ul").find("li").on("click",function(){
                var thisindex=$j(".ui-tag-change .tag-change-ul li").index($j(this));
                $j(this).addClass("selected").siblings("li").removeClass("selected");
                $j(".tag-change-in").eq(thisindex).addClass("block").siblings(".tag-change-in").removeClass("block");
            });
	});
}

//覆盖main.js中的li点击事件效果
function  cancelbindClick(){	
	$j(".ui-tag-change").each(function(){
		$j(this).find(".tag-change-ul").find("li").unbind();
		
		if(!$j(this).find(".tag-change-ul").find("li").hasClass("selected")){
			$j(this).find(".tag-change-ul").find("li").eq(0).addClass("selected");
		}
		
		$j(this).find(".tag-change-ul").find("li").on("click",function(){
			var thisindex=$j(".ui-tag-change .tag-change-ul li").index($j(this));
				if(thisindex!=0){					
//					$j(".tag-change-ul").find("li").eq(0).addClass("selected").siblings().removeClass("selected");			
//	                $j(".tag-change-in").eq(0).addClass("block").siblings(".tag-change-in").removeClass("block");
	                if($j("#definitionPath").val().trim().length<=0 || $j("#definitionPath").val().trim()==""){
	                	return nps.info(nps.i18n("COMPONENT_MANAGER_TIPS"),nps.i18n("COMPONENT_MANAGER_SAVE_MSG"));
	                }
				}	
		});
});
}


//根据dialogPath 获取绘制编辑属性tag页
function DrawDynamicProperty(definitionPath,url){
	
	var json ={"definitionPath":definitionPath};
	if(checkStrIsNotNull(definitionPath)){
		
		loxia.asyncXhr(url, json,{type: "GET",
    		success: function(data){    			
    			
    			var dialogFiledArray=eval(data);//转换json格式

    			var propertyHtml="";
    			var rowFiled="";

    			for(var i=0 ;i<dialogFiledArray.length;i++){

					rowFiled="<div class='ui-block-line'>"
							+"<label>"+dialogFiledArray[i].fieldName+"</label>";
					
    				switch(dialogFiledArray[i].type){
    					case 1:
    						rowFiled +="<div>"
    				            +"<input type='text' name='propertyValues' value=''" +
    				            " loxiaType='"+(dialogFiledArray[i].editer=='date'?'date':'input')+ "' " +
    				            		" mandatory='" +dialogFiledArray[i].required+
    				              		"'/>"
    							+"</div>";
    						
    						break;
    					case 2:
    						rowFiled +="<div>"
    				            +"<input type='text' name='propertyValues' value='" +
    				              		"' mandatory='" +dialogFiledArray[i].required+ "'" +
    						              " onkeydown='return false;' loxiaType='input' " +
    				              		"'/>"+
    				              		"<a  class='func-button ml5' href='javascript:void(0)'> " +nps.i18n("COMPONENT_LABEL_EDITPROP")+
    				              		"</a>" +
    				              		"<span id='childproperyPath'"+i+" style='display:none'>"+dialogFiledArray[i].path+"</span><span id='childproperyType'"+i+" style='display:none'>"+dialogFiledArray[i].type+"</span>"
    							+"</div>";
    						break;
    					case 3:
    						rowFiled +="<div>"
    				            +"<input type='text' name='propertyValues' value='" +
    				              		"' mandatory='" +dialogFiledArray[i].required+ "'" +
    						              " onkeydown='return false;' loxiaType='input' " +
    				              		"'/>"+
    				              		"<a  class='func-button ml5' href='javascript:void(0)'> " +nps.i18n("COMPONENT_LABEL_EDITPROP")+
    				              		"</a>" +
    				              		"<span id='childproperyPath'"+i+" style='display:none'>"+dialogFiledArray[i].path+"</span><span id='childproperyType'"+i+" style='display:none'>"+dialogFiledArray[i].type+"</span>"
    							+"</div>";
    						break;
    					default : break;
    				}
    				
    				
    				rowFiled+="</div>";
    				rowFiled +="<input type='hidden' loxiaType='input' name='propertyCodes' value='" +
		              		dialogFiledArray[i].code
		              		+"' mandatory='" +dialogFiledArray[i].required+
		              		"'/>";
    				propertyHtml+=rowFiled;
    			}
    			$j("#dialogFields").html(propertyHtml);
    			
    			$j("#dialogFields").find("[loxiaType]").each(function(){
    				loxia.initLoxiaWidget(this);
    			});
    			
    		}	
    	});
		
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
	    								"<div > <input type='text' id='"+chlidproptery[i].code+"' name='"+chlidproptery[i].code+"'" +
	    								" loxiaType='"+(chlidproptery[i].editer=='date'?'date':'input')+"'" +
	    								" value='"+(inputPropertyNodeValue[chlidproptery[i].code] == undefined?'':inputPropertyNodeValue[chlidproptery[i].code])+"'" +
	    							    " mandatory='true'/>";
	    				  
	    				}else{
	    						html="<div class='ui-block-line'> <label>"+chlidproptery[i].fieldName+"</label>" +
	    							"<div > <input type='text' id='"+chlidproptery[i].code+"' name='"+chlidproptery[i].code+"'" +
	    							" loxiaType='"+(chlidproptery[i].editer=='date'?'date':'input')+"'" +
	    							"  value='"+(inputPropertyNodeValue[chlidproptery[i].code] == undefined?'':inputPropertyNodeValue[chlidproptery[i].code])+"'" +
	    							"  mandatory='false'/>";
	    				}
		    			html+="</div></div>";
		    			divChildNode.append(html);
	    			}
    			}else{
    				html=nps.i18n("COMPONENT_PROP_NODATA");
    				divChildNode.append(html);
    			}
    			$j("#childrenMapPropDialogContent").find("[loxiaType]").each(function(){
    				loxia.initLoxiaWidget(this);
				});
    			
    		}	
    	});		
		$j("#childrenPropMap-dialog").dialogff({type:'open',close:'in',width:'500px',height:'400px'});

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
    			//$j("#listchildPropertyDiv").find("input[class='firstInput']").attr("placeholder",listData[0].required?'该属性必填':'');
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
    				//listChildNode.html("");
    				
    				index++;
					var html="<div class='ui-block-line  ui-block-content border-grey' id='listchildPropertyDiv' name='listchildPropertyDiv'>"+
								"<label class=''>"+index+"</label>"+										            
								"<div id='top'>"+
								"<div class='ui-block-line'>"+
								"<label>"+listData[0].fieldName+"</label>"+
								"<input id='' type='text' name='"+listData[0].code+"' value='' " +
								" loxiaType='"+(listData[0].editer=="date"?"date":"input")+"'  mandatory='"+listData[0].required+"' />"+
								"<span class='new-list-add delete fRight display-inline width-auto clear-none mt0 ml5'>" +nps.i18n("COMPONENT_PROP_DELETE")+
								"</span>";
					if(listData.length>1){//长度大于1才显示收起或者展开
						html+="<span class='new-list-add hideOrShow fRight display-inline width-auto clear-none mt0'>" +nps.i18n("COMPONENT_PROP_SHOWORHIDE")+
								"</span>";
					}
					
					html += " </div></div>"+					
							"<div id='listContent'   class='listContent'>";
					
					for(var j=1;j<listData.length;j++){					
						html += "<div class='ui-block-line'> <label>"+listData[j].fieldName+
								"</label><div ><input type='text' id='' name='"+listData[j].code+
								"' value=''  size='50' loxiaType='"+(listData[j].editer=="date"?"date":"input")+
								"' mandatory='"+listData[j].required+"' aria-disabled='false'></div></div>";
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
    								" loxiaType='"+(listData[0].editer=='date'?'date':'input')+"' mandatory='"+listData[0].required+"' placeholder=''/>"+
    								"<span class='new-list-add delete fRight display-inline width-auto clear-none mt0 ml5'>" +nps.i18n("COMPONENT_PROP_DELETE")+
    								"</span>";
    					if(listData.length>1){//长度大于1才显示收起或者展开
    						html+="<span class='new-list-add hideOrShow fRight display-inline width-auto clear-none mt0'>" +nps.i18n("COMPONENT_PROP_SHOWORHIDE")+
    								"</span>";
    					}
    						
    					html+=" </div></div>" +
    							"<div id='listContent' style='display:none'  class='listContent'>";
    					
    					for(var j=1;j<listData.length;j++){					
    						html +=  "<div class='ui-block-line'> <label>"+listData[j].fieldName+
    								"</label><div ><input type='text' id='' name='"+listData[j].code+
    								"' value='"+currObj[listData[j].code]+"'  size='50' loxiaType='"+(listData[j].editer=='date'?'date':'input')+
    								"' mandatory='"+listData[j].required+"' aria-disabled='false'></div></div>";
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


function updateBaseInfo(){
	 nps.submitForm('userForm',{mode: 'async', 
			successHandler : function(data){
				if(data.isSuccess == true)
				{
					var definitionPathValue= $j("#definitionPath").val().trim();
					if(definitionPathValue.length>0 && definitionPathValue!=""){
							
							bindClick();
							var cssStr=$j("#tempDefinitionPath").attr("class");
							if(cssStr.indexOf("NoEdit")==-1){//只有页面初始化为空，并且选择了模版才获取动态属性和区域信息
								DrawDynamicProperty($j("#definitionPath").val(),getDynamicPropertyFieldsUrl);
							}	
							$j(".common-ic.sel-dialog.zoom-ic").remove();
							$j("#tempDefinitionPath").attr("readonly","readonly");
							$j("#tempDefinitionPath").attr("class","fLeft NoEdit ui-loxia-default ui-corner-all");
							//提示不能被修改
							 $j("#tempDefinitionPath").on("click",function(){
								 $j("#loxiaTip-r1").show();
									 $j("#definitionPathMessge").html(nps.i18n("COMPONENT_NOT_UPDATE_PATH"));
									 setTimeout(function(){ 
										$j("#loxiaTip-r1").hide();
									 },2000); 	
								
							});
							
							//DrawDynamicProperty($j("#definitionPath").val(),getDynamicPropertyFieldsUrl);
						
						}else{
							cancelbindClick();
						}
						
						return nps.info(nps.i18n("COMPONENT_MANAGER_TIPS"),nps.i18n("COMPONENT_EDIT_BASEINFO_SUCCESS"));
				}else
				{
					return nps.info(nps.i18n("COMPONENT_MANAGER_TIPS"),nps.i18n("COMPONENT_EDIT_BASEINFO_FAILURE"));
				}
			}
		});
}

$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
    //选择模板绑定事件 
    $j(".button.orange.tempselok").on("click",function(){

    	var treeObj = tree.getTree();
    	var path =null;
//		var id=null;
//		var type=null;
		
		var nodes_ = treeObj.getCheckedNodes(true);
		if(nodes_.length == 0){
			//
			nps.info(nps.i18n("COMPONENT_MANAGER_TIPS"),nps.i18n("COMPONENT_TEMPLATE_SEL"));
			return false;
		}else{
			for (var i=0, l=nodes_.length; i<l; i++) {
				path =nodes_[i].path;
//				uri = nodes_[i].uri ;
//				id = nodes_[i].id ;
//				dialogPath = nodes_[i].dialogPath;
				
			}
			
			var inputObj=curSelTreeObj.parent().children().eq(0);
			inputObj.val(path);
			
			var difinition_root = $j("#difinition_root").val();
			
			//显示
			var tempInputObj = $j("#tempDefinitionPath");
			tempInputObj.val(path.substring(difinition_root.length,path.length));
			$j("#tempDefinitionPath").focus();
			$j("#tempDefinitionPath").blur();
			$j("#tempDefinitionPath").attr("class","ui-loxia-default ui-corner-all ui-loxia-highlight");
			
		}
		
		$j("#template-dialog").dialogff({type:'close'});
		
		$j("#tempDefinitionPath").focus();
	});
    
    $j(".button.orange.tempselcancel").on("click",function(){
    	$j("#template-dialog").dialogff({type:'close'});
    	$j("#tempDefinitionPath").focus();

    });
	
	var definitionPathValue= $j("#definitionPath").val().trim();
	if(definitionPathValue.length>0 && definitionPathValue!=""){
		bindClick();
	}else{
		cancelbindClick();
	}
    
    
    var parentNodePathTemp = $j("#parentNodePath").val();
    
    var prefixPathTemp =parentNodePathTemp.substring(componentRoot.length,parentNodePathTemp.length);
    
    $j("#span_prefixPath").html(prefixPathTemp);
    
    
    //路径不能修改
    $j("#name").on("click",function(){
		 $j("#loxiaTip-r").show();
			 $j("#parentPathMessage").html(nps.i18n("COMPONENT_NOT_UPDATE_PATH"));
			 setTimeout(function(){ 
				$j("#loxiaTip-r").hide();
			 },2000); 	
		
	});
    
    //模板路径不能修改
    $j(".fLeft.NoEdit").on("click",function(){
		 $j("#loxiaTip-r1").show();
			 $j("#definitionPathMessge").html(nps.i18n("COMPONENT_NOT_UPDATE_PATH"));
			 setTimeout(function(){ 
				$j("#loxiaTip-r1").hide();
			 },2000); 	
		
	});
    
  //预览
    $j(".button.preview").click(function(){
    	var parentNodePath = $j("#parentNodePath").val();
		var path = $j("#name").val();
		var fullPath = parentNodePath +path;
    	if(!checkStrIsNotNull($j.trim($j("#definitionPath").val()) )){
    		return nps.info(nps.i18n("COMPONENT_MANAGER_TIPS"),nps.i18n("COMPONENT_UPDATE_NOTEMPLATE_NOPREIVEW"));
    	}
    	var validatedataJson={"path":fullPath};
    	var data = loxia.syncXhr(validatePreviewComponentPathUrl,validatedataJson,{type: "GET"});
    	if(data.isSuccess){
    		
    		//模板属性
    		var getTypeJson ={"definitionPath":$j.trim($j("#definitionPath").val())};
    		var type = loxia.syncXhr(getComponentTemplateTypeUrl,getTypeJson,{type: "GET"});
    		
    		//动态
    		if(type==2){
    			$j("#dynamicPath").val(fullPath);
    			$j("#dynamicTemplateParam-dialog").dialogff({type:'open',close:'in',width:'600px',height:'300px'});
    		}else{
            	loxia.openPage(previewComponentUrl+"?path="+fullPath);
    		}
    	}else{
    		return nps.info(nps.i18n("COMPONENT_MANAGER_TIPS"),nps.i18n(data.exception.message));
    	}
    	
    });
    
    //
	 $j("#dynamicTemplateParam-dialog").on("click","input.button.orange.dynamicTplateSelCancel",function(){	    	
		   $j("#dynamicTemplateParam-dialog").dialogff({type:'close'});
	    });
	 
	 
	 //
	 $j("#dynamicTemplateParam-dialog").on("click","input.button.orange.dynamicTplateSelok",function(){	 
		 
		  $j("#dynamicTemplateParam-dialog").dialogff({type:'close'});
		  
		  var tempPreviewComponentUrl=previewComponentUrl;
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
    	var parentNodePath = $j("#parentNodePath").val();
		var path = $j("#name").val();
		var fullPath = parentNodePath +path;
		if(!checkStrIsNotNull($j.trim($j("#definitionPath").val()) )){
    		return nps.info(nps.i18n("COMPONENT_MANAGER_TIPS"),nps.i18n("COMPONENT_UPDATE_NOTEMPLATE_NOVIEW"));
    	}
    	
    	var tempViewTemplateUrl="";
    		
		var validateVmJson ={"path":fullPath};
		var vmFileIsExist = loxia.syncXhr(validateTemplateUrl,validateVmJson,{type: "GET"});
		if(vmFileIsExist.isSuccess){
			tempViewTemplateUrl = viewTemplateUrl +'?path='+fullPath;
    		
        	loxia.openPage(tempViewTemplateUrl);
		}else{
			return nps.info(nps.i18n("COMPONENT_MANAGER_TIPS"),vmFileIsExist.exception.message);
		}
    	
    });

  


	//更新基本信息
	$j(".button.orange.save").eq(0).on("click",function(){
		
		var parentNodePath = $j("#parentNodePath").val();
		var path = $j("#name").val();
		var fullPath = parentNodePath +path;
		$j("#path").attr("value",fullPath);
		$j("#componentPath").attr("value",fullPath);
		$j("#propPath").attr("value",fullPath);
		
		
		if($j.trim($j("#tempDefinitionPath").val())==""){
			$j("#definitionPath").val("");
		}
		
		if(!checkStrIsNotNull($j.trim($j("#definitionPath").val()))){
			//为空
			updateBaseInfo();
		 }else{
			var cssStr=$j("#tempDefinitionPath").attr("class");
			if(cssStr.indexOf("NoEdit")==-1){//只有页面初始化为空，并且选择了模版才提示
					nps.confirm(nps.i18n("COMPONENT_MANAGER_TIPS"),nps.i18n("COMPONENT_TEMPLTE_NOMODIFY"), function(){
						updateBaseInfo();
					});				
			}else{
				updateBaseInfo();
			}
		 }
		
		
    });
	
	//更新组件
	$j(".button.orange.save").eq(1).on("click", function() {
		
		var parentNodePath = $j("#parentNodePath").val();
		var path = $j("#name").val();
		var fullPath = parentNodePath +path;
		$j("#path").attr("value",fullPath);
		$j("#componentPath").attr("value",fullPath);
		
		$j("#cssFilePaths").attr("value",$j.trim($j("#cssFilePaths").val()));
		$j("#cssFileContent").attr("value",$j.trim($j("#cssFileContent").val()));
		$j("#jsFilePaths").attr("value",$j.trim($j("#jsFilePaths").val()));
		$j("#jsFileContent").attr("value",$j.trim($j("#jsFileContent").val()));

		//提交
		 nps.submitForm('componentContentForm',{mode: 'async', 
				successHandler : function(data){
			if(data.isSuccess == true){
				return nps.info(nps.i18n("COMPONENT_MANAGER_TIPS"),nps.i18n("COMPONENT_EDIT_SUCCESS"));
			}else{
				return nps.info(nps.i18n("COMPONENT_MANAGER_TIPS"),nps.i18n("COMPONENT_EDIT_FAILURE"));
			}
		}});
	});
	
	//更新属性
	$j(".button.orange.save").eq(2).on("click",function(){
		
		
		var parentNodePath = $j("#parentNodePath").val();
		var path = $j("#name").val();
		var fullPath = parentNodePath +path;
		$j("#path").attr("value",fullPath);
		$j("#propPath").attr("value",fullPath);
		
		var divField=$j("#dialogFields").find("input[type='text']");
		divField.each(function(){					
			var curInput=$j(this);
				if(curInput.attr("mandatory")=='true' && curInput.val().trim()==""){
					curInput.val("");
					curInput.focus();
					curInput.blur();
				}
		});
		
		nps.submitForm('propForm',{mode: 'async', 
			successHandler : function(data){
				if(data.isSuccess == true)
				{
					return nps.info(nps.i18n("COMPONENT_MANAGER_TIPS"),nps.i18n("COMPONENT_EDIT_PROPERTY_SUCCESS"));
				}else
				{
					return nps.info(nps.i18n("COMPONENT_MANAGER_TIPS"),nps.i18n("COMPONENT_EDIT_PROPERTY_FAILURE"));
				}
			}
		});
    });
	
    //返回
    $j(".button.return").on("click",function(){
    	
    	 location.href= componentListUrl;
    });
    
  //选择模板
    $j(".sel-dialog").on("click",function(){
    	var treeId="tempTree"; //树的divId 在jsp中定义的  必需
    	var rootName=componentRootName;//根节点的显示名称  必需
    	var rootPath=tempdefinitionRoot;//根节点的实际路径  必需
    	
    	var nodeType=defaultNodeType +"," +definitionNodeType;  //构建树的节点的仓库类型
    	var onClickHandler=onClick;   //点击树节点时候的响应事件， 必需
    	var searchKeyId="key";		  //搜索输入框的id	
    	var searchResultId="search_result";  //显示搜索结果的文本Id
    	var checkEnable=true;    //节点是否能够被选择
    	var chkStyle="radio";//如果可以被选择，选择的类型： checkbox radio
    	var chkType = "all";//选择类型的参数  参见 ztree API setting.check.chkStyle 
    	var selectType=definitionNodeType;//能够被选中的仓库类型
    	var nodeLevel=3;//能够被选中的节点类型    0 父子节点均被选择    1父子节点均不被选择   2只选择父节点   3只选择子节点
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
		
    	curSelTreeObj=$j(this);
    	$j("#template-dialog").dialogff({type:'open',close:'in',width:'600px',height:'300px'});
 	});
    

	 /*************************组合编辑子属性*********************************/
	 
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