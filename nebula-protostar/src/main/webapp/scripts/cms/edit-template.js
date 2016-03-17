$j.extend(loxia.regional['zh-CN'],{
	"PROMPT_INFO":"提示信息",
	"EDIT_AREA_CODE_NOT_EXISTS":"该区域的code不存在",
	"EDIT_AREA_DATA_NOT_EMPTY":"页面编辑区域的数据不可以为空",
	"NO_SAVE_NOT_PREVIEW":"数据有改动,请先保存,再浏览"
	
});

/**
 * 获取编辑对象 （标记有cms-xxx-edit CSS class）
 * 
 * 从obj往上查找，直到找到为止。
 * @param obj 查到起点
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

/**
 * 查找所选定dom区域的CMS编辑方式
 * 
 * 从当前dom开始查到cms-xxx-edit css class, 向父层逐层查找，直到找到或到的‘HTML’标签为止。
 * @param dom 当前鼠标选中的dom区域
 * @returns
 */
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
/**
 * 根据当前选定的dom所包含的内容，来决定这部分CMS template可以有哪些可编辑的内容
 * 
 * 比如：对于一个带图片的A标签，可以设置“编辑图片”，“编辑链接地址”。这样该模板的实例就可以进行这2项操作
 * @param dialog 弹出层，用于设置CMS template
 * @param dom 当前选定的dom区域
 */
function  showHideTag(dialog,dom){
	//dom为A标签时
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
	}else{
		dialog.find(".title").hide();
		dialog.find(".thref").hide();
		dialog.find(".retitle").hide();
		dialog.find(".rethref").hide();
		
	}
	
	//dom为A标签且带图片时
	if(dom.children().length>0 && 
			dom.get(0).tagName=="A" && dom.find("img").length > 0){
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
	}else{
		dialog.find(".img").hide();
		dialog.find(".ihref").hide();
		dialog.find(".reimg").hide();
		dialog.find(".reihref").hide();
	}
	
	//设置cms-area-desc功能按钮
	if((!dom.hasClass(".cms-area-href") &&
			!dom.hasClass(".cms-area-title")&&
			!dom.hasClass(".cms-area-desc")&&
			!dom.hasClass(".cms-area-img") &&
			dom.find(".cms-area-href").length==0 &&
			dom.find(".cms-area-title").length==0&&
			dom.find(".cms-area-desc").length==0&&
			dom.find(".cms-area-img").length==0 &&
			dom.children().length>0 &&
			dom.children().get(0).tagName != "IMG") 
		||(dom.children().length==0)){
		
		if(dom.children().length==0 && dom.children().children().length==0 && 
				dom.get(0).tagName != "A" && dom.get(0).tagName!="IMG" && !dom.parent().hasClass("cms-imgarticle-edit")){
			if(dom.hasClass("cms-area-desc")){
				dialog.find(".desc").hide();
				dialog.find(".redesc").show();
			}else{
				dialog.find(".desc").show();
				dialog.find(".redesc").hide();
			}
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
	
	//有操作按钮时，infoFlag =false
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
 * 检查当前编辑项是否已经被设置
 * 
 * 检查范围为editObj
 * @param cls 编辑项对应的 CSS class
 * @param type 用于if判断
 * @returns {Boolean}
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

/**
 * 显示所选中的dom的可操作项
 * 
 * @param dialog 弹出层，用于设置CMS template的可编辑项
 * @param zNode dom树当前选中的节点
 */
function showEditButton(dialog, zNode){
	dialog.find("."+CSS_CLASS.BUTTON_HREF).hide();
	dialog.find("."+CSS_CLASS.BUTTON_REMOVE_HREF).hide();
	dialog.find("."+CSS_CLASS.BUTTON_COORDS).hide();
	dialog.find("."+CSS_CLASS.BUTTON_REMOVE_COORDS).hide();
	dialog.find("."+CSS_CLASS.BUTTON_IMG).hide();
	dialog.find("."+CSS_CLASS.BUTTON_REMOVE_IMG).hide();
	dialog.find("."+CSS_CLASS.BUTTON_TITLE).hide();
	dialog.find("."+CSS_CLASS.BUTTON_REMOVE_TITLE).hide();
	dialog.find("."+CSS_CLASS.BUTTON_DESC).hide();
	dialog.find("."+CSS_CLASS.BUTTON_REMOVE_DESC).hide();
	
	if(!zNode){
		return;
	}
	
	//查找该tag的可编辑配置
	var jDom = zDom[zNode.id];
	var cssClasses = domConfigManager.getCssClass(jDom);
	if(cssClasses.length == 0){
		//无按钮时不显示lable
		dialog.find(".editSetting").hide();
		return;
	}
	
	//如果当前dom可配置href
	if(cssClasses.indexOf(CSS_CLASS.EDIT_HREF) != -1){
		if(jDom.hasClass(CSS_CLASS.EDIT_HREF)){
			dialog.find("."+CSS_CLASS.BUTTON_REMOVE_HREF).show();
		}else{
			dialog.find("."+CSS_CLASS.BUTTON_HREF).show();
		}
	}
	//如果当前dom可配置coords
	if(cssClasses.indexOf(CSS_CLASS.EDIT_COORDS) != -1){
		if(jDom.hasClass(CSS_CLASS.EDIT_COORDS)){
			dialog.find("."+CSS_CLASS.BUTTON_REMOVE_COORDS).show();
		}else{
			dialog.find("."+CSS_CLASS.BUTTON_COORDS).show();
		}
	}
	//如果当前dom可配置img
	if(cssClasses.indexOf(CSS_CLASS.EDIT_IMG) != -1){
		if(jDom.hasClass(CSS_CLASS.EDIT_IMG)){
			dialog.find("."+CSS_CLASS.BUTTON_REMOVE_IMG).show();
		}else{
			dialog.find("."+CSS_CLASS.BUTTON_IMG).show();
		}
	}
	//如果当前dom可配置title
	if(cssClasses.indexOf(CSS_CLASS.EDIT_TITLE) != -1){
		if(jDom.hasClass(CSS_CLASS.EDIT_TITLE)){
			dialog.find("."+CSS_CLASS.BUTTON_REMOVE_TITLE).show();
		}else{
			dialog.find("."+CSS_CLASS.BUTTON_TITLE).show();
		}
	}
	//如果当前dom可配置desc
	if(cssClasses.indexOf(CSS_CLASS.EDIT_DESC) != -1){
		if(jDom.hasClass(CSS_CLASS.EDIT_DESC)){
			dialog.find("."+CSS_CLASS.BUTTON_REMOVE_DESC).show();
		}else{
			dialog.find("."+CSS_CLASS.BUTTON_DESC).show();
		}
	}
	
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
		//当前选中dom本身不是编辑区域，而是编辑区域内部的元素
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
				var dialog = $j(".cms-imgArticle-edit-dialog");
//				$j(".subList").hide();
//				$j(".resubList").hide();
				
				//showHideTag(dialog,me);
				//--判断是否显示‘.setSame’按钮 START-----------------------
				//标记有cms-xxx-edit的dom，即编辑区域
				var modeObj = getEditWay(me);
				if(modeObj.find(".cms-area-list-element").length>0){
					dialog.find(".setSame").show();
				}else{
					dialog.find(".setSame").hide();
				}
				//--判断是否显示‘.setSame’按钮 END-----------------------
				
				//--画dom树 START-----------------------
				var setting = {
						view: {
							showIcon: false,
							fontCss: function(treeId, node){
								return domTreeManager.getZnodeFont(node);
							}
						},
						data: {
							simpleData: {
								enable: true
							}
						},
						callback: {
							onClick:  function (event, treeId, treeNode, clickFlag) {
								showEditButton(dialog, treeNode);
							}
						}
					};
				var zNodes=domTreeManager.buildZnodes(me);
				console.log(JSON.stringify(zNodes));
				var zTree = $j.fn.zTree.init($j("#zTreeForDom"), setting, zNodes);
				//展开dom树
				zTree.expandAll(true);
				
				//找到第一个可编辑的dom元素
				var zNode = domTreeManager.findFirstEditableDom(zTree);
				if(zNode){
					zTree.selectNode(zNode);
				}
				
				//显示第一个可编辑的dom对应的操作按钮
				showEditButton(dialog, zNode);
				//--画dom树 END-----------------------
				
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
			}else{
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
		// 删除页面热点中的onclick事件
		removeMapOnClick();
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
		
		// 增加页面热点中的onclick事件
		addMapOnClick();
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
	
	//dialog设置标题
	$j('.cms-imgArticle-edit-dialog').on("click","."+CSS_CLASS.BUTTON_TITLE,function(){
		var me =$j(this);
		var zTree = $j.fn.zTree.getZTreeObj("zTreeForDom");
		if(domTreeManager.checkDomOperation(CSS_CLASS.EDIT_TITLE, zTree)){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "标题已经设置");
			return ;
		}
		
		var selectedZnode = domTreeManager.getSelectedZnode(zTree);
		var jDom = zDom[selectedZnode.id];
		jDom.addClass(CSS_CLASS.EDIT_TITLE);
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		doSameSet(jDom, CSS_CLASS.EDIT_TITLE);
		
		me.hide();
		me.parent().parent().find("."+CSS_CLASS.BUTTON_REMOVE_TITLE).show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "设置标题成功");
	});
	$j('.cms-imgArticle-edit-dialog').on("click","."+CSS_CLASS.BUTTON_REMOVE_TITLE,function(){
		var me =$j(this);

		var zTree = $j.fn.zTree.getZTreeObj("zTreeForDom");
		var selectedZnode = domTreeManager.getSelectedZnode(zTree);
		var jDom = zDom[selectedZnode.id];
		jDom.removeClass(CSS_CLASS.EDIT_TITLE);
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		doSameSet(jDom, CSS_CLASS.EDIT_TITLE, true);
		me.hide();
		me.parent().parent().find("."+CSS_CLASS.BUTTON_TITLE).show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "删除标题成功");
	});
	//dialog设置图片
	$j('.cms-imgArticle-edit-dialog').on("click","."+CSS_CLASS.BUTTON_IMG,function(){
		var me =$j(this);
		var zTree = $j.fn.zTree.getZTreeObj("zTreeForDom");
		if(domTreeManager.checkDomOperation(CSS_CLASS.EDIT_IMG, zTree)){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "图片已经设置");
			return ;
		}

		var selectedZnode = domTreeManager.getSelectedZnode(zTree);
		var jDom = zDom[selectedZnode.id];
		jDom.addClass(CSS_CLASS.EDIT_IMG);
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		doSameSet(jDom, CSS_CLASS.EDIT_IMG);
		me.hide();
		me.parent().parent().find("."+CSS_CLASS.BUTTON_REMOVE_IMG).show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "设置图片成功");
	});
	$j('.cms-imgArticle-edit-dialog').on("click","."+CSS_CLASS.BUTTON_REMOVE_IMG,function(){
		var me =$j(this);

		var zTree = $j.fn.zTree.getZTreeObj("zTreeForDom");
		var selectedZnode = domTreeManager.getSelectedZnode(zTree);
		var jDom = zDom[selectedZnode.id];
		jDom.removeClass(CSS_CLASS.EDIT_IMG);
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		doSameSet(jDom, CSS_CLASS.EDIT_IMG, true);
		me.hide();
		me.parent().parent().find("."+CSS_CLASS.BUTTON_IMG).show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "删除图片成功");
	});
	//dialog设置超链接
	$j('.cms-imgArticle-edit-dialog').on("click","."+CSS_CLASS.BUTTON_HREF,function(){
		var me =$j(this);
		var zTree = $j.fn.zTree.getZTreeObj("zTreeForDom");
		if(domTreeManager.checkDomOperation(CSS_CLASS.EDIT_HREF, zTree)){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "超链接已经设置");
			return ;
		}

		var selectedZnode = domTreeManager.getSelectedZnode(zTree);
		var jDom = zDom[selectedZnode.id];
		jDom.addClass(CSS_CLASS.EDIT_HREF);
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		doSameSet(jDom, CSS_CLASS.EDIT_HREF);
		me.hide();
		me.parent().parent().find("."+CSS_CLASS.BUTTON_REMOVE_HREF).show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "设置超链接成功");
	});
	$j('.cms-imgArticle-edit-dialog').on("click","."+CSS_CLASS.BUTTON_REMOVE_HREF,function(){
		var me =$j(this);

		var zTree = $j.fn.zTree.getZTreeObj("zTreeForDom");
		var selectedZnode = domTreeManager.getSelectedZnode(zTree);
		var jDom = zDom[selectedZnode.id];
		jDom.removeClass(CSS_CLASS.EDIT_HREF);
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		me.hide();
		doSameSet(jDom, CSS_CLASS.EDIT_HREF, true);
		me.parent().parent().find("."+CSS_CLASS.BUTTON_HREF).show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "删除超链接成功");
	});
	//dialog设置图片热点
	$j('.cms-imgArticle-edit-dialog').on("click","."+CSS_CLASS.BUTTON_COORDS,function(){
		var me =$j(this);
		var zTree = $j.fn.zTree.getZTreeObj("zTreeForDom");
		if(domTreeManager.checkDomOperation(CSS_CLASS.EDIT_COORDS, zTree)){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "图片热点已经设置");
			return ;
		}

		var selectedZnode = domTreeManager.getSelectedZnode(zTree);
		var jDom = zDom[selectedZnode.id];
		jDom.addClass(CSS_CLASS.EDIT_COORDS);
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		doSameSet(jDom, CSS_CLASS.EDIT_COORDS);
		me.hide();
		me.parent().parent().find("."+CSS_CLASS.BUTTON_REMOVE_COORDS).show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "设置图片热点成功");
	});
	$j('.cms-imgArticle-edit-dialog').on("click","."+CSS_CLASS.BUTTON_REMOVE_COORDS,function(){
		var me =$j(this);

		var zTree = $j.fn.zTree.getZTreeObj("zTreeForDom");
		var selectedZnode = domTreeManager.getSelectedZnode(zTree);
		var jDom = zDom[selectedZnode.id];
		jDom.removeClass(CSS_CLASS.EDIT_COORDS);
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		me.hide();
		doSameSet(jDom, CSS_CLASS.EDIT_COORDS, true);
		me.parent().parent().find("."+CSS_CLASS.BUTTON_COORDS).show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "删除图片热点成功");
	});
	//dialog设置描述
	$j('.cms-imgArticle-edit-dialog').on("click","."+CSS_CLASS.BUTTON_DESC,function(){
		var me =$j(this);
		var zTree = $j.fn.zTree.getZTreeObj("zTreeForDom");
		if(domTreeManager.checkDomOperation(CSS_CLASS.EDIT_DESC, zTree)){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "标题已经设置");
			return ;
		}
		
		var selectedZnode = domTreeManager.getSelectedZnode(zTree);
		var jDom = zDom[selectedZnode.id];
		jDom.addClass(CSS_CLASS.EDIT_DESC);
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		doSameSet(jDom, CSS_CLASS.EDIT_DESC);
		me.hide();
		me.parent().parent().find("."+CSS_CLASS.BUTTON_REMOVE_DESC).show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "设置标题成功");
	});
	$j('.cms-imgArticle-edit-dialog').on("click","."+CSS_CLASS.BUTTON_REMOVE_DESC,function(){
		var me =$j(this);

		var zTree = $j.fn.zTree.getZTreeObj("zTreeForDom");
		var selectedZnode = domTreeManager.getSelectedZnode(zTree);
		var jDom = zDom[selectedZnode.id];
		jDom.removeClass(CSS_CLASS.EDIT_DESC);
		me.parent().parent().find(".html").val(editObj.prop("outerHTML"));
		doSameSet(jDom, CSS_CLASS.EDIT_DESC, true);
		me.hide();
		me.parent().parent().find("."+CSS_CLASS.BUTTON_DESC).show();
		isSave =false; 
		nps.info(nps.i18n("INFO_TITLE_DATA"), "删除标题成功");
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
	
	/**
	 * 显示（删除）"设置子元素为列表"按钮
	 */
	function showSubListButton(){
		$j(".subList").hide();
		$j(".resubList").hide();
		if(editObj.children().length == 0){
			return;
		}
		
		if(editObj.hasClass("cms-area-list-element") || editObj.children().hasClass("cms-area-list-element")){
			$j(".resubList").show();
		}else{
			$j(".subList").show();
		}
	}
	
	/**
	 * 切换（删除）"设置子元素为列表"按钮
	 */
	function togglerSubListButton(button){
		button.hide();
		button.parent().parent().find(".resubList").show();
	}
	
	$j(".mode").change(function(){
		//var me = $j(this);
		showSubListButton();
	});
	
	//设置为列表模式"cms-area-list-element"
	$j('.subList').on("click",function(){
		var me =$j(this);
		var areaList = editObj;
		areaList.children().each(function(i,dom){
			var me = $j(dom);
			if(!me.hasClass("cms-area-list-element")){
				me.addClass("cms-area-list-element");
			}
		});
		//刷新代码显示
		$j('.cms-tmp-edit-dialog').find(".html").val(editObj.prop("outerHTML"));
		togglerSubListButton(me);
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

/**
 * 图文模式下，进行“将相同操作应用到列表的其余项”操作
 * 
 * 先判断是否勾选了该功能
 * @param jDom 当前正在操作的dom
 * @param cssClass 
 * @param isRemoveClass 是否删除该CSS class
 */
function doSameSet(jDom, cssClass, isRemoveClass){
	var checked = $j(".cms-imgArticle-edit-dialog .isSelect").attr("checked");
	if(typeof(checked)=="undefined" ||  checked==null || checked==""){
		return ;
	}
	
	var jEditWay = getEditWay(editObj);
	domTreeManager.copyCssClassSetting(jEditWay, jDom, cssClass, isRemoveClass);
}


/**
 * 在页面中所有map中area中增加属性onclick='return false;'
 * 由于“编辑模块”中不允许热点可以点击跳转，所以写了如上代码
 */
function addMapOnClick(){
	$j(".web-update").contents().find("map").each(function(){
		$j(this).find("area").each(function(){
			$j(this).attr("onclick","return false;");
		});
	});
}

/**
 * 删除页面中所有map中area中的属性onclick='return false;'
 * 由于“编辑模块”中不允许热点可以点击跳转，所以写了如上代码，在点击保存按钮的时候要调用这里，把如上代码remove掉
 */
function removeMapOnClick(){
	$j(".web-update").contents().find("map").each(function(){
    	$j(this).find("area").each(function(){
    		$j(this).removeAttr("onclick");
    	});
    });
}