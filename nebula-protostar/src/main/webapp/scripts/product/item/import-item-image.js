$j.extend(loxia.regional['zh-CN'],{
	"SYSTEM_ITEM_MESSAGE":"提示信息",
	"CONFIRM_TOTAL_IMPORT":"全量导入会将原有的商品图片删除,再添加上传的商品图片.确定全量导入吗?",
	"UPLOAD_FILE_IS_EMPTY":"请上传图片文件(zip文件包)"
});

var uploadImgZipUrl = base + '/itemImage/uploadItemImgZip.json';

var itemListUrl = base + "/item/itemList.htm";

//商品图片上传完成
function fnComplete(data) {
	var prompt = '商品图片批量上传成功';
	if(data.exception != '' && data.exception != null && data.exception != undefined){
		prompt = data.exception.message;
	}
	$j('#errorTip').find('.showError').html('<span>'+prompt+'</span>');
	$j('#uploadImgZip').clearForm();
	$j('#uploadImgZip').find('select').val(1);
	$j('.save').attr("class","button orange save");
	$j('.save').val("保存");
	$j('.save').removeAttr("disabled");
}
//商品图片上传之前
function fnBeforeSend(formData) {
	for(var i in formData){
		var name = formData[i].name;
		var data = formData[i].value;
		
		if(name != 'itemImgFile'){
			continue;
		}
		if(data == '' || data == null || data == undefined){
			$j('#errorTip').show();
			$j('#errorTip').find('.showError').html('<span>'+nps.i18n('UPLOAD_FILE_IS_EMPTY')+'</span>');
			return false;
		}
	}
	$j('#errorTip').show();
	$j('#errorTip').find('.showError').html('<span>正在处理上传的商品图片, 请稍等...</span>');
	return true;
}

$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
    $j('#errorTip').hide();
    
    // 保存
    $j('.save').click(function(){
    	var uploadType = $j('#uploadImgZip').find('select').val();
    	var uploadFile = $j('#itemImgFile').val();
    	if(!uploadFile){
    		nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n('UPLOAD_FILE_IS_EMPTY'));
    		return;
    	}
    	
    	if(uploadType == '0'){
    		nps.confirm(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("CONFIRM_TOTAL_IMPORT"), function(){
    			$j('.save').attr("class","button save");
    			$j('.save').val("正在导入");
    			$j('.save').attr("disabled","disabled");
    			$j('#uploadImgZip').submit();
    		});
    	}else{
    		$j('.save').attr("class","button save");
    		$j('.save').val("正在导入");
    		$j('.save').attr("disabled","disabled");
    		$j('#uploadImgZip').submit();
    	}
    });
    
    $j('#uploadImgZip').ajaxForm({
    	url: uploadImgZipUrl,
    	dataType: 'json',
    	beforeSubmit: fnBeforeSend,
        success: fnComplete  
   });
    
    /** 返回*/
    $j('.return').click(function(){
    	window.location.href = itemListUrl;
    });
});

