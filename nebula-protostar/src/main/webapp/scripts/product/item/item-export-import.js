$j.extend(loxia.regional['zh-CN'],{
	"SYSTEM_MESSAGE":"提示信息",
	"CHOICE_NEED_EXPORT_COLUMN":"请选择你需要导出的字段！",
	"UPLOAD_NEED_FILE":"确保您已上传需要导入的文件！",
	"PROCESSING":"正在处理, 请稍后...",
	"PROCESS_SUCCESS":"操作成功",
	"PROCESS_FAILTRUE":"操作失败[{0}]",
	"EXPORT_ITEM_OUT_COUNT":"最多只能导出100个商品信息"
});

var itemListUrl = base + '/item/itemList.htm';

var findPropertyByIndustryIdUrl = base + '/item/findPropertyByIndustryId.json';
// 商品导入
var itemImportUrl = base + '/item/itemImport.json';

/**
 * 加载行业属性
 */
function loadIndustryProperty(){
	var industryId = $j('#industry').val();
	var json={"industryId":industryId};
	var result = nps.syncXhrPost(findPropertyByIndustryIdUrl, json);

	var propertyList = result.description;
		
	var hasCommonProperty = false;
		
	var commonProperty = new Array();
	//var salesProperty = new Array();
	var template = '<label class="normal"><input type="checkbox" name="selectCodes" value="TEMPLATE-INPUT-NAME">TEMPLATE-NAME</label>';
	for(var i = 0; i < propertyList.length; i++){
		var property = propertyList[i];
		if(property.isSaleProp || property.isColorProp){
			//salesProperty.push(template.replace(/TEMPLATE-NAME/g, property.name).replace(/TEMPLATE-INPUT-NAME/g, property.id));
		}else{
			commonProperty.push(template.replace(/TEMPLATE-NAME/g, property.name).replace(/TEMPLATE-INPUT-NAME/g, property.id));
			hasCommonProperty = true;
		}
	}

	if(!hasCommonProperty || propertyList == ''){
		$j('.commonProperty').parent('.ui-block-line').hide();
	}else{
		$j('.commonProperty').parent('.ui-block-line').show();
	}
	
	//$j('.salesProperty').html(salesProperty.join(''));
	$j('.commonProperty').html(commonProperty.join(''));
}

function fnBeforeSubmit(formData){
	nps.info(nps.i18n("SYSTEM_MESSAGE"), nps.i18n("PROCESSING"));
}

function fnComplete(data){
	if(data.isSuccess){
		nps.info(nps.i18n("SYSTEM_MESSAGE"), nps.i18n("PROCESS_SUCCESS"));
	}else{
		nps.info(nps.i18n("SYSTEM_MESSAGE"), nps.i18n("PROCESS_FAILTRUE", [data.description]));
	}
	$j('input[name="excelFile"]').val('');
	$j('.import').attr("class","button orange import");
	$j('.import').val("确定导入");
	$j('.import').removeAttr("disabled");
}

$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
    loadIndustryProperty();
    
	$j('.ui-block-content').on('change', '.ui-block-line :checkbox', function(){
		var this_checked=$j(this).is(':checked');
		var hl_span=$j(this).parent();
		if(this_checked){hl_span.addClass('selected');}
		else{hl_span.removeClass('selected');}
	});
	
	// 返回
	$j('.back').click(function(){
		window.location.href = itemListUrl;
	});
	
	/**
	//下载该行业模板
	$j("#downLoadTmplOfSkuInfo").on("click",function(){
		var industryId = $j("#industry").val();
		//var json={"industryId":industryId};
		location.href = base + "/sku/tplt_sku_import.xls?industryId="+industryId;
	});
	*/
	// 修改行业是加载行业的属性
	$j("#industry").change(function(){
		loadIndustryProperty();
	});
	
	
	// 确定导出
	$j('.export').click(function(){
		var selectedObj = $j('.dashedwp').find('.normal.selected');
		if(selectedObj.length <= 0){
			nps.info(nps.i18n("SYSTEM_MESSAGE"), nps.i18n("CHOICE_NEED_EXPORT_COLUMN"));
			return;
		}
		
		var itemCodes = $j('textarea[name="itemCodes"]').val();
		if(itemCodes.split('\n').length > 100){
			nps.info(nps.i18n("SYSTEM_MESSAGE"), nps.i18n("EXPORT_ITEM_OUT_COUNT"));
			return;
		}
		
		$j('#industryId').val($j("#industry").val());
		$j('#exportItemForm').submit();
	});

	/*******************************导入******************************************/
	
    $j('#importItemForm').ajaxForm({
    	url: itemImportUrl,
    	dataType: 'json',
    	beforeSubmit: fnBeforeSubmit,
        success: fnComplete  
   });
	
	// 确定导入
	$j('.import').click(function(){
		var file = $j('input[name="excelFile"]').val();
		if(!file){
			nps.info(nps.i18n("SYSTEM_MESSAGE"), nps.i18n("UPLOAD_NEED_FILE"));
			return;
		}
		$j('#import-industry-id').val($j("#import-industry").val());
		$j('.import').attr("class","button import");
		$j('.import').val("正在导入");
		$j('.import').attr("disabled","disabled");
		$j('#importItemForm').submit();
	});
});