$j.extend(loxia.regional['zh-CN'], {
	"LABEL_FEE_DISTRIBUTION" : "物流方式",
	"LABEL_FEE_AREA_NAME" : "目的地名称",
	"LABEL_FEE_FIRST_PART_UNIT" : "首件运费单位",
	"LABEL_FEE_SUBSEQUENT_PART_UNIT" : "续件运费单位",
	"LABEL_FEE_FIRST_PART_PRICE" : "首件运费价格",
	"LABEL_FEE_SUBSUQUENT_PART_PRICE" : "续件运费价格",
	"LABEL_FEE_BASE_PRICE" : "基础价格",
	
	"LABEL_FEE_DESIGNATE" : "类型",
	"LABEL_FEE_DESIGNATE" : "分组编号",
	"LABEL_FEE_DESIGNATE" : "操作",
	"INFO_TITLE_DATA" : "提示信息"
});
// Json格式动态获取数据库信息
var shippingFeeConfigUrl = base + '/freight/shippingFeeConfigList.json';
//return 模板界面
var returnShippingTemplateUrl = base + '/freight/shippingTemeplateList.htm';


// 获取id
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' name='chid' class='checkId' value='" + loxia.getObject("id", data) + "' disabled='disabled'/>";
}
// 刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}


$j(document).ready(function() {

	loxia.init({
		debug : true,
		region : 'zh-CN'
	});

	// 动态获取数据库区域信息表
	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		cols : [ {
			
			name : "distributionModeName",
			label : nps.i18n("LABEL_FEE_DISTRIBUTION"),
			width : "8%"
		},
		{
			name : "destAreaName",
			label : nps.i18n("LABEL_FEE_AREA_NAME"),
			width : "16%"
		}, {
			name : "firstPartUnit",
			label : nps.i18n("LABEL_FEE_FIRST_PART_UNIT"),
			width : "8%"
		}, {
			name : "subsequentPartUnit",
			label : nps.i18n("LABEL_FEE_SUBSEQUENT_PART_UNIT"),
			width : "8%"
		
		}, {
			name : "firstPartPrice",
			label : nps.i18n("LABEL_FEE_FIRST_PART_PRICE"),
			width : "8%"
		}, {
			name : "subsequentPartPrice",
			label : nps.i18n("LABEL_FEE_SUBSUQUENT_PART_PRICE"),
			width : "8%"
		}, {
			name : "basePrice",
			label : nps.i18n("LABEL_FEE_BASE_PRICE"),
			width : "8%"
		}
		],
		dataurl : shippingFeeConfigUrl+"?templateId="+templateId
	});
	refreshData();
	
	//返回
	$j(".return-back").click(function(){
		window.location.href = returnShippingTemplateUrl;
	});
	
});