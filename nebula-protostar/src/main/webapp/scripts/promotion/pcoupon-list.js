$j.extend(loxia.regional['zh-CN'],{
	"TO_VIEW":"查看",
	"TO_UPDATE":"修改",
	"TO_ENABLE":"启用",
	"TO_DEL":"删除",
	"PROPERT_OPERATOR_TIP":"属性提示信息",
	"PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的属性么？",
	"INFO_TITLE_DATA":"提示信息",
	"INFO_DELETE_SUCCESS":"删除记录成功!",
    "INFO_DELETE_FAIL":"删除记录失败!",
    "INFO_START_SUCCESS":"启用成功!",
    "INFO_START_FAIL":"启用失败!",
    "INFO_CANCLE_START_SUCCESS":"取消启用成功!",
    "INFO_CANCEL_START_FAIL":"取消启用失败!",
	"TO_CANCEL":"禁用"
});

// 活动列表
var promotionUrl = base+'/coupon/list.json'; 
// 启用
var ACTIVATION_URL = base + "/coupon/updateActive.json?active=1";
// 取消启用
var INACTIVATION_URL = base + "/coupon/updateActive.json?active=0";
//新增界面
var createURL = base + "/coupon/create.htm";
//查看界面
var viewURl = base + "/coupon/view.htm";
//修改
var editURl =base + "/coupon/edit.htm"; 
//删除
var delURl =base + "/coupon/delCouponsByIds.json"; 

function drawEditor(data){
	var id=loxia.getObject("id", data);
	var activeMark=loxia.getObject("activeMark",data);
	var view="<a href='"+ viewURl+"?id="+id+"' class='func-button view'>"+nps.i18n("TO_VIEW")+"</a>";
	var update="<a href='"+ editURl+"?id="+id+"' class='func-button editor'>"+nps.i18n("TO_UPDATE")+"</a>";
	var enable ;
	if(activeMark == 0){
		enable="<a href='javascript:void(0);' val='"+id+"' type= '1' class='func-button enable'>"+nps.i18n("TO_ENABLE")+"</a>";
	}else{
		enable="<a href='javascript:void(0);' val='"+id+"' type= '2' class='func-button disable'>"+nps.i18n("TO_CANCEL")+"</a>";
	}
    var del="<a href='javascript:void(0);' val='"+id+"' class='func-button delete'>"+nps.i18n("TO_DEL")+"</a>";
	
	return view+update+enable+del;
}


// 刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}

// 获取日期格式
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		if(val.createTime==null||val.createTime==''){
			return "&nbsp;";
		}
		var date=new Date(val.createTime);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
} 

function type(data){
	var type = loxia.getObject("type", data);	
	var result = "";
	if(type == 1){
		result = "金额";
	}else{
		result = "折扣";
	}
 	return result; 
} 
 
/**
 * checkbox
 */
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' name='id' value='"
			+ loxia.getObject("id", data) + "'/>";
}


/**
 * 有效或无效优惠劵
 */
function updateActive(val,activeMark){
	var msg = "";
	if(activeMark== 1){
		msg = "确定要启用优惠劵?";
	}else{
		msg = "确定要禁用优惠劵?";
	}
	nps.confirm(nps.i18n("INFO_TITLE_DATA"),msg,function(){
		var json={"id":val}; 
		var url ;
		if(activeMark == 1){
			url = ACTIVATION_URL;
		}else{
			url = INACTIVATION_URL;
		}
	  	nps.asyncXhrPost(url, json,{successHandler:function(data, textStatus){
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
/**
 * 批量删除优惠劵
 */
function confirmDelete(id){
	nps.confirm(nps.i18n("PROPERT_OPERATOR_TIP"),nps.i18n("PROPERT_CONFIRM_DELETE_SEL_PROPERT"),function(){
		var json ;
		if(id != null){
			 json={"ids":id}; 
		}else{
			var checkbox=$j("input[name='id']");
			var data=""; 
			  $j.each(checkbox, function(i,val){   
				  if(val.checked){
					  data=data+$j(this).val()+",";
				  }
			 }); 
			  if(data!=""){
				  data=data.substr(0,data.length-1);
			  }  
			  if(data == ""){
				  nps.info(nps.i18n("INFO_TITLE_DATA"),"请选择需要删除的优惠劵");
				  return;
			  }
			 json={"ids":data}; 
			 
		}
	  	 nps.asyncXhrPost(delURl, json,{successHandler:function(data, textStatus){
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
		nodatamessage:'<span>查询数据为空</span>',
		form:"searchForm",
		cols : [{
				label : "<input type='checkbox'  />",
				width : "5%",
				template : "drawCheckbox"
			},{
				name : "couponName",
				label : "优惠劵名称",
				width : "14%" 
				
			}, {
				name : "type",
				label : "优惠劵类型",
				width : "10%" , 
				template:"type" 
			}, {
				name : "discount",
				label : "金额/折扣",
				width : "10%" ,
			}, {
				name : "limitTimes",
				label : "使用次数",
				width : "10%" ,
			}, {
				name : "activeMark",
				label : "状态",
				width : "10%" , 
				type: "yesno"
			} , {
				name : "createUserName",
				label : "创建人",
				width : "10%" 
			}, {
				name : "createTime",
				label : "创建时间",
				width : "10%" , 
				template:"formatDate" 
			}, {
				label :"操作",
				width : "15%", 			 
//				type:"oplist", 
//				oplist:operateType
				template:"drawEditor" 
			}],
		dataurl : promotionUrl
	});
	//加载数据
	refreshData();
	// 筛选数据
	$j(".func-button.search").click(function(){
		 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	});  
	//新建界面
	$j(".button.addcoupon").click(function(){
	        window.location.href=createURL;
	});
	//执行批量删除
	$j(".button.delete").click(function() {
		confirmDelete(null);
	});

	 $j("#table1").on("click",".func-button.enable",function(){
		  var curObject=$j(this);
		  updateActive(curObject.attr("val"),curObject.attr("type"));
	    });

	 $j("#table1").on("click",".func-button.disable",function(){
		 var curObject=$j(this);
		 updateActive(curObject.attr("val"),curObject.attr("type"));
	    });

	 $j("#table1").on("click",".func-button.delete",function(){
		 var curObject=$j(this);
		 confirmDelete(curObject.attr("val"));
	   });

}); 




