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
			if(!self.hasClass("end-sp")){
				$("<div class='black-opacityff' style='clear:both; display:none; position:absolute; width:100%; height:100%; left:0; top:0; background:#000; opacity:0.7; margin:0; padding:0;'></div>").css({"opacity":"0.7","height":$(document).height(),"z-index":zidx}).detach().appendTo("body").stop(true,true).fadeIn(300);

				self.addClass("end-sp").appendTo("<div class='dialog-content' style='clear:both; display:none; position:absolute; width:100%; margin:0; padding:0; left:0; top:0;'></div>").css({"display":"block","position":"relative","margin":"0 auto","left":0,"top":0,"margin-top":parseInt($(window).scrollTop())+100}).append("<div class='dialog-close'>X</div>").parent(".dialog-content").css("z-index",zidx+1).detach().appendTo("body").stop(true,true).fadeIn(300);
			}
			
			
			
			
			
			if(ssclose=='in'){
				self.find(".dialog-close").click(function(event){
					event.stopPropagation();
					var before_co=self.parent('.dialog-content');
					self.parent('.dialog-content').stop(true,true).fadeOut(100).prev(".black-opacityff").stop(true,true).fadeOut(100).remove();
					self.appendTo("body").stop(true,true).hide(function(){self.removeClass("end-sp");});
					before_co.remove();
					self.find(".dialog-close").remove();
					
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
			self.appendTo("body").stop(true,true).hide(function(){self.removeClass("end-sp");});
			before_co.remove();
			self.find(".dialog-close").remove();
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
//		console.log(zidx);
};
	$.fn.sAlert = function(obj){
	        var self = $(this)
	        ,ss={
	            type:'',
	            width:'350px',
	            height:'auto',
	            title:'',
	            content:'',
	            conf:''
	        };
	        $.extend(ss, obj);
	       
	        var sstype = ss.type;
	        var ssclose = ss.close;
	        var sswidth = ss.width;
	        var ssheight = ss.height;
	        var sstitle = ss.title;
	        var sscontent = ss.content;
	        var ssconf = ss.conf;
	       
	       
	        function saf(){
	            $("<div class='black-opacityff' style='clear:both; display:none; position:absolute; width:100%; height:100%; left:0; top:0; background:#000; opacity:0.7; margin:0; padding:0;'></div>").css({"opacity":"0.7","height":$(document).height(),"z-index":99990}).detach().appendTo("body").stop(true,true).fadeIn(300);
	               
	            $("<div class='dialog-content' style='clear:both; display:block; position:absolute; width:100%; margin:0; padding:0; left:0; top:0;'><div class='proto-dialog' style='display:block'><h5>"+sstitle+"</h5><p class='center-p'>"+sscontent+"</p><div class='proto-dialog-button-line'><a class='button blue-button s-confirm' href='javascript:void(0);'>确定</a></div><div class='dialog-close'></div></div></div>"
	            ).css({"z-index":"99995"}).find(".proto-dialog").css({"width":sswidth,"height":ssheight,"margin-top":parseInt($(window).scrollTop())+120}).parent(".dialog-content").detach().appendTo("body").find(".dialog-close").click(function(){
	                var before_co=$(this).parent().parent('.dialog-content');
	                $(this).parent().parent('.dialog-content').stop(true,true).fadeOut(100).prev(".black-opacityff").stop(true,true).fadeOut(100).remove();
	                before_co.remove();
	            }).siblings(".proto-dialog-button-line").find(".s-confirm").click(function(){
	                if(ssconf!=""){
	                    ssconf(self);
	                }
	               
	                var before_co=$(this).parent().parent().parent('.dialog-content');
	                $(this).parent().parent().parent('.dialog-content').stop(true,true).fadeOut(100).prev(".black-opacityff").stop(true,true).fadeOut(100).remove();
	                before_co.remove();
	            });
	        }
	       
	       
	        if(sstype=='open'){
	            saf();
	        }
	        else{
	            self.click(function(){
	                saf();
	            });
	
	        }
	       
	        $(window).resize(function(){
	            $(".black-opacityff").css({"height":$(document).height()});
	        });
	};
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	$.fn.sConfirm = function(obj){
	        var self = $(this)
	        ,ss={
	            type:'',
	            width:'300px',
	            height:'auto',
	            title:'',
	            content:'',
	            conf:''
	        };
	        $.extend(ss, obj);
	       
	        var sstype = ss.type;
	        var ssclose = ss.close;
	        var sswidth = ss.width;
	        var ssheight = ss.height;
	        var sstitle = ss.title;
	        var sscontent = ss.content;
	        var ssconf = ss.conf;
	       
	       
	        function scf(){
	            $("<div class='black-opacityff' style='clear:both; display:none; position:absolute; width:100%; height:100%; left:0; top:0; background:#000; opacity:0.7; margin:0; padding:0;'></div>").css({"opacity":"0.7","height":$(document).height(),"z-index":99980}).detach().appendTo("body").stop(true,true).fadeIn(300);
	               
	            $("<div class='dialog-content' style='clear:both; display:block; position:absolute; width:100%; margin:0; padding:0; left:0; top:0;'><div class='proto-dialog' style='display:block'><h5>"+sstitle+"</h5><p class='center-p'>"+sscontent+"</p><div class='proto-dialog-button-line'><a class='button blue-button s-confirm mr5' href='javascript:void(0);'>确定</a><a class='button grey-button s-cancel' href='javascript:void(0);'>取消</a></div><div class='dialog-close'></div></div></div>"
	            ).css({"z-index":"99985"}).find(".proto-dialog").css({"width":sswidth,"height":ssheight,"margin-top":parseInt($(window).scrollTop())+120}).parent(".dialog-content").detach().appendTo("body").find(".dialog-close").click(function(){
	                var before_co=$(this).parent().parent('.dialog-content');
	                $(this).parent().parent('.dialog-content').stop(true,true).fadeOut(100).prev(".black-opacityff").stop(true,true).fadeOut(100).remove();
	                before_co.remove();
	            }).siblings(".proto-dialog-button-line").find(".s-cancel").click(function(){
	                var before_co=$(this).parent().parent().parent('.dialog-content');
	                $(this).parent().parent().parent('.dialog-content').stop(true,true).fadeOut(100).prev(".black-opacityff").stop(true,true).fadeOut(100).remove();
	                before_co.remove();
	            }).siblings(".s-confirm").click(function(){
	                if(ssconf!=""){
	                    ssconf(self);
	                }
	               
	                var before_co=$(this).parent().parent().parent('.dialog-content');
	                $(this).parent().parent().parent('.dialog-content').stop(true,true).fadeOut(100).prev(".black-opacityff").stop(true,true).fadeOut(100).remove();
	                before_co.remove();
	            });
	        }
	       
	       
	        if(sstype=='open'){
	            scf();
	        }
	        else{
	            self.click(function(){
	                scf();
	            });
	
	        }
	       
	        $(window).resize(function(){
	            $(".black-opacityff").css({"height":$(document).height()});
	        });
	};
	
	$.fn.tabledrag = function(obj){
		var self = $(this);
		
		self.on("mousedown","tbody tr .cursor-hand",function(e){
			e.stopPropagation();
			
			
			if(e.button==0){
				$(this).parents("tr").addClass("dragtr");
				
				var ys=parseInt(e.clientY);
				var thishtml=$(this).parents("tr").html();
				
				$('<div id="tabledrag" style="display:none; clear:both; position:absolute; overflow:hidden; background:#fff; border:1px solid #dedede; z-index:50;"><table style="width:100%"><tr></tr></table><div>').appendTo("body").css({"width":self.outerWidth(),"left":self.offset().left,"top":ys+parseInt($(window).scrollTop()),"display":"block"});
				
				$("#tabledrag table tr").html(thishtml);
				$("#tabledrag table tr td").css({"text-align":"center","vertical-align":"middle","padding":"5px"});
				self.find("th").each(function(){
					var thiswidth=$(this).attr("width");
					var thisindex=self.find("th").index($(this));
					$("#tabledrag table tr td").eq(thisindex).attr("width",thiswidth);
				});
				$("body").addClass("cursor-pointer").addClass("unselect");
			}
		});
		
		$("body").mousemove(function(e){
			var ys2=parseInt(e.clientY);
			$("#tabledrag").css({"top":ys2+parseInt($(window).scrollTop())});
		});
		
		$("body").mouseup(function(e){
			self.removeClass("unselect").find("tbody").find("tr").each(function(){
				if(parseInt($("#tabledrag").offset().top)-parseInt($(this).offset().top)>0&&parseInt($("#tabledrag").offset().top)-parseInt($(this).offset().top)<parseInt($(this).outerHeight())){
					$(".dragtr").insertAfter($(this));
				}
				else if(parseInt($("#tabledrag").offset().top)-parseInt($(this).offset().top)<0&&$(this).index()==0){
					$(".dragtr").insertBefore($(this));
				}
			});
			
			self.find("tr").removeClass("dragtr");
			$("#tabledrag").remove();
			$("body").removeClass("cursor-pointer").removeClass("unselect");
		});
			
};

})(jQuery);


/**
 * 日期格式化
 		:new Date().format("yyyy-MM-dd hh:mm:ss");
 * @param format
 * @returns
 */
Date.prototype.format = function(format){ 
	var o = { 
		"M+" : this.getMonth()+1, //month 
		"d+" : this.getDate(), //day 
		"h+" : this.getHours(), //hour 
		"m+" : this.getMinutes(), //minute 
		"s+" : this.getSeconds(), //second 
		"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
		"S"  : this.getMilliseconds() //millisecond 
	};

	if(/(y+)/.test(format)) { 
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	} 

	for(var k in o) { 
		if(new RegExp("("+ k +")").test(format)) { 
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
		} 
	} 
	return format; 
};

/**
 * 处理缩略图的URL
 * 通过不同的图片的尺寸,生成相应的图片的URL
 * @param imgUrl
 * @param size
 * @returns
 */
function processImgUrl(imgUrl,size){
	 
	if(null==imgUrl||""==imgUrl){
		return defaultImg;
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
