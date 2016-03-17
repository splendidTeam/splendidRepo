/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'],{
	"ROLE_NAME" : "角色名称",
	"NO_DATA":"无数据",
	"LABLE_PRODUCT" : "商品",
	"LABLE_CONSULTANT_ITEM" : "咨询内容",
	"LABLE_MEMBER_NAME" : "会员名称",
	"LABLE_CREATE_TIME" : "咨询时间",
	"LABLE_OPERATION" : "操作",
	"INFO_TITLE_DATA" : "提示信息",
	"INFO_REPLY_SUCCESS" : "回复成功",
	"INFO_REPLY_FAILURE" : "回复失败",
	"INFO_UPDATE_SUCCESS" : "更新成功",
	"INFO_UPDATE_FAILURE" : "更新失败",
	"INFO_PUBLISH_SUCCESS" : "公示成功",
	"INFO_PUBLISH_FAILURE" : "公示失败",
	"INFO_UNPUBLISH_SUCCESS" : "取消公示成功",
	"INFO_UNPUBLISH_FAILURE" : "取消公示失败",
	"INFO_BATCH_PUBLISH_SUCCESS" : "批量公示成功",
	"INFO_BATCH_PUBLISH_FAILURE" : "批量公示失败",
	"INFO_BATCH_UNPUBLISH_SUCCESS" : "批量取消公示成功",
	"INFO_BATCH_UNPUBLISH_FAILURE" : "批量取消公示失败",
	"INFO_NO_CHECKED" : "请先勾选需要批量处理的数据",
	"PAGE_REPLY" : "客服回复",
	"OPERATION_RESOLVE" : "回复",
	"OPERATION_UPDATE" : "更新",
	"OPERATION_PUBLISH" : "公示",
	"OPERATION_UNPUBLISH" : "取消公示",
	"RESOLVE_NOT_NULL" : "回复不能为空",
	"RESOLVE_IS_AVAILABLE" : "回复可用"
});

/* ------------------------------------------------- URL ------------------------------------------------- */
var consultantListUrl = base + "/consultant/consultantList.json";
var getConsultatnByIdUrl = base + "/consultant/getConsultantById.json";
var resolveUrl = base + "/consultant/resolveConsultant.json";
var updateUrl = base + "/consultant/updateConsultant.json";
var publishUrl = base + "/consultant/publishConsultant.json";
var unpublishUrl = base + "/consultant/unpublishConsultant.json";
var batchPublishUrl = base + "/consultant/batchPublishConsultant.json";
var batchUnpublishUrl = base + "/consultant/batchUnpublishConsultant.json";
var exportUrl = base + "/consultant/consultant-list.xlsx";

/* ------------------------------------------------- 全局常量 ------------------------------------------------- */
var CONSTANT_RESOLVED = 1;	//已解决
var CONSTANT_UNRESOLVED = 0;	//未解决
var CONSTANT_PUBLISH_MARK_YES = 1;	//公示
var CONSTANT_PUBLISH_MARK_NO = 0;	//不公示

/* ------------------------------------------------- 全局变量 ------------------------------------------------- */
//标识当前是 ‘待解决’ 页面，还是 ‘已解决’ 页面，默认是 ‘待解决’ 页面
var IS_RESOLVED = false;
var PRIVILEGE = false;		//操作权限

$j(document).ready(function() {
	
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	
	//TODO
	/*searchFilter.init({formId: 'resolvedSearchForm', searchButtonClass: '.func-button.search'});
	searchFilter.init({formId: 'unresolvedSearchForm', searchButtonClass: '.func-button.search'});*/
	
	/* ------------------------------------------------- 权限控制 ------------------------------------------------- */
	PRIVILEGE =  !! $j("#hid_pri").val();

	//‘待解决’ 与 ‘已解决’ 切换
	$j("li.memberbase").click(function(){
		var val = $j(this).attr("val") - 0;
		if (val == CONSTANT_RESOLVED) {
			IS_RESOLVED = true;
			$j(".reslovedTitleDiv").show();
			$j(".unReslovedTitleDiv").hide();
		} else if (val == CONSTANT_UNRESOLVED) {
			IS_RESOLVED = false;
			$j(".reslovedTitleDiv").hide();
			$j(".unReslovedTitleDiv").show();
		}
	});
	
	//重置按钮
	$j(".func-button.reset").click(function(){
		$j(this).parents(".consultant-form").find(":text").val("");
		$j(this).parents(".consultant-form").find("select").prop("selectedIndex", 0);
	});
	
	//搜索按钮
	$j(".func-button.search").click(function(){
		var curTable = IS_RESOLVED ? $j("#reslovedTable") : $j("#unResloveTable");
		curTable.data().uiLoxiasimpletable.options.currentPage= 1;
		curTable.loxiasimpletable("refresh");
	});

	//‘待解决’ 表格
	$j("#unResloveTable").loxiasimpletable({
		page : true,
		size : 10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		cols : [ {
					label : "<input type='checkbox'/>",
					witdh : "5%",
					template : "drawCheckbox"
				}, {
					name : "pdInfo",
					label : nps.i18n("LABLE_PRODUCT"),
					width : "20%",
					template : "drawPdInfo"
				}, {
					name : "question",
					label : nps.i18n("LABLE_CONSULTANT_ITEM"),
					width : "30%",
					template : "drawUnresolvedEvaInfo"
					
				}, {
					name : "name",
					label : nps.i18n("LABLE_MEMBER_NAME"),
					width : "15%",
					template : "drawMemberInfo"
				},{		
					name:"createTime",
					label : nps.i18n("LABLE_CREATE_TIME"),
					width : "15%",
					formatter:"formatDate"
				},{
					label : nps.i18n("LABLE_OPERATION"),
					width : "15%",
					template : "drawUnResloveOpType"
				}  ],
		form:"unresolvedSearchForm",
		dataurl : consultantListUrl
	});

	//‘已解决’ 表格
	$j("#reslovedTable").loxiasimpletable({
		page : true,
		size : 10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		cols : [ {
					label : "<input type='checkbox'/>",
					witdh : "5%",
					template : "drawCheckbox"
				}, {
					name : "pdInfo",
					label : nps.i18n("LABLE_PRODUCT"),
					width : "20%",
					template : "drawPdInfo"
				}, {
					name : "question",
					label : nps.i18n("LABLE_CONSULTANT_ITEM"),
					width : "30%",
					template : "drawResolvedEvaInfo"
				}, {
					name : "name",
					label : nps.i18n("LABLE_MEMBER_NAME"),
					width : "15%",
					template : "drawMemberInfo"
				},{		
					name:"createTime",
					label : nps.i18n("LABLE_CREATE_TIME"),
					width : "15%",
					formatter:"formatDate"
				},{
					label : nps.i18n("LABLE_OPERATION"),
					width : "15%",
					template : "drewResloveOpType"
				}  ],
		form:"resolvedSearchForm",
		dataurl : consultantListUrl
	});
	
	/* ------------------------------------------------- 回复咨询 ------------------------------------------------- */
	//‘待回复’页面 -- 回复按钮
	$j("#unResloveTable").on("click", ".answerBtn", function(){
		$j("#answer_dialog_answer").removeClass("ui-loxia-error");
		$j("#loxiaTip-r").hide();
		var id = $j(this).attr("val") - 0;
		nps.asyncXhrPost(getConsultatnByIdUrl, {id : id}, {successHandler : function(data, textStatus) {
			$j("#answer_dialog_itemName").text(data.itemName);
			$j("#answer_dialog_itemCode").text(data.itemCode);
			$j("#answer_dialog_memberName").text(data.memberName);
			$j("#answer_dialog_createTime").text(formatDate(data.createTime));
			$j("#answer_dialog_content").text(data.content);
			$j("#answer_dialog_id").val(data.id);
			$j("#answer_dialog").dialogff({type:'open',close:'in',width:'700px',height:'430px'});
		}});
	});
	
	$j("#answer_dialog_answer").blur(function(){
		var resolveNote = $j.trim($j(this).val());
		if (resolveNote.length == 0) {
			$j("#loxiaTip-r").show();
			$j("#loxiaTip-r .inner.ui-corner-all").html(nps.i18n("RESOLVE_NOT_NULL"));
			$j("#answer_dialog_answer").addClass("ui-loxia-error");
		} else {
			$j("#loxiaTip-r").show();
		 	$j("#loxiaTip-r .inner.ui-corner-all").html(nps.i18n("RESOLVE_IS_AVAILABLE"));
			setTimeout(function(){ 
				$j("#loxiaTip-r").hide();
		 	},2000); 
		}
	});
	
	//提交回复
	$j("#answer_dialog .copyok").click(function(){
		var id = $j("#answer_dialog_id").val() - 0;
		var publishMark = $j("#answer_dialog_publishMark").prop("checked") ? 1 : 0;
		var resolveNote = $j.trim($j("#answer_dialog_answer").val());	//TODO 防止非法字符
		if (resolveNote.length == 0) {
			$j("#loxiaTip-r").show();
			$j("#loxiaTip-r .inner.ui-corner-all").html(nps.i18n("RESOLVE_NOT_NULL"));
			$j("#answer_dialog_answer").addClass("ui-loxia-error");
			return;
		}
		
		nps.asyncXhrPost(resolveUrl, {id : id, resolveNote : resolveNote, publishMark : publishMark}, 
			{successHandler : function(data, textStatus) {
				if (data.isSuccess) {
					$j("#answer_dialog .dialog-close").click();
					
					//清空输入框
					$j("#answer_dialog_id").val('');
					$j("#answer_dialog_publishMark").prop("checked", false);
					$j("#answer_dialog_answer").val('');
					
					//刷新数据列表
					$j("#unResloveTable").loxiasimpletable("refresh");
					$j("#reslovedTable").loxiasimpletable("refresh");
					
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_REPLY_SUCCESS"));
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_REPLY_FAILURE"));
				}
		}});
	});
	
	//关闭 回复对话框
	$j("#answer_dialog .copycancel").click(function(){
		$j("#answer_dialog .dialog-close").click();
	});
	
	/* ------------------------------------------------- 更新回复 ------------------------------------------------- */
	//‘已回复’页面 -- 更新按钮
	$j("#reslovedTable").on("click", ".updateAnswerBtn", function(){
		$j("#update_dialog_answer").removeClass("ui-loxia-error");
		$j("#update-loxiaTip-r").hide();
		var id = $j(this).attr("val") - 0;
		nps.asyncXhrPost(getConsultatnByIdUrl, {id : id}, {successHandler : function(data, textStatus) {
			$j("#update_dialog_itemName").text(data.itemName);
			$j("#update_dialog_itemCode").text(data.itemCode);
			$j("#update_dialog_memberName").text(data.memberName);
			$j("#update_dialog_createTime").text(formatDate(data.createTime));
			$j("#update_dialog_content").text(data.content);
			$j("#update_dialog_id").val(data.id);
			$j("#update_dialog_answer").val(data.resolveNote);
			$j("#update_dialog_lastAnswer").text(data.resolveNote);
			$j("#update_dialog_publishMark").prop("checked", data.publishMark == 1 ? true : false);
			$j("#update_dialog").dialogff({type:'open',close:'in',width:'700px',height:'500px'});
			$j("#update_dialog .proto-dialog-content").css("height", "390px");	//纠正弹出层的高度
		}});
	});
	
	$j("#update_dialog_answer").blur(function(){
		var resolveNote = $j.trim($j(this).val());
		if (resolveNote.length == 0) {
			$j("#update-loxiaTip-r").show();
			$j("#update-loxiaTip-r .inner.ui-corner-all").html(nps.i18n("RESOLVE_NOT_NULL"));
			$j("#update_dialog_answer").addClass("ui-loxia-error");
		} else {
			$j("#update-loxiaTip-r").show();
		 	$j("#update-loxiaTip-r .inner.ui-corner-all").html(nps.i18n("RESOLVE_IS_AVAILABLE"));
			setTimeout(function(){ 
				$j("#update-loxiaTip-r").hide();
		 	},2000); 
		}
	});
	
	//提交更新
	$j("#update_dialog .copyok").click(function(){
		var id = $j("#update_dialog_id").val() - 0;
		var publishMark = $j("#update_dialog_publishMark").prop("checked") ? 1 : 0;
		var resolveNote = $j.trim($j("#update_dialog_answer").val());	//TODO 防止非法字符
		if (resolveNote.length == 0) {
			$j("#update-loxiaTip-r").show();
			$j("#update-loxiaTip-r .inner.ui-corner-all").html(nps.i18n("RESOLVE_NOT_NULL"));
			$j("#update_dialog_answer").addClass("ui-loxia-error");
			return;
		}
		
		nps.asyncXhrPost(updateUrl, {id : id, resolveNote : resolveNote, publishMark : publishMark}, 
			{successHandler : function(data, textStatus) {
				if (data.isSuccess) {
					$j("#update_dialog .dialog-close").click();
					
					//清空输入框
					$j("#update_dialog_id").val('');
					$j("#update_dialog_publishMark").prop("checked", false);
					$j("#update_dialog_answer").val('');
					
					//刷新数据列表
					$j("#unResloveTable").loxiasimpletable("refresh");
					$j("#reslovedTable").loxiasimpletable("refresh");
					
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_UPDATE_SUCCESS"));
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_UPDATE_FAILURE"));
				}
		}});
	});
	
	//关闭 更新对话框
	$j("#update_dialog .copycancel").click(function(){
		$j("#update_dialog .dialog-close").click();
	});
	
	/* ------------------------------------------------- 公示 ------------------------------------------------- */
	//公示 按钮
	$j("#reslovedTable").on("click", ".publishBtn", function(){
		var id = $j(this).attr("val") - 0;
		nps.asyncXhrPost(publishUrl, {id : id}, {successHandler : function(data, textStatus) {
			if (data.isSuccess) {
				//刷新数据列表
				$j("#reslovedTable").loxiasimpletable("refresh");
				
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_PUBLISH_SUCCESS"));
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_PUBLISH_FAILURE"));
			}
		}});
	});

	/* ------------------------------------------------- 取消公示 ------------------------------------------------- */
	//取消公示 按钮
	$j("#reslovedTable").on("click", ".unpublishBtn", function(){
		var id = $j(this).attr("val") - 0;
		nps.asyncXhrPost(unpublishUrl, {id : id}, {successHandler : function(data, textStatus) {
			if (data.isSuccess) {
				//刷新数据列表
				$j("#reslovedTable").loxiasimpletable("refresh");
				
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_UNPUBLISH_SUCCESS"));
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_UNPUBLISH_FAILURE"));
			}
		}});
	});
	
	/* ------------------------------------------------- 批量公示 ------------------------------------------------- */
	$j(".batchPublishBtn").click(function(){
		var ids = "";	//所有ID
		$j("#reslovedTable :checkbox[name='check']:checked").each(function(i, dom){
			ids += dom.value + ",";
		});
		ids = ids.substring(0, ids.length - 1);
		
		//判断是否已勾选
		if($j.trim(ids).length == 0) {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_CHECKED"));
			return;
		}
		
		nps.asyncXhrPost(batchPublishUrl, {ids : ids}, {successHandler : function(data, textStatus) {
			if (data.isSuccess) {
				//刷新数据列表
				$j("#reslovedTable").loxiasimpletable("refresh");
				
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_BATCH_PUBLISH_SUCCESS"));
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_BATCH_PUBLISH_FAILURE"));
			}
		}});
	});
	
	/* ------------------------------------------------- 批量取消公示 ------------------------------------------------- */
	$j(".batchUnpublishBtn").click(function(){
		var ids = "";	//所有ID
		$j("#reslovedTable :checkbox[name='check']:checked").each(function(i, dom){
			ids += dom.value + ",";
		});
		ids = ids.substring(0, ids.length - 1);
		
		//判断是否已勾选
		if($j.trim(ids).length == 0) {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_CHECKED"));
			return;
		}
		
		nps.asyncXhrPost(batchUnpublishUrl, {ids : ids}, {successHandler : function(data, textStatus) {
			if (data.isSuccess) {
				//刷新数据列表
				$j("#reslovedTable").loxiasimpletable("refresh");
				
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_BATCH_UNPUBLISH_SUCCESS"));
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_BATCH_UNPUBLISH_FAILURE"));
			}
		}});
	});
	
	/* ------------------------------------------------- EXCEL导出 ------------------------------------------------- */
	//待回复导出
	$j(".unResloveExportBtn").click(function() {
		var form = document.getElementById("unresolvedSearchForm");
		var param = {};
		for (var i = 0; i < form.length; i ++) {
			param[form.elements[i].name] = form.elements[i].value;
		}
		var paramStr = $j.param(param);
		window.location.href = base + exportUrl + "?" + paramStr;
	});

	//已回复导出
	$j(".resloveExportBtn").click(function() {
		var form = document.getElementById("resolvedSearchForm");
		var param = {};
		for (var i = 0; i < form.length; i ++) {
			param[form.elements[i].name] = form.elements[i].value;
		}
		var paramStr = $j.param(param);
		window.location.href = base + exportUrl + "?" + paramStr;
	});
	
	/* ------------------------------------------------- init ------------------------------------------------- */
	$j(".reslovedTitleDiv").hide();
	$j(".unReslovedTitleDiv").show();
	
	$j("#unResloveTable").loxiasimpletable("refresh");
	$j("#reslovedTable").loxiasimpletable("refresh");

	/* ------------------------------------------------- 移动弹出title层 ------------------------------------------------- */
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

/* ------------------------------------------------- loxia-table template定义 ------------------------------------------------- */
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' name='check' class='tagCheckId' value='"
			+ loxia.getObject("id", data) + "'/>";
}

function drawPdInfo(data, args , idx) {
	var code = loxia.getObject("itemCode", data);
	var name = loxia.getObject("itemName", data);
	return "<label><div>" + code + "</div><br /><div>" + name + "</div></label>";
}

function drawUnresolvedEvaInfo(data, args, idx){
	var createTime = formatDate(loxia.getObject("createTime", data));
	var content = loxia.getObject("content", data);
	var contentDisplay ="";
	if (content) {
		if(content.length>120){
		    contentDisplay ="　　"+content.substring(0,120)+"......";
		}else{
			contentDisplay ="　　"+content;
		}
	}
	
	return "<label class='movetitletd'>"+
				"<div>" +
				createTime + 
				"</div>"+
					"</br>"+
				"<div custtalk='"+content+"'" +
				"servicetalk='' class='movetitle'>" 
				+contentDisplay+
                "</div></br></label>";
}

function drawResolvedEvaInfo(data, args, idx){
	var createTime = formatDate(loxia.getObject("createTime", data));
	var content = loxia.getObject("content", data);
	var reply = nps.i18n("PAGE_REPLY") + ": " + loxia.getObject("resolveNote", data);
	var contentDisplay ="";
	if(content.length>120){
	    contentDisplay ="　　"+content.substring(0,120)+"......";
	}else{
		contentDisplay ="　　"+content;
	}
	return "<label class='movetitletd'>"+
				"<div>" +
				createTime + 
				"</div>"+
					"</br>"+
				"<div custtalk='" + content + "'" +
				"servicetalk='" + reply + 
				"' class='movetitle'>" + contentDisplay+
                "</div></br></label>";
}

function drawMemberInfo(data, args, idx) {
	var name = loxia.getObject("memberName", data);
	return "<label><div>" + name + "</div></label>";
}

function drawUnResloveOpType(data, args, idx) {
	return PRIVILEGE ? "<a href='javascript:void(0);' class='func-button answerBtn' val='"
		+ loxia.getObject("id", data) + "'>" + nps.i18n("OPERATION_RESOLVE") + "</a>" : "";
}

function drewResloveOpType(data, args, idx) {
	var rs = "";
	if (PRIVILEGE ) {
		rs += "<a href='javascript:void(0);' class='func-button updateAnswerBtn' val='"
			+ loxia.getObject("id", data) + "'>" + nps.i18n("OPERATION_UPDATE") +"</a>";
	}
	if (PRIVILEGE && data.publishMark == CONSTANT_PUBLISH_MARK_NO) {
		rs += "<a href='javascript:void(0);' class='func-button publishBtn' val='"
			+ loxia.getObject("id", data) + "'>" + nps.i18n("OPERATION_PUBLISH") + "</a>";
	}
	if (PRIVILEGE && data.publishMark == CONSTANT_PUBLISH_MARK_YES) {
		rs += "<a href='javascript:void(0);' class='func-button unpublishBtn' val='"
			+ loxia.getObject("id", data) + "'>" + nps.i18n("OPERATION_UNPUBLISH") + "</a>";
	}
	return rs;
}

/* ------------------------------------------------- 工具 ------------------------------------------------- */
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
}

function checkStrIsNotNull(str) {
	if (str != null && str.trim() != "") {
		return true;
	} else {
		return false;
	}
}
