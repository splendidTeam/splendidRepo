/* ------------------------------------------------- @author - 項邵瀧の父 ------------------------------------------------- */
/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'],{
	"NO_DATA":"无数据",
	"INFO_TITLE_DATA":"提示信息",
	"INFO_NOT_COMPLETE":"请完整填写限购信息",
	"INFO_EARLY_DATE":"结束时间必须晚于起始时间",
	"INFO_ILLEGAL_NAME":"限购名称必须由‘字母’、‘数字’、‘汉字’或‘下划线’组成",
	"INFO_HEAD_SUCCESS":"限购头部设置成功",
	"INFO_AUDIENCE_SUCCESS":"限购受限人群设置成功",
	"INFO_SCOPE_SUCCESS":"限购范围设置成功",
	"INFO_CONDITION_SUCCESS":"限购条件设置成功",
	"INFO_SETTING_SUCCESS":"限购优惠设置成功",
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
	"INFO_UPDATE_SCOPE":"修改限购范围将导致限购条件与限购优惠设置的所有已有数据失效，是否继续？",
	"INFO_NO_LIMIT":"选择“不限”后，无法选择其他条件项",
	"INFO_STEP_TYPE":"阶梯条件项的类型与商品范围必须相同",
	"INFO_CONDITION_TYPE_FROZEN":"限购条件类型一旦确认将无法修改，是否继续？",
	"INFO_SUCCESS":"限购编辑成功！",
	"INFO_SYSTEM_ERROR":"系统错误，请联系管理员",
});

/* ------------------------------------------------- URL ------------------------------------------------- */
var STEP_ONE_URL = base + "/limit/stepOne.json";
var STEP_TWO_URL = base + "/limit/stepTwo.json";
var STEP_THREE_URL = base + "/limit/stepThree.json";
var STEP_FOUR_URL = base + "/limit/stepFour.json";
var MEMBER_FILTER_LIST_URL = base + "/promotion/member-filter-list.json";
var PRODUCT_FILTER_LIST_URL = base + "/promotion/product-filter-list.json";
var PRODUCT_FILTER_OBJECT_URL = base + "/promotion/product-filter-info.json";
var DELETE_STEP_URL = base + "/limit/deleteStep.json";
var RETURN_URL = base + "/limitation/limitationPublishList.htm";
/* ------------------------------------------------- 全局常量 ------------------------------------------------- */
var SEPERATOR = "\n";	// 表达式文本分隔符
var CONDITION_EXPRESSION_ARRAY = //条件表达式数组
	["ordskuqty(?,?)","orditemqty(?,?)","ordqty(?,?)","hisordskuqty(?,?)","hisorditemqty(?,?)","histordqty(?,?)"];
/* ------------------------------------------------- 全局变量 ------------------------------------------------- */

/* ------------------------------------------------- ready ------------------------------------------------- */
$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	
	//‘修改’按钮
	$j(".btn-update").click(function() {
		toNextStep(0);
		$j(this).hide();
	});
	
	// 查看
	if ($j("#is-view").val() == "false") {
		$j(".btn-return").hide();
		$j(".btn-update").click();
	} else {
		$j(".btn-update").hide();
		$j(".btn-return").show();
	}
	
	// 返回
	$j(".btn-return").click(function() {
		window.location.href = RETURN_URL;
	});
	
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
		var name = $j.trim($j(".input-mode .pro-name").val());	//限购名称
		
		if (! isPlainString(name)) {	//名称含有非法字符
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ILLEGAL_NAME"));
			return;
		}
		
		var startTime = $j.trim($j(".input-mode .pro-startTime").val());	//限购起始时间
		var endTime = $j.trim($j(".input-mode .pro-endTime").val());	//限购结束时间
		
		if (! compareDate(endTime, startTime)) {	//结束日期早于其实日期
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_EARLY_DATE"));
			return;
		}
		
		var id = $j("#limit-id").val();	//限购ID
		var json = {
			id: id,
			name: name,
			startTime: startTime,
			endTime: endTime
		};
		nps.asyncXhrPost(STEP_ONE_URL, json, {successHandler : function(data, textStatus) {
			if (data.isSuccess) {
				$j("#limit-id").val(data.description);
				$j("#step-1 .text-mode .pro-name").text(name);
				$j("#step-1 .text-mode .pro-startTime").text(startTime);
				$j("#step-1 .text-mode .pro-endTime").text(endTime);
				toNextStep(1);
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
		
		var id = $j("#limit-id").val();	//限购ID
		var audienceId = $j("#audience-id").val();	//人群ID
		nps.asyncXhrPost(STEP_TWO_URL, {id: id, memberComboId: comboId, audienceId: audienceId}, {successHandler : function(data, textStatus) {
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
		if (comboId) {
			var data = nps.syncXhrPost(PRODUCT_FILTER_OBJECT_URL, {id: comboId});
			if (data.isSuccess) {
				$j("#step-3 tbody").empty();
				
				var type = +$j("#step-3 .pro-product-type").val();
				
				$j(".pro-condition-scope").empty();	//第4步范围下拉框
				var option = "<option value='cmbid:" + data.combo.id + "'>" + data.combo.name + " [筛选器]" + "</option>";
				$j(".pro-condition-scope").append(option);
				
				var prefix = "---";	// 阶层表现商品范围的前缀
				
				if (type == 1) {	//商品类型
					fillWithProduct(data.details, prefix);
				} else if (type == 2) {	//分类类型
					fillWithCategory(data.details, prefix);
				} else if(type == 3){
					fillWithProduct(data.details, prefix);
				}else if (type == 4) {	//组合类型
					$j.each(data.detailsList, function(i, details) {
						$j(".pro-condition-scope")
							.append("<option value='cmbid:" + details.id + "'>" + prefix + details.name + " [筛选器]" + "</option>");
						if (details.type == 1) {
							fillWithProduct(details, prefix + prefix);
						} else {
							fillWithCategory(details, prefix + prefix);
						}
					});
				}
				colourTbody($j("#step-3 .tbl-include tbody"));
				colourTbody($j("#step-3 .tbl-exclude tbody"));
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
			}
			
		}
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
		
		var id = $j("#limit-id").val();	//限购ID
		var scopeId = $j("#scope-id").val();	//范围ID
		var originalComboId = $j("#step-3 .pro-product-value").attr("data");
		if (scopeId.length != 0 && originalComboId != comboId) { //修改范围
			nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_UPDATE_SCOPE"), function() {
				var data = nps.syncXhrPost(DELETE_STEP_URL, {id: id});
				if (data.isSuccess) {
					$j("#step-4 tbody").empty();
					$j("#condition-id").val("");
					nps.asyncXhrPost(STEP_THREE_URL, {id: id, productComboId: comboId, scopeId: scopeId}, {successHandler : function(data, textStatus) {
						if (data.isSuccess) {
							$j("#scope-id").val(data.description);
							$j("#step-3 .pro-scope").text(comboName + " [" + comboTypeTxt + "]");
							$j("#step-3 .pro-product-value").attr("data", comboId);
							toNextStep(3);
						} else {
							nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
						}
					}});
				} else {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
				}
			});
		} else {	//新建范围
			nps.asyncXhrPost(STEP_THREE_URL, {id: id, productComboId: comboId, scopeId: scopeId}, {successHandler : function(data, textStatus) {
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
	var STEP_4 = $j("#step-4 .input-mode .condition");	//步骤4输入模式块
	
	//‘常规-添加’按钮
	STEP_4.find(".btn-add").click(function() {
		var tbody = STEP_4.find("tbody");
		var type = +STEP_4.find(".pro-condition-scope-type").val();	//子类型
		var typeTxt = STEP_4.find(".pro-condition-scope-type option:selected").text();	//子类型名称
		var scopeExp = STEP_4.find(".pro-condition-scope").val();	//商品范围表达式
		var scopeTxt = " {" + /[^-].*/.exec(STEP_4.find(".pro-condition-scope option:selected").text()) + "} ";	//商品范围表达式
		var number = $j.trim(STEP_4.find(".pro-condition-amount").val());
		var operatorTxt = "小于等于";
		var unitTxt = "件数";
		if(type==1 ||  type==4 ){
			unitTxt = "样数";
		}
		if(type==5){
			unitTxt = "单数";
		}
		if (! checkNumber(STEP_4.find(".pro-condition-amount"))) {	//检查数字
			return;
		}
		
		var exp = CONDITION_EXPRESSION_ARRAY[type]
			.replace(/\?/, number)
			.replace(/\?/, scopeExp);	//条件表达式
		var txt = typeTxt + scopeTxt + operatorTxt + number + unitTxt;
		
		/* 检查是否范围类型和范围都重复 */
		for (var i = 0; i < tbody.find("tr").length; i++) {
			var row = tbody.find("tr").eq(i);
			if (isConflictingExpression(row.attr("data"), exp)) {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SAME_CONDITION"));
				return;
			}
		}
		
		var tr = $j("<tr data=''><td></td><td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a></td></tr>");
		
		tr.attr("data", exp)
			.find("td:first").text(txt);
		
		tr.appendTo(tbody);
		colourTbody(tbody);	//上色
	});
	STEP_4.find(".pro-condition-scope-type").change(function(){
		var me = $j(this);
		var key = me.val();
		if(key==0 ||  key==2 || key==3){
			$j(".num_info").html("件数");
		}
		if(key==1 ||  key==4 ){
			$j(".num_info").html("样数");
		}
		if(key==5){
			$j(".num_info").html("单数");
		}
	});
	//‘常规-删除’按钮
	STEP_4.find("tbody").on("click", ".btn-remove", function() {
		$j(this).parent().parent().remove();
		var tbody = STEP_4.find("tbody");
		colourTbody(tbody);	//上色
	});

	//‘保存’按钮
	$j("#step-4 .btn-save").click(function() {
		var tbody = $j("#step-4 .input-mode tbody");	//表格体
		if (! checkBeforeSave(tbody, "CONDITION")) {	//检查表格数据正确性
			return;
		}

		var id = $j("#limit-id").val();	//限购ID
		var conditionId = $j("#condition-id").val();	//条件ID
		
		var exp = "";	//表达式
		var expTxt = "";
		tbody.find("tr").each(function(i, dom) {
			exp += "&" + $j(dom).attr("data");
			expTxt += SEPERATOR + $j(dom).find("td:eq(0)").text();
		});
		exp = exp.substring(1);
		expTxt = expTxt.substring(1);
		var json = {
			id: id,
			conditionId: conditionId,
			conditionExpression: exp,
			conditionText: expTxt
		};
		nps.asyncXhrPost(STEP_FOUR_URL, json, {successHandler : function(data, textStatus) {
			if (data.isSuccess) {
				$j("#condition-id").val(data.description);
				var txtBody = $j("#step-4 .text-mode .condition tbody");
				txtBody.empty();
				tbody.find("tr").each(function(i, dom) {
					clone = $j(dom).clone();
					clone.find("td:last").remove();
					txtBody.append(clone);
				});
				toNextStep(4);
			} else {
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_CREATE_FAILURE") + data.description);
			}
		}});
	});

	//返回
    $j(".cancel").click(function(){
    	//返回
    	nps.confirm(nps.i18n("INFO_TITLE_DATA"),'取消之后设置信息不会保存,是否确定取消？',function(){
    		window.location.href="/limitation/limitationEditList.htm?keepfilter=true";
    	});
        	
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
	if (index == 4) {	//最后一步
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
function fillWithProduct(details, prefix) {
	var comboName = details.name;
	$j.each(details.atomList, function(i, obj) {
		
		var url;
		if(pdp_base_url.indexOf("code")>-1){
			url = pdp_base_url.substring(0,pdp_base_url.length-4);
			url = url.replace("(@)",obj.code);
		}else{
			url = pdp_base_url.substring(0,pdp_base_url.length-6);
			url = url.replace("(@)",obj.id);
		}
		var html = "<tr><td>" + comboName + "</td><td>" 
		+'<a href="'+url+'" target="_blank" class="func-button" >'+ obj.name + "</a></td><td>" + obj.price + "</td></tr>";
		$j("#step-3 .tbl-include tbody").append(html);
		
		//填充第4、5步下拉框
		var option = "<option value='pid:" + obj.id + "'>" + prefix + obj.name + "</option>";
		$j(".pro-condition-scope").append(option);
	});
}

/**
 * 分类类型筛选器填充表格，并填充第4步下拉框
 * @param data
 */
function fillWithCategory(details, prefix) {
	var comboName = details.name;
	$j.each(details.atomList, function(i, obj) {
		var html = "<tr><td>" + comboName + "</td><td>" 
		+ obj.name + "</td><td>" + (obj.price ? obj.price : "") + "</td></tr>";
		
		if (obj.isOut) {
			$j("#step-3 .tbl-exclude tbody").append(html);
		} else {
			$j("#step-3 .tbl-include tbody").append(html);
			
			//填充第4、5步下拉框
			var cateName = obj.name;
			var option = "<option value='cid:" + obj.id + "'>" + prefix + cateName + "</option>";
			if (obj.id == 0) {	//全场
				option = "<option value='call:'>" + prefix + cateName + "</option>";
			}
			
			$j(".pro-condition-scope").append(option);
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
	if (! /^[0-9]+$/.test(number)) {	
		$input.blur().focus();
		return false;
	}
	return true;
}

/**
 * 检查两个表达式是否类型和范围都相同，即冲突
 * @param exp1
 * @param exp2
 * @returns {Boolean}
 */
function isConflictingExpression(exp1, exp2) {
	var regType = /[^(]+/;
	var regScope = /[a-z]+:\d+/;
	return regType.exec(exp1) + " " + regScope.exec(exp1) == regType.exec(exp2) + " " + regScope.exec(exp2);
}

/* ------------------------------------------------- loxia-table-template ------------------------------------------------- */
/*$j(function(){
	$j("#step-1 .text-mode").show();
	$j("#step-2 .input-mode").show();
	$j("#step-3 .input-mode").show();
	$j("#step-4 .input-mode").show();
	$j("#step-5 .input-mode").show();
})*/
