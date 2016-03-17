
var userRoleList={
	urList:new Array(),		//列表数据
	orgMap:new Object(),	//组织map id:name
	roleMap:new Object(),	//角色map id:name
	orgTypeMap:new Object(), //组织类型map id:name
	//添加元素(userRole id,机构类型id,角色id,组织id数组,机构类型名称,角色名称,组织名称数组)
	addElement:function(urId,orgTypeId,roleId,orgIds,orgType,role,orgs){
		this.urList[this.urList.length]={"urId":urId,"orgTypeId":orgTypeId,"roleId":roleId,"orgIds":orgIds,"orgType":orgType,"role":role,"orgs":orgs.join(",")}
	},
	//删除元素urId:是userRole表的id
	deleteElement:function(urId){
		for(var i=0;i<this.urList.length;i++){
			if(this.urList[i].urId==urId){
				this.urList.splice(i,1);
				return ;
			}
		}
	},
	//修改 返回true表示修改成功
	modifyElement:function(urId,orgTypeId,roleId,orgIds,orgs){
		
		for(var i=0;i<this.urList.length;i++){
			if(this.urList[i].urId==urId){
				this.urList[i]={"urId":urId,"orgTypeId":orgTypeId,"roleId":roleId,"orgIds":orgIds,"orgType":this.orgTypeMap[orgTypeId],"role":this.roleMap[roleId],"orgs":orgs.join(",")};
				return true;
			}
		}
		return false;
	},
	findAll:function(){
		return this.urList;
	},
	findByUrId:function(urId){
		for(var i=0;i<this.urList.length;i++){
			if(this.urList[i].urId==urId){
				
				return this.urList[i];
			}
		}
	},
	findByRoleId:function(roleId){
		for(var i=0;i<this.urList.length;i++){
			if(this.urList[i].roleId==roleId){
				
				return this.urList[i];
			}
		}
	},
	init:function(data){
		this.urList=data;
	}
		
}
