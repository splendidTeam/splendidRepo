/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'],{
	"NO_DATA":"无数据",
	"INFO_TITLE_DATA":"提示信息",
	"INFO_NO_NAME":"请输入模板名称",
	"INFO_NO_DATA":"包含列表不能为空",
	"INFO_SUCCESS":"创建成功",
	"INFO_FAILURE":"创建失败",
	"INFO_REPEATED_NAME":"组合名称已被使用，请更换其他名称",
	"INFO_SYSTEM_ERROR":"系统错误，请联系管理员",
	"INFO_UPLOAD_IMAGE":"上传图片格式不正确,请重新上传图片"
	
});

//保存模板
var saveTemplateUrl = base + "/cmsModuleTemplate/save.json";
//页面模板
var entryTemplateListUrl = base+'/newcms/moduleTemplateList.htm'; 

var uploadImgZipUrl = base + '/cms/uploadtext.json';

/** 上传图片 之后的回调 */
function fnCallback(data, hName){
	var imgUrl = data.url;
	// 检查上传图片的后缀
	var isImg = checkUploadImgExt(imgUrl);
	if(!isImg){
		nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_UPLOAD_IMAGE"));
		return;
	}
	$j('#template-img').val(imgUrl);
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

$j(document).ready(function(){
	
	//保存按钮
	$j(".template-save").click(function() {
		// 提交表单
		nps.submitForm('saveTemplate',{mode: 'sync'});
	});
	
	//后退按钮
	$j(".button.return").click(function() {
		window.location.href=entryTemplateListUrl;
	});


});

