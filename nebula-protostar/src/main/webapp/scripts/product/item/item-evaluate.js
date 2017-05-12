$j.extend(loxia.regional['zh-CN'],{
	"SYSTEM_ITEM_RATE_MESSAGE":"提示信息",
	"TIP_OF_PASS":"您确认要通过该条评论？",
	"TIP_OF_DELETE":"您确认要删除该条评论？",
	"PASS_SUCCESS":"该评论已通过",
	"DELETE_SUCCESS":"该评论已被删除",
	"REPLY_SUCCESS":"回复成功",
	"ITEM_RATE_TIP_NOSEL":"请选择要操作的评论",
	"TIP_OF_BATCH_PASS":"您确定要通过所选评论？",
	"TIP_OF_BATCH_DELETE":"您确定要删除所选评论？",
	"OPERATE_SUCCESS":"操作成功",
	"OPERATE_FAILURE":"操作失败",
	"REPLY_MUSTBE_NOTNULL":"请填写回复内容"
    
});
//获取评论列表
var itemEvaluateListUrl = base+'/product/itemEvaluateList.json';
//单条通过
var enableRateByIdUrl = base+'/product/enableRateById.json';
//单条删除
var disableRateByIdUrl = base+'/product/disableRateById.json';
//回复
var replyItemEvaluateUrl = base+ '/product/replyItemEvaluate.Json';
//批量删除
var disableRateByIdsUrl = base+ '/product/disableRateByIds.json';
//批量通过
var enableRateByIdsUrl = base+ '/product/enableRateByIds.json';

var itemRates=new Object();
$j(document).ready(function() {
		loxia.init({
			debug : true,
			region : 'zh-CN'
		});
		nps.init();
		
	    /**
	     * 已处理列表
	     */
		$j("#table1").loxiasimpletable({
			page : true,
			size : 10,
			nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
			form:"evaToDoSearchForm",
			cols : [ 
				{label : "<input type='checkbox'/>",template : "drawCheckbox" },
				{label:"商品",width:"15%",template:"drawItemInfo"},
				{name:"score",label:"商品满意度",width:"15%",template:"drawEvaInfoScore"},
				{name:"score2",label:"配送/施工满意度",width:"15%",template:"drawEvaInfoScore2"},
				{name:"createTime",label:"评价时间",width:"15%",template:"drawEvaInfoCreateTime"},
   			    {name:"content",label:"评价内容",width:"15%",template:"drawEvaInfoContent"},
//				{name:"evaluate",label:"评价",width:"60%",template:"drawEvaInfo"},
				{name:"loginName",label:"会员",width:"10%"},
				{label:"操作",width:"15%", align:"left", template:"drawEditTodo"}
			  ],
		    dataurl : itemEvaluateListUrl
		});
		
		refreshData1();
		//筛选
		$j(".func-button.evaToDoSearch").click(function(){
			 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
			 refreshData1();
		});
		
		/**
		 * 未处理列表
		 */
		$j("#table2").loxiasimpletable({
			page : true,
			size : 15,
			nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
			form:"evaDoneSearchForm",
			cols : [ 
	            {label : "<input type='checkbox'/>",template : "drawCheckbox" },
				{label:"商品",width:"15%",template:"drawItemInfo"},
				{name:"score",label:"商品满意度",width:"15%",template:"drawEvaInfoScore"},
				{name:"score2",label:"配送/施工满意度",width:"15%",template:"drawEvaInfoScore2"},
				{name:"createTime",label:"评价时间",width:"15%",template:"drawEvaInfoCreateTime"},
   			    {name:"content",label:"评价内容",width:"15%",template:"drawEvaInfoContent"},
//				{name:"evaluate",label:"评价",width:"60%",template:"drawEvaInfo"},
				{name:"loginName",label:"会员",width:"10%"},
				{label:"操作",width:"15%", align:"left", template:"drawEditDone"}
			],
		    dataurl : itemEvaluateListUrl
		});
	
		refreshData2();
		
		//筛选
		$j(".func-button.evaDoneSearch").click(function(){
			 $j("#table2").data().uiLoxiasimpletable.options.currentPage=1;
			 refreshData2();
		});
		
	//---------------------------------------------------------搜索框 评分
	    //待处理评价 搜索评分范围操作 
	    $j("#scoreRange").change(function() {
            if (!$j("#scoreRange").attr("checked")) {
                $j("#minScore").attr("disabled",true);
                $j("#maxScore").attr("disabled",true);
                $j("#score").attr("disabled",false);
                $j("#scoreTd").hide();
            }else if($j("#scoreRange").attr("checked")){
            	$j("#minScore").attr("disabled",false);
            	$j("#maxScore").attr("disabled",false);
            	$j("#score").attr("disabled",true);
            	$j("#scoreTd").show();
            }
        });
	    //已处理评价 搜索评分范围操作 
	    $j("#scoreRangeOfEvaluateDone").change(function() {
            if (!$j("#scoreRangeOfEvaluateDone").attr("checked")) {
                $j("#minScoreOfEvaluateDone").attr("disabled",true);
                $j("#maxScoreOfEvaluateDone").attr("disabled",true);
                $j("#scoreOfEvaluateDone").attr("disabled",false);
                $j("#scoreTdOfEvaluateDone").hide();
            }else if($j("#scoreRangeOfEvaluateDone").attr("checked")){
            	$j("#minScoreOfEvaluateDone").attr("disabled",false);
            	$j("#maxScoreOfEvaluateDone").attr("disabled",false);
            	$j("#scoreOfEvaluateDone").attr("disabled",true);
            	$j("#scoreTdOfEvaluateDone").show();
            }
        });
	    
	    //-------------------------------------------------------------------评价详细，搜索会员 dialog-------------------------
		//  详细弹出	
		$j(".ui-block").on("click",".details",function(){
			var id = $j(this).attr("idVal");
			var itemRate = itemRates[id];
		  	 //评分
			var score = itemRate.score;
			var starStr ="";
			for(var i=0;i<score;i++){
				starStr+="<span class='product-star'></span>";
			}
			//添加--配送施工评分
			var score2 = itemRate.score2;
			var starStr2 ="";
			for(var i=0;i<score2;i++){
				starStr2+="<span class='product-star'></span>";
			}	
			
			//创建时间
			var createTime = formatDate(itemRate.createTime,"yyyy-MM-dd HH:mm:ss");
			//回复时间
			var replyTime = itemRate.replyTime;
			//回复内容
			var reply ="";
			var replyTip ="";
			var isReply =2;
			if(null!=replyTime){
				//最后回复时间
				var lastReplyTime = itemRate.lastReplyTime;
				var newReplyflag ="";
				if(null!=lastReplyTime){
					var lastReplyTime = formatDate(itemRate.lastReplyTime,"yyyy-MM-dd HH:mm:ss");
					newReplyflag='<font style="color:red">(于'+lastReplyTime+'更新)</font>：';
				}
				isReply=1;
				reply =itemRate.reply;
				replyTip = '<div class="ui-block-line">' +
				' <label>上次回复：</label>' +
				' <div class="pt7">' +
				' <p class="yellow">'+reply+'</p>' +
				' </div>' +
				' </div>';
			
				
			}else{
				replyTip ="立即回复";
				reply ="";
			}
			
			//评价内容
			var content = itemRate.content;
			//用户
			var loginName = itemRate.loginName;
			//某某商品
			var itemName = itemRate.itemName;
			
			//添加--评论的图片
			var itemImage = itemRate.img_names;
			var imagename="";
			if(itemImage != null && itemImage != undefined && itemImage.indexOf(":") > 0){		
				var splitImagename=itemImage.split(":");
				for(var i=0;i<splitImagename.length;i++){
					imagename += "<img src='"+ imgbase + splitImagename +"' width=60 height=60/>";					
				}								
			}else if(itemImage != ""){	
				imagename += "<img src='" + imgbase + splitImagename +"' width=60 height=60/>";
			}
			
			var detailsHtml ='<div class="ui-block-line">' +
				' <label>商品编码：</label>' +
				' <div class="pt7">' +
				' <a href="#" class="blue">'+itemRate.itemCode+'</a>' +
				' </div>' +
				' </div>' +
				'<div class="ui-block-line">' +
				' <label>商品名称：</label>' +
				' <div class="pt7">' +
				' <a href="#" class="blue">'+itemRate.itemName+'</a>' +
				' </div>' +
				' </div>' +
			
				' <div class="ui-block-line">' +
				' <label>用户名称：</label>' +
				' <div class="pt7 grey">' +
				' <a href="javascript:void(0);">'+loginName+'</a> 于 '+createTime+' 针对<a href="javascript:void(0);"> '+itemName+'</a> 的评价' +
				' </div>' +
				' </div>' +
			
				' <div class="ui-block-line">' +
				' <label>商品满意度：</label>' +
				' <div>' +
				' <span class="product-star-content">'+starStr+'</span>' +
				' </div>' +
				' </div>' +
				
				' <div class="ui-block-line">' +
				' <label>配送施工满意度：</label>' +
				' <div>' +
				' <span class="product-star-content">'+starStr2+'</span>' +
				' </div>' +
				' </div>' +
				
				' <div class="ui-block-line">' +
				' <label>图片：</label>' +
				' <div>' +
				' <p class="mt5"> '+imagename+'</p>' +
				' </div>' +
				' </div>' +
				' <div class="ui-block-line">' +
				' <label>评价内容：</label>' +
				' <div>' +
				' <p class="mt5"> '+content+'</p>' +
				' </div>' +
				' </div>' +replyTip+
				' <div class="ui-block-line bold pt10">' +
				' <label>修改回复：</label>' +
				' <div class="pt7">' +
				' 	&nbsp;' +
				' </div>' +
				' </div>' +
			
				' <form>' +
				'<input type="hidden" id="isReplied" value="'+isReply+'">'+
				'<input type="hidden" id="itemRateId" value="'+id+'">'+
				' <textarea aria-disabled="false" class="ui-loxia-default ui-corner-all mt10" rows="3" cols="30" style="width:100%; height:100px; resize:none" id="itemRateReply">'+reply+'</textarea>'
		
			
				' </form>';
			
			 //dialog 内容
			$j(".details_content").html(detailsHtml);
			
			//dialog 按钮
			var detailsButtonHtml ="";
			if(itemRate.lifecycle==1){
				$j(".button.orange.replyAndPass").hide();
			}else if(itemRate.lifecycle==0){
				$j(".button.orange.replyAndPass").hide();
				$j(".button.orange.reply").hide();
				$j(".button.delete").hide();
			}
			
			$j("#detail-dialog").dialogff({type:'open',close:'in',width:'700px',height:'400px'});
			
		});
		//回复并通过
		$j(".button.orange.replyAndPass").on("click",function(){
			 var itemRateId =$j("#itemRateId").val();
			 var itemRateReply =$j("#itemRateReply").val();
			 var isReply=$j("#isReplied").val();
			 if(itemRateReply.trim()==""){
				 nps.info(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("REPLY_MUSTBE_NOTNULL"));
				 return;
			 }
			 var json={"id":itemRateId,"reply":itemRateReply,"isReply":isReply,"isPass":1};
        	 var backWarnEntity = loxia.syncXhr(replyItemEvaluateUrl, json,{type: "GET"});
        	 if(backWarnEntity.isSuccess){
        		 nps.info(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("REPLY_SUCCESS"));
        	 }
        	 refreshData1();
        	 refreshData2();
			 $j("#detail-dialog").dialogff({type:'close'});
		});
		
		//回复
		$j(".button.orange.reply").on("click",function(){
			 var itemRateId =$j("#itemRateId").val();
			 var itemRateReply =$j("#itemRateReply").val();
			 var isReply=$j("#isReplied").val();
			 if(itemRateReply.trim()==""){
				 nps.info(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("REPLY_MUSTBE_NOTNULL"));
				 return;
			 }
			 var json={"id":itemRateId,"reply":itemRateReply,"isReply":isReply,"isPass":2};
        	 var backWarnEntity = loxia.syncXhr(replyItemEvaluateUrl, json,{type: "GET"});
        	 if(backWarnEntity.isSuccess){
        		 nps.info(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("REPLY_SUCCESS"));
        	 }
        	 refreshData1();
        	 refreshData2();
			 $j("#detail-dialog").dialogff({type:'close'});
		});
		
		$j(".button.delete").on("click",function(){
			var id =$j("#itemRateId").val();
			nps.confirm(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("TIP_OF_PASS"),function(){
				var json={"id":id};
			  	nps.asyncXhrGet(disableRateByIdUrl, json,{successHandler:function(data, textStatus){
			  		var backWarnEntity = data;
			  		if(backWarnEntity.isSuccess){
				  		nps.info(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("DELETE_SUCCESS"));
				  		$j("#detail-dialog").dialogff({type:'close'});
				  	} 
			  		refreshData1();
			  		refreshData2();
			  		
			  	}}); 
			});
			
		});

		
		
	    //-----------------------------------------------------------批量按钮----------------------------------------------------
		$j(".batchPass").on("click", function(){
		    var data="";
		   	$j(".checkId:checked").each(function(i,n){
            	if(i!=0){
                	data+=",";
                	}
            	data+=$j(this).val();
            	
            });
			 if(data==""){
	 	    		nps.info(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("ITEM_RATE_TIP_NOSEL"));
	 	    		return ;
	 	    }
	        nps.confirm(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("TIP_OF_BATCH_PASS"), function(){
	        	var json={"ids":data};
	        	var backWarnEntity = loxia.syncXhr(enableRateByIdsUrl, json,{type: "GET"});
	        	if(backWarnEntity.isSuccess){
	            	nps.info(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("OPERATE_SUCCESS"));
		           	refreshData1();
		        	refreshData2();
	        	}
	        	else{
	            	nps.info(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("OPERATE_FAILURE"));
	        	}
	        });
	    });
		$j(".batchReject").on("click", function(){
			  var data="";
			   	$j(".checkId:checked").each(function(i,n){
	            	if(i!=0){
	                	data+=",";
	                	}
	            	data+=$j(this).val();
	            	
	            });
				 if(data==""){
		 	    		nps.info(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("ITEM_RATE_TIP_NOSEL"));
		 	    		return ;
		 	    }
		        nps.confirm(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("TIP_OF_BATCH_DELETE"), function(){
		        	var json={"ids":data};
		        	var backWarnEntity = loxia.syncXhr(disableRateByIdsUrl, json,{type: "GET"});
		        	if(backWarnEntity.isSuccess){
		            	nps.info(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("OPERATE_SUCCESS"));
		           	    refreshData1();
		        	    refreshData2();
		        	}
		        	else{
		            	nps.info(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("OPERATE_FAILURE"));
		        	}
		        });
	    });
		$j(".button.orange.export").on("click", function(){
	        alert("导出成功");
	    });
		
		
		
		//-------------------------------------------------移动弹出title层--------------------------------
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
			var thisserver=$j(this).attr("servicetalk");
			$j(".moveoutdialog").stop(true,true).fadeOut(100).html("").append($j(this).parent("label").html()).append("<div class='blue pt5'>"+thisserver+"</div>").find(".movetitle").text(thiscust);

			$j(".moveoutdialog").css({"left":ex,"top":ey+60}).stop(true,true).fadeIn(300);
		}).on("mouseleave",".movetitle",function(e){
			ssetim=setTimeout(function(){$j(".moveoutdialog").stop(true,true).fadeOut(100);},500);
		});

		

});

//------------------------------------------------------------------------列表列模板 start----------------------------
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' name='id' class='checkId' value='"+ loxia.getObject("id", data) + "'/>";
}

function drawItemInfo(data, args, idx){
	var itemCode=loxia.getObject("itemCode", data);
	var itemName=loxia.getObject("itemName", data);
	return "<label>"+
				"<div>编码："+itemCode+"</div>"+
					"</br>"+
				"<div>名称："+itemName+"</div>"+
					"</br>"+
			"</label>";
}
function drawEvaInfo(data, args, idx){
	//评分
	var score = loxia.getObject("score", data);
	var starStr ="";
	for(var i=0;i<score;i++){
		starStr+="<span class='product-star'></span>";
	}
	//创建时间
	var createTime = formatDate(loxia.getObject("createTime", data),"yyyy-MM-dd HH:mm:ss");
	//是否回复
	var replyFlag="";
	var replyTime = loxia.getObject("replyTime", data);
	var reply ="";
	if(null!=replyTime){
		replyFlag ="<label style='color: red'>(已回复)</label>";
		reply = "客服回复："+loxia.getObject("reply", data);
	}
	//评价内容
	var content = loxia.getObject("content", data);
	var contentDisplay ="";
	if(content.length>120){
	    contentDisplay ="　　"+content.substring(0,120)+"......";
	}else{
		contentDisplay ="　　"+content;
	}
	//客服回答
	
	return "<label class='movetitletd'>"+
				"<div><span class='product-star-content'>"+starStr+"</span>" +
				createTime + 
				replyFlag +
				"</div>"+
					"</br>"+
				"<div custtalk='"+content+"'" +
				"servicetalk='"+reply+
				"'class='movetitle'>" 
				+contentDisplay+
                "</div>"+
					"</br>"+
			"</label>";
}

function drawEditTodo(data, args, idx){
	var result ="";
	var id=loxia.getObject("id", data);
	
	result+="<a href='javascript:void(0);' class='func-button pass' onclick='passEva("+id+")'>审核通过</a>";
	result+="<a href='javascript:void(0);' class='func-button delete' onclick='deleteEva("+id+")'>删除</a>";
	result+="<a href='javascript:void(0);' class='func-button details' idVal='"+id+"' >查看详细</a>";
	itemRates[id]=data;
	return result;
}
function drawEditDone(data, args, idx){
	var result ="";
	var id=loxia.getObject("id", data);
	result+="<a href='javascript:void(0);' class='func-button modify' onclick='deleteEva("+id+")'>删除</a>";
	result+="<a href='javascript:void(0);' class='func-button details' idVal='"+id+"'>查看详细</a>";
	itemRates[id]=data;
	return result;
}


//商品评论--添加的列


//商品满意度评价
function drawEvaInfoScore(data, args, idx){
	//评分
	var score = loxia.getObject("score", data);
	var starStr ="";
	for(var i=0;i<score;i++){
		starStr+="<span class='product-star'></span>";
	}
	return "<span class='product-star-content'>"+starStr+"</span>"
}
//配送施工满意度评价
function drawEvaInfoScore2(data, args, idx){
	//评分
	var score2 = loxia.getObject("score2", data);
	var starStr2 ="";
	for(var i=0;i<score2;i++){
		starStr2+="<span class='product-star'></span>";
	}
	return "<span class='product-star-content'>"+starStr2+"</span>"
}
//评论的时间
function drawEvaInfoCreateTime(data, args, idx){
	//创建时间
	var createTime = formatDate(loxia.getObject("createTime", data),"yyyy-MM-dd HH:mm:ss");
	return "<span class='product-star-content'>"+createTime+"</span>"
}
//评论的内容
function drawEvaInfoContent(data, args, idx){
	//评价内容
	var content = loxia.getObject("content", data);
	var contentDisplay ="";
	if(content.length>120){
	    contentDisplay ="　　"+content.substring(0,120)+"......";
	}else{
		contentDisplay ="　　"+content;
	}	
	return	"<label class='movetitletd'>"+"<div class='movetitle'>"+contentDisplay+"</div>"+"</label>";	
}







//-----------------------------------------------------列表单条操作--------------
//通过
function passEva(id){
	nps.confirm(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("TIP_OF_PASS"),function(){
		var json={"id":id};
	  	nps.asyncXhrGet(enableRateByIdUrl, json,{successHandler:function(data, textStatus){
	  		var backWarnEntity = data;
	  		if(backWarnEntity.isSuccess){
		  		nps.info(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("PASS_SUCCESS"));
		  	} 
	  		refreshData1();
	  		refreshData2();
	  	}}); 
	});
}

//删除
function deleteEva(id){
	nps.confirm(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("TIP_OF_PASS"),function(){
		var json={"id":id};
	  	nps.asyncXhrGet(disableRateByIdUrl, json,{successHandler:function(data, textStatus){
	  		var backWarnEntity = data;
	  		if(backWarnEntity.isSuccess){
		  		nps.info(nps.i18n("SYSTEM_ITEM_RATE_MESSAGE"),nps.i18n("DELETE_SUCCESS"));
		  	} 
	  		refreshData1();
	  		refreshData2();
	  	}}); 
	});
}
//------------------------------------------------------------------------列表列模板 end----------------------------
//------------------------------------------------------------------------页面效果方法 start----------------------------
//刷新数据
function refreshData1() {
	$j("#table1").loxiasimpletable("refresh");
}
function refreshData2() {
	$j("#table2").loxiasimpletable("refresh");
}

//高级选项
function slideAdvancedOptions(){
	$j("#advancedOptions").slideToggle(10);
	$j(".advancedOptionsButton").hide();
	$j(".advancedOptionsButtonHide").show();
	$j("#lifecycle").attr("disabled",false);
	$j("#operatorRealName").attr("disabled",false);
	$j("#isReply").attr("disabled",false);
	$j("#lifecycle_pass").attr("disabled",true);
}
//隐藏高级选项
function slideAdvancedOptionsHide(){
	$j("#advancedOptions").slideToggle(10);
	$j(".advancedOptionsButton").show();
	$j(".advancedOptionsButtonHide").hide();
	$j("#lifecycle").attr("disabled",true);
	$j("#operatorRealName").attr("disabled",true);
	$j("#isReply").attr("disabled",true);
	$j("#lifecycle_pass").attr("disabled",false);
	
}
//待审核评价列表
function doUnchecked(){
	$j(".title_checked").hide();
	$j(".title_unchecked").show();

}
//已审核评价列表
function doChecked(){
	$j(".title_unchecked").hide();
	$j(".title_checked").show();
   
}


//-----------------------------------------------------------------------页面效果方法  end----------------------------


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