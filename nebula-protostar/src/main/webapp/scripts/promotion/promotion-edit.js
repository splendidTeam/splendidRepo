/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'],{
	"NO_DATA":"无数据",
	"INFO_TITLE_DATA":"提示信息",
	"INFO_NOT_COMPLETE":"请完整填写促销信息",
	"INFO_EARLY_DATE":"结束时间必须晚于起始时间",
	"INFO_ILLEGAL_NAME":"促销名称必须由‘字母’、‘数字’、‘汉字’或‘下划线’组成",
	"INFO_HEAD_SUCCESS":"促销头部设置成功",
	"INFO_AUDIENCE_SUCCESS":"促销受益人群设置成功",
	"INFO_SCOPE_SUCCESS":"促销范围设置成功",
	"INFO_CONDITION_SUCCESS":"促销条件设置成功",
	"INFO_SETTING_SUCCESS":"促销优惠设置成功",
	"INFO_CREATE_FAILURE":"设置失败：", 
	"INFO_NO_MEMBER_FILTER":"请选择会员筛选器",
	"INFO_NO_PRODUCT_FILTER":"请选择商品筛选器",
	"INFO_NOLIMIT_OVERWRITE":"选择“不限”后将删除先前已选条件项，是否继续？",
	"INFO_INVALID_NUMBER":"请输入有效的数字",
	"INFO_NO_CONDITION":"请先添加条件项",
	"INFO_NO_SETTING":"请先添加优惠项",
	"INFO_STEP_NUMBER":"阶梯条件的数值必须递增",
	"INFO_STEP_DISCOUNT":"阶梯条件的折扣必须递减",
	"INFO_NO_MAIN_DEPUTY":"主商品和选购商品至少各有一项",
	"INFO_SAME_SCOPE":"主商品和选购商品的范围不得相同",
	"INFO_SAME_CONDITION":"该类型条件项已存在",
	"INFO_SAME_SETTING":"该类型优惠项已存在",
	"INFO_UPDATE_SCOPE":"修改促销范围将导致促销条件与促销优惠设置的所有已有数据失效，是否继续？",
	"INFO_NO_LIMIT":"选择“不限”后，无法选择其他条件项",
	"INFO_STEP_TYPE":"阶梯条件项的类型与商品范围必须相同",
	"INFO_CONDITION_TYPE_FROZEN":"促销条件类型一旦确认将无法修改，是否继续？",
	"INFO_DISCOUNT_NUMBER_ERROR":"折扣必须在0~99之间",
	"INFO_SUCCESS":"促销编辑成功！",
	"INFO_SYSTEM_ERROR":"系统错误，请联系管理员",
});

/* ------------------------------------------------- URL ------------------------------------------------- */
var CHECK_NAME_URL = base + "/promotion/check-name.json";
var STEP_ONE_URL = base + "/promotion/step-one.json";
var STEP_TWO_URL = base + "/promotion/step-two.json";
var STEP_THREE_URL = base + "/promotion/step-three.json";
var STEP_FOUR_URL = base + "/promotion/step-four.json";
var STEP_FIVE_URL = base + "/promotion/step-five.json";
var MEMBER_FILTER_LIST_URL = base + "/promotion/member-filter-list.json";
var PRODUCT_FILTER_LIST_URL = base + "/promotion/product-filter-list.json";
var PRODUCT_FILTER_OBJECT_URL = base + "/promotion/product-filter-info.json";
var DELETE_STEP_URL = base + "/promotion/delete-step.json";
var getCustomizeFilterClassUrl = base + "/promotion/findCustomizeFilterClass.json";
var getCustomizeSettingClassUrl = base + "/promotion/findCustomizeSettingClass.json";
var discount_min = 0;
var discount_max = 99;
/* ------------------------------------------------- 全局常量 ------------------------------------------------- */
var CONDITION_EXPRESSION_ARRAY = //条件表达式数组
	["nolmt","ordamt(?,?)","ordpcs(?,?)","scpordamt(?,?,?)","scpordpcs(?,?,?)","scpprdamt(?,?,?)","scpprdpcs(?,?,?)","ordcoupon(?,?)","scpcoupon(?,?,?)","ordmgnrate(?)", "scpmgnrate(?,?)", "ccdtt(?,?)"];
var SETTING_EXPRESSION_ARRAY = //优惠设置表达式数组
	["freeship","orddisc","ordrate","scporddisc","scpordrate","scpprddisc","scpprdrate","scppcsdisc","scppcsrate","scpgift", "ccdtt", "cstset"];

var SEPARATOR = "\n";

var item_select_flag=0;
/* ------------------------------------------------- 全局变量 ------------------------------------------------- */

function startWith(str,s){
	  if(s==null||s==""||str==null || str.length==0||s.length>str.length){
		  return false;
	  }
	 
	  if(str.substr(0,s.length)==s){
		  return true;
	  }
	  else{
		  return false;
	  }
	  return true;
}
//设置商品筛选器方法
function setItemSelector(comboId,step){
	if (comboId) {
		var data = nps.syncXhrPost(PRODUCT_FILTER_OBJECT_URL, {id: comboId});
		if (data.isSuccess) {
			var p_select="<option value='p-select'>请选择</option>";
			var option = "<option value='cmbid:" + data.combo.id + "'>" + data.combo.name + " [筛选器]" + "</option>";
			var type = +$j("#step-3 .pro-product-type").val();
			if(step==3){
				$j("#step-3 tbody").empty();
				$j(".pro-condition-scope").empty();	//第4步范围下拉框
				$j(".pro-condition-scope").append(p_select);
				$j(".pro-condition-scope").append(option);
			}else{
				//$j("#tag-change-in-gift tbody").empty();
				$j(".pro-condition-scope-gift").empty();	//第4步范围下拉框
				$j(".pro-condition-scope-gift").append(p_select);
				$j(".pro-condition-scope-gift").append(option);
			}
			var prefix = "---";	// 阶层表现商品范围的前缀
			
			if (type == 1) {	//商品类型
				fillWithProduct(data.details, prefix,step);
			} else if (type == 2) {	//分类类型
				fillWithCategory(data.details, prefix,step);
			} else if (type == 3) {	//自定义类型
				fillWithProduct(data.details, prefix,step);
			} else if (type == 4) {	//组合类型
				$j.each(data.detailsList, function(i, details) {
					if(step==3){
						$j(".pro-condition-scope").append("<option value='cmbid:" + details.id + "'>" + prefix + details.name + " [筛选器]" + "</option>");
					}else{
						$j(".pro-condition-scope-gift").append("<option value='cmbid:" + details.id + "'>" + prefix + details.name + " [筛选器]" + "</option>");
					}
					if (details.type == 1) {
						fillWithProduct(details, prefix + prefix,step);
					} else {
						fillWithCategory(details, prefix + prefix,step);
					}
				});
			}
			colourTbody($j("#step-3 .tbl-include tbody"));
			colourTbody($j("#step-3 .tbl-exclude tbody"));
		} else {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
		}
		
	}
	//初始化礼品筛选器
	if(step!=3){
		initGifts();
	}
	
}
//隐藏用户选择提示信息
function hideUserselectInfo(){
	var proid = $j('#pro-id').val();
	if(proid==null || proid==""){
		var t5_user_select= $j('#step-5 .user-select-step');
		for ( var i = 0; i < t5_user_select.length; i++) {
			$j(".user-selected-stepuserselect"+i).hide();
		}
	}
}

//修改时 显示用户选择提示信息
function showUserselectInfo(){
	var proid = $j('#pro-id').val();
	if(proid!=null && proid!=""){
		var t5_user_select= $j('#step-5 .user-select-step');
		for ( var i = 0; i < t5_user_select.length; i++) {
			if($j(".user-select-step:eq("+i+")").attr('checked')) {
				//not-selected-stepuserselect0
				$j(".not-selected-stepuserselect"+i).show();
				$j(".user-selected-stepuserselect"+i).hide();
			}else{
				$j(".not-selected-stepuserselect"+i).hide();
				$j(".user-selected-stepuserselect"+i).show();
			}
		}
	}
}
//初始化礼品筛选器
function initGifts(){
	var tag_change_gift =$j("#tag-change-in-gift .pro-condition-scope");
	for ( var i = 0; i < tag_change_gift.length; i++) {
		//初始化礼品筛选器
		var selected = $j("#tag-change-in-gift .pro-condition-scope:eq('"+i+"')");
		if(selected.attr("data").length != 0) {
			var data = selected.attr("data");
			var datas=data.split(",");
			if(startWith(data,'scpgift')){
				//scpgift(1,cmbid:21,1，0，1)
				selected.find("option[value='" + datas[1] + "']").attr("selected",true);
			}
			if(datas[3]==1){
				$j("#tag-change-in-gift [type=checkbox]:eq('"+i+"')").attr("checked",true);
			}else{
				$j("#tag-change-in-gift [type=checkbox]:eq('"+i+"')").attr("checked",false);
			}
			if(startWith(data,"scpgift")){
				$j("#tag-change-in-gift [type=text]:eq('"+i+"')").val(datas[4].substring(0,datas[4].length-1));
			}
		}
	}
}
function drawSettingtype(type){
	var result="无"; 
	if(type !=null){
		if(type==1) 
			result="满减";
		if(type==2) 
			result="折扣";
		if(type==3) 
			result="优惠价";
		if(type==4) 
			result="免运费"; 
		if(type==5) 
			result="赠品"; 
	}
 	return result; 
}
/* ------------------------------------------------- ready ------------------------------------------------- */
$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	
	//‘修改’按钮
	$j(".btn-update").click(function() {
		item_select_flag=0;
		toNextStep(0);
		$j(this).hide();
	});
	
	//查看时 将活动角标 转换成对应的文字说明 
	var logo = $j("#pro-mark-logo").text();
	$j("#pro-mark-logo").text(drawSettingtype(logo));
	//修改初始化对应的用户选择提示信息
	showUserselectInfo();
	$j(".btn-return").click(function(){
		window.location.href = base + "/promotion/promotionList.htm";
	}).hide();
	
	/* 查看 */
	if ($j("#isView").val() == "false") {
		$j(".btn-update").click();
	} else {
		$j(".btn-update").hide();
		$j(".btn-return").show();
	}
	
	/* ------------------------------------------------- step-1 ------------------------------------------------- */
	//‘保存’按钮
	$j("#step-1 .btn-save").click(function() {
		if (! checkFormElementIsBlank(	//必填项不能为空
				$j(".input-mode .pro-name"),
				$j(".input-mode .pro-startTime"),
				$j(".input-mode .pro-endTime"))) {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NOT_COMPLETE"));
			return;
		} 
		
		var isEffective = $j("#isEffective").val();	//是否是生效期
		
		var name = $j.trim($j(".input-mode .pro-name").val());	//促销名称
		
		if (! isPlainString(name)) {	//名称含有非法字符
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ILLEGAL_NAME"));
			return;
		}
		
		var startTime = $j.trim($j(".input-mode .pro-startTime").val());	//促销起始时间
		var endTime = $j.trim($j(".input-mode .pro-endTime").val());	//促销结束时间
		var mark = $j(".input-mode .pro-mark").val();	//促销角标
		
		if (! compareDate(endTime, startTime)) {	//结束日期早于其实日期
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_EARLY_DATE"));
			return;
		}
		
		var id = $j("#pro-id").val();	//促销ID
		var data = nps.syncXhrPost(CHECK_NAME_URL, {id: id, name: name});
		if (! data.isSuccess) {	//促销重名
			nps.info(nps.i18n("INFO_TITLE_DATA"),data.exception.message);
			return;
		}
		
		var json = {
			id: id,
			promotionName: name,
			startTime: startTime,
			endTime: endTime,
			promotionLogoType: mark
		};
		nps.asyncXhrPost(STEP_ONE_URL, json, {successHandler : function(data, textStatus) {
			if (data.isSuccess) {
				$j("#pro-id").val(data.description);
				$j("#step-1 .text-mode .pro-name").text(name);
				$j("#step-1 .text-mode .pro-startTime").text(startTime);
				$j("#step-1 .text-mode .pro-endTime").text(endTime);
				var markTxt = $j(".input-mode .pro-mark option[value='" + mark + "']").text();
				$j("#step-1 .text-mode .pro-mark").text(markTxt);
				
				if (isEffective != "true") {
					toNextStep(1);
				} else {
					var curStep = $j("#step-1");
					curStep.find(".input-mode").hide();
					curStep.find(".text-mode").show();
					$j(".btn-update").show();
				}
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
			}
		}});
	});
	
	/* ------------------------------------------------- step-2 ------------------------------------------------- */
	//‘会员筛选器类型’下拉框
	$j("#step-2 .pro-member-type").change(function() {
		$j("#step-2 .pro-member-value").empty().append('<option value="">请选择</option>');
		var type = $j(this).val().length == 0 ? null : $j(this).val();
		if (type) {
			var data = nps.syncXhrPost(MEMBER_FILTER_LIST_URL, {type: type});
			$j("#step-2 .pro-member-value").empty().append('<option value="">请选择</option>');
			$j.each(data, function(i, elem) {
				$j("#step-2 .pro-member-value").append("<option value='" + elem.id + "'>" + elem.name + "</option>");
			});
		}
	}).change();
	
	//回填会员筛选器
	if ($j("#step-2 .pro-member-value").attr("data").length != 0) {
		var comboId = $j("#step-2 .pro-member-value").attr("data");
		$j("#step-2 .pro-member-value option[value='" + comboId + "']").prop("selected", true);
	}
	
	//‘保存’按钮
	$j("#step-2 .btn-save").click(function() {
		var comboId = $j("#step-2 .pro-member-value").val();	//筛选器ID
		if (comboId.length == 0) {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_MEMBER_FILTER"));
			return;
		}
		
		var comboType = $j("#step-2 .pro-member-type").val();
		var comboTypeTxt = $j("#step-2 .pro-member-type option[value='" + comboType + "']").text();
		var comboName = $j("#step-2 .pro-member-value option[value='" + comboId + "']").text();
		
		var id = $j("#pro-id").val();	//促销ID
		var audienceId = $j("#audience-id").val();	//人群ID
		nps.asyncXhrPost(STEP_TWO_URL, {promotionId: id, comboId: comboId, id: audienceId}, {successHandler : function(data, textStatus) {
			if (data.isSuccess) {
				$j("#audience-id").val(data.description);
				$j("#step-2 .pro-audience").text(comboName + " [" + comboTypeTxt + "]");
				
				toNextStep(2);
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
			}
		}});
	});
	
	/* ------------------------------------------------- step-3 ------------------------------------------------- */
	//‘商品筛选器类型’下拉框
	$j("#step-3 .pro-product-type").change(function() {
		$j("#step-3 .pro-product-value").empty().append('<option value="">请选择</option>');
		var type = $j(this).val().length == 0 ? null : $j(this).val();
		if (type) {
			var data = nps.syncXhrPost(PRODUCT_FILTER_LIST_URL, {type: type});
			$j.each(data, function(i, elem) {
				$j("#step-3 .pro-product-value").append("<option value='" + elem.id + "'>" + elem.name + "</option>");
			});
		}
	}).change();
	
	//回填商品筛选器
	if ($j("#step-3 .pro-product-value").attr("data").length != 0) {
		var comboId = $j("#step-3 .pro-product-value").attr("data");
		$j("#step-3 .pro-product-value option[value='" + comboId + "']").prop("selected", true);
	}
	
	//‘商品筛选器’下拉框
	$j("#step-3 .pro-product-value").change(function() {
		var comboId = $j(this).val().length == 0 ? null : $j(this).val();
		setItemSelector(comboId,3);
	}).change();
	
	//‘保存’按钮
	$j("#step-3 .btn-save").click(function() {
		var comboId = $j("#step-3 .pro-product-value").val();	//筛选器ID
		if (comboId.length == 0) {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_PRODUCT_FILTER"));
			return;
		}
		
		var comboType = $j("#step-3 .pro-product-type").val();
		var comboTypeTxt = $j("#step-3 .pro-product-type option[value='" + comboType + "']").text();
		var comboName = $j("#step-3 .pro-product-value option[value='" + comboId + "']").text();
		
		var id = $j("#pro-id").val();	//促销ID
		var scopeId = $j("#scope-id").val();	//范围ID
		var originalComboId = $j("#step-3 .pro-product-value").attr("data");
		if (scopeId.length != 0 && originalComboId != comboId) { //修改范围
			nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_UPDATE_SCOPE"), function() {
				var data = nps.syncXhrPost(DELETE_STEP_URL, {id: id});
				if (data.isSuccess) {
					$j("#step-4 tbody,#step-5 tbody").empty();
					$j("#condition-id").val("");
					$j("#setting-id").val("");
					nps.asyncXhrPost(STEP_THREE_URL, {promotionId: id, comboId: comboId, id: scopeId}, {successHandler : function(data, textStatus) {
						if (data.isSuccess) {
							$j("#scope-id").val(data.description);
							$j("#step-3 .pro-scope").text(comboName + " [" + comboTypeTxt + "]");
							$j("#step-3 .pro-product-value").attr("data", comboId);
							toNextStep(3);
							
							$j("#step-4 .input-mode .pro-condition-type").prop("disabled", false);
							STEP_4_NORMAL.find(".btn-add").show();
						} else {
							nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
						}
					}});
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
				}
			});
		} else {	//新建范围
			nps.asyncXhrPost(STEP_THREE_URL, {promotionId: id, comboId: comboId, id: scopeId}, {successHandler : function(data, textStatus) {
				if (data.isSuccess) {
					$j("#scope-id").val(data.description);
					$j("#step-3 .pro-scope").text(comboName + " [" + comboTypeTxt + "]");
					$j("#step-3 .pro-product-value").attr("data", comboId);
					toNextStep(3);
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
				}
			}});
		}
	});
	/* ------------------------------------------------- step-4 ------------------------------------------------- */
	var STEP_4_NORMAL = $j("#step-4 .input-mode .condition-normal");	//步骤4常规输入模式块
	var STEP_4_STEP = $j("#step-4 .input-mode .condition-step");	//步骤4阶梯输入模式块
	var STEP_4_CHOICE = $j("#step-4 .input-mode .condition-choice");	//步骤4选购输入模式块
	
	//‘促销条件类型’下拉框
	$j("#step-4 .input-mode .pro-condition-type").change(function() {
		var type = $j(this).val();
		$j("#step-4 [class^='ui-block-line condition-'],#step-5 [class^='ui-block-line setting-']").hide();
		$j("#step-4 .ui-block-line.condition-" + type.toLowerCase() + ",#step-5 .ui-block-line.setting-" + type.toLowerCase()).show();
	}).change(); 
	
	//‘常规-条件范围类型’下拉框
	STEP_4_NORMAL.find(".pro-condition-scope-type").change(function() {
		var type = +$j(this).val();
		STEP_4_NORMAL.find(".fuzzy").hide();
		STEP_4_NORMAL.find(".pro-condition-amount-span span").text($j(this).find("option:selected").attr("data_unit"));
		if (type == 1 || type == 2) {	//整单金额 || 整单件数
			STEP_4_NORMAL.find(".pro-condition-amount-span").show();
		} else if (type >= 3 && type <= 6) {	//商品范围XXXX
			STEP_4_NORMAL.find(".pro-normal,.pro-condition-amount-span").show();
		} else if (type == 7) {	//优惠券
			STEP_4_NORMAL.find(".pro-condition-coupon").show();
		} else if (type == 8) {	//商品范围优惠券
			STEP_4_NORMAL.find(".pro-normal,.pro-condition-coupon").show();
		} else if(type == 9 ){// 整单最大幅度
			STEP_4_NORMAL.find(".pro-condition-radius-max-span, .norInfo").show();
		} else if(type == 10){//商品范围最大幅度
			STEP_4_NORMAL.find(".pro-normal, .pro-condition-radius-max-span, .norInfo").show();
		}else if(type == 11){ // 自定义条件
			var result = nps.syncXhrPost(getCustomizeFilterClassUrl);
			var html = '<option value="p-select">请选择</option>';
			$j.each(result, function(i, obj){
				html += '<option value="'+CONDITION_EXPRESSION_ARRAY[type]+':'+obj.id+'">'+obj.scopeName+'</option>';
			});
			STEP_4_NORMAL.find(".pro-custom").html(html).show();
		}
		
		/* 倍增 */
		if (type >= 1 && type <= 6 || type == 11) {
			STEP_4_NORMAL.find(".span-double").show();
		}
		STEP_4_NORMAL.find(".span-double").find(":checkbox").prop("checked", false);
		STEP_4_NORMAL.find(".pro-condition-coupon").change();
	}).change();
	
	// ‘优惠券’下拉框
	STEP_4_NORMAL.find(".pro-condition-coupon").change(function() {
		var type = + STEP_4_NORMAL.find(".pro-condition-scope-type").val();
		var couponType = + $j(this).find("option:selected").attr("data_type");
		if ((type == 7 || type == 8) && couponType == 2) {	// 折扣类型，显示‘单件计 ’
			$j(".span-single-piece").show();
		} else {
			$j(".span-single-piece").hide();
		}
	}).change();
	
	//‘常规-添加’按钮
	STEP_4_NORMAL.find(".btn-add").click(function() {
		var tbody = STEP_4_NORMAL.find("tbody");
		var mark = STEP_4_NORMAL.find(".pro-condition-mark").val();	//‘1’/‘2’
		var markExp = mark == "1" ? "&" : "|";	//表达式逻辑符
		var markTxt = (tbody.find("tr").length == 0) ? 
				"" : STEP_4_NORMAL.find(".pro-condition-mark option:selected").text();	//‘且’/‘或’,第一行不显示
		var type = +STEP_4_NORMAL.find(".pro-condition-scope-type").val();	//子类型
		var typeTxt = STEP_4_NORMAL.find(".pro-condition-scope-type option:selected").text();	//子类型名称
		var scopeExp = STEP_4_NORMAL.find(".pro-condition-scope:visible").length > 0 ? 
				STEP_4_NORMAL.find(".pro-condition-scope:visible").val() : "";	//商品范围表达式
		var scopeTxt = STEP_4_NORMAL.find(".pro-condition-scope:visible option:selected").length > 0 ?
				STEP_4_NORMAL.find(".pro-condition-scope:visible option:selected").text() : "";	//商品范围表达式
		var number = STEP_4_NORMAL.find(".pro-condition-amount:visible").length > 0 ? 
				$j.trim(STEP_4_NORMAL.find(".pro-condition-amount:visible").val()) : "";
		var couponId = STEP_4_NORMAL.find(".pro-condition-coupon").val();
		var coupoTxt = STEP_4_NORMAL.find(".pro-condition-coupon option:selected:visible").length > 0 ?
				"：" + STEP_4_NORMAL.find(".pro-condition-coupon option:selected:visible").text() : "";
		var unitTxt = STEP_4_NORMAL.find(".pro-condition-scope-type option:selected").attr("data_unit");	//单位
		var geTxt = (type >= 1 && type <= 6 ) ? "大于等于" : "";	//大于等于
		getTxt = (type == 9 || type == 10) ? "小于等于":"";//小于等于
		if(scopeExp=="p-select"){
			nps.info(nps.i18n("INFO_TITLE_DATA"),'请选择条件');
			return;
		}	
		/* 倍增 */
		var isDouble = STEP_4_NORMAL.find(".span-double").find(":checkbox").prop("checked");	//是否倍增
		var doubleExp = isDouble ? 1 : 0;
		var doubleTxt = isDouble ? "【倍增】" : "【不倍增】";
		
		/* 单件计 */
		var isSingle = STEP_4_NORMAL.find(".span-single-piece").find(":checkbox").prop("checked");
		var singleExp = isSingle ? 3 : 2;
		var singleTxt = isSingle ? "【按单件计】" : "【不按单件计】";
		var couponType = + STEP_4_NORMAL.find(".pro-condition-coupon option:selected").attr("data_type");
		if (couponType == 1) {	// 金额类型优惠券，没有‘单件计’
			singleTxt = "";
		}
		
		if (type >= 1 && type <= 6
				&& (! checkNumber(STEP_4_NORMAL.find(".pro-condition-amount")))) {	//检查数字
			return;
		}
		
		/* 折扣时，数值必须在1-99之间 */
		if (type == 9 || type == 10) {
			number = STEP_4_NORMAL.find('.pro-condition-radius-max-span').find('.pro-condition-radius-max').val();
			if (number < discount_min || number > discount_max) {
				if(number.indexOf(".")>-1){
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("类型是折扣时，不能为小数"));
					return;
				}
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISCOUNT_NUMBER_ERROR"));
				return;
			}
		}
		
		/* 无限 */
		if (tbody.find("tr").length > 0 && tbody.find("tr").attr("data_scope_type") == 0) {
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_LIMIT"));
			return;
		}
		
		/* 检查是否范围类型和范围都重复 */
		for (var i = 0; i < tbody.find("tr").length; i++) {
			var row = tbody.find("tr").eq(i);
			//if (row.attr("data_scope_type") == (""+type) && row.attr("data_scope") == scopeExp) {
			  if (row.attr("data_scope_type") == (""+type) && row.attr("data").indexOf("("+couponId+",") > -1 && row.attr("data_scope") == scopeExp) {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SAME_CONDITION"));
				return;
			}
		}
		
		var tr = $j("<tr data=''><td></td><td></td><td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a></td></tr>");
		var exp = markExp + CONDITION_EXPRESSION_ARRAY[type];
		if(type != 11){
			exp = exp.replace(/\?/, (type == 7 || type == 8) ? couponId : number);
		}

		if ((type >= 3 && type <= 6) || type == 8 || type == 10) {	// 商品范围
			exp = exp.replace(/\?/, scopeExp);
		}else if (type == 11){
			exp = exp.replace(/\?/, scopeExp.substring(scopeExp.lastIndexOf(":")+1));
		}
		
		if ((type >= 1 && type <= 6) || type == 11) {	// 倍增
			exp = exp.replace(/\?/, doubleExp);
		} else if (type == 7 || type == 8) {	// 单件计
			exp = exp.replace(/\?/, singleExp);
		}
		
		scopeTxt = " {" + /[^-].*/.exec(scopeTxt) + "} ";	// 去掉前缀
		var txt = "";
		if (type == 0) {
			txt += typeTxt;
		} else if (type > 0 && type < 3) {
			txt += typeTxt + " " + geTxt + " " + number + " " + unitTxt + doubleTxt;
		} else if (type >= 3 && type <= 6) {
			txt += typeTxt.substring(0, 4) + " " + scopeTxt + " " + typeTxt.substring(4) + " " + geTxt + " " + number + " " + unitTxt + doubleTxt;
		} else if (type == 7) {
			txt += typeTxt + coupoTxt + singleTxt;
		} else if (type == 8) {
			txt += typeTxt.substring(0, 4) + " " + scopeTxt + " " + typeTxt.substring(4) + coupoTxt + singleTxt;
		} else if (type == 9) {
			txt += typeTxt + " " + number +"折";
		} else if ( type == 10) {
			txt += typeTxt + " " + scopeTxt + " " + number +"折";
		}else if(type == 11) {
			txt += typeTxt + " " + scopeTxt + doubleTxt;
		}
		
		
		tr.attr("data", exp).attr("data_scope_type", type).attr("data_scope", scopeExp)
			.find("td:first").text(markTxt)
			.next().text(txt);
		if (type == 0) {	//不限，提示覆盖
			if (tbody.find("tr").length != 0) {	//添加‘不限’时，若表格不为空，则提示覆盖
				nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NOLIMIT_OVERWRITE"), function() {
					tbody.find(".btn-remove").click();
					tr.find("td:first").text("");
					tr.appendTo(tbody);
					colourTbody(tbody);
					$j("#step-4 .condition-normal .btn-add").hide();
				});
			} else {
				tr.appendTo(tbody);
				colourTbody(tbody);	//上色
				$j(this).hide();
			}
		} else {
			if (type == 7 || type == 8) {	// 优惠券，关联第5步
				var clone = tr.clone().attr("data_scope_type", "");	// 将‘data_scope_type’设为空，防止第5步添加时发生冲突
				clone.find("td:first").remove();
				//clone.find("td:last").html("");
				var tbody5 = STEP_5_NORMAL.find("tbody");
				clone.appendTo(tbody5);
				colourTbody(tbody5);	//上色
			}
			
			tr.appendTo(tbody);
			colourTbody(tbody);	//上色
		}
	});
	
	//‘常规-删除’按钮
	STEP_4_NORMAL.find("tbody").on("click", ".btn-remove", function() {
		var tr = $j(this).parent().parent().remove();
		
		var tbody = STEP_4_NORMAL.find("tbody");
		tbody.find("tr:first td:first").text("");	//清空第一行第一列
		colourTbody(tbody);	//上色
		if (tbody.find("tr").length == 0) {	//表格空时，显示‘添加’按钮
			$j("#step-4 .condition-normal .btn-add").show();
		}
		
		/* 优惠券，关联第5步 */
		var type = + tr.attr("data_scope_type");
		var exp = tr.attr("data");
		if (type == 7 || type == 8) {	
			var tbody5 = STEP_5_NORMAL.find("tbody");
			tbody5.find("tr").each(function(i, dom) {
				if (exp == $j(dom).attr("data")) {
					$j(dom).remove();
					return;
				}
			});
			colourTbody(tbody5);	//上色
		}
	});
	
	//‘阶梯-条件范围类型’下拉框
	STEP_4_STEP.find(".pro-condition-scope-type").change(function() {
		var type = +$j(this).val();
		STEP_4_STEP.find(".fuzzy").hide();
		STEP_4_STEP.find(".data-unit").text($j(this).find("option:selected").attr("data_unit"));
		if (type == 1 || type == 2) {	//整单金额 || 整单件数
			STEP_4_STEP.find(".pro-condition-amount-span").show();
			STEP_4_STEP.find('.compare-string').html('大于等于');
		} else if (type >= 3 && type <= 6) {	//商品范围XXXX
			STEP_4_STEP.find(".pro-condition-scope,.pro-condition-amount-span").show();
			STEP_4_STEP.find('.compare-string').html('大于等于');
		} else if (type == 9 ){ // 整单最大幅度
			STEP_4_STEP.find(".pro-condition-amount-span, .norInfo").show();
			STEP_4_STEP.find('.compare-string').html('小于等于');
		} else if(type == 10) { //商品范围最大幅度
			STEP_4_STEP.find(".pro-condition-scope,.pro-condition-amount-span, .norInfo").show();
			STEP_4_STEP.find('.compare-string').html('小于等于');
		}
	}).change();
	//用户选择商品时 设置数据为1
	$j('#step-5').on("change",'.gift_item',function() {
		var itemid = $j(this).val();
		var itemFlag = $j(this).attr('itemFlag');
		var  sl='.'+itemFlag;
		if(startWith(itemid,"pid")){
			$j(sl).val(1);
			$j(sl).attr("readonly","readonly");
		}else{
			$j(sl).removeAttr("readonly");
			$j(sl).val(null);
		}
	});
	 if ($j.browser.msie) {
		  $('input:checkbox').click(function () { 
		   this.blur();   
		   this.focus(); 
		  });   
	};
	$j(".not-selected-nor").hide();
	$j('#step-5 .user-select-nor').live("change",function() {
		//display:none
		if($j(this).is(':checked')){
			$j(".user-selected-nor").hide();
			$j(".not-selected-nor").show();
		}else{
			$j(".user-selected-nor").show();
			$j(".not-selected-nor").hide();
		}
	});
	//
	
	$j('#step-5 .pro-condition-scope').live("change",function() {
		var itemid=$j(this).val();
		//gift_count_nor
		if(startWith(itemid,"pid")){
			$j('.gift_count_nor').val(1);
			$j('.gift_count_nor').attr("readonly","readonly");
		}else{
			$j('.gift_count_nor').removeAttr("readonly");
			$j('.gift_count_nor').val(null);
		}
		
	});
	hideUserselectInfo();
	$j('#step-5 .user-select-step').live("change",function() {
		var userselect=$j(this).attr("userselect");
		//display:none
		if($j(this).is(':checked')){
			$j(".user-selected-step"+userselect).hide();
			$j(".not-selected-step"+userselect).show();
		}else{
			$j(".user-selected-step"+userselect).show();
			$j(".not-selected-step"+userselect).hide();
		}
	});
	
	//‘阶梯-添加’按钮
	STEP_4_STEP.find(".btn-add").click(function() {
		var tbody = STEP_4_STEP.find("tbody");
		var type = +STEP_4_STEP.find(".pro-condition-scope-type").val();	//子类型
		var typeTxt = STEP_4_STEP.find(".pro-condition-scope-type option:selected").text();	//子类型名称
		var scopeExp = STEP_4_STEP.find(".pro-condition-scope:visible").length > 0 ? 
				STEP_4_STEP.find(".pro-condition-scope:visible").val() : "";	//商品范围表达式
		var scopeTxt = STEP_4_STEP.find(".pro-condition-scope:visible option:selected").length > 0 ?
				" {" + STEP_4_STEP.find(".pro-condition-scope:visible option:selected").text() + "} " : "";	//商品范围表达式
		var number = $j.trim(STEP_4_STEP.find(".pro-condition-amount:visible").val());
		var unitTxt = STEP_4_STEP.find(".pro-condition-scope-type option:selected").attr("data_unit");	//单位
		var geTxt = (type == 9 || type == 10)?"小于等于":"大于等于";
		if(scopeExp=="p-select"){
			nps.info(nps.i18n("INFO_TITLE_DATA"),'请选择商品');
			return;
		}

		/* 折扣时，数值必须在1-99之间 */
		if (type == 9 || type == 10) {
			if (number < discount_min || number > discount_max) {
				if(number.indexOf(".")>-1){
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("类型是折扣时，不能为小数"));
					return;
				}
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISCOUNT_NUMBER_ERROR"));
				return;
			}
		}
		
		if (! checkNumber(STEP_4_STEP.find(".pro-condition-amount"))) return;
		
		/* 检查类型是否相同 */
		if (tbody.find("tr").length > 0) {
			var dataExp = tbody.find("tr:first").attr("data");
			if (/^\w+/.exec(dataExp)[0] != /^\w+/.exec(CONDITION_EXPRESSION_ARRAY[type])[0]) {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_STEP_TYPE"));
				return;
			}
		}
		
		/* 检查数值是否递增 */
		for (var i = 0; i < tbody.find("tr").length; i++) {
			var row = tbody.find("tr").eq(i);
			if (+row.attr("data_number") >= +number) {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_STEP_NUMBER"));
				return;
			}
		}
		
		var tr = $j("<tr data=''><td></td><td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a></td></tr>");
		var exp = CONDITION_EXPRESSION_ARRAY[type].replace(/\?/, number);
		if ((type >= 3 && type <= 6) || type == 10) {
			exp = exp.replace(/\?/, scopeExp);	//条件表达式
		}
		
		/* 倍增 */
		exp = exp.replace(/\?/, "0");	// 阶梯都不倍增
		
		var txt = typeTxt + scopeTxt + geTxt + number + unitTxt;
		tr.attr("data", exp).attr("data_number", number).find("td:first").text(txt);
		tr.appendTo(tbody);
		colourTbody(tbody);	//上色
		
		/* 与第5步联动的阶梯表格联动 */
		var settingBody = $j("#step-5 .input-mode .setting-step tbody");
		clone = tr.clone(true);
		clone.find("td:last").remove();
		clone.append($j("<td></td>").append($j("#loxia-number-template").clone(true).show()));
		settingBody.append(clone);	
		colourTbody(settingBody);	//上色
		
		//礼品标识生成
		var flag = item_select_flag++;
		var item_select="<select loxiaType='select' itemFlag='item"+flag+"' class='pro-condition-scope-gift  gift_item ui-loxia-default ui-corner-all ui-loxia-number'></select>";
		var info =$j("<div class='user-selected-stepuserselect"+flag+"'>推送礼品样数</div><div class='not-selected-stepuserselect"+flag+"'>最多礼品样数</div>");
		var user_selected="<input type='checkbox' class='user-select user-select-step' userselect='userselect"+flag+"'/>";
		//var user_select_help=$j("#user-select-help-js").clone(true).show();
		//"<a href='javascript:void(0)' id='user-select-help'><img src='../images/main/qs.png' style='width: 20px;' ></a>";
		var gift_tr = $j("<tr><td>"+txt+"</td><td>"+item_select+"</td><td>"+user_selected+"</td></tr>");
		gift_tr.attr("data", exp).attr("data_number", number);
		var settingBody_gift = $j("#step-5 .input-mode .setting-step-gift tbody");
		
		var clone_gift = gift_tr.clone(true);
		var num_limit=$j("#loxia-number-template");
		if(num_limit.hasClass("item"+(flag-1))){
			num_limit.removeClass("item"+(flag-1));
		}
		num_limit.addClass("item"+flag);
		clone_gift.append($j("<td></td>").append(info.clone(true)).append(num_limit.clone(true).show()));
		settingBody_gift.append(clone_gift);	
		colourTbody(settingBody_gift);	//上色
		
		STEP_4_STEP.find(".pro-condition-scope-type").prop("disabled", true);	//禁用
		STEP_4_STEP.find(".pro-condition-scope").prop("disabled", true);	//禁用
	});
	
	//‘阶梯-删除’按钮
	STEP_4_STEP.find("tbody").on("click", ".btn-remove", function() {
		var curTr = $j(this).parent().parent();
		curTr.remove();
		
		var tbody = STEP_4_STEP.find("tbody");
		colourTbody(tbody);	//上色
		if (tbody.find("tr").length == 0) {	//表格空时，启用类型下拉框
			$j("#step-4 .condition-step .pro-condition-scope-type").prop("disabled", false);	//禁用
			$j("#step-4 .condition-step .pro-condition-scope").prop("disabled", false);	//禁用
		}
		
		/* 与第5步联动的阶梯表格联动 */
		var settingBody = $j("#step-5 .input-mode .setting-step tbody");
		settingBody.find("tr").each(function(i, dom) {
			if ($j(dom).attr("data_number") == curTr.attr("data_number")) {
				$j(dom).remove();
				return;
			}
		});
		var settingBodyGift = $j("#step-5 .input-mode .setting-step-gift tbody");
		settingBodyGift.find("tr").each(function(i, dom) {
			if ($j(dom).attr("data_number") == curTr.attr("data_number")) {
				$j(dom).remove();
				return;
			}
		});
		colourTbody(settingBody);	//上色
	});

	//‘选购-添加’按钮
	STEP_4_CHOICE.find(".btn-add").click(function() {
		var tbody = STEP_4_CHOICE.find("tbody");
		var type = STEP_4_CHOICE.find(".pro-condition-choice-type").val();	//子类型
		var typeTxt = STEP_4_CHOICE.find(".pro-condition-choice-type option:selected").text();	//子类型名称
		var scopeExp = STEP_4_CHOICE.find(".pro-condition-scope:visible").length > 0 ? 
				STEP_4_CHOICE.find(".pro-condition-scope:visible").val() : "";	//商品范围表达式
		var scopeTxt = STEP_4_CHOICE.find(".pro-condition-scope:visible").find('option:selected').length > 0 ?
				" {" + STEP_4_CHOICE.find(".pro-condition-scope:visible").find('option:selected').text() + "} " : "";	//商品范围表达式
		var number = STEP_4_CHOICE.find(".pro-condition-amount:visible").length > 0 ? 
				$j.trim(STEP_4_CHOICE.find(".pro-condition-amount:visible").val()) : "";
		var unitTxt = "件";	//单位
		var geTxt = "大于等于";	//大于等于
		if(scopeExp=="p-select"){
			nps.info(nps.i18n("INFO_TITLE_DATA"),'请选择商品');
			return;
		}	
		var comboType = $j("#step-3 .pro-product-type").val();
		if(comboType==2){
			nps.info(nps.i18n("INFO_TITLE_DATA"),"分类类型的筛选器不能作为套餐商品");
			return;
		}
		if(comboType==1){
			if(startWith(scopeExp,"cmbid")){
				nps.info(nps.i18n("INFO_TITLE_DATA"),"主商品或选购商品不能选择商品筛选器总范围");
				return;
			}
		}
		if(comboType==4){
			var scope = STEP_4_CHOICE.find(".pro-condition-scope").find("option").eq(1).val();
			if(scope==scopeExp){
				nps.info(nps.i18n("INFO_TITLE_DATA"),"主商品或选购商品不能选择商品筛选器总范围");
				return;
			}
		}
		if (! checkNumber(STEP_4_CHOICE.find(".pro-condition-amount"))) return;
	
		var ttr= tbody.find("tr").length;
		var itemExp=null ;
		if(type == 'addtprd'){
			if(startWith(scopeExp,"cmbid")){
				var cmbid = scopeExp.substring(scopeExp.indexOf(":")+1,scopeExp.length);
				//判断类型是否是商品类型
				var data = nps.syncXhrPost(PRODUCT_FILTER_OBJECT_URL, {id: cmbid});
				itemExp = data.combo.expression;
				if(data.combo!=null  && data.combo.type!=1){
					nps.info(nps.i18n("INFO_TITLE_DATA"),"选购商品，请选择具体的商品或商品筛选器！");
					return;
				}
			}else{
				if(!startWith(scopeExp,"pid")){
					nps.info(nps.i18n("INFO_TITLE_DATA"),"选购商品，请选择具体的商品或商品筛选器！");
					return;
				}
			}
			
		}
		
		//检查主商品是否添加
		for (var i = 0; i < ttr  ; i++) {
			var row = tbody.find("tr").eq(i);
			if (row.attr("data_type") == type && type == 'prmprd') {
				nps.info(nps.i18n("INFO_TITLE_DATA"),"主商品已添加!");
				return;
			}
		}
		//获取主商品范围
		var prmprdScope =null;
		for (var i = 0; i < ttr  ; i++) {
			var row = tbody.find("tr").eq(i);
			if (row.attr("data_type")  == 'prmprd') {
				prmprdScope = row.attr("data_scope");
				break;
			}
		}
	
		if(prmprdScope != null || prmprdScope!=""){
			if(prmprdScope==scopeExp){
				nps.info(nps.i18n("INFO_TITLE_DATA"),"主商品与选购商品不能相同");
				return;
			}
		}
		
		if(prmprdScope !=null && startWith(prmprdScope,"cmbid")){
			var comboId = prmprdScope.substring(prmprdScope.indexOf(":")+1,prmprdScope.length);
			var itemIds = null;
			if(itemExp != null){
				itemIds = itemExp.substring(itemExp.indexOf("(")+1,itemExp.indexOf(")"));
			}else{
				itemIds = scopeExp.substring(scopeExp.indexOf(":")+1,scopeExp.length);	
			}
			var json = {"comboId":comboId,"itemIds":itemIds};
			var url = base +"/promotion/validateChooseItem.json";
			var result = nps.syncXhrPost(url, json);
			if(result != null && result!=""){
				nps.info(nps.i18n("INFO_TITLE_DATA"),"选购商品不能包含在主商品范围内:"+result.description);
				return;
			}
		}
		var tprdnum =0;
		//选购商品最多只能添加5组
		for (var i = 0; i < ttr ; i++) {
			var row = tbody.find("tr").eq(i);
			if (row.attr("data_type") == 'addtprd') {
				++tprdnum;
			}
			if(tprdnum==5){
				nps.info(nps.i18n("INFO_TITLE_DATA"),"选购商品最多只能添加5组!");
				return;
			}
		}
		/* 检查是否范围都重复 */
		for (var i = 0; i < tbody.find("tr").length; i++) {
			var row = tbody.find("tr").eq(i);
			if (row.attr("data_scope") == scopeExp) {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SAME_CONDITION"));
				return;
			}
		}
		
		var tr = $j("<tr data=''><td></td><td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a></td></tr>");
		var exp = CONDITION_EXPRESSION_ARRAY[6]
			.replace(/\?/, number)
			.replace(/\?/, scopeExp)	//条件表达式
			/* 倍增 */
			.replace(/\?/, "1");	// 选购都倍增
		
		var txt = typeTxt + scopeTxt + geTxt + number + unitTxt;
		tr.attr("data_type", type).attr("data", exp).attr("data_scope", scopeExp)
			.find("td:first").text(txt);
		tr.appendTo(tbody);
		colourTbody(tbody);	//上色
		
		/* 与第5步联动的阶梯表格联动 */
		var settingBody = $j("#step-5 .input-mode .setting-choice tbody");
		//if (tr.attr("data_type") == "addtprd") {	//只添加选购商品
		var clone = tr.clone(true);
		clone.find("td:last").remove();
		var sltType = $j("#step-5 .input-mode .setting-step .pro-setting-scope-type").clone();
		clone.append($j("<td></td>").append(sltType).append($j("#loxia-number-template").clone(true).show()));
		settingBody.append(clone);	
		colourTbody(settingBody);	//上色
		//}
	});
	
	//‘选购-删除’按钮
	STEP_4_CHOICE.find("tbody").on("click", ".btn-remove", function() {
		var curTr = $j(this).parent().parent();
		curTr.remove();
		colourTbody(STEP_4_CHOICE.find("tbody"));	//上色
		
		/* 与第5步联动的阶梯表格联动 */
		var settingBody = $j("#step-5 .input-mode .setting-choice tbody");
		//if (curTr.attr("data_type") == "addtprd") {	//选购商品
		settingBody.find("tr").each(function(i, dom) {
			if ($j(dom).attr("data") == curTr.attr("data")) {
				$j(dom).remove();
				return;
			}
		});
		colourTbody(settingBody);	//上色
		//}
	});

	//TODO 何波  优惠价格验证 ，优惠金额必须小于商品列表中的最小金额
	function validatePrice(number,type){
		//该校验经常影响商城开展业务，故将其短路。
		return true;
		
		var types=['1','3','5','7'];
		var  exit = false;
		for ( var i = 0; i < types.length && !exit; i++) {
			var arrEle = types[i];
			if(arrEle == type){
				exit = true;
			}
		}
		//不做校验
		if(exit == false){
			return  true;
		}
		var url = base + "/promotion/getItemsByCateIds.json";
		
		//var protype = $j("#step-4 .input-mode .pro-condition-type").val();	//条件类型
		//整单优惠
		if(type==1){
			//if(1 == pro_product_type){
				var includeItems = getIncludeItemsPrice();
				if(includeItems.length == 0){
					return  true;
				}
				//var cateIds =includeItems.join(",");
				var pro_condition_scope = $j('#step-3 .pro-product-value').find("option:selected").val();
				var json={"inProItems":"cmbid:"+pro_condition_scope,"proType":2};
				var minPrice = nps.syncXhrPost(url, json);
				//将categoryIds传输到服务端获取最小价格
				if(number >= minPrice){
					nps.info(nps.i18n("INFO_TITLE_DATA"),"优惠金额必须小于商品中最低价格:"+minPrice);
					return false;
				}
				
		}else{
			var pro_condition_scope;
//			if("Choice"==protype){
//				var tbody = $j("#step-4 .input-mode .condition-choice tbody");	//表格体
//				var deputyList = tbody.find("tr[data_type='addtprd']");	//选购商品列表
//				pro_condition_scope =deputyList.attr("data_scope");
//			}else{
				pro_condition_scope = $j('.setting-normalstep .pro-condition-scope').find("option:eq(1)").val();
			//}
			//范围和件数优惠
			var json={"inProItems":pro_condition_scope,"proType":2};
			var minPrice = nps.syncXhrPost(url, json);
			//将categoryIds传输到服务端获取最小价格
			if(number >= minPrice){
				nps.info(nps.i18n("INFO_TITLE_DATA"),"优惠金额必须小于商品中最低价格:"+minPrice);
				return false;
			}
		}
		
		
	}
	//获取排除列表商品价格
	function getIncludeItemsPrice(){
		var tbl_include = $j(".tbl-include").find('tbody');
		return getPriceSort(tbl_include);
	}
	//获取价格并排序
	function getPriceSort(itemstbody){
		var itemsprices = new Array();
		for (var i = 0; i < itemstbody.find("td").length; i++) {
			var  td = itemstbody.find("td").eq(i);
			//获取筛选器名称
			if(i%3==0){
				itemsprices.push(td.text());
			}
		}
		//排序
		return itemsprices;
	}
	//对象比较
	function compareObj(obj1,obj2){
		if(obj1.cp == obj2.cp){
			return  true;
		}
		return false;
		
	}
	
	//‘保存’按钮
	$j("#step-4 .btn-save").click(function() {
		var tbody = $j("#step-4 .input-mode tbody:visible");	//表格体
		if (! checkBeforeSave(tbody, "CONDITION")) {	//检查表格数据正确性
			return;
		}
		if ($j("#step-4 .input-mode .pro-condition-type").prop("disabled")) {
			saveStep4();
		} else {
			nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CONDITION_TYPE_FROZEN"), function() {
				saveStep4();
			});
		}
	});
	/* ------------------------------------------------- step-5 ------------------------------------------------- */
	var STEP_5_NORMAL = $j("#step-5 .input-mode .setting-normal");	//第5步常规
	
	//常规 - 优惠类型下拉框
	STEP_5_NORMAL.find(".pro-setting-scope-type").change(function() {
		STEP_5_NORMAL.find(".fuzzy").hide();
		var type = +$j(this).val();	//类型
		var unit = $j(this).children("option:selected").attr("data_unit");	//单位
		if (type <= 2) {	//运费，整单XX
			$j(this).siblings(".pro-setting-amount,.span-setting-unit-" + unit).show();
		} else if ((type >= 3 && type <= 8)) {	//商品范围XXXX
			$j(this).siblings(".pro-condition-scope,.pro-setting-amount,.span-setting-unit-" + unit).show();
		} else if (type == 12) {	//商品范围优惠价
			$j(this).siblings(".pro-condition-scope").show();
		} else if (type == 13) {	//商品范围优惠价
			var result = nps.syncXhrPost(getCustomizeSettingClassUrl);
			var html = '<option value="p-select">请选择</option>';
			$j.each(result, function(i, obj){
				html += '<option value="'+obj.id+'">'+obj.scopeName+'</option>';
			});
			STEP_5_NORMAL.find(".setting-custom").html(html).show();
		}else if (type == 9) {	//赠品
			$j(this).siblings(".pro-condition-scope").show();
			$j(this).siblings(".span-setting-user-select ").show();
			$j(this).siblings(".span-setting-item-count").show();
			$j(this).siblings(".span-setting-user-select-info").show();
		}
		
		/* 单件计 */
		if (type == 4 || type == 6 || type == 8 || type == 9) {
			$j(this).siblings(".span-setting-single-piece").show();
		}
		$j(this).siblings(".span-setting-single-piece").find(":checkbox").prop("checked", false);
	}).change();
	
	//step-5 常规 - ‘添加’按钮
	STEP_5_NORMAL.find(".btn-add").click(function() {
		var type = +$j(this).siblings(".pro-setting-scope-type").val();	//类型
		if (type <= 8 && (! checkNumber($j(this).siblings(".pro-setting-amount")))) return;	//检查数字
		var tbody = STEP_5_NORMAL.find("tbody");
		var number = $j(this).siblings(".pro-setting-amount:visible").length > 0 ?
				$j.trim($j(this).siblings(".pro-setting-amount").val()) : "";
		var typeTxt = $j(this).siblings(".pro-setting-scope-type").children("option:selected").text();	//子类型名称
		var tr = $j("<tr data=''><td></td><td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a></td></tr>");
		var scopeExp = $j(this).siblings(".pro-condition-scope:visible").length > 0 ?
				$j(this).siblings(".pro-condition-scope").val() : "";	//商品范围表达式
		var scopeExpSetting = $j(this).siblings(".setting-custom:visible").length > 0 ?
				$j(this).siblings(".setting-custom").val() : "";//自定义下拉
	
		if(scopeExp=="p-select"){
			nps.info(nps.i18n("INFO_TITLE_DATA"),'请选择商品');
			return;
		}

		if(scopeExpSetting=="p-select"){
			nps.info(nps.i18n("INFO_TITLE_DATA"),'请选择自定义优惠');
			return;
		}				
				
		var scopeTxt = $j(this).siblings(".pro-condition-scope:visible").length > 0 ?
				$j(this).siblings(".pro-condition-scope").children("option:selected").text() : "";	//商品范围表达式
				
		/* 单件计 */
		var isSingle = $j(this).siblings(".span-setting-single-piece").find(":checkbox").prop("checked");	
		var singleExp = isSingle ? 3 : 2;
		var singleTxt = isSingle ? "【按单件计】" : "【不按单件计】";
		//TODO 何波  获取包含列表中商品价格
		
		var types=['0','1','3','5','7'];
		var  exit = false;
		for ( var i = 0; i < types.length && !exit; i++) {
			if(types[i] == type){
				exit = true;
			}
		}
		var countreg=/^[0-9]+([.]{1}[0-9]{1,2})?$/;
    	if(exit && countreg.test(number)==false){
    		 nps.info("提示信息","优惠金额必须是整数或小数精度为2位");
    		return;
    	}
		if(validatePrice(number,type) == false){
			return;
		}
		/* 折扣时，数值必须在1-99之间 */
		var isDiscount = $j(this).siblings(".pro-setting-scope-type").children("option:selected").attr("data_unit") == "discount";
		if (isDiscount) {
			if (number < discount_min || number > discount_max) {
				if(number.indexOf(".")>-1){
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("类型是折扣时，不能为小数"));
					return;
				}
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISCOUNT_NUMBER_ERROR"));
				return;
			}
		}
		
		scopeTxt = /[^-].*/.exec(scopeTxt);	// 去掉前缀
		var user_count=null;
		if(type==9){
			user_count=$j(".span-setting-item-count .gift_count").val();
			if(user_count==null || user_count==""){
				nps.info(nps.i18n("INFO_TITLE_DATA"),'请输入推送礼品样数或用户选择样数');
				return;
			}
			if(user_count >3 ){
				nps.info(nps.i18n("INFO_TITLE_DATA"),'推送礼品样数或用户选择样数不能大于3');
				$j(".span-setting-item-count .gift_count").focus();
				return;
			}
		}
		var exp = "";	//表达式
		var txt = "";	//表达式文本解释
		switch (type) {
		case 0:	//整单免运费
			exp = "freeship(" + number + ",2)";	
			txt = typeTxt + " " + number + " 元 ";
			break;
		case 1:	//整单优惠
			exp = "orddisc(" + number + ",2)";	
			txt = typeTxt + " " + number + " 元 ";
			break;
		case 2:	//整单折扣
			exp = "ordrate(" + number + ",2)";	
			txt = typeTxt + " " + number + " 折";
			break;
		case 3:	//商品范围整单优惠
			exp = "scporddisc(" + number + "," + scopeExp + ",2)";		
			txt = typeTxt.substring(0, 4) + " {" + scopeTxt + "} " + typeTxt.substring(4) + " " + number + " 元";
			break;
		case 4:	//商品范围整单折扣
			exp = "scpordrate(" + number + "," + scopeExp + "," + singleExp + ")";	
			txt = typeTxt.substring(0, 4) + " {" + scopeTxt + "} " + typeTxt.substring(4) + " " + number + " 折" + singleTxt;
			break;
		case 5:	//商品范围单品优惠
			exp = "scpprddisc(" + number + "," + scopeExp + ",2)";		
			txt = typeTxt.substring(0, 4) + " {" + scopeTxt + "} " + typeTxt.substring(4) + " " + number + " 元";
			break;
		case 6:	//商品范围单品折扣
			exp = "scpprdrate(" + number + "," + scopeExp + "," + singleExp + ")";	
			txt = typeTxt.substring(0, 4) + " {" + scopeTxt + "} " + typeTxt.substring(4) + " " + number + " 折" + singleTxt;
			break;
		case 7:	//商品范围单件优惠
			exp = "scppcsdisc(" + number + "," + scopeExp + ",2)";		
			txt = typeTxt.substring(0, 4) + " {" + scopeTxt + "} " + typeTxt.substring(4) + " " + number + " 元";
			break;
		case 8:	//商品范围单件折扣
			exp = "scppcsrate(" + number + "," + scopeExp + "," + singleExp + ")";	
			txt = typeTxt.substring(0, 4) + " {" + scopeTxt + "} " + typeTxt.substring(4) + " " + number + " 折" + singleTxt;
			break;
		case 12:	//商品范围优惠价scpmrkdnprice(cmbid:21，0)
			exp = "scpmrkdnprice(" + scopeExp + ",0)";		
			txt = typeTxt.substring(0, 4) + " {" + scopeTxt + "} " + typeTxt.substring(4) + "，请确认优惠价设置！";
			break;
		case 13:	//自定义优惠设置cstset(1,1)			
			scopeExp = STEP_5_NORMAL.find(".setting-custom:visible option:selected").length > 0 ? 
					STEP_5_NORMAL.find(".setting-custom:visible option:selected").val() : "";	//自定义下拉
			scopeTxt = STEP_5_NORMAL.find(".setting-custom:visible option:selected").length > 0 ?
					STEP_5_NORMAL.find(".setting-custom:visible option:selected").text() : "";	//自定义下拉
					
			exp = "cstset(" + scopeExp + ",1)";		
			txt = typeTxt.substring(0, 5) + " {" + scopeTxt + "} " + typeTxt.substring(5) + "，请确认优惠价设置！";	
			break;			
		case 9:	//赠品
			//scpgift(1,cmbid:21,2，0，1)
			var comboType = $j("#step-3 .pro-product-type").val();
			if(comboType==2){
				nps.info(nps.i18n("INFO_TITLE_DATA"),"分类筛选器不能作为赠品");
				return;
			}
			if(startWith(scopeExp,"call")){
				nps.info(nps.i18n("INFO_TITLE_DATA"),"赠品不能为全场");
				return;	
			}
			//判断类型是否是商品类型
			if(!startWith(scopeExp,"pid")){
				var cmbid = scopeExp.substring(scopeExp.indexOf(":")+1,scopeExp.length);
				var data = nps.syncXhrPost(PRODUCT_FILTER_OBJECT_URL, {id: cmbid});
				if(data.combo!=null  && data.combo.type!=1){
					nps.info(nps.i18n("INFO_TITLE_DATA"),"赠品只能是商品筛选器或具体的商品");
					return;	
				}
			}
			var user_select;
			var user_select_msg="推送礼品样数:";
			if($j(".span-setting-user-select .user-select").is(':checked')) {
				user_select=1;
				user_select_msg="最多礼品样数:";
			}else{
				user_select=0;
			}
			exp = "scpgift(" + 1 + "," + scopeExp + "," + singleExp + ","+user_select+","+user_count+")";		
			txt = typeTxt + " {" + scopeTxt + "} " + singleTxt+"["+user_select_msg+user_count+"]";
			break;
		}
		
		/* 检查是否范围类型和范围都重复 */
		for (var i = 0; i < tbody.find("tr").length; i++) {
			var row = tbody.find("tr").eq(i);
			if(type==9 && row.attr("data_scope_type") == ("" +type)){
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("赠品类型只能添加一项"));
				return;
			}
			if(type==13 && row.attr("data_scope_type") == ("" +type)){
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("自定义优惠只能添加一项"));
				return;
			}
			if (row.attr("data_scope_type") == ("" +type) && row.attr("data_scope") == scopeExp) {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SAME_SETTING"));
				return;
			}
		}
		
		exp = "&" + exp;
		tr.attr("data", exp).attr("data_scope_type", type).attr("data_scope", scopeExp)
			.find("td:first").text(txt);
		tr.appendTo(tbody);
		colourTbody(tbody);	//上色
	});
	
	//常规 - ‘删除’按钮
	STEP_5_NORMAL.find("tbody").on("click", ".btn-remove", function() {
		$j(this).parent().parent().remove();
		colourTbody(STEP_5_NORMAL.find("tbody"));	//上色
	});
	
	var STEP_5_STEP = $j("#step-5 .input-mode .setting-step");	//第5步阶梯
	
	$j("#step-5 .input-mode").on("blur", ".pro-setting-amount", function() {
		var number = $j.trim($j(this).val());
		if (/^[1-9]+\d*$/.test(number)) {
			$j(this).removeClass("ui-loxia-error");
		} else {
			$j(this).addClass("ui-loxia-error");
		}
	});
	
	//阶梯-优惠设置 回填
	if ($j("#step-scope").val().length > 0) {
		STEP_5_STEP.find(".pro-condition-scope option[value='" + $j("#step-scope").val() + "']").prop("selected", true);
	}
	//根据类型显示对于的金额   礼品
	 var tab_selected = $j("#tag-change-in-gift .pro-condition-scope:eq(0)").attr("data");
	 if(startWith(tab_selected,'scpgift')){
		 $j(".ui-tag-change").each(function(){
			if(!$j(this).find(".tag-change-ul").find("li").eq(1).hasClass("selected")){
				$j(this).find(".tag-change-ul").find("li").eq(1).addClass("selected");
				$j(this).find(".tag-change-ul").find("li").eq(0).removeClass("selected");
				$j(this).find(".tag-change-content").find(".tag-change-in").eq(1).addClass("block");
				$j(".tag-change-in").eq(1).addClass("block").siblings(".tag-change-in").removeClass("block");
			}
		});
	 }
	 

	//‘保存’按钮
	$j("#step-5 .btn-save").click(function() {
		var id = $j("#pro-id").val();	//促销ID
		var settingId = $j("#setting-id").val();	//优惠ID
		var type = $j("#step-4 .input-mode .pro-condition-type").val();	//条件类型
		var tbody = $j("#step-5 .input-mode tbody:visible");
		
		var exp = "";	//表达式
		var expTxt = "";
		 //金额/折扣   礼品 标识
		var amountFlag =$j('li.memberbase.amount.selected').text();
		var proType = STEP_5_STEP.find(".pro-setting-scope-type").val();	//范围类型
		// 何波  获取包含列表中商品价格 start 
		if('Normal'!==type && amountFlag!=""){
			if(type!="Choice"){
				var number = $j('.setting-normalstep .pt10 .pro-setting-amount').val();
				var countreg=/^[0-9]+([.]{1}[0-9]{1,2})?$/;
				var types=['0','1','3','5','7'];
				var  exit = false;
				for ( var i = 0; i < types.length && !exit; i++) {
					if(types[i] == type){
						exit = true;
					}
				}
				
		    	if(exit && countreg.test(number)==false){
		    		 nps.info("提示信息","优惠金额必须是整数或小数精度为2位");
		    		return;
		    	}
				if(validatePrice(number,proType) == false){
					return;
				}
			}
		}
		//何波  获取包含列表中商品价格 end 
		switch (type) {
		case "Normal":	//常规条件
			if (! checkBeforeSave(tbody, "SETTING")) {	//检查表格数据正确性
				return;
			}
			tbody.find("tr").each(function(i, dom) {
				exp += $j(dom).attr("data");
				expTxt += "\n且" + $j(dom).find("td:first").text();
			});
			exp = exp.substring(1);
			expTxt = expTxt.substring(2);
			var json = {
				id: settingId,
				promotionId: id,
				settingName: expTxt,
				settingExpression: exp,
				conditionType: type
			};
			nps.asyncXhrPost(STEP_FIVE_URL, json, {successHandler : function(data, textStatus) {
				if (data.isSuccess) {
					$j("#setting-id").val(data.description);
					var txtBody = $j("#step-5 .text-mode .setting-normal tbody");
					txtBody.empty();
					tbody.find("tr").each(function(i, dom) {
						clone = $j(dom).clone();
						clone.find("td:last").remove();
						txtBody.append(clone);
					});
					toNextStep(5);
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
				}
			}});
			break;
		case "Step":	//阶梯条件
			//验证数值是否递增
			var numberArr = [];
			var itemNameArr = [];
			var item_user_select = [];
			var item_gift_count = [];
			var isError = false;
			if(amountFlag!=""){
				tbody.find("input").each(function(i, dom) {
					if (! checkNumber($j(dom))) {	// TODO 数字验证 红色框
						isError = true;
						return; 	
					}
					numberArr[i] = +$j(dom).val();
					
					if (STEP_5_STEP.find(".pro-setting-scope-type option:selected").attr("data_unit") == "rmb") {	//金额要递增
						if (i > 0 && numberArr[i] <= numberArr[i-1]) {
							nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_STEP_NUMBER"));
							isError = true;
							return;
						}
					} else {	//折扣要递减
						if (numberArr[i] < discount_min || numberArr[i] > discount_max) {	// 折扣时，数值必须在0-99之间
							if(numberArr[i].indexOf(".")>-1){
								nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("类型是折扣时，不能为小数"));
								isError = true;
								return;
							}
							nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISCOUNT_NUMBER_ERROR"));
							isError = true;
							return;
						}
						if (i > 0 && numberArr[i] >= numberArr[i-1] && (numberArr[i-1]!=0 || numberArr[0]!=0)) {
							nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_STEP_DISCOUNT"));
							isError = true;
							return;
						}
					}
					
				});
			}else{
				$j('.gitf_tab').find("select").each(function(i, dom) {
					var user_select_item= $j(dom).val() ;
					if(user_select_item=="p-select"){
						nps.info(nps.i18n("INFO_TITLE_DATA"),'请选择赠品');
						isError = true;
						return;
					}
					//判断赠品是否是商品
					var comboType = $j("#step-3 .pro-product-type").val();
					if(comboType==2){
						nps.info(nps.i18n("INFO_TITLE_DATA"),"分类筛选器不能作为赠品");
						isError = true;
						$j(dom).addClass("ui-loxia-error");
						return;
					}
					if(startWith(user_select_item,"call")){
						nps.info(nps.i18n("INFO_TITLE_DATA"),"赠品不能为全场");
						$j(dom).addClass("ui-loxia-error");
						isError = true;
						return;	
					}
					//判断类型是否是商品类型
					if(!startWith(user_select_item,"pid")){
						var cmbid = user_select_item.substring(user_select_item.indexOf(":")+1,user_select_item.length);
						var data = nps.syncXhrPost(PRODUCT_FILTER_OBJECT_URL, {id: cmbid});
						if(data.combo!=null && data.combo.type!=1){
							nps.info(nps.i18n("INFO_TITLE_DATA"),"赠品只能是商品筛选器或具体的商品");
							isError = true;
							$j(dom).addClass("ui-loxia-error");
							return;	
						}
					}
					
					numberArr[i] =user_select_item;
					itemNameArr[i]=$j(dom).find("option:selected").text();
				});
				$j('.gitf_tab').find("input[type=checkbox]").each(function(i, dom) {
					if($j(dom).is(':checked')) {
						item_user_select[i]=1;
					}else{
						item_user_select[i]=0;
					}
				});
				var gift_count = $j('.gitf_tab').find(".pro-setting-amount");
				if(gift_count.length==0){
					gift_count=$j('.gitf_tab').find(".gift_count");
				}
				gift_count.each(function(i, dom) {
					var gift_count= $j(dom).val();
					if(gift_count==null || gift_count==""){
						nps.info(nps.i18n("INFO_TITLE_DATA"),'请输入推送礼品样数或用户选择样数');
						isError = true;
						return;
					}
					if(gift_count >3 ){
						nps.info(nps.i18n("INFO_TITLE_DATA"),'推送礼品样数或用户选择样数不能大于3');
						$j(dom).focus();
						isError = true;
						return;
					}
					item_gift_count[i]=gift_count;
				});
				
			}
			//验证没通过
			if (isError){
				return;
			}
			var scopeType = STEP_5_STEP.find(".pro-setting-scope-type").val();	//范围类型
			var typeTxt = STEP_5_STEP.find(".pro-setting-scope-type option:selected").text();
			var scopeExp = STEP_5_STEP.find(".pro-condition-scope").val();	//商品范围
			var scopeTxt = STEP_5_STEP.find(".pro-condition-scope").text();
			if(scopeTxt!=null && scopeTxt!=""){
				scopeTxt = scopeTxt.substring("请选择".length,scopeTxt.indexOf("[筛选器]")+"[筛选器]".length);
			}
			var typeExp = SETTING_EXPRESSION_ARRAY[scopeType];	
			if(amountFlag!=""){
				if(scopeExp=="p-select"){
					nps.info(nps.i18n("INFO_TITLE_DATA"),'请选择商品');
					return;
				}	
				for (var i = 0; i < numberArr.length; i++) {
					exp += "\n" + typeExp + "(" + numberArr[i] + "," + scopeExp + ",0)"; 
					expTxt += "\n" + typeTxt.substring(0, 4) + " {" + scopeTxt + "} " + typeTxt.substring(4) + " " + numberArr[i] + " " 
						+ (STEP_5_STEP.find(".pro-setting-scope-type option:selected").attr("data_unit") == "rmb" ? "元" : "折");
				}
			}else{
				//scpgift(1,cmbid:21,1，0，1)
				for (var i = 0; i < numberArr.length; i++) {
					var  gift_id = numberArr[i] ;
					exp += "\n" + "scpgift(" + "1," + gift_id +",2,"+item_user_select[i] +","+item_gift_count[i]+")"; 
					var msg = "推送礼品样数:";
					if(item_user_select[i]==1){
						msg="最多礼品样数:";
					}
					expTxt += "\n" + typeTxt.substring(0, 4) + " {" + scopeTxt + "} " + typeTxt.substring(4) + 
					"[礼品] " + itemNameArr[i]+"["+msg+item_gift_count[i]+"]";
				}
			}
			
			exp = exp.substring(1);
			expTxt = expTxt.substring(1);
			var json = {
					id: settingId,
					promotionId: id,
					settingName: expTxt,
					settingExpression: exp,
					conditionType: type
			};
			nps.asyncXhrPost(STEP_FIVE_URL, json, {successHandler : function(data, textStatus) {
				if (data.isSuccess) {
					$j("#setting-id").val(data.description);
					var txtBody = $j("#step-5 .text-mode .setting-step tbody");
					txtBody.empty();
					$j.each(expTxt.split("\n"), function(i, txt) {
						txtBody.append("<tr><td>" + txt + "</td></tr>");
					});
					colourTbody(txtBody);
					toNextStep(5);
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
				}
			}});
			break;
		case "Choice":	//选购条件
			var isError = false;
			tbody.find("input").each(function(i, dom) {	
				if (! checkNumber($j(dom))) {	// TODO 数字验证 红色框
					isError = true;
					return; 	
				}
			});
			if (isError) return;
			
			tbody.find("tr").each(function(i, dom) {
				var scopeType = $j(dom).find(".pro-setting-scope-type").val();	//范围类型
				var typeTxt = $j(dom).find(".pro-setting-scope-type option:selected").text();
				var scopeExp = $j(dom).attr("data_scope");	//商品范围
				var scopeTxt = /{.*}/.exec($j(dom).find("td:first").text());
				var typeExp = SETTING_EXPRESSION_ARRAY[scopeType];	
				var number = $j.trim($j(dom).find("td:last input").val());
				var countreg=/^[0-9]+([.]{1}[0-9]{1,2})?$/;
		    	if(countreg.test(number)==false){
		    		 nps.info("提示信息","优惠金额必须是整数或小数精度为2位");
		    		 isError = true;
		    		return;
		    	}
				if(validatePrice(number,proType) == false){
					isError = true;
					return;
				}
				exp += "\n" + typeExp + "(" + number + "," + scopeExp + ",0)"; 
				//主商品 {---styleTest2} 大于等于1件
				if($j(dom).attr("data_type")=="prmprd"){
					expTxt += "\n"+"主商品   " + typeTxt.substring(0, 4) + " " + scopeTxt + " " + typeTxt.substring(4) + " " + number + " " 
					+ ($j(dom).find(".pro-setting-scope-type option:selected").attr("data_unit") == "rmb" ? "元" : "折");
				}else{
					expTxt += "\n" +"选购商品   " + typeTxt.substring(0, 4) + " " + scopeTxt + " " + typeTxt.substring(4) + " " + number + " " 
					+ ($j(dom).find(".pro-setting-scope-type option:selected").attr("data_unit") == "rmb" ? "元" : "折") ;
				}
				/* 折扣时，数值必须在1-99之间 */
				var isDiscount = $j(dom).find(".pro-setting-scope-type option:selected").attr("data_unit") == "discount";
				if (isDiscount && ($j(dom).val() < discount_min || $j(dom).val() > discount_max)) {	
					if($j(dom).val().indexOf(".")>-1){
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("类型是折扣时，不能为小数"));
						isError = true;
						return;
					}
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISCOUNT_NUMBER_ERROR"));
					isError = true;
					return;
				}
			});
			
			if (isError) return;
			exp = exp.substring(1);
			expTxt = expTxt.substring(1);
			var json = {
					id: settingId,
					promotionId: id,
					settingName: expTxt,
					settingExpression: exp,
					conditionType: type
			};
			nps.asyncXhrPost(STEP_FIVE_URL, json, {successHandler : function(data, textStatus) {
				if (data.isSuccess) {
					$j("#setting-id").val(data.description);
					var txtBody = $j("#step-5 .text-mode .setting-choice tbody");
					txtBody.empty();
					$j.each(expTxt.split("\n"), function(i, txt) {
						txtBody.append("<tr><td>" + txt + "</td></tr>");
					});
					colourTbody(txtBody);
					toNextStep(5);
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
				}
			}});
			break;
		case "NormalStep":	//常规+阶梯条件
			var bodyNor = tbody.eq(0);
			var bodyStep = tbody.eq(1);
			
			var expNor = "";
			var expTxtNor = "";
			bodyNor.find("tr").each(function(i, dom) {
				expNor += $j(dom).attr("data");
				expTxtNor += "\n且" + $j(dom).find("td:first").text();
			});
			
			expNor = expNor.substring(1);
			expTxtNor = expTxtNor.substring(2);
			
			//验证数值是否递增
			var numberArr = [];
			var itemNameArr=[];
			var item_user_select = [];
			var item_gift_count = [];
			var isError = false;
			if(amountFlag!=""){
				bodyStep.find("input").each(function(i, dom) {	
					if (! checkNumber($j(dom))) {	// 数字验证 红色框
						isError = true;
						return; 	
					}
					numberArr[i] = +$j(dom).val();
					
					if (STEP_5_STEP.find(".pro-setting-scope-type option:selected").attr("data_unit") == "rmb") {	//金额要递增
						if (i > 0 && numberArr[i] <= numberArr[i-1]) {
							nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_STEP_NUMBER"));
							isError = true;
							return;
						}
					} else {
						if (numberArr[i] < discount_min || numberArr[i] > discount_max) {	// 折扣时，数值必须在0-99之间
							if(numberArr[i].indexOf(".")>-1){
								nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("类型是折扣时，不能为小数"));
								isError = true;
								return;
							}
							nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISCOUNT_NUMBER_ERROR"));
							isError = true;
							return;
						}
						//折扣要递减
						if (i > 0 && numberArr[i] >= numberArr[i-1] && (numberArr[i-1]!=0 || numberArr[0]!=0)) {
							nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_STEP_DISCOUNT"));
							isError = true;
							return;
						}
					}
					
				});
			}else{
				$j('.gitf_tab').find("select").each(function(i, dom) {
					var giftScope=$j(dom).val();
					if(giftScope=="p-select"){
						nps.info(nps.i18n("INFO_TITLE_DATA"),'请选择赠品');
						$j(dom).focus();
						isError = true;
						return;
					}
					//判断赠品是否是商品
					var comboType = $j("#step-3 .pro-product-type").val();
					if(comboType==2){
						nps.info(nps.i18n("INFO_TITLE_DATA"),"分类筛选器不能作为赠品");
						isError = true;
						$j(dom).addClass("ui-loxia-error");
						return;
					}
					if(startWith(giftScope,"call")){
						nps.info(nps.i18n("INFO_TITLE_DATA"),"赠品不能为全场");
						$j(dom).addClass("ui-loxia-error");
						isError = true;
						return;	
					}
					//判断类型是否是商品类型
					if(!startWith(giftScope,"pid")){
						var cmbid = giftScope.substring(giftScope.indexOf(":")+1,giftScope.length);
						var data = nps.syncXhrPost(PRODUCT_FILTER_OBJECT_URL, {id: cmbid});
						if(data.combo !=null && data.combo.type!=1){
							nps.info(nps.i18n("INFO_TITLE_DATA"),"赠品只能是商品筛选器或具体的商品");
							$j(dom).addClass("ui-loxia-error");
							isError = true;
							return;	
						}
					}
					numberArr[i] = giftScope ;
					itemNameArr[i]=$j(dom).find("option:selected").text();
				});
				$j('.gitf_tab').find("input[type=checkbox]").each(function(i, dom) {
					if($j(dom).is(':checked')) {
						item_user_select[i]=1;
					}else{
						item_user_select[i]=0;
					}
				});
				var gift_count = $j('.gitf_tab').find(".pro-setting-amount");
				if(gift_count.length==0){
					gift_count=$j('.gitf_tab').find(".gift_count");
				}
				gift_count.each(function(i, dom) {
					var gift_count= $j(dom).val();
					if(gift_count==null || gift_count==""){
						nps.info(nps.i18n("INFO_TITLE_DATA"),'请输入推送礼品样数或用户选择样数');
						isError = true;
						return;
					}
					if(gift_count >3 ){
						nps.info(nps.i18n("INFO_TITLE_DATA"),'推送礼品样数或用户选择样数不能大于3');
						$j(dom).focus();
						isError = true;
						return;
					}
					item_gift_count[i]=gift_count;
				});
			}
			if (isError){
				return;
			}
			
			var scopeType = STEP_5_STEP.find(".pro-setting-scope-type").val();	//范围类型
			var typeTxt = STEP_5_STEP.find(".pro-setting-scope-type option:selected").text();
			var scopeExp = STEP_5_STEP.find(".pro-condition-scope").val();	//商品范围
			var scopeTxt = STEP_5_STEP.find(".pro-condition-scope option:selected").text();	
			var typeExp = SETTING_EXPRESSION_ARRAY[scopeType];	
			//金额或折扣
			if(amountFlag!=""){
				if(scopeExp=="p-select"){
					nps.info(nps.i18n("INFO_TITLE_DATA"),'请选择商品');
					return;
				}	
				for (var i = 0; i < numberArr.length; i++) {
					//添加默认 0  常规+阶梯  何波
					exp += "\n" + typeExp + "(" + numberArr[i] + "," + scopeExp+',0' + ")"; 
					expTxt += "\n" + typeTxt.substring(0, 4) + " {" + scopeTxt + "} " + typeTxt.substring(4) + " " + numberArr[i] + " " 
						+ (STEP_5_STEP.find(".pro-setting-scope-type option:selected").attr("data_unit") == "rmb" ? "元" : "折");
				}
			}else{//礼品
				//scpgift(1,cmbid:21,1，0，1)
				for (var i = 0; i < numberArr.length; i++) {
					var  gift_id = numberArr[i] ;
					exp += "\n" + "scpgift(" + "1," + gift_id +",2,"+item_user_select[i] +","+item_gift_count[i]+")"; 
					var msg = "推送礼品样数:";
					if(item_user_select[i]==1){
						msg="最多礼品样数:";
					}
					expTxt += "\n" + typeTxt.substring(0, 4) + " {" + scopeTxt + "} " + typeTxt.substring(4) + 
					"[礼品] " + itemNameArr[i]+"["+msg+item_gift_count[i]+"]";
				}
			}
			
			exp = exp.substring(1);
			expTxt = expTxt.substring(1);
			var json = {
					id: settingId,
					promotionId: id,
					settingName: expTxtNor + "\n\n" + expTxt,
					settingExpression: expNor + "\n\n" + exp,
					conditionType: type
			};
			nps.asyncXhrPost(STEP_FIVE_URL, json, {successHandler : function(data, textStatus) {
				if (data.isSuccess) {
					$j("#setting-id").val(data.description);
					var txtBody = $j("#step-5 .text-mode .setting-normal tbody");
					txtBody.empty();
					bodyNor.find("tr").each(function(i, dom) {
						clone = $j(dom).clone();
						clone.find("td:last").remove();
						txtBody.append(clone);
					});
					txtBody = $j("#step-5 .text-mode .setting-step tbody");
					txtBody.empty();
					$j.each(expTxt.split("\n"), function(i, txt) {
						txtBody.append("<tr><td>" + txt + "</td></tr>");
					});
					toNextStep(5);
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
				}
			}});
			break;
		}
	});
	
	$j(".validateNumber").keyup(function(){
		if(this.value.indexOf('.')>-1){
			var v=  this.value.substring(this.value.indexOf('.')+1,this.value.length);
			if(v.length>=2){
				this.value = this.value.substring(0,this.value.indexOf('.')+3);
			}
		}else{
	        this.value = this.value.replace(/[^\d.]/g, '');
		}
    }).bind("paste",function(){  //CTR+V事件处理     
        this.val('');     
    }).bind("mouseleave",function(){  //回车事件
    		if(this.value.indexOf('.')>-1){
    			var v=  this.value.substring(this.value.indexOf('.')+1,this.value.length);
    			if(v.length>=2){
    				this.value = this.value.substring(0,this.value.indexOf('.')+3);
    			}
    		}else{
    	        this.value = this.value.replace(/[^\d.]/g, '');
    		}  
    });
	
	//‘倍增’帮助
	$j("#double-help").mouseover(function() {
		$j("#div-double-help").css("top", $j(this).offset().top + 10).css("left", $j(this).offset().left + 10).show();
	}).mouseout(function() {
		$j("#div-double-help").hide();
	});
	//‘礼品’帮助
	$j(".user-select-help").mouseover(function() {
		$j("#div-user-select-help").css("top", $j(this).offset().top + 10).css("left", $j(this).offset().left + 10).show();
	}).mouseout(function() {
		$j("#div-user-select-help").hide();
	});
	
	//返回
    $j(".cancel").click(function(){
    	nps.confirm(nps.i18n("INFO_TITLE_DATA"),'取消之后设置信息不会保存,是否确定取消？',function(){
    		window.location.href="/promotion/promotionEdit.htm?keepfilter=true";
    	});
    	
    });
    discountInfo($j(".dicountInfo").val());
    //第五步设置时，选择
    $j(".dicountInfo").change(function(){ 
    	var option = $j(this);
    	var type = option.val();
    	discountInfo(type);
    });
});

/* ------------------------------------------------- util ------------------------------------------------- */
/**
 * 检查表单元素是否为空
 * @param $args	jQuery对象
 * @returns {Boolean}
 */
function checkFormElementIsBlank($args) {
	var args = arguments;
	for (var i = 0; i < args.length; i++) {
		if ($j.trim(args[i].val()).length == 0) {
			args[i].blur().focus();
			return false;
		}
	}
	return true;
}

/**
 * 比较日期大小
 * @param data1
 * @param data2
 * @returns {Boolean}
 */
function compareDate(data1, data2) {
	return Date.parse(data1.replace(/-/g,"/")) > Date.parse(data2.replace(/-/g,"/"));
}

/**
 * 检查字符串是否只由‘数字’、‘字母’、‘汉字’组成
 * @param str
 * @returns {Boolean}
 */
function isPlainString(str) {
	return /^[_a-zA-Z0-9\u4e00-\u9fa5]*$/.test(str);
}

/**
 * 前往下一步
 * @param index 当前步骤索引
 */
function toNextStep(index) {
	if (index == 0) {	//第一步
		var nextStep = $j("#step-" + (index + 1));
		nextStep.find(".input-mode").show();
		nextStep.find(".text-mode").hide();
		return;
	}
	var curStep = $j("#step-" + index);
	curStep.find(".input-mode").hide();
	curStep.find(".text-mode").show();
	if (index == 5) {	//最后一步
		$j(".btn-update").show();
		nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SUCCESS"));
		return;
	}
	var nextStep = $j("#step-" + (index + 1));
	nextStep.find(".input-mode").show();
	nextStep.find(".text-mode").hide();
	$j("body").animate({scrollTop:nextStep.offset().top}, 'slow');
}

/**
 * 商品类型筛选器填充表格，并填充第4步下拉框
 * @param data
 */
function fillWithProduct(details, prefix,step) {
	var comboName = details.name;
	var  url = pdp_base_url;
	if(details.atomList!=null){
		$j.each(details.atomList, function(i, obj) {
			if(step==3){
				var name_url;
				if(url.indexOf("code") >-1){
					name_url = url.substring(0,url.length-4);
					name_url = name_url.replace("(@)",obj.code);
				}else{
					name_url = url.substring(0,url.length-6);
					name_url = name_url.replace("(@)",obj.id);
				}
				var html = "<tr><td>" + comboName + "</td><td>" 
				+'<a href="'+name_url+'" target="_blank" class="func-button" >'+ obj.name + "</a></td><td>" + obj.price + "</td></tr>";
				$j("#step-3 .tbl-include tbody").append(html);
			}
			//填充第4、5步下拉框
			var option = "<option value='pid:" + obj.id + "'>" + prefix + obj.name + "</option>";
			if(step==3){
				$j(".pro-condition-scope").append(option);
			}else{
				$j(".pro-condition-scope-gift").append(option);
			}
		});
	}
}

/**
 * 分类类型筛选器填充表格，并填充第4步下拉框
 * @param data
 */
function fillWithCategory(details, prefix,step) {
	var comboName = details.name;
	$j.each(details.atomList, function(i, obj) {
		var html = "<tr><td>" + comboName + "</td><td>" 
		+ obj.name + "</td><td>" + (obj.price ? obj.price : "") + "</td></tr>";
		
		if (obj.isOut&&step==3) {
			$j("#step-3 .tbl-exclude tbody").append(html);
		} else {
			if(step==3){
				$j("#step-3 .tbl-include tbody").append(html);
				
			}
			//填充第4、5步下拉框
			var cateName = obj.name;
			var option = "<option value='cid:" + obj.id + "'>" + prefix + cateName + "</option>";
			if (obj.id == 0) {	//全场
				option = "<option value='call:'>" + prefix + cateName + "</option>";
			}
			if(step==3){
				$j(".pro-condition-scope").append(option);
			}else{
				$j(".pro-condition-scope-gift").append(option);
			}
			
		}
		
	});
}

/**
 * 为表格体上色
 * @param tbody
 */
function colourTbody(tbody) {
	tbody.find("tr").each(function(i, dom) {
		var cls = (i%2==0) ? "even" : "odd";
		$j(dom).removeClass("even odd").addClass(cls);
	});
}


/**
 * 保存前，检查表格数据正确性
 * @param tbody
 * @param mark
 * @returns {Boolean}
 */
function checkBeforeSave(tbody, mark) {
	var rs = true;
	tbody.each(function(i, dom) {
		var trs = $j(dom).find("tr");
		if (trs.length == 0) {	//表格为空
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_" + mark));
			rs = false;
			return;
		}
	});
	
	return rs;
}

/**
 * 检查是否是合法数字
 * @param $input
 * @returns {Boolean}
 */
function checkNumber($input) {
	var number = $j.trim($input.val());
	if (! /^[0-9]+([.]{1}[0-9]{1,2})?$/.test(number)) {	
		$input.blur().focus();
		return false;
	}
	return true;
}
/**
 * 检查不同阶梯赠品不能相同
 * @param gifts
 * @param giftstxt
 */
function checkGiftIsRept(gifts, giftstxt) {
	for ( var i = 0; i < gifts.length; i++) {
		var gift = gifts[i];
		for ( var j = gifts.length - 1; j > i; j--) {
			if (gift == gifts[j]) {
				nps.info(nps.i18n("INFO_TITLE_DATA"), '存在相同的礼品:'+giftstxt[j]);
				return true;
			}
		}
	}
	return false;
}
function saveStep4() {
	var id = $j("#pro-id").val();	//促销ID
	var conditionid = $j("#condition-id").val();	//条件ID
	var type = $j("#step-4 .input-mode .pro-condition-type").val();	//条件类型
	var typeTxt = $j("#step-4 .input-mode .pro-condition-type option[value='" + type + "']").text();	//条件类型名
	if(type=="Normal"|| type=="Choice"){
		$j('#ui-tag-change-id').hide();
	}
	if(type=="Step" || type=="NormalStep"){
		$j('#ui-tag-change-id').show();
	}
	switch (type) {
	case "Normal":	//常规条件
		var tbody = $j("#step-4 .input-mode .condition-normal tbody");	//表格体
		if (! checkBeforeSave(tbody, "CONDITION")) {	//检查表格数据正确性
			return;
		}
		
		var exp = "";	//表达式
		var expTxt = "";
		tbody.find("tr").each(function(i, dom) {
			exp += $j(dom).attr("data");
			var markTxt = i == 0 ? "" : $j(dom).find("td:eq(0)").text();
			expTxt += "\n" + markTxt + $j(dom).find("td:eq(1)").text();
		});
		exp = exp.substring(1);
		expTxt = expTxt.substring(1);
		var json = {
			id: conditionid,
			promotionId: id,
			conditionType: type,
			conditionName: expTxt,
			conditionExpress: exp
		};
		nps.asyncXhrPost(STEP_FOUR_URL, json, {successHandler : function(data, textStatus) {
			if (data.isSuccess) {
				$j("#condition-id").val(data.description);
				$j("#step-4 .text-mode .pro-condition-type").text(typeTxt);
				var txtBody = $j("#step-4 .text-mode .condition-normal tbody");
				txtBody.empty();
				tbody.find("tr").each(function(i, dom) {
					clone = $j(dom).clone();
					clone.find("td:eq(2)").remove();
					txtBody.append(clone);
				});
				toNextStep(4);
				
				$j("#step-4 .input-mode .pro-condition-type").prop("disabled", true);
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
			}
		}});
		break;
	case "Step":	//阶梯条件
		var tbody = $j("#step-4 .input-mode .condition-step tbody");	//表格体
		if (! checkBeforeSave(tbody, "CONDITION")) {	//检查表格数据正确性
			return;
		}
		var exp = "";	//表达式
		var expTxt = "";
		tbody.find("tr").each(function(i, dom) {
			exp += "\n" + $j(dom).attr("data");
			expTxt += "\n" + $j(dom).find("td:eq(0)").text();
		});
		exp = exp.substring(1);
		expTxt = expTxt.substring(1);
		var json = {
			id: conditionid,
			promotionId: id,
			conditionType: type,
			conditionName: expTxt,
			conditionExpress: exp
		};
		nps.asyncXhrPost(STEP_FOUR_URL, json, {successHandler : function(data, textStatus) {
			if (data.isSuccess) {
				$j("#condition-id").val(data.description);
				$j("#step-4 .text-mode .pro-condition-type").text(typeTxt);
				var txtBody = $j("#step-4 .text-mode .condition-step tbody");
				txtBody.empty();
				tbody.find("tr").each(function(i, dom) {
					clone = $j(dom).clone();
					clone.find("td:last").remove();
					txtBody.append(clone);
				});
				
				toNextStep(4);
				//显示礼品table
				$j("#step-5 .input-mode .setting-step-gift").show();
				//加载礼品选择器
				//‘商品筛选器’下拉框
				var cmid = $j("#step-3 .pro-product-value").val();
				setItemSelector(cmid,4);
				//隐藏用户选择提示
				hideUserselectInfo();
				$j("#step-4 .input-mode .pro-condition-type").prop("disabled", true);
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
			}
		}});
		break;
	case "Choice":	//选购条件
		var tbody = $j("#step-4 .input-mode .condition-choice tbody");	//表格体
		if (! checkBeforeSave(tbody, "CONDITION")) {	//检查表格数据正确性
			return;
		}
		var mainList = tbody.find("tr[data_type='prmprd']");	//主商品列表
		var deputyList = tbody.find("tr[data_type='addtprd']");	//选购商品列表
		if (mainList.length == 0 || deputyList.length == 0) {	//主副商品起码各有一项
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_MAIN_DEPUTY"));
			return;
		}
		
		var mainExp = "";	//主商品表达式
		var mainExpTxt = "";
		mainList.each(function(i, dom) {
			mainExp += "\n" + $j(dom).attr("data");
			mainExpTxt += "\n" + $j(dom).find("td:eq(0)").text();
		});
		mainExp = mainExp.substring(1);
		mainExpTxt = mainExpTxt.substring(1);
		var deputyExp = "";	//选购商品表达式
		var deputyExpTxt = "";
		deputyList.each(function(i, dom) {
			deputyExp += "\n" + $j(dom).attr("data");
			deputyExpTxt += "\n" + $j(dom).find("td:eq(0)").text();
		});
		deputyExp = deputyExp.substring(1);
		deputyExpTxt = deputyExpTxt.substring(1);
		var json = {
			id: conditionid,
			promotionId: id,
			conditionType: type,
			conditionName: mainExpTxt + "\n\n" + deputyExpTxt,
			conditionExpress: mainExp + "\n\n" + deputyExp
		};
		
		nps.asyncXhrPost(STEP_FOUR_URL, json, {successHandler : function(data, textStatus) {
			if (data.isSuccess) {
				$j("#condition-id").val(data.description);
				$j("#step-4 .text-mode .pro-condition-type").text(typeTxt);
				var txtBody = $j("#step-4 .text-mode .condition-choice tbody");
				txtBody.empty();
				tbody.find("tr").each(function(i, dom) {
					var clone = $j(dom).clone();
					clone.find("td:last").remove();
					txtBody.append(clone);
				});
				toNextStep(4);
				
				$j("#step-4 .input-mode .pro-condition-type").prop("disabled", true);
				$j("#step-5 .pro-setting-scope-type option[value!='7']").remove();
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
			}
		}});
		break;
	case "NormalStep":	//常规+阶梯条件
		var tbodyNormal = $j("#step-4 .input-mode .condition-normal tbody");	//常规表格体
		var tbodyStep = $j("#step-4 .input-mode .condition-step tbody");	//阶梯表格体
		if (! checkBeforeSave(tbodyNormal, "CONDITION") || ! checkBeforeSave(tbodyStep, "CONDITION")) {	//检查表格数据正确性
			return;
		}
		
		var expNor = "";	//常规表达式
		var expTxtNor = "";
		tbodyNormal.find("tr").each(function(i, dom) {
			expNor += $j(dom).attr("data");
			var markTxt = i == 0 ? "" : $j(dom).find("td:eq(0)").text();
			expTxtNor += "\n" + markTxt + $j(dom).find("td:eq(1)").text();
		});
		expNor = expNor.substring(1);
		expTxtNor = expTxtNor.substring(1);
		
		var expStep = "";	//阶梯表达式
		var expTxtStep = "";
		tbodyStep.find("tr").each(function(i, dom) {
			expStep += "\n" + $j(dom).attr("data");
			expTxtStep += "\n" + $j(dom).find("td:eq(0)").text();
		});
		expStep = expStep.substring(1);
		expTxtStep = expTxtStep.substring(1);
		var json = {
			id: conditionid,
			promotionId: id,
			conditionType: type,
			conditionName: expTxtNor + "\n\n" + expTxtStep,
			conditionExpress: expNor + "\n\n" + expStep
		};
		nps.asyncXhrPost(STEP_FOUR_URL, json, {successHandler : function(data, textStatus) {
			if (data.isSuccess) {
				$j("#condition-id").val(data.description);
				$j("#step-4 .text-mode .pro-condition-type").text(typeTxt);
				var txtBodyNor = $j("#step-4 .text-mode .condition-normal tbody");
				txtBodyNor.empty();
				tbodyNormal.find("tr").each(function(i, dom) {
					clone = $j(dom).clone();
					clone.find("td:eq(2)").remove();
					txtBodyNor.append(clone);
				});
				var txtBodyStep = $j("#step-4 .text-mode .condition-step tbody");
				txtBodyStep.empty();
				tbodyStep.find("tr").each(function(i, dom) {
					clone = $j(dom).clone();
					clone.find("td:last").remove();
					txtBodyStep.append(clone);
				});
				
				toNextStep(4);
				//加载礼品选择器
				//‘商品筛选器’下拉框
				var cmid = $j("#step-3 .pro-product-value").val();
				setItemSelector(cmid,4);
				//隐藏用户选择提示
				hideUserselectInfo();
				$j("#step-4 .input-mode .pro-condition-type").prop("disabled", true);
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
			}
		}});
		break;
	}
}
function discountInfo(type){
	var types=['2','4','6','8'];
	var flag = false;
	for (var index = 0; index < types.length; index++) {
		if(type==types[index]){
			flag = true;
			break;
		}
	}
	if(flag){
		$j(".norInfo").show();
	}else{
		$j(".norInfo").hide();
	}
};
/* ------------------------------------------------- loxia-table-template ------------------------------------------------- */
/*$j(function(){
	$j("#step-3 .input-mode").show();
	$j("#step-4 .input-mode").show();
	$j("#step-5 .input-mode").show();
})*/
