$j.extend(loxia.regional['zh-CN'],{  
	    "LABEL_NAME":"选项名称",
	    "LABEL_TYPE":"类型", 
	    "LABEL_PROPERTY_VALUE_NAME":"关联属性可选值",	   
	    "LABEL_SORT":"顺序",
	    "LABEL_STATE":"状态",
	    "LABEL_CONFIG":"操作",
	    "TO_UPDATE":"修改",
	    "TO_ENABLE":"启用",
	    "TO_DELETE":"删除",
	    "TO_SETTING":"设置选项",
	    "PRODUCT_INFO_CONFIRM":"确认信息",
	    "PRODUCT_CONFIRM_DISABLE_USER":"确认使选项失效",
	    "PRODUCT_CONFIRM_ENABLE_USER":"确认启用选项",  
	    "INFO_TITLE_DATA":"提示信息",
	    "INFO_ENABLE_SUCCESS":"启用成功",
	    "INFO_DISABLE_SUCCESS":"失效成功", 
	    "INFO_ENABLE_FAIL":"启用失败",
	    "INFO_DISABLE_FAIL":"失效失败",
	    "USER_FORM_CHECK_ERROR":"错误信息",
	    "NO_CATEGORY":"无",  
	    "NO_DATA":"无数据",
	    "DRAW_FIND":"查看",
	    "DRAW_NO":"失效",
	    "INFO_COMBO_INEXISTED":"该筛选器不存在" ,
	 	"INFO_SYSTEM_ERROR":"系统错误" ,
	 	"LABEL_MIN" :"最小值",
	 	"LABEL_MAX" :"最大值",
		"PRODUCT_CONFIRM_DELETE":"确认删除",
		"INFO_DELETE_SUCCESS":"删除成功",
		"SORT_NO":"排序编号"
});

//商品组合查询
var comboProductListUrl = base+'/product/customProductComboList.json'; 

var endisableSearchConditionUrl=base+"/item/itemSearchCondition/endisableSearchConditionItem.json";
var searchConditionUrl=base+'/item/itemSearchCondition/searchConditionItemList.json';
var comboProductActionURl=base+'/product/productcomboedit.htm';

var removeSearchConditionUrl=base+'/item/itemSearchCondition/removeSearchConditionItem.json';
var removeSearchConditionsUrl=base+'/item/itemSearchCondition/removeSearchConditionItemByIds.json';

var conditionUrl="/item/itemSearchCondition/manager.htm";
var addConditionUrl="/i18n/item/itemSearchCondition/managerAddConditionItem.htm";
var settingConditionUrl="/item/itemSearchCondition/managerSetting.htm";
//查看
var VIEW_URL=base+'/product/combo/view.json';
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
//鼠标移动隐藏时间
function categoryHideMenu() {
	$j("#categoryMenuContent").fadeOut("fast");
	$j("body").unbind("mousedown", categoryOnBodyDown);
}
function drawEditor(data, args, idx){
	var result="";  
	var id=loxia.getObject("id", data);
	 var state=loxia.getObject("lifecycle", data);
	 var condid=$j("#pid").val();
	 var modify="<a href='"+ addConditionUrl+"?id="+id+"&pid="+condid+"' class='func-button modify'>"+nps.i18n("TO_UPDATE")+"</a>";
	 var to_delete="<a href='javascript:void(0);' data='" + id +"' class='func-button delete'>"+nps.i18n("TO_DELETE")+"</a>";
	 //var to_setting="<a href='"+settingConditionUrl+"' data='" + id +"' class='func-button setting'>"+nps.i18n("TO_SETTING")+"</a>";
	 if(state==0){
		  result+=modify+to_delete+"<a href='javascript:void(0);' val='"+id+"' class='func-button enable'>"+nps.i18n("TO_ENABLE")+"</a>";
	  }else if(state=1){ 
		  result+=modify+to_delete+"<a href='javascript:void(0);' val='"+id+"' class='func-button disable'>"+nps.i18n("DRAW_NO")+"</a>";
	 } 
	return result;
} 
function categoryOnBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "categoryName" || event.target.id == "categoryMenuContent" || $j(event.target).parents("#categoryMenuContent").length>0)) {
		categoryHideMenu();
	}
} 
function drawExpressionText(data, args, idx){	 
	var expressionText = loxia.getObject("groupExpressionText", data);
	var text =""; 
	if(expressionText==null)
	return text;
	 
	var moveText="";
	if(expressionText.indexOf("【排除】")!=-1){ 
	  expressionText = expressionText.replace("【排除】","<br/>【排除】"); 
	}
	 moveText=expressionText;
	if(expressionText.length>50){
		text +=expressionText.substring(0,50)+"......";
	}else{
		text +=expressionText;
	}  
	text= "<label class='movetitletd'>"+ 
		"<div custtalk='"+moveText+"' class='movetitle'>" 
		+text+
     "</div> </label>";
	
	return text;
}

 
//刷新数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}
function drawCheckbox(data, args, idx){
	var result=""; 
	var state=loxia.getObject("lifecycle", data);
	if(state==1 ){
		return "<input name='limitValue' type='checkbox' class='limitValue'  checked='false'  value='" + loxia.getObject("limitValue", data)+"' disabled='disabled'/>";
	}
	return "<input name='limitValue' type='checkbox' class='limitValue'   value='" + loxia.getObject("limitValue", data)+"' disabled='disabled'/>";
	 
}

function drawValueName(data, args, idx){
	var result=""; 
	//var type=loxia.getObject("type", data);
	var name=loxia.getObject("propertyName", data);
	var valueName=loxia.getObject("propertyValueName", data);
	if(valueName!=null&&valueName!=""){
		return name+" > "+valueName;
	}
	return "";
}
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
	var cityObj = $j("#categoryName"); 
	cityObj.attr("value", v);
	$j("#categoryId").attr("value",id);
	categoryHideMenu();
}

//获取日期格式
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
} 

function checkboxs(data, args, idx){
	return "<input type='checkbox' name='id' class='chkcid'  value='" + loxia.getObject("id", data)+"'/>";
}

function confirmDelete(){
	var checkbox=$j("input[name='id']");
	var data=""; 
	  $j.each(checkbox, function(i,val){   
		  if(val.checked){
			  data=data+$j(this).val()+",";
		  }
	 }); 
	  if(data!=""){
		  data=data.substr(0,data.length-1);
		  
		
			nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_DELETE"),function(){
				
					var json={"ids":data}; 
				  	 nps.asyncXhrPost(removeSearchConditionsUrl, json,{successHandler:function(data, textStatus){
						var backWarnEntity = data;
		  				if (backWarnEntity.isSuccess) {
		  					nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
		  					refreshData();
		  				} else {
		  					nps.info(nps.i18n("INFO_TITLE_DATA"),backWarnEntity.description);
		  				}
					 }});
			});
	
	  }  
}

$j(document).ready(function() {
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();

	searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});

	//通过loxiasimpletable动态获取数据
	$j("#table1").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form : "searchForm",
		cols : [
		{label:"<input type='checkbox'/>",width:"5%", template:"checkboxs"},
		{
			name : "sort",
			label : nps.i18n("SORT_NO"),
			width : "10%",
			sort: ["t1.sort asc","t1.sort desc"]
		}, {
			name : "name",
			label : nps.i18n("LABEL_NAME"),
			width : "15%"
		}, {
			name : "propertyValueName",
			label : nps.i18n("LABEL_PROPERTY_VALUE_NAME"),
			width : "15%" ,
			template:"drawValueName"
		}, {
			name : "areaMin",
			label : nps.i18n("LABEL_MIN"),
			width : "10%" 
		},  {
			name : "areaMax",
			label : nps.i18n("LABEL_MAX"),
			width : "10%"
		}, {
			name : "lifecycle",
			label : nps.i18n("LABEL_STATE"),
			width : "10%",
			type:"yesno"
		},{
			label : nps.i18n("LABEL_CONFIG"),
			width : "10%", 			 
			template : "drawEditor"
		}
		
		],
		dataurl : searchConditionUrl
	});
	refreshData();
	 
	$j("#industry").click(function(){
		$j("#view-block-item").dialogff({type:'open',close:'in',width:'1000px',height:'550px'});
	});
	
	//筛选数据
	 $j(".func-button.search").click(function(){
			 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
			 refreshData();
	   }); 
	
 	 $j(".button.orange.addCondition").click(function(){
	        window.location.href=addConditionUrl+"?pid="+$j(this).attr("codid");
	 });
 	 
	 	$j(".button.orange.backCondition").click(function(){
	        window.location.href=conditionUrl;
	 	});
	 //分类
		$j.fn.zTree.init($j("#categoryDemo"), categorySetting, category_ZNodes);    
		
		$j("#categoryName").click(function() {
			var cityObj = $j(this);
			var cityOffset = $j(this).offset();
			$j("#categoryMenuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

			$j("body").bind("mousedown", categoryOnBodyDown);
		});
		
		  
		   // 禁用单行
		   $j("#table1").on("click",".func-button.disable",function(){
		        var curObject=$j(this);
		        nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_DISABLE_USER"), function(){

		            var json={"id":curObject.attr("val"),"activeMark":0};

		        	var _d = nps.syncXhr(endisableSearchConditionUrl, json,{type: "POST"});
		        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DISABLE_SUCCESS")); 
		        	refreshData(); 
		            	
		        });
		    });

		    
	  // 启用单行
		   $j("#table1").on("click",".func-button.enable",function(){
		        var curObject=$j(this);
		        nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_ENABLE_USER"), function(){

		            var json={"id":curObject.attr("val"),"activeMark":1};

		        	var _d = nps.syncXhr(endisableSearchConditionUrl, json,{type: "POST"});
		        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_ENABLE_SUCCESS")); 
		        	refreshData(); 
		        	
		            	
		        });
		    });
		   
		 //删除一行
		   $j("#table1").on("click",".func-button.delete",function(){
		        var curObject=$j(this);
		        nps.confirm(nps.i18n("PRODUCT_INFO_CONFIRM"),nps.i18n("PRODUCT_CONFIRM_DELETE"), function(){

		            var json={"id":curObject.attr("data")};

		        	nps.syncXhr(removeSearchConditionUrl, json,{type: "POST"});
		        	nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS")); 
		        	refreshData(); 
		            	
		        });
		    });
		   
		   //多行删除
		   $j("#deleteAll").click(function(){
			   confirmDelete();
		    });
		   
		   refreshData();  
		   //-------------------------------------------------移动弹出表达式名称--------------------------------
			var ssetim=0;
			$j("<div class='moveoutdialog' style='display:none; clear:both; position:absolute; width:500px; height:auto; padding:20px; background:#fff; border:1px solid #888888; line-height:1.5; z-index:50; border-radius:5px;'></div>").appendTo("body").mouseenter(function(){
				clearTimeout(ssetim);
			}).mouseleave(function(){
				ssetim=setTimeout(function(){$j(".moveoutdialog").stop(true,true).fadeOut(100);},500);
			});
		
			$j(".ui-block").on("mouseenter",".movetitle",function(e){
				e.stopPropagation();
				clearTimeout(ssetim);
				var ex=parseInt($j(this).parent("label").offset().left);
				var ey=parseInt($j(this).parent("label").offset().top);
				
				var thiscust=$j(this).attr("custtalk"); 
				$j(".moveoutdialog").stop(true,true).fadeOut(100).html("").append($j(this).attr("custtalk"));

				$j(".moveoutdialog").css({"left":ex,"top":ey+60}).stop(true,true).fadeIn(300);
			}).on("mouseleave",".movetitle",function(e){
				ssetim=setTimeout(function(){$j(".moveoutdialog").stop(true,true).fadeOut(100);},500);
			});
			
			
			
			//查看	
			$j(".content-box").on("click", ".view", function() {
				
				var id = $j(this).attr("data");
				var data = nps.syncXhrPost(VIEW_URL, {id:id});
				if (data.isSuccess) {
					var details = data.details;
					var combo = data.combo;
					var type = combo.comboType;
					var name = combo.comboName;
					var typeName = "";
					if (type == 1) {
						$j("#view-block-item .include-list tbody").empty();
						typeName = "商品";
						$j("#view-block-item table").find("th[id='item-price']").show();
						$j("#view-block-item .txt-type").val(typeName);
						$j("#view-block-item .txt-name").val(name);
						
						$j.each(details.itemList, function(i, obj) {
							var html = "<tr class='" + ((i%2==0)?"even":"odd") + "'><td>" 
							+ obj.title + "</td>";
							html +="<td>" + obj.salePrice + "</td>";
							html +="</tr>";
							$j("#view-block-item .include-list tbody").append(html);
						});
						
						$j("#view-block-item").dialogff({type:'open',close:'in',width:'1000px',height:'550px'});
					
					} else if (type == 2) {
						$j("#view-block-category .include-list tbody").empty();
						$j("#view-block-category .exclude-list tbody").empty();
						typeName = "分类";
						$j("#view-block-category table").find("th[id='item-price']").hide();
						$j("#view-block-category table").find("th[id='exc-item-price']").show();
						$j("#view-block-category .txt-type").val(typeName);
						$j("#view-block-category .txt-name").val(name);
						
						$j.each(details.categoryList, function(i, obj) {
							if (obj.id == 0) {	//全场
								obj.name = "全场";
							}
							var html = "<tr class='" + ((i%2==0)?"even":"odd") + "'><td>" 
							+ obj.name + "</td>";
							html +="</tr>";
							$j("#view-block-category .include-list tbody").append(html);
						});
						
						if (details.itemList) {
							$j.each(details.itemList, function(i, obj) {
								var html = "<tr class='" + ((i%2==0)?"even":"odd") + "'><td>" 
									+ obj.title + "</td>";
								if(type == 2){
									html +="<td>" + obj.salePrice + "</td>";
								}
								html +="</tr>";
								$j("#view-block-category .exclude-list tbody").append(html);
							});
						}
						
						$j("#view-block-category").dialogff({type:'open',close:'in',width:'1000px',height:'550px'});
					
					} else if (type == 4) {
						$j("#view-block-all .include-list tbody").empty();
						typeName = "自定义";
						$j("#view-block-all table").find("th[id='item-price']").hide();
						$j("#view-block-all .txt-type").val(typeName);
						$j("#view-block-all .txt-name").val(name);
						
						$j.each(data.detailsList, function(i, details) {
							var html = "<tr class='" + ((i%2==0)?"even":"odd") + "'><td>" 
							+ details.name + "</td>";
							html +="</tr>";
							$j("#view-block-all .include-list tbody").append(html);
						});
						
						$j("#view-block-all").dialogff({type:'open',close:'in',width:'1000px',height:'550px'});
					}
				} else {
					if (data.errorCode == 7003) {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_COMBO_INEXISTED"));
					} else {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_SYSTEM_ERROR"));
					}
				}
			});
			
			//浮层确定
			$j("#view-block-item .btn-ok").click(function() {
				$j("#view-block-item").dialogff({type : 'close'});
			});
			$j("#view-block-category .btn-ok").click(function() {
				$j("#view-block-category").dialogff({type : 'close'});
			});
			$j("#view-block-all .btn-ok").click(function() {
				$j("#view-block-all").dialogff({type : 'close'});
			});

});

