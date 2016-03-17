$j.extend(loxia.regional['zh-CN'], {
	"SYSTEM_ITEM_MESSAGE":"提示信息",
	"WRITE_ALL_INFO":"请将信息填写完整",
	"UPDATEITEM_FAIL":"保存商品图片失败",
	"UPDATEITEM_SUCCESS":"保存商品图片成功",
	"SELECT_COLOR_PROPERTY":"请选择颜色属性",
	"ITEM_IMAGE_DESCRIPTION":"图片描述",
	"IMAGE_PATH":"图片路径",
	"IMAGE_BROWSE":"浏览",

	"UPLOAD_IMAGE":"图片地址不可以是默认地址,请上传图片",
	"IMAGE_TYPE":"图片类型",
	"REMOVE_ALL_ITEMIMAGE":"确定删除{0}商品的全部图片吗?",
	"ITEM_NO_IMAGE":"{0}商品还没有上传图片",
	"ITEM.IMAGE.POSITION":"图片排序"
});


var batchRemoveItemImageByItemIdUrl = base + '/itemImage/removeItemImageByItemId.json';

//缩略图大小
var thumbnailConfig ="";
//上传组件中的hname图片Id
var hnameIndex = 100;
//商品Id
var itemId = 100;
//图片路径的域名, 如:http://127.0.0.1
var baseImageUrl = '';

var DEFAULT_IMAGE_URL = '../images/main/mrimg.jpg';


///**
// * 验证只能添加一下为空
// * @returns {Boolean}
// */
//function validatorOnlyOneNewIsEmpty(){
//	var currPropertyArray = $j('.ui-block-line.color-select-line');
//	var isEmpty = false;
//	$j.each(currPropertyArray, function(index, itemImage){
//		var _picUrl = $j(this).find('input[id="itemImageUrl"]').val();
//		if( _picUrl == '' || _picUrl == DEFAULT_IMAGE_URL){
//			$j(this).find('input[name="itemImages.picUrl"]').addClass('ui-loxia-error');
//			isEmpty = true;
//		}
//	});
//	return isEmpty;
//}
/**
 * 是否存在增加了itemImage,但是没有上传图片
 * @returns {Boolean}
 */
function isUploadImage(){
	var currPropertyArray = $j('.ui-block-line.color-select-line');
	var isImage = false;
	//var isNeedCheck =false;
	$j.each(currPropertyArray, function(index, itemImage){
		//isNeedCheck =false;
		
		if(i18nOnOff){
			$j.each($j(this).find('.itemImage-add'), function(idx, itm){
				var curLang =$j(this).find("img").attr("lang");
				if(curLang==defaultlang){
					var _picUrl = $j(this).find('input[id="itemImageUrl"]').val();
					if(_picUrl == '' || _picUrl == DEFAULT_IMAGE_URL){
						$j(this).find('input[name="itemImages.picUrl"]').addClass('ui-loxia-error');
						isImage = true;
					}
				}
			});
			
		}else{
			var _picUrl = $j(this).find('input[id="itemImageUrl"]').val();
			if(_picUrl == '' || _picUrl == DEFAULT_IMAGE_URL){
				$j(this).find('input[name="itemImages.picUrl"]').addClass('ui-loxia-error');
				isImage = true;
			}
		}
		
	});
	return isImage;
}

/**
 * 上传图片完成后, 回调的方法
 */
function colorComplete(data,hName){
	//var _picUrl = data.url; 
	//上传图片完成后, 将在Img中显示上传的图片
	var selector="";
	var lang ="";
	if(i18nOnOff){
		 selector = hName.split("-")[0];
		 lang = hName.split("-")[1];
	}else{
		selector = hName;
	}
	var currPropertyArray = $j("."+selector);
	
	$j.each(currPropertyArray, function(index, itemImage){
		var p = null;
		if(i18nOnOff){
			p = $j(this).parent();
		}else{
			p = $j(this);
		}
		var _picUrl = p.find('input[name="'+hName+'"]').val();
		if(_picUrl != DEFAULT_IMAGE_URL && _picUrl != undefined){
			p.find(".picUrl-mutl").each(function(){
				var me = $j(this);
				if(i18nOnOff){
					var key = me.attr("lang");
					if(lang==key){
						me.val(_picUrl);
					}
				}else{
					me.val(_picUrl);
				}
			});
			p.parents(".color-select-line-add").find("img").each(function(){
				var me = $j(this);
				if(i18nOnOff){
					var key = me.attr("lang");
					if(lang==key){
						me.attr("src",_picUrl);
					}
				}else{
					me.attr("src",_picUrl);
				}
			});
		}
		
		var role = $j(this).find('.fileupload').attr('role');
		
		// show or hide url table
		if(i18nOnOff){
			if(lang==$j(this).find("img").attr("lang")){
				//渲染对应语言的url table
				
				generateUrlTable(_picUrl, role, $j(this));
			}			
		}else{
			generateUrlTable(_picUrl, role, $j(this).parents(".ui-block-line"));
		}
		
		
		
	});
	
	
	
}

/**
 * 生成URL table
 */
function generateUrlTable(picUrl, role, object){
	var htmlArr = new Array();
	htmlArr.push('<table>');
	htmlArr.push('<tr><th style="width:80px;">规格</th><th style="width:380px;">URL</th></tr>');
	
	var imageSizes = role.split('|');
	
	for(var size in imageSizes){
		console.log(imageSizes[size], role, picUrl);
		htmlArr.push('<tr style="left: 10px;">');
		htmlArr.push('<td style="text-align: center;">');
		htmlArr.push(imageSizes[size]);
		htmlArr.push('</td>');
		htmlArr.push('<td style="text-align: center;">');
		htmlArr.push(picUrl.split('_')[0] + '_' + imageSizes[size] + picUrl.substring(picUrl.lastIndexOf('.')));
		htmlArr.push('</td>');
		htmlArr.push('</tr>');
	}
	htmlArr.push('</table>');
	
	object.find('.urlTab').html("");
	
	object.find('.urlTab').html(htmlArr.join(''));
}


/**
 * 更改属性值时, 显示对应的图片信息
 */
function changePropertyValue(){
	var itemProeprties = $j('#colorProperty').val();
	if(itemProeprties == '' ){
		$j('#colorPropertyContent').children('div').hide();
	}else{
		$j("#itemProeprtiesIs"+itemProeprties+"").show().siblings('div[id^="itemProeprtiesIs"]').hide();
	}
}

function resetseq(){
	//重新计算下标
	var lineAdd = $j(".color-select-line-add");
	lineAdd.each(function(i,dom){
		var me1 = $j(dom);
		var resetClass ='';
		if(i18nOnOff){
			resetClass =".itemImage-add";
		}else{
			resetClass ='.imgTag';
		}
		me1.find(resetClass).each(function(j,dom){
			var me = $j(dom);
			
			if(i18nOnOff){
				var desc = me.find(".description-mutl");
				var name1 = desc.attr("name");
				var name1 = name1.split("[")[0] + "[" + i + "-" + name1.split("-")[1];
				desc.attr("name",name1);
				
				var desclang = me.find(".description-mutl-lang");
				var name3 = desclang.attr("name");
				var name3 = name3.split("[")[0] + "[" + i + "-" + name3.split("-")[1];
				desclang.attr("name",name3);
				
				var url = me.find(".picUrl-mutl");
				var name2 = url.attr("name");
				var name2 = name2.split("[")[0] + "[" + i + "-" + name2.split("-")[1];
				url.attr("name",name2);
				
				var urllang = me.find(".picUrl-mutl-lang");
				var name4 = urllang.attr("name");
				var name4 = name4.split("[")[0] + "[" + i + "-" + name4.split("-")[1];
				urllang.attr("name",name4);
			}else{
				var desc = me.find(".description-mutl");
				var name1 = desc.attr("name");
				var arr1 = name1.split("[");
				var name1 = arr1[0]+"["+i+"]";
				desc.attr("name",name1);
				
				/*var desclang = me.find(".description-mutl-lang");
				var name3 = desclang.attr("name");
				var arr3 = name3.split("-");
				var name3 = arr3[0].substring(0,arr3[0].length-1)+i+"-"+arr3[1];
				desclang.attr("name",name3);*/
				
				var url = me.find(".picUrl-mutl");
				var name2 = url.attr("name");
				var arr2 = name2.split("[");
				var name2 = arr2[0]+"["+i+"]";
				url.attr("name",name2);
				
				/*var urllang = me.find(".picUrl-mutl-lang");
				var name4 = urllang.attr("name");
				var arr4 = name4.split("-");
				var name4 = arr4[0].substring(0,arr4[0].length-1)+i+"-"+arr4[1];
				urllang.attr("name",name4);*/
			}
			
			
		});
		
	});
}

$j(document).ready(function() {
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
    itemImageTypeHtml = $j('#chooseOptionItemImageType').html();
    
    itemId = $j("#itemId").val();
    
    thumbnailConfig = $j("#thumbnailConfig").val();
    
    baseImageUrl = $j('#baseImageUrl').val();
    
    
    changePropertyValue();
    
    $j.each($j("select[name='itemImages.type']"), function(i, obj){
    	var obj = $j(this);
    	var imgTypeValue = obj.val();
    	$j.each(thumbConfigList, function(i, config){
    		var imgRoleLabel = config.optionLabel;
    		if(imgTypeValue == imgRoleLabel){
    			var imgRoleValue = config.optionValue;
    			obj.parents(".color-select-line").find(".imgUploadComponet").attr("role", imgRoleValue);
    		}
    	});
    });
    
    //--------向上 
	$j(".ui-block").on("click",".up-ic",function(e){
		e.stopPropagation();
		var thisprev=$j(this).parents(".color-select-line-add").prev(".color-select-line-add");
		if(thisprev.length>0){
			$j(this).parents(".color-select-line-add").detach().insertBefore(thisprev);
			//重新计算下标
			resetseq();
		}
	});
	
	//-------向下
	$j(".ui-block").on("click",".down-ic",function(e){
		e.stopPropagation();
		var thisnext=$j(this).parents(".color-select-line-add").next(".color-select-line-add");
		if(thisnext.length>0){
			$j(this).parents(".color-select-line-add").detach().insertAfter(thisnext);
			//重新计算下标
			resetseq();
		}
	});
	
	//-------删除
	$j(".ui-block").on("click",".color-select-line .delete-ic",function(e){
		e.stopPropagation();
		var thiscolor=$j(this).parent().parent().parent(".color-select-line");
		thiscolor.remove();
		//重新计算下标
		resetseq();
		
	});
	
	//-------添加
	$j('#colorPropertyContent').on("click", ".color-select-add", function(){
		var type = $j(this).attr('optionValue');
		var itemProperties = $j('#colorProperty').val();
		if(itemProperties == undefined){
			itemProperties = '';
		}
		var roleValue = '';
		var $colorPropertyContent = '';
		if(isImageTypeGroup){
			roleValue = type;
			$colorPropertyContent = $j("#"+type+itemProperties);
		}else{
			roleValue = $j('#chooseOptionItemImageType').children('select').val();;
			$colorPropertyContent = $j(this).parents('div[id=itemProeprtiesIs'+itemProperties+']').find('.ui-block-content');
		}
		
		//验证是否有新建的, 且没有填写内容
//		var isEmpty = validatorOnlyOneNewIsEmpty();
//		if(isEmpty){
//			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("UPLOAD_IMAGE"));
//			return;
//		}
		var imgRoleValue = "";
		$j.each(thumbConfigList, function(i, config){
    		var imgRoleLabel = config.optionLabel;
    		if(roleValue == imgRoleLabel){
    			imgRoleValue = config.optionValue;
    		}
    	});
		var htmlArr = new Array();
		var len = $j(".color-select-line-add").length;
		if(i18nOnOff){
			htmlArr.push('<div class="ui-block-line color-select-line color-select-line-add" style="border-top: 1px solid gray;margin-top: 2px;">');
			/*htmlArr.push('<label><img src="'+DEFAULT_IMAGE_URL+'" class="color-select-img"/></label>');*/
			htmlArr.push('<input type="hidden" loxiaType="input" name="itemImages.id" value="" />');
			htmlArr.push('<input type="hidden" loxiaType="input" name="itemImages.itemProperties" value="'+itemProperties+'" />');
			
			htmlArr.push('<div style="margin-left: 0px;">');
			htmlArr.push('<div class="color-select-line">');
			htmlArr.push('<span style="font: inherit;font-size: 13px;margin-left: 134px;">'+nps.i18n('IMAGE_TYPE')+':</span>');
			if(isImageTypeGroup){
				htmlArr.push(itemImageTypeHtml.replace('selected', '').replace('"'+type+'"', '"'+type+'"'+'selected').replace('=""', ''));
			}else{
				htmlArr.push(itemImageTypeHtml);
			}
			htmlArr.push('<span class="common-ic delete-ic"></span>');
			htmlArr.push('</div>');
			for ( var j = 0; j < i18nLangs.length; j++) {
				var key =i18nLangs[j].key;
				var val =i18nLangs[j].value;
				//包裹1
				htmlArr.push('<div class="itemImage'+len+' itemImage-add" style="float: left;width: 100%;margin-top: 2px;">');
				htmlArr.push('<label><img lang="'+key+'"  src="'+DEFAULT_IMAGE_URL+'" class="color-select-img"/>');
				htmlArr.push('<a class="func-button ml5 uploadlink toggleBtn" href ="javascript:void(0);"><span>显示URL</span></a>');
				htmlArr.push('</label>');
				
				//包裹2
				htmlArr.push('<div style="float: left;margin-left: 15px;">');
				//选择文件
				htmlArr.push('<div class="color-select-line">');
				htmlArr.push('<input type="hidden" readonly="true" lang="'+key+'"  class="picUrl-mutl" placeholder="'+nps.i18n('IMAGE_PATH')+'" id="itemImageUrl" name="itemImages.picUrl.values['+len+'-'+j+']"/>');
				htmlArr.push('<input type="hidden" class="picUrl-mutl-lang" name="itemImages.picUrl.langs['+len+'-'+j+']" value="'+key+'"/>');
				htmlArr.push('<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span>'+nps.i18n('IMAGE_BROWSE')+'</span>');
				htmlArr.push('<input style="left:2px;" callback="colorComplete" class="imgUploadComponet fileupload" role="'+imgRoleValue+
						'" model="C" hName="itemImage'+len+'-'+key+'" hValue="'+DEFAULT_IMAGE_URL+'" type="file" url="/demo/upload.json"/>&nbsp;'+val+'</a>');
				//if(!isImageTypeGroup){
					if(key==defaultlang){
						htmlArr.push('<span class="common-ic up-ic" style=" margin-left: 140px;"></span>');
					}
				//}
				//htmlArr.push('<input type="hidden" name="itemImage'+j+'"  value="'+DEFAULT_IMAGE_URL+'"/>');
				htmlArr.push('</div>');
				//位置
				/*if(isImageTypeGroup && key==defaultlang){
					htmlArr.push('<div class="color-select-line">');
					htmlArr.push('<span style="font: inherit;font-size: 13px; line-height: 25px;">'+nps.i18n("ITEM.IMAGE.POSITION")+':</span>');
					htmlArr.push('<input loxiaType="number" name="itemImages.position" placeholder="'+nps.i18n("ITEM.IMAGE.POSITION")+
							'" mandatory="true" style="width:160px;float: right;margin-right: 120px;" value=""/>');
					htmlArr.push('</div>');
				}*/
				//描述
				htmlArr.push('<div class="color-select-line">');
				htmlArr.push('<label style="font: inherit;font-size: 13px;">'+nps.i18n('ITEM_IMAGE_DESCRIPTION')+':</label>');
				htmlArr.push('<input loxiaType="input"  class="description-mutl" name="itemImages.description.values['+len+'-'+j+']" placeholder="'+nps.i18n('ITEM_IMAGE_DESCRIPTION')+'" style="width: 156px;margin-left: -59px;"/>');
				htmlArr.push('<input type="hidden" class="description-mutl-lang" name="itemImages.description.langs['+len+'-'+j+']" value="'+key+'"/>');
				//if(!isImageTypeGroup){
					if(key==defaultlang){
						htmlArr.push('<span class="common-ic down-ic"></span>');
					}
				//}
				htmlArr.push('</div>');
				
				//包裹2 end
				htmlArr.push('</div>');
				
				htmlArr.push('<div class="urlTab" style="float: left;margin-left: 15px;display:none;">');
				htmlArr.push('</div>');
				
				
				//包裹1 end
				htmlArr.push('</div>');
				
			}
			htmlArr.push('</div>');
			htmlArr.push('</div>');
		}else{
			htmlArr.push('<div class="ui-block-line color-select-line color-select-line-add" style="border-top: 1px solid gray;margin-top: 2px;">');
			htmlArr.push('<label><img src="'+DEFAULT_IMAGE_URL+'" class="color-select-img"/>');
			htmlArr.push('<a class="func-button ml5 uploadlink toggleBtn" href ="javascript:void(0);"><span>显示URL</span></a>');
			htmlArr.push('</label>');
			htmlArr.push('<input type="hidden" loxiaType="input" name="itemImages.id" value="" />');
			htmlArr.push('<input type="hidden" loxiaType="input" name="itemImages.itemProperties" value="'+itemProperties+'" />');
			htmlArr.push('<div class="imgTag" >');
			htmlArr.push('<div class="color-select-line">');
			htmlArr.push('<span style="font: inherit;font-size: 13px;">'+nps.i18n('IMAGE_TYPE')+':</span>');
			if(isImageTypeGroup){
				htmlArr.push(itemImageTypeHtml.replace('selected', '').replace('"'+type+'"', '"'+type+'"'+'selected').replace('=""', ''));
			}else{
				htmlArr.push(itemImageTypeHtml);
			}
			htmlArr.push('<span class="common-ic delete-ic"></span>');
			htmlArr.push('</div>');
			htmlArr.push('<div class="color-select-line itemImage'+hnameIndex+'">');
			htmlArr.push('<input type="hidden" class="picUrl-mutl" readonly="true" placeholder="'+nps.i18n('IMAGE_PATH')+'" id="itemImageUrl" name="itemImages.picUrl.value['+len+']"/>');
			htmlArr.push('<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span>'+nps.i18n('IMAGE_BROWSE')+'</span>');
			htmlArr.push('<input style="left:2px;" callback="colorComplete" class="imgUploadComponet fileupload" role="'+imgRoleValue+'" model="C" hName="itemImage'+hnameIndex+'" hValue="'+DEFAULT_IMAGE_URL+'" type="file" url="/demo/upload.json"/></a>');
			if(!isImageTypeGroup){
				htmlArr.push('<span class="common-ic up-ic" style=" margin-left: 168px;"></span>');
			}
			//htmlArr.push('<input type="hidden" name="itemImage'+hnameIndex+'" value="'+DEFAULT_IMAGE_URL+'"/>');
			htmlArr.push('</div>');
			/*if(isImageTypeGroup){
				htmlArr.push('<div class="color-select-line">');
				htmlArr.push('<span style="font: inherit;font-size: 13px; line-height: 25px;">'+nps.i18n("ITEM.IMAGE.POSITION")+':</span>');
				htmlArr.push('<input loxiaType="number" name="itemImages.position" placeholder="'+nps.i18n("ITEM.IMAGE.POSITION")+'" mandatory="true" style="width:160px;float: right;margin-right: 120px;" value=""/>');
				htmlArr.push('</div>');
			}*/
			htmlArr.push('<div class="color-select-line">');
			htmlArr.push('<label style="font: inherit;font-size: 13px;">'+nps.i18n('ITEM_IMAGE_DESCRIPTION')+':</label>');
			htmlArr.push('<input loxiaType="input" class="description-mutl" name="itemImages.description.value['+len+']" placeholder="'+nps.i18n('ITEM_IMAGE_DESCRIPTION')+'" style="width: 156px;margin-left: -59px;"/>');
			if(!isImageTypeGroup){
				htmlArr.push('<span class="common-ic down-ic"></span>');
			}
			htmlArr.push('</div>');
			
			htmlArr.push('</div>');
			htmlArr.push('<div class="urlTab" style="float: left;margin-left: 15px;display:none;">');
			htmlArr.push('</div>');
		}
		
		$colorPropertyContent.append(htmlArr.join(""));
		hnameIndex++;
		//加载上传组件
		$j.getScript(base+'/scripts/ajaxfileupload.js');
		//初始化id为colorPropertyContent的div内部的所有Loxia组件
		loxia.initContext($j($colorPropertyContent));
		
		if(isImageTypeGroup){
			$colorPropertyContent.parent('.ui-block-content').find('.noImage').remove();
		}else{
			$colorPropertyContent.find('.noImage').remove();
		}
		//重新计算下标
		resetseq();
		if(isImageTypeGroupFlag=="true"){
			$j(".isImageTypeGroupFlag").attr("disabled","disabled");
		}
	});
	
	//-------更改属性值时, 显示对应的图片信息
	$j(".ui-block-line").on("change", "#colorProperty", function(){
		changePropertyValue();
	});
	
    //-------返回
	$j(".button.return").on("click",function(){
		window.location.href = base + '/item/itemList.htm';
	});
	
	//-------保存商品图片
	$j(".button.orange.submit").click(function(){
		var isImage = isUploadImage();
		if(isImage){
			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("UPLOAD_IMAGE"));
			return;
		}
		$j(".isImageTypeGroupFlag").removeAttr("disabled");
	    nps.submitForm('itemForm',{mode: 'async', 
			successHandler : function(data){
			if(data.isSuccess){
				window.location.reload();
				return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("UPDATEITEM_SUCCESS"));
			}else{
				return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("UPDATEITEM_FAIL"));
			}
	    }});
	});
	
	// 更改 "图片类型" 
	$j('.ui-block-content').on("change", "#imageType", function(){
		var obj = $j(this);
		var imgTypeValue = obj.val();
		var type = obj.val();
		var contentObj = obj.parents('div[id^="itemProeprtiesIs"]').find('.'+type).next('.ui-block-content');
		if(isImageTypeGroup){
			var afterRoleValue = '';
	    	$j.each(thumbConfigList, function(i, config){
	    		if(imgTypeValue == config.optionLabel){
	    			afterRoleValue = config.optionValue;
	    		}
	    	});
	    	
	    	var beforeRoleValue = obj.parent('div[class="color-select-line"]').next('.color-select-line').find('.imgUploadComponet').attr('role');
	    	var imgUrl = obj.parents('.ui-block-line').find('img').attr('src');
	    	var relativeImgUrl = obj.parent('div[class="color-select-line"]').next('.color-select-line').children('input').val();
	    	// 更换位置 
	    	var changedTypeHtml = '<div class="ui-block-line color-select-line">'+obj.parents('.ui-block-line').html()+'</div>';
	    	// 修改图片类型
	    	changedTypeHtml = changedTypeHtml.replace('selected', '').replace('"'+imgTypeValue+'"', '"'+imgTypeValue+'"'+'selected').replace('=""', '');
	    	// 修改图片可以生成缩略图的尺寸
	    	changedTypeHtml = changedTypeHtml.replace(beforeRoleValue, afterRoleValue);
	    	// 清空图片内容
	    	changedTypeHtml = changedTypeHtml.replace(imgUrl, DEFAULT_IMAGE_URL);
	    	changedTypeHtml = changedTypeHtml.replace(relativeImgUrl, '');
	    	
	    	contentObj.children('div[id^="'+type+'"]').append(changedTypeHtml);
	    	obj.parents('.ui-block-line').remove();
		}else{
			// 修改图片可以生成缩略图的尺寸
	    	$j.each(thumbConfigList, function(i, config){
	    		var imgRoleLabel = config.optionLabel;
	    		if(imgTypeValue == imgRoleLabel){
	    			var imgRoleValue = config.optionValue;
	    			obj.parent().parent().parent(".color-select-line").find(".imgUploadComponet").attr("role", imgRoleValue);
	    		}
	    	});
	    	// 清空图片内容
			$j(this).parent('.color-select-line').next('.color-select-line').find('input[name="itemImages.picUrl"]').val("");
			$j(this).parent('.color-select-line').parents('.color-select-line').find('label').find('img').attr('src', DEFAULT_IMAGE_URL);
		}
		contentObj.find('.noImage').remove();
    	//加载上传组件
    	$j.getScript(base+'/scripts/ajaxfileupload.js');
	});
	
	
	// 删除item全部商品图片
	$j('.batchDelete').click(function(){
		var titleArr = $j('#colorPropertyContent').children('div[id^="itemProeprtiesIs"]').find('.ui-block-title1');
		var noImageArr = $j('#colorPropertyContent').children('div[id^="itemProeprtiesIs"]').find('.ui-block-content').find('.noImage');
		if(noImageArr.length >= titleArr.length){
			nps.info(nps.i18n('SYSTEM_ITEM_MESSAGE'), nps.i18n('ITEM_NO_IMAGE', [$j('#itemName').val()]));
			return;
		}
		
		nps.confirm(nps.i18n('SYSTEM_ITEM_MESSAGE'), nps.i18n('REMOVE_ALL_ITEMIMAGE', [$j('#itemName').val()]), function(){
			var itemId = $j('#itemId').val();
			var json = {'itemId': itemId};
			var resultData = loxia.syncXhrPost(batchRemoveItemImageByItemIdUrl, json, null);
			if(resultData.isSuccess){
				window.location.reload();
			}
		});
	});

	
	
	// 删除item全部商品图片
	$j('.batchDelete').click(function(){
		var noImage = $j('#colorPropertyContent').children('.noImage');
		if(noImage.length > 0){
			nps.info(nps.i18n('SYSTEM_ITEM_MESSAGE'), nps.i18n('ITEM_NO_IMAGE', [$j('#itemName').val()]));
			return;
		}
		
		nps.confirm(nps.i18n('SYSTEM_ITEM_MESSAGE'), nps.i18n('REMOVE_ALL_ITEMIMAGE', [$j('#itemName').val()]), function(){
			var itemId = $j('#itemId').val();
			var json = {'itemId': itemId};
			var resultData = loxia.syncXhrPost(batchRemoveItemImageByItemIdUrl, json, null);
			if(resultData.isSuccess){
				window.location.reload();
			}
		});
	});
	if(isImageTypeGroupFlag=="true"){
		//imageType
		$j(".isImageTypeGroupFlag").attr("disabled","disabled");
	}else{
		$j(".isImageTypeGroupFlag").removeAttr("disabled");
	}
	
	// 显示或隐藏URL Table
	$j(".ui-block").on('click','.toggleBtn',function(){
		var tabObj = null;
		if(i18nOnOff){
			tabObj =$j(this).parents(".itemImage-add").find(".urlTab");
		}else{
			tabObj =$j(this).parents(".ui-block-line").find(".urlTab");
		}
		tabObj.toggle();
		if(tabObj.css("display")=='block'){
			$j(this).find('span').html("隐藏URL");
		}else{
			$j(this).find('span').html("显示URL");
		}
	});
	// 显示或隐藏URL Table
	/*$j(".ui-block").on('click','.toggleBtn',function(){
		var tabObj =$j(this).parents(".ui-block-line").find(".urlTab");
		tabObj.toggle();
		if(tabObj.css("display")=='block'){
			$j(this).find('span').html("隐藏URL");
		}else{
			$j(this).find('span').html("显示URL");
		}
	});*/
	
	
});