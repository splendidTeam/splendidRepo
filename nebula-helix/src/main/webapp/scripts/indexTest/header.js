$j(document).ready(function(){
	$j("#header_payment").click(
	   function(){
		   $j("#header_payment_form").submit();
		   
	   }		
	);
	
	/*$j(".esprit_nav ul li").mouseover(function(){
			
		$j(this).index() == 4 && $j('.esn_nav_title', this).css('color', '#e5304d');


	})

		$j(".esprit_nav ul li").mouseleave(function(){
			if (!window.top.location.href.match('sale.htm') && !window.top.location.href.match('2-----.htm')){

				 $j('.esn_nav_title', this).css('color', '#8A827E');
			
			}
	
		})*/
	
});

function ScrollImgLeft(){
	var speed=20;
	var scroll_begin = document.getElementById("scroll_begin");
	var scroll_end = document.getElementById("scroll_end");
	var scroll_div = document.getElementById("scroll_div");
	scroll_end.innerHTML=scroll_begin.innerHTML;
	function Marquee(){
		if(scroll_end.offsetWidth-scroll_div.scrollLeft<=0)
		  scroll_div.scrollLeft-=scroll_begin.offsetWidth;
		else
		  scroll_div.scrollLeft++;
	}
	var MyMar=setInterval(Marquee,speed);
	scroll_div.onmouseover=function() {clearInterval(MyMar);}
	scroll_div.onmouseout=function() {MyMar=setInterval(Marquee,speed);}
}