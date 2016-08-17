$j.extend(loxia.regional['zh-CN'],{
	"LABEL_ORDER_CODE":"订单号",
	"LABEL_ORDER_OMSCODE":"OMS订单号 ",
	"LABEL_ORDER_CONSIGNEE_NAME":"收货人",
	"LABEL_ORDER_MEMBER_NAME":"会员名",
	"LABEL_ORDER_SOURCE":"来源",
	"LABEL_ORDER_PAYMENT":"支付方式",
	"LABEL_ORDER_LOGISTICS":"物流状态",
	"LABEL_ORDER_FINANCIAL":"财务状态",
	"LABEL_ORDER_QUANTITY":"总数量",
	"LABEL_ORDER_DISCOUNT":"折扣",
	"LABEL_ORDER_TOTAL":"订单总金额",
	"LABEL_ORDER_CREATETIME":"创建时间",
	"LABEL_ORDER_REMARK":"备注"
});

var orderListUrl = base +'/order/orderList.json';

var orderDetailUrl = base +'/order/orderDetail.htm';

var guestCreateSalesOrderUrl = base +'/order/createOrder/guest.htm';
var memberCreateSalesOrderUrl = base +'/order/createOrder/member.htm';
var manualCreateSalesOrderUrl = base +'/order/createOrder/manual.htm';

function drawOrder(data, args, idx){
	
	var tempUrl =orderDetailUrl+"?orderCode="+loxia.getObject("code", data)+"&orderId="+loxia.getObject("id", data);
	
	return "<a style='cursor:pointer;' onclick=\"loxia.openPage('" +tempUrl+
			"');\">"+loxia.getObject("code", data)+"</a>";
}

//计算订单总金额
function calTotalAmount(data){
	return data.total+data.actualFreight;
}

//支付方式
function drawPayment(data, args, idx){
	var tempPayment = loxia.getObject("payment", data);
	var result = "";
	if(tempPayment == '1'){
		result += "货到付款";
	}else if(tempPayment == '2'){
		if(result != ""){
			result += "&&";
		}
		result += "银行电汇";
	}else if(tempPayment == '3'){
		if(result != ""){
			result += "&&";
		}
		result += "网银在线";
	}else if(tempPayment == '4'){
		if(result != ""){
			result += "&&";
		}
		result += "微信支付";
	}else if(tempPayment == '6'){
		if(result != ""){
			result += "&&";
		}
		result += "支付宝";
	}else if(tempPayment == '7'){
		if(result != ""){
			result += "&&";
		}
		result += "快钱";
	}else if(tempPayment == '10'){
		if(result != ""){
			result += "&&";
		}
		result += "预付卡";
	}else if(tempPayment == '300'){
		if(result != ""){
			result += "&&";
		}
		result += "财付通";
	}else if(tempPayment == '12'){
		if(result != ""){
			result += "&&";
		}
		result += "外部积分兑换";
	}else if(tempPayment == '104'){
		if(result != ""){
			result += "&&";
		}
		result += "新华一成卡";
	}else if(tempPayment == '108'){
		if(result != ""){
			result += "&&";
		}
		result += "LEVIS淘宝B2C－支付宝";
	}else if(tempPayment == '200'){
		if(result != ""){
			result += "&&";
		}
		result += "百付宝（汇付天下）";
	}else if(tempPayment == '9'){
		if(result != ""){
			result += "&&";
		}
		result += "现金收款";
	}else if(tempPayment == '14'){
		if(result != ""){
			result += "&&";
		}
		result += "信用卡-支付宝";
	}else if(tempPayment == '18'){
		if(result != ""){
			result += "&&";
		}
		result += "支付宝-快捷支付网关接口";
	}else if(tempPayment == '19'){
		result += "支付宝信用卡分期付款";
	}else if(tempPayment == '20'){
		if(result != ""){
			result += "&&";
		}
		result += "零元购";
	}else if(tempPayment == '320'){
		if(result != ""){
			result += "&&";
		}
		result += "银联支付";
	}else{
		result += "";
	}	
	return result;
}

//刷新表格数据 
function refreshData(){
	$j("#table1").loxiasimpletable("refresh"); 
}
//获取日期
function formatDate(date, format) {  
    if (!date) return;  
    if (!format) format = "yyyy-MM-dd";  
    switch(typeof date) {  
        case "string":  
            date = new Date(date.replace(/-/, "/"));  
            break;  
        case "number":  
            date = new Date(date);  
            break;  
    }   
    if (!date instanceof Date) return;  
    var dict = {  
        "yyyy": date.getFullYear(),  
        "M": date.getMonth() + 1,  
        "d": date.getDate(),  
        "H": date.getHours(),  
        "m": date.getMinutes(),  
        "s": date.getSeconds(),  
        "MM": ("" + (date.getMonth() + 101)).substr(1),  
        "dd": ("" + (date.getDate() + 100)).substr(1),  
        "HH": ("" + (date.getHours() + 100)).substr(1),  
        "mm": ("" + (date.getMinutes() + 100)).substr(1),  
        "ss": ("" + (date.getSeconds() + 100)).substr(1)  
    };      
    return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function() {  
        return dict[arguments[0]];  
    });                  
}

//获取日期时间
function formatDateTime(date, format) {  
    if (!date) return;  
    if (!format) format = "yyyy-MM-dd HH:mm:ss";  
    switch(typeof date) {  
        case "string":  
            date = new Date(date.replace(/-/, "/"));  
            break;  
        case "number":  
            date = new Date(date);  
            break;  
    }   
    if (!date instanceof Date) return;  
    var dict = {  
        "yyyy": date.getFullYear(),  
        "M": date.getMonth() + 1,  
        "d": date.getDate(),  
        "H": date.getHours(),  
        "m": date.getMinutes(),  
        "s": date.getSeconds(),  
        "MM": ("" + (date.getMonth() + 101)).substr(1),  
        "dd": ("" + (date.getDate() + 100)).substr(1),  
        "HH": ("" + (date.getHours() + 100)).substr(1),  
        "mm": ("" + (date.getMinutes() + 100)).substr(1),  
        "ss": ("" + (date.getSeconds() + 100)).substr(1)  
    };      
    return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function() {  
        return dict[arguments[0]];  
    });                  
}

$j(document).ready(function() {
	
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	
	searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});
	
	
    $j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form : "searchForm",
		cols : [ {
			name:"code",
			label:nps.i18n("LABEL_ORDER_CODE"),
			width:"10%",
			template:"drawOrder"
		},	{
			name:"omsCode",
			label:nps.i18n("LABEL_ORDER_OMSCODE"),
			width:"10%"
		},	{
			name:"name",
			label:nps.i18n("LABEL_ORDER_CONSIGNEE_NAME"),
			width:"6%",
			sort: ["sc.name asc","sc.name desc"]
		},{
			name:"memberName",
			label:nps.i18n("LABEL_ORDER_MEMBER_NAME"),
			width:"6%",
			sort: ["o.member_name asc","o.member_name desc"]
		},{
			name:"sourceLabel",
			label:nps.i18n("LABEL_ORDER_SOURCE"),
			width:"5%"
		},	{
			name:"paymentLabel",
			label:nps.i18n("LABEL_ORDER_PAYMENT"),
			width:"6%",
			template:"drawPayment"
		},	{
			name:"logisticsLabel",
			label:nps.i18n("LABEL_ORDER_LOGISTICS"),
			width:"6%"
		},	{
			name:"financialLabel",
			label:nps.i18n("LABEL_ORDER_FINANCIAL"),
			width:"6%"
		},	{
			name:"quantity",
			label:nps.i18n("LABEL_ORDER_QUANTITY"),
			width:"4%"
		},	{
			name:"discount",
			label:nps.i18n("LABEL_ORDER_DISCOUNT"),
			width:"5%"
		},	{
			/*name:"total",*/
			label:nps.i18n("LABEL_ORDER_TOTAL"),
			width:"8%",
			template:"calTotalAmount",
			sort: ["o.total asc","o.total desc"]
		},	{
			name:"createTime",
			label:nps.i18n("LABEL_ORDER_CREATETIME"),
			width:"12%",
			formatter:"formatDateTime",
			sort: ["o.create_time asc","o.create_time desc"]
		},	{
			name:"remark",
			label:nps.i18n("LABEL_ORDER_REMARK"),
			width:"16%"
		} ],
		dataurl : orderListUrl
	});

	refreshData();

	//筛选
	$j(".func-button.search").click(function(){
		 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	});
	
	//游客下单
	 $j(".button.orange.guestCreate").click(function(){
		 window.location.href =guestCreateSalesOrderUrl;
	 });
	 
	//会员下单
	 $j(".button.orange.memberCreate").click(function(){
		 window.location.href =memberCreateSalesOrderUrl;
	 });
	
	//手工下单
	 $j(".button.orange.manualCreate").click(function(){
		 window.location.href =manualCreateSalesOrderUrl;
	 }); 
	 
});