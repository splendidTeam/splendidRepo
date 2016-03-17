/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'],{  
	"NO_DATA":"无数据",
	"INFO_TITLE_DATA":"提示信息",
	"ERROR_NO_FILE":"请先选择要上传的文件",
	"ERROR_INCOMPLETE_INFO":"请先填写优惠券信息",
	"INFO_UNIQUE_FILE":"每次只能上传一份文件",
	"INFO_IMPORT_SUCCESS":"导入成功",
});

/* ------------------------------------------------- URL ------------------------------------------------- */
var SWF_URL = base + '/scripts/uploadify3/uploadify.swf';

var UPLOAD_URL = base + '/promotion/couponImport.json;jsessionid=';
var REFER_URL = base + "/promotion/couponimport.htm";
var TEMPLATE_URL = base + "/promotion/coupon-code-import.xlsx";
var controllerRepeat = true;
/* ------------------------------------------------- 全局常量 ------------------------------------------------- */

/* ------------------------------------------------- 全局变量 ------------------------------------------------- */
  
/* ------------------------------------------------- ready ------------------------------------------------- */
$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();  
	
	//解决uploadify对于火狐浏览器的丢失session
	UPLOAD_URL += $j("#session-id").val();
	
	var tokenValue = $j("meta[name='_csrf']").attr("content");
	if(typeof(tokenValue) != "undefined") {
		UPLOAD_URL += "?_csrf="+tokenValue;
	}
	
	$j("#coupon-upload").uploadify({
		'swf'        		: SWF_URL, 
		'uploader'          : UPLOAD_URL,
		//解决uploadify会在当前路径下寻找而导致的404错误
		'button_image_url'	: base + '/scripts/uploadify3/none.png',	
		'auto'				: false,
		'buttonText'		: '浏  览',
		'fileTypeDesc'		: '支持文件：',
		'fileTypeExts'		: '*.xlsx',
		'multi'				: false,
		'queueSizeLimit'	: 1,
		'successTimeout'  	: 60,	//响应时间（秒），为大数据量预留足够时间    
		'removeTimeout'		: 1,
		'onSelectError'		: function(){
			alert("每次只能上传一个文件！");
		},
		'onUploadSuccess' 	: function(file, data, response) {
			controllerRepeat =true;
			var jdata = $.parseJSON(data);
			if (jdata.isSuccess) {
				if (jdata.errorList.length == 0) {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_IMPORT_SUCCESS"));
				} else {
					var str = "";
					for(var i = 0; i < jdata.errorList.length; i++) {
						str += "," + jdata.errorList[i];
					}
					str = str.substring(1);
					nps.info(nps.i18n("INFO_TITLE_DATA"),"未导入成功的优惠券：\n" + str);
				} 
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),"导入失败：" + jdata.message);
			}
			
        }
        
	});	
	
	// ‘确认’按钮
	$j("#btn-ok").click(function() {
		if(controllerRepeat){
			var type = $j("#coupon-type").val();
//			var startTime = $j.trim($j("#start-time").val());
//			var endTime = $j.trim($j("#end-time").val());
//			if (isBlank(type) || isBlank(startTime) || isBlank(endTime)) {
//				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("ERROR_INCOMPLETE_INFO"));
//				return;
//			}
			if (! checkImport('#coupon-upload-queue')) return;
			//, startTime: startTime, endTime: endTime
			$j('#coupon-upload').uploadify('settings', 'formData', {'couponId': type});
			$j('#coupon-upload').uploadify('upload');
			controllerRepeat =false;
		}
		
	});

	//‘取消’按钮
	$j("#btn-cancel").click(function() {
		$j('#coupon-upload').uploadify("cancel");
	});
	
	//‘模版下载’
	$j(".btn-download").click(function() {
		window.location.href = TEMPLATE_URL;
	});
	
	//‘返回’
	$j(".btn-return").click(function() {
		window.location.href = REFER_URL;
	});
	
	//date-picker位置错误bug
	//	$j('.date-picker').focus(function(){
	//		$j("#ui-datepicker-div").css({top:parseInt($j(this).offset().top)+parseInt($j(this).outerHeight())});
	//	});
});

/* ------------------------------------------------- util ------------------------------------------------- */
//如果点击‘导入文件’时选择文件为空，则提示
function checkImport(id) {
    if ($j.trim($j(id).html()) == "") {
    	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("ERROR_NO_FILE"));
        return false;
    }
    return true;
}

//空字符串
function isBlank(str) {
	return str == null || $j.trim(str).length == 0;
}

//获取日期格式
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
} 

/**
 * 生成优惠券状态字符串
 * @param isUsed
 * @param endTime
 * @returns
 */
function generateCouponState(isUsed, endTime) {
	if (isUsed == 1) {
		return "已使用";
	} else {
		return endTime < new Date() ? "已过期" : "未使用";
	}
}

 