var newarray=[];
;(function($){
	$.fn.dragarraylist = function(obj){
			var self = $j(this)
			, o = {
				newarray:[]
				, add:true,
				// callbacks
				
				beforecallBack:null, //点击选中前
				callBack:null, //点击选中时
				deleteli:null, //点击删除时
				draglist:null //拖拽完毕时
			};
			$.extend(o, obj);
			newarray = o.newarray;
			var isAdd = o.add;
			
			
				self.sortable({ 
					items: '.list-element',
					stop: function(event, ui) {
						eachaindex();
						o.draglist && $.isFunction(o.draglist) && o.draglist(obj);
					} 
				});
					
				self.disableSelection();

				
				var strli=0;
				
				
				$j.each(newarray,function(e,val){
					strli+=
					'<li id="'+val.id+'" scn=""><a href="javascript:void(0)"><span class="num">'+val.num+'</span>'+
					'<span class="text" title="">'+val.text+'</span>'+
					'<span class="type">'+val.type+'</span>'+
					'<span class="id">'+val.id+'</span>'+
					'<span class="state">'+val.state+'</span>'+'<em></em>'+
					'</a></li>';
				});
				
				
				
				$j(strli).appendTo(self).on("click",function(){
					var nosignlength=$j(".no-sign").length;
					if(nosignlength>0){
						o.beforecallBack && $.isFunction(o.beforecallBack) && o.beforecallBack(obj);
					}
					else{
						$j(this).addClass("selected").siblings("li").removeClass("selected");
						
						
						$j(this).find("em").on("click",function(e){
							e.stopPropagation();
							
							
							
							if($j(".list-all li.selected").prev("li").length>0){
								var beforeli=$j(".list-all li.selected").prev("li").attr("id");
								$j("#idvalue").val(beforeli)
							}
							else if($j(".list-all li.selected").prev("li").length<=0&&$j(".list-all li.selected").next("li").length>0){
								var nextli=$j(".list-all li.selected").next("li").attr("id");
								$j("#idvalue").val(nextli)
							}
							
							
						
							
							eachaindex();
							o.deleteli && $.isFunction(o.deleteli) && o.deleteli(obj);
						});
						
						
						var cid=parseInt($j(this).find(".id").text());
						var obj = null;
						
						for(var i=0;i<newarray.length;i++){
							if(newarray[i].id==cid){
								
								obj= newarray[i];
								break;
							}
						}
						
						o.callBack && $.isFunction(o.callBack) && o.callBack(obj);
					}
				});
				
				var scn=0;
				self.find("li").each(function(){
					if($j(this).find(".state").text()=='1'){
						$j(this).addClass("list-element");
					}
					else
					{
						$j(this).addClass("no-element").detach().appendTo(self);
					}
					
					$j(this).attr("scn",scn);
					scn=scn+1;
				});
				eachaindex();
			
				
				$j('<div class="new-list-add"><label>+</label><span>新增</span></div>').insertAfter(self).click(function(){
				if(newarray.length==self.find("li").length){
					self.find("li").removeClass("selected");
					$j(".percent70-content-right .ui-block-line").eq(0).find(".ui-loxia-default").focus();
					
					$j('<li id="" scn="" class="list-element no-sign"><a href="javascript:void(0)"><span class="num">'+'</span>'+
					'<span class="text" title="">未定义名称</span>'+
					'<span class="type">未定义类型</span>'+
					'<span class="id">none</span>'+
					'<span class="state">1</span>'+'<em></em>'+
					'</a></li>').attr("scn",scn).addClass("selected").appendTo(self).on("click",function(){
						$j(this).addClass("selected").siblings("li").removeClass("selected");
						
						
						$j(this).find("em").on("click",function(e){
							e.stopPropagation();
							
							if($j(".list-all li.selected").prev("li").length>0){
								var beforeli=$j(".list-all li.selected").prev("li").attr("id");
								$j("#idvalue").val(beforeli)
							}
							else if($j(".list-all li.selected").prev("li").length<=0&&$j(".list-all li.selected").next("li").length>0){
								var nextli=$j(".list-all li.selected").next("li").attr("id");
								$j("#idvalue").val(nextli)
							}
							
							eachaindex();
							o.deleteli && $.isFunction(o.deleteli) && o.deleteli(obj);
						});
						
						
						var cid=parseInt($j(this).find(".id").text());
						var obj = null;
						
						for(var i=0;i<newarray.length;i++){
							if(newarray[i].id==cid){
								
								obj= newarray[i];
								break;
							}
						}
						
						o.callBack && $.isFunction(o.callBack) && o.callBack(obj);
					}).trigger("click");
					
					
					$j(".no-element").detach().appendTo(self);
					eachaindex();
					scn=scn+1;
					
					self.find(".text").each(function(){
						$j(this).attr("title",$j(this).text());
					});
				}

				});
				
				
				self.find(".text").each(function(){
					$j(this).attr("title",$j(this).text());
				});
				
				
				if(!isAdd){
					$j(".new-list-add").hide();
				}
				
				
				function eachaindex(){
					var aindex=1;
					
					self.find("li").each(function(){
						$j(this).find(".num").text(aindex);
						aindex=aindex+1;
						
					});
				}
				
	}

})(jQuery);


function savelistarry(){
	var ain=1;
	
	$j(".list-all li").each(function(){
		var scnum=parseInt($j(this).attr("scn"));
		newarray[scnum].num=ain;
		ain++;
	});	
	
}