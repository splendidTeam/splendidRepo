$j.extend(loxia.regional['zh-CN'], {
	"ORDER_DETAIL_TIPS":"友情提示：",
	"ORDER_ISFINSHED":"订单是否完成",
	"ORDER_LOGISTICS_DESCRIPTION":"物流跟踪描述",
	"OPTION_YES":"是",
	"OPTION_NO":"否",
	"NOT_FOUND_DATA":"未找到任何数据",
	"LOGISTICS_INFO":"订单物流信息"
});

//获取支付流水
var gePayNoInfoUrl="/order/getPayNoList.json";

//获取物流信息
var getLogisticsUrl="/order/getLogistics.json";

//格式化日期
Date.prototype.format = function (format) {
	var o = {
	"M+": this.getMonth() + 1,
	"d+": this.getDate(),
	"h+": this.getHours(),
	"m+": this.getMinutes(),
	"s+": this.getSeconds(),
	"q+": Math.floor((this.getMonth() + 3) / 3),
	"S": this.getMilliseconds()
	};
	if (/(y+)/.test(format)) {
	format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for (var k in o) {
	if (new RegExp("(" + k + ")").test(format)) {
	format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
	}
	}
	return format;
} ;


//解析日期
function getFormatDate(date, pattern) {
	if (date == undefined) {
	date = new Date();
	}
	if (pattern == undefined) {
	pattern = "yyyy-MM-dd hh:mm:ss";
	}
	return date.format(pattern);
} 


$j(document).ready(function(){
	
/*	//点击每一行的点击事件响应 获取支付流水的信息
		$j("#paymentInfo").find("tr[class*='paymentLine']").each(function(){
				var currentObj=$j(this);
				currentObj.on("click",function(){
				var tableObj=$j("#payment-dialogInfo-table");
				//alert();
				var payInfoId=currentObj.children().eq(0).find("input").val();
				var payInfoData={"payInfoId":payInfoId};
				loxia.asyncXhr(gePayNoInfoUrl, payInfoData,{type: "GET",
		    		success: function(data){ 
		    			var payNolist=eval(data);//转换json格式    
		    			tableObj.children().find("tr:gt(0)").remove();
		    			if(payNolist!=null && payNolist.length>0){		    				
		    				for(var i=0;i<payNolist.length;i++){
			    				var trhtml= "<tr class='"+(i%2==0?'odd':'even')+"'>"+
			 								"<td>"+(i+1)+"</td>"+
			 								"<td>"+payNolist[i].payNo+"</td>"+
			 								"<td>"+payNolist[i].payInfoId+"</td>"+
			 								"<td>"+getFormatDate(new Date(payNolist[i].createTime))+"</td>"+			 				
			 								"</tr>";
			    				tableObj.append(trhtml);
		    				}
		    			}else{
		    				tableObj.append("<tr><td  colspan='4' style='text-align:center;color:red;'>"+nps.i18n("NOT_FOUND_DATA")+"</td></tr>");
		    			}
		    			
		    			
		    		}});
				
				$j("#payment-dialog").dialogff({type:'open',close:'in',width:'600px',height:'300px'});
				
			});
		});*/
		
		//列表字属性复层的取消按钮
	 $j("#payment-dialog").on("click","input.button.orange.close",function(){	    	
		   $j("#payment-dialog").dialogff({type:'close'});
	    });
	 
	 
	 
	 //异步加载物流信息
	 $j(".ui-tag-change").each(function(){
			$j(this).find(".tag-change-ul").find("li:last").on("click",function(){
				
				var orderIdData={"orderId":$j("#orderId").val().trim()};
				loxia.asyncXhr(getLogisticsUrl, orderIdData,{type: "GET",
		    		success: function(data){ 
		    			
		    			$j("#logisticsInfo").html("");
		    			$j("#logisticsInfo").html("<div class='ui-block-title1'>"+nps.i18n("LOGISTICS_INFO")+"</div>");	
		    			var html="";
		    			if(data=="" || data.length<=0 ||data==null){
		    				html+="<div class='ui-block-line ui-block-line-37' style='color:red;'>"+nps.i18n("NOT_FOUND_DATA")+"</div>";
		    			}
		    			if(data!="" && data!=null){
			    			var logisticsData=eval(data);		    			
			    			  html=	"<div class='ui-block-line ui-block-line-37'>"+
						       			" <label>"+nps.i18n("ORDER_ISFINSHED")+"</label>"+
						       			"<label> "+(logisticsData.isFinish==true?nps.i18n("OPTION_YES"):nps.i18n("OPTION_NO"))+"</label>"+
						       			"</div>";	
			    			 
						       	html+="<div class='ui-block-line ui-block-line-37'>"+
						       			"   <label>"+nps.i18n("ORDER_LOGISTICS_DESCRIPTION")+"</label>"+
						       			"  <label>"+logisticsData.trackingDescription+"</label>"+
						       			"</div>";
		    			}
		    			$j("#logisticsInfo").append(html);
		    		}});
			});
		});


});