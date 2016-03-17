<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<script type="text/javascript">

function uploadAndSubmit() { 
	 var form = document.forms["demoForm"]; 
	    
	 if (form["file"].files.length > 0) { 
	 // 寻找表单域中的 <input type="file" ... /> 标签
	 var file = form["file"].files[0]; 
	 // try sending 
	 var reader = new FileReader(); 

	 reader.onloadstart = function() { 
	 // 这个事件在读取开始时触发
	 console.log("onloadstart"); 
	 document.getElementById("bytesTotal").textContent = file.size; 
	 } 
	 reader.onprogress = function(p) { 
	 // 这个事件在读取进行中定时触发
	 console.log("onprogress"); 
	 document.getElementById("bytesRead").textContent = p.loaded; 
	 } 

	 reader.onload = function() { 
	    // 这个事件在读取成功结束后触发
	 console.log("load complete"); 
	 } 

	 reader.onloadend = function() { 
	    // 这个事件在读取结束后，无论成功或者失败都会触发
	 if (reader.error) { 
	 console.log(reader.error); 
	 } else { 
	 document.getElementById("bytesRead").textContent = file.size; 
	 
	 
	 
	 // 构造 XMLHttpRequest 对象，发送文件 Binary 数据
	 var xhr = new XMLHttpRequest(); 
	 xhr.open(/* method */ "POST", 
	 /* target url */ "/demo/upload.json?fileName=" + file.name 
	 /*, async, default to true */); 
	 xhr.overrideMimeType("application/octet-stream"); 
	// xhr.sendAsBinary(reader.result);
	xhr.sendAsBinary(reader.result);
	
	
	
	 xhr.onreadystatechange = function() { 
	 if (xhr.readyState == 4) { 
	 if (xhr.status == 200) { 
	 console.log("upload complete"); 
	 console.log("response: " + xhr.responseText); 
	 } 
	 } 
	 } 
	 } 
	 } 

	 reader.readAsBinaryString(file); 
	 } else { 
	 alert ("Please choose a file."); 
	 } 
	 } 
</script>

<script type="text/javascript">

function getXMLObject(){
	
	return new XMLHttpRequest();
}

function getBinary(file){
	var xhr = new XMLHttpRequest();
	xhr.open("GET", file, false);
	xhr.overrideMimeType("text/plain; charset=x-user-defined");
	xhr.send(null);
	return xhr.responseText;
}


function sendBinary(data, url){
	var xhr = new XMLHttpRequest();
	xhr.open("POST", url, true);
	 
	if (typeof XMLHttpRequest.prototype.sendAsBinary == "function") { // Firefox 3 & 4
		var tmp = '';
		for (var i = 0; i < data.length; i++) tmp += String.fromCharCode(data.charCodeAt(i) & 0xff);
		data = tmp;
	}
	else { // Chrome 9
	// http://javascript0.org/wiki/Portable_sendAsBinary
		XMLHttpRequest.prototype.sendAsBinary = function(text){
		var data = new ArrayBuffer(text.length);
		var ui8a = new Uint8Array(data, 0);
		for (var i = 0; i < text.length; i++) ui8a[i] = (text.charCodeAt(i) & 0xff);
		 
		var bb = new BlobBuilder(); // doesn't exist in Firefox 4
		bb.append(data);
		var blob = bb.getBlob();
		this.send(blob);
		}
	}
	 
	xhr.sendAsBinary(data);	
}

</script>
</head>
<body>
<h1>File API Demo</h1> 
 <p> 
 <!-- 用于文件上传的表单元素 --> 
 <form name="demoForm" id="demoForm" method="post" enctype="multipart/form-data" 
 action="javascript: uploadAndSubmit();"> 
 <p>Upload File: <input type="file" name="file" /></p> 
 <p><input type="submit" value="Submit" /></p> 
 </form> 
 <div>Progessing (in Bytes): <span id="bytesRead"> 
 </span> / <span id="bytesTotal"></span> 
 </div> 
 </p> 

</body>
</html>