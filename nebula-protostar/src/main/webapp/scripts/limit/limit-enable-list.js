$j.extend(loxia.regional['zh-CN'],{
	"LABEL_PROMOTION_PROMOTIONNAME":"活动名称",
	 "LABEL_PROMOTION_STARTTIME":"开始时间",
	    "LABEL_PROMOTION_ENDTIME":"结束时间",
	    "LABEL_PROMOTION_AUDIENCEVALUE":"受限人群",
	    "LABEL_PROMOTION_TYPE":"状态",
	    "LABEL_PROMOTION_LIMITVALUE":"限购",
	    "LABEL_PROMOTION_GROUP":"优惠项",
	    "LABEL_PROMOTION_UPDATETIME":"更新时间",
	    "LABEL_PROMOTION_UPDATE":"更新人员", 
	    "LABEL_MEMBER_OPERATE":"操作",	    
	    "LABEL_PROMOTION_WAT":"待启用",
	    "LABEL_PROMOTION_STARTUSERFUL":"已启用",
	    "LABEL_PROMOTION_USEFUL":"已生效",
	    "LABEL_PROMOTION_CANCEL":"已取消",  
	    "LABEL_PROMOTION_LOGOTYPE":"角标",
	    "TO_VIEW":"查看",
	    "TO_UPDATE":"修改",
	    "TO_ENABLE":"启用",
	    "TO_CANCEL":"取消启用",
	    "TO_CANCEL_VALIDE":"取消生效",
	    "TO_DRAFT_COPY":"复制草稿", 
	    "INFO_TITLE_DATA":"提示信息",
	    "INFO_STOP_FAIL":"取消启用失败!",
	    "INFO_DISABLE_SUCCESS":"取消启用成功",
	    "NO_CATEGORY":"无", 
	    "USER_CONFIRM_ENABLE_PROMTION":"确认要启用限购吗？",
	    "USER_CONFIRM_DISABLE_PROMTION":"确认要取消启用活动", 
	    "USER_CONFIRM_COPY_PROMTION":"确认要复制为草稿", 
	    "USER_INFO_CONFIRM":"确认信息",
	    "NO_DATA":"无数据",
	    "NO_OVER_DATA":"无重叠数据",
	    "USER_TIP_NOSEL":"请选择您要禁用的会员",
	    "USER_TIP_ENABLE_NOSEL":"请选择您要启用的会员",
	    "INFO_TITLE_DATA":"提示信息",
	    "INFO_ENABLE_SUCCESS":"启用成功", 
	    "INFO_ENABLE_FAIL":"启用失败",
	    "INFO_COPY_SUCCESS":"启用成功",
	    "INFO_ENABLE_FAILTIME":"活动开始时间已经过期！启动活动失败",
	    "INFO_ENABLE_OVERLAPPTIME":"活动时间重叠",
	    "INFO_COPY_FAIL":"复制草稿失败",
	    "INFO_CANCEL_FAIL":"取消启用失败"
});

// 限购列表
var limitUrl = base+'/limit/limitEditList.json'; 

// 操作活动跳转到编辑页面
var promotionPageURl = base+'/limit/edit.htm'; 

var promotionViewURl = base+'/limit/view.htm'; 
//取消启用，失效
var INACTIVATION_URL = base + "/limit/inactivate.json";

// 启用前冲突检查
var BEFORE_ACTIVATION_URL = base + "/limit/beforeActivation.json";
// 启用
var ACTIVATION_URL = base + "/limit/activate.json";

// 启用活动验证时间商品重叠
var enableCheckPromotionTimelappUrl="";//base+"/promotion/enableCheckPromotionTimeOverlappUrl.json";
// 获取id
function drawCheckbox(data, args, idx){
	var result=""; 
	var state=loxia.getObject("limitMark", data);
	if(state==1 ){
		return "<input name='limitValue' type='checkbox' class='limitValue'  checked='false'  value='" + loxia.getObject("limitValue", data)+"' disabled='disabled'/>";
	}
	return "<input name='limitValue' type='checkbox' class='limitValue'   value='" + loxia.getObject("limitValue", data)+"' disabled='disabled'/>";
	 
}
 
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
	var state=loxia.getObject("lifecycle",data);
	var id=loxia.getObject("id", data);
	var result="<a href='"+ promotionViewURl+"?id="+id+"' class='func-button view'>"+nps.i18n("TO_VIEW")+"</a>";
	var updat="<a href='"+ promotionPageURl+"?id="+id+"' class='func-button editor'>"+nps.i18n("TO_UPDATE")+"</a>";
    var cancel1="<a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button disable'>"+nps.i18n("TO_CANCEL")+"</a>";
    var cancel2="<a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button disable'>"+nps.i18n("TO_CANCEL_VALIDE")+"</a>";
	var enable="<a href='javascript:void(0);' val='"+loxia.getObject("id", data)+"' class='func-button enable'>"+nps.i18n("TO_ENABLE")+"</a>";
	 if(state==0){ 
		result+=updat+enable; 
	 }else if(state==1){
		 result+=cancel1;
	}else if(state==2){
		 result+=cancel2;
	}else if(state==3){
		 result;
	} 
	return result;
}


 
// 刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}
function statusLifeycle(data, args, idx){ 
	var result="";
	var state=loxia.getObject("lifecycle", data);
	if(state==0){
		result += nps.i18n("LABEL_PROMOTION_WAT");
	}else if(state==1){
	    result += nps.i18n("LABEL_PROMOTION_STARTUSERFUL");
	}else if(state==2){
	    result += nps.i18n("LABEL_PROMOTION_USEFUL");
	}else if(state==3){
	   result += nps.i18n("LABEL_PROMOTION_CANCEL");
	} 
	return result;
}

// 获取日期格式
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
} 
// 获取id
function drawCheckbox(data, args, idx){
	return "<input name='chedkId' type='checkbox' class='checkId'  value='" + loxia.getObject("id", data)+"'/>";
}

// 通过loxiasimpletable动态获取数据
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
			width : "17%" 
			
		},{
			name : "startTime",
			label : nps.i18n("LABEL_PROMOTION_STARTTIME"),
			width : "10%",
		    formatter:"formatDate",
		    sort:["h.START_TIME asc","h.START_TIME desc"	]
		},  {
			name : "endTime",
			label : nps.i18n("LABEL_PROMOTION_ENDTIME"),
			width : "10%",
			formatter:"formatDate",
			sort:["h.END_TIME asc","h.END_TIME desc"]
		}, {
			name : "memberComboType",
			label : nps.i18n("LABEL_PROMOTION_AUDIENCEVALUE"),
			width : "7%" ,
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
			width : "9%" ,
			formatter:"formatDate",
			sort:["h.update_time asc","h.update_time desc"]
		}, {
			name : "updateName",
			label : nps.i18n("LABEL_PROMOTION_UPDATE"),
			width : "8%" 
		} , {
			name : "操作",
			label : nps.i18n("LABEL_MEMBER_OPERATE"),
			width : "15%", 			 
			template : "drawEditor"
		} ],
		dataurl : limitUrl+"?type="
	});
	refreshData();
	 
	// 筛选数据
	 $j(".func-button.search").click(function(){
			 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
			 refreshData();
	   });  

	   $j(".button.addPromotion").click(function(){
	        window.location.href=promotionEditorSaveURl;
	    });
	   
	// 启用前检查
	$j("#table1").on("click",".func-button.enable",function(){   
		var id = $j(this).attr("val");
		nps.confirm(nps.i18n("USER_INFO_CONFIRM"),nps.i18n("USER_CONFIRM_ENABLE_PROMTION"), function(){
			var data = nps.syncXhrPost(BEFORE_ACTIVATION_URL, {id: id});
			if (data.isSuccess) {
				$j("#div-conflict tbody").empty();
				var list = data.description;
				$j.each(list, function(i, limit) {
					var tr = $j("<tr><td></td><td></td><td></td><td></td><td></td></tr>");
					tr.addClass(i % 2 == 0 ? "even" : "odd")
						.find("td:first").text(limit.name)
						.next().text(formatDate(limit.startTime))
						.next().text(formatDate(limit.endTime))
						.next().text(limit.memberComboName)
						.next().text(limit.productComboName);
					tr.appendTo($j("#div-conflict tbody"));
				});
				$j("#div-conflict").dialogff({type:'open',close:'in',width:'800px',height:'400px'});
				$j("#div-conflict .btn-ok").attr("data", id);	// 传入当前限购ID
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"), data.description);
			}
		});
	});
	
	// 启用
	$j("#div-conflict .btn-ok").click(function() {
		var id = $j(this).attr("data");
		var data = nps.syncXhrPost(ACTIVATION_URL, {id: id});
		if (data.isSuccess) {
			$j("#div-conflict .dialog-close").click();
			refreshData();
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ENABLE_SUCCESS"));
		} else {
			nps.info(nps.i18n("INFO_TITLE_DATA"), data.description);
		}
	});
	
	// 禁用单行
	   $j("#table1").on("click",".func-button.disable",function(){
	        var curObject=$j(this);
	        nps.confirm(nps.i18n("USER_INFO_CONFIRM"),nps.i18n("USER_CONFIRM_DISABLE_PROMTION"), function(){

	            var json={"id":curObject.attr("val")};

	        	var _d = nps.syncXhr(INACTIVATION_URL, json,{type: "POST"});
	        	if(_d.isSuccess){
	        		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISABLE_SUCCESS"));
	        		refreshData();
	        	}
	        	else{   
	        		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CANCEL_FAIL"));
	        	}
	            	
	        });
	    });

	    refreshData();
		
}); 