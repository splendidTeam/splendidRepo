function validate() {
	var detail_name = $.trim($("#detail_name").val());
	var detail_full_address = $.trim($("#detail_full_address").val());
	var detail_ename = $.trim($("#detail_ename").val());
	var detail_ename1 = $.trim($("#detail_ename1").val());
	var detail_ename2 = $.trim($("#detail_ename2").val());
	//var detail_address = $.trim($("#detail_address").val());
	//var detail_extension = $.trim($("#detail_extension").val());
	var detail_phone = $.trim($("#detail_phone").val());
	var detail_hours = $.trim($("#detail_hours").val());
	var detail_province = $.trim($("#_province").val());
	var detail_city = $.trim($("#_city").val());
	var detail_district = $.trim($("#_district").val());
//	var detail_image = $.trim($("#imagePath").val());
	
	if (detail_name=="") {
		alert("请输入店铺名称");
		return false;
	}
	if (detail_full_address=="") {
		alert("请输入店铺详细地址");
		return false;
	}
	if (detail_ename=="") {
		alert("请输入店铺英文名称");
		return false;
	}
	if (detail_ename1=="") {
		alert("请输入店铺英文名称1");
		return false;
	}
	if (detail_ename2=="") {
		alert("请输入店铺英文名称2");
		return false;
	}
	/* if (detail_address=="") {
		alert("请输入店铺简要地址");
		return false;
	} */
	if (detail_phone=="") {
		alert("请输入店铺电话");
		return false;
	}
	if (detail_hours=="") {
		alert("请输入店铺营业时间");
		return false;
	}
	if (detail_province=="选择省份") {
		alert("请选择省份");
		return false;
	}
	if (detail_city=="选择城市" || detail_city=="") {
		alert("请选择城市");
		return false;
	}
	if (detail_district=="选择地区" || detail_district=="") {
		alert("请选择地区");
		return false;
	}
	/*if (detail_image=="") {
		alert("请上传店铺图片");
		return false;
	}*/
	return true;
}

function addStoreLocator() {
	if (!validate()) {
		return;
	}
	
	var p = $("#_province").find("option:selected").text();
	var c = $("#_city").find("option:selected").text();
	$("#detail_province").attr("value",p);
	$("#detail_city").attr("value",c);
	
	$.ajax({
		type : "POST",
		url : base+"/offlineStore/addStore.json",
		data : $('#storeForm').serialize(),
		dataType : "text",
		success : function(data) {
			alert(data);
			if (data=="1") {
				alert("新增店铺成功");
				window.location=base+"/offlineStore/store.htm";
			}
		}
	});
}

function updateStoreLocator() {
	if (!validate()) {
		return;
	}
	
	var p = $("#_province").find("option:selected").text();
	var c = $("#_city").find("option:selected").text();
	$("#detail_province").attr("value",p);
	$("#detail_city").attr("value",c);
	
	$.ajax({
		type : "POST",
		url : base+"/offlineStore/updateStore.json",
		data : $('#storeForm').serialize(),
		dataType : "text",
		success : function(data) {
			if (data=="1") {
				alert("修改店铺成功");
				window.location.href=base+"/offlineStore/store.htm";
			}
		}
	});
}

	$(document).ready(function() {
		var t = $("#detail_id_type").val();
		$("#sub_store").click(function(){
			if (t=="add") {
				addStoreLocator();
			} else if (t=="update") {
				updateStoreLocator();
			}
		});
		
		/* $("a[id^='index_banner_update_order_']").live("click",function(){
			bannerUpdateOrder();
		}); */
		//$("#update_group").hide();
		//alert($("#editTable tr").eq(1).height());
		//alert($("#imageType_1").find("tr").size());
		var province = "${store.province}";
		var city = "${store.city}";
		var district = "${store.district}";
		$("#_province").find("option").each(function(index,obj){
			if (province==$(obj).text()){
				$(obj).attr("selected","selected");
				$("#_province").trigger("change");
			}
		});
		$("#_city").find("option").each(function(index,objCity){
			if (city==$(objCity).text()){
				$(objCity).attr("selected","selected");
				$("#_city").trigger("change");
			}
		});
		
		$("#_district").find("option").each(function(index,objDistrict){
			if (district==$(objDistrict).text()){
				$(objDistrict).attr("selected","selected");
			}
		});
		//店铺图片
		$("#storeImage-upload").uploadify({
			'width' : 150,  
			'swf' : base+'/scripts/uploadify/uploadify.swf',
			'buttonText' : '浏览', //按钮上的文字 
			'uploader' : base+'/offlineStore/uploadStoreImage.json;jsessionid='+$("#sessionId").val(),
			'fileTypeDesc' : '图片',//如果配置了以下的'fileTypeExts'属性，那么这个属性是必须的  
			'fileTypeExts' : '*.jpg;*.png;*.bmp', //允许的格式
			'method'   : 'Post',
			'formData' : {},
			'multi' : true,
			'queueSizeLimit' : 1,
			'onUploadStart' : function(file) {
				$("#storeImage-upload").uploadify('settings','formData' ,{'storeId':$("#detail_id").val()});
			},
			'onUploadError' : function(file, errorCode, errorMsg, errorString) {
	            alert('The file ' + file.name + ' could not be uploaded: ' + errorString);
	        },
	        'onUploadSuccess' : completeUploadStoreImage
		});
		//位置图片
		$("#mapImage-upload").uploadify({
			'width' : 150,  
			'swf' : base+'/scripts/uploadify/uploadify.swf',
			'buttonText' : '浏览', //按钮上的文字 
			'uploader' : base+'/offlineStore/uploadStoreImage.json;jsessionid='+$("#sessionId").val(),
			'fileTypeDesc' : '图片',//如果配置了以下的'fileTypeExts'属性，那么这个属性是必须的  
			'fileTypeExts' : '*.jpg;*.png;*.bmp', //允许的格式
			'method'   : 'Post',
			'formData' : {},
			'multi' : true,
			'queueSizeLimit' : 1,
			'onUploadStart' : function(file) {
				$("#mapImage-upload").uploadify('settings','formData' ,{'storeId':$("#detail_id").val()});
			},
			'onUploadError' : function(file, errorCode, errorMsg, errorString) {
				alert('The file ' + file.name + ' could not be uploaded: ' + errorString);
			},
			'onUploadSuccess' : completeUploadMapImage
		});
	});
	
	function completeUploadStoreImage(file, data, response) {
		if (response) {
			data=eval('('+data+')');
			if (data.flag) {
				//alert("上传完成，原文件名称：" + data.fileName+"，新文件名称："+data.complateServerPath);
				// 更改上传后的新图片文件名称
				$("#storeImagePath").val(data.complateServerPath);
				$("#storebanner_image_url").find("img").attr("src",base+"/offlineStore/getStoreImage.json?path="+data.complateServerPath);
			} else {
				if (data.warnMsg != null && data.warnMsg != ""){
					alert(data.warnMsg);
				}else {
					alert("上传文件出错");
				}
			}
		}
	}
	function completeUploadMapImage(file, data, response) {
		if (response) {
			data=eval('('+data+')');
			if (data.flag) {
				//alert("上传完成，原文件名称：" + data.fileName+"，新文件名称："+data.complateServerPath);
				// 更改上传后的新图片文件名称
				$("#mapImagePath").val(data.complateServerPath);
				$("#mapbanner_image_url").find("img").attr("src",base+"/offlineStore/getStoreImage.json?path="+data.complateServerPath);
			} else {
				if (data.warnMsg != null && data.warnMsg != ""){
					alert(data.warnMsg);
				}else {
					alert("上传文件出错");
				}
			}
		}
	}

	//如果点击‘导入文件’时选择文件为空，则提示
	function checkImport(id) {
		if ($.trim($(id).html()) == "") {
			alert('请先选择要上传的图片！');
			return false;
		}
		return true;
	}