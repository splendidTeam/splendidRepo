$j.extend(loxia.regional['zh-CN'], {
	"LABEL_SHIPPING_NAME" : "模板名称",
	"LABEL_SHIPPING_CALCULATIONTYPE" : "计价类型",
	"LABEL_SHIPPING_DEFAULTFEE" : "默认邮费",
	"LABEL_SHIPPING_ISDEFAULT" : "默认模板",
	"LABEL_SHIPPING_BEANNAME" : "自定义类名",
	"LABEL_SHIPPING_OPERATE" : "操作",
	"INFO_TITLE_DATA" : "提示信息",
	"TO_VIEW" : "查看运费表",
	"TO_UPDATE" : "编辑",
	"TO_DELETE" : "删除",
	"SET_DEFAULT" : "设为默认",
	"IS_DEFAULT" : "默认模板",
	"NO_DATA":"无数据",
	"INFO_DELETE_SUCCESS" : "删除成功",
	"INFO_ENABLE_SUCCESS" : "生效成功",
	"INFO_SET_SUCCESS" : "设置成功",
	"TEMPLATE_TIP_NOSEL_DEL" : "请选择要删除的模板",
	"CONFIRM_DELETE_TEMPLATE" : "确认删除运费模板",
	"CONFIRM_DELETE":"确认删除",
	"INFO_SYSTEM_ERROR" : "系统错误"
});
// 鼠标移动隐藏时间
function categoryHideMenu() {
	$j("#categoryMenuContent").fadeOut("fast");
	$j("body").unbind("mousedown", categoryOnBodyDown);
}
// 运费模板地址
var shippingTemplateListUrl = base + '/freight/shippingTemeplateList.json';
//全部生效
var enableShippingTemplateUrl = base + "/freight/enableShippingTemplate.htm";
// 新建
var createShippingTemplateUrl = base + "/freight/createShippingTemplate.htm";
//删除运费模板
var deleteShippingTemplateUrl = base + "/freight/deleteShippingTemplate.htm";
//批量删除
var butchDeleteShippingTemplateUrl = base + '/freight/butchRemoveShippingTemplate.json';
//进入运费模板
var entryShippingTemplateUrl = base + "/freight/shippingTemeplateList.htm";
//设置默认运费模板
var setDefaultShippingTemplateUrl = base + "/freight/setDefaultShippingTemplate.json";
//查看运费表
var viewShippingFeeConfigUrl = base + "/freight/shippingFeeConfigList.htm";

//获取id
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' name='chid' class='checkId' value='" + loxia.getObject("id", data) + "'/>";
}

function drawEditor(data, args, idx) {
	var result = "";
	var id = loxia.getObject("id", data);
	var result = "";
	var toview = "<a href='javascript:void(0);' val='" + id
			+ "' class='func-button view-shipping'>" + nps.i18n("TO_VIEW") + "</a>";
	var tomodify = "<a href='javascript:void(0);' val='" + id
			+ "' class='func-button update-shipping'>"
			+ nps.i18n("TO_UPDATE") + "</a>";
	var todelete = "<a href='javascript:void(0);' val='" + id
			+ "' class='func-button delete-shipping'>" + nps.i18n("TO_DELETE") + "</a>";
	var state = loxia.getObject("default", data);
	var setdefault = "";
	if(!state){
		setdefault = "<a href='javascript:void(0);' val='" + id
			+ "' class='func-button set-default'>" + nps.i18n("SET_DEFAULT") + "</a>";
	}
	result += toview + tomodify + todelete + setdefault;
	return result;
}

// 刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}

function drawIsdefault(data, args, idx) {
	var result = "";
	var state = loxia.getObject("default", data);
	if (state) {
		return "<input name='limitValue' type='checkbox' class='limitValue' checked='checked' disabled='disabled'/>";
	}
	return "<input name='limitValue' type='checkbox' class='limitValue' disabled='disabled'/>";

}
function drawCalculationType(data, args, idx) {
	var result = "基础";
	var type = loxia.getObject("calculationType", data);
	if (type == "unit") {
		result = "计件";
	} else if (type == "weight") {
		result = "计重";
	} else if (type == "base") {
		result = "基础";
	} else if (type == "custom") {
		result = "自定义";
	}
	return result;
}

// 通过loxiasimpletable动态获取数据
$j(document).ready(function() {

	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();

	searchFilter.init({
		formId : 'searchForm',
		searchButtonClass : '.func-button.search'
	});

	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		form : "searchForm",
		cols : [ {
			label : "<input type='checkbox'/>",
			width : "5%",
			template : "drawCheckbox"
		},
		{
			name : "name",
			label : nps.i18n("LABEL_SHIPPING_NAME"),
			width : "10%"
		}, {
			name : "calculationType",
			label : nps.i18n("LABEL_SHIPPING_CALCULATIONTYPE"),
			width : "9%",
			template : "drawCalculationType"
		}, {
			name : "defaultFee",
			label : nps.i18n("LABEL_SHIPPING_DEFAULTFEE"),
			width : "8%"
		}, {
			name : "default",
			label : nps.i18n("LABEL_SHIPPING_ISDEFAULT"),
			width : "8%",
			template : "drawIsdefault"
		}, {
			name : "beanName",
			label : nps.i18n("LABEL_SHIPPING_BEANNAME"),
			width : "16%"
		}, {
			name : nps.i18n("LABEL_SHIPPING_OPERATE"),
			label : nps.i18n("LABEL_SHIPPING_OPERATE"),
			width : "15%",
			template : "drawEditor"
		} ],
		dataurl : shippingTemplateListUrl
	});
	refreshData();

	// 筛选数据
	$j(".func-button.search").click(function() {
		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();
	});

	// 全部生效按钮
	$j(".button.orange.enabletemplate").click(function() {
//		window.location.href = enableShippingTemplateUrl ;
		var json = {
				
		};
		nps.asyncXhrPost(enableShippingTemplateUrl,json, {
			successHandler : function(data, textStatus) {
				if (data.isSuccess) {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ENABLE_SUCCESS"));
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_FAILUE"));
				}
			}
		});
	});
	
	// 新建按钮
	$j(".button.orange.addtemplate").click(function() {
		window.location.href = createShippingTemplateUrl + "?id=";
	});

	// 批量逻辑删除
	$j(".button.butch.delete").click(function() {
		var data = "";
		$j(".checkId:checked").each(function(i, n) {
			if (i != 0) {
				data += ",";
			}
			data += $j(this).val();

		});
		if (data == "") {
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("TEMPLATE_TIP_NOSEL_DEL"));
			return;
		}
		nps.confirm(nps.i18n("CONFIRM_DELETE"), nps.i18n("CONFIRM_DELETE_TEMPLATE"), function() {
			var json = {
				"ids" : data
			};
			nps.asyncXhrPost(butchDeleteShippingTemplateUrl, json, {
				successHandler : function(data, textStatus) {
					if (data.isSuccess) {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
						window.location.href=entryShippingTemplateUrl;
					} else {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_FAILUE"));
					}
				}
			});
		});
	});
	
	
	//编辑按钮
	$j("#table1").on("click", ".update-shipping", function(){
		var curObject=$j(this);
		var currentId = curObject.attr("val");
		window.location.href=createShippingTemplateUrl+"?id="+currentId;
	});
	
	//查看按钮
	$j("#table1").on("click", ".view-shipping", function(){
		var curObject=$j(this);
		var currentId = curObject.attr("val");
		var url = viewShippingFeeConfigUrl+"?templateId="+currentId;
		window.location.href=url;
		
	});
	
	//编辑删除按钮
	$j("#table1").on("click", ".delete-shipping", function(){
		var curObject=$j(this);
		var currentId = curObject.attr("val");
		nps.confirm(nps.i18n("CONFIRM_DELETE"), nps.i18n("CONFIRM_DELETE_TEMPLATE"), function() {
			var json = {
					'id':currentId
			};
			// 提交表单
			nps.asyncXhrPost(deleteShippingTemplateUrl, json, {
				successHandler : function(data, textStatus) {
					if (data.isSuccess) {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
						window.location.href=entryShippingTemplateUrl;
					} else {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_FAILUE"));
					}
				}
			});
		});
	});
	
	//设置默认按钮
	$j("#table1").on("click", ".set-default", function(){
		var curObject=$j(this);
		var currentId = curObject.attr("val");
		var json = {
				'id':currentId
		};
		// 提交表单
		nps.asyncXhrPost(setDefaultShippingTemplateUrl, json, {
			   successHandler : function(data, textStatus) {
				   if (data.isSuccess) {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SET_SUCCESS"));
						 window.location.href=entryShippingTemplateUrl;
					} else {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_FAILUE"));
					}
			   }
		});
	});

});
