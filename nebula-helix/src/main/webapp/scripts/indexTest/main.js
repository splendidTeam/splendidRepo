$j(document).ready(function(){
	
	
	
	
	
	/*neilyo_js*/
	$j("a").focus(function(){ 
		$j(this).blur();
	});
	
	$j(".esprit_nav ul li").mouseover(function(e){
		e.stopPropagation();
		$j(this).addClass("on");
	});
	$j(".esprit_nav ul li").mouseout(function(e){
		e.stopPropagation();
		$j(this).removeClass("on");
	});
	
	$j(".eh_right").hover(function(){
		var _price = $j(".en_price").html().split("&nbsp;")[0];
		if(_price==0){
			$j(".buy_car").css("display","none");
		}else{
			$j(".buy_car").css("display","block");
		}
		
	},function(){
		$j(".eh_right .buy_car").css("display","none");
	});
	
	/*$j(".eh_right").mouseover(
		function(){
			var _price = $j(".en_price").html().split("&nbsp;")[0];
			if(_price==0){
				$j(".buy_car").css("display","none");
			}else{
				$j(".buy_car").css("display","block");
			}
			
		}					 
	);
	
	$j(".eh_right").mouseout(
		function(){
			$j(".eh_right .buy_car").css("display","none");
		}					  
	);*/
	
	$j(".nav_seacrh_text").keypress(
		function(e){
			e.stopPropagation();
			$j(".search_result").css("display","block");
		}					  
	);
	
	$j(".search_result dd").click(
		function(){
			var srd_value=$j(this).text();
			$j(".nav_seacrh_text").val(srd_value);
		}						  
	);
	
	$j(document).click(
		function(){
			$j(".search_result").css("display","none");
		}			   
	);
	
	$j(".sorry_text").val("");
						  
	$j(".sorry_text,.stl_content span").click(
		function(event){
			event.stopPropagation();
			$j(".stl_content span").css("display","none");
			$j(".sorry_text").focus();
		}
	);
	
	$j(".sorry_text").blur(
	     function(){
			  var abc=$j(".sorry_text").val();	
		      if(abc==""){			  
				  $j(".stl_content span").css("display","block");
			  }
		 }				  
	);
	
	/*favor_select*/
	
	$j(".table_favor_update").click(
		 function(e){
			  e.stopPropagation();
			  var xzb=e.clientX;
			  var yzb=e.clientY;
			  var ksb=$j(window).scrollTop();
			  $j(".dialog_update_pen").detach().appendTo("body").fadeIn(500).css({top:yzb+ksb,left:xzb-"254"});
			  $j(".dia_size").fadeOut(15);
			  $j(".dialog_delete_cha").fadeOut(15);
		 }
	);

	
	$j(".color_se_g ul li").click(
		function(){
		     $j(".color_se_g ul li .whblin").removeClass("whblin2");
			 $j(this).find(".whblin").addClass("whblin2");
		}						
	);
	
	$j(".size_se_g ul li").click(
		function(){
		     $j(".size_se_g ul li .whblin").removeClass("whblin2");
			 $j(this).find(".whblin").addClass("whblin2");
		}						
	);
	
	$j(".color_secl").click(
		function(){
			 $j(".dia_size").slideUp(15);
			 $j(".color_se_g").slideDown(500);
		}			   
	);
	
	$j(".size_secl").click(
		function(){
			 $j(".dia_size").slideUp(15);
			 $j(".size_se_g").slideDown(500);
		}			   
	);
	
	
	
	$j(".size_btn_dia").click(
		function(){
			 var size_val=$j(".size_se_g .whblin2").next("span").html();
			 $j(".size_secl").html(size_val);
			 $j(".dia_size").slideUp(15);
		}					   
	);
	
	$j(".dup_confirm_one,.dup_cancel_one").click(function(){$j(".dialog_update_pen").fadeOut(15);});
	
	/*favor_select_end*/
	
	/*favor_delete*/
	$j(".table_favor_delete").click(
		 function(e){
			  e.stopPropagation();
			  var xzb=e.clientX;
			  var yzb=e.clientY;
			  var ksb=$j(window).scrollTop();
			  $j(".dialog_delete_cha").detach().appendTo("body").fadeIn(500).css({top:yzb+ksb,left:xzb-"346"});
			  $j(".dialog_update_pen").fadeOut(15);
		 }
	);
	
	
	$j(".dtb_yes,.dtb_no").click(function(){$j(".dialog_delete_cha").fadeOut(15);});
	
	$j(".dialog_update_pen,.dialog_delete_cha").click(function(e){e.stopPropagation();$j(this).css("display","block");});
		
	$j(document).click(function(){$j(".dialog_update_pen,.dialog_delete_cha").hide();
		$j('.color_secl').html('选择您要的颜色');
		$j('.size_secl').html('选择您要的尺寸');
		$j('.color_secl').attr("skuId","");
		$j('.size_secl').attr("sizeCode","");
	});
	/*favor_delete_end*/
	
	
	
	/*favor_left_lst_help*/
	/*var fllh=$j(".my_esprit_menu").height();
	var mrht=$j(".myfavorate_right").height();
	var meht=$j(".my_esprit_help").height();
	
	if(mrht>500||mrht==500){
		var aaaht=mrht-meht
		$j(".my_esprit_help").css("top",aaaht);
		if($j(".myfavorate_right").attr("class")=="myfavorate_right mypassword_right myaddress_right myinfo"){
			var myp_end_h = $j(".myp_end").height();
			aaaht = aaaht-myp_end_h+4;
			$j(".my_esprit_help").css("top",aaaht);
		}
	}
	if(mrht<500)
	{
		$j(".my_esprit_menu").css("height","500px");
		$j(".my_esprit_help").css("top","275px");
	}*/
	/*favor_left_lst_help_end*/
	
	
	$j(".zfb_address_btn").mouseover(
		function(){
			$j(".zfb_tanchu_hand").show();
		}								 
	);
	
	$j(".zfb_address_btn").mouseout(
		function(){
			$j(".zfb_tanchu_hand").hide();
		}								 
	);
	
	$j(".internet_bank_way a img").click(
		function(){
			$j(".internet_bank_way a img").css("border","1px solid #e5e5e5");
			$j(this).css("border","1px solid #f20026");
		}								 
	);
	
	/*neilyo_js_end*/
	
	
	
	
	/*footer*/
	
	$j(":input[rel]").click(function(){
		if($j("#exposeMask")){$j("#exposeMask").remove();}
	});
	
	$j(":input[rel]").overlay({mask: '#000', effect: 'apple'});
	
	$j(":input[rel]").click(function(){
		$j("#exposeMask").next("img").remove();
	});
	
	/*footer end*/
	
							
							
	
/**
 * 渲染商品分类左侧动作	
 * 需要使用到到各自页面加载
移到下面，需要使用此片段的同仁，请调用方法 navLeftHandle(),
注意：js中尽量将代码片段封装成一个个有意义的方法体		
*/			
/*function  navLeftHandle(){		
	nav_left first layer
	$j(".nav_left dl dt").click(function(event){
		  if($j(this).parent().hasClass("hover")){
			  $j(this).parent().removeClass("hover");
			  if($j(this).parent().find("dd")){
				  $j(this).parent().find("dd").find("ul").hide();
			  }
		  }else{
			  
			  
			  $j(this).parent().addClass("hover").siblings().removeClass("hover");
			  if($j(this).parent().find("dd")){
				  $j(this).parent().find("dd").find("ul").show();
			  }
		  }
	});
	
	nav_left second layer
	$j(".nav_left dl ul li a").click(function(event){
		  
		  if($j(this).parent().hasClass("hover")){
			  $j(this).parent().removeClass("hover");
		   	  $j(this).parent().find(".nav_item").hide();
		  }else{
			  $j(this).parent().addClass("hover").siblings().removeClass("hover");
			  $j(this).parent().find(".nav_item").show();
		  }
	});
	nav_left second layer(a modify span)
	$j(".nav_left dl ul li span").click(function(event){
		  
		  if($j(this).parent().hasClass("hover")){
			  $j(this).parent().removeClass("hover");
		   	  $j(this).parent().find(".nav_item").hide();
		  }else{
			  $j(this).parent().addClass("hover").siblings().removeClass("hover");
			  $j(this).parent().find(".nav_item").show();
		  }
	});
	$j(".nav_left dl ul li span").mouseover(function(event){
		$j(this).addClass("hover");
	});
	$j(".nav_left dl ul li span").mouseleave(function(event){
	    $j(this).removeClass("hover");
    });
	
	nav_left third layer
	$j(".nav_item dl dt").mouseover(function(event){
		   $j(this).siblings().removeClass("hover");
	   }
  	);
	$j(".nav_item dl dt").click(function(event){
		   $j(this).addClass("on").siblings().removeClass("on");
	   }
  	);
}	*/
	
	/*filter1 cat page*/
	$j(".pageurl .filter1 li").mouseover(function(event){
		   $j(this).find(".f_item").show();
	   }
  	);
	$j(".pageurl .filter1 li").mouseleave(function(event){
		    $j(this).find(".f_item").hide();
	   }
  	);
	
	$j(".pageurl .filter1 li dt").mouseover(function(event){
		   $j(this).addClass("hover");
	   }
  	);
	$j(".pageurl .filter1 li dt").mouseleave(function(event){
		    $j(this).removeClass("hover");
	   }
  	);
	
	$j(".pageurl .filter1 li dt").click(function(event){
		   $j(this).parent().parent().parent().parent().find(".f_a").html($j(this).find("a").html());
	   }
  	);
	
/*---------------------------------*/
	
		var list = $j('.new_1 a'), len = list.length, curIndex = 0;
		 function moveTo(){
		     list.fadeOut(2000).eq(curIndex).fadeIn(2000);
		   
		 }
		 setInterval(function(){
		    ++curIndex >= len && (curIndex = 0);
		   
		     moveTo();
		 }, 3000);

	
	
		 	var list2 = $j('.new_3 a'), len2 = list2.length, curIndex2 = 0;
			function moveTo2(){
			     list2.fadeOut(2000).eq(curIndex2).fadeIn(2000);
			   
			 }
			 setInterval(function(){
			    ++curIndex2 >= len2 && (curIndex2 = 0);
			   
			     moveTo2();
			 }, 2500);
//	$j(".sku_color a img").mouseover(function(event){
//		   
//		  var sku_name = $j(this).parent().attr("class");
//		  
//		  var obj_skupic = $j(this).parent().parent().parent().find(".sku_pic");
//		  obj_skupic.find(".img1").attr("src","shop/PicDestStyleOver_List_Cross/"+sku_name+".png");
//		  obj_skupic.find(".img2").attr("src","shop/PicDestStyleOver_List_CrossBack/"+sku_name+".png");
//		  
//		  var obj_skuclk = $j(this).parent().parent().parent().find(".sku_clk");
//		  obj_skuclk.find(".clk_pic img").attr("src","shop/PicDestStyleOver_List_CrossBack2/"+sku_name+".png");		   
//		   
//		   
//	   }
//  	);
	
	
	/*sku_color2 click*/
	$j(".sku_color2 a").click(function(event){
		$j(this).addClass("hover").siblings().removeClass("hover");
	});
	
	/*size select click
	$j(".sku_size dt").click(function(e){
		e.stopPropagation();
		if($j(this).hasClass("dtclick")){
			$j(this).parents(".sku_size").removeClass("sku_size_click");
			$j(this).removeClass("dtclick");
			$j(this).next("dd").removeClass("ddclick");
		}else{
			$j(this).parents(".sku_size").addClass("sku_size_click");
			$j(this).addClass("dtclick");
			$j(this).next("dd").addClass("ddclick");
		}
		$j(this).next("dd").toggle();
	});
	$j(".sku_size dd li").mouseover(function(){
		$j(this).addClass("hover");
	});
	
	$j(".sku_size dd li").mouseout(function(){
		$j(this).removeClass("hover");
	});
	$j(".sku_size dd li").click(function(e){
		e.stopPropagation();
		$j(this).parent().find("input").removeAttr("checked");
		$j(this).find("input").attr("checked","checked");
	});
	$j(".sku_size dd .btn_select").click(function(e){
		e.stopPropagation();
		var _size = $j(this).parents(".ddclick").find("input[name='rsize'][checked]").next("span").html();
		$j(this).parents(".sku_size").removeClass("sku_size_click").find(".dtclick").removeClass("dtclick").children(".ml10").html(_size);
		$j(this).parents(".ddclick").removeClass("ddclick").hide();
	});
	*/
	/*rcm sku price*/
	$j(".rcm dl dt").mouseover(function(){
		$j(this).next("dd").show();
	});
	
	$j(".rcm dl dt").mouseout(function(){
		$j(this).next("dd").hide();
	});	
	
	/*------by daemon for login----*/
	$j("a[rel]").click(function(){
		if($j("#exposeMask")){$j("#exposeMask").remove();}
	});
	$j("a:not([id=diaocha])[rel]").overlay({mask: '#000', effect: 'apple'});
	
	$j("a[id=diaocha][rel]").overlay({mask: '#000', effect: 'apple',closeOnClick: false, close:"#closet"});
	
	
	$j("a[rel]").click(function(){
		$j("#exposeMask").next("img").remove();
	});
	/*daemon end*/
	
	/*contactus Right ear*/
	$j(".contactus_rear").css("position","fixed");
	if(isie67() == 'IE 6.0'){
		$j(".contactus_rear").css("position","absolute");
		$j(window).scroll(function(){
			$j(".contactus_rear").css("top",($j(document).scrollTop()+205));
			$j(".contactus_rear").css("right",(-$j(document).scrollLeft()));
		});
		$j(window).resize(function(){
			$j(".contactus_rear").css("top",($j(document).scrollTop()+205));
			$j(".contactus_rear").css("right",0);
		});
	 }
	/*contactus Right ear*/
	$j(".contactus_rear").mousemove(function(){
		//$j(".contactus_rear").addClass("hover");
		if(isie67() == 'IE 6.0'){
			$j(this).parent("shape").hide();
		 }
	});

	$j(".contactus_rear").mouseout(function(){
		//$j(".contactus_rear").removeClass("hover");
		if(isie67() == 'IE 6.0'){
			$j(this).parent("shape").hide();
		 }
	});	
	
});


function refreshoverlay(){
	if($j("#exposeMask")){//Remove the initial pop-up layer
		$j("#exposeMask").remove();
	}										
	$j("input[rel]").overlay({mask: '#000', effect: 'apple'});//Pop-up layer
	$j("input[rel]").click(function(){//Popup pop-up a picture that no longer exists, to remove
		$j("#exposeMask").next("img").remove();
	});
}
/**
 * 注册尺码下拉列表显示或隐藏
 * @return
 */
function sizeLiHandle(){
	  /*size select click*/
		$j("#sizeListdt").unbind().click(function(e){
			e.stopPropagation();
			if($j(".sku_size dt").hasClass("dtclick")){
				$j(".sku_size dt").parents(".sku_size").removeClass("sku_size_click");
				$j(".sku_size dt").removeClass("dtclick");
				$j(".sku_size dt").next("dd").removeClass("ddclick");
			}else{
				$j(".sku_size dt").parents(".sku_size").addClass("sku_size_click");
				$j(".sku_size dt").addClass("dtclick");
				$j(".sku_size dt").next("dd").addClass("ddclick");
			}
			$j(".sku_size dt").next("dd").toggle();
		});
		$j(".sku_size dd li").mouseover(function(){
			$j(this).addClass("hover");
		});
		
		$j(".sku_size dd li").mouseout(function(){
			$j(this).removeClass("hover");
		});
		$j(".sku_size dd li").click(function(e){
			e.stopPropagation();
			$j(this).parent().find("input").removeAttr("checked");
			$j(this).find("input").attr("checked","checked");
		/*});
		$j(".sku_size dd .btn_select").click(function(e){
			e.stopPropagation();*/
			var _size = $j(this).parents(".ddclick").find("input[name='tmaskuSize'][checked]").next("span").html();
			$j(this).parents(".sku_size").removeClass("sku_size_click").find(".dtclick").removeClass("dtclick").children(".ml10").html(_size);
			$j(this).parents(".ddclick").removeClass("ddclick").hide();
		});
		$j(document).click(function(){
			/*sku_size hide*/
			var obj_sku_size = $j(".sku_size");
			obj_sku_size.removeClass("sku_size_click").find(".dtclick").removeClass("dtclick").children(".ml10");
			obj_sku_size.find(".ddclick").removeClass("ddclick").hide();
		});
		/*$j(document).click(function(){
			
			sku_size hide
			var obj_sku_size = $j(".sku_size");
			obj_sku_size.removeClass("sku_size_click").find(".dtclick").removeClass("dtclick").children(".ml10").html("请选择你的尺码");
			obj_sku_size.find(".ddclick").removeClass("ddclick").hide();
		});
		if($j(".dialog_sku").is(":hidden")){alert(123);
			sku_size hide
			var obj_sku_size = $j(".sku_size");
			obj_sku_size.removeClass("sku_size_click").find(".dtclick").removeClass("dtclick").children(".ml10").html("请选择你的尺码");
			obj_sku_size.find(".ddclick").removeClass("ddclick").hide();
		}*/
    }

function getSkus(url,_this){
	//基于需要f5刷新的需要，将局部刷新修改为页面跳转
	window.location.href=url;
}
	
//点击左侧商品分类。显示或隐藏子类
function navLeftSpanHandle(_this){
	  if($j(_this).parent().hasClass("hover")){
		  $j(_this).parent().removeClass("hover");
	   	  $j(_this).parent().find(".nav_item").hide();
	  }else{
		  $j(_this).parent().addClass("hover").siblings().removeClass("hover");
		  $j(_this).parent().find(".nav_item").show();
	  }
}

function isie67(){
	 var browser=navigator.appName;
	 if(browser=="Microsoft Internet Explorer"){
		 var b_version=navigator.appVersion;
		 var version=b_version.split(";");
		 
		 var trim_Version=version[1].replace(/[ ]/g,"");
		 if( trim_Version=="MSIE7.0"){
			 return "IE 7.0";
		 }else if( trim_Version=="MSIE6.0"){				 
			 return "IE 6.0";
		 }
	 }
}













