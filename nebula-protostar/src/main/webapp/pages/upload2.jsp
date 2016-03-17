<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<script type="text/javascript" src="${base}/scripts/jquery/jquery-1.9.1.js"></script>

<script type="text/javascript" src="${base}/scripts/ajaxfileupload.js" ></script>  


<script type="text/javascript">

function mycallback(url){
	alert(url);
}

function mybeforeSend(){
	alert("ddd");
}

function mycomplete(){
	alert("complete");
}


</script>
</head>  
<body>  
 <form id="upform" action="/demo/uploadsave.json"  >
    <table cellpadding="0" cellspacing="0" class="tableForm">  
        <thead>  
            <tr>  
                <th>  
                    Ajax File Upload  
                </th>  
            </tr>  
        </thead>  
        <tbody>  
            <tr>  
                <td>  
                    <input  beforeSend="mybeforeSend"  class="imgUploadComponet" role="250X300" model="C" hName="file30"   type="file" url="/demo/upload.json"/>
                   
                </td>  
            </tr> 
            
            <tr>  
                <td>  
                    <input  beforeSend="mybeforeSend" complete="mycomplete"  class="imgUploadComponet" role="250X300" hName="file1" hValue="http://img.aaa.com" imgValue="http://img.aaa.com"  type="file"/>
                   
                </td>  
            </tr> 
            
            <tr>  
                <td>  
                    <input name="uname"/>
                   
                </td>  
            </tr>   
             
            <tr>  
                <td>  
                    Please select a file and click Upload button  
                </td>  
            </tr>  
        </tbody>  
        <tfoot>  
            <tr>  
                <td> 
                <input type="submit" value="提交"/> 
                   
                </td>  
            </tr>  
        </tfoot>  
    </table>  
 </form>
</body>  