$j.extend(loxia.regional['zh-CN'],{
	"PROMPT_INFO":"提示信息",
	"EDIT_AREA_CODE_NOT_EXISTS":"该区域的code不存在",
	"EDIT_AREA_DATA_NOT_EMPTY":"页面编辑区域的数据不可以为空",
	"NO_SAVE_NOT_PREVIEW":"数据有改动,请先保存,再浏览",
	"repeat_data":"同一个编辑或列表区域只能设置一种编辑项",
	"set_s":"设置成功"
	
});

var prd_cls =["cms-show-product-title","cms-show-product-salesprice",
              "cms-show-product-listprice","cms-show-product-desc",
              "cms-show-product-img","cms-show-product-href","cms-show-product-text-href"];
var p_title="cms-show-product-title";
var p_salesprice="cms-show-product-salesprice";
var p_listprice="cms-show-product-listprice";
var p_desc="cms-show-product-desc";
var p_img="cms-show-product-img";
var p_img_h="cms-show-product-href";
var p_text_h="cms-show-product-text-href";
var p_title_c=".cms-show-product-title";
var p_salesprice_c=".cms-show-product-salesprice";
var p_listprice_c=".cms-show-product-listprice";
var p_desc_c=".cms-show-product-desc";
var p_img_c=".cms-show-product-img";
var p_img_h_c=".cms-show-product-href";
var p_text_h_c=".cms-show-product-text-href";
/**
 * 设置当前编辑对象可以编辑的选项
 * 
 * @param obj
 * @param dialog
 */
function setItemEidt(obj,dialog){
	var p = obj.parent();
	if(obj.parents(".cms-area-list-element").length>0){
		dialog.find(".product-setSame").show();
	}else{
		dialog.find(".product-setSame").hide();
	}
	// 显示或隐藏正确的设置项
	if(obj.get(0).tagName=="IMG"){
		dialog.find(".product-title").hide();
		dialog.find(".re-product-title").hide();
		dialog.find(".product-salesprice").hide();
		dialog.find(".re-product-salesprice").hide();
		dialog.find(".product-listprice").hide();
		dialog.find(".re-product-listprice").hide();
		dialog.find(".product-desc").hide();
		dialog.find(".re-product-desc").hide();
		dialog.find(".product-text-href").hide();
		dialog.find(".re-product-text-href").hide();
		dialog.find(".img").show();
		if(obj.hasClass(p_img) || obj.find(p_img_c).length>0){
			dialog.find(".product-img").hide();
			dialog.find(".re-product-img").show();
		}else{
			dialog.find(".product-img").show();
			dialog.find(".re-product-img").hide();
		}
		if(p.get(0).tagName=="A"){
			if(p.hasClass(p_img_h) || p.find(p_img_h_c).length>0 ||p.hasClass(p_img_h_c )){
				dialog.find(".product-href").hide();
				dialog.find(".re-product-href").show();
			}else{
				dialog.find(".product-href").show();
				dialog.find(".re-product-href").hide();
			}
		}else{
			dialog.find(".product-href").hide();
			dialog.find(".re-product-href").hide();
		}
		
	}else if(obj.get(0).tagName=="A"){
		
		if(obj.hasClass(p_title) || obj.find(p_title_c).length>0){
			dialog.find(".product-title").hide();
			dialog.find(".re-product-title").show();
		}else{
			dialog.find(".product-title").show();
			dialog.find(".re-product-title").hide();
		}
		
		if(obj.hasClass(p_salesprice) || obj.find(p_salesprice_c).length>0 ){
			dialog.find(".product-salesprice").hide();
			dialog.find(".re-product-salesprice").show();
		}else{
			dialog.find(".product-salesprice").show();
			dialog.find(".re-product-salesprice").hide();
		}
		
		if(obj.hasClass(p_listprice) || obj.find(p_listprice_c).length>0){
			dialog.find(".product-listprice").hide();
			dialog.find(".re-product-listprice").show();
		}else{
			dialog.find(".product-listprice").show();
			dialog.find(".re-product-listprice").hide();
		}
		
		if(obj.hasClass(p_text_h) || obj.find(p_text_h_c).length>0){
			dialog.find(".product-text-href").hide();
			dialog.find(".re-product-text-href").show();
		}else{
			dialog.find(".product-text-href").show();
			dialog.find(".re-product-text-href").hide();
		}
		
		
		if(obj.hasClass(p_desc) || obj.find(p_desc_c).length>0 ){
			dialog.find(".product-desc").hide();
			dialog.find(".re-product-desc").show();
		}else{
			dialog.find(".product-desc").show();
			dialog.find(".re-product-desc").hide();
		}
		
		dialog.find(".product-img").hide();
		dialog.find(".re-product-img").hide();
		dialog.find(".product-href").hide();
		dialog.find(".re-product-href").hide();
		dialog.find(".img").hide();
		
	}else{
		if(obj.hasClass(p_title) || obj.find(p_title_c).length>0){
			dialog.find(".product-title").hide();
			dialog.find(".re-product-title").show();
		}else{
			dialog.find(".product-title").show();
			dialog.find(".re-product-title").hide();
		}
		
		if(obj.hasClass(p_salesprice) || obj.find(p_salesprice_c).length>0 ){
			dialog.find(".product-salesprice").hide();
			dialog.find(".re-product-salesprice").show();
		}else{
			dialog.find(".product-salesprice").show();
			dialog.find(".re-product-salesprice").hide();
		}
		
		if(obj.hasClass(p_listprice) || obj.find(p_listprice_c).length>0){
			dialog.find(".product-listprice").hide();
			dialog.find(".re-product-listprice").show();
		}else{
			dialog.find(".product-listprice").show();
			dialog.find(".re-product-listprice").hide();
		}
		
		if(obj.hasClass(p_desc) || obj.find(p_desc_c).length>0 ){
			dialog.find(".product-desc").hide();
			dialog.find(".re-product-desc").show();
		}else{
			dialog.find(".product-desc").show();
			dialog.find(".re-product-desc").hide();
		}
		dialog.find(".product-img").hide();
		dialog.find(".re-product-img").hide();
		dialog.find(".product-href").hide();
		dialog.find(".re-product-href").hide();
		dialog.find(".product-text-href").hide();
		dialog.find(".re-product-text-href").hide();
		dialog.find(".img").hide();
	}
};
/**
 * 检查当前编辑对象是否有商品模式class
 */

function checkCls(type,cls){
	var modeObj = editObj.parents(".cms-area-list-element");
	if(modeObj.length == 0){
		modeObj = editObj.parents(".cms-product-edit");
	}
	if(modeObj.find("."+cls).length > 0){
		return true;
	}
	for ( var i = 0; i < prd_cls.length; i++) {
		if(type=="imgh"){
			if(prd_cls[i]=="cms-show-product-img"){
				continue;
			}
			if(editObj.hasClass(prd_cls[i])){
				return true;
			}
		}else if(type=="a"){
			if(prd_cls[i]==p_text_h){
				if(editObj.hasClass(p_text_h) ){
					return true;
				}
			}
		}else if(type=="img"){
			if(prd_cls[i]=="cms-show-product-href"){
				continue;
			}
			if(editObj.hasClass(prd_cls[i]) ){
				return true;
			}
		}else{
			if(prd_cls[i]==p_text_h){
				continue;
			}
			if(editObj.hasClass(prd_cls[i])){
				return true;
			}
		}
	}
	return false;
};
function addeditMode(addcls,me,cls,type){
	var p = me.parent();
	if(type=="add"){
		if(addcls=="cms-show-product-img"){
			productSameSet(addcls,"img",type);
		}else if(addcls=="cms-show-product-href"){
			productSameSet(addcls,"imgh",type);
		}else{
			productSameSet(addcls,"comm",type);
		}
		editObj.addClass(addcls);
		nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("set_s"));
	}else{
		if(addcls=="cms-show-product-img"){
			productSameSet(addcls,"img",type);
		}else if(addcls=="cms-show-product-href"){
			productSameSet(addcls,"imgh",type);
		}else{
			productSameSet(addcls,"comm",type);
		}
		editObj.find("."+addcls).removeClass(addcls);
		editObj.removeClass(addcls);
		nps.info(nps.i18n("INFO_TITLE_DATA"), "去除成功");
	}
	p.parent().find(".html").val(editObj.prop("outerHTML"));
	p.find(cls).show();
	me.hide();
	
}

function updateImgType(type){
	if(type=='add'){
		$j(".cms-product-edit-dialog").find(".img-type").trigger("change");
	}else{
		var modeObj = editObj.parents(".cms-product-edit");
		if(modeObj.find(".cms-area-list-element").length == 0){
			modeObj.children().removeAttr("img-type");
		}else{
			modeObj = modeObj.find(".cms-area-list-element");
			modeObj.removeAttr("img-type");
		}
	}
}

/**
 * 删除商品模式相关class
 * @param obj
 */
function delProductCls(obj){
	obj.removeClass("cms-product-edit");
	obj.removeAttr("cms-area-product-code");
	obj.removeAttr("img-type");
	for ( var i = 0; i < prd_cls.length; i++) {
		obj.removeClass(prd_cls[i]);
		var delObj =   obj.find("."+prd_cls[i]);
		delObj.removeClass(prd_cls[i]);
		delObj.removeClass("cms-area-list-element");
		delObj.removeAttr("cms-area-product-code");
		delObj.removeAttr("img-type");
	}
};
function findEditChildren(obj){
	var p = obj.parent();
	if(p.hasClass("cms-product-edit") || p.hasClass("cms-area-list-element")){
		return  obj;
	}else{
		return findEditChildren(p);
	}
	
}
function findLocation(obj){
	var ch =  findEditChildren(obj);
	ch.attr("location","location");
	var mode = obj.parents(".cms-area-list-element");
	if(mode.length == 0){
		obj.parents(".cms-product-edit");
	}
	for ( var i = 0; i < mode.children().length; i++) {
		var ch = mode.children().eq(i);
		if(ch.attr("location")=="location"){
			ch.removeAttr("location");
			return i;
		}
	}
	return 0;
}

function productSameSet(cls,type,oper){
	var checked = $j(".cms-product-edit-dialog .product-isSelect").attr("checked");
	if(typeof(checked)=="undefined" ||  checked==null || checked==""){
		return ;
	}
	var loc = findLocation(editObj);
	var mode = getEditWay(editObj);
	var tagName =  editObj.get(0).tagName;
	var children = mode.children();
	if(children.hasClass("cms-area-list-element")){
		for ( var i = 0; i < children.length; i++) {
			var ch = children.eq(i);
			var obj = ch.children().eq(loc);
			if(oper=="add"){
				if(obj.find(tagName).length>0){
					if(type=="img"){
						if(obj.find(tagName).find("img").length>0){
							obj.find(tagName).find("img").addClass(cls);
						}else{
							obj.find(tagName).addClass(cls);
						}
					}else{
						obj.find(tagName).addClass(cls);
					}
				}else{
					obj.addClass(cls);
				}
			}else{
				if(obj.find(tagName).length>0){
					obj.find("."+cls).removeClass(cls);
				}else{
					obj.removeClass(cls);
				}
			}
		}
	}
	
};
$j(window).load(function(){
	//商品名称
	$j(".cms-product-edit-dialog").on("click",".product-title",function(){
		if(checkCls("comm",p_title)){
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("repeat_data"));
			return;
		}
		var me = $j(this);
		addeditMode(p_title,me,".re-product-title","add");
	});
	$j(".cms-product-edit-dialog").on("click",".product-salesprice",function(){
		if(checkCls("comm")){
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("repeat_data"));
			return;
		}
		var me = $j(this);
		addeditMode(p_salesprice,me,".re-product-salesprice","add");
	});
	$j(".cms-product-edit-dialog").on("click",".product-listprice",function(){
		if(checkCls("comm",p_listprice)){
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("repeat_data"));
			return;
		}
		var me = $j(this);
		addeditMode(p_listprice,me,".re-product-listprice","add");
	});
	$j(".cms-product-edit-dialog").on("click",".product-desc",function(){
		if(checkCls("comm",p_desc)){
			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("repeat_data"));
			return;
		}
		var me = $j(this);
		addeditMode(p_desc,me,".re-product-desc","add");
	});
	$j(".cms-product-edit-dialog").on("click",".product-img",function(){
		if(checkCls("img",p_img)){
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("repeat_data"));
			return;
		}
		var me = $j(this);
		var p = me.parent();
		editObj.children().addClass(p_img);
		//productSameSet(cls,type,oper)
		productSameSet(p_img,"img","add");
		p.parent().find(".html").val(editObj.prop("outerHTML"));
		p.find(".re-product-img").show();
		//p.parent().find(".img").hide();
		me.hide();
		nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("set_s"));
		
		updateImgType("add");
	});
	$j(".cms-product-edit-dialog").find(".img-type").change(function(){
		var type = $j(this).val();
		var modeObj = editObj.parents(".cms-product-edit");
		if(modeObj.find(".cms-area-list-element").length == 0){
			modeObj.children().removeAttr("img-type");
			modeObj.children().attr("img-type",type);
		}else{
			modeObj = modeObj.find(".cms-area-list-element");
			modeObj.removeAttr("img-type");
			modeObj.attr("img-type",type);
		}
	});
	$j(".cms-product-edit-dialog").on("click",".product-href",function(){
		if(checkCls("imgh",p_img_h)){
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("repeat_data"));
			return;
		}
		var me = $j(this);
		addeditMode(p_img_h,me,".re-product-href","add");
		
		updateImgType("add");
		
	});
	$j(".cms-product-edit-dialog").on("click",".product-text-href",function(){
		if(checkCls("a",p_text_h)){
			nps.info(nps.i18n("INFO_TITLE_DATA"), nps.i18n("repeat_data"));
			return;
		}
		var me = $j(this);
		addeditMode(p_text_h,me,".re-product-text-href","add");
	});
	//删除操作
	$j(".cms-product-edit-dialog").on("click",".re-product-title",function(){
		var me = $j(this);
		addeditMode(p_title,me,".product-title","del");
	});
	$j(".cms-product-edit-dialog").on("click",".re-product-salesprice",function(){
		var me = $j(this);
		addeditMode(p_salesprice,me,".product-salesprice","del");
	});
	$j(".cms-product-edit-dialog").on("click",".re-product-listprice",function(){
		var me = $j(this);
		addeditMode(p_listprice,me,".product-listprice","del");
	});
	$j(".cms-product-edit-dialog").on("click",".re-product-desc",function(){
		var me = $j(this);
		addeditMode(p_desc,me,".product-desc","del");
		
	});
	$j(".cms-product-edit-dialog").on("click",".re-product-img",function(){
		var me = $j(this);
		var p = me.parent();
		var modeObj = editObj.parents(".cms-product-edit");
		if(modeObj.find(".cms-area-list-element").length == 0){
			modeObj.children().removeAttr("img-type");
		}else{
			modeObj = modeObj.find(".cms-area-list-element");
			modeObj.removeAttr("img-type");
		}
		editObj.children().removeClass(p_img);
		productSameSet(p_img,"img","del");
		p.parent().find(".html").val(editObj.prop("outerHTML"));
		p.find(".product-img").show();
		//p.parent().find(".img").show();
		me.hide();
		nps.info(nps.i18n("INFO_TITLE_DATA"), "去除成功");
	});
	$j(".cms-product-edit-dialog").on("click",".re-product-href",function(){
		var me = $j(this);
		addeditMode(p_img_h,me,".product-href","del");
	});

	$j(".cms-product-edit-dialog").on("click",".re-product-text-href",function(){
		var me = $j(this);
		addeditMode(p_text_h,me,".product-text-href","del");
	});
});


