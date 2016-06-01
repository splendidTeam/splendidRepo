$j(document).ready(function(){
	$j(':radio[name=type]').bind('change', function(){
		var currVal = $j(this).val();
		
		if(currVal == 'product') {
			$j('#selectProList_product').show();
			$j('#selectProList_style').hide();
		} else {
			$j('#selectProList_product').hide();
			$j('#selectProList_style').show();
		}
	});
});

function findProduct(){
	$j("#selectProList_product").loxiasimpletable({
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
}