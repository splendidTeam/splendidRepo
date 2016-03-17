$j.extend(loxia.regional['zh-CN'],{
	"PROMPT_INFO":"提示信息",
	"EDIT_AREA_CODE_NOT_EXISTS":"该区域的code不存在",
	"EDIT_AREA_DATA_NOT_EMPTY":"页面编辑区域的数据不可以为空",
	
});

function getDimension($obj){
	if($obj.length == 0) return [0,0];
	var w = parseInt($obj.innerWidth()), h = parseInt($obj.innerHeight());
	if(h== 0){
		var d = getDimension($obj.children().first());
		if(d[1] > 0) d[1] = -d[1];
		return d;
	}else{
		return [w,h];
	}
}

function addEvent(cls){
	$j(".web-update").contents().find(cls).hover(
			function(){
				$j(this).find(".wui-tips").css("display","block");
			},
			function(){
				$j(this).find(".wui-tips").css("display","none");
			}
		).find(".wui-tips").css("opacity","0.8").each(function(){
			var d = getDimension($j(this).parent(cls));
			$j(this).css({"width": d[0],
			"top": d[1] > 0? 0: d[1],
			"height": Math.abs(d[1]),
			"line-height":Math.abs(d[1])+'px'});
	});	
}
function addAllEditEvent(){
	nps.init();
	addEvent(".cms-html-edit");
	addEvent(".cms-imgarticle-edit");
	addEvent(".cms-product-edit");
	addShade(".cms-html-edit");
	addShade(".cms-imgarticle-edit");
	addShade(".cms-product-edit");
}

/**
 * 保存表单后（由于dom可能发生变化）重新设置图文模式编辑事件
 * 
 * @param areaCode
 * @param cls
 */
function addEditEvent(areaCode,cls){
	$j(".web-update").contents().find('[code="'+areaCode+'"]').hover(
		function(){
			$j(this).find(".wui-tips").css("display","block");
		},
		function(){
			$j(this).find(".wui-tips").css("display","none");
		}
	).find(".wui-tips").css("opacity","0.8").each(function(){
		var d = getDimension($j(this).parent(cls));
		$j(this).css({"width":d[0],
			"top": d[1] > 0? 0: d[1],
			"height":Math.abs(d[1]),
			"line-height":Math.abs(d[1])+'px'});
	});
};

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
};
var dialog_content;
var arealistObj=[];

function removeTem(num){
	for ( var i = 0; i < arealistObj.length; i++) {
		 var tem  = arealistObj[i];
		 if(num == tem.num){
			 tem.obj.remove();
		 }
	}
}

//图文模式，当form发生变化时，暂存数据
//型如：lazySaveData[111]['href'] = "www.a.com"
//111为zNode id
var lazySaveData = new Object();
/**
 * 显示选中zNode对应的表单（包含数据组装）
 * 
 * @param areaList CMS Instance编辑页面
 * @param zNode 当前选中的dom tree节点
 */
function showForm(areaList, zNode){
	areaList.find("."+CSS_CLASS.INPUT_TITLE).hide();
	areaList.find("."+CSS_CLASS.DIV_IMG).hide();
	areaList.find("."+CSS_CLASS.INPUT_IMG_ALT).hide();
	areaList.find("."+CSS_CLASS.INPUT_HREF).hide();
	areaList.find("."+CSS_CLASS.INPUT_COORDS).hide();
	areaList.find("."+CSS_CLASS.TEXTAREA_DESC).hide();

	var jDom = zDom[zNode.id];
	//dom可编辑项的配置
	if(!domTreeManager.isDomEditable(jDom)){
		return;
	}
	
	//如果数据被修改过，但未保存，则取修改后的数据组装表单
	if(jDom.hasClass(CSS_CLASS.EDIT_TITLE)){
		var cachedData = lazySaveData[zNode.id] == undefined ? undefined : lazySaveData[zNode.id][CSS_CLASS.INPUT_TITLE];
		areaList.find("."+CSS_CLASS.INPUT_TITLE+" input").val(cachedData != undefined ? cachedData : jDom.html());
		areaList.find("."+CSS_CLASS.INPUT_TITLE+" input").parent().show();
	}
	if(jDom.hasClass(CSS_CLASS.EDIT_IMG)){
		//图片src编辑
		var cachedData = lazySaveData[zNode.id] == undefined ? undefined : lazySaveData[zNode.id][CSS_CLASS.INPUT_IMG_URL];
		areaList.find(".img ."+CSS_CLASS.INPUT_IMG_URL).val(cachedData != undefined ? cachedData : jDom.attr("src"));
		//--标识当前显示的file控件 START--------------------------
		$j(".cms-imgarticle-edit-dialog").find(".fileupload").attr("hName","templateImageUrl");
		areaList.find(".fileupload").attr("hName","hNameCurrent");
		//--标识当前显示的file控件 END--------------------------
		areaList.find(".img ."+CSS_CLASS.INPUT_IMG_URL).parent().show();
		//图片alt编辑
		cachedData = lazySaveData[zNode.id] == undefined ? undefined : lazySaveData[zNode.id][CSS_CLASS.INPUT_IMG_ALT];
		areaList.find("."+CSS_CLASS.INPUT_IMG_ALT+" input").val(cachedData != undefined ? cachedData : jDom.attr("alt"));
		areaList.find("."+CSS_CLASS.INPUT_IMG_ALT+" input").parent().show();
	}
	if(jDom.hasClass(CSS_CLASS.EDIT_HREF)){
		var cachedData = lazySaveData[zNode.id] == undefined ? undefined : lazySaveData[zNode.id][CSS_CLASS.INPUT_HREF];
		areaList.find("."+CSS_CLASS.INPUT_HREF+" input")
				.val(cachedData != undefined ? cachedData : jDom.attr("href"));
		areaList.find("."+CSS_CLASS.INPUT_HREF+" input").parent().show();
	}
	if(jDom.hasClass(CSS_CLASS.EDIT_COORDS)){
		var cachedData = lazySaveData[zNode.id] == undefined ? undefined : lazySaveData[zNode.id][CSS_CLASS.INPUT_COORDS];
		areaList.find("."+CSS_CLASS.INPUT_COORDS+" input")
				.val(cachedData != undefined ? cachedData : jDom.attr("coords"));
		areaList.find("."+CSS_CLASS.INPUT_COORDS+" input").parent().show();
	}
	if(jDom.hasClass(CSS_CLASS.EDIT_DESC)){
		var cachedData = lazySaveData[zNode.id] == undefined ? undefined : lazySaveData[zNode.id][CSS_CLASS.TEXTAREA_DESC];
		areaList.find("."+CSS_CLASS.TEXTAREA_DESC+" textarea").val(cachedData != undefined ? cachedData : jDom.html());
		areaList.find("."+CSS_CLASS.TEXTAREA_DESC+" textarea").parent().show();
	}
	
}

/**
 * 获取图文数据并设置到编辑框中
 * 
 * @param me 被选中的、待编辑的dom区域
 * @param num 在列表模式时，列表项的index
 */
function addImgarticle2(me,num){
	//获取到编辑对话框
	dialog_content = $j(".cms-imgarticle-edit-dialog");
	var areaList = $j(".cms-imgarticle-edit-dialog .li-area-list").clone(true).find(".area-list")
			.removeClass("area-list").addClass("area-list-add").show().parent();
	areaList.removeClass("li-area-list").addClass("li-area-list-add");
	areaList.attr("oldIndex",me.attr("oldIndex"));

	//--画dom树 START-----------------------
	var treeId="zTreeForDom"+num;
	areaList.find(".ztree").attr("id", treeId);
	
	var setting = {
			view: {
				showIcon: false,
				fontCss: function (treeId, node){
					return domTreeManager.getWithCssClassZnodeFont(node, true);
				}
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick:  function (event, treeId, treeNode, clickFlag) {
					showForm(areaList, treeNode);
				}
			}
		};
	var zNodes=domTreeManager.buildZnodes(me);
	console.log("为dom树组织数据："+JSON.stringify(zNodes));
	var zTree = $j.fn.zTree.init(areaList.find(".ztree"), setting, zNodes);
	//展开dom树
	zTree.expandAll(true);
	
	//找到第一个可编辑的dom元素
	var zNode = domTreeManager.findFirstEditableDom(zTree);
	if(zNode){
		zTree.selectNode(zNode);
		//显示第一个可编辑的dom对应的表单（包含数据组装）
		showForm(areaList, zNode);
	}
	
	//--画dom树 END-----------------------
	
	
	if(num==0){
		areaList.find(".zkss a").html("收起-"+(num+1));
	}else{
		areaList.find(".zkss a").html("展开-"+(num+1));
	}
	
	areaList.find(".remove").attr("num",num);
	var tem ={"num":num,"obj":me};
	arealistObj.push(tem);
	if(num == 0){
		areaList.find(".slideToggle").show();
	}else{
		areaList.find(".slideToggle").hide();
	}
//	if(me.find(".cms-area-title").length>0){
//		areaList.find(".title").show();
//	}else{
//		areaList.find(".title").hide();
//	}
//	if(me.find(".cms-area-title").hasClass("cms-area-href")){
//		areaList.find(".href-title").show();
//	}else{
//		areaList.find(".href-title").hide();
//	}
//	if(me.find(".cms-area-img").length>0 || me.hasClass("cms-area-img")){
//		areaList.find(".img").show();
//	}else{
//		areaList.find(".img").hide();
//	}
//	if(me.find(".cms-area-img").parent().hasClass("cms-area-href")){
//		areaList.find(".href-img").show();
//	}else{
//		areaList.find(".href-img").hide();
//	}
//	if(me.find(".cms-area-desc").length>0){
//		areaList.find(".desc").show();
//	}else{
//		areaList.find(".desc").hide();
//	}
	
	 var hide  =getEditWay(me).attr("hide");
	 if(hide=="1"|| typeof(hide)=="undefined"){
		 dialog_content.find(".show-area").hide();
		 dialog_content.find(".hide-area").show();
	 }else{
		 dialog_content.find(".show-area").show();
		 dialog_content.find(".hide-area").hide();
	 }
	//追加到页面中显示
	dialog_content.find("#element-list").append(areaList);
};

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
	$j(".cms-imgarticle-edit-dialog ").find("."+cls).each(function(){
		var val =$j(this).val();
		var display = $j(this).parent().parent().css("display");
		if(display !="none"){
			display = $j(this).parent().css("display");
		}
		if(val!=null && val!="" && $j(this).hasClass("ui-loxia-error")){
			$j(this).removeClass("ui-loxia-error");
		}
		if(display !="none" && $j(this).hasClass("ui-loxia-error")){
			result = false;
		}
		if(display !="none" && (val==null || val=="")){
			$j(this).addClass("ui-loxia-error");
			result = false;
		}
		
	 });
	return result; 
};
function addShade(cls){
	$j(".web-update").contents().find(cls).find(".wui-tips-shade").css("opacity","0.8").each(function(){
		 	var  me = $j(this);  
		 	var parent = me.parent();  
		 	var hide = 	parent.attr("hide");
		 	if(hide=="1"|| typeof(hide)=="undefined"){
		 		me.hide();
		 	}else{
		 		me.show();
		 	}
			$j(this).css({"width":parseInt($j(this).parent(cls).width())
			+parseInt($j(this).parent(cls).css("padding-left"))
			+parseInt($j(this).parent(cls).css("padding-right")),
			"height":parseInt($j(this).parent(cls).height())
			+parseInt($j(this).parent(cls).css("padding-top"))
			+parseInt($j(this).parent(cls).css("padding-bottom")),
			"line-height":parseInt($j(this).parent(cls).height())
			+parseInt($j(this).parent(cls).css("padding-top"))
			+parseInt($j(this).parent(cls).css("padding-bottom"))+"px"});

	});	
}
var picTextObj;
$j(window).load(function(){
	// 页面初始化表单方式设置oldIndex
	$j(".web-update").contents().find(".cms-imgarticle-edit").each(function(){
		var i = 1;
		$j(this).find(".cms-area-list-element").each(function(){
			$j(this).attr("oldIndex",i);
			i++;
		});
		var j = 1;
		$j(this).find(".cms-area-list-element").each(function(){
			$j(this).attr("oldIndex",j);
			j++;
		});
	});
	// 页面初始化商品方式设置oldIndex
	$j(".web-update").contents().find(".cms-product-edit").each(function(){
		var i = 1;
		$j(this).find(".cms-area-list-element").each(function(){
			$j(this).attr("oldIndex",i);
			i++;
		});
		var j = 1;
		$j(this).find(".cms-area-list-element").each(function(){
			$j(this).attr("oldIndex",j);
			j++;
		});
	});
	
	// 绑定拖动效果
	$j(function() {
	    $j("#element-list").sortable();
	  });
	// 绑定拖动效果
	$j(function() {
	    $j("#item-element-list").sortable();
	  });
	// 使用sortable插件绑定ul会造成ul范围内的blur事件失效，使用下面的命令可以重新启用事件
	$j('#element-list').mousedown(function(){
		document.activeElement.blur();
	});
	// 使用sortable插件绑定ul会造成ul范围内的blur事件失效，使用下面的命令可以重新启用事件
	$j('#item-element-list').mousedown(function(){
		document.activeElement.blur();
	});
	
	var editHtml = '<div class="wui-tips" onclick="return false;" style="clear:both; display:none; position:absolute; width:100%; left:0; top:0; z-index:100; background:#ff6600; color:#fff; font-size:20px; text-align:center; cursor:pointer; font-weight:bold;">编辑</div>';
	var editHtmlShade = '<div class="wui-tips-shade" onclick="return false;" style="clear:both;position:absolute; width:100%; left:0; top:0; z-index:90; background:gray; color:#fff; font-size:20px; text-align:center; cursor:pointer; font-weight:bold;">已隐藏</div>';
	var editHtmlShadeHide = '<div class="wui-tips-shade" onclick="return false;" style="clear:both;display:none;position:absolute; width:100%; left:0; top:0; z-index:90; background:gray; color:#fff; font-size:20px; text-align:center; cursor:pointer; font-weight:bold;">已隐藏</div>';

	//选择层
	$j(".web-update").css({"height":$j(".web-update").contents().find("body").height()});
	$j(".web-update").contents().find(".cms-html-edit").css("position","relative").append(editHtml);
	$j(".web-update").contents().find(".cms-imgarticle-edit").css("position","relative").append(editHtml);
	$j(".web-update").contents().find(".cms-product-edit").css("position","relative").append(editHtml);
	//遮住层
	$j(".web-update").css({"height":$j(".web-update").contents().find("body").height()});
	$j(".web-update").contents().find(".cms-html-edit").css("position","relative").append(editHtmlShade);
	$j(".web-update").contents().find(".cms-imgarticle-edit").css("position","relative").append(editHtmlShade);
	$j(".web-update").contents().find(".cms-product-edit").css("position","relative").append(editHtmlShade);
	addAllEditEvent();
	
	$j(".web-update").contents().find("[hide='0']").each(function(){
		$j(this).attr("style","position:relative;");
		$j(this).find(".wui-tips-shade").show();
	});
	
	$j(".web-update").contents().find(".cms-html-edit").on('click', '.wui-tips', function(e){
		e.stopPropagation();
		var cmsHtmlEditObj = $j(this).parent('.cms-html-edit');
		picTextObj=cmsHtmlEditObj;
		var areaCode = cmsHtmlEditObj.attr('code');
		var editObj = cmsHtmlEditObj.clone(true);
		editObj.find(".wui-tips").remove();
		editObj.find(".wui-tips-shade").remove();
		var areaHtml = editObj.html();
		
		 var hide  =cmsHtmlEditObj.attr("hide");
		 var  dialog_content = $j(".cms-html-edit-dialog");
		 if(hide=="1"|| typeof(hide)=="undefined"){
			 dialog_content.find(".show-area").hide();
			 dialog_content.find(".hide-area").show();
		 }else{
			 dialog_content.find(".show-area").show();
			 dialog_content.find(".hide-area").hide();
		 }
		// 去掉 editHtml 的html
		//areaHtml = areaHtml.substring(0, areaHtml.lastIndexOf('<div'));
		dialog_content.dialogff({type:'open',close:'in',width:'800px', height:'500px'});
		$j('.cms-html-edit-dialog #areaCode').val(areaCode);
		$j('#areaHtml').val(areaHtml);
		return false;
	});
	//图文模式
	$j(".web-update").contents().find(".cms-imgarticle-edit").on('click', '.wui-tips', function(e){
		e.stopPropagation();
		var me =  $j(this);
		var cmsHtmlEditObj = me.parent('.cms-imgarticle-edit');
		picTextObj=cmsHtmlEditObj;
		//获取图文模式code
		var areaCode = cmsHtmlEditObj.attr('code');
		//编辑之前先删除上次生成的页面
		 $j(".cms-imgarticle-edit-dialog .li-area-list-add").each(function(i,dom){
			$j(dom).remove();
		});
		//清空图文模式列表对象
		arealistObj=[];
		if(cmsHtmlEditObj.find(".cms-area-list-element").length>0){
			//列表模式
			var list = cmsHtmlEditObj.find(".cms-area-list-element");
			list.each(function(i,dom){
				var me = $j(dom);
				//设置图文模式对话框数据
				addImgarticle2(me,i);
			});
		}else{
			//普通模式
			addImgarticle2(cmsHtmlEditObj,0);
		}
		//如果下面没有任何编辑方式
		if(cmsHtmlEditObj.find(".cms-area-title").length==0 &&
				cmsHtmlEditObj.find(".cms-area-desc").length==0 &&
				cmsHtmlEditObj.find(".cms-area-img").length==0 &&
				cmsHtmlEditObj.find(".cms-area-href").length==0 ){
			$j(".cms-imgarticle-edit-dialog .imgarticle-info").show();
		}else{
			$j(".cms-imgarticle-edit-dialog .imgarticle-info").hide();
		}
		$j('.cms-imgarticle-edit-dialog #areaCode').val(areaCode);
		if(me.parent(".cms-imgarticle-edit").find(".cms-area-list-element").length>0){
			//列表模式需要显示的内容
			$j(".proto-dialog-button-line .add-tmp").show();
			$j(".cms-imgarticle-edit-dialog .list-line").css("border","1px solid #27A2C5");
			$j('.remove').parent().show();
			$j(".cms-imgarticle-edit-dialog .zkss").show();
			$j(".cms-imgarticle-edit-dialog").dialogff({type:'open',close:'in',width:'800px', height:'700px'});
		}else{
			//普通模式需要隐藏的内容
			$j(".proto-dialog-button-line .add-tmp").hide();
			$j(".cms-imgarticle-edit-dialog .zkss").hide();
			$j(".cms-imgarticle-edit-dialog .list-line").css("border","");
			$j('.remove').parent().hide();
			$j(".cms-imgarticle-edit-dialog").find(".slideToggle").show();
			$j(".cms-imgarticle-edit-dialog").dialogff({type:'open',close:'in',width:'800px', height:'450px'});
		}
		// 重新加载上传组件
		//$j.getScript(base+'/scripts/ajaxfileupload.js');
		jQuery(".imgUploadComponet").each(function(i,n){
			jQuery.initUploadImg(jQuery(this));
		});
		
	});
	
	/** dialog 取消 */
	$j('.cencal').click(function(){
		$j('.dialog-close').click();
	});
	
	/** html编辑 dialog 确定*/
	$j('.confrim-html').click(function(){
		var areaCode = $j('.cms-html-edit-dialog #areaCode').val();
		var areaHtml = $j('.cms-html-edit-dialog #areaHtml').val();
		if($j.trim(areaCode) == ''){
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('EDIT_AREA_CODE_NOT_EXISTS'));
			return;
		}
		if($j.trim(areaHtml) == ''){
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('EDIT_AREA_DATA_NOT_EMPTY'));
			return;
		}
		areaHtml += editHtml;
		picTextObj.find(".wui-tips-shade").each(function(){
			 var hide = $j(this).parent().attr("hide");
			 if(hide=="1"|| typeof(hide)=="undefined"){
				 areaHtml += editHtmlShadeHide; 
			 }else{
				 areaHtml += editHtmlShade;
			 }		 
		});
		 
		$j(".web-update").contents().find('[code="'+areaCode+'"]').empty();
		$j(".web-update").contents().find('[code="'+areaCode+'"]').append(areaHtml);
		addEditEvent(areaCode,".cms-html-edit");
		addShade(".cms-html-edit");
		setTimeout(function(){addEditEvent(areaCode,".cms-html-edit");},1000);
		$j("#isSaved").val(false);
//		alert($j("#isSaved").val());
		$j('.dialog-close').click();
	});
	
	/** 图文模式 dialog 确定*/
	$j('.confrim-imgarticle').click(function(){
		
		for(zNodeId in lazySaveData){
			console.log("zDom["+zNodeId+"]有变化");
			var jDom = zDom[zNodeId];
			
			if(lazySaveData[zNodeId][CSS_CLASS.INPUT_TITLE] != undefined){
				jDom.html(lazySaveData[zNodeId][CSS_CLASS.INPUT_TITLE]);
			}
			if(lazySaveData[zNodeId][CSS_CLASS.INPUT_HREF] != undefined){
				jDom.attr("href", lazySaveData[zNodeId][CSS_CLASS.INPUT_HREF]);
			}
			if(lazySaveData[zNodeId][CSS_CLASS.INPUT_COORDS] != undefined){
				jDom.attr("coords", lazySaveData[zNodeId][CSS_CLASS.INPUT_COORDS]);
			}
			if(lazySaveData[zNodeId][CSS_CLASS.INPUT_IMG_URL] != undefined){
				jDom.attr("src", lazySaveData[zNodeId][CSS_CLASS.INPUT_IMG_URL]);
			}
			if(lazySaveData[zNodeId][CSS_CLASS.INPUT_IMG_ALT] != undefined){
				jDom.attr("alt", lazySaveData[zNodeId][CSS_CLASS.INPUT_IMG_ALT]);
			}
			if(lazySaveData[zNodeId][CSS_CLASS.TEXTAREA_DESC] != undefined){
				jDom.html(lazySaveData[zNodeId][CSS_CLASS.TEXTAREA_DESC]);
			}
			console.log("CMS Instance节点发生改变："+jDom.prop("outerHTML"));
		}
		
		//重新设置图文模式编辑事件
		addEditEvent(areaCode,".cms-imgarticle-edit2");
		setTimeout(function(){addEditEvent(areaCode,".cms-imgarticle-edit2");},1000);
		$j("#isSaved").val(false);
		//清楚暂存数据
		lazySaveData = new Object();
		changeImageSort(".cms-imgarticle-edit-dialog");
		$j('.dialog-close').click();
	});
	
	$j('.add-tmp').click(function(){
		
		//获取编辑的图文对象 并克隆一下新的对象添加到列表中
		var cloneObj = picTextObj.find(".cms-area-list-element :last").clone(true);
		domTreeManager.cleanZnodeValues(cloneObj);
		picTextObj.find(".cms-area-list-element :last").after(cloneObj);
		
		//--为areaList编号 START---------------------------------------------
		var areaList = $j(".cms-imgarticle-edit-dialog .area-list-add");
		
		//收起展开项
		//找到编号最大值
		var maxAreaNumber = 1;
		areaList.each(function(i, area){
			var jAreaZkssA = $j(area).find(".zkss a");
			var mateched = jAreaZkssA.html().match(/(收起|展开)-(\d+)/);
			var openOrClose = mateched[1];
			var areaNumber = parseInt(mateched[2]);
			if(areaNumber > maxAreaNumber){
				maxAreaNumber = areaNumber;
			}
			if("收起" == openOrClose){
				jAreaZkssA.click();
			}
		});
		//--为areaList编号 END---------------------------------------------
		
		addImgarticle2(cloneObj, maxAreaNumber);
		//展开新添加的area
		$j(".cms-imgarticle-edit-dialog .area-list-add").last().find(".zkss a").click();

//		$j(".cms-imgarticle-edit-dialog .proto-dialog-content").scrollTop(250*num);
		//重新设置图文模式编辑事件
		var areaCode = $j('.cms-imgarticle-edit-dialog #areaCode').val();
		addEditEvent(areaCode,".cms-imgarticle-edit");
		
		var nextIndex = getNextIndex(areaCode);
		
		$j(".web-update").contents().find("[code='"+areaCode+"']").find(".cms-area-list-element").last().attr("oldIndex",nextIndex);
		//给定原始编号直接对应dom图片的oldIndex
		$j(".cms-imgarticle-edit-dialog .li-area-list-add").last().attr("oldIndex",nextIndex);
	});
	
	$j('.remove').click(function(){
		//删除对应的图文数据
		var me  = $j(this);
		nps.confirm("提示信息","确定要删除?",function(){
			var num = me.attr("num");
			me.parent().parent().parent().remove();
			if(num != null && num != ""){
				removeTem(num);
				//重新设置图文模式编辑事件
				var areaCode = $j('.cms-imgarticle-edit-dialog #areaCode').val();
				addEditEvent(areaCode,".cms-imgarticle-edit");
			}
		});
		
	});
	
	$j('body').on("click",".zkss a",function(){
		var me = $j(this);
		var zkss = me.html().substring(0,me.html().indexOf("-"));
		var info ="";
		if(zkss=="展开"){
			info=me.html().replace("展开","收起");
		}else{
			info=me.html().replace("收起","展开");
		}
		me.html(info);
		me.parent().parent().find(".slideToggle").slideToggle();
		me.parent().parent().find(".slideToggle").attr("st","st");
		me.parent().parent().parent().parent().find(".slideToggle").each(function(i,dom){
			var me = $j(dom);
			if(me.attr("st")!="st"){
				var zs = me.parent().find(".select").html();
				 zs = zs.replace("收起","展开");
				 me.parent().find(".select").html(zs);
				 me.hide();
			}else{
				me.removeAttr("st");
			}
		});
		
	});
	
	$j(".web-update").contents().find("a").on("click",function(){
		return false;
	});
	//显示
	$j("body").on("click",".show-area",function(){
		var me = $j(this);
		var parent = me.parent();
		var areaCode = parent.parent().find("#areaCode").val();
		nps.confirm("提示信息","确定要显示?",function(){
			showOrHideEditArea("1",areaCode);
			me.hide();
			parent.find(".hide-area").show();
		});
		
	});
	//隐藏
	$j("body").on("click",".hide-area",function(){
		var me = $j(this);
		var parent = me.parent();
		var areaCode = parent.parent().find("#areaCode").val();
		nps.confirm("提示信息","确定要隐藏?",function(){
			showOrHideEditArea("0",areaCode);
			me.hide();
			parent.find(".show-area").show();
		});
	});
	
	//图文模式，当form发生变化时，暂存数据
	$j(".cms-imgarticle-edit-dialog .proto-dialog-content").on("blur","input,textarea",function(){
		var me = $j(this);
		
		//--找当前zNode及其操作项配置 Start-------------------------
		var zTreeId = me.parents(".area-list-add").find(".ztree").attr("id");
		var zTree = $j.fn.zTree.getZTreeObj(zTreeId);
		var zNode = zTree.getSelectedNodes()[0];
		//--找当前zNode及其操作项配置 END-------------------------
		
		if(!lazySaveData[zNode.id]){
			lazySaveData[zNode.id] = new Object();
		}
		if(me.parent().hasClass(CSS_CLASS.INPUT_TITLE)){
			lazySaveData[zNode.id][CSS_CLASS.INPUT_TITLE] = me.val();
		}
		if(me.parent().hasClass(CSS_CLASS.INPUT_HREF)){
			lazySaveData[zNode.id][CSS_CLASS.INPUT_HREF] = me.val();
		}
		if(me.parent().hasClass(CSS_CLASS.INPUT_COORDS)){
			lazySaveData[zNode.id][CSS_CLASS.INPUT_COORDS] = me.val();
		}
		if(me.hasClass(CSS_CLASS.INPUT_IMG_URL)){
			lazySaveData[zNode.id][CSS_CLASS.INPUT_IMG_URL] = me.val();
		}
		if(me.parent().hasClass(CSS_CLASS.INPUT_IMG_ALT)){
			lazySaveData[zNode.id][CSS_CLASS.INPUT_IMG_ALT] = me.val();
		}
		if(me.parent().hasClass(CSS_CLASS.TEXTAREA_DESC)){
			lazySaveData[zNode.id][CSS_CLASS.TEXTAREA_DESC] = me.val();
		}
		
	});
	
});

function  showOrHideEditArea(hide,areaCode){
	var templateId;
	var pageId;
	var type =  $j('#type').val();
	if(type=="page"){
		 templateId =$j('#templateId').val();
		 pageId = $j('#pageId').val();
	}else{
		 templateId =$j('#templateId').val();
		 pageId = $j('#moduleId').val();
	}
	var json={"areaCode":areaCode,"templateId":templateId,"pageId":pageId ,"hide":hide,"type":type};
 	nps.asyncXhrPost(base+"/cms/editAreaHide.json", json,{successHandler:function(data, textStatus){
			if (data.isSuccess) {
				$j("#isSaved").val(false);
				if(hide==1){
					nps.info(nps.i18n("PROMPT_INFO"), "显示成功");
					picTextObj.find(".wui-tips-shade").hide();
					picTextObj.attr("hide","1");
				}else{
					nps.info(nps.i18n("PROMPT_INFO"), "隐藏成功");
					picTextObj.find(".wui-tips-shade").show();
					picTextObj.attr("hide","0");
				}
			} else {
				if(hide==1){
					nps.info(nps.i18n("PROMPT_INFO"),data.description);
				}else{
					nps.info(nps.i18n("PROMPT_INFO"),data.description);
				}
			}
	 }});
}

/**
 * 将新的顺序应用到图文模式上
 * @param dialogClass 因为有两种模式，这里传入不同模式的dialog的class样式
 */
function changeImageSort(dialogClass){
	var code = $j(dialogClass+" #areaCode").val();
	var arealist = $j(dialogClass+" .li-area-list-add");
	var newIndex = [];
	var i = 0;
	arealist.each(function(){
		newIndex[i] = ($j(this).attr("oldIndex"));
		i++;
	});
	// 按页面上的图片的顺序暂存
	var list = $j(".web-update").contents().find("[code='"+code+"']").find(".cms-area-list-element");
	
	// 删除页面上的图片
	$j(".web-update").contents().find("[code='"+code+"']").find(".cms-area-list-element").remove();
	
	// 新的顺序
	var newlist = [];
	
	// 根据newIndex中的index来重新排列页面上的图片顺序
	for (var j = 0; j < newIndex.length; j++){
		var element;
		list.each(function(){
			if($j(this).attr("oldIndex") == newIndex[j]){
				element = $j(this);
			}		
		});
		newlist[j] = element;
	}
	
	// 循环新顺序的元素添加到原来的地方
	for (var k = 0; k < newlist.length; k++){
		$j(".web-update").contents().find("[code='"+code+"']").append(newlist[k]);
	}
}

/**
 * 获取该区域下一个index
 * @param code
 * @returns
 */
function getNextIndex(code){
	var oldIndexs = [];
	var i = 0;
	$j(".web-update").contents().find("[code='"+code+"']").find(".cms-area-list-element").each(function(){
		oldIndexs[i] = $j(this).attr("oldIndex");
	});
	oldIndexs = oldIndexs.sort();
	return parseInt(oldIndexs[oldIndexs.length-1])+1;
}