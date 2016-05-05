<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
.color-item-list{ height: 60px; min-height: 40px; overflow: hidden;clear: both;margin: 0px 0px 5px;background: #fff;position: absolute;}
.color-item-list-hover{height: auto;background: #fff;z-index: 99;-webkit-box-shadow: 0px 3px 3px rgba(0,0,0,0.4);-moz-box-shadow: 0px 3px 3px rgba(0,0,0,0.4);box-shadow: 0px 3px 3px rgba(0, 0, 0, 0.4);}
.more-color{display: none;}
.color-number .more-color{display: block;position: absolute;z-index: 99;width: 100%;height: 26px;background: #fff;padding: 3px 0px 0px 0px;border: 1px solid #ddd;}
.color-number-hover .more-color{display: none;}
.tag-change-content { padding: 10px 10px 190px 10px;}
.check-txt {
  font-size: 12px;
  line-height: 14px;
  cursor: pointer;
  /* float: left; */
}
.check-input {
  vertical-align: middle;
  display: inline-flex;
  width: 12px;
  height: 13px;
  margin-right: 7px;
  background: url(../images/check.png) 0 0 no-repeat;
}
.check-current {
  background-position: 0 -17px;
}
</style>

<div class="border-grey ui-loxia-simple-table" style="display: none;" id="sortedTable" caption="<spring:message code='itemcategorysort.sorted.list'/>"></div>
	<div style=" float: right;  clear: both;">
		<input type="hidden" id="isSuccess" name="isSuccess" value="${isSuccess }"/>
 		<a id="updateItemSort" href="javascript:void(0);" class="button orange saveItemSort" onclick="saveItemSort()">生效此排序</a>
 		<input type="button" value="移除排序" title="移除排序" class="button orange delsort" onclick="delsort()" />
 	</div>
	<ul class="gbin1-list" id="sortTable">
	<c:forEach items="${resultPage.items }" var="item" varStatus="index">
		<li>
			<c:if test="${fn:length(item.imageUrl)>0 }">
					<c:forEach items="${item.imageUrl}" var="imageUrl"  begin="0" end="0">
						<c:if test="${! empty imageUrl}">
							<span> 
								<img id="${item.id }" src="${UPLOAD_IMG_DOMAIN}${imageUrl}" width="150px" height="200px"/>
		                	</span>
	                	</c:if>
	                </c:forEach>
	        </c:if>
			<%-- 
			<c:choose>
				<c:when test="${fn:length(itemProties.colorCommandUAs.mainColorImageList)>=1 }">
					<c:forEach items="${itemProties.colorCommandUAs.mainColorImageList }" var="mainColorList">
						<c:if test="${! empty mainColorList.itemColor }">
							<span> 
								<img id="${itemProties.itemId }" src="${UPLOAD_IMG_DOMAIN }<url:img size='150X200' imgUrl='${mainColorList.picUrl}'/>" />
		                	</span>
	                	</c:if>
	                </c:forEach>
				</c:when>
				<c:otherwise>
					<c:forEach items="${itemProties.colorCommandUAs.colorImageList }" var="colorList" varStatus="coloIndex">
						<c:if test="${! empty colorList.itemColor && coloIndex.index == '0'}">
							<span> 
								<img id="${colorList.itemId }" src="${UPLOAD_IMG_DOMAIN }<url:img size='150X200' imgUrl='${colorList.picUrl}'/>" />
								<c:if test="${itemProties.isNewItem eq 'true'}">
			               			<img class="badge-2" src="/images/badge-2.png">
			               		</c:if>
		                	</span>
	                	</c:if>
	                </c:forEach>
				</c:otherwise>
			</c:choose>
			 
			
			
			
               <ul class="color-item-list" style="  padding: 0px 0px 0px 4px;">
               	<c:choose>
					<c:when test="${fn:length(itemProties.colorCommandUAs.mainColorImageList)>=1 }">
		                <c:forEach items="${itemProties.colorCommandUAs.mainColorImageList }" var="mainColorList">
							<c:if test="${! empty mainColorList.itemColor }">
								<input type="hidden" id="newMainColor" name="newMainColor" value="${mainColorList.itemId }">
								<li style="float: left;  margin: 0px 2px;  height: 60px;">
									<img src="${UPLOAD_IMG_DOMAIN }<url:img size='150X200' imgUrl='${mainColorList.picUrl}'/>" style="display: none;" />
				                	<div id="${mainColorList.itemId }" class="color-item" onclick="changeColor(this);" style="cursor: pointer;width: 36px;  height: 26px;  border: 1px solid #DDD;  padding: 2px;">
				                		<a style="position: relative; background-color: ${fn:split(fn:split(mainColorList.itemColor, '|')[0], '/')[0] }; border: 1px solid #DDD; display: inline-block;width: 30px;height: 20px;">
				                			<c:choose>
												<c:when test="${fn:split(fn:split(mainColorList.itemColor, '|')[0], '/')[0] eq '#FFFFFF' }">
													<div class="ico_position ico_selected-grey ico_show"></div>											
												</c:when>
												<c:otherwise>
													<div class="ico_position ico_show"></div>
												</c:otherwise>
											</c:choose>
				                		</a>
				                	</div>
				                	<div>${mainColorList.availableQty }</div>
				                	<div>${mainColorList.properties }</div>
			                	</li>
		                	</c:if>
		                </c:forEach>
		                <c:forEach items="${itemProties.colorCommandUAs.colorImageList }" var="colorList">
							<c:if test="${! empty colorList.itemColor }">
								<li style="float: left;  margin: 0px 2px;  height: 60px;">
			                		<img src="${UPLOAD_IMG_DOMAIN }<url:img size='150X200' imgUrl='${colorList.picUrl}'/>" style="display: none;" />
				                	<div class="more-color">...</div>
				                	<div id="${colorList.itemId }" class="color-item" onclick="changeColor(this);" style="cursor: pointer;width: 36px;  height: 26px;  border: 1px solid #DDD;  padding: 2px;">
				                		<a style="position: relative;background-color: ${fn:split(fn:split(colorList.itemColor, '|')[0], '/')[0] }; border: 1px solid #DDD; display: inline-block;width: 30px;height: 20px;" >
				                			<div class="ico_position"></div>
				                		</a>
				                	</div>
				                	<div>${colorList.availableQty }</div>
				                	<div>${colorList.properties }</div>
			                	</li>
		                	</c:if>
		                </c:forEach>
		            </c:when>
		            <c:otherwise>
		            	<c:forEach items="${itemProties.colorCommandUAs.colorImageList }" var="colorList" varStatus="coloIndex">
							<c:if test="${! empty colorList.itemColor && coloIndex.index == '0'}">
								<input type="hidden" id="newMainColor" name="newMainColor" value="${colorList.itemId }">
								<li style="float: left;  margin: 0px 2px;  height: 60px;">
			                		<img src="${UPLOAD_IMG_DOMAIN }<url:img size='150X200' imgUrl='${colorList.picUrl}'/>" style="display: none;" />
				                	<div id="${colorList.itemId }" class="color-item" onclick="changeColor(this);" style="cursor: pointer;width: 36px;  height: 26px;  border: 1px solid #DDD;  padding: 2px;">
				                		<a style="position: relative;background-color: ${fn:split(fn:split(colorList.itemColor, '|')[0], '/')[0] }; border: 1px solid #DDD; display: inline-block;width: 30px;height: 20px;" >
				                			<c:choose>
												<c:when test="${fn:split(fn:split(mainColorList.itemColor, '|')[0], '/')[0] eq '#FFFFFF' }">
													<div class="ico_position ico_selected-grey ico_show"></div>											
												</c:when>
												<c:otherwise>
													<div class="ico_position ico_show"></div>
												</c:otherwise>
											</c:choose>
				                		</a>
				                	</div>
				                	<div>${colorList.availableQty }</div>
				                	<div>${colorList.properties }</div>
			                	</li>
		                	</c:if>
		                </c:forEach>
		                <c:forEach items="${itemProties.colorCommandUAs.colorImageList }" var="colorList" varStatus="coloIndex">
							<c:if test="${! empty colorList.itemColor && coloIndex.index > '0'}">
								<li style="float: left;  margin: 0px 2px;  height: 60px;">
			                		<img src="${UPLOAD_IMG_DOMAIN }<url:img size='150X200' imgUrl='${colorList.picUrl}'/>" style="display: none;" />
				                	<div class="more-color">...</div>
				                	<div id="${colorList.itemId }" class="color-item" onclick="changeColor(this);" style="cursor: pointer;width: 36px;  height: 26px;  border: 1px solid #DDD;  padding: 2px;">
				                		<a style="position: relative;background-color: ${fn:split(fn:split(colorList.itemColor, '|')[0], '/')[0] }; border: 1px solid #DDD; display: inline-block;width: 30px;height: 20px;" >
				                			<div class="ico_position"></div>
				                		</a>
				                	</div>
				                	<div>${colorList.availableQty }</div>
				                	<div>${colorList.properties }</div>
			                	</li>
		                	</c:if>
		                </c:forEach>
		            </c:otherwise>
	            </c:choose>
               </ul>
               
               --%>
               <div style="min-height: 18px; overflow: hidden; margin: 60px 0px 0px; width: 100%;cursor: pointer;" onclick="changeClass1(this);">${itemProties.title }</div>
               <div style="width:248px;cursor: pointer;"onclick="changeClass2(this);">
	               <label class="check-txt" id="${item.code}" >
	               	   <i class="check-input" ></i>
					   <span>${item.code }</span>
			           <span>(${index.count})</span>
					</label>
			   </div>
		</li>
 		
	</c:forEach>
	</ul>
