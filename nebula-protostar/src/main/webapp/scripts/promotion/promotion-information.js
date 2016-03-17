$j.extend(loxia.regional['zh-CN'], {
	"NO_DATA" : "无数据",
	"INFO_TITLE_DATA" : "提示信息",
	"INFO_SUCCESS" : "操作成功！",
	"INFO_FAILURE" : "操作失败：",
	"INFO_IMCOMPLETE_DATA" : "请完整填写信息",
});
// 找到分类
var CATEGORY_URL = base + "/promotion/information/findByCategoryCode.json";
// 更新数据
var UPDATEPROINFO_URL = base + "/promotion/information/updateProInfo.json";

$j(document).ready(
		function() {
			// 浮层返回
			$j(".button.btn-ok").click(function() {
				$j("#view-promotion-div .dialog-close").click();
			});

			$j("#categoryCode").change(function() {
				var code = $j(this).val();
				var json = {
					categoryCode : code
				};
				var data = nps.syncXhrPost(CATEGORY_URL, json);

				$j("#msgId").val(data.id);
				editor.setData(data.content);

			});

			// 保存
			$j(".btn-save").click(
					function() {

						var msgId = $j("#msgId").val();
						var categoryCode = $j("#categoryCode").val();
						var content = editor.getData();

						if (categoryCode.length == 0
								|| categoryCode.length == 0) {
							nps.info(nps.i18n("INFO_TITLE_DATA"), nps
									.i18n("INFO_IMCOMPLETE_DATA"));
							return;
						}

						var json = {
							prmInfoId : msgId,
							categoryCode : categoryCode,
							content : content
						};
						var data = nps.syncXhrPost(UPDATEPROINFO_URL, json);

						if (data.isSuccess) {
							nps.info(nps.i18n("INFO_TITLE_DATA"), nps
									.i18n("INFO_SUCCESS"));

						} else {
							nps.info(nps.i18n("INFO_TITLE_DATA"), nps
									.i18n("INFO_FAILURE")
									+ data.description);
						}
					});

		});
