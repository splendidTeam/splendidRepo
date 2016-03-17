$j.extend(loxia.regional['zh-CN'], {
	"DEFINITION_INSTALL_TIPS" : "友情提示：",
	"PLEASE_CHOOSE_FILE" : "请选择要安装的模板",
	"FILE_SUFFIX_ERROR" : "请选择正确的模板",
	"FILE_UPLOAD_FAILURE" : "请检查安装文件路径标识等是否正确",
	"FILE_UPLOAD_SUCCESS" : "安装模板成功",
	"IF_COVER" : "此模板已安装，是否覆盖",
	"DIALOG_NOT_BLANK":"DialogPath不存在或者为空"
});
var installDefinitionUrl = base + '/cms/installDefinition.json';
var toInstallDefinitionUrl = base + '/cms/toInstallDefinition.htm';
var sureInstallDefinitionUrl = base + '/cms/sureInstallDefinition.json';

function installFormValidate(form) {
	var componentFilePath = $j("#componentFilePath").val();
	if (null == componentFilePath || componentFilePath.length == 0
			|| undefined == componentFilePath || componentFilePath == '.zip') {
		return nps.i18n("PLEASE_CHOOSE_FILE");
	}
	var ext = componentFilePath
			.substring(componentFilePath.lastIndexOf(".") + 1);
	if (null == ext || ext.length == 0 || undefined == ext || ext != 'zip') {
		return nps.i18n("FILE_SUFFIX_ERROR");
	}
	return loxia.SUCCESS;
}

// 查询url中带有的参数的值
function getQueryString(ifSuccess) {

	var reg = new RegExp("(^|&)" + ifSuccess + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
		return decodeURI(r[2]);
	}
	return null;

}
// 判断字符串是不是空
function isNotBlank(str) {
	if ("" != str && null != str) {
		return true;
	} else {
		return false;
	}
}
var loadCount = 0;
$j(document).ready(
		function() {
			loxia.init({
				debug : true,
				region : 'zh-CN'
			});
			nps.init();

			var ifSuccess = getQueryString("ifSuccess");
			var dialogPathError = getQueryString("exceptionMsg");
			if (isNotBlank(ifSuccess)) {
				if (ifSuccess == 'success') {
					nps.info(nps.i18n("DEFINITION_INSTALL_TIPS"), nps
							.i18n("FILE_UPLOAD_SUCCESS"));
					location = toInstallDefinitionUrl;
				} else {
					if(isNotBlank(dialogPathError)){
						nps.info(nps.i18n("DEFINITION_INSTALL_TIPS"),nps
								.i18n("DIALOG_NOT_BLANK"));
					}else{
						nps.info(nps.i18n("DEFINITION_INSTALL_TIPS"), nps
								.i18n("FILE_UPLOAD_FAILURE"));
					}
				}
				location = toInstallDefinitionUrl;
			}

			var exist = getQueryString("exist");
			var filePath = getQueryString("filePath");
			
			if (isNotBlank(exist) && isNotBlank(filePath)) {
				nps.confirm(nps.i18n("DEFINITION_INSTALL_TIPS"),nps.i18n("IF_COVER"),function(){
					var data = loxia.syncXhr(sureInstallDefinitionUrl,{"path":filePath,"type":1},{type: "GET"});
			    	if(data.isSuccess){
			    		nps.info(nps.i18n("DEFINITION_INSTALL_TIPS"), nps
								.i18n("FILE_UPLOAD_SUCCESS"));
			    	}else{
			    		nps.info(nps.i18n("DEFINITION_INSTALL_TIPS"), nps
								.i18n("FILE_UPLOAD_FAILURE"));
			    	}
			    	location = toInstallDefinitionUrl;
				});
			}
			
			/**
			 * 提交表单
			 */
			$j(".button.orange.save").click(function() {
				nps.submitForm('installForm', {
					mode : 'sync',
					type : "POST"
				});
			});

			$j("#previewFile").click(function() {
				// 得到file对象
				var file = $j("#componentFile");
				// 弹出选择文件的对话框
				file.click();
				// 弹出file的值
				$j("#componentFilePath").attr("value", file.value);
			});

		});