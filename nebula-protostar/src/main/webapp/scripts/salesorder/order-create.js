$j.extend(loxia.regional['zh-CN'],{
	"ORDER_CREATE_TIPS":"友情提示：",
	"DELETE_CONFIRM":"确认删除该订单行",
	"ADDRESS_DELETE_CONFIRM":"确认删除该地址",
	"CLEAN_CONFIRM":"确认清空购物车",
	"CREATE_SUCCESS":"创建订单成功",
	"CREATE_FAILURE":"创建订单失败",
    "EMAIL_NOT_VALID":"邮箱不符合规则,例如：xxx@163.com",
    "MOBILE_NOT_VALID":"手机号码填写有误，请输入11位手机号码",
    "TEL_NOT_VALID":"电话号码不合法,正确格式：区号(2到3位)-电话号码(7到8位)-分机号",
    "POSTCODE_NOT_VALID":"邮政编码填写有误,请输入6位邮政编码",
    "ORDER_LINES_MUST_NOTEMPTY":"提交订单之前，请先添加商品!",
    "COUPONUSE_FAILURE":"优惠券无效",
    "COUPONUSE_NOT_BIND_ACTIVE":"该优惠券没有绑定活动",
    "ORDER_MUST_VALIDATE_COUPON":"需要验证优惠券"
});
//移除地址
var deleteMemberContactUrl="/account/address/delete";
//获取地址
var findContactDetailUrl= base+'/member/findContactDetail.json';
//会员详情
var memberDetailsUrl = base+'/member/view.json';
//会员查询
var memberListUrl = base+'/member/memberList.json';
//商品列表搜索
var itemSkuListUrl = base +'/item/itemSolrSetting/searchItemList.json';
//详情
var getItemDetailUrl = base + '/product/getItemDetail.json';
//刷新数据
var refreshDataUrl =pagebase +"/order/refreshData";
//加入购物车
var addShoppingCartUrl = base + '/order/addToShoppingCart.json';
//清空购物车
var clearShoppingCartUrl = base +'/order/cleanShoppingCart.json';
//删除购物车行
var removeShoppingCartLineUrl = base +'/order/removeShoppingCartLine.json';
//验证优惠券
var validateCouponUrl = base + '/order/validCoupon.json';
//验证优惠券标志
var couponValdidateFlag = false;
//PDP前缀替换字符串
var repStr = "(@)";

//获取商品库存 TODO
var getSkuInventoryUrl = base + '/product/findInventory.json'; 

//一个item 下面的sku 集合
var skuCommandJson = null;
var lifecycle = null;
var isRealTime = false;
var inventory = 1;


/********************赠品相关*******************/
var frontendBaseUrl="";
var customBaseUrl="";
var smallSize = "";
var pdpPrefix = "";
//不限购
var NON_LIMIT_FLAG = 0;
//默认不限购
var DEFAULT_LIMIT_FLAG =NON_LIMIT_FLAG;
var removeGiftUrl = base + '/shopping/cart/deleteGift';
var groupGiftMap = null;
var getItemDnaymicPropUrl = base + '/item/findItemsSalesPropByItemIds.json';
//修改购物车礼品行
var updateGiftUrl = base + '/shopping/cart/updateGift';
//获得sku的信息
var findSkuInvenoryByItemIdAndItemProp = base + '/item/findInventory';

$j(document).ready(function(){
		loxia.init({debug: true, region: 'zh-CN'});
	    nps.init(); 
		
		
		//省市区初始化
		$j.each(districtJson["1"],function(i,item){
			 $j("<option></option>")
	                    .val(i)
	                    .text(item)
	                    .appendTo($j("#provience"));
		});
		
		$j("#provience").change(onProvienceChange);
		$j("#city").change(onCityChange);
		$j("#provience").change();
		
		//地质编辑省市区初始化
		$j.each(districtJson["1"],function(i,item){
			 $j("<option></option>")
	                    .val(i)
	                    .text(item)
	                    .appendTo($j("#ads_provience"));
		});
		$j("#ads_provience").change(onProvienceChange_ads);
		$j("#ads_city").change(onCityChange_ads);
		$j("#ads_provience").change();
	    
	    
	    refreshOrderData();
	 
//---------------------------------------------------
	        /**
		     * 提交订单
		     */
			$j(".button.orange.submit").click(function()
			{
			
				nps.submitForm('orderForm',{mode: 'async', 
					successHandler : function(data){
					if(data.isSuccess == true)
					{
					    nps.info(nps.i18n("ORDER_CREATE_TIPS"),nps.i18n("CREATE_SUCCESS"));
					    window.location.href=base+"/order/orderList.htm";
					}else{
						nps.info(nps.i18n("ORDER_CREATE_TIPS"),data.description);
					}
				}});
		    });
			
			 /**
			  * 返回列表
			  */
			$j(".button.orange.return").click(function()
			{
				window.location.href=base+"/order/orderList.htm";
		    });
			
			//选中地址
			$j("#selAddress").live("click",function(){
				var contactId = $j(this).val();
				var json ={"contactId":contactId};
				nps.asyncXhr(findContactDetailUrl, json, {
						type : "POST",
						successHandler : function(data) {
							 $j("#name").val(data.name);
							 $j("#address").val(data.address);
							 $j("#mobile").val(data.mobile);
							 $j("#tel").val(data.tel);
							 $j("#email").val(data.email);
							 $j("#postcode").val(data.postcode);
							 //省市区
							 $j("#provience").val(data.provinceId);
							 $j("#provience").change();
							 $j("#city").val(data.cityId);
							 $j("#city").change();
							 $j("#area").val(data.areaId);
							 //刷新数据
							 refreshOrderData();
						}
				 });
			});
			
			//------------------------------------------地址CRUD操作start-------------------------------------
			//打开添加页面
			$j("#orderInfoBody").on("click","#addressAdd",function(){
				 $j("#ads_id").val("");
				 $j("#ads_name").val("");
				 $j("#ads_address").val("");
				 $j("#ads_mobile").val("");
				 $j("#ads_telphone").val("");
				 $j("#ads_postcode").val("");
				 $j("#addressManageDialog").dialogff({type:'open',close:'in',width:'700px',height:'500px'});
			});
			
			//确定
			$j("#orderInfoBody").on("click",".addressManageDialogSubmit",function(){
				 $j("#ads_memberId").val($j("#memberId").val());
				var isDefault = false;
				if($j("#ads_isDefaultButton").attr("checked")=="checked"){
					isDefault = true;
				}
				$j("#ads_isDefault").val(isDefault);
				nps.submitForm('addressManageForm',{mode: 'async', 
					successHandler : function(data){
						if (data.isSuccess) {
							if(data.description != null && data.description !=""){
								$j(this).sAlert({
									type:"open",
									title:'温馨提示！',
									content:"保存成功！",
									conf:function(e){
										$j("#addressManageDialog").dialogff({type:'close'});
										var memberId = $j("#memberId").val();
										refreshMemberInfo(memberId,data.description);
									}
								});
								
							}
							
						}
				}});
			});
			
			$j("#orderInfoBody").on("click","#editAddress",function(){
					var contactId = $j(this).attr("currentId");
					var json ={"contactId":contactId};
					nps.asyncXhr(findContactDetailUrl, json, {
							type : "POST",
							successHandler : function(data) {
								 $j("#ads_name").val(data.name);
								 $j("#ads_address").val(data.address);
								 $j("#ads_mobile").val(data.mobile);
								 $j("#ads_telphone").val(data.tel);
								 $j("#ads_postcode").val(data.postcode);
								 //省市区
								 $j("#ads_provience").val(data.provinceId);
								 $j("#ads_provience").change();
								 $j("#ads_city").val(data.cityId);
								 $j("#ads_city").change();
								 $j("#ads_area").val(data.areaId);
								 
								 $j("#ads_id").val(data.id);
								 
								 if(data.isDefault==true){
									 $j("#ads_isDefaultButton").attr("checked",true);
								 }else{
									 $j("#ads_isDefaultButton").attr("checked",false);
								 }
								 
								 $j("#addressManageDialog").dialogff({type:'open',close:'in',width:'700px',height:'500px'});
							}
					 });
			});
			$j("#orderInfoBody").on("click","#deleteAddress",function(){
				 var contactId =$j(this).attr("currentId");
				 var memberId =$j("#memberId").val();
				 var json ={"contactId":contactId,"memberId":memberId};
				 nps.confirm(nps.i18n("ORDER_CREATE_TIPS"), nps.i18n("ADDRESS_DELETE_CONFIRM"), function() {
					   nps.asyncXhr(deleteMemberContactUrl, json, {
							type : "POST",
							successHandler : function(data) {
								if(data.isSuccess){
									refreshMemberInfo(memberId,null);
								}
							}
					   });
				   });
			});
			
			//取消
			$j("#orderInfoBody").on("click",".addressManageDialogCancel",function(){
				 $j("#addressManageDialog").dialogff({type:'close'});
			});
			
			//------------------------------------------地址CRUD操作end-------------------------------------
			
			//------------------------------------------会员搜索----------------------------------
			
			$j("#orderInfoBody").on("click","#memberSearch",function(){
				 $j("#memberSeachDialog").dialogff({type:'open',close:'in',width:'1000px',height:'650px'});
			});
			
			  /**
		      * 会员列表
		      */
		 
			$j("#table2").loxiasimpletable({
				page : true,
				size : 15,
				nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
				form:"memberSeachForm",
				cols : [{
					name : "loginName",
					label : "会员名",
					width : "20%",
					sort:["a.login_name asc","a.login_name desc"]
				   
				}, {
					name : "nickName",
					label : "昵称",
					width : "10%",
					sort:["b.nickname asc","b.nickname desc"]
				},{
					name : "typeName",
					label : "类型",
					width : "10%",
					sort:["a.type asc","a.type desc"]
				}, {
					name : "lifecycle",
					label : "状态",
					width : "10%",
					type:"yesno"
				}, {
					name : "registerTime",
					label : "注册时间",
					width : "15%",
					formatter:"formatDate",
					sort:["c.register_time asc","c.register_time desc"]
				}, {
					name : "loginTime",
					label : "最近登录时间",
					width : "15%",
					formatter:"formatDate",
					sort: ["c.login_time asc","c.login_time desc"]
				},{
					template : "memDrawEditor",label:"操作",width:"20%"
				}],
				dataurl : memberListUrl
			});
			
			refreshData2();

			//筛选
			$j("#memberSearchButton").click(function(){
				refreshData2();
			});
			
			//选中会员
			$j("#memberSeachDialog").on("click",".selectedMember",function(){
				var memberId = $j(this).attr("memberId");
				refreshMemberInfo(memberId,null);
			});
			
			/**
			 * 关闭
			 */
		    $j("#orderInfoBody").on("click",".memberSeachDialogCancel",function(){
		    	$j("#memberSeachDialog").dialogff({
					type : 'close'
				});
			 });
			
//-----------------------------------------------------搜索商品    --------------------------
	        /**
		 	 * 打开
		 	 */
		   $j("#orderInfoBody").on("click","#addShoppingcart",function(){
			   if(!checkIsMem()){
				    $j(this).sAlert({
						type:'open',
						title:'温馨提示！',
						content:'请先选定会员，再作相应操作'
					});
					return;
				}
			   //open
			   $j("#seachDialog").dialogff({type:'open',close:'in',width:'1000px',height:'650px'});
		   });
		   /**
		    * 清空
		    */
		   $j("#orderInfoBody").on("click","#cleanShoppingcart",function(){
			   if(!checkIsMem()){
				    $j(this).sAlert({
						type:'open',
						title:'温馨提示！',
						content:'请先选定会员，再作相应操作'
					});
					return;
				}
			   var json = {"memberId":$j("#memberId").val()};
			   nps.confirm(nps.i18n("ORDER_CREATE_TIPS"), nps.i18n("CLEAN_CONFIRM"), function() {
				   nps.asyncXhr(clearShoppingCartUrl, json, {
						type : "POST",
						successHandler : function(data) {
							if(data.isSuccess){
								refreshOrderData();
							}
						}
				   });
			   });
			  
		   });
		   
		   /**
		    * 删除行
		    */
		   $j("#orderInfoBody").on("click","#removeShoppingCartLine",function(){
			   if(!checkIsMem()){
				    $j(this).sAlert({
						type:'open',
						title:'温馨提示！',
						content:'请先选定会员，再作相应操作'
					});
					return;
				}
			   var json = {"extentionCode": $j(this).attr("extentionCode"),"memberId":$j("#memberId").val()};
			   nps.confirm(nps.i18n("ORDER_CREATE_TIPS"), nps.i18n("DELETE_CONFIRM"), function() {
				   nps.asyncXhr(removeShoppingCartLineUrl, json, {
						type : "POST",
						successHandler : function(data) {
							if(data.isSuccess){
								refreshOrderData();
							}
						}
				   });
			   });
		   });
		   
		   /**
		    * 关闭
		    */
		    $j("#orderInfoBody").on("click",".seachDialogCancel",function(){
		    	$j("#seachDialog").dialogff({
					type : 'close'
				});
			 });
			      
//---------------------------------------------------------------商品列表
	     
	     /**
	      * 查询商品生成列表
	      */
	 
		$j("#table1").loxiasimpletable({
			page : true,
			size : 10,
			nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
			form : "searchForm",
			
			cols:
			[
			{name:"code",label:"商品编码",width:"30%"},
			{name:"title",label:"商品名称",width:"30%" },
			{template : "drawEditor",label:"操作",width:"40%"}
			],
			dataurl : 
				itemSkuListUrl
		});
		
		refreshData();

		//筛选
		$j("#search").click(function(){
			refreshData();
		});

		
		//查看详情
		$j("#seachDialog").on("click",".toAddShoppingCart",function(){
			var itemId = $j(this).attr("itemId");
			
			var json = {"itemId":itemId};
			
			var result = loxia.syncXhr(getItemDetailUrl,json,{type : "POST"});
			
			if(result == null||result == undefined){
				
				$j(this).sAlert({
					type:"open",
					title:'添加购物车',
					content:'未查到该商品信息!'
				});
			}
			
			skuCommandJson = result.skuCommandList;
			lifecycle = result.lifecycle;
			
			
			var content = '<div class="ui-block-line">'+
			   	  '<label>商品名称：</label>'+
				  '<div class="pt7">'+
					   '<a href="javascript:void(0);" class="blue">itemTitleToReplace lifeCycleStrToReplace</a>'+
				  '</div>'+
			 '</div>'+
			 
			 '<div class="ui-block-line">'+
		   	  '<label>商品编号：</label>'+
			  '<div class="pt7">'+
				   '<span> itemCodeToReplace </span>'+
			  '</div>'+
		     '</div>'+
			 
			 '<div class="ui-block-line">'+
			   	  '<label>销售价：</label>'+
				  '<div class="pt7">'+
					   '<span id="salePrice">￥itemSalePriceToReplace</span>'+
				  '</div>'+
			 '</div>'+
			 
			 '<div class="ui-block-line">'+
			   	  '<label>吊牌价：</label>'+
				  '<div class="pt7">'+
					   '<span id="listPrice">￥listPriceToReplace</span>'+
				  '</div>'+
			 '</div>PROPHTML'+
			 '<div class="ui-block-line">'+
			   	  '<label>数量：</label>'+
				  '<div class="pt7">'+
					   '<input type="text" id="skuQty" loxiaType="input" mandatory="true" placeholder=""/>'+
					   '<input type="hidden" id="skuId" />'+
					   '<input type="hidden" id="extentionCode" />'+
					   '<span id="skuInventory" style="color:red"></span>'+
				  '</div>'+
			 '</div>';
			
		
			content = content.replace('itemTitleToReplace',result.title);
			content = content.replace('itemCodeToReplace',result.code);
			content = content.replace('itemSalePriceToReplace',result.salePrice);
			content = content.replace('listPriceToReplace',result.listPrice);
			
			 var propAllHtml="";
			
			 var salePropertyList = result.salePropCommandList;
			 if(salePropertyList!=null&&salePropertyList.length>0){
          	  
          	   
          	   for(var i=0;i<salePropertyList.length;i++ ){
          		 
          		   
          		 var propHtml = '<div class="ui-block-line">'+
								   	  '<label>propertyNameHtml：</label>'+
									  '<div class="pt7">'+
										   '<select loxiaType="select" id="itemProp" isColorProp="isColorPropTORepalce" propId="propIdToReplace" propValue="propertyValueTORepalce">PROPVALUEHTML'+
									       '</select>'+
									  '</div>'+
								     '</div>';
          		  var propertyName = salePropertyList[i].property.name;
          		  
            	   //看是否有 salePropertyList 中的每个元素是否有 itemProperties 
          		   if(salePropertyList[i].itemPropertiesList!=null&&salePropertyList[i].itemPropertiesList.length>0){
          			   //list大于1
          			   var itemPropertiesList = salePropertyList[i].itemPropertiesList;
          			   var propValueAllHtml ="";
          			   for(var k=0;k<itemPropertiesList.length;k++){
          				   var isColorProp = salePropertyList[i].property.isColorProp;
          				   var propId =salePropertyList[i].property.id;
          				   var propValue = itemPropertiesList[k].propertyValue;
          				   
	          				propHtml = propHtml.replace('isColorPropTORepalce',isColorProp);
	       					propHtml = propHtml.replace('propIdToReplace',propId);
	       					propHtml = propHtml.replace('propertyValueTORepalce',propValue);
       					
          				   
          				   var id = itemPropertiesList[k].item_properties_id;
          				   var propertyValue = itemPropertiesList[k].propertyValue;
          				   
          				   var propValueHtml ='<option value="idToReplace">propertyValueToReplace</option>';
       
          				   propValueHtml = propValueHtml.replace('idToReplace',id);
          				   propValueHtml = propValueHtml.replace('propertyValueToReplace',propertyValue);
          				   
          				 propValueAllHtml+=propValueHtml;
          			   };
          			 propHtml = propHtml.replace('PROPVALUEHTML',propValueAllHtml);
          			 
          		   }else{
          			   // list等于1
          			   var isColorProp = salePropertyList[i].property.isColorProp;
          			   var propId =salePropertyList[i].property.id;
          			   var propValue = salePropertyList[i].itemProperties.propertyValue;
          			    propHtml = propHtml.replace('isColorPropTORepalce',isColorProp);
	   					propHtml = propHtml.replace('propIdToReplace',propId);
	   					propHtml = propHtml.replace('propertyValueTORepalce',propValue);
	   					
          			   var id = salePropertyList[i].itemProperties.id;
          			   var propertyValue = salePropertyList[i].itemProperties.propertyValue;
	          		   var propValueHtml ='<option value="idToReplace">propertyValueToReplace</option>';
					 	   
	   				   propValueHtml = propValueHtml.replace('idToReplace',id);
	   				   propValueHtml = propValueHtml.replace('propertyValueToReplace',propertyValue);
	   				   
	   				  propHtml = propHtml.replace('PROPVALUEHTML',propValueHtml);
          		   }
          		propHtml = propHtml.replace('propertyNameHtml',propertyName);  	 
          		   
          		propAllHtml+=propHtml;
          	   };
          	  
             }

			
			 content = content.replace('PROPHTML',propAllHtml);
		
			
			var lifeCycleStr ="";
			if(result.lifecycle != 1 ){
				lifeCycleStr = '<span style="color: red;">(该商品已下架)</span>';
			}
			
			content = content.replace('lifeCycleStrToReplace',lifeCycleStr);
			
			
			
			$j("#addShoppingCart-dialog .proto-dialog-content").html(content);
			
			$j("#skuQty").val(1);
			
			itemPropOnChange();
			
			$j("#addShoppingCart-dialog").dialogff({type:'open',close:'in',width:'500px',height:'340px'});
		});
		
		
		
		//加入购物车
		$j(".addbuycart").on('click', function(){
			if(!checkIsMem()){
				$j(".addbuycart").sAlert({
					type:'open',
					title:'温馨提示！',
					content:'请先选定会员，再进行购物！'
				});
				return;
			}
			//增加到购物车
			var json = {"skuId": $j('#skuId').val(), "extentionCode": $j('#extentionCode').val(), "quantity":$j("#skuQty").val(),"memberId":$j("#memberId").val()};
			//var json ={"extentionCode": "pbx_girl_write_36", "quantity": "1", "skuId": "3177"}
			nps.asyncXhr(addShoppingCartUrl, json, {type:"POST",
				successHandler: function(data){ 
		    		if(data.resultCode == 1){		    			
		    			$j(".addbuycart").sAlert({
							type:'open',
							title:'温馨提示！',
							content:'加入购物车成功！'
						});
		    			$j("#addShoppingCart-dialog").dialogff({type:'close'});
		    			refreshOrderData();
		    		}else if(data.resultCode == 0){
						$j(this).sAlert({
							type:"open",
							title:'温馨提示！',
							content:'加入购物车失败！'
						});
					}else if(data.resultCode == 10){
						$j(this).sAlert({
							type:"open",
							title:'温馨提示！',
							content:'该商品已下架！'
						});
					}else if(data.resultCode == 11){
						$j(this).sAlert({
							type:"open",
							title:'温馨提示！',
							content:'该商品购买的数量已达到限购的最大数量！'
						});
					}else if (data.resultCode == 12) {
						$j(this).sAlert({
							type : "open",
							title : '温馨提示！',
							content : '该商品库存不足！'
						});
					}else if (data.resultCode == 13) {
						$j(this).sAlert({
							type : "open",
							title : '温馨提示！',
							content : '该商品未上架！'
						});
					}
		    	}
			});
		});
		
		$j(".addbuycartCancel").on('click',function(){
			$j("#addShoppingCart-dialog").dialogff({
				type : 'close'
			});
		});
		
		
		
		//点击销售属性
		$j('#orderInfoBody').on('change', '#itemProp',function(){
			itemPropOnChange();
		});
		
		
		//输入时
		$j('#orderInfoBody').on("keyup", "#skuQty", function(){
			var skuQty = $j(this).val();
			var reg = new RegExp("^[0-9]*$");
			if(skuQty == ''){
				$j(this).val(1);
				return;
			}
			if(parseInt(skuQty) < 1 || !reg.test(skuQty)){
				$j(this).val(1);
				return;
			}
			
			if(parseInt(inventory) != 0){
				if(parseInt(skuQty) >= parseInt(inventory)){
					$j(this).val((parseInt(inventory)));
				}
			}
		});
		
		
		//验证优惠券
		$j("#useCoupon").on("click",function(){
			 if(!checkIsMem()){
				    $j(this).sAlert({
						type:'open',
						title:'温馨提示！',
						content:'请先选定会员，再作相应操作'
					});
					return;
				}
			var $this = $j(this);
			var couponCode = $j("#coupon").val();
			$this.attr('disabled',true);
			if(couponCode!=null && couponCode.trim()!=""){
				
				var json ={"couponCode":couponCode};
				nps.asyncXhr(validateCouponUrl,json,{type:"POST",successHandler:function(data){
					if(!data.isSuccess){
						$j("#coupon").val("");
						nps.info(nps.i18n("ORDER_CREATE_TIPS"),nps.i18n("COUPONUSE_FAILURE"));
					}
					couponValdidateFlag=true;
					refreshOrderData();
					$this.attr('disabled',false);
				}});
			}else{
				refreshOrderData();
				$this.attr('disabled',false);
			}
			
			
			
		});
		
		/********************************************************************************赠品先关start***************************************************/
		// 删除赠品
		$j('#orderInfoBody').on('click', '.removeGift', function(){
			var obj = $j(this);
			var json = {"lineId" : obj.attr('line'),memberId:$j("#memberId").val()};
			$j(this).sConfirm({
				type:"open",
				title:'温馨提示?',
				content:'是否删除选中的赠品?',
				conf:function(e){
					nps.asyncXhr(removeGiftUrl, json, {
						type:"POST",
						successHandler : function(resultData){
							if(resultData.resultCode == '1'){
								refreshOrderData();
							}else{
								$j(this).sAlert({
									type : 'open',
									title : '友情提示！',
									content : '删除失败,请与管理员联系'
								});
							}
						}
					});
				}
			});
		});
		
		// 点击"赠品选择"
		$j("#orderInfoBody").on('click', '.chooseGift', function(e){
			var lineG = $j(this).attr("data");
			var promotionIds = $j(this).attr("promotionIds");
			if(groupGiftMap && groupGiftMap[lineG]){
				var lines = new Array();
				if(promotionIds != 'PROMOTION_IDS_REPLACE'){
					var tmpLines = groupGiftMap[lineG];
					for(var i in tmpLines){
						if(tmpLines[i].gift && tmpLines[i].promotionIds == promotionIds){
							lines.push(tmpLines[i]);
						}
					}
				}else{
					lines = groupGiftMap[lineG];
				}
				generateGiftPopHtml(lines, lineG);
			}
	    });
		
		/** 确定选择赠品 */
		$j("#cartSalesPropSure").click(function(){
			var giftCountLimit = $j(this).parents('#sendactive').find('input[id^="item_id_"]').val().split(':')[2];
			var promotionId = $j(this).parents('#sendactive').find('input[name="promotionId"]').val();
			var selecedGift = $j("#sendactive").find("input[name='itemSel']:checked");
			var selectedGiftCount = selecedGift.length;
			if(selectedGiftCount == 0 || selectedGiftCount > giftCountLimit){
				$j(this).sAlert({
					type : 'open',
					title : '友情提示！',
					content : '您可以选择 '+giftCountLimit+' 个赠品！'
				});
				return;
			}
			var lineGroups = new Array();
			var skuIds = new Array();
			for(var i=0;i<selecedGift.length;i++){
				var selCheckBox = $j(selecedGift[i]).val();
				var promotionParams = $j("#item_id_"+selCheckBox).val();
				var skuId = $j("#sku_id_"+selCheckBox).val();
				if(!skuId){
					$j(this).sAlert({
						type : 'open',
						title : '友情提示！',
						content : '请选择尺码！'
					});
					return;
				}
				lineGroups.push(promotionParams.toString().split(":")[0]);
				skuIds.push(skuId);
			}
			
			// 已选赠品保存到购物车
			var json = {'skuIds': skuIds, 'lineGroups':lineGroups, 'promotionId': promotionId,memberId:$j("#memberId").val()};
	
			nps.asyncXhr(updateGiftUrl,json,{type:"POST",successHandler:function(resultData){
					if(resultData.resultCode == 1){
						refreshOrderData();
					}else{
						$j(this).sAlert({
							type : 'open',
							title : '友情提示！',
							content : '赠品选择失败,请与管理员联系'
						});
					}
				}
			});
			// 关闭dialog
			$j('.dialog-close').click();
		});
		
		/** dialog 的取消按钮*/
		$j('#dialog_cencal').click(function(){
			$j('.dialog-close').click();
		});
		/** 选择尺码时, 获取sku信息 */
		$j('.send-active-table').on('change', '.salesProp', function(){
			var itemPropId = $j(this).val();
			var itemId = $j(this).parents('td').find('input[id^="item_id_"]').attr('itemId');
			var json = {'itemId': itemId, 'itemProperties':itemPropId};
			var resultData = nps.syncXhrPost(findSkuInvenoryByItemIdAndItemProp, json);
			if(resultData != ''){
				var skuInvenory = resultData.availableQty;
				if(skuInvenory <= 0){
					$j(this).sAlert({
						type : 'open',
						title : '友情提示！',
						content : '您选择的款式库存不足！'
					});
					$j(this).find("option[value='']").attr("selected","selected");
					$j('#sku_id_'+itemId).val('');
				}else{
					$j('#sku_id_'+itemId).val(resultData.id);
				}
			}else{
				// sku不存在
				$j(this).sAlert({
					type : 'open',
					title : '友情提示！',
					content : '您选择的款式已下架！'
				});
			}
		});
		
		$j('.send-active-table').on('click', '.default-check', function(){
			checkSelectedGiftNum();
		});
		
		/********************************************************************************赠品先关end***************************************************/
	
});

/********************************************************************************赠品先关start***************************************************/
/*
 * 生成弹出礼品选择的html
 */
function generateGiftPopHtml(lines, lineG){
	var giftItemLineMap = {};
	var itemIdRef = new Array();
	for(var i=0;i<lines.length;i++){
		var line = lines[i];
		var itemId = line.itemId;
		if(itemId != '' && itemId != null && itemId != undefined){
			itemIdRef.push(itemId);
			giftItemLineMap[itemId] = line;
		}
	}
	fnGetDynmicProp(itemIdRef,giftItemLineMap,lineG);
}

/**
 * 商品动态属性
 */
function fnGetDynmicProp(itemIds,giftItemLineMap,lineG){
	var retArr = new Array();
	var itemTrArr = new Array();
	itemTrArr.push('<tr>');
	itemTrArr.push('<td width="3%"><input type="checkbox" name="itemSel" value="ITEM_ID_USE" IS_CHECKED_REPLACE class="default-check"></td>');
	itemTrArr.push('<td width="16%"><a href="ITEM_LINK_URL"><img width="110px" height="110px" class="border-grey" src="ITEM_IMAGE_URL"></a></td>');
	itemTrArr.push('<td width="57%" class="ver-top left">');
	itemTrArr.push('<p class="send-active-name"><a href="ITEM_LINK_URL">ITEM_TITLE_SHOW</a></p>');
	itemTrArr.push('<span class="fLeft mr5 inline-block pt10">款式：</span>');
	itemTrArr.push('<ul class="send-pro-type">');
	itemTrArr.push('<li><a class="selected" href="javascript:void(0)" onclick="return false;"><img src="ITEM_KUANSHI_IMG_SHOW"></a></li>');
	itemTrArr.push('</ul>');
	itemTrArr.push('</td>');
	itemTrArr.push('<td width="24%">');
	itemTrArr.push('<input type="hidden" name="promotionId" value="PROMOTION_ID_REPLACE" /><input type="hidden" id="item_id_ITEM_ID_USE" itemId="ITEM_ID_USE" value="GIFT_PARAMS_INFO" /><input type="hidden" id="sku_id_ITEM_ID_USE" value="SKU_ID_REPLACE" />');
	itemTrArr.push('<select id="sales_prop_sel_'+lineG+'ITEM_ID_USE" class="salesProp"><option value="">请选择尺码</option>SALES_PROP_OPT_ADD</select>');
	itemTrArr.push('</td>');
	itemTrArr.push('</tr>');
	var itemTrHtml = itemTrArr.join("");
	var json = {"itemIds": itemIds.join()};
	nps.asyncXhr(getItemDnaymicPropUrl, eval(json), {
		type:'GET',
		successHandler:function(data){
			var html = '';
			try {
			    for(var i=0;i<itemIds.length;i++){
			    	var itemIdIn = itemIds[i];
			    	var line = giftItemLineMap[itemIdIn];
			    	var itemTitle = line.itemName;
			    	// 是否选中的赠品
			    	var settlementState = line.settlementState;
			    	var saleProperty = line.saleProperty;
			    	if(saleProperty){
			    		saleProperty = saleProperty.substring(1, saleProperty.length-1);
			    	}
			    	
//			    	var code = line.productCode;
//			    	var itemUrl =getItemUrlByCode(code);
//			    	var itemImg = processImgUrl(line.itemPic,SMALL_IMG_SIZE);
//			    	var img110 = processImgUrl(line.itemPic,LITTLE_IMG_SIZE);
			    	
			    	
			    	var itemImg = customBaseUrl+processImgUrl(line.itemPic,smallSize);
			    	var img110 =customBaseUrl+processImgUrl(line.itemPic,smallSize);;
			    	var itemUrl = frontendBaseUrl+pdpPrefix.replace(repStr,line.productCode);
			    	
			    	
			    	var needAppend = itemTrHtml;
			    	var giftProInfo = line.lineGroup+ ":" + itemIdIn + ":" + line.giftCountLimited;
			    	needAppend = needAppend.replace(/ITEM_LINK_URL/g,itemUrl);
			    	needAppend = needAppend.replace(/ITEM_KUANSHI_IMG_SHOW/,itemImg);
			    	needAppend = needAppend.replace("ITEM_TITLE_SHOW",itemTitle);
			    	needAppend = needAppend.replace(/ITEM_IMAGE_URL/g,img110);
			    	needAppend = needAppend.replace(/ITEM_ID_USE/g,itemIdIn);
			    	needAppend = needAppend.replace(/GIFT_PARAMS_INFO/g,giftProInfo);
			    	needAppend = needAppend.replace('PROMOTION_ID_REPLACE', line.promotionId);
			    	
			    	if(line.id != null){
			    		needAppend = needAppend.replace('SKU_ID_REPLACE',line.skuId);
			    	}else{
			    		needAppend = needAppend.replace('SKU_ID_REPLACE','');
			    	}
			    	//needAppend = needAppend.replace(/LINEGROUP_USE/g,lineG);
			    	var chimaOpt = generateSalesOptProp(data[itemIdIn], saleProperty, line.id);
			    	needAppend = needAppend.replace("SALES_PROP_OPT_ADD",chimaOpt);
			    	if(line.id != null && settlementState == '1'){
			    		needAppend = needAppend.replace("IS_CHECKED_REPLACE", 'checked="checked"');
			    	}else{
			    		needAppend = needAppend.replace("IS_CHECKED_REPLACE", '');
			    	}
			    	retArr.push(needAppend);
			    }
			} catch(e){
			}
			$j("#sendactive .send-active-table").html(retArr.join(""));
			checkSelectedGiftNum();
			$j("#sendactive").dialogff({type:'open',close:'in',width:'700px',height:'500px'});
		}
	});
}

/**
 * 生成尺码
 * 
 * @return
 */
function generateSalesOptProp(salePropRef, saleProperty, id){
	var retStr = new Array();
	$j.each(salePropRef.salePropCommandList, function(i, dynProp){
		if(dynProp.itemProperties != null){
			if(id != null && saleProperty == dynProp.itemProperties.item_properties_id){
				retStr.push('<option value="'+dynProp.itemProperties.item_properties_id+'" selected="selected">'+dynProp.itemProperties.propertyValue+'</option>');
			}else{
				retStr.push('<option value="'+dynProp.itemProperties.item_properties_id+'">'+dynProp.itemProperties.propertyValue+'</option>');
			}
		}else{
			$j.each(dynProp.itemPropertiesList,function(key,prop){
				if(id != null && saleProperty == prop.item_properties_id){
					retStr.push('<option value="'+prop.item_properties_id+'" selected="selected">'+prop.propertyValue+'</option>');
				}else{
					retStr.push('<option value="'+prop.item_properties_id+'">'+prop.propertyValue+'</option>');
				}
			});
		}
		
	});
	return retStr.join("");
}

/**
 * 检察用户选中的赠品个数
 * @param object
 */
function checkSelectedGiftNum(){
	// 赠品个数
	var giftCountLimit = $j('#sendactive').find('input[id^="item_id_"]').val().split(':')[2];
	// 已选赠品
	var selecedGift = $j("#sendactive").find("input[name='itemSel']:checked");
	var checkboxs = $j("#sendactive").find("input[name='itemSel']");
	if(selecedGift && selecedGift.length >= giftCountLimit){
		for(var i=0; i<checkboxs.length; i++){
			if(!checkboxs[i].checked){
				checkboxs.eq(i).attr('disabled', 'disabled');
			}
		}
	}else{
		checkboxs.removeAttr('disabled');
	}
}

/********************************************************************************赠品先关end***************************************************/


function checkIsMem(){
	var isMemOrder = $j("#isMemOrder").val();
	var memberId = $j("#memberId").val();
	if(isMemOrder=="true" && memberId=="" ){
		return false;
	}
	return true;
}

function itemPropOnChange(){
	var itemProps = new Array();
	var itemPropOptions = $j("#addShoppingCart-dialog #itemProp");
	if(itemPropOptions!=null && itemPropOptions.length>0){
		$j.each(itemPropOptions,function(i,obj){
			var itemPropId = $j(obj).find("option:selected").val();
			itemProps[i] = parseInt(itemPropId);
		});
	}

	itemProps = sortItemPropId(itemProps);
	var skuCommand = "";
	//是否是实时的数据
	if(isRealTime){
		var json = {"itemId":itemId, "itemProperties": itemProps.toString()};
		var backData = syncXhr(getSkuInventoryUrl, json, {type: "POST"});
		skuCommand = backData;
	}else{
		$j.each(skuCommandJson, function(i, skuCommandData){
			if(skuCommandData.properties==null ||skuCommandData.properties == "["+itemProps+"]"){
				skuCommand = skuCommandData;
				return false;
			}
		});
	}
	//不存在sku 或 没有上架的商品
	if(skuCommand == '' || lifecycle != 1){
		$j('#skuInventory').html('(<em>商品缺货</em>)');
		if(skuCommand == ''){
			$j('#skuId').val("");
			$j(".addbuycart").attr("disabled","disabled");
		}
		return;
	}
	inventory = skuCommand.availableQty;
	if(lifecycle == 1){
		if( inventory==null || parseInt(inventory) < 1){
			$j(".addbuycart").attr("disabled","disabled");
			$j('#skuInventory').html('(<em>商品缺货</em>)');
		}else{
			$j(".addbuycart").removeAttr('disabled');
			$j('#skuInventory').html('(库存'+inventory+'件)');
			
		}
	}
	var _salePrice = skuCommand.salePrice;
	var _listPrice = skuCommand.listPrice;
	$j('#salePrice').html('￥'+_salePrice.toFixed(2));
	//存在吊牌价时, 显示,则隐藏
	if(_listPrice != '' && _listPrice != undefined){
		$j('#listPrice').html('￥'+_listPrice.toFixed(2));
	}
	
	$j('#skuId').val(skuCommand.id);
	$j('#extentionCode').val(skuCommand.extentionCode);

	//购买数量不可以超过库存数
	var skuQty = $j("#skuQty");
	if(parseInt(inventory) > 0){
		if(parseInt(skuQty.val()) >= parseInt(inventory)){
			skuQty.val(parseInt(inventory));
		}
	}
	
	//获得颜色属性的图片
	var isColorProp = $j(this).attr('isColorProp');
	if(isColorProp == 'true'){
		//加载相关图片
//		var backData = getItemImage(itemProperties);
	}
	
//	$j('.promiddleimg').jqzoom({
//		position:"right",
//		title:false,
//		zoomWidth: 400,  
//        zoomHeight: 400
//	});
}
function drawEditor(data, args, idx){
	var itemId = loxia.getObject("id",data);
	var result="<a href='javascript:void(0);' itemId='"+itemId+"' class='func-button modify toAddShoppingCart'>"+"立即查看"+"</a>";
	return result;
} 

function memDrawEditor(data, args, idx){
	var memberId = loxia.getObject("id",data);
	var result="<a href='javascript:void(0);' memberId='"+memberId+"' class='func-button modify selectedMember'>"+"选定"+"</a>";
	return result;
} 

function refreshData(){
	$j("#table1").loxiasimpletable("refresh");
}

function refreshData2(){
	$j("#table2").loxiasimpletable("refresh");
}

//-------------------------------------------一系列验证----------------------------
/**
 * 验证表单
 * @param form
 * @returns
 */
function orderFormValidate(form){
		//检查发票抬头
		var receiptTitleOpt = $j('input:radio[name="receiptTitleOpt"]:checked').val();
		var receiptTitle= $j("#receiptTitle").val();
		if(receiptTitleOpt==2 && receiptTitle.trim()==""){
			loxia.byId($j("input[name=receiptTitle]")).state(false,"请填写单位名称");
			return loxia.FAILURE;
		}
		if(receiptTitleOpt==1){
			$j("#receiptTitle").val($j("#name").val());
		}
	    
		//清单个数
		var orderLineCount =$j("#productTable").find("tr[id='noData']").length;
		
		if(orderLineCount==1){
			return nps.info(nps.i18n("ORDER_CREATE_TIPS"),nps.i18n("ORDER_LINES_MUST_NOTEMPTY"));
		}
		
		var couponCode = $j("#coupon").val();
		if(couponCode!="" && couponValdidateFlag==false ){
			return nps.info(nps.i18n("ORDER_CREATE_TIPS"),nps.i18n("ORDER_MUST_VALIDATE_COUPON"));
		 }
		

		return loxia.SUCCESS;
}
/**
 * 邮箱check
 * @return
 */
function checkEmail(sel, obj){
	    var emailreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	    var email = sel;
	    if(email.trim()!="" && !emailreg.test(email))
	     {     
	         return nps.i18n("EMAIL_NOT_VALID");             
	     }
	    return loxia.SUCCESS;
}


/**
 * 验证手机
 */
function checkMobile(sel, obj){
		var mobilereg=/^1[3|4|5|8][0-9]\d{8}$/;
		var mobile = sel;
		if(mobile.trim()!="" && !mobilereg.test(mobile)){
			 return nps.i18n("MOBILE_NOT_VALID");  
		}
		return loxia.SUCCESS;
	
}
/**
 * 验证电话号码
 */
function checkTel(sel, obj){
		var telreg=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
		var tel = sel;
		if(tel.trim()!="" && !telreg.test(tel)){
			return nps.i18n("TEL_NOT_VALID");  
		}
		return loxia.SUCCESS;
	
}

/**
 * 验证邮政编码
 */
function checkPostCode(sel, obj){
		var postreg=/^[1-9][0-9]{5}$/;
		var postcode = sel;
	    if(postcode.trim()!="" && !postreg.test(postcode))
	    {        
	        return nps.i18n("POSTCODE_NOT_VALID");            
	    }
	    return loxia.SUCCESS;
	
}

//-------------------------------------- 公共方法---


/**
 * 重新加载数据
 */
function refreshOrderData(){
	if(!checkIsMem()){
		var productHtml = '<tr id="orderLineTitle">'+
        '<th width="15%">&nbsp;</th>'+
        '<th width="25%" class="left">商品名称</th>'+
        '<th width="15%">交易单价</th>'+
        '<th width="15%">订购数量</th>'+
        '<th width="15%">金额小计</th>'+
        '<th width="15%">操作</th>'+
        '</tr>';
		productHtml+="<tr id='noData'><td colspan='6' style='color:red;text-align:center'>你还未选购商品</td></tr>";
	 
        $j("#productTable").html(productHtml);
        
		return;
	}
	var json = {
			'provinceId' : $j("#provience").find("option:selected").val(),
			'cityId': $j("#city").find("option:selected").val(),
			'areaId' : $j("#area").find("option:selected").val(),
			'coupons' : $j("#coupon").val(),
	        'memberId' : $j("#memberId").val(),
	        'distributionModeId':$j("input[name='distributionModeId']:checked").val()
	};
	
	
	
	nps.asyncXhr(refreshDataUrl, eval(json), {
		type : "POST",
		successHandler : function(data) {
			var isCouponBindActive = data.isCouponBindActive;
			var shoppingCartCommand = eval(data.shoppingCartCommand);
			frontendBaseUrl = data.frontendBaseUrl;
			customBaseUrl = data.customBaseUrl;
		    smallSize = data.smallSize;
			pdpPrefix = data.pdpPrefix;
			
			if(shoppingCartCommand!=null && $j("#coupon").val().trim()!=""){
				if(isCouponBindActive==false){
					nps.info(nps.i18n("ORDER_CREATE_TIPS"),nps.i18n("COUPONUSE_NOT_BIND_ACTIVE"));
					$j("#coupon").val("");
				}else{
					couponValdidateFlag=true;
				}
				
			}
			
			var productHtml = '<tr id="orderLineTitle">'+
            '<th width="15%">&nbsp;</th>'+
            '<th width="25%" class="left">商品名称</th>'+
            '<th width="15%">交易单价</th>'+
            '<th width="15%">订购数量</th>'+
            '<th width="15%">金额小计</th>'+
            '<th width="15%">操作</th>'+
            '</tr>';
			var orderTotalHtml ="";
			
			var payPrice="0.00";
			
			if(shoppingCartCommand!=null){
				  $j.each(shoppingCartCommand.shoppingCartByShopIdMap,function(index,obj){
					  var resultMap = getCartLine(obj.shoppingCartLineCommands, false);
					  productHtml += resultMap['itemLinesHtml'];
					  groupGiftMap = resultMap['groupGiftMap'];
				  });
				/*  $j.each(shoppingCartCommand.shoppingCartLineCommands,function(index,obj){
		            	var salePrice = 0;
		            	if(obj.salePrice !=null){
		            		salePrice = obj.salePrice.toFixed(2);
		            	}

		            	var skuPropertys = "";
		            	if(obj.skuPropertys!=null && obj.skuPropertys.length >0 ){
		            		 $j.each(obj.skuPropertys,function(index,obj){
		            			 skuPropertys+=obj.value+"&nbsp;&nbsp;";
		            		 });
		            	}
		            	var itemImg = customBaseUrl+processImgUrl(obj.itemPic,smallSize);
		            	
		            	var itemUrl = frontendBaseUrl+pdpPrefix.replace(repStr,obj.productCode);
		            	
		                productHtml+= '<tr>'+
			               '<td><a href="'+itemUrl+'"  target="_blank"><img src="'+itemImg+'" class="border-grey" width="60" height="60"/></a></td>'+
			               '<td class="left"><a href="'+itemUrl+'"  target="_blank">'+obj.itemName +'&nbsp;&nbsp;&nbsp;<font color="red">'+skuPropertys+'</font></a></td>'+
			               '<td><p>￥'+salePrice +'</p></td>'+
			               '<td>'+obj.quantity +'</td>'+
			               '<td><p class="blue bold">￥'+obj.subTotalAmt +'</p></td>'+
			               '<td><a id="removeShoppingCartLine" extentionCode="'+obj.extentionCode+'"  href="javascript:void(0)"  class="func-button ml5">删除</a></td>'+
			               '</tr>';
					});*/
				  
				  
				   //金额合计
				   var sum =shoppingCartCommand.summaryShopCartList[0];
		            
		           orderTotalHtml ='<div class="statistic fr">'+
									         '<div class="list">'+
									            '<span>总商品金额：</span>'+
									            '<em class="price">￥'+sum.subtotalCurrentPayAmount.toFixed(2) +'</em>'+
									        '</div>'+
									        '<div class="list">'+
									            '<span>折扣：</span>'+
									            '<em class="price">￥'+sum.offersTotal.toFixed(2)  +'</em>'+
									        '</div>'+
									        '<div class="list" id="showFreightPrice" style="padding-left:140px;">'+
									        		'<span id="freightSpan" style="width:40px;">运费：</span> '+
									        		'<em class="price" id="freightPrice">￥'+sum.originShoppingFee.toFixed(2) +'</em>'+
									        '</div>'+
									        '<div class="list">'+
									             '<span>应付总额：</span>'+
									             '<em id="sumPayPrice" class="price">￥'+sum.realPayAmount.toFixed(2) +'</em>'+
									        '</div>'+
										'</div>';
		            
		            
		            
		           payPrice = sum.realPayAmount.toFixed(2);

			}else{
				productHtml+="<tr id='noData'><td colspan='6' style='color:red;text-align:center'>你还未选购商品</td></tr>";
				orderTotalHtml="";
			}
			$j("#payPrice").html(payPrice);
			
            $j("#productTable").html(productHtml);
            
            $j(".order-summary").html(orderTotalHtml);
            
         
		}});
}
/**
 * 判断字符是不是空
 * @param str
 */
function isNotNullOrEmpty(str){
	if(str!=undefined&&str!=null&&str!=""&&str!="null"){
		return true;
	}else{
		return false;
	}
}
/**
 * 地址相关
 * @param e
 */
function onProvienceChange(e){
	onDistrictSelectionChange("provience","city");
}
 
function onCityChange(e){
	onDistrictSelectionChange("city","area");
}
 
function onProvienceChange_ads(e){
	onDistrictSelectionChange("ads_provience","ads_city");
}
 
function onCityChange_ads(e){
	onDistrictSelectionChange("ads_city","ads_area");
}

function onDistrictSelectionChange(parentSelectId,subSelectId){
	$j("#"+subSelectId).children().remove();
	var parentId = $j("#"+parentSelectId).val();
	//console.log(parentId);
	if(districtJson[parentId] !=null ){
		$j("#"+subSelectId).show();
		$j.each(districtJson[parentId],function(i,item){
			$j("<option></option>")
			.val(i)
			.text(item)
			.appendTo($j("#"+subSelectId));
		});
		$j("#"+subSelectId).change();
	}else{
		$j("#"+subSelectId).hide();
	}
}

//排序函数
function sortNumber(a,b){
	return a - b
}
//对商品属性Id从小到大排序
function sortItemPropId(itemProps){
	itemProps.sort(sortNumber);
	return itemProps;
}


/**
 * 优惠券值变动，状态设为未验证
 */
function updateFlag(){
	couponValdidateFlag=false;
}


function processImgUrl(imgUrl,size){
	 
	if(null==imgUrl||""==imgUrl){
		return null;
	}
	 
	var index=imgUrl.lastIndexOf(".");
		var index2=imgUrl.lastIndexOf("_");
		var result="";
		//如果找到了下划线 "_",截取下划线及之前的部分
		if(index2!=-1){
				
			result+=imgUrl.substring(0,index2);
			
		}
		//如果找不到下划线 "_",截取.之前的部分
		else{
			result+=imgUrl.substring(0,index);
			
		}
		if("source"!=size){
			result+="_";
			result+=size;
		}
		
		result+=imgUrl.substring(index);
		
		return imgbase + result;
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

function refreshMemberInfo(memberId,contactId){
	 var json = {"memberId":memberId};
	 nps.asyncXhr(memberDetailsUrl, json, {
			type : "POST",
			successHandler : function(data) {
				
				var memberPersonalDataCommand = data.memberPersonalDataCommand;
				var contacts = data.contacts;
				
				$j("#memberId").val(memberPersonalDataCommand.memberId);
				
				var contactHtml ="";
				var contactDiv ="";
				if(contacts!=null && contacts!=""){
					$j.each(contacts,function(index,obj){
						var checkHtml ="";
						if(contactId!=null){
							if(obj.id ==contactId){
								checkHtml ="checked='checked'";
							}
						}else{
							if(obj.isDefault ==true){
								checkHtml ="checked='checked'";
							}
						}
						
		                contactHtml+='<div class="address-line">'+
			           	    '<input type="radio" value="'+obj.id+'" id="selAddress" '+checkHtml+'  name="selAddress"  class="default-check"/>'+
			                '<span class="ol-word">'+obj.province+'&nbsp'+obj.city+'&nbsp'+obj.area+'&nbsp'+obj.address+' ('+obj.name+' 收) '+obj.mobile+'</span>'+
			                '&nbsp;&nbsp;<a href="javascript:void(0);" class="control-btn" id="editAddress" currentId="'+obj.id+'">编辑</a>'+
			                '&nbsp;&nbsp;<a href="javascript:void(0);" class="control-btn" id="deleteAddress" currentId="'+obj.id+'">删除</a>'+
			                '</div>';
						
					});
					contactDiv ='<div class="ui-block-line ">'+
								'<label> 请选择送货地址：</label>'+
								'<div id="contactDiv" class="pt7">'+
								contactHtml+
								'<br><a id="addressAdd" href="javascript:void(0)" class="func-button ml5">添加地址</a>'+
								'</div>'+
							    '<br></div>';
					
				}else{
					contactDiv ='<div class="ui-block-line ">'+
					'<label> 请选择送货地址：</label>'+
					'<br><a id="addressAdd" href="javascript:void(0)" class="func-button ml5">添加地址</a>'+
				    '<br><br></div>';
				}
				var tel = memberPersonalDataCommand.short2==null?"":memberPersonalDataCommand.short2;
				
				var mobile = memberPersonalDataCommand.mobile==null?"": memberPersonalDataCommand.mobile;
				
				var loginName =  memberPersonalDataCommand.loginName==null?"未填写": memberPersonalDataCommand.loginName;
				
				var email = memberPersonalDataCommand.email==null?"未填写": memberPersonalDataCommand.email;
				
				var mobileHtml ="";
				if(tel=="" && mobile==""){
					mobileHtml = "未填写";
				}else{
					mobileHtml = tel+"&nbsp;"+mobile;
				}
				
				var memberInfoHtml = '<div class="ui-block-line ">'+
										'<label> 登录名：</label>'+
										'<div id="loginNameDiv"  class="pt7">'+
										loginName+
										'</div>'+
									 '</div>'+
									 '<div class="ui-block-line">'+
										'<label> 邮箱：</label>'+
										'<div id="emailDiv"  class="pt7">'+
										email+
										'</div>'+
									 '</div>'+
									 '<div class="ui-block-line ">'+
										'<label> 联系方式：</label>'+
										'<div id="contactDiv"  class="pt7">'+
										mobileHtml+
										'</div>'+
									 '</div>'+
									 contactDiv;
									 
				
				$j("#memberInfoDiv").html(memberInfoHtml).show();
				
				$j("#memberSeachDialog").dialogff({
					type : 'close'
				});
				
				$j('input:radio[name="selAddress"]:checked').click();
				refreshOrderData();
			}
	   });
}