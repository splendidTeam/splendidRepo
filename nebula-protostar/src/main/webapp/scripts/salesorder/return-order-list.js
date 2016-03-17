$j.extend(loxia.regional['zh-CN'],{
	"LABEL_ITEMNAME":"名称",
	"LABEL_ITEMCOUNT":"数量",

	"LABEL_ORDER_CONSIGNEE_NAME":"收货人",
	"LABEL_ORDER_CONSIGNEE_ADDRESS":"地址",
	"LABEL_ORDER_CONSIGNEE_MOBILE":"电话号码",
	"NO_DESC":"无",
	
	"LABEL_RETURN_ORDER_MEMBERNAME":"会员名",
	"LABEL_ORDER_CODE":"订单号",
	"LABEL_ITEMINFO":"商品信息",
	"LABEL_RETURN_ORDER_SERVICETYPE":"服务类型",
	"LABEL_RETURN_ORDER_APPSTATUS":"申请状态",
	"LABEL_RETURN_ORDER_ISRECEIPT":"有无发票",
	"LABEL_RETURN_ORDER_PIC":"问题图片",
	"LABEL_RETURN_ORDER_DESCRIBE":"问题描述",
	"LABEL_RETURN_ORDER_CONSIGNEE":"联系人信息",
	"LABEL_RETURN_ORDER_CREATETIME":"创建时间",
	"LABEL_RETURN_ORDER_HANDLENAME":"处理人"
});


var returnOrderListUrl =base +'/order/returnApplicationList.json';

var orderDetailUrl = base +'/order/orderDetail.htm';

function drawItemInfo(data, args, idx){
	
	var item_pic=loxia.getObject("itemPic",data);
	
	var result ="";
	
	result+="<div id = 'imgDiv' style = 'text-align:left'>";
	var itemPic="";
	if(item_pic==null||item_pic==''){
		itemPic="<img src='"+base+"/images/error.jpg' width=60 height=60>";
	}else{
		itemPic="<img src='"+base+"/images/"+item_pic+"' width=60 height=60>";
	}
	result +=itemPic;
	result +="</div>";
	
	result +="<div id = 'itemInfoDiv' style = 'text-align:left'>";
	
	itemInfo ="<label style ='margin:auto'>"+
					"<div>"+nps.i18n("LABEL_ITEMNAME")+" : "+(loxia.getObject("itemName", data)==null?"":loxia.getObject("itemName", data))+"</div>"+
						"</br>"+
					"<div>"+nps.i18n("LABEL_ITEMCOUNT")+" : "+(loxia.getObject("itemCount", data)==null?"":loxia.getObject("itemCount", data))+"</div>"+
				"</label>";
	result+=itemInfo;
	result +="</div>";
	return result;
}

function drawProbIcon(data, args, idx){
	
	var pic=loxia.getObject("pic",data);
	
	var probPic="";
	if(pic==null||pic==''){
		probPic="<img src='"+base+"/images/error.jpg' width=60 height=60>";
	}else{
		probPic="<img src='"+base+"/images/"+pic+"' width=60 height=60>";
	}
	
	return probPic;
}


function drawOrder(data, args, idx){
	
	var tempUrl =orderDetailUrl+"?orderCode="+loxia.getObject("orderCode", data);
	
	return "<a style='cursor:pointer;' onclick=\"loxia.openPage('" +tempUrl+
			"');\">"+loxia.getObject("orderCode", data)+"</a>";
}

function drawMemberInfo(data, args, idx){
	return "<label>"+
				"<div>"+nps.i18n("LABEL_ORDER_CONSIGNEE_NAME")+" : "+loxia.getObject("name", data)+"</div>"+
					"</br>"+
				"<div>"+nps.i18n("LABEL_ORDER_CONSIGNEE_ADDRESS")+" : "+loxia.getObject("address", data)+"</div>"+
					"</br>"+
				"<div>"+nps.i18n("LABEL_ORDER_CONSIGNEE_MOBILE")+" : "+loxia.getObject("mobile", data)+"</div>"+
			"</label>";
}

function descTemp(data, args, idx){
	//describe
	var describe =loxia.getObject("describe", data);
	
	var resultSpan ="";
	
	var tempData ="";
	
	if(describe!=null&&describe.trim()!=''){
		tempData=(describe.length < 12?describe:(describe.substring(0,12)+"..."));
		resultSpan ="<span title ="+describe+">"+tempData+"</span>";
	}else{
		resultSpan =nps.i18n("NO_DESC");
	}
	
	return resultSpan;
	
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
        "ss": ("" + (date.getSeconds() + 100)).substr(1), 
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
        "ss": ("" + (date.getSeconds() + 100)).substr(1),  
    };      
    return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function() {  
        return dict[arguments[0]];  
    });                  
}

//刷新表格数据 
function refreshData(){
	$j("#table1").loxiasimpletable("refresh");
}

$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
	
	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage :'<span>'+nps.i18n("NO_DATA")+'</span>',
		form :"searchForm",
		cols : [ {
			name:"orderCode",
			label:nps.i18n("LABEL_ORDER_CODE"),
			width:"10%",
			template:"drawOrder"
		},	{
			name:"itemInfo",
			label:nps.i18n("LABEL_ITEMINFO"),
			width:"8%",
			template:"drawItemInfo"
		},	{
			name:"memberName",
			label:nps.i18n("LABEL_RETURN_ORDER_MEMBERNAME"),
			width:"7%",
			sort: ["sro.MEMBER_NAME asc","sro.MEMBER_NAME desc"]
		},	{
			name:"name",
			label:nps.i18n("LABEL_ORDER_CONSIGNEE_NAME"),
			width:"7%",
			sort: ["sro.name asc","sro.name desc"]
		},	{
			name:"serviceName",
			label:nps.i18n("LABEL_RETURN_ORDER_SERVICETYPE"),
			width:"5%"
		},	{
			name:"appStatus",
			label:nps.i18n("LABEL_RETURN_ORDER_APPSTATUS"),
			width:"7%"
		},	{
			name:"isReceipt",
			label:nps.i18n("LABEL_RETURN_ORDER_ISRECEIPT"),
			width:"5%",
			type:"yesno"
		},	{
			name:"pic",
			label:nps.i18n("LABEL_RETURN_ORDER_PIC"),
			width:"7%",
			template:"drawProbIcon"
		},	{
			name:"describe",
			label:nps.i18n("LABEL_RETURN_ORDER_DESCRIBE"),
			width:"15%",
			template:"descTemp"
		},	{
			name:"consigneeInfo",
			label:nps.i18n("LABEL_RETURN_ORDER_CONSIGNEE"),
			width:"12%",
			template:"drawMemberInfo"
		},	{
			name:"createTime",
			label:nps.i18n("LABEL_RETURN_ORDER_CREATETIME"),
			width:"10%",
			formatter:"formatDateTime",
			sort: ["sro.create_time asc","sro.create_time desc"]
		},	{
			name:"handleName",
			label:nps.i18n("LABEL_RETURN_ORDER_HANDLENAME"),
			width:"7%",
			sort: ["sro.HANDLE_NAME asc","sro.HANDLE_NAME desc"]
		} ],
		dataurl : returnOrderListUrl
	});
	refreshData();

	//筛选
	$j(".func-button.search").click(function(){
		 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	});
});