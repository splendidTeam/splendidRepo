var zidx=100;
;(function($){
	$.fn.dialogff = function(obj){
			var self = $(this)
			,ss={
				type:'open',
				close:'in',
				width:'550px',
				height:'300px'
			};
			$.extend(ss, obj);
			
			var sstype = ss.type;
			var ssclose = ss.close;
			var sswidth = ss.width;
			var ssheight = ss.height;
			
			self.css({"width":sswidth,"height":ssheight});
			self.find(".proto-dialog-content").css("height",parseInt(self.height())-parseInt(self.find("h5").outerHeight())-parseInt(self.find(".proto-dialog-button-line").outerHeight()))
			
			if(sstype=='open'){

				$("<div class='black-opacityff' style='clear:both; display:none; position:absolute; width:100%; height:100%; left:0; top:0; background:#000; opacity:0.7; margin:0; padding:0;'></div>").css({"opacity":"0.7","height":$(document).height(),"z-index":zidx}).detach().appendTo("body").stop(true,true).fadeIn(300);

				self.appendTo("<div class='dialog-content' style='clear:both; display:none; position:absolute; width:100%; margin:0; padding:0; left:0; top:0;'></div>").css({"display":"block","margin-top":parseInt($(window).scrollTop())+100}).append("<div class='dialog-close'>X</div>").parent(".dialog-content").css("z-index",zidx+1).detach().appendTo("body").stop(true,true).fadeIn(300);
				
				zidx=zidx+2;
				
				if(ssclose=='in'){
					self.on("click",".dialog-close",function(event){
						event.stopPropagation();
						var before_co=self.parent('.dialog-content');
						self.parent('.dialog-content').stop(true,true).fadeOut(100).prev(".black-opacityff").stop(true,true).fadeOut(100).remove();
						self.appendTo("body").css("display","none");
						before_co.remove();
						self.find(".dialog-close").remove();
						zidx=zidx-2;
					});
				}
				else
				{
					
				}

			}
			else if(sstype=='close')
			{
				var before_co=self.parent('.dialog-content');
				self.parent('.dialog-content').stop(true,true).fadeOut(100).prev(".black-opacityff").stop(true,true).fadeOut(100).remove();
				self.appendTo("body").css("display","none");
				before_co.remove();
				self.find(".dialog-close").remove();
				zidx=zidx-2;
			}

			$(window).resize(function(){
				$(".black-opacityff").css({"height":$(document).height()});
			});
			
			var relatleft=0;
			var relattop=0;
			var x1=0;
			var y1=0;
			var x2=0;
			var y2=0;
			var dowidth=parseInt($(document).outerWidth());
			var doheight=parseInt($(document).outerHeight());
			self.find("h5").mousedown(function(e){
				relatleft=parseInt(self.offset().left);
				relattop=parseInt(self.offset().top);
				x1=e.clientX;
				y1=e.clientY;
				self.addClass("dialog-move").css({"position":"absolute","left":relatleft,"top":relattop,"margin":"0px"});
				
				if(self.hasClass("dialog-move")){
					$(document).mousemove(function(event){
						x2=event.clientX-x1;
						y2=event.clientY-y1;
						$("body").css("overflow","hidden");
						self.stop(true,true).animate({"left":relatleft+x2,"top":relattop+y2},0);
					});
				}
				
				
			}).mouseup(function(e){
				self.removeClass("dialog-move");
				$(document).unbind("mousemove"); 
				$("body").css("overflow","visible");
			});
	}
})(jQuery);





$j(document).ready(function(){
	if(parseInt($j(window).width())<1260){
		$j("header").addClass("header-screen");
		$j(".notice").addClass("notice-screen");
	}
	$j(window).resize(function(){
		if(parseInt($j(window).width())<1260){
			$j("header").addClass("header-screen");
			$j(".notice").addClass("notice-screen");
		}
		else{
			$j("header").removeClass("header-screen");
			$j(".notice").removeClass("notice-screen");
		}
	});
	
	/*tag*/
	$j(".ui-tag-change").each(function(){
		if(!$j(this).find(".tag-change-ul").find("li").hasClass("selected")){
			$j(this).find(".tag-change-ul").find("li").eq(0).addClass("selected");
			$j(this).find(".tag-change-content").find(".tag-change-in").eq(0).addClass("block");
		}
		
		$j(this).find(".tag-change-ul").find("li").on("click",function(){
            if($j(this).find("a").length == 0){
                var thisindex=$j(".ui-tag-change .tag-change-ul li").index($j(this));
                $j(this).addClass("selected").siblings("li").removeClass("selected");
                $j(".tag-change-in").eq(thisindex).addClass("block").siblings(".tag-change-in").removeClass("block");
            }
		});
	});
	/*tag-end*/
	
	
	/*head-right*/
	var settsss=0;
	$j(".head-dia-cur").mouseenter(function(){
		clearTimeout(settsss);
		var thisindex=$j(".head-dia-cur").index($j(this));
		
		if(!$j(this).hasClass("selected")){
			$j(".head-dia-cur").removeClass("selected");
			$j(this).addClass("selected");
			$j(".head-right-control-dialog").css("display","none").eq(thisindex).stop(true,true).slideDown(300);
		}
		
	});
	
	$j(".login-inform").mouseleave(function(){
		settsss=setTimeout(function(){
			$j(".head-dia-cur").removeClass("selected");
			$j(".head-right-control-dialog").stop(true,true).slideUp(300);
		},500);
	});
	$j(".login-inform").mouseenter(function(){
		clearTimeout(settsss);
	});
	/*head-right-end*/
});
