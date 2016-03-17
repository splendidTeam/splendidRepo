function loadLastView(){
	if($j("#history").size()>0){
		var loadurl = $j("#history").attr("loadurl");
		loxia.asyncXhr(loadurl,{},
				{success : function(data, textStatus){
					if(data && data.html){
						$j("#history>#id_recommend_info").html(data.html);		
						loxia.fixPng($j("#sku_recommend>#id_recommend_info").get(0));						
					}
				}});
	}
}

$j(document).ready(function(){
	
	//fix cache problem
	$j("a.dynamic").click(function(evt){
		evt.preventDefault();
		window.location.href=loxia.encodeUrl($j(this).attr("href"),true);
		return false;
	});
    //退出
	
	$j("#logOut a").click(function(evt){
		evt.preventDefault();
		loxia.asyncXhr($j(this).attr("href"),{da:new　Date().getTime()},{
			success: function(data,textStatus){
				if(data && data.result){
					
					 alert("您已成功登出，点击确定按钮将返回商城首页");
					window.location.href = $j(document.body).attr("root") + "/index.htm";
					//$j(".icon-logout").addClass("hidden");
					//$j(".icon-login").removeClass("hidden");
				}
			}
		});
	});
	$j("#espritMember").hide();
	$j("#logOut").hide();

	//读取用户名
	var data = loxia.syncXhr($j(document.body).attr("root") + "/member/member.json",{da:new　Date().getTime()});
	if(data && data.member&&data.member.id){		
		if(data.member.status!=50 && data.member.status!=4){
			$j("#commonMember").hide();
			$j("#login").hide();
			$j("#register").hide();
			$j("#meberInfoTmp").show();
			$j("#meberInfoTmp").html("欢迎您，"+data.member.realName).attr("title",data.member.realName);
			$j("#espritMember div").html("欢迎您，"+data.member.realName).attr("title",data.member.realName+"");
			$j("#myEspritWelcome").html("热烈欢迎您,"+data.member.realName+"!");
			$j("#espritMember").show();
			$j("#logOut").show();
		}else{
			$j("#login").show();
			$j("#register").show();
		}
	   }else{
			$j("#login").show();
			$j("#register").show();
	   }
	loadLastView();
});

//
function stopBubble(e){
	if(document.all&&event.srcElement.tagName!='AREA')
		{
			window.open(e.href,'_blank','');
			//top.location.href = e.href;
		}
}