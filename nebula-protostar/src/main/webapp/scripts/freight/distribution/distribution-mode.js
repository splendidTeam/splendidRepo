$j.extend(loxia.regional['zh-CN'], {
	"LABEL_DISTRIBUTION_NAME" : "物流名称",
	"LBAEL_DISTRIBUTION_OPERATE" : "操作",
	"TO_VIEW" : "查看支持区域",
	"TO_UPDATE" : "编辑",
	"TO_DELETE" : "删除",
	"NO_DATA":"无数据",
	"INFO_TITLE_DATA":"提示信息",
	"CONFIRM_DELETE":"确认删除",
	"INFO_SUCCESS":"创建成功",
	"INFO_MODIFY_SUCCESS":"修改成功",
	"DISTRIBUTION_TIP_NOSEL_DEL" : "请选择要删除的物流",
	"CONFIRM_DELETE_DISTRIBUTION" : "确认删除物流",
	"INFO_DELETE_SUCCESS":"删除成功",
	"INFO_FAILUE":"操作失败",
	"INFO_FILL_NAME":"请输入物流名称",
	"INFO_NO_SELECT_DATA":"请填写物流名称"
});
// Json格式动态获取数据库信息
var distributionListUrl = base + '/freight/distributionModeList.json';
//保存物流
var saveDistributionUrl = base + "/freight/saveDistributionMode.json";
//删除物流
var deleteDistributionUrl = base + "/freight/deleteDistributionMode.htm";
//批量删除
var butchDeleteDistributionUrl = base + '/freight/butchRemove.json';
//进入物流
var entryDistributionUrl = base + "/freight/distributionMode.htm";
//查看支持区域
var viewSupportedAreaUrl = base + "/freight/supportedAreaList.htm";
//新建
var createDistributionUrl = base + "/freight/createDistributionMode.htm";

// 获取id
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' name='chid' class='checkId' value='" + loxia.getObject("id", data) + "'/>";
}
// 刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}
//类型名单
function drawEditOperate(data) {
	var id = loxia.getObject("id", data);
	var result="";  
	var state=loxia.getObject("lifecycle", data);
	var toview   ="<a href='javascript:void(0);' val='"+id+"' class='func-button view-area'>"+nps.i18n("TO_VIEW")+"</a>";
	var tomodify ="<a href='javascript:void(0);' val='"+id+"' class='func-button update-distribution'>"+nps.i18n("TO_UPDATE")+"</a>";
	var todelete ="<a href='javascript:void(0);' val='"+id+"' class='func-button delete'>"+nps.i18n("TO_DELETE")+"</a>";
	result +=toview + tomodify + todelete;
	return result;
}


$j(document).ready(function() {

	loxia.init({
		debug : true,
		region : 'zh-CN'
	});

	// 动态获取数据库物流信息表
	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		cols : [ {
			label : "<input type='checkbox'/>",
			width : "10%",
			template : "drawCheckbox"
		},
		{
			name : "name",
			label : nps.i18n("LABEL_DISTRIBUTION_NAME"),
			width : "15%"
		},
		{
			label : nps.i18n("LBAEL_DISTRIBUTION_OPERATE"),
			width : "20%",
			template : "drawEditOperate"
		} 
		],
		dataurl : distributionListUrl
	});
	refreshData();

	// 筛选
	$j(".func-button.search").click(function() {
		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();
	});
	
	//新建按钮
	$j(".add-distribution").click(function() {
		window.location.href=createDistributionUrl+"?id=";
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
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("DISTRIBUTION_TIP_NOSEL_DEL"));
			return;
		}
		nps.confirm(nps.i18n("CONFIRM_DELETE"), nps.i18n("CONFIRM_DELETE_DISTRIBUTION"), function() {
			var json = {
				"ids" : data
			};
			
			nps.asyncXhrPost(butchDeleteDistributionUrl, json, {
				successHandler : function(data, textStatus) {
					if (data.isSuccess) {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
						window.location.href=entryDistributionUrl;
					} else {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_FAILUE"));
					}
				}
			});
		});
	});
	
	//编辑按钮
	$j("#table1").on("click", ".update-distribution", function(){
		var curObject=$j(this);
		var currentId = curObject.attr("val");
		window.location.href=createDistributionUrl+"?id="+currentId;
	});
	
	//查看按钮
	$j("#table1").on("click", ".view-area", function(){
		var curObject=$j(this);
		var currentId = curObject.attr("val");
		var url = viewSupportedAreaUrl+"?distributionModeId="+currentId;
		window.location.href=url;
	});
	
	//编辑删除按钮
	$j("#table1").on("click", ".delete", function(){
		var curObject=$j(this);
		var currentId = curObject.attr("val");
		nps.confirm(nps.i18n("CONFIRM_DELETE"), nps.i18n("CONFIRM_DELETE_DISTRIBUTION"), function() {
			
			var json = {
					'id':currentId
			};
			// 提交表单
			nps.asyncXhrPost(deleteDistributionUrl, json, {
				successHandler : function(data, textStatus) {
					if (data.isSuccess) {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
						window.location.href=entryDistributionUrl;
					} else {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_FAILUE"));
					}
				}
			});
		});
	});
	
	//编辑保存按钮
	$j(".btn-save").click(function() {
		
		var currentId = $j("#mode_id").val();
		var currentName = $j("#new_name").val();
		if (currentName.length == 0 || currentName == null) {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_SELECT_DATA"));
			return;
		}
		var json = {
				'id':currentId,
				'name' : currentName
		};
		// 提交表单
		nps.asyncXhrPost(saveDistributionUrl, json, {
			   successHandler : function(data, textStatus) {
				   if (data.isSuccess) {
					   if (currentId.length == 0 || currentId == null) {
						   nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SUCCESS"));
					   }else{
						   nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_MODIFY_SUCCESS"));
					   }
						 window.location.href=entryDistributionUrl;
					} else {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_FAILUE"));
					}
			   }
		});
	});
	
	
	//返回按钮
	$j(".btn-return").click(function() {
		window.location.href=entryDistributionUrl;
	});
});