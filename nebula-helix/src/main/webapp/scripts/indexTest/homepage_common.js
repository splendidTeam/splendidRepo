var key_search_default='输入关键词';
$j(document).ready(function() {
	//购物车去结账
	$j("#header_payment").click(
	   function(){
		   $j("#header_payment_form").submit();
		   
	   }		
	);
	//处理女装男装首页搜索框样式
	$j("#women_search,#men_search").focus(function(){$j(this).css('color','#000');});
	//加载默认提示
	$j("#text_keyword").val(key_search_default);
	//点击搜索输入框，改变样式、清空默认提示；输入框失去焦点，恢复样式、提示信息等
	$j("#text_keyword").focus(function(){
		 $j(this).css('color','#000');
		var keyword=$j.trim($j(this).val());
		if(null!=keyword &&key_search_default==keyword){	
			$j(this).val('');
		}
	}).blur(function(){
		var keyword=$j.trim($j(this).val());
		if(null==keyword ||""==keyword){	
			$j(this).val(key_search_default);
		}
		 $j(this).css('color','#B1B1B1');
	});
	//点击搜索按钮，提交form
	$j("#btn_forsearch").click(function(){
		var keyword=$j.trim($j("#text_keyword").val());
		if(null!=keyword &&""!=keyword&&key_search_default!=keyword){	
			$j("#text_keyword").val(char_cv(keyword));
			$j("#form_search").submit();
		}
	});
	
	
	
	
	
	
	
	
	
	/*20120724*/
	
	
	$j(".remen_one").hover(function(){
		$j(this).addClass("hover2");
		$j(this).find("shape").eq(1).find("fill").attr("src",$j(this).find("h3 div").attr("rel1"));
		//src请改为动态获取！！！！！！！！！
	},function(){
		$j(this).removeClass("hover2");
		$j(this).find("shape").eq(1).find("fill").attr("src",$j(this).find("h3 div").attr("rel2"));
		//src请改为动态获取！！！！！！！！！
	});
	
	/*rexiao js*/
	$j(".rexiao .rexiao_top dt").hover(function(){
		if($j(this).hasClass("bgred")){
			$j(this).addClass("hover3").siblings().removeClass("on3").removeClass("on1");
		}else{
			$j(this).addClass("hover1").siblings().removeClass("on3").removeClass("on1");
		}
		var _index = $j(this).index();
		$j(this).parents(".rexiao").find(".rexiao_cont dd").eq(_index).show().siblings().hide();
		//动态加载图片
		$j(this).parents(".rexiao").find(".rexiao_cont dd").eq(_index).find("li").each(function(){
			if($j(this).find("img").attr("need-load")=="no")
				$j(this).find("img").attr("src",$j(this).find("img").attr("lazy-src"));
		});
	},function(){
		if($j(this).hasClass("bgred")){
			$j(this).removeClass("hover3");
		}else{
			$j(this).removeClass("hover1");
		}
	});
	
	$j(".cx_img").hover(function () {
		if(!$j(this).find(".abc1").is(":hidden")){
			$j(this).find(".abc1").hide();
			$j(this).find(".abc2").show();
		}else{
			$j(this).find(".abc2").hide();
			$j(this).find(".abc1").show();
		}
	});
	
	
	
	
	
	$j(".rexiao .rexiao_cont dd").mousemove(function(){
		var _index = $j(this).index();
		var _this = $j(this).parents(".rexiao").find(".rexiao_top dt").eq(_index);
		if(_this.hasClass("bgred")){
			_this.addClass("on3").siblings().removeClass("on3").removeClass("on1");
		}else{
			_this.addClass("on1").siblings().removeClass("on3").removeClass("on1");
		}
	});
	$j(".rexiao").mouseout(function(){
		var _index = $j(this).find(".rexiao_cont dd:visible").index();
		var _this = $j(this).find(".rexiao_top dt").eq(_index);
		if(_this.hasClass("bgred")){
			_this.addClass("on3").siblings().removeClass("on3").removeClass("on1");
		}else{
			_this.addClass("on1").siblings().removeClass("on3").removeClass("on1");
		}
	});
	$j(".btnright").click(function(){
		var _index = $j(this).parents(".rexiao").find(".rexiao_cont dd:visible").index();
		var _thisdt = $j(this).parents(".rexiao").find(".rexiao_top dt").eq(_index).next();
		var _thisdd = $j(this).parents(".rexiao").find(".rexiao_cont dd").eq(_index).next();
		if(_index==5){
			_thisdt = $j(this).parents(".rexiao").find(".rexiao_top dt").eq(0);
		    _thisdd = $j(this).parents(".rexiao").find(".rexiao_cont dd").eq(0);
		}
		if(_thisdt.hasClass("bgred")){
			_thisdt.addClass("on3").siblings().removeClass("on3").removeClass("on1");
			_thisdd.show().siblings().hide();
		}else{
			_thisdt.addClass("on1").siblings().removeClass("on3").removeClass("on1");
			_thisdd.show().siblings().hide();
		}
	});
	$j(".btnleft").click(function(){
		var _index = $j(this).parents(".rexiao").find(".rexiao_cont dd:visible").index();
		var _thisdt = $j(this).parents(".rexiao").find(".rexiao_top dt").eq(_index).prev();
		var _thisdd = $j(this).parents(".rexiao").find(".rexiao_cont dd").eq(_index).prev();
		if(_index==0){
			_thisdt = $j(this).parents(".rexiao").find(".rexiao_top dt").eq(5);
		    _thisdd = $j(this).parents(".rexiao").find(".rexiao_cont dd").eq(5);
		}
		if(_thisdt.hasClass("bgred")){
			_thisdt.addClass("on3").siblings().removeClass("on3").removeClass("on1");
			_thisdd.show().siblings().hide();
		}else{
			_thisdt.addClass("on1").siblings().removeClass("on3").removeClass("on1");
			_thisdd.show().siblings().hide();
		}
	});
	/*rexiao js end*/
	
	/*colorsize pic change*/
	/*$j(".lianyi .colorsize a").mousemove(function(){
		var _num = $j(this).find("img").attr("src").split(".jpg")[0].split("_")[1];
		var _a = $j(this).parents("li").find(".ld3pic");
		var _src = _a.find("img").attr("src").split("_")[0];
		_a.find("img").remove();
		$j('<img width="212" height="319" />').attr("src",_src+"_"+_num+".jpg").appendTo(_a);
	});*/
	
	/*backtotopld3*/
	$j(window).scroll(function(){
		if($j(".backtotopld3")&&$j(document).scrollTop()<=0){
			$j(".backtotopld3").hide();
		}else{
			if($j(window).width()<=1100){
				var _offset = $j(".content1000").offset();
				if(isie60() == 'IE 6.0'){
					var _top = $j(window).height()+$j(document).scrollTop()-90;
					$j(".backtotopld3").css({ "position":"absolute", "right":10, "bottom":"auto",'top':_top}).show();
					return false;
				}
				$j(".backtotopld3").css("right",(10)).show();
			}else{
				var _offset = $j(".content1000").offset();
				if(isie60() == 'IE 6.0'){
					var _right = $j(window).width()-(_offset.left+1000+40);
					var _top = $j(window).height()+$j(document).scrollTop()-90;
					$j(".backtotopld3").css({ "position":"absolute", "right":_right, "bottom":"auto",'top':_top}).show();
					return false;
				}
				$j(".backtotopld3").css("right",(_offset.left-34)).show();
			}
		}
	});
	$j(window).resize(function(){
		if($j(".backtotopld3")&&$j(document).scrollTop()<=0){
			$j(".backtotopld3").hide();
		}else{
			if($j(window).width()<=1100){
				var _offset = $j(".content1000").offset();
				if(isie60() == 'IE 6.0'){
					var _top = $j(window).height()+$j(document).scrollTop()-90;
					$j(".backtotopld3").css({ "position":"absolute", "right":10, "bottom":"auto",'top':_top}).show();
					return false;
				}
				$j(".backtotopld3").css("right",(10)).show();
			}else{
				var _offset = $j(".content1000").offset();
				if(isie60() == 'IE 6.0'){
					var _right = $j(window).width()-(_offset.left+1000+40);
					var _top = $j(window).height()+$j(document).scrollTop()-90;
					$j(".backtotopld3").css({ "position":"absolute", "right":_right, "bottom":"auto",'top':_top}).show();
					return false;
				}
				$j(".backtotopld3").css("right",(_offset.left-34)).show();
			}
		}
	});
	if($j(".backtotopld3")&&$j(document).scrollTop()<=0){
		$j(".backtotopld3").hide();
	}
	/*right ear banner*/
	if(isie60() == 'IE 6.0'&&$j(".banner_right")){
		var _top = $j(window).height()-176;
		$j(".banner_right").css({ "position":"absolute", "right":0, "bottom":"auto",'top':_top});
	}
	$j(window).scroll(function(){
		if(isie60() == 'IE 6.0'&&$j(".banner_right")){
			var _top = $j(window).height()+$j(document).scrollTop()-176;
			$j(".banner_right").css({ "position":"absolute", "right":0, "bottom":"auto",'top':_top});
		}
	});
	$j(window).resize(function(){
		if(isie60() == 'IE 6.0'&&$j(".banner_right")){
			var _top = $j(window).height()+$j(document).scrollTop()-176;
			$j(".banner_right").css({ "position":"absolute", "right":0, "bottom":"auto",'top':_top});
		}
	});
	$j(".banner_right .close").click(function(){
		$j(this).parents(".banner_right").hide();
	});
	
	function isie60(){
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
	/*backtotopld3 end,right ear banner end*/
	/*20120724 end*/

	/*----------- book -------------------*/
	
	$j("#email_1").focus(function(){
		if(!checkEmail($j(this).val())){
			$j(this).val("");
		}
	});
	
	$j("#idCommit1_1").click(function(){
		//$j(".prompt271_2").show();
		var _email = $j.trim($j("#email_1").val());
		var url = $j(this).attr("loadUrl");
		var sex = "women";
		if(checkEmail(_email)){
			bookemail(url, sex, _email)
		}else{
			alert("请输入正确的邮箱地址");
		}
	});
	
	$j("#idCommit1_2").click(function(){
		//$j(".prompt271_2").show();
		var _email = $j.trim($j("#email_1").val());
		var url = $j(this).attr("loadUrl");
		var sex = "man";
		if(checkEmail(_email)){
			bookemail(url, sex, _email)
		}else{
			alert("请输入正确的邮箱地址");
		}
	});
	
	$j("#idCommit1").click(function(){
		//$j(".prompt271_2").show();
		var _email = $j.trim($j("#email").val());
		var url = $j(this).attr("loadUrl");
		var sex = "women";
		if(checkEmail(_email)){
			$j(".prompt271_4").hide();
			bookemail(url, sex, _email)
		}else{
			$j(".prompt271_4").show();
		}
	});
	
	$j("#idCommit2").click(function(){
		//$j(".prompt271_2").show();
		var _email = $j.trim($j("#email").val());
		var url = $j(this).attr("loadUrl");
		var sex = "man";
		if(checkEmail(_email)){
			$j(".prompt271_4").hide();
			bookemail(url, sex, _email)
		}else{
			$j(".prompt271_4").show();
		}
	});
	
	$j(".prompt271_4 .close3").click(function(){
		$j(".prompt271_4").hide();
	});
	/*----------- book -------------------*/
});



function checkEmail(b) {
	var a = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
	return a.test(b.replace(/(^\s*)|(\s*$)/g, ""));
}

function bookemail(url,sex,email){
//	alert(url+","+sex+","+email);
	$j.ajax({
	 url:loxia.encodeUrl(url),
	 data:{email:$j.trim(email),sex:sex},
	 dataType:"json",
	 success:function(data){
		 if(data.result=="success"){
			 $j("#mailAdress").html(email);
			 $j("#mies1").overlay({mask: '#000', effect: 'apple'});
			 $j("#mies1").overlay().load();
			 $j("#exposeMask").next("img").remove();
		 }else if(data.result=="existed"){
			 $j(".mailAdress").html(email);
			 $j("#mies2").overlay({mask: '#000', effect: 'apple'});
			 $j("#mies2").overlay().load();
			 $j("#exposeMask").next("img").remove();
		 }else{
			 $j(".mailAdress").html("订阅邮件失败");
			 $j("#mies2").overlay({mask: '#000', effect: 'apple'});
			 $j("#mies2").overlay().load();
			 $j("#exposeMask").next("img").remove();; 
		 }
	 },
	 error:function(data){
		 alert(data);
	 }
	});
}





//注册首页季度最受欢迎搜索框的事件
function handleSearchByKey(dom){
	$j(dom).val('');
}
//根据关键字搜索，主要用于首页季度最受欢迎处
function searchByKey(dom){
	$j('#text_keyword').val(char_cv($j('#'+dom).val()));
		$j("#form_search").submit();
}
//转义特殊字符  
function char_cv(str){  
    if (str != ''){  
        str = str.replace(/</g,'&lt;');  
        str = str.replace(/%3C/g,'&lt;');  
        str = str.replace(/>/g,'&gt;');  
        str = str.replace(/%3E/g,'&gt;');  
        str = str.replace(/'/g,'&#39;');  
        str = str.replace(/"/g,'&quot;');  
    }  
    return str;  
} 
//再次搜索
function searchAgain(){
	var keyword=$j(".sorry_text").val();
	if(null!=keyword &&""!=keyword){				
		$j(".sorry_text").val(char_cv(keyword));
		$j("#form_search_again").submit();
	}
}