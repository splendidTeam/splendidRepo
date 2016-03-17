$j.extend(loxia.regional['zh-CN'],{
	"PROPERT_OPERATOR_TIP":"属性提示信息",
	"ITEM_CODE_NOT_EXISTS":"商品编码不存在",
	"INPUT_NOT_EMPTY":"输入不能为空",
	"LABEL_ITEM_CODE":"商品编码",
	"INFO_TITLE_DATA":"提示信息",
	"SAVE_SUCCESS":"保存成功",
	"SAVE_FAILURE":"保存失败"
   
});
//************************ url ************************
var recommandItemListUrl = base + "/recommand/recommanditemList.json";
//新建
var createRecommandItemUrl = base+"/recommand/createRecommandItem.htm";

var toRecommandItemPageUrl = base + "/system/recommandItemManager.htm"; 
//检查商品code是否存在
var validatorItemCodeUrl = base + "/item/findItemCommandByCode.json";
//分类列表
var categorySetting = {
		treeNodeKey : "id",
        treeNodeParentKey : "parentId",
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},  
		view: {
			dblClickExpand: false,
			showIcon:false,
			fontCss:function(treeId,treeNode){
				if(treeNode.lifecycle==0){
					return {color:"#666"};
				}else{
					return {color:"#000"};
				}
			}
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onClick: categoryonClick,
			onCheck: categoryonCheck
		}
	};

//分类点击函数 获得树结构
function categoryonClick(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("categoryDemo");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

//分类点击函数 获得树结构值
function categoryonCheck(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("categoryDemo"),
	nodes = zTree.getCheckedNodes(true),
	v = "";
	id="";
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
		id+= nodes[i].id + ",";
	}
	if (v.length > 0 ) v = v.substring(0, v.length-1);
	if (id.length > 0 ) id = id.substring(0, id.length-1); 
	var cateObj = $j("#categoryName"); 
	cateObj.attr("value", v);
	$j("#categoryId").attr("value",id);
	categoryHideMenu();
}



//鼠标移动隐藏时间
function categoryHideMenu() {
	$j("#categoryMenuContent").fadeOut("fast");
	$j("body").unbind("mousedown", categoryOnBodyDown);
}
function categoryOnBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "categoryName" || event.target.id == "categoryMenuContent" || $j(event.target).parents("#categoryMenuContent").length>0)) {
		categoryHideMenu();
	}
}

//单击时间
function onClick(event, treeId, treeNode) {
	
	var tempTreeId="";
	var tempTreeName="";
	
	if(treeNode.isParent||treeNode.id ==0){
		nps.info(nps.i18n("ITEM_CATEGORY_TIPS"),nps.i18n("ITEM_CATEGORY_CHOOSE_LEAF"));	
	}else{
		tempTreeId=treeNode.id;
		tempTreeName=treeNode.name;
		$j("#categoryId").val(tempTreeId);
			
		$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
		refreshData();

		showItemList(tempTreeName);
	}
}		

$j(document).ready(function() {	
	//分类
	$j.fn.zTree.init($j("#categoryDemo"), categorySetting, category_ZNodes);
		
	var treeObjCategory = $j.fn.zTree.getZTreeObj("categoryDemo");
	var treeCategoryNodes = treeObjCategory.transformToArray(treeObjCategory.getNodes());
	for(var i = 0;i<treeCategoryNodes.length;i++){
		if(treeCategoryNodes[i].isParent){
			treeCategoryNodes[i].nocheck = true;
			treeObjCategory.refresh();
		}
	}
	
	$j("#categoryName").click(function() {
		var cityObj = $j(this);
		var cityOffset = $j(this).offset();
		$j("#categoryMenuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

		$j("body").bind("mousedown", categoryOnBodyDown);
	});



	 
	 /** 推荐类型 */
	 $j('select[name="type"]').on("change", function(){
		 var type = $j(this).val();
		 switch(type){
			 case '1':
				 $j('#rec_param').show().siblings('div[id^="rec"]').hide();
				 break;
			 case '2':
				 $j('#rec_category').show().siblings('div[id^="rec"]').hide();
				 break;
			 case '3':
				 $j('#rec_item').show().siblings('div[id^="rec"]').hide();
				 break;
		 }
	 });
	 
	 /** 返回 */
	 $j('.button.return').click(function(){
		 window.location = toRecommandItemPageUrl;
	 });
	 
	 
	 $j(".itemCode").on("change", function(){
		 var currObj = $j(this);
		 var flag = currObj.attr('flag');
		 var itemCode = currObj.val();
		 var promptId = '';
		 if(flag == "0"){
			 promptId = 'loxiaTip-r';
		 }else{
			 promptId = 'loxiaTip-c';
		 }
		 if(itemCode != ""){
			 var json = {"code": currObj.val()};
			 var data = loxia.syncXhr(validatorItemCodeUrl, json, {type:"POST"});
			 if(data == ""){
				 $j("#"+promptId).show();
				 $j("#"+promptId).find('.codetip').html(nps.i18n("ITEM_CODE_NOT_EXISTS"));
				 setTimeout(function(){ 
					$j("#"+promptId).hide();
				 },4000);
				 return;
			}else{
				$j('input[name="recommandItem.itemId"]').val(data.id);
			}
		}else{
			 $j("#"+promptId).show();
			 $j("#"+promptId).find('.codetip').html(nps.i18n("INPUT_NOT_EMPTY"));
			 setTimeout(function(){ 
				$j("#"+promptId).hide();
			 },4000);
			 return;
		}
	 });
	 
	 $j(".button.orange.submit").click(function(){
		 var itemCode = $j('input[name="itemCode"]').val();
		 var json = {"code": itemCode};
		 var data = loxia.syncXhr(validatorItemCodeUrl, json, {type:"POST"});
		 if(data == ""){
			 nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("ITEM_CODE_NOT_EXISTS"));
			 return;
		 }
	     nps.submitForm('recommandItemForm',{mode: 'async', 
			successHandler : function(data){
			if(data.isSuccess == true){
				nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("SAVE_SUCCESS"));
				window.location.href=window.location.href;
				return;
			}else{
				return nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("SAVE_FAILURE"));
			}
	    }});
	 });
});