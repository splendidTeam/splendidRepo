$j.extend(loxia.regional['zh-CN'],{
	"SYSTEM_ITEM_MESSAGE":"提示信息",
	"SYSTEM_ITEM_SELECT_INDUSTRY":"请选择商品所在行业",
	"ITEM_UPDATE_CODE_ENBLE":"商品编码不可用",
	"ITEM_UPDATE_CODE_ABLE":"商品编码可用",
	"ITEM_CODE_VALID_FAIL":itemCodeValidMsg,
	"INDUSTRY_FIND":" 共找到",
	"PLEASE_SELECT":" 请选择",
	"INDUSTRY_RESULT":"个结果",
	"ADDITEM_FAIL":"新增商品失败",
	"MERCHANT_CODING":"商家编码",
	"MERCHANT_SALEPRICE":"销售价",
	"MERCHANT_LISTPRICE":"吊牌价",
	"CODING_TIPS":"您需要选择所有的属性，才能组合成完整的规格信息!",
	"OTHERS":"其他",
	"MUST_SELECT":"为必选项",
	"SELECT_COLOR_PROPERTY":"请选择颜色属性",
	"WRITE_ALL_INFO":"请将信息填写完整",
	"DELETE_THIS_CATEGORY":"删除此分类",
	"SET_THIS_DEFCATEGORY":"设为默认",
	"THIS_BE_DEFCATEGORY":"默认",
	"PLEASE_SELECT_DEFAULT_CATEGORY":"请为商品选择默认分类",
	"NOT_REPEATEDLY_RELEVANCE_CATEGORY":"同一商品不可以多次关联同一分类",
	"IMAGE_SELECT_COLOR_PROPERTY":"颜色属性",
	"NOT_DATA_FORMAT":"数据格式不正确",
	"SALEPRICE_OUT_OF_RANGE":"销售价超出sku设置的销售价区间",
	"PLEASE_INPUT_SALEPRICES":"请输入sku销售价格",
	"LISTPRICE_OUT_OF_RANGE":"吊牌价超出sku设置的吊牌价区间",
	"PLEASE_INPUT_LISTPRICE":"请输入sku吊牌价格",
	"MERCHANT_CODING_EQUAL":"商家编码相同",
    "PLEASE_SET_CODE":"请进行编码设置",
    "SKU_CODE_REPEAT":"以下商家编码已经存在：",
    "CUSTOM_PROPERTY_SAME":"填写的自定义属性相同",
    "SALES_PROPERTY_CHANGED":"销售属性已经更新，但是没有重新进行编码设置",
    "PLEASE_INPUT_ONE_SKU_CODE":"请输入至少一个sku编码",
    "PLEASE_SET_DEF_CATEGORY":"请设定默认分类",
    "PLEASE_SELECT_PROPERTY_GROUP":"请选择属性分组"
});
var propertyArray  = new Array();
var propertyNameArray  = new Array();
var mustCheckArray  = new Array();
//修改过的颜色属性存在数组中: 格式:{propertyValueId:[itemimage对象...]} 
//如:{"propertyValueId":"716","itemImages":[{"description":"red1","picUrl":"1"},{"description":"red2","picUrl":"2"}]}
var changedColorPropertyArray = new Array();
var itemProperties = 0;
//上传组件中的hname图片Id
var hnameIndex = 1;
//是否存在颜色属性
var isExistColorProp = false;
//url
var findItemImageByPVIdUrl = base + '/item/findItemImageByPVId.json';
var validateItemCodeUrl =base + '/item/validateItemCode.json';
var validateSkuCodesUrl =base +'/item/validateSkuCode.json';
var dynamicPropertyCommandListJsonStr="";
//缩略图大小
//var thumbnailConfig ="";

// 用于提交 表单时候，包含 有序的propertyId 用
var propertyIdArray = new Array();
var propertyIdArrayIndex = 0;

// 属性名称Array  顺序和 上边的 propertyIdArray 对应
var propertyNameArray = new Array();

var clickFlag = false;

var spChangedFlag = false;

//属性数量,全局范围，分组切换使用
var num=0;


var setting = {
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},
		view: {
			dblClickExpand: true,
			showIcon:false,
			fontCss: getFontCss
		},
		data: { 
			simpleData: {
				enable: true
			}  
		},
		callback: {
			onClick: onClick,
			beforeRemove: beforeRemove,
			onRemove: onRemove,
			onCheck: onCheck,
		}
	};

var defaultCategorySetting = {
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},
		view: {
			dblClickExpand: false,
			showIcon:false
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onClick: defaultOnClick1,
			onCheck: defaultOnCheck1,
		}
};
var key,lastValue = "", nodeList = [], fontCss = {};
function onCheck(event, treeId, treeNode){
	
}
function onClick(event, treeId, treeNode){
	var treeObj = $j.fn.zTree.getZTreeObj("industrytreeDemo");
	var nodes = treeObj.transformToArray(treeObj.getNodes());
	for(var i = 0;i<nodes.length;i++){
		if(!nodes[i].isParent){
			nodes[i].checked = false;
			treeObj.refresh();
		}
	}
		treeNode.checked = true;
		treeObj.refresh();
		onCheck(event, treeId, treeNode);
}
function beforeRemove(event, treeId, treeNode){
	
}
function onRemove(event, treeId, treeNode){
	
}

//*******************************默认分类
function defaultOnClick1(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	
	hideMenu("defaultMenuContent");
	return false;
}

function defaultOnCheck1(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree"),
	nodes = zTree.getCheckedNodes(true),
	v = "",
	categoryHtml = "";
	var defaultHtml = $j("#chooseDefaultCategory").html();
	var id = $j('#chooseDefaultCategory').children('div').attr('class');
	$j("#chooseDefaultCategory").html("");
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
		var inode =$j("."+nodes[i].id+"");
		if(inode.length==0){
			categoryHtml = "<div class="+nodes[i].id +">"+nodes[i].name + 
			"<input type='hidden' name='defaultCategoryId'  value='"+nodes[i].id+"' />"+
			"<a href='javascript:void(0);'id="+nodes[i].id+" style='float:right;margin-right: 760px;text-decoration: underline;color:#F8A721' onclick='delDefaultCategroy(this.id)'>"+nps.i18n("DELETE_THIS_CATEGORY")+"</a><br/></div>";
			$j("#chooseDefaultCategory").append(categoryHtml);
		}else{
			$j("#chooseDefaultCategory").html(defaultHtml);
			zTree.checkNode(treeNode, !treeNode.checked, null, false);
			
			var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree"),
			nodes = zTree.getCheckedNodes(false);
			for (var i=0, l=nodes.length; i<l; i++) {
				if(id==nodes[i].id){
					nodes[i].checked= true;
				}
			}
			zTree.refresh();
			
			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("NOT_REPEATEDLY_RELEVANCE_CATEGORY"));
			return;
		}
		
	}
}
function onBodyDownDefault(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "industry" || event.target.id == "defaultMenuContent" || $j(event.target).parents("#defaultMenuContent").length>0)) {
		hideMenu("defaultMenuContent");
	}
}
/**********************industrytreeDemo 展示  end********************/

/**
 * 搜索节点方法
 * 1.搜索到含有相应关键字的节点 
 * 2.展开该段节点 
 * 3.将字置为黄色
 */
function searchNode(e) {
	var zTree = $j.fn.zTree.getZTreeObj("industrytreeDemo");
	var value = $j.trim(key.get(0).value);
	if(value==""){
		$j("#search_result").html("");
		updateNodes(false);
	}
		if (key.hasClass("empty")) {
			value = "";
		}
		if (lastValue === value) return;
		lastValue = value;
		if (value === "") return;
		updateNodes(false);
		
	nodeList = zTree.getNodesByParamFuzzy("name", value);
	
	$j("#search_result").html(nps.i18n("INDUSTRY_FIND")+ nodeList.length+ nps.i18n("INDUSTRY_RESULT"));
	
	if (nodeList.length > 0) {
	$j.each(nodeList, function(i, node){      
 		 zTree.expandNode(node.getParentNode(),true, true, true);
		}); 
	}
	updateNodes(true);
	$j("#key").focus();
}
function focusKey(e) {
	if (key.hasClass("empty")) {
		key.removeClass("empty");
	}
}
function blurKey(e) {
	if (key.get(0).value === "") {
		key.addClass("empty");
	}
}
/**
 * 将搜索到的节点字体置为黄色
 */
function getFontCss(treeId, treeNode) {
	return (!!treeNode.highlight) ? {color:"#333","background-color":"yellow"} : {color:"#333", "font-weight":"normal","background-color":""};		
}
/**
 * 将搜索到的节点展开的方法
 */
function updateNodes(highlight) {
	var zTree = $j.fn.zTree.getZTreeObj("industrytreeDemo");
	for( var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}

/**********************industrytreeDemo 搜索  end********************/

var categorySetting = {
		check: {
			enable: true
		},
		view: {
			dblClickExpand: false,
			showIcon:false
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onClick: onClick1,
			onCheck: onCheck1,
		}
	};

function onClick1(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("treeDemo");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

function onCheck1(e, treeId, treeNode) {
	var zTree = $j.fn.zTree.getZTreeObj("treeDemo"),
	nodes = zTree.getCheckedNodes(true),
	v = "",
	categoryHtml = "";
	var defaultHtml = $j("#chooseCategory").html();
	$j("#chooseCategory").html("");
	var defCategroyId =$j("#defCategroyId").val();
	
	var innerDefHtml ="";
	
	
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
		var inode =$j("."+nodes[i].id+"");
		if(inode.length==0){
			
			if(defCategroyId !='' &&defCategroyId==nodes[i].id){
				innerDefHtml =nps.i18n("THIS_BE_DEFCATEGORY");
			}else{
				innerDefHtml ="<a href='javascript:void(0);'id="+nodes[i].id+" onclick='setCategroyDef(this.id)' style='color:#F8A721;text-decoration: underline;'>"+nps.i18n("SET_THIS_DEFCATEGORY")+"</a>";
			}
			
			categoryHtml = "<div class="+nodes[i].id +">"+nodes[i].name + 
			"<input type='hidden' name='categoriesIds'  value='"+nodes[i].id+"' /><span style='float:right;margin-right: 1000px;'>"+innerDefHtml+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
			"<a href='javascript:void(0);'id="+nodes[i].id+" style='color:#F8A721;text-decoration: underline;' onclick='delCategroy(this.id)'>"+nps.i18n("DELETE_THIS_CATEGORY")+"</a></span><br/></div>";
			$j("#chooseCategory").append(categoryHtml);
		}else{
			$j("#chooseCategory").html(defaultHtml);
			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("NOT_REPEATEDLY_RELEVANCE_CATEGORY"));
			zTree.checkNode(treeNode, !treeNode.checked, null, false);
			return;
		}
		
	}
}
function hideMenu(id) {
	$j("#"+id).fadeOut("fast");
	//$j("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "industry" || event.target.id == "menuContent" || $j(event.target).parents("#menuContent").length>0)) {
		hideMenu('menuContent');
	}
}
//选取其他
function doOther(selOp,proId){
	var len = selOp.length-1;
	var sel =selOp.selectedIndex;
	if(len==sel){
		$j("#pv_"+proId).attr("type","text"); 
	}else{
		$j("#pv_"+proId).attr("type","hidden");  
		$j("#pv_"+proId).val("");  
	}
}

/**********************treeDemo 展示  end********************/
//删除分类
function delCategroy(id){
	$j("."+id+"").remove();
	var zTree = $j.fn.zTree.getZTreeObj("treeDemo"),
	nodes = zTree.getCheckedNodes(true);
	for (var i=0, l=nodes.length; i<l; i++) {
		if(id==nodes[i].id){
			nodes[i].checked= false;
		}
	}
	
	if(id==$j("#defCategroyId").val()){
		$j("#defCategroyId").val('');
	}
	zTree.refresh();
}

//设为默认分类
function setCategroyDef(id){
	$j("#defCategroyId").val(id);
	var zTree = $j.fn.zTree.getZTreeObj("treeDemo"),
	nodes = zTree.getCheckedNodes(true);
	var nodeObj=null;
	for (var i=0, l=nodes.length; i<l; i++) {
		if(id==nodes[i].id){
			nodeObj =nodes[i];
		}
	}
	onCheck1(null,null,nodeObj);
}

//删除默认分类
function delDefaultCategroy(id){
	$j("."+id+"").remove();
	var zTree = $j.fn.zTree.getZTreeObj("defaultCategoryTree"),
	nodes = zTree.getCheckedNodes(true);
	for (var i=0, l=nodes.length; i<l; i++) {
		if(id==nodes[i].id){
			nodes[i].checked= false;
		}
	}
	zTree.refresh();
}

//绘制编码table
list="";
function itTable(array,i,array1,str,properties,propertiesName) {	
	if (array[i+1]!=null) {
		for (var k = 0 ; k < array1[0].length; k++) {
			str += "<td style='width:150px'>"+ array1[0][k] + "</td>";
			//json
			properties+="{'id':'"+array1[1][k]+"','pId':'"+array1[3][k]+"'},";
			propertiesName+="{'id':'"+array1[1][k]+"','pId':'"+array1[3][k]+"','value':'"+array1[0][k]+"','pName':'"+array1[2][k]+"'},";
			if (k > 0) {
				str = str.replace("<td style='width:150px'>"+array1[0][k-1]+"</td>","");
				//json
				properties = properties.replace("{'id':'"+array1[1][k-1]+"','pId':'"+array1[3][k-1]+"'},","");
				
			}
			itTable(array,i+1,array[i+1],str,properties,propertiesName);
		}
	}else {
		for (var j = 0 ; j < array1[0].length; j++) {
			var t1 = document.getElementById("extensionTable");
			var tr1 = t1.insertRow(-1);
			tr1.innerHTML = str+"<td style='width:150px'>"+array1[0][j]+
			"</td><td style='width:150px'><input type='text'  loxiaType='input'  name='codes' value=''/></td>" +
			"<td style='width:150px'><input type='text' loxiaType='number' decimal='2' id='salePrices' name='salePrices' value=''/></td>" +
			"<td style='width:150px'><input type='text'  loxiaType='number' decimal='2' id='listPrices' name='listPrices' value=''/></td>";
			//json
			var p ="'properties':'["+properties+"{'id':'"+array1[1][j]+"','pId':'"+array1[3][j]+"'}]'";
			var pn ="'propertiesName':'["+propertiesName+"{'id':'"+array1[1][j]+"','pId':'"+array1[3][j]+"','value':'"+array1[0][j]+"','pName':'"+array1[2][j]+"'}]'";
			var list1 ="{'itemId':'',"+p+","+pn+",'outid':''},";
			list+=list1;
			
		}
	}
}
//编码
function produceExtension(){
	$j("#exten").css("display","block");
	
	if(propertyArray.length>0){ //如果有 销售属性
		//用于保存所选择的的属性可选值
		var propertyValueArray=new Array();  
		//用于保存属性
		var propertyNamesArray = new Array();
		$j.each(propertyArray,function(i,val){
			propertyValueArray[i]=new Array(); 
			propertyValueArray[i][0]=new Array(); 
			propertyValueArray[i][1]=new Array(); 
			propertyValueArray[i][2]=new Array(); 
			propertyValueArray[i][3]=new Array(); 
			$j("input[propertyId='"+propertyArray[i]+"']").each(function(index){
				if($j(this).attr("checked")){
					propertyNamesArray[i]=propertyNameArray[i];
					propertyValueArray[i][0].push($j(this).attr("pvValue"));
					propertyValueArray[i][1].push($j(this).attr("pvId"));
					propertyValueArray[i][2].push($j(this).attr("propertyName"));
					propertyValueArray[i][3].push($j(this).attr("propertyId"));
				}
		
			});
			$j("textarea[propertyid='"+propertyArray[i]+"'][editingtype='5']").each(function(){
				if($j(this).val()!=""){
					var content = $j(this).val();
					var valueArray = content.split("||");
					for(var k=0;k<valueArray.length;k++){
						propertyNamesArray[i]=propertyNameArray[i];
						propertyValueArray[i][0].push(valueArray[k]);
						propertyValueArray[i][3].push($j(this).attr("propertyId"));
					}
					
				}
				
			});
		});
	    //去除空值
		for(var i=0;i<propertyValueArray.length;i++){
			if (typeof propertyValueArray[i] != "undefined")
			{
				if(propertyValueArray[i][0].length==0){
					propertyValueArray.splice(i, 1);
					i=i-1;
				}
			}
			
		}
		 //去除空值
		for(var j=0;j<propertyNamesArray.length;j++){
			if (typeof propertyNamesArray[j] == "undefined")
			{
					propertyNamesArray.splice(j, 1);
					j=j-1;
			}
			
		}
		
		var html = "";
		if(propertyValueArray.length !=propertyArray.length){
			html =  nps.i18n("CODING_TIPS");
			$j("#extensionTable").html(html);
			$j("#jsonSku").val("");
		}else{
			htmlA="";
			for(var j=0;j<propertyNamesArray.length;j++){
				htmlA+="<td style='width:150px'>"+propertyNamesArray[j]+"</td>";
			}
			html = "<tr>"+htmlA +
					"<td style='width:150px'>"+nps.i18n("MERCHANT_CODING")+"</td>" +
					"<td style='width:150px'>"+nps.i18n("MERCHANT_SALEPRICE")+"</td>" +
					"<td style='width:150px'>"+nps.i18n("MERCHANT_LISTPRICE")+"</td>" +
					"</tr>";
			$j("#extensionTable").html(html);
			list="";
			itTable(propertyValueArray,0,propertyValueArray[0],"","","");
			list="["+list.substring(0, list.length-1)+"]";
			$j("#jsonSku").val(list);
			
		}
	}else{// 如果没有销售属性
		html = "<tr>" +
		"<td style='width:150px'>"+nps.i18n("MERCHANT_CODING")+"</td>" +
		"<td style='width:150px'>"+nps.i18n("MERCHANT_SALEPRICE")+"</td>" +
		"<td style='width:150px'>"+nps.i18n("MERCHANT_LISTPRICE")+"</td>" +
		"</tr>";
		 html += "<td style='width:150px'><input type='text' mandatory='true' loxiaType='input'  name='codes' value=''/></td><td style='width:150px'><input type='text' mandatory='true' loxiaType='number' decimal='2' id='salePrices' name='salePrices' value=''/></td><td style='width:150px'><input type='text'  loxiaType='number' decimal='2' id='listPrices' name='listPrices' value=''/></td>";
		$j("#extensionTable").html(html);
		
		list=   "[ {'itemId': '','properties': '[]','propertiesName': '[]','outid': ''}]";
		$j("#jsonSku").val(list);
	}
	
	$j("#extensionTable").find("[loxiaType]").each(function(i,n){
		loxia.initLoxiaWidget(this);
	});
	return loxia.SUCCESS; 
}

function sortNumber(a,b)
{
return a - b;
}
var editors =[];
//表单验证
function itemFormValidate(form){
	
	if(!clickFlag){
		return nps.i18n("PLEASE_SET_CODE");
	}
	if(i18nOnOff){
		for ( var i = 0; i < editors.length; i++) {
			var editor = editors[i];
			var content=editor.getData();
			$j("textarea[name='itemCommand.description.values["+i+"]']").val(content);
		}
	}else{
		for ( var i = 0; i < editors.length; i++) {
			var editor = editors[i];
			var content=editor.getData();
			$j("textarea[name='itemCommand.description.value']").val(content);
		}
	}
	var code = $j("#code").val();
    // 正则验证
    if(pdValidCode != null && pdValidCode.length > 0){
    	var re =new RegExp(pdValidCode);
		if(!re.test(code)){
				$j("#code").val("");
				$j("#loxiaTip-r").show();
				$j(".codetip").html(nps.i18n("ITEM_CODE_VALID_FAIL",[code]));
				$j("#code").addClass("ui-loxia-error");
				return nps.i18n("ITEM_CODE_VALID_FAIL",[code]);
			}else{
				$j("#loxiaTip-r").show();
				$j(".codetip").html(nps.i18n("ITEM_UPDATE_CODE_ABLE"));
				setTimeout(function(){ 
					$j("#loxiaTip-r").hide();
				},2000);
			}
		}
    //判断复选框必选
	var att ="";
	$j.each(mustCheckArray,function(j,val){
		var a=0;
		$j("#notSalepropertySpace").find("[mustCheck='"+mustCheckArray[j]+"']").each(function(i,n){
		    if(this.checked){
		    	a+=1;
		    }
	    });
		if(a==0){
			att+="【"+mustCheckArray[j]+"】,";
		}
	});
	if(att!=""){
	     nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),att.substring(0,att.length-1)+nps.i18n("MUST_SELECT"));
	     return;
	}
	
	//如果选了分类，则必须设置默认分类
	var cateLen =$j("input[name='categoriesIds']").length;
	if(cateLen >0){
		var defCategroyId =$j("#defCategroyId").val();
		if(defCategroyId ==null ||defCategroyId ==''){
			return nps.i18n("PLEASE_SET_DEF_CATEGORY");
		}
	}
	
	var salePrice = $j("#salePrice").val();
	
	   var salePriceArray = new Array();
	   var salePriceIndex = 0;
	   var originalSalePriceArray = new Array();
	   $j("#extensionTable").find(".dynamicInputNameSalePrices").each(function(i,n){
		   var salePrice = parseFloat(n.value);
		   originalSalePriceArray[i] = salePrice;
		   if(!isNaN(salePrice)){
			   salePriceArray[salePriceIndex++] = salePrice;
		   }
		});
	   salePriceArray = salePriceArray.sort(sortNumber);

	   if(salePriceArray.length>0){
		   if(!(salePrice>=salePriceArray[0]&&salePrice<=salePriceArray[salePriceArray.length-1])){
			   
			   return nps.i18n("SALEPRICE_OUT_OF_RANGE");
		   }
	   }
	   
	   
	   var listPrice = $j("#listPrice").val();
	   if(listPrice!=null&&listPrice!=""){
		   var listPriceArray = new Array();
		   var listPriceIndex = 0;
		  
		   $j("#extensionTable").find(".dynamicInputNameListPrices").each(function(i,n){
			   var listPrice =  parseFloat(n.value);
			   if(!isNaN(listPrice)){
				   listPriceArray[listPriceIndex++] = listPrice;   
			   }
			});
		   listPriceArray = listPriceArray.sort(sortNumber);
	   
		   if(listPriceArray.length>0){
			   if(!(listPrice>=listPriceArray[0]&&listPrice<=listPriceArray[listPriceArray.length-1])){
				   
				   return nps.i18n("LISTPRICE_OUT_OF_RANGE");
			   }
		   }
	   }
	   
	   //验证 商家编码是否相同
	   var skuCodesArray = new Array();
	   
	   // 验证是否至少填写了一个sku编码 PLEASE_INPUT_ONE_SKU_CODE
	   var atLeastOneCode = false;
	   
	   $j("#extensionTable").find(".dynamicInputNameSkuCode").each(function(i,n){
		   skuCodesArray[i] = $j(this).val();
		});
	   
	   var validateArrayStr="";
		   for(var i=0;i<skuCodesArray.length;i++){
			   var curCode = skuCodesArray[i];
			   
			   if(curCode!=null&&curCode!=""){
				   atLeastOneCode = true;
				   var curSp =  originalSalePriceArray[i];
					  if(isNaN(curSp)){
						  return nps.i18n("PLEASE_INPUT_SALEPRICES");
					  }
			   }
			   for(var j=0;j<skuCodesArray.length;j++){
				   if(i!=j&&curCode!=""&&curCode==skuCodesArray[j]){
					   return nps.i18n("MERCHANT_CODING_EQUAL");
				   }
			   }
			   
		   }
		   
	   
	   if(!atLeastOneCode){
		   return nps.i18n("PLEASE_INPUT_ONE_SKU_CODE");
	   }
	   
	   if(spChangedFlag){
		   return nps.i18n("SALES_PROPERTY_CHANGED");
	   }
	   
	   if(skuCodesArray.length>0){
		 //向服务器提交验证 TODO
		   
		   for(var i=0;i<skuCodesArray.length;i++){
			   validateArrayStr+=skuCodesArray[i];
			   if(i!=skuCodesArray.length-1){
				   validateArrayStr+=",";
			   }
		   }
		   var skuCodeArrayJsonStr = JSON.stringify(skuCodesArray);
		   var validateJson = {"skuCodes":validateArrayStr};
		   console.log(validateJson);
		  var data = nps.syncXhr(validateSkuCodesUrl,validateJson);
		  
		  if (data.isSuccess==false) {
			  return nps.i18n("SKU_CODE_REPEAT")+data.description;
		 }

	   }
    
    return loxia.SUCCESS;  
}
//销售价
function selectSalePrices(){
	var selectedVal = $j("#salePrice").find("option:selected").text();
	//alert(selectedVal);
	$j("#salePrice").empty();
	var priceArray = [];
	$j("#extensionTable").find("input[id=salePrices]").each(function(i,n){
		priceArray[i] = n.value;
	});
	var uniqueArray = unique(priceArray);
	$j("#salePrice").prepend("<option value='0' selected>请选择</option>");
	$j(uniqueArray).each(function(i,n){
		var isSelected ="";
		if(n!="请选择" && parseInt(n)==parseInt(selectedVal)){
			isSelected = "selected";
		}
		$j("#salePrice").append("<option value='"+n+"' "+isSelected+">"+n+"</option>");
	});
}
//吊牌价
function selectListPrices(){
	var selectedVal = $j("#listPrice").find("option:selected").text();
	//alert(selectedVal);
	$j("#listPrice").empty();
	var priceArray = [];
	$j("#extensionTable").find("input[id=listPrices]").each(function(i,n){
		priceArray[i] = n.value;
	});
	var uniqueArray = unique(priceArray);
	$j("#listPrice").prepend("<option value='0' selected>请选择</option>");
	$j(uniqueArray).each(function(i,n){
		var isSelected ="";
		if(n!="请选择" && parseInt(n)==parseInt(selectedVal)){
			isSelected = "selected";
		}
		$j("#listPrice").append("<option value='"+n+"' "+isSelected+">"+n+"</option>");
	});
}
//去重
function unique(data){
	data = data || [];
        var a = {};
	for (var i=0; i<data.length; i++) {
		var v = data[i];
		if (typeof(a[v]) == 'undefined'){
			a[v] = 1;
		}
	};
	data.length=0;
	for (var i in a){
		data[data.length] = i;
	}
	return data;
}

//
///**
// * 将修改过的属性值,存到changedColoerPropertyArray中
// * @returns
// */
//function changeColorProperty(){
//	var currPropertyArray = $j('.ui-block-line.color-select-line');
//	var currItemImage = "";
//	var _itemPropId = 0;
//	var _itemIdAndItemPropertyId = $j('#colorProperty').val();
//	if(_itemIdAndItemPropertyId != undefined){
//		var itemId = $j('#itemid').val();
//		var propertyValueId = '';
//		var itemProperties = '';
//		if(_itemIdAndItemPropertyId.indexOf('#') != -1){
//			var _itemIdAndItemPropertyIds = _itemIdAndItemPropertyId.split('#');
//			if(_itemIdAndItemPropertyIds.length > 0 && _itemIdAndItemPropertyIds != ''){
//				propertyValueId = _itemIdAndItemPropertyIds[0];
//				itemProperties = _itemIdAndItemPropertyIds[1];
//			}
//		}else{
//			return loxia.ERROR;
//		}
//		$j.each(currPropertyArray, function(index, itemImage){
//			_itemPropId = $j(this).find('input[name="itemImages.itemProperties"]').val();
//			//var _picUrl = $j(this).find('img').attr('src');
//			var _desc = $j(this).find('input[name="itemImages.description"]').val();
//			var _picUrl = $j(this).find('a').find('input[name*="itemImage"]').val();
//			if(_picUrl == '../images/main/mrimg.jpg'){
//				_picUrl = $j(this).find('img').attr('src');
//			}
//			if(index == 0){
//				currItemImage += '{"itemId":"'+itemId+'","itemProperties":"'+itemProperties+'","propertyValueId":"'+propertyValueId+'","itemImages":['
//								+'{"description":"'+_desc+'","picUrl":"'+_picUrl+'"},';
//			}else{
//				currItemImage += '{"description":"'+_desc+'","picUrl":"'+_picUrl+'"},';
//			}
//		});
//		currItemImage = currItemImage.substring(0, currItemImage.length-1)+']}';
//		//通过propertyValueId相比较, 相等时删除该数据, 再添加修改后的数据
//		//删除修改前数组中存在的数据
//		$j.each(changedColorPropertyArray, function(index, itemImages){
//			var dataObj = $j.parseJSON(itemImages);
//			if(itemProperties != '' && itemProperties != undefined){
//				if(dataObj.itemProperties == itemProperties){
//					//删除
//					changedColorPropertyArray.splice(index,1);
//				}
//			}else if(propertyValueId != '' && propertyValueId != undefined){
//				if(dataObj.propertyValueId == propertyValueId){
//					//删除
//					changedColorPropertyArray.splice(index,1);
//				}
//			}
//		});
//		
//		//添加修改后的json数据放到changedColorPropertyArray数组中
//		changedColorPropertyArray[changedColorPropertyArray.length] = currItemImage;
//	}else{
//		//商品全局图片
//		changedColorPropertyArray.splice(0, changedColorPropertyArray.length);
//		var globalItemImage = '';
//		var $colorPropertyContent = $j('#colorPropertyContent');
//		var _desc = $colorPropertyContent.find('input[name="itemImage.description"]').val();
//		var _picUrl = $colorPropertyContent.find('a').find('input[name*="itemImage"]').val();
//		globalItemImage = '{"itemId":"", "itemProperties":"", "propertyValueId":"", "itemImages":[{"description":"'+_desc+'", "picUrl":"'+_picUrl+'"}]}';
//		changedColorPropertyArray[changedColorPropertyArray.length] = globalItemImage;
//	}
////	alert(changedColorPropertyArray);
//	return loxia.SUCCESS;
//}
//
///**
// * 去掉description为空的且picUrl="../images/main/mrimg.jpg"的
// * ../images/main/mrimg.jpg 是item的默认图片路径
// */
//function restItemImagesArray(){
//	var finalChangedColorPropertyArray = new Array();
//	$j.each(changedColorPropertyArray, function(index, itemImages){
//		var currItemImageObj = $j.parseJSON(itemImages);
//		var currItemImages = currItemImageObj.itemImages;
//		var isDeleteItemImage = false;
//		var itemId = currItemImageObj.itemId=='undefined'?'':currItemImageObj.itemId;
//		var citemProperties=currItemImageObj.itemProperties=='undefined'?'':currItemImageObj.itemProperties;
//		var cpropertyValueId=currItemImageObj.propertyValueId=='undefined'?'':currItemImageObj.propertyValueId;
//		var finalItemImages = '{"itemId":"'+itemId+'","itemProperties":"'+citemProperties+'","propertyValueId":"'+cpropertyValueId+'", "itemImages":[';
//		for(var i in currItemImages){
//			var _desc = currItemImages[i].description=='undefined'?'':currItemImages[i].description;
//			var _picUrl = currItemImages[i].picUrl;
//			if(_picUrl == 'undefined'){
//				_picUrl = '../images/main/mrimg.jpg';
//			}
//			if(_desc != '' || _picUrl != '../images/main/mrimg.jpg'){
//				finalItemImages += '{"description":"'+_desc+'","picUrl":"'+_picUrl+'"},';
//				isDeleteItemImage = true;
//			}
//		}
//		if(!isDeleteItemImage){
//			finalItemImages += ',';
//		}
//		finalItemImages = finalItemImages.substring(0, finalItemImages.length-1)+']}';
//		finalChangedColorPropertyArray[finalChangedColorPropertyArray.length] = finalItemImages;
//	});
////	alert(finalChangedColorPropertyArray);
//	return finalChangedColorPropertyArray;
//}
//
///**
// * 验证只能添加一下为空
// * @returns {Boolean}
// */
//function validatorOnlyOneNewIsEmpty(){
//	var currPropertyArray = $j('.ui-block-line.color-select-line');
//	var isEmpty = false;
//	$j.each(currPropertyArray, function(index, itemImage){
//		var _picUrl = $j(this).find('input[name="itemImageUrl"]').val();
//		var _desc = $j(this).find('input[name="itemImages.description"]').val();
//		if(_desc == '' &&( _picUrl == ''|| _picUrl == '../images/main/mrimg.jpg')){
//			$j(this).find('input[name="itemImages.description"]').addClass('ui-loxia-error');
//			isEmpty = true;
//		}
//	});
//	return isEmpty;
//}
//
///**
// * 上传图片完成后, 回调的方法
// */
//function colorComplete(){
//	//上传图片完成后, 将在Img中显示上传的图片
//	var currPropertyArray = $j('.ui-block-line.color-select-line');
//	$j.each(currPropertyArray, function(index, itemImage){
//		var _picUrl = $j(this).find('a').find('input[name*="itemImage"]').val();
//		if(_picUrl != '../images/main/mrimg.jpg' && _picUrl != undefined){
//			$j(this).find('input[name="itemImageUrl"]').val(_picUrl);
//			$j(this).find('input[name="itemImage.picUrl"]').val(_picUrl);
//			$j(this).find('img').attr('src', _picUrl);
//		}
//	});
//	changeColorProperty();
//}
///**
// * 属性一级菜单选中
// * @return
// */
//function chooseFilterOrgType(sel, obj){
//	var html ='<option value="">请选择</option>';
//	var itemid = $j("#itemid").val();
//	$j.each(eval(dynamicPropertyCommandListJsonStr), function(index, obj){
//		if(obj.property.name==sel){
//			$j.each(obj.propertyValueList, function(index, proval){
//				html+='<option value="'+proval.id+'#'+proval.propertyId+'#'+itemid+'">'+proval.value+'</option>';			
//			});
//		}
//	});
//	$j("#colorProperty").html(html);
//    return loxia.SUCCESS;
//}
//
//
//function loadColorProp(sel, obj){
//	var editingType = obj.attr('editingType');
//	var isColorProp = obj.attr('iscolorprop');
//	if(isColorProp == 'true'){
//		var $colorPropertySelect = $j('.colorPropertySelect');
//		if(sel != ''){
//			var colors = sel.split('||');
//			var str = '<label>'+nps.i18n('IMAGE_SELECT_COLOR_PROPERTY')+'</label>';
//			str += '<select loxiaType="select" id="colorProperty" name="propertyValueIdAndProperyId">';
//			for(var i in colors){
//				str += '<option value="#'+colors[i]+'">'+colors[i]+'</option>';
//			}
//			str += '</select>';
//			$colorPropertySelect.html(str);
//			//初始化Loxia组件
//			loxia.initContext($j(".colorPropertySelect"));
//		}
//	}
//	//如果最后的是'||', 就除掉
//	var lastTwoChar = sel.substring(sel.length-2);
//	var propertyValues = '';
//	if('||' == lastTwoChar){
//		propertyValues = sel.substring(0, sel.length-2);
//		nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("NOT_DATA_FORMAT"));
//		//obj.val(propertyValues);
//		//obj.addClass("ui-loxia-error");
//		obj.focus();
//		return;
//	}
//	var firstTwoChar = propertyValues.substring(0,2);
//	if('||' == firstTwoChar){
//		nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("NOT_DATA_FORMAT"));
////		obj.val(propertyValues.substring(2));
////		obj.addClass("ui-loxia-error");
//		obj.focus();
//		return;
//	}
//	return loxia.SUCCESS;
//}
//
//function globalItemImage(){
//	if(!isExistColorProp){
//		var $colorPropertySelect = $j('.colorPropertySelect');
//		var $addButton = $j('#colorPropertyContent').next('div .ui-block-line');
//		$colorPropertySelect.html('<label>'+nps.i18n('IMAGE_SELECT_COLOR_PROPERTY')+'</label>');
//		var _html = '<div class="ui-block-line color-select-line">'
//			+'<label>'
//			+'<img src="../images/main/mrimg.jpg" class="color-select-img"/></label>'
//			+'<div>'
//			+'<div class="color-select-line">'
//			+'<input loxiaType="input" readonly="true" type="hidden" name="itemImage.itemId" value=""/>'
//			+'<input loxiaType="input" readonly="true" name="itemImage.picUrl"/>'
//			+'<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span>浏览</span>'		
//			+'<input complete="colorComplete" class="imgUploadComponet fileupload" role="'+thumbnailConfig+'" model="C" hName="itemImage" hValue="../images/main/mrimg.jpg" type="file" url="/demo/upload.json"/></a>'
//			+'</div>'
//			+'<div class="color-select-line">'
//			+'<input loxiaType="input" name="itemImage.description" checkmaster="changeColorProperty" style="width:218px;"/>'
//			+'</div>'
//			+'<div class="color-select-line">'
//			+'<opt:select name="itemImage.type" id="imageType"  expression="chooseOption.IMAGE_TYPE" defaultValue="3" otherProperties="loxiaType=\"select\" "/>'								
//			+'</div></div></div>';
//		$j('#colorPropertyContent').append(_html);
//		//隐藏添加按钮
//		$addButton.hide();
//	}
//	//加载上传组件
//	$j.getScript(base+'/scripts/ajaxfileupload.js');
//}

/**
 * only use for save item
 * @param form
 * @param args
 * @param param
 */
function saveItem(form,args,param){
	
    var f = loxia._getForm(form),
    mode = args.mode||"sync",
    lock = (args.lock == undefined || args.lock)?true:false;

	if(lock){
	    loxia.lockPage();
	}
	
	var result=$j.extend({}, f,param);
	
	var c = this.validateForm(result);
	if(c){
	        nps.asyncXhr($(result).attr("action"),result,args)
	    
	}else{
	    loxia.unlockPage();
	}
	
}

function showSkuTable(saleInfo){
	clickFlag = true;
//	showSth();
	saleInfo.find("#exten").css("display","block");
	saleInfo.find("#extensionTable").html("");
	//拿到  销售属性,  根据销售属性 确定 列
	var salesProperty = new Array();
	
	var salesPropertyIndex = 0;
	for(var i=0;i<dynamicPropertyCommandListJsonStr.length;i++){
		var dynamicPropertyCommand = dynamicPropertyCommandListJsonStr[i];
		console.log(dynamicPropertyCommand);
		//编辑属性为4 或者5 的是 销售属性
		if(dynamicPropertyCommand!=null&&(dynamicPropertyCommand.property.editingType == 4 ||dynamicPropertyCommand.property.editingType == 5)&&dynamicPropertyCommand.property.isSaleProp==true){
			salesProperty[salesPropertyIndex] = dynamicPropertyCommand;
			salesPropertyIndex++;
		}
	}
	
	var propertyIdArray = new Array();
	var propertyValueArray = new Array();
	var propertyValueInputArray = new Array();
	
	var propertyValueArrayIndex = 0;
	var propertyNameArrayIndex = 0;
	
	var sameNameFlag = false;
	for(var i = 0;i<salesProperty.length;i++){
		var saleProperty = salesProperty[i];
		var propertyId = saleProperty.property.id;
		
//		propertyValueArray[propertyValueArrayIndex]=new Array();//第一层
		
		var curPropertyName = null;
		
		var selectedArray = new Array();
		
		var pInputArray = new Array();
		
		var index = 0;
		
		$j("input[propertyId='"+propertyId+"']").each(function(){
			
			if($j(this).attr("checked")){
				var pvValue = $j(this).attr("pvValue");
				var pvId = $j(this).attr("pvId");
				var propertyName = $j(this).attr("propertyName");
				var propertyId = $j(this).attr("propertyId");
				
				var pArray = new Array();//最里边
				pArray[0]=$j(this).attr("pvValue");
				pArray[1]=$j(this).attr("pvId");
				pArray[2]=$j(this).attr("propertyName");
				pArray[3]=$j(this).attr("propertyId");
				
				pInputArray[index] = pArray[1];
				
				
				selectedArray[index]=pArray;
				index++;
				curPropertyName = propertyName;
			}
			
		});
		
		if(selectedArray.length>0){
			propertyIdArray[propertyValueArrayIndex] = propertyId;
			propertyValueArray[propertyValueArrayIndex] = selectedArray;
			
			propertyValueInputArray[propertyValueArrayIndex] = pInputArray;
			propertyValueArrayIndex++;
			
			$j("input[pid='"+propertyId+"']").each(function(index){
				var selectdStr="";
				for(var i=0;i<pInputArray.length;i++){
					selectdStr+=pInputArray[i];
					selectdStr+=",";
				}
				selectdStr=selectdStr.substring(0, selectdStr.length-1);
				$j(this).val(selectdStr);
				
			});
		}
		
		saleInfo.find("textarea[propertyid='"+propertyId+"'][editingtype='5']").each(function(){
			if($j(this).val()!=""){
				var index = 0;
				var content = $j(this).val();
				var valueArray = content.split("||");
				var inputValueArray = new Array();
				for(var k=0;k<valueArray.length;k++){
					var pArray =new Array();
					
					pArray[0]=valueArray[k];
					pArray[2]=saleProperty.property.name;
					pArray[3]=$j(this).attr("propertyId");
					inputValueArray[index]=pArray;
					
					index++;
					
					curPropertyName = pArray[2];
				}
				
				if(inputValueArray.length>0){
					propertyIdArray[propertyValueArrayIndex] = propertyId;
					propertyValueArray[propertyValueArrayIndex] = inputValueArray;
					propertyValueInputArray[propertyValueArrayIndex] = valueArray;
					propertyValueArrayIndex++;
					
					saleInfo.find("input[pid='"+propertyId+"']").each(function(index){
						var selectdStr="";
						for(var i=0;i<pInputArray.length;i++){
							selectdStr+=pInputArray[i];
							selectdStr+=",";
						}
						selectdStr.substring(0, selectdStr.length-1);
						
						for(var i=0;i<valueArray.length;i++){
							for(var j=0;j<valueArray.length;j++){
								if(i!=j){
									if(valueArray[i]==valueArray[j]){
										sameNameFlag = true;
										break;
									}
								}
							}
						}
						
						$j(this).val(content);
						
					});
					
				}
				
			}
			
		});
		
		//其他语言(仅editType=5)的hidden propertyValueInputs input设置
		if(i18nOnOff){
			$j(".saleInfo").each(function(i,dom){
				var me = $j(this);
				var lang = me.attr("lang");
				if(lang != defaultlang){
					me.find("textarea[propertyId='"+propertyId+"'][editingtype='5']").each(function(){
						if($j(this).val()!=""){
							var index = 0;
							var content = $j(this).val();
							var valueArray = content.split("||");
							var inputValueArray = new Array();
							for(var k=0;k<valueArray.length;k++){
								var pArray =new Array();
								
								pArray[0]=valueArray[k];
								pArray[2]=saleProperty.property.name;
								pArray[3]=$j(this).attr("propertyId");
								inputValueArray[index]=pArray;
								
								index++;
								
								//curPropertyName = pArray[2];
							}
							
							if(inputValueArray.length>0){
								/*propertyIdArray[propertyValueArrayIndex] = propertyId;
								propertyValueArray[propertyValueArrayIndex] = inputValueArray;
								propertyValueInputArray[propertyValueArrayIndex] = valueArray;
								propertyValueArrayIndex++;*/
								
								me.find("input[pid='"+propertyId+"']").each(function(index){
									var selectdStr="";
									for(var i=0;i<pInputArray.length;i++){
										selectdStr+=pInputArray[i];
										selectdStr+=",";
									}
									selectdStr.substring(0, selectdStr.length-1);
									
									for(var i=0;i<valueArray.length;i++){
										for(var j=0;j<valueArray.length;j++){
											if(i!=j){
												if(valueArray[i]==valueArray[j]){
													
													sameNameFlag = true;
													break;
												}
											}
										}
									}
									
									$j(this).val(content);
									
								});
								
							}
							
						}
						
					});
				}
			});
		}
		
		
	}
	
	if(sameNameFlag){
		clickFlag = false;
		html=nps.i18n("CUSTOM_PROPERTY_SAME");
		saleInfo.find("#extensionTable").html(html);
		
		return;
	}
	if(propertyValueInputArray.length!=propertyNameArray.length){//如果用户未把每个属性都填上值，则提示用户
		html =  nps.i18n("CODING_TIPS");
		saleInfo.find("#extensionTable").html(html);
	}else{// 画表格
		drawTableContent(propertyValueArray,propertyNameArray,propertyValueInputArray,saleInfo);
	}
	
	saleInfo.find("#extensionTable").find("[loxiaType]").each(function(i,n){
		loxia.initLoxiaWidget(this);
	});
	
}

function drawTableContent(propertyValueArray,propertyNameArray,propertyInputValueArray,saleInfo){

	var skuInfoList = new Array();
	var skuInfoListIndex = 0;
	var tableHeader="<tr>";
	for(var j=0;j<propertyNameArray.length;j++){//动态生成 销售属性的列
		tableHeader+="<td style='width:150px'>"+propertyNameArray[j]+"</td>";
	}
	
	tableHeader+= (
			"<td style='width:150px'>"+nps.i18n("MERCHANT_CODING")+"</td>" +
			"<td style='width:150px'>"+nps.i18n("MERCHANT_SALEPRICE")+"</td>" +
			"<td style='width:150px'>"+nps.i18n("MERCHANT_LISTPRICE")+"</td>" +
			"</tr>");
	
	var tableContent="";
	if(propertyValueArray.length>0){
		
		if(propertyValueArray.length==1){// 销售属性只有1个
			for(var i = 0;i<propertyValueArray[0].length;i++){ 
				var dynamicStr = "<td style='width:150px'>"+propertyValueArray[0][i][0]+"</td>";
				var codesName = getDynamicInputName("code",propertyValueArray[0][i],null);
				var salePriceName = getDynamicInputName("salePrice",propertyValueArray[0][i],null);
				var listPriceName = getDynamicInputName("listPrice",propertyValueArray[0][i],null);
				
				var proHtml="<td style='width:150px'><input type='text' class = 'dynamicInputNameSkuCode'  name='codesNameToReplace' loxiaType='input'  value='CODE_VALUE'/></td>" +
				"<td style='width:150px'><input type='text' class = 'dynamicInputNameSalePrices' id='salePrices'  name='salePriceNameToReplace' decimal='2' loxiaType='number' value='salePrices_value'/></td>" +
				"<td style='width:150px'><input type='text' class = 'dynamicInputNameListPrices' id='listPrices' name='listPriceToReplace' decimal='2' loxiaType='number' value='listPrices_value'/></td>";

				proHtml = proHtml.replace('codesNameToReplace', codesName);
				proHtml = proHtml.replace('salePriceNameToReplace', salePriceName);
				proHtml = proHtml.replace('listPriceToReplace', listPriceName);
				
				var salePrice = $j("#salePrice").val();
				var listPrice = $j("#listPrice").val();
				proHtml = proHtml.replace('CODE_VALUE', "");
				proHtml = proHtml.replace('salePrices_value', salePrice);
				proHtml = proHtml.replace('listPrices_value', listPrice);

				tableContent += ("<tr>"+dynamicStr+proHtml+"</tr>");
			}
			
			
		}else{
			//多个销售属性的展示
			tableContent = buildTable(null, propertyValueArray, 0);
			
		}
		
		
		$j("#jsonSku").val(JSON.stringify(skuInfoList));
	}else{
		
		var proHtml="<td style='width:150px'><input type='text' class = 'dynamicInputNameSkuCode' mandatory='true' name='skuCode' loxiaType='input'  value='CODE_VALUE'/></td>" +
		"<td style='width:150px'><input type='text' id='salePrices' class = 'dynamicInputNameSalePrices' mandatory='true' name='skuSalePrice' decimal='2' loxiaType='number' value='salePrices_value'/></td>" +
		"<td style='width:150px'><input type='text' id='listPrices' class = 'dynamicInputNameListPrices' name='skuListPrice' decimal='2' loxiaType='number' value='listPrices_value'/></td>";

		proHtml = proHtml.replace('CODE_VALUE', "");
		proHtml = proHtml.replace('salePrices_value', "");
		proHtml = proHtml.replace('listPrices_value', "");
			
		tableContent += ("<tr>"+proHtml+"</tr>");
		list=   "[ {'itemId': '','properties': '[]','propertiesName': '[]','outid': ''}]";
		$j("#jsonSku").val(list);
	}
	
	
	var html =  tableHeader+tableContent;
	saleInfo.find("#extensionTable").html(html);
	
}

//--------------------------------多个销售属性循环start-------------------------------
function buildTable(table, data, dataRowIndex){
	var result = new Array();
	if (table == null){
		table = result;
	}
	
	if (dataRowIndex >= data.length){
		var htmlStr = "";

		for(var i = 0;i<table.length;i++){
			var htmlLine = "";
			var arrays = new Array();//所有属性的数组
			for(var j=0;j<table[i].length;j++){
				htmlLine +="<td style='width:150px'>"+table[i][j][0]+"</td> ";
				arrays.push(table[i][j]);
			}
			
			var codesName = getMoreDynamicInputName("code",arrays);
			var salePriceName = getMoreDynamicInputName("salePrice",arrays);
			var listPriceName = getMoreDynamicInputName("listPrice",arrays);
			var tmpArray = clone(arrays);
			
			var proHtml="<td style='width:150px'><input type='text' class = 'dynamicInputNameSkuCode' name='codesNameToReplace' loxiaType='input'  value='CODE_VALUE'/></td>" +
			"<td style='width:150px'><input type='text' class = 'dynamicInputNameSalePrices' id='salePrices'  name='salePriceNameToReplace' decimal='2' loxiaType='number' value='salePrices_value'/></td>" +
			"<td style='width:150px'><input type='text' class = 'dynamicInputNameListPrices' id='listPrices' name='listPriceToReplace' decimal='2' loxiaType='number' value='listPrices_value'/></td>";

			proHtml = proHtml.replace('codesNameToReplace', codesName);
			proHtml = proHtml.replace('salePriceNameToReplace', salePriceName);
			proHtml = proHtml.replace('listPriceToReplace', listPriceName);
			
			var salePrice = $j("#salePrice").val();
			var listPrice = $j("#listPrice").val();
			var code = "";
			
			proHtml = proHtml.replace('CODE_VALUE', code);
			proHtml = proHtml.replace('salePrices_value', salePrice);
			proHtml = proHtml.replace('listPrices_value', listPrice);
			
			if(htmlLine.length!=0){
				htmlStr += "<tr>"+htmlLine+proHtml+"</tr>";
			}
			
		}
		return htmlStr;
	}
	// 组织第i个"销售属性"
	var dataRow = data[dataRowIndex];
	var dynamicStr ="";
	var proHtml="";
	// 组织第1个"销售属性"时
	if (table.length == 0){
		for (var i = 0; i < dataRow.length; i++) {
			var targetRow = new Array();
			
			targetRow.push(dataRow[i]);
			result.push(targetRow);
		};
	}else{
		//循环遍历某一个销售属性dataRow的全部值
		for ( var i = 0; i < dataRow.length; i++) {
			for ( var j = 0; j < table.length; j++) {
				var subResult = clone(table[j]);
				subResult.push(dataRow[i]);
				result.push(subResult);
			};
		}
	}
	// 递归组织下一行数据
	return buildTable(result, data, ++dataRowIndex);
}
//--------------------------------多个销售属性循环end-------------------------------

//获取动态输入框中的值    多个销售属性
function getMoreDynamicInputName(key,array){
	var fullKey = "";
	for (var i = 0; i < array.length; i++) {
		if(array[i]!=null){
			fullKey += getKeyFromArray(array[i]);
			fullKey+="_";
		}
	}
	fullKey += key;
	return fullKey;
}

//js克隆
function clone(obj){  
    var o;  
    switch(typeof obj){  
    case 'undefined': break;  
    case 'string'   : o = obj + '';break;  
    case 'number'   : o = obj - 0;break;  
    case 'boolean'  : o = obj;break;  
    case 'object'   :  
        if(obj === null){  
            o = null;  
        }else{  
            if(obj instanceof Array){  
                o = [];  
                for(var i = 0, len = obj.length; i < len; i++){  
                    o.push(clone(obj[i]));  
                }  
            }else{  
                o = {};  
                for(var k in obj){  
                    o[k] = clone(obj[k]);  
                }  
            }  
        }  
        break;  
    default:          
        o = obj;break;  
    }  
    return o;     
}

function getDynamicInputName(key,array1,array2){
	var fullKey = "";
	if(array1!=null){
		fullKey += getKeyFromArray(array1);
		fullKey+="_";
	}
	
	if(array2!=null){
		
		fullKey+=getKeyFromArray(array2);
		fullKey+="_";
	}
	
	fullKey+=key;
	
	return fullKey;
}

function getKeyFromArray(array){
	//[0] 是pvname  [1] 是 pvid   对于有pvid的 传pvid 对于没有pvId 的 传pvName
	var arraykey="";
	if(array[1]!=null||array[1]!=undefined){//多选
		arraykey = array[1];
	}else{//自定义多选 有pvName 而没pvId
		arraykey = array[0];
	}
	return arraykey;
}
function getSkuStr(pArray){
	var sku = new Object();
	sku.itemId="";
	sku.properties="[]";
	sku.propertiesName = new Array();
	sku.outid ="";
	for(var i=0;i<pArray.length;i++){
		var propertyId = pArray[i][3]
		var pName = pArray[i][2];
		var propertyValueId = pArray[i][1];
		var propertyValue = pArray[i][0];
		
		var propertyName = new Object();
		
		propertyName.id =propertyValueId;
		propertyName.pId = propertyId;
		propertyName.value = propertyValue;
		propertyName.pName = pName;
		
		sku.propertiesName[i]=propertyName;
	}
	sku.propertiesName = JSON.stringify(sku.propertiesName);
	return sku;
}
function getSkuInfoByProperyValueArray(pArray){
	var itemPropertyIdArray = new Array();
	var index = 0;
	for(var i=0;i<pArray.length;i++){
		var propertyId = pArray[i][3];
		var propertyValueId = pArray[i][1];
		var propertyValue = pArray[i][0];
		var itemProperty = findItemPropertyByProvertyIdAndValue(propertyId,propertyValueId,propertyValue);
		
		if(itemProperty!=null){
			var itemPropertyId = itemProperty.id;
			itemPropertyIdArray[index] = itemPropertyId;
			index++;
		}
	}
	itemPropertyIdArray=itemPropertyIdArray.sort();
	
	for(var k=0;k<skuList.length;k++){
		var sku = skuList[k];
		var skuPropertiesArray = JSON.parse(sku.properties);
		skuPropertiesArray = skuPropertiesArray.sort();
		
		if(skuPropertiesArray.toString()==itemPropertyIdArray.toString()){
			return sku;
		}
	}
	
	return null;
}

function findItemPropertyByProvertyIdAndValue(propertyId,propertyValueId,propertyValue){
	
	for(var i = 0 ;i<itemPropertiesStr.length;i++){
		var itemProperty = itemPropertiesStr[i];
		if(propertyValueId!=undefined&&propertyValueId!=null){//多选
			if(itemProperty.propertyId==propertyId&&propertyValueId==itemProperty.propertyValueId){
				return itemProperty;
			}
			
		}else{// 自定义多选
			if(itemProperty.propertyId==propertyId&&propertyValue==itemProperty.propertyValue){
				return itemProperty;
			}
		}
	}
	return null;
}

//obj 下拉框dom对象,type 属性类型(1:销售属性，2:普通属性) 两种属性生成HTML不同故做区分
function changeProGroup(obj,type){
		//获取分组下属性列表请求参数
		var json={proGroupId:$j(obj).val(),propertyId:$j(obj).attr('propertyId')};
		var backWarnEntity = loxia.syncXhr(base+'/item/findProGroupInfo.json', json,{type: "GET"});
		var html = ""
		//
		if(backWarnEntity.isSuccess){
	  		var propertyValueArray = backWarnEntity.description.propertyValueList;	
	  		var property = backWarnEntity.description.property
	  		var picUrl = "";
			if(propertyValueArray!=null && propertyValueArray.length>0){
				
				if(type==1){
					$j.each(propertyValueArray,function(j){
						html+= "<div class='priDiv'><span class='children-store'>" +
								"<input type='checkbox' class='spCkb'  name='propertyValueIds' editingType='4' " +
								"pvId='"+propertyValueArray[j].id+"' propertyId='"+property.id+"' " +
								"pvValue='"+propertyValueArray[j].value+"' propertyName='"+property.name+ "'" +
								"value='"+propertyValueArray[j].id+"'>"+picUrl+propertyValueArray[j].value+"</input></span> </div>";
						
				     });
				}else if(type==2){
					$j.each(propertyValueArray,function(j,val){
						html+= "<span class='children-store normalCheckBoxSpan'><input type='checkbox' class = 'normalCheckBoxCls' pid="+property.id+" mustCheck='"+property.name+"' name='' " +
								"value='"+propertyValueArray[j].id+"'>"+propertyValueArray[j].value+"</input></span>";
				     });
					

				}
				
				$j(obj).next().html(html);
				$j(".normalCheckBoxCls").each(function(){
					var curCheckBox = $j(this);
					curCheckBox.change(function(){
						drawNoSalePropEditing4Type(num);				
					});
				});
				
			}
		}
}




function drawNoSalePropEditing4Type(curSize){
	if(curSize==undefined ||curSize ==null){
		curSize =0;
	}
	var tempNum =curSize;
	//去除所有
	$j(".hidBoxSpan").find("div[class='repNormalCheckBoxCls']").siblings().remove();
	
	$j(".hidBoxSpan").find("div[class='repNormalCheckBoxCls']").each(function(){
		var pid=$j(this).attr("pid");
		var pvid=$j(this).attr("pvid");
		var isCheck =false;
		$j(".normalCheckBoxCls").each(function(){
			if(pid ==$j(this).attr("pid") &&pvid==$j(this).attr("value")){
				if('checked' == $j(this).attr("checked")){
					isCheck =true ;
				}
				return ;
			}
		});
		if(isCheck){
			var parent =$j(this).parent();
			var ipropertiesHtml = "<input type='hidden' value='propertyId_toReplace' name='iProperties.propertyId'/>"+
			"<input type='hidden' value='propertyValueId_toRepalce' name='iProperties.propertyValueId'/>"+
			"<input type='hidden' value='' name='iProperties.id'><input type='hidden' value='' name='iProperties.propertyDisplayValue'><input type='hidden' value='' name='iProperties.createTime'><input type='hidden' value='' name='iProperties.modifyTime'><input type='hidden' value='' name='iProperties.version'><input type='hidden' value='' name='iProperties.itemId'><input type='hidden' value='' name='iProperties.picUrl'>";

			if(i18nOnOff){
			 	   for ( var j = 0; j < i18nLangs.length; j++) {
							var i18nLang = i18nLangs[j];
							ipropertiesHtml = ipropertiesHtml+ "<input type='hidden' name='iProperties.propertyValue.values["+tempNum+"-"+j+"]' value=''/>";
							ipropertiesHtml = ipropertiesHtml+ "<input type='hidden' name='iProperties.propertyValue.langs["+tempNum+"-"+j+"]' value='"+i18nLang.key+"'/>";
			 	   }
			    }else{
			    	ipropertiesHtml = ipropertiesHtml +"<input type='hidden'name='iProperties.propertyValue.value["+tempNum+"]' value=''/>";
			    }
			//}
			ipropertiesHtml=ipropertiesHtml.replace("propertyId_toReplace", pid);
			ipropertiesHtml=ipropertiesHtml.replace("propertyValueId_toRepalce", pvid);
			parent.append(ipropertiesHtml);
			tempNum ++;
		}
	});
}




$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    
    $j("#secondBtnLineDiv").hide();
    
    $j.fn.zTree.init($j("#industrytreeDemo"), setting, zNodes);
    //缩略图大小
//    thumbnailConfig = $j("#thumbnailConfig").val();
    //global function
//	globalItemImage();
	key = $j("#key");
	key.bind("focus", focusKey)
	.bind("blur", blurKey)
	.bind("propertychange", searchNode)
	.bind("input", searchNode);
	
	var treeObj = $j.fn.zTree.getZTreeObj("industrytreeDemo");
	//1.将所有的节点转换为简单 Array 格式
	var nodes = treeObj.transformToArray(treeObj.getNodes());
	var booleanFlag = true;
	for(var i = 0;i<nodes.length;i++){
		//2.如果此节点为父节点 或者 为ROOT节点 ，则让此节点没有radio选框
		if(nodes[i].isParent || nodes[i].id == 0){
			nodes[i].nocheck = true;
		}else{
			if(booleanFlag){
				//3.第一个叶子节点的 radio为checked，然后把booleanFlag设置为false
				nodes[i].checked = true;
				var node = nodes[i].getParentNode();
				console.log(node.name);
				if(null != node){
					node.open = true;
					firstNodeId=nodes[i].id;
					firstNodeName=nodes[i].name;
					booleanFlag = false;
				}
				//onCheck(event, treeId, nodes[i]);
				
			}
		}
		treeObj.refresh();
	}

	//商品分类下拉树
	$j.fn.zTree.init($j("#treeDemo"), categorySetting, categoryzNodes);
	//让商品分类下拉树只有叶子节点可选
	var _treeObj = $j.fn.zTree.getZTreeObj("treeDemo");
	//1.将所有的节点转换为简单 Array 格式
	var _nodes = _treeObj.transformToArray(_treeObj.getNodes());
	for(var i = 0;i<_nodes.length;i++){
		//2.如果此节点为父节点 或者 为ROOT节点 ，则让此节点没有radio选框
		if(_nodes[i].isParent || _nodes[i].id == 0){
			_nodes[i].nocheck = true;
		}
		_treeObj.refresh();
	}
    $j("#category").click(function() {
		var cityObj = $j(this);
		var cityOffset = $j(this).offset();
		$j("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top+ cityObj.outerHeight() + "px"}).slideDown("fast");

		$j("body").bind("mousedown", onBodyDown);
	});
//********************************************默认分类
//    //商品分类下拉树
//	$j.fn.zTree.init($j("#defaultCategoryTree"), defaultCategorySetting, defaultCategoryzNodes);
//	//让商品分类下拉树只有叶子节点可选
//	var $tree = $j.fn.zTree.getZTreeObj("defaultCategoryTree");
//	//节点全部展开
//	$tree.expandAll(true); 
//	//1.将所有的节点转换为简单 Array 格式
//	var _defaultNodes = $tree.transformToArray($tree.getNodes());
//	for(var i = 0;i<_defaultNodes.length;i++){
//		//2.如果此节点为父节点 或者 为ROOT节点 ，则让此节点没有radio选框
//		if(_defaultNodes[i].isParent || _defaultNodes[i].id == 0){
//			_defaultNodes[i].nocheck = true;
//		}
//		$tree.refresh();
//	}
//
//    $j("#defaultCategory").click(function() {
//    	var cityObj = $j(this);
//    	var cityOffset = $j(this).offset();
//    	$j("#defaultMenuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
//    	
//    	$j("body").bind("mousedown", onBodyDownDefault);
//    });
//    
//    $j("#category").on("change",function(){
//		//var categoryTree = $j.fn.zTree.getZTreeObj("treeDemo");
//		//var node = categoryTree.getCheckedNodes();
//		//alert("sdfsdf");
//	});

    
    

    //下一步
	$j(".button.orange.next").on("click",function(){
		
		$j("#secondBtnLineDiv").show();
		$j("#firstBtnLineDiv").hide();
		var ztree = $j.fn.zTree.getZTreeObj("industrytreeDemo");
		var nodes_ = ztree.getCheckedNodes(true);
		num = 0;
		if (nodes_.length>0) {
			var industryId =nodes_[0].id;
			var name = nodes_[0].name;
			var json={"industryId":industryId};
	  		var _data = loxia.syncXhr(base+'/item/findDynamicPropertisJson.json', json,{type: "GET"});
	  		dynamicPropertyCommandListJsonStr=_data.description;
	  		dynamicPropertyCommandListJsonStr = JSON.parse(dynamicPropertyCommandListJsonStr);
	  		var backWarnEntity = loxia.syncXhr(base+'/item/findDynamicPropertis.json', json,{type: "GET"});
	  		if(backWarnEntity.isSuccess){
	  			$j("#chooseIndustry").html(name);
	  			$j("#industryId").val(industryId);
				$j("#first").css("display","none");
				$j("#second").css("display","block");
				//清空
				propertyArray.splice(0,propertyArray.length);
				propertyNameArray.splice(0,propertyNameArray.length);
				jsonArray = backWarnEntity.description;
				var SalepropertySpaceHtmlArr= [];
				var notSalepropertySpaceHtml="";
				var propSelectHtml="<option value=''>请选择</option>";
				//var num = 0;-将num提取到前面 一般属性类型-多选 将按照最大num append hidden到所有一般属性最后
				//处理销售属性
				if(i18nOnOff){
					for ( var j = 0; j < i18nLangs.length; j++) {
						var i18nLang = i18nLangs[j];
						var SalepropertySpaceHtml="";
						$j.each(jsonArray,function(i,val){
						if(jsonArray[i].property.isSaleProp){
							propertyIdArray[propertyIdArrayIndex] = jsonArray[i].property.id;
							propertyIdArrayIndex++;
							var html2 ="";
							if(jsonArray[i].property.editingType == 4 && i18nLang.key == defaultlang){
								jsonArray2 = jsonArray[i].propertyValueList;
								
								//属性值分组列表
								jsonProGroupArray = jsonArray[i].propertyValueGroupList;
								
								var html1="";
								var picUrl="";
								
								
								// 属性分组相关HMTL
								var html3 ="";  
								
								//如果选项值为空 不显示该多选框
								if(jsonArray2!=null&&jsonArray2.length>0){
									//销售属性
									propertyArray.push(jsonArray[i].property.id);
									propertyNameArray.push(jsonArray[i].property.name);
									$j.each(jsonArray2,function(j,val){
										if(jsonArray[i].property.isColorProp && jsonArray[i].property.hasThumb ){
											picUrl ="<img src='"+baseUrl+"/images/1.png'>";
										}
										html1+= "<div class='priDiv'><span class='children-store'>" +
												"<input type='checkbox' class='spCkb'  name='propertyValueIds' editingType='4' " +
												"pvId='"+jsonArray2[j].id+"' propertyId='"+jsonArray[i].property.id+"' " +
												"pvValue='"+jsonArray2[j].value+"' propertyName='"+jsonArray[i].property.name+ "'" +
												"value='"+jsonArray2[j].id+"'>"+picUrl+jsonArray2[j].value+"</input></span> </div>";
										
								     });
									
									//属性值分组处理
									if(jsonProGroupArray != null && jsonProGroupArray.length>0){
										 html3 = '<br><select  onchange="changeProGroup(this,1)" loxiaType="select"  propertyId="'+jsonArray[i].property.id+'">' ;
										 html3 += '<option value="">'+nps.i18n("PLEASE_SELECT_PROPERTY_GROUP")+'</option>';
										 $j.each(jsonProGroupArray, function (i) {
										      html3 += '<option value="'+jsonProGroupArray[i].id + '">'+  jsonProGroupArray[i].name + '</option>';
										 });
										 html3 += "</select>"
									}
									
									html2= "<div class='ui-block-line '><label style=''>"+jsonArray[i].property.name+"</label><div >"+
										"<input type='hidden' value='"+ jsonArray[i].property.id +"'  name = 'propertyIds' />"+
										"<input type='hidden' value=''  name='propertyValueInputIds' pid='"+jsonArray[i].property.id+"' />"+
										html3+
								         "<div class='wl-right-auto width-percent50 mt10'>"+
										          html1+
								        " </div>"+
								        " </div>"+
									    " </div>";
								}
							  }else if(jsonArray[i].property.editingType == 5){
								propertyArray.push(jsonArray[i].property.id);
								var add= true;
								for ( var m = 0; m < propertyNameArray.length; m++) {
								   var str = propertyNameArray[m];
								   if(str==jsonArray[i].property.name){
									   add = false;
								   }
								}
								if(add){
									propertyNameArray.push(jsonArray[i].property.name);
								}
								html2 = '<div class="ui-block-line "><label>'+jsonArray[i].property.name+'</label>';
								html2+= "<input type='hidden' name = 'propertyIds' value='"+ jsonArray[i].property.id +"' />";
								html2+="<input type='hidden' value='' class='propertyValueInputs' name='propertyValueInputs' pid='"+jsonArray[i].property.id+"' />";
								html2+="<input type='hidden' value='"+i18nLang.key+"'  name='propertyValueInputs'/>";
								html2 += '<div class="priDiv">';
								if(jsonArray[i].property.isColorProp){
									html2 += '<textarea class="customerSelect spTa" loxiaType="input" name="propertyValue" editingType="5" isColorProp="true" propertyId="'+jsonArray[i].property.id+'" style="width: 600px; height: 45px"  ></textarea>';
								}else{
									html2 += '<textarea class="customerSelect spTa" loxiaType="input" name="propertyValue" editingType="5" isColorProp="false" propertyId="'+jsonArray[i].property.id+'" style="width: 600px; height: 45px"  ></textarea>';
									
								}
								html2 += '<span style="margin: 17px 0 0 10px; position: absolute;">'+i18nLang.value+'&nbsp;&nbsp;属性名之间请以"||"分隔</span>'
								+'</div></div>';
							}
							SalepropertySpaceHtml= SalepropertySpaceHtml+html2;
							//是否存在颜色属性
							if(jsonArray[i].property.isColorProp){
								isExistColorProp = true;
								var propertyValueListJson = '';
								//editingType为多选
								var str = "";
								if(jsonArray[i].property.editingType == 4){
									propertyValueListJson = jsonArray[i].propertyValueList;
									if(propertyValueListJson != '' && propertyValueListJson.length > 0){
										str = '<label>'+nps.i18n('IMAGE_SELECT_COLOR_PROPERTY')+'</label>';
										str += '<select loxiaType="select" id="colorProperty" name="propertyValueIdAndProperyId">';
										$j.each(propertyValueListJson, function(index, propertyValue){
											str += '<option value="'+propertyValue.id+'#">'+propertyValue.value+'</option>';
										});
										str += '</select>';
									}
								}else if(jsonArray[i].property.editingType == 5){
									str = '<label>'+nps.i18n('IMAGE_SELECT_COLOR_PROPERTY')+'</label>';
									str += '<select loxiaType="select" id="colorProperty" name="propertyValueIdAndProperyId">';
									str += '<option value="">'+nps.i18n('PLEASE_SELECT')+'</option>';
									str += '</select>';
								}
								$j('.colorPropertySelect').html(str);
								$j('#colorPropertyContent').empty();
								$j('#colorPropertyContent').next('.ui-block-line').show();
							}
							 }
							
						});
						
						var SalepropertySpaceHtmlObj= {};
						SalepropertySpaceHtmlObj.html = SalepropertySpaceHtml;
						SalepropertySpaceHtmlObj.lang = i18nLang.key;
						SalepropertySpaceHtmlArr.push(SalepropertySpaceHtmlObj);
					}
				}else{  //  i18nOn
					var SalepropertySpaceHtml="";
					$j.each(jsonArray,function(i,val){
					   if(jsonArray[i].property.isSaleProp){
						propertyIdArray[propertyIdArrayIndex] = jsonArray[i].property.id;
						propertyIdArrayIndex++;
						var html2 ="";
						
						// 属性分组相关HMTL
						var html3 ="";  
						if(jsonArray[i].property.editingType == 4){
							
							//属性值列表
							jsonArray2 = jsonArray[i].propertyValueList;
							
							//属性值分组列表
							jsonProGroupArray = jsonArray[i].propertyValueGroupList;
							var html1="";
							var picUrl="";
							//如果选项值为空 不显示该多选框
							if(jsonArray2!=null&&jsonArray2.length>0){
								//销售属性
								propertyArray.push(jsonArray[i].property.id);
								propertyNameArray.push(jsonArray[i].property.name);
								//propSelectHtml+='<option value="'+jsonArray[i].property.name+'" }>'+jsonArray[i].property.name+'</option>';
								
								//属性值分组处理
								if(jsonProGroupArray != null && jsonProGroupArray.length>0){
									 html3 = '<br><select  onchange="changeProGroup(this,1)" loxiaType="select"  propertyId="'+jsonArray[i].property.id+'">' ;
									 html3 += '<option value="">'+nps.i18n("PLEASE_SELECT_PROPERTY_GROUP")+'</option>';
									 $j.each(jsonProGroupArray, function (i) {
									      html3 += '<option value="'+jsonProGroupArray[i].id + '">'+  jsonProGroupArray[i].name + '</option>';
									 });
									 html3 += "</select>"
								}
								
								
								//属性值处理
								$j.each(jsonArray2,function(j,val){
									if(jsonArray[i].property.isColorProp && jsonArray[i].property.hasThumb ){
										picUrl ="<img src='"+baseUrl+"/images/1.png'>";
									}
									html1+= "<div class='priDiv'><span class='children-store'>" +
											"<input type='checkbox' class='spCkb'  name='propertyValueIds' editingType='4' " +
											"pvId='"+jsonArray2[j].id+"' propertyId='"+jsonArray[i].property.id+"' " +
											"pvValue='"+jsonArray2[j].value+"' propertyName='"+jsonArray[i].property.name+ "'" +
											"value='"+jsonArray2[j].id+"'>"+picUrl+jsonArray2[j].value+"</input></span> </div>";
									
							     });
								
								
								
								html2= "<div class='ui-block-line '><label style=''>"+jsonArray[i].property.name+"</label><div >"+
									"<input type='hidden' value='"+ jsonArray[i].property.id +"'  name = 'propertyIds' />"+
									"<input type='hidden' value='' name='propertyValueInputIds' pid='"+jsonArray[i].property.id+"' />"+
									html3+"<div class='wl-right-auto width-percent50 mt10'>"+ html1+
							        " </div>"+
							        " </div>"+
								    " </div>";
							}
						  }else if(jsonArray[i].property.editingType == 5){
							  
							propertyArray.push(jsonArray[i].property.id);
							propertyNameArray.push(jsonArray[i].property.name);
							
							html2 = '<div class="ui-block-line "><label>'+jsonArray[i].property.name+'</label>';
							html2+= "<input type='hidden' name = 'propertyIds' value='"+ jsonArray[i].property.id +"' />";
							html2+="<input type='hidden' value='' name='propertyValueInputs' pid='"+jsonArray[i].property.id+"' />";
							html2 += '<div class="priDiv">';
							if(jsonArray[i].property.isColorProp){
								html2 += '<textarea class="customerSelect spTa" loxiaType="input" name="propertyValue" editingType="5" isColorProp="true" propertyId="'+jsonArray[i].property.id+'" style="width: 600px; height: 45px"  ></textarea>';
							}else{
								html2 += '<textarea class="customerSelect spTa" loxiaType="input" name="propertyValue" editingType="5" isColorProp="false" propertyId="'+jsonArray[i].property.id+'" style="width: 600px; height: 45px"  ></textarea>';
								
							}
								html2 += '<span style="margin: 17px 0 0 10px; position: absolute;">属性名之间请以"||"分隔</span>'
								+'</div></div>';
						}
						SalepropertySpaceHtml=SalepropertySpaceHtml+html2;
						
						var SalepropertySpaceHtmlObj= {};
						SalepropertySpaceHtmlObj.html = SalepropertySpaceHtml;
						SalepropertySpaceHtmlArr.push(SalepropertySpaceHtmlObj);
						//是否存在颜色属性
						if(jsonArray[i].property.isColorProp){
							isExistColorProp = true;
							var propertyValueListJson = '';
							//editingType为多选
							var str = "";
							if(jsonArray[i].property.editingType == 4){
								propertyValueListJson = jsonArray[i].propertyValueList;
								if(propertyValueListJson != '' && propertyValueListJson.length > 0){
									str = '<label>'+nps.i18n('IMAGE_SELECT_COLOR_PROPERTY')+'</label>';
									str += '<select loxiaType="select" id="colorProperty" name="propertyValueIdAndProperyId">';
									$j.each(propertyValueListJson, function(index, propertyValue){
										str += '<option value="'+propertyValue.id+'#">'+propertyValue.value+'</option>';
									});
									str += '</select>';
								}
							}else if(jsonArray[i].property.editingType == 5){
								str = '<label>'+nps.i18n('IMAGE_SELECT_COLOR_PROPERTY')+'</label>';
								str += '<select loxiaType="select" id="colorProperty" name="propertyValueIdAndProperyId">';
								str += '<option value="">'+nps.i18n('PLEASE_SELECT')+'</option>';
								str += '</select>';
							}
							$j('.colorPropertySelect').html(str);
							$j('#colorPropertyContent').empty();
							$j('#colorPropertyContent').next('.ui-block-line').show();
						}
					}
				  });
				}
				//非销售属性
				$j.each(jsonArray,function(i,val){
					if(!jsonArray[i].property.isSaleProp){
						//编辑类型 ：1 单行输入2可输入单选3单选4多选
						var html3="";
						jsonArray2 = jsonArray[i].propertyValueList  ;
						var isIncrease =true ;
						if(jsonArray[i].property.editingType==1){
							var required="";
							if(jsonArray[i].property.required){
								required="mandatory='true'";
							}							
							var inputHtml ="";
							if(i18nOnOff){
								if(jsonArray[i].property.valueType==1){
									for ( var j = 0; j < i18nLangs.length; j++) {
										var i18nLang = i18nLangs[j];
										inputHtml=inputHtml+"<input type=\"text\" name=\"iProperties.propertyValue.values["+num+"-"+j+"]\" style='width: 600px' loxiaType=\"input\" "+required+" />";
										inputHtml= inputHtml+'<input class="i18n-lang"  type="text" '+
										'name="iProperties.propertyValue.langs['+num+'-'+j+']"  value="'+i18nLang.key+'"/><span>'+i18nLang.value+'</span><br>';
									}
								}
								if(jsonArray[i].property.valueType==2){
									for ( var j = 0; j < i18nLangs.length; j++) {
										var i18nLang = i18nLangs[j];
										inputHtml=inputHtml+"<input type=\"text\" name=\"iProperties.propertyValue.values["+num+"-"+j+"]\" loxiaType=\"number\" "+required+" />";
										inputHtml= inputHtml+'<input class="i18n-lang"  type="text" '+
										'name="iProperties.propertyValue.langs['+num+'-'+j+']"  value="'+i18nLang.key+'"/><span>'+i18nLang.value+'</span><br>';
									}
								}
								if(jsonArray[i].property.valueType==3){
									for ( var j = 0; j < i18nLangs.length; j++) {
										var i18nLang = i18nLangs[j];
										inputHtml=inputHtml+"<input type=\"text\"   name=\"iProperties.propertyValue.values["+num+"-"+j+"]\" loxiaType=\"date\" "+required+" />";
										inputHtml= inputHtml+'<input class="i18n-lang"  type="text" '+
										'name="iProperties.propertyValue.langs['+num+'-'+j+']"  value="'+i18nLang.key+'"/><span>'+i18nLang.value+'</span><br>';
									}	
								}
								if(jsonArray[i].property.valueType==4){
									for ( var j = 0; j < i18nLangs.length; j++) {
										var i18nLang = i18nLangs[j];
										inputHtml=inputHtml+"<input type=\"text\" showtime=\"true\"   name=\"iProperties.propertyValue.values["+num+"-"+j+"]\" loxiaType=\"date\" "+required+" />";
										inputHtml= inputHtml+'<input class="i18n-lang"  type="text" '+
										'name="iProperties.propertyValue.langs['+num+'-'+j+']"  value="'+i18nLang.key+'"/><span>'+i18nLang.value+'</span><br>';
									}
								}
							}else{
								if(jsonArray[i].property.valueType==1){
									inputHtml="<input type=\"text\" name=\"iProperties.propertyValue.value["+num+"]\" style='width: 600px' loxiaType=\"input\" "+required+" />";
								}
								if(jsonArray[i].property.valueType==2){
									inputHtml="<input type=\"text\" name=\"iProperties.propertyValue.value["+num+"]\" loxiaType=\"number\" "+required+" />";
								}
								if(jsonArray[i].property.valueType==3){
									inputHtml="<input type=\"text\"   name=\"iProperties.propertyValue.value["+num+"]\" loxiaType=\"date\" "+required+" />";
								}
								if(jsonArray[i].property.valueType==4){
									inputHtml="<input type=\"text\" showtime=\"true\"   name=\"iProperties.propertyValue.value["+num+"]\" loxiaType=\"date\" "+required+" />";
								}
								
							}
						
							html3="<label >"+jsonArray[i].property.name+"</label>"+
		                      "<div >" +inputHtml+
		                       "<input type='hidden' name='iProperties.propertyId' value='"+jsonArray[i].property.id+"'/>" +
		                       "<input type='hidden' name='iProperties.id' value=''/>" +
		                       "<input type='hidden' name='iProperties.propertyDisplayValue' value=''/>" + 
		                       "<input type='hidden' name='iProperties.createTime' value=''/>" + 
		                       "<input type='hidden' name='iProperties.modifyTime' value=''/>" + 
		                       "<input type='hidden' name='iProperties.version' value=''/>" + 
		                       "<input type='hidden' name='iProperties.itemId' value=''/>" + 
		                       "<input type='hidden' name='iProperties.propertyValueId' value=''/>" + 
		                       "<input type='hidden' name='iProperties.picUrl' value=''/>" + 
		                       "</div>";
						}else if(jsonArray[i].property.editingType==2){
							html3="<label >"+jsonArray[i].property.name+"</label><div ><select style='width:160px;height:25px' onchange='doOther(this,"+jsonArray[i].property.id+")' name='iProperties.propertyValueId'>";
		                      $j.each(jsonArray2,function(j,val){
									html3+= "<option value ='"+jsonArray2[j].id+"' >"+jsonArray2[j].value+"</option>";
							     });
		                      html3+=" <option value ='' >"+nps.i18n("OTHERS")+"</option></select>" +
		                       "<input type='hidden' name='iProperties.propertyId' value='"+jsonArray[i].property.id+"'/>" +
		                       "<input type='hidden' name='iProperties.id' value=''/>" +
		                       "<input type='hidden' name='iProperties.propertyDisplayValue' value=''/>" + 
		                       "<input type='hidden' name='iProperties.createTime' value=''/>" + 
		                       "<input type='hidden' name='iProperties.modifyTime' value=''/>" + 
		                       "<input type='hidden' name='iProperties.version' value=''/>" + 
		                       "<input type='hidden' name='iProperties.itemId' value=''/>" + 
		                       "<input type='hidden' name='iProperties.picUrl' value=''/>" ;
		                      if(i18nOnOff){
		                    	  for ( var j = 0; j < i18nLangs.length; j++) {
										var i18nLang = i18nLangs[j];
										html3 = html3 +	
					                       "&nbsp;<input type='hidden' id='pv_"+jsonArray[i].property.id+"'  name='iProperties.propertyValue.values["+num+"-"+j+"]' value=''/>";
				                    	html3= html3+'<input class="i18n-lang"  type="text" '+
											'name="iProperties.propertyValue.langs['+num+'-'+j+']"  value="'+i18nLang.key+'"/>';
		                    	  }
		                    	  html3 = html3 +"</div>";
		                      }else{
		                    	  html3 = html3 +	
			                       "&nbsp;<input type='hidden' id='pv_"+jsonArray[i].property.id
			                       +"'  name='iProperties.propertyValue.value["+num+"]' value=''/>" + 
			                      "</div>";
		                      }
		                       
						}else if(jsonArray[i].property.editingType==3){
							html3="<label >"+jsonArray[i].property.name+"</label><div ><select style='width:160px;height:25px' name='iProperties.propertyValueId'>";
		                      $j.each(jsonArray2,function(j,val){
									html3+= "<option value ='"+jsonArray2[j].id+"'>"+jsonArray2[j].value+"</option>";
							     });
		                      html3+="</select>" +
		                       "<input type='hidden' name='iProperties.propertyId' value='"+jsonArray[i].property.id+"'/>" +
		                       "<input type='hidden' name='iProperties.id' value=''/>" +
		                       "<input type='hidden' name='iProperties.propertyDisplayValue' value=''/>" + 
		                       "<input type='hidden' name='iProperties.createTime' value=''/>" + 
		                       "<input type='hidden' name='iProperties.modifyTime' value=''/>" + 
		                       "<input type='hidden' name='iProperties.version' value=''/>" + 
		                       "<input type='hidden' name='iProperties.itemId' value=''/>" + 
		                       "<input type='hidden' name='iProperties.picUrl' value=''/>" ;
		                       if(i18nOnOff){
		                    	   for ( var j = 0; j < i18nLangs.length; j++) {
										var i18nLang = i18nLangs[j];
									    html3 = html3+ "<input type='hidden' name='iProperties.propertyValue.values["+num+"-"+j+"]' value=''/></div>";
			                    	    html3= html3+'<input class="i18n-lang"  type="text" '+
										'name="iProperties.propertyValue.langs['+num+'-'+j+']"  value="'+i18nLang.key+'"/>';
		                    	   }
		                       }else{
		                    	   html3 = html3+ "<input type='hidden' name='iProperties.propertyValue.value["+num+"]' value=''/></div>";
		                       }
						}else if(jsonArray[i].property.editingType==4){
							isIncrease =false ;
							if(jsonArray2!=null&&jsonArray2.length>0){
								html3="<label >"+jsonArray[i].property.name+"</label>";
								if(jsonArray[i].property.required){
								    mustCheckArray.push(jsonArray[i].property.name);
								}
								
								//属性值分组列表
								jsonProGroupArray = jsonArray[i].propertyValueGroupList;
								
								//属性值分组处理
								if(jsonProGroupArray != null && jsonProGroupArray.length>0){
									html3 += '<br><select  onchange="changeProGroup(this,2)" loxiaType="select"  propertyId="'+jsonArray[i].property.id+'">' ;
									html3 += '<option value="">'+nps.i18n("PLEASE_SELECT_PROPERTY_GROUP")+'</option>';
									 $j.each(jsonProGroupArray, function (i) {
										 html3 += '<option value="'+jsonProGroupArray[i].id + '">'+  jsonProGroupArray[i].name + '</option>';
									 });
									 html3 += "</select>"
								}
								
								html3 += "<div>"
								
								$j.each(jsonArray2,function(j,val){
									html3+= "<span class='children-store normalCheckBoxSpan'><input type='checkbox' class = 'normalCheckBoxCls' pid="+jsonArray[i].property.id+" mustCheck='"+jsonArray[i].property.name+"' name='' " +
											"value='"+jsonArray2[j].id+"'>"+jsonArray2[j].value+"</input></span>";
							     });
								
								html3 += "</div>"

								
							}
						}else if(jsonArray[i].property.editingType==5){
							html3 = '<label>'+jsonArray[i].property.name+'</label>';
							html3 += '<div class="priDiv">';
							if(i18nOnOff){
								for ( var j = 0; j < i18nLangs.length; j++) {
									var i18nLang = i18nLangs[j];
									html3 = html3+'<textarea loxiaType="input" name="iProperties.propertyValue.values['+num+'-'+j+']"  style="width: 600px; height: 45px" ></textarea>';
									html3= html3+'<input class="i18n-lang"  type="text" name="iProperties.propertyValue.langs['+num+'-'+j+']"  value="'+i18nLang.key+'"/>';
									html3 = html3+'<span style="margin: 17px 0 0 10px; position: absolute;">'+i18nLang.value+'&nbsp;&nbsp;属性名之间请以"||"分隔</span><br>';
								}  
							}else{
								html3 = html3+'<textarea loxiaType="input" name="iProperties.propertyValue"  style="width: 600px; height: 45px" ></textarea>';
								html3 = html3+'<span style="margin: 17px 0 0 10px; position: absolute;">属性名之间请以"||"分隔</span>';
							}
		                    html3 = html3+'<input type="hidden" name="iProperties.propertyId" value="'+jsonArray[i].property.id+'"/>'
								+'<input type="hidden" name="iProperties.id" value=""/>'
								+'<input type="hidden" name="iProperties.propertyDisplayValue" value=""/>'
								+'<input type="hidden" name="iProperties.createTime" value=""/>'
								+'<input type="hidden" name="iProperties.modifyTime" value=""/>'
								+'<input type="hidden" name="iProperties.version" value=""/>'
								+'<input type="hidden" name="iProperties.itemId" value=""/>'
								+'<input type="hidden" name="iProperties.picUrl" value=""/>'
								+'<input type="hidden" name="iProperties.propertyValueId" value=""/></div>';
		                    
						}
						
						var html4="";
						if(html3!=""){
							html4="<div class='ui-block-line '>"+html3+"</div>";
						}
						notSalepropertySpaceHtml=notSalepropertySpaceHtml+html4;
						
						if(isIncrease){
							num++;
						}
						
					}
				});
				
				var rhtml3 ="";
				
				$j.each(jsonArray,function(i,val){
					if(!jsonArray[i].property.isSaleProp){
						jsonArray2 = jsonArray[i].propertyValueList  ;
						if(jsonArray[i].property.editingType==4){
							if(jsonArray2!=null&&jsonArray2.length>0){
								$j.each(jsonArray2,function(j,val){
									rhtml3+= "<span class='hidBoxSpan'><div type='hidden' class = 'repNormalCheckBoxCls' pid="+jsonArray[i].property.id+" pvid='"+jsonArray2[j].id+"'/></span>";
							     });
							}
						}
					}
				});
				
				notSalepropertySpaceHtml +=rhtml3 ;
				
				
				
				//销售属性
				for ( var n = 0; n < SalepropertySpaceHtmlArr.length; n++) {
					var a = SalepropertySpaceHtmlArr[n];
					if(i18nOnOff){
						$j(".saleInfo[lang='"+a.lang+"']").find("#propertySpace").html(a.html);
						
						
						
						
					}else{
						$j(".saleInfo").find("#propertySpace").html(a.html);
					}
				}
				
				if(i18nOnOff){
					/*var isNeedDelSomeTable =true;
					$j.each(jsonArray,function(i,val){
						if(jsonArray[i].property.isSaleProp){
							//编辑类型 ：1 单行输入2可输入单选3单选4多选 5自定义多选
							if(jsonArray[i].property.editingType==5){
								isNeedDelSomeTable =false;
								return ;
							}
						}
					});*/
					
					
					//if(isNeedDelSomeTable){
						$j(".saleInfo").each(function(i,obj){
							var lang =$j(this).attr("lang");
							if(lang!=defaultlang){
								$j(this).find("#extensionTable").remove();
							}
						});
					//}
					
				}
				
				
//				$j(".saleInfo").find("#propertySpace").html(SalepropertySpaceHtml);
//				$j("#propertySpace").html(SalepropertySpaceHtml);
				$j("#notSalepropertySpace").html(notSalepropertySpaceHtml);
				$j("#propSelect").html(propSelectHtml);
				$j("#notSalepropertySpace").find("[loxiaType]").each(function(i,n){
					loxia.initLoxiaWidget(this);
				});
				
			
	  		 }
		}
		// nodes_.length>0  end 
		else{
			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("SYSTEM_ITEM_SELECT_INDUSTRY"));
		}
		//初始化id为colorPropertyContent的div内部的所有Loxia组件
		loxia.initContext($j(".ui-block "));
		
		$j(".spCkb").each(function(){
			var curCheckBox = $j(this);
			
			curCheckBox.change(function(){
				spChangedFlag = true;
				clickFlag = false;
			});
			
		});
		
		$j(".spTa").each(function(){
			var ta = $j(this);
			
			ta.bind("change",function(){
				spChangedFlag = true;
				clickFlag = false;
			});
		});
		
		$j(".normalCheckBoxCls").each(function(){
			
			var curCheckBox = $j(this);
			curCheckBox.change(function(){
				drawNoSalePropEditing4Type(num);				
			});
			
		});
	});
	
	//  ($j.button.orange.next").on("click"..  end 
	

	
	
	

    //上一步
    $j(".button.back").on("click",function(){
    	window.location.href=base+"/item/createItem.htm";
//    	$j("#first").css("display","block");
//		$j("#second").css("display","none");
//		//清空编码
//		$j("#extensionTable").html("");
//		//清空属性值
//		$j("#propertySpace").html("");
//		$j("#notSalepropertySpace").html("");
//		//清空jsonSku
//		$j("#jsonSku").val("");
    });
    //返回
	$j(".button.return").on("click",function(){
		window.location.href=base+"/item/itemList.htm";
	});

  //检查商品编码是否具有唯一性
	$j("#code").bind("blur",function()
	 {
		    var code = $j("#code").val();
	        if(code.trim()=="")return;
	        // 正则验证
	        if(pdValidCode != null && pdValidCode.length > 0){
	        	var re =new RegExp(pdValidCode);
				if(!re.test(code)){
 					$j("#code").val("");
 	 				$j("#loxiaTip-r").show();
 					$j(".codetip").html(nps.i18n("ITEM_CODE_VALID_FAIL",[code]));
 					$j("#code").addClass("ui-loxia-error");
 					return;
 				}else{
 					$j("#loxiaTip-r").show();
 	 				$j(".codetip").html(nps.i18n("ITEM_UPDATE_CODE_ABLE"));
 	 				setTimeout(function(){ 
 	 					$j("#loxiaTip-r").hide();
 	 				},2000);
 				}
 			}
 			var json={"code":code};
 		  	var _d = loxia.syncXhr(validateItemCodeUrl, json,{type: "GET"});
 			if(_d.isSuccess == false){
 				$j("#code").val("");
 				$j("#loxiaTip-r").show();
				$j(".codetip").html(nps.i18n("ITEM_UPDATE_CODE_ENBLE"));
				$j("#code").addClass("ui-loxia-error");
				return;
 			}else if(_d.isSuccess == true){
 				 $j("#loxiaTip-r").show();
 				 $j(".codetip").html(nps.i18n("ITEM_UPDATE_CODE_ABLE"));
 				 setTimeout(function(){ 
 					$j("#loxiaTip-r").hide();
 				 },2000); 
 				 return;
 			}
 		
	 });
   $j("#code").bind("focus",function()
			 {
			   $j("#loxiaTip-r").hide();
			 });
   
  //保存商品
	$j(".button.orange.submit").click(function(){
		// 商品图片
//	   var changePropertyJson = "";
//	   $j.each(restItemImagesArray(), function(index, itemImages){
//		   if(index == changedColorPropertyArray.length-1){
//			   changePropertyJson += itemImages;
//		   }else{
//			   changePropertyJson = itemImages + '|';
//		   }
//	   });
//		$j("#changePropertyJson").val(changePropertyJson);
	
		//验证默认分类
//	   var _defaultCategoryId = $j('input[name="defaultCategoryId"]').val();
//	   if(_defaultCategoryId == undefined){
//		   nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"), nps.i18n("PLEASE_SELECT_DEFAULT_CATEGORY"));
//		   return;
//	   }
	   
	  
	   
	   nps.submitForm('itemForm',{mode: 'async', 
			successHandler : function(data){
			if(data.isSuccess == true)
			{
				window.location.href=base+"/item/itemList.htm";
			}else
			{
				return nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("ADDITEM_FAIL"));
			}
	   }});
    });
	


    //图片预览
	$j("input[name='img1']").on("change",function(){
		
		//alert($j("input[name='img1']").files.item(0).getAsDataURL());
		//alert($j("input[name='img1']").files.item(0).getAsDataURL());
		//alert(document.getElementByTabName("img1").value);
	});
	
    //编码设置
	$j(".func-button.extension").on("click",function(){
//		produceExtension();
		spChangedFlag = false;
		var allSaleInfo = $j(".saleInfo");
		
		var flag = false;
		if(allSaleInfo.length>=2){
			var customerPropSize =$j(".saleInfo").eq(0).find(".customerSelect").length;
			for(var idx =0;idx < customerPropSize;idx++){
				var next = 0;
				allSaleInfo.each(function(i,dom){
					var me = $j(this);	
					var val = me.find(".customerSelect").eq(idx).val();
					if(i>0){
						if(val==""){
							flag = true;
						}
					}
					if(typeof(val)!="undefined" && val != null){
						var len = val.split("||").length;
						me.find(".propertyValueInputs").eq(idx).val(val);
						if(val!="||" && i>0){
							if(len != next){
								flag = true;
							}
						}else{
							next = len;
						}
					}
					
				});
				if(flag) break;
			}
		}
		
		var saleInfo = $j(this).parents(".saleInfo");
		if(flag){
			saleInfo.find("#exten").show();
			saleInfo.find(".extensionTable").html("多语言属性设置数量不一致");
			return;
		}
		showSkuTable(saleInfo);
	});
//
//	
//	//--------向上 
//	$j(".ui-block").on("click",".up-ic",function(e){
//		e.stopPropagation();
//		var thisprev=$j(this).parent().parent().parent(".color-select-line").prev(".color-select-line");
//		if(thisprev.length>0){
//			$j(this).parent().parent().parent(".color-select-line").detach().insertBefore(thisprev);
//			
//			changeColorProperty();
//		}
//	});
//	
//	//向下
//	$j(".ui-block").on("click",".down-ic",function(e){
//		e.stopPropagation();
//		var thisnext=$j(this).parent().parent().parent(".color-select-line").next(".color-select-line");
//		if(thisnext.length>0){
//			$j(this).parent().parent().parent(".color-select-line").detach().insertAfter(thisnext);
//			
//			changeColorProperty();
//		}
//	});
//	
//	//添加
//	$j(".ui-block-line").on("click",".color-select-add",function(){
//		//var str='<div class="ui-block-line color-select-line"><label><img src="../images/main/mrimg.jpg" class="color-select-img"/></label><div><div class="color-select-line"><input loxiaType="input"/><a class="func-button ml5 uploadlink" href="javascript:void(0);"><span>浏览</span></a><span class="common-ic up-ic"></span></div><div class="color-select-line"><input loxiaType="input" style="width:218px;"/><span class="common-ic down-ic"></span></div></div></div>';
//		var itemIdAndItemProperties = $j('#colorProperty').val();
//		var itemIdAndItemPropertiess = itemIdAndItemProperties.split('#');
//		var itemPropeties = "";
//		if(itemIdAndItemPropertiess.length > 0 && itemIdAndItemPropertiess != ''){
//			itemPropeties = itemIdAndItemPropertiess[1];
//		}else{
//			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("SELECT_COLOR_PROPERTY"));
//			return;
//		}
//		//验证是否有新建的, 且没有填写内容
//		var isEmpty = validatorOnlyOneNewIsEmpty();
//		if(isEmpty){
//			nps.info(nps.i18n("SYSTEM_ITEM_MESSAGE"),nps.i18n("WRITE_ALL_INFO"));
//			return;
//		}
//		
//		var str = '<div class="ui-block-line color-select-line">'
//				+'<label><img src="../images/main/mrimg.jpg" class="color-select-img"/></label>'
//				+'<input type="hidden" loxiaType="input" name="itemImages.itemProperties" value="'+itemProperties+'" />'
//				+'<div><div class="color-select-line">'
//				//+'<input complete="colorComplete" class="imgUploadComponet" role="'+thumbnailConfig+'" model="C" hName="itemImage'+hnameIndex+'" hValue="../images/main/mrimg.jpg" type="file" url="/demo/upload.json"/>'
//				+'<input loxiaType="input" readonly="true" name="itemImageUrl"/>'
//				+'<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span>浏览</span>'
//				+'<input complete="colorComplete" class="imgUploadComponet fileupload" role="'+thumbnailConfig+'" model="C" hName="itemImage'+hnameIndex+'" hValue="../images/main/mrimg.jpg" type="file" url="/demo/upload.json"/></a>'
//				+'<span class="common-ic up-ic"></span>'
//				+'<input type="hidden" name="itemImage'+hnameIndex+'" value="../images/main/mrimg.jpg"/>'
//				+'</div>'
//				+'<div class="color-select-line">'
//				+'<input loxiaType="input" name="itemImages.description" checkmaster="changeColorProperty" style="width:218px;"/><span class="common-ic down-ic"></span>'
//				+'</div>'
//				+'<div class="color-select-line">'
//                +'<span class="common-ic delete-ic"></span>'
//				+'</div></div></div>';
//		//$j(str).insertBefore($j(this).parent(".ui-block-line"));
//		var _colorPropertyContent = $j("#colorPropertyContent");
//		_colorPropertyContent.append(str);
////		loxia.init({debug: true, region: 'zh-CN'});
////		nps.init();
//		hnameIndex++;
//		//加载上传组件
//		$j.getScript(base+'/scripts/ajaxfileupload.js');
//		//初始化id为colorPropertyContent的div内部的所有Loxia组件
//		loxia.initContext($j("#colorPropertyContent"));
//		changeColorProperty();
//	});
//	//删除
//	$j(".ui-block").on("click",".color-select-line .delete-ic",function(e){
//		e.stopPropagation();
//		var thiscolor=$j(this).parent().parent().parent(".color-select-line");
//		thiscolor.remove();
//		changeColorProperty();
//	});
//	//属性--一级菜单
//	$j(".ui-block-line").on("change", "#propSelect", function(){
//		$j("#colorPropertyContent").empty();
//	});
//	//属性值--二级菜单
//	$j(".ui-block-line").on("change", "#colorProperty", function(){
//		// propertyValueIdAndProperyId的数据格式:  ${propertyValueId}#${ItemPproperties} 如:717#595
//		var itemIdAndItemPropertyId = $j(this).val();
//		var itemIdAndItemPropertyIds = itemIdAndItemPropertyId.split('#');
//		
//		var _colorPropertyContent = $j("#colorPropertyContent");
//		var propertyValueId = '';
//		if(itemIdAndItemPropertyIds.length > 0 && itemIdAndItemPropertyIds != ''){
//			propertyValueId = itemIdAndItemPropertyIds[0];
//			itemProperties = itemIdAndItemPropertyIds[1];
//			//判断数组中存在propertyValueId, 存在就遍历, 不存在就到数据库中读取
//			var isExist = false;//是否存在
//			$j.each(changedColorPropertyArray, function(index, itemImages){
//				_colorPropertyContent.empty();
//				var dataObj = $j.parseJSON(itemImages);
//				//多选
//				if(dataObj.propertyValueId != '' && dataObj.propertyValueId != 'undefined'){
//					if(dataObj.propertyValueId == propertyValueId){
//						//遍历 
//						$j.each(dataObj.itemImages, function(i, itemImage){
//							var _picUrl = itemImage.picUrl;
//							if(_picUrl == 'undefined'){
//								_picUrl = "../images/main/mrimg.jpg";
//							}
//							var str = '<div class="ui-block-line color-select-line">'
//								+'<label><img src="'+_picUrl+'" class="color-select-img"/></label>'
//								//+'<input type="hidden" loxiaType="input" name="itemImages.itemProperties" value="'+itemProperties+'" />'
//								+'<div>'
//								+'<div class="color-select-line">'
//								//+'<input complete="colorComplete" class="imgUploadComponet" role="'+thumbnailConfig+'" model="C" hName="itemImages'+hnameIndex+'" hValue="../images/main/mrimg.jpg"  type="file" url="/demo/upload.json"/>'
//								+'<input loxiaType="input" readonly="true" name="itemImageUrl" value="'+_picUrl+'"/>'
//								+'<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span>浏览</span>'
//								+'<input complete="colorComplete" class="imgUploadComponet fileupload" role="'+thumbnailConfig+'" model="C" hName="itemImage'+hnameIndex+'" hValue="../images/main/mrimg.jpg" type="file" url="/demo/upload.json"/></a>'
//								+'<span class="common-ic up-ic"></span>'
//					            +'</div>'
//					            +'<div class="color-select-line">'
//					            +'<input loxiaType="input" name="itemImages.description" checkmaster="changeColorProperty" style="width:218px;" value="'+itemImage.description+'"/>'
//					            +'<span class="common-ic down-ic"></span>'
//					            +'</div>'
//								+'<div class="color-select-line">'
//				                +'<span class="common-ic delete-ic"></span>'
//					            +'</div>'
//					            +'</div>'
//					            +'</div>';
//								//追加id="colorPropertyContent"的内容
//								_colorPropertyContent.append(str);
//								hnameIndex++;
//						});
//						isExist = true;
//						return false;
//					}
//				}else if(dataObj.itemProperties != '' && dataObj.itemProperties != 'undefined'){
//				//自定义多选
//					if(dataObj.itemProperties == itemProperties){
//						
//						//遍历 
//						$j.each(dataObj.itemImages, function(i, itemImage){
//							var _picUrl = itemImage.picUrl;
//							if(_picUrl == 'undefined'){
//								_picUrl = "../images/main/mrimg.jpg";
//							}
//							var str = '<div class="ui-block-line color-select-line">'
//								+'<label><img src="'+_picUrl+'" class="color-select-img"/></label>'
//								//+'<input type="hidden" loxiaType="input" name="itemImages.itemProperties" value="'+itemProperties+'" />'
//								+'<div>'
//								+'<div class="color-select-line">'
//								//+'<input complete="colorComplete" class="imgUploadComponet" role="'+thumbnailConfig+'" model="C" hName="itemImages'+hnameIndex+'" hValue="../images/main/mrimg.jpg"  type="file" url="/demo/upload.json"/>'
//								+'<input loxiaType="input" readonly="true" name="itemImageUrl" value="'+_picUrl+'"/>'
//								+'<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span>浏览</span>'
//								+'<input complete="colorComplete" class="imgUploadComponet fileupload" role="'+thumbnailConfig+'" model="C" hName="itemImage'+hnameIndex+'" hValue="../images/main/mrimg.jpg" type="file" url="/demo/upload.json"/></a>'
//								+'<span class="common-ic up-ic"></span>'
//					            +'</div>'
//					            +'<div class="color-select-line">'
//					            +'<input loxiaType="input" name="itemImages.description" checkmaster="changeColorProperty" style="width:218px;" value="'+itemImage.description+'"/>'
//					            +'<span class="common-ic down-ic"></span>'
//					            +'</div>'
//								+'<div class="color-select-line">'
//				                +'<span class="common-ic delete-ic"></span>'
//					            +'</div>'
//					            +'</div>'
//					            +'</div>';
//								//追加id="colorPropertyContent"的内容
//								_colorPropertyContent.append(str);
//								hnameIndex++;
//						});
//						isExist = true;
//						return false;
//					}
//				}
//			});
//			if(!isExist){
//				//清空id='colorPropertyContent'里的元素
//				_colorPropertyContent.empty();
//			}
//		}else{
//			//清空id='colorPropertyContent'里的元素
//			_colorPropertyContent.empty();			
//		}
//		//加载上传组件
//		$j.getScript(base+'/scripts/ajaxfileupload.js');
//		//初始化id为colorPropertyContent的div内部的所有Loxia组件
//		loxia.initContext($j("#colorPropertyContent"));
//	});
	
//	$j('.ui-block .customerSelect').live('blur', function(){
//		loadColorProp($j(this).val(), $j(this));
//	})
	if(i18nOnOff){
		var i18nSize = i18nLangs.length;
		editors=[];
		for ( var i = 0; i < i18nSize; i++) {
		  var editor = CKEDITOR.replace( 'editor'+i,
				{
					toolbar : 'Full' ,
					filebrowserImageUploadUrl:'/img/upload.json'
				});
		  editors.push(editor);
		}
	}else{
		 editors=[];
		 var editor = CKEDITOR.replace( 'editor',
					{
						toolbar : 'Full' ,
						filebrowserImageUploadUrl:'/img/upload.json'
					});
		 editors.push(editor);
	}
	if(i18nOnOff){
		$j(".saleInfo").each(function(){
			var me = $j(this);
			var lang = me.attr("lang");
			if(lang != defaultlang){
				me.find(".exten").remove();
				me.find(".func-button.extension").parent().parent().remove();
			}
		});
	}
	

	
	
	
	
});