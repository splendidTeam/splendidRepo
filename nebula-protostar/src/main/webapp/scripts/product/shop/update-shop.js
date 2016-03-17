$j.extend(loxia.regional['zh-CN'],{
	"SHOP_MANAGER_TIPS":"提示信息：",
	"shop.update.updateshopfail":"修改店铺失败",
	"shop.update.updateshopsuccess":"修改店铺成功",
	"shop.add.chooseonindustry":"请选择至少一个行业关联属性",
	"shop.update.updateshoppropertyfail":"修改并设置店铺属性失败",
	"shop.update.fillshopcode":"请填写店铺编码",
	"shop.update.shopcode.enble":"店铺编码不可用,为您恢复到原始的店铺编码",
	"shop.update.shopcode.able":"店铺编码可用",
	"shop.add.nothavelist":"行业下没有关联行业属性，无法编辑"
});
var check="";
var oldval="";
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

	function beforeClick(treeId, treeNode) {
		var zTree = $j.fn.zTree.getZTreeObj("treeDemo");
		zTree.checkNode(treeNode, !treeNode.checked, null, true);
		return false;
	}
	function onCheck(e, treeId, treeNode) {
		var zTree = $j.fn.zTree.getZTreeObj("treeDemo"),
		nodes = zTree.getCheckedNodes(true),
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
	
	$j(document).ready(function(){
		loxia.init({debug: true, region: 'zh-CN'});
	    nps.init();
		oldval = $j("#shopcode").val();
		$j.fn.zTree.init($j("#treeDemo"), setting, zNodes);	
		
		
		var treeObj = $j.fn.zTree.getZTreeObj("treeDemo");
		var nodes = treeObj.transformToArray(treeObj.getNodes());
		var booleanFlag = true;
		for(var i = 0;i<nodes.length;i++){
			//如果此节点不是父节点  或者是 ROOT节点
			if(!(nodes[i].isParent || nodes[i].id == 0)){
				//如果此节点radio被选中
				if(nodes[i].checked){
					//取到此节点所在的父节点把他 展开 open = true
					var node = nodes[i].getParentNode();
//					console.log(nodes[i].name + "||==="+nodes[i].getParentNode().name+ "||==="+node.id);
					if(null != node){
						if(node.open == false)
						node.open = true;
					}
				}

			}
			treeObj.refresh();
		}
		
		$j(".button.orange.submit").click(function(){

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
			
			nps.submitForm('shopForm',{mode: 'async', 
				successHandler : function(data){
				if(data.isSuccess == true)
				{
					$j("input[group='subArray']").remove();
					nps.info(nps.i18n("SHOP_MANAGER_TIPS"),nps.i18n("shop.update.updateshopsuccess"));
					return false;
				}else
				{ 
					$j("input[group='subArray']").remove();
					nps.info(nps.i18n("SHOP_MANAGER_TIPS"),nps.i18n("shop.update.updateshopfail"));
				    return false;
				}
		   }});
	    });
		
		//修改店铺属性
		$j(".button.updateShopPropertyBtn.submit").click(function(){
			//onCheck(null, null, null);
			$j("#updatespp").val("save");
			nps.submitForm('shopForm',{mode: 'async', 
				successHandler : function(data){
				if(data.isSuccess == true)
				{
					var shopId = $j("#shopid").val();
					window.location.href=base+"/shop/shopPropertymanager.htm?shopId="+shopId;
				}else
				{
					return nps.i18n("shop.update.updateshoppropertyfail");
				}
		   }});
		});
		
		$j("#setspp_1").click(function(){
			var shopId = $j("#shopid").val();
			$j("#setspp_1").addClass("selected");
			window.location.href=base+"/shop/shopPropertymanager.htm?shopId="+shopId;
		});
		//顶部选项卡切换至修改店铺页面
		$j("#setspp_2").click(function(){
			var shopid = $j("#shopid").val();
			window.location.href=base+"/shop/updateShop.htm?shopid="+shopid;
		});
		
		//检查店铺编码是否具有唯一性
		$j("#shopcode").bind("blur",function()
		 {
			 var code = $j("#shopcode").val();
			 if(code.trim()=="")return;
			 if(oldval==$j("#shopcode").val()){
				 return ;
			 }else if(code==""||code==null){
	 			return nps.info(nps.i18n("SHOP_MANAGER_TIPS"),nps.i18n("shop.update.fillshopcode"));
	 		}else{
	 			var json={"shopcode":code};
	 		  	var _d = loxia.syncXhr(validateShopCodeUrl, json,{type: "GET"});
	 			if(_d.isSuccess == false){
	 			     $j("#shopcode").val(oldval);
	 				 $j("#loxiaTip-r").show();
	 				 $j(".inner.ui-corner-all").html(nps.i18n("shop.update.shopcode.enble"));
	 				 setTimeout(function(){ 
	 					$j("#loxiaTip-r").hide();
	 				 },4000); 
	 				 return;

	 			}else if(_d.isSuccess == true){
	 				 $j("#loxiaTip-r").show();
	 				 $j(".inner.ui-corner-all").html(nps.i18n("shop.update.shopcode.able"));
	 			     setTimeout(function(){ 
	 					$j("#loxiaTip-r").hide();
	 				 },2000); 
	 				 return;
	 			}
	 		}
		 });
		$j("#code").bind("focus",function()
				 {
				   $j("#loxiaTip-r").hide();
				 });
		 //返回
		$j(".button.goBack").click(function(){
			 window.location.href=base+"/shop/shopList.htm?keepfilter=true";
		});
		
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
	
