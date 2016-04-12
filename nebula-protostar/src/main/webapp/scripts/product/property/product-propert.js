$j.extend(loxia.regional['zh-CN'],{
	"PROPERT_OPERATOR_TIP":"属性提示信息",
    "PROPERT_CONFIRM_DELETE_SEL_PROPERT":"确定要删除选定的属性么？",
    "PROPERT_STATE_ENABLE":"有效",
    "PROPERT_STATE_DISABLE":"无效",
    "PROPERT_CONFIRM_ENABLE":"确定要启用该属性么？",
    "PROPERT_CONFIRM_DISABLE":"确定要禁用该属性么？",
    "NO_DATA":"未找到数据",
    "VALUE_NO_EFFECT":"无效",
    "VALUE_EFFECTIVE":"有效",
    "VALUE_INDUSTRY_PROPERTY":"行业属性",
    "VALUE_SHOP_PROPERTY":"店铺自定义属性",
    "VALUE_EDIT_DH":"单行输入",
    "VALUE_EDIT_KSRDX":"可输入单选",
    "VALUE_EDIT_ZDYDX":"自定义多选", 
    "VALUE_EDIT_DANX":"单选",
    "VALUE_EDIT_DX":"多选",
    "VALUE_TYPE_WB":"文本",
    "VALUE_TYPE_SZ":"数值",
    "VALUE_TYPE_RQ":"日期",
    "VALUE_TYPE_RQSJ":"日期时间",
    "VALUE_ISSELECT_NO":"否",
    "VALUE_ISSELECT_YES":"是",
    "PROPERTY_CLICK_EDIT":"修改",
    "PROPERTY_CLICK_START":"启用",
    "PROPERTY_CLICK_STOP":"禁用",
    "PROPERTY_CLICK_DELETE":"删除",
    "PROPERTY_CLICK_SETVALUE":"设置属性可选值",
    "INFO_DELETE_SUCCESS":"删除记录成功!",
    "INFO_DELETE_FAIL":"删除记录失败!",
    "INFO_START_SUCCESS":"启用成功!",
    "INFO_START_FAIL":"启用失败!",
    "INFO_STOP_SUCCESS":"禁用成功!",
    "INFO_TITLE_DATA":"提示信息",
    "INFO_STOP_FAIL":"禁用失败!",
    "LABEL_PROPERTY_NAME":"属性名称",
    "LABEL_PROPERTY_EDITTYPE":"编辑类型",
    "LABEL_PROPERTY_VALUETYPE":"值类型",
    "LABEL_PROPERTY_SALEPROP":"销售属性",
    "LABEL_PROPERTY_COLORPROP":"颜色属性",
    "LABEL_PROPERTY_REQUIRED":"必输属性",
    "LABEL_PROPERTY_SEARCHABLE":"是否检索",
    "LABEL_PROPERTY_HASTHUMB":"是否配图",
    "LABEL_PROPERTY_SORTNO":"顺序",
    "LABEL_PROPERTY_INDUSTRYNAME":"所属行业",
    "LABEL_PROPERTY_LIFECYCLE":"状态",
    "LABEL_PROPERTY_OPERATE":"操作",
    "LABEL_PROPERTY_GROUP":"所属分组"
});
//商品属性列表
var propertyListUrl = base+'/property/propertyList.json';
// 删除
var removepropertyUrl = base+'/property/removeProperty.json';
// 启用或禁用
var enableOrDisablePropertyUrl = base+'/property/enableOrDisableproperty.json';

/**
 * 状态格式
 * @param val
 * @returns {String}
 */
/*function formatGender(val) {
	if (val == 0)
		return nps.i18n("VALUE_NO_EFFECT");
	else
		return nps.i18n("VALUE_EFFECTIVE");
}*/
/**
 * 行业属性格式
 * @param val
 * @returns {String}
 */
function formatType(val) {
	if (val == 1)
		return nps.i18n("VALUE_INDUSTRY_PROPERTY");
	else
		return nps.i18n("VALUE_SHOP_PROPERTY");
}
/**
 * 编辑类型格式
 * @param val
 * @returns {String}
 */
function formatEditingType(val) {
	if (val == 1) {
		return nps.i18n("VALUE_EDIT_DH");
	} else if (val == 2) {
		return nps.i18n("VALUE_EDIT_KSRDX");
	} else if (val == 3) {
		return nps.i18n("VALUE_EDIT_DANX");
	} else if(val == 4){
		return nps.i18n("VALUE_EDIT_DX");
	}else {
		return nps.i18n("VALUE_EDIT_ZDYDX");
	}
}


/**
 * 字符长度大于8 就截取掉用...  代替后面的字符
 * @param data
 * @param args
 * @param idx
 * @returns
 */
function formatNameLength(data, args, idx){
	var propertyName=loxia.getObject("name", data);
	if(propertyName==null){
		return  "";
	}
	if(propertyName.length>8){
		propertyName=propertyName.substring(0,8);
		propertyName+="...";
	}
	return  propertyName;
}

/**
 * checkbox
 * @param data
 * @param args
 * @param idx
 * @returns {String}
 */
function drawCheckbox(data, args, idx) {
	return "<input type='checkbox' name='id' value='"
			+ loxia.getObject("id", data) + "'/>";
}
/**
 * 值类型 格式
 * @param val
 * @returns {String}
 */
function formatValueType(val) {
	if (val == 1) {
		return nps.i18n("VALUE_TYPE_WB");
	} else if (val == 2) {
		return nps.i18n("VALUE_TYPE_SZ");
	} else if (val == 3) {
		return nps.i18n("VALUE_TYPE_RQ");
	} else {
		return nps.i18n("VALUE_TYPE_RQSJ");
	}
}
/**
 * 格式化各个字段销售属性/颜色属性/必输属性/是否检索/是否配图 等字段
 * @param val
 * @returns {String}
 */
function formatWhetherOrNot(val) {
	if (val == 1) {
		return nps.i18n("VALUE_ISSELECT_YES");
	} else {
		return nps.i18n("VALUE_ISSELECT_NO");
	}
}




// 刷新表格数据
function refreshData() {
	$j("#table1").loxiasimpletable("refresh");
}



//				下拉列表
var setting = {
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
			onClick: onClick,
			onCheck: onCheck
		}
	};


	function onClick(e, treeId, treeNode) {
		var zTree = $j.fn.zTree.getZTreeObj("treeDemo");
		zTree.checkNode(treeNode, !treeNode.checked, null, true);
		return false;
	}

	function onCheck(e, treeId, treeNode) {
		var zTree = $j.fn.zTree.getZTreeObj("treeDemo"),
		nodes = zTree.getCheckedNodes(true),
		v = "";
		id="";
		for (var i=0, l=nodes.length; i<l; i++) {
			v += nodes[i].name + ",";
			id+= nodes[i].id + ",";
		}
		if (v.length > 0 ) v = v.substring(0, v.length-1);
		if (id.length > 0 ) id = id.substring(0, id.length-1); 
		var cityObj = $j("#industryName"); 
		cityObj.attr("value", v);
		$j("#industryId").attr("value",id);
		hideMenu();
	}

	
	function hideMenu() {
		$j("#menuContent").fadeOut("fast");
		$j("body").unbind("mousedown", onBodyDown);
	}
	function onBodyDown(event) {
		if (!(event.target.id == "menuBtn" || event.target.id == "industryName" || event.target.id == "menuContent" || $j(event.target).parents("#menuContent").length>0)) {
			hideMenu();
		}
	}

	

	/**
	 * 批量删除属性
	 */
	function confirmDelete(){
		nps.confirm(nps.i18n("PROPERT_OPERATOR_TIP"),nps.i18n("PROPERT_CONFIRM_DELETE_SEL_PROPERT"),function(){
			var checkbox=$j("input[name='id']");
			var data=""; 
			  $j.each(checkbox, function(i,val){   
				  if(val.checked){
					  data=data+$j(this).val()+",";
				  }
			 }); 
			  if(data!=""){
				  data=data.substr(0,data.length-1);
			  }  
				var json={"propertyIds":data}; 
			  	 nps.asyncXhrPost(removepropertyUrl, json,{successHandler:function(data, textStatus){
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

	/**
	 * 删除属性
	 * @param val
	 */
	function deleteByid(val){
		nps.confirm(nps.i18n("PROPERT_OPERATOR_TIP"),nps.i18n("PROPERT_CONFIRM_DELETE_SEL_PROPERT"),function(){
			var json={"propertyIds":val};
		  	nps.asyncXhrGet(removepropertyUrl, json,{successHandler:function(data, textStatus){
//		  		if(data.success!=0){
//			  		nps.info(nps.i18n("INFO_DELETE_SUCCESS"));
//			  	}else{
//			  		nps.info(nps.i18n("INFO_DELETE_FAIL"));
//			  	} 
		  		var backWarnEntity = data;
					if (backWarnEntity.isSuccess) {
						nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_DELETE_SUCCESS"));
						refreshData();
					} else {
						nps.info(nps.i18n("INFO_TITLE_DATA"),backWarnEntity.description);
					}
		  		refreshData();
		  	}});
		});
	}

	/**
	 * 启用禁用
	 * @param val
	 * @param state
	 */
	function enableOrDisable(val,state){
		var info="";
		//0==禁用， 1=启用
		if(state!=1){
			info=nps.i18n("PROPERT_CONFIRM_DISABLE");
		}else{
			info=nps.i18n("PROPERT_CONFIRM_ENABLE");
		}
		nps.confirm(nps.i18n("PROPERT_OPERATOR_TIP"),info,function(){
			var json={"propertyId":val,"state":state};
		  	nps.asyncXhrGet(enableOrDisablePropertyUrl, json,{successHandler:function(data, textStatus){
		  		var backWarnEntity = data;
		  		if(backWarnEntity.isSuccess){
			  		if(state!=1){
			  			//禁用成功
			  			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_STOP_SUCCESS"));
			  		}else{
			  			//启用成功
			  			nps.info(nps.i18n("INFO_TITLE_DATA"),nps.i18n("INFO_START_SUCCESS"));
			  		}
			  	} 
		  		refreshData();
		  	}}); 
		});
	}
	
	/**
	 * 格式化操作 字段
	 * @param data
	 * @param args
	 * @param idx
	 * @returns {String}
	 */
	function propertyValue(data) {
		var list = '';
		if(data.editingType == 5 || data.editingType == 1){
			list = [{label:nps.i18n("PROPERTY_CLICK_EDIT"), type:"href", content:base+'/property/updateProperty.htm?industryId='+data.industryId+'&properyId='+data.id},
                    {label:nps.i18n("PROPERTY_CLICK_STOP"), type:"jsfunc", content:"disableProp"},
                    {label:nps.i18n("PROPERTY_CLICK_DELETE"), type:"jsfunc", content:"deleteProp"}];
		}else{
			list = [{label:nps.i18n("PROPERTY_CLICK_EDIT"), type:"href", content:base+'/property/updateProperty.htm?industryId='+data.industryId+'&properyId='+data.id},
                    {label:nps.i18n("PROPERTY_CLICK_STOP"), type:"jsfunc", content:"disableProp"},
                    {label:nps.i18n("PROPERTY_CLICK_DELETE"), type:"jsfunc", content:"deleteProp"},
                    {label:nps.i18n("PROPERTY_CLICK_SETVALUE"), type:"href", content:base+"/i18n/property/editPropertyValue.htm?propertyId="+data.id}];
		}
        if(data.lifecycle == 0){
            list[1].label = nps.i18n("PROPERTY_CLICK_START");
            list[1].content = "enableProp";
        }
        return list;
	} 
	 
	//启用
	function enableProp(data,args,caller){
		enableOrDisable(data.id,1);
	}
	
	//禁用
	function disableProp(data,args,caller){
		enableOrDisable(data.id,0);
	}
	
	//删除
	function deleteProp(data,args,caller){
  	   	 
		deleteByid(data.id);
    
	}

	

	/**
	 * 查询属性列表
	 */
	$j(document).ready(function() {
		loxia.init({
			debug : true,
			region : 'zh-CN'
		});
		
		searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});
		$j("#table1").loxiasimpletable({
			page : true,
			size : 15,
			nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
			form:"searchForm",
			cols : [ {
				label : "<input type='checkbox'  />",
				width : "5%",
				template : "drawCheckbox"
			}, {
				name : "name",
				label : nps.i18n("LABEL_PROPERTY_NAME"),
				width : "15%",
				sort: ["tpp.name asc","tpp.name desc"],
				template : "formatNameLength"
			}, {
				name : "editingType",
				label : nps.i18n("LABEL_PROPERTY_EDITTYPE"),
				width : "9%",
				formatter : "formatEditingType"
			}, {
				name : "valueType",
				label : nps.i18n("LABEL_PROPERTY_VALUETYPE"),
				width : "6%",
				formatter : "formatValueType"
			}, {
				name : "isSaleProp",
				label : nps.i18n("LABEL_PROPERTY_SALEPROP"),
				width : "5%",
				type:"yesno"
			}, {
				name : "isColorProp",
				label : nps.i18n("LABEL_PROPERTY_COLORPROP"),
				width : "5%",
				type:"yesno"
			}, {
				name : "required",
				label : nps.i18n("LABEL_PROPERTY_REQUIRED"),
				width : "5%",
				type:"yesno"
			}, {
				name : "searchable",
				label : nps.i18n("LABEL_PROPERTY_SEARCHABLE"),
				width : "5%",
				type:"yesno"
			},  {
				name : "sortNo",
				label : nps.i18n("LABEL_PROPERTY_SORTNO"),
				sort: ["tpp.sort_no asc","tpp.sort_no desc"],
				width : "5%"
			}, {
				name : "industryName",
				label : nps.i18n("LABEL_PROPERTY_INDUSTRYNAME"),
				sort: ["industryName asc","industryName desc"],
				width : "10%"
			}, {
				name : "groupName",
				label : nps.i18n("LABEL_PROPERTY_GROUP"),
				sort: ["groupName asc","groupName desc"],
				width : "10%"
			}, {
				name : "lifecycle",
				label : nps.i18n("LABEL_PROPERTY_LIFECYCLE"),
				width : "5%",
				type:"yesno"
			}, {
				label : nps.i18n("LABEL_PROPERTY_OPERATE"),
				width : "20%",
				type:"oplist", 
				oplist:propertyValue
			} ],
			dataurl : propertyListUrl
		});
		refreshData();
		

		
		
		
		// 筛选列表
		$j(".func-button.search").click(function() {
			$j("#table1").data().uiLoxiasimpletable.options.currentPage = 1;
			refreshData();
		});
		 
		$j.fn.zTree.init($j("#treeDemo"), setting, zNodes);
		
		var treeObjIndustry = $j.fn.zTree.getZTreeObj("treeDemo");
		var treeIndustryNodes = treeObjIndustry.transformToArray(treeObjIndustry.getNodes());
		for(var i = 0;i<treeIndustryNodes.length;i++){
			if(treeIndustryNodes[i].isParent){
				treeIndustryNodes[i].nocheck = true;
				treeObjIndustry.refresh();
			}
		}
		
		
		var q_long_industryId = $j("input[name='q_long_industryId']").val().trim();
		
		
		if(q_long_industryId!=null&&q_long_industryId!=''){
			
			var industry_TreeObj=$j.fn.zTree.getZTreeObj("treeDemo");
			
			var industry_Nodes=industry_TreeObj.transformToArray(industry_TreeObj.getNodes());
			
			for(var i = 0 ; i < industry_Nodes.length ; i++){
				if(industry_Nodes[i].id==q_long_industryId){
					
					industry_Nodes[i].checked = true;
					
					$j("#industryName").val(industry_Nodes[i].name);
					
					break;
				}
			}
			industry_TreeObj.refresh();
		}
		
		$j("#industryName").click(function() {
			var cityObj = $j(this);
			var cityOffset = $j(this).offset();
			$j("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

			$j("body").bind("mousedown", onBodyDown);
		});
		$j(".button.orange.addpro").click(function() {
			window.location.href=base+"/property/createProperty.htm";
		});
		
		$j(".button.delete").click(function() {
			confirmDelete();
		});
		
	});




