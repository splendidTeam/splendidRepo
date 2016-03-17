$j.extend(loxia.regional['zh-CN'], {
	"DIALOG.MANAGER.TIPS":"友情提示：",
	"PATH.DISABALE":"path不可用",
	"PATH.EXISTS":"path已存在",
	"DIALOG.ADD.SUCCESS":"修改成功",
	"DIALOG.ADD.FAILURE":"修改失败",
	"DIALOG.DELETE.COMFIRM":"确认删除？",
	"DIALOG.ADD.MUSTBESINGLE":"组合类型只能添加一个子属性",
	"DIALOG.ADD.EXTENDMUSTNOTSELF":"不能继承当前属性本身",
	"DIALOG.CODE.MUSTBE.NOT.SAME":"属性编码重复",
	"DIALOG.VALUE.MUSTBE.NOT.SAME":"属性名称重复",
	"DIALOG.PLEASE.SELECT.EXTEND":"请选择继承的dialog!",
	"DIALOG.PLEASE.COMPLETE.INFO":"请先填写完整信息！",
	"DIALOG.MANAGE.PROP":"属性",
	"DIALOG.MANAGE.CHILD.PROP":"子属性",
	"DIALOG.MANAGE.CODE":"属性编码",
	"DIALOG.MANAGE.SHOW.HIDE":"点击展开/收起",
	"DIALOG.MANAGE.DELETE":"删除",
	"DIALOG.MANAGE.PROP.NAME":"属性名称",
	"DIALOG.MANAGE.EDIT":"编辑",
	"DIALOG.MANAGE.PROP.TYPE":"属性类型",
	"DIALOG.MANAGE.PROP.EDITOR":"属性编辑器",
	"DIALOG.MANAGE.REQUIRED":"是否必须",
	"DIALOG.MANAGE.GROUP":"属性分组",
	"DIALOG.MANAGE.SORT":"排序号",
	"DIALOG.MANAGE.PROP.DESC":"属性描述",
	"DIALOG.MANAGE.ADD":"新增",
	"DIALOG.MANAGE.DESC":"描述"

});
var flag=true;
var definitonPathPrefix = '/definition';
var isClick = true;
var curSelTreeObj = null;
var tempPath = "/";
var isSave = true;	
var i=0;
var j=0;


/***************************************************************************************************kkkkkkkkkk**************/
//验证url是否重复
var validatePageUrl = base + "/cms/validateDialogUrl.json";
var toDialogUrl= base + "/cms/toDialog.htm";
var rootUrl = base + "/definition/dialog";
var updateDialogUrl= base + "/cms/updateDialog.json";

/**模版配置开始**/
var treeId="tempTree"; //树的divId 在jsp中定义的  必需
var rootName="dialog";//根节点的显示名称  必需
var rootPath="/definition/dialog";//根节点的实际路径  必需
var nodeType="nt:unstructured,cms:dialog";  //构建树的节点的仓库类型
var onClickHandler=onClickTemp;   //点击树节点时候的响应事件， 必需
var checkEnable=true;    //节点是否能够被选择
var chkStyle="radio";//如果可以被选择，选择的类型： checkbox radio
var chkType = { "Y": "ps", "N": "ps" };//选择类型的参数  参见 ztree API setting.check.chkStyle 
var selectType="cms:dialog";//能够被选中的仓库类型
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
function onClickTemp(event, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("tempTree");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
	// refreshData();
}
/**模版配置结束**/

//验证url是否存在
function checkPathisExists(val, obj){
	var path = ($j("#parentPath").val()||"") + val;
	 var pattern = new RegExp("/");
	 if(pattern.test(val)){
		 return nps.i18n("PATH.DISABALE");
	 }
	var r = loxia.syncXhr(validatePageUrl, {"path":path},{type: "POST"});
	if(!r.isSuccess)
		return nps.i18n("PATH.DISABALE");
	return loxia.SUCCESS;
}

function checkPathExists(){
	var parentPath=$j("#parentPath").val();
	var path=$j("#path").val();
	if(path.trim()=="")return ;
	var fullpath=parentPath+path;
	var json={"path":fullpath};
	var _d = loxia.syncXhr(validatePageUrl, json,{type: "GET"});
	return  _d.isSuccess;
	
}
/**表单验证**/
function baseInfoFormValidate(form) {
    if(!checkPathExists()){
    	 return nps.i18n("PATH.EXISTS");
    }
	return loxia.SUCCESS;
}
//子属性判断（非空和重复）
function childPropertyValidate(){
    flag=true;
    //判断code
	$j("input[id='input3']").each(function(indexi,datai){
		if(datai.value==""){	
			$j(this).addClass("ui-loxia-error");
			flag=false;
		}else{
			$j(this).removeClass("ui-loxia-error");
		}
		
	});
	 //判断name
	$j("input[id='input4']").each(function(indexi,datai){
		if(datai.value==""){
			$j(this).addClass("ui-loxia-error");
			flag=false;
		}else{
			$j(this).removeClass("ui-loxia-error");
		}
	});
	if(!flag){
		return nps.info(nps.i18n("DIALOG.MANAGER.TIPS"),nps.i18n("DIALOG.PLEASE.COMPLETE.INFO"));
	}
	//判断重复
	
    $j('.ui-block-content.childrenPropDialogContent').find("input[id='input3']").each(function(indexi,datai){
 		$j('.ui-block-content.childrenPropDialogContent').find("input[id='input3']").each(function(indexj,dataj){ 
 			if(indexi!=indexj&&datai.value.trim()==dataj.value.trim()){
 				$j(this).addClass("ui-loxia-error");
     			flag=false;  
     		}
     	});
 	});
 	if(flag){
 		$j('.ui-block-content.childrenPropDialogContent').find("input[id='input3']").each(function(indexi,datai){
 			$j(this).removeClass("ui-loxia-error");
 		});
 		$j('.ui-block-content.childrenPropDialogContent').find("input[id='input4']").each(function(indexi,datai){
 			$j('.ui-block-content.childrenPropDialogContent').find("input[id='input4']").each(function(indexj,dataj){ 
	    			if(indexi!=indexj&&datai.value.trim()==dataj.value.trim()){
	    				$j(this).addClass("ui-loxia-error");
	        			flag=false;  
	        		}
	        	});
 	    });
 	}else{
 		return nps.info(nps.i18n("DIALOG.MANAGER.TIPS"),nps.i18n("DIALOG.CODE.MUSTBE.NOT.SAME"));
 	}
 	if(!flag){
 		return nps.info(nps.i18n("DIALOG.MANAGER.TIPS"),nps.i18n("DIALOG.VALUE.MUSTBE.NOT.SAME"));
 	}else{
 		$j('.ui-block-content.childrenPropDialogContent').find("input[id='input4']").each(function(indexi,datai){
 			$j(this).removeClass("ui-loxia-error");
 		});
 	}
}
//属性判断（非空和重复）
function propertyValidate(){
  	
	flag=true;
	//判断非空
    //判断code
	$j("input[id='input1']").each(function(indexi,datai){
		if(datai.value.trim()==""){	
			$j(this).addClass("ui-loxia-error");
			flag=false;
		}else{
			$j(this).removeClass("ui-loxia-error");
		}
		
	});
	 //判断name
	$j("input[id='input2']").each(function(indexi,datai){
		if(datai.value.trim()==""){
			$j(this).addClass("ui-loxia-error");
			flag=false;
		}else{
			$j(this).removeClass("ui-loxia-error");
		}
	});
	if(!flag){
		return nps.info(nps.i18n("DIALOG.MANAGER.TIPS"),nps.i18n("DIALOG.PLEASE.COMPLETE.INFO"));
	}
	//判断重复

	$j("input[id='input1']").each(function(indexi,datai){
		$j("input[id='input1']").each(function(indexj,dataj){ 
			if(indexi!=indexj&&datai.value.trim()==dataj.value.trim()){
				$j(this).addClass("ui-loxia-error");
    			flag=false;  
    		}
    	});
	});
	if(flag){
		$j("input[id='input1']").each(function(indexi,datai){
			$j(this).removeClass("ui-loxia-error");
		});
		$j("input[id='input2']").each(function(indexi,datai){
    		$j("input[id='input2']").each(function(indexj,dataj){ 
    			if(indexi!=indexj&&datai.value.trim()==dataj.value.trim()){
    				$j(this).addClass("ui-loxia-error");
        			flag=false;  
        		}
        	});
	    });
	}else{
		return nps.info(nps.i18n("DIALOG.MANAGER.TIPS"),nps.i18n("DIALOG.CODE.MUSTBE.NOT.SAME"));
	}
	if(flag){
		$j("input[id='input2']").each(function(indexi,datai){
			$j(this).removeClass("ui-loxia-error");
		});
	}else{
		return nps.info(nps.i18n("DIALOG.MANAGER.TIPS"),nps.i18n("DIALOG.VALUE.MUSTBE.NOT.SAME"));
	}
}
$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	
     /**
      * 选择继承
      */
     $j(".sel-dialog").on("click",function(){
		 	curSelTreeObj=$j(this);
		 	tree= new CmsTree(cmsTempalteTreeSetting);
		 	tree.getInit();
	    	$j("#template-dialog").dialogff({type:'open',close:'in',width:'600px',height:'300px'});
	 });
     
   /**
    * 继承字段树
    */
	$j(".button.orange.tempselok").on("click", function() {
		var ztree = $j.fn.zTree.getZTreeObj("tempTree");
		var name = null;
		var pId = null;
		var id = null;
		var uri = null;
		var nodes_ = ztree.getCheckedNodes(true);
		if (nodes_.length == 0) {
			nps.info(nps.i18n("AREA.MANAGER.TIPS"),nps.i18n("DIALOG.PLEASE.SELECT.EXTEND"));
			return false;
		} else {
			// 取到选择的行业名称
			for ( var i = 0, l = nodes_.length; i < l; i++) {
				//uri = nodes_[i].uri;
				id = nodes_[i].id;
				pid = nodes_[i].pId;
				name = nodes_[i].name;
				uri = pid.substring(definitonPathPrefix.length,pid.length) + "/" + name;
			}
			var inputObj = curSelTreeObj.parent().children().eq(0);
			var realPath= $j("#realPath").val();
			var realPath1=definitonPathPrefix+uri;
			if(realPath==realPath1){
				return nps.info(nps.i18n("DIALOG.MANAGER.TIPS"),nps.i18n("DIALOG.ADD.EXTENDMUSTNOTSELF"));
			}
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
	
	/**
	 * 保存基本信息
	 */
	$j(".button.orange.save").eq(0).on("click", function() {
		nps.submitForm('baseInfoForm',{mode:'async',
			successHandler : function(data){
				if(data.isSuccess == true){
					isClick = true;
					isSave=true;
					$j("#updateDialogPath").val($j("#parentPath").val()+$j("#path").val());
					return nps.info(nps.i18n("DIALOG.MANAGER.TIPS"),nps.i18n("DIALOG.ADD.SUCCESS"));
				}else
				{
					return nps.info(nps.i18n("DIALOG.MANAGER.TIPS"),nps.i18n("DIALOG.ADD.FAILURE"));
				}	
    		}});
    });
	/**
	 * 保存动态属性
	 */
	$j(".button.orange.save").eq(1).on("click", function() {
		//验证
	    propertyValidate();

    	if(flag){
    		nps.submitForm('propForm',{mode:'async',
			successHandler : function(data){
				if(data.isSuccess == true){
					return nps.info(nps.i18n("DIALOG.MANAGER.TIPS"),nps.i18n("DIALOG.ADD.SUCCESS"));
				}else
				{
					return nps.info(nps.i18n("DIALOG.MANAGER.TIPS"),nps.i18n("DIALOG.ADD.FAILURE"));
				}	
    		}});
    	}
    });
	
    /**
     * 返回按钮
     */
     $j(".button.return").on("click",function(){    	
    	 location.href=toDialogUrl;
     });
 

 	/**
 	 * 显示‘编辑’按钮
 	 */
 	$j("#dialogBody").on("change",".proType",function(){
 		   var sort =$j(this).next().next().val();
 		   $j("#isWhatType_"+sort).val($j(this).val());
		   if($j(this).val()!=1){
			   $j(this).next().removeClass("none");
			   $j(this).parent().parent().children().eq(2).children().eq(1).val("string").attr("disabled",true);
		   }
		   else
			{
			   $j(this).next().addClass("none");
			   $j(this).parent().parent().children().eq(2).children().eq(1).val("string").attr("disabled",false);
			}
	   });
 	
 	/**
 	 * 点击‘编辑’
 	 */
 	   $j("#dialogBody").on("click","#add_pro",function(){
 		  
 		  //当前父节点序号
 		  
 		  var nextVal= $j(this).next().val();
 		  
 		  //标志当前父节点序列号
 		  
		   $j("#currentNO").val(nextVal);
		   
 		  //克隆当前节点的子节点隐藏域到弹出对话框
		   
 		   var aimhtml =$j("#childrenProp_"+nextVal).children().clone();
 		   
 			 var selects = $j("#childrenProp_"+nextVal).children().find("select");
 		     //下拉框
 	    	 $j(selects).each(function(i) {
 	    	   var select = this;
 	    	   $j(aimhtml).find("select").eq(i).val($j(select).val());
 	    	 });
 	    	 //textarea
 	    	 var textareas = $j("#childrenProp_"+nextVal).children().find("textarea");
 	 		
 	    	 $j(textareas).each(function(i) {
 	    	   var textarea = this;
 	    	   $j(aimhtml).find("textarea").eq(i).val($j(textarea).val());
 	    	 });
 	    	 
 	    	 
		   $j(".childrenPropDialogContent").html(aimhtml);
 		  //找出当前子节点最大序列号
		   
 		  var childPropSortNOValue=$j("#childPropSortNO_"+nextVal).val();
 		  if (typeof  childPropSortNOValue== "undefined"){
 			 j=0;
 		  }else{
 			 j=childPropSortNOValue;
 		  }
 		   //open
		   $j("#childrenProp-dialog").dialogff({type:'open',close:'in',width:'1000px',height:'450px'});
	   });
 	   
 	   
 	  /**
 	   * 确认‘编辑’
 	   */
 	    $j("#dialogBody").on("click",".chiProSelok",function(){
 	     	//验证
 	    	childPropertyValidate();
 	    	if(flag){
	    	
 			 var currentNOValue=$j("#currentNO").val();
 			 /**
 			 if($j("#isWhatType_"+currentNOValue).val()==2){
 		    	 if($j('.ui-block-content.childrenPropDialogContent').find('.ui-block-content.border-grey').size()>1 ){
 		    		 return nps.info(nps.i18n("DIALOG.MANAGER.TIPS"),nps.i18n("DIALOG.ADD.MUSTBESINGLE"));
 		          }
 		      }**/
 	    	
 	    	 //克隆当前节点的子节点到隐藏域
 	    	 var sorchtml=$j(".childrenPropDialogContent").children().clone();
 	    	
 	    	 //下拉框
 	    	var selects = $j(".childrenPropDialogContent").children().find("select");
 	    	 $j(selects).each(function(i) {
 	    	   var select = this;
 	    	   $j(sorchtml).find("select").eq(i).val($j(select).val());
 	    	 });
 	    	 //textarea
 	    	var textareas = $j(".childrenPropDialogContent").children().find("textarea");
 	    	 $j(textareas).each(function(i) {
 	 	    	   var textarea = this;
 	 	    	   $j(sorchtml).find("textarea").eq(i).val($j(textarea).val());
 	 	     });
 	    	 
 	    	$j("#childrenProp_"+currentNOValue).html(sorchtml);
 	    	 
 	    	  //标志当前子节点最大序列号
 	    	 var childPropSortNOValue=$j("#childPropSortNO_"+currentNOValue).val();
 	    	
	    	  if (typeof  childPropSortNOValue== "undefined"){
	    		  $j("#childrenProp_"+currentNOValue).append("<input type='hidden' id='childPropSortNO_"+currentNOValue+"'  name='childPropSortNO_"+currentNOValue+"' value='"+j+"' />");
	 		  }else{
	 			 $j("#childPropSortNO_"+currentNOValue).val(j);
	 		  }
 	    	
 	         //close
 	    	 $j("#childrenProp-dialog").dialogff({
 				type : 'close'
 			 });
 	    	}
 		 });
 	    
 	   /**
 	    * 关闭‘编辑’
 	    */
 	    $j("#dialogBody").on("click",".chiProSelCancel",function(){
 	    	$j("#childrenProp-dialog").dialogff({
 				type : 'close'
 			});
 		 });
 	   
 	     /**
 	      * 显示隐藏属性
 	      */
 	     $j("#dialogBody").on("click",".new-list-add.hideOrShow",function(){
 	 	   	var obj =$j(this).parent().parent().parent().children().eq(2);
 	 	    var prependcontent=$j(this).parent().parent("#top").siblings("#content");
 	 	   	if(obj.is(":hidden")){
 	 	   		prependcontent.prepend("<div class='ui-block-line'></div>");
 	 	   		$j(this).siblings(".attr-name-ds").addClass("ml0").detach().prependTo(prependcontent.find(".ui-block-line").eq(0));
 	 	   		obj.show();
 	 	   	}else{
 	 	   		var atsds=prependcontent.find(".attr-name-ds");
 	 	   		atsds.detach().removeClass("ml0").insertAfter($j(this));
 	 	   		prependcontent.find(".ui-block-line").eq(0).remove();
 	 	   		obj.hide();
 	 	   	}
 	 	 });
 	     
      /**
 	    * 删除属性
 	    */
 	 	$j("#dialogBody").on("click",".new-list-add.delete",function(){
 	 		var currentProperty =$j(this);
 	 		nps.confirm(nps.i18n("DIALOG.MANAGER.TIPS"),nps.i18n("DIALOG.DELETE.COMFIRM"), function(){
 	 		  var obj =currentProperty.parent().parent().parent().parent();
 	     	  obj.remove();
 	 		 });
 	 	 });
 	 	
	   /**
	    * 增加属性
	    */
	   $j("#dialogBody").on("click",".addProp",function(){

		    //验证
	       propertyValidate();
	       if(flag){
		   //如果必填信息填写完整则收缩
		   $j("#dialogBody").find(".new-list-add.hideOrShow").each(function(){
	    		var obj =$j(this).parent().parent().parent().children().eq(2);
	    		if(!(obj.is(":hidden"))){
		    		$j(this).click();
	    		}
	    	});
		   var propSortMaxNO=  $j("#propSortMaxNO").val();
            if(typeof  propSortMaxNO!= "undefined" && i==0){
            	i=propSortMaxNO;
            }

	    	i++;
	    	//属性编辑器
	    	var editerSelectHtml= $j("#editerSelectHtml").html()+'';
	    	editerSelectHtml=editerSelectHtml.replace("\n", "")
	    	.replace("fields[0]","fields["+i+"]");

	        //是否必须
	    	var requiredSelectHtml= $j("#requiredSelectHtml").html()+'';
	    	requiredSelectHtml=requiredSelectHtml.replace("\n", "")
	    	.replace("fields[0]","fields["+i+"]");
	    	
	    	//属性类型
	    	var typeSelectHtml= $j("#typeSelectHtml").html()+'';
	    	typeSelectHtml=typeSelectHtml.replace("\n", "")
	    	.replace("fields[0]","fields["+i+"]");

	    	
	    	
	    	var html=$j('<div class="ui-block-content border-grey" style="margin-bottom:1'+i+'px;">'+
				    '<div class="ui-block-line ">'+
		             '<label class="">'+nps.i18n("DIALOG.MANAGE.PROP")+i+'</label>'+
					 '<div id="top">'+
					       '<div class="ui-block-line">'+
							    '<label>'+nps.i18n("DIALOG.MANAGE.CODE")+'　</label>'+
							    '<input class="fLeft"  type="text" id="input1" name="fields['+i+'].code" loxiaType="input" value="" mandatory="true" size="5'+i+'" placeHolder=""/>'+
						        '<span class="new-list-add delete fRight display-inline width-auto clear-none mt0 ml5">'+nps.i18n("DIALOG.MANAGE.DELETE")+'</span>'+
                               '<span class="new-list-add hideOrShow fRight display-inline width-auto clear-none mt0">'+nps.i18n("DIALOG.MANAGE.SHOW.HIDE")+'</span>'+
                               
                           '</div>'+
                     '</div>'+

                    '<div id="content">'+
                       '<div class="ui-block-line">'+
		                    '<div class="display-inline fLeft attr-name-ds ml0">'+
		                         '<label>'+nps.i18n("DIALOG.MANAGE.PROP.NAME")+'　</label>'+
							     '<input type="text" id="input2" name="fields['+i+'].fieldName"  loxiaType="input" value="" mandatory="true" size="50" placeHolder=""/>'+
				   		    '</div>'+
		   	           '</div>'+
                    
					   '<div class="ui-block-line">'+
						   '<label>'+nps.i18n("DIALOG.MANAGE.PROP.TYPE")+'　</label>'+
						   typeSelectHtml+
						   '<a id="add_pro" href="javascript:void(0)" class="func-button ml5 none">'+nps.i18n("DIALOG.MANAGE.EDIT")+'</a>'+
						   '<input type="hidden" id="propSortNO" class="propSortNO"  name="propSortNO" value="'+i+'" />'+
					   '</div>'+

					   '<div class="ui-block-line">'+
						   '<label>'+nps.i18n("DIALOG.MANAGE.PROP.EDITOR")+' </label>'+
						   editerSelectHtml+
					   '</div>'+
					  
					   '<div class="ui-block-line">'+
						   '<label>'+nps.i18n("DIALOG.MANAGE.REQUIRED")+'  </label>'+
						   requiredSelectHtml+
					   '</div>'+
					   
						'<div class="ui-block-line">'+
						   '<label>'+nps.i18n("DIALOG.MANAGE.GROUP")+'  </label>'+
						   '<input  name="fields['+i+'].group" type="text"  loxiaType="number"  value="1" size="50" placeHolder=""/>'+
					   '</div>'+
					   '<div class="ui-block-line">'+
						   '<label>'+nps.i18n("DIALOG.MANAGE.SORT")+'  </label>'+
						   '<input  name="fields['+i+'].sortNo" type="text"   loxiaType="number"  value="'+i+'" size="50" placeHolder=""/>'+
					   '</div>'+
					   
					   '<div class="ui-block-line">'+
						   '<label>'+nps.i18n("DIALOG.MANAGE.PROP.DESC")+'</label>'+
						   '<textarea name="fields['+i+'].desc" type="text"  rows="2" placeholder="'+nps.i18n("DIALOG.MANAGE.DESC")+'" style="width:500px;" class="ui-loxia-default ui-corner-all" aria-disabled="false"></textarea>'+
					   '</div>'+
					 	
		             '</div>'+
		         '</div>'+
		         '<div id="childrenProp_'+i+'" class="proto-dialog">'+
		             '<div class="new-list-add addChildrenProp" style="width:100%;">'+
		             '<input type="hidden" id="isWhatType_'+i+'"  name="isWhatType_'+i+'" value="" />'+
                     '<span >+'+nps.i18n("DIALOG.MANAGE.ADD")+'</span>'+
                     '</div>'+
			     '</div>'+
		   '</div>');
	    	
	    	loxia.initContext(html[0]);
	    	$j(this).before(html[0]);
	       }
	    });
	   
	   /**
	    *增加‘子属性’
	    */
	   $j("#dialogBody").on("click",".addChildrenProp",function(){
		   //验证
	       childPropertyValidate();

           if(flag){
		   //如果必填信息填写完整则收缩
		   $j("#childrenProp-dialog").find(".new-list-add.hideOrShow").each(function(){
	    		var obj =$j(this).parent().parent().parent().children().eq(2);
	    		if(!(obj.is(":hidden"))){
		    		$j(this).click();
	    		}
	    	});
		   
		    var cval=$j("#currentNO").val();
		    /**
		    if($j("#isWhatType_"+cval).val()==2){
		    	 if($j('.ui-block-content.childrenPropDialogContent').find('.ui-block-content.border-grey').size()>0 ){
		    		 return nps.info(nps.i18n("DIALOG.MANAGER.TIPS"),nps.i18n("DIALOG.ADD.MUSTBESINGLE"));
		          }
		    }
		    **/
	    	j++;
	    	//属性编辑器
	    	var editerChildSelectHtml= $j("#editerChildSelectHtml").html()+'';
	    	editerChildSelectHtml=editerChildSelectHtml.replace("\n", "")
	    	.replace("fields[0]","fields["+cval+"]").replace("children[0]","children["+j+"]");
	        //是否必须
	    	var requiredChildSelectHtml= $j("#requiredChildSelectHtml").html()+'';
	    	requiredChildSelectHtml=requiredChildSelectHtml.replace("\n", "")
	    	.replace("fields[0]","fields["+cval+"]").replace("children[0]","children["+j+"]");
	    	
	    	var html=$j('<div class="ui-block-content border-grey" style="margin-bottom:10px;">'+
				    '<div class="ui-block-line ">'+
					             '<label class="">'+nps.i18n("DIALOG.MANAGE.CHILD.PROP")+j+'</label>'+
								 '<div id="top">'+
								       '<div class="ui-block-line">'+
										    '<label>'+nps.i18n("DIALOG.MANAGE.CODE")+'　</label>'+
										    '<input class="fLeft" type="text" id="input3" name="fields['+cval+'].children['+j+'].code" loxiaType="input" value="" mandatory="true" size="50" placeHolder=""/>'+
									        '<span class="new-list-add delete fRight display-inline width-auto clear-none mt0 ml5">'+nps.i18n("DIALOG.MANAGE.DELETE")+'</span>'+
			                                '<span class="new-list-add hideOrShow fRight display-inline width-auto clear-none mt0">'+nps.i18n("DIALOG.MANAGE.SHOW.HIDE")+'</span>'+
									  '</div>'+
		                          '</div>'+
	
	                             '<div id="content">'+  	                             
								   '<div class="ui-block-line">'+
								       '<div class="display-inline fLeft attr-name-ds ml0">'+
									     '<label>'+nps.i18n("DIALOG.MANAGE.PROP.NAME")+'　</label>'+
										 '<input type="text" id="input4" name="fields['+cval+'].children['+j+'].fieldName"  loxiaType="input" value="" mandatory="true" size="50" placeHolder=""/>'+
										'</div>'+	
									'</div>'+											                          
	
								   '<div class="ui-block-line">'+
									   '<label>'+nps.i18n("DIALOG.MANAGE.PROP.EDITOR")+' </label>'+
									   editerChildSelectHtml+
								   '</div>'+
								  
								   '<div class="ui-block-line">'+
								      '<label>'+nps.i18n("DIALOG.MANAGE.REQUIRED")+'  </label>'+
								      requiredChildSelectHtml+
							       '</div>'+
								 
									'<div class="ui-block-line">'+
									   '<label>'+nps.i18n("DIALOG.MANAGE.GROUP")+'  </label>'+
									   '<input  name="fields['+cval+'].children['+j+'].group" type="text"  loxiaType="number"  value="1" size="50" placeHolder=""/>'+
								   '</div>'+
								   '<div class="ui-block-line">'+
									   '<label>'+nps.i18n("DIALOG.MANAGE.SORT")+'  </label>'+
									   '<input  name="fields['+cval+'].children['+j+'].sortNo" type="text"   loxiaType="number"  value="'+j+'" size="50" placeHolder=""/>'+
								   '</div>'+
								   
								   '<div class="ui-block-line">'+
									   '<label>'+nps.i18n("DIALOG.MANAGE.PROP.DESC")+'</label>'+
									   '<textarea  name="fields['+cval+'].children['+j+'].desc" type="text" rows="2" placeholder="'+nps.i18n("DIALOG.MANAGE.DESC")+'" style="width:500px;" class="ui-loxia-default ui-corner-all" aria-disabled="false"></textarea>'+
								   '</div>'+
					             '</div>'+
					        
					    '</div>'+
		    
		        '</div>');
	    	
	    	
	    	loxia.initContext(html[0]);
	    	$j(this).before(html[0]);
           }
	    });


});