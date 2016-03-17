$j.extend(loxia.regional['zh-CN'],{  
	    "LABEL_PRODUCT_OPERATE" :"操作",
	    "INFO_TITLE_DATA"		:"提示信息",
	    "INFO_ENABLE_SUCCESS"   :"启用成功",
	    "INFO_DISABLE_SUCCESS"  :"禁用成功", 
	    "INFO_ENABLE_FAIL"      :"启用失败",
	    "INFO_DISABLE_FAIL"     :"禁用失败",
	    "TIP_FORM_CHECK_ERROR"  :"错误信息",
	    "CSFILTE_RENABLE_TEST_ERRORMSG":"测试未通过,无法启用:{0}",
	    "CSFILTE_INITIATIVE_TEST_ERRORMSG":"主动更新失败:{0}"
});

//自定义筛选搜索
var csfilterListUrl = base+'/rule/customizeFilterList.json'; 
  
var enableOrDisableCustomizeFilterByIdsUrl = base+'/rule/enableOrDisableCustomizeFilterByIds.json';

var csfilterCreateUrl =base+'/rule/createCustomFilter.htm';

var csFilterUpdateUrl =base +'/rule/updateCustomFilter.htm';

var STATE_ENABLED =1;
var STATE_DISABLED =0;

var resultForTestCustomFilter = base +"/rule/validateCustomizeFilter.json";

var initiativeCustomFilterURL =base +"/rule/initiativeCustomFilter.json";

function drawEditor(data, args, idx){
	var result="";  
	var id=loxia.getObject("id", data);
	 var state=loxia.getObject("lifecycle", data);
	 var modify="<a href='javascript:void(0);' val='"+id+"' class='func-button modify'>"+"修改"+"</a>";
	 if(state==0){
		  result+=modify+"<a href='javascript:void(0);' val='"+id+"' class='func-button enable'>"+"启用"+"</a>";
	  }else if(state==1){ 
		  result+="<a href='javascript:void(0);' val='"+id+"' class='func-button disable'>"+"禁用"+"</a>";
		  result +="<a href='javascript:void(0);' val='"+id+"' class='func-button initiative'>"+"主动更新"+"</a>";
	 }
	 //result +="<a href='javascript:void(0);' val='"+id+"' class='func-button initiative'>"+"主动更新"+"</a>";
	return result;
} 

//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
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
	
	searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});

	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [
		 {
			name : "id",
			label : "编号",
			width : "10%" 
			
		}, {
			name : "scopeName",
			label : "名称",
			width : "20%" ,
		}, {
			name : "scopeTypeName",
			label : "自定义类型",
			width : "15%" 
		}, {
			name : "serviceName",
			label : "服务名",
			width : "20%" 
		}, 
		{
			name : "lifecycle",
			label : "状态",
			width : "15%",
			type : "yesno"
		}, {
			name : nps.i18n("LABEL_PRODUCT_OPERATE"),
			label : nps.i18n("LABEL_PRODUCT_OPERATE"),
			width : "20%", 			 
			template : "drawEditor"
		} ],
		dataurl : csfilterListUrl
	});
	refreshData();
	
	 
	//筛选数据
	$j(".func-button.search").click(function(){
		$j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		refreshData();
	}); 
	
 	$j(".button.orange.addcsfiltercls").click(function(){
	   window.location.href=csfilterCreateUrl;
	});
 	 
	 // 禁用单行
	$j("#table1").on("click",".func-button.disable",function(){
	    var curObject=$j(this);
	    nps.confirm("确认信息","确认禁用?", function(){
	
	        var json={"ids":curObject.attr("val"),"state":STATE_DISABLED};
	
	    	var _d = nps.syncXhr(enableOrDisableCustomizeFilterByIdsUrl, json,{type: "GET"});
	    	if(_d.isSuccess){
	    		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISABLE_SUCCESS")); 
	    		  refreshData(); 
	    	}
	    	else{
	    		nps.info(nps.i18n("TIP_FORM_CHECK_ERROR"),nps.i18n("INFO_DISABLE_FAIL"));
	    	}
	        	
	    });
	});

		    
	 // 启用单行
   $j("#table1").on("click",".func-button.enable",function(){
	   var curObject=$j(this);
	   
	   nps.confirm("确认信息","确认启用?", function(){
		   
		   	
	        var json={"ids":curObject.attr("val"),"state":STATE_ENABLED};
	
	    	var _d = nps.syncXhr(enableOrDisableCustomizeFilterByIdsUrl, json,{type: "GET"});
	    	if(_d.isSuccess){
	    		var testRlt =loxia.syncXhr(resultForTestCustomFilter, {"id":curObject.attr("val")},{type:"GET"});
	    		
    		   if(!testRlt.isSuccess){
    			    var disabled_json={"ids":curObject.attr("val"),"state":STATE_DISABLED};
    				
    		    	nps.syncXhr(enableOrDisableCustomizeFilterByIdsUrl, disabled_json,{type: "GET"});
    				return nps.info(nps.i18n("TIP_FORM_CHECK_ERROR"),nps.i18n("CSFILTE_RENABLE_TEST_ERRORMSG",[testRlt.description]));
    		   }
	    		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ENABLE_SUCCESS")); 
	    		refreshData(); 
	    	}
	    	else{
	    		nps.info(nps.i18n("TIP_FORM_CHECK_ERROR"),nps.i18n("INFO_ENABLE_FAIL"));
	    	}
	        	
	    });
    });
		   
	//修改	
    $j("#table1").on("click",".func-button.modify",function(){
        var curObject=$j(this);
        var id = curObject.attr("val");
        var url =csFilterUpdateUrl+"?id="+id;
    	window.location.href=url;
    });
    
    //主动更新
    // 只有启用状态下的才能主动更新
    $j("#table1").on("click",".func-button.initiative",function(){
        var json={"id":$j(this).attr("val")};
        var testRlt =loxia.syncXhr(resultForTestCustomFilter, json,{type:"GET"});
        if(testRlt){
        	var result = nps.syncXhr(initiativeCustomFilterURL, json,{type: "GET"});
        	if(result.isSuccess){
        		nps.info(nps.i18n("INFO_TITLE_DATA"),"主动更新成功!");
        	}else{
        		nps.info(nps.i18n("TIP_FORM_CHECK_ERROR"),nps.i18n("CSFILTE_INITIATIVE_TEST_ERRORMSG",[testRlt.description]));
        	}
        }else{
        	nps.info(nps.i18n("TIP_FORM_CHECK_ERROR"),nps.i18n("CSFILTE_INITIATIVE_TEST_ERRORMSG",[testRlt.description]));
        }
    });
});

