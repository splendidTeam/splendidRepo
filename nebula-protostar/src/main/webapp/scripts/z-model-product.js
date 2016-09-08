$j.extend(loxia.regional['zh-CN'],{
	"PROMPT_INFO":"提示信息",
	"EDIT_AREA_CODE_NOT_EXISTS":"该区域的code不存在",
	"EDIT_AREA_DATA_NOT_EMPTY":"页面编辑区域的数据不可以为空",
	"NO_DATA":"无数据",
	"INFO_TITLE_DATA":"提示信息",
	 "LABEL_ITEM_CODE":"商品编码",
	 "LABEL_ITEM_CATEGORY":"所属分类",
	 "LABEL_ITEM_TITLE":"商品名称",
	 "LABEL_ITEM_SALE_PRICE":"标价",
	 "LABEL_ITEM_LIST_PRICE":"贴牌价",
	 "NO_CATEGORY":"无"
	
});
function drawCheckbox(data, args, idx){
	return "<input name='chedkId' type='radio' class='checkId'  value='" + loxia.getObject("code", data)+"'/>";
}
function refreshData(){
	$j("#table1").loxiasimpletable("refresh");
}
function formatCategoryNames(data, args, idx){
	var propertyNameArray =loxia.getObject("categoryNames", data);
	
	var categoryNames =propertyNameArray;
	
	if(propertyNameArray==null||propertyNameArray==''){
		categoryNames= nps.i18n("NO_CATEGORY");
	}
	var hiddenCategoryNameInput ="<input type='hidden' id='itemCategoryName_"+loxia.getObject("id", data)+"' value='"+propertyNameArray+"' />";
	categoryNames +=hiddenCategoryNameInput;
	
	return categoryNames;
}
function getTitle(data, args, idx){
	var title =loxia.getObject("title", data);
	var name =title;
	
	if(title==null||title==''){
		name= nps.i18n("NO_CATEGORY");
	}	
	var hiddenNameInput ="<input type='hidden' id='title_"+loxia.getObject("id", data)+"' value='"+title+"' />";
	name +=hiddenNameInput;
	return name;
}
function getCode(data, args, idx){
	var code =loxia.getObject("code", data);
	var hiddenNameInput ="<input type='hidden' id='code_"+loxia.getObject("id", data)+"' value='"+code+"' />";
	code +=hiddenNameInput;
	return code;
}
function getSalePrice(data, args, idx){
	var title =loxia.getObject("salePrice", data);
	
	var name =title;
	
	if(title==null||title==''){
		name= nps.i18n("NO_CATEGORY");
	}	
	var hiddenNameInput ="<input type='hidden' id='salePrice_"+loxia.getObject("id", data)+"' value='"+title+"' />";
	name +=hiddenNameInput;
	return name;
};
function removeItemTem(num){
	for ( var i = 0; i < arealistItems.length; i++) {
		 var tem  = arealistItems[i];
		 if(num == tem.num){
			 tem.obj.remove();
		 }
	}
}
function productList(dialog,list,num,type){
	
	var areaList = dialog.find(".li-area-list").clone(true).find(".area-list")
	.removeClass("area-list").addClass("area-list-add").show().parent();
	areaList.removeClass("li-area-list").addClass("li-area-list-add");
	areaList.attr("oldIndex",list.attr("oldIndex"));
	
	areaList.find(".item-remove").attr("num",num);
	var tem ={"num":num,"obj":list};
	arealistItems.push(tem);
	var productCode;
	if(num==0){
		areaList.find(".zkss a").html("收起-"+(num+1));
		areaList.find(".zkss").show();
		areaList.find(".slideToggle").show();
	}else{
		areaList.find(".zkss").show();
		areaList.find(".zkss a").html("展开-"+(num+1));
		areaList.find(".slideToggle").hide();
	}
	if(type == "list"){
		 productCode = list.attr('cms-area-product-code');
	}else{
		 productCode = list.children().attr('cms-area-product-code');
	}
	areaList.find('.productCode').val(productCode);
	//追加到页面中显示
	dialog.find("#item-element-list").append(areaList);
};
function getItemEditArea(editArea){
	var json={"editArea":editArea.prop("outerHTML")};
	var data = loxia.syncXhr(base+"/cms/getItemEditArea.json", json, {type:'post'});
	if (data.isSuccess) {
		editArea.empty();
		editArea.html(data.description);
		return true;
	} else {
		nps.info(nps.i18n("INFO_TITLE_DATA"), "确定失败");
		return false;
	}
	
};
function validateInputItem(cls){
	var result = true;
	if(cls==null || cls==""){
		return result; 
	}
	var list = $j(".cms-product-edit-dialog").find(".area-list-add").find("."+cls);
	for ( var i = 0; i < list.length; i++) {
		var me = list.eq(i);
		var val = list.eq(i).val();
		if(val!=null && val!="" && me.hasClass("ui-loxia-error")){
			me.removeClass("ui-loxia-error");
		}
		if(me.hasClass("ui-loxia-error")){
			result = false;
			me.parents(".slideToggle").show();
		}
		if(val==null || val==""){
			me.addClass("ui-loxia-error");
			result = false;
			me.parents(".slideToggle").show();
		}
	}
	return result; 
};
//已分类商品列表url
var itemCtListUrl = base + '/product/scope/itemSelectNoShopid.json';
var selectObj;
var arealistItems=[];
$j(window).load(function(){
	//商品列表
	$j("#table1").loxiasimpletable({
		page : true,
		size : 10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols : [ {
			label : "<input type='checkbox'  />",
			width : "3%",
			template:"drawCheckbox"
		},{
			name : "code",
			label : nps.i18n("LABEL_ITEM_CODE"),
			width : "10%",
			template:"getCode"
		},
		{
			name : "title",
			label : nps.i18n("LABEL_ITEM_TITLE"),
			width : "10%",
			template:"getTitle",
		},  {
			name : "categoryNames",
			label : nps.i18n("LABEL_ITEM_CATEGORY"),
			width : "20%",
			template : "formatCategoryNames"
		},
		{
			name : "salePrice",
			label : nps.i18n("LABEL_ITEM_SALE_PRICE"),
			width : "10%",
			template:"getSalePrice",
		}, {
			name : "listPrice",
			label : nps.i18n("LABEL_ITEM_LIST_PRICE"),
			width : "12%",
		} ],
		dataurl : itemCtListUrl
	});
	refreshData();
	//筛选
	$j(".func-button.search").click(function(){
		 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	});
	
	$j(".web-update").contents().find(".cms-product-edit").on('click', '.wui-tips', function(e){
		e.stopPropagation();
		var cmsHtmlEditObj = $j(this).parent('.cms-product-edit');
		var areaCode = cmsHtmlEditObj.attr('code');
		picTextObj = cmsHtmlEditObj;
		var dialog = $j(".cms-product-edit-dialog");
		dialog.find(".li-area-list-add").remove();
		dialog.find('.areaCode').val(areaCode);
		arealistItems = [];
		var hide  = cmsHtmlEditObj.attr("hide");
		 if(hide=="1"|| typeof(hide)=="undefined"){
			 dialog.find(".show-area").hide();
			 dialog.find(".hide-area").show();
		 }else{
			 dialog.find(".show-area").show();
			 dialog.find(".hide-area").hide();
		 }
		if(cmsHtmlEditObj.find(".cms-area-list-element").length>0){
			var list = cmsHtmlEditObj.find(".cms-area-list-element");
			dialog.find(".list-line").css("border","1px solid #27A2C5");
			for ( var i = 0; i < list.length; i++) {
				productList(dialog,list.eq(i),i,"list");
			}
			$j('.cms-product-edit-dialog .add-item-tmp').show();
			dialog.dialogff({type:'open',close:'in',width:'600px', height:'500px'});
		}else{
//			var productCode = cmsHtmlEditObj.attr('cms-area-product-code');
//			var type = cmsHtmlEditObj.attr("img-type");
//			dialog.find('.productCode').val(productCode);
//			dialog.find(".img-type option[value='"+type+"']").attr("selected",true);
			$j('.cms-product-edit-dialog .add-item-tmp').hide();
			productList(dialog,cmsHtmlEditObj,0,"nolist");
			dialog.find(".zkss").hide();
			dialog.find(".list-line").css("border","");
			dialog.dialogff({type:'open',close:'in',width:'600px', height:'300px'});
			
		}
		
	});
	//选择按钮
	$j(".cms-product-edit-dialog").on("click",".product-select",function(){
		var me = $j(this);
		selectObj = me.parent();
		$j("#dialog-item-select").dialogff({type:'open',close:'in',width:'1000px',height:'600px'});
	});
	//商品选择确定按钮
	$j("#dialog-item-select").on("click",".checkId",function(){
		var productCode = null;
		$j("#dialog-item-select").find(".checkId:checked").each(function(i,n){
			productCode = $j(this).val();
		});
		if(productCode == null || productCode==""){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "请选择商品");
			return;
		}
		selectObj.find('.productCode').val(productCode);
		selectObj.find('.productCode').removeClass("ui-loxia-error");
		$j("#dialog-item-select").dialogff({type:'close'});
	});
	
	$j("#dialog-item-select").on("click","tr",function(){
		var me = $j(this);
		var productCode = me.find("td").eq(0).find(".checkId").val();
		if(productCode == null || productCode==""){
			nps.info(nps.i18n("INFO_TITLE_DATA"), "请选择商品");
			return;
		}
		selectObj.find('.productCode').val(productCode);
		selectObj.find('.productCode').removeClass("ui-loxia-error");
		$j("#dialog-item-select").dialogff({type:'close'});
	});
	
	//商品编辑确定按钮
	$j(".cms-product-edit-dialog").on("click",".product-confrim",function(){
		if(validateInputItem("ismandatory")==false){
			return ;
		}
		var dialog = $j(this).parents(".cms-product-edit-dialog");
		var datas = dialog.find(".area-list-add");
		var codes = [];
		for ( var i = 0; i < datas.length; i++) {
			var obj = datas.eq(i);
			var code = obj.find(".productCode").val();
			codes.push(code);
		}
		var list = picTextObj.find(".cms-area-list-element");
		if(list.length>0){
			for ( var i = 0; i < list.length; i++) {
				var me = list.eq(i);
				me.attr("cms-area-product-code",codes[i]);
			}
		}else{
			picTextObj.children().attr("cms-area-product-code",codes[0]);
		}
		var code = dialog.find(".areaCode").val();
		var editArea = $j(".web-update").contents().find('[code="'+code+'"]');
		var result = getItemEditArea(editArea);
		if(result){
			changeImageSort(".cms-product-edit-dialog");
			dialog.dialogff({type:'close'});
			nps.info(nps.i18n("INFO_TITLE_DATA"), "确定成功");
			$j("#isSaved").val(false);
		}else{
			nps.info(nps.i18n("INFO_TITLE_DATA"), "确定失败");
		}
	
	});
	
	
	$j('.cms-product-edit-dialog .add-item-tmp').click(function(){
		var dialog = $j(".cms-product-edit-dialog");
		//添加图文模式
		var areaList = dialog.find(".li-area-list").clone(true).find(".area-list")
		.removeClass("area-list").addClass("area-list-add").show().parent();
		areaList.removeClass("li-area-list").addClass("li-area-list-add");
		
		//将图文模式编辑框数据设置空
		areaList.find(".slideToggle").children().each(function(i,dom){
			$j(dom).show();
			$j(dom).find("input").val(null);
		});
		//新增模板
		areaList.find(".select").html("收起-新增模板");
		areaList.find(".item-remove").val("删除");
		areaList.find(".zkss").show();
		//获取编辑的图文对象 并克隆一下新的对象添加到列表中
		var cloneObj = picTextObj.find(".cms-area-list-element :last").clone(true);
		//添加到列表中
		var num = arealistItems.length;
		var tem ={"num":num,"obj":cloneObj};
		areaList.find(".item-remove").attr("num",num);
		arealistItems.push(tem);
		//提供删除时 功能
		picTextObj.find(".cms-area-list-element :last").after(cloneObj);
		dialog.find("#item-element-list").append(areaList);
		dialog.find(".proto-dialog-content").scrollTop(80*num);
		//重新设置图文模式编辑事件
		var areaCode = dialog.find('#areaCode').val();
		addEditEvent(areaCode,".cms-product-edit");
		
		var nextIndex = getNextIndex(areaCode);
		
		$j(".web-update").contents().find("[code='"+areaCode+"']").find(".cms-area-list-element").last().attr("oldIndex",nextIndex);
		//给定原始编号直接对应dom图片的oldIndex
		$j(".cms-product-edit-dialog .li-area-list-add").last().attr("oldIndex",nextIndex);
	});
	
	$j('.cms-product-edit-dialog .item-remove').click(function(){
		//删除对应的图文数据
		var me  = $j(this);
		nps.confirm("提示信息","确定要删除?",function(){
			var num = me.attr("num");
			if(num != null && num != ""){
				me.parent().parent().parent().remove();
				removeItemTem(num);
				//重新设置图文模式编辑事件
				var areaCode = $j('.cms-product-edit-dialog #areaCode').val();
				addEditEvent(areaCode,".cms-product-edit");
			}
		});
		
	});
	
});
