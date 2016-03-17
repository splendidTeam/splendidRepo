/* ------------------------------------------------- 国际化 ------------------------------------------------- */
$j.extend(loxia.regional['zh-CN'],{
	"NO_DATA":"无数据",
	"INFO_TITLE_DATA":"提示信息",
	"INFO_NO_NAME":"请输入组合名称",
	"INFO_NO_DATA":"包含列表不能为空",
	"INFO_SUCCESS":"保存成功",
	"INFO_REPEATED_NAME":"此名称已被使用，请更换其他名称",
	"INFO_NO_ADD_ITEM":"请先查询所需数据",
	"INFO_SYSTEM_ERROR":"系统错误，请联系管理员",
	"INFO_CONFLICT":"对不起，两个列表中的条件有冲突，是否继续？",
	"INFO_REPEATED_MEMBER":"您已经勾选过部分会员，是否合并？",
	"INFO_REPEATED_GROUP":"您已经勾选过部分分组，是否合并？",
	"INFO_REPEATED_CUSTOM":"您已经勾选过部分组合，是否合并？",
	"INFO_SELECT_ALL":"选择“全体”后将删除先前已选分组，是否继续？",
	"INFO_INCLUDE_FIRST":"请先添加包含列表",
	"INFO_INCLUDE_ERROR":"您勾选的会员不属于包含列表中的分组，请重新选择",
	"INFO_INCLUDE_ERROR2":"排除列表中含有不被包含的会员，请重新编辑",
	"INFO_EXCLUDE_ALL":"不能排除全体，请重新选择",
	"INFO_HAS_ALL":"选择‘全体’后，不能添加其他分组",
	"TEXT_REMOVE":"删除",
	"TEXT_INCLUDE":"包含",
	"TEXT_EXCLUDE":"排除",
	"TEXT_STAFF":"全体"
});

/* ------------------------------------------------- URL ------------------------------------------------- */
var ADD_GROUP_URL = base + "/member/combo/add.json";
var CHECK_GROUP_URL = base + "/member/combo/check.json";
var PREVIOUS_URL = base + "/member/membercombolist.htm";
var CUSTOM_LIST_URL = base + "/member/combo/custom-list.json";
var UPDATE_URL = base + "/member/combo/update.json";

var USER_DEFINED_LIST_URL = base + '/member/combo/userDefinedList.json';

/* ------------------------------------------------- 全局常量 ------------------------------------------------- */
/* ------------------------------------------------- 全局变量 ------------------------------------------------- */
var IS_GROUP_IN = true;	// 分组类型时，根据此变量判断是包含还是排除
/* ------------------------------------------------- ready ------------------------------------------------- */
$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
	
	var MEMBER_IN_TBODY = $j("#member-block tbody");	// 会员包含列表
	var GROUP_IN_TBODY = $j("#group-block-include tbody");		// 分组包含列表
	var GROUP_OUT_TBODY = $j("#group-block-exclude tbody");	// 分组排除列表
	var COMBO_IN_TBODY = $j("#custom-block tbody");		// 组合包含列表
	var CUSTOM_IN_TBODY = $j("#user-defined-block tbody");	//自定义
	
	/* 数据回填 */
	var IS_UPDATE = $j("#hid-isUpdate").val() == "true";
	if (IS_UPDATE) {
		var type = $j("#hid-type").val();
		$j("#slt-type option[value='" + type + "']").prop("selected", true);
		$j("#slt-type").prop("disabled", true);
		ADD_GROUP_URL = UPDATE_URL;
	}
	
	//‘筛选器类型’下拉框事件
	$j("#slt-type").change(function() {
		$j(".toggle-block").hide();
		var type = + $j(this).val();
		switch (type) {
		case 1:	//会员
			$j("#member-block").show();
			break;
		case 2:	//分组
			$j("#group-block").show();
			break;
		case 3:	//自定义固定分组
			$j("#user-defined-block").show();
			break;
		case 4:	//组合
			$j("#custom-block").show();
			break;
		}
	}).change();
	
	/* ------------------------------------------------- 会员 ------------------------------------------------- */
	//会员‘查询’按钮
	$j("#member-block .btn-search").click(function() {
		$j("#member-selector").dialogff({type:'open',close:'in',width:'1000px',height:'550px'});
	});
	
	//会员选择器‘确定’按钮点击事件
	$j("#member-selector-data").click(function() {
		var type = + $j("#slt-type").val();
		var html = "<tr><td></td><td><a href='javascript:void(0);' class='func-button btn-remove'>删除</a></td></tr>";
		if (type == 1) {	//会员类型
			var memberList = $j(this).data("memberList");
			var trs = MEMBER_IN_TBODY.find("tr");
			$j.each(memberList, function(i, mem) {
				var has = false;
				trs.each(function(j, dom) {
					if (mem.id == $j(dom).attr("data")) {
						has = true;
						return;
					}
				});
				if (! has) {
					var tr = $j(html).attr("data", mem.id);
					tr.find("td:first").text(mem.name);
					MEMBER_IN_TBODY.append(tr);
				}
			});
		} else if (type == 2) {	//分组类型
			var groupTrs = GROUP_IN_TBODY.find("tr");
			if (groupTrs.length == 0) {	//包含列表为空
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_INCLUDE_FIRST"));
				return;
			}
			var memberList = $j(this).data("memberList");
			
			if (groupTrs.eq(0).attr("data") != '0') {	//包含列表不为‘全体’，发送验证
				var members = "";
				var groups = "";
				groupTrs.each(function(i, dom){
					groups += $j(dom).attr("data") + ",";
				});
				$j.each(memberList, function(i, mem){
					members += mem.id + ",";
				});
				members = members.substring(0, members.length - 1);
				groups = groups.substring(0, groups.length - 1);
				var json = {
						members: members,
						groups: groups
				};
				var data = nps.syncXhrPost(CHECK_GROUP_URL, json);
				if (! data.isSuccess) {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_INCLUDE_ERROR"));
					return;
				}
			} 
			var trs = GROUP_OUT_TBODY.find("tr");
			$j.each(memberList, function(i, mem) {
				var has = false;
				trs.each(function(j, dom) {
					if (mem.id == $j(dom).attr("data")) {
						has = true;
						return;
					}
				});
				if (! has) {
					var tr = $j(html).attr("data", mem.id);
					tr.find("td:first").text(mem.name);
					GROUP_OUT_TBODY.append(tr);
				}
			});
		}
		

		$j("#member-selector .dialog-close").click();
		
		/* 清空选择器的输入项 */
		$j("#member-selector table :text").val("");
		$j("#member-selector table select").val("");
		$j("#member-selector-table").loxiasimpletable("refresh");
		
		colourTbody(type == 1 ? MEMBER_IN_TBODY : GROUP_OUT_TBODY);
		
	});
	
	//删除按钮
	MEMBER_IN_TBODY.on("click", ".btn-remove", function() {
		$j(this).parent().parent().remove();
		colourTbody(MEMBER_IN_TBODY);
	});
	
	//保存按钮
	$j(".btn-save").click(function() {
		var name = $j.trim($j("#txt-name").val());
		if (!name) {
			$j("#txt-name").blur().focus();
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_NAME"));
			return;
		}
		
		var id = $j("#hid-id").val();
		var type = parseInt($j("#slt-type").val());
		switch (type) {
			case 1:	//会员
				var trs = MEMBER_IN_TBODY.find("tr");
				if (trs.length == 0) {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_DATA"));
					return;
				}
				var exp = "";
				var txt = "";
				trs.each(function(i, dom) {
					exp += $j(dom).attr("data") + ",";
					txt += $j(dom).find("td:first").text() + ",";
				});
				exp = exp.substring(0, exp.length - 1);
				txt = txt.substring(0, txt.length - 1);
				var json = {
						id: id,
						name: name,
						expression: exp,
						text: "【" + nps.i18n("TEXT_INCLUDE") + "】" + txt,
						type: type
				};
				nps.asyncXhrPost(ADD_GROUP_URL, json, {successHandler : function(data, textStatus) {
					if (data.isSuccess) {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SUCCESS"));
						if (! IS_UPDATE) {
							MEMBER_IN_TBODY.empty();
							$j("#txt-name").val("");
						}
					} else {
						nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
					}
				}});
				break;
			case 2:	//分组
				var inTrs = GROUP_IN_TBODY.find("tr");
				var outTrs = GROUP_OUT_TBODY.find("tr");
				if (inTrs.length == 0) {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_DATA"));
					return;
				}
				/*if (inTrs.eq(0).attr("data") != "0" && outTrs.length > 0) {	//不是全体,发送验证
					var members = "";
					var groups = "";
					$j.each(GROUP_LIST, function(i, grp){
						groups += grp.id + ",";
					});
					$j.each(MEMBER_LIST_EXCLUDE, function(i, mem){
						members += mem.id + ",";
					});
					members = members.substring(0, members.length - 1);
					groups = groups.substring(0, groups.length - 1);
					var json = {
							members: members,
							groups: groups
					};
					var data = nps.syncXhrPost(CHECK_GROUP_URL, json);
					if (! data.isSuccess) {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_INCLUDE_ERROR2"));
						return;
					}
				}*/
				var exp = "";
				var txt = "【" + nps.i18n("TEXT_INCLUDE") + "】";
				inTrs.each(function(i, dom) {
					var id = $j(dom).attr("data");
					var name = $j(dom).find("td:first").text();
					exp += id + ",";
					txt += name + ",";
				});
				exp = exp.substring(0, exp.length - 1) + ";";
				txt = txt.substring(0, txt.length - 1) + " ";
				var expMember = "";	// 会员排除
				var expGroup = "";	// 分组排除
				if (outTrs.length > 0) {
					txt += "【" + nps.i18n("TEXT_EXCLUDE") + "】";
					outTrs.each(function(i, dom) {
						var id = $j(dom).attr("data");
						var name = $j(dom).find("td:first").text();
						var isGroup = $j(dom).attr("data_isGroup");
						if (isGroup == "true") {
							expGroup += "," + id;
						} else { 
							expMember += "," + id;
						}
						txt += name + ",";
					});
					txt = txt.substring(0, txt.length - 1);
				}
				expMember = expMember.length > 0 ? expMember.substring(1) : expMember;
				expGroup = expGroup.length > 0 ? expGroup.substring(1) : expGroup;
				exp += expGroup + " ; " + expMember;
				var json = {
						id: id,
						name: name,
						expression: exp,
						text: txt,
						type: type
				};
				nps.asyncXhrPost(ADD_GROUP_URL, json, {successHandler : function(data, textStatus) {
					if (data.isSuccess) {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SUCCESS"));
						if (! IS_UPDATE) {
							$j("#group-block tbody").empty();
							$j("#group-block-exclude tbody").empty();
							$j("#txt-name").val("");
							$j("#group-block-include .btn-search").show();
						}
					} else {
						nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
					}
				}});
				break;
			case 3:	//自定义
				var trs = CUSTOM_IN_TBODY.find('tr');
				if (trs.length == 0) {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_DATA"));
					return;
				}
				
				var tr = CUSTOM_IN_TBODY.find('tr');
				
				var exp = tr.map(function(){
					return $j(this).attr('data');
				}).get().join(',');
				
				var text = tr.find('td:first').map(function(){
					return $j(this).html();
				}).get().join(',');
				
				var json = {
						id:id,
						name: name,
						expression: exp,
						text: "【" + nps.i18n("TEXT_INCLUDE") + "】" + text,
						type: type,
				};
				
				nps.asyncXhrPost(ADD_GROUP_URL, json, {
					successHandler : function(data, textStatus) {
						if (data.isSuccess) {
							nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SUCCESS"));
							if (! IS_UPDATE) {
								CUSTOM_IN_TBODY.empty();
								$j("#txt-name").val("");
							}
						} else {
							nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
						}
					}
				});
				break;
			case 4:	//组合
				var trs = COMBO_IN_TBODY.find("tr");
				if (trs.length == 0) {
					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_NO_DATA"));
					return;
				}
				var exp = "";
				var txt = "";
				trs.each(function(i, dom) {
					var id = $j(dom).attr("data");
					var name = $j(dom).find("td:first").text();
					exp += id + ",";
					txt += name + ",";
				});
				exp = exp.substring(0, exp.length - 1);
				txt = txt.substring(0, txt.length - 1);
				var json = {
						id: id,
						name: name,
						expression: exp,
						text: "【" + nps.i18n("TEXT_INCLUDE") + "】" + txt,
						type: type,
				};
				nps.asyncXhrPost(ADD_GROUP_URL, json, {
					successHandler : function(data, textStatus) {
						if (data.isSuccess) {
							nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SUCCESS"));
							if (! IS_UPDATE) {
								$j("#custom-block tbody").empty();
								$j("#txt-name").val("");
							}
						} else {
							nps.info(nps.i18n("INFO_TITLE_DATA"),data.description);
						}
					}
				});
				break;
			};
	});
	
	/* ------------------------------------------------- 分组 ------------------------------------------------- */
	//分组包含‘查询’按钮
	$j("#group-block-include .btn-search").click(function() {
		IS_GROUP_IN = true;
		$j("#allStaff_id").show();
		$j("#group-selector").dialogff({type:'open',close:'in',width:'530px',height:'300px'});
	});
	
	//浮层选‘全体’
	$j("#chk-all").change(function() {
		if ($j(this).prop("checked")) {
			$j("#group-selector .ui-block:eq(1)").hide();
		} else {
			$j("#group-selector .ui-block:eq(1)").show();
		}
	});
	
	//分组选择器‘确定’按钮
	$j("#group-selector .btn-ok").click(function() {
		var html = "<tr><td></td><td><a href='javascript:void(0);' class='func-button btn-remove'>删除</a></td></tr>";
		var curTbody = IS_GROUP_IN ? GROUP_IN_TBODY : GROUP_OUT_TBODY;
		var trs = curTbody.find("tr");
		if ($j("#chk-all").prop("checked")) {	//全体
			if (! IS_GROUP_IN) {
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_EXCLUDE_ALL"));
				return;
			}
			$j("#group-block-include .btn-search").hide();
			if (trs.length == 0) {	//表里无数据
				curTbody.append("<tr data='0' class='even'><td style='color:blue;font-weight:bold'>" + nps.i18n("TEXT_STAFF") 
						+ "</td><td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a></td></tr>");
				
				$j("#group-selector .dialog-close").click();
				$j("#group-selector :checkbox").prop("checked", false);
				$j("#group-selector .ui-block:eq(1)").show();
			} else {	//表里有数据
				nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SELECT_ALL"), function() {
					curTbody.empty();
					curTbody.append("<tr data='0' class='even'><td style='color:blue;font-weight:bold'>" + nps.i18n("TEXT_STAFF") 
							+ "</td><td><a href='javascript:void(0);' class='func-button btn-remove' title='删除'>删除</a></td></tr>");
					
					$j("#group-selector .dialog-close").click();
					$j("#group-selector :checkbox").prop("checked", false);
					$j("#group-selector .ui-block:eq(1)").show();
				});
			}
		} else {	//非全体
			if (trs.eq(0).attr("data") == "0") {
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("INFO_HAS_ALL"));
				return;
			}
			
			var chks = $j("#group-selector :checked:visible");
			chks.each(function(i, dom) {
				var has = false;
				trs.each(function(j, dom2) {
					if ($j(dom2).attr("data") == $j(dom).val()) {
						has = true;
						return;
					}
				});
				if (! has) {
					var tr = $j(html).attr("data", $j(dom).val());
					tr.find("td:first").text($j(dom).parent().text());
					if (! IS_GROUP_IN) {
						tr.attr("data_isGroup", "true");	// 是否是分组
					}
					curTbody.append(tr);
				}
			});
			
			$j("#group-selector .dialog-close").click();
			$j("#group-selector :checkbox").prop("checked", false);
			$j("#group-selector .ui-block:eq(1)").show();
		}
	});
	
	//分组包含‘删除’按钮
	$j("#group-block-include tbody").on("click", ".btn-remove", function() {
		var tr = $j(this).parent().parent().remove();
		if (tr.attr("data") == 0) {	//全体
			$j("#group-block-include .btn-search").show();
		}
		colourTbody(GROUP_IN_TBODY);
	});
	
	//分组排除‘查询’按钮
	$j("#group-block-exclude .btn-search").click(function() {
		IS_GROUP_IN = false;
		$j("#allStaff_id").hide();
		var type = + $j("#slt-exclude-type").val();
		if (type == 1) {	// 排除会员
			$j("#member-selector").dialogff({type:'open',close:'in',width:'1000px',height:'550px'});
		} else {	// 排除分组
			$j("#group-selector").dialogff({type:'open',close:'in',width:'530px',height:'300px'});
		}
	});
	
	//分组排除‘删除’按钮
	$j("#group-block-exclude tbody").on("click", ".btn-remove", function() {
		$j(this).parent().parent().remove();
		colourTbody(GROUP_IN_TBODY);
	});
	
	/* ------------------------------------------------- 组合 ------------------------------------------------- */
	//组合 ‘查询’按钮
	$j("#custom-block .btn-search").click(function() {
		var data = nps.syncXhrPost(CUSTOM_LIST_URL);
		var div = $j("#custom-selector .combo-list");
		div.empty();
		$j.each(data, function(i, obj) {
			var html = '<span class="children-store" style="width: 145px;">' 
				+ '<label><input type="checkbox" name="chk-group" data_type="' 
				+ obj.type + '" value="' 
				+ obj.id + '" />' + obj.name + '</label></span>';
			div.append(html);
		});
		$j("#custom-selector .btn-search").click();
		$j("#custom-selector").dialogff({type:'open',close:'in',width:'550px',height:'400px'});
	});
	
	//组合浮层‘确定’按钮
	$j("#custom-selector .btn-ok").click(function() {
		var html = "<tr><td></td><td><a href='javascript:void(0);' class='func-button btn-remove'>删除</a></td></tr>";
		var trs = COMBO_IN_TBODY.find("tr");
		$j("#custom-selector .combo-list :checked:visible").each(function(i, dom) {
			var has = false;
			trs.each(function(i, dom2) {
				if ($j(dom).val() == $j(dom2).attr("data")) {
					has = true;
					return;
				}
			});
			if (!has) {
				var tr = $j(html).attr("data", $j(dom).val());
				tr.find("td:first").text($j(dom).parent().text());
				COMBO_IN_TBODY.append(tr);
			}
		});
		
		colourTbody(COMBO_IN_TBODY);
		$j("#custom-selector .dialog-close").click();
		$j("#custom-selector :checkbox").prop("checked", false);
	});
	
	//组合‘删除’按钮
	$j("#custom-block tbody").on("click", ".btn-remove", function() {
		$j(this).parent().parent().remove();
		colourTbody(COMBO_IN_TBODY);
	});
	
	//浮层‘查询’按钮
	$j("#custom-selector .btn-search").click(function() {
		var name = $j.trim($j("#custom-selector .txt-name").val());
		var type = $j("#custom-selector select").val();
		var chks = $j("#custom-selector :checkbox");
		chks.parent().parent().hide();
		var reg = new RegExp("^.*" + name + ".*$", "i");
		if (type.length == 0) {	//不限
			chks.each(function(i, dom) {
				if (reg.test($j(dom).parent().text())) {
					$j(dom).parent().parent().show();
				}
			});
		} else {
			chks.each(function(i, dom) {
				if (type == $j(dom).attr("data_type") && reg.test($j(dom).parent().text())) {
					$j(dom).parent().parent().show();
				}
			});
		}
		
	});
	
	/* ------------------------------------------------- 自定义 ------------------------------------------------- */
	// "查询"
	$j('#user-defined-block').on('click', '.btn-search', function(){
		var result = nps.syncXhrPost(USER_DEFINED_LIST_URL);
		var userDefinedSelector = $j('#user-defined-selector');
		var comboList = userDefinedSelector.find('.combo-list');
		
		var filterIds = $j('#user-defined-block').find('table').find('tr').map(function(){
			return $j(this).attr('data');
		});
		
		var filterIdMap = {};
		for(var i=0; i<filterIds.length; i++){
			filterIdMap[filterIds[i]]=filterIds[i];
		}
		comboList.empty();
		$j.each(result, function(i, obj){
			var html = '<span class="children-store" style="width: 145px;">' 
				+ '<label><input type="checkbox" data_type="' +obj.scopeType + '"' ;
			if(filterIdMap[obj.id] != undefined) {
				html += 'checked="checked"';
			}
			html += ' value="' + obj.id + '" serviceName="'+ obj.serviceName
				+'" scopeName="'+obj.scopeName+'"/>' + obj.scopeName + '</label></span>';
			comboList.append(html);
		});
		$j("#user-defined-selector .btn-search").click();
		// 弹出dialog
		userDefinedSelector.dialogff({type:'open',close:'in',width:'550px',height:'400px'});
	});
	
	
	// 浮层中的"确定"按钮
	$j('#user-defined-selector').on('click', '.btn-ok', function(){
		var userDefinedSelector = $j('#user-defined-selector');
		var filterIds = $j('#user-defined-block').find('table').find('tr').map(function(){
			return $j(this).attr('data');
		});
		
		var filterIdMap = {};
		for(var i=0; i<filterIds.length; i++){
			filterIdMap[filterIds[i]]=filterIds[i];
		}
		
		var trHtmlArr = new Array();
		userDefinedSelector.find('.combo-list').find('input:checkbox:checked').each(function(i, obj){
			if(filterIdMap[$j(obj).val()] == undefined) {
				trHtmlArr.push('<tr data="'+$j(obj).val()+'"><td>');
				trHtmlArr.push($j(obj).attr('scopeName'));
				trHtmlArr.push('</td><td><a href="javascript:void(0);" class="func-button btn-remove">删除</a></td></tr>');
			}
		});
		CUSTOM_IN_TBODY.append(trHtmlArr.join(''));
		userDefinedSelector.find('.dialog-close').click();
	});
	
	// "删除"按钮
	$j('#user-defined-block').on('click', '.btn-remove', function(){
		$j(this).parents('tr').remove();
		colourTbody(CUSTOM_IN_TBODY);
	});
	
	//浮层‘查询’按钮
	$j("#user-defined-selector .btn-search").click(function() {
		var name = $j.trim($j("#user-defined-selector .txt-name").val());
		var chks = $j("#user-defined-selector :checkbox");
		chks.parent().parent().hide();
		var reg = new RegExp("^.*" + name + ".*$", "i");
		chks.each(function(i, dom) {
			if (reg.test($j(dom).parent().text())) {
				$j(dom).parent().parent().show();
			}
		});
	});
	
	//‘返回’按钮
	$j(".btn-return").click(function() {
		window.location.href = PREVIOUS_URL;
	});
});


/* ------------------------------------------------- util ------------------------------------------------- */
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
