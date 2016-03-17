$j.extend(loxia.regional['zh-CN'],{
 	"VALUE_EDIT_DH":"单行输入",
    "VALUE_EDIT_KSRDX":"可输入单选",
    "VALUE_EDIT_DANX":"单选",
    "VALUE_EDIT_DX":"多选",   
	"SYSTEM_PROPERTY_MESSAGE":"提示信息",
	"SYSTEM_PROPERTY_DELETE_SUCCRSS":"删除成功",
	"SYSTEM_PROPERTY_DISABLE":"禁用属性不能设置属性可选值",
	"SYSTEM_PROPERTY_SELECT":"请选择一条属性",
	"SYSTEM_PROPERTY_INDUSTY_LIST":"行业属性列表",
	"SYSTEM_PROPERTY_SELECT_INDUSTRY":"请选择要修改的节点",
	"SYSTEM_PROPERTY_EXIST":"该属性已经存在",
	"SYSTEM_PROPERTY_SUCCESS":"操作成功",
	"SYSTEM_PROPERTY_NAME":"请填写属性名称",
	"SYSTEM_PROPERTY_DISABLE_SUCCRSS":"禁用成功",
	"SYSTEM_PROPERTY_INSERT_SUCCRSS":"新增成功",
	"SYSTEM_PROPERTY_SORT_SUCCRSS":"更新排序成功",
	"SYSTEM_PROPERTY_ERROR":"系统异常",
	"SYSTEM_PROPERTY_SORT_ERROR":"当前有新增并且未定义的属性",
	"SYSTEM_PROPERTY_NO_SORT":"当前没有属性",
	"SYSTEM_PROPERTY_NO_SAVE":"未定义属性是否保存",
	"SYSTEM_UNKNOW_PROPERYY":"未定义属性不能设置属性可选值"
});

/**
 * 左侧拖拽条单击时触发setValue方法
 * @param o (callback回调的json数据)
 * @returns {String}
 */
function setValue(o){
	loxia.byId($j("input[name=name]")).state(null);
	if(o){
		if(o.state==0){
			$j(".button-line .button.orange.add:eq(0)").css("display","inline-block");
			$j(".button-line .button.orange.add:not(:eq(0))").css("display","none");	
		}else{
			$j(".button-line .button.orange.add:lt(2)").css("display","none");
			$j(".button-line .button.orange.add:gt(1)").css("display","inline-block");
		}
		$j("input[name='name']").val(o.name);
		$j("#editingType").attr("value",o.editingType);
		$j("#valueType").attr("value",o.valueType);
		$j("#isSaleProp").attr("value",o.isSaleProp);
		$j("#isColorProp").attr("value",o.isColorProp);
		$j("#required").attr("value",o.required);
		$j("#searchable").attr("value",o.searchable);
		$j("#hasThumb").attr("value",o.hasThumb);
		$j("#propertyId").val(o.id);
		$j("#sortNo").val(o.num);
	}else{
		if( $j(".ui-sortable li").hasClass('selected')){
		$j(".button-line .button.orange.add:eq(1)").css("display","inline-block");
		$j(".button-line .button.orange.add:not(:eq(1))").css("display","none");
		}else{
			$j(".button-line .button.orange.add").css("display","none");
		}
		$j("#propertyId").val("");
		refreshRight();
	}
	
	
}

function beforeValue(){
	nps.confirm(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NO_SAVE"), function(){
		
		
		var sortNo = $j(".list-all.ui-sortable li").length;
		var name =  $j("input[name='name']").val();
		var industryId = $j("#industryId").val()
		
		if(name!=""&& industryId!="" && sortNo!=""){	
		

			if(!validatePropertyName(industryId,name)){
			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_EXIST"));
				return false;
			}
			
			var json={"id": -1, "industryId": industryId ,"name":name,"editingType":$j("#editingType").attr("value"),"valueType":$j("#valueType").attr("value"),"isSaleProp":$j("#isSaleProp").attr("value"),"isColorProp":$j("#isColorProp").attr("value"),"required":$j("#required").attr("value"),"searchable":$j("#searchable").attr("value"),"hasThumb":$j("#hasThumb").attr("value"),"sortNo":sortNo,"lifecycle":1 };
			//var backWarnEntity = loxia.syncXhr(base+'/property/saveProperty.Json', json,{type: "GET"});
			var backWarnEntity = new Object();
			backWarnEntity.isSuccess=true;
			backWarnEntity.description=json;
			
  			if(backWarnEntity.isSuccess){
  				var property = backWarnEntity.description
  				property.state = parseInt(property.lifecycle);
  				property.num = parseInt(property.sortNo);
  				property.text = property.name;
  				property.type = formatEditingType(parseInt($j("#editingType").attr("value")));
  				newarray.push(property);
  				
  				$j("#property-list li,.new-list-add").remove();
  				$j("#property-list").dragarraylist({
  					newarray:newarray
  					, add:true
  					, callBack:function(o){
  					     setValue(o);
  					},deleteli:function(o){
  					     deleteValue(o);
  					},beforecallBack:function(o){
  						beforeValue(o);
  					}
  				});
  				
  				$j(".ui-sortable #"+property.id).click();
  				
  				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_INSERT_SUCCRSS"));
  			}
		}else{
			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
		}	
		
	});
	
}

/**
 * 左侧拖拽条删除时触发setValue方法
 * @param o (callback回调的json数据)
 * @returns {String}
 */
function deleteValue(o){
	if(o && o.id!=""){
	
	var propertyId = o.id;
	var json={"propertyId": propertyId ,"state":2};
	
	 nps.asyncXhrPost(base+'/property/enableOrDisableproperty.json', json, {
    			   successHandler : function(data, textStatus) {
    	  				if (data.success!=0) {
    	  					nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_DELETE_SUCCRSS"));	
    	  					for(var i=0;i<newarray.length;i++){
							if(newarray[i].id==propertyId){	
								newarray.splice(i,1);
								break;
								}
							}	
							
							 $j("#property-list li,.new-list-add").remove();
							
							$j("#property-list").dragarraylist({
								newarray:newarray
								, add:true
								, callBack:function(o){
							     setValue(o);
								},deleteli:function(o){
					 			    deleteValue(o);
								},beforecallBack:function(o){
  								beforeValue(o);
  								}
							});
							
					
							var liid = $j("#idvalue").val();
							if(liid){
							$j(".ui-sortable #"+liid).click();
							}
    	  				} else {
    	  					nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_DELETE_SUCCRSS"));
    	  			}
    	  		}
    	  	 });
    	 }
    	 
    	 $j("#property-list li,.new-list-add").remove();
							
							$j("#property-list").dragarraylist({
								newarray:newarray
								, add:true
								, callBack:function(o){
							     setValue(o);
								},deleteli:function(o){
					 			    deleteValue(o);
								},beforecallBack:function(o){
  								beforeValue(o);
  							}
			});
			
			var liid = $j("#idvalue").val();
							if(liid){
							$j(".ui-sortable #"+liid).click();
							}
    	 	
	}

/**
 * 刷新右侧填选框的方法
 */
function refreshRight(){
	$j("input[name='name']").val("未定义名称");
	$j("#editingType").attr("value",1);
	$j("#valueType").attr("value",1);
	$j("#isSaleProp").attr("value","false");
	$j("#isColorProp").attr("value","false");
	$j("#required").attr("value","false");
	$j("#searchable").attr("value","true");
	$j("#hasThumb").attr("value","false");
	$j("#propertyId").val("");
	$j("#sortNo").val("");
}

function validatePropertyName(industryId,name){
	var json={"industryId":industryId,"name":name};
	//var result = loxia.syncXhr(base+'/shop/validatePropertyName.json', json,{type: "GET"});
	var result=new Object();
	result.isSuccess=true;
	
	if(result.isSuccess!=undefined && result.isSuccess== true){
		return true;
	}else{
		$j("input[name='name']").focus();
		return false;
	}
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
	} else {
		return nps.i18n("VALUE_EDIT_DX");
	}
}



$j(document).ready(function(){
	
	//颜色属性必须配图
	$j("#isColorProp").change(function(){
		if( $j("#isColorProp").attr("value")=="true"){
		$j("#hasThumb").attr("value","true");
		}
	});

	/**
	 * 返回按钮
	 */
	$j(".return_button").click(function() {
		window.location.href="/property/propertyList.htm";
	});
	
	/**
	 * 设置属性按钮
	 */
	$j("#propertyValue_button").click(function() {
		if( $j(".ui-sortable li").hasClass('selected')){
			if($j("#propertyId").val()==""){
			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_UNKNOW_PROPERYY"));	
			}else if($j(".ui-sortable li").hasClass('no-element selected')){
			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_DISABLE"));	
			}else{
			window.location.href=base+"/property/propertyValueList.json?propertyId="+$j("#propertyId").val();;
		}
		}else{
			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SELECT"));
		}
	});
	
	/**
	 * 下一步按钮
	 */
	$j(".button.orange.comfirm").click(function(){		
		var zTree = $j.fn.zTree.getZTreeObj("treeDemo");
		var nodes = zTree.getCheckedNodes(true);
		if (nodes.length>0) {
			var industryId =nodes[0].id;
			var name = nodes[0].name;
			
			var json={"industryId":industryId};
	  		//var backWarnEntity = loxia.syncXhr(base+'/property/propertyListByIndustryid.json', json,{type: "GET"});
			
	  		if(true){
	  			$j("#industryId").val(industryId);
	  			$j("#industrySelDiv").css("display","none");
				$j("#propertySelDiv").css("display","block");
				$j("#industryPropertyDiv").html(name+nps.i18n("SYSTEM_PROPERTY_INDUSTY_LIST"));
				//jsonArray = backWarnEntity.description  ;
				
				$j.each(jsonArray,function(i,val){
					jsonArray[i].state = parseInt(jsonArray[i].lifecycle);
					jsonArray[i].num = parseInt(jsonArray[i].sortNo);
					jsonArray[i].text = jsonArray[i].name;
					jsonArray[i].type = formatEditingType(jsonArray[i].editingType);
				});
				
				$j("#property-list").dragarraylist({
					newarray:jsonArray
					, add:true
					, callBack:function(o){
					     setValue(o);
					},deleteli:function(o){
					     deleteValue(o);
					},beforecallBack:function(o){
  						beforeValue(o);
  					}
				});
					
	  		 }
		}else{
			  nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SELECT_INDUSTRY"));
		}
	});	
	
	/**
	 * 修改按钮
	 */
	$j(".button-line .button.orange.add:eq(2)").click(function(){	
		var propertyId = $j("#propertyId").val();
		var name =  $j("input[name='name']").val();
		var industryId = $j("#industryId").val()
		var sortNo = $j("#sortNo").val()
		
		
		if(name!="" && propertyId!="" && industryId!="" && sortNo!=""){	
			var originalName ;
			for(var i=0;i<newarray.length;i++){
					if(newarray[i].id==propertyId){	
						originalName = newarray[i].name;
						break;
					}
				}	
			if(originalName!=name){
					if(!validatePropertyName(industryId,name)){
					nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_EXIST"));
							return false;
					}
			}
			
			var json={"id": propertyId, "industryId": industryId ,"name":name,"editingType":$j("#editingType").attr("value"),"valueType":$j("#valueType").attr("value"),"isSaleProp":$j("#isSaleProp").attr("value"),"isColorProp":$j("#isColorProp").attr("value"),"required":$j("#required").attr("value"),"searchable":$j("#searchable").attr("value"),"hasThumb":$j("#hasThumb").attr("value"),"sortNo":sortNo,"lifecycle":1 };
			var backWarnEntity = loxia.syncXhr(base+'/property/saveProperty.Json', json,{type: "GET"});
  			if(backWarnEntity.isSuccess){
  				var property = backWarnEntity.description
  				$j(".ui-sortable .selected .text").html(name);
  				$j(".ui-sortable .selected .type").html(formatEditingType(parseInt($j("#editingType").attr("value"))));
  				$j(".ui-sortable .selected .text").attr("title",name);
  				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SUCCESS"));
  				
  				for(var i=0;i<newarray.length;i++){
					if(newarray[i].id==propertyId){	
						newarray[i].editingType = $j("#editingType").attr("value");
						newarray[i].name = name;
						newarray[i].text = name;
						newarray[i].editingType =  $j("#editingType").attr("value");
						newarray[i].valueType = $j("#valueType").attr("value");
						newarray[i].isSaleProp =$j("#isSaleProp").attr("value");
						newarray[i].isColorProp =$j("#isColorProp").attr("value");
						newarray[i].required =$j("#required").attr("value");
						newarray[i].searchable =$j("#searchable").attr("value");
						newarray[i].type = formatEditingType(parseInt($j("#editingType").attr("value")))  ;
						newarray[i].state =1;
						newarray[i].lifecycle=1;
						newarray[i].title= name;
						break;
					}
				}	
  			}
		}else{
			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
		}	
	});	
	
	/**
	 * 修改并启用按钮
	 */
	$j(".button-line .button.orange.add:eq(0)").click(function(){	
		var propertyId = $j("#propertyId").val();
		var name =  $j("input[name='name']").val();
		var industryId = $j("#industryId").val()
		var sortNo = $j("#sortNo").val()
		if(name!="" && propertyId!="" && industryId!="" && sortNo!=""){		
			
			var originalName ;
			for(var i=0;i<newarray.length;i++){
					if(newarray[i].id==propertyId){	
						originalName = newarray[i].name;
						break;
					}
				}	
			if(originalName!=name){
					if(!validatePropertyName(industryId,name)){
					nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_EXIST"));
							return false;
					}
			}
		
		
		
			var json={"id": propertyId, "industryId": industryId ,"name":name,"editingType":$j("#editingType").attr("value"),"valueType":$j("#valueType").attr("value"),"isSaleProp":$j("#isSaleProp").attr("value"),"isColorProp":$j("#isColorProp").attr("value"),"required":$j("#required").attr("value"),"searchable":$j("#searchable").attr("value"),"hasThumb":$j("#hasThumb").attr("value"),"sortNo":sortNo,"lifecycle":1 };
			var backWarnEntity = loxia.syncXhr(base+'/property/saveProperty.Json', json,{type: "GET"});
  			
  			if(backWarnEntity.isSuccess){
  				var property = backWarnEntity.description
  				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SUCCESS"));
  				
  				for(var i=0;i<newarray.length;i++){
					if(newarray[i].id==propertyId){	
						newarray[i].editingType = $j("#editingType").attr("value");
						newarray[i].name = name;
						newarray[i].text = name;
						newarray[i].editingType =  $j("#editingType").attr("value");
						newarray[i].valueType = $j("#valueType").attr("value");
						newarray[i].isSaleProp =$j("#isSaleProp").attr("value");
						newarray[i].isColorProp =$j("#isColorProp").attr("value");
						newarray[i].required =$j("#required").attr("value");
						newarray[i].searchable =$j("#searchable").attr("value");
						newarray[i].type = formatEditingType(parseInt($j("#editingType").attr("value")));
						newarray[i].state =1;
						newarray[i].lifecycle=1;
						$j("#property-list li,.new-list-add").remove();
						break;
					}
				}
  				$j("#property-list").dragarraylist({
  					newarray:newarray
  					, add:true
  					, callBack:function(o){
  					     setValue(o);
  					},deleteli:function(o){
  					     deleteValue(o);
  					},beforecallBack:function(o){
  						beforeValue(o);
  					}
  				});
  				if($j(".ui-sortable #"+propertyId)){
  				$j(".ui-sortable #"+propertyId).click();
  				}
  			}
		}else{
			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
		}	
	});		
	
	/**
	 * 禁用按钮
	 */	
	$j(".button-line .button.orange.add:eq(3)").click(function(){	
		var propertyId = $j("#propertyId").val();
		if(propertyId){
		var json={"propertyId": propertyId ,"state":0};
  		var _d = loxia.syncXhr(base+'/property/enableOrDisableproperty.json', json,{type: "GET"});
  		if (_d.success!=0) {
  			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_DISABLE_SUCCRSS"))	;
  			for(var i=0;i<newarray.length;i++){
				if(newarray[i].id==propertyId){	
					newarray[i].state =0;
					$j("#property-list li,.new-list-add").remove();
					break;
				}
			}

  			$j("#property-list").dragarraylist({
				newarray:newarray
				, add:true
				, callBack:function(o){
				     setValue(o);
				},deleteli:function(o){
				     deleteValue(o);
				},beforecallBack:function(o){
  					beforeValue(o);
  				}
			});
  			if($j(".ui-sortable #"+propertyId)){
  			$j(".ui-sortable #"+propertyId).click();
  			}
  		 }
		}
	});	
		
	/**
	 * 新增并且启用按钮
	 */
	$j(".button-line .button.orange.add:eq(1)").click(function(){	
		var sortNo = $j(".list-all.ui-sortable li").length;
		var name =  $j("input[name='name']").val();
		var industryId = $j("#industryId").val()
		
		if(name!=""&& industryId!="" && sortNo!=""){	
		
			if(!validatePropertyName(industryId,name)){
			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_EXIST"));
				return false;
			}
			
			var json={"id": -1, "industryId": industryId ,"name":name,"editingType":$j("#editingType").attr("value"),"valueType":$j("#valueType").attr("value"),"isSaleProp":$j("#isSaleProp").attr("value"),"isColorProp":$j("#isColorProp").attr("value"),"required":$j("#required").attr("value"),"searchable":$j("#searchable").attr("value"),"hasThumb":$j("#hasThumb").attr("value"),"sortNo":sortNo,"lifecycle":1 };
			var backWarnEntity = loxia.syncXhr(base+'/property/saveProperty.Json', json,{type: "GET"});
  			if(backWarnEntity.isSuccess){
  				var property = backWarnEntity.description
  				property.state = parseInt(property.lifecycle);
  				property.num = parseInt(property.sortNo);
  				property.text = property.name;
  				property.type = formatEditingType(parseInt($j("#editingType").attr("value")));
  				newarray.push(property);
  				
  				$j("#property-list li,.new-list-add").remove();
  				$j("#property-list").dragarraylist({
  					newarray:newarray
  					, add:true
  					, callBack:function(o){
  					     setValue(o);
  					},deleteli:function(o){
  					     deleteValue(o);
  					},beforecallBack:function(o){
  						beforeValue(o);
  					}
  				});
  				
  				if($j(".ui-sortable #"+property.id)){
  						$j(".ui-sortable #"+property.id).click();
  				}

  				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_INSERT_SUCCRSS"));
  			}
		}else{
			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NAME"));
		}	
		
	});
	
	/**
	 * 排序按钮
	 * 当前有未定义的属性时不允许排序
	 */
	$j(".border-right-dashed .button-line .button").click(function(){
		var lilength = $j(".list-all.ui-sortable li").length;
		var l = newarray.length;
		if(l>0){
			if(lilength==l){
				savelistarry();
			var str ="" ;
			for(var i=0;i<newarray.length;i++){
				newarray[i].sortNo = newarray[i].num;
				str += "propertyId"+newarray[i].id+"sortNo"+newarray[i].num+","
			}
			var json ={"ids":str};
			var backWarnEntity = loxia.syncXhr(base+'/property/updatePropertyBylist.json',json,{type: "GET"});
			if(backWarnEntity.isSuccess){
				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SORT_SUCCRSS"));		
			}else{
				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_ERROR"));				
			}
			}else{
				nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SORT_ERROR"));	
			}
		}else if(lilength==1 && l==0){
			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_SORT_ERROR"));
		}else{
			nps.info(nps.i18n("SYSTEM_PROPERTY_MESSAGE"),nps.i18n("SYSTEM_PROPERTY_NO_SORT"));	
		}
	});	
});
