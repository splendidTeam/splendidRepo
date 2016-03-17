$j.extend(loxia.regional['zh-CN'],{
    "NPS_FORM_CHECK_INFO":"操作提示",
    "SYS_SETOPTION":"查看",
    "GROUP_CODE":"分组编码",
    "GROUP_DESC":"分组描述",
    "GROUP_OPERATING":"操作"
});
var optionListUrl='/option/optionGroupList.json';
var setOptionUrl='/option/option-list.htm';
var findAllOptionListUrl = base + '/option/findAllOptionList.json';
var chooseOptionlist = new Array();

function edit(data, args, idx){ 
	var str="";
	var desc = loxia.getObject("groupDesc", data);
	if(desc == undefined || desc == null){
		desc = "";
	}
	str+="<a href='javascript:void(0);' class='func-button setoption' desc='"+ desc +"'  val='"+loxia.getObject("groupCode", data)+"'>"+nps.i18n("SYS_SETOPTION")+"</a>";
	return str; 
}

//刷新表格数据 
function refreshData(){
	$j("#table1").loxiasimpletable("refresh");
}

/**
 * oplist:chooseOptionList
 * @param data
 * @returns {Array}
 */
function chooseOptionList(groupCode, groupDesc){
//	var groupCode = loxia.getObject('groupCode', data);
//	var list = new Array();
	var size = 1;
//	list[0] = {label:loxia.getObject('groupDesc', data), type:"jsfunc", content:""};
	var html = '<div class="chooseOptionDesc"><div class="chooseOptionCurrent">'+groupDesc+'</div><ul>'
	for(var i=0; i<chooseOptionlist.length; i++){
		if(loxia.getObject('groupCode', chooseOptionlist[i]) == groupCode){
			var lifecycle = loxia.getObject('lifecycle', chooseOptionlist[i]);
			if(lifecycle == 1){
//				list[size] = {label:loxia.getObject('optionLabel', chooseOptionlist[i])+" | "+loxia.getObject('optionValue', chooseOptionlist[i])+" |", type:'jsfunc',content:'enable'};
				html += '<li class="enable">'+loxia.getObject('optionLabel', chooseOptionlist[i])+' | '+loxia.getObject('optionValue', chooseOptionlist[i])+' | </li>'
			}else{
//				list[size] = {label:loxia.getObject('optionLabel', chooseOptionlist[i])+" | "+loxia.getObject('optionValue', chooseOptionlist[i])+" |", type:'jsfunc',content:'disable'};
				html += '<li class="disable">'+loxia.getObject('optionLabel', chooseOptionlist[i])+' | '+loxia.getObject('optionValue', chooseOptionlist[i])+' | </li>';
			}
			size++;
		}
	}
	html += '</ul></div>';
	return html;
}

$j(document).ready(function(){
	loxia.init({debug: true, region: 'zh-CN'});
    nps.init();
    chooseOptionlist = loxia.syncXhr(findAllOptionListUrl, null, {type:'POST'});

	searchFilter.init({formId: 'searchForm', searchButtonClass: '.func-button.search'});
	$j("#table1").loxiasimpletable({
		page: false,
		size:10,
		nodatamessage:'<span>'+nps.i18n("NO_DATA")+'</span>',
		form:"searchForm",
		cols:
		[{
			name:"groupCode",
			label:nps.i18n("GROUP_CODE"),
			width:"33%",
			sort:["t.group_code asc","t.group_code desc"]},
		{
					
			name:"groupDesc",
			label:nps.i18n("GROUP_DESC"),
			width:"33%",
			sort:["t.group_desc asc","t.group_desc desc"]},
		{
			label:nps.i18n("GROUP_OPERATING"),
			width:"33%",
			template:"edit"}
		],
		dataurl : optionListUrl
	});
    refreshData();
    
  //筛选列表
    $j(".func-button.search").click(function(){
    
		 $j("#table1").data().uiLoxiasimpletable.options.currentPage=1;
		 refreshData();
	 });

	
    $j("#table1").on("click",".func-button.setoption", function(){
        window.location.href="choose-option.html";
    });
	$j(".button.addchoosegp").on("click", function(){
        window.location.href = base +"/option/createOptionGroup.htm";
    });
	
	//设置选项页面
	$j("#table1").on("click",".func-button.setoption",function(){
    	window.location.href=setOptionUrl+"?groupCode="+$j(this).attr("val") +"&groupDesc="+ $j(this).attr("desc");
    });
	
	$j('table tbody tr td[class^="col-1"] span').live('mouseover', function(e){
		var groupCode = $j(this).parent('td').prev('td').find('span').attr('title');
		var groupDesc = $j(this).attr('title');
		var html = chooseOptionList(groupCode, groupDesc);
		$j('body').append(html);
		var top = $j(this).offset().top;
		var right = $j(this).offset().left;
		$j('.chooseOptionDesc').css('top', top+'px').css('right', right+461+'px').slideDown();
	});
	
	$j('table tbody tr td[class^="col-1"] span').live('mouseout', function(e){
		$j('.chooseOptionDesc').remove();
		$j('.chooseOptionDesc').css('visibility', 'hidden').slideUp();
	});
	
});
