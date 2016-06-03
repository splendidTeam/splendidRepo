function getStoreList(pageNo,name) {
		$j("#skuTblFooterEx").remove();
		$j.ajax({
					type : "POST",
					url : base+"/offlineStore/storeList.json",
					data : {pageNo:pageNo,name:name},
					dataType : "json",
					success : function(pagination) {
						var html = "";
						var storeList = pagination.items;
						if (storeList != null && storeList != "") {
							for (var i=0;i<storeList.length;i++) {
								if (i%2==0){
									html+='<tr class="even">';
								} else {
									html+='<tr class="odd">';
								}
								
								html+='<td>'+storeList[i].id+'</td>';
								html+='<td>'+storeList[i].name+'</td>';
								html+='<td>'+storeList[i].fullAddress+'</td>';
								html+='<td>'+storeList[i].province+'</td>';
								html+='<td>'+storeList[i].city+'</td>';
								html+='<td>'+storeList[i].district+'</td>';
								html+='<td><a id="update_store'+storeList[i].id+'" onclick="toUpdateStore('+storeList[i].id+')" href="javascript:;" class="button">修改</a><a id="delete_store'+storeList[i].id+'" onclick="deleteStore('+storeList[i].id+')" href="javascript:;" class="button orange">删除</a></td>';
								html+='</tr>';
							}
						}
						$j("#stores_list").html(html);
						///////////////////////////////////
						$j("#store_storeList")
								.append(
										'<div id="footerTempEx" style="margin-top:5px;" class="nav-pager"> <div id="skuTblFooterEx" class="nav-pager" style="line-height:30px;">'
												+ ' &nbsp;共有&nbsp;'
												+ pagination.count
												+ '&nbsp;条记录&nbsp;&nbsp;&nbsp;&nbsp; 当前第 '
												+ pagination.currentPage
												+ '/'
												+ pagination.totalPages
												+ '&nbsp;页</div></div> ');
						if (pagination.firstPage) {
							$j("#skuTblFooterEx")
									.append(
											'<a href="javascript:;" class="prev disabled"><</a>');
						} else {
							$j("#skuTblFooterEx")
									.append(
											'<a href="javascript:;" class="prev" '
													+ 'onClick="getStoreList('
													+ (pagination.currentPage - 1)
													+ ',\''+$j.trim($j("#store_name").val())+'\');"><</a>');
						}

						if (pagination.lastPage) {
							$j("#skuTblFooterEx")
									.append(
											'<a href="javascript:;" class="next disabled">></a>');
						} else {
							$j("#skuTblFooterEx")
									.append(
											'<a href="javascript:;" class="next" '
													+ 'onClick="getStoreList('
													+ (pagination.currentPage + 1)
													+ ',\''+$j.trim($j("#store_name").val())+'\');">></a>');
						}

						$j("#skuTblFooterEx")
								.append(
										'<input type="text" id="pageNo" value=""/><a class="button" style="text-indent: 0px; font-size:12px; padding:0px 0px;" href="javascript:;" onBlur="this.blur();" onClick="getStoreList($j(this).prev().val(),\''+$j.trim($j("#store_name").val())+'\'); return false;">GO</a>');

						$j($j("div[name='divTip']")).each(function(i, o) {
							$j(o).attr("title", $j(o).html());
						});
					}
				});
	}
	
	// 去新增店铺
	function toAddStore() {
		window.location.href=base+"/offlineStore/toAddStore.htm";
	}
	
	// 去修改店铺
	function toUpdateStore(id) {
		window.location.href="/offlineStore/toUpdateStore.htm?id="+id;
	}
	
	// 删除店铺
	function deleteStore(id) {
		if(confirm("确定要删除店铺吗？")){
			$j.ajax({
				type : "POST",
				url : base+"/offlineStore/deleteStore.json",
				data : {id:id},
				dataType : "text",
				success : function(data) {
					if (data=="1") {
						alert("删除店铺成功");
						window.location.href=base+"/offlineStore/store.htm";
					}
				}
			});
		}
	}

	$j(document).ready(function() {
		/* $("a[id^='index_banner_update_order_']").live("click",function(){
			bannerUpdateOrder();
		}); */
		//$("#update_group").hide();
		//alert($("#editTable tr").eq(1).height());
		//alert($("#imageType_1").find("tr").size());
		getStoreList(1,"");
		$j("#search_store").click(function(){
			getStoreList(1,$j.trim($j("#store_name").val()));
		});
	});