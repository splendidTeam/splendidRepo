$j.extend(loxia.regional['zh-CN'],{
	"LABEL_PROMOTION_PROMOTIONNAME":"活动名称",
	 "LABEL_PROMOTION_STARTTIME":"开始时间",
	    "LABEL_PROMOTION_ENDTIME":"结束时间",
	    "LABEL_PROMOTION_AUDIENCEVALUE":"受益人群",
	    "LABEL_PROMOTION_EFFECTMARK":"参与计算",
	    "LABEL_PROMOTION_TYPE":"状态",
	    "LABEL_PROMOTION_LIMITVALUE":"限购",
	    "LABEL_PROMOTION_GROUP":"优惠项",
	    "LABEL_PROMOTION_UPDATETIME":"发布时间",
	    "LABEL_PROMOTION_UPDATE":"发布人员", 
	    "LABEL_PROMOTION_SCOPE":"商品范围",
	    "LABEL_MEMBER_OPERATE":"操作",	    
	    "LABEL_PROMOTION_WAT":"待启用",
	    "LABEL_PROMOTION_STARTUSERFUL":"已启用",
	    "LABEL_PROMOTION_USEFUL":"已生效",
	    "LABEL_PROMOTION_CANCEL":"已取消",  
	    "LABEL_PROMOTION_LOGOTYPE":"角标",
	    "INFO_SELECT_EFFECTMARK":"请选择活动！参与计算必须是大于2个活动才可以保存！", 	    
	    "INFO_NO_NAME":"请输入！调整名称",
	    "TO_UPDATE_UPSWAP":"上移",
	    "TO_UODATE_DOWNSWAP":"下移",
	    "TO_CANCEL":"取消启用",
	    "TO_DRAFT_COPY":"复制草稿", 
	    "INFO_TITLE_DATA":"提示信息",
	    "INFO_STOP_FAIL":"取消启用失败!",
	    "INFO_SAVE_FAIL":"保存失败" ,
	    "NO_CATEGORY":"无",  
	    "USER_INFO_CONFIRM":"确认信息",
	    "NO_DATA":"无数据", 
	    "INFO_TITLE_DATA":"提示信息",
	    "INFO_UPDATE_SUCCESS":"修改成功", 
	    "INFO_UPSWAP_END":"当前活动已是第一条", 
	    "INFO_COPY_SUCCESS":"启用成功" 
});

// 获取重叠的活动
var promotionAdjustUpdatepageUrl = base+'/promotion/promotionAdjustUpdatepage.json'; 
// 保存调整信息
var savePriorityAdjustUrl=base+'/promotion/savePriorityAdjust.json'; 
 
  
function comboTypeCk(data){
	var result=""; 
	var type=loxia.getObject("memComboType", data);	  
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
	var state=loxia.getObject("lifecycle",data);
	var id=loxia.getObject("promotionId", data);
	var radio="<input  type='radio' disabled='true' />";
	var upSwap="<a href='javascript:void(0);' val='"+id+"' class='func-button upSwap'>"+nps.i18n("TO_UPDATE_UPSWAP")+"</a>";
	var downSwap="<a href='javascript:void(0);'  val='"+id+"' class='func-button downSwap'>"+nps.i18n("TO_UODATE_DOWNSWAP")+"</a>";  
	result+=radio+upSwap+downSwap;
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
//获取id
function drawCheckbox(data, args, idx){
	return "<input name='promotionId' type='checkbox'   value='" + loxia.getObject("promotionId", data)+"'/>";
}

function drawSettingtype(data){
	var result="无"; 
	var type=loxia.getObject("logo", data);	  
	if(type !=null){
		if(type==1) 
			result="减";
		if(type==2) 
			result="折";
		if(type==3) 
			result="免运费";
		if(type==4) 
			result="优惠价"; 
		if(type==5) 
			result="赠品"; 
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

	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [
		{
			name : "promotionName",
			label : nps.i18n("LABEL_PROMOTION_PROMOTIONNAME"),
			width : "17%" 
			
		},{
			name : "startTime",
			label : nps.i18n("LABEL_PROMOTION_STARTTIME"),
			width : "10%",
		    formatter:"formatDate",
		    sort:["h.START_TIME asc","h.START_TIME desc"	]
		}, {
			name : "endTime",
			label : nps.i18n("LABEL_PROMOTION_ENDTIME"),
			width : "10%",
			formatter:"formatDate",
			sort:["h.END_TIME asc","h.END_TIME desc"]
		}, {
			name : "memComboName",
			label : nps.i18n("LABEL_PROMOTION_AUDIENCEVALUE"),
			width : "7%"  
		}, 
		{
			name : "scopeName",
			label : nps.i18n("LABEL_PROMOTION_SCOPE"),
			width : "10%"  
			
		}, {
			name : "lifecycle",
			label : nps.i18n("LABEL_PROMOTION_TYPE"),
			width : "7%" , 
			template:"statusLifeycle" 
			
		},   {
			name : "logo",
			label : nps.i18n("LABEL_PROMOTION_LOGOTYPE"),
			emplate:"drawSettingtype",
			width : "3%"  
		}, {
			name : "publishTime",
			label : nps.i18n("LABEL_PROMOTION_UPDATETIME"),
			width : "9%" ,
			formatter:"formatDate",
			sort:["h.LAST_UPDATE_TIME asc","h.LAST_UPDATE_TIME desc"]
		},{
			name : "realname",
			label : nps.i18n("LABEL_PROMOTION_UPDATE"),
			width : "7%"  
		},{ 
			label : nps.i18n("LABEL_PROMOTION_EFFECTMARK"),
			width : "5%"  ,
			template :"drawCheckbox"
		}, {
			name : "操作",
			label : nps.i18n("LABEL_MEMBER_OPERATE"),
			width : "10%", 			 
			template : "drawEditor"
		} ],
		dataurl : promotionAdjustUpdatepageUrl
	});
	refreshData();  
	    
	 // 重置信息
    $j(".button.refresh").click(function(){
	    refreshData();
      }); 
	 
    $j(".button.orange.updatePromotion").click(function(){   
		  var moduleObjs = [];  
		  $j("#table1 tbody tr").each(function(i, dom){
			  //获得参与计算活动
				var checkValue= $j(dom).find("input[type='checkbox']").attr("checked"); 
				if(checkValue!=undefined){ 
					var moduleObj = {}; 
					 var name =$j(dom).find(".col-0 span").attr("title");
					 var promotionId =$j(dom).find(".upSwap").attr("val"); 
					 moduleObj.name=name; 
					 moduleObj.promotionId=promotionId;
					 moduleObjs.push(moduleObj);
				} 
			})
		//必填项验证		
		if(moduleObjs.length<2){
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SELECT_EFFECTMARK"));
			 return;
		} 
		//必填项验证	
		var adjustname=$j("#adjustname").val();
		var startTime=$j("#startTime").val();
		var endTime=$j("#endTime").val();  
		if (adjustname.length == 0) {
			 $j("#adjustname").blur().focus();
			 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_NAME"));
			 return;
		}
		var tableJson= $j.toJSON(moduleObjs);  
		var json = {  "adjustname":adjustname,
				      "startTime":startTime,
				      "endTime":endTime,
					  "priorityJson":tableJson 
						 }  
			 
		var data = nps.syncXhrPost(savePriorityAdjustUrl,json);
			 if (data.isSuccess) {
			    nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_UPDATE_SUCCESS"));
			    refreshData();
			   return;
			 }else{
				 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SAVE_FAIL"));
			 }
	 });
	 
 // 模块向下模块交换位置
	 $j("#table1").on("click",".func-button.downSwap",function(){  
		   var className=$j(this).parents(".col-10").parents().attr("class");
	       var curObject=$j(this).parents(".col-10").parents("."+className+"");  
	       var s=curObject.next().after(curObject); 
	       $j("#table1 tbody tr").each(function(i,dom){
	        	$j(dom).find("input[type='radio']").attr("checked",false); 
	        }); 
	       $j(this).siblings('input[type="radio"]').attr("checked","checked");
	    });  
	// 模块向上模块交换位置
	 $j("#table1").on("click",".func-button.upSwap",function(){
		 var className=$j(this).parents(".col-10").parents().attr("class");
	        var curObject=$j(this).parents(".col-10").parents("."+className+""); 
	        if (curObject.prevAll().size() <1) { 
	    		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_UPSWAP_END"));
	    		return;
	    	}
	        curObject.prev().before(curObject);   
	        $j("#table1 tbody tr").each(function(i,dom){
	        	$j(dom).find("input[type='radio']").attr("checked",false); 
	        }); 
			$j(this).siblings('input[type="radio"]').attr("checked","checked");
	  });  
	 
	 
});   


