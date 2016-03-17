$j.extend(loxia.regional['zh-CN'],{
	"LABEL_PROMOTION_PROMOTIONNAME":"限购名称",
	 "LABEL_PROMOTION_STARTTIME":"开始时间",
	    "LABEL_PROMOTION_ENDTIME":"结束时间",
	    "LABEL_PROMOTION_AUDIENCEVALUE":"受限人群",
	    "LABEL_PROMOTION_TYPE":"状态", 
	    "LABEL_PROMOTION_GROUP":"优惠项",
	    "LABEL_PROMOTION_UPDATETIME":"更新时间",
	    "LABEL_PROMOTION_UPDATE":"更新人员", 
	    "LABEL_MEMBER_OPERATE":"操作",	    
	    "LABEL_PROMOTION_WAT":"待启用",
	    "LABEL_PROMOTION_STARTUSERFUL":"已启用",
	    "LABEL_PROMOTION_USEFUL":"已生效",
	    "LABEL_PROMOTION_CANCEL":"已取消", 	 
	    "LABEL_PROMOTION_LOGOTYPE":"角标",
	    "TO_DISABLE":"禁用",
	    "TO_ENABLE":"启用",
	    "TO_OPERATING":"编辑",	  
	    "INFO_STOP_SUCCESS":"禁用成功!",
	    "NO_CATEGORY":"无",
	    "NO_DATA":"无数据",
	    "INFO_TITLE_DATA":"提示信息",
	    "INFO_ENABLE_SUCCESS":"启用成功",
	    "INFO_DISABLE_SUCCESS":"禁用成功"
});

//促销活动编辑列表
var promotionEditListUrl = base+'/limit/limitEditList.json'; 
 

//跳转到编辑页面
var promotionUpdateURl = base+'/limit/edit.htm';
var promotionCreateURl = base+'/limit/edit.htm';

 
 
function drawSettingtype(data){
	var result=""; 
	var setting=loxia.getObject("settingList", data);	 
	var type=loxia.getObject("settingType", setting); 
	if(type !=null){
		if(type=1) 
			result+="减";
		if(type=2) 
			result+="折";
		if(type=3) 
			result+="免运费";
		if(type=4) 
			result+="优惠价"; 
	}
 	return result; 
}
function comboTypeCk(data){
	var result=""; 
	var type=loxia.getObject("memberComboType", data);	  
	if(type !=null){
		if(type==1) 
		result="会员";
		if(type==2) 
		result="分组";
		if(type==4) 
		result="组合"; 
	}
 	return result; 
} 
function drawEditor(data, args, idx){  
	var result="";
	 result+="<a href='"+promotionUpdateURl+"?id="+loxia.getObject("id", data)+"' val='"+loxia.getObject("promotionId", data)+"' class='func-button editor'>"+nps.i18n("TO_OPERATING")+"</a>";
	return result;
}
//获取id
function drawCheckbox(data, args, idx){
	return "<input name='chedkId' type='checkbox' class='checkId'  value='" + loxia.getObject("promotionId", data)+"'/>";
}
 
//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}
function statusLifeycle(data, args, idx){ 
	var result="";
	var state=loxia.getObject("lifecycle", data);
	if(state==0){
		result+= nps.i18n("LABEL_PROMOTION_WAT");
	}else if(state==1){
	    result += nps.i18n("LABEL_PROMOTION_STARTUSERFUL");
	}else if(state==2){
	    result+= nps.i18n("LABEL_PROMOTION_USEFUL");
	}else if(state==3){
	   result+= nps.i18n("LABEL_PROMOTION_CANCEL");
	} 
	return result;
}

//获取日期格式
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
} 

//通过loxiasimpletable动态获取数据
$j(document).ready(function() {
	
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();

	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [
		 {
			name : "name",
			label : nps.i18n("LABEL_PROMOTION_PROMOTIONNAME"),
			width : "20%" 
			
		},   {
			name : "startTime",
			label : nps.i18n("LABEL_PROMOTION_STARTTIME"),
			width : "10%",
		    formatter:"formatDate",
		    sort:["h.START_TIME asc","h.START_TIME desc"	]
		},  {
			name : "endTime",
			label : nps.i18n("LABEL_PROMOTION_ENDTIME"),
			width : "10%",
			formatter:"formatDate" ,
			sort:["h.END_TIME asc","h.END_TIME desc"]
		}, {
			name : "memberComboType",
			label : nps.i18n("LABEL_PROMOTION_AUDIENCEVALUE"),
			width : "5%" ,
			template : "comboTypeCk"
		}, {
			name : "lifecycle",
			label : nps.i18n("LABEL_PROMOTION_TYPE"),
			width : "10%" , 
			template:"statusLifeycle"
			
		},  
		   {
			name : "updateTime",
			label : nps.i18n("LABEL_PROMOTION_UPDATETIME"),
			width : "10%" ,
			formatter:"formatDate",
			sort:["h.update_time asc","h.update_time desc"]
		}, {
			name : "updateName",
			label : nps.i18n("LABEL_PROMOTION_UPDATE"),
			width : "5%" 
		} , {
			name : "操作",
			label : nps.i18n("LABEL_MEMBER_OPERATE"),
			width : "7%", 			 
			template : "drawEditor"
		} ],
		dataurl : promotionEditListUrl+"?type='ec'"
	});
	refreshData();
	 
	//筛选数据
	 $j(".func-button.search").click(function(){
			 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
			 refreshData();
	   });  
	 
	 
    $j(".button.orange.addPromotion").click(function(){
        window.location.href=promotionCreateURl;
    });
    
    
});
