$j.extend(loxia.regional['zh-CN'],{
	"SYSTEM_ITEM_MESSAGE":"提示信息",
	"SAVE_SUCCESS":"保存成功",
	"SAVE_FAILURE":"保存失败",
	"CONFIRM_DELETE":"是否确定删除?",
	"DELETE_SUCCESS":"删除成功",
	"PLEASE_UPLOAD_IMAGE":"请上传图片",
	"PUBLISH_SUCCESS":"发布成功",
	"PUBLISH_FAILURE":"发布失败",
	"NO_IMAGE_VIEW":"没有图片可以被查看",
	

});
var propertyArray  = new Array();
var propertyNameArray  = new Array();
var mustCheckArray  = new Array();
var categoryInputObj = "";
var currImgFloatObj = "";

//url
var saveColumnComponentUrl = base + '/column/saveColumnComponent.htm';

var findItemCommandByCodeUrl = base + '/item/findItemCommandByCode.json';

var nowPublishUrl = base + '/column/nowPublish.json';

/** 上传图片 之后的回调 */
function fnCallback(data, hName){
	var $uploadInput = $j('input[hName="'+hName+'"]');
	var banDiaObj = $uploadInput.parents('.proto-dialog');
	var imgUrl = processImgUrl(data.url, 'source');
	var thisheight = data.height;
	var thiswidth = data.width;
	banDiaObj.find("img").attr("src", imgUrl);
	if(thiswidth>thisheight){
		banDiaObj.find(".image-url").css({width:"130px",height:"auto"});
	}else if(thiswidth<thisheight){
		banDiaObj.find(".image-url").css({height:"130px",width:"auto"});
	}else{
		banDiaObj.find(".image-url").css({width:"130px",height:"auto"});
	}
	
	var $colorSelectLine = $uploadInput.parents('.color-select-line');
	var oldImgUrl = $colorSelectLine.find('input[name$="-url"]').val();
	$colorSelectLine.find('input[name$="-url"]').val(imgUrl);
	$colorSelectLine.find('.view-img').attr('vWidth', thiswidth).attr('vHeight', thisheight);
	
	// 是否是PC图片, 如果没有上传平板, 手机端的图片就默认用PC端的图片
	if($uploadInput.parents('.ui-block-line').hasClass('pc-image')){
		
		banDiaObj.find('input[name="vWidth"]').val(data.width);
		banDiaObj.find('input[name="vHeight"]').val(data.height);
		
		var role = $uploadInput.attr('role');
		var mobileRole = '';
		var tabletRole = '';
		var tabletWidth = thiswidth;
		var tabletHeigth = thisheight;
		var mobileWidth = thiswidth;
		var mobileHeigth = thisheight;
		if(role){
			var roles = role.split('|');
			if(roles.length == 2){
				tabletRole = roles[0];
				if(tabletRole){
					var tabletRoles = tabletRole.split('X');
					if(tabletRoles.length == 2){
						tabletWidth = tabletRoles[0];
						tabletHeigth = tabletRoles[1];
					}else{
						console.log('tablet size is error!');
					}
				}
				mobileRole = roles[1];
				if(mobileRole){
					var moblieRoles = mobileRole.split('X');
					if(moblieRoles.length == 2){
						mobileWidth = moblieRoles[0];
						mobileHeigth = moblieRoles[1];
					}else{
						console.log('mobile size is error!');
					}
				}
			}else{
				mobileRole = tabletRole = 'source';
			}
		}else{
			mobileRole = tabletRole = 'source';
		}
		
		/** 平板端 */
		var $tabletImage = $uploadInput.parents('.ui-block-line').siblings('.tablet-image');
		var tabletImageUrl = $tabletImage.find('input[name$="tablet-dialog-url"]').val();
		
		if(!tabletImageUrl || processImgUrl(oldImgUrl,tabletRole) == tabletImageUrl || oldImgUrl == tabletImageUrl){
			$tabletImage.find('input[name$="tablet-dialog-url"]').val(processImgUrl(imgUrl,tabletRole));
			$tabletImage.find('.view-img').attr('vWidth', tabletWidth).attr('vHeight', tabletHeigth);
		}
		/** 手机端 */
		var $mobileImage = $uploadInput.parents('.ui-block-line').siblings('.mobile-image');
		var mobileImageUrl = $mobileImage.find('input[name$="mobile-dialog-url"]').val();
		
		if(!mobileImageUrl || processImgUrl(oldImgUrl,mobileRole) == mobileImageUrl || oldImgUrl == mobileImageUrl){
			$mobileImage.find('input[name$="mobile-dialog-url"]').val(processImgUrl(imgUrl,mobileRole));
			$mobileImage.find('.view-img').attr('vWidth', mobileWidth).attr('vHeight', mobileHeigth);
		}
	}
}

/** 获取商品信息*/
function fnGetItemInfo(sel, obj){
	var json = {"code": sel};
	var itemCommand = loxia.syncXhr(findItemCommandByCodeUrl, json, {type: "POST"});
	var dialogClass = obj.attr("name");
	var currObj = $j('.'+dialogClass);
	var picUrl = defaultNonItemImgUrl;
	var itemName = "";
	var itemId = "";
	if(itemCommand != ''){
		picUrl = itemCommand.picUrl;
		itemName = itemCommand.title;
		itemId = itemCommand.id;
		$j('.'+dialogClass).find('.orange').removeClass('disabled');
		$j('.'+dialogClass).find('.propmt').hide();
	}else{
		$j('.'+dialogClass).find('.orange').addClass('disabled');
		$j('.'+dialogClass).find('.propmt').show();
	}
	currObj.find('img').attr('src', picUrl);
	currObj.find('.text').html(itemName);
	currObj.find('input[name="itemId"]').val(itemId);
	return loxia.SUCCESS;
}

/** 保存版块-组件 */
function fnSaveColumnComponent(formId){
	var data = loxia.syncXhr(saveColumnComponentUrl, formId, { type: "POST"});
	if(data.isSuccess){
		nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("SAVE_SUCCESS"));
	}else{
		nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n(data.exception.message));
	}
}

/** 新增 版块-组件*/
function fnNewColumnComponent(name){
	/** 清空dialog中的信息 */
	fnClearDialogMessage(name+"-dialog");
	var currDialogObj = $j('.'+name+'-dialog');
	currDialogObj.find('h5').html('新增信息');
	currDialogObj.find('#'+name+'-flag').val('isNew');
	currDialogObj.find('.text').html("");
	currDialogObj.dialogff({type:'open',close:'in',width:'420px', height:'450px'});
}


/** 清空dialog中的信息 */
function fnClearDialogMessage(dialogClass){
	var dialogObj = $j("."+dialogClass).children(".proto-dialog-content");
	dialogObj.find('input').val("");
	dialogObj.find('img').attr('src', defaultNonItemImgUrl);
	dialogObj.find('select').val(1);
	dialogObj.find('.propmt').hide();
}


function imageCenter(){
	$j(".img-float img").each(function(){
		var thiswidth=parseInt($j(this).attr("vwidth"));
		var thisheight=parseInt($j(this).attr("vheight"));
		if(thiswidth!=""&&thisheight!=""){
			if(thiswidth>thisheight){
				$j(this).css({width:"100%",height:"auto"});
			}else if(thiswidth<thisheight){
				$j(this).css({height:"100%",width:"auto"});
			}else{
				$j(this).css({width:"100%",height:"auto"});
			}
		}
	});
}

// 封装ext数据
function fnProcessExt(object){
	// 平板端
	var $tabletImage = object.find('.tablet-image');
	var $mobileImage = object.find('.mobile-image');
	var extArr = new Array();
	extArr.push('{"tabletImg" : "'+$tabletImage.find('input[name$="tablet-dialog-url"]').val().replace(customBaseUrl, ''));
	extArr.push('", "tabletWidth" : "'+$tabletImage.find('.view-img').attr('vWidth'));
	extArr.push('", "tabletHeight" : "'+$tabletImage.find('.view-img').attr('vHeight'));
	extArr.push('", "mobileImg" : "'+$mobileImage.find('input[name$="mobile-dialog-url"]').val().replace(customBaseUrl, ''));
	extArr.push('", "mobileWidth" : "'+$mobileImage.find('.view-img').attr('vWidth'));
	extArr.push('", "mobileHeight" : "'+$mobileImage.find('.view-img').attr('vHeight'));
	extArr.push('"}');
	return extArr.join('');
}

// 解析ext数据
function fnParseExt(object, ext, vWidth, vHeight){
	if(ext){
		var extJson = $j.parseJSON(ext);
		// pc
		object.find('.pc-image').find('.view-img').attr('vWidth', vWidth).attr('vHeight', vHeight);
		// tablet
		var $tabletImage = object.find('.tablet-image'); 
		$tabletImage.find('input[name="banner-tablet-dialog-url"]').val(customBaseUrl+extJson.tabletImg);
		$tabletImage.find('.view-img').attr('vWidth', extJson.tabletWidth).attr('vHeight', extJson.tabletHeight);
		// mobile
		var $mobileImage = object.find('.mobile-image');
		$mobileImage.find('input[name="banner-mobile-dialog-url"]').val(customBaseUrl+extJson.mobileImg);
		$mobileImage.find('.view-img').attr('vWidth', extJson.mobileWidth).attr('vHeight', extJson.mobileHeight);
	}
}


$j(document).ready(function(){
//***************************************BANNER****************************************************
	 /** 新增banner */
    $j(".ui-block-content").on("click", ".newBanner", function(){
    	fnNewColumnComponent("banner");
    });
    
    /** 确定*/
    $j(".banner_confirm").on("click", function(){
    	var bannerDialogObj = $j(".banner-dialog");
    	var banner_flag = $j('#banner-flag').val(); 
    	var imgUrl = bannerDialogObj.find('input[name$="-url"]').val();
    	var vWidth = bannerDialogObj.find('input[name="vWidth"]').val();
    	var vHeight = bannerDialogObj.find('input[name="vHeight"]').val();
    	if(imgUrl == ''){
    		bannerDialogObj.find('input[name$="-url"]').addClass('ui-loxia-error');
    		nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("PLEASE_UPLOAD_IMAGE"));
    		return;
    	}
    	var url = bannerDialogObj.find('input[name="url"]').val();
    	var id = $j('#banner-columnComp-id').val();
    	if(banner_flag == 'isNew'){
    		id = "";
    	}
      	//封装ext数据 
    	var ext = fnProcessExt(bannerDialogObj);
    	
    	var htmlArr = new Array();
    	htmlArr.push("<div class='img-float idis'"+id+">");
    	htmlArr.push("<table cellspacing='0' cellpadding='0' border='0' class='mc-img-tb'><tr><td><img src='"+imgUrl+"' vWidth='"+vWidth+"' vHeight='"+vHeight+"' /></td></tr></table>");
    	htmlArr.push("<input loxiaType='input' readonly='true' value='链接:"+url+"'/>");
    	htmlArr.push("<input type='hidden' name='columnComponents.img' readonly='true' value='"+imgUrl+"'/>");
    	htmlArr.push("<input type='hidden' name='columnComponents.url' readonly='true' value='"+url+"'/>");
    	htmlArr.push("<input type='hidden' name='columnComponents.imgWidth' readonly='true' value='"+vWidth+"'/>");
    	htmlArr.push("<input type='hidden' name='columnComponents.imgHeight' readonly='true' value='"+vHeight+"'/>");
    	htmlArr.push("<input name='columnComponents.id' type='hidden' value='"+id+"'/>");
    	htmlArr.push("<input name='columnComponents.ext' type='hidden' value='"+ext+"'/>");
    	htmlArr.push("<div class='if-control-c'>");
    	htmlArr.push("<a href='javascript:void(0)' class='ifc-button ifc-update banner-update'>编辑</a>");
    	htmlArr.push("<a href='javascript:void(0)' class='ifc-button ifc-delete'>删除</a>");
    	htmlArr.push("</div></div>");
    	
    	var html = htmlArr.join('');
    	if(banner_flag == 'isNew'){
    		$j(".banner-content").append(html);
    	}else{
    		currImgFloatObj.replaceWith(html);
    	}
    	imageCenter();
    	$j('.dialog-close').click();
    	loxia.initContext($j(".banner-content"));
    });
    
    /** banner 编辑*/
    $j(".ui-block-content").on("click",".banner-update",function(){
    	fnNewColumnComponent("banner");
    	
    	var currObj = $j(this).parents('.img-float');
    	currImgFloatObj = currObj;
    	var imgUrl = currObj.find('input[name="columnComponents.img"]').val();
    	var url = currObj.find('input[name="columnComponents.url"]').val();
    	var id = currObj.find('input[name="columnComponents.id"]').val();
    	var moduleId = currObj.find('input[name="columnComponents.moduleId"]').val();
    	var vWidth = currObj.find('input[name="columnComponents.imgWidth"]').val();
    	var vHeight = currObj.find('input[name="columnComponents.imgHeight"]').val();
    	var ext = currObj.find('input[name="columnComponents.ext"]').val();
    	

    	var bannerDialogObj = $j(".banner-dialog");
    	if(vWidth>vHeight){
    		bannerDialogObj.find("img").css({width:"130px",height:"auto"});
    	}else if(vWidth<vHeight){
    		bannerDialogObj.find("img").css({height:"130px",width:"auto"});
    	}else{
    		bannerDialogObj.find("img").css({width:"130px",height:"auto"});
    	}
    	
    	//解析ext数据 
    	fnParseExt(bannerDialogObj, ext, vWidth, vHeight);
    	
    	bannerDialogObj.find('h5').html('编辑信息');
    	$j('#banner-columnComp-module-id').val(moduleId);
    	bannerDialogObj.find('#banner-columnComp-id').val(id);
    	bannerDialogObj.find('img').attr('src', imgUrl);
    	bannerDialogObj.find('input[name="banner-pc-dialog-url"]').val(imgUrl);
    	bannerDialogObj.find('input[name="url"]').val(url);
    	bannerDialogObj.find('input[name="vWidth"]').val(vWidth);
    	bannerDialogObj.find('input[name="vHeight"]').val(vHeight);
    	$j('#banner-flag').val('isUpdate');
    	//bannerDialogObj.dialogff({type:'open',close:'in',width:'420px', height:'450px'});
	});

    /** banner 保存 */
    $j(".save_banner").on("click", function(){
    	fnSaveColumnComponent("banner-form");
    });
    
//***************************************BRAND****************************************************
    /** 新增*/
    $j(".ui-block-content").on("click", ".new-brand", function(){
    	fnNewColumnComponent("brand");
    });
    
    /** 确定*/
    $j(".brand-confirm").on("click", function(){
    	var bannerDialogObj = $j(".brand-dialog");
    	var banner_flag = $j('#brand-flag').val(); 
    	var imgUrl = bannerDialogObj.find('input[name$="-url"]').val();
    	var vWidth = bannerDialogObj.find('input[name="vWidth"]').val();
    	var vHeight = bannerDialogObj.find('input[name="vHeight"]').val();
    	if(imgUrl == ''){
    		bannerDialogObj.find('input[name$="-url"]').addClass('ui-loxia-error');
    		nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("PLEASE_UPLOAD_IMAGE"));
    		return;
    	}
    	var url = bannerDialogObj.find('input[name="url"]').val();
    	var id = $j('#brand-columnComp-id').val();
    	if(banner_flag == 'isNew'){
    		id = "";
    	}
    	//封装ext数据 
    	var ext = fnProcessExt(bannerDialogObj);
    	
    	var htmlArr = new Array();
    	htmlArr.push("<div class='img-float idis'"+id+">");
    	htmlArr.push("<table cellspacing='0' cellpadding='0' border='0' class='mc-img-tb'><tr><td><img src='"+imgUrl+"' vWidth='"+vWidth+"' vHeight='"+vHeight+"' /></td></tr></table>");
    	htmlArr.push("<input loxiaType='input' readonly='true' value='链接:"+url+"'/>");
    	htmlArr.push("<input type='hidden' name='columnComponents.img' readonly='true' value='"+imgUrl+"'/>");
    	htmlArr.push("<input type='hidden' name='columnComponents.url' readonly='true' value='"+url+"'/>");
    	htmlArr.push("<input type='hidden' name='columnComponents.imgWidth' readonly='true' value='"+vWidth+"'/>");
    	htmlArr.push("<input type='hidden' name='columnComponents.imgHeight' readonly='true' value='"+vHeight+"'/>");
    	htmlArr.push("<input name='columnComponents.id' type='hidden' value='"+id+"'/>");
    	htmlArr.push("<input name='columnComponents.ext' type='hidden' value='"+ext+"'/>");
    	htmlArr.push("<div class='if-control-c'>");
    	htmlArr.push("<a href='javascript:void(0)' class='ifc-button ifc-update brand-update'>编辑</a>");
    	htmlArr.push("<a href='javascript:void(0)' class='ifc-button ifc-delete'>删除</a>");
    	htmlArr.push("</div></div>");
    	
    	var html = htmlArr.join('');
    	if(banner_flag == 'isNew'){
    		$j(".brand-content").append(html);
    	}else{
    		currImgFloatObj.replaceWith(html);
    	}
    	imageCenter();
    	$j('.dialog-close').click();
    	loxia.initContext($j(".brand-content"));
    });
    
    /**编辑*/
    $j(".ui-block-content").on("click",".brand-update",function(){
    	fnNewColumnComponent("brand");
    	
    	var currObj = $j(this).parents('.img-float');
    	currImgFloatObj = currObj;
    	var imgUrl = currObj.find('input[name="columnComponents.img"]').val();
    	var url = currObj.find('input[name="columnComponents.url"]').val();
    	var id = currObj.find('input[name="columnComponents.id"]').val();
    	var moduleId = currObj.find('input[name="columnComponents.moduleId"]').val();
    	var vWidth = currObj.find('input[name="columnComponents.imgWidth"]').val();
    	var vHeight = currObj.find('input[name="columnComponents.imgHeight"]').val();
    	var ext = currObj.find('input[name="columnComponents.ext"]').val();
    	
    	
    	var bannerDialogObj = $j(".brand-dialog");
    	if(vWidth>vHeight){
    		bannerDialogObj.find("img").css({width:"130px",height:"auto"});
    	}else if(vWidth<vHeight){
    		bannerDialogObj.find("img").css({height:"130px",width:"auto"});
    	}else{
    		bannerDialogObj.find("img").css({width:"130px",height:"auto"});
    	}
    	
    	//解析ext数据 
    	fnParseExt(bannerDialogObj, ext, vWidth, vHeight);
    	
    	bannerDialogObj.find('h5').html('编辑信息');
    	$j('#brand-columnComp-module-id').val(moduleId);
    	bannerDialogObj.find('#brand-columnComp-id').val(id);
    	bannerDialogObj.find('img').attr('src', imgUrl);
    	bannerDialogObj.find('input[name="brand-pc-dialog-url"]').val(imgUrl);
    	bannerDialogObj.find('input[name="url"]').val(url);
    	bannerDialogObj.find('input[name="vWidth"]').val(vWidth);
    	bannerDialogObj.find('input[name="vHeight"]').val(vHeight);
    	$j('#brand-flag').val('isUpdate');
    	//bannerDialogObj.dialogff({type:'open',close:'in',width:'420px', height:'450px'});
    });
    
    /** 保存 */
    $j(".save-brand").on("click", function(){
    	fnSaveColumnComponent("brand-form");
    });
    
//***************************************新品主推活动管理****************************************************
    /** 新增 */
    $j(".ui-block-content").on("click", ".newnew-push-activity", function(){
    	fnNewColumnComponent("new-push-activity");
    });
    
    /** 确定*/
    $j(".new-push-activity_confirm").on("click", function(){
    	var newPushDialogObj = $j(".new-push-activity-dialog");
    	var banner_flag = $j('#new-push-activity-flag').val(); 
    	var imgUrl = newPushDialogObj.find('input[name$="-url"]').val();
    	var vWidth = newPushDialogObj.find('input[name="vWidth"]').val();
    	var vHeight = newPushDialogObj.find('input[name="vHeight"]').val();
    	if(imgUrl == ''){
    		newPushDialogObj.find('input[name$="-url"]').addClass('ui-loxia-error');
    		nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("PLEASE_UPLOAD_IMAGE"));
    		return;
    	}
    	var url = newPushDialogObj.find('input[name="url"]').val();
    	var id = $j('#new-push-activity-columnComp-id').val();
    	if(banner_flag == 'isNew'){
    		id = "";
    	}
    	//封装ext数据 
    	var ext = fnProcessExt(newPushDialogObj);
    	
    	var htmlArr = new Array();
    	htmlArr.push("<div class='img-float idis'"+id+">");
    	htmlArr.push("<table cellspacing='0' cellpadding='0' border='0' class='mc-img-tb'><tr><td><img src='"+imgUrl+"' vWidth='"+vWidth+"' vHeight='"+vHeight+"' /></td></tr></table>");
    	htmlArr.push("<input loxiaType='input' readonly='true' value='链接:"+url+"'/>");
    	htmlArr.push("<input type='hidden' name='columnComponents.img' readonly='true' value='"+imgUrl+"'/>");
    	htmlArr.push("<input type='hidden' name='columnComponents.url' readonly='true' value='"+url+"'/>");
    	htmlArr.push("<input type='hidden' name='columnComponents.imgWidth' readonly='true' value='"+vWidth+"'/>");
    	htmlArr.push("<input type='hidden' name='columnComponents.imgHeight' readonly='true' value='"+vHeight+"'/>");
    	htmlArr.push("<input name='columnComponents.id' type='hidden' value='"+id+"'/>");
    	htmlArr.push("<input name='columnComponents.ext' type='hidden' value='"+ext+"'/>");
    	htmlArr.push("<div class='if-control-c'>");
    	htmlArr.push("<a href='javascript:void(0)' class='ifc-button ifc-update banner-update'>编辑</a>");
    	htmlArr.push("<a href='javascript:void(0)' class='ifc-button ifc-delete'>删除</a>");
    	htmlArr.push("</div></div>");
    	
    	var html = htmlArr.join('');
    	if(banner_flag == 'isNew'){
    		$j(".new-push-activity-content").append(html);
    	}else{
    		currImgFloatObj.replaceWith(html);
    	}
    	imageCenter();
    	$j('.dialog-close').click();
    	loxia.initContext($j(".new-push-activity-content"));
    });
    
    /** banner 编辑*/
    $j(".ui-block-content").on("click",".new-push-activity-update",function(){
    	fnNewColumnComponent("new-push-activity");
    	
    	var currObj = $j(this).parents('.img-float');
    	currImgFloatObj = currObj;
    	var imgUrl = currObj.find('input[name="columnComponents.img"]').val();
    	var url = currObj.find('input[name="columnComponents.url"]').val();
    	var id = currObj.find('input[name="columnComponents.id"]').val();
    	var moduleId = currObj.find('input[name="columnComponents.moduleId"]').val();
    	var vWidth = currObj.find('input[name="columnComponents.imgWidth"]').val();
    	var vHeight = currObj.find('input[name="columnComponents.imgHeight"]').val();
    	var ext = currObj.find('input[name="columnComponents.ext"]').val();
    	
    	var newPushDialogObj = $j(".new-push-activity-dialog");
    	if(vWidth>vHeight){
    		newPushDialogObj.find("img").css({width:"130px",height:"auto"});
    	}else if(vWidth<vHeight){
    		newPushDialogObj.find("img").css({height:"130px",width:"auto"});
    	}else{
    		newPushDialogObj.find("img").css({width:"130px",height:"auto"});
    	}
    	
    	//解析ext数据 
    	fnParseExt(newPushDialogObj, ext, vWidth, vHeight);
    	
    	newPushDialogObj.find('h5').html('编辑信息');
    	$j('#new-push-activity-columnComp-module-id').val(moduleId);
    	newPushDialogObj.find('#new-push-activity-columnComp-id').val(id);
    	newPushDialogObj.find('img').attr('src', imgUrl);
    	newPushDialogObj.find('input[name="banner-pc-dialog-url"]').val(imgUrl);
    	newPushDialogObj.find('input[name="url"]').val(url);
    	newPushDialogObj.find('input[name="vWidth"]').val(vWidth);
    	newPushDialogObj.find('input[name="vHeight"]').val(vHeight);
    	$j('#new-push-activity-flag').val('isUpdate');
    	//newPushDialogObj.dialogff({type:'open',close:'in',width:'400px', height:'400px'});
    });
    
    /** banner 保存 */
    $j(".save_new-push-activity").on("click", function(){
    	fnSaveColumnComponent("new-push-activity-form");
    });
//***************************************************LEVIS商品推荐管理***************************************************
    /** 新增 */
    $j('.levis-new').on('click', function(){
    	fnNewColumnComponent("levis");
    });
    
    $j('.levis-dialog-code').on('change', function(){
    	fnGetItemInfo($j(this).val(), $j(this));
    });
    
    /** 编辑*/
    $j(".ui-block-content").on("click",".levis-update",function(){
    	var nhpObj = $j(this).parents('.img-float');
    	currImgFloatObj = nhpObj;
    	var picUrl = nhpObj.find('img').attr('src');
    	var itemTitle = nhpObj.find('input[name="itemTile"]').val();
    	var itemCode = nhpObj.find('input[name="itemCode"]').val();
    	var id = nhpObj.find('input[name="columnComponents.id"]').val();
    	var moduleId = nhpObj.find('input[name="columnComponents.moduleId"]').val();
    	var targetId = nhpObj.find('input[name="columnComponents.targetId"]').val();
    		
    	var nhpdDialogObj = $j('.levis-dialog');
    	nhpdDialogObj.find("img").css({width:"180px",height:"180px"});
    	nhpdDialogObj.find('.propmt').hide();
    	nhpdDialogObj.find('.orange').removeClass('disabled');
    	nhpdDialogObj.find('h5').html('编辑信息');
    	$j('#levis-columnComp-id').val(id);
    	$j('#levis-columnComp-module-id').val(moduleId);
    	nhpdDialogObj.find('img').attr('src', picUrl);
    	nhpdDialogObj.find('.text').html(itemTitle);
    	nhpdDialogObj.find('.levis-dialog-code ').val(itemCode);
    	nhpdDialogObj.find('input[name="itemId"]').val(targetId);
    	$j("#levis-flag").val("isUpdate");
    	nhpdDialogObj.dialogff({type:'open',close:'in',width:'400px', height:'400px'});
    });
    
    /** 确定 */
    $j('.levis-dialog-confirm').on('click', function(){
    	if($j(this).hasClass('disabled')){
    		return;
    	}
    	var flag = $j("#levis-flag").val();
    	var nhpdObj = $j('.levis-dialog');
    	var picUrl = nhpdObj.find('img').attr("src");
    	var title = nhpdObj.find('.text').html();;
    	var itemId = nhpdObj.find('input[name="itemId"]').val();
    	var id = $j('#levis-columnComp-id').val();
    	var itemCode = nhpdObj.find('input[name="levis-dialog"]').val();
    	if(flag == 'isNew'){
    		id = '';
    	}
    	var html = '<div class="img-float idis'+id+'">'
    		+'<table cellspacing="0" cellpadding="0" border="0" class="mc-img-tb"><tr><td><img src="'+picUrl+'" style="width: 100%; height: auto;"/></td></tr></table>'
    		+'<input loxiaType="input" readonly="true" value="商品名称:'+title+'"/>'
    		+'<input name="columnComponents.img" type="hidden" value="'+picUrl+'"/>'
    		+'<input name="columnComponents.targetId" type="hidden" value="'+itemId+'"/>'
    		+'<input name="columnComponents.id" type="hidden" value="'+id+'"/>'
    		+'<input name="itemCode" type="hidden" value="'+itemCode+'"/>'
    		+'<div class="if-control-c">'
    		+'<a href="javascript:void(0)" class="ifc-button ifc-update levis-update">编辑</a>'
    		+'<a href="javascript:void(0)" class="ifc-button ifc-delete">删除</a>'
    		+'</div>'
    		+'</div>';
    	if(flag == 'isNew'){
    		$j('.levis-content').append(html);
    	}else{
    		currImgFloatObj.replaceWith(html);
    	}
    	
    	$j('.dialog-close').click();
    	loxia.initContext($j(".levis-content"));
    });
    
    /** 保存 */
    $j(".levis-save").on("click", function(){
    	fnSaveColumnComponent("levis-form");
    });
    
//*********************************************CONVERSE商品推荐管理************************************************
    /** 新增 */
    $j('.converse-new').on('click', function(){
    	fnNewColumnComponent("converse");
    });
    
    $j('.converse-dialog-code').on('change', function(){
    	fnGetItemInfo($j(this).val(), $j(this));
    });
    
    /** 编辑*/
    $j(".ui-block-content").on("click",".converse-update",function(){
    	var currObj = $j(this).parents('.img-float');
    	currImgFloatObj = currObj;
    	var picUrl = currObj.find('img').attr('src');
    	var itemTitle = currObj.find('input[name="itemTile"]').val();
    	var itemCode = currObj.find('input[name="itemCode"]').val();
    	var id = currObj.find('input[name="columnComponents.id"]').val();
    	var moduleId = currObj.find('input[name="columnComponents.moduleId"]').val();
    	var targetId = currObj.find('input[name="columnComponents.targetId"]').val();
    	
    	var currDialogObj = $j('.converse-dialog');
    	currDialogObj.find("img").css({width:"180px",height:"180px"});
    	currDialogObj.find('h5').html('编辑信息');
    	currDialogObj.find('.propmt').hide();
    	currDialogObj.find('.orange').removeClass('disabled');
    	$j('#converse-columnComp-id').val(id);
    	$j('#converse-module-id').val(moduleId);
    	currDialogObj.find('img').attr('src', picUrl);
    	currDialogObj.find('.text').html(itemTitle);
    	currDialogObj.find('.converse-dialog-code').val(itemCode);
    	currDialogObj.find('input[name="itemId"]').val(targetId);
    	$j("#new-hot-push-flag").val("isUpdate");
    	currDialogObj.dialogff({type:'open',close:'in',width:'400px', height:'400px'});
    });
    
    /** 确定 */
    $j('.converse-confirm').on('click', function(){
    	if($j(this).hasClass('disabled')){
    		return;
    	}
    	var name = 'converse';
    	var flag = $j("#converse-flag").val();
    	var currDialogObj = $j('.converse-dialog');
    	var picUrl = currDialogObj.find('img').attr("src");
    	var itemName = currDialogObj.find('.text').html();;
    	var itemId = currDialogObj.find('input[name="itemId"]').val();
    	var id = $j('#'+name+'-columnComp-id').val();
    	var itemCode = currDialogObj.find('input[name="converse-dialog"]').val();
    	if(flag == 'isNew'){
    		id = '';
    	}
    	var html = '<div class="img-float idis'+id+'">'
    		+'<table cellspacing="0" cellpadding="0" border="0" class="mc-img-tb"><tr><td><img src="'+picUrl+'" style="width: 100%; height: auto;"/></td></tr></table>'
    		+'<input loxiaType="input" readonly="true" value="商品名称:'+itemName+'"/>'
    		+'<input name="columnComponents.img" type="hidden" value="'+picUrl+'"/>'
    		+'<input name="columnComponents.targetId" type="hidden" value="'+itemId+'"/>'
    		+'<input name="columnComponents.id" type="hidden" value="'+id+'"/>'
    		+'<input name="itemCode" type="hidden" value="'+itemCode+'"/>'
    		+'<div class="if-control-c">'
    		+'<a href="javascript:void(0)" class="ifc-button ifc-update converse-update">编辑</a>'
    		+'<a href="javascript:void(0)" class="ifc-button ifc-delete">删除</a>'
    		+'</div>'
    		+'</div>';
    	if(flag == 'isNew'){
    		$j('.converse-content').append(html);
    	}else{
    		currImgFloatObj.replaceWith(html);
    	}
    	
    	$j('.dialog-close').click();
    	loxia.initContext($j(".converse-content"));
    });
    
    /** 保存 */
    $j(".converse-save").on("click", function(){
    	fnSaveColumnComponent("converse-form");
    });  

//***************************************************NIKE商品推荐管理************************************************
      /** 新增 */
      $j('.nike-new').on('click', function(){
      	fnNewColumnComponent("nike");
      });
      
      $j('.nike-dialog-code').on('change', function(){
    	  var val = $j(this).val();
    	  $j('.nike-dialog').find('input[name="itemCode"]').val(val);
          fnGetItemInfo(val, $j(this));
      });
      
      /** 编辑*/
      $j(".ui-block-content").on("click",".nike-update",function(){
      	var nhpObj = $j(this).parents('.img-float');
      	currImgFloatObj = nhpObj;
      	var picUrl = nhpObj.find('img').attr('src');
      	var itemTitle = nhpObj.find('input[name="itemTile"]').val();
      	var itemCode = nhpObj.find('input[name="itemCode"]').val();
      	var id = nhpObj.find('input[name="columnComponents.id"]').val();
      	var moduleId = nhpObj.find('input[name="columnComponents.moduleId"]').val();
      	var targetId = nhpObj.find('input[name="columnComponents.targetId"]').val();
      		
      	var currDialogObj = $j('.nike-dialog');
      	currDialogObj.find("img").css({width:"180px",height:"180px"});
      	currDialogObj.find('h5').html('编辑信息');
      	currDialogObj.find('.propmt').hide();
      	currDialogObj.find('.orange').removeClass('disabled');
      	currDialogObj.find('input[name="columnComp-id"]').val(id);
      	currDialogObj.find('input[name="module-id"]').val(moduleId);
      	currDialogObj.find('img').attr('src', picUrl);
      	currDialogObj.find('.text').html(itemTitle);
      	currDialogObj.find('.nike-dialog-code ').val(itemCode);
      	currDialogObj.find('input[name="itemId"]').val(targetId);
      	currDialogObj.find('input[name="itemCode"]').val(itemCode);
      	$j("#nike-flag").val("isUpdate");
      	currDialogObj.dialogff({type:'open',close:'in',width:'400px', height:'400px'});
      });
      
      /** 确定 */
      $j('.nike-dialog-confirm').on('click', function(){
	    if($j(this).hasClass('disabled')){
      		return;
      	}
      	var flag = $j("#nike-flag").val();
      	var nhpdObj = $j('.nike-dialog');
      	var picUrl = nhpdObj.find('img').attr("src");
      	var title = nhpdObj.find('.text').html();;
      	var itemId = nhpdObj.find('input[name="itemId"]').val();
      	var id = nhpdObj.find('input[name="columnComp-id"]').val();
//      	var moduleId = nhpdObj.find('input[name="module-id"]').val();
      	var itemCode = nhpdObj.find('input[name="itemCode"]').val();
      	if(flag == 'isNew'){
      		id = '';
      	}
      	var html = '<div class="img-float idis'+id+'">'
      	+'<table cellspacing="0" cellpadding="0" border="0" class="mc-img-tb"><tr><td><img src="'+picUrl+'" style="width: 100%; height: auto;"/></td></tr></table>'
      		+'<input loxiaType="input" readonly="true" value="商品名称:'+title+'"/>'
      		+'<input name="columnComponents.img" type="hidden" value="'+picUrl+'"/>'
      		+'<input name="columnComponents.targetId" type="hidden" value="'+itemId+'"/>'
      		+'<input name="columnComponents.id" type="hidden" value="'+id+'"/>'
    		+'<input name="itemCode" type="hidden" value="'+itemCode+'"/>'
    		+'<input name="itemTitle" type="hidden" value="'+title+'"/>'
      		+'<div class="if-control-c">'
      		+'<a href="javascript:void(0)" class="ifc-button ifc-update nike-update">编辑</a>'
      		+'<a href="javascript:void(0)" class="ifc-button ifc-delete">删除</a>'
      		+'</div>'
      		+'</div>';
      	if(flag == 'isNew'){
      		$j('.nike-content').append(html);
      	}else{
      		currImgFloatObj.replaceWith(html);
      	}
      	
      	$j('.dialog-close').click();
      	loxia.initContext($j(".nike-content"));
      });
      
      /** 保存 */
      $j(".nike-save").on("click", function(){
      	fnSaveColumnComponent("nike-form");
      });  

//***************************************************JORDAN商品推荐管理***********************************************
      /** 新增 */
      $j('.jordan-new').on('click', function(){
    	  fnNewColumnComponent("jordan");
      });
      
      $j('.jordan-dialog-code').on('change', function(){
    	  var val = $j(this).val();
    	  $j('.jordan-dialog').find('input[name="itemCode"]').val(val);
    	  fnGetItemInfo(val, $j(this));
      });
      
      /** 编辑*/
      $j(".ui-block-content").on("click",".jordan-update",function(){
    	  var nhpObj = $j(this).parents('.img-float');
    	  currImgFloatObj = nhpObj;
    	  var picUrl = nhpObj.find('img').attr('src');
    	  var itemTitle = nhpObj.find('input[name="itemTile"]').val();
    	  var itemCode = nhpObj.find('input[name="itemCode"]').val();
    	  var id = nhpObj.find('input[name="columnComponents.id"]').val();
    	  var moduleId = nhpObj.find('input[name="columnComponents.moduleId"]').val();
    	  var targetId = nhpObj.find('input[name="columnComponents.targetId"]').val();
    	  
    	  var currDialogObj = $j('.jordan-dialog');
    	  currDialogObj.find('h5').html('编辑信息');
    	  currDialogObj.find("img").css({width:"180px",height:"180px"});
    	  currDialogObj.find('.propmt').hide();
    	  currDialogObj.find('.orange').removeClass('disabled');
    	  currDialogObj.find('input[name="columnComp-id"]').val(id);
    	  currDialogObj.find('input[name="module-id"]').val(moduleId);
    	  currDialogObj.find('img').attr('src', picUrl);
    	  currDialogObj.find('.text').html(itemTitle);
    	  currDialogObj.find('.jordan-dialog-code ').val(itemCode);
    	  currDialogObj.find('input[name="itemId"]').val(targetId);
    	  currDialogObj.find('input[name="itemCode"]').val(itemCode);
    	  $j("#jordan-flag").val("isUpdate");
    	  currDialogObj.dialogff({type:'open',close:'in',width:'400px', height:'400px'});
      });
      
      /** 确定 */
      $j('.jordan-dialog-confirm').on('click', function(){
    	  if($j(this).hasClass('disabled')){
    		  return;
    	  }
    	  var flag = $j("#jordan-flag").val();
    	  var nhpdObj = $j('.jordan-dialog');
    	  var picUrl = nhpdObj.find('img').attr("src");
    	  var title = nhpdObj.find('.text').html();;
    	  var itemId = nhpdObj.find('input[name="itemId"]').val();
    	  var id = nhpdObj.find('input[name="columnComp-id"]').val();
    	  var itemCode = nhpdObj.find('input[name="itemCode"]').val();
    	  if(flag == 'isNew'){
    		  id = '';
    	  }
    	  var html = '<div class="img-float idis'+id+'">'
	    	  +'<table cellspacing="0" cellpadding="0" border="0" class="mc-img-tb"><tr><td><img src="'+picUrl+'" style="width: 100%; height: auto;"/></td></tr></table>'
	    	  +'<input loxiaType="input" readonly="true" value="商品名称:'+title+'"/>'
	    	  +'<input name="columnComponents.img" type="hidden" value="'+picUrl+'"/>'
	    	  +'<input name="columnComponents.targetId" type="hidden" value="'+itemId+'"/>'
	    	  +'<input name="columnComponents.id" type="hidden" value="'+id+'"/>'
	    	  +'<input name="itemCode" type="hidden" value="'+itemCode+'"/>'
	    	  +'<input name="itemTitle" type="hidden" value="'+title+'"/>'
	    	  +'<div class="if-control-c">'
	    	  +'<a href="javascript:void(0)" class="ifc-button ifc-update jordan-update">编辑</a>'
	    	  +'<a href="javascript:void(0)" class="ifc-button ifc-delete">删除</a>'
	    	  +'</div>'
	    	  +'</div>';
    	  if(flag == 'isNew'){
    		  $j('.jordan-content').append(html);
    	  }else{
    		  currImgFloatObj.replaceWith(html);
    	  }
    	  
    	  $j('.dialog-close').click();
    	  loxia.initContext($j(".jordan-content"));
      });
      
      /** 保存 */
      $j(".jordan-save").on("click", function(){
    	  fnSaveColumnComponent("jordan-form");
      });  
   
//*********************************************common*******************************************************************
    /** 取消 */
    $j(".cencal").click(function(){
    	$j('.dialog-close').click();
    });
    
    /** 删除 */
    $j(".ui-block-content").on("click", ".ifc-delete", function(){
    	var thisObj = $j(this);
    	nps.confirm(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("CONFIRM_DELETE"),function(){
    		thisObj.parents('.img-float').remove();
    		nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("DELETE_SUCCESS"));
    	});
    });
    
    /** 查看图片 */
    $j('.view-img').on('click', function(){
    	var imgUrl = $j(this).parents('.color-select-line').children('input').val();
    	if(!imgUrl){
    		nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("NO_IMAGE_VIEW"));
    		return;
    	}
    	var $imageViewDialog = $j('.image-view-dialog');
    	var $imageView = $imageViewDialog.find('.image-view');
    	$imageView.attr('src', imgUrl);
    	
    	var imgWidth = parseInt($j(this).attr('vWidth'));
    	var imgHeight = parseInt($j(this).attr('vHeight'));
    	var defaultSize = 500;
    	// 初始化img的width, height
    	$imageView.attr('width', 'auto').attr('height', 'auto');
    	
    	// 宽方形图片
    	if(imgWidth > imgHeight){
    		imgWidth = defaultSize;
    		$imageView.attr('width', imgWidth).attr('height', 'auto');
    	}
    	// 长方形图片
    	else if(imgWidth < imgHeight){
    		imgHeight = defaultSize;
    		$imageView.attr('width', 'auto').attr('height', imgHeight);
    	}
    	// 正方形图片, 如果长宽大于500, 就压缩到500
    	else if(imgHeight > defaultSize){
    		imgWidth = defaultSize;
    		imgHeight = defaultSize;
    	}
    	// open dialog
    	$imageViewDialog.dialogff({type:'open',close:'in',width:'525px', height:'525px'});
    });
    
//*********************************************现在发布******************************************************************
    $j('.now-publish').on('click', function(){
    	var json = {"pageCode":"HOME_PAGE"};
    	var data = loxia.syncXhr(nowPublishUrl, json, {type: "POST"});
    	if(data.isSuccess){
			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("PUBLISH_SUCCESS"));
		}else{
			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("PUBLISH_FAILURE"));
		}
    });
});

$j(window).load(function(){
	imageCenter();
});