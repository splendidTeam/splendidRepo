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
/**
 * 获取图文数据并设置编辑框中
 * @param me
 * @param num
 */
function addImgarticle(me,num){
	//获取到编辑对话框
	dialog_content = $j(".cms-imgarticle-edit-dialog");
	var areaList = $j(".cms-imgarticle-edit-dialog .area-list").clone(true)
	.removeClass("area-list").addClass("area-list-add").show();
	areaList.find(".fileupload").attr("hName","hName"+num);
	//存在标题并设置数据
	if(me.hasClass("cms-area-title") || me.find(".cms-area-title").length>0){
		if(me.find(".cms-area-title").hasClass("cms-area-href")){
			var href =  me.find(".cms-area-title");
			areaList.find(".href-title input").val(href.attr("href"));
			areaList.find(".href-title input").parent().show();
			var  title= me.find(".cms-area-title");
			var titleInput = areaList.find(".title input");
			
			if(title.val()==null || title.val()==""){
				titleInput.val(title.html());
				if(num==0){
					areaList.find(".zkss a").html("收起-"+title.html());
				}else{
					areaList.find(".zkss a").html("展开-"+title.html());
				}
			}else{
				titleInput.val(title.val());
				if(num==0){
					areaList.find(".zkss a").html("收起-"+title.val());
				}else{
					areaList.find(".zkss a").html("展开-"+title.val());
				}
			}
		
			titleInput.parent().show();
		}else{
			var  title= me.find(".cms-area-title");
			if(me.hasClass("cms-area-title")){
				title = me;
			}
			if(title.val()==null || title.val()==""){
				 title.find(".wui-tips").remove();
				 areaList.find(".title input").val(title.html());
				 if(num==0){
						areaList.find(".zkss a").html("收起-"+title.html());
					}else{
						areaList.find(".zkss a").html("展开-"+title.html());
					}
			}else{
				areaList.find(".title input").val(title.val());
				if(num==0){
					areaList.find(".zkss a").html("收起-"+title.val());
				}else{
					areaList.find(".zkss a").html("展开-"+title.val());
				}
			}
			if(me.get(0).tagName=="A"){
				areaList.find(".href-title input").val(me.attr("href"));
				areaList.find(".href-title input").parent().show();
				areaList.find(".title input").parent().show();
			}
			
		}
		
	}else{
		if(num==0){
			areaList.find(".zkss a").html("收起-"+(num+1));
		}else{
			areaList.find(".zkss a").html("展开-"+(num+1));
		}
	}
	//存在图片并设置数据
	if(me.find(".cms-area-img").length>0){
		var img= me.find(".cms-area-img");
		if(me.find(".cms-area-img").parent().hasClass("cms-area-href")){
			var href =  me.find(".cms-area-img").parent();
			areaList.find(".href-img input").val(href.attr("href"));
			areaList.find(".href-img input").parent().show();
			
			areaList.find(".img .img-url").val(img.attr("src"));
			areaList.find(".img .img-url").parent().show();
		}else{
			areaList.find(".img .img-url").val(img.attr("src"));
			areaList.find(".img .img-url").parent().show();
		}
		//图片alt编辑
		areaList.find(".img-alt input").val(img.attr("alt"));
		areaList.find(".img-alt input").parent().show();
		
	}else if(me.children().length>0 && me.children().get(0).tagName=="IMG"){
		if(me.get(0).tagName=="A"){
			areaList.find(".href-img input").val(me.attr("href"));
			areaList.find(".href-img input").parent().show();
		}
	}else{
		if(me.hasClass("cms-area-img")){
			areaList.find(".img .img-url").val(me.attr("src"));
			areaList.find(".img .img-url").parent().show();
			if(me.parent().hasClass("cms-area-href") && me.parent().get(0).tagName=="A"){
				areaList.find(".href-img input").val(me.attr("href"));
				areaList.find(".href-img input").parent().show();
			}
		}
	}
	//存在描述并设置数据
	if(me.find(".cms-area-desc").length>0){
		var desc= me.find(".cms-area-desc");
		//desc.parent().show();
		var descarea = areaList.find(".desc");
		for(var i=0; i<desc.length; i++){
			if($j(desc[i]).val()==null || $j(desc[i]).val()==""){
				if(i>0){
					var appenddescarea = descarea.clone();
					appenddescarea.attr('num', i);
					$j(appenddescarea).find('textarea').val($j(desc[i]).html());
					$j(areaList.find('.desc').last().after(appenddescarea));
				}else{
					areaList.find(".desc").attr('num', i);
					areaList.find(".desc textarea").val($j(desc[i]).html());
				}				
			}else{
				if($j(desc[i]).val()==null || $j(desc[i]).val()==""){
					if(i>0){
						var appenddescarea = descarea.clone();
						appenddescarea.attr('num', i);
						$j(appenddescarea).find('textarea').val($j(desc[i]).html());
						$j(areaList.find('.desc').last().after(appenddescarea));
					}else{
						areaList.find(".desc").attr('num', i);
						areaList.find(".desc textarea").val($j(desc[i]).val());
					}	
			}
		}
		}
		areaList.find(".desc textarea").parent().show();
	}
	if(me.hasClass("cms-area-desc")){
		var desc= me;
		//desc.parent().show();
		if(desc.val()==null || desc.val()==""){
			areaList.find(".desc textarea").val(desc.html());
		}else{
			areaList.find(".desc textarea").val(desc.val());
		}
		areaList.find(".desc textarea").parent().show();
	}
	areaList.find(".remove").attr("num",num);
	var tem ={"num":num,"obj":me};
	arealistObj.push(tem);
	if(num == 0){
		areaList.find(".slideToggle").show();
	}else{
		areaList.find(".slideToggle").hide();
	}
	if(me.find(".cms-area-title").length>0){
		areaList.find(".title").show();
	}else{
		areaList.find(".title").hide();
	}
	if(me.find(".cms-area-title").hasClass("cms-area-href")){
		areaList.find(".href-title").show();
	}else{
		areaList.find(".href-title").hide();
	}
	if(me.find(".cms-area-img").length>0 || me.hasClass("cms-area-img")){
		areaList.find(".img").show();
	}else{
		areaList.find(".img").hide();
	}
	if(me.find(".cms-area-img").parent().hasClass("cms-area-href")){
		areaList.find(".href-img").show();
	}else{
		areaList.find(".href-img").hide();
	}
	if(me.find(".cms-area-desc").length>0){
		areaList.find(".desc").show();
	}else{
		areaList.find(".desc").hide();
	}
	
	 var hide  =getEditWay(me).attr("hide");
	 if(hide=="1"|| typeof(hide)=="undefined"){
		 dialog_content.find(".show-area").hide();
		 dialog_content.find(".hide-area").show();
	 }else{
		 dialog_content.find(".show-area").show();
		 dialog_content.find(".hide-area").hide();
	 }
	//追加到页面中显示
	dialog_content.find(".proto-dialog-content").append(areaList);
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
		$j('.cms-html-edit-dialog #areaHtml').val(areaHtml);
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
		 $j(".cms-imgarticle-edit-dialog .area-list-add").each(function(i,dom){
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
				addImgarticle(me,i);
			});
		}else{
			//普通模式
			addImgarticle(cmsHtmlEditObj,0);
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
		var areaCode = $j('.cms-imgarticle-edit-dialog #areaCode').val();
		if($j.trim(areaCode) == ''){
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('EDIT_AREA_CODE_NOT_EXISTS'));
			return;
		}
		if(validateInput("ismandatory")==false){
			return ;
		}
		var imgarticle = $j(".web-update").contents().find('[code="'+areaCode+'"]');
		var area_list_add = $j(".cms-imgarticle-edit-dialog .area-list-add");
		//以下数组存放用户修改的数据
		var ts = [];
		var ths = [];
		var imgs = [];
		var imgalts =[];
		var imghrefs = [];
		var descs = [];
		//获取修改值
		area_list_add.each(function(i,dom){
			var me = $j(dom);
			//标题
			var t = me.find(".title input").val();
			//标题链接
			var th = me.find(".href-title input").val();
			//图片
			var img = me.find(".img .img-url").val();
			//图片alt
			var imgalt =me.find(".img-alt input").val();
			//图片链接
			var imgh = me.find(".href-img input").val();
			//描述
			var desc = me.find(".desc textarea");
			ts.push(t);
			ths.push(th);
			imgs.push(img);
			imgalts.push(imgalt);
			imghrefs.push(imgh);
			for(var i=0; i<desc.length; i++){	
				descs.push($j(desc[num=i]).val());
			}
			//descs.push(desc);
		});
		
		if(imgarticle.find(".cms-area-list-element").length>0){
			//列表模式
			var list = imgarticle.find(".cms-area-list-element");
			var sum = 0 ;
			list.each(function(i,dom){
				//循环设置数据
				var me = $j(dom);
				me.find(".cms-area-title").html(ts[i]);
				me.find(".cms-area-title").attr("title",ts[i]);
				if(me.find(".cms-area-title").parent().hasClass("cms-area-href")){
					me.find(".cms-area-title").parent().attr("href",ths[i]);
				}else{
					if(ths[i]!=null && ths[i]!=""){
						me.find(".cms-area-href").attr("href",ths[i]);
					}
				}
				if(me.hasClass("cms-area-title")){
					me.html(ts[i]);
				}
				me.find(".cms-area-img").attr("src",imgs[i]);
				me.find(".cms-area-img").attr("alt",imgalts[i]);
				if(me.find(".cms-area-img").parent().hasClass("cms-area-href")){
					me.find(".cms-area-img").parent().attr("href",imghrefs[i]);
				}else{
					if(imghrefs[i]!=null && imghrefs[i]!=""){
						me.find(".cms-area-href").attr("href",imghrefs[i]);
					}
				}
				if(me.hasClass("cms-area-img")){
					me.attr("src",imgs[i]);
				}
				if(me.find(".cms-area-desc").length>0){
					me.find(".cms-area-desc").each(function(i){
						$j(this).html(descs[sum]);
						sum++;
					});
				}
				if(me.hasClass("cms-area-desc")){
					me.html(descs[sum]);
					sum++;
				}
				
			});
		}else{
			//普通模式
			imgarticle.find(".cms-area-title").html(ts[0]);
			imgarticle.find(".cms-area-title").attr("title",ts[0]);
			if(imgarticle.find(".cms-area-title").parent().hasClass("cms-area-href")){
				imgarticle.find(".cms-area-title").parent().attr("href",ths[0]);
			}else{
				if(ths[0]!=null && ths[0]!=""){
					imgarticle.find(".cms-area-href").attr("href",ths[0]);
				}
				
			}
			imgarticle.find(".cms-area-img").attr("src",imgs[0]);
			imgarticle.find(".cms-area-img").attr("alt",imgalts[0]);
			if(imgarticle.find(".cms-area-img").parent().hasClass("cms-area-href")){
				imgarticle.find(".cms-area-img").parent().attr("href",imghrefs[0]);
			}else{
				if(imghrefs[0]!=null && imghrefs[0]!=""){
					imgarticle.find(".cms-area-href").attr("href",imghrefs[0]);
				}
			}
			if(imgarticle.find(".cms-area-desc").length>0){
				imgarticle.find(".cms-area-desc").each(function(i){
					$j(this).html(descs[i]);
				});
			}
			if(imgarticle.hasClass("cms-area-desc")){
				imgarticle.html(descs[0]);
			}
		}
		//重新设置图文模式编辑事件
		addEditEvent(areaCode,".cms-imgarticle-edit");
		setTimeout(function(){addEditEvent(areaCode,".cms-imgarticle-edit");},1000);
		$j("#isSaved").val(false);
		$j('.dialog-close').click();
	});
	$j('.add-tmp').click(function(){
		//添加图文模式
		var areaList = $j(".cms-imgarticle-edit-dialog .area-list").clone(true)
		.removeClass("area-list").addClass("area-list-add");
		areaList.show();
		//将图文模式编辑框数据设置空
		areaList.find(".slideToggle").children().each(function(i,dom){
			$j(dom).show();
			if($j(dom).find("input").find(".fileupload").length==0){
				$j(dom).find("input").val(null);
			}
			$j(dom).find("textarea").val(null);
		});
		//新增模板
		areaList.find(".select").html("收起-新增模板");
		areaList.find(".remove").val("删除");
		//获取编辑的图文对象 并克隆一下新的对象添加到列表中
		var cloneObj = picTextObj.find(".cms-area-list-element :last").clone(true);
		//设置对应编辑为空
		cloneObj.find(".cms-area-title").html(null);
		cloneObj.find(".cms-area-desc").html(null);
		cloneObj.find(".cms-area-img").attr("src",null);
		cloneObj.find(".cms-area-img").attr("alt",null);
		//设置图片链接
		if(cloneObj.find(".cms-area-img").parent().hasClass("cms-area-href")){
			cloneObj.find(".cms-area-img").parent().find(".cms-area-href").attr("href",null);
		}else{
			cloneObj.find(".cms-area-href").attr("href",null);
		}
		//设置标题链接
		if(cloneObj.find(".cms-area-title").hasClass("cms-area-href")){
			cloneObj.find(".cms-area-title").parent().attr("href",null);
		}else{
			cloneObj.find(".cms-area-href").attr("href",null);
		}
		//提供删除时 功能
		var num = arealistObj.length;
		//添加到列表中
		var tem ={"num":num,"obj":cloneObj};
		arealistObj.push(tem);
		areaList.find(".remove").attr("num",parseInt(num)+1);
		picTextObj.find(".cms-area-list-element :last").after(cloneObj);
		if(cloneObj.find(".cms-area-title").length>0){
			areaList.find(".title").show();
		}else{
			areaList.find(".title").hide();
		}
		if(cloneObj.find(".cms-area-title").hasClass("cms-area-href")){
			areaList.find(".href-title").show();	
		}else{
			areaList.find(".href-title").hide();
		}
		if(cloneObj.find(".cms-area-img").length>0){
			areaList.find(".img").show();
			areaList.find(".img-alt").show();
		}else{
			areaList.find(".img").hide();
			areaList.find(".img-alt").hide();
		}
		if(cloneObj.find(".cms-area-img").parent().hasClass("cms-area-href")){
			areaList.find(".href-img").show();
		}else{
			areaList.find(".href-img").hide();
		}
		if(cloneObj.find(".cms-area-desc").length>0){
			areaList.find(".desc").show();
		}else{
			areaList.find(".desc").hide();
		}
		dialog_content.find(".proto-dialog-content").append(areaList);
		// 重新加载上传组件
		//$j.getScript(base+'/scripts/ajaxfileupload.js');
		jQuery(".imgUploadComponet").each(function(i,n){
			jQuery.initUploadImg(jQuery(this));
		});

		$j(".cms-imgarticle-edit-dialog .proto-dialog-content").scrollTop(250*num);
		//重新设置图文模式编辑事件
		var areaCode = $j('.cms-imgarticle-edit-dialog #areaCode').val();
		addEditEvent(areaCode,".cms-imgarticle-edit");
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
		me.parent().parent().parent().find(".slideToggle").each(function(i,dom){
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
	
});

function  showOrHideEditArea(hide,areaCode){
	var templateId;
	var pageId;
	var type =  $j('#type').val();
	var versionId;
	var json;
	var editAreaHideUrl;
	if(type=="page"){
		 templateId =$j('#templateId').val();
		 pageId = $j('#pageId').val();
		 versionId = $j("#versionId").val();
	}else{
		 templateId =$j('#templateId').val();
		 pageId = $j('#moduleId').val();
		 versionId = $j("#versionId").val();
	}
	if(versionId != null && versionId != ""){
		json={"areaCode":areaCode,"templateId":templateId,"pageId":pageId , "versionId":versionId, "hide":hide,"type":type};
		editAreaHideUrl = base+"/cms/editAreaVersionHide.json";
	}else{
		json={"areaCode":areaCode,"templateId":templateId,"pageId":pageId , "hide":hide,"type":type};
		editAreaHideUrl = base+"/cms/editAreaHide.json";
	}
	
 	nps.asyncXhrPost(editAreaHideUrl, json,{successHandler:function(data, textStatus){
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