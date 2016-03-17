$j.extend(loxia.regional['zh-CN'],{
	"SYSTEM_ITEM_MESSAGE":"提示信息",
	"ERROR_NO_FILE":"请先选择要上传的文件",
	"IMP_SUCCESS":"导入成功"
});

var SWF_URL = base + '/scripts/uploadify3/uploadify.swf';
var UPLOAD_URL = base + '/sku/skuUpload.json;jsessionid=';
var userFilekey;
$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
   //解决uploadify对于火狐浏览器的丢失session
	UPLOAD_URL += $j("#session-id").val();
	var tokenValue = $j("meta[name='_csrf']").attr("content");
	if(typeof(tokenValue) != "undefined") {
		UPLOAD_URL += "?_csrf="+tokenValue;
	}
	$j("#sku-upload").uploadify({
		'swf'        		: SWF_URL, 
		'uploader'          : UPLOAD_URL,
		'button_image_url'	: base + '/scripts/uploadify3/none.png',	
		'auto'				: false,
		'buttonText'		: '浏  览',
		'fileTypeDesc'		: '支持文件：',
		'fileTypeExts'		: '*.xls',
		'multi'				: false,
		'queueSizeLimit'	: 1,
		'successTimeout'  	: 1200,	//响应时间（秒），为大数据量预留足够时间    
		'removeTimeout'		: 1,
		'onSelectError'		: function(){
			alert("每次只能上传一个文件！");
		},
		'onUploadSuccess' 	: function(file, data, response) {
			var result = eval('('+data+')');
			var html="";
			if(result.isSuccess){
				$j("#errorTip").hide();
				nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("IMP_SUCCESS"));
			}else{
				userFilekey = result.userFilekey;
				$j.each(result.description,function(idx,obj){
					html+=obj+"</br>";
				});
				html = "<input type='button' id='downLoadTmpErrorInfo' value='下载带错误信息的文件'><br>" +html;
				$j(".showError").html(html);
				$j("#errorTip").show();	
			}
			
        }
        
	});	
	 $j("body").on("click","#downLoadTmpErrorInfo",function(){
		 if(userFilekey!=null){
			 location.href="/item/downloadUserFile.xls?userFilekey="+userFilekey;
			 userFilekey = null;
		 }
	
	});
    $j("#downLoadTmplOfSkuInfo").on("click",function(){
        var industryId = $j("#industry").val();
		var json={"industryId":industryId};
		location.href="/sku/tplt_sku_import.xls?industryId="+industryId;
	});
    
	// ‘确认’按钮
	$j("#btn-ok").click(function() {
		if (! checkImport('#sku-upload-queue')) return;
		$j(".showError").html("正在处理数据，请稍后...");
		$j("#errorTip").show();	
		$j('#sku-upload').uploadify('upload');
	});

	//‘取消’按钮
	$j("#btn-cancel").click(function() {
		$j("#errorTip").hide();
		$j('#sku-upload').uploadify("cancel");
	});
    
    
});

//----------------------------------------------------------------------------------------------------
//如果点击‘导入文件’时选择文件为空，则提示
function checkImport(id) {
    if ($j.trim($j(id).html()) == "") {
    	nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("ERROR_NO_FILE"));
        return false;
    }
    return true;
}