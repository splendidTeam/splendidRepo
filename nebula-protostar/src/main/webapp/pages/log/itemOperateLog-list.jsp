<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="item.update.manage"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/log/itemOperateLog-list.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png">商品上下架操作日志管理
    </div>
    <form id="searchForm">
    <div class="ui-block">
            <div class="ui-block-content ui-block-content-lb">
                <table>
                <tr>
                        <td><label><spring:message code="item.code"/></label></td>
                        <td><span id="searchkeytext"> <input type="text"
								loxiaType="input" mandatory="false"
								name="q_sl_code" placeholder="<spring:message code='item.code'/>"></input>
						    </span>
						</td>
                        <td><label><spring:message code="item.name"/></label></td>
                       <td><span id="searchkeytext"><input type="text"
								loxiaType="input" mandatory="false"
								name="q_sl_title" placeholder="<spring:message code='item.name'/>">
						    </span></input>
						</td>
                    </tr>
                    <tr>
                    	<td><label>操作类型</label></td>
                        <td> <span id="searchkeytext"> 
                        		<opt:select name="q_int_operateType" loxiaType="select" expression="chooseOption.LOG_OPERATE_TYPE"/>
							</span> 
						</td>
                        <td><label>操作时间</label></td>
                        <td>
                           <input type="text" name="q_date_operateBeginTime" loxiaType="date" mandatory="false"></input>
			            </td>
                        <td align="center"><label>——</label></td>
                         <td>
			            	<input type="text" name="q_date_operateEndTime" loxiaType="date" mandatory="false"></input>
			            </td>


                   		 <td><label>操作人</label></td>	
	                     <td><input 
								type="text" loxiaType="input" name="q_sl_operatorName"
								mandatory="false" placeholder="操作人"/>
						</td>
                    </tr>
                    
                    <tr>
                    
                    	<td><label>在架时长[单位：天]</label></td>
                         <td>
                            <input type="text" name="q_long_activeTime" loxiaType="input" mandatory="false" placeholder="在架时长"></input>
			            </td>
                    </tr>
                    
                </table>
                <div class="button-line1">
                <a href="javascript:void(0);"
                   class="func-button search"><span>搜索</span></a>
                </div>
            </div>
 
   	 		
    		</div>
    </form>
    <div class="ui-block">
		<div id="table1" class="border-grey"
				caption="<span style='font-size:14px;margin-left: 30px;'></span>"></div>
    </div>

</div>

    <div id="categoryMenuContent" class="menuContent" style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
		<ul id="categoryDemo" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
	</div>

    <div id="industryMenuContent" class="menuContent" style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
		<ul id="industryDemo" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
	</div>


</body>
</html>
