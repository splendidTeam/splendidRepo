$j.extend(loxia.regional['zh-CN'],{  
    	"LABEL_CODE":"商品编码",
	    "LABEL_NAME":"商品名称",
	    "LABEL_CATEGORY":"分类", 
	    "LABEL_INDURSTY":"行业",
	    "LABEL_CONFIG":"操作",
	    "TO_UPDATE":"更新",
	    "TO_ENABLE":"启用",
	    "TO_DELETE":"删除",
	    "TO_SETTING":"设置选项",
	    "PRODUCT_INFO_CONFIRM":"确认信息",
	    "PRODUCT_CONFIRM_DISABLE_USER":"确认使选项失效",
	    "PRODUCT_CONFIRM_ENABLE_USER":"确认启用选项",  
	    "INFO_TITLE_DATA":"提示信息",
	    "INFO_ENABLE_SUCCESS":"启用成功",
	    "INFO_DISABLE_SUCCESS":"失效成功", 
	    "INFO_ENABLE_FAIL":"启用失败",
	    "INFO_DISABLE_FAIL":"失效失败",
	    "USER_FORM_CHECK_ERROR":"错误信息",
	    "NO_CATEGORY":"无",  
	    "NO_DATA":"无数据",
	    "DRAW_NO":"失效",
	    "INFO_COMBO_INEXISTED":"该筛选器不存在" ,
	 	"INFO_SYSTEM_ERROR":"系统错误" ,
		"PRODUCT_CONFIRM_DELETE":"确认删除",
		"PRODUCT_CONFIRM_UPDATE":"确认更新",
		"INFO_DELETE_SUCCESS":"删除成功",
		"INFO_UPDATE_SUCCESS":"更新成功",
		"INFO_DELETE_NOT_SUCCESS":"服务器忙，删除失败",
		"INFO_UPDATE_NOT_SUCCESS":"服务器忙，更新失败",
		"INFO_NOT_SUCCESS":"之前的请求还未完成，请稍后再试",
		"INFO_ITEM_CODE_IS_NOT_NULL":"商品编码不可以为空",
		"CONFIRM_UPDATA_ITEM_SOLR_INDEX":"确定更新这些商品的索引?",
		"CONFIRM_DETELE_ITEM_SOLR_INDEX":"确定删除这些商品的索引?"
});

//商品组合查询
var changeSelectUrl = base+'/item/itemSolrSetting/changeByIds.json'; 
var changeAllUrl=base+'/item/itemSolrSetting/changeAll.json';

var searchConditionUrl=base+'/item/itemSolrSetting/searchItemList.json';
// 批量操作中的批量删除
var batchDeteleItemSolrIndexUrl = base + "/item/solr/index/batchDeteleItemSolrIndex.json";
// 批量操作中的批量更新
var batchUpdateItemSolrIndexUrl = base + "/item/solr/index/batchUpdateItemSolrIndex.json";

var canDo = true;
//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}

function drawEditor(data, args, idx){
	var result="";  
	var id=loxia.getObject("id", data);
	var modify="<a href='javascript:void(0);' data='" + id +"' class='func-button modify'>"+nps.i18n("TO_UPDATE")+"</a>";
	var to_delete="<a href='javascript:void(0);' data='" + id +"' class='func-button delete'>"+nps.i18n("TO_DELETE")+"</a>";
	
	result+=modify+to_delete;
	return result;
} 


function checkboxs(data, args, idx){
	return "<input type='checkbox' name='id' class='chkcid'  value='" + loxia.getObject("id", data)+"'/>";
}

function confirmChange(type){
	var checkbox=$j("input[name='id']");
	var data=""; 
	  $j.each(checkbox, function(i,val){   
		  if(val.checked){
			  data=data+$j(this).val()+",";
		  }
	 }); 
	  if(data!=""){
		  if(type=="delete"){
			  data=data.substr(0,data.length-1);
		  
		
				nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_DELETE"),function(){
					
						var json={"ids":data,"type":type}; 
					  	 nps.asyncXhrPost(changeSelectUrl, json,{successHandler:function(data, textStatus){
							var backWarnEntity = data;
			  				if (backWarnEntity.isSuccess) {
			  					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
			  					refreshData();
			  				} else {
			  					nps.info(nps.i18n("INFO_TITLE_DATA"),backWarnEntity.description);
			  				}
						 }});
				});
		  }else if(type=="update"){
			  data=data.substr(0,data.length-1);
			  
				
				nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_UPDATE"),function(){
					
						var json={"ids":data,"type":type}; 
					  	 nps.asyncXhrPost(changeSelectUrl, json,{successHandler:function(data, textStatus){
							var backWarnEntity = data;
			  				if (backWarnEntity.isSuccess) {
			  					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_UPDATE_SUCCESS"));
			  					refreshData();
			  				} else {
			  					nps.info(nps.i18n("INFO_TITLE_DATA"),backWarnEntity.description);
			  				}
						 }});
				});
		  }
	  }  
}

function confirmChangeAll(type){
		if(canDo){
		  if(type=="delete"){
		  
		
				nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_DELETE"),function(){
					
						var json={"type":type}; 
					  	 nps.asyncXhrPost(changeAllUrl, json,{successHandler:function(data, textStatus){
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
		  else if(type=="update"){
			  
				
				nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_UPDATE"),function(){
					
						var json={"type":type}; 
					  	 nps.asyncXhrPost(changeAllUrl, json,{successHandler:function(data, textStatus){
							var backWarnEntity = data;
			  				if (backWarnEntity.isSuccess) {
			  					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_UPDATE_SUCCESS"));
			  					refreshData();
			  				} else {
			  					nps.info(nps.i18n("INFO_TITLE_DATA"),backWarnEntity.description);
			  				}
						 }});
				});
		  }
		}else{
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NOT_SUCCESS"));
		}
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
			label : nps.i18n("LABEL_CODE"),
			width : "15%"
		}, {
			name : "title",
			label : nps.i18n("LABEL_NAME"),
			width : "15%"
		}, {
			name : "categoryNames",
			label : nps.i18n("LABEL_CATEGORY"),
			width : "15%" 
		},  {
			name : "industryName",
			label : nps.i18n("LABEL_INDURSTY"),
			width : "15%"
		},	{
			label : nps.i18n("LABEL_CONFIG"),
			width : "15%", 			 
			template : "drawEditor"
		}
		
		],
		dataurl : searchConditionUrl
	});

		    
	// 更新单行
   $j("#table1").on("click",".func-button.modify",function(){
        var curObject=$j(this);
        nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_UPDATE"), function(){

            var json={"ids":curObject.attr("data"),"type":"update"};

        	var _d = nps.asyncXhrPost(changeSelectUrl, json,{successHandler:function(data, textStatus){
        		var backWarnEntity = data;
  				if (backWarnEntity.isSuccess) {
  					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_UPDATE_SUCCESS"));
  					refreshData();
  				} else {
  					nps.info(nps.i18n("INFO_TITLE_DATA"),backWarnEntity.description);
  				}
			 }});
        	
        });
    });
   
 //删除一行
   $j("#table1").on("click",".func-button.delete",function(){
        var curObject=$j(this);
        nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_DELETE"), function(){

            var json={"ids":curObject.attr("data"),"type":"delete"};

            var _d = nps.asyncXhrPost(changeSelectUrl, json,{successHandler:function(data, textStatus){
        		var backWarnEntity = data;
  				if (backWarnEntity.isSuccess) {
  					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
  					refreshData();
  				} else {
  					nps.info(nps.i18n("INFO_TITLE_DATA"),backWarnEntity.description);
  				}
			 }});
        	 
            	
        });
    });
   
   //选择
   $j("input[effect='changeSelect']").click(function(){
	   confirmChange($j(this).attr("methodType"));
    });
   
   //全部
   $j("input[effect='changeAll']").click(function(){
	   confirmChangeAll($j(this).attr("methodType"));
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
	

	refreshData();
	
	$j("#search").click(function(){
		refreshData();
	});
	
	/*********************************批量操作弹层********************************************/
	// 批量操作
	$j('#batchOeration').click(function(){
		$j("#itemCodes").val("");
		$j('#batch-operation-dialog').dialogff({type:'open',close:'in',width:'600px',height:'300px'});
	});
	
	$j('#cancel-dialog').click(function(){
		$j("#batch-operation-dialog").dialogff({type:'close'});
	});
	
	// 批量更新
	$j("#batch-update").click(function(){
		var itemCodes = $j("#itemCodes").val();
		if(!itemCodes){
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_ITEM_CODE_IS_NOT_NULL"));
			return;
		}
		
		var param = {"itemCodes":itemCodes};
		nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"), nps.i18n("CONFIRM_UPDATA_ITEM_SOLR_INDEX"), function(){
			nps.asyncXhrPost(batchUpdateItemSolrIndexUrl, param, {
				successHandler:function(data){
					if(data.isSuccess){
	  					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_UPDATE_SUCCESS"));
	  					refreshData();
					}
				}
			});
		});
	});

	// 批量删除
	$j("#batch-detele").click(function(){
		var itemCodes = $j("#itemCodes").val();
		if(!itemCodes){
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_ITEM_CODE_IS_NOT_NULL"));
			return;
		}
		
		var param = {"itemCodes":itemCodes};
		nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"), nps.i18n("CONFIRM_DETELE_ITEM_SOLR_INDEX"), function(){
			nps.asyncXhrPost(batchDeteleItemSolrIndexUrl, param, {
				successHandler:function(data){
					if(data.isSuccess){
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
	  					refreshData();
					}
				}
			});
		});
		
	});
	
});

