$j(window).load(function(){
	$j(".web-update").css({"height":$j(".web-update").contents().find("body").height()});
	
	$j(".web-update").contents().find(".web-update-item").append('<div class="wui-tips" style="clear:both; display:none; position:absolute; width:50px; height:30px; right:0; top:0; z-index:100; background:#ff6600; color:#fff; line-height:30px; font-size:12px; text-align:center; cursor:pointer;">编辑</div>').hover(
		function(){
			$j(this).find(".wui-tips").css("display","block");
		},
		function(){
			$j(this).find(".wui-tips").css("display","none");
		}
	).find(".wui-tips").css("opacity","0.6").hover(
		function(){
			$j(this).css("opacity","1")
		},
		function(){
			$j(this).css("opacity","0.6")
		}
	).click(function(e){
		e.stopPropagation();
		
		var thisindex=$j(".web-update").contents().find(".web-update-item").find(".wui-tips").index($j(this));
		
		$j(".web-update-dialog").eq(thisindex).dialogff({type:'open',close:'in',width:'400px', height:'400px'});
	});
	
});