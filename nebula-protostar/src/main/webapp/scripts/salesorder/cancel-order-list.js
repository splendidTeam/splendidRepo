$j.extend(loxia.regional['zh-CN'],{
	"LABEL_ORDER_CODE":"订单号",
	"LABEL_MEMBER_NAME":"会员名称",
	"LABEL_REASON":"取消原因",
	"LABEL_MOBILE":"联系人电话",
	"LABEL_STATUS_INFO":"状态",
	"LABEL_MESSAGE":"留言",
	"LABEL_HANDLE_NAME":"处理人",
	"LABEL_CREATE_TIME":"订单日期"
	
});

var cancelApplicationOrderListUrl = base +'/order/cancelApplicationOrderList.json';

var orderDetailUrl = base +'/order/orderDetail.htm';

//转换日期
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
}


function drawOrder(data, args, idx){
	
	var tempUrl =orderDetailUrl+"?orderCode="+loxia.getObject("orderCode", data);
	
	return "<a style='cursor:pointer;' onclick=\"loxia.openPage('" +tempUrl+"','_blank',null,[0,0]);\">"+loxia.getObject("orderCode", data)+"</a>";
}

//刷新表格数据 
function refreshData(){
	$j("#table1").loxiasimpletable("refresh"); 
}



$j(document).ready(function() {
	
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	
	searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});
	
	
    $j("#table1").loxiasimpletable({
		page: true,
		size : 10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols:
		[
			{name:"orderCode",label:nps.i18n("LABEL_ORDER_CODE"),width:"20%",sort:["sco.ORDER_CODE asc","sco.ORDER_CODE desc"],template:"drawOrder"},
			{name:"memberName",label:nps.i18n("LABEL_MEMBER_NAME"),width:"15%"},
			{name:"reason",label:nps.i18n("LABEL_REASON"),width:"15%"},
			{name:"mobile",label:nps.i18n("LABEL_MOBILE"),width:"10%"},
			{name:"statusInfo",label:nps.i18n("LABEL_STATUS_INFO"),width:"8%",sort:["sco.STATUS asc","sco.STATUS desc"]},
			{name:"message",label:nps.i18n("LABEL_MESSAGE"),width:"10%"},
			{name:"handleName",label:nps.i18n("LABEL_HANDLE_NAME"),width:"10%"},
			{name:"createTime",label:nps.i18n("LABEL_CREATE_TIME"),width:"12%",formatter:"formatDate",sort: ["sco.create_time asc","sco.create_time desc"]}
		],
		dataurl : cancelApplicationOrderListUrl
	});
    
    
    refreshData();

	//筛选
	$j(".func-button.search").click(function(){
		 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	});
});