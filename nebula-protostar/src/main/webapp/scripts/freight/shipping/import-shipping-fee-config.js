$j.extend(loxia.regional['zh-CN'],{  
	"NO_DATA":"无数据",
	"INFO_TITLE_DATA":"提示信息",
	"SYSTEM_ITEM_MESSAGE":"提示信息",
	"ERROR_NO_FILE":"请先选择要上传的文件",
	"INFO_UNIQUE_FILE":"每次只能上传一份文件",
	"INFO_IMPORT_SUCCESS":"导入成功"
});

//查看运费表
var viewShippingFeeConfigUrl = base + "/freight/shippingFeeConfigList.htm";
//下载模板地址
var loadTemplateUrl = base + "/freight/shippingFeeImport.xlsx";
//导出数据地址
var exportTemplateUrl = base + "/freight/shippingFeeExport.xlsx";

var SWF_URL = base + '/scripts/uploadify3/uploadify.swf';

var UPLOAD_URL = base + '/freight/shippingFeeImport.json;jsessionid=';

$j(document).ready(function() {
	
	//导出运费表
	$j("#ExportTmplOfFeeInfo").click(function() {
		window.location.href = exportTemplateUrl + "?templateId="+templateId;
	});
	
	
	//返回按钮
	$j(".button.return").click(function() {
		var url = viewShippingFeeConfigUrl+"?templateId="+templateId;
		window.location.href=url;
	});
	
	 //下载模板
    $j("#downLoadTmplOfFeeInfo").click(function() {
    	window.location.href = loadTemplateUrl+ "?templateId="+templateId;
    });
    
    
  //解决uploadify对于火狐浏览器的丢失session
	UPLOAD_URL += $j("#session-id").val();
	
	var tokenValue = $j("meta[name='_csrf']").attr("content");
	if(typeof(tokenValue) != "undefined") {
		UPLOAD_URL += "?_csrf="+tokenValue;
	}
	
	$j("#fee-upload").uploadify({
		'swf'        		: SWF_URL, 
		'uploader'          : UPLOAD_URL,
		//解决uploadify会在当前路径下寻找而导致的404错误
		'button_image_url'	: base + '/scripts/uploadify3/none.png',	
		'auto'				: false,
		'buttonText'		: '浏  览',
		'fileTypeDesc'		: 'Excel2007,2003',
		'fileTypeExts'		: '*.xlsx;*.xls',
		'multi'				: false,
		'queueSizeLimit'	: 1,
		'successTimeout'  	: 1000,	//响应时间（秒），为大数据量预留足够时间    
		'removeTimeout'		: 1,
		'onSelectError'		: function(){
			alert("每次只能上传一个文件！");
		},
		'onUploadSuccess' 	: function(file, data, response) {
			var result = eval('('+data+')');
			var html="";
			if(result.isSuccess){
				$j("#errorTip").hide();
				nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("INFO_IMPORT_SUCCESS"));
			}else{
				$j.each(result.description,function(idx,obj){
					html+=obj+"</br>";
				});
				$j(".showError").html(html);
				$j("#errorTip").show();	
			}
			
        }
        
	});	
	
	// ‘确定’按钮
	$j("#btn-ok").click(function() {
		var templateId = $j("#shippingTemplateId").val();
		if (! checkImport('#fee-upload-queue')) return;
		$j('#fee-upload').uploadify('settings', 'formData', {'templateId': templateId});
		$j('#fee-upload').uploadify('upload');
	});
	
	//‘取消’按钮
	$j("#btn-cancel").click(function() {
		$j("#errorTip").hide();
		$j('#fee-upload').uploadify("cancel");
	});
});

//如果点击‘导入文件’时选择文件为空，则提示
function checkImport(id) {
    if ($j.trim($j(id).html()) == "") {
    	nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("ERROR_NO_FILE"));
        return false;
    }
    return true;
}