$j.extend(loxia.regional['zh-CN'],{
	"PROMPT_INFO":"提示信息",
	"EDIT_AREA_CODE_NOT_EXISTS":"该区域的code不存在",
	"CONFIRM_PUBLISH_PAGE":"确定要发布该页面吗",
	"SAVE_SUCCESS":"保存成功",
	"SAVE_FAILURE":"保存失败",
	"PUBLISH_SUCCESS":"发布成功",
	"PUBLISH_FAILURE":"发布失败",
	"PAGE_CODE_EXISTS":"页面编码({0})已存在",
	"PAGE_URL_EXISTS":"页面url({0})已存在",
	"NO_SAVE_NOT_PREVIEW":"数据有改动,请先保存,再浏览",
	"NO_SAVE_NOT_PUBLISH":"数据有改动,请先保存,再发布",
	"CONFIRM_RETURN":"是否离开该页面",
	"INFO_UPLOAD_IMAGE":"上传图片格式不正确,请重新上传图片",
	"OVER_PUBLISH_INSTANCE_SCOPE":"超出了发布实例时间范围",
	"PAGE_INSTANCE_NOT_PUBLISHED":"页面实例没有发布",
	"PAGE_VERSION_NOT_SAVE":"页面实例版本没有保存"
});

var findPageInstanceListByTemplateIdUrl = base+'/page/findPageInstanceListByTemplateId.json';

var findPageInstanceVersionListByInstanceIdUrl = base + '/page/findPageInstanceVersionListByInstanceId.htm';

var findTemplatePageAreaByTemplateIdUrl = base + '/page/findTemplatePageVersionAreaByTemplateId.htm';

var publishPageInstanceVersionUrl = base + '/page/publishPageInstanceVersion.json';

var checkPageInstanceCodeUrl = base + '/page/checkPageInstanceCode.json';

var checkPageInstanceUrlUrl = base + '/page/checkPageInstanceUrl.json';

/** 去新增页面 */
var newPageUrl = base + '/page/toNewAddPage.htm';

/**检测发布实例时间**/
var checkPublishTimeUrl = base + '/page/checkPublishTime.json';

/**
 * 检察页面实例编码是否可用
 */
function checkPageInstanceCode(){
	$j('#isSaved').val(false);
	var pageId = $j('#versionId').val();
	var code = $j('#code').val();
	var json = '';
	if(pageId == ''){
		json = {'code':code};
	}else{
		json = {'code':code, 'pageId':pageId};
	}
	var data = loxia.syncXhr(checkPageInstanceCodeUrl, json, {type:'post'});
	if(data != '' && data != null){
		nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('PAGE_CODE_EXISTS', code));
		return loxia.ERROR;
	}
	return loxia.SUCCESS;
}

/**
 * 检察页面实例url是否可用
 */
function checkPageInstanceUrl(){
	$j('#isSaved').val(false);
	var pageId = $j('#pageId').val();
	var url = $j('#url').val();
	var json;
	if(pageId == ''){
		json = {'url':url};
	}else{
		json = {'url':url, 'pageId':pageId};
	}
	json.tmpId = $j('#templateId').val();
	var data = loxia.syncXhr(base+'/page/checkPublishPageInstanceUrl.json', json, {type:'post'});
	if(data != '' && data != null){
		nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('PAGE_URL_EXISTS', url));
		return loxia.ERROR;
	}
	return loxia.SUCCESS;
}

function checkPageName(){
	$j('#isSaved').val(false);
	return loxia.SUCCESS;
}

function checkSeoTitle(){
	$j('#isSaved').val(false);
	return loxia.SUCCESS;
}

function checkSeoKeyWords(){
	$j('#isSaved').val(false);
	return loxia.SUCCESS;
}

function checkSeoDesc(){
	$j('#isSaved').val(false);
	return loxia.SUCCESS;
}
/** 上传图片 之后的回调 */
function fnCallback(data, hName){
	if(data==null || data==""){
		nps.info(nps.i18n("PROMPT_INFO"), nps.i18n("INFO_UPLOAD_IMAGE"));
		return;
	}
	var imgUrl = data.url;
	// 检查上传图片的后缀
	var isImg = checkUploadImgExt(imgUrl);
	if(!isImg){
		nps.info(nps.i18n("PROMPT_INFO"), nps.i18n("INFO_UPLOAD_IMAGE"));
		return;
	}
	$j(".cms-imgarticle-edit-dialog  input[hName='"+hName+"']").parent().parent().find(".img-url").val(imgUrl);
}
function checkUploadImgExt(imgUrl){
	if(imgUrl == '' || imgUrl == undefined){
		return false;
	}
	
	var ext = imgUrl.substring(imgUrl.lastIndexOf('.'), imgUrl.length);
	if(ext == ''){
		return false;
	}
	ext = ext.toLowerCase();
	if(ext == '.jpg' || ext == '.png' || ext == '.gif' || ext == '.bmp' || ext == '.jpeg'){
		return true;
	}
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
function parseDate(str){  
	   if(typeof str == 'string'){     
	     var results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) *$/);  
	     if(results && results.length>3){  
	        return  new Date(parseInt(results[1],10),(parseInt(results[2],10) -1),parseInt(results[3],10));      
	     }  
	     results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) +(\d{1,2}):(\d{1,2}):(\d{1,2}) *$/);     
	     if(results && results.length>6)     
	       return new Date(parseInt(results[1],10),parseInt(results[2],10) -1,parseInt(results[3],10),parseInt(results[4],10),parseInt(results[5],10),parseInt(results[6],10));      
	     results = str.match(/^ *(\d{4})-(\d{1,2})-(\d{1,2}) +(\d{1,2}):(\d{1,2}):(\d{1,2})\.(\d{1,9}) *$/);     
	     if(results && results.length>7)     
	       return new Date(parseInt(results[1],10),parseInt(results[2],10) -1,parseInt(results[3],10),parseInt(results[4],10),parseInt(results[5],10),parseInt(results[6],10),parseInt(results[7],10));      
	   }     
	   return null;     
};
function supportType(type){
	var html="";
	if(type==0){
		html="综合";
	}
	if(type==1){
		html="pc";
	}
	if(type==2){
		html="mobile";
	}
	return html;
}

function savePage(preview){
	var html = $j(".web-update").contents().find('html').html();
	$j('#html').val(html);
	var instanceId = $j("#pageId").val();
	if(instanceId == "" || undefined == instanceId){
		nps.info(nps.i18n('PROMPT_INFO'), "页面没有实例关联");
		return;
	}
	nps.submitForm('pageInstanceForm', {mode:'async',
		successHandler: function(data){
			if(data != null){
				$j('#versionId').val(data.id);
				$j('#isSaved').val(true);
				$j(".cms-publish-dialog").attr("wid",data.id);
				if(preview){
					setTimeout(openPage,1);
				}
			}else{
				nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('SAVE_FAILURE'));
			}
			
		}
	});
}
function openPage(){
	var versionId = $j('#versionId').val();
	var templateId = $j('#templateId').val();
	var url =findTemplatePageVersionAreaByTemplateIdUrl+'?templateId='+templateId+'&versionId='+versionId+'&isEdit=false';
	loxia.openPage(url,"newwindow", null, [1000,600]);
}

/**
 * 检测发布实例版本的时间是否在实例发布时间之内
 */
function checkPublishTime(versionId, stime, etime){
	var json = {'versionId':versionId, 'startTime':stime,'endTime':etime};
	var data = loxia.syncXhr(checkPublishTimeUrl, json, {type:'post'});
	if(!data.isSuccess){
		if(data.errorCode == 0){
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('PAGE_INSTANCE_NOT_PUBLISHED'));
		}else if(data.errorCode == 1){
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('PAGE_INSTANCE_NOT_PUBLISHED'));
		}else{
			nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('OVER_PUBLISH_INSTANCE_SCOPE'));
		}
		return loxia.ERROR;
	}
	return loxia.SUCCESS;
}

var sttime;
var edtime;
$j(document).ready(function() {
	var editHtml = '<div class="wui-tips" style="clear:both; display:none; position:absolute; width:100%; left:0; top:0; z-index:100; background:#ff6600; color:#fff; font-size:20px; text-align:center; cursor:pointer; font-weight:bold;">编辑</div>';
	/** 保存 */
	$j('.button.save').click(function(){
		var html = $j(".web-update").contents().find('html').html();
		$j('#html').val(html);
		var instanceId = $j("#pageId").val();
		if(instanceId == "" || undefined == instanceId){
			nps.info(nps.i18n('PROMPT_INFO'), "页面没有实例关联");
			return;
		}
		nps.submitForm('pageInstanceForm', {mode:'async',
			successHandler: function(data){
				if(data != null){
					if($j('#versionId').val() != ''){
						nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('SAVE_SUCCESS'));
						window.location.reload();
					}else{
						window.location.href = findPageInstanceVersionListByInstanceIdUrl+'?instanceId='+$j('#pageId').val();
					}
					$j('#versionId').val(data.id);
					$j('#isSaved').val(true);
				}else{
					nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('SAVE_FAILURE'));
				}
				
			}
		});
	});
	
	/** 返回*/
	$j('.return').click(function(){
		if($j('#isSaved').val() == 'false'){
			nps.confirm(nps.i18n('PROMPT_INFO'), nps.i18n('CONFIRM_RETURN'), function(){
				window.location.href = findPageInstanceVersionListByInstanceIdUrl+'?instanceId='+$j('#pageId').val();
			});
		}else{
			window.location.href = findPageInstanceVersionListByInstanceIdUrl+'?instanceId='+$j('#pageId').val();
		}
	});
	
	/** 预览 */
	$j('.preview').click(function(){
		if($j('#isSaved').val() == 'false'){
			nps.confirm("提示信息","需要先保存才能预览，是否就当前保存并预览?(预览窗口有可能被某些浏览器拦截)",function(){
				savePage(true);
			});
		}else{
			var versionId = $j('#versionId').val();
			var templateId = $j('#templateId').val();
			var pageId = $j("#pageId").val();
			var url = findTemplatePageAreaByTemplateIdUrl+'?templateId='+templateId+'&pageId='+pageId+'&versionId='+versionId+'&isEdit=false';
			loxia.openPage(url,null, null, [1000,600]);
		}
	
	});
	
	/** 发布按钮 */
	$j('.publish').click(function(){
		if($j('#isSaved').val() == 'false'){
			nps.confirm("提示信息","需要先保存才能发布，是否立即保存?",function(){
				savePage(false);
				var versionId = $j('#versionId').val();
				var dialog = $j(".cms-publish-dialog").attr("wid",versionId);
				if(sttime!=null){
					dialog.find(".starttime").val(sttime);
				}
				if(edtime!=null){
					dialog.find(".endtime").val(edtime);
				}
				dialog.dialogff({type:'open',close:'in',width:'450px', height:'350px'});
			});
		}else{
			var versionId = $j('#versionId').val();
			var dialog = $j(".cms-publish-dialog").attr("wid",versionId);
			if(sttime!=null){
				dialog.find(".starttime").val(sttime);
			}
			if(edtime!=null){
				dialog.find(".endtime").val(edtime);
			}
			dialog.dialogff({type:'open',close:'in',width:'450px', height:'350px'});
		}
		
		
	});
	
	/** 发布 */
	$j('.cms-publish-dialog').on('click', '.confrim', function(){
//		if(checkPageInstanceUrl() == loxia.ERROR){
//			return;
//		}
		var dialog = $j(this).parent().parent();
		var versionId = dialog.attr('wid');
		var stime = dialog.find(".starttime").val();
		var etime=dialog.find(".endtime").val();
		
		if(stime == "" || stime == undefined || etime == "" || etime == undefined){
			nps.info(nps.i18n('PROMPT_INFO'), "发布时间和发布结束时间为必填项");
			return;
		}
		
		var sdate;
		
		if(etime!=""){
			var date=parseDate(etime);
			if((date.getTime()-new Date().getTime()) <= 0){
				nps.info(nps.i18n('PROMPT_INFO'), "结束时间应大于当前时间");
				return;
			}
		}
		if(etime!="" && stime!=""){
			var edate=parseDate(etime);
			sdate=parseDate(stime);
			if((edate.getTime()-sdate.getTime()) <= 0){
				nps.info(nps.i18n('PROMPT_INFO'), "结束时间应大于开始时间");
				return;
			}
		}
		if(checkPublishTime(versionId, stime,etime) == loxia.ERROR){
			return; 
		}
		sttime = stime;
		edime = etime;
		var json = {'versionId':versionId,"startTime":stime,"endTime":etime};
		nps.confirm(nps.i18n('PROMPT_INFO'), nps.i18n('CONFIRM_PUBLISH_PAGE'), function(){
			var data = loxia.syncXhr(publishPageInstanceVersionUrl, json, {type:'post'});
			if(data.isSuccess){
				$j('.cms-publish-dialog').dialogff({type : 'close'});
				nps.info(nps.i18n('PROMPT_INFO'), nps.i18n('PUBLISH_SUCCESS'));
			}else{
				nps.info(nps.i18n('PROMPT_INFO'),data.description);
			}
		});
	});
	
	$j('body').on("click",".recover",function(){
		var me = $j(this);
		nps.confirm(nps.i18n('PROMPT_INFO'), "重置之后编辑的数据将被删除,替换成模板最新数据,确定还要重置?", function(){
			var templateId = $j('#templateId').val();
			var code = me.parent().parent().find("#areaCode").val();
			var versionId = $j("#versionId").val();
			if(typeof(code)=="undefined" ||code==null || code ==""){
				code = me.parent().parent().find("#code").val();
			}
			var json={"templateId":templateId,"code":code, "versionId":versionId};
			var data = loxia.syncXhr(base+"/cms/recoverTemplateCodeArea.json", json, {type:'post'});
			if(data.isSuccess){
				var editArea = $j(".web-update").contents().find('[code="'+code+'"]');
				editArea.empty();
				editArea.removeClass("cms-imgarticle-edit");
				editArea.removeClass("cms-html-edit");
				editArea.removeClass("cms-product-edit");
				var arr= data.description.split("EDIT_CLASS_SEP");
				editArea.addClass(arr[0]);
				editArea.append(arr[1]+editHtml);
				addEditEvent(code,".cms-html-edit");
				addEditEvent(code,".cms-imgarticle-edit");
				addEditEvent(code,".cms-product-edit");
				nps.info(nps.i18n('PROMPT_INFO'), "重置成功");
				$j('.dialog-close').click();
			}else{
				nps.info(nps.i18n('PROMPT_INFO'), data.description);
			}
	
		});
	});
	$j('.cms-publish-dialog').on("click",".close",function(){
		$j(this).parent().parent().dialogff({type : 'close'});
	});
	
});