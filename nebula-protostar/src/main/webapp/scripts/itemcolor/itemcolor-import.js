/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'], {
	"NO_DATA" : "无数据",
	"INFO_TITLE_DATA" : "提示信息",
	"ERROR_NO_FILE" : "请先选择要上传的文件",
	"INFO_UNIQUE_FILE" : "每次只能上传一份文件",
	"INFO_IMPORT_SUCCESS" : "导入成功",
});

/*
 * ------------------------------------------------- URL
 * -------------------------------------------------
 */


var SWF_URL = base + '/scripts/uploadify3/uploadify.swf';
var UPLOAD_URL = base + '/itemColor/itemColorValueRefAnalysis.json;jsessionid=';
var TEMPLATE_URL = base + "/itemColor/itemColorValueRef.xls";
var controllerRepeat = true;
/*
 * ------------------------------------------------- 全局常量
 * -------------------------------------------------
 */

/*
 * ------------------------------------------------- 全局变量
 * -------------------------------------------------
 */

/*
 * ------------------------------------------------- ready
 * -------------------------------------------------
 */
$j(document)
		.ready(
				function() {
					loxia.init({
						debug : true,
						region : 'zh-CN'
					});
					nps.init();
					// 解决uploadify对于火狐浏览器的丢失session
					UPLOAD_URL += $j("#session-id").val();
					var tokenValue = $j("meta[name='_csrf']").attr("content");
					if(typeof(tokenValue) != "undefined") {
						UPLOAD_URL += "?_csrf="+tokenValue;
					}
					$j("#coupon-upload")
							.uploadify(
									{
										'swf' : SWF_URL,
										'uploader' : UPLOAD_URL,
										// 解决uploadify会在当前路径下寻找而导致的404错误
										'button_image_url' : base
												+ '/scripts/uploadify3/none.png',
										'auto' : false,
										'buttonText' : '浏  览',
										'fileTypeDesc' : '支持文件：',
										'fileTypeExts' : '*.xlsx;*.xls',
										'multi' : false,
										'queueSizeLimit' : 1,
										'successTimeout' : 60, // 响应时间（秒），为大数据量预留足够时间
										'removeTimeout' : 1,
										'onSelectError' : function() {
											alert("每次只能上传一个文件！");
										},
										'onUploadSuccess' : function(file,data, response) 
										{
											controllerRepeat = true;
											var jdata = $j.parseJSON(data);
											if (jdata.flag) {
												if (jdata.errorList.length == 0) {
													$j("#upload-sku-result").html(nps.i18n("INFO_IMPORT_SUCCESS"));
												} else {
													var str = "";
													for (var i = 0; i < jdata.errorList.length; i++) {
														str += jdata.errorList[i];
													}
													str = str.substring(1);
													$j("#upload-sku-result").html("上传失败：<br/>"+ str);
												}
												refreshData();
											} else {
												$j("#upload-sku-result").html("上传解析失败：");
											}

										}

									});

					// ‘确认’按钮
					$j("#btn-ok").click(
							function() {
								if (controllerRepeat) {
									
									if (!checkImport('#coupon-upload-queue')){
										return;
									}
									$j('#coupon-upload').uploadify('upload');
									controllerRepeat = false;
								}

							});

					// ‘取消’按钮
					$j("#btn-cancel").click(function() {
						$j('#coupon-upload').uploadify("cancel");
					});

					// ‘模版下载’
					$j(".btn-download").click(function() {
						window.location.href = TEMPLATE_URL;
					});
				});

/*
 * ------------------------------------------------- util
 * -------------------------------------------------
 */
// 如果点击‘导入文件’时选择文件为空，则提示
function checkImport(id) {
	if ($j.trim($j(id).html()) == "") {
		nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("ERROR_NO_FILE"));
		return false;
	}
	return true;
}

// 空字符串
function isBlank(str) {
	return str == null || $j.trim(str).length == 0;
}
