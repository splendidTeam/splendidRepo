$j.extend(loxia.regional['zh-CN'],{  
	    "LABEL_MEMBER_CREATETIME":"创建时间",
	    "LABEL_MEMBER_CREATEMEMBER":"创建人员", 
	    "LABEL_MEMBER_OPERATE":"操作",	   
	    "LABEL_MEMBER_GROUPNAME":"筛选器名称",
	    "LABEL_MEMBER_TYPE":"生效",
	    "LABEL_MEMBER_GROUPEXPRESSIONTEXT":"表达式名称",
	    "TO_UPDATE":"修改",
	    "TO_ENABLE":"启用",
	    "TO_CANCEL":"取消启用",
	    "TO_DRAFT_COPY":"复制草稿", 
	    "INFO_TITLE_DATA":"提示信息",
	    "INFO_STOP_FAIL":"取消启用失败!",
	    "INFO_DISABLE_SUCCESS":"取消启成功", 
	    "LABEL_MEMBER_GROUPEXPRESSION":"人群组合表达式",
	    "NO_CATEGORY":"无",  
	    "NO_DATA":"无数据",
	    "TO_UPDATE":"修改",
	    "DRAW_FIND":"查看",
	    "MEMBER_INFO_CONFIRM":"确认信息",
	    "MEMBER_CONFIRM_DISABLE_USER":"确认变该组合失效",
	    "MEMBER_CONFIRM_ENABLE_USER":"确认启用该组合",
	    "USER_FORM_CHECK_ERROR":"错误信息",
	    "DRAW_NO":"失效",
	    "TO_ENABLE":"启用",
	    "INFO_TITLE_DATA":"提示信息",
	    "INFO_ENABLE_SUCCESS":"启用成功",
	    "INFO_DISABLE_SUCCESS":"失效成功", 
	    "INFO_ENABLE_FAIL":"启用失败",
	    "INFO_DISABLE_FAIL":"失效失败" ,
	    "INFO_COMBO_INEXISTED":"该筛选器不存在" ,
	    "INFO_SYSTEM_ERROR":"系统错误" ,
});

// 人群组合查询
var memberCustomgroupListUrl = base+'/member/memberCustomgroupList.json';  

var ACTIVATE_URL=base+'/member/activateMemberTagRule.json';
var INACTIVATE_URL=base+'/member/inactivateMemberTagRule.json';
var CREATE_URL = base + '/member/combo/add.htm';
var VIEW_URL = base + '/member/combo/view.json';
var MODIFY_URL = base + '/member/combo/update.htm';


function drawEditor(data, args, idx){
	var result="";  
	var id=loxia.getObject("id", data);
	var state=loxia.getObject("lifecycle", data);
	var modify="<a href='javascript:void(0);' data='" + id +"' class='func-button modify'>"+nps.i18n("TO_UPDATE")+"</a>";
	var view="<a href='javascript:void(0);' data='" + id +"' class='func-button view'>"+nps.i18n("DRAW_FIND")+"</a>";
	  if(state==0){
		  result+=view+modify+"<a href='javascript:void(0);' val='"+id+"' class='func-button enable'>"+nps.i18n("TO_ENABLE")+"</a>";
	  }else if(state=1){ 
		  result+=view+modify+"<a href='javascript:void(0);' val='"+id+"' class='func-button disable'>"+nps.i18n("DRAW_NO")+"</a>";
	 } 
	return result;
} 
function drawExpressionText(data, args, idx){	 
	var expressionText = loxia.getObject("text", data);
	var text =""; 
	if(expressionText==null)
	return text;
	 
	var moveText="";
	if(expressionText.indexOf("【排除】")!=-1){ 
	  expressionText = expressionText.replace("【排除】","<br/>【排除】"); 
	}
	 moveText=expressionText;
	if(expressionText.length>50){
		text +=expressionText.substring(0,50)+"......";
	}else{
		text +=expressionText;
	}  
	text= "<label class='movetitletd'>"+ 
		"<div custtalk='"+moveText+"' class='movetitle'>" 
		+text+
     "</div> </label>";
	
	return text;
}
  
// 刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}
function drawCheckbox(data, args, idx){
	
	var state=loxia.getObject("lifecycle", data);
	if(state==1 ){
		return "<span class='ui-pyesno ui-pyesno-yes limitValue' value='" + loxia.getObject("limitValue", data)+"'></span>";
	}
	return "<span class='ui-pyesno ui-pyesno-no limitValue'   value='" + loxia.getObject("limitValue", data)+"'></span>";
	 
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
			label : nps.i18n("LABEL_MEMBER_GROUPNAME"),
			width : "9%"  
		}, {
			name : "lifecycle",
			label : nps.i18n("LABEL_MEMBER_TYPE"),
			width : "9%" ,
			template:"drawCheckbox"
		},{
			name : "text",
			label : nps.i18n("LABEL_MEMBER_GROUPEXPRESSIONTEXT"),
			width : "17%" ,
			template:"drawExpressionText"
		},{
			name : "createTime",
			label : nps.i18n("LABEL_MEMBER_CREATETIME"),
			width : "9%",
			formatter:"formatDate",
			sort:["mr.create_time asc","mr.create_time desc"]
		}, {
			name : "createName",
			label : nps.i18n("LABEL_MEMBER_CREATEMEMBER"),
			width : "9%" 
		},{
			name : nps.i18n("LABEL_MEMBER_OPERATE"),
			label : nps.i18n("LABEL_MEMBER_OPERATE"),
			width : "10%", 			 
			template : "drawEditor"
		} ],
		dataurl : memberCustomgroupListUrl
	});
	refreshData();
	 
	// 筛选数据
	 $j(".func-button.search").click(function(){
			 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
			 refreshData();
	   }); 
	 	$j(".button.orange.addcustomgroup").click(function(){ 
	        window.location.href=CREATE_URL;
	    });
	   
	   // 禁用单行
	   $j("#table1").on("click",".func-button.disable",function(){
	        var curObject=$j(this);
	        nps.confirm(nps.i18n("MEMBER_INFO_CONFIRM"),nps.i18n("MEMBER_CONFIRM_DISABLE_USER"), function(){

	            var json={"id":curObject.attr("val"),"lifecycle":0};

	        	var _d = nps.syncXhr(INACTIVATE_URL, json,{type: "POST"});
	        	if(_d.isSuccess){
	        		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISABLE_SUCCESS"));
	        		refreshData();
	        	}
	        	else{
	        		nps.info(nps.i18n("USER_FORM_CHECK_ERROR"), _d.description);
	        	}
	            	
	        });
	    });

	    
  // 启用单行
	   $j("#table1").on("click",".func-button.enable",function(){
	        var curObject=$j(this);
	        nps.confirm(nps.i18n("MEMBER_INFO_CONFIRM"),nps.i18n("MEMBER_CONFIRM_ENABLE_USER"), function(){

	            var json={"id":curObject.attr("val"),"lifecycle":1};

	        	var _d = nps.syncXhr(ACTIVATE_URL, json,{type: "POST"});
	        	if(_d.isSuccess){
	        		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ENABLE_SUCCESS"));
	        		refreshData();
	        	}
	            	
	        	else{
	        		nps.info(nps.i18n("USER_FORM_CHECK_ERROR"),_d.description);
	        	}
	            	
	        });
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

	//查看	
	$j("#table1").on("click", ".view", function() {
		$j("#view-block .include-list tbody").empty();
		$j("#view-block .exclude-list tbody").empty();
		
		var id = $j(this).attr("data");
		var data = nps.syncXhrPost(VIEW_URL, {id:id});
		if (data.isSuccess) {
			var type = data.combo.type;
			var name = data.combo.name;
			$j("#view-block .txt-type").attr('disabled', 'disabled').val(type);
			$j("#view-block .txt-name").val(name);
			
			$j.each(data.incList, function(i, obj) {
				if (obj.id == 0) {
					obj.name = "全体";
				}
				var html = "<tr class='" + ((i%2==0)?"even":"odd") + "'><td>" 
				+ obj.name + "</td></tr>";
				$j("#view-block .include-list tbody").append(html);
			});
			
			if (data.excList) {
				$j.each(data.excList, function(i, obj) {
					var html = "<tr class='" + ((i%2==0)?"even":"odd") + "'><td>" 
						+ obj.name + "</td></tr>";
					$j("#view-block .exclude-list tbody").append(html);
				});
			}
			
			$j("#view-block").dialogff({type:'open',close:'in',width:'1000px',height:'550px'});
		} else {
			if (data.errorCode == 7003) {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_COMBO_INEXISTED"));
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SYSTEM_ERROR"));
			}
		}
	});
	
	//浮层确定
	$j("#view-block .btn-ok").click(function() {
		$j("#view-block .dialog-close").click();
	});
	
	 //修改	
	$j("#table1").on("click",".modify",function(){
		var id =$j(this).attr("data");
		var url = MODIFY_URL + "?id=" + id;
		var json={"comboId":id};
		var result = nps.syncXhr(base+"/member/combo/validatMemTagRule.json", json,{type: "GET"});
		if(result==true || result=="true"){
			window.location.href=url;
		}else{
			nps.info(nps.i18n("INFO_TITLE_DATA"),"会员筛选器已经在生效的促销活动引用,不能修改!");
		}
	});

});




