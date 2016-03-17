/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'],{
	"NO_DATA":"无数据",
	"NO_CATEGORY":"无",
    "LABEL_MEMBER_LOGINNAME":"会员名",
    "LABEL_MEMBER_NICKNAME":"昵称",
    "LABEL_MEMBER_GROUP":"分组",
    "LABEL_MEMBER_LIFECYCLE":"状态"
});

/* ------------------------------------------------- URL ------------------------------------------------- */
var MEMBER_LIST_URL = base + "/member/memberList.json";

/* ------------------------------------------------- 全局常量 ------------------------------------------------- */

/* ------------------------------------------------- 全局变量 ------------------------------------------------- */


/* ------------------------------------------------- ready ------------------------------------------------- */
$j(document).ready(function() {
	
	$j("#member-selector-table").loxiasimpletable({
		page : true,
		size : 10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"frm-member-selector",
		cols : [ {
			label : "<input type='checkbox' />",
			width : "5%",
			template : "drawCheckbox"
		}, {
			name : "loginName",
			label : nps.i18n("LABEL_MEMBER_LOGINNAME"),
			width : "10%",
			template:"drawName"
		}, {
			name : "groupName",
			label : nps.i18n("LABEL_MEMBER_GROUP"),
			width : "5%",
			template:"formatGroupName"
		}, {
			name : "lifecycle",
			label : nps.i18n("LABEL_MEMBER_LIFECYCLE"),
			width : "5%",
			type:"yesno"
		} ],
		dataurl : MEMBER_LIST_URL
	});
	$j("#member-selector-table").loxiasimpletable("refresh");
	
	$j(".func-button.search").click(function(){
		$j("#member-selector-table").data().uiLoxiasimpletable.options.currentPage=1;
		$j("#member-selector-table").loxiasimpletable("refresh");
	});

	$j("#member-selector .btn-ok").click(function() {
		var arr = [];	//用户选择的会员列表
		$j(".chk-id:checked").each(function(i, dom) {
			arr[i] = {
				id: $j(dom).attr("value"),
				name: $j(dom).attr("data_name")
			};
		});
		$j("#member-selector-data").data("memberList", arr).click();	//此隐藏域的点击事件是对外开放的接口，可以自定义点击事件
	});
});


/* ------------------------------------------------- loxia-table template定义 ------------------------------------------------- */
function drawCheckbox(data, args, idx) {
	return "<input name='chk-id' type='checkbox' class='chk-id' value='"
			+ loxia.getObject("id", data) + "' data_name='" + loxia.getObject("loginName", data) + "' />";
}
function drawName(data, args, idx) {
	return loxia.getObject("loginName", data);
}
function formatGroupName(data, args, idx) {
	var propertyNameArray = loxia.getObject("groupName", data);
	if (propertyNameArray == null || propertyNameArray == '') {
		return nps.i18n("NO_CATEGORY");
	}
	return propertyNameArray;
}
