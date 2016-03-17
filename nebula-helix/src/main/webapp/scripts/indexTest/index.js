$j(document).ready(function(){	


	
	
	 var settime=null;
	  var indexs=0;
	  function fnStop(e){
		  clearInterval(settime);
		  indexs=$j(this).index();
	   	  moveTo(); 
	  }
	  function fnStart(){
		  clearInterval(settime);
		  settime=setInterval(function(){
				++indexs >= 8 && (indexs = 0);
				moveTo();
		  }, 3000)
	  }
	  function moveTo(){
	       $j('.aret_ptort_tiete > ul li').eq(indexs).addClass("cur").siblings().removeClass("cur");
	       $j('.morduct_t').hide();
	       $j('.morduct_t:eq('+indexs+')').show();
	  }
	  
	$j(".aret_ptort_tiete li").mouseenter(fnStop);
	$j("#are_pottet_all").hover(function(){
		clearInterval(settime);
	}, fnStart);
	fnStart();
	
	//首页kv轮播
	seamless_horizontal('#newers_siderst', {
	    'elements': '.newers_siderst_et ul li',
	    'scrolls': 3,
	    'visibles' : 3,	
	    'auto': 2000,
	    'preventDefault': false,     
	    'prev': '.cha_aleft_e',
	    'next': '.cha_arigt_e'
	 });	
});




fixedAdElement();
var seam = new seamless_horizontal('#sidli_chage', {
    'elements': '.lmoveap_t li',
    'focus': 'cur',
    'scrolls': 1,
    'auto': 2000,
    'preventDefault': false,
    
    'prev': '.cha_aleft',
    'next': '.cha_arigt'
});
seam.on('begin', function(c, i){
  
});
seam.on('done', function(c, o, n){
    $j('#sidli_chage .ctmt li').removeClass('cur').eq(c).addClass('cur');
});



var seam_t = new seamless_horizontal('#cent_texts', {
    'elements': 'div.c_p_al',
    'scrolls': 1,
    'auto': 2000,
    'preventDefault': false ,
    'prev': '.c_tex_tie a:eq(0)',
    'next': '.c_tex_tie a:eq(1)',
    isSeamless:false,
    lastIndex:1
    
});

seam_t.on('begin', function(c, i){
	    
});
seam_t.on('done', function(c, o, n){
    $j('#cent_texts .e_ctmt li').removeClass('cur').eq(c).addClass('cur');
});

function commandTab(container){
	  $j('.pdect_entex', container).delegate('li', 'mouseenter', function(){
	      var i = $j(this).index();
	      $j('.pdect_all_t li', container).hide().eq(i).show();
	      $j('.pdect_tr_all li', container).hide().eq(i).show();
	  });
}

$j('.product_M').each(function(){
  commandTab(this);
});

/* $j('.product_M .pdect_entex').mouseleave(function(){
  $j('.product_M .pdect_all_t li').hide().eq(0).show();
  $j('.product_M .pdect_tr_all li').hide().eq(0).show();
});*/

  var tab = function (container, options) {
    var config = {
        trigger: '',
        panles: '',
        cls: 'cur'
    }
    $j.extend(config, options);
    var t = $j(config.trigger, container), p = $j(config.panles, container), c = config.cls;
    $j(t).mouseenter(function () {
        var i = $j(this).index();
        $j(this).addClass(c).siblings().removeClass(c);
        $j(p).hide().eq(i).show();
    });
};
new tab('#ente_cha_priall', {
    trigger: '.productAreaHead ul li',
    panles: '.produe_all .product_M'
});


