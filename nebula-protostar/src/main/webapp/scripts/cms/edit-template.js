$j.extend(loxia.regional['zh-CN'],{
	"PROMPT_INFO":"提示信息",
	"EDIT_AREA_CODE_NOT_EXISTS":"该区域的code不存在",
	"EDIT_AREA_DATA_NOT_EMPTY":"页面编辑区域的数据不可以为空",
	"NO_SAVE_NOT_PREVIEW":"数据有改动,请先保存,再浏览"
	
});
/**
 * 获取编辑对象
 * @param editObj
 * @returns
 */
function getEditWay(obj){
	if(obj.hasClass("cms-imgarticle-edit") || obj.hasClass("cms-html-edit") || obj.hasClass("cms-product-edit")){
		return  obj;
	}else{
		return getEditWay(obj.parent());
	}
}
/**
 * 验证输入是否为空
 * @param cls
 * @returns {Boolean}
 */
function validateInput(cls){
	var result = true;
	if(cls==null || cls==""){
		return result; 
	}
	$j("."+cls).each(function(){
		var val =$j(this).val();
		if(val!=null && val!="" && $j(this).hasClass("ui-loxia-error")){
			$j(this).removeClass("ui-loxia-error");
		}
		if($j(this).hasClass("ui-loxia-error")){
			result = false;
		}
		if(val==null || val==""){
			$j(this).addClass("ui-loxia-error");
			result = false;
		}
		
	 });
	return result; 
};

function findEditMode(dom){
	var tagName = dom.get(0).tagName;
	var result={};
	if(dom.hasClass("cms-html-edit")){
		result.cls= "cms-html-edit";
		result.obj=dom;
		return result;
	}
	if(dom.hasClass("cms-product-edit")){
		result.cls= "cms-product-edit";
		result.obj=dom;
		return result;
	}
	if(dom.hasClass("cms-imgarticle-edit")){
		result.cls= "cms-imgarticle-edit";
		result.obj=dom;
		return result;
	}
	if(tagName != "HTML"){
		return findEditMode(dom.parent());
	}else{
		return null;
	}
};
var infoFlag = true;
function  showHideTag(dialog,dom){
	//dom = dom.parent();
	if(dom.get(0).tagName=="A" && dom.find("img").length==0){
		if(dom.hasClass("cms-area-title")){
			dialog.find(".title").hide();
			dialog.find(".retitle").show();
		}else{
			dialog.find(".title").show();
			dialog.find(".retitle").hide();
		}
		if(dom.hasClass("cms-area-href")){
			dialog.find(".thref").hide();
			dialog.find(".rethref").show();
		}else{
			dialog.find(".thref").show();
			dialog.find(".rethref").hide();
		}
//		dialog.find(".title").show();
//		dialog.find(".thref").show();
//		dialog.find(".retitle").show();
//		dialog.find(".rethref").show();
	}else{
		dialog.find(".title").hide();
		dialog.find(".thref").hide();
		dialog.find(".retitle").hide();
		dialog.find(".rethref").hide();
		
	}
	if(dom.children().length>0 && dom.children().length>0 &&
			dom.get(0).tagName=="A" && 
			dom.find("img").length > 0){
		if(dom.hasClass("cms-area-href")){
			dialog.find(".ihref").hide();
			dialog.find(".reihref").show();
		}else{
			dialog.find(".ihref").show();
			dialog.find(".reihref").hide();
		}
		
		if(dom.find("img").hasClass("cms-area-img")){
			dialog.find(".img").hide();
			dialog.find(".reimg").show();
		}else{
			dialog.find(".img").show();
			dialog.find(".reimg").hide();
		}
//		dialog.find(".img").show();
//		dialog.find(".ihref").show();
//		dialog.find(".reimg").show();
//		dialog.find(".reihref").show();
	}else{
		dialog.find(".img").hide();
		dialog.find(".ihref").hide();
		dialog.find(".reimg").hide();
		dialog.find(".reihref").hide();
	}
	if((!dom.hasClass(".cms-area-href") &&
			!dom.hasClass(".cms-area-title")&&
			!dom.hasClass(".cms-area-desc")&&
			!dom.hasClass(".cms-area-img") &&
			dom.find(".cms-area-href").length==0 &&
			dom.find(".cms-area-title").length==0&&
			dom.find(".cms-area-desc").length==0&&
			dom.find(".cms-area-img").length==0 &&
			dom.children().length>0 &&
			dom.children().get(0).tagName != "IMG") ||(dom.children().length==0)){
		
		if(dom.children().length==0 && dom.children().children().length==0 && 
				dom.get(0).tagName != "A" && dom.get(0).tagName!="IMG" && !dom.parent().hasClass("cms-imgarticle-edit")){
			if(dom.hasClass("cms-area-desc")){
				dialog.find(".desc").hide();
				dialog.find(".redesc").show();
			}else{
				dialog.find(".desc").show();
				dialog.find(".redesc").hide();
			}
//			dialog.find(".desc").show();
//			dialog.find(".redesc").show();
		}else{
			if(dom.children().length==0 && dom.children().children().length==0 && 
					dom.get(0).tagName != "A" && dom.get(0).tagName!="IMG" &&dom.hasClass("cms-area-desc")){
				dialog.find(".desc").hide();
				dialog.find(".redesc").show();
			}else if(dom.children().length==0 && dom.children().children().length==0 && 
					dom.get(0).tagName != "A" && dom.get(0).tagName!="IMG" ){
				dialog.find(".desc").show();
				dialog.find(".redesc").hide();
			}else{
				dialog.find(".desc").hide();
				dialog.find(".redesc").hide();
			}
		}
	}else{
		if(dom.children().length==0 && dom.children().children().length==0 && 
				dom.get(0).tagName != "A" && dom.get(0).tagName!="IMG" &&dom.hasClass("cms-area-desc")){
			dialog.find(".desc").hide();
			dialog.find(".redesc").show();
		}else if(dom.children().length ==0 && dom.children().children().length==0 && 
				dom.get(0).tagName != "A" && dom.get(0).tagName!="IMG"){
			dialog.find(".desc").show();
			dialog.find(".redesc").hide();
		}else{
			dialog.find(".desc").hide();
			dialog.find(".redesc").hide();
		}
	}
	//cms-area-href cms-area-title
	//cms-area-desc cms-area-img
//	if(dom.parent().hasClass("cms-imgarticle-edit")){
//		if(dom.hasClass("cms-area-list-element")){
//			dialog.find(".list").hide();
//			dialog.find(".relist").show();
//		}else{
//			dialog.find(".list").show();
//			dialog.find(".relist").hide();
//		}
////		dialog.find(".list").show();
////		dialog.find(".relist").show();
//	}else{
//		dialog.find(".list").hide();
//		dialog.find(".relist").hide();
//	}
	var btns =dialog.find(".proto-dialog-content").find("input[type='button']");
	btns.each(function(i,dom){
		var me =$j(dom);
		var display = me.css("display");
		if(display!="none"){
			infoFlag =false;
		}
	});
};
var wapperRecord=new Array();
var selectRecord=new Array();
function setSelectArea(cls,cls_prefix){
	var me = editObj;
	var record = {};
	record.code = cls_prefix;
	selectRecord.push(record);
	
	var leftCls = "border-area-left-"+cls_prefix+" save-remove"+" "+cls_prefix;
	var rightCls = "border-area-right-"+cls_prefix+" save-remove"+" "+cls_prefix;;
	var topCls = "border-area-top-"+cls_prefix+" save-remove"+" "+cls_prefix;;
	var bottomCls = "border-area-bottom-"+cls_prefix+" save-remove"+" "+cls_prefix;;

	$j(".web-update").contents().find("body")
	.append('<div class="'+leftCls+'" style="clear:both; display:none; position:absolute; width:3px; z-index:90; background:red;"></div>')
	.append('<div class="'+rightCls+'" style="clear:both; display:none; position:absolute; width:3px; z-index:90; background:red;"></div>')
	.append('<div class="'+topCls+'" style="clear:both; display:none; position:absolute; height:3px; z-index:90; background:red;"></div>')
	.append('<div class="'+bottomCls+'" style="clear:both; display:none; position:absolute; height:3px; z-index:90; background:red;"></div>');

	me.addClass("border-area-class");
	if(me.get(0).tagName=="A"){
		if(me.children().length!=0){
			me = me.children().eq(0);
		}
	}
	var thiswidth=parseInt(me.outerWidth());
	var thisheight=parseInt(me.height())+parseInt(me.css("padding-top"))+parseInt(me.css("padding-bottom"));
	var thisleft=parseInt(me.offset().left);
	var thistop=parseInt(me.offset().top);
	
	$j(".web-update").contents().find("body").find(".border-area-left-"+cls_prefix).css({
		"display":"block",
		"height":thisheight,
		"left":thisleft,
		"top":thistop
	});
	
	$j(".web-update").contents().find("body").find(".border-area-right-"+cls_prefix).css({
		"display":"block",
		"height":thisheight,
		"left":thisleft+thiswidth-3,
		"top":thistop
	});
	
	$j(".web-update").contents().find("body").find(".border-area-top-"+cls_prefix).css({
		"display":"block",
		"width":thiswidth,
		"left":thisleft,
		"top":thistop
	});
	
	$j(".web-update").contents().find("body").find(".border-area-bottom-"+cls_prefix).css({
		"display":"block",
		"width":thiswidth,
		"left":thisleft,
		"top":thistop+thisheight-3
	});
	
}
function setWapperBorder(cls){
	var wme= null;
	if(cls!=null && cls!=""){
		wme = $j(".web-update").contents().find(cls);
	}else{
		 wme = editObj;
	}
	if(wme.length == 0){
		return ;
	}
	wme.each(function(i,dom){
		var me = $j(dom);
		var cls_prefix= new Date().getTime();
		var record = {};
		record.code = me.attr("code");
		record.rmCls= cls_prefix;
		wapperRecord.push(record);
		
		var leftCls = "border-area-left-"+cls_prefix+" save-remove"+" "+cls_prefix;
		var rightCls = "border-area-right-"+cls_prefix+" save-remove"+" "+cls_prefix;;
		var topCls = "border-area-top-"+cls_prefix+" save-remove"+" "+cls_prefix;;
		var bottomCls = "border-area-bottom-"+cls_prefix+" save-remove"+" "+cls_prefix;;
	
		$j(".web-update").contents().find("body")
		.append('<div class="'+leftCls+'" style="clear:both; display:none; position:absolute; width:3px; z-index:90; background:rgb(13, 103, 128);"></div>')
		.append('<div class="'+rightCls+'" style="clear:both; display:none; position:absolute; width:3px; z-index:90; background:rgb(13, 103, 128);"></div>')
		.append('<div class="'+topCls+'" style="clear:both; display:none; position:absolute; height:3px; z-index:90; background:rgb(13, 103, 128);"></div>')
		.append('<div class="'+bottomCls+'" style="clear:both; display:none; position:absolute; height:3px; z-index:90; background:rgb(13, 103, 128);"></div>');
	
		me.addClass("border-area-class");
		if(me.get(0).tagName=="A"){
			if(me.children().length!=0){
				me = me.children().eq(0);
			}
		}
		var thiswidth=parseInt(me.outerWidth());
		var thisheight=parseInt(me.height())+parseInt(me.css("padding-top"))+parseInt(me.css("padding-bottom"));
		var thisleft=parseInt(me.offset().left);
		var thistop=parseInt(me.offset().top);
		
		$j(".web-update").contents().find("body").find(".border-area-left-"+cls_prefix).css({
			"display":"block",
			"height":thisheight,
			"left":thisleft,
			"top":thistop
		});
		
		$j(".web-update").contents().find("body").find(".border-area-right-"+cls_prefix).css({
			"display":"block",
			"height":thisheight,
			"left":thisleft+thiswidth-3,
			"top":thistop
		});
		
		$j(".web-update").contents().find("body").find(".border-area-top-"+cls_prefix).css({
			"display":"block",
			"width":thiswidth,
			"left":thisleft,
			"top":thistop
		});
		
		$j(".web-update").contents().find("body").find(".border-area-bottom-"+cls_prefix).css({
			"display":"block",
			"width":thiswidth,
			"left":thisleft,
			"top":thistop+thisheight-3
		});
	});
	
}
//设置选择工具
function setSelectTool(){
	$j(".web-update").contents().find("body")
	.append('<div class="border-area-left" style="clear:both; display:none; position:absolute; width:3px; z-index:100; background:red;"></div>')
	.append('<div class="border-area-right" style="clear:both; display:none; position:absolute; width:3px; z-index:100; background:red;"></div>')
	.append('<div class="border-area-top" style="clear:both; display:none; position:absolute; height:3px; z-index:100; background:red;"></div>')
	.append('<div class="border-area-bottom" style="clear:both; display:none; position:absolute; height:3px; z-index:100; background:red;"></div>');
	//添加选择功能
	$j(".web-update").contents().find("*").mouseenter(function(e){
		e.stopPropagation();
		$j(".web-update").contents().find("*").removeClass("border-area-class");
		$j(this).addClass("border-area-class");
		
		var thiswidth=parseInt($j(this).outerWidth());
		var thisheight=parseInt($j(this).height())+parseInt($j(this).css("padding-top"))+parseInt($j(this).css("padding-bottom"));
		var thisleft=parseInt($j(this).offset().left);
		var thistop=parseInt($j(this).offset().top);
		
		$j(".web-update").contents().find("body").find(".border-area-left").css({
			"display":"block",
			"height":thisheight,
			"left":thisleft,
			"top":thistop
		});
		
		$j(".web-update").contents().find("body").find(".border-area-right").css({
			"display":"block",
			"height":thisheight,
			"left":thisleft+thiswidth-3,
			"top":thistop
		});
		
		$j(".web-update").contents().find("body").find(".border-area-top").css({
			"display":"block",
			"width":thiswidth,
			"left":thisleft,
			"top":thistop
		});
		
		$j(".web-update").contents().find("body").find(".border-area-bottom").css({
			"display":"block",
			"width":thiswidth,
			"left":thisleft,
			"top":thistop+thisheight-3
		});
	});
};

/**
 * 检查当前编辑项是否已经设置
 */
function checkExist(cls,type){
	if(editObj.hasClass("cms-area-list-element")){
		var ele = editObj.find(cls);
		if(ele.length == 0){
			return false;
		}
		if(type="htitle"){
			if(ele.get(0).tagName == "A"){
				if(editObj.hasClass(cls)){
					return  true;
				}
			}
		}
		if(type == "himg"){
			if(ele.get(0).tagName == "A"){
				if(editObj.hasClass(cls)){
					return  true;
				}
			}
		}
		if(mode.find(cls).length > 0){
			return  true;
		}
	}
	var mode = editObj.parents(".cms-area-list-element");
	if(mode.length == 0){
		mode = editObj.parents(".cms-imgarticle-edit");
	}
	var ele = mode.find(cls);
	if(ele.length == 0){
		return false;
	}
	if(type="htitle"){
		if(ele.get(0).tagName == "A"){
			if(editObj.hasClass(cls)){
				return  true;
			}
		}
	}
	if(type == "himg"){
		if(ele.get(0).tagName == "A"){
			if(editObj.hasClass(cls)){
				return  true;
			}
		}
	}
	return false;
}
//编辑对象
var editObj;
var isSave= true;
var deleditObj = null;
$j(window).load(function(){
	//设置iframe高
	$j(".web-update").css({"height":$j(".web-update").contents().find("body").height()});
	//设置选择工具
	setSelectTool();
	//点击事件
	$j(".web-update").contents().find("body").on("click",".border-area-class",function(dom){
		var me = null;
		if(deleditObj!=null){
			me = deleditObj;
			deleditObj = null;
		}else{
			me = $j(this);
		}
		if(me.find(".cms-html-edit").length>0){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "该区域下已经设置了纯编辑html方式");
			return;
		}
		if(me.find(".cms-imgarticle-edit").length>0){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "该区域下已经设置了表单编辑方式");
			return;
		}
		if(me.find(".cms-product-edit").length>0){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "该区域下已经设置了商品编辑方式");
			return;
		}
		var  cobj = me;
		if(me.get(0).tagName=="IMG"){
			me = $j(this).parent();
		}
		if(me.html()==null || me.html()==""){
			return;
		}
		var result = findEditMode(me);
		
		editObj = me;
		var code = editObj.attr("code");
		var dialog_mode = $j(".cms-tmp-edit-dialog");
		var width = 800;
		var height = 500;
		if(code!=null && code!=""){
			dialog_mode.find(".del").show();
			dialog_mode.find(".update").show();
			dialog_mode.find(".expand").hide();
			dialog_mode.find(".info").html("当前区域为可编辑状态<br>在修改编辑区域时只允许修改实例编辑方式，无法扩大选区。只有通过先“去除编辑状态”后重新设置编辑区域才能够实现。");
			dialog_mode.find(".editway").hide();
			dialog_mode.find(".onlycode").hide();
			dialog_mode.find(".confrim").hide();
			width = 400;
			height = 200;
			$j(".resubList").hide();
			$j(".subList").hide();
		}else{
			dialog_mode.find(".del").hide();
			dialog_mode.find(".expand").show();
			dialog_mode.find(".editway").show();
			dialog_mode.find(".onlycode").show();
			dialog_mode.find(".confrim").show();
			dialog_mode.find(".update").hide();
			dialog_mode.find(".info").html("");
		}
		if(!me.hasClass("cms-html-edit") &&! me.hasClass("cms-product-edit")
				&& !me.hasClass("cms-imgarticle-edit") && result != null){
			mode = result.cls;
			if(mode=="cms-html-edit"){
				dialog_mode.find(".del").show();
				dialog_mode.find(".update").show();
				dialog_mode.find(".expand").hide();
				dialog_mode.find(".info").html("当前区域为可编辑状态<br>在修改编辑区域时只允许修改实例编辑方式，无法扩大选区。只有通过先“去除编辑状态”后重新设置编辑区域才能够实现。");
				dialog_mode.find(".editway").hide();
				dialog_mode.find(".onlycode").hide();
				dialog_mode.find(".confrim").hide();
				width = 400;
				height = 200;
				$j(".resubList").hide();
				$j(".subList").hide();
				dialog_mode.find(".update").hide();
				dialog_mode.find(".html").val(me.prop("outerHTML"));
				dialog_mode.find(".code").val(code);
				if(mode=="cms-imgarticle-edit"){
					dialog_mode.find(".mode option[value='3']").attr("selected",true);
					if(me.hasClass("cms-area-list-element") || me.children().hasClass("cms-area-list-element")){
						$j(".subList").hide();
						$j(".resubList").show();
					}else{
						$j(".subList").show();
						$j(".resubList").hide();
					}
					dialog_mode.find(".update").show();
				}else{
					dialog_mode.find(".mode option[value='1']").attr("selected",true);
					$j(".subList").hide();
					$j(".resubList").hide();
				}
				if(me.children().length<=1){
					$j(".resubList").hide();
					$j(".subList").hide();
				}
				dialog_mode.dialogff({type:'open',close:'in',width:width, height:height});
			}else if(mode=="cms-product-edit"){
//				if(me.hasClass("cms-area-list-element") || me.find(".cms-area-list-element").length>0){
//					nps.info(nps.i18n("INFO_TITLE_DATA"), "列表区域不能编辑");
//					return;
//				}
				var product_dialog= $j(".cms-product-edit-dialog");
				product_dialog.find(".html").val(me.prop("outerHTML"));
				//处理商品模式
				var modeObj = cobj.parents(".cms-area-list-element");
				var type ;
				if(modeObj.length == 0){
					type = cobj.parents(".cms-product-edit").children().attr("img-type");
				}else{
					type = modeObj.attr("img-type");
				}
				product_dialog.find(".img-type option[value='"+type+"']").attr("selected",true);
				setItemEidt(cobj,product_dialog);
				product_dialog.dialogff({type:'open',close:'in',width:800, height:550});
			}else if(mode=="cms-imgarticle-edit"){
//				if(me.hasClass("cms-area-list-element") || me.find(".cms-area-list-element").length>0){
//					nps.info(nps.i18n("INFO_TITLE_DATA"), "列表区域不能编辑");
//					return;
//				}
				var dialog = $j(".cms-imgArticle-edit-dialog");
				$j(".subList").hide();
				$j(".resubList").hide();
				showHideTag(dialog,me);
				var modeObj = getEditWay(me);
				if(modeObj.find(".cms-area-list-element").length>0){
					dialog.find(".setSame").show();
				}else{
					dialog.find(".setSame").hide();
				}
				if(infoFlag){
					dialog.find(".setSame").hide();
					dialog.find(".editSetting").hide();
				}else{
					dialog.find(".setSame").show();
					dialog.find(".editSetting").show();
				}
				var peditObj =getEditWay(editObj);
				if(peditObj.find(".cms-area-list-element").length==0){
					dialog.find(".setSame").hide();
				}else{
					dialog.find(".setSame").show();
				}
				infoFlag = true;
				dialog.find(".html").val(me.prop("outerHTML"));
				dialog.dialogff({type:'open',close:'in',width:'800px', height:'550px'});
			}else{
				dialog_mode.find(".html").val(me.prop("outerHTML"));
				dialog_mode.find(".code").val(code);
				if(mode=="cms-imgarticle-edit"){
					dialog_mode.find(".mode option[value='3']").attr("selected",true);
				}else{
					dialog_mode.find(".mode option[value='1']").attr("selected",true);
				}
				if(me.children().length==0){
					$j(".resubList").hide();
					$j(".subList").hide();
				}
				dialog_mode.dialogff({type:'open',close:'in',width:width, height:height});
			}
		}else{
			dialog_mode.find(".html").val(me.prop("outerHTML"));
			if(result != null){
				mode = result.cls;
				if(mode=="cms-imgarticle-edit"){
					dialog_mode.find(".mode option[value='3']").attr("selected",true);
					$j(".subList").hide();
					$j(".resubList").hide();
				}else{
					dialog_mode.find(".mode option[value='1']").attr("selected",true);
					$j(".subList").hide();
					$j(".resubList").hide();
				}
			}else{
				if(dialog_mode.find(".mode").val()==3){
					$j(".subList").show();
				}else{
					$j(".subList").hide();
				}
			}
			dialog_mode.find(".code").val(code);
			if(me.children().length < 1){
				$j(".resubList").hide();
				$j(".subList").hide();
			}
			dialog_mode.dialogff({type:'open',close:'in',width:width, height:height});
		}
		$j('.cms-tmp-edit-dialog').find(".dialog-close").hide();
	});
	
	$j(".web-update").contents().find("a").each(function(i,dom){
		var me = $j(this);
		me.attr("href-bak",me.attr("href"));
		me.attr("href","javascript:void(0)");
		me.attr("onclick-bak",me.attr("onclick"));
		me.attr("onclick","");
		//target="_blank"
		me.attr("target-bak",me.attr("target"));
		me.attr("target","");
	});
	//修改按钮
	$j('.cms-tmp-edit-dialog').on("click",".update",function(){
		var me = $j(this);
		var dialog = $j('.cms-tmp-edit-dialog');
		dialog.css("width",800);
		dialog.css("height",500);
		dialog.find(".proto-dialog-content").css("height",380);
		dialog.find(".editway").show();
		dialog.find(".onlycode").show();
		dialog.find(".confrim").show();
		dialog.find(".del").hide();
		$j(".subList").hide();
		$j(".resubList").hide();
		if(editObj.hasClass("cms-imgarticle-edit")){
			if(editObj.hasClass("cms-area-list-element") || editObj.children().hasClass("cms-area-list-element")){
				dialog.find(".resubList").show();
				dialog.find(".subList").hide();
			}else{
				dialog.find(".resubList").hide();
				dialog.find(".subList").show();
			}
		}
		me.hide();
		$j(".cms-tmp-edit-dialog").find(".info").html("当前区域为可编辑状态");
	});
	//确定按钮
	$j('.cms-tmp-edit-dialog').on("click",".confrim",function(){
		if(validateInput("ismandatory")==false){
			return;
		}
		var me = $j(this);
		var dialog =  me.parent().parent();
		var code = dialog.find(".code").val();
		var code_bak = code;
		if(code == null || code==""){
			code = new Date().getTime();
		}
		//验证code
		var codes = $j(".web-update").contents().find('html').find("[code]");
		var objCode = editObj.attr("code");
		var error= false;
		codes.each(function(i,dom){
			var me = $j(dom);
			var c= me.attr("code");
			if(typeof(objCode)=="undefined"|| objCode==null 
					|| objCode=="" || objCode != code){
				if(c==code){
					nps.info(nps.i18n("INFO_TITLE_DATA"), "编码已经存在");
					error = true;
					return ;
				}
			}
		});
		if(error){
			return ;
		}
		if(editObj.find(".cms-html-edit").length>0 
				|| editObj.find(".cms-imgarticle-edit").length>0
				|| editObj.find(".cms-product-edit").length>0){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "该区域的子元素已经存在可编辑区域，不允许重叠，您可以取消对应区域的编辑状态");
			return;
		}
		editObj.attr("code",code);
		var mode = dialog.find(".mode").val();
		//删除原有class
		editObj.removeClass("cms-html-edit");
		editObj.removeClass("cms-product-edit");
		editObj.removeClass("cms-imgarticle-edit");
		
		editObj.removeClass("cms-area-href");
		editObj.removeClass("cms-area-title");
		editObj.removeClass("cms-area-desc");
		editObj.removeClass("cms-area-img");
		
		//删除下面编辑class
		//cms-area-href cms-area-title
		//cms-area-desc cms-area-img
		editObj.find(".cms-area-href").removeClass("cms-area-href");
		editObj.find(".cms-area-title").removeClass("cms-area-title");
		editObj.find(".cms-area-desc").removeClass("cms-area-desc");
		editObj.find(".cms-area-img").removeClass("cms-area-img");
		// 删除商品模式相关class
		//delProductCls(editObj);
		if(mode=="1"){
			editObj.addClass("cms-html-edit");
		}
		if(mode=="2"){
			var brk =  false;
			if(editObj.children().length==0){
				brk= true;
				nps.info(nps.i18n("INFO_TITLE_DATA"), "商品方式不支持单个html标签的编辑,请扩大选区");
			}
			if(brk){
				editObj.removeAttr("code");
				return;
			}
			editObj.addClass("cms-product-edit");
		}
		if(mode=="3"){
			var brk =  false;
			if(editObj.get(0).tagName=="A" && editObj.children().length==1){
				if(editObj.children().get(0).tagName=="IMG"){
					editObj.addClass("cms-area-href");
					editObj.children().addClass("cms-area-img");
					nps.info(nps.i18n("INFO_TITLE_DATA"), "选择区域符合表单方式(图片)并设置完成");
				}
				
			}else if(editObj.get(0).tagName=="A" && editObj.children().length==0){
					//editObj.addClass("cms-area-href");
					//editObj.addClass("cms-area-title");
					brk= true;
					nps.info(nps.i18n("INFO_TITLE_DATA"), "表单方式不支持单个html标签的编辑,请扩大选区");
			}
			if(editObj.children().length==0){
				brk= true;
				nps.info(nps.i18n("INFO_TITLE_DATA"), "表单方式不支持单个html标签的编辑,请扩大选区");
			}
			if(brk){
				editObj.removeAttr("code");
				return;
			}
			editObj.addClass("cms-imgarticle-edit");
		}
		var cls_prefix = editObj.attr("expandArea");
		if(typeof(cls_prefix)!="undefined"){
			$j(".web-update").contents().find("."+cls_prefix).remove();
			editObj.removeAttr("expandArea");
		}
		if(selectRecord.length>0){
			for ( var i = 0; i < selectRecord.length; i++) {
				$j(".web-update").contents().find("."+selectRecord[i].code).remove();
			}
		}
	
		$j(".cms-tmp-edit-dialog").dialogff({type : 'close'});
		$j(".web-update").contents().find(".border-area-left").hide();
		$j(".web-update").contents().find(".border-area-right").hide();
		$j(".web-update").contents().find(".border-area-top").hide();
		$j(".web-update").contents().find(".border-area-bottom").hide();
		if(code_bak == null || code_bak==""){
			setWapperBorder(null);
		}
		isSave =false; 
	});
	
	$j('.cms-tmp-edit-dialog').on("click",".del",function(){
		var me =$j(this);
		editObj = getEditWay(editObj);
		editObj.removeClass("cms-area-href");
		editObj.removeClass("cms-area-title");
		editObj.removeClass("cms-area-desc");
		editObj.removeClass("cms-area-img");
		editObj.find(".cms-area-list-element").removeClass("cms-area-list-element");
		editObj.find(".cms-area-href").removeClass("cms-area-href");
		editObj.find(".cms-area-title").removeClass("cms-area-title");
		editObj.find(".cms-area-desc").removeClass("cms-area-desc");
		editObj.find(".cms-area-img").removeClass("cms-area-img");
		// 删除商品模式相关class
		//delProductCls(editObj);
		editObj.removeClass("cms-area-list-element");
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		me.parent().parent().find(".code").val(null);
		var code = editObj.attr("code");
		var record=null;
		for ( var i = 0; i < wapperRecord.length; i++) {
			if(wapperRecord[i].code == code){
				record = wapperRecord[i];
				break;
			}
		}
		if(record!=null){
			$j(".web-update").contents().find("body").find("."+record.rmCls).each(function(i,dom){
				var me =$j(dom);
				me.remove();
			});
		}
		
		editObj.removeAttr("code");
		//删除原有class
		editObj.removeClass("cms-html-edit");
		editObj.removeClass("cms-product-edit");
		editObj.removeClass("cms-imgarticle-edit");
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "去除成功");
		$j(".cms-tmp-edit-dialog").dialogff({type : 'close'});
	});
	//取消
	$j('.cms-tmp-edit-dialog').on("click",".close",function(){
		var cls_prefix = editObj.attr("expandArea");
		$j(".web-update").contents().find("."+cls_prefix).remove();
		if(selectRecord.length>0){
			for ( var i = 0; i < selectRecord.length; i++) {
				$j(".web-update").contents().find("."+selectRecord[i].code).remove();
			}
		}
		editObj.removeAttr("expandArea");
		$j(".web-update").contents().find(".border-area-left").hide();
		$j(".web-update").contents().find(".border-area-right").hide();
		$j(".web-update").contents().find(".border-area-top").hide();
		$j(".web-update").contents().find(".border-area-bottom").hide();
		$j(".cms-tmp-edit-dialog").dialogff({type : 'close'});
	});
	//保存按钮
	$j('.save').on("click",function(){
		//还原链接
		$j(".web-update").contents().find("a").each(function(i,dom){
			var me = $j(this);
			me.attr("href",me.attr("href-bak"));
			me.removeAttr("href-bak");
			me.attr("onclick",me.attr("onclick-bak"));
			me.removeAttr("onclick-bak");
			me.attr("target",me.attr("target-bak"));
			me.removeAttr("target-bak");
		});
		
		$j(".web-update").contents().find(".border-area-class").each(function(i,dom){
			$j(dom).removeClass("border-area-class");
		});
		//border-area-left border-area-right
		//border-area-top border-area-bottom
		$j(".web-update").contents().find("body").find(".border-area-left").remove();
		$j(".web-update").contents().find("body").find(".border-area-right").remove();
		$j(".web-update").contents().find("body").find(".border-area-top").remove();
		$j(".web-update").contents().find("body").find(".border-area-bottom").remove();
		
		$j(".web-update").contents().find("body").find(".save-remove").remove();
		
		var url = base+"/cms/editCmsPageTemplate.json";
		$j(".web-update").contents().find('html').removeAttr("code");
		var data =$j(".web-update").contents().find('html').prop("outerHTML");
		var json ={"id":tmpId,"data":data,"repeatData":repeatData};
	    nps.asyncXhrPost(url, json,{successHandler:function(data, textStatus){
	    	setWapperBorder(".cms-html-edit");
	    	setWapperBorder(".cms-imgarticle-edit");
	    	setWapperBorder(".cms-product-edit");
	    	if(data.isSuccess){
	    		nps.info(nps.i18n("INFO_TITLE_DATA"), "保存成功");
	    		//耍新页面
				window.location.reload();
	    	}else{
	    		nps.info(nps.i18n("INFO_TITLE_DATA"), data.description);
	    	}
	    	$j(".web-update").contents().find("a").each(function(i,dom){
	    		var me = $j(this);
	    		me.attr("href-bak",me.attr("href"));
	    		me.attr("href","javascript:void(0)");
	    		me.attr("onclick-bak",me.attr("onclick"));
	    		me.attr("onclick","");
	    		me.attr("target-bak",me.attr("target"));
	    		me.attr("target","");
	    	});
	    }});
	    isSave = true; 
	  //设置选择工具
		setSelectTool();
	});
	
	//保存按钮
	$j('.save-module').on("click",function(){
		//还原链接
		$j(".web-update").contents().find("a").each(function(i,dom){
			var me = $j(this);
			me.attr("href",me.attr("href-bak"));
			me.removeAttr("href-bak");
			me.attr("onclick",me.attr("onclick-bak"));
			me.removeAttr("onclick-bak");
			me.attr("target",me.attr("target-bak"));
			me.removeAttr("target-bak");
		});
		
		$j(".web-update").contents().find(".border-area-class").each(function(i,dom){
			$j(dom).removeClass("border-area-class");
		});
		//border-area-left border-area-right
		//border-area-top border-area-bottom
		$j(".web-update").contents().find("body").find(".border-area-left").remove();
		$j(".web-update").contents().find("body").find(".border-area-right").remove();
		$j(".web-update").contents().find("body").find(".border-area-top").remove();
		$j(".web-update").contents().find("body").find(".border-area-bottom").remove();
		
		$j(".web-update").contents().find("body").find(".save-remove").remove();
		
		var url = base+"/module/editCmsModuleTemplate.json";
		$j(".web-update").contents().find('html').removeAttr("code");
		var data =$j(".web-update").contents().find('html').prop("outerHTML");
		var json ={"id":tmpId,"data":data,"repeatData":repeatData};
	    nps.asyncXhrPost(url, json,{successHandler:function(data, textStatus){
	    	setWapperBorder(".cms-html-edit");
	    	setWapperBorder(".cms-imgarticle-edit");
	    	if(data.isSuccess){
	    		nps.info(nps.i18n("INFO_TITLE_DATA"), "保存成功");
	    	}else{
	    		nps.info(nps.i18n("INFO_TITLE_DATA"), data.description);
	    	}
	    	$j(".web-update").contents().find("a").each(function(i,dom){
	    		var me = $j(this);
	    		me.attr("href-bak",me.attr("href"));
	    		me.attr("href","javascript:void(0)");
	    		me.attr("onclick-bak",me.attr("onclick"));
	    		me.attr("onclick","");
	    		me.attr("target-bak",me.attr("target"));
	    		me.attr("target","");
	    	});
	    }});
	    isSave = true; 
	  //设置选择工具
		setSelectTool();
	});
	//扩大范围
	$j('.cms-tmp-edit-dialog').on("click",".expand",function(){
		var  editObjBak = editObj;
		editObj = editObj.parent();
		if(editObj.get(0).tagName =="BODY"){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "扩大选区只能到body内");
			editObj = editObjBak;
			return;
		}
		var reexpandArea= editObj.attr("expandArea");
		if(reexpandArea!=null && reexpandArea!=""){
			$j(".web-update").contents().find("."+reexpandArea).remove();
			editObj.removeAttr("expandArea");
		}
		var me = $j(this);
		if(editObj.children().length >= 1){
			if(me.parent().parent().find(".mode").val()==3){
				if(editObj.find(".cms-area-list-element").length>0){
					$j(".resubList").show();
					$j(".subList").hide();
				}else{
					$j(".resubList").hide();
					$j(".subList").show();
				}
				
			}
		}
		$j(".web-update").contents().find(".border-area-left").hide();
		$j(".web-update").contents().find(".border-area-right").hide();
		$j(".web-update").contents().find(".border-area-top").hide();
		$j(".web-update").contents().find(".border-area-bottom").hide();
		if(selectRecord.length>0){
			for ( var i = 0; i < selectRecord.length; i++) {
				$j(".web-update").contents().find("."+selectRecord[i].code).remove();
			}
		}
		var cls_prefix= new Date().getTime();
		editObj.attr("expandArea",cls_prefix);
		setSelectArea(null,cls_prefix);
		$j('.cms-tmp-edit-dialog').find(".html").val(editObj.prop("outerHTML"));
	});
	
	//设置标题
	$j('.cms-imgArticle-edit-dialog').on("click",".title",function(){
		var me =$j(this);
		if(checkExist(".cms-area-title","title")){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "标题已经设置");
			return ;
		}
		addSameSet("cms-area-title","t");
		editObj.addClass("cms-area-title");
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		isSave =false; 
		me.hide();
		me.parent().parent().find(".retitle").show();
		nps.info(nps.i18n("INFO_TITLE_DATA"), "设置标题成功");
	});
	$j('.cms-imgArticle-edit-dialog').on("click",".retitle",function(){
		var me =$j(this);
		editObj.removeClass("cms-area-title");
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		delSameSet("cms-area-title","t");
		isSave =false; 
		me.hide();
		me.parent().parent().find(".title").show();
		nps.info(nps.i18n("INFO_TITLE_DATA"), "删除标题成功");
	});
	
	//设置标题链接
	$j('.cms-imgArticle-edit-dialog').on("click",".thref",function(){
		var me =$j(this);
		if(checkExist(".cms-area-href","htitle")){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "标题链接已经设置");
			return ;
		}
		if(editObj.get(0).tagName=="A"){
			editObj.addClass("cms-area-href");
			me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		}
		addSameSet("cms-area-href","th");
		me.hide();
		me.parent().parent().find(".rethref").show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "设置标题链接成功");
	});
	$j('.cms-imgArticle-edit-dialog').on("click",".rethref",function(){
		var me =$j(this);
		editObj.removeClass("cms-area-href");
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		delSameSet("cms-area-href","th");
		me.hide();
		me.parent().parent().find(".thref").show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "删除标题链接成功");
	});
	//设置图片
	$j('.cms-imgArticle-edit-dialog').on("click",".img",function(){
		var me =$j(this);
		if(checkExist(".cms-area-img","img")){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "图片已经设置");
			return ;
		}
		if(editObj.find("img").length>0){
			editObj.find("img").addClass("cms-area-img");
			me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		}
		if(editObj.get(0).tagName=="IMG"){
			editObj.addClass("cms-area-img");
			me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		}
		addSameSet("cms-area-img","img");
		me.hide();
		me.parent().parent().find(".reimg").show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "设置图片成功");
	});
	$j('.cms-imgArticle-edit-dialog').on("click",".reimg",function(){
		var me =$j(this);
		editObj.find("img").removeClass("cms-area-img");
		editObj.removeClass("cms-area-img");
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		delSameSet("cms-area-img","img");
		me.hide();
		me.parent().parent().find(".img").show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "删除图片成功");
	});
	//设置图片链接
	$j('.cms-imgArticle-edit-dialog').on("click",".ihref",function(){
		var me =$j(this);
		if(checkExist(".cms-area-href","himg")){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "图片链接已经设置");
			return ;
		}
		if(editObj.find("img").length>0 && editObj.get(0).tagName=="A"){
			editObj.addClass("cms-area-href");
			me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		}
		addSameSet("cms-area-href","imgh");
		me.hide();
		me.parent().parent().find(".reihref").show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "设置图片链接成功");
	});
	$j('.cms-imgArticle-edit-dialog').on("click",".reihref",function(){
		var me =$j(this);
		editObj.removeClass("cms-area-href");
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		me.hide();
		delSameSet("cms-area-href","imgh");
		me.parent().parent().find(".ihref").show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "删除图片链接成功");
	});
	//设置描述
	$j('.cms-imgArticle-edit-dialog').on("click",".desc",function(){
		var me =$j(this);
		if(checkExist(".cms-area-desc","desc")){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "描述已经设置");
			return ;
		}
		if(editObj.find("a").length==0 && editObj.find("img").length==0){
			editObj.addClass("cms-area-desc");
			me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		}
		addSameSet("cms-area-desc","desc");
		me.hide();
		me.parent().parent().find(".redesc").show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "设置描述成功");
	});
	$j('.cms-imgArticle-edit-dialog').on("click",".redesc",function(){
		var me =$j(this);
		editObj.removeClass("cms-area-desc");
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		me.hide();
		delSameSet("cms-area-desc","desc");
		me.parent().parent().find(".desc").show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "删除描述成功");
	});
	
	$j('.resubList').on("click",function(){
		var me =$j(this);
		var reList = editObj;
		var error = false; 
		var children = editObj.childrean();
		for ( var i = 0; i < children.length-1; i++) {
			var ch1 =  children.eq(i);
			var ch2 =  children.eq(i+1);
			if(ch1.get(0).tagName != ch2.get(0).tagName){
				error = true;
			}
		}
		if(error){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "直接子元素之间不相同,不能设置成列表");
			return ;
		}
		reList.find(".cms-area-list-element").each(function(i,dom){
			$j(dom).removeClass("cms-area-list-element");
		});
		reList.find(".cms-area-href").each(function(i,dom){
			var me = $j(dom);
			me.removeClass("cms-area-href");
		});
		reList.find(".cms-area-title").each(function(i,dom){
			var me = $j(dom);
			me.removeClass("cms-area-title");
		});
		reList.find(".cms-area-desc").each(function(i,dom){
			var me = $j(dom);
			me.removeClass("cms-area-desc");
		});
		reList.find(".cms-area-img").each(function(i,dom){
			var me = $j(dom);
			me.removeClass("cms-area-img");
		});
		reList.removeClass("cms-area-img").removeClass("cms-area-desc").removeClass("cms-area-title").removeClass("cms-area-href");
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		me.hide();
		me.parent().parent().find(".subList").show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "移出列表成功");
	});
	//关闭
	$j('.cms-imgArticle-edit-dialog').on("click",".close",function(){
		$j(".cms-imgArticle-edit-dialog").dialogff({type : 'close'});
	});
	//关闭
	$j('.cms-product-edit-dialog').on("click",".close",function(){
		$j(".cms-product-edit-dialog").dialogff({type : 'close'});
	});
	//预览模板
	$j('.preview').on("click",function(){
		if(isSave == false){
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("NO_SAVE_NOT_PREVIEW"));
			return ;
		}
		var url = base +"/cms/previewTemplate.htm?id=" + tmpId;
		loxia.openPage(url,null, null, [1000,600]);
	});
	
	//预览模板
	$j('.preview-module').on("click",function(){
		if(isSave == false){
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("NO_SAVE_NOT_PREVIEW"));
			return ;
		}
		var url = base +"/module/previewTemplate.htm?id=" + tmpId;
		loxia.openPage(url,null, null, [1000,600]);
	});
	
	$j(".mode").change(function(){
		var me = $j(this);
		var type = me.val();
		if(editObj.children().length<1){
			$j(".subList").hide();
			$j(".resubList").hide();
			return;
		}
		if(type==1){
			$j(".subList").hide();
		}else{
			if(editObj.hasClass("cms-area-list-element") || editObj.children().hasClass("cms-area-list-element")){
				$j(".subList").hide();
				$j(".resubList").show();
			}else{
				$j(".subList").show();
				$j(".resubList").hide();
			}
		}
		if(editObj.children().length==0){
			$j(".resubList").hide();
			$j(".subList").hide();
		}
	});
	
	//设置列表
	$j('.subList').on("click",function(){
		var me =$j(this);
		var areaList = editObj;
		areaList.children().each(function(i,dom){
			var me = $j(dom);
			if(!me.hasClass("cms-area-list-element")){
				me.addClass("cms-area-list-element");
			}
		});
		
		$j('.cms-tmp-edit-dialog').find(".html").val(editObj.prop("outerHTML"));
		me.hide();
		me.parent().parent().find(".resubList").show();
		//设置选择工具
		setSelectTool();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "设置成功");
		$j('.cms-tmp-edit-dialog').find(".confrim").trigger("click");
	});
	//设置标题
	$j('body').on("click",".removeEdit",function(){
		editObj = getEditWay(editObj);
		editObj.addClass("border-area-class");
		deleditObj = editObj;
		editObj.trigger("click");
		editObj.removeClass("border-area-class");
		$j(".cms-imgArticle-edit-dialog").dialogff({type : 'close'});
		$j(".cms-product-edit-dialog").dialogff({type : 'close'});
	});
	setWapperBorder(".cms-html-edit");
	setWapperBorder(".cms-imgarticle-edit");
	setWapperBorder(".cms-product-edit");
	
});

function addSameSet(cls,type){
	var checked = $j(".cms-imgArticle-edit-dialog .isSelect").attr("checked");
	if(typeof(checked)=="undefined" ||  checked==null || checked==""){
		return ;
	}
	var mode = getEditWay(editObj);
	var children = mode.children();
	var tag = editObj.get(0).tagName;
	var ctag = null;
	if(editObj.children().length>0){
		ctag = editObj.children().get(0).tagName;
	}
	if(children.hasClass("cms-area-list-element")){
		for (var i = 0; i < children.length; i++) {
			var ch = children.eq(i);
			if(ctag!=null && ctag!="" && ctag=="IMG"){
				if(type=="img"){
					if(ch.find(tag+" " +ctag).length==0){
						ch.find(ctag).addClass(cls);
					}else{
						ch.find(tag+" " +ctag).addClass(cls);
					}
				}
				if(type=="imgh"){
					if(ch.find(tag+" " +ctag).length==0){
						ch.addClass(cls);
					}else{
						ch.find(tag+" " +ctag).parent().addClass(cls);
					}
				}
			}else{
				if(type=="t"){
					ch.find(tag).eq(0).addClass(cls);
				}
				if(type=="th"){
					ch.find(tag).eq(0).addClass(cls);
				}
				if(type=="desc"){
					ch.find(tag).eq(0).addClass(cls);
				}
			}
		}
	}
	
}

function delSameSet(cls,type){
	var checked = $j(".cms-imgArticle-edit-dialog .isSelect").attr("checked");
	if(typeof(checked)=="undefined" ||  checked==null || checked==""){
		return ;
	}
	var mode = getEditWay(editObj);
	var children = mode.children();
	var tag = editObj.get(0).tagName;
	var ctag = null;
	if(editObj.children().length>0){
		ctag = editObj.children().get(0).tagName;
	}
	if(children.hasClass("cms-area-list-element")){
		for (var i = 0; i < children.length; i++) {
			var ch = children.eq(i);
			if(ctag!=null && ctag!="" && ctag=="IMG"){
				if(type=="img"){
					if(ch.find(tag+" " +ctag).length==0){
						ch.find(ctag).removeClass(cls);
					}else{
						ch.find(tag+" " +ctag).removeClass(cls);
					}
				}
				if(type=="imgh"){
					if(ch.find(tag+" " +ctag).length==0){
						ch.removeClass(cls);
					}else{
						ch.find(tag+" " +ctag).parent().removeClass(cls);
					}
				}
			}else{
				if(type=="t"){
					ch.find(tag).each(function(i,dom){
						var me = $j(dom);
						if(me.children().length==0){
							me.removeClass(cls);
						}
					});
				}
				if(type=="th"){
					ch.find(tag).each(function(i,dom){
						var me = $j(dom);
						if(me.children().length == 0){
							me.removeClass(cls);
						}
					});
				}
				if(type=="desc"){
					ch.find(tag).each(function(i,dom){
						var me = $j(dom);
						if(me.children().length == 0){
							me.removeClass(cls);
						}
					});
				}
			}
		}
	}
	
}