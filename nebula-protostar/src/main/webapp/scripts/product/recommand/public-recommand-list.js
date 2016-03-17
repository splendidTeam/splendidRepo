$j.extend(loxia.regional['zh-CN'],{
	"PROPERT_OPERATOR_TIP":"属性提示信息",
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
	"PLEASE_SELECT_RECOMMAND_PARAM":"请选择推荐类型",
	"SAVE_SUCCESS":"保存成功",
	"SAVE_FAILURE":"保存失败",
	"IS_CHANGE_TYPE":"请先保存,再更换推荐类型,否则数据会丢失,是否更换推荐类型?"
});

/** 推荐类型 */
var type = 1;

var itemIdList = new Array();

var hasUpdate = false;

var paramValue = '';

var cursorHand= '/images/wmi/blacks/16x16/cursor_hand.png';

//************************ url ************************
var recommandItemListUrl = base + '/recommand/recommanditemList.json';

var findItemListByIdsUrl = base + '/item/findItemListByIds.json';
//新建
var createRecommandItemUrl = base+"/recommand/createRecommandItem.htm";
//修改
var updateRecommandItemUrl = base + '/recommand/updateRecommandItem.htm';
// 启用或禁用
var enabledOrDisenabledRecUrl = base + '/recommand/enabledOrDisenabledRecItem.json';
//删除
var removeRecommandItemUrl = base + '/recommand/removeRecommandItem.json';
//批量删除
var batchRemoveRecItemUrl = base + '/recommand/batchRemoveRecItem.json';


//获取日期
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
}


function checkRecommandParam(){
	var param = $j('#recommandParam').val();
	if(param == '' || param == null || param == undefined){
		nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("PLEASE_SELECT_RECOMMAND_PARAM"));
		return;
	}
	return param;
}

/**
 * 获取公共推荐商品列表
 */
function getPublicRecommandItem(param){
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
//				html += '<td>'+recommandItemCommand.paramName+'</td>';
				html += '<td><a href="javascript:void(0);" class="func-button delete-recommand"><span>删除</span></a></td>';
				html += '</tr>';
			});
			$j('#public-recommand-item-table').find('tbody').eq(0).html(html);
		}
	});
}

/**
 * 添加推荐商品
 * @param itemCodes
 */
function addRecommand(itemIds){
//	console.log(itemIds);
	var json = {"itemIds": itemIds};
	var data = loxia.syncXhr(findItemListByIdsUrl, json, {type:"POST"});
	var $tr = $j('#public-recommand-item-table').find('tbody').eq(0).find('tr');
	var param = checkRecommandParam();
//	var paramName = '';
//	$j.each(recommandParamJson, function(index, recommandParam){
//		if(recommandParam.optionValue == param){
//			paramName = recommandParam.optionLabel;
//		}
//	});
	
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
		html += '<td  title="点击此处可以拖动"><div class="cursor-hand"></div></td>';
		html += '<td>'+itemCommand.code+'</td>';
		html += '<td>'+itemCommand.title+'</td>';
//		html += '<td>'+paramName+'</td>';
		html += '<td><a href="javascript:void(0);" class="func-button delete-recommand"><span>删除</span></a></td>';
		html += '</tr>';
	});
	hasUpdate = true;
	$j('#public-recommand-item-table').find('tbody').eq(0).append(html);
}

$j(document).ready(function() {	
	// 隐藏dialog
	$j('.filtrate-item-dialog').hide();
	param = $j('#recommandParam').val();
	getPublicRecommandItem(param);
	
	 //新增
	 $j(".content-box .add").click(function(){
		var param = $j('#recommandParam').val();
		if(param == '' || param == null || param == undefined){
			nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("PLEASE_SELECT_RECOMMAND_PARAM"));
			return;
		}
		$j('#itemCodes').val("");
		dialogRefreshData();
		$j('.filtrate-item-dialog').dialogff({type:'open',close:'in',width:'900px', height:'500px'});
	 });
	 
	 
	/**逻辑删除(单个)*/
	$j("#public-recommand-item-table").on("click",".delete-recommand",function(){
		var param = $j('#recommandParam').val();
		if(param == '' || param == null || param == undefined){
			nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("PLEASE_SELECT_RECOMMAND_PARAM"));
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
		var param = $j('#recommandParam').val();
		if(param == '' || param == null || param == undefined){
			nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("PLEASE_SELECT_RECOMMAND_PARAM"));
			return;
		}
		
		var checkedRecommand = $j('#public-recommand-item-table').find('tbody').eq(0).find('input:checkbox:checked');
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
	 
	 /** 推荐类型 */
	 $j('#recommandParam').on("change", function(){
		if(hasUpdate){
			var param = $j('#recommandParam').val();
			
			nps.confirm(nps.i18n("INFO_TITLE_DATA"),nps.i18n("IS_CHANGE_TYPE"), function(){
				// 当修改推荐类型时, 去掉thead中的checkbox的选中状态
				$j('#public-recommand-item-table thead').find('input:checkbox').removeAttr('checked');
				hasUpdate = false;
				getPublicRecommandItem(param);
				$j('#recommandParam').val(param);
			});
			$j('#recommandParam').val(paramValue);
			
		}else{
			// 当修改推荐类型时, 去掉thead中的checkbox的选中状态
			$j('#public-recommand-item-table thead').find('input:checkbox').removeAttr('checked');
			var param = $j(this).val();
			getPublicRecommandItem(param);
		}
		paramValue = $j('#recommandParam').val();
	 });

	 
	 /** 全选 */
	 $j('#public-recommand-item-table thead').on('click', 'input:checkbox', function(){
		if($j(this).attr('checked') == 'checked'){
			$j(this).parents('table').find('tbody').eq(0).find('input:checkbox').attr('checked', 'checked');
		}else{
			$j(this).parents('table').find('tbody').eq(0).find('input:checkbox').removeAttr('checked');
		}
	 });
	 
	 /** 保存 */
	 $j('.button.save').click(function(){
		var param = $j('#recommandParam').val();
		if(param == '' || param == null || param == undefined){
			nps.info(nps.i18n('INFO_TITLE_DATA'), nps.i18n("PLEASE_SELECT_RECOMMAND_PARAM"));
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

