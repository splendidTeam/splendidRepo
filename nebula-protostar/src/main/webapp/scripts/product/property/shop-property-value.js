$j.extend(loxia.regional['zh-CN'],{
	"info":"提示信息",
	"industry.product.duplicatedpropertyvalue":"属性值重复",
	"only.input.tencharacters":"此处最多只能输入10个字符",
	"successful-operation":"操作成功",
	"PLEASE_DATA_FILL":"请将信息填写完整"
});
//点击新增按钮次数
var time = 0;

var validateShopPropertyValueNameUrl =base + '/shop/validatePropertyValueName.json';

function checkFieldLength(value, obj){
	if(value.length > 10)
		return "only.input.tencharacters";
	return loxia.SUCCESS;
}

function setInput5(value, obj){
	alert("value:" + value);
	if(value === "date"){
		loxia.byId($j(obj.element).next("input").get(0)).val("2010-12-10");
	}else if(value === "number"){
		loxia.byId($j(obj.element).next("input").get(0)).val("100");
	}
	return loxia.SUCCESS;
}

/**
 * 上传图片后回调函数
 */
function propertyImageComplete(){
	
	var array = $j('input[name^="uploadPropertyImage"]');
	$j.each(array, function(i, obj){
		var _imgSrc = $j(this).val();
		var name = $j(this).attr('name');
		if(_imgSrc != '../images/main/mrimg.jpg' && _imgSrc != undefined && _imgSrc != ''){
			$j(this).siblings('input[name="propertyValues.thumb"]').val(_imgSrc);
			var $nextTd = $j(this).parent('td').next('td');
			$nextTd.find('div[id^="imguploadPropertyImage"]').find('img').attr('src', _imgSrc);	
		}
	})
}

/**
 * 修改商品的sortNo
 */
function modifyItemSortNo(){
	//倒数第二个tr
	var $tr = $j('#propertyTable').find('tbody:first').find('tr').eq(-2);
	var sortNo = $tr.find('td:first').find('input[name="propertyValues.sortNo"]').val();
	//倒数第一个tr
	var _$tr = $j('#propertyTable').find('tbody:first').find('tr:last');
	_$tr.find('td:first').find('input[name="propertyValues.sortNo"]').val(parseInt(sortNo)+1);
}

$j(document).ready(function(){
	
	$j('div[id^="uploadPropertyImage"]').hide();
	/**
	 * 提交表单
	 */
    $j(".button.orange.submit").click(function(val){
    	var flag=true;
    	$j("input[id='input3']").each(function(indexi,datai){
    		$j("input[id='input3']").each(function(indexj,dataj){ 
    			if(indexi!=indexj&&datai.value==dataj.value){
    				$j(this).addClass("ui-loxia-error");
    				nps.info(nps.i18n("info"),nps.i18n("industry.product.duplicatedpropertyvalue"));
        			flag=false;  
        		}
        	});
    	});
    	if(flag){
    		nps.submitForm('propertyForm',{mode:'async',
    			successHandler : function(data){
				if(data.isSuccess)
				{
					 nps.info(nps.i18n("info"),nps.i18n("successful-operation"));		
				}
		   }});   
    	}    		
    });  
    $j(".button.goback").click(function(){
           history.go(-1);
    });  
    
    //可选属性排序
    //向上
    $j('.arrow_up').live('click', function(){

    	var $currTr = $j(this).parents('tr');
    	var $prevTr = $j(this).parents('tr').prev('tr');
    	
    	//交换属性值的value
    	var _currValue = $currTr.find('input[name="propertyValues.value"]').val();
    	var _prevValue = $prevTr.find('input[name="propertyValues.value"]').val();
    	
    	$currTr.find('input[name="propertyValues.value"]').val(_prevValue);
    	$prevTr.find('input[name="propertyValues.value"]').val(_currValue);

    	//交换propertyValueId
    	var _currPVId = $currTr.find('input[name="propertyValues.id"]').val();
    	var _prevPVId = $prevTr.find('input[name="propertyValues.id"]').val();
    	
    	$currTr.find('input[name="propertyValues.id"]').val(_prevPVId);
    	$prevTr.find('input[name="propertyValues.id"]').val(_currPVId);
    	
    	//交换propertyId
    	var _currPId = $currTr.find('input[name="propertyValues.propertyId"]').val();
    	var _prevPId = $prevTr.find('input[name="propertyValues.propertyId"]').val();
    	
    	$currTr.find('input[name="propertyValues.propertyId"]').val(_prevPId);
    	$prevTr.find('input[name="propertyValues.propertyId"]').val(_currPId);
    	
    	//交换thumb
    	var _currThumb = $currTr.find('input[name="propertyValues.thumb"]').val();
    	var _prevThumb = $prevTr.find('input[name="propertyValues.thumb"]').val();
    	
    	$currTr.find('input[name="propertyValues.thumb"]').val(_prevThumb);
    	$prevTr.find('input[name="propertyValues.thumb"]').val(_currThumb);
    	
    	var _currImgThumb = $currTr.find('div[id^=imguploadPropertyImage]').find('img').attr('src');
    	var _prevImgThumb = $prevTr.find('div[id^=imguploadPropertyImage]').find('img').attr('src');
    	
    	$currTr.find('div[id^=imguploadPropertyImage]').find('img').attr('src', _prevImgThumb);
    	$prevTr.find('div[id^=imguploadPropertyImage]').find('img').attr('src', _currImgThumb);    	    	
    });
    
    //向下
    $j('.arrow_down').live('click', function(){
    	
    	var $currTr = $j(this).parents('tr');
    	var $nextTr =  $j(this).parents('tr').next('tr');
    	
    	//交换属性值的value
    	var _currValue = $currTr.find('input[name="propertyValues.value"]').val();
    	var _nextValue = $nextTr.find('input[name="propertyValues.value"]').val();
    	
    	$currTr.find('input[name="propertyValues.value"]').val(_nextValue);
    	$nextTr.find('input[name="propertyValues.value"]').val(_currValue);

    	//交换propertyValueId
    	var _currPVId = $currTr.find('input[name="propertyValues.id"]').val();
    	var _nextPVId = $nextTr.find('input[name="propertyValues.id"]').val();
    	
    	$currTr.find('input[name="propertyValues.id"]').val(_nextPVId);
    	$nextTr.find('input[name="propertyValues.id"]').val(_currPVId);
    	
    	//交换propertyId
    	var _currPId = $currTr.find('input[name="propertyValues.propertyId"]').val();
    	var _nextPId = $nextTr.find('input[name="propertyValues.propertyId"]').val();
    	
    	$currTr.find('input[name="propertyValues.propertyId"]').val(_nextPId);
    	$nextTr.find('input[name="propertyValues.propertyId"]').val(_currPId);
    	
    	//交换thumb
    	var _currThumb = $currTr.find('input[name="propertyValues.thumb"]').val();
    	var _nextThumb = $nextTr.find('input[name="propertyValues.thumb"]').val();
    	
    	$currTr.find('input[name="propertyValues.thumb"]').val(_nextThumb);
    	$nextTr.find('input[name="propertyValues.thumb"]').val(_currThumb);
    	
    	var _currImgThumb = $currTr.find('div[id^=imguploadPropertyImage]').find('img').attr('src');
    	var _nextImgThumb = $nextTr.find('div[id^=imguploadPropertyImage]').find('img').attr('src');
    	
    	$currTr.find('div[id^=imguploadPropertyImage]').find('img').attr('src', _nextImgThumb);
    	$nextTr.find('div[id^=imguploadPropertyImage]').find('img').attr('src', _currImgThumb);  
    	
    });
    
	$j(".ui-loxia-table").on("click","#propertyTable .ui-button[action='add']",function(){
		time++;
    	//****不可以新增两个或以上的空属性值
    	var i = 0;
    	var count = 0;
    	var $table = $j('#propertyTable');
    	var flag = false;
    	var $currObj = '';
    	$j("input[id='input3']").each(function(index, data){
    		count++;
    		if(data.value == ''){
    			i++;
       			if(i == 1){
       				$currObj = $j(this);
    			}
    			if(i > 1){
    				$currObj.addClass("ui-loxia-error");
	    			$table.find('tbody:first').find('tr:last').remove();
	    			nps.info(nps.i18n('info'), nps.i18n('PLEASE_DATA_FILL'));
	    			flag = true;
	    			return false;
	    		}
    		}
    	});
    	
    	if(flag){
    		return;
    	}
    	
    	var _td = '<td style="text-align: left;">';
		var _up = '<a href="javascript:void(0)" class="arrow_up"><img src="'+base+'/images/wmi/blacks/16x16/arrow_top.png" /></a>';
		var _down = '<a href="javascript:void(0)" class="arrow_down" ><img src="'+base+'/images/wmi/blacks/16x16/arrow_bottom.png" /></a>';
		var _td2 = '</td>';
		var _tdHtml = _td + _up + _down + _td2;
		
		//*****修改上下移动的按钮
		
		//原最后一个的tr中的html
		if(count == 1){
			var $firstTr = $table.find('tbody:first').find('tr:first');
			var $firstTrLastTd = $firstTr.find('td:last');
			$firstTrLastTd.remove();
			$firstTr.append(_td + '<a href="javascript:void(0)" class="arrow_down first"><img src="'+base+'/images/wmi/blacks/16x16/arrow_bottom.png" /></a>'  + _td2);
			
			var $lastTr = $table.find('tbody:first').find('tr:last');
			var $lastTrLastTd = $lastTr.find('td:last');
			$lastTrLastTd.remove();
			$lastTr.append(_td + _td2);
		}else if(count == 2){
			var $firstTr = $table.find('tbody:first').find('tr:first');
			var $firstTrLastTd = $firstTr.find('td:last');
			$firstTrLastTd.remove();
			$firstTr.append(_td + '<a href="javascript:void(0)" class="arrow_down first"><img src="'+base+'/images/wmi/blacks/16x16/arrow_bottom.png" /></a>'  + _td2);
			
			var $lastTr = $table.find('tbody:first').find('tr:last');
			var $lastTrLastTd = $lastTr.find('td:last');
			$lastTrLastTd.remove();
			$lastTr.append(_td + _up  + _td2);
		}else if(count > 2){
			var $tr = $table.find('tbody:first').find('tr').eq(-2);
			var $td = $tr.find('td:last');
			$td.remove();
			$tr.append(_tdHtml);
			
			
			var $lastTr = $table.find('tbody:first').find('tr:last');
			var $lastTrLastTd = $lastTr.find('td:last');
			$lastTrLastTd.remove();
			$lastTr.append(_td + _up  + _td2);	
		}
		
		//修改上传组件的hName属性
		var $upload = $table.find('tbody:first').find('tr:last').find('.imgUploadComponet');
		var _hName = $upload.attr('hname');
		$upload.attr('hname', _hName + "Template" + time);
		
		//*****修改商品的sortNo
		modifyItemSortNo();
		
	    //加载上传组件
		$j.getScript(base+'/scripts/ajaxfileupload.js');
	})
    //*****修改商品的sortNo
    modifyItemSortNo();
    
});

/**
 * 删除按钮时的提示中的确定
 */
$j(window).load(function(){
	$j(window.top.document).find(".button-ok").click(function(){
		//删除时修改第一个和最后一个的向上和向下的按钮
    	var _td = '<td style="text-align: left;">';
		var _up = '<a href="javascript:void(0)" class="arrow_up"><img src="'+base+'/images/wmi/blacks/16x16/arrow_top.png" /></a>';
		var _down = '<a href="javascript:void(0)" class="arrow_down first" ><img src="'+base+'/images/wmi/blacks/16x16/arrow_bottom.png" /></a>';
		var _td2 = '</td>';
		var $tbody = $j('#propertyTable').find('tbody:first');
		var $firstTr = $tbody.find('tr:first');
		var $firstTrLastTd = $firstTr.find('td:last');
		$firstTrLastTd.remove();
		$firstTr.append(_td + _down + _td2);
		
		var $lastTr = $tbody.find('tr:last');
		var $lastTrLastTd = $lastTr.find('td:last');
		$lastTrLastTd.remove();
		$lastTr.append(_td + _up + _td2);
	});
});


