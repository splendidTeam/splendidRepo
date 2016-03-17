$j(document).ready(function(){
	
	/*tag*/
	$j(".ui-tag-change").each(function(){
		if(!$j(this).find(".tag-change-ul").find("li").hasClass("selected")){
			$j(this).find(".tag-change-ul").find("li").eq(0).addClass("selected");
			$j(this).find(".tag-change-content").find(".tag-change-in").eq(0).addClass("block");
		}
		
		$j(this).find(".tag-change-ul").find("li").on("click",function(){
			var thisindex=$j(".ui-tag-change .tag-change-ul li").index($j(this));
			$j(this).addClass("selected").siblings("li").removeClass("selected");
			$j(".tag-change-in").eq(thisindex).addClass("block").siblings(".tag-change-in").removeClass("block");
		});
	});
	/*tag-end*/
	
	
	$j(".head-store").click(
		function(e){
			e.stopPropagation();
			$j(this).find("ul").css("display","inline");
			
			$j(".head-cust-inform>div").css("display","none");
			$j(".update-pwd").removeClass("block");
			$j(".per-inform").addClass("block");
		}
	);
	
	$j(".head-cust-inform").click(
		function(e){
			e.stopPropagation();
			$j(".head-cust-inform>div").css("display","block");
			
			$j(".head-store ul").css("display","none");
		}
	);
	$j(document).click(function(){
		$j(".head-store ul").css("display","none");
		$j(".head-cust-inform>div").css("display","none");
		$j(".update-pwd").removeClass("block");
		$j(".per-inform").addClass("block");
		
		$j(window.top.document).find(".head-store").find("ul").css("display","none");
		$j(window.top.document).find(".head-cust-inform>div").css("display","none");
		$j(window.top.document).find(".update-pwd").removeClass("block");
		$j(window.top.document).find(".per-inform").addClass("block");
	});
	
	
	
	/*percent-right*/
	$j(".percent70-content-right").css({"width":parseInt($j("body").outerWidth())-parseInt($j(".percent30-content-left").outerWidth())-20});
	$j(window).resize(function(){
		$j(".percent70-content-right").css({"width":parseInt($j("body").outerWidth())-parseInt($j(".percent30-content-left").outerWidth())-20});
	});
	/*percent-right-end*/
});
