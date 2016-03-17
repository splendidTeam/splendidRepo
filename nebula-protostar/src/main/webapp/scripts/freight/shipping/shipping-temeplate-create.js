$j.extend(loxia.regional['zh-CN'], {
	"LABEL_DISTRIBUTION_NAME" : "物流名称",
	"LBAEL_DISTRIBUTION_OPERATE" : "操作",
	"TO_VIEW" : "查看",
	"TO_UPDATE" : "编辑",
	"TO_DELETE" : "删除",
	"NO_DATA":"无数据",
	"INFO_TITLE_DATA":"提示信息",
	"INFO_SUCCESS":"创建成功",
	"INFO_MODIFY_SUCCESS":"修改成功",
	"INFO_FAILUE":"创建失败",
	"INFO_FILL_NAME":"请输入物流名称",
	"INFO_NO_SELECT_DATA":"请填写物流名称"
});

//保存运费模板
var saveShippingTemplateUrl = base + "/freight/saveShippingTemplate.json";

//进入运费模板
var entryShippingTemplateUrl = base + "/freight/shippingTemeplateList.htm";




$j(document).ready(function() {
	
	
	//编辑保存按钮
	$j(".template-save").click(function() {
		
		var currentId = $j("#shipping_id").val();
		var currentName = $j("#template-name").val();
		if (currentName.length == 0 || currentName == null) {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_SELECT_DATA"));
			return;
		}
		var calculationType = $j("#calculationType").val();
		var defaultFee = $j("#defaultFee").val();
		var isDefault = $j("#isDefault").val();
		var distributionModeIds = "";
		$j(".dbm").find(".dbm-ids").each(function(i,dom){
			var me = $j(this);
			if(me.attr("checked")){
				distributionModeIds = distributionModeIds+me.val()+",";
			}
		});
		distributionModeIds = distributionModeIds.substring(0,distributionModeIds.length-1);
		var json = {
				'id':currentId,
				'name' : currentName,
				'calculationType':calculationType,
				'defaultFee':defaultFee,
				'default':isDefault,
				'distributionModeIds':distributionModeIds
		};
		// 提交表单
		nps.asyncXhrPost(saveShippingTemplateUrl, json, {
			   successHandler : function(data, textStatus) {
				   if (data.isSuccess) {
					   if (currentId.length == 0 || currentId == null) {
						   nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SUCCESS"));
					   }else{
						   nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_MODIFY_SUCCESS"));
					   }
						 window.location.href=entryShippingTemplateUrl;
					} else {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_FAILUE"));
					}
			   }
		});
	});
	
	
	//返回按钮
	$j(".button.return").click(function() {
		window.location.href=entryShippingTemplateUrl;
	});
});