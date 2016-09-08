$j.extend(loxia.regional['zh-CN'],{
	"TO_VIEW":"查看",
	"TO_UPDATE":"修改",
	"TO_ENABLE":"启用",
	"TO_DEL":"删除",
	"PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的SkuInventoryChangeLog么？",
	"INFO_TITLE_DATA":"提示信息",
	"INFO_DELETE_SUCCESS":"删除记录成功!",
	"INFO_ADD_SUCCESS":"添加成功!",
	"INFO_EDIT_SUCCESS":"修改成功!",
    "INFO_DELETE_FAIL":"删除记录失败!",
    "INFO_START_SUCCESS":"启用成功!",
    "INFO_START_FAIL":"启用失败!",
    "INFO_CANCLE_START_SUCCESS":"取消启用成功!",
    "INFO_CANCEL_START_FAIL":"取消启用失败!",
	"TO_CANCEL":"禁用",
	"YES":"有效",
	"NO":"无效",
	"CONFIRM_EN":"确定要启用SkuInventoryChangeLog?",
	"CONFIRM_DIS":"确定要禁用SkuInventoryChangeLog?",
	"EN_SUCCESS":"启用成功",
	"EN_FAIL":"启用失败",
	"DIS_FAIL":"禁用失败",
	"DIS_SUCCESS":"禁用成功",
	"SELECT_EMAIL":"请选择需要删除的SkuInventoryChangeLog",
	"drawEditor":"操作",
	"NO_DATA":"查询数据为空"
});

// 列表
var urlList = base+'/backlog/scmReceivedLog/page.json';

// 刷新数据
function refreshData() {
	$j("#tableList").loxiasimpletable("refresh");
}

$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	// 商品状态
	$j.ui.loxiasimpletable().typepainter.threeState = {
		getContent : function(data) {
			if (data == 0) {
				return "<span class='ui-pyesno ui-pyesno-no' title='下架'></span>";
			} else if (data == 1) {
				return "<span class='ui-pyesno ui-pyesno-yes' title='上架'></span>";
			} else if (data == 3) {
				return "<span class='ui-pyesno ui-pyesno-wait' title='新建'></span>";
			}
		},
		postHandle : function(context) {
			// do nothing
		}
	};
	// 通过loxiasimpletable动态获取数据
	$j("#tableList").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [{
				name : "ifIdentify",
				label : "接口标识",
				width : "10%",
				template : "getTypeIfIdentify"
			},
			{
				name : "sendTime",
				label : "Msg发送时间",
				width : "10%",
			    formatter : "formatDate"
			},
			{
				name : "msgId",
				label : "消息Id",
				width : "10%"
			},
			{
				name : "msgBody",
				label : "消息内容",
				width : "10%",
			}
			,
			{
				name : "isProccessed",
				label : "是否已经处理过",
				width : "10%"
			}],
		dataurl : urlList
	});
	//加载数据
	refreshData();
	// 筛选数据
	$j(".func-button.search").click(function(){
		 $j("#tableList").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	});
	 //重置查询表单
	 $j(".reset").on("click",function(){
		 $j("#searchForm")[0].reset();
	 });
});
//获取类型
function getTypeIfIdentify(data, args, idx){
	var val =loxia.getObject("ifIdentify", data);
	if (val == null || val == '') {
		return "&nbsp;";
	} else if(val == 'O1-1'){
		return "订单状态同步(scm2shop)";
	} else if(val == 'P1-1'){
		return "商品信息同步";
	} else if(val == 'P2-1'){
		return "在售商品同步";
	} else if(val == 'P1-2'){
	   return "商品价格同步";
    } else if(val == 'I1-1'){
	   return "库存同步(全)";
    } else if(val == 'I1-2'){
	   return "库存同步(增)";
    } else if(val == 'O2-1'){
    	return "订单推送";
    } else if(val == 'O2-2'){
    	return "付款推送";
    } else if(val == 'O2-3'){
    	return "订单状态同步(shop2scm)";
    } else if(val == 'L1-1'){
    	return "物流跟踪";
    } else if(val == 'L2-1'){
    	return "SF上门取件";
}
}

/*//获取来源
function getSourceLabel(data, args, idx){
	var val =loxia.getObject("source", data);
	if (val == null || val == '') {
		return "&nbsp;";
	} else if(val == '1'){
		return "商城";
	} else if(val == '2'){
		return "PTS";
	} else if(val == '3'){
		return "定时任务";
	} else if(val == '4'){
		return "OMS";
	}
}
*/
//获取日期
function formatDate(val) {
	if (val == null || val == '') {
		return "&nbsp;";
	} else {
		var date = new Date(val);
		return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":"
				+ date.getMinutes() + ":" + date.getSeconds();
	}
}


