$j.extend(loxia.regional['zh-CN'],{
	"SHOP_MANAGER_TIPS":"友情提示：",
	"shop.add.chooseonindustry":"请选择至少一个行业关联属性",
	"shop.add.addshopfail":"店铺增加失败",
	"shop.add.addshopsuccess":"店铺增加成功",
	"shop.add.addandsetpropertyfail":"店铺增加并且设置属性失败",
	"shop.add.fillshopcode":"请填写店铺名称",
	"shop.add.shopcode.enble":"店铺编码不可用",
	"shop.add.shopcode.able":"店铺编码可用",
	"shop.add.nothavelist":"行业下没有关联行业属性，无法编辑"
});

var check="";

var validateShopCodeUrl =base + '/shop/validateShopCode.json';

var setting = {
//		check: {
//			enable: true,
//			chkboxType: {"Y":"", "N":""}
//		},
		view: {
			dblClickExpand: true,
			showIcon:false,
			addDiyDom: addDiyDom
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onClick : onClick
		}
	};
	function beforeClick(treeId, treeNode) {
		var zTree = $j.fn.zTree.getZTreeObj("treeDemo");
		zTree.checkNode(treeNode, !treeNode.checked, null, true);
		return false;
	}
	
	function onCheck(e, treeId, treeNode) {
		var zTree = $j.fn.zTree.getZTreeObj("treeDemo");
		//获取被选中的checkbox的集合
		nodes = zTree.getCheckedNodes(true);
		v = "";
		for (var i=0, l=nodes.length; i<l; i++) {
			v += nodes[i].id + ",";
		}
		if (v.length > 0 ) v = v.substring(0, v.length-1);
		check = v;
		$j("#industrys").val(check);
	}

	function userFormValidate(){
		if($j("input[group='subArray']").length<=0)
		{
			return nps.i18n("shop.add.chooseonindustry");
		}
		 return loxia.SUCCESS; 
	}
	
	function funprocess(data, textStatus){
		alert(data);
	}
	
	function onClick(event, treeId , treeNode) {
		if(treeNode.isParent || treeNode.id == 0) return;
		openDialog(treeNode.id);
	}	
	
	function addDiyDom(treeId, treeNode) {
		if(treeNode.isParent || treeNode.id == 0) return;
		if($j("#diyBtn_"+treeNode.id).length>0) return;
		var checkType=propertys[""+treeNode.id]["checkType"];
		var spanStr="";
		if(checkType==0){
			spanStr="<span style=\"color:#999999\">[未选]</span>";
		}else if(checkType==1){
			spanStr="<span style=\"color:orange\">[已选]</span>";
		}else if(checkType==2){
			spanStr="<span style=\"color:#669966\">[全选]</span>";
		}
		//lv#6BE61A
		//黄 orange
		//灰 #999999
		var haveProp = propertys[""+treeNode.id]["list"].length>0;
		var editStr = "<span id='diySpan_"+treeNode.id+"'>"+spanStr+"</span> <span class='ui-block'><a "+(haveProp?"":"disabled='disabled'")+" id='diyBtn_"+treeNode.id+"' title='编辑' style=\"color:#27a1c5\">编辑</a></span>";
		//alert(treeNode.tId);
		var aObj = $j("#"+treeNode.tId).append(editStr);
		var btn = $j("#diyBtn_"+treeNode.id);
		if (btn) btn.bind("click", function(){
			//alert("diy Button for " + treeNode.name);
			openDialog(treeNode.id,treeNode.name);
		});
	};
	
	function openDialog(industryId,industryName){
		
		//var array1=propertys.parseJSON();
		var array=propertys[""+industryId]["list"];
		if(array.length<=0){
			return nps.info(nps.i18n("SHOP_MANAGER_TIPS"),nps.i18n("shop.add.nothavelist"));
		}
		//alert(array);
		var html="<span>行业 "+industryName+" 包含属性如下:</span><br/><br/>";
		var checkCount=0;
		$j.each(array,function(i, n){
			if(n.checked){
				checkCount++;
			}
			html+="<span class=\"children-store\" style=\"width: 145px; display: block;\">";
			html+="<label>";
			html+="<input type=\"checkbox\" name=\"chk-group\" " + (n.checked?" checked=\"checked\" ":"")+
					" value=\""+n.id+"\">"+n.prop_name;
			html+="</label>";
			html+="</span>";
		});
		
		if(checkCount>0&&checkCount>=array.length){
			$j("#checkAll").attr("checked",true);
		}else if(checkCount>0){
			$j("#checkAll").removeAttr("checked");
		}else{
			$j("#checkAll").removeAttr("checked");
		}
		
		$j("#props").html(html);
		
		$j("#dialog").attr("industryId",industryId);
		$j("#dialog").dialogff({type:'open',close:'in',width:'530px',height:'300px'});
	}
	
	
$j(document).ready(function(){
		$j.fn.zTree.init($j("#treeDemo"), setting, zNodes);
		
		//保存店铺
		$j(".button.orange.submit").click(function()
		{
			var html="";
			$j.each(propertys,function(i,n){
					var arrays=n["list"];
					$j.each(arrays,function(c,p){
						if(p["checked"]){
							html+="<input type=\"hidden\" group=\"subArray\" name=\"shopPropertys.industryId\" value=\""+i+"\">";
							html+="<input type=\"hidden\" group=\"subArray\" name=\"shopPropertys.propertyId\" value=\""+p.id+"\">";
						}
					});
			});
			
		   $j("#shopForm").append(html);
		   nps.submitForm('userForm',{mode: 'async', 
				successHandler : function(data){
				if(data.isSuccess == true)
				{
					window.location.href=base+"/shop/shopList.htm";
				}else
				{
					$j("input[group='subArray']").remove();
					return nps.info(nps.i18n("SHOP_MANAGER_TIPS"),nps.i18n("shop.add.addshopfail"));
				}
		   }});
	    });
		 //保存并且设置店铺属性
		 $j(".button.configShopProperty.submit").click(function(){
			 nps.submitForm('userForm',{mode: 'async', 
					successHandler : function(data){
					if(data.isSuccess == true)
					{
						//var shopId =$j("#shopid").val();
						console.log(data.errorCode);
						window.location.href=base+"/shop/shopPropertymanager.htm?shopId="+data.errorCode;
					}else
					{
						return  nps.info(nps.i18n("SHOP_MANAGER_TIPS"),nps.i18n("shop.add.addandsetpropertyfail"));
					}
			   }});
		 });
		 //返回
		 $j(".button.goBack").click(function(){
			 window.location.href=base+"/shop/shopList.htm?keepfilter=true";
		 });
		//检查店铺编码是否具有唯一性
		 $j("#shopcode").bind("blur",function()
		 {
			 var code = $j("#shopcode").val();
			 if(code.trim()=="")return;
			 
 			var json={"shopcode":code};
 		  	var _d = loxia.syncXhr(validateShopCodeUrl, json,{type: "GET"});
 			if(_d.isSuccess == false)
 			{
 				$j("#shopcode").val("")
 				$j("#loxiaTip-r").show();
				$j(".inner.ui-corner-all").html(nps.i18n("shop.add.shopcode.enble"));
				$j("#shopcode").addClass("ui-loxia-error");
				return;
 				//return nps.info(nps.i18n("SHOP_MANAGER_TIPS"),nps.i18n("shop.add.shopcode.enble"));
 			}else if(_d.isSuccess == true)
 			{
 				 $j("#loxiaTip-r").show();
 				 $j(".inner.ui-corner-all").html(nps.i18n("shop.add.shopcode.able"));
 				 setTimeout(function(){ 
 					$j("#loxiaTip-r").hide();
 				 },2000); 
 				 return;
 			}
	 		
		 });
		 $j("#shopcode").bind("focus",function()
				 {
				   $j("#loxiaTip-r").hide();
				 }
		 );
			
			$j("#dialog .btn-ok").click(function(){
				var industryId=$j("#dialog").attr("industryId");
				var array=propertys[""+industryId]["list"];
				var indu_name=propertys[""+industryId]["indu_name"];
				
				var checkedBoxs=$j("#props input:checked");
				
				var checkCount=0;
				$j.each(array,function(i, n){
					var checked=false;
					for(var i=0;i<checkedBoxs.length;i++){
						if($j(checkedBoxs[i]).val()==n.id){
							checked=true;
							checkCount++;
							break;
						}
					}
					n["checked"]=checked;
				});
				
				$j("#dialog").dialogff({type:'close'});
				
				var treeObj = $j.fn.zTree.getZTreeObj("treeDemo");
				var node = treeObj.getNodeByParam("id", industryId, null);
				if(checkCount>0){
					treeObj.checkNode(node, true, null , true);
				}else{
					treeObj.checkNode(node, false, null , true);
				}
				
				var spanStr="";
				//TODO 名称更新（全选/已选/未选）
				if(checkCount>0&&checkCount>=array.length){
					spanStr="<span style=\"color:#669966\">[全选]</span>";
				}else if(checkCount>0){
					spanStr="<span style=\"color:orange\">[已选]</span>";
				}else{
					spanStr="<span style=\"color:#999999\">[未选]</span>";
				}
				$j("#diySpan_"+node.id).html(spanStr);
			});
			
			$j("#dialog .canel").click(function(){
				$j("#dialog").dialogff({type:'close'});
			});
			
			//全选/取消全选
			$j("#checkAll").click(function(){
				if($j(this).attr("checked")){
					$j("input[type='checkbox'][name='chk-group']").attr("checked",true);
				}else{
					$j("input[type='checkbox'][name='chk-group']").removeAttr("checked");
				}
			});
	});	
