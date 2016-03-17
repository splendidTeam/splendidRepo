$j(document).ready(function(){
	setInterval('$j("#scrollDiv").find("ul:first").animate({marginTop:"-25px"},500,function(){$j(this).css({marginTop:"0px"}).find("li:first").appendTo(this);});',2500)
	
	$j(".cat-more2").mouseover (
	  function(){$j(".cat-hover2").removeClass("cat-hidden").addClass("cat-show")}
	  );
	$j(".cat-hover2").mouseover (
	  function(){$j(this).addClass("cat-show")}
	  );
	$j(".cat-hover2").mouseout (
	  function(){$j(this).removeClass("cat-show").addClass("cat-hidden")}
	  );
	
	$j(".cat-more3").mouseover (
	  function(){$j(".cat-hover3").removeClass("cat-hidden").addClass("cat-show")}
	  );
	$j(".cat-hover3").mouseover (
	  function(){$j(this).addClass("cat-show")}
	  );
	$j(".cat-hover3").mouseout (
	  function(){$j(this).removeClass("cat-show").addClass("cat-hidden")}
	  );
	
	$j(".cat-more5").mouseover (
	  function(){$j(".cat-hover5").removeClass("cat-hidden").addClass("cat-show")}
	  );
	$j(".cat-hover5").mouseover (
	  function(){$j(this).addClass("cat-show")}
	  );
	$j(".cat-hover5").mouseout (
	  function(){$j(this).removeClass("cat-show").addClass("cat-hidden")}
	  );
	
	$j(".cat-more6").mouseover (
	  function(){$j(".cat-hover6").removeClass("cat-hidden").addClass("cat-show")}
	  );
	$j(".cat-hover6").mouseover (
	  function(){$j(this).addClass("cat-show")}
	  );
	$j(".cat-hover6").mouseout (
	  function(){$j(this).removeClass("cat-show").addClass("cat-hidden")}
	  );
	//loadBulletin($j("#scrollDiv").attr("loadurl"));
	//loadHomeProductList($j("#List1").attr("loadurl"));
	/*GetObj("List2").innerHTML = GetObj("List1").innerHTML;
	loxia.fixPng($j("#List1").get(0));
    GetObj('ISL_Cont').scrollLeft = fill;
    GetObj("ISL_Cont").onmouseover = function(){clearInterval(AutoPlayObj);}
    GetObj("ISL_Cont").onmouseout = function(){AutoPlay();}
    AutoPlay();
	$j(".LeftBotton").mousedown(ISL_GoUp).mouseup(ISL_StopUp).mouseout(ISL_StopUp);
	$j(".RightBotton").mousedown(ISL_GoDown).mouseup(ISL_StopDown).mouseout(ISL_StopDown);
	
	var _wrap=$j('#scrollDiv ul');
	var _interval=1500;
	var _moving;
	_wrap.hover(function(){
		clearInterval(_moving);
	},function(){
		_moving=setInterval(function(){
			var _field=_wrap.find('li:first');
			var _h=_field.height();
			_field.animate({marginTop:-_h+'px'},600,function(){
			_field.css('marginTop',0).appendTo(_wrap);
			})
		},_interval)
	}).trigger('mouseleave');*/
	
	
	
	$j("#mainContent div.cb a").click(function(){
		trackNavigationalClick(this,"middle tout");
		trackCreativeBannerClick(this,$j(this).attr("banner"));
	});
	
	$j("#Map area").click(function(){
		trackNavigationalClick(this,"top tout");
		trackCreativeBannerClick(this,$j(this).attr("banner"));
	});
	
	$j("#banner a").click(function(){
		trackNavigationalClick(this,"top tout");
		trackCreativeBannerClick(this,$j(this).attr("banner"));
	});
	

	$j("#scrollDiv a").click(function(){
		//trackNavigationalClick(this,"bottom tout");
	});
	
	$j("#List1 a").click(function(){
		trackNavigationalClick(this,"gallery");
	});
	
	$j("#bottom-banner a.bb1").click(function(){
		trackNavigationalClick(this,"bottom tout");
	});
	
	$j("#bottom-banner a.bb2").click(function(){
		trackNavigationalClick(this,"bottom tout");
	});
	
	$j("#bottom-banner a.bb3").click(function(){
		trackNavigationalClick(this,"bottom tout");
	});
	
	$j("#bottom-banner a.bb4").click(function(){
		trackNavigationalClick(this,"bottom tout");
	});
	
//	s.prop17="home";
//	var s_code=s.t();if(s_code)document.write(s_code);
	
	
	/*--------------------------combine home_headline.js-----------------------------*/
	
	
	//首页KV延时加载并且延时滚动	
	
	$j('#banner').cycle({ //KV滚动
			fx:'scrollLeft',
			pager:'#btn'
	});
	
	
  currentList=-1;
  
  var searchNamevalue;
var _txt="";
$j("#btn-search-field").val(_txt).click(function(){	
							 var _tt=$j(this).val();
							 if(_tt==_txt)$j(this).val("")
							//$j("#s_m").css({"display":"block"});
							}).blur(function(){
   var _tt=$j(this).val();if(_tt=="")$j(this).val(_txt) 
   });
	$j(".s_m_li").click(function(){								
		$j("#s_m").css({"display":"none"});
		var item=$j(this).find("a").html();
		 onClickSuggestItem(item);
		 $j("#btn-search-field").val(item);
		// $j(this).parent().empty();
		 });
   $j(document).click(function(event){
		 
		 if(event.target!=$j("#btn-search-field")){
			 select_hide();
		 }
		
	});
      timecount=0;  
	$j("#btn-search-field1").keyup(function(event)
	{
			clearTimeout(timecount);
		 if((event.keyCode!=16)&&(event.keyCode!=17)&&(event.keyCode!=37)&&(event.keyCode!=38)&&(event.keyCode!=39)&&(event.keyCode!=40)){    //判断键盘事件,抛弃上下键跟回车键	
			timecount= setTimeout("skuSuggestResult()","2000");
		  }
		
	});
	
	function skuSuggestResult()
	{
	  var searchName=$j("#btn-search-field").val();   //alert("[searchName]"+searchName);
		  if($j.trim(searchName)=='')
		  {
			  select_hide();
			  return ;
		  }
		  searchNamevalue=searchName;
		  var data = loxia.syncXhr($j(document.body).attr("root") + "/product/searchName.json",{name:searchName,count:10});
		  // alert("[data]"+data.list);
			  if(typeof(data.list)!="undefined")
			  {
			  var count=data.list.length;
			 if(count>0)
				{
				  select_empty();
				  // alert("[count]"+data.list.length);
				 //var skuSuggest=data.list;
				// alert("[skuSuggest]"+skuSuggest);
			     $j.each(data.list,function(i,n){ 
				  search_result(n.name,n.id,n.searchCount);  
			     }); 
			     select_expand();
			     currentList=-1;
			     }else
			     {
			    	 select_hide();
			     }	
			}		     
	}
	
	$j(document).keydown(function(event){
	 var  liLength= $j(".s_m_container li").size();
	//alert("[liSize]"+liLength);
		if(liLength>0)
		{
		event = event || window.event;  //兼容多浏览器
		 //alert("[code]"+event.keyCode);
		  if(event.keyCode==38){ //监听向上键
				if(currentList<1)
				{
					currentList=liLength;
				}
				currentList--;       
			 var val=$j(".s_m_li").eq(currentList).find("a").html();
			 $j("#btn-search-field").val(val);	
			 $j(".s_m_li a").removeClass();//先清除样式 避免冲突                             
			 $j(".s_m_li a").eq(currentList).addClass("hover");
			  
		  }
			 
		 if(event.keyCode==40){ //监听向下键
			 if(currentList>=liLength-1)
			 {
				currentList=-1; 
			 }
			 currentList++;
			  var val=$j(".s_m_li").eq(currentList).find("a").html();
			 $j("#btn-search-field").val(val);
			 $j(".s_m_li a").removeClass();//先清除样式 避免冲突                             
			 $j(".s_m_li a").eq(currentList).addClass("hover");
		  }
		 if(event.keyCode==13){ //监听回车键	
			 
		  }	 

		} 
   });
});

function select_hide(){//隐藏
	$j("#s_m").css({"display":"none"});
	}


