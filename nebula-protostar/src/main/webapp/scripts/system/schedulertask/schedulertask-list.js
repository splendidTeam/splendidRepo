$j.extend(loxia.regional['zh-CN'],{  
	    "LABEL_NAME":"任务编码",
	    "LABEL_DESC":"任务描述", 
	    "LABEL_BEAN_NAME":"Bean Name",	   
	    "LABEL_METHOD_NAME":"方法名称", 
	    "LABEL_TIME_EXP":"时间表达式",
	    "LABEL_STATE":"状态",
	    "LABEL_CONFIG":"操作",
	    "TO_UPDATE":"修改",
	    "TO_ENABLE":"启用",
	    "TO_DELETE":"删除",
	    "PRODUCT_INFO_CONFIRM":"确认信息",
	    "PRODUCT_CONFIRM_DISABLE_USER":"确认使任务失效",
	    "PRODUCT_CONFIRM_ENABLE_USER":"确认启用任务", 
	    "INFO_TITLE_DATA":"提示信息",
	    "INFO_ENABLE_SUCCESS":"启用成功",
	    "INFO_DISABLE_SUCCESS":"失效成功", 
	    "INFO_ENABLE_FAIL":"启用失败",
	    "INFO_DISABLE_FAIL":"失效失败",
	    "USER_FORM_CHECK_ERROR":"错误信息",
	    "NO_CATEGORY":"无",  
	    "NO_DATA":"无数据",
	    "DRAW_FIND":"查看",
	    "DRAW_NO":"失效",
	 	"INFO_SYSTEM_ERROR":"系统错误" ,
	 	"PRODUCT_CONFIRM_DELETE":"确认删除",
	 	"INFO_DELETE_SUCCESS":"删除成功",
	 	"ITEM_CATEGORY_TIPS":"错误信息"
});
//鼠标移动隐藏时间
function categoryHideMenu() {
	$j("#categoryMenuContent").fadeOut("fast");
	$j("body").unbind("mousedown", categoryOnBodyDown);
}
//商品组合查询
var searchConditionUrl = base+'/system/schedulerTask/schedulerTaskList.json';
  
var endisableSearchConditionUrl= base+'/system/schedulerTask/enableSchedulerTaskByIds.json';
var unendisableSearchConditionUrl = base+'/system/schedulerTask/unEnableSchedulerTaskByIds.json';
var removeSearchConditionUrl= base+'/system/schedulerTask/deleteSchedulerTaskByIds.json';
var removeSearchConditionsUrl= base+'/system/schedulerTask/deleteSchedulerTaskByIds.json';

var comboProductActionURl=base+'/product/productcomboedit.htm';
var industryListUrl=base+"/item/itemSearchCondition/findIndustryByCategoryId.json";
var propertyListUrl=base+"/item/itemSearchCondition/findPropertyByIndustryId.json";
var addConditionUrl="/system/schedulerTask/toSaveOrUpdateSchedulerTask.htm";
var settingConditionUrl="/item/itemSearchCondition/managerSetting.htm";
//查看
var VIEW_URL=base+'/product/combo/view.json';


function drawPropertyName(data, args, idx){
	var result=""; 
	var name=loxia.getObject("propertyName", data);
	var iname=loxia.getObject("industryName", data);
	if(name!=null&&name!=""){
		return iname+" > "+name;
	}
	return "";
}

/**
 * 批量删除
 */
function confirmDelete(){
	var checkbox=$j("input[name='id']");
	var data=""; 
	  $j.each(checkbox, function(i,val){   
		  if(val.checked){
			  data=data+$j(this).val()+",";
		  }
	 }); 
	  if(data!=""){
		  data=data.substr(0,data.length-1);
		  
		
			nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_DELETE"),function(){
				
					var json={"ids":data}; 
				  	 nps.asyncXhrPost(removeSearchConditionsUrl, json,{successHandler:function(data, textStatus){
						var backWarnEntity = data;
		  				if (backWarnEntity.isSuccess) {
		  					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
		  					refreshData();
		  				} else {
		  					nps.info(nps.i18n("INFO_TITLE_DATA"),backWarnEntity.description);
		  				}
					 }});
			});
	
	  }  
}

function drawEditor(data, args, idx){
	var result="";  
	var id=loxia.getObject("id", data);
	var state=loxia.getObject("lifecycle", data);
	var modify="<a href='"+ addConditionUrl+"?id="+id+"' class='func-button modify'>"+nps.i18n("TO_UPDATE")+"</a>";
	var to_delete="<a href='javascript:void(0);' data='" + id +"' class='func-button delete'>"+nps.i18n("TO_DELETE")+"</a>";
	if(state==0){
		result+=modify+to_delete+"<a href='javascript:void(0);' val='"+id+"' class='func-button enable'>"+nps.i18n("TO_ENABLE")+"</a>";
	}else if(state=1){ 
		result+=modify+to_delete+"<a href='javascript:void(0);' val='"+id+"' class='func-button disable'>"+nps.i18n("DRAW_NO")+"</a>";
	} 
	return result;
} 


//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}

function drawCheckbox(data, args, idx){
	var result=""; 
	var state=loxia.getObject("lifecycle", data);
	if(state==1 ){
		return "<input name='limitValue' type='checkbox' class='limitValue'  checked='false'  value='' disabled='disabled'/>";
	}
	return "<input name='limitValue' type='checkbox' class='limitValue'  value='' disabled='disabled'/>";
	 
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

function checkboxs(data, args, idx){
	return "<input type='checkbox' name='id' class='chkcid'  value='" + loxia.getObject("id", data)+"'/>";
}


$j(document).ready(function() {
	
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();

	searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});
	
	//通过loxiasimpletable动态获取数据
	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form : "searchForm",
		cols : [
		{label:"<input type='checkbox'/>",width:"5%", template:"checkboxs"},
		{
			name : "code",
			label : nps.i18n("LABEL_NAME"),
			width : "10%"
		}, {
			name : "description",
			label : nps.i18n("LABEL_DESC"),
			width : "10%" 
		},{
			name : "beanName",
			label : nps.i18n("LABEL_BEAN_NAME"),
			width : "10%" 
		}, {
			name : "methodName",
			label : nps.i18n("LABEL_METHOD_NAME"),
			width : "15%" 
		},{
			name : "timeExp",
			label : nps.i18n("LABEL_TIME_EXP"),
			width : "15%" 
		},{
			name : "lifecycle",
			label : nps.i18n("LABEL_STATE"),
			width : "10%",
			type:"yesno"
		},{
			label : nps.i18n("LABEL_CONFIG"),
			width : "15%", 			 
			template : "drawEditor"
		}
		
		],
		dataurl : searchConditionUrl
	});
	
	
	//筛选数据
	 $j(".func-button.search").click(function(){
			 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
			 refreshData();
	   }); 
	
 	 $j(".button.orange.addCondition").click(function(){
	        window.location.href=addConditionUrl;
	 });
 	
		   // 禁用单行
		   $j("#table1").on("click",".func-button.disable",function(){
		        var curObject=$j(this);
		        nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_DISABLE_USER"), function(){

		            var json={"ids":curObject.attr("val")};

		        	nps.syncXhr(unendisableSearchConditionUrl, json,{type: "POST"});
		        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISABLE_SUCCESS")); 
		        	refreshData();
		            	
		        });
		    });

		    
	  // 启用单行
		   $j("#table1").on("click",".func-button.enable",function(){
		        var curObject=$j(this);
		        nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_ENABLE_USER"), function(){

		            var json={"ids":curObject.attr("val")};

		        	nps.syncXhr(endisableSearchConditionUrl, json,{type: "POST"});
		        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ENABLE_SUCCESS")); 
		        	refreshData(); 
		            	
		        });
		    });
		   
		   //删除一行
		   $j("#table1").on("click",".func-button.delete",function(){
		        var curObject=$j(this);
		        nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_DELETE"), function(){

		            var json={"ids":curObject.attr("data")};

		        	nps.syncXhr(removeSearchConditionUrl, json,{type: "POST"});
		        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS")); 
		        	refreshData(); 
		            	
		        });
		    });
		   
		   //多行删除
		   $j("#deleteAll").click(function(){
			   confirmDelete();
		    });
		   
		   refreshData();  
		    	
		   //-------------------------------------------------移动弹出表达式名称--------------------------------
			var ssetim=0;
			$j("<div class='moveoutdialog' style='display:none; clear:both; position:absolute; width:500px; height:auto; padding:20px; background:#fff; border:1px solid #888888; line-height:1.5; z-index:50; border-radius:5px;'></div>").appendTo("body").mouseenter(function(){
				clearTimeout(ssetim);
			}).mouseleave(function(){
				ssetim=setTimeout(function(){$j(".moveoutdialog").stop(true,true).fadeOut(100);},500);
			});
		
			$j(".ui-block").on("mouseenter",".movetitle",function(e){
				e.stopPropagation();
				clearTimeout(ssetim);
				var ex=parseInt($j(this).parent("label").offset().left);
				var ey=parseInt($j(this).parent("label").offset().top);
				
				var thiscust=$j(this).attr("custtalk"); 
				$j(".moveoutdialog").stop(true,true).fadeOut(100).html("").append($j(this).attr("custtalk"));

				$j(".moveoutdialog").css({"left":ex,"top":ey+60}).stop(true,true).fadeIn(300);
			}).on("mouseleave",".movetitle",function(e){
				ssetim=setTimeout(function(){$j(".moveoutdialog").stop(true,true).fadeOut(100);},500);
			});
			
			
			
			//refreshData();
			
});

