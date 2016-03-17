$j.extend(loxia.regional['zh-CN'],{
	"LABEL_MEMBER_FAVORITES_ITEMNAME":"商品名称",
	"LABEL_MEMBER_FAVORITES_CREATETIME" :"收藏时间",
	"LABEL_MEMBER_FAVORITES_PICURL":"商品主图", 
	"LABEL_MEMBER_FAVORITES_PRICE":"商品价格",
	"LABEL_MEMBER_CONSIGNEENAME":"收货人姓名",
	"LABEL_MEMBER_CONSIGNEEMAIL" :"收货人邮件",
	"LABEL_MEMBER_RECEIVERPHONE":"收货人电话",
	"LABEL_MEMBER_CONSIGNEEPHONE":"收货人手机",
	"LABEL_MEMBER_CONSIGNEEHOMEADDRESS" :"收货人家庭地址",
	"LABEL_MEMBER_CONSIGNEEDEFAULTADDRESS":"是否为默认地址",  
	//"LABEL_MEMBER_CONSIGNEEVERIFICATION" :"默认地址",
	//"LABEL_MEMBER_CONSIGNEEVERIFICATIONNO":"非默认地址", 
	 
	"NO_DATA":"无数据"
	
});
  
var memberFavoritesListUrl = base +'/member/memberFavoritesList.json';
var memberContactListUrl = base +'/member/memberContactList.json';
 
//获取日期格式
function formatDate(val){
	if(val==null||val==''){
		return "&nbsp;";
	}
	else{
		var date=new Date(val);
		return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
}
//显示图片
function drawpicURl(data, args, idx){
	var sku_pic=loxia.getObject("picUrl",data);
	var imgResult="";
	if(sku_pic!=null||sku_pic!=''){
		imgResult="<img src='"+customBaseUrl+sku_pic+"' width=60 height=60>";
	}  
	return imgResult;
}
function addressVerification(data, args, idx){
	var address="";
	var ifDefault=loxia.getObject("isDefault",data);
	if(ifDefault!=null||ifDefault!=''){  
		if(ifDefault)
			address="默认地址";
		else address="非默认地址"; 
	}
	return address;
}
//拼接收货地址
function getHomeAddress(val){ 
	if(val!=null||val!=''){   
		var d=val.country+val.province+val.city+val.area+val.town+val.address;
		return d;
	}
}

// 通过loxiasimpletable动态获取数据
$j(document).ready(function() {
	var memberId=$j("#memberId").val();
	loxia.init({
		debug : true,
		region : 'zh-CN'
	});
	nps.init();
 
	$j("#table2").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>', 
		cols : [ {
			          name:"name",
			          label:nps.i18n("LABEL_MEMBER_CONSIGNEENAME"),
			          width:"8%" 
			        } ,  {
			        	name:"email",
			        	label:nps.i18n("LABEL_MEMBER_CONSIGNEEMAIL"),
			        	width:"8%" 
			        },{
			        	name:"telphone",
			        	label:nps.i18n("LABEL_MEMBER_RECEIVERPHONE"),
			        	width:"8%" 
			        },{
			        	name:"mobile",
			        	label:nps.i18n("LABEL_MEMBER_CONSIGNEEPHONE"),
			        	width:"8%"
			        },{
			        	name:"ContactCommand",
			        	label:nps.i18n("LABEL_MEMBER_CONSIGNEEHOMEADDRESS"),
			        	width:"15%",
			        	template :"getHomeAddress"
			        },{
			        	name:"isDefault",
			        	label:nps.i18n("LABEL_MEMBER_CONSIGNEEDEFAULTADDRESS"),
			        	width:"7%", 
			        	template : "addressVerification"
			        }
				   ],
		dataurl : memberContactListUrl+'?memberId='+memberId
	});
	 $j("#table3").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>', 
		cols : [ {
			          name:"picUrl",
			          label:nps.i18n("LABEL_MEMBER_FAVORITES_PICURL"),
			          width:"10%",
			          template : "drawpicURl"
			        },  {
			           name:"itemName",
			           label:nps.i18n("LABEL_MEMBER_FAVORITES_ITEMNAME"),
			           width:"10%"
			        }, {
				           name:"salePrice",
				           label:nps.i18n("LABEL_MEMBER_FAVORITES_PRICE"),
				           width:"10%"
				    },  {
			        	name:"createTime",
			        	label:nps.i18n("LABEL_MEMBER_FAVORITES_CREATETIME"),
			        	width:"10%",
			            formatter:"formatDate"}
				   ],
		dataurl : memberFavoritesListUrl+'?memberId='+memberId
	});  
	$j("#table2").loxiasimpletable("refresh");
	$j("#table3").loxiasimpletable("refresh");
});