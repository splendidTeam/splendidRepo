$j.extend(loxia.regional['zh-CN'],{
	"PROPERT_OPERATOR_TIP":"属性提示信息",
	"NO_DATA":"未找到数据",
	"LABEL_ITEM_CODE":"商品编码",
	"INFO_TITLE_DATA":"提示信息",
	"OPERATE_ENABLED":"启用",
	"OPERATE_DISENABLED":"禁用",
	"CONFIRM_DELETE":"是否删除",
	"DELETE_SUCCESS":"删除成功",
	"DELETE_FAILURE":"删除失败",
	"PLEASE_SELECT_REC":"请选择推荐信息",
	"CONFIRM_ENABLED":"确定启用推荐吗?",
	"CONFIRM_DISENABLED":"确定禁用推荐吗?",
	"ENABLED_FAILURE":"启用失败",
	"DISENABLED_FAILURE":"禁用失败",
	"ENABLED_SUCCESS":"启用成功",
	"DISENABLED_SUCCESS":"禁用成功",
	"ITEM_CODE_NOT_EMPTY":"主商品编码不可以为空",
	"MAIN_ITEM_CODE_NOT_EXISTS":"主商品编码不存在",
	"PLEASE_SAVE_DATA":"请先保存,再更换主商品,否则数据会丢失,是否更换主商品?",
	"MAIN_ITEM_ONLY_ONE":"主商品只能有一个"
});

/** 推荐类型 */
var type = '3';

var hasUpdate = false;

var param = '';

var paramCode = '';

var oldParamCode = '';

var paramName = '';

var cursorHand= '/images/wmi/blacks/16x16/cursor_hand.png';

//************************ url ************************
var recommandItemListUrl = base + '/recommand/recommanditemList.json';

var findItemInfoListJsonUrl = base + "/recommand/findItemInfoList.json";

var findItemListByIdsUrl = base + '/item/findItemListByIds.json';

var findItemByCodeUrl = base + '/item/findItemCommandByCode.json';

/**
 * 添加推荐商品
 * @param itemCodes
 */
function addRecommand(itemIds){
	
	var json = {"itemIds": itemIds};
	var data = loxia.syncXhr(findItemListByIdsUrl, json, {type:"POST"});
	var $tr = $j('#item-recommand-table').find('tbody').eq(0).find('tr');
	
	var html = '';
	$j.each(data, function(index, itemCommand){
		if(($tr.length+index)%2 == 1){
			html += '<tr class="odd">';
		}else{
			html += '<tr class="even">';
		}
		html += '<td><input type="checkbox" value=""/>';
		html += '<input name="recommandItems.id" type="hidden" value="" />';
		html += '<input name="recommandItems.type" type="hidden" value="'+type+'" />';
		html += '<input name="recommandItems.param" type="hidden" value="'+param+'" />';
		html += '<input name="recommandItems.itemId" type="hidden" value="'+itemCommand.id+'" />';
		html += '</td>';
		html += '<td title="点击此处可以拖动"><div class="cursor-hand"></div></td>';
		html += '<td>'+itemCommand.code+'</td>';
		html += '<td>'+itemCommand.title+'</td>';
		html += '<td>'+paramName+'</td>';
		html += '<td><a href="javascript:void(0);" class="func-button delete-recommand"><span>删除</span></a></td>';
		html += '</tr>';
	});
	hasUpdate = true;
	$j('#item-recommand-table').find('tbody').eq(0).append(html);
}

/**
 * 获取推荐商品列表
 */
function getRecommandItem(param){
	var json='';
	if(param == ''){
		json = {"type":type};
	}else{
		json = {"type":type, "param":param};
	}
	loxia.asyncXhr(recommandItemListUrl, json,{
		type:"POST",
		success:function(data){
			//console.log(data);
			var html = '';
			$j.each(data, function(index, recommandItemCommand){
				if(index%2 == 1){
					html += '<tr class="odd">';
				}else{
					html += '<tr class="even">';
				}
				html += '<td><input type="checkbox" value="'+recommandItemCommand.id+'"/>';
				html += '<input name="recommandItems.id" type="hidden" value="'+recommandItemCommand.id+'" />';
				html += '<input name="recommandItems.type" type="hidden" value="'+recommandItemCommand.type+'" />';
				html += '<input name="recommandItems.param" type="hidden" value="'+recommandItemCommand.param+'" />';
				html += '<input name="recommandItems.itemId" type="hidden" value="'+recommandItemCommand.itemId+'" />';
				html += '</td>';
				html += '<td title="点击此处可以拖动"><div class="cursor-hand"></div></td>';
				html += '<td>'+recommandItemCommand.code+'</td>';
				html += '<td>'+recommandItemCommand.title+'</td>';
				html += '<td>'+recommandItemCommand.paramName+'</td>';
				html += '<td><a href="javascript:void(0);" class="func-button delete-recommand"><span>删除</span></a></td>';
				html += '</tr>';
			});
			$j('#item-recommand-table').find('tbody').eq(0).html(html);
		}
	});
}

/**
 * 刷新数据
 */
function mainItemRefreshData() {
	$j("#main-item-table").loxiasimpletable("refresh");
}

function checkboxTemplate(data){
	return "<input type='radio' name='chid' class='checkId' value='"+loxia.getObject("code", data)+"'/>";
}

function operateTemplate(data){
	return '<a href="javascript:void(0);" class="func-button take-back" value="'+ loxia.getObject("code", data) +'"><span>带回</span></a>';
}

function changeItemCode(code){
	oldParamCode = paramCode;
	if(code == ''){
		getRecommandItem('');
	}else{
		var json = {"code":code};
		if(hasUpdate){
			nps.confirm(nps.i18n("INFO_TITLE_DATA"), nps.i18n("PLEASE_SAVE_DATA"), function(){
				var data = loxia.syncXhr(findItemByCodeUrl, json, {type: "POST"});
				if(data == '' || data == null || data == undefined){
					nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("MAIN_ITEM_CODE_NOT_EXISTS"));
				}
				param = data.id;
				paramName = data.title;
				hasUpdate = false;
				getRecommandItem(param);
				$j('#itemCode').val(code);
			});
			$j('#itemCode').val(oldParamCode);
		}else{
			var data = loxia.syncXhr(findItemByCodeUrl, json, {type: "POST"});
			if(data == '' || data == null || data == undefined){
				nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("MAIN_ITEM_CODE_NOT_EXISTS"));
			}
			param = data.id;
			paramName = data.title;
			getRecommandItem(param);
		}
		paramCode = code;
	}
}

$j(document).ready(function() {	
	
	getRecommandItem('');
	
	
	/** 查询 */
	$j('.mainItemQuery').click(function(){
		$j('#mainItemCode').val('');
		$j('#mainItemName').val('');
		mainItemRefreshData();
		$j('.main-item-dialog').dialogff({type:'open',close:'in',width:'900px', height:'500px'});
	});
	
	// search
	$j('.mainItemSearch').click(function(){
		mainItemRefreshData();
	});
	
	/** 确定 */
	$j('.main-item-dialog').on('click', '.confirm', function(){
		var checkBox = $j('.main-item-dialog').find('table tbody tr td').find('input:radio:checked');
		if(checkBox.length >= 2){
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("MAIN_ITEM_ONLY_ONE"));
			return;
		}
		var itemCode = checkBox.val();
		$j('#itemCode').val(itemCode);
		changeItemCode(itemCode);
		$j('.dialog-close').click();
	});

	/** 带回 */
	$j('.main-item-dialog').on('click', '.take-back', function(){
		var itemCode = $j(this).attr("value");
		$j('#itemCode').val(itemCode);
		changeItemCode(itemCode);
		$j('.dialog-close').click();
	});
	
	// 动态获取数据库商品信息表
	$j("#main-item-table").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		form : "mainItemDialogSearchForm",
		cols : [ {
			label : "",
			width : "5%",
			template : "checkboxTemplate",
		}, {
			name : "code",
			label : "商品编码",
			width : "10%",
			sort: ["tpi.code asc","tpi.code desc"]
		}, {
			name : "title",
			label : "商品名称",
			width : "20%",
			sort: ["tpii.title asc","tpii.title desc"]
		}, {
			name : "操作",
			label : "操作",
			width : "5%",
			template : "operateTemplate",
		}],
		dataurl : findItemInfoListJsonUrl
	});
	
	
	
	
	/** 修改主商品编码 */
	$j('#itemCode').change(function(){
		changeItemCode($j('#itemCode').val());
	});	
	

	//新增
	$j(".content-box .add").click(function(){
		if(param == ''){
			nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("MAIN_ITEM_CODE_NOT_EXISTS"));
			return;
		}
		$j('#itemCodes').val("");
		dialogRefreshData();
		$j('.filtrate-item-dialog').dialogff({type:'open',close:'in',width:'900px', height:'500px'});
	});
	 
	 

	/**逻辑删除(单个)*/
	$j("#item-recommand-table").on("click",".delete-recommand",function(){
		if( $j.trim(param) == ''){
			nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("ITEM_CODE_NOT_EMPTY"));
			return;
		}
		var currTrObj = $j(this).parents('tr');
		nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("CONFIRM_DELETE"), function(){
			currTrObj.remove();
			hasUpdate = true;
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("DELETE_SUCCESS"));
			return;
		});
	});
		 
	/** 批量逻辑删除 */
	$j(".button.delete").click(function(){
		if(param == ''){
			nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("MAIN_ITEM_CODE_NOT_EXISTS"));
			return;
		}
		
		var checkedRecommand = $j('#item-recommand-table').find('tbody').eq(0).find('input:checkbox:checked');
		if(checkedRecommand.length < 1){
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("PLEASE_SELECT_REC"));
			return;
		}
		nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("CONFIRM_DELETE"), function(){
			checkedRecommand.parents('tr').remove();
			hasUpdate = true;
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("DELETE_SUCCESS"));
			return;
		});
	});
	
	/** 全选 */
	$j('#item-recommand-table thead').on('click', 'input:checkbox', function(){
		if($j(this).attr('checked') == 'checked'){
			$j(this).parents('table').find('tbody').eq(0).find('input:checkbox').attr('checked', 'checked');
		}else{
			$j(this).parents('table').find('tbody').eq(0).find('input:checkbox').removeAttr('checked');
		}
	});
	 
	 
	 /** 保存 */
	$j('.button.save').click(function(){
		
		if(param == ''){
			nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("MAIN_ITEM_CODE_NOT_EXISTS"));
			return;
		}
		$j('#type').val(type);
		$j('#param').val(param);
		
		nps.submitForm('recommandForm',{mode: 'async', 
			successHandler : function(data){
			if(data.isSuccess == true){
				nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("SAVE_SUCCESS"));
				hasUpdate = false;
			}else{
				return nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("SAVE_FAILURE"));
			}
		}});
	});
});