<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/product/item/item-evaluate.js"></script>
<script type="text/javascript">
var baseImageUrl = "${baseImageUrl}";
</script>
</head>
<body>
 <div class="content-box width-percent100">
        <div class="ui-block">
        	<div class="ui-title1 title_unchecked"><img src="../images/wmi/blacks/32x32/tag.png">评价管理
                            	        <input type="button" value="批量删除" title="批量删除"  class="button  batchReject" />
						                <input type="button" value="批量通过" title="批量通过"  class="button orange batchPass" />
			</div>
			
			<div class="ui-title1 title_checked" style="display: none"><img src="../images/wmi/blacks/32x32/tag.png">评价列表
				                        <input type="button" value="批量删除" title="批量删除"  class="button  batchReject" />
				                        <input type="button" value="导出" title="导出"  class="button orange export" />
			</div>
            <div class="ui-block-content" style="padding-top:0">
              
                    <div class="ui-tag-change">
                        <ul class="tag-change-ul">
                            <li class="memberbase" onclick="doUnchecked()">待审核评价列表</li>
                            <li class="memberbase" onclick="doChecked()">已审核评价列表</li>
                        </ul>
                        <div class="tag-change-content">
                        
                            <div class="tag-change-in">
                            
                            <!-- 搜索开始 -->
                                <div class="ui-block-content ui-block-content-lb">
                               
                                  <form id="evaToDoSearchForm">
                                   <input type="hidden"  name="q_long_lifecycle" value="5">
						                <table>
						                <tr>
						                        <td><label>商品编码</label></td>
						                        <td><input type="text" loxiaType="input"  id="code" name="q_sll_code" placeholder="商品编码">
						                       </td>
						                        <td><label>商品名称</label></td>
						                        <td colspan="3"><input type="text" loxiaType="input"  id="title" name="q_sl_title" placeholder="商品名称"></td>
						                        <td></td>
				                        </tr>
				                        
				                         <tr>
				                                <td><label>评分</label></td>
						                        <td><select loxiatype="select" id="score" name="q_int_score">
						                                <option value="">请选择</option>
						                                <option value="1">1分</option>
						                                <option value="2">2分</option>
						                                <option value="3">3分</option>
						                                <option value="4">4分</option>
						                                <option value="5">5分</option>
						                        </select></td>
						                        <td><input type="checkbox" id="scoreRange" /><label>范围</label></td>
						                        <td  id="scoreTd" style="display: none">
							                        <label>最低</label>
	                                                <select loxiatype="select" id="minScore" name="q_int_minScore"  disabled>
							                                <option value="1">1分</option>
							                                <option value="2">2分</option>
							                                <option value="3">3分</option>
							                                <option value="4">4分</option>
							                                <option value="5">5分</option>
							                        </select>
							                        
							                        <label>最高</label>
							                        <select loxiatype="select" id="maxScore" name="q_int_maxScore"   disabled>
							                                <option value="5">5分</option>
							                                <option value="4">4分</option>
							                                <option value="3">3分</option>
							                                <option value="2">2分</option>
							                                <option value="1">1分</option>
							                        </select>
						                        </td>
						                    </tr>
				                       
						                </table>
						                <div class="button-line1">
						                <a href="javascript:void(0);"
						                   class="func-button evaToDoSearch"><span>查询</span></a>
						                </div>
						                </form>
						            </div>
						            <br>
						            <!-- 搜索结束 -->
                                <div class="border-grey" id="table1" caption="待审核评价列表"></div>
                            </div>
                            <div class="tag-change-in">
                            <!-- 搜索开始 -->
                                <div class="ui-block-content ui-block-content-lb">
                                  <form id="evaDoneSearchForm">
                                  <input type="hidden" name="q_long_lifecycle" id="lifecycle_pass" value="1">
						                <table>
						                   <tr>
						                        <td><label>商品编码</label></td>
						                        <td><input type="text" loxiaType="input"  id="code" name="q_sll_code" placeholder="商品编码"></td>
						                        <td><label>商品名称</label></td>
						                        <td><input type="text" loxiaType="input"  id="title" name="q_sl_title" placeholder="商品名称"></td>
						                    </tr>
						                     
						                      <tr>
				                                <td><label>评分</label></td>
						                        <td><select loxiatype="select" id="scoreOfEvaluateDone" name="q_int_score">
						                                <option value="">请选择</option>
						                                <option value="1">1分</option>
						                                <option value="2">2分</option>
						                                <option value="3">3分</option>
						                                <option value="4">4分</option>
						                                <option value="5">5分</option>
						                        </select></td>
						                        <td><input type="checkbox" id="scoreRangeOfEvaluateDone" /><label>范围</label></td>
						                        <td  id="scoreTdOfEvaluateDone" style="display: none" colspan="4">
							                        <label>最低</label>
	                                                <select loxiatype="select" id="minScoreOfEvaluateDone" name="q_int_minScore" disabled>
							                                <option value="1">1分</option>
							                                <option value="2">2分</option>
							                                <option value="3">3分</option>
							                                <option value="4">4分</option>
							                                <option value="5">5分</option>
							                        </select>
							                        
							                        <label>最高</label>
							                        <select loxiatype="select" id="maxScoreOfEvaluateDone" name="q_int_maxScore" disabled>
							                                <option value="5">5分</option>
							                                <option value="4">4分</option>
							                                <option value="3">3分</option>
							                                <option value="2">2分</option>
							                                <option value="1">1分</option>
							                        </select>
						                        </td>
						                    </tr>
						                    
						                    <tr>    
						                        <td><label>会员账号</label></td>
						                        <td><input loxiatype="input" placeholder="会员账号" name="q_string_memberLoginName"/></td>
						                        <td><label>类型</label></td>
						                        <td><input loxiatype="input" readonly placeholder="类型"/></td>
						                         <td><label>昵称</label></td>
						                        <td><input loxiatype="input" readonly placeholder="昵称"/></td>
						                        <td><a href="javascript:void(0);"
						                   class="func-button searchMember"><span>查找会员</span></a></td>
						                    </tr>
						                    
						                    <tr>
						                        <td><label>评价时间</label></td>
						                        <td> <input loxiaType="date"  mandatory="false" id="createDateStart" name="q_date_createDateStart"></input>
						                        </td>
						                        <td align="center"><label>——</label></td>
						                         <td><input loxiaType="date"  mandatory="false" id="createDateEnd" name="q_date_createDateEnd" ></input>
						                        </td>
						
						
						                        <td><label>审核时间</label></td>
						                        <td> <input loxiaType="date"  mandatory="false" id="passDateStart"  name="q_date_passDateStart"></input>
						                        </td>
						                        <td align="center"><label>——</label></td>
						                        <td> <input loxiaType="date" mandatory="false"  id="passDateEnd"  name="q_date_passDateEnd" ></input>
						                        </td>
						                        <td colspan="2"><a href="javascript:void(0);"
						                   class="func-button advancedOptionsButton"><span onclick="slideAdvancedOptions()">高级选项</span></a></td>
						                    </tr>
						                     <tr id="advancedOptions" style="display:none">
						                        <td><label>状态</label></td>
						                        <td><select loxiatype="select" id="lifecycle" name="q_long_lifecycle" disabled>
						                                <option value="1">通过</option>
						                                <option value="0">删除</option>
						                        </select></td>
						                        <td><label>审核人</label></td>
						                        <td><input loxiatype="input"  id="operatorRealName" name="q_sl_operatorRealName" placeholder="审核人" disabled/></td>
						                       
						                        <td><label>是否回复</label></td>
						                        <td><select loxiatype="select" id="isReply"name="q_int_isReply" disabled>
						                                <option value="1">是</option>
						                                <option value="2">否</option>
						                        </select></td>
						                        <td></td>
						                        <td></td>
						                    </tr>
						                    
						                </table>
						                <div class="button-line1">
						                <a href="javascript:void(0);"
						                   class="func-button evaDoneSearch"><span>查询</span></a>
						                <a href="javascript:void(0);"
						                   class="func-button advancedOptionsButtonHide" style="display: none"><span onclick="slideAdvancedOptionsHide()">隐藏高级选项</span></a>
						                </div>
						                </form>
						            </div>
						            <br>
                            
                            <!-- 搜索结束 -->
                                <div class="border-grey" id="table2" caption="已审核评价列表" ></div>
                               
                            </div>
                        </div>
                    </div>
        
            </div>
              <div class="ui-title1 title_unchecked" >
				                        <input type="button" value="批量删除" title="批量删除"  class="button  batchReject" />
				                        <input type="button" value="批量通过" title="批量通过"  class="button orange batchPass" />
			  </div>
				  
               <div class="ui-title1 title_checked" style="display: none">
				                        <input type="button" value="批量删除" title="批量删除"  class="button  batchReject" />
				                        <input type="button" value="导出" title="导出"  class="button orange export" />
			  </div>
             
        </div>
    </div>
    <div id="categoryContent" class="menuContent" style="z-index:999;display:none; position: absolute; background-color:#f0f6e4;border: 1px solid #617775;padding:3px;">
	   <ul id="categoryDemo" class="ztree" style="margin-top:0; width:180px; height: 100%;"></ul>
    </div>
    
    <!-- 评价详细  dialog-->
    <div id="detail-dialog" class="proto-dialog">
		 <h5>评价详细</h5>
	     <div class="proto-dialog-content details_content">	
	              
		 </div>
		 <div class="proto-dialog-button-line">
		      <input type="button" value="回复" class="button orange reply"/>
		 	  <input type="button" value="回复并通过" class="button orange replyAndPass"/>
		 	  <input type="button" value="删除" class="button delete"/>
		 </div>
	</div>

</body>
</html>
